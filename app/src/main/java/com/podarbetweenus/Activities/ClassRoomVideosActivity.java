package com.podarbetweenus.Activities;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.podarbetweenus.Adapter.ExpandablelistVideosAdapter;
import com.podarbetweenus.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 10/9/2015.
 */
public class ClassRoomVideosActivity extends Activity implements View.OnClickListener{

    ExpandableListView expandableListView_class_room_videos;
    VideoView video_view;
    LinearLayout lay_back_investment;

    HeaderControler header;
    List<String> groupList_videos;
    List<String> childList_videos;
    Map<String, List<String>> videos_collection;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.class_room_videos);
        findViews();
        init();
        createGroupListVideos();
        createCollectionVidoes();
        final ExpandablelistVideosAdapter expListAdapter_videos = new ExpandablelistVideosAdapter(this,groupList_videos,videos_collection);
        expandableListView_class_room_videos.setAdapter(expListAdapter_videos);
        expandableListView_class_room_videos.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int prevPosition = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != prevPosition) {
                    expandableListView_class_room_videos.collapseGroup(prevPosition);
                    prevPosition = groupPosition;
                }
            }
        });
        expandableListView_class_room_videos.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Intent i = new Intent(ClassRoomVideosActivity.this,OpenVideosActivity.class);
                startActivity(i);
                return false;
            }
        });
    }

    private void createGroupListVideos() {
        groupList_videos = new ArrayList<String>();
        groupList_videos.add("Biology");
        groupList_videos.add("chemistry");
        groupList_videos.add("Physics");
        groupList_videos.add("Maths");
    }

    private void createCollectionVidoes(){
        String[] biology_topics = { "Control and Coordination","Life Process(PART1)","Life Process(PART2)","Sources of Energy"};
        String[] chemistey_topics = { "Acids Bases and salts","Carbon and its compound (PART1)","Carbon and its compound (PART2)" };
        String[] physics_topics = { "Electricity","Human eye","Gravity"};
        String[] maths_topics = { "Permutation","Derivation","Integration" };

        videos_collection = new LinkedHashMap<String, List<String>>();

        for (String subject : groupList_videos) {
            if (subject.equalsIgnoreCase("Biology")) {
                loadChild_videos(biology_topics);
            } else if (subject.equalsIgnoreCase("Chemistry"))
                loadChild_videos(chemistey_topics);
            else if (subject.equals("Physics"))
                loadChild_videos(physics_topics);
            else if (subject.equalsIgnoreCase("Maths"))
                loadChild_videos(maths_topics);
          /*  else
                loadChild(lenovoModels);*/

            videos_collection.put(subject, childList_videos);
        }
    }

    private void loadChild_videos(String[] subject_topics) {
        childList_videos = new ArrayList<String>();
        for (String model : subject_topics)
            childList_videos.add(model);
    }

    private void findViews() {
        expandableListView_class_room_videos = (ExpandableListView) findViewById(R.id.expandableListView_class_room_videos);
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
        video_view = (VideoView) findViewById(R.id.video_view);
        video_view.setVideoPath(
                "http://www.androidbegin.com/tutorial/AndroidCommercial.3gp");

        final MediaController mediaController = new
                MediaController(this);
        mediaController.setAnchorView(video_view);
        video_view.setMediaController(mediaController);

        video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener()  {
        @Override
        public void onPrepared(MediaPlayer mp)
        {
          Log.i("DURATION", "Duration = " + video_view.getDuration());
            mediaController.setAnchorView(video_view);
        }
        });

        video_view.start();

    }

    private void init()
    {
        header = new HeaderControler(this,true,false,"Class Room Videos");
        lay_back_investment.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
    if(v==lay_back_investment)
    {
       /* Intent back = new Intent(ClassRoomVideosActivity.this,ProfileActivity.class);
        startActivity(back);*/
        finish();
    }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Intent back = new Intent(ClassRoomVideosActivity.this,ProfileActivity.class);
        startActivity(back);*/
        finish();
    }
}
