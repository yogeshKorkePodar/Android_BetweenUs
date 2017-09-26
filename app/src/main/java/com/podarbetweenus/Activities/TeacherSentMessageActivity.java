package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.Adapter.TeacherBehaviourAdapter;
import com.podarbetweenus.Adapter.TeacherMessageAdapter;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.Entity.ViewMessageResult;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;
import com.podarbetweenus.Utility.LoadMoreListView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Gayatri on 1/19/2016.
 */
public class TeacherSentMessageActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener{
    //Button
    Button btn_receiver;
    //TextView
    TextView tv_error_msg;
    //ProgressDialog
    ProgressDialog progressDialog;
    //ListView
    ListView lv_teacher_sent_msg;

    //LayoutEntities
    HeaderControler header;
    DataFetchService dft;
    LoginDetails login_details;
    TeacherMessageAdapter teacherMessageAdapter;

    String clt_id,usl_id,month="0",check="1",school_name,teacher_name,version_name,board_name,detail_msg,sender_name,date,subject,toUslId,
            stud_id,pmuId,org_id,clas_teacher,academic_year,teacher_div,teacher_std,teacher_shift,attachment_name,attachment_path;
    String ShowSentMessagesMethod_name = "GetSentMessageDataTeacher";
    public String Loadmore="";
    int pageIndex=1,notificationId;
    int pageSize=200;
    boolean isLoading = false;

    ArrayList<ViewMessageResult> viewMessageResult_loadMore = new ArrayList<ViewMessageResult>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.teacher_sent_messages);
        findViews();
        init();
        getIntetnData();
        //Call WS for sent messages
        callWebserviceForSentMessages(clt_id, usl_id, month, check, pageIndex,pageSize);
    }

    private void getIntetnData() {

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
            AppController.versionName = version_name;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        progressDialog = Constant.getProgressDialog(this);
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        lv_teacher_sent_msg.setOnItemClickListener(this);
    }
    private void findViews() {
        //TextView
        tv_error_msg = (TextView) findViewById(R.id.tv_error_msg);
        //ListView
        lv_teacher_sent_msg = (ListView) findViewById(R.id.lv_teacher_sent_msg);
    }
    private void callWebserviceForSentMessages(String clt_id,String usl_id,String month,String check,int pageIndex,int pageSize) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getTeacherSentMessagest(clt_id, usl_id, month, check, pageIndex, pageSize, ShowSentMessagesMethod_name, Request.Method.POST,
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
                                lv_teacher_sent_msg.setVisibility(View.VISIBLE);
                                setUIDataDataForList();

                            } else {
                                lv_teacher_sent_msg.setVisibility(View.GONE);
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
                        Log.d("SendMessageActivity", "ERROR.._---" + error.getCause());
                    }
                });
    }

    private void setUIDataDataForList() {
        teacherMessageAdapter = new TeacherMessageAdapter(TeacherSentMessageActivity.this, login_details.ViewMessageResult, notificationId,clt_id,usl_id,school_name,academic_year,org_id,teacher_name,teacher_div,teacher_shift,teacher_std,board_name,version_name);
        lv_teacher_sent_msg.setAdapter(teacherMessageAdapter);
    }
    @Override
    public void onClick(View v) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            Log.d("<<ListItemClick","ItemClicked");
            AppController.SendMessageLayout = "false";
            detail_msg = login_details.ViewMessageResult.get(position).pmg_Message;
            sender_name = login_details.ViewMessageResult.get(position).Fullname;
            date = login_details.ViewMessageResult.get(position).pmg_date;
            subject = login_details.ViewMessageResult.get(position).pmg_subject;
            stud_id = login_details.ViewMessageResult.get(position).stu_id;
            toUslId = login_details.ViewMessageResult.get(position).usl_ID;
            pmuId = login_details.ViewMessageResult.get(position).pmu_ID;
            attachment_name = login_details.ViewMessageResult.get(position).pmg_file_name;
            attachment_path = login_details.ViewMessageResult.get(position).pmg_file_path;
            Intent detail_msg_intent = new Intent(TeacherSentMessageActivity.this, TeacherDetailMessageActivity.class);
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
            detail_msg_intent.putExtra("Attachment Name",attachment_name);
            detail_msg_intent.putExtra("Attachment Path",attachment_path);
            detail_msg_intent.putExtra("toUslId", toUslId);
            detail_msg_intent.putExtra("Pmu_Id", pmuId);
            detail_msg_intent.putExtra("org_id", org_id);
            detail_msg_intent.putExtra("cls_teacher", clas_teacher);
            detail_msg_intent.putExtra("academic_year", academic_year);
            startActivity(detail_msg_intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        try {
            Intent back = new Intent(TeacherSentMessageActivity.this, TeacherProfileActivity.class);
            AppController.enterMessageBack = "false";
            back.putExtra("clt_id", clt_id);
            back.putExtra("usl_id", usl_id);
            back.putExtra("School_name", school_name);
            back.putExtra("Teacher_name", teacher_name);
            back.putExtra("version_name", AppController.versionName);
            back.putExtra("board_name", board_name);
            back.putExtra("org_id", org_id);
            back.putExtra("cls_teacher", clas_teacher);
            back.putExtra("academic_year", academic_year);
            back.putExtra("Teacher_div", teacher_div);
            back.putExtra("Teacher_Shift",teacher_shift);
            back.putExtra("Tecaher_Std",teacher_std);
            startActivity(back);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
