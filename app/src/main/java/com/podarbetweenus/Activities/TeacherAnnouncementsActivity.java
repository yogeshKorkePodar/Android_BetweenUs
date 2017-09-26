package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.Adapter.AnnouncementAdapter;
import com.podarbetweenus.Adapter.TeacherAnnouncementAdapter;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.AnnouncementResult;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Services.ListResultReceiver;
import com.podarbetweenus.Utility.AppController;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 1/18/2016.
 */
public class TeacherAnnouncementsActivity extends Activity implements View.OnClickListener,ListResultReceiver.Receiver,AdapterView.OnItemClickListener{
    //Button
    Button btn_announce,btn_dashboard,btn_attendance,btn_behaviour;
    //Linear Layout
    LinearLayout lay_back_investment,ll_footer;
    //ImageView
    ImageView btn_back,img_drawer;
    //TextView
    TextView tv_no_record,tv_version_name,tv_version_code,tv_cancel,tv_child_name_drawer,tv_academic_year_drawer,tv_teacher_class;
    //DrawerLayout
    DrawerLayout drawer;
    //Relative Layout
    RelativeLayout leftfgf_drawer,rl_profile;
    //ListView
    ListView drawerListView,list_teacherannouncement;
    //LayoutEntities
    HeaderControler header;
    ContentDisplaceDrawerToggle mContentDisplaceToggle;
    DataFetchService dft;
    LoginDetails login_details;
    TeacherAnnouncementAdapter teacherannouncement_adpater;
    AnnouncementAdapter announcement_adpater;
    AlarmReceiver alaram_receiver;
    //ProgressDialog
    ProgressDialog progressDialog;

