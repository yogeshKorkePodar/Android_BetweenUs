package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Created by Gayatri on 2/23/2016.
 */
public class TeacherSendMessageActivity extends Activity implements View.OnClickListener{

    //ProgressDialog
    ProgressDialog progressDialog;
    //Button
    Button btn_send_message,btn_attachment;
    //EditText
    EditText ed_message_subject,enter_sms;
    //TExtView
    TextView tv_charachter_length,tv_error_msg,tv_cancel,tv_child_name_drawer,tv_academic_year_drawer,tv_version_name,tv_version_code,tv_teacher_class;
    //LinearLayout
    LinearLayout lay_back_investment;
    //DrawerLayout
    DrawerLayout drawer;
    //Relative Layout
    RelativeLayout leftfgf_drawer,rl_profile;
    //ListView
    ListView drawerListView;
    //Imageview
    ImageView img_drawer;

    ContentDisplaceDrawerToggle mContentDisplaceToggle;
    DataFetchService dft;
    LoginDetails login_details;
    HeaderControler header;
    JSONObject jsonResponse;

    String clt_id,usl_id,msd_id,board_name,school_name,teacher_name,version_name,class_id,studIdColanseperated="",toUslId="0",filename = "0",filepath="0",
            stud_id,selectedCheckbox,selectAll = "false",studentName="",message_subject,message_content,clas_teacher,academic_year,org_id,teacher_div,
            teacher_std,teacher_shift,FilePath,filePath="0",attachedfilename="0",extension,responseStatus;
    String sendMessage_Method_name = "PostMessageToStudent";
    String[] data_without_sibling;
    int[] icons_without_sibling;
    int notificationID = 1;
    long length;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.teacher_send_message);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        findViews();
        init();
        getIntentData();
        setDrawer();
        //charachter lenght
     //   CheckCharachterLength();
        try{
            StrictMode.ThreadPolicy policy1 = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy1);
            String str = android.os.Build.MODEL;
            Log.d("<<dev", str);
            getDeviceName();
            Log.d("<<NAME", getDeviceName());
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setDrawer() {
        try {
            if(LoginActivity.get_class_teacher(TeacherSendMessageActivity.this).equalsIgnoreCase("1")) {
                icons_without_sibling = new int[]{R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sms_48x48, R.drawable.announcementicon_48x48, R.drawable.att_48x48, R.drawable.behaviouricon_48x48,R.drawable.subjectlist_48x48,R.drawable.setting1_hdpi, R.drawable.logout1_hdpi, R.drawable.info};
                data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement", "Attendance", "Behaviour","Subject List","Setting", "Sign Out", "About"};
                drawerListView.setAdapter(new CustomAccount(TeacherSendMessageActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));
                drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherSendMessageActivity.this, TeacherProfileActivity.class);
                                teacherProfile.putExtra("clt_id", clt_id);
                                teacherProfile.putExtra("usl_id", usl_id);
                                teacherProfile.putExtra("School_name", school_name);
                                teacherProfile.putExtra("Teacher_name", teacher_name);
                                teacherProfile.putExtra("org_id", org_id);
                                teacherProfile.putExtra("cls_teacher", clas_teacher);
                                teacherProfile.putExtra("academic_year", academic_year);
                                teacherProfile.putExtra("Teacher_div",teacher_div);
                                teacherProfile.putExtra("Teacher_Shift",teacher_shift);
                                teacherProfile.putExtra("Tecaher_Std",teacher_std);
                                teacherProfile.putExtra("version_name", AppController.versionName);
                                teacherProfile.putExtra("board_name", board_name);
                                startActivity(teacherProfile);
                            }
                        } else if (position == 1) {
                            //Message
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherSendMessageActivity.this, TeacherMessageActivity.class);
                                teacherProfile.putExtra("clt_id", clt_id);
                                teacherProfile.putExtra("usl_id", usl_id);
                                teacherProfile.putExtra("School_name", school_name);
                                teacherProfile.putExtra("Teacher_name", teacher_name);
                                teacherProfile.putExtra("org_id", org_id);
                                teacherProfile.putExtra("cls_teacher", clas_teacher);
                                teacherProfile.putExtra("academic_year", academic_year);
                                teacherProfile.putExtra("Teacher_div",teacher_div);
                                teacherProfile.putExtra("Teacher_Shift",teacher_shift);
                                teacherProfile.putExtra("Tecaher_Std",teacher_std);
                                teacherProfile.putExtra("version_name", AppController.versionName);
                                teacherProfile.putExtra("board_name", board_name);
                                startActivity(teacherProfile);
                            }
                        } else if (position == 2) {
                            //SMS
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherSendMessageActivity.this, TeacherSMSActivity.class);
                                teacherProfile.putExtra("clt_id", clt_id);
                                teacherProfile.putExtra("usl_id", usl_id);
                                teacherProfile.putExtra("School_name", school_name);
                                teacherProfile.putExtra("Teacher_name", teacher_name);
                                teacherProfile.putExtra("org_id", org_id);
                                teacherProfile.putExtra("cls_teacher", clas_teacher);
                                teacherProfile.putExtra("academic_year", academic_year);
                                teacherProfile.putExtra("Teacher_div",teacher_div);
                                teacherProfile.putExtra("Teacher_Shift",teacher_shift);
                                teacherProfile.putExtra("Tecaher_Std",teacher_std);
                                teacherProfile.putExtra("version_name", AppController.versionName);
                                teacherProfile.putExtra("board_name", board_name);
                                startActivity(teacherProfile);
                            }
                        } else if (position == 3) {
                            //announcement
                            if (dft.isInternetOn() == true) {
                                Intent teacherannouncment = new Intent(TeacherSendMessageActivity.this, TeacherAnnouncementsActivity.class);
                                teacherannouncment.putExtra("clt_id", clt_id);
                                teacherannouncment.putExtra("usl_id", usl_id);
                                teacherannouncment.putExtra("School_name", school_name);
                                teacherannouncment.putExtra("Teacher_name", teacher_name);
                                teacherannouncment.putExtra("version_name", AppController.versionName);
                                teacherannouncment.putExtra("board_name", board_name);
                                teacherannouncment.putExtra("Teacher_div",teacher_div);
                                teacherannouncment.putExtra("Teacher_Shift",teacher_shift);
                                teacherannouncment.putExtra("Tecaher_Std",teacher_std);
                                teacherannouncment.putExtra("cls_teacher", clas_teacher);
                                teacherannouncment.putExtra("academic_year", academic_year);
                                teacherannouncment.putExtra("org_id", org_id);
                                startActivity(teacherannouncment);
                            }
                        } else if (position == 4) {
                            //Attendance
                            if (dft.isInternetOn() == true) {
                                Intent teacherattendance = new Intent(TeacherSendMessageActivity.this, TeacherAttendanceActivity.class);
                                teacherattendance.putExtra("clt_id", clt_id);
                                teacherattendance.putExtra("usl_id", usl_id);
                                teacherattendance.putExtra("School_name", school_name);
                                teacherattendance.putExtra("Teacher_name", teacher_name);
                                teacherattendance.putExtra("version_name", AppController.versionName);
                                teacherattendance.putExtra("board_name", board_name);
                                teacherattendance.putExtra("cls_teacher", clas_teacher);
                                teacherattendance.putExtra("academic_year", academic_year);
                                teacherattendance.putExtra("org_id", org_id);
                                teacherattendance.putExtra("Teacher_div",teacher_div);
                                teacherattendance.putExtra("Teacher_Shift",teacher_shift);
                                teacherattendance.putExtra("Tecaher_Std",teacher_std);
                                startActivity(teacherattendance);
                            }
                        } else if (position == 5) {
                            //Behaviour
                            if (dft.isInternetOn() == true) {
                                Intent teacher_behaviour = new Intent(TeacherSendMessageActivity.this, TeacherBehaviourActivity.class);
                                teacher_behaviour.putExtra("clt_id", clt_id);
                                teacher_behaviour.putExtra("usl_id", usl_id);
                                teacher_behaviour.putExtra("School_name", school_name);
                                teacher_behaviour.putExtra("Teacher_name", teacher_name);
                                teacher_behaviour.putExtra("version_name", AppController.versionName);
                                teacher_behaviour.putExtra("board_name", board_name);
                                teacher_behaviour.putExtra("org_id", org_id);
                                teacher_behaviour.putExtra("Teacher_div",teacher_div);
                                teacher_behaviour.putExtra("Teacher_Shift",teacher_shift);
                                teacher_behaviour.putExtra("Tecaher_Std",teacher_std);
                                teacher_behaviour.putExtra("cls_teacher", clas_teacher);
                                teacher_behaviour.putExtra("academic_year", academic_year);
                                startActivity(teacher_behaviour);
                            }
                        } else if (position == 6) {
                            //Sbject List
                            if (dft.isInternetOn() == true) {
                                Intent subject_list = new Intent(TeacherSendMessageActivity.this, SubjectListActivity.class);
                                subject_list.putExtra("usl_id", usl_id);
                                subject_list.putExtra("version_name", AppController.versionName);
                                subject_list.putExtra("clt_id", clt_id);
                                subject_list.putExtra("School_name", school_name);
                                subject_list.putExtra("Teacher_name", teacher_name);
                                subject_list.putExtra("board_name", board_name);
                                subject_list.putExtra("org_id", org_id);
                                subject_list.putExtra("cls_teacher", clas_teacher);
                                subject_list.putExtra("academic_year", academic_year);
                                subject_list.putExtra("Teacher_div",teacher_div);
                                subject_list.putExtra("Teacher_Shift",teacher_shift);
                                subject_list.putExtra("Tecaher_Std",teacher_std);
                                startActivity(subject_list);
                            }
                        } else if (position == 7) {
                            Intent setting = new Intent(TeacherSendMessageActivity.this, SettingActivity.class);
                            setting.putExtra("usl_id", usl_id);
                            setting.putExtra("version_name", AppController.versionName);
                            setting.putExtra("clt_id", clt_id);
                            setting.putExtra("School_name", school_name);
                            setting.putExtra("Teacher_name", teacher_name);
                            setting.putExtra("board_name", board_name);
                            setting.putExtra("org_id", org_id);
                            setting.putExtra("Teacher_div",teacher_div);
                            setting.putExtra("Teacher_Shift",teacher_shift);
                            setting.putExtra("Tecaher_Std",teacher_std);
                            setting.putExtra("cls_teacher", clas_teacher);
                            setting.putExtra("academic_year", academic_year);
                            startActivity(setting);
                        } else if (position == 8) {
                            //Signout
                            Intent signout = new Intent(TeacherSendMessageActivity.this, LoginActivity.class);
                            Constant.SetLoginData("", "", TeacherSendMessageActivity.this);
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancel(notificationID);
                            AppController.iconCount = 0;
                            AppController.iconCountOnResume = 0;
                            AppController.iconAnnouncementCount = 0;
                            AppController.iconAnnouncementCountOnResume = 0;
                            AppController.OnBackpressed = "false";
                            AppController.loginButtonClicked = "false";
                            AppController.drawerSignOut = "true";
                            AppController.announcementActivity = "false";
                            startActivity(signout);
                        } else if (position == 9) {
                            try {
                                final Dialog alertDialog = new Dialog(TeacherSendMessageActivity.this);
                                LayoutInflater inflater = getLayoutInflater();
                                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                alertDialog.getWindow().setBackgroundDrawable(
                                        new ColorDrawable(Color.TRANSPARENT));
                                View convertView = inflater.inflate(R.layout.about_us, null);
                                alertDialog.setContentView(convertView);
                                tv_version_name = (TextView) convertView.findViewById(R.id.tv_version_name);
                                tv_version_code = (TextView) convertView.findViewById(R.id.tv_version_code);
                                tv_cancel = (TextView) convertView.findViewById(R.id.tv_cancel);
                                tv_version_name.setText("Version Name: " + AppController.versionName);
                                tv_version_code.setText("Version Code: " + AppController.versionCode);
                                alertDialog.show();

                                tv_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
            else{
                icons_without_sibling = new int[]{R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sms_48x48, R.drawable.announcementicon_48x48,R.drawable.subjectlist_48x48,R.drawable.setting1_hdpi, R.drawable.logout1_hdpi, R.drawable.info};
                data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement","Subject List","Setting", "Sign Out", "About"};
                drawerListView.setAdapter(new CustomAccount(TeacherSendMessageActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));
                drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherSendMessageActivity.this, TeacherProfileActivity.class);
                                teacherProfile.putExtra("clt_id", clt_id);
                                teacherProfile.putExtra("usl_id", usl_id);
                                teacherProfile.putExtra("School_name", school_name);
                                teacherProfile.putExtra("Teacher_name", teacher_name);
                                teacherProfile.putExtra("version_name", version_name);
                                teacherProfile.putExtra("board_name", board_name);
                                teacherProfile.putExtra("org_id", org_id);
                                teacherProfile.putExtra("Teacher_div",teacher_div);
                                teacherProfile.putExtra("Teacher_Shift",teacher_shift);
                                teacherProfile.putExtra("Tecaher_Std", teacher_std);
                                teacherProfile.putExtra("academic_year", academic_year);
                                teacherProfile.putExtra("cls_teacher", clas_teacher);
                                startActivity(teacherProfile);
                            }
                        } else if (position == 1) {
                            //Message
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherSendMessageActivity.this, TeacherMessageActivity.class);
                                teacherProfile.putExtra("clt_id", clt_id);
                                teacherProfile.putExtra("usl_id", usl_id);
                                teacherProfile.putExtra("School_name", school_name);
                                teacherProfile.putExtra("Teacher_name", teacher_name);
                                teacherProfile.putExtra("version_name", version_name);
                                teacherProfile.putExtra("board_name", board_name);
                                teacherProfile.putExtra("org_id", org_id);
                                teacherProfile.putExtra("academic_year", academic_year);
                                teacherProfile.putExtra("cls_teacher", clas_teacher);
                                teacherProfile.putExtra("Teacher_div",teacher_div);
                                teacherProfile.putExtra("Teacher_Shift",teacher_shift);
                                teacherProfile.putExtra("Tecaher_Std",teacher_std);
                                startActivity(teacherProfile);
                            }
                        } else if (position == 2) {
                            //SMS
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherSendMessageActivity.this, TeacherSMSActivity.class);
                                teacherProfile.putExtra("clt_id", clt_id);
                                teacherProfile.putExtra("usl_id", usl_id);
                                teacherProfile.putExtra("School_name", school_name);
                                teacherProfile.putExtra("Teacher_name", teacher_name);
                                teacherProfile.putExtra("version_name", version_name);
                                teacherProfile.putExtra("board_name", board_name);
                                teacherProfile.putExtra("org_id", org_id);
                                teacherProfile.putExtra("Teacher_div",teacher_div);
                                teacherProfile.putExtra("Teacher_Shift",teacher_shift);
                                teacherProfile.putExtra("Tecaher_Std",teacher_std);
                                teacherProfile.putExtra("academic_year", academic_year);
                                teacherProfile.putExtra("cls_teacher", clas_teacher);
                                startActivity(teacherProfile);
                            }
                        } else if (position == 3) {
                            //announcement
                            if (dft.isInternetOn() == true) {
                                Intent teacherannouncment = new Intent(TeacherSendMessageActivity.this, TeacherAnnouncementsActivity.class);
                                teacherannouncment.putExtra("clt_id", clt_id);
                                teacherannouncment.putExtra("usl_id", usl_id);
                                teacherannouncment.putExtra("School_name", school_name);
                                teacherannouncment.putExtra("Teacher_name", teacher_name);
                                teacherannouncment.putExtra("version_name", version_name);
                                teacherannouncment.putExtra("board_name", board_name);
                                teacherannouncment.putExtra("org_id", org_id);
                                teacherannouncment.putExtra("Teacher_div",teacher_div);
                                teacherannouncment.putExtra("Teacher_Shift",teacher_shift);
                                teacherannouncment.putExtra("Tecaher_Std",teacher_std);
                                teacherannouncment.putExtra("academic_year", academic_year);
                                teacherannouncment.putExtra("cls_teacher", clas_teacher);
                                startActivity(teacherannouncment);
                            }
                        }
                        else if (position == 4) {
                            //Sbject List
                            if (dft.isInternetOn() == true) {
                                Intent subject_list = new Intent(TeacherSendMessageActivity.this, SubjectListActivity.class);
                                subject_list.putExtra("usl_id", usl_id);
                                subject_list.putExtra("version_name", version_name);
                                subject_list.putExtra("clt_id", clt_id);
                                subject_list.putExtra("School_name", school_name);
                                subject_list.putExtra("Teacher_name", teacher_name);
                                subject_list.putExtra("board_name", board_name);
                                subject_list.putExtra("academic_year", academic_year);
                                subject_list.putExtra("cls_teacher", clas_teacher);
                                subject_list.putExtra("org_id", org_id);
                                subject_list.putExtra("Teacher_div",teacher_div);
                                subject_list.putExtra("Teacher_Shift",teacher_shift);
                                subject_list.putExtra("Tecaher_Std",teacher_std);
                                startActivity(subject_list);
                            }
                        } else if (position == 5) {
                            Intent setting = new Intent(TeacherSendMessageActivity.this, SettingActivity.class);
                            setting.putExtra("usl_id", usl_id);
                            setting.putExtra("version_name", version_name);
                            setting.putExtra("clt_id", clt_id);
                            setting.putExtra("School_name", school_name);
                            setting.putExtra("Teacher_name", teacher_name);
                            setting.putExtra("board_name", board_name);
                            setting.putExtra("academic_year", academic_year);
                            setting.putExtra("cls_teacher", clas_teacher);
                            setting.putExtra("org_id", org_id);
                            setting.putExtra("Teacher_div",teacher_div);
                            setting.putExtra("Teacher_Shift",teacher_shift);
                            setting.putExtra("Tecaher_Std",teacher_std);
                            startActivity(setting);
                        } else if (position == 6) {
                            //Signout
                            Intent signout = new Intent(TeacherSendMessageActivity.this, LoginActivity.class);
                            Constant.SetLoginData("", "", TeacherSendMessageActivity.this);
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancel(notificationID);
                            AppController.iconCount = 0;
                            AppController.iconCountOnResume = 0;
                            AppController.iconAnnouncementCount = 0;
                            AppController.iconAnnouncementCountOnResume = 0;
                            AppController.OnBackpressed = "false";
                            AppController.loginButtonClicked = "false";
                            AppController.announcementActivity = "false";
                            startActivity(signout);
                        } else if (position == 7) {
                            try {
                                final Dialog alertDialog = new Dialog(TeacherSendMessageActivity.this);
                                LayoutInflater inflater = getLayoutInflater();
                                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                alertDialog.getWindow().setBackgroundDrawable(
                                        new ColorDrawable(Color.TRANSPARENT));
                                View convertView = inflater.inflate(R.layout.about_us, null);
                                alertDialog.setContentView(convertView);
                                tv_version_name = (TextView) convertView.findViewById(R.id.tv_version_name);
                                tv_version_code = (TextView) convertView.findViewById(R.id.tv_version_code);
                                tv_cancel = (TextView) convertView.findViewById(R.id.tv_cancel);
                                tv_version_name.setText("Version Name: " + AppController.versionName);
                                tv_version_code.setText("Version Code: " + AppController.versionCode);
                                alertDialog.show();

                                tv_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
            }
            Constant.setTeacherDrawerData(academic_year, teacher_name, teacher_div, teacher_std, teacher_shift, TeacherSendMessageActivity.this);
            setDrawerData();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setDrawerData() {
        tv_academic_year_drawer.setText(academic_year);
        tv_child_name_drawer.setText(teacher_name);
        if((LoginActivity.get_class_teacher(TeacherSendMessageActivity.this).equalsIgnoreCase("1")))
        {
            tv_teacher_class.setText(teacher_shift + " | " + teacher_std + " | " + teacher_div);
        }
        else{
            tv_teacher_class.setVisibility(View.GONE);
        }
    }

    private void CheckCharachterLength() {
        enter_sms.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int a, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                Check_SMS_Length(enter_sms);
            }
        });
    }

    public void Check_SMS_Length(EditText ed_description) throws NumberFormatException
    {
        try
        {
            if (ed_description.getText().toString().length() <= 0)
            {
                tv_charachter_length.setText("0/132");
            }
            else
            {
                int valid_len = ed_description.getText().toString().length();
                tv_charachter_length.setText(String.valueOf(valid_len) + "/" + 132);
            }
        }
        catch (Exception e)
        {
            Log.d("<<error", "" + e);
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
        class_id = intent.getStringExtra("class_id");
        stud_id = intent.getStringExtra("stud_id");
        org_id = intent.getStringExtra("org_id");
        teacher_div = intent.getStringExtra("Teacher_div");
        teacher_std = intent.getStringExtra("Tecaher_Std");
        teacher_shift = intent.getStringExtra("Teacher_Shift");
        clas_teacher = intent.getStringExtra("cls_teacher");
        academic_year = intent.getStringExtra("academic_year");
        Log.d("<<stud id intent",stud_id);
        AppController.versionName = version_name;
    }
    private void init() {
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        progressDialog = Constant.getProgressDialog(this);
        btn_send_message.setOnClickListener(this);
        btn_attachment.setOnClickListener(this);
        img_drawer.setOnClickListener(this);
        lay_back_investment.setOnClickListener(this);
        header = new HeaderControler(this, true, false, "Send Message");
    }
    private void findViews() {
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
        tv_child_name_drawer = (TextView) findViewById(R.id.tv_child_name_drawer);
        tv_academic_year_drawer = (TextView) findViewById(R.id.tv_academic_year_drawer);
        tv_charachter_length = (TextView) findViewById(R.id.tv_charachter_length);
        tv_teacher_class = (TextView) findViewById(R.id.tv_teacher_class);
        tv_error_msg = (TextView) findViewById(R.id.tv_error_msg);
        btn_send_message = (Button) findViewById(R.id.btn_send_message);
        btn_attachment = (Button) findViewById(R.id.btn_attachment);
        ed_message_subject = (EditText) findViewById(R.id.ed_message_subject);
        enter_sms = (EditText) findViewById(R.id.enter_sms);

        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
        img_drawer = (ImageView) findViewById(R.id.img_drawer);
        // for drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        leftfgf_drawer = (RelativeLayout) findViewById(R.id.leftfgf_drawer);
        rl_profile = (RelativeLayout) findViewById(R.id.rl_profile);
        drawerListView = (ListView) findViewById(R.id.drawerListView);
        mContentDisplaceToggle = new ContentDisplaceDrawerToggle(this, drawer,
                R.id.rl_profile, Gravity.START);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
       // int width = dm.widthPixels;
        int width = getResources().getDisplayMetrics().widthPixels/2+30;
        DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) leftfgf_drawer.getLayoutParams();
        lp.width = width + 30;
        leftfgf_drawer.setLayoutParams(lp);
    }

    @Override
    public void onClick(View v) {
        try {
            if (v == btn_send_message) {
                message_subject = ed_message_subject.getText().toString();
                message_content = enter_sms.getText().toString();

                if (message_subject.equalsIgnoreCase("")) {
                    tv_error_msg.setText("Please Enter Message Subject");
                } else if (message_content.equalsIgnoreCase("")) {
                    tv_error_msg.setText("Please Enter Message");
                } else {
                    if(attachedfilename.equalsIgnoreCase("")){
                        attachedfilename = "0";
                    }
                    if(filePath.equalsIgnoreCase("")){
                        filePath = "0";
                    }
                    //Call webservice to send Message
                    callWsToSendMessage(clt_id, class_id, message_subject, message_content, usl_id, stud_id, toUslId, attachedfilename, filePath);
                }
            }else if(v==btn_attachment){
                if (getDeviceName().contains("Samsung")) {
                    Intent intent1 = new Intent("com.sec.android.app.myfiles.PICK_DATA");
                    intent1.putExtra("CONTENT_TYPE", "*/*");
                    intent1.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivityForResult(intent1, 1);
                } else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    Intent i = Intent.createChooser(intent, "View Default File Manager");
                    startActivityForResult(i, 1);

                }
            }
            else if (v == lay_back_investment) {
                Intent i = new Intent(TeacherSendMessageActivity.this, TeacherStudentListForMessage.class);
                i.putExtra("clt_id", clt_id);
                i.putExtra("usl_id", usl_id);
                i.putExtra("School_name", school_name);
                i.putExtra("Teacher_name", teacher_name);
                i.putExtra("version_name", version_name);
                i.putExtra("board_name", board_name);
                i.putExtra("class_id", class_id);
                i.putExtra("cls_teacher", clas_teacher);
                i.putExtra("academic_year", academic_year);
                i.putExtra("org_id", org_id);
                i.putExtra("Teacher_div",teacher_div);
                i.putExtra("Teacher_Shift",teacher_shift);
                i.putExtra("Tecaher_Std", teacher_std);
                startActivity(i);
            } else if (v == img_drawer) {
                drawer.openDrawer(Gravity.RIGHT);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Fix no activity available
        if (data == null)
            return;
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    try {
                        FilePath = data.getData().getPath();
                        //FilePath is your file as a string
                        Log.d("<<FilePath", FilePath);
                        File file = new File(FilePath);
                        attachedfilename = file.getName();
                        Log.d("<<NAME", attachedfilename);
                        length = file.length();
                        length = length / 1024;
                        Log.d("<< File size : ", length + " KB");
                        if(!attachedfilename.contains(".")){
                            Toast.makeText(TeacherSendMessageActivity.this,"Not valid file",Toast.LENGTH_LONG).show();
                        }
                        else {
                            extension = attachedfilename.substring(attachedfilename.lastIndexOf("."));
                            Log.d("<<extension", extension);

                            int pos = attachedfilename.lastIndexOf(".");
                            if (pos > 0) {
                                filename = attachedfilename.substring(0, pos);
                                Log.d("<<ONLY NAME", filename);
                            }
                        }
                        //Upload File
                        if (length > 500) {
                            Toast.makeText(TeacherSendMessageActivity.this, "File size is greater than 500KB", Toast.LENGTH_LONG).show();
                        } else if (!(extension.equalsIgnoreCase(".pdf") || extension.equalsIgnoreCase(".docx") || extension.equalsIgnoreCase(".doc"))) {
                            Toast.makeText(TeacherSendMessageActivity.this, "Upload only Word or PDF Document", Toast.LENGTH_LONG).show();
                        } else {
                           new TheTask().execute("http://www.betweenus.in/PODARAPP/PodarApp.svc/UploadAdminMsgAttachment");

                        }

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }

        }

    }

    class TheTask extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // update textview here
            //  textView.setText("Server message is "+result);
            progressDialog.dismiss();
          /*  Constant.showOkPopup(TeacherSendMessageActivity.this, responseStatus, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }

            });*/
            Toast.makeText(TeacherSendMessageActivity.this,responseStatus,Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            HttpParams param = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(param, 15000);
            HttpConnectionParams.setSoTimeout(param, 20000);
            HttpClient httpclient = new DefaultHttpClient(param);
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpParams httpParameters = httpclient.getParams();
            HttpConnectionParams.setTcpNoDelay(httpParameters, true);
            httpclient.getParams().setParameter("\"http.socket.timeout\"", new Integer(90000));
            // HttpClient httpclient = new DefaultHttpClient();
            // 123 HttpPost httppost = new HttpPost("http://www.betweenus.in/PODARAPP/PodarApp.svc/UploadAdminMsgAttachment");
         HttpPost httppost = new HttpPost("http://www.betweenus.in/PODARAPP/PodarApp.svc/UploadAdminMsgAttachment");
            // Add Headers
            httppost.addHeader("Filename", filename);
            httppost.addHeader("usl_id", usl_id);
            httppost.addHeader("clt_id", clt_id);
            httppost.addHeader("extension", extension);
            try {
                FileEntity entity = new FileEntity(new File(FilePath), "application/octet-stream");
                entity.setChunked(true);
                httppost.setEntity(entity);
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                Log.d("<<response new",String.valueOf(response));
                HttpEntity responseEntity = response.getEntity();
                if(responseEntity!=null)
                {
                    String abc  = EntityUtils.toString(responseEntity);
                    Log.d("<<RES",abc);
                    try {
                        jsonResponse = new JSONObject(abc);
                        Log.d("<<json rres", String.valueOf(jsonResponse));
                        filePath = jsonResponse.getString("Filepath");
                        responseStatus = jsonResponse.getString("StatusMsg");
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
            return "network";
        }

    }
    private void callWsToSendMessage(final String clt_id, final String class_id,String message_subject,String message_content, final String usl_id,String stud_id,String toUslId,String filename,String filepath) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getSendTeacherMessage(clt_id, class_id, message_subject, message_content, usl_id, stud_id, toUslId, filename, filepath, sendMessage_Method_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("<<teachersendmessage1", response.toString());
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            Log.d("<<teachersendmessage2", login_details.toString());
                            if (login_details.Status.equalsIgnoreCase("1")) {

                                tv_error_msg.setText("Message Sent Successfully");
                                ed_message_subject.setText("");
                                tv_error_msg.setText("");
                                AppController.sentMsgsTab = "true";
                                Intent messageactivity = new Intent(TeacherSendMessageActivity.this,TeacherMessageActivity.class);
                                messageactivity.putExtra("clt_id", clt_id);
                                messageactivity.putExtra("usl_id",usl_id);
                                messageactivity.putExtra("School_name", school_name);
                                messageactivity.putExtra("Teacher_name", teacher_name);
                                messageactivity.putExtra("version_name", version_name);
                                messageactivity.putExtra("board_name",board_name);
                                messageactivity.putExtra("class_id",class_id);
                                messageactivity.putExtra("cls_teacher", clas_teacher);
                                messageactivity.putExtra("academic_year", academic_year);
                                messageactivity.putExtra("org_id",org_id);
                                messageactivity.putExtra("Teacher_div",teacher_div);
                                messageactivity.putExtra("Teacher_Shift",teacher_shift);
                                messageactivity.putExtra("Tecaher_Std",teacher_std);
                                messageactivity.putExtra("sentMsgTab",AppController.sentMsgsTab);
                                startActivity(messageactivity);

                            } else if (login_details.Status.equalsIgnoreCase("0")) {

                                tv_error_msg.setText("Message Not Sent!!");
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

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        try {
            Intent i = new Intent(TeacherSendMessageActivity.this, TeacherStudentListForMessage.class);
            i.putExtra("clt_id", clt_id);
            i.putExtra("usl_id", usl_id);
            i.putExtra("School_name", school_name);
            i.putExtra("Teacher_name", teacher_name);
            i.putExtra("version_name", version_name);
            i.putExtra("board_name", board_name);
            i.putExtra("class_id", class_id);
            i.putExtra("cls_teacher", clas_teacher);
            i.putExtra("academic_year", academic_year);
            i.putExtra("org_id", org_id);
            i.putExtra("Teacher_div",teacher_div);
            i.putExtra("Teacher_Shift",teacher_shift);
            i.putExtra("Tecaher_Std",teacher_std);
            startActivity(i);
            finish();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
