package com.xuyuan.autoanswer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends Activity implements android.view.View.OnClickListener{
	private Button bgButton;
	private Button exitButton;
	private Button saveButton;
	private CheckBox isAutoAnswer;
	private RadioGroup radioGroup;
	private RadioButton immedMode;
	private boolean flag;
	private String msg;
	private EditText inputText;
	private IntentFilter intentFilter;
	private PhonecallReceiver phonecallReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		inputText=(EditText)findViewById(R.id.inputText);
		isAutoAnswer=(CheckBox)findViewById(R.id.autoAnswer);
		saveButton=(Button)findViewById(R.id.save);
		bgButton=(Button)findViewById(R.id.bg_button);
		exitButton=(Button)findViewById(R.id.exit_button);
		radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
		immedMode=(RadioButton)findViewById(R.id.mode_immed);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId==immedMode.getId()) {
//				来电立即挂断模式
					flag=true;
					SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
					editor.putBoolean("mode", flag);
					editor.commit();
				}else{
					//响铃完毕回复模式
					flag=false;
					SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
					editor.putBoolean("mode", flag);
					editor.commit();
				}
			}
		});
		saveButton.setOnClickListener(this);
		bgButton.setOnClickListener(this);
		exitButton.setOnClickListener(this);
		isAutoAnswer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					//开启模式
					intentFilter=new IntentFilter();
					intentFilter.addAction("android.intent.action.PHONE_STATE");
					phonecallReceiver=new PhonecallReceiver();
					registerReceiver(phonecallReceiver, intentFilter);
				}else{
					//关闭模式
					unregisterReceiver(phonecallReceiver);
				}
			}
		});
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save:
			msg=inputText.getText().toString();
			if(msg.equals("")){
			Toast.makeText(this, "please enter your message", Toast.LENGTH_SHORT).show();
			}else{
			SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(this).edit();
			editor.putString("MSG", msg);
			editor.commit();
			Toast.makeText(this, "Set", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.bg_button:
			//后台运行
			Intent intent =new Intent(Intent.ACTION_MAIN);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addCategory(Intent.CATEGORY_HOME);
			this.startActivity(intent);
			break;
		case R.id.exit_button:
				finish();
			break;
		default:
			break;
		}
	}
}
