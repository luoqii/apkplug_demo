package com.apkplug.bundle.manager;
import org.apkplug.Bundle.installCallback;
import org.apkplug.app.FrameworkFactory;
import org.apkplug.app.FrameworkInstance;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import android.app.Application;
import android.util.Log;

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
				//文档见 http://www.apkplug.com/javadoc/Maindoc1.4.6/
				//org.apkplug.app 
				//     接口 FrameworkInstance
				frame=FrameworkFactory.getInstance().start(null,this);
				//FrameworkFactory.getInstance().init(this);
				System.out.println("ProxyApplication1");
				BundleContext context =frame.getSystemBundleContext();
				InstallBundle ib=new InstallBundle(context);
				ib.install(context, "ActivityForResultDemo.apk", "1.0.0", null, 2, true);
				ib.install(context, "BundleDemoJni.apk", "1.0.0", null, 2, true);
				ib.install(context, "BundleDemoShow.apk", "1.0.0", null, 2, true);
				ib.install(context, "BundleDemoStartActivity1.apk", "1.0.0", null, 2, true);
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
