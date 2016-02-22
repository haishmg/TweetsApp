package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        VideoView v = (VideoView) findViewById(R.id.videoView);

        String url = null;
        if (getIntent().getExtras() != null) {
            url = getIntent().getExtras().getString("url");

            if (url != null) {
                v.setMediaController(new MediaController(this));
                //   v.setOnCompletionListener(this);
                v.setVideoURI(Uri.parse(url));
                v.start();
            }
        }

        if (url == null) {
            throw new IllegalArgumentException("Must set url extra paremeter in intent.");
        }
    }



    public void onCompletion(MediaPlayer v) {
        finish();
    }

    //Convenience method to show a video
    public static void showRemoteVideo(Context ctx, String url) {
        Intent i = new Intent(ctx, VideoPlayerActivity.class);

        i.putExtra("url", url);
        ctx.startActivity(i);
    }
}