package com.podarbetweenus.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.Activities.LoginActivity;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.AnnouncementResult;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;
import com.podarbetweenus.Utility.BadgeView;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 10/26/2015.
 */
public class AnnouncementAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;
    private Activity cont;
    String date,msg_id,announcment,old_announcement;
    //Button
    Button btn_update,btn_cancel;
    //EditText
    EditText ed_enter_announcement;
    DataFetchService dft;
    ProgressDialog progressDialog;
    LoginDetails login_details = new LoginDetails();
    String AdminEditAnnouncementMethodName = "UpdateAdminAnnoucement";
    private ArrayList<AnnouncementResult> announcement_list = new ArrayList<AnnouncementResult>();

    public AnnouncementAdapter(Context context,
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View showLeaveStatus = convertView;

        if (showLeaveStatus == null)
        {
            holder = new ViewHolder();
            if(AppController.announcementActivity.equalsIgnoreCase("true")){
                showLeaveStatus = layoutInflater.inflate(R.layout.template_announcement_admin, null);
                holder.tv_announce_date = (TextView) showLeaveStatus.findViewById(R.id.tv_announce_date);
                holder.tv_announcement = (TextView) showLeaveStatus.findViewById(R.id.tv_announcement);
                holder.img_edit_announcement = (ImageView) showLeaveStatus.findViewById(R.id.img_edit_announcement);
                holder.badge = new BadgeView(context, holder.tv_notifiaction_count);
                holder.badge.setBadgeBackgroundColor(Color.RED);
                holder.badge.setTextColor(Color.WHITE);
            }
            else{
                showLeaveStatus = layoutInflater.inflate(R.layout.template_announcement, null);
                holder.tv_announce_date = (TextView) showLeaveStatus.findViewById(R.id.tv_announce_date);
                holder.tv_announcement = (TextView) showLeaveStatus.findViewById(R.id.tv_announcement);
                holder.tv_notifiaction_count = (TextView) showLeaveStatus.findViewById(R.id.tv_notifiaction_count);
                holder.badge = new BadgeView(context, holder.tv_notifiaction_count);
                holder.badge.setBadgeBackgroundColor(Color.RED);
                holder.badge.setTextColor(Color.WHITE);
            }
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
        try {
            if (announcement_list.get(position).pmu_readunreadstatus.equalsIgnoreCase("0") || announcement_list.get(position).pmu_readunreadstatus.equalsIgnoreCase("")) {
                if(LoginActivity.get_RollId(context).equalsIgnoreCase("2")){
                    String msg = announcement_list.get(position).msg_Message.trim();
                    String message = msg.replaceAll("\n", "");
                    holder.tv_announcement.setText(message);
                }
                else {
                    holder.tv_announcement.setTypeface(Typeface.DEFAULT);
                    String msg = announcement_list.get(position).msg_Message.trim();
                    String message = msg.replaceAll("\n", "");
                    holder.tv_announcement.setText(message);
                    holder.badge.hide();
                }
            } else {
                if(LoginActivity.get_RollId(context).equalsIgnoreCase("2")){
                    String msg = announcement_list.get(position).msg_Message.trim();
                    String message = msg.replaceAll("\n", "");
                    holder.tv_announcement.setText(message);
                }
                else{
                    holder.tv_announcement.setTypeface(Typeface.DEFAULT_BOLD);
                    String msg = announcement_list.get(position).msg_Message.trim();
                    String message = msg.replaceAll("\n", "");
                    holder.tv_announcement.setText(message);
                    holder.badge.setText("1");
                    holder.badge.show();
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        holder.tv_announce_date.setText(formatted_date);
        return showLeaveStatus;
    }
    private void callWSToEditAnnouncement(String msg_id, final String announcment) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getTeacherEditannaouncement(msg_id, announcment, AdminEditAnnouncementMethodName, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_details.Status.equalsIgnoreCase("1")) {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();

                                }
                                    Constant.showOkPopup(cont,login_details.StatusMsg, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            dialogInterface.dismiss();
                                        }
                                    });
                            } else if (login_details.Status.equalsIgnoreCase("0")) {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Show error or whatever...
                        Log.d("TeacherAnnouncActivity", "ERROR.._---" + error.getCause());
                    }
                });

    }
    public static class ViewHolder
    {
        TextView tv_announce_date,tv_announcement,tv_notifiaction_count;
        ImageView img_edit_announcement;
        BadgeView badge;
    }

}

