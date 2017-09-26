package com.podarbetweenus.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.podarbetweenus.Entity.AttendHistory;
import com.podarbetweenus.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 11/7/2015.
 */
public class AbsentHistoryAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;

    private ArrayList<AttendHistory> AttemHistoryList = new ArrayList<AttendHistory>();
    public AbsentHistoryAdapter(Context context,
                          ArrayList<AttendHistory> attendHistoryArrayList) {
        this.context = context;
        this.AttemHistoryList = attendHistoryArrayList;
        this.layoutInflater = layoutInflater.from(this.context);
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return AttemHistoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return AttemHistoryList.get(position);
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
            showLeaveStatus = layoutInflater.inflate(R.layout.template_absent_history, null);
            holder.tv_absent_dateValue = (TextView) showLeaveStatus.findViewById(R.id.tv_absent_dateValue);
            holder.tv_absent_reasonValue = (TextView) showLeaveStatus.findViewById(R.id.tv_absent_reasonValue);


        } else
        {
            holder = (ViewHolder) showLeaveStatus.getTag();
        }
        showLeaveStatus.setTag(holder);
        String date = AttemHistoryList.get(position).atn_date;
        String[] date_array = date.split(" ");
        String newDate = date_array[0];
        String reason = AttemHistoryList.get(position).atn_Reason;


        String inputPattern = "MM/dd/yyyy";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date Formatdate = null;
        String formatted_date = null;

        try {
            Formatdate = inputFormat.parse(newDate);
            formatted_date = outputFormat.format(Formatdate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.tv_absent_dateValue.setText(formatted_date);
        holder.tv_absent_reasonValue.setText(reason);
           return showLeaveStatus;
    }
    public static class ViewHolder
    {
        TextView tv_absent_dateValue,tv_absent_reasonValue;

    }


}
