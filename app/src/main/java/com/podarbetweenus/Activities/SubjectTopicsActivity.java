package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.Adapter.ExpandableTopicListAdapter;
import com.podarbetweenus.Adapter.ExpandableTopicsAdapter;
import com.podarbetweenus.Adapter.ResourceAdapter;
import com.podarbetweenus.Adapter.TeacherTopicListAdapter;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gayatri on 3/3/2016.
 */
public class SubjectTopicsActivity extends Activity implements View.OnClickListener{
    //UI Variables
    //EditTExt
    EditText ed_cycle_test;
    //ListView
    ListView list_resource;
    ExpandableListView lv_subject_topiclist;
    //TextView
    TextView tv_no_record,tv_cancel,tv_version_code,tv_version_name,tv_child_name_drawer,tv_academic_year_drawer,tv_std_subj,tv_teacher_class;
    //DrawerLayout
    DrawerLayout drawer;
    //Linear Layout
    LinearLayout lay_back_investment;
    //Relative Layout
    RelativeLayout leftfgf_drawer,rl_profile;
    //ListView
    ListView drawerListView;
    //ImageView
    ImageView img_drawer,img_close;

    String usl_id,clt_id,board_name,school_name,teacher_name,version_name,firstTypetopic="",firstcycle_test,firstTimeCycle="",cycleTest_id,
            subject_name,classs,noResultFound="false",class_id,std,firstTimeCycletestSelection="true",crf_id,crl_file,topic_name,org_id,clas_teacher,academic_year,teacher_div,teacher_std,teacher_shift;
    String cycleTestMethod_name = "CycleTestDropDown";
    String TopicsMethodName = "GetTeacherTopicList";
    String resourcesMethodName = "GetTeacherResourceList";
    ArrayList<String> strings_cycleTest;
    String[] select_cycleTest;
    int notificationID = 1;

    List<String> groupList;
    Map<String, List<String>> topicCollection;
    List<String> childList;
    String[] data_without_sibling;
    int[] icons_without_sibling;

