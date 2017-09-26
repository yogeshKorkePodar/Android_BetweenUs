package com.podarbetweenus.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.podarbetweenus.Entity.BehavioursResult;
import com.podarbetweenus.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 10/26/2015.
 */
public class BehaviourAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    Context context;
    String date;
    private ArrayList<BehavioursResult> behaviour_list = new ArrayList<BehavioursResult>();

    public BehaviourAdapter(Context context,
                               ArrayList<BehavioursResult> behaviour_list) {
        this.context = context;
        this.behaviour_list = behaviour_list;
        this.layoutInflater = layoutInflater.from(this.context);
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return behaviour_list.size();
    }

    @Override
    public Object getItem(int position) {
        return behaviour_list.get(position);
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
            showLeaveStatus = layoutInflater.inflate(R.layout.template_behaviour, null);
            holder.tv_date = (TextView) showLeaveStatus.findViewById(R.id.tv_date);
            holder.tv_msg = (TextView) showLeaveStatus.findViewById(R.id.tv_msg);
            holder.tv_name = (TextView) showLeaveStatus.findViewById(R.id.tv_name);

        } else
        {
            holder = (ViewHolder) showLeaveStatus.getTag();
        }
        showLeaveStatus.setTag(holder);

        date = behaviour_list.get(position).date1;
        String date_array[] = date.split(" ");
        String date_format = date_array[0];
        Log.e("DATE:- ", date_format);

        String inputPattern = "MM/dd/yyyy";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String formatted_date = null;

        try {
            date = inputFormat.parse(date_format);
            formatted_date = outputFormat.format(date);
            Log.e("New Date",formatted_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tv_date.setText(formatted_date);
        holder.tv_msg.setText(behaviour_list.get(position).msg_Message);
        holder.tv_name.setText(behaviour_list.get(position).sbj_name);
        return showLeaveStatus;
    }

    public static class ViewHolder
    {
        TextView tv_date,tv_msg,tv_name;
    }
}

