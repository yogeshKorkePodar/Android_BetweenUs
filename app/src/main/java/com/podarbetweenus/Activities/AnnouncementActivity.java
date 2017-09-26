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
import com.podarbetweenus.Adapter.MessageAdpater;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.AnnouncementResult;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.Entity.ViewMessageResult;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Services.ListResultReceiver;
import com.podarbetweenus.Utility.AppController;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 9/28/2015.
 */
public class AnnouncementActivity extends Activity implements View.OnClickListener,ListResultReceiver.Receiver,AdapterView.OnItemClickListener {
    //UI Variables
    //Button
    Button btn_announce,btn_dashboard,btn_attendance,btn_behaviour;
    //Linear Layout
    LinearLayout lay_back_investment,ll_footer;
    //Relative Layout
    RelativeLayout leftfgf_drawer,rl_profile;
    //ProgressDialog
    ProgressDialog progressDialog;
    //ListView
    ListView list_announcement,drawerListView;
    //TextView
    TextView tv_no_record,tv_version_name,tv_version_code,tv_cancel,tv_academic_year_drawer,tv_child_name_drawer,tv_std_value_drawer,tv_roll_no_value_drawer;
    //Drawaer Layout
    DrawerLayout drawer;
    //ImageView
    ImageView img_drawer,img_icon;
    ContentDisplaceDrawerToggle mContentDisplaceToggle;
    Context context;


    //Layout Entities
    HeaderControler header;
    DataFetchService dft;
    AnnouncementAdapter announcement_adpater;
    LoginDetails login_details;
    AlarmReceiver alaram_receiver;

