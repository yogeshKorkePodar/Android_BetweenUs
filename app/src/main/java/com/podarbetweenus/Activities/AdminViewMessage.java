package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.Adapter.AdminMesageAdapter;
import com.podarbetweenus.Adapter.TeacherMessageAdapter;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.Entity.ViewMessageResult;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Services.ListResultReceiver;
import com.podarbetweenus.Utility.AppController;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Gayatri on 3/16/2016.
 */
public class AdminViewMessage extends Activity implements AdapterView.OnItemClickListener,ListResultReceiver.Receiver {
    //UI Variables
    //ListView
    ListView lv_admin_view_message;
    //TextView
    TextView tv_no_record;
    //ProgressDialog
    ProgressDialog progressDialog;

    DataFetchService dft;
    LoginDetails login_details;
    AdminMesageAdapter teacherMessageAdapter;
    public static SharedPreferences resultpreLoginData;

    String usl_id,clt_id,board_name,org_id,version_name,school_name,admin_name,check = "0",detail_msg,sender_name,date,subject,toUslId,pmuId,stud_id,academic_year,
    attachment_name,attachment_path,attachment;
    String ShowViewMessagesMethod_name = "ViewAdminMessageDetails";
    int pageNo = 1,notificationId,notificationID=1,viewTeacherMessageListSize,tab_position;
    int pageSize = 200;

