package com.podarbetweenus.Activities;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.MediaController;
import android.widget.VideoView;

import com.podarbetweenus.R;


/**
 * Created by Administrator on 10/9/2015.
 */
public class OpenVideosActivity extends Activity{

    VideoView video_player_view;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.open_videos);
        findViews();

     /*   video_player_view.setVideoPath(
                "http://www.ebookfrenzy.com/android_book/movie.mp4");*/
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(video_player_view);
        video_player_view.setMediaController(mediaController);
// Set video link (mp4 format )
       Uri video = Uri.parse("http://www.androidbegin.com/tutorial/AndroidCommercial.3gp");
        video_player_view.setVideoURI(video);
        video_player_view.start();

    }


    private void findViews() {
        video_player_view = (VideoView) findViewById(R.id.video_player_view);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
