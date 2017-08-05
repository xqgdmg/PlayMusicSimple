package com.sheepm.broadcast;

import com.sheepm.Utils.Constants;
import com.sheepm.application.MyApplication;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/*
 * 来电的时候停止播放音乐
 */
public class PhoneBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
			if (MyApplication.isPlay) {
				Message msg = Message.obtain();
				msg.obj = context;
				handler.sendMessage(msg);
			}
		} else {
			if (MyApplication.isPlay) {
				Message msg = Message.obtain();
				msg.obj = context;
				handler.sendMessage(msg);
			}
			TelephonyManager tManager = (TelephonyManager) context
					.getSystemService(Service.TELEPHONY_SERVICE);
			tManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		}

	}

	PhoneStateListener listener = new PhoneStateListener() {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				break;

			case TelephonyManager.CALL_STATE_OFFHOOK:

				break;
			case TelephonyManager.CALL_STATE_RINGING:

				break;

			default:
				break;
			}
		}

	};

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Context context = (Context) msg.obj;
			Intent phonepause = new Intent();
			phonepause.setAction(Constants.ACTION_PAUSE);
			context.sendBroadcast(phonepause);
		}

	};

}