    ArrayList<ViewMessageResult> results;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.admin_viewmessage_layout);
        findViews();
        init();
        getIntentId();
        onNewIntent(getIntent());
        //call ws to view messages
        callWsToViewMessages(clt_id, usl_id, check, pageNo, pageSize);

    }

    private void getIntentId() {
        try {
            Intent intent = getIntent();
            usl_id = intent.getStringExtra("usl_id");
            clt_id = intent.getStringExtra("clt_id");
            school_name = intent.getStringExtra("School_name");
            admin_name = intent.getStringExtra("Admin_name");
            version_name = intent.getStringExtra("version_name");
            org_id = intent.getStringExtra("org_id");
            academic_year = intent.getStringExtra("academic_year");
            AppController.versionName = version_name;
            board_name = intent.getStringExtra("board_name");
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void init() {
        dft= new DataFetchService(this);
        login_details = new LoginDetails();
        progressDialog = Constant.getProgressDialog(this);
        lv_admin_view_message.setOnItemClickListener(this);
    }
    @Override
    public void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        extras.containsKey("clt_id");
        extras.containsKey("msd_ID");
        extras.containsKey("usl_id");
        extras.containsKey("board_name");
        clt_id = extras.getString("clt_id");
        usl_id = extras.getString("usl_id");
        org_id = extras.getString("org_id");
        tab_position = intent.getIntExtra("Tab_Position", 0);
        Log.e("tab_position",String.valueOf(tab_position));
        board_name = extras.getString("board_name");
        AppController.versionName = version_name;
    }
    private void findViews() {
        //TExtView
        tv_no_record = (TextView) findViewById(R.id.tv_no_record);
        //ListView
        lv_admin_view_message = (ListView) findViewById(R.id.lv_admin_view_message);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //call ws to view messages
        callWsToViewMessages(clt_id,usl_id,check,pageNo,pageSize);
    }
    private void callWsToViewMessages(String clt_id,String usl_id,String check,int pageNo,int pageSize) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getAdminMessages(clt_id, usl_id, check, pageNo, pageSize, ShowViewMessagesMethod_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);

                            if (login_details.Status.equalsIgnoreCase("1")) {

                                tv_no_record.setVisibility(View.GONE);
                                // getListView().setVisibility(View.VISIBLE);
                                lv_admin_view_message.setVisibility(View.VISIBLE);
                                setUIDataDataForList();

                            } else {
                                lv_admin_view_message.setVisibility(View.GONE);
                                tv_no_record.setVisibility(View.VISIBLE);
                                tv_no_record.setText("No Records Found");
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

    private void setUIDataDataForList() {
        teacherMessageAdapter = new AdminMesageAdapter(AdminViewMessage.this,login_details.ViewMessageResult,notificationId,clt_id,usl_id,school_name,academic_year,org_id,admin_name,board_name,version_name);
        lv_admin_view_message.setAdapter(teacherMessageAdapter);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            AppController.SendMessageLayout = "true";

            if (login_details.ViewMessageResult.get(position).pmu_readunreadstatus.equalsIgnoreCase("1")) {
                AppController.UnreadMessage = "true";
                if (position == 0) {
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(notificationID);
                }
            } else {
                AppController.UnreadMessage = "false";
            }
            detail_msg = login_details.ViewMessageResult.get(position).pmg_Message;
            sender_name = login_details.ViewMessageResult.get(position).Fullname;
            date = login_details.ViewMessageResult.get(position).pmg_date;
            subject = login_details.ViewMessageResult.get(position).pmg_subject;
            stud_id = login_details.ViewMessageResult.get(position).stu_id;
            toUslId = login_details.ViewMessageResult.get(position).usl_ID;
            pmuId = login_details.ViewMessageResult.get(position).pmu_ID;
            attachment_name = login_details.ViewMessageResult.get(position).pmg_file_name;
            attachment_path = login_details.ViewMessageResult.get(position).pmg_file_path;
            Intent detail_msg_intent = new Intent(AdminViewMessage.this, AdminDetailMessage.class);
            detail_msg_intent.putExtra("Detail Message", detail_msg);
            detail_msg_intent.putExtra("Sender Name", sender_name);
            detail_msg_intent.putExtra("Date", date);
            detail_msg_intent.putExtra("clt_id", clt_id);
            detail_msg_intent.putExtra("usl_id", usl_id);
            detail_msg_intent.putExtra("School_name", school_name);
            detail_msg_intent.putExtra("Admin_name", admin_name);
            detail_msg_intent.putExtra("version_name", AppController.versionName);
            detail_msg_intent.putExtra("board_name", board_name);
            detail_msg_intent.putExtra("Attachment Name", attachment_name);
            detail_msg_intent.putExtra("Attachment Path", attachment_path);
            // Log.e("PAth",attachment_path);
            detail_msg_intent.putExtra("Subject", subject);
            detail_msg_intent.putExtra("stud_id", stud_id);
            detail_msg_intent.putExtra("org_id", org_id);
            detail_msg_intent.putExtra("toUslId", toUslId);
            detail_msg_intent.putExtra("academic_year", academic_year);
            detail_msg_intent.putExtra("Pmu_Id", pmuId);
            startActivity(detail_msg_intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

    }

    public class AlarmReceiver extends BroadcastReceiver {
        /**
         * action string for our broadcast receiver to get notified
         */
        public final static String ACTION_TEXT_CAPITALIZED= "com.android.guide.exampleintentservice.intent.action.ACTION_TEXT_CAPITALIZED";
        @Override
        public void onReceive(Context context, Intent intent) {

            ///    AppController.Notification_send = "true";
            try {
                Bundle bundle = intent.getExtras();
                results = (ArrayList<ViewMessageResult>) bundle.getSerializable("results");
                viewTeacherMessageListSize = AppController.results.size();
                AppController.viewTeacherMessageListSize = viewTeacherMessageListSize;
                teacherMessageAdapter = new AdminMesageAdapter(AdminViewMessage.this, AppController.results, notificationId,clt_id,usl_id,school_name,academic_year,org_id,admin_name,board_name,version_name);
                lv_admin_view_message.setAdapter(teacherMessageAdapter);

            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        try {
            Intent back = new Intent(AdminViewMessage.this, AdminProfileActivity.class);
            AppController.OnBackpressed = "false";
            AppController.clt_id = clt_id;
            AppController.usl_id = usl_id;
            AppController.Board_name = board_name;
            back.putExtra("clt_id", AppController.clt_id);
            back.putExtra("msd_ID", AppController.msd_ID);
            back.putExtra("msd_ID", AppController.msd_ID);
            back.putExtra("usl_id", AppController.usl_id);
            back.putExtra("board_name", board_name);
            back.putExtra("School_name", school_name);
            back.putExtra("Admin_name", admin_name);
            back.putExtra("org_id", org_id);
            back.putExtra("academic_year", academic_year);
            back.putExtra("version_name", AppController.versionName);
            back.putExtra("verion_code", AppController.versionCode);
            startActivity(back);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
