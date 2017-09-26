package com.podarbetweenus.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.podarbetweenus.Entity.BehavioursResult;
import com.podarbetweenus.Entity.TopicList;
import com.podarbetweenus.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gayatri on 3/4/2016.
 */
public class TeacherTopicListAdapter extends BaseAdapter{
    LayoutInflater layoutInflater;
    Context context;
    String date;
    private ArrayList<TopicList> topicLists = new ArrayList<TopicList>();

    public TeacherTopicListAdapter(Context context,
                            ArrayList<TopicList> topicLists) {
        this.context = context;
        this.topicLists = topicLists;
        this.layoutInflater = layoutInflater.from(this.context);
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return topicLists.size();
    }

    @Override
    public Object getItem(int position) {
        return topicLists.get(position);
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
            showLeaveStatus = layoutInflater.inflate(R.layout.subject_topic_list_item, null);
            holder.tv_topicname = (TextView) showLeaveStatus.findViewById(R.id.tv_topicname);
            holder.tv_total_logs_value = (TextView) showLeaveStatus.findViewById(R.id.tv_total_logs_value);
            holder.tv_log_filled_value = (TextView) showLeaveStatus.findViewById(R.id.tv_log_filled_value);
            holder.ll_topic = (LinearLayout) showLeaveStatus.findViewById(R.id.ll_topic);
            holder.tv_srNo = (TextView) showLeaveStatus.findViewById(R.id.tv_srNo);
            holder.tv_view_log = (TextView) showLeaveStatus.findViewById(R.id.btn_view_log);
            holder.ll_subfields = (LinearLayout) showLeaveStatus.findViewById(R.id.ll_subfields);
            holder.img_add_button = (ImageView) showLeaveStatus.findViewById(R.id.img_add_button);
        } else
        {
            holder = (ViewHolder) showLeaveStatus.getTag();
        }
        showLeaveStatus.setTag(holder);
        holder.tv_topicname.setText(topicLists.get(position).crf_topicname);
        holder.tv_log_filled_value.setText("Total Logs - "+topicLists.get(position).No_of_logs_filled);
        holder.tv_total_logs_value.setText("Logs Filled - "+topicLists.get(position).total_logs);
      //  holder.tv_srNo.setText(topicLists.get(position).);
        final ViewHolder finalHolder = holder;

        finalHolder.img_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalHolder.ll_subfields.getVisibility()==View.GONE)
                {
                    finalHolder.img_add_button.setImageResource(R.drawable.minus);
                    finalHolder.ll_subfields.setVisibility(View.VISIBLE);
                }
                else {
                    finalHolder.img_add_button.setImageResource(R.drawable.add_plus_button);
                    finalHolder.ll_subfields.setVisibility(View.GONE);
                }
            }
        });
        return showLeaveStatus;
    }

    public static class ViewHolder
    {
        TextView tv_topicname,tv_total_logs_value,tv_log_filled_value,tv_srNo,tv_view_log;
        LinearLayout ll_subfields,ll_topic;
        ImageView img_add_button;
    }

}
