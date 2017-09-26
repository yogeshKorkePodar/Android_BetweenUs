package com.podarbetweenus.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.podarbetweenus.Entity.AnnouncementResult;
import com.podarbetweenus.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gayatri on 1/28/2016.
 */
public class TeacherAnnouncementAdapter extends BaseAdapter{

    LayoutInflater layoutInflater;
    Context context;
    String date;
    private ArrayList<AnnouncementResult> announcement_list = new ArrayList<AnnouncementResult>();

    public TeacherAnnouncementAdapter(Context context,
                               ArrayList<AnnouncementResult> announcement_list) {
        this.context = context;
        this.announcement_list = announcement_list;
        this.layoutInflater = layoutInflater.from(this.context);
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {

        return announcement_list.size();
    }

    @Override
    public Object getItem(int position) {
        return announcement_list.get(position);
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
            showLeaveStatus = layoutInflater.inflate(R.layout.teacher_announcment_item, null);
            holder.tv_teacher_announcment_date = (TextView) showLeaveStatus.findViewById(R.id.tv_teacher_announcment_date);
            holder.tv_announcement_text = (TextView) showLeaveStatus.findViewById(R.id.tv_announcement_text);

        } else
        {
            holder = (ViewHolder) showLeaveStatus.getTag();
        }
        showLeaveStatus.setTag(holder);
        date = announcement_list.get(position).msg_date;
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

        holder.tv_teacher_announcment_date.setText(formatted_date);
        String msg = announcement_list.get(position).msg_Message.trim();
        String message = msg.replaceAll("\r\n","");
        Log.e("Message",msg);
        Log.e("New msg",message);
        holder.tv_announcement_text.setText(message);
        return showLeaveStatus;
    }

    public static class ViewHolder
    {
        TextView tv_teacher_announcment_date,tv_announcement_text;
    }
}