    String usl_id,clt_id,school_name,teacher_name,version_name,board_name,org_id,clas_teacher,academic_year,teacher_div,teacher_std,teacher_shift,msg_id,announcement_usl_id;
    String TeacherAnnouncementMethodName = "GetTeacherAnnouncement";
    String AnnouncementReadMethodName = "UpdateAnnoucementReadStatus";
    String[] data_without_sibling;
    ArrayList<AnnouncementResult> announcementresults;
    public ArrayList<AnnouncementResult> announcement_result = new ArrayList<AnnouncementResult>();
    int[] icons_without_sibling;
    int notificationID = 1,viewannouncementListSize,notificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.teacher_announcement);
        findViews();
        init();
        getIntentData();
        idClassTeacher();
        setDrawer();
        registerReceiver();
        AppController.announcementActivity = "false";
        AppController.Notification_send = "true";
        try {
            //Call Webservice for Teacher Announcment
            CallTeacherAnnouncementWebservice(clt_id, usl_id);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void idClassTeacher() {
        if(LoginActivity.get_class_teacher(TeacherAnnouncementsActivity.this).equalsIgnoreCase("1")){
            btn_announce.setVisibility(View.VISIBLE);
            btn_attendance.setVisibility(View.VISIBLE);
            btn_behaviour.setVisibility(View.VISIBLE);
            ll_footer.setWeightSum(3.0f);
        }
        else{
            btn_announce.setVisibility(View.VISIBLE);
            btn_attendance.setVisibility(View.GONE);
            btn_behaviour.setVisibility(View.GONE);
            ll_footer.setWeightSum(1.0f);
        }
    }

    private void setDrawer() {
        try {
            if (LoginActivity.get_class_teacher(TeacherAnnouncementsActivity.this).equalsIgnoreCase("1")) {
                icons_without_sibling = new int[]{R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sms_48x48, R.drawable.announcementicon_48x48, R.drawable.att_48x48, R.drawable.behaviouricon_48x48,R.drawable.subjectlist_48x48, R.drawable.setting1_hdpi, R.drawable.logout1_hdpi, R.drawable.info};
                data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement", "Attendance", "Behaviour","Subject List","Setting", "Sign Out", "About"};
                drawerListView.setAdapter(new CustomAccount(TeacherAnnouncementsActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));
                drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            if (dft.isInternetOn() == true) {
                                Intent teacherdashboard = new Intent(TeacherAnnouncementsActivity.this, TeacherProfileActivity.class);
                                teacherdashboard.putExtra("clt_id", clt_id);
                                teacherdashboard.putExtra("usl_id", usl_id);
                                teacherdashboard.putExtra("School_name", school_name);
                                teacherdashboard.putExtra("Teacher_name", teacher_name);
                                teacherdashboard.putExtra("version_name", AppController.versionName);
                                teacherdashboard.putExtra("board_name", board_name);
                                teacherdashboard.putExtra("org_id", org_id);
                                teacherdashboard.putExtra("Teacher_div",teacher_div);
                                teacherdashboard.putExtra("Teacher_Shift",teacher_shift);
                                teacherdashboard.putExtra("Tecaher_Std",teacher_std);
                                teacherdashboard.putExtra("academic_year", academic_year);
                                teacherdashboard.putExtra("cls_teacher", clas_teacher);
                                startActivity(teacherdashboard);
                            }
                        } else if (position == 1) {
                            //Message
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherAnnouncementsActivity.this, TeacherMessageActivity.class);
                                teacherProfile.putExtra("clt_id", clt_id);
                                teacherProfile.putExtra("usl_id", usl_id);
                                teacherProfile.putExtra("School_name", school_name);
                                teacherProfile.putExtra("Teacher_name", teacher_name);
                                teacherProfile.putExtra("version_name", AppController.versionName);
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
                                Intent teacherProfile = new Intent(TeacherAnnouncementsActivity.this, TeacherSMSActivity.class);
                                teacherProfile.putExtra("clt_id", clt_id);
                                teacherProfile.putExtra("usl_id", usl_id);
                                teacherProfile.putExtra("School_name", school_name);
                                teacherProfile.putExtra("Teacher_name", teacher_name);
                                teacherProfile.putExtra("version_name", AppController.versionName);
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
                            //Announcment
                            if (dft.isInternetOn() == true) {
                                Intent teacherannouncment = new Intent(TeacherAnnouncementsActivity.this, TeacherAnnouncementsActivity.class);
                                teacherannouncment.putExtra("clt_id", clt_id);
                                teacherannouncment.putExtra("usl_id", usl_id);
                                teacherannouncment.putExtra("School_name", school_name);
                                teacherannouncment.putExtra("Teacher_name", teacher_name);
                                teacherannouncment.putExtra("version_name", AppController.versionName);
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
                            //Attendance
                            if (dft.isInternetOn() == true) {
                                Intent teacherattendance = new Intent(TeacherAnnouncementsActivity.this, TeacherAttendanceActivity.class);
                                teacherattendance.putExtra("clt_id", clt_id);
                                teacherattendance.putExtra("usl_id", usl_id);
                                teacherattendance.putExtra("School_name", school_name);
                                teacherattendance.putExtra("Teacher_name", teacher_name);
                                teacherattendance.putExtra("version_name", AppController.versionName);
                                teacherattendance.putExtra("board_name", board_name);
                                teacherattendance.putExtra("org_id", org_id);
                                teacherattendance.putExtra("Teacher_div",teacher_div);
                                teacherattendance.putExtra("Teacher_Shift",teacher_shift);
                                teacherattendance.putExtra("Tecaher_Std",teacher_std);
                                teacherattendance.putExtra("academic_year", academic_year);
                                teacherattendance.putExtra("cls_teacher", clas_teacher);
                                startActivity(teacherattendance);
                            }
                        } else if (position == 5) {
                            //Behaviour
                            if (dft.isInternetOn() == true) {
                                Intent teacher_behaviour = new Intent(TeacherAnnouncementsActivity.this, TeacherBehaviourActivity.class);
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
                                teacher_behaviour.putExtra("academic_year", academic_year);
                                teacher_behaviour.putExtra("cls_teacher", clas_teacher);
                                startActivity(teacher_behaviour);
                            }
                        } else if (position == 6) {
                            //Sbject List
                            if (dft.isInternetOn() == true) {
                                Intent subject_list = new Intent(TeacherAnnouncementsActivity.this, SubjectListActivity.class);
                                subject_list.putExtra("usl_id", usl_id);
                                subject_list.putExtra("version_name", version_name);
                                subject_list.putExtra("clt_id", clt_id);
                                subject_list.putExtra("School_name", school_name);
                                subject_list.putExtra("Teacher_name", teacher_name);
                                subject_list.putExtra("board_name", board_name);
                                subject_list.putExtra("org_id", org_id);
                                subject_list.putExtra("Teacher_div",teacher_div);
                                subject_list.putExtra("Teacher_Shift",teacher_shift);
                                subject_list.putExtra("Tecaher_Std",teacher_std);
                                subject_list.putExtra("academic_year", academic_year);
                                subject_list.putExtra("cls_teacher", clas_teacher);
                                startActivity(subject_list);
                            }
                        } else if (position == 7) {
                            Intent setting = new Intent(TeacherAnnouncementsActivity.this, SettingActivity.class);
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
                            setting.putExtra("academic_year", academic_year);
                            setting.putExtra("cls_teacher", clas_teacher);
                            startActivity(setting);
                        } else if (position == 8) {
                            //Signout
                            Intent signout = new Intent(TeacherAnnouncementsActivity.this, LoginActivity.class);
                            Constant.SetLoginData("", "", TeacherAnnouncementsActivity.this);
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
                            final Dialog alertDialog = new Dialog(TeacherAnnouncementsActivity.this);
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
                        }

                    }

                });
            }
            else {
                    icons_without_sibling = new int[]{R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sms_48x48, R.drawable.announcementicon_48x48,R.drawable.subjectlist_48x48,R.drawable.setting1_hdpi, R.drawable.logout1_hdpi, R.drawable.info};
                    data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement","Subject List","Setting", "Sign Out", "About"};
                    drawerListView.setAdapter(new CustomAccount(TeacherAnnouncementsActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));
                    drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0) {
                                if (dft.isInternetOn() == true) {
                                    Intent teacherProfile = new Intent(TeacherAnnouncementsActivity.this, TeacherProfileActivity.class);
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
                                    Intent teacherProfile = new Intent(TeacherAnnouncementsActivity.this, TeacherMessageActivity.class);
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
                                    Intent teacherProfile = new Intent(TeacherAnnouncementsActivity.this, TeacherSMSActivity.class);
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
                                    Intent teacherannouncment = new Intent(TeacherAnnouncementsActivity.this, TeacherAnnouncementsActivity.class);
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
                                    Intent subject_list = new Intent(TeacherAnnouncementsActivity.this, SubjectListActivity.class);
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
                                Intent setting = new Intent(TeacherAnnouncementsActivity.this, SettingActivity.class);
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
                                Intent signout = new Intent(TeacherAnnouncementsActivity.this, LoginActivity.class);
                                Constant.SetLoginData("", "", TeacherAnnouncementsActivity.this);
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
                            } else if (position == 7) {
                                try {
                                    final Dialog alertDialog = new Dialog(TeacherAnnouncementsActivity.this);
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
                Constant.setTeacherDrawerData(academic_year, teacher_name,teacher_div,teacher_std,teacher_shift, TeacherAnnouncementsActivity.this);
                setDrawerData();
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (AppController.drawerSignOut.equalsIgnoreCase("true")) {
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
                intent.putExtra("Teacher_div",teacher_div);
                intent.putExtra("Teacher_Shift",teacher_shift);
                intent.putExtra("Tecaher_Std",teacher_std);
                intent.putExtra("Sibling", AppController.SiblingListSize);
                startActivity(intent);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void setDrawerData() {
        tv_academic_year_drawer.setText(academic_year);
        tv_child_name_drawer.setText(teacher_name);if((LoginActivity.get_class_teacher(TeacherAnnouncementsActivity.this).equalsIgnoreCase("1")))
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
            org_id = intent.getStringExtra("org_id");
            teacher_div = intent.getStringExtra("Teacher_div");
            teacher_std = intent.getStringExtra("Tecaher_Std");
            teacher_shift = intent.getStringExtra("Teacher_Shift");
            clas_teacher = intent.getStringExtra("cls_teacher");
            academic_year = intent.getStringExtra("academic_year");
            AppController.versionName = version_name;
            Bundle bundle=intent.getExtras();
            announcement_result =  (ArrayList<AnnouncementResult>)bundle.getSerializable("results");
            notificationId = getIntent().getExtras().getInt("notificationID");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        header = new HeaderControler(this, true, false, "Announcements");
        btn_attendance.setOnClickListener(this);
        btn_announce.setOnClickListener(this);
        btn_behaviour.setOnClickListener(this);
        lay_back_investment.setOnClickListener(this);
        list_teacherannouncement.setOnItemClickListener(this);
        img_drawer.setOnClickListener(this);
        progressDialog = Constant.getProgressDialog(this);
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        btn_back.setVisibility(View.VISIBLE);
        btn_behaviour.setBackgroundColor(Color.parseColor("#D6D6CC"));
        btn_attendance.setBackgroundColor(Color.parseColor("#D6D6CC"));
        btn_announce.setBackgroundColor(Color.parseColor("#38b8b2"));
        btn_announce.setTextColor(Color.WHITE);
        btn_attendance.setTextColor(Color.BLACK);
        btn_behaviour.setTextColor(Color.BLACK);
    }
    private void findViews() {
        //Linear Layout
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
        ll_footer = (LinearLayout) findViewById(R.id.ll_footer);
         //Button
        btn_announce = (Button) findViewById(R.id.btn_announce);
        btn_attendance = (Button) findViewById(R.id.btn_attendance);
        btn_behaviour = (Button) findViewById(R.id.btn_behaviour);
        //ImageView
        btn_back = (ImageView) findViewById(R.id.btn_back);
        //ListView
        list_teacherannouncement = (ListView) findViewById(R.id.list_teacherannouncement);
        //TextView
        tv_no_record = (TextView) findViewById(R.id.tv_no_record);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_version_code = (TextView) findViewById(R.id.tv_version_code);
        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
        tv_child_name_drawer = (TextView) findViewById(R.id.tv_child_name_drawer);
        tv_academic_year_drawer = (TextView) findViewById(R.id.tv_academic_year_drawer);
        tv_teacher_class = (TextView) findViewById(R.id.tv_teacher_class);
        // for drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        img_drawer = (ImageView) findViewById(R.id.img_drawer);
        leftfgf_drawer = (RelativeLayout) findViewById(R.id.leftfgf_drawer);
        rl_profile = (RelativeLayout) findViewById(R.id.rl_profile);
        drawerListView = (ListView) findViewById(R.id.drawerListView);
        mContentDisplaceToggle = new ContentDisplaceDrawerToggle(this, drawer,
                R.id.rl_profile, Gravity.START);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
      //  int width = dm.widthPixels;
        int width = getResources().getDisplayMetrics().widthPixels/2+30;
        DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) leftfgf_drawer.getLayoutParams();
        lp.width = width + 30;
        leftfgf_drawer.setLayoutParams(lp);
    }
    private void CallTeacherAnnouncementWebservice(String clt_id, final String usl_id) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getTeacherannaouncement(clt_id, usl_id, TeacherAnnouncementMethodName, Request.Method.POST,
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
                                list_teacherannouncement.setVisibility(View.VISIBLE);
                                setUIData();
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            } else if (login_details.Status.equalsIgnoreCase("0")) {

                                tv_no_record.setText("No Records Found");
                                tv_no_record.setVisibility(View.VISIBLE);
                                list_teacherannouncement.setVisibility(View.GONE);
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

    private void setUIData() {
        viewannouncementListSize = login_details.AnnouncementResult.size();
        AppController.viewTeacherAnnouncementListSize = viewannouncementListSize;
        announcement_adpater = new AnnouncementAdapter(TeacherAnnouncementsActivity.this,login_details.AnnouncementResult);
        list_teacherannouncement.setAdapter(announcement_adpater);
    }
    @Override
    public void onClick(View v) {
        try {
            if (v == btn_attendance) {
                if (dft.isInternetOn() == true) {
                    Intent teacherattendance = new Intent(TeacherAnnouncementsActivity.this, TeacherAttendanceActivity.class);
                    teacherattendance.putExtra("clt_id", clt_id);
                    teacherattendance.putExtra("usl_id", usl_id);
                    teacherattendance.putExtra("School_name", school_name);
                    teacherattendance.putExtra("Teacher_name", teacher_name);
                    teacherattendance.putExtra("version_name", AppController.versionName);
                    teacherattendance.putExtra("board_name", board_name);
                    teacherattendance.putExtra("cls_teacher", clas_teacher);
                    teacherattendance.putExtra("Teacher_div",teacher_div);
                    teacherattendance.putExtra("Teacher_Shift",teacher_shift);
                    teacherattendance.putExtra("Tecaher_Std",teacher_std);
                    teacherattendance.putExtra("academic_year", academic_year);
                    teacherattendance.putExtra("org_id", org_id);
                    startActivity(teacherattendance);
                }
            } else if (v == btn_behaviour) {
                if (dft.isInternetOn() == true) {
                    Intent teacherbehaviour = new Intent(TeacherAnnouncementsActivity.this, TeacherBehaviourActivity.class);
                    teacherbehaviour.putExtra("clt_id", clt_id);
                    teacherbehaviour.putExtra("usl_id", usl_id);
                    teacherbehaviour.putExtra("School_name", school_name);
                    teacherbehaviour.putExtra("Teacher_name", teacher_name);
                    teacherbehaviour.putExtra("version_name", AppController.versionName);
                    teacherbehaviour.putExtra("board_name", board_name);
                    teacherbehaviour.putExtra("org_id", org_id);
                    teacherbehaviour.putExtra("Teacher_div",teacher_div);
                    teacherbehaviour.putExtra("Teacher_Shift",teacher_shift);
                    teacherbehaviour.putExtra("Tecaher_Std",teacher_std);
                    teacherbehaviour.putExtra("cls_teacher", clas_teacher);
                    teacherbehaviour.putExtra("academic_year", academic_year);
                    startActivity(teacherbehaviour);
                }
            } else if (v == btn_announce) {
                if (dft.isInternetOn() == true) {
                    Intent teacherannouncment = new Intent(TeacherAnnouncementsActivity.this, TeacherAnnouncementsActivity.class);
                    teacherannouncment.putExtra("clt_id", clt_id);
                    teacherannouncment.putExtra("usl_id", usl_id);
                    teacherannouncment.putExtra("School_name", school_name);
                    teacherannouncment.putExtra("Teacher_name", teacher_name);
                    teacherannouncment.putExtra("version_name", AppController.versionName);
                    teacherannouncment.putExtra("board_name", board_name);
                    teacherannouncment.putExtra("org_id", org_id);
                    teacherannouncment.putExtra("Teacher_div",teacher_div);
                    teacherannouncment.putExtra("Teacher_Shift",teacher_shift);
                    teacherannouncment.putExtra("Tecaher_Std",teacher_std);
                    teacherannouncment.putExtra("cls_teacher", clas_teacher);
                    teacherannouncment.putExtra("academic_year", academic_year);
                    startActivity(teacherannouncment);
                }
            } else if (v == lay_back_investment) {
                Intent back = new Intent(TeacherAnnouncementsActivity.this, TeacherProfileActivity.class);
                back.putExtra("clt_id", clt_id);
                back.putExtra("usl_id", usl_id);
                back.putExtra("School_name", school_name);
                back.putExtra("Teacher_name", teacher_name);
                back.putExtra("version_name", AppController.versionName);
                back.putExtra("board_name", board_name);
                back.putExtra("org_id", org_id);
                back.putExtra("cls_teacher", clas_teacher);
                back.putExtra("academic_year", academic_year);
                back.putExtra("Teacher_div",teacher_div);
                back.putExtra("Teacher_Shift",teacher_shift);
                back.putExtra("Tecaher_Std",teacher_std);
                startActivity(back);
            } else if (v == img_drawer) {
                drawer.openDrawer(Gravity.RIGHT);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        //   super.onBackPressed();
        try {
            Intent back = new Intent(TeacherAnnouncementsActivity.this, TeacherProfileActivity.class);
            back.putExtra("clt_id", clt_id);
            back.putExtra("usl_id", usl_id);
            back.putExtra("School_name", school_name);
            back.putExtra("Teacher_name", teacher_name);
            back.putExtra("version_name", AppController.versionName);
            back.putExtra("board_name", board_name);
            back.putExtra("org_id", org_id);
            back.putExtra("cls_teacher", clas_teacher);
            back.putExtra("Teacher_div",teacher_div);
            back.putExtra("Teacher_Shift",teacher_shift);
            back.putExtra("Tecaher_Std",teacher_std);
            back.putExtra("academic_year", academic_year);
            startActivity(back);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        	/* we register BroadcastReceiver here*/
        registerReceiver();

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
    public void onReceiveResult(int resultCode, Bundle resultData) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            //Call Ws to read announcement
            msg_id = login_details.AnnouncementResult.get(position).msg_ID.toString();
            announcement_usl_id = login_details.AnnouncementResult.get(position).usl_id.toString();
            callWsToReadAnnouncement(msg_id,usl_id);
            announcement_adpater.notifyDataSetChanged();
            if(login_details.AnnouncementResult.get(position).pmu_readunreadstatus.equalsIgnoreCase("1")){
                AppController.UnreadMessage = "true";
                if(position==0) {
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(notificationID);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void callWsToReadAnnouncement(String msg_id, final String announcement_usl_id) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getannaouncementReadStatus(msg_id, announcement_usl_id,AnnouncementReadMethodName, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_details.Status.equalsIgnoreCase("1")) {
                                Log.e("Read", "Read");
                                //Call Webservice for Teacher Announcment
                                CallTeacherAnnouncementWebservice(clt_id, usl_id);
                            } else if (login_details.Status.equalsIgnoreCase("0")) {
                                Log.e("UnRead", "UnRead");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Show error or whatever...
                        Log.d("AnnouncementActivity", "ERROR.._---" + error.getCause());
                    }
                });
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
                announcementresults = (ArrayList<AnnouncementResult>) bundle.getSerializable("results");
                String abc = announcementresults.get(0).msg_Message;
                Log.e("TEacherMESSAGE",abc);
                viewannouncementListSize = AppController.announcement_result.size();
                AppController.viewTeacherAnnouncementListSize = viewannouncementListSize;
                announcement_adpater = new AnnouncementAdapter(TeacherAnnouncementsActivity.this, AppController.announcement_result);
                list_teacherannouncement.setAdapter(announcement_adpater);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
