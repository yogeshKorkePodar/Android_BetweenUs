package com.podarbetweenus.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.podarbetweenus.Entity.AttendHistory;
import com.podarbetweenus.Entity.DateDropdownValueDetails;
import com.podarbetweenus.Entity.ViewStudAttendRes;
import com.podarbetweenus.R;
import android.view.ViewGroup.LayoutParams;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Gayatri on 2/3/2016.
 */
public class TeacherViewAttendanceAdapter extends BaseAdapter{

    LayoutInflater layoutInflater;
    Context context;
    String hideReason;

    private ArrayList<ViewStudAttendRes> ViewStudAttendRes = new ArrayList<ViewStudAttendRes>();

    public TeacherViewAttendanceAdapter(Context context, ArrayList<ViewStudAttendRes> ViewStudAttendRes,String hideReason) {
        this.context = context;
        Log.d("<<Inside","TeacherViewAttendanceAdapter");
        this.hideReason = hideReason;
        this.ViewStudAttendRes = ViewStudAttendRes;
        this.layoutInflater = layoutInflater.from(this.context);
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return ViewStudAttendRes.size();
    }

    @Override
    public Object getItem(int position) {
        return ViewStudAttendRes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View showLeaveStatus = convertView;
        LayoutParams layoutparams;

       /* if (showLeaveStatus == null)
        {*/
            holder = new ViewHolder();
            if(hideReason.equalsIgnoreCase("0")) {
                showLeaveStatus = layoutInflater.inflate(R.layout.template_absent_history, null);
                Log.d("<<Long reason","template_absent_history");
            }
            else if(hideReason.equalsIgnoreCase("1")){
                showLeaveStatus = layoutInflater.inflate(R.layout.template_present_history, null);
                Log.d("<<Long reason","template_present_history");
            }

            holder.linearLayout = (LinearLayout) showLeaveStatus.findViewById(R.id.main_row2) ;
            holder.tv_absent_dateValue = (TextView) showLeaveStatus.findViewById(R.id.tv_absent_dateValue);
            holder.tv_absent_reasonValue = (TextView) showLeaveStatus.findViewById(R.id.tv_absent_reasonValue);

       /* } else
        {
            holder = (ViewHolder) showLeaveStatus.getTag();
        }*/
        showLeaveStatus.setTag(holder);
        String date = ViewStudAttendRes.get(position).atn_date;
        String[] date_array = date.split(" ");
        String newDate = date_array[0];
        String reason = ViewStudAttendRes.get(position).atn_Reason;


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
        if(hideReason.equalsIgnoreCase("1")){

            holder.tv_absent_dateValue.setText(formatted_date);
        }else if(hideReason.equalsIgnoreCase("0")){

            Log.d("<<Long reason",reason);
            Log.d("<<length reason",String.valueOf(reason.length()));

        if (reason.length() > 30 && reason.length() < 50){

            Log.d("<<Long reason",reason);
            LayoutParams params = holder.linearLayout.getLayoutParams();
            // Changes the height and width to the specified *pixels*
            params.height = 70;
            holder.linearLayout.setLayoutParams(params);
        }
            if (reason.length() > 50 && reason.length() < 80){
                Log.d("<<Long reason","Detected");
                LayoutParams params = holder.linearLayout.getLayoutParams();
                // Changes the height and width to the specified *pixels*
                params.height = 100;
                holder.linearLayout.setLayoutParams(params);
            }
        else if (reason.length() > 80){
            Log.d("<<Long reason","Detected");
            LayoutParams params = holder.linearLayout.getLayoutParams();
            // Changes the height and width to the specified *pixels*
            params.height = 140;
            holder.linearLayout.setLayoutParams(params);
        }
        holder.tv_absent_dateValue.setText(formatted_date);
        holder.tv_absent_reasonValue.setText(reason);
        }
        return showLeaveStatus;
    }
    public static class ViewHolder
    {
        TextView tv_absent_dateValue,tv_absent_reasonValue;
        LinearLayout linearLayout;

    }
}
