package com.podarbetweenus.Activities;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.TabActivity;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

/**
 * Created by Gayatri on 1/19/2016.
 */
public class TeacherMessageActivity extends TabActivity implements View.OnClickListener,TabHost.OnTabChangeListener{
    //LayoutEntities
    HeaderControler header;
    //TextView
    TextView title,tv_version_name,tv_version_code,tv_cancel,tv_child_name_drawer,tv_academic_year_drawer,tv_teacher_class;
    ImageView img_drawer;
    //Linear Layout
    LinearLayout lay_back_investment,lL_drawer;
    //DrawerLayout
    DrawerLayout drawer;
    //Relative Layout
    RelativeLayout leftfgf_drawer,rl_profile;
    //ListView
    ListView drawerListView;

    ContentDisplaceDrawerToggle mContentDisplaceToggle;
    DataFetchService dft;

    String usl_id,clt_id,school_name,teacher_name,version_name,board_name,org_id,clas_teacher,academic_year,sentMesgTab,teacher_shift,teacher_std,teacher_div;
    String[] data_without_sibling;
    int[] icons_without_sibling;
    int notificationID = 1;

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
       protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.teacher_message_layout);
        Log.d("<< Inside","TeacherMessageActivity");

        findViews();
        init();
        getIntentData();
        setTabs();
        setDrawer();
        TabHost tabHost = getTabHost();
          if(AppController.sentMsgsTab.equalsIgnoreCase("true"))
          {
              tabHost.setCurrentTab(2);
          }
          else if(AppController.enterMessageBack.equalsIgnoreCase("true")){
              tabHost.setCurrentTab(1);
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
        academic_year = intent.getStringExtra("academic_year");
        org_id = intent.getStringExtra("org_id");
        teacher_div = intent.getStringExtra("Teacher_div");
        teacher_std = intent.getStringExtra("Tecaher_Std");
        teacher_shift = intent.getStringExtra("Teacher_Shift");
        sentMesgTab = intent.getStringExtra("sentMsgTab");
        AppController.versionName = version_name;
    }

    private void setTabs() {
        addTab("View Messages", TeacherViewMessageActivity.class);
        addTab("Write Message ", TeacherEnterMessageActivity.class);
        addTab("Sent Messages", TeacherSentMessageActivity.class);
    }
    @Override
    public void onTabChanged(String tabId) {

         TabHost tabHost = getTabHost();
        for(int j=0;j<tabHost.getTabWidget().getChildCount();j++)
        {
            TextView title = (TextView) tabHost.getTabWidget().getChildAt(j).findViewById(R.id.title); // for Unselected Tab
            title.setTextColor(Color.parseColor("#ffffff"));
        }
        int i = tabHost.getCurrentTab();
        Log.e("tab position", String.valueOf(i));
        AppController.TeacherTabPosition = i;
        Log.e("appTab",String.valueOf(AppController.TeacherTabPosition));
        TextView title = (TextView) tabHost.getCurrentTabView().findViewById(R.id.title); //for Selected Tab
        title.setTextColor(Color.parseColor("#000000"));

    }
    private void setDrawer() {
        try {
            if(LoginActivity.get_class_teacher(TeacherMessageActivity.this).equalsIgnoreCase("1")) {
                icons_without_sibling = new int[]{R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sms_48x48, R.drawable.announcementicon_48x48, R.drawable.att_48x48, R.drawable.behaviouricon_48x48,R.drawable.subjectlist_48x48, R.drawable.setting1_hdpi, R.drawable.logout1_hdpi, R.drawable.info};
                data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement", "Attendance", "Behaviour", "Subject List","Setting", "Sign Out", "About"};
                drawerListView.setAdapter(new CustomAccount(TeacherMessageActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));
                drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherMessageActivity.this, TeacherProfileActivity.class);
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
                                Intent teacherProfile = new Intent(TeacherMessageActivity.this, TeacherMessageActivity.class);
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
                                Intent teacherProfile = new Intent(TeacherMessageActivity.this, TeacherSMSActivity.class);
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
                        } else if (position == 3) {
                            //announcement
                            if (dft.isInternetOn() == true) {
                                Intent teacherannouncment = new Intent(TeacherMessageActivity.this, TeacherAnnouncementsActivity.class);
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
                                Intent teacherattendance = new Intent(TeacherMessageActivity.this, TeacherAttendanceActivity.class);
                                teacherattendance.putExtra("clt_id", clt_id);
                                teacherattendance.putExtra("usl_id", usl_id);
                                teacherattendance.putExtra("School_name", school_name);
                                teacherattendance.putExtra("Teacher_name", teacher_name);
                                teacherattendance.putExtra("version_name", AppController.versionName);
                                teacherattendance.putExtra("board_name", board_name);
                                teacherattendance.putExtra("cls_teacher", clas_teacher);
                                teacherattendance.putExtra("academic_year", academic_year);
                                teacherattendance.putExtra("org_id", org_id);
                                teacherattendance.putExtra("Teacher_div", teacher_div);
                                teacherattendance.putExtra("Teacher_Shift",teacher_shift);
                                teacherattendance.putExtra("Tecaher_Std",teacher_std);
                                startActivity(teacherattendance);
                            }
                        } else if (position == 5) {
                            //Behaviour
                            if (dft.isInternetOn() == true) {
                                Intent teacher_behaviour = new Intent(TeacherMessageActivity.this, TeacherBehaviourActivity.class);
                                teacher_behaviour.putExtra("clt_id", clt_id);
                                teacher_behaviour.putExtra("usl_id", usl_id);
                                teacher_behaviour.putExtra("School_name", school_name);
                                teacher_behaviour.putExtra("Teacher_name", teacher_name);
                                teacher_behaviour.putExtra("version_name", AppController.versionName);
                                teacher_behaviour.putExtra("board_name", board_name);
                                teacher_behaviour.putExtra("org_id", org_id);
                                teacher_behaviour.putExtra("cls_teacher", clas_teacher);
                                teacher_behaviour.putExtra("Teacher_div",teacher_div);
                                teacher_behaviour.putExtra("Teacher_Shift",teacher_shift);
                                teacher_behaviour.putExtra("Tecaher_Std",teacher_std);
                                teacher_behaviour.putExtra("academic_year", academic_year);
                                startActivity(teacher_behaviour);
                            }
                        } else if (position == 6) {
                            //Sbject List
                            if (dft.isInternetOn() == true) {
                                Intent subject_list = new Intent(TeacherMessageActivity.this, SubjectListActivity.class);
                                subject_list.putExtra("usl_id", usl_id);
                                subject_list.putExtra("version_name", AppController.versionName);
                                subject_list.putExtra("clt_id", clt_id);
                                subject_list.putExtra("School_name", school_name);
                                subject_list.putExtra("Teacher_name", teacher_name);
                                subject_list.putExtra("board_name", board_name);
                                subject_list.putExtra("org_id", org_id);
                                subject_list.putExtra("cls_teacher", clas_teacher);
                                subject_list.putExtra("Teacher_div",teacher_div);
                                subject_list.putExtra("Teacher_Shift",teacher_shift);
                                subject_list.putExtra("Tecaher_Std",teacher_std);
                                subject_list.putExtra("academic_year", academic_year);
                                startActivity(subject_list);
                            }
                        } else if (position == 7) {
                            Intent setting = new Intent(TeacherMessageActivity.this, SettingActivity.class);
                            setting.putExtra("usl_id", usl_id);
                            setting.putExtra("version_name", AppController.versionName);
                            setting.putExtra("clt_id", clt_id);
                            setting.putExtra("School_name", school_name);
                            setting.putExtra("Teacher_name", teacher_name);
                            setting.putExtra("board_name", board_name);
                            setting.putExtra("org_id", org_id);
                            setting.putExtra("cls_teacher", clas_teacher);
                            setting.putExtra("academic_year", academic_year);
                            setting.putExtra("Teacher_div",teacher_div);
                            setting.putExtra("Teacher_Shift",teacher_shift);
                            setting.putExtra("Tecaher_Std", teacher_std);
                            startActivity(setting);
                        } else if (position == 8) {
                            //Signout
                            Intent signout = new Intent(TeacherMessageActivity.this, LoginActivity.class);
                            Constant.SetLoginData("", "", TeacherMessageActivity.this);
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancel(notificationID);
                            AppController.iconCount = 0;
                            AppController.iconCountOnResume = 0;
                            AppController.OnBackpressed = "false";
                            AppController.iconAnnouncementCount = 0;
                            AppController.iconAnnouncementCountOnResume = 0;
                            AppController.loginButtonClicked = "false";
                            AppController.drawerSignOut = "true";
                            AppController.announcementActivity = "false";
                            startActivity(signout);
                        } else if (position == 9) {
                            try {
                                final Dialog alertDialog = new Dialog(TeacherMessageActivity.this);
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
                icons_without_sibling = new int[]{R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sms_48x48, R.drawable.announcementicon_48x48,R.drawable.subjectlist_48x48, R.drawable.setting1_hdpi, R.drawable.logout1_hdpi, R.drawable.info};
                data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement","Subject List","Setting", "Sign Out", "About"};
                drawerListView.setAdapter(new CustomAccount(TeacherMessageActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));
                drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherMessageActivity.this, TeacherProfileActivity.class);
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
                                Intent teacherProfile = new Intent(TeacherMessageActivity.this, TeacherMessageActivity.class);
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
                                Intent teacherProfile = new Intent(TeacherMessageActivity.this, TeacherSMSActivity.class);
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
                                Intent teacherannouncment = new Intent(TeacherMessageActivity.this, TeacherAnnouncementsActivity.class);
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
                                Intent subject_list = new Intent(TeacherMessageActivity.this, SubjectListActivity.class);
                                subject_list.putExtra("usl_id", usl_id);
                                subject_list.putExtra("version_name", version_name);
                                subject_list.putExtra("clt_id", clt_id);
                                subject_list.putExtra("School_name", school_name);
                                subject_list.putExtra("Teacher_name", teacher_name);
                                subject_list.putExtra("board_name", board_name);
                                subject_list.putExtra("academic_year", academic_year);
                                subject_list.putExtra("cls_teacher", clas_teacher);
                                subject_list.putExtra("Teacher_div",teacher_div);
                                subject_list.putExtra("Teacher_Shift",teacher_shift);
                                subject_list.putExtra("Tecaher_Std",teacher_std);
                                subject_list.putExtra("org_id", org_id);
                                startActivity(subject_list);
                            }
                        } else if (position == 5) {
                            Intent setting = new Intent(TeacherMessageActivity.this, SettingActivity.class);
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
                            Intent signout = new Intent(TeacherMessageActivity.this, LoginActivity.class);
                            Constant.SetLoginData("", "", TeacherMessageActivity.this);
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
                                final Dialog alertDialog = new Dialog(TeacherMessageActivity.this);
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
            Constant.setTeacherDrawerData(academic_year,teacher_name,teacher_div,teacher_std,teacher_shift,TeacherMessageActivity.this);
            setDrawerData();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setDrawerData() {
        tv_academic_year_drawer.setText(academic_year);
        tv_child_name_drawer.setText(teacher_name);
        if((LoginActivity.get_class_teacher(TeacherMessageActivity.this).equalsIgnoreCase("1")))
        {
            tv_teacher_class.setText(teacher_shift + " | " + teacher_std + " | " + teacher_div);
        }
        else{
            tv_teacher_class.setVisibility(View.GONE);
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
                intent.putExtra("org_id",org_id);
                intent.putExtra("Teacher_div",teacher_div);
                intent.putExtra("Teacher_Shift",teacher_shift);
                intent.putExtra("Tecaher_Std",teacher_std);
                intent.putExtra("cls_teacher", clas_teacher);
                intent.putExtra("academic_year", academic_year);
                intent.putExtra("Sibling", AppController.SiblingListSize);
                startActivity(intent);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void addTab(String labelId, Class<?> c) {
        TabHost tabHost = getTabHost();
        Intent intent = new Intent(this, c);
        intent.putExtra("usl_id",usl_id);
        intent.putExtra("clt_id", clt_id);
        intent.putExtra("School_name", school_name);
        intent.putExtra("Teacher_name", teacher_name);
        intent.putExtra("org_id",org_id);
        intent.putExtra("version_name",AppController.versionName);
        intent.putExtra("board_name", board_name);
        intent.putExtra("cls_teacher", clas_teacher);
        intent.putExtra("academic_year", academic_year);
        intent.putExtra("Teacher_div",teacher_div);
        intent.putExtra("Teacher_Shift",teacher_shift);
        intent.putExtra("Tecaher_Std",teacher_std);
        tabHost.getTabWidget().setShowDividers(TabWidget.SHOW_DIVIDER_MIDDLE);
        TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);
        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator_for_teacher, getTabWidget(), false);
        title = (TextView) tabIndicator.findViewById(R.id.title);
        title.setText(labelId);

        spec.setIndicator(tabIndicator);
        spec.setContent(intent);

        tabHost.addTab(spec);
        int i = tabHost.getCurrentTab();
        Log.e("tab position", String.valueOf(i));
        TextView title = (TextView) tabHost.getCurrentTabView().findViewById(R.id.title); //for Selected Tab
        title.setTextColor(Color.parseColor("#000000"));
    }

    private void findViews() {
        //TextView
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_version_code = (TextView) findViewById(R.id.tv_version_code);
        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
        tv_child_name_drawer = (TextView) findViewById(R.id.tv_child_name_drawer);
        tv_academic_year_drawer = (TextView) findViewById(R.id.tv_academic_year_drawer);
        tv_teacher_class = (TextView) findViewById(R.id.tv_teacher_class);
        //Linear Layout
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
        lL_drawer = (LinearLayout) findViewById(R.id.lL_drawer);
        //ImageView
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
        //int width = dm.widthPixels;
        int width = getResources().getDisplayMetrics().widthPixels/2+30;
        DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) leftfgf_drawer.getLayoutParams();
        lp.width = width + 30;
        leftfgf_drawer.setLayoutParams(lp);
    }
    private void init(){

        header = new HeaderControler(this, true, false, "Messages");
        lay_back_investment.setOnClickListener(this);
        img_drawer.setOnClickListener(this);
        dft = new DataFetchService(this);
        lL_drawer.setOnClickListener(this);
        TabHost tabHost = getTabHost();
        tabHost.setOnTabChangedListener(this);
    }
    @Override
    public void onClick(View v) {
        try {
            if (v == lay_back_investment) {
                Intent back = new Intent(TeacherMessageActivity.this, TeacherProfileActivity.class);
                AppController.OnBackpressed = "false";
                AppController.clt_id = clt_id;
                AppController.usl_id = usl_id;
                AppController.Board_name = board_name;
                AppController.enterMessageBack = "false";
                back.putExtra("clt_id", clt_id);
                back.putExtra("usl_id", usl_id);
                back.putExtra("School_name", school_name);
                back.putExtra("Teacher_name", teacher_name);
                back.putExtra("org_id", org_id);
                back.putExtra("version_name", AppController.versionName);
                back.putExtra("board_name", board_name);
                back.putExtra("cls_teacher", clas_teacher);
                back.putExtra("Teacher_div",teacher_div);
                back.putExtra("Teacher_Shift",teacher_shift);
                back.putExtra("Tecaher_Std",teacher_std);
                back.putExtra("academic_year", academic_year);
                startActivity(back);
            } else if (v == img_drawer) {
                drawer.openDrawer(Gravity.RIGHT);
            }
            else if(v==lL_drawer){
                TabHost tabHost = getTabHost();
                tabHost.setClickable(false);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            Intent back = new Intent(TeacherMessageActivity.this, TeacherProfileActivity.class);
            AppController.OnBackpressed = "false";
            AppController.clt_id = clt_id;
            AppController.usl_id = usl_id;
            AppController.Board_name = board_name;
            AppController.enterMessageBack = "false";
            back.putExtra("clt_id", AppController.clt_id);
            back.putExtra("usl_id", AppController.usl_id);
            back.putExtra("board_name", board_name);
            back.putExtra("Teacher_name", teacher_name);
            back.putExtra("School_name", school_name);
            back.putExtra("org_id", org_id);
            back.putExtra("cls_teacher", clas_teacher);
            back.putExtra("academic_year", academic_year);
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
}

