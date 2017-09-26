package com.podarbetweenus.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.podarbetweenus.Entity.ResourceList;
import com.podarbetweenus.Entity.TopicList;
import com.podarbetweenus.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 11/25/2015.
 */
public class ExpandableTopicsAdapter extends BaseAdapter {

    private Activity context;
    LayoutInflater layoutInflater;
    Context con;
    private List<String> groupTopicslist;
    private Map<String, List<String>> laptopCollections;
    private List<String> childResourceList;
    ArrayList<ResourceList> resource_list;
    ArrayList<TopicList> topic_list = new ArrayList<TopicList>();
    ListView lv_resourceList;
    private ArrayAdapter<String> listAdapter;
    ArrayList<String> resorce = new ArrayList<String>();
    ResourceAdapter resourceAdapter;


    public ExpandableTopicsAdapter(Activity context,ArrayList<TopicList> topic_list) {

        this.context = context;
        this.topic_list = topic_list;
        this.layoutInflater = layoutInflater.from(this.context);
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return topic_list.size();
    }

    @Override
    public Object getItem(int position) {
        return topic_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View showLeaveStatus = convertView;

        if (showLeaveStatus == null)
        {
            holder = new ViewHolder();
            showLeaveStatus = layoutInflater.inflate(R.layout.topic_list_item, null);
            holder.tv_topicname = (TextView) showLeaveStatus.findViewById(R.id.tv_topicname);


        } else
        {
            holder = (ViewHolder) showLeaveStatus.getTag();
        }
        showLeaveStatus.setTag(holder);

        holder.tv_topicname.setText(topic_list.get(position).crf_topicname);
        return showLeaveStatus;
    }
    @Override
     public boolean isEnabled(int position)
    {
        return true;
    }

    public static class ViewHolder
    {
        TextView tv_topicname;
    }
}