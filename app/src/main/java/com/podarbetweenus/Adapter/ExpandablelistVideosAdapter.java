package com.podarbetweenus.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.podarbetweenus.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 10/9/2015.
 */
public class ExpandablelistVideosAdapter extends BaseExpandableListAdapter {
    private Activity context;
    private Map<String, List<String>> videosCollections;
    private List<String> subject;
  //  final String namess[] ={"Jun 2015","Jul 2015","Aug 2015","Sept 2015"};


    public ExpandablelistVideosAdapter(Activity  context, List<String> subject, Map<String, List<String>> videos_collection) {
        this.videosCollections = videos_collection;
        this.subject =  subject;
        this.context = context;

    }

    @Override
    public int getGroupCount() {
        return subject.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return videosCollections.get(subject.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return subject.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return videosCollections.get(subject.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String subject = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_item_videos,
                    null);
        }
        TextView subject_name = (TextView) convertView.findViewById(R.id.subject);
        subject_name.setTypeface(null, Typeface.BOLD);
        subject_name.setText(subject);
        ImageView img_view = (ImageView) convertView.findViewById(R.id.img_view);


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String topic = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
        {
                convertView = inflater.inflate(R.layout.child_item_videos, null);
        }
        TextView topics = (TextView) convertView.findViewById(R.id.topics);
        topics.setText(topic);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
