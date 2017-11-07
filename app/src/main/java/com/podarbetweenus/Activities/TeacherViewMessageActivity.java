package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.Adapter.MessageAdpater;
import com.podarbetweenus.Adapter.TeacherMessageAdapter;
import com.podarbetweenus.Adapter.TeacherViewAttendanceAdapter;
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
 * Created by Gayatri on 1/19/2016.
 */
public class TeacherViewMessageActivity extends Activity implements AdapterView.OnItemClickListener,ListResultReceiver.Receiver
{
    //ListView
    ListView lv_teacher_view_messages;
    //TextView
    TextView tv_error_msg;
    //ProgressDialog
    ProgressDialog progressDialog;

    DataFetchService dft;
    LoginDetails login_details;
    TeacherMessageAdapter teacherMessageAdapter;
    AlarmReceiver alaram_receiver;

    int pageIndex=1,viewTeacherMessageListSize,notificationId,notificationID=1;
    int pageSize=500;
    String clt_id,usl_id,month="0",check="0",school_name,teacher_name,version_name,board_name,detail_msg,sender_name,date,subject,toUslId,
            stud_id,pmuId,org_id,clas_teacher,academic_year,teacher_div,teacher_std,teacher_shift,attachment_name,attachment_path;
    String ShowViewMessagesMethod_name = "GetSentMessageDataTeacher";
    ArrayList<ViewMessageResult> results;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.teacher_view_message);
        Log.d("<< Inside","TeacherViewMessageActivity");
    findViews();
    init();
    getIntentData();
    onNewIntent(getIntent());
    try {
     //Call Ws For View Messages
    CallWebserviceToViewMessages(clt_id, usl_id, month, check, pageIndex, pageSize);
    }
    catch (Exception e){
        e.printStackTrace();
    }
}

    private void getIntentData() {
        try {
            Intent intent = getIntent();
            usl_id = intent.getStringExtra("usl_id");
            clt_id = intent.getStringExtra("clt_id");
            school_name = intent.getStringExtra("School_name");
            teacher_name = intent.getStringExtra("Teacher_name");
            version_name = intent.getStringExtra("version_name");
            board_name = intent.getStringExtra("board_name");
            org_id = intent.getStringExtra("org_id");
            clas_teacher = intent.getStringExtra("cls_teacher");
            academic_year = intent.getStringExtra("academic_year");
            teacher_div = intent.getStringExtra("Teacher_div");
            teacher_std = intent.getStringExtra("Tecaher_Std");
            teacher_shift = intent.getStringExtra("Teacher_Shift");
            notificationId = getIntent().getExtras().getInt("notificationID");
            AppController.versionName = version_name;
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        teacher_div = extras.getString("Teacher_div");
        teacher_shift = extras.getString("Teacher_Shift");
        teacher_std = extras.getString("Tecaher_Std");
        clas_teacher = intent.getStringExtra("cls_teacher");
        academic_year = intent.getStringExtra("academic_year");
        board_name = extras.getString("board_name");
        AppController.versionName = version_name;
    }

    private void init() {
        progressDialog = Constant.getProgressDialog(this);
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        lv_teacher_view_messages.setOnItemClickListener(this);
    }
    private void findViews() {
        //TextView
        tv_error_msg = (TextView) findViewById(R.id.tv_error_msg);
        //ListView
        lv_teacher_view_messages = (ListView) findViewById(R.id.lv_teacher_view_messages);

    }
    private void registerReceiver() {
		/*create filter for exact intent what we want from other intent*/
        IntentFilter intentFilter =new IntentFilter(AlarmReceiver.ACTION_TEXT_CAPITALIZED);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);

		/* create new broadcast receiver*/
        alaram_receiver=new AlarmReceiver();

		/* registering our Broadcast receiver to listen action*/
        registerReceiver(alaram_receiver, intentFilter);
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            //Call Ws For View Messages
            CallWebserviceToViewMessages(clt_id, usl_id, month, check, pageIndex, pageSize);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void CallWebserviceToViewMessages(String clt_id,String usl_id,String month,String check,int pageIndex,int pageSize) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getTeacherSentMessagest(clt_id, usl_id, month, check, pageIndex, pageSize, ShowViewMessagesMethod_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);

                            if (login_details.Status.equalsIgnoreCase("1")) {

                                tv_error_msg.setVisibility(View.GONE);
                                lv_teacher_view_messages.setVisibility(View.VISIBLE);
                                setUIDataDataForList();

                            } else {
                                lv_teacher_view_messages.setVisibility(View.GONE);
                                tv_error_msg.setVisibility(View.VISIBLE);
                                tv_error_msg.setText("No Records Found");
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

        viewTeacherMessageListSize = login_details.ViewMessageResult.size();
        AppController.viewTeacherMessageListSize = viewTeacherMessageListSize;
        teacherMessageAdapter = new TeacherMessageAdapter(TeacherViewMessageActivity.this,login_details.ViewMessageResult,notificationId,clt_id,usl_id,school_name,academic_year,org_id,teacher_name,teacher_div,teacher_shift,teacher_std,board_name,version_name);
        lv_teacher_view_messages.setAdapter(teacherMessageAdapter);
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
                teacherMessageAdapter = new TeacherMessageAdapter(TeacherViewMessageActivity.this, AppController.results, notificationId,clt_id,usl_id,school_name,academic_year,org_id,teacher_name,teacher_div,teacher_shift,teacher_std,board_name,version_name);
                lv_teacher_view_messages.setAdapter(teacherMessageAdapter);

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
            Intent back = new Intent(TeacherViewMessageActivity.this, TeacherProfileActivity.class);
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
            back.putExtra("Teacher_name", teacher_name);
            back.putExtra("cls_teacher", clas_teacher);
            back.putExtra("academic_year", academic_year);
            back.putExtra("org_id", org_id);
            back.putExtra("Teacher_div",teacher_div);
            back.putExtra("Teacher_Shift",teacher_shift);
            back.putExtra("Tecaher_Std",teacher_std);
            back.putExtra("version_name", AppController.versionName);
            back.putExtra("verion_code", AppController.versionCode);
            startActivity(back);
        }
        catch (Exception e){
            e.printStackTrace();
        }
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
            Intent detail_msg_intent = new Intent(TeacherViewMessageActivity.this, TeacherDetailMessageActivity.class);
            detail_msg_intent.putExtra("Detail Message", detail_msg);
            detail_msg_intent.putExtra("Sender Name", sender_name);
            detail_msg_intent.putExtra("Date", date);
            detail_msg_intent.putExtra("clt_id", clt_id);
            detail_msg_intent.putExtra("usl_id", usl_id);
            detail_msg_intent.putExtra("School_name", school_name);
            detail_msg_intent.putExtra("Teacher_name", teacher_name);
            detail_msg_intent.putExtra("version_name", AppController.versionName);
            detail_msg_intent.putExtra("board_name", board_name);
            detail_msg_intent.putExtra("Subject", subject);
            detail_msg_intent.putExtra("stud_id", stud_id);
            detail_msg_intent.putExtra("org_id", org_id);
            detail_msg_intent.putExtra("toUslId", toUslId);
            detail_msg_intent.putExtra("Attachment Name", attachment_name);
            detail_msg_intent.putExtra("Attachment Path", attachment_path);
            detail_msg_intent.putExtra("Teacher_div",teacher_div);
            detail_msg_intent.putExtra("Teacher_Shift",teacher_shift);
            detail_msg_intent.putExtra("Tecaher_Std",teacher_std);
            detail_msg_intent.putExtra("cls_teacher", clas_teacher);
            detail_msg_intent.putExtra("academic_year", academic_year);
            detail_msg_intent.putExtra("Pmu_Id", pmuId);
            startActivity(detail_msg_intent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
