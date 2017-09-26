package com.podarbetweenus.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.podarbetweenus.Entity.teacherStudBehaviourResult;
import com.podarbetweenus.R;

import java.util.ArrayList;

/**
 * Created by Gayatri on 2/2/2016.
 */
public class ViewBehaviourAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    Context context;
    String date;
    ArrayList<teacherStudBehaviourResult> TeacherStudBehaviourResultv = new ArrayList<teacherStudBehaviourResult>();

    public ViewBehaviourAdapter(Context context,ArrayList<teacherStudBehaviourResult> TeacherStudBehaviourResultv) {
        this.context = context;
        this.TeacherStudBehaviourResultv = TeacherStudBehaviourResultv;
        this.layoutInflater = layoutInflater.from(this.context);
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return TeacherStudBehaviourResultv.size();
    }

    @Override
    public Object getItem(int position) {
        return TeacherStudBehaviourResultv.get(position);
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
            showLeaveStatus = layoutInflater.inflate(R.layout.view_behaviour_list_item, null);
            holder.tv_behaviour_date = (TextView) showLeaveStatus.findViewById(R.id.tv_behaviour_date);
            holder.tv_behaviour_desc = (TextView) showLeaveStatus.findViewById(R.id.tv_behaviour_desc);
            holder.img_behaviour_response = (ImageView)showLeaveStatus.findViewById(R.id.img_behaviour_response);

        } else
        {
            holder = (ViewHolder) showLeaveStatus.getTag();
        }
        showLeaveStatus.setTag(holder);

        holder.tv_behaviour_date.setText(TeacherStudBehaviourResultv.get(position).sbh_date1);
        holder.tv_behaviour_desc.setText(TeacherStudBehaviourResultv.get(position).bhr_name);
        if(TeacherStudBehaviourResultv.get(position).bhr_type.equalsIgnoreCase("0"))
        {
            holder.img_behaviour_response.setBackgroundResource(R.drawable.thumbsup);
        }
        else{
            holder.img_behaviour_response.setBackgroundResource(R.drawable.thumbsdown);
        }


        return showLeaveStatus;
    }

    public static class ViewHolder
    {
        TextView tv_behaviour_date,tv_behaviour_desc;
        ImageView img_behaviour_response;
    }

}
