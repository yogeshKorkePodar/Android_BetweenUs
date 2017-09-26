package com.podarbetweenus.Adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.Entity.TeacherAttendaceResult;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Gayatri on 2/3/2016.
 */
public class ShowAttendanceAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    Context context;
    DataFetchService dft;
    LoginDetails login_details;
    ProgressDialog progressDialog;
    String clt_id,msd_id,board_name,school_name,usl_id,teacher_name,version_name,month_id,atn_valid,academic_yearId,hideReason;
    String ShowAttendanceReasonMethod_name = "ViewStudAttendDetails";
    public ArrayList<TeacherAttendaceResult> teacherAttendaceResult = new ArrayList<TeacherAttendaceResult>();

    //Attendence adapter Constructor

    public ShowAttendanceAdapter(Context context,
                                   ArrayList<TeacherAttendaceResult> teacherAttendaceResult,String academic_yearId,String clt_id){
        this.context = context;
        this.teacherAttendaceResult = teacherAttendaceResult;
        this.layoutInflater = layoutInflater.from(this.context);
        this.academic_yearId = academic_yearId;
        this.clt_id = clt_id;
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

         dft = new DataFetchService(context);
         login_details = new LoginDetails();
         progressDialog = Constant.getProgressDialog(context);
    }
    @Override
    public int getCount() {
        return teacherAttendaceResult.size();
    }

    @Override
    public Object getItem(int position) {
        return teacherAttendaceResult.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View showLeaveStatus = convertView;

        if (showLeaveStatus == null)
        {
            holder = new ViewHolder();
            showLeaveStatus = layoutInflater.inflate(R.layout.show_attendance_list_item, null);
            holder.tv_student_name = (TextView) showLeaveStatus.findViewById(R.id.tv_student_name);
            holder.tv_roll_no_value = (TextView) showLeaveStatus.findViewById(R.id.tv_roll_no_value);
            holder.tv_attendance_Presentvalue = (TextView) showLeaveStatus.findViewById(R.id.tv_attendance_Presentvalue);
            holder.tv_attendance_value = (TextView) showLeaveStatus.findViewById(R.id.tv_attendance_value);
            holder.ll_main = (LinearLayout) showLeaveStatus.findViewById(R.id.ll_main);
            holder.ll_roll_no = (LinearLayout) showLeaveStatus.findViewById(R.id.ll_roll_no);
            holder.ll_attendance = (LinearLayout) showLeaveStatus.findViewById(R.id.ll_attendance);
            holder.ll_attendance_present = (LinearLayout)showLeaveStatus.findViewById(R.id.ll_attendance_present);

        } else
        {
            holder = (ViewHolder) showLeaveStatus.getTag();
        }


        showLeaveStatus.setTag(holder);
        // If even position then set this color or else other color
        if(position % 2 == 0){
            holder.ll_main.setBackgroundColor(Color.parseColor("#617b81"));
            holder.ll_attendance.setBackgroundResource(R.color.attendance_listview_even_row);
            holder.ll_roll_no.setBackgroundResource(R.color.attendance_listview_even_row);
            holder.tv_student_name.setBackgroundResource(R.color.attendance_listview_even_row);
            holder.ll_attendance_present.setBackgroundResource(R.color.attendance_listview_even_row);
        }
        else{
            holder.ll_main.setBackgroundColor(Color.parseColor("#617b81"));
            holder.ll_attendance.setBackgroundResource(R.color.attendance_listview_odd_row);
            holder.ll_roll_no.setBackgroundResource(R.color.attendance_listview_odd_row);
            holder.tv_student_name.setBackgroundResource(R.color.attendance_listview_odd_row);
            holder.ll_attendance_present.setBackgroundResource(R.color.attendance_listview_odd_row);
        }
       // holder.tv_attendance_value.setPaintFlags(holder.tv_attendance_value.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.tv_roll_no_value.setText(teacherAttendaceResult.get(position).Roll_No);
        holder.tv_student_name.setText(teacherAttendaceResult.get(position).student_Name);
        if(teacherAttendaceResult.get(position).total.equalsIgnoreCase("0")){
            holder.tv_attendance_value.setText("");
        }
        else{
            holder.tv_attendance_value.setText(teacherAttendaceResult.get(position).total);
        }
        if(teacherAttendaceResult.get(position).totalp.equalsIgnoreCase("0")){
            holder.tv_attendance_Presentvalue.setText("");
        }
        else {
            holder.tv_attendance_Presentvalue.setText(teacherAttendaceResult.get(position).totalp);
        }
       // atn_valid = teacherAttendaceResult.get(position).atn_valid.toString();
        month_id = teacherAttendaceResult.get(position).month.toString();

        final ViewHolder finalHolder = holder;
        holder.ll_attendance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideReason = "0";
                if(!(teacherAttendaceResult.get(position).total.equalsIgnoreCase("")))
                {

                    msd_id = teacherAttendaceResult.get(position).msd_id;
                    Log.e("ATTENDACE MSD", msd_id);
                    atn_valid = "0";
                    //View Attendance with reason
                    callWebserviceToViewAttendance(msd_id, clt_id, month_id,atn_valid,academic_yearId);
                }
            }
        });
        holder.ll_attendance_present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideReason = "1";
                if(!(teacherAttendaceResult.get(position).totalp.equalsIgnoreCase("")))
                {
                    msd_id = teacherAttendaceResult.get(position).msd_id;
                    Log.e("ATTENDACE MSD", msd_id);
                    atn_valid = "1";
                    //View Attendance with reason
                    callWebserviceToViewAttendance(msd_id, clt_id, month_id,atn_valid,academic_yearId);
                }

            }
        });
        return showLeaveStatus;
    }
    private void callWebserviceToViewAttendance(String msd_id,String clt_id,String month_id,String atn_valid,String academic_yearId) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getTeacherAttendanceListReason(msd_id, clt_id, month_id,atn_valid,academic_yearId, ShowAttendanceReasonMethod_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);

                            if (login_details.Status.equalsIgnoreCase("1")) {

                                try {
                                    if (hideReason.equalsIgnoreCase("0")) {
                                        final Dialog alertDialog = new Dialog(context);
                                        //  final AlertDialog alertDialog = builder.create();
                                        //  LayoutInflater inflater = context.getLayoutInflater();
                                        LayoutInflater inflater = LayoutInflater.from(context);

                                        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        alertDialog.getWindow().setBackgroundDrawable(
                                                new ColorDrawable(Color.TRANSPARENT));
                                        View convertView = inflater.inflate(R.layout.absent_history, null);
                                        alertDialog.setContentView(convertView);

                                        //Image View
                                        ImageView img_close;
                                        final ListView list_absent_history = (ListView) convertView.findViewById(R.id.list_absent_history);
                                        img_close = (ImageView) convertView.findViewById(R.id.img_close);
                                        TeacherViewAttendanceAdapter teacher_attendnaceAdapter = new TeacherViewAttendanceAdapter(context, login_details.ViewStudAttendRes,hideReason);
                                        list_absent_history.setAdapter(teacher_attendnaceAdapter);
                                        alertDialog.show();

                                        img_close.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                alertDialog.dismiss();
                                            }
                                        });

                                    }
                                    else if(hideReason.equalsIgnoreCase("1")){
                                        final Dialog alertDialog = new Dialog(context);
                                        //  final AlertDialog alertDialog = builder.create();
                                        //  LayoutInflater inflater = context.getLayoutInflater();
                                        LayoutInflater inflater = LayoutInflater.from(context);

                                        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        alertDialog.getWindow().setBackgroundDrawable(
                                                new ColorDrawable(Color.TRANSPARENT));
                                        View convertView = inflater.inflate(R.layout.present_history, null);
                                        alertDialog.setContentView(convertView);

                                        //Image View
                                        ImageView img_close;
                                        final ListView list_present_history = (ListView) convertView.findViewById(R.id.list_present_history);
                                        img_close = (ImageView) convertView.findViewById(R.id.img_close);
                                        TeacherViewAttendanceAdapter teacher_attendnaceAdapter = new TeacherViewAttendanceAdapter(context, login_details.ViewStudAttendRes,hideReason);
                                        list_present_history.setAdapter(teacher_attendnaceAdapter);
                                        alertDialog.show();

                                        img_close.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                alertDialog.dismiss();
                                            }
                                        });
                                    }
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Show error or whatever...
                        Log.d("LoginActivity", "ERROR.._---" + error.getCause());
                    }
                });
    }
    public static class ViewHolder
    {
        TextView tv_roll_no_value,tv_student_name,tv_attendance_value,tv_attendance_Presentvalue;
        LinearLayout ll_roll_no,ll_main,ll_attendance,ll_attendance_present;
    }
}
