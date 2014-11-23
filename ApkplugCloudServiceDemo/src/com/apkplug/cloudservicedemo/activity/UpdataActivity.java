package com.apkplug.cloudservicedemo.activity;
import java.util.List;
import org.apkplug.app.FrameworkInstance;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.apkplug.CloudService.*;
import com.apkplug.CloudService.bean.updateAppBean;
import com.apkplug.CloudService.bean.updateAppInfo;
import com.apkplug.CloudService.model.AppQueryModel;
import com.apkplug.CloudService.model.appModel;
import com.apkplug.CloudService.update.updateCallBack;
import com.apkplug.cloudservicedemo.ProxyApplication;
import com.apkplug.cloudservicedemo.R;
import com.apkplugdemo.adapter.BundleStutes;
import com.apkplugdemo.adapter.UpdataBundleAdapter;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 0.0.1 版本新增安装本地插件接口
 * MainActivity.install(String path,installCallback callback)
 * @author 梁前武 QQ 1587790525
 * www.apkplug.com
 */
public class UpdataActivity extends Activity {
	@Override
	protected void onRestart() {
		frame=((ProxyApplication)this.getApplication()).getFrame();
		super.onRestart();
	}


	private FrameworkInstance frame=null;
	private List<BundleStutes> bundles=null;
	private ListView bundlelist =null;
	private UpdataBundleAdapter adapter=null;
	private TextView info=null;
	public Handler mHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		frame=((ProxyApplication)this.getApplication()).getFrame();
		info =(TextView)this.findViewById(R.id.info);
		initBundleList();
	}

	/**
	 * 初始化显示已安装插件的UI
	 */
	public void initBundleList(){
		 //已安装插件列表
        bundlelist=(ListView)findViewById(R.id.bundlelist);
		bundles=new java.util.ArrayList<BundleStutes>();

		BundleContext context =frame.getSystemBundleContext();
		updateAppBean bean=new updateAppBean();
		List<updateAppInfo> l=new java.util.ArrayList<updateAppInfo>();
		for(int i=0;i<context.getBundles().length;i++)
		{		
				BundleStutes bs=new BundleStutes();
				bs.b=context.getBundles()[i];
				bs.updatastutes=0;
				bs.appid=context.getBundles()[i].getSymbolicName();
				bundles.add(bs);   
				//填充更新查询字段
				l.add(updateAppInfo.createUpdateAppInfo(context.getBundles()[i]));
		}
		adapter=new UpdataBundleAdapter(UpdataActivity.this,bundles);
		bundlelist.setAdapter(adapter);
		bean.setApps(l);
		//从云服务器查询所有已安装插件版本状态
		ApkplugCloudAgent.getInstance(null).getcheckupdate().checkupdate(bean,new impupdateCallBack());
	}
	/**
	 * 通过appid匹配已安装插件
	 * @param appid
	 * @return
	 */
	public BundleStutes getBundleByAppid(String appid){
		for(int i=0;i<bundles.size();i++){
			if(bundles.get(i).appid.equals(appid)){
				//查找到
				return bundles.get(i);
			}
		}
		return null;
	} 

	/**
	 * 插件版本更新OSGI服务回调函数
	 * @author 梁前武
	 *
	 */
	class impupdateCallBack implements updateCallBack{
		@Override
		public void onFailure(int arg0, final String arg1) {
			UpdataActivity.this.runOnUiThread(new Runnable(){
				@Override
				public void run() {
					info.setText("查询插件版本状态出现异常:\n\r"+arg1);
					Toast.makeText(UpdataActivity.this, "查询插件版本状态出现异常:\n\r"+arg1,
						     Toast.LENGTH_SHORT).show();
				}
			});
		}
		@Override
		public void onSuccess(int arg0, final AppQueryModel<appModel> arg1) {
						// arg1 有更新版本的插件appbean arg2 插件服务状态信息
						UpdataActivity.this.runOnUiThread(new Runnable(){
							@Override
							public void run() {
								Toast.makeText(UpdataActivity.this, "查询插件版本状态完成:\n\r有新版本插件数量为:"+arg1.getData().size(),
									     Toast.LENGTH_SHORT).show();
								info.setText("查询插件版本状态完成:\n\r有新版本插件数量为:"+arg1.getData().size());
							}
						});
						for(int i=0;i<arg1.getData().size();i++){
							appModel a=arg1.getData().get(i);
							BundleStutes bs=getBundleByAppid(a.getAppid());
							if(bs!=null){
								//匹配到插件,并更新其状态
								bs.updatastutes=1;
								bs.ab=a;
							}
						}
						//刷新列表
						UpdataActivity.this.runOnUiThread(new Runnable(){
							@Override
							public void run() {
								adapter.notifyDataSetChanged();
							}
						});
		}
		
		
	}
 	
}
