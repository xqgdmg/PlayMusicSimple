package com.sheepm.copyxiami;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.sheepm.Utils.Constants;
import com.sheepm.Utils.MediaUtil;
import com.sheepm.application.MyApplication;
import com.sheepm.bean.MusicInfo;

import java.util.List;

public class MainActivity extends Activity implements OnClickListener {

    private LocalMusicBroadcastReceiver receiver;
    private NotificationManager manager;
    private TextView tvTitle;
    private ImageView btnPlayOrStop;
    private List<MusicInfo> musicInfos;
    private boolean isFirst = true;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        regBroadcastReceiver();
        setOnclickListener();
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        btnPlayOrStop = (ImageView) findViewById(R.id.btnPlayOrStop);
        musicInfos = MediaUtil.getMp3Infos(this);

    }

    private void setOnclickListener() {
        btnPlayOrStop.setOnClickListener(this);
    }


    private void regBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_PAUSE);
        filter.addAction(Constants.ACTION_PLAY);
        filter.setPriority(700);
        receiver = new LocalMusicBroadcastReceiver();
        registerReceiver(receiver, filter);
    }

    /*
     * 广播接收者
     */
    public class LocalMusicBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.ACTION_PLAY)) {
                if (isFirst) {
                    isFirst = false;
                    MyApplication.isPlay = true;
                    Message message = Message.obtain();
                    message.obj = musicInfos.get(intent
                            .getIntExtra("position", 0));
                    handler.sendMessage(message);

                } else {
                    MyApplication.isPlay = true;
                    Message message = Message.obtain();
                    message.obj = MyApplication.isPlay;
                    handler2.sendMessage(message);
                }

            } else if (intent.getAction().equals(Constants.ACTION_PAUSE)) {
                MyApplication.isPlay = false;
                isFirst = false;


                Message message = Message.obtain();
                message.obj = musicInfos.get(position);
                handler.sendMessage(message);

                Message message2 = Message.obtain();
                message2.obj = MyApplication.isPlay;
                handler2.sendMessage(message2);

            }
        }

    }

    public Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {

            MusicInfo info = (MusicInfo) msg.obj;
            tvTitle.setText(info.getTitle());

            btnPlayOrStop.setImageResource(R.drawable.player_btn_radio_pause_normal);

        }

        ;
    };

    private Handler handler2 = new Handler() {

        public void handleMessage(Message msg) {
            boolean is = (Boolean) msg.obj;
            if (is) {
                btnPlayOrStop.setImageResource(R.drawable.player_btn_radio_pause_normal);
            } else {
                btnPlayOrStop.setImageResource(R.drawable.player_btn_radio_play_normal);
            }
        }

        ;

    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnPlayOrStop: // 方法按钮，暂停和播放都是发送广播
                if (btnPlayOrStop.getDrawable()
                        .getConstantState()
                        .equals(getResources().getDrawable(
                                R.drawable.player_btn_radio_pause_normal)
                                .getConstantState())) {

                    Intent broadcast = new Intent();
                    broadcast.setAction(Constants.ACTION_PAUSE);
                    sendBroadcast(broadcast);

                    btnPlayOrStop.setImageResource(R.drawable.player_btn_radio_play_normal);
                } else if (btnPlayOrStop
                        .getDrawable()
                        .getConstantState()
                        .equals(getResources().getDrawable(
                                R.drawable.player_btn_radio_play_normal)
                                .getConstantState())) {

                    Intent broadcast = new Intent();
                    broadcast.setAction(Constants.ACTION_PLAY);
                    sendBroadcast(broadcast);

                    btnPlayOrStop.setImageResource(R.drawable.player_btn_radio_pause_normal);
                }
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

}
