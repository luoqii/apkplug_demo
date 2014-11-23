package com.apkplug.test;
import org.apkplug.Bundle.installCallback;
import org.apkplug.app.FrameworkFactory;
import org.apkplug.app.FrameworkInstance;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ProxyApplication extends Application {

	private FrameworkInstance frame=null;
	
	public FrameworkInstance getFrame() {
		return frame;
	}

	public void onCreate() {   
		
		
		 super.onCreate(); 
		 try
	        {
	        	//启动框架
				frame=FrameworkFactory.getInstance().start(null,this);
				System.out.println("ProxyApplication1");
				BundleContext context =frame.getSystemBundleContext();
				InstallBundle ib=new InstallBundle(context);
				ib.install(context, "TestBundle.apk", "1.0.0", new installCallback(){
					@Override
					public void callback(int arg0, Bundle arg1) {
						if(arg0==installCallback.stutas5||arg0==installCallback.stutas7){
							Log.d("",String.format("插件安装 %s ： %d",arg1.getName(),arg0));
							
							return;
						}
						else{
							Log.d("","插件安装失败 ：%s"+arg1.getName());
						}
					}
					},
					2, false);
				
	        }
	        catch (Exception ex)
	        {
	            System.err.println("Could not create : " + ex);
	            ex.printStackTrace();
	            int nPid = android.os.Process.myPid();
				android.os.Process.killProcess(nPid);
	        }
	}
	
}
