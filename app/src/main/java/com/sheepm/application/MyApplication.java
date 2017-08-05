package com.sheepm.application;

import java.util.ArrayList;
import java.util.List;

import com.sheepm.Utils.Constants;
import com.sheepm.Utils.MediaUtil;
import com.sheepm.Utils.ServiceUtil;
import com.sheepm.bean.MusicInfo;
import com.sheepm.service.MusicService;

import android.app.Application;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;

public class MyApplication extends Application {
	
	private static List<MusicInfo> infos;
	
	public static boolean isPlay = false;
	
	public static int state =2;
	

	/*
	 * 获取音乐，开启服务
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		infos = MediaUtil.getMp3Infos(getApplicationContext());
		if (!ServiceUtil.isServiceRunning(getApplicationContext(), Constants.MUSIC_SERVICE)) {
			startService();
		}
		
	}

	/*
	 * 开启服务
	 */
	private void startService(){
		Intent service = new Intent();
		service.setClass(getApplicationContext(), MusicService.class);
		service.putParcelableArrayListExtra("musicInfos",(ArrayList<? extends Parcelable>) infos);
		getApplicationContext().startService(service);
	}
	
}