    String clt_id,usl_id,msd_id,board_name,school_name,versionName,announcementCount,behaviourCount,std_name,isStudentresource,msg_id,announcement_usl_id;
    String AnnouncementMethodName = "GetAnnouncementData";
    String AnnouncementReadMethodName = "UpdateAnnoucementReadStatus";
    int notificationID = 1,viewannouncementListSize,notificationId;
    ArrayList<AnnouncementResult> announcementresults;
    public ArrayList<AnnouncementResult> announcement_result = new ArrayList<AnnouncementResult>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.template_expandable_announcents);
        findViews();
        init();
        getIntentId();
        announcentTab();
        behaviourTab();
        AppController.dropdownSelected="false";
        AppController.AttenAnnounceBehaviourdropdown="true";
        AppController.Notification_send = "true";
        setDrawer();
        registerReceiver();

        //Call Webservcie for Announcement
        CallAnnouncementWebservice(clt_id, usl_id, msd_id);
    }

   /* @Override
    protected void onPause() {
        Log.i("onPause", "onPause()");
		*//* we should unregister BroadcastReceiver here*//*
        unregisterReceiver(alaram_receiver);
        super.onPause();
    }*/

    @Override
    protected void onRestart() {
        super.onRestart();
        	/* we register BroadcastReceiver here*/
        registerReceiver();

    }

    private void setDrawer() {
        try {
            if (AppController.SiblingActivity.equalsIgnoreCase("true")) {
                if (AppController.isStudentResourcePresent.equalsIgnoreCase("0")) {
                    int[] icons_with_sibling = {R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sib_48x48, R.drawable.payfeesonline_48x48, R.drawable.studentresources_48x48, R.drawable.att_48x48, R.drawable.parentinformation_48x48, /*R.drawable.studentresources_48x48,*/ R.drawable.setting1_hdpi,
                            R.drawable.logout1_hdpi, R.drawable.info};
                    String[] data_with_sibling = {"Dashboard", "Messages", "Sibling", "Fees Information", "Student Resources", "Attendance", "Student Information"/*,"Class Room Videos"*/, "Setting",
                            "Sign Out", "About"};
                    drawerListView.setAdapter(new CustomAccount(AnnouncementActivity.this, android.R.layout.simple_list_item_1, icons_with_sibling, data_with_sibling));
                    drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if (position == 0) {
                                Intent profile = new Intent(AnnouncementActivity.this, Profile_Sibling.class);
                                AppController.msd_ID = msd_id;
                                AppController.clt_id = clt_id;
                                AppController.school_name = school_name;
                                AppController.usl_id = usl_id;
                                profile.putExtra("msd_ID", AppController.msd_ID);
                                profile.putExtra("clt_id", AppController.clt_id);
                                profile.putExtra("School_name", AppController.school_name);
                                profile.putExtra("board_name", AppController.Board_name);
                                profile.putExtra("usl_id", usl_id);
                                profile.putExtra("Std Name", std_name);
                                profile.putExtra("annpuncement_count", AppController.announcementCount);
                                profile.putExtra("behaviour_count", behaviourCount);
                                profile.putExtra("version_name", AppController.versionName);
                                profile.putExtra("version_code", AppController.versionCode);
                                profile.putExtra("isStudentResource", isStudentresource);
                                startActivity(profile);
                            } else if (position == 1) {
                                if (dft.isInternetOn() == true) {
                                    Intent dashboard = new Intent(AnnouncementActivity.this, DashboardActivity.class);
                                    AppController.Board_name = board_name;
                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_id;
                                    AppController.usl_id = usl_id;
                                    AppController.school_name = school_name;
                                    dashboard.putExtra("clt_id", AppController.clt_id);
                                    dashboard.putExtra("msd_ID", AppController.msd_ID);
                                    dashboard.putExtra("usl_id", AppController.usl_id);
                                    dashboard.putExtra("Std Name", std_name);
                                    dashboard.putExtra("board_name", board_name);
                                    dashboard.putExtra("School_name", AppController.school_name);
                                    dashboard.putExtra("version_name", AppController.versionName);
                                    dashboard.putExtra("version_code", AppController.versionCode);
                                    dashboard.putExtra("isStudentResource", isStudentresource);
                                    dashboard.putExtra("annpuncement_count", AppController.announcementCount);
                                    dashboard.putExtra("behaviour_count", behaviourCount);
                                    startActivity(dashboard);
                                }
                            } else if (position == 2) {
                                Intent sibling = new Intent(AnnouncementActivity.this, SiblingActivity.class);
                                AppController.clt_id = clt_id;
                                AppController.usl_id = usl_id;
                                AppController.msd_ID = msd_id;
                                sibling.putExtra("clt_id", AppController.clt_id);
                                sibling.putExtra("usl_id", AppController.usl_id);
                                sibling.putExtra("School_name", AppController.school_name);
                                sibling.putExtra("msd_ID", AppController.msd_ID);
                                sibling.putExtra("board_name", board_name);
                                sibling.putExtra("Std Name", std_name);
                                sibling.putExtra("annpuncement_count", AppController.announcementCount);
                                sibling.putExtra("behaviour_count", behaviourCount);
                                sibling.putExtra("isStudentResource", isStudentresource);
                                sibling.putExtra("version_name", AppController.versionName);
                                sibling.putExtra("version_code", AppController.versionCode);
                                startActivity(sibling);
                            } else if (position == 3) {
                                if (dft.isInternetOn() == true) {
                                    Intent feesInfo = new Intent(AnnouncementActivity.this, FeesInfoActivity.class);

                                    feesInfo.putExtra("board_name", board_name);
                                    feesInfo.putExtra("clt_id", AppController.clt_id);
                                    feesInfo.putExtra("msd_ID", AppController.msd_ID);
                                    feesInfo.putExtra("usl_id", AppController.usl_id);
                                    feesInfo.putExtra("School_name", AppController.school_name);
                                    feesInfo.putExtra("Std Name", std_name);
                                    feesInfo.putExtra("version_name", AppController.versionName);
                                    feesInfo.putExtra("annpuncement_count", AppController.announcementCount);
                                    feesInfo.putExtra("behaviour_count", behaviourCount);
                                    feesInfo.putExtra("isStudentResource", isStudentresource);
                                    feesInfo.putExtra("version_code", AppController.versionCode);
                                    startActivity(feesInfo);
                                }
                            } else if (position == 4) {
                                if (dft.isInternetOn() == true) {
                                    Intent student_resource = new Intent(AnnouncementActivity.this, StudentResourcesActivity.class);
                                    student_resource.putExtra("board_name", board_name);
                                    student_resource.putExtra("clt_id", AppController.clt_id);
                                    student_resource.putExtra("msd_ID", AppController.msd_ID);
                                    student_resource.putExtra("usl_id", AppController.usl_id);
                                    student_resource.putExtra("School_name", AppController.school_name);
                                    student_resource.putExtra("Std Name", std_name);
                                    student_resource.putExtra("isStudentResource", isStudentresource);
                                    student_resource.putExtra("version_name", AppController.versionName);
                                    student_resource.putExtra("version_code", AppController.versionCode);
                                    student_resource.putExtra("behaviour_count", behaviourCount);
                                    student_resource.putExtra("annpuncement_count", AppController.announcementCount);
                                    startActivity(student_resource);
                                }
                            } else if (position == 5) {
                                if (dft.isInternetOn() == true) {
                                    Intent attendance = new Intent(AnnouncementActivity.this, AttendanceActivity.class);
                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_id;
                                    AppController.usl_id = usl_id;
                                    attendance.putExtra("clt_id", AppController.clt_id);
                                    attendance.putExtra("msd_ID", AppController.msd_ID);
                                    attendance.putExtra("usl_id", AppController.usl_id);
                                    attendance.putExtra("board_name", board_name);
                                    attendance.putExtra("Std Name", std_name);
                                    attendance.putExtra("isStudentResource", isStudentresource);
                                    attendance.putExtra("School_name", AppController.school_name);
                                    attendance.putExtra("version_name", AppController.versionName);
                                    attendance.putExtra("version_code", AppController.versionCode);
                                    attendance.putExtra("behaviour_count", behaviourCount);
                                    attendance.putExtra("annpuncement_count", AppController.announcementCount);
                                    startActivity(attendance);
                                }
                            } else if (position == 6) {
                                if (dft.isInternetOn() == true) {
                                    Intent parentinfo = new Intent(AnnouncementActivity.this, ParentInformationActivity.class);
                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_id;
                                    AppController.usl_id = usl_id;
                                    parentinfo.putExtra("clt_id", AppController.clt_id);
                                    parentinfo.putExtra("msd_ID", AppController.msd_ID);
                                    parentinfo.putExtra("usl_id", AppController.usl_id);
                                    parentinfo.putExtra("board_name", board_name);
                                    parentinfo.putExtra("Std Name", std_name);
                                    parentinfo.putExtra("isStudentResource", isStudentresource);
                                    parentinfo.putExtra("School_name", AppController.school_name);
                                    parentinfo.putExtra("version_name", AppController.versionName);
                                    parentinfo.putExtra("version_code", AppController.versionCode);
                                    parentinfo.putExtra("annpuncement_count", AppController.announcementCount);
                                    parentinfo.putExtra("behaviour_count", behaviourCount);
                                    startActivity(parentinfo);
                                }
                            } else if (position == 7) {
                                Intent setting = new Intent(AnnouncementActivity.this, SettingActivity.class);
                                AppController.usl_id = usl_id;
                                setting.putExtra("usl_id", usl_id);
                                setting.putExtra("version_name", AppController.versionName);
                                setting.putExtra("Std Name", std_name);
                                setting.putExtra("isStudentResource", isStudentresource);
                                setting.putExtra("version_code", AppController.versionCode);
                                startActivity(setting);
                            } else if (position == 8) {
                                Intent signout = new Intent(AnnouncementActivity.this, LoginActivity.class);
                                Constant.SetLoginData("", "", AnnouncementActivity.this);
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancel(notificationID);
                                AppController.drawerSignOut = "true";
                                LoginActivity.loggedIn = "false";
                                startActivity(signout);
                            } else if (position == 9) {
                                final Dialog alertDialog = new Dialog(AnnouncementActivity.this);
                                LayoutInflater inflater = getLayoutInflater();
                                ;
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
                } else {
                    int[] icons_with_sibling = {R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sib_48x48, R.drawable.payfeesonline_48x48, R.drawable.att_48x48, R.drawable.parentinformation_48x48, /*R.drawable.studentresources_48x48,*/ R.drawable.setting1_hdpi,
                            R.drawable.logout1_hdpi, R.drawable.info};
                    String[] data_with_sibling = {"Dashboard", "Messages", "Sibling", "Fees Information", "Attendance", "Student Information"/*,"Class Room Videos"*/, "Setting",
                            "Sign Out", "About"};
                    drawerListView.setAdapter(new CustomAccount(AnnouncementActivity.this, android.R.layout.simple_list_item_1, icons_with_sibling, data_with_sibling));
                    drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if (position == 0) {
                                Intent profile = new Intent(AnnouncementActivity.this, Profile_Sibling.class);
                                AppController.msd_ID = msd_id;
                                AppController.clt_id = clt_id;
                                AppController.school_name = school_name;
                                AppController.usl_id = usl_id;
                                profile.putExtra("msd_ID", AppController.msd_ID);
                                profile.putExtra("clt_id", AppController.clt_id);
                                profile.putExtra("School_name", AppController.school_name);
                                profile.putExtra("board_name", board_name);
                                profile.putExtra("usl_id", usl_id);
                                profile.putExtra("Std Name", std_name);
                                profile.putExtra("isStudentResource", isStudentresource);
                                profile.putExtra("version_name", AppController.versionName);
                                profile.putExtra("version_code", AppController.versionCode);
                                profile.putExtra("annpuncement_count", AppController.announcementCount);
                                profile.putExtra("behaviour_count", behaviourCount);
                                startActivity(profile);
                            } else if (position == 1) {
                                if (dft.isInternetOn() == true) {
                                    Intent dashboard = new Intent(AnnouncementActivity.this, DashboardActivity.class);
                                    AppController.Board_name = board_name;
                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_id;
                                    AppController.usl_id = usl_id;
                                    AppController.school_name = school_name;
                                    dashboard.putExtra("clt_id", AppController.clt_id);
                                    dashboard.putExtra("msd_ID", AppController.msd_ID);
                                    dashboard.putExtra("usl_id", AppController.usl_id);
                                    dashboard.putExtra("Std Name", std_name);
                                    dashboard.putExtra("isStudentResource", isStudentresource);
                                    dashboard.putExtra("board_name", board_name);
                                    dashboard.putExtra("School_name", AppController.school_name);
                                    dashboard.putExtra("version_name", AppController.versionName);
                                    dashboard.putExtra("version_code", AppController.versionCode);
                                    dashboard.putExtra("annpuncement_count", AppController.announcementCount);
                                    dashboard.putExtra("behaviour_count", behaviourCount);
                                    startActivity(dashboard);
                                }
                            } else if (position == 2) {
                                Intent sibling = new Intent(AnnouncementActivity.this, SiblingActivity.class);
                                AppController.clt_id = clt_id;
                                AppController.usl_id = usl_id;
                                AppController.msd_ID = msd_id;
                                sibling.putExtra("clt_id", AppController.clt_id);
                                sibling.putExtra("usl_id", AppController.usl_id);
                                sibling.putExtra("School_name", AppController.school_name);
                                sibling.putExtra("msd_ID", AppController.msd_ID);
                                sibling.putExtra("board_name", board_name);
                                sibling.putExtra("Std Name", std_name);
                                sibling.putExtra("isStudentResource", isStudentresource);
                                sibling.putExtra("annpuncement_count", AppController.announcementCount);
                                sibling.putExtra("behaviour_count", behaviourCount);
                                sibling.putExtra("version_name", AppController.versionName);
                                sibling.putExtra("version_code", AppController.versionCode);
                                startActivity(sibling);
                            } else if (position == 3) {
                                if (dft.isInternetOn() == true) {
                                    Intent feesInfo = new Intent(AnnouncementActivity.this, FeesInfoActivity.class);

                                    feesInfo.putExtra("board_name", board_name);
                                    feesInfo.putExtra("clt_id", AppController.clt_id);
                                    feesInfo.putExtra("msd_ID", AppController.msd_ID);
                                    feesInfo.putExtra("usl_id", AppController.usl_id);
                                    feesInfo.putExtra("School_name", AppController.school_name);
                                    feesInfo.putExtra("Std Name", std_name);
                                    feesInfo.putExtra("version_name", AppController.versionName);
                                    feesInfo.putExtra("annpuncement_count", AppController.announcementCount);
                                    feesInfo.putExtra("behaviour_count", behaviourCount);
                                    feesInfo.putExtra("isStudentResource", isStudentresource);
                                    feesInfo.putExtra("version_code", AppController.versionCode);
                                    startActivity(feesInfo);
                                }
                            } else if (position == 4) {
                                if (dft.isInternetOn() == true) {
                                    Intent attendance = new Intent(AnnouncementActivity.this, AttendanceActivity.class);

                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_id;
                                    AppController.usl_id = usl_id;
                                    attendance.putExtra("clt_id", AppController.clt_id);
                                    attendance.putExtra("msd_ID", AppController.msd_ID);
                                    attendance.putExtra("usl_id", AppController.usl_id);
                                    attendance.putExtra("board_name", board_name);
                                    attendance.putExtra("Std Name", std_name);
                                    attendance.putExtra("School_name", AppController.school_name);
                                    attendance.putExtra("version_name", AppController.versionName);
                                    attendance.putExtra("version_code", AppController.versionCode);
                                    attendance.putExtra("isStudentResource", isStudentresource);
                                    attendance.putExtra("annpuncement_count", AppController.announcementCount);
                                    attendance.putExtra("behaviour_count", behaviourCount);
                                    startActivity(attendance);
                                }
                            } else if (position == 5) {
                                if (dft.isInternetOn() == true) {
                                    Intent parentinfo = new Intent(AnnouncementActivity.this, ParentInformationActivity.class);

                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_id;
                                    AppController.usl_id = usl_id;
                                    parentinfo.putExtra("clt_id", AppController.clt_id);
                                    parentinfo.putExtra("msd_ID", AppController.msd_ID);
                                    parentinfo.putExtra("usl_id", AppController.usl_id);
                                    parentinfo.putExtra("board_name", board_name);
                                    parentinfo.putExtra("Std Name", std_name);
                                    parentinfo.putExtra("isStudentResource", isStudentresource);
                                    parentinfo.putExtra("School_name", AppController.school_name);
                                    parentinfo.putExtra("version_name", AppController.versionName);
                                    parentinfo.putExtra("version_code", AppController.versionCode);
                                    parentinfo.putExtra("annpuncement_count", AppController.announcementCount);
                                    parentinfo.putExtra("behaviour_count", behaviourCount);
                                    startActivity(parentinfo);
                                }
                            } else if (position == 6) {
                                Intent setting = new Intent(AnnouncementActivity.this, SettingActivity.class);
                                AppController.usl_id = usl_id;
                                setting.putExtra("usl_id", usl_id);
                                setting.putExtra("Std Name", std_name);
                                setting.putExtra("isStudentResource", isStudentresource);
                                setting.putExtra("version_name", AppController.versionName);
                                setting.putExtra("version_code", AppController.versionCode);
                                setting.putExtra("annpuncement_count", AppController.announcementCount);
                                setting.putExtra("behaviour_count", behaviourCount);
                                startActivity(setting);
                            } else if (position == 7) {
                                Intent signout = new Intent(AnnouncementActivity.this, LoginActivity.class);
                                Constant.SetLoginData("", "", AnnouncementActivity.this);
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancel(notificationID);
                                LoginActivity.loggedIn = "false";
                                AppController.drawerSignOut = "true";
                                startActivity(signout);
                            } else if (position == 8) {
                                final Dialog alertDialog = new Dialog(AnnouncementActivity.this);
                                //  final AlertDialog alertDialog = builder.create();
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
            } else if (AppController.SiblingActivity.equalsIgnoreCase("false")) {
                if (AppController.isStudentResourcePresent.equalsIgnoreCase("0")) {
                    int[] icons_without_sibling = {R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.payfeesonline_48x48, R.drawable.studentresources_48x48, R.drawable.att_48x48, R.drawable.parentinformation_48x48,/* R.drawable.studentresources_48x48,*/ R.drawable.setting1_hdpi,
                            R.drawable.logout1_hdpi, R.drawable.info};
                    final String[] data_without_sibling = {"Dashboard", "Messages", "Fees Information", "Student Resources", "Attendance", "Student Information",/* "Class Room Videos",*/ "Setting",
                            "Sign Out", "About"};
                    drawerListView.setAdapter(new CustomAccount(AnnouncementActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));
                    drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if (position == 0) {
                                Intent profile = new Intent(AnnouncementActivity.this, ProfileActivity.class);

                                AppController.msd_ID = msd_id;
                                AppController.clt_id = clt_id;
                                AppController.school_name = school_name;
                                profile.putExtra("msd_ID", AppController.msd_ID);
                                profile.putExtra("clt_id", AppController.clt_id);
                                profile.putExtra("isStudentResource", isStudentresource);
                                profile.putExtra("School_name", AppController.school_name);
                                profile.putExtra("usl_id", AppController.usl_id);
                                profile.putExtra("board_name", AppController.Board_name);
                                profile.putExtra("Std Name", std_name);
                                profile.putExtra("version_name", AppController.versionName);
                                profile.putExtra("annpuncement_count", AppController.announcementCount);
                                profile.putExtra("behaviour_count", behaviourCount);
                                startActivity(profile);
                            } else if (position == 1) {
                                if (dft.isInternetOn() == true) {
                                    Intent dashboard = new Intent(AnnouncementActivity.this, DashboardActivity.class);
                                    AppController.Board_name = board_name;
                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_id;
                                    AppController.usl_id = usl_id;
                                    AppController.school_name = school_name;
                                    dashboard.putExtra("clt_id", AppController.clt_id);
                                    dashboard.putExtra("msd_ID", AppController.msd_ID);
                                    dashboard.putExtra("usl_id", AppController.usl_id);
                                    dashboard.putExtra("Std Name", std_name);
                                    dashboard.putExtra("board_name", board_name);
                                    dashboard.putExtra("isStudentResource", isStudentresource);
                                    dashboard.putExtra("School_name", AppController.school_name);
                                    dashboard.putExtra("version_name", AppController.versionName);
                                    dashboard.putExtra("version_code", AppController.versionCode);
                                    dashboard.putExtra("annpuncement_count", AppController.announcementCount);
                                    dashboard.putExtra("behaviour_count", behaviourCount);
                                    startActivity(dashboard);
                                }
                            } else if (position == 2) {
                                if (dft.isInternetOn() == true) {
                                    Intent feesInfo = new Intent(AnnouncementActivity.this, FeesInfoActivity.class);

                                    feesInfo.putExtra("board_name", AppController.Board_name);
                                    feesInfo.putExtra("clt_id", AppController.clt_id);
                                    feesInfo.putExtra("msd_ID", AppController.msd_ID);
                                    feesInfo.putExtra("usl_id", AppController.usl_id);
                                    feesInfo.putExtra("isStudentResource", isStudentresource);
                                    feesInfo.putExtra("School_name", AppController.school_name);
                                    feesInfo.putExtra("Std Name", std_name);
                                    feesInfo.putExtra("version_name", AppController.versionName);
                                    feesInfo.putExtra("behaviour_count", behaviourCount);
                                    feesInfo.putExtra("annpuncement_count", AppController.announcementCount);
                                    startActivity(feesInfo);
                                }
                            } else if (position == 3) {
                                if (dft.isInternetOn() == true) {
                                    Intent student_resource = new Intent(AnnouncementActivity.this, StudentResourcesActivity.class);

                                    student_resource.putExtra("board_name", AppController.Board_name);
                                    student_resource.putExtra("clt_id", AppController.clt_id);
                                    student_resource.putExtra("msd_ID", AppController.msd_ID);
                                    student_resource.putExtra("usl_id", AppController.usl_id);
                                    student_resource.putExtra("School_name", AppController.school_name);
                                    student_resource.putExtra("version_name", AppController.versionName);
                                    student_resource.putExtra("annpuncement_count", AppController.announcementCount);
                                    student_resource.putExtra("behaviour_count", behaviourCount);
                                    student_resource.putExtra("isStudentResource", isStudentresource);
                                    student_resource.putExtra("Std Name", std_name);
                                    startActivity(student_resource);
                                }
                            } else if (position == 4) {
                                if (dft.isInternetOn() == true) {
                                    Intent attendance = new Intent(AnnouncementActivity.this, AttendanceActivity.class);

                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_id;
                                    AppController.usl_id = usl_id;
                                    attendance.putExtra("clt_id", AppController.clt_id);
                                    attendance.putExtra("msd_ID", AppController.msd_ID);
                                    attendance.putExtra("usl_id", AppController.usl_id);
                                    attendance.putExtra("School_name", AppController.school_name);
                                    attendance.putExtra("board_name", AppController.Board_name);
                                    attendance.putExtra("Std Name", std_name);
                                    attendance.putExtra("isStudentResource", isStudentresource);
                                    attendance.putExtra("annpuncement_count", announcementCount);
                                    attendance.putExtra("behaviour_count", behaviourCount);
                                    attendance.putExtra("version_name", AppController.versionName);
                                    startActivity(attendance);
                                }
                            } else if (position == 5) {
                                if (dft.isInternetOn() == true) {
                                    Intent parentinfo = new Intent(AnnouncementActivity.this, ParentInformationActivity.class);
                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_id;
                                    AppController.usl_id = usl_id;
                                    AppController.Board_name = board_name;
                                    parentinfo.putExtra("clt_id", AppController.clt_id);
                                    parentinfo.putExtra("msd_ID", AppController.msd_ID);
                                    parentinfo.putExtra("usl_id", AppController.usl_id);
                                    parentinfo.putExtra("board_name", AppController.Board_name);
                                    parentinfo.putExtra("Std Name", std_name);
                                    parentinfo.putExtra("isStudentResource", isStudentresource);
                                    parentinfo.putExtra("School_name", AppController.school_name);
                                    parentinfo.putExtra("version_name", AppController.versionName);
                                    parentinfo.putExtra("annpuncement_count", AppController.announcementCount);
                                    parentinfo.putExtra("behaviour_count", behaviourCount);
                                    startActivity(parentinfo);
                                }
                            } else if (position == 6) {
                                Intent setting = new Intent(AnnouncementActivity.this, SettingActivity.class);
                                AppController.usl_id = usl_id;
                                setting.putExtra("usl_id", AppController.usl_id);
                                setting.putExtra("Std Name", std_name);
                                setting.putExtra("version_name", AppController.versionName);
                                setting.putExtra("isStudentResource", isStudentresource);
                                setting.putExtra("annpuncement_count", AppController.announcementCount);
                                setting.putExtra("behaviour_count", behaviourCount);
                                startActivity(setting);
                            } else if (position == 7) {
                                Intent signout = new Intent(AnnouncementActivity.this, LoginActivity.class);
                                Constant.SetLoginData("", "", AnnouncementActivity.this);
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancel(notificationID);
                                LoginActivity.loggedIn = "false";
                                AppController.drawerSignOut = "true";
                                startActivity(signout);
                            } else if (position == 8) {
                                final Dialog alertDialog = new Dialog(AnnouncementActivity.this);
                                LayoutInflater inflater = getLayoutInflater();
                                ;
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
                } else {
                    int[] icons_without_sibling = {R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.payfeesonline_48x48, R.drawable.att_48x48, R.drawable.parentinformation_48x48,/* R.drawable.studentresources_48x48,*/ R.drawable.setting1_hdpi,
                            R.drawable.logout1_hdpi, R.drawable.info};
                    final String[] data_without_sibling = {"Dashboard", "Messages", "Fees Information", "Attendance", "Student Information",/* "Class Room Videos",*/ "Setting",
                            "Sign Out", "About"};
                    drawerListView.setAdapter(new CustomAccount(AnnouncementActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));
                    drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if (position == 0) {
                                Intent profile = new Intent(AnnouncementActivity.this, ProfileActivity.class);
                                AppController.msd_ID = msd_id;
                                AppController.clt_id = clt_id;
                                AppController.school_name = school_name;
                                profile.putExtra("msd_ID", AppController.msd_ID);
                                profile.putExtra("clt_id", AppController.clt_id);
                                profile.putExtra("School_name", AppController.school_name);
                                profile.putExtra("usl_id", AppController.usl_id);
                                profile.putExtra("board_name", AppController.Board_name);
                                profile.putExtra("Std Name", std_name);
                                profile.putExtra("isStudentResource", isStudentresource);
                                profile.putExtra("version_name", AppController.versionName);
                                profile.putExtra("annpuncement_count", AppController.announcementCount);
                                profile.putExtra("behaviour_count", behaviourCount);
                                startActivity(profile);
                            } else if (position == 1) {
                                if (dft.isInternetOn() == true) {
                                    Intent dashboard = new Intent(AnnouncementActivity.this, DashboardActivity.class);
                                    AppController.Board_name = board_name;
                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_id;
                                    AppController.usl_id = usl_id;
                                    AppController.school_name = school_name;
                                    dashboard.putExtra("clt_id", AppController.clt_id);
                                    dashboard.putExtra("msd_ID", AppController.msd_ID);
                                    dashboard.putExtra("usl_id", AppController.usl_id);
                                    dashboard.putExtra("Std Name", std_name);
                                    dashboard.putExtra("board_name", board_name);
                                    dashboard.putExtra("isStudentResource", isStudentresource);
                                    dashboard.putExtra("School_name", AppController.school_name);
                                    dashboard.putExtra("version_name", AppController.versionName);
                                    dashboard.putExtra("version_code", AppController.versionCode);
                                    dashboard.putExtra("annpuncement_count", AppController.announcementCount);
                                    dashboard.putExtra("behaviour_count", behaviourCount);
                                    startActivity(dashboard);
                                }
                            } else if (position == 2) {
                                if (dft.isInternetOn() == true) {
                                    Intent feesInfo = new Intent(AnnouncementActivity.this, FeesInfoActivity.class);
                                    feesInfo.putExtra("board_name", AppController.Board_name);
                                    feesInfo.putExtra("clt_id", AppController.clt_id);
                                    feesInfo.putExtra("msd_ID", AppController.msd_ID);
                                    feesInfo.putExtra("usl_id", AppController.usl_id);
                                    feesInfo.putExtra("School_name", AppController.school_name);
                                    feesInfo.putExtra("Std Name", std_name);
                                    feesInfo.putExtra("version_name", AppController.versionName);
                                    feesInfo.putExtra("isStudentResource", isStudentresource);
                                    feesInfo.putExtra("annpuncement_count", AppController.announcementCount);
                                    feesInfo.putExtra("behaviour_count", behaviourCount);
                                    startActivity(feesInfo);
                                }
                            } else if (position == 3) {
                                if (dft.isInternetOn() == true) {
                                    Intent attendance = new Intent(AnnouncementActivity.this, AttendanceActivity.class);
                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_id;
                                    AppController.usl_id = usl_id;
                                    attendance.putExtra("clt_id", AppController.clt_id);
                                    attendance.putExtra("msd_ID", AppController.msd_ID);
                                    attendance.putExtra("usl_id", AppController.usl_id);
                                    attendance.putExtra("School_name", AppController.school_name);
                                    attendance.putExtra("board_name", AppController.Board_name);
                                    attendance.putExtra("Std Name", std_name);
                                    attendance.putExtra("isStudentResource", isStudentresource);
                                    attendance.putExtra("version_name", AppController.versionName);
                                    attendance.putExtra("annpuncement_count", AppController.announcementCount);
                                    attendance.putExtra("behaviour_count", behaviourCount);
                                    startActivity(attendance);
                                }
                            } else if (position == 4) {
                                if (dft.isInternetOn() == true) {
                                    Intent parentinfo = new Intent(AnnouncementActivity.this, ParentInformationActivity.class);
                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_id;
                                    AppController.usl_id = usl_id;
                                    AppController.Board_name = board_name;
                                    parentinfo.putExtra("clt_id", AppController.clt_id);
                                    parentinfo.putExtra("msd_ID", AppController.msd_ID);
                                    parentinfo.putExtra("usl_id", AppController.usl_id);
                                    parentinfo.putExtra("board_name", AppController.Board_name);
                                    parentinfo.putExtra("Std Name", std_name);
                                    parentinfo.putExtra("isStudentResource", isStudentresource);
                                    parentinfo.putExtra("School_name", AppController.school_name);
                                    parentinfo.putExtra("version_name", AppController.versionName);
                                    parentinfo.putExtra("annpuncement_count", AppController.announcementCount);
                                    parentinfo.putExtra("behaviour_count", behaviourCount);
                                    startActivity(parentinfo);
                                }
                            } else if (position == 5) {
                                Intent setting = new Intent(AnnouncementActivity.this, SettingActivity.class);
                                AppController.usl_id = usl_id;
                                setting.putExtra("usl_id", AppController.usl_id);
                                setting.putExtra("Std Name", std_name);
                                setting.putExtra("version_name", AppController.versionName);
                                setting.putExtra("annpuncement_count", AppController.announcementCount);
                                setting.putExtra("behaviour_count", behaviourCount);
                                setting.putExtra("isStudentResource", isStudentresource);
                                startActivity(setting);
                            } else if (position == 6) {
                                try {
                                    Intent signout = new Intent(AnnouncementActivity.this, LoginActivity.class);
                                    Constant.SetLoginData("", "", AnnouncementActivity.this);
                                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.cancel(notificationID);
                                    LoginActivity.loggedIn = "false";
                                    AppController.drawerSignOut = "true";
                                    startActivity(signout);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (position == 7) {
                                final Dialog alertDialog = new Dialog(AnnouncementActivity.this);
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
            }
            Constant.setDrawerData(AppController.drawerAcademic_year, AppController.drawerChild_name, AppController.drawerStd, AppController.drawerRoll_no, AnnouncementActivity.this);
            setDrawerData();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getIntentId() {
        Intent intent = getIntent();
        clt_id = intent.getStringExtra("clt_id");
        msd_id = intent.getStringExtra("msd_ID");
        usl_id = intent.getStringExtra("usl_id");
        board_name = intent.getStringExtra("board_name");
        school_name = intent.getStringExtra("School_name");
        versionName = intent.getStringExtra("version_name");
        announcementCount = intent.getStringExtra("annpuncement_count");
        behaviourCount = intent.getStringExtra("behaviour_count");
        std_name = intent.getStringExtra("Std Name");
        isStudentresource = intent.getStringExtra("isStudentResource");
        AppController.versionName = versionName;
        AppController.clt_id = clt_id;
        AppController.msd_ID = msd_id;
        AppController.usl_id = usl_id;
        AppController.school_name = school_name;
        AppController.Board_name = board_name;
        Bundle bundle=intent.getExtras();
        announcement_result =  (ArrayList<AnnouncementResult>)bundle.getSerializable("results");
        notificationId = getIntent().getExtras().getInt("notificationID");
        Log.e("Notification id_Dash...",String.valueOf(notificationId));
    }

    private void announcentTab() {

            if (Integer.valueOf(announcementCount) > 0) {
                btn_announce.setVisibility(View.VISIBLE);
            } else {
                btn_announce.setVisibility(View.GONE);
                if (Integer.valueOf(announcementCount) == 0 && Integer.valueOf(behaviourCount) == 0) {
                    ll_footer.setWeightSum(2.0f);
                } else {
                    ll_footer.setWeightSum(3.0f);
                }
            }

    }
    private void behaviourTab() {
        try {
            if (Integer.valueOf(behaviourCount) > 0) {
                btn_behaviour.setVisibility(View.VISIBLE);
            } else {
                btn_behaviour.setVisibility(View.GONE);

                if(Integer.valueOf(announcementCount)==0 && Integer.valueOf(behaviourCount)==0){
                    ll_footer.setWeightSum(2.0f);
                }
                else{
                    ll_footer.setWeightSum(3.0f);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void init() {

        img_drawer.setOnClickListener(this);
        btn_announce.setOnClickListener(this);
        btn_attendance.setOnClickListener(this);
        btn_behaviour.setOnClickListener(this);
        btn_dashboard.setOnClickListener(this);
        lay_back_investment.setOnClickListener(this);
        header = new HeaderControler(this,true,false,"Announcement");
        progressDialog = Constant.getProgressDialog(this);
        dft = new DataFetchService(this);
        list_announcement.setOnItemClickListener(this);
        login_details = new LoginDetails();
    }

    private void findViews() {
        //Button
        btn_announce = (Button) findViewById(R.id.btn_announce);
        btn_attendance = (Button) findViewById(R.id.btn_attendance);
        btn_behaviour = (Button) findViewById(R.id.btn_behaviour);
        btn_dashboard = (Button) findViewById(R.id.btn_dashboard);
        //Linear Layout
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
        ll_footer = (LinearLayout) findViewById(R.id.ll_footer);
        //ListView
        list_announcement = (ListView) findViewById(R.id.list_announcement);
        //TextView
        tv_no_record = (TextView) findViewById(R.id.tv_no_record);
        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
        tv_version_code = (TextView) findViewById(R.id.tv_version_code);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_academic_year_drawer = (TextView) findViewById(R.id.tv_academic_year_drawer);
        tv_child_name_drawer = (TextView) findViewById(R.id.tv_child_name_drawer);
        tv_std_value_drawer = (TextView) findViewById(R.id.tv_std_value_drawer);
        tv_roll_no_value_drawer = (TextView) findViewById(R.id.tv_roll_no_value_drawer);

        //ImageView
        img_drawer = (ImageView) findViewById(R.id.img_drawer);
        img_icon = (ImageView) findViewById(R.id.img_icon);
        //Button
        btn_dashboard.setBackgroundColor(Color.parseColor("#D6D6CC"));
        btn_behaviour.setBackgroundColor(Color.parseColor("#D6D6CC"));
        btn_attendance.setBackgroundColor(Color.parseColor("#D6D6CC"));
        btn_announce.setBackgroundColor(Color.parseColor("#2d2c23"));
        btn_dashboard.setTextColor(Color.BLACK);
        btn_announce.setTextColor(Color.WHITE);
        btn_attendance.setTextColor(Color.BLACK);
        btn_behaviour.setTextColor(Color.BLACK);
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
        int width = getResources().getDisplayMetrics().widthPixels/2 +30;
        DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) leftfgf_drawer.getLayoutParams();
        lp.width = width + 30;
        leftfgf_drawer.setLayoutParams(lp);

    }
    private void CallAnnouncementWebservice(String clt_id, final String usl_id,String msd_id) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getannaouncement(clt_id, usl_id, msd_id, AnnouncementMethodName, Request.Method.POST,
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
                                list_announcement.setVisibility(View.VISIBLE);
                                setUIData();
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            } else if(login_details.Status.equalsIgnoreCase("0")) {

                                tv_no_record.setText("No Records Found");
                                tv_no_record.setVisibility(View.VISIBLE);
                                list_announcement.setVisibility(View.GONE);
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

    @Override
    protected void onResume() {
        super.onResume();
        if(AppController.drawerSignOut.equalsIgnoreCase("true")) {
            Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
            AppController.ProfileWithoutSibling = "false";
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            AppController.Board_name = board_name;
            AppController.school_name = school_name;
            AppController.versionName = versionName;
            AppController.usl_id = usl_id;
            AppController.clt_id = clt_id;
            AppController.msd_ID = msd_id;
            AppController.school_name = school_name;
            AppController.versionName = versionName;
            intent.putExtra("usl_id", AppController.usl_id);
            intent.putExtra("msd_ID", AppController.msd_ID);
            intent.putExtra("version_name", AppController.versionName);
            intent.putExtra("clt_id", AppController.clt_id);
            intent.putExtra("School_name", AppController.school_name);
            intent.putExtra("board_name", AppController.Board_name);
            intent.putExtra("Sibling", AppController.SiblingListSize);
            intent.putExtra("isStudentResource",isStudentresource);
            startActivity(intent);
        }
    }
    private void setUIData() {
        viewannouncementListSize = login_details.AnnouncementResult.size();
        AppController.viewAnnouncementListSize = viewannouncementListSize;
        announcement_adpater = new AnnouncementAdapter(AnnouncementActivity.this,login_details.AnnouncementResult);
        list_announcement.setAdapter(announcement_adpater);
    }
    private void setDrawerData() {
        tv_academic_year_drawer.setText(Constant.GetAcademicYear(this));
        tv_child_name_drawer.setText(Constant.GetChildName(this));
        tv_std_value_drawer.setText(Constant.GetStandard(this));
        tv_roll_no_value_drawer.setText(Constant.GetRollNo(this));
    }
    @Override
    public void onClick(View v) {
        if(v==img_drawer)
        {
            drawer.openDrawer(Gravity.RIGHT);
        }
        else if(v==btn_announce)
        {  if (dft.isInternetOn() == true) {
            Intent announce = new Intent(AnnouncementActivity.this, AnnouncementActivity.class);
            AppController.clt_id = clt_id;
            AppController.msd_ID = msd_id;
            AppController.usl_id = usl_id;
            AppController.school_name = school_name;
            announce.putExtra("clt_id", AppController.clt_id);
            announce.putExtra("msd_ID", AppController.msd_ID);
            announce.putExtra("usl_id", AppController.usl_id);
            announce.putExtra("board_name", board_name);
            announce.putExtra("version_name", AppController.versionName);
            AppController.versionName = versionName;
            announce.putExtra("isStudentResource", isStudentresource);
            announce.putExtra("annpuncement_count", announcementCount);
            announce.putExtra("behaviour_count", behaviourCount);
            announce.putExtra("School_name", AppController.school_name);
            startActivity(announce);
        }
        }
        else if(v==btn_attendance)
        {
            if (dft.isInternetOn() == true) {
                Intent attendance = new Intent(AnnouncementActivity.this, AttendanceActivity.class);
                AppController.clt_id = clt_id;
                AppController.msd_ID = msd_id;
                AppController.usl_id = usl_id;
                AppController.school_name = school_name;
                attendance.putExtra("clt_id", AppController.clt_id);
                attendance.putExtra("msd_ID", AppController.msd_ID);
                attendance.putExtra("usl_id", AppController.usl_id);
                attendance.putExtra("board_name", board_name);
                attendance.putExtra("version_name", AppController.versionName);
                attendance.putExtra("School_name", AppController.school_name);
                attendance.putExtra("annpuncement_count", announcementCount);
                attendance.putExtra("behaviour_count", behaviourCount);
                attendance.putExtra("isStudentResource", isStudentresource);
                startActivity(attendance);
            }
        }
        else if(v==btn_behaviour)
        {
            if (dft.isInternetOn() == true) {
                Intent behaviour = new Intent(AnnouncementActivity.this, BehaviourActivity.class);
                AppController.clt_id = clt_id;
                AppController.msd_ID = msd_id;
                AppController.usl_id = usl_id;
                AppController.school_name = school_name;
                behaviour.putExtra("clt_id", AppController.clt_id);
                behaviour.putExtra("msd_ID", AppController.msd_ID);
                behaviour.putExtra("usl_id", AppController.usl_id);
                behaviour.putExtra("board_name", board_name);
                behaviour.putExtra("board_name", board_name);
                behaviour.putExtra("version_name", AppController.versionName);
                behaviour.putExtra("School_name", AppController.school_name);
                behaviour.putExtra("annpuncement_count", announcementCount);
                behaviour.putExtra("behaviour_count", behaviourCount);
                behaviour.putExtra("isStudentResource", isStudentresource);
                startActivity(behaviour);
            }
        }
        else if(v==btn_dashboard)
        {
            if (dft.isInternetOn() == true) {
                Intent dash = new Intent(AnnouncementActivity.this, DashboardActivity.class);
                AppController.clt_id = clt_id;
                AppController.msd_ID = msd_id;
                AppController.usl_id = usl_id;
                AppController.school_name = school_name;
                dash.putExtra("clt_id", AppController.clt_id);
                dash.putExtra("msd_ID", AppController.msd_ID);
                dash.putExtra("usl_id", AppController.usl_id);
                dash.putExtra("board_name", board_name);
                dash.putExtra("version_name", AppController.versionName);
                dash.putExtra("annpuncement_count", announcementCount);
                dash.putExtra("behaviour_count", behaviourCount);
                dash.putExtra("isStudentResource", isStudentresource);
                dash.putExtra("School_name", AppController.school_name);
                startActivity(dash);
            }
        }
        else if(v==lay_back_investment)
        {  if(AppController.SiblingActivity.equalsIgnoreCase("true"))
        {
            Intent back = new Intent(AnnouncementActivity.this,Profile_Sibling.class);
            AppController.OnBackpressed = "false";
            AppController.clt_id = clt_id;
            AppController.msd_ID = msd_id;
            AppController.usl_id = usl_id;
            AppController.school_name = school_name;
            back.putExtra("clt_id", AppController.clt_id);
            back.putExtra("msd_ID", AppController.msd_ID);
            back.putExtra("usl_id", AppController.usl_id);
            back.putExtra("board_name",board_name);
            back.putExtra("isStudentResource",isStudentresource);
            back.putExtra("annpuncement_count",announcementCount);
            back.putExtra("behaviour_count",behaviourCount);
            back.putExtra("version_name", AppController.versionName);
            back.putExtra("School_name", AppController.school_name);
            startActivity(back);
        }
        else {
            Intent back = new Intent(AnnouncementActivity.this,ProfileActivity.class);
            AppController.OnBackpressed = "false";
            AppController.clt_id = clt_id;
            AppController.msd_ID = msd_id;
            AppController.usl_id = usl_id;
            AppController.school_name = school_name;
            back.putExtra("clt_id", AppController.clt_id);
            back.putExtra("msd_ID", AppController.msd_ID);
            back.putExtra("usl_id", AppController.usl_id);
            back.putExtra("board_name",board_name);
            back.putExtra("isStudentResource",isStudentresource);
            back.putExtra("annpuncement_count",announcementCount);
            back.putExtra("behaviour_count", behaviourCount);
            back.putExtra("version_name", AppController.versionName);
            back.putExtra("School_name", AppController.school_name);
            startActivity(back);
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
        } else {
            if (AppController.SiblingActivity.equalsIgnoreCase("true")) {
                Intent back = new Intent(AnnouncementActivity.this, Profile_Sibling.class);
                AppController.OnBackpressed = "false";
                AppController.clt_id = clt_id;
                AppController.msd_ID = msd_id;
                AppController.usl_id = usl_id;
                AppController.school_name = school_name;
                back.putExtra("clt_id", AppController.clt_id);
                back.putExtra("msd_ID", AppController.msd_ID);
                back.putExtra("usl_id", AppController.usl_id);
                back.putExtra("board_name", board_name);
                back.putExtra("version_name", AppController.versionName);
                back.putExtra("annpuncement_count", announcementCount);
                back.putExtra("behaviour_count", behaviourCount);
                back.putExtra("isStudentResource",isStudentresource);
                back.putExtra("School_name", AppController.school_name);
                startActivity(back);
            } else {
                Intent back = new Intent(AnnouncementActivity.this, ProfileActivity.class);
                AppController.OnBackpressed = "false";
                AppController.clt_id = clt_id;
                AppController.msd_ID = msd_id;
                AppController.usl_id = usl_id;
                AppController.school_name = school_name;
                back.putExtra("clt_id", AppController.clt_id);
                back.putExtra("msd_ID", AppController.msd_ID);
                back.putExtra("usl_id", AppController.usl_id);
                back.putExtra("board_name", board_name);
                back.putExtra("annpuncement_count", announcementCount);
                back.putExtra("behaviour_count", behaviourCount);
                back.putExtra("isStudentResource",isStudentresource);
                back.putExtra("version_name", AppController.versionName);
                back.putExtra("School_name", AppController.school_name);
                startActivity(back);
            }
        }
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
       //Call Ws to read announcement

            msg_id = login_details.AnnouncementResult.get(position).msg_ID.toString();
            announcement_usl_id = login_details.AnnouncementResult.get(position).usl_id.toString();
            callWsToReadAnnouncement(msg_id, usl_id);
            announcement_adpater.notifyDataSetChanged();
            if (login_details.AnnouncementResult.get(position).pmu_readunreadstatus.equalsIgnoreCase("1")) {
                AppController.UnreadMessage = "true";
                if (position == 0) {
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(notificationID);
                }
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
                                //Call Webservcie for Announcement
                                CallAnnouncementWebservice(clt_id, usl_id, msd_id);
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
                Log.e("MESSAGE",abc);
                viewannouncementListSize = AppController.announcement_result.size();
                AppController.viewAnnouncementListSize = viewannouncementListSize;
                announcement_adpater = new AnnouncementAdapter(AnnouncementActivity.this, AppController.announcement_result);
                list_announcement.setAdapter(announcement_adpater);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}