    LoginDetails login_details;
    DataFetchService dft;
    TeacherTopicListAdapter teacherTopicListAdapter;
    ResourceAdapter resourceAdapter;
    //LayoutEntities
    HeaderControler header;
    //ProgressDialog
    ProgressDialog progressDialog;
    ContentDisplaceDrawerToggle mContentDisplaceToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.subject_topicslist);
        findViews();
        init();
        getIntentData();
        setDrawer();
        lv_subject_topiclist.setDivider(null);
        lv_subject_topiclist.setDividerHeight(0);
        firstTimeCycle = "false";
        try {
            //Call Webservice for cycleTEst
            CallCycleTestWebservice(board_name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        AppController.CycleTest = "false";
        lv_subject_topiclist.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    lv_subject_topiclist.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });
    }
    private void setDrawer() {
        try {
            if(LoginActivity.get_class_teacher(SubjectTopicsActivity.this).equalsIgnoreCase("1")) {
                icons_without_sibling = new int[]{R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sms_48x48, R.drawable.announcementicon_48x48, R.drawable.att_48x48, R.drawable.behaviouricon_48x48, R.drawable.subjectlist_48x48, R.drawable.setting1_hdpi, R.drawable.logout1_hdpi, R.drawable.info};
                data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement", "Attendance", "Behaviour", "Subject List", "Setting", "Sign Out", "About"};
                drawerListView.setAdapter(new CustomAccount(SubjectTopicsActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));
                drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(SubjectTopicsActivity.this, TeacherProfileActivity.class);
                                teacherProfile.putExtra("clt_id", clt_id);
                                teacherProfile.putExtra("usl_id", usl_id);
                                teacherProfile.putExtra("School_name", school_name);
                                teacherProfile.putExtra("Teacher_name", teacher_name);
                                teacherProfile.putExtra("org_id", org_id);
                                teacherProfile.putExtra("cls_teacher", clas_teacher);
                                teacherProfile.putExtra("academic_year", academic_year);
                                teacherProfile.putExtra("version_name", AppController.versionName);
                                teacherProfile.putExtra("board_name", board_name);
                                teacherProfile.putExtra("Teacher_div",teacher_div);
                                teacherProfile.putExtra("Teacher_Shift",teacher_shift);
                                teacherProfile.putExtra("Tecaher_Std",teacher_std);
                                startActivity(teacherProfile);
                            }
                        } else if (position == 1) {
                            //Message
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(SubjectTopicsActivity.this, TeacherMessageActivity.class);
                                teacherProfile.putExtra("clt_id", clt_id);
                                teacherProfile.putExtra("usl_id", usl_id);
                                teacherProfile.putExtra("School_name", school_name);
                                teacherProfile.putExtra("Teacher_name", teacher_name);
                                teacherProfile.putExtra("org_id", org_id);
                                teacherProfile.putExtra("cls_teacher", clas_teacher);
                                teacherProfile.putExtra("academic_year", academic_year);
                                teacherProfile.putExtra("version_name", AppController.versionName);
                                teacherProfile.putExtra("board_name", board_name);
                                teacherProfile.putExtra("Teacher_div",teacher_div);
                                teacherProfile.putExtra("Teacher_Shift",teacher_shift);
                                teacherProfile.putExtra("Tecaher_Std",teacher_std);
                                startActivity(teacherProfile);
                            }
                        } else if (position == 2) {
                            //SMS
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(SubjectTopicsActivity.this, TeacherSMSActivity.class);
                                teacherProfile.putExtra("clt_id", clt_id);
                                teacherProfile.putExtra("usl_id", usl_id);
                                teacherProfile.putExtra("School_name", school_name);
                                teacherProfile.putExtra("Teacher_name", teacher_name);
                                teacherProfile.putExtra("org_id", org_id);
                                teacherProfile.putExtra("cls_teacher", clas_teacher);
                                teacherProfile.putExtra("academic_year", academic_year);
                                teacherProfile.putExtra("version_name", AppController.versionName);
                                teacherProfile.putExtra("board_name", board_name);
                                teacherProfile.putExtra("Teacher_div",teacher_div);
                                teacherProfile.putExtra("Teacher_Shift", teacher_shift);
                                teacherProfile.putExtra("Tecaher_Std",teacher_std);
                                startActivity(teacherProfile);
                            }
                        } else if (position == 3) {
                            //announcement
                            if (dft.isInternetOn() == true) {
                                Intent teacherannouncment = new Intent(SubjectTopicsActivity.this, TeacherAnnouncementsActivity.class);
                                teacherannouncment.putExtra("clt_id", clt_id);
                                teacherannouncment.putExtra("usl_id", usl_id);
                                teacherannouncment.putExtra("School_name", school_name);
                                teacherannouncment.putExtra("Teacher_name", teacher_name);
                                teacherannouncment.putExtra("version_name", AppController.versionName);
                                teacherannouncment.putExtra("board_name", board_name);
                                teacherannouncment.putExtra("cls_teacher", clas_teacher);
                                teacherannouncment.putExtra("academic_year", academic_year);
                                teacherannouncment.putExtra("org_id", org_id);
                                teacherannouncment.putExtra("Teacher_div",teacher_div);
                                teacherannouncment.putExtra("Teacher_Shift",teacher_shift);
                                teacherannouncment.putExtra("Tecaher_Std",teacher_std);
                                startActivity(teacherannouncment);
                            }
                        } else if (position == 4) {
                            //Attendance
                            if (dft.isInternetOn() == true) {
                                Intent teacherattendance = new Intent(SubjectTopicsActivity.this, TeacherAttendanceActivity.class);
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
                                Intent teacher_behaviour = new Intent(SubjectTopicsActivity.this, TeacherBehaviourActivity.class);
                                teacher_behaviour.putExtra("clt_id", clt_id);
                                teacher_behaviour.putExtra("usl_id", usl_id);
                                teacher_behaviour.putExtra("School_name", school_name);
                                teacher_behaviour.putExtra("Teacher_name", teacher_name);
                                teacher_behaviour.putExtra("version_name", AppController.versionName);
                                teacher_behaviour.putExtra("board_name", board_name);
                                teacher_behaviour.putExtra("org_id", org_id);
                                teacher_behaviour.putExtra("cls_teacher", clas_teacher);
                                teacher_behaviour.putExtra("academic_year", academic_year);
                                teacher_behaviour.putExtra("Teacher_div",teacher_div);
                                teacher_behaviour.putExtra("Teacher_Shift",teacher_shift);
                                teacher_behaviour.putExtra("Tecaher_Std",teacher_std);
                                startActivity(teacher_behaviour);
                            }
                        } else if (position == 6) {
                            //Sbject List
                            if (dft.isInternetOn() == true) {
                                Intent subject_list = new Intent(SubjectTopicsActivity.this, SubjectListActivity.class);
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
                            Intent setting = new Intent(SubjectTopicsActivity.this, SettingActivity.class);
                            setting.putExtra("usl_id", usl_id);
                            setting.putExtra("version_name", AppController.versionName);
                            setting.putExtra("clt_id", clt_id);
                            setting.putExtra("School_name", school_name);
                            setting.putExtra("Teacher_name", teacher_name);
                            setting.putExtra("board_name", board_name);
                            setting.putExtra("org_id", org_id);
                            setting.putExtra("cls_teacher", clas_teacher);
                            setting.putExtra("Teacher_div",teacher_div);
                            setting.putExtra("Teacher_Shift",teacher_shift);
                            setting.putExtra("Tecaher_Std",teacher_std);
                            setting.putExtra("academic_year", academic_year);
                            startActivity(setting);
                        } else if (position == 8) {
                            //Signout
                            Intent signout = new Intent(SubjectTopicsActivity.this, LoginActivity.class);
                            Constant.SetLoginData("", "", SubjectTopicsActivity.this);
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancel(notificationID);
                            AppController.iconCount = 0;
                            AppController.iconCountOnResume = 0;
                            AppController.OnBackpressed = "false";
                            AppController.loginButtonClicked = "false";
                            AppController.drawerSignOut = "true";
                            startActivity(signout);
                        } else if (position == 9) {
                            try {
                                final Dialog alertDialog = new Dialog(SubjectTopicsActivity.this);
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
            else {
                icons_without_sibling = new int[]{R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sms_48x48, R.drawable.announcementicon_48x48, R.drawable.subjectlist_48x48, R.drawable.setting1_hdpi, R.drawable.logout1_hdpi, R.drawable.info};
                data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement", "Subject List", "Setting", "Sign Out", "About"};
                drawerListView.setAdapter(new CustomAccount(SubjectTopicsActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));
                drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(SubjectTopicsActivity.this, TeacherProfileActivity.class);
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
                        } else if (position == 1) {
                            //Message
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(SubjectTopicsActivity.this, TeacherMessageActivity.class);
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
                        } else if (position == 2) {
                            //SMS
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(SubjectTopicsActivity.this, TeacherSMSActivity.class);
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
                                Intent teacherannouncment = new Intent(SubjectTopicsActivity.this, TeacherAnnouncementsActivity.class);
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
                        } else if (position == 4) {
                            //Sbject List
                            if (dft.isInternetOn() == true) {
                                Intent subject_list = new Intent(SubjectTopicsActivity.this, SubjectListActivity.class);
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
                            Intent setting = new Intent(SubjectTopicsActivity.this, SettingActivity.class);
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
                            Intent signout = new Intent(SubjectTopicsActivity.this, LoginActivity.class);
                            Constant.SetLoginData("", "", SubjectTopicsActivity.this);
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancel(notificationID);
                            AppController.iconCount = 0;
                            AppController.iconCountOnResume = 0;
                            AppController.OnBackpressed = "false";
                            AppController.loginButtonClicked = "false";
                            AppController.drawerSignOut = "true";
                            startActivity(signout);
                        } else if (position == 7) {
                            try {
                                final Dialog alertDialog = new Dialog(SubjectTopicsActivity.this);
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
            Constant.setTeacherDrawerData(academic_year, teacher_name,teacher_div,teacher_std,teacher_shift, SubjectTopicsActivity.this);
            setDrawerData();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setDrawerData() {
        tv_academic_year_drawer.setText(academic_year);
        tv_child_name_drawer.setText(teacher_name);
        if((LoginActivity.get_class_teacher(SubjectTopicsActivity.this).equalsIgnoreCase("1")))
        {
            tv_teacher_class.setText(teacher_shift + " | " + teacher_std + " | " + teacher_div);
        }
        else{
            tv_teacher_class.setVisibility(View.GONE);
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
            subject_name = intent.getStringExtra("Subject_name");
            classs = intent.getStringExtra("Classs");
            org_id = intent.getStringExtra("org_id");
            teacher_div = intent.getStringExtra("Teacher_div");
            teacher_std = intent.getStringExtra("Tecaher_Std");
            teacher_shift = intent.getStringExtra("Teacher_Shift");
            class_id = intent.getStringExtra("class_id");
            clas_teacher = intent.getStringExtra("cls_teacher");
            academic_year = intent.getStringExtra("academic_year");
            AppController.versionName = version_name;
            try {
                String[] val = classs.split("-");
                std = val[0];
            }
            catch (Exception e){
                e.printStackTrace();
            }
            header = new HeaderControler(this, true, false,std+" >> "+subject_name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        progressDialog = Constant.getProgressDialog(this);
        ed_cycle_test.setOnClickListener(this);
        img_drawer.setOnClickListener(this);
        lay_back_investment.setOnClickListener(this);
    }

    private void findViews() {
        ed_cycle_test = (EditText) findViewById(R.id.ed_cycle_test);
      //  lv_subject_topiclist = (ListView) findViewById(R.id.lv_subject_topiclist);
        lv_subject_topiclist = (ExpandableListView) findViewById(R.id.lv_subject_topiclist);
        list_resource = (ListView) findViewById(R.id.list_resource);
        tv_no_record = (TextView) findViewById(R.id.tv_no_record);
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
        //ImageView
        img_drawer = (ImageView) findViewById(R.id.img_drawer);
        img_close = (ImageView) findViewById(R.id.img_close);
        //TextView
        tv_version_code = (TextView) findViewById(R.id.tv_version_code);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_no_record = (TextView) findViewById(R.id.tv_no_record);
        tv_std_subj = (TextView) findViewById(R.id.tv_std_subj);
        tv_child_name_drawer = (TextView) findViewById(R.id.tv_child_name_drawer);
        tv_academic_year_drawer = (TextView) findViewById(R.id.tv_academic_year_drawer);
        tv_teacher_class = (TextView) findViewById(R.id.tv_teacher_class);
        // for drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        leftfgf_drawer = (RelativeLayout) findViewById(R.id.leftfgf_drawer);
        rl_profile = (RelativeLayout) findViewById(R.id.rl_profile);
        drawerListView = (ListView) findViewById(R.id.drawerListView);
        mContentDisplaceToggle = new ContentDisplaceDrawerToggle(this, drawer,
                R.id.rl_profile, Gravity.START);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
       // int width3 = dm.widthPixels;
        int width = getResources().getDisplayMetrics().widthPixels/2+30;
        DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) leftfgf_drawer.getLayoutParams();
        lp.width = width + 10;
        leftfgf_drawer.setLayoutParams(lp);

    }
    private void  CallCycleTestWebservice(final String board_name) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getcycleTestdeopdown(board_name, cycleTestMethod_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_details.Status.equalsIgnoreCase("1")) {
                                tv_no_record.setVisibility(View.GONE);
                                firstTypetopic = "true";
                                strings_cycleTest = new ArrayList<String>();
                                try {
                                    for (int i = 0; i < login_details.CycleTest.size(); i++) {
                                        strings_cycleTest.add(login_details.CycleTest.get(i).cyc_name.toString());
                                        select_cycleTest = new String[strings_cycleTest.size()];
                                        select_cycleTest = strings_cycleTest.toArray(select_cycleTest);
                                        firstcycle_test = login_details.CycleTest.get(0).cyc_name;
                                        AppController.CycleTestID = firstcycle_test;
                                        if(firstTimeCycle.equalsIgnoreCase("false")) {
                                            ed_cycle_test.setText(firstcycle_test);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                selectCycleTest(select_cycleTest);
                                AppController.CycleTest = "false";
                                // Call Webservice For Subject
                                if(firstTimeCycletestSelection.equalsIgnoreCase("true")) {
                                    CallTopicListWebservice(class_id,board_name,std,subject_name,firstcycle_test,clt_id,usl_id);
                                }
                            } else if (login_details.Status.equalsIgnoreCase("0")) {
                                lv_subject_topiclist.setVisibility(View.GONE);
                                tv_no_record.setVisibility(View.VISIBLE);
                                tv_no_record.setText("No Data Found");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Show error or whatever...
                        Log.d("SubjectTopicActivity", "ERROR.._---" + error.getCause());


                    }
                });

    }
    private void selectCycleTest( final String[] select_cycleTest) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.dialog_list, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Select Cycle Test");
        firstTimeCycle = "true";
        final ListView select_list = (ListView) convertView.findViewById(R.id.select_list);

        alertDialog.setItems(select_cycleTest, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                ed_cycle_test.setText(select_cycleTest[item]);
                dialog.dismiss();
                String selectedcycleTest = select_cycleTest[item];
                cycleTest_id = selectedcycleTest;
                AppController.CycleTestID = cycleTest_id;
                try {
                    //Webservcie call for Sent Messages
                    CallTopicListWebservice(class_id, board_name, std, subject_name, AppController.CycleTestID, clt_id, usl_id);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        if (AppController.CycleTest.equalsIgnoreCase("true")) {
            alertDialog.show();
        }
    }
    private void CallTopicListWebservice(String class_id,String board_name,String std,String subject_name,String cycleTest,String clt_id,String usl_id) {


        dft.getteacherTopicsList(class_id, board_name, std, subject_name, cycleTest, clt_id, usl_id, TopicsMethodName, Request.Method.POST,
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
                            lv_subject_topiclist.setVisibility(View.VISIBLE);
                            setUIData();
                            AppController.TopicsListsize = login_details.TopicList;

                        } else if (login_details.Status.equalsIgnoreCase("0")) {
                            lv_subject_topiclist.setVisibility(View.GONE);
                            tv_no_record.setVisibility(View.VISIBLE);
                            noResultFound = "true";
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
                        Log.d("SubjectTopicsActivity", "ERROR.._---" + error.getCause());
                    }
                });

    }

    private void setUIData() {
       createGroupList();
       createCollection();
       ExpandableTopicListAdapter expandableTopicsAdapter = new ExpandableTopicListAdapter(SubjectTopicsActivity.this,groupList,topicCollection,login_details.TopicList,usl_id,clt_id,school_name,teacher_name,org_id,clas_teacher,academic_year,board_name,teacher_div,teacher_shift,teacher_std,subject_name,classs,class_id);
        lv_subject_topiclist.setAdapter(expandableTopicsAdapter);
    }

    private void createCollection() {
        String[] hpModels = { "HP Pavilion G6-2014TX" };
        String[] hclModels = { "HCL S2101" };
        String[] lenovoModels = { "IdeaPad Z Series"};
        String[] sonyModels = { "VAIO E Series" };
        String[] dellModels = { "Inspiron"};
        String[] samsungModels = { "NP Series"};
        topicCollection = new LinkedHashMap<String, List<String>>();

        for (String laptop : groupList) {
            if (laptop.equals("September")) {
                loadChild(hpModels);
            } else if (laptop.equals("August"))
                loadChild(dellModels);
            else if (laptop.equals("July"))
                loadChild(sonyModels);
            else if (laptop.equals("June"))
                loadChild(hclModels);
            else
                loadChild(lenovoModels);

            topicCollection.put(laptop, childList);
        }
    }

    private void loadChild(String[] laptopModels) {
        childList = new ArrayList<String>();
        for (String model : laptopModels)
            childList.add(model);
    }

    private void createGroupList() {
        groupList = new ArrayList<String>();
        for (int i = 0; i < login_details.TopicList.size(); i++) {
            topic_name = login_details.TopicList.get(i).crf_topicname;
            groupList.add(topic_name);
        }
    }

    private void setResourceIDData() {
        final Dialog alertDialog = new Dialog(this);
        //  final AlertDialog alertDialog = builder.create();
        LayoutInflater inflater = this.getLayoutInflater();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        View convertView = (View) inflater.inflate(R.layout.template_resource_list, null);
        list_resource = (ListView)convertView.findViewById(R.id.list_resource);
        img_close = (ImageView) convertView.findViewById(R.id.img_close);
        alertDialog.setContentView(convertView);
        resourceAdapter = new ResourceAdapter(this,login_details.ResourceList);
        list_resource.setAdapter(resourceAdapter);
        alertDialog.show();

        list_resource.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    //Open Google Drive File
                    crl_file = login_details.ResourceList.get(position).crl_file;
                    String file_name[] = crl_file.split(":");
                    String file_id = file_name[1];
                    String resourcefile_url = "https://drive.google.com/a/podar.org/file/d/"+file_id;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(resourcefile_url));
                    startActivity(i);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(AppController.drawerSignOut.equalsIgnoreCase("true")) {
            Intent intent = new Intent(getApplicationContext(), SplashScreen.class);

            AppController.ProfileWithoutSibling = "false";
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            AppController.Board_name = board_name;
            AppController.school_name = school_name;
            AppController.versionName = version_name;
            AppController.usl_id = usl_id;
            AppController.clt_id = clt_id;
            AppController.school_name = school_name;
            AppController.versionName = version_name;
            intent.putExtra("usl_id", AppController.usl_id);
            intent.putExtra("msd_ID", AppController.msd_ID);
            intent.putExtra("version_name", AppController.versionName);
            intent.putExtra("clt_id", AppController.clt_id);
            intent.putExtra("School_name", AppController.school_name);
            intent.putExtra("board_name", AppController.Board_name);
            intent.putExtra("academic_year", academic_year);
            intent.putExtra("cls_teacher", clas_teacher);
            intent.putExtra("Teacher_div",teacher_div);
            intent.putExtra("Teacher_Shift",teacher_shift);
            intent.putExtra("Tecaher_Std",teacher_std);
            intent.putExtra("Sibling", AppController.SiblingListSize);
            intent.putExtra("org_id",org_id);
            startActivity(intent);
        }
    }
    @Override
    public void onClick(View v) {
        if(v==img_drawer)
        {
            drawer.openDrawer(Gravity.RIGHT);
        }
        else if(v==ed_cycle_test){
            AppController.CycleTest="true";
            firstTimeCycle = "true";
            firstTimeCycletestSelection = "false";
            //Call Webservice for cycleTEst
            CallCycleTestWebservice(board_name);
        }
        else if(v==lay_back_investment)
        {
            Intent back = new Intent(SubjectTopicsActivity.this,TeacherProfileActivity.class);
            back.putExtra("clt_id", clt_id);
            back.putExtra("usl_id",usl_id);
            back.putExtra("School_name", school_name);
            back.putExtra("Teacher_name", teacher_name);
            back.putExtra("org_id",org_id);
            back.putExtra("academic_year", academic_year);
            back.putExtra("cls_teacher", clas_teacher);
            back.putExtra("version_name",AppController.versionName);
            back.putExtra("board_name",board_name);
            back.putExtra("Teacher_div",teacher_div);
            back.putExtra("Teacher_Shift",teacher_shift);
            back.putExtra("Tecaher_Std",teacher_std);
            startActivity(back);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            Intent back = new Intent(SubjectTopicsActivity.this,TeacherProfileActivity.class);
            back.putExtra("clt_id", clt_id);
            back.putExtra("usl_id",usl_id);
            back.putExtra("School_name", school_name);
            back.putExtra("Teacher_name", teacher_name);
            back.putExtra("org_id",org_id);
            back.putExtra("academic_year", academic_year);
            back.putExtra("cls_teacher", clas_teacher);
            back.putExtra("version_name",AppController.versionName);
            back.putExtra("board_name", board_name);
            back.putExtra("Teacher_div",teacher_div);
            back.putExtra("Teacher_Shift",teacher_shift);
            back.putExtra("Tecaher_Std",teacher_std);
            startActivity(back);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
