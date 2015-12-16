package com.xuyuan.autoanswer;



import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhonecallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive( Context context, Intent intent) {
		String action =intent.getAction();
		Log.d("phonecallReceiver",action);
		if (action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
			handleIncomingCall(context,intent);
		}
	}
	public void handleIncomingCall(Context context,Intent intent){
		SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(context);
		String msg=prefs.getString("MSG", "");
		boolean isImmed=prefs.getBoolean("mode",false);
		String phoneNumber=intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
		TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);  
		int state = telephony.getCallState();  
		switch(state){  
		case TelephonyManager.CALL_STATE_RINGING:  
//			����״̬
			if(isImmed){
				try {
			 //��ȡandroid.os.ServiceManager��Ķ����getService()���� 
					Method method=Class.forName("android.os.ServiceManager"). 
                    getMethod("getService",String.class); 
            // ��ȡԶ��TELEPHONY_SERVICE��IBinder����Ĵ��� 
					IBinder binder =(IBinder)method.invoke(null, new Object[] {Context.TELEPHONY_SERVICE});  
            // ��IBinder����Ĵ���ת��ΪITelephony���� 
					ITelephony telephony1=ITelephony.Stub.asInterface(binder); 
            //�Ҷϵ绰 
					telephony1.endCall(); 
				} catch (Exception e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					sendMsg(phoneNumber,msg);
					Log.d("123","call_state_ringing");
				}
			}
				break;  
		case TelephonyManager.CALL_STATE_IDLE:
			//�Ҷ�
			if (isImmed==false) {
				sendMsg(phoneNumber, msg);
				Log.d("123","call_state_idle");
			}
			break;  
		case TelephonyManager.CALL_STATE_OFFHOOK:  
			//�����绰
				Log.d("123", "call_state_offhook");
		 	break;  
				        }  
	}
	private void sendMsg(String phoneNumber,String msg){
		if(msg.length()>0){
			android.telephony.SmsManager smsManager=android.telephony.SmsManager.getDefault();
			smsManager.sendTextMessage(phoneNumber, null, msg, null, null);
			Log.d("123", msg);
			}
	}

}
