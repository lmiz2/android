package com.example.songhyeonseok.drawing;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoViewACT extends AppCompatActivity {

    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view_act);
        videoView = (VideoView)findViewById(R.id.video_view);

        Intent intent = getIntent();

        MediaController controller = new MediaController(VideoViewACT.this);
        videoView.setMediaController(controller);

        videoView.requestFocus();

        videoView.setVideoPath(intent.getStringExtra("path"));

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Toast.makeText(VideoViewACT.this,"동영상을 재생합니다.",Toast.LENGTH_SHORT).show();
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.seekTo(0);
            }
        });

        videoView.seekTo(0);
        videoView.start();
    }
}
