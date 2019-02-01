package com.eladapp.elachat.chat;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.VideoView;
import com.eladapp.elachat.R;

public class PlayvideoActivity extends AppCompatActivity {

    private VideoView mVideoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        String videosrc = this.getIntent().getStringExtra("videosrc");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        mVideoView = (VideoView) findViewById(R.id.video_view);
        mVideoView.setVideoURI(Uri.parse(videosrc));
        mVideoView.start();
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVideoView.start();
            }
        });
    }
}