package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.Adapter.FeesInfoAdapter;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

import org.json.JSONObject;

/**
 * Created by Administrator on 12/2/2015.
 */
public class FeesInfoActivity extends Activity implements View.OnClickListener{

    //ListView
    ListView lv_payment_history,drawerListView;
    //Linear Layout
    LinearLayout lay_back_investment,ll_feesInfoOnline;
    //TextView
    TextView tv_no_record,tv_version_name,tv_version_code,tv_cancel,tv_academic_year_drawer,tv_child_name_drawer,tv_std_value_drawer,tv_roll_no_value_drawer;
    //Drawaer Layout
    DrawerLayout drawer;
    //Relative Layout
    RelativeLayout leftfgf_drawer,rl_profile,rl_payment_history;
    //ImageView
    ImageView img_drawer,img_icon;
    //Button
    Button btn_payFeesOnline;
    ContentDisplaceDrawerToggle mContentDisplaceToggle;
    Context context;
    //View
    View view_line;
    //ProgressDialog
    ProgressDialog progressDialog;

    //LayoutEntity
    DataFetchService dft;
    LoginDetails login_details;
    HeaderControler header;
    FeesInfoAdapter feesInfoAdapter;

    String board_name,clt_id,msd_id,usl_id,std_name,school_name,versionName,announcementCount,behaviourCount,isStudentresource,sch_id,cls_id,acy_id,stud_id,brd_id,username,password;
    String feesInfoMethodName = "GetPaymentHistory";
    String feesOutstandingoMethodName = "CheckFeeOutStanding";
    int versionCode,notificationID=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fessinfo);
        getIntentData();
        findViews();
        init();
        setDrawer();
        ll_feesInfoOnline.setVisibility(View.GONE);
        rl_payment_history.setVisibility(View.GONE);
        lv_payment_history.setDivider(null);
        lv_payment_history.setDividerHeight(0);
        AppController.SentEnterDropdown = "false";
        try {
            //Webservice call for FeesInfo
            callWebserviceGetfeesInfo(msd_id, clt_id);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setDrawer() {
        try {
            if (AppController.SiblingActivity.equalsIgnoreCase("true")) {
                if (AppController.isStudentResourcePresent.equalsIgnoreCase("0")) {
                    int[] icons_with_sibling = {R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sib_48x48, R.drawable.payfeesonline_48x48, R.drawable.studentresources_48x48, R.drawable.att_48x48, R.drawable.parentinformation_48x48, /*R.drawable.studentresources_48x48,*/ R.drawable.setting1_hdpi,
                            R.drawable.logout1_hdpi, R.drawable.info};
                    String[] data_with_sibling = {"Dashboard", "Messages", "Sibling", "Fees Information", "Student Resources", "Attendance", "Student Information"/*,"Class Room Videos"*/, "Setting",
                            "Sign Out", "About"};
                    drawerListView.setAdapter(new CustomAccount(FeesInfoActivity.this, android.R.layout.simple_list_item_1, icons_with_sibling, data_with_sibling));

                    drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if (position == 0) {
                                Intent profile = new Intent(FeesInfoActivity.this, Profile_Sibling.class);
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
                                profile.putExtra("isStudentResource", isStudentresource);
                                profile.putExtra("annpuncement_count", AppController.announcementCount);
                                profile.putExtra("behaviour_count", behaviourCount);
                                profile.putExtra("version_name", AppController.versionName);
                                profile.putExtra("version_code", AppController.versionCode);
                                startActivity(profile);
                            } else if (position == 1) {
                                if (dft.isInternetOn() == true) {
                                    Intent dashboard = new Intent(FeesInfoActivity.this, DashboardActivity.class);
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
                                Intent sibling = new Intent(FeesInfoActivity.this, SiblingActivity.class);
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
                                    Intent feesInfo = new Intent(FeesInfoActivity.this, FeesInfoActivity.class);

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
                                    Intent student_resource = new Intent(FeesInfoActivity.this, StudentResourcesActivity.class);

                                    student_resource.putExtra("board_name", board_name);
                                    student_resource.putExtra("clt_id", AppController.clt_id);
                                    student_resource.putExtra("msd_ID", AppController.msd_ID);
                                    student_resource.putExtra("usl_id", AppController.usl_id);
                                    student_resource.putExtra("School_name", AppController.school_name);
                                    student_resource.putExtra("Std Name", std_name);
                                    student_resource.putExtra("version_name", AppController.versionName);
                                    student_resource.putExtra("version_code", AppController.versionCode);
                                    student_resource.putExtra("behaviour_count", behaviourCount);
                                    student_resource.putExtra("isStudentResource", isStudentresource);
                                    student_resource.putExtra("annpuncement_count", AppController.announcementCount);
                                    startActivity(student_resource);
                                }
                            } else if (position == 5) {
                                if (dft.isInternetOn() == true) {
                                    Intent attendance = new Intent(FeesInfoActivity.this, AttendanceActivity.class);

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
                                    attendance.putExtra("behaviour_count", behaviourCount);
                                    attendance.putExtra("isStudentResource", isStudentresource);
                                    attendance.putExtra("annpuncement_count", AppController.announcementCount);
                                    startActivity(attendance);
                                }
                            } else if (position == 6) {
                                if (dft.isInternetOn() == true) {
                                    Intent parentinfo = new Intent(FeesInfoActivity.this, ParentInformationActivity.class);
                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_id;
                                    AppController.usl_id = usl_id;
                                    parentinfo.putExtra("clt_id", AppController.clt_id);
                                    parentinfo.putExtra("msd_ID", AppController.msd_ID);
                                    parentinfo.putExtra("usl_id", AppController.usl_id);
                                    parentinfo.putExtra("board_name", board_name);
                                    parentinfo.putExtra("Std Name", std_name);
                                    parentinfo.putExtra("School_name", AppController.school_name);
                                    parentinfo.putExtra("version_name", AppController.versionName);
                                    parentinfo.putExtra("version_code", AppController.versionCode);
                                    parentinfo.putExtra("behaviour_count", behaviourCount);
                                    parentinfo.putExtra("isStudentResource", isStudentresource);
                                    parentinfo.putExtra("annpuncement_count", AppController.announcementCount);
                                    startActivity(parentinfo);
                                }
                            } else if (position == 7) {
                                Intent setting = new Intent(FeesInfoActivity.this, SettingActivity.class);
                                AppController.usl_id = usl_id;
                                setting.putExtra("usl_id", usl_id);
                                setting.putExtra("version_name", AppController.versionName);
                                setting.putExtra("Std Name", std_name);
                                setting.putExtra("version_code", AppController.versionCode);
                                setting.putExtra("isStudentResource", isStudentresource);
                                startActivity(setting);
                            } else if (position == 8) {
                                Intent signout = new Intent(FeesInfoActivity.this, LoginActivity.class);
                                Constant.SetLoginData("", "", FeesInfoActivity.this);
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancel(notificationID);
                                AppController.drawerSignOut = "true";
                                LoginActivity.loggedIn = "false";
                                startActivity(signout);
                            } else if (position == 9) {
                                final Dialog alertDialog = new Dialog(FeesInfoActivity.this);
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
                } else {
                    int[] icons_with_sibling = {R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sib_48x48, R.drawable.payfeesonline_48x48, R.drawable.att_48x48, R.drawable.parentinformation_48x48, /*R.drawable.studentresources_48x48,*/ R.drawable.setting1_hdpi,
                            R.drawable.logout1_hdpi, R.drawable.info};
                    String[] data_with_sibling = {"Dashboard", "Messages", "Sibling", "Fees Information", "Attendance", "Student Information"/*,"Class Room Videos"*/, "Setting",
                            "Sign Out", "About"};
                    drawerListView.setAdapter(new CustomAccount(FeesInfoActivity.this, android.R.layout.simple_list_item_1, icons_with_sibling, data_with_sibling));
                    drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if (position == 0) {
                                Intent profile = new Intent(FeesInfoActivity.this, Profile_Sibling.class);
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
                                profile.putExtra("behaviour_count", behaviourCount);
                                profile.putExtra("annpuncement_count", AppController.announcementCount);
                                startActivity(profile);
                            } else if (position == 1) {
                                if (dft.isInternetOn() == true) {
                                    Intent dashboard = new Intent(FeesInfoActivity.this, DashboardActivity.class);
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
                                Intent sibling = new Intent(FeesInfoActivity.this, SiblingActivity.class);
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
                                    Intent feesInfo = new Intent(FeesInfoActivity.this, FeesInfoActivity.class);

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
                                    Intent attendance = new Intent(FeesInfoActivity.this, AttendanceActivity.class);

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
                                    attendance.putExtra("behaviour_count", behaviourCount);
                                    attendance.putExtra("isStudentResource", isStudentresource);
                                    attendance.putExtra("annpuncement_count", AppController.announcementCount);
                                    startActivity(attendance);
                                }
                            } else if (position == 5) {
                                if (dft.isInternetOn() == true) {
                                    Intent parentinfo = new Intent(FeesInfoActivity.this, ParentInformationActivity.class);

                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_id;
                                    AppController.usl_id = usl_id;
                                    parentinfo.putExtra("clt_id", AppController.clt_id);
                                    parentinfo.putExtra("msd_ID", AppController.msd_ID);
                                    parentinfo.putExtra("usl_id", AppController.usl_id);
                                    parentinfo.putExtra("board_name", board_name);
                                    parentinfo.putExtra("Std Name", std_name);
                                    parentinfo.putExtra("School_name", AppController.school_name);
                                    parentinfo.putExtra("version_name", AppController.versionName);
                                    parentinfo.putExtra("version_code", AppController.versionCode);
                                    parentinfo.putExtra("behaviour_count", behaviourCount);
                                    parentinfo.putExtra("isStudentResource", isStudentresource);
                                    parentinfo.putExtra("annpuncement_count", AppController.announcementCount);
                                    startActivity(parentinfo);
                                }
                            } else if (position == 6) {
                                Intent setting = new Intent(FeesInfoActivity.this, SettingActivity.class);
                                AppController.usl_id = usl_id;
                                setting.putExtra("usl_id", usl_id);
                                setting.putExtra("Std Name", std_name);
                                setting.putExtra("version_name", AppController.versionName);
                                setting.putExtra("version_code", AppController.versionCode);
                                setting.putExtra("behaviour_count", behaviourCount);
                                setting.putExtra("isStudentResource", isStudentresource);
                                setting.putExtra("annpuncement_count", AppController.announcementCount);
                                startActivity(setting);
                            } else if (position == 7) {
                                Intent signout = new Intent(FeesInfoActivity.this, LoginActivity.class);
                                Constant.SetLoginData("", "", FeesInfoActivity.this);
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancel(notificationID);
                                AppController.drawerSignOut = "true";
                                LoginActivity.loggedIn = "false";
                                startActivity(signout);
                            } else if (position == 8) {
                                final Dialog alertDialog = new Dialog(FeesInfoActivity.this);
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
                    drawerListView.setAdapter(new CustomAccount(FeesInfoActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));
                    drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if (position == 0) {
                                Intent profile = new Intent(FeesInfoActivity.this, ProfileActivity.class);
                                AppController.msd_ID = msd_id;
                                AppController.clt_id = clt_id;
                                AppController.school_name = school_name;
                                profile.putExtra("msd_ID", AppController.msd_ID);
                                profile.putExtra("clt_id", AppController.clt_id);
                                profile.putExtra("School_name", AppController.school_name);
                                profile.putExtra("usl_id", AppController.usl_id);
                                profile.putExtra("board_name", AppController.Board_name);
                                profile.putExtra("Std Name", std_name);
                                profile.putExtra("version_name", AppController.versionName);
                                profile.putExtra("behaviour_count", behaviourCount);
                                profile.putExtra("isStudentResource", isStudentresource);
                                profile.putExtra("annpuncement_count", AppController.announcementCount);
                                startActivity(profile);
                            } else if (position == 1) {
                                if (dft.isInternetOn() == true) {
                                    Intent dashboard = new Intent(FeesInfoActivity.this, DashboardActivity.class);
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
                                    dashboard.putExtra("annpuncement_count", AppController.announcementCount);
                                    dashboard.putExtra("behaviour_count", behaviourCount);
                                    dashboard.putExtra("isStudentResource", isStudentresource);
                                    startActivity(dashboard);
                                }
                            } else if (position == 2) {
                                if (dft.isInternetOn() == true) {
                                    Intent feesInfo = new Intent(FeesInfoActivity.this, FeesInfoActivity.class);
                                    feesInfo.putExtra("board_name", AppController.Board_name);
                                    feesInfo.putExtra("clt_id", AppController.clt_id);
                                    feesInfo.putExtra("msd_ID", AppController.msd_ID);
                                    feesInfo.putExtra("usl_id", AppController.usl_id);
                                    feesInfo.putExtra("School_name", AppController.school_name);
                                    feesInfo.putExtra("Std Name", std_name);
                                    feesInfo.putExtra("version_name", AppController.versionName);
                                    feesInfo.putExtra("annpuncement_count", AppController.announcementCount);
                                    feesInfo.putExtra("behaviour_count", behaviourCount);
                                    feesInfo.putExtra("isStudentResource", isStudentresource);
                                    startActivity(feesInfo);
                                }
                            } else if (position == 3) {
                                if (dft.isInternetOn() == true) {
                                    Intent student_resource = new Intent(FeesInfoActivity.this, StudentResourcesActivity.class);
                                    student_resource.putExtra("board_name", AppController.Board_name);
                                    student_resource.putExtra("clt_id", AppController.clt_id);
                                    student_resource.putExtra("msd_ID", AppController.msd_ID);
                                    student_resource.putExtra("usl_id", AppController.usl_id);
                                    student_resource.putExtra("School_name", AppController.school_name);
                                    student_resource.putExtra("version_name", AppController.versionName);
                                    student_resource.putExtra("behaviour_count", behaviourCount);
                                    student_resource.putExtra("isStudentResource", isStudentresource);
                                    student_resource.putExtra("annpuncement_count", AppController.announcementCount);
                                    student_resource.putExtra("Std Name", std_name);
                                    startActivity(student_resource);
                                }
                            } else if (position == 4) {
                                if (dft.isInternetOn() == true) {
                                    Intent attendance = new Intent(FeesInfoActivity.this, AttendanceActivity.class);
                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_id;
                                    AppController.usl_id = usl_id;
                                    attendance.putExtra("clt_id", AppController.clt_id);
                                    attendance.putExtra("msd_ID", AppController.msd_ID);
                                    attendance.putExtra("usl_id", AppController.usl_id);
                                    attendance.putExtra("School_name", AppController.school_name);
                                    attendance.putExtra("board_name", AppController.Board_name);
                                    attendance.putExtra("Std Name", std_name);
                                    attendance.putExtra("behaviour_count", behaviourCount);
                                    attendance.putExtra("isStudentResource", isStudentresource);
                                    attendance.putExtra("annpuncement_count", AppController.announcementCount);
                                    attendance.putExtra("version_name", AppController.versionName);
                                    startActivity(attendance);
                                }
                            } else if (position == 5) {
                                if (dft.isInternetOn() == true) {
                                    Intent parentinfo = new Intent(FeesInfoActivity.this, ParentInformationActivity.class);
                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_id;
                                    AppController.usl_id = usl_id;
                                    AppController.Board_name = board_name;
                                    parentinfo.putExtra("clt_id", AppController.clt_id);
                                    parentinfo.putExtra("msd_ID", AppController.msd_ID);
                                    parentinfo.putExtra("usl_id", AppController.usl_id);
                                    parentinfo.putExtra("board_name", AppController.Board_name);
                                    parentinfo.putExtra("Std Name", std_name);
                                    parentinfo.putExtra("School_name", AppController.school_name);
                                    parentinfo.putExtra("version_name", AppController.versionName);
                                    parentinfo.putExtra("behaviour_count", behaviourCount);
                                    parentinfo.putExtra("isStudentResource", isStudentresource);
                                    parentinfo.putExtra("annpuncement_count", AppController.announcementCount);
                                    startActivity(parentinfo);
                                }
                            } else if (position == 6) {
                                Intent setting = new Intent(FeesInfoActivity.this, SettingActivity.class);
                                AppController.usl_id = usl_id;
                                setting.putExtra("usl_id", AppController.usl_id);
                                setting.putExtra("Std Name", std_name);
                                setting.putExtra("version_name", AppController.versionName);
                                setting.putExtra("annpuncement_count", AppController.announcementCount);
                                setting.putExtra("behaviour_count", behaviourCount);
                                setting.putExtra("isStudentResource", isStudentresource);
                                startActivity(setting);
                            } else if (position == 7) {
                                Intent signout = new Intent(FeesInfoActivity.this, LoginActivity.class);
                                Constant.SetLoginData("", "", FeesInfoActivity.this);
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancel(notificationID);
                                AppController.drawerSignOut = "true";
                                LoginActivity.loggedIn = "false";
                                startActivity(signout);
                            } else if (position == 8) {
                                final Dialog alertDialog = new Dialog(FeesInfoActivity.this);
                                //  final AlertDialog alertDialog = builder.create();
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
                    drawerListView.setAdapter(new CustomAccount(FeesInfoActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));
                    drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if (position == 0) {
                                Intent profile = new Intent(FeesInfoActivity.this, ProfileActivity.class);
                                AppController.msd_ID = msd_id;
                                AppController.clt_id = clt_id;
                                AppController.school_name = school_name;
                                profile.putExtra("msd_ID", AppController.msd_ID);
                                profile.putExtra("clt_id", AppController.clt_id);
                                profile.putExtra("School_name", AppController.school_name);
                                profile.putExtra("usl_id", AppController.usl_id);
                                profile.putExtra("board_name", AppController.Board_name);
                                profile.putExtra("Std Name", std_name);
                                profile.putExtra("version_name", AppController.versionName);
                                profile.putExtra("behaviour_count", behaviourCount);
                                profile.putExtra("isStudentResource", isStudentresource);
                                profile.putExtra("annpuncement_count", AppController.announcementCount);
                                startActivity(profile);
                            } else if (position == 1) {
                                if (dft.isInternetOn() == true) {
                                    Intent dashboard = new Intent(FeesInfoActivity.this, DashboardActivity.class);
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
                                Intent feesInfo = new Intent(FeesInfoActivity.this, FeesInfoActivity.class);

                                feesInfo.putExtra("board_name", AppController.Board_name);
                                feesInfo.putExtra("clt_id", AppController.clt_id);
                                feesInfo.putExtra("msd_ID", AppController.msd_ID);
                                feesInfo.putExtra("usl_id", AppController.usl_id);
                                feesInfo.putExtra("School_name", AppController.school_name);
                                feesInfo.putExtra("Std Name", std_name);
                                feesInfo.putExtra("version_name", AppController.versionName);
                                feesInfo.putExtra("behaviour_count", behaviourCount);
                                feesInfo.putExtra("isStudentResource", isStudentresource);
                                feesInfo.putExtra("annpuncement_count", AppController.announcementCount);
                                startActivity(feesInfo);
                            } else if (position == 3) {
                                if (dft.isInternetOn() == true) {
                                    Intent attendance = new Intent(FeesInfoActivity.this, AttendanceActivity.class);

                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_id;
                                    AppController.usl_id = usl_id;
                                    attendance.putExtra("clt_id", AppController.clt_id);
                                    attendance.putExtra("msd_ID", AppController.msd_ID);
                                    attendance.putExtra("usl_id", AppController.usl_id);
                                    attendance.putExtra("School_name", AppController.school_name);
                                    attendance.putExtra("board_name", AppController.Board_name);
                                    attendance.putExtra("Std Name", std_name);
                                    attendance.putExtra("version_name", AppController.versionName);
                                    attendance.putExtra("behaviour_count", behaviourCount);
                                    attendance.putExtra("isStudentResource", isStudentresource);
                                    attendance.putExtra("annpuncement_count", AppController.announcementCount);
                                    startActivity(attendance);
                                }
                            } else if (position == 4) {
                                if (dft.isInternetOn() == true) {
                                    Intent parentinfo = new Intent(FeesInfoActivity.this, ParentInformationActivity.class);
                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_id;
                                    AppController.usl_id = usl_id;
                                    AppController.Board_name = board_name;
                                    parentinfo.putExtra("clt_id", AppController.clt_id);
                                    parentinfo.putExtra("msd_ID", AppController.msd_ID);
                                    parentinfo.putExtra("usl_id", AppController.usl_id);
                                    parentinfo.putExtra("board_name", AppController.Board_name);
                                    parentinfo.putExtra("Std Name", std_name);
                                    parentinfo.putExtra("School_name", AppController.school_name);
                                    parentinfo.putExtra("version_name", AppController.versionName);
                                    parentinfo.putExtra("behaviour_count", behaviourCount);
                                    parentinfo.putExtra("isStudentResource", isStudentresource);
                                    parentinfo.putExtra("annpuncement_count", AppController.announcementCount);
                                    startActivity(parentinfo);
                                }
                            } else if (position == 5) {
                                Intent setting = new Intent(FeesInfoActivity.this, SettingActivity.class);
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
                                    Intent signout = new Intent(FeesInfoActivity.this, LoginActivity.class);
                                    Constant.SetLoginData("", "", FeesInfoActivity.this);
                                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.cancel(notificationID);
                                    AppController.drawerSignOut = "true";
                                    LoginActivity.loggedIn = "false";
                                    startActivity(signout);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (position == 7) {
                                final Dialog alertDialog = new Dialog(FeesInfoActivity.this);
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
            }
            Constant.setDrawerData(AppController.drawerAcademic_year, AppController.drawerChild_name, AppController.drawerStd, AppController.drawerRoll_no, FeesInfoActivity.this);
            setDrawerData();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void setDrawerData() {
        tv_academic_year_drawer.setText(Constant.GetAcademicYear(this));
        tv_child_name_drawer.setText(Constant.GetChildName(this));
        tv_std_value_drawer.setText(Constant.GetStandard(this));
        tv_roll_no_value_drawer.setText(Constant.GetRollNo(this));
    }


    private void getIntentData() {
        Intent intent = getIntent();
        board_name = intent.getStringExtra("board_name");
        clt_id = intent.getStringExtra("clt_id");
        AppController.clt_id = clt_id;
        msd_id = intent.getStringExtra("msd_ID");
        usl_id = intent.getStringExtra("usl_id");
        school_name = intent.getStringExtra("School_name");
        std_name = intent.getStringExtra("Std Name");
        versionName = intent.getStringExtra("version_name");
        versionCode = intent.getIntExtra("version_code", 0);
        announcementCount = intent.getStringExtra("annpuncement_count");
        behaviourCount = intent.getStringExtra("behaviour_count");
        isStudentresource = intent.getStringExtra("isStudentResource");
        AppController.versionName = versionName;
        AppController.school_name = school_name;
        AppController.usl_id = usl_id;
        username = Constant.GetLoginusername(this);
        password = Constant.GetLoginPassword(this);
        Log.e("Fee Username",username);
        Log.e("Fee Password",password);
        if(AppController.SiblingListSize>1){
            cls_id = Profile_Sibling.get_cls_id(FeesInfoActivity.this);
            acy_id = Profile_Sibling.get_acy_id(FeesInfoActivity.this);
            stud_id = Profile_Sibling.get_stud_id(FeesInfoActivity.this);
            sch_id = Profile_Sibling.get_sch_id(FeesInfoActivity.this);
            brd_id = Profile_Sibling.get_Brd_ID(FeesInfoActivity.this);
        }
        else {
            cls_id = ProfileActivity.get_cls_id(FeesInfoActivity.this);
            acy_id = ProfileActivity.get_acy_id(FeesInfoActivity.this);
            stud_id = ProfileActivity.get_stud_id(FeesInfoActivity.this);
            sch_id = ProfileActivity.get_sch_id(FeesInfoActivity.this);
            brd_id = ProfileActivity.get_Brd_ID(FeesInfoActivity.this);
        }
    }
    private void findViews(){
        //ListView
        lv_payment_history = (ListView) findViewById(R.id.lv_payment_history);
        //Linear Layout
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
        ll_feesInfoOnline = (LinearLayout) findViewById(R.id.ll_feesInfoOnline);
        //TextView
        tv_no_record = (TextView) findViewById(R.id.tv_no_record);
        //View
      //  view_line = (View)findViewById(R.id.view_line);
        //TextView
        tv_no_record = (TextView) findViewById(R.id.tv_no_record);
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
        btn_payFeesOnline = (Button) findViewById(R.id.btn_payFeesOnline);
        // for drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        leftfgf_drawer = (RelativeLayout) findViewById(R.id.leftfgf_drawer);
        rl_profile = (RelativeLayout) findViewById(R.id.rl_profile);
        rl_payment_history = (RelativeLayout) findViewById(R.id.rl_payment_history);
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
        img_drawer.setOnClickListener(this);
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        progressDialog = Constant.getProgressDialog(this);
        lay_back_investment.setOnClickListener(this);
        header = new HeaderControler(this,true,false,"Fees Information");
        ll_feesInfoOnline.setOnClickListener(this);
        btn_payFeesOnline.setOnClickListener(this);
    }

    private void callWebserviceGetfeesInfo(String msd_id,String clt_id) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getFeesInfo(msd_id, clt_id, feesInfoMethodName, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            lv_payment_history.setVisibility(View.VISIBLE);
                            rl_payment_history.setVisibility(View.VISIBLE);
                            tv_no_record.setVisibility(View.GONE);

                            if (login_details.Status.equalsIgnoreCase("1")) {

                                setUIData();
                                //Webservice call to cehckFessOutstanding
                                callCheckFeeOutstandingWebservice(cls_id,sch_id,acy_id,brd_id,stud_id);
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            } else if (login_details.Status.equalsIgnoreCase("0")) {
                                lv_payment_history.setVisibility(View.GONE);
                                ll_feesInfoOnline.setVisibility(View.GONE);
                                rl_payment_history.setVisibility(View.GONE);
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

    private void callCheckFeeOutstandingWebservice(String cls_id, String sch_id, String acy_id,String brd_id, String stud_id) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getFeesOutStandingStatus(cls_id, sch_id, acy_id, brd_id, stud_id, feesOutstandingoMethodName, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("<< CheckFeeOutStanding",response.toString());

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);

                            if (login_details.StatusMsg.equalsIgnoreCase("Show")) {
                                Log.d("<< CheckFeeOutStanding",login_details.Status);
                                ll_feesInfoOnline.setVisibility(View.VISIBLE);

                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            } else if (login_details.StatusMsg.equalsIgnoreCase("Dont Show")) {
                                Log.d("<< CheckFeeOutStanding",login_details.Status);
                                ll_feesInfoOnline.setVisibility(View.GONE);
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

    private void setUIData() {
        feesInfoAdapter = new FeesInfoAdapter(this,login_details.PaymentList);
        lv_payment_history.setAdapter(feesInfoAdapter);
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
                intent.putExtra("isStudentResource", isStudentresource);
                intent.putExtra("School_name", AppController.school_name);
                intent.putExtra("board_name", AppController.Board_name);
                intent.putExtra("Sibling", AppController.SiblingListSize);
                startActivity(intent);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        if(v==img_drawer)
        {
            drawer.openDrawer(Gravity.RIGHT);
        }
        else if(v==lay_back_investment){
            if(AppController.SiblingActivity.equalsIgnoreCase("true"))
            {
                Intent back = new Intent(FeesInfoActivity.this,Profile_Sibling.class);
                AppController.OnBackpressed = "false";
                AppController.clt_id = clt_id;
                AppController.msd_ID = msd_id;
                AppController.usl_id = usl_id;
                back.putExtra("clt_id", AppController.clt_id);
                back.putExtra("msd_ID", AppController.msd_ID);
                back.putExtra("usl_id", AppController.usl_id);
                back.putExtra("board_name",board_name);
                back.putExtra("version_name", AppController.versionName);
                back.putExtra("verion_code", AppController.versionCode);
                back.putExtra("School_name", AppController.school_name);
                back.putExtra("isStudentResource",isStudentresource);
                startActivity(back);
            }
            else{
                Intent back = new Intent(FeesInfoActivity.this,ProfileActivity.class);
                AppController.OnBackpressed = "false";
                AppController.clt_id = clt_id;
                AppController.msd_ID = msd_id;
                AppController.usl_id = usl_id;
                back.putExtra("clt_id", AppController.clt_id);
                back.putExtra("msd_ID", AppController.msd_ID);
                back.putExtra("usl_id", AppController.usl_id);
                back.putExtra("board_name",board_name);
                back.putExtra("version_name", AppController.versionName);
                back.putExtra("verion_code", AppController.versionCode);
                back.putExtra("School_name", AppController.school_name);
                back.putExtra("isStudentResource",isStudentresource);
                startActivity(back);
            }
        }
        else if(v==ll_feesInfoOnline){
            String params = "Username="+username+"&Password="+password+"&msd_id="+msd_id;
            String url = "http://www.betweenus.in/AppAutoLogin.aspx?";
            String final_url = url+params;
            Log.e("Final Url = ",final_url);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(final_url));
            startActivity(browserIntent);
        }
        else if(v==btn_payFeesOnline){
            String params = "Username="+username+"&Password="+password+"&msd_id="+msd_id;
            String url = "http://www.betweenus.in/AppAutoLogin.aspx?";
            Log.d("<<Redirection URL", url);
            String final_url = url+params;
            Log.e("Final Url = ",final_url);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(final_url));
            startActivity(browserIntent);
        }
    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
        }
        else {
            if (AppController.SiblingActivity.equalsIgnoreCase("true")) {
                Intent back = new Intent(FeesInfoActivity.this, Profile_Sibling.class);
                AppController.OnBackpressed = "false";
                AppController.clt_id = clt_id;
                AppController.msd_ID = msd_id;
                AppController.usl_id = usl_id;
                back.putExtra("clt_id", AppController.clt_id);
                back.putExtra("msd_ID", AppController.msd_ID);
                back.putExtra("usl_id", AppController.usl_id);
                back.putExtra("board_name", board_name);
                back.putExtra("School_name", AppController.school_name);
                back.putExtra("version_name", AppController.versionName);
                back.putExtra("verion_code", AppController.versionCode);
                back.putExtra("isStudentResource",isStudentresource);
                startActivity(back);
            } else {
                Intent back = new Intent(FeesInfoActivity.this, ProfileActivity.class);
                AppController.OnBackpressed = "false";
                AppController.clt_id = clt_id;
                AppController.msd_ID = msd_id;
                AppController.usl_id = usl_id;
                back.putExtra("clt_id", AppController.clt_id);
                back.putExtra("msd_ID", AppController.msd_ID);
                back.putExtra("usl_id", AppController.usl_id);
                back.putExtra("board_name", board_name);
                back.putExtra("School_name", AppController.school_name);
                back.putExtra("version_name", AppController.versionName);
                back.putExtra("verion_code", AppController.versionCode);
                back.putExtra("isStudentResource",isStudentresource);
                startActivity(back);
            }
        }
    }
}
