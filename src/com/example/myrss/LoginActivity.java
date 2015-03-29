package com.example.myrss;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends Activity{

	private Button loginButton;
	
	private TextView loginText;
	
	private ImageView imagView;
	
	private int i_alpha = 255;
	private Handler mhandler = new Handler();
	boolean isShow = false;
	private Thread thread;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.login);
		loginButton = (Button)findViewById(R.id.login_in);
		loginText = (TextView)findViewById(R.id.logining);
		imagView = (ImageView)findViewById(R.id.login_rss);
		
		imagView.setAlpha(i_alpha);
		isShow = true;
		
		mhandler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				imagView.setAlpha(i_alpha);
			}
		};
		
		thread = new Thread(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(100);
					updateAlpha();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});
		
		loginButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent  = new Intent();
				intent.setClass(LoginActivity.this, SelectChannel.class);
				thread.start();
				
				loginButton.setVisibility(View.INVISIBLE);
				loginText.setVisibility(View.VISIBLE);
			}
		});
	}
	
	
	protected void updateAlpha() {
		if ((i_alpha - 25) >= 0) {
			i_alpha = i_alpha -25;
		} else {
			i_alpha = 0;
			isShow = false;
			
			startActivity(intent);
			LoginActivity.this.finish();
		}
		
		mhandler.sendMessage(mhandler.obtainMessage());
	}
	

	

}
