package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.BuildConfig;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;
import com.podarbetweenus.Utility.BadgeView;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 1/18/2016.
 */
public class TeacherProfileActivity extends Activity implements View.OnClickListener{
    //ImageView
    ImageView btn_back,img_drawer,img_icon,img_sms,img_attendance,img_behaviour;
    //Linear Layout
    LinearLayout ll_attendance,ll_behaviour,ll_announcement,ll_sub_list,ll_sms,ll_messages;
    //TextView
    TextView tv_school_name,tv_teacher_name,tv_version_name,tv_version_code,tv_cancel,tv_child_name_drawer,tv_teacher_class,tv_academic_year_drawer;
    //DrawerLayout
    DrawerLayout drawer;
    //Relative Layout
    RelativeLayout leftfgf_drawer,rl_profile;
    //BageView
    BadgeView badge;
    //ListView
    ListView drawerListView;
    public static ImageView img_message,img_announcement;
    //LayoutEntities
    HeaderControler header;
    DataFetchService dft;
    LoginDetails login_details;
    ContentDisplaceDrawerToggle mContentDisplaceToggle;
    public static SharedPreferences resultpreLoginData;
    //ProgressDialog
    ProgressDialog progressDialog;


    public static String usl_id,clt_id,school_name,teacher_name,version_name,board_name,badge_count,month= "0",check = "0",teachermsgStatus,teacherAnnouncementStatus,
            teachermsgStatusOnResume,teacherAnnouncementStatusOnResume,org_id,clas_teacher,academic_year,playstore_version,teacher_shift,teacher_std,teacher_div;
    String[] data_without_sibling;
    int[] icons_without_sibling;
    String ShowViewMessagesMethod_name = "GetSentMessageDataTeacher";
    String TeacherAnnouncementMethodName = "GetTeacherAnnouncement";
    ArrayList<String> teachermsgStatusList = new ArrayList<String>();
    ArrayList<String> teachermsgStatusListOnResume = new ArrayList<String>();
    ArrayList<String> teacherAnnouncementStatusListOnResume = new ArrayList<String>();
    ArrayList<String> teacherAnnouncementStatusList = new ArrayList<String>();
    int pageIndex = 1,teacherMsgstatusSize,teacherMsgstatusSizeOnResume,notificationID=1,teacherAnnouncementtatusSizeOnResume,teacherAnnouncementtatusSize;
    int pageSize = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("<<Inside","TeacherProfileActivity");
        super.onCreate(savedInstanceState);

        Calendar now = Calendar.getInstance();
        Date date = now.getTime();
        SimpleDateFormat format = new SimpleDateFormat("MM");
        month = format.format(date);

