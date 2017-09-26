package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.Adapter.TeacherEnterMessageAdapter;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

import org.json.JSONObject;

/**
 * Created by Gayatri on 1/19/2016.
 */
public class TeacherEnterMessageActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener{

    //Linear Layout
    LinearLayout ll_student_list,ll_title;
    //Button
    Button show_students_messages;
    //Checkbox
    CheckBox select_all_checkbox;

    //ListView to display list of classes to which teacher can send message
    ListView lv_select_send_message_student;
    //TextView
    TextView tv_version_name,tv_version_code,tv_cancel,tv_error_msg,tv_child_name_drawer,tv_academic_year_drawer;
    //ImageView
    ImageView img_drawer;
    //Linear Layout
    LinearLayout lay_back_investment;
    //Relative Layout
    RelativeLayout leftfgf_drawer,rl_profile;
    DrawerLayout drawer;
    //ListView
    ListView drawerListView;
    //ProgressDialog
    ProgressDialog progressDialog;
    //LayoutEntities
    HeaderControler header;
    ContentDisplaceDrawerToggle mContentDisplaceToggle;
    DataFetchService dft;
    LoginDetails login_details;
    TeacherEnterMessageAdapter teacherEnterMessageAdapter;


    String clt_id,usl_id,msd_id,board_name,school_name,teacher_name,version_name,class_id,org_id,clas_teacher,academic_year,teacher_shift,teacher_std,teacher_div;
    String MessageDropdown_Method_name = "GetTeacherSMSDropDtls";
    int notificationID=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.teacher_enter_message);
        findViews();
        init();
        getIntentData();
        lv_select_send_message_student.setDivider(null);
        lv_select_send_message_student.setDividerHeight(0);
        try {
            //  call ws for student selection
            callWebserviceForSelectStudenttoEnterMessage(clt_id, usl_id);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getIntentData() {
            Intent intent = getIntent();
            usl_id = intent.getStringExtra("usl_id");
            clt_id = intent.getStringExtra("clt_id");
            school_name = intent.getStringExtra("School_name");
            teacher_name = intent.getStringExtra("Teacher_name");
            version_name = intent.getStringExtra("version_name");
            board_name = intent.getStringExtra("board_name");
            clas_teacher = intent.getStringExtra("cls_teacher");
            teacher_div = intent.getStringExtra("Teacher_div");
            teacher_std = intent.getStringExtra("Tecaher_Std");
            teacher_shift = intent.getStringExtra("Teacher_Shift");
            academic_year = intent.getStringExtra("academic_year");
            org_id = intent.getStringExtra("org_id");
            AppController.versionName = version_name;
    }
    private void init() {
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        progressDialog = Constant.getProgressDialog(this);
        lv_select_send_message_student.setOnItemClickListener(this);
    }
    private void findViews() {
        //TextView
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_error_msg = (TextView) findViewById(R.id.tv_error_msg);
        tv_version_code = (TextView) findViewById(R.id.tv_version_code);
        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
        tv_child_name_drawer = (TextView) findViewById(R.id.tv_child_name_drawer);
        tv_academic_year_drawer = (TextView) findViewById(R.id.tv_academic_year_drawer);
        lv_select_send_message_student = (ListView) findViewById(R.id.lv_select_send_message_student);
        // for drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        leftfgf_drawer = (RelativeLayout) findViewById(R.id.leftfgf_drawer);
        rl_profile = (RelativeLayout) findViewById(R.id.rl_profile);
        drawerListView = (ListView) findViewById(R.id.drawerListView);
        img_drawer = (ImageView) findViewById(R.id.img_drawer);
        drawerListView = (ListView) findViewById(R.id.drawerListView);
        mContentDisplaceToggle = new ContentDisplaceDrawerToggle(this, drawer,
                R.id.rl_profile, Gravity.START);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //int width = dm.widthPixels;
        int width = getResources().getDisplayMetrics().widthPixels/2+30;
        DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) leftfgf_drawer.getLayoutParams();
        lp.width = width + 30;
        leftfgf_drawer.setLayoutParams(lp);
    }
    private void callWebserviceForSelectStudenttoEnterMessage(final String clt_id, String usl_id) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getTeacherSMSDropdown(clt_id, usl_id, MessageDropdown_Method_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_details.Status.equalsIgnoreCase("1")) {
                                lv_select_send_message_student.setVisibility(View.VISIBLE);
                                tv_error_msg.setVisibility(View.GONE);
                                setUIData();

                            } else if (login_details.Status.equalsIgnoreCase("0")) {
                                lv_select_send_message_student.setVisibility(View.GONE);
                                tv_error_msg.setVisibility(View.VISIBLE);
                                tv_error_msg.setText("Data does not exist");

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Show error or whatever...
                        Log.d("TeacherEntrMesgActivity", "ERROR.._---" + error.getCause());
                    }
                });

    }

    private void setUIData() {
        teacherEnterMessageAdapter = new TeacherEnterMessageAdapter(TeacherEnterMessageActivity.this,login_details.StandardResult,login_details.AcedmicYearResult,login_details.DivisionResult,login_details.ShiftResult,login_details.SectionList);
        lv_select_send_message_student.setAdapter(teacherEnterMessageAdapter);
    }
    // To go back to TeacherProfileActivity
    @Override
    public void onClick(View v) {
        try {
            if (v == lay_back_investment) {
                Intent back = new Intent(TeacherEnterMessageActivity.this, TeacherProfileActivity.class);
                AppController.enterMessageBack = "false";
                back.putExtra("clt_id", clt_id);
                back.putExtra("usl_id", usl_id);
                back.putExtra("School_name", school_name);
                back.putExtra("Teacher_name", teacher_name);
                back.putExtra("version_name", AppController.versionName);
                back.putExtra("board_name", board_name);
                back.putExtra("Teacher_div",teacher_div);
                back.putExtra("Teacher_Shift",teacher_shift);
                back.putExtra("Tecaher_Std",teacher_std);
                back.putExtra("cls_teacher", clas_teacher);
                back.putExtra("academic_year", academic_year);
                back.putExtra("org_id", org_id);
                startActivity(back);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
//  To go to student selection list
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            if (dft.isInternetOn() == true) {
                Intent showstudentlist = new Intent(TeacherEnterMessageActivity.this, TeacherStudentListForMessage.class);
                class_id = login_details.StandardResult.get(position).class_id;
                showstudentlist.putExtra("clt_id", clt_id);
                showstudentlist.putExtra("usl_id", usl_id);
                showstudentlist.putExtra("School_name", school_name);
                showstudentlist.putExtra("Teacher_name", teacher_name);
                showstudentlist.putExtra("version_name", AppController.versionName);
                showstudentlist.putExtra("board_name", board_name);
                showstudentlist.putExtra("class_id", class_id);
                showstudentlist.putExtra("org_id", org_id);
                showstudentlist.putExtra("Teacher_div",teacher_div);
                showstudentlist.putExtra("Teacher_Shift",teacher_shift);
                showstudentlist.putExtra("Tecaher_Std",teacher_std);
                showstudentlist.putExtra("cls_teacher", clas_teacher);
                showstudentlist.putExtra("academic_year", academic_year);
                Log.e("cls_id", class_id);
                startActivity(showstudentlist);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        try {
            Intent back = new Intent(TeacherEnterMessageActivity.this, TeacherProfileActivity.class);
            AppController.enterMessageBack = "false";
            back.putExtra("clt_id", clt_id);
            back.putExtra("usl_id", usl_id);
            back.putExtra("School_name", school_name);
            back.putExtra("Teacher_name", teacher_name);
            back.putExtra("version_name", AppController.versionName);
            back.putExtra("board_name", board_name);
            back.putExtra("class_id", class_id);
            back.putExtra("Teacher_div",teacher_div);
            back.putExtra("Teacher_Shift",teacher_shift);
            back.putExtra("Tecaher_Std",teacher_std);
            back.putExtra("org_id", org_id);
            back.putExtra("cls_teacher", clas_teacher);
            back.putExtra("academic_year", academic_year);
            startActivity(back);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
