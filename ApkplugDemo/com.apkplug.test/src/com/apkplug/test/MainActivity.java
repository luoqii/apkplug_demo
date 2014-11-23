package com.apkplug.test;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button start=(Button) this.findViewById(R.id.button1);
		start.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent i=new Intent();
				i.setClassName(MainActivity.this,"com.apkplug.test.bundle.MainActivity");
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				MainActivity.this.startActivity(i);
			}
			
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() != KeyEvent.ACTION_UP) {
			AlertDialog.Builder alertbBuilder = new AlertDialog.Builder(this);
			alertbBuilder
					.setTitle("真的要退出？")
					.setMessage("你确定要退出？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									int nPid = android.os.Process.myPid();
									android.os.Process.killProcess(nPid);
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							}).create();
			alertbBuilder.show();
			return true;
		} else {
			return super.dispatchKeyEvent(event);
		}
	} 
}