        Log.d("<<<Month", month);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(LoginActivity.get_class_teacher(TeacherProfileActivity.this).equalsIgnoreCase("1")){
        setContentView(R.layout.teacher_profile_layout);
        }
        else {
            setContentView(R.layout.teacher_profile_layout_without_attendance_behaviour);
        }
        findViews();
        init();
        getIntentID();
        setProfileData();
        AppController.Notification_send = "true";
        //find the home launcher Package
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        String currentHomePackage = resolveInfo.activityInfo.packageName;
        String appPackageName = getPackageName();
        version_name = BuildConfig.VERSION_NAME;
        AppController.versionName = version_name;
        playstore_version = null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            playstore_version = Jsoup.connect("https://play.google.com/store/apps/details?id=" + appPackageName)
                    .timeout(30000)
                    .userAgent(
                            "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com").get()
                    .select("div[itemprop=softwareVersion]").first()
                    .ownText();

        } catch (IOException e) {
            e.printStackTrace();
        }
        //versionUpdate
        if(Constant.version_update.equalsIgnoreCase("true") && playstore_version!=null) {
            versionUpdate();
        }
        AppController.SentEnterDropdown = "false";
        AppController.dropdownSelected = "false";
        AppController.Notification_send = "true";
        AppController.ProfileWithoutSibling = "true";
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
            //set Date When No Internet Connectivity
            if (connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                    connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
                clt_id = TeacherProfileActivity.get_clt_id(this);
                usl_id = TeacherProfileActivity.get_usl_id(this);
                teacher_name = TeacherProfileActivity.get_teacher_name(this);
                board_name = TeacherProfileActivity.get_board_name(this);
                school_name = TeacherProfileActivity.get_school_name(this);
                org_id = LoginActivity.get_org_id(this);
                tv_teacher_name.setText(teacher_name);
                tv_school_name.setText(school_name);
                teacher_div = LoginActivity.get_teacher_div(this);
                teacher_std = LoginActivity.get_teacher_std(this);
                teacher_shift = LoginActivity.get_teacher_shift(this);
                //For Drawer
                setDrawer();
                //setLogo
                setLogo();
            }
            setDrawer();
            //setLogo
            setLogo();
            //Call Ws For View Messages
            CallWebserviceToViewMessages(clt_id, usl_id, month, check, pageIndex, pageSize);
            //Call Webservice for Teacher Announcment
            CallTeacherAnnouncementWebservice(clt_id, usl_id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void versionUpdate() {
        if(!AppController.versionName.equalsIgnoreCase(playstore_version))
        {
            try {
                final Dialog alertDialog = new Dialog(this);
                alertDialog.setCanceledOnTouchOutside(false);
                LayoutInflater inflater = this.getLayoutInflater();
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(Color.TRANSPARENT));
                View convertView =  inflater.inflate(R.layout.version_update, null);
                alertDialog.setContentView(convertView);
                TextView tv_later,tv_update;
                tv_update = (TextView) convertView.findViewById(R.id.tv_update);
                alertDialog.show();

                tv_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            alertDialog.dismiss();
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void setLogo() {
        try {
            //Org id==2
            if(org_id.equalsIgnoreCase("2") && board_name.equalsIgnoreCase("CBSE") && !school_name.contains("Podar Jumbo")){
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.cbse_225x225));
            }
            else if(org_id.equalsIgnoreCase("2") && board_name.equalsIgnoreCase("ICSE")){
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.icse_225x225));
            }
            else if(org_id.equalsIgnoreCase("2") && board_name.equalsIgnoreCase("CIE")){
                //CIE
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.cie_250x100));
            }
            else if(org_id.equalsIgnoreCase("2") && board_name.equalsIgnoreCase("SSC")){
                //SSC
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.ssc_250x80));
            }
            else if(org_id.equalsIgnoreCase("2") && board_name.equalsIgnoreCase("IB")){
                //IB
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.podarort_225x225));
            }
            else if(org_id.equalsIgnoreCase("2") && school_name.contains("Podar Jumbo")){
                //Jumbo kids
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.jumbokids_184x184));
            }
            //Org id==4
            else if(org_id.equalsIgnoreCase("4") && board_name.contains("ICSE")){
                //Lilavti
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.lilavati_250x125));
            }
            else if(org_id.equalsIgnoreCase("4") && board_name.equalsIgnoreCase("CBSE.")){
                //RN podar
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.rnpodar_225x100));
            }
            else if (org_id.equalsIgnoreCase("4") && board_name.equalsIgnoreCase("IB")) {
                //IB
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.ib_250x100));
            } else if (org_id.equalsIgnoreCase("4") && board_name.equalsIgnoreCase("SSC")) {
                //SSC
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.ssc_250x100));
            }
            else if(org_id.equalsIgnoreCase("4") && clt_id.equalsIgnoreCase("39")){
                //PWC
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.pwc_225x225));
            }
            else if(org_id.equalsIgnoreCase("4") && clt_id.equalsIgnoreCase("38")){
                //PIC
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.pic_225x225));
            }
            //Org id==3
            else if(org_id.equalsIgnoreCase("3") && board_name.equalsIgnoreCase("CBSE")) {
                //PWS
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.pws_300x95));

            }
            else if(org_id.equalsIgnoreCase("3") && board_name.equalsIgnoreCase("CBSE") && school_name.contains("Podar Jumbo")){
                //Jumbo Kids
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.jumbokids_184x184));
            }
            //Org id==1
           else if(org_id.equalsIgnoreCase("1")){
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.cbse_225x225));
            } else {
                img_icon.setVisibility(View.INVISIBLE);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setDrawer() {
        try {
            if(LoginActivity.get_class_teacher(TeacherProfileActivity.this).equalsIgnoreCase("1")){
                icons_without_sibling = new int[]{R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sms_48x48, R.drawable.announcementicon_48x48, R.drawable.att_48x48, R.drawable.behaviouricon_48x48,R.drawable.subjectlist_48x48, R.drawable.setting1_hdpi, R.drawable.logout1_hdpi, R.drawable.info};
                data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement", "Attendance", "Behaviour", "Subject List", "Setting", "Sign Out", "About"};
                drawerListView.setAdapter(new CustomAccount(TeacherProfileActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));

                drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherProfileActivity.this, TeacherProfileActivity.class);
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
                                Intent teacherProfile = new Intent(TeacherProfileActivity.this, TeacherMessageActivity.class);
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
                                Intent teacherProfile = new Intent(TeacherProfileActivity.this, TeacherSMSActivity.class);
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
                                Intent teacherannouncment = new Intent(TeacherProfileActivity.this, TeacherAnnouncementsActivity.class);
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
                            //Attendance
                            if (dft.isInternetOn() == true) {
                                Intent teacherattendance = new Intent(TeacherProfileActivity.this, TeacherAttendanceActivity.class);
                                teacherattendance.putExtra("clt_id", clt_id);
                                teacherattendance.putExtra("usl_id", usl_id);
                                teacherattendance.putExtra("School_name", school_name);
                                teacherattendance.putExtra("Teacher_name", teacher_name);
                                teacherattendance.putExtra("version_name", version_name);
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
                                Intent teacher_behaviour = new Intent(TeacherProfileActivity.this, TeacherBehaviourActivity.class);
                                teacher_behaviour.putExtra("clt_id", clt_id);
                                teacher_behaviour.putExtra("usl_id", usl_id);
                                teacher_behaviour.putExtra("School_name", school_name);
                                teacher_behaviour.putExtra("Teacher_name", teacher_name);
                                teacher_behaviour.putExtra("version_name", version_name);
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
                                Intent subject_list = new Intent(TeacherProfileActivity.this, SubjectListActivity.class);
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
                        } else if (position == 7) {
                            Intent setting = new Intent(TeacherProfileActivity.this, SettingActivity.class);
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
                        } else if (position == 8) {
                            //Signout
                            Intent signout = new Intent(TeacherProfileActivity.this, LoginActivity.class);
                            Constant.SetLoginData("", "", TeacherProfileActivity.this);
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancel(notificationID);
                            AppController.iconCount = 0;
                            AppController.iconCountOnResume = 0;
                            AppController.iconAnnouncementCount = 0;
                            AppController.iconAnnouncementCountOnResume = 0;
                            AppController.OnBackpressed = "false";
                            teacherAnnouncementStatusListOnResume.clear();
                            teachermsgStatusListOnResume.clear();
                            AppController.loginButtonClicked = "false";
                            AppController.drawerSignOut = "true";
                            AppController.announcementActivity = "false";
                            startActivity(signout);
                        } else if (position == 9) {
                            try {
                                final Dialog alertDialog = new Dialog(TeacherProfileActivity.this);
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
                drawerListView.setAdapter(new CustomAccount(TeacherProfileActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));
                drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherProfileActivity.this, TeacherProfileActivity.class);
                                teacherProfile.putExtra("clt_id", clt_id);
                                teacherProfile.putExtra("usl_id", usl_id);
                                teacherProfile.putExtra("School_name", school_name);
                                teacherProfile.putExtra("Teacher_name", teacher_name);
                                teacherProfile.putExtra("version_name", version_name);
                                teacherProfile.putExtra("board_name", board_name);
                                teacherProfile.putExtra("org_id", org_id);
                                teacherProfile.putExtra("Teacher_div", teacher_div);
                                teacherProfile.putExtra("Teacher_Shift", teacher_shift);
                                teacherProfile.putExtra("Tecaher_Std", teacher_std);
                                teacherProfile.putExtra("academic_year", academic_year);
                                teacherProfile.putExtra("cls_teacher", clas_teacher);
                                startActivity(teacherProfile);
                            }
                        } else if (position == 1) {
                            //Message
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherProfileActivity.this, TeacherMessageActivity.class);
                                teacherProfile.putExtra("clt_id", clt_id);
                                teacherProfile.putExtra("usl_id", usl_id);
                                teacherProfile.putExtra("School_name", school_name);
                                teacherProfile.putExtra("Teacher_name", teacher_name);
                                teacherProfile.putExtra("version_name", version_name);
                                teacherProfile.putExtra("board_name", board_name);
                                teacherProfile.putExtra("org_id", org_id);
                                teacherProfile.putExtra("Teacher_div", teacher_div);
                                teacherProfile.putExtra("Teacher_Shift", teacher_shift);
                                teacherProfile.putExtra("Tecaher_Std", teacher_std);
                                teacherProfile.putExtra("academic_year", academic_year);
                                teacherProfile.putExtra("cls_teacher", clas_teacher);
                                startActivity(teacherProfile);
                            }
                        } else if (position == 2) {
                            //SMS
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherProfileActivity.this, TeacherSMSActivity.class);
                                teacherProfile.putExtra("clt_id", clt_id);
                                teacherProfile.putExtra("usl_id", usl_id);
                                teacherProfile.putExtra("School_name", school_name);
                                teacherProfile.putExtra("Teacher_name", teacher_name);
                                teacherProfile.putExtra("version_name", version_name);
                                teacherProfile.putExtra("board_name", board_name);
                                teacherProfile.putExtra("org_id", org_id);
                                teacherProfile.putExtra("Teacher_div", teacher_div);
                                teacherProfile.putExtra("Teacher_Shift", teacher_shift);
                                teacherProfile.putExtra("Tecaher_Std", teacher_std);
                                teacherProfile.putExtra("academic_year", academic_year);
                                teacherProfile.putExtra("cls_teacher", clas_teacher);
                                startActivity(teacherProfile);
                            }
                        } else if (position == 3) {
                            //announcement
                            if (dft.isInternetOn() == true) {
                                Intent teacherannouncment = new Intent(TeacherProfileActivity.this, TeacherAnnouncementsActivity.class);
                                teacherannouncment.putExtra("clt_id", clt_id);
                                teacherannouncment.putExtra("usl_id", usl_id);
                                teacherannouncment.putExtra("School_name", school_name);
                                teacherannouncment.putExtra("Teacher_name", teacher_name);
                                teacherannouncment.putExtra("version_name", version_name);
                                teacherannouncment.putExtra("board_name", board_name);
                                teacherannouncment.putExtra("org_id", org_id);
                                teacherannouncment.putExtra("Teacher_div", teacher_div);
                                teacherannouncment.putExtra("Teacher_Shift", teacher_shift);
                                teacherannouncment.putExtra("Tecaher_Std", teacher_std);
                                teacherannouncment.putExtra("academic_year", academic_year);
                                teacherannouncment.putExtra("cls_teacher", clas_teacher);
                                startActivity(teacherannouncment);
                            }
                        }
                        else if (position == 4) {
                            //Sbject List
                            if (dft.isInternetOn() == true) {
                                Intent subject_list = new Intent(TeacherProfileActivity.this, SubjectListActivity.class);
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
                        }
                        else if (position == 5) {
                            Intent setting = new Intent(TeacherProfileActivity.this, SettingActivity.class);
                            setting.putExtra("usl_id", usl_id);
                            setting.putExtra("version_name", version_name);
                            setting.putExtra("clt_id", clt_id);
                            setting.putExtra("School_name", school_name);
                            setting.putExtra("Teacher_name", teacher_name);
                            setting.putExtra("board_name", board_name);
                            setting.putExtra("academic_year", academic_year);
                            setting.putExtra("cls_teacher", clas_teacher);
                            setting.putExtra("org_id", org_id);
                            setting.putExtra("Teacher_div", teacher_div);
                            setting.putExtra("Teacher_Shift", teacher_shift);
                            setting.putExtra("Tecaher_Std", teacher_std);
                            startActivity(setting);
                        } else if (position == 6) {
                            //Signout
                            Intent signout = new Intent(TeacherProfileActivity.this, LoginActivity.class);
                            Constant.SetLoginData("", "", TeacherProfileActivity.this);
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancel(notificationID);
                            AppController.iconCount = 0;
                            AppController.iconCountOnResume = 0;
                            AppController.iconAnnouncementCount = 0;
                            AppController.iconAnnouncementCountOnResume = 0;
                            teacherAnnouncementStatusListOnResume.clear();
                            AppController.OnBackpressed = "false";
                            teachermsgStatusListOnResume.clear();
                            AppController.loginButtonClicked = "false";
                            AppController.drawerSignOut = "true";
                            AppController.announcementActivity = "false";
                            startActivity(signout);
                        } else if (position == 7) {
                            try {
                                final Dialog alertDialog = new Dialog(TeacherProfileActivity.this);
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
            Constant.setTeacherDrawerData(academic_year, teacher_name,teacher_div,teacher_std,teacher_shift, TeacherProfileActivity.this);
            setDrawerData();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setDrawerData() {
        tv_child_name_drawer.setText(teacher_name);
        tv_academic_year_drawer.setText(academic_year);
        if((LoginActivity.get_class_teacher(TeacherProfileActivity.this).equalsIgnoreCase("1")))
        {
            tv_teacher_class.setText(teacher_shift + " | " + teacher_std + " | " + teacher_div);
        }
        else{
            tv_teacher_class.setVisibility(View.GONE);
        }
    }

    private void setProfileData() {
        tv_school_name.setText(school_name);
        tv_teacher_name.setText(teacher_name);
    }

    private void getIntentID() {
        Intent intent = getIntent();
        usl_id = intent.getStringExtra("usl_id");
        clt_id = intent.getStringExtra("clt_id");
        school_name = intent.getStringExtra("School_name");
        teacher_name = intent.getStringExtra("Teacher_name");
        version_name = intent.getStringExtra("version_name");
        org_id = intent.getStringExtra("org_id");
        clas_teacher = intent.getStringExtra("cls_teacher");
        academic_year = intent.getStringExtra("academic_year");
        AppController.versionName = version_name;
        board_name = intent.getStringExtra("board_name");
        teacher_div = intent.getStringExtra("Teacher_div");
        teacher_std = intent.getStringExtra("Tecaher_Std");
        teacher_shift = intent.getStringExtra("Teacher_Shift");
        TeacherProfileActivity.Set_id(clt_id, usl_id, teacher_name, school_name, board_name, org_id, teacher_div, teacher_std, teacher_shift, TeacherProfileActivity.this);
    }

    private void findViews() {
        //Button
        btn_back = (ImageView) findViewById(R.id.btn_back);
        img_drawer = (ImageView) findViewById(R.id.img_drawer);
        img_message = (ImageView) findViewById(R.id.img_message);
        img_icon = (ImageView) findViewById(R.id.img_icon);
        img_behaviour = (ImageView) findViewById(R.id.img_behaviour);
        img_announcement = (ImageView) findViewById(R.id.img_announcement);
        img_sms = (ImageView) findViewById(R.id.img_sms);
        img_attendance = (ImageView) findViewById(R.id.img_attendance);
        img_behaviour = (ImageView) findViewById(R.id.img_behaviour);
        //Linear Layout
        ll_announcement = (LinearLayout)findViewById(R.id.ll_announcement);
        ll_attendance = (LinearLayout) findViewById(R.id.ll_attendance);
        ll_sub_list = (LinearLayout) findViewById(R.id.ll_sub_list);
        ll_sms = (LinearLayout) findViewById(R.id.ll_sms);
        ll_messages = (LinearLayout) findViewById(R.id.ll_messages);
        ll_behaviour = (LinearLayout) findViewById(R.id.ll_behaviour);
        //TextView
        tv_teacher_name = (TextView) findViewById(R.id.tv_teacher_name);
        tv_school_name = (TextView) findViewById(R.id.tv_school_name);
        tv_version_code = (TextView) findViewById(R.id.tv_version_code);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_academic_year_drawer = (TextView) findViewById(R.id.tv_academic_year_drawer);
        tv_child_name_drawer = (TextView) findViewById(R.id.tv_child_name_drawer);
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
       // int width = dm.widthPixels;
        int width = getResources().getDisplayMetrics().widthPixels/2+30;
        DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) leftfgf_drawer.getLayoutParams();
        lp.width = width + 30;
        leftfgf_drawer.setLayoutParams(lp);
    }
    private void init(){
        ll_attendance.setOnClickListener(this);
        ll_announcement.setOnClickListener(this);
        ll_sub_list.setOnClickListener(this);
        ll_sms.setOnClickListener(this);
        ll_messages.setOnClickListener(this);
        ll_behaviour.setOnClickListener(this);
        img_drawer.setOnClickListener(this);
        progressDialog = Constant.getProgressDialog(this);
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        img_announcement.setOnClickListener(this);
        img_behaviour.setOnClickListener(this);
        img_attendance.setOnClickListener(this);
        img_sms.setOnClickListener(this);
        img_message.setOnClickListener(this);

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
                                if (AppController.OnBackpressed.equalsIgnoreCase("true")) {
                                    AppController.viewTeacherMessageListSize = login_details.ViewMessageResult.size();
                                    for (int i = 0; i < login_details.ViewMessageResult.size(); i++) {
                                        teachermsgStatusOnResume = login_details.ViewMessageResult.get(i).pmu_readunreadstatus;
                                        if (teachermsgStatusOnResume.equalsIgnoreCase("1")) {
                                            teachermsgStatusListOnResume.add(teachermsgStatusOnResume);
                                            teacherMsgstatusSizeOnResume = teachermsgStatusListOnResume.size();
                                        }
                                        AppController.iconCountOnResume = teacherMsgstatusSizeOnResume;
                                        Log.e("Teacher Profile onRes", String.valueOf(AppController.iconCountOnResume));
                                    }
                                } else {
                                    AppController.viewTeacherMessageListSize = login_details.ViewMessageResult.size();
                                    for (int i = 0; i < login_details.ViewMessageResult.size(); i++) {
                                        teachermsgStatus = login_details.ViewMessageResult.get(i).pmu_readunreadstatus.toString();
                                        if (teachermsgStatus.equalsIgnoreCase("1")) {
                                            teachermsgStatusList.add(teachermsgStatus);
                                            teacherMsgstatusSize = teachermsgStatusList.size();
                                        }
                                        AppController.iconCount = teacherMsgstatusSize;
                                    }
                                }

                            } else {
                            }
                            setIconCount();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Show error or whatever...
                        Log.d("TeacherProfileActivity", "ERROR.._---" + error.getCause());
                    }
                });
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
                                if (AppController.OnBackpressed.equalsIgnoreCase("true")) {
                                    AppController.viewAnnouncementListSize = login_details.AnnouncementResult.size();
                                    for (int i = 0; i < login_details.AnnouncementResult.size(); i++) {
                                        teacherAnnouncementStatusOnResume = login_details.AnnouncementResult.get(i).pmu_readunreadstatus.toString();
                                        if (teacherAnnouncementStatusOnResume.equalsIgnoreCase("1")) {
                                            teacherAnnouncementStatusListOnResume.add(teacherAnnouncementStatusOnResume);
                                            teacherAnnouncementtatusSizeOnResume = teacherAnnouncementStatusListOnResume.size();
                                        }
                                        AppController.iconAnnouncementCountOnResume = teacherAnnouncementtatusSizeOnResume;
                                        Log.e("TeacherProf onResAnnoun", String.valueOf(AppController.iconAnnouncementCountOnResume));
                                    }
                                } else {
                                    AppController.viewAnnouncementListSize = login_details.AnnouncementResult.size();
                                    for (int i = 0; i < login_details.AnnouncementResult.size(); i++) {
                                        teacherAnnouncementStatus = login_details.AnnouncementResult.get(i).pmu_readunreadstatus.toString();
                                        if (teacherAnnouncementStatus.equalsIgnoreCase("1")) {
                                            teacherAnnouncementStatusList.add(teacherAnnouncementStatus);
                                            teacherAnnouncementtatusSize = teacherAnnouncementStatusList.size();
                                        }
                                        AppController.iconAnnouncementCount = teacherAnnouncementtatusSize;
                                    }
                                }

                            } else if (login_details.Status.equalsIgnoreCase("0")) {

                            }
                            setAnnouncementIconCount();
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

    private void setAnnouncementIconCount() {
        if(AppController.OnBackpressed.equalsIgnoreCase("true")) {
            badge = new BadgeView(this, img_announcement);
            badge.setText(String.valueOf(AppController.iconAnnouncementCountOnResume));
            badge_count = String.valueOf(badge.getText());
            Log.e("Badge AnnounCountResume", badge_count);
            badge.setTextSize(15);
            badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
            badge.setBadgeBackgroundColor(Color.RED);
            badge.setTextColor(Color.WHITE);
            if (badge.getText().equals("0")) {
                badge.hide();
                Log.e("Badge Countresume zero", badge_count);
            } else if (Integer.valueOf(badge_count) <= 0) {
                badge.hide();
                Log.e("Badge Count -1", badge_count);
            } else {
                badge.show();
            }
        }
        else{
            badge = new BadgeView(this, img_announcement);
            badge.setText(String.valueOf(AppController.iconAnnouncementCount));
            badge_count = String.valueOf(badge.getText());
            Log.e("Badge Count", badge_count);
            badge.setTextSize(15);
            badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
            badge.setBadgeBackgroundColor(Color.RED);
            badge.setTextColor(Color.WHITE);
            if (badge.getText().equals("0")) {
                badge.hide();
                Log.e("Badge Count zero", badge_count);
            } else if (Integer.valueOf(badge_count) <= 0) {
                badge.hide();
                Log.e("Badge Count -1", badge_count);
            } else {
                badge.show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (AppController.fromSplashScreen.equalsIgnoreCase("true")) {

                //AppController.fromSplashScreen = "false";
                AppController.killApp = "false";
                AppController.ProfileWithoutSibling = "true";
                Calendar now = Calendar.getInstance();
                Date date = now.getTime();
                SimpleDateFormat format = new SimpleDateFormat("MM");
                month = format.format(date);

                Log.d("<<<Month", month);
                if (AppController.OnBackpressed.equalsIgnoreCase("true")) {
                    CallWebserviceToViewMessages(clt_id, usl_id, month, check, pageIndex, pageSize);
                    CallTeacherAnnouncementWebservice(clt_id, usl_id);
                }
                Intent intent = new Intent(getApplicationContext(), SplashScreen.class);

                AppController.ProfileWithoutSibling = "false";
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                AppController.Board_name = board_name;
                AppController.school_name = school_name;
                AppController.versionName = version_name;
                AppController.usl_id = usl_id;
                AppController.clt_id = clt_id;
                AppController.school_name = school_name;
                intent.putExtra("usl_id", AppController.usl_id);
                intent.putExtra("version_name", AppController.versionName);
                intent.putExtra("clt_id", AppController.clt_id);
                intent.putExtra("School_name", AppController.school_name);
                intent.putExtra("board_name", AppController.Board_name);
                intent.putExtra("org_id",org_id);
                intent.putExtra("Teacher_div",teacher_div);
                intent.putExtra("Teacher_Shift",teacher_shift);
                intent.putExtra("Tecaher_Std",teacher_std);
                intent.putExtra("Teacher_name", teacher_name);
                startActivity(intent);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        }

    @Override
    public void onClick(View v) {
        if(v==img_announcement){
            if (dft.isInternetOn() == true) {
                Intent teacherannouncment = new Intent(TeacherProfileActivity.this, TeacherAnnouncementsActivity.class);
                teacherannouncment.putExtra("clt_id", clt_id);
                teacherannouncment.putExtra("usl_id", usl_id);
                teacherannouncment.putExtra("School_name", school_name);
                teacherannouncment.putExtra("Teacher_name", teacher_name);
                teacherannouncment.putExtra("version_name", AppController.versionName);
                teacherannouncment.putExtra("board_name", board_name);
                teacherannouncment.putExtra("org_id",org_id);
                teacherannouncment.putExtra("cls_teacher",clas_teacher);
                teacherannouncment.putExtra("academic_year", academic_year);
                teacherannouncment.putExtra("Teacher_div",teacher_div);
                teacherannouncment.putExtra("Teacher_Shift",teacher_shift);
                teacherannouncment.putExtra("Tecaher_Std",teacher_std);
                startActivity(teacherannouncment);
            }
        }
        else if(v==img_attendance){
            if (dft.isInternetOn() == true) {
                Intent teacherattendance = new Intent(TeacherProfileActivity.this, TeacherAttendanceActivity.class);
                teacherattendance.putExtra("clt_id", clt_id);
                teacherattendance.putExtra("usl_id", usl_id);
                teacherattendance.putExtra("School_name", school_name);
                teacherattendance.putExtra("Teacher_name", teacher_name);
                teacherattendance.putExtra("version_name", AppController.versionName);
                teacherattendance.putExtra("board_name", board_name);
                teacherattendance.putExtra("org_id",org_id);
                teacherattendance.putExtra("cls_teacher",clas_teacher);
                teacherattendance.putExtra("academic_year",academic_year);
                teacherattendance.putExtra("Teacher_div",teacher_div);
                teacherattendance.putExtra("Teacher_Shift",teacher_shift);
                teacherattendance.putExtra("Tecaher_Std",teacher_std);
                startActivity(teacherattendance);
            }
        }
        else if(v==ll_sub_list){
            if (dft.isInternetOn() == true) {
                Intent teachersub_list = new Intent(TeacherProfileActivity.this, SubjectListActivity.class);
                teachersub_list.putExtra("clt_id", clt_id);
                teachersub_list.putExtra("usl_id", usl_id);
                teachersub_list.putExtra("School_name", school_name);
                teachersub_list.putExtra("Teacher_name", teacher_name);
                teachersub_list.putExtra("version_name", AppController.versionName);
                teachersub_list.putExtra("board_name", board_name);
                teachersub_list.putExtra("cls_teacher",clas_teacher);
                teachersub_list.putExtra("academic_year",academic_year);
                teachersub_list.putExtra("org_id",org_id);
                teachersub_list.putExtra("Teacher_div",teacher_div);
                teachersub_list.putExtra("Teacher_Shift",teacher_shift);
                teachersub_list.putExtra("Tecaher_Std",teacher_std);
                startActivity(teachersub_list);
            }
        }
        else if(v==img_behaviour){
            if (dft.isInternetOn() == true) {
                Intent teacher_behaviour = new Intent(TeacherProfileActivity.this, TeacherBehaviourActivity.class);
                teacher_behaviour.putExtra("clt_id", clt_id);
                teacher_behaviour.putExtra("usl_id", usl_id);
                teacher_behaviour.putExtra("School_name", school_name);
                teacher_behaviour.putExtra("Teacher_name", teacher_name);
                teacher_behaviour.putExtra("version_name", AppController.versionName);
                teacher_behaviour.putExtra("board_name", board_name);
                teacher_behaviour.putExtra("org_id",org_id);
                teacher_behaviour.putExtra("cls_teacher",clas_teacher);
                teacher_behaviour.putExtra("academic_year",academic_year);
                teacher_behaviour.putExtra("Teacher_div",teacher_div);
                teacher_behaviour.putExtra("Teacher_Shift",teacher_shift);
                teacher_behaviour.putExtra("Tecaher_Std",teacher_std);
                startActivity(teacher_behaviour);
            }
        }
        else if(v==img_drawer){
            drawer.openDrawer(Gravity.LEFT);

        }else if(v==img_sms){
            if (dft.isInternetOn() == true) {
                Intent teacher_sms = new Intent(TeacherProfileActivity.this, TeacherSMSActivity.class);
                teacher_sms.putExtra("usl_id", usl_id);
                teacher_sms.putExtra("clt_id", clt_id);
                teacher_sms.putExtra("School_name", school_name);
                teacher_sms.putExtra("Teacher_name", teacher_name);
                teacher_sms.putExtra("version_name", AppController.versionName);
                teacher_sms.putExtra("board_name", board_name);
                teacher_sms.putExtra("org_id",org_id);
                teacher_sms.putExtra("cls_teacher",clas_teacher);
                teacher_sms.putExtra("academic_year",academic_year);
                teacher_sms.putExtra("Teacher_div",teacher_div);
                teacher_sms.putExtra("Teacher_Shift",teacher_shift);
                teacher_sms.putExtra("Tecaher_Std",teacher_std);
                startActivity(teacher_sms);
            }
        }
        else if(v==img_message){
            if (dft.isInternetOn() == true) {
                Intent teacher_messages = new Intent(TeacherProfileActivity.this, TeacherMessageActivity.class);
                teacher_messages.putExtra("usl_id", usl_id);
                teacher_messages.putExtra("clt_id", clt_id);
                teacher_messages.putExtra("School_name", school_name);
                teacher_messages.putExtra("Teacher_name", teacher_name);
                teacher_messages.putExtra("version_name", AppController.versionName);
                teacher_messages.putExtra("board_name", board_name);
                teacher_messages.putExtra("org_id",org_id);
                teacher_messages.putExtra("cls_teacher",clas_teacher);
                teacher_messages.putExtra("academic_year",academic_year);
                teacher_messages.putExtra("Teacher_div",teacher_div);
                teacher_messages.putExtra("Teacher_Shift",teacher_shift);
                teacher_messages.putExtra("Tecaher_Std",teacher_std);
                startActivity(teacher_messages);
            }
        }
    }
    //Set Icon Count
    private void setIconCount() {
        if(AppController.OnBackpressed.equalsIgnoreCase("true")) {
            badge = new BadgeView(this, img_message);
            badge.setText(String.valueOf(AppController.iconCountOnResume));
            badge_count = String.valueOf(badge.getText());
            Log.e("Badge CountResume", badge_count);
            badge.setTextSize(15);
            badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
            badge.setBadgeBackgroundColor(Color.RED);
            badge.setTextColor(Color.WHITE);
            if (badge.getText().equals("0")) {
                badge.hide();
                Log.e("Badge Countresume zero", badge_count);
            } else if (Integer.valueOf(badge_count) <= 0) {
                badge.hide();
                Log.e("Badge Count -1", badge_count);
            } else {
                badge.show();
            }
        }
        else{
            badge = new BadgeView(this, img_message);
            badge.setText(String.valueOf(AppController.iconCount));
            badge_count = String.valueOf(badge.getText());
            Log.e("Badge Count", badge_count);
            badge.setTextSize(15);
            badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
            badge.setBadgeBackgroundColor(Color.RED);
            badge.setTextColor(Color.WHITE);
            if (badge.getText().equals("0")) {
                badge.hide();
                Log.e("Badge Count zero", badge_count);
            } else if (Integer.valueOf(badge_count) <= 0) {
                badge.hide();
                Log.e("Badge Count -1", badge_count);
            } else {
                badge.show();
            }
        }

    }

    public static void Set_id(String clt_id,String usl_id,String teacher_name,String school_name,String board_name,String org_id,String teacher_div,String teacher_std,String teacher_shift, Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = resultpreLoginData.edit();
        editor.putString("clt_id", clt_id);
        editor.putString("usl_id", usl_id);
        editor.putString("teacher_name", teacher_name);
        editor.putString("school_name", school_name);
        editor.putString("board_name", board_name);
        editor.putString("org_id",org_id);
        editor.putString("teacher_div",teacher_div);
        editor.putString("teacher_std",teacher_std);
        editor.putString("teacher_shift",teacher_shift);
        editor.putString("versionName",AppController.versionName);
        editor.putString("academic_year",academic_year);

        editor.commit();
    }

    public static String get_clt_id(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("clt_id", "");
    }
    public static String get_academic_year(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("academic_year", "");
    }
    public static String get_version_Name(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("versionName", "");
    }
    public static String get_usl_id(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("usl_id", "");
    }
    public static String get_teacher_name(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("teacher_name", "");
    }
    public static String get_school_name(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("school_name", "");
    }
    public static String get_board_name(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("board_name", "");
    }
    public static String get_org_id(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("org_id", "");
    }
    public static String get_teacher_div(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("teacher_div", "");
    }
    public static String get_teacher_std(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("teacher_std", "");
    }
    public static String get_teacher_shift(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("teacher_shift", "");
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.closeDrawer(Gravity.LEFT);
        }
        else {
            try {
                AppController.OnBackpressed = "true";
                AppController.iconCount = 0;
                AppController.iconCountOnResume = 0;
                AppController.iconAnnouncementCount = 0;
                AppController.iconAnnouncementCountOnResume = 0;
                AppController.Board_name = board_name;
                try {
                    badge.hide();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent back = new Intent(Intent.ACTION_MAIN);
            back.addCategory(Intent.CATEGORY_HOME);
            back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            AppController.fromSplashScreen = "true";
            startActivity(back);
        }
    }
}
