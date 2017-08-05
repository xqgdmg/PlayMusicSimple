package com.sheepm.service;

import java.util.List;

import com.sheepm.Utils.Constants;
import com.sheepm.application.MyApplication;
import com.sheepm.bean.MusicInfo;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service implements OnPreparedListener,OnCompletionListener {

	public static MediaPlayer mediaPlayer;
	private MyBroadcastReceiver receiver;
	private List<MusicInfo> musicInfos;
	private int position;
	private boolean isFirst = true;
	public static int current;
	public static boolean isPlaying;

	@Override
	public void onCreate() {
		super.onCreate();
		regBroadcastReceiver();
	}
	
	private void regBroadcastReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.ACTION_PAUSE);
		filter.addAction(Constants.ACTION_PLAY);
		filter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
		filter.setPriority(1000);
		receiver = new MyBroadcastReceiver();
		registerReceiver(receiver, filter);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mediaPlayer = new MediaPlayer(); // 创建 MediaPlayer
		musicInfos = intent.getParcelableArrayListExtra("musicInfos"); // 获取所有本地音乐
		Log.e("chris","chris==" + musicInfos.size());
		return super.onStartCommand(intent, flags, startId);
	}

	/*
	 * 处理逻辑在于广播
	 */
	public class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Constants.ACTION_PAUSE)) {
				isPlaying = false;
				if (mediaPlayer.isPlaying()) {
					pauseMusic();
				}
			} else if (intent.getAction().equals(Constants.ACTION_PLAY)) {
				isPlaying = true;
				if (!mediaPlayer.isPlaying()) {
					if (isFirst) {
						position = intent.getIntExtra("position", 0);
						prepareMusic(position);
						isFirst = false;
					} else {
						mediaPlayer.seekTo(current);
						mediaPlayer.start();
					}
				}
			}else if (intent.getAction().equals(
					AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
				isPlaying = false;
					if (intent.getIntExtra("state", 0) == 0 ) {
						Intent intent2 = new Intent();
						intent2.setAction(Constants.ACTION_PAUSE);
						sendBroadcast(intent2);
					}

			}
		}

	}

	private void prepareMusic(int position) {
		mediaPlayer.reset();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		String url = musicInfos.get(position).getUrl();
		try {
			mediaPlayer.setDataSource(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.prepareAsync();
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		if ((MyApplication.state % 3) == 2) {
			prepareMusic(position);
		}

	}

	@Override
	public void onPrepared(MediaPlayer arg0) {
		mediaPlayer.start();
	}

	public void pauseMusic() {
		current = mediaPlayer.getCurrentPosition();
		mediaPlayer.pause();
	}
}
