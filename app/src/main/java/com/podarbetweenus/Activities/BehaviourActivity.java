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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.Adapter.BehaviourAdapter;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

import org.json.JSONObject;

/**
 * Created by Administrator on 9/28/2015.
 */
public class BehaviourActivity extends Activity implements View.OnClickListener
{
    //Button
    Button btn_announce,btn_dashboard,btn_attendance,btn_behaviour;
    //Linear Layout
    LinearLayout lay_back_investment,ll_footer;
    //TextView
    TextView tv_no_record,tv_version_name,tv_version_code,tv_cancel,tv_academic_year_drawer,tv_child_name_drawer,tv_std_value_drawer,tv_roll_no_value_drawer;
    //Relative Layout
    RelativeLayout leftfgf_drawer,rl_profile;
    //ListView
    ListView list_behaviours,drawerListView;
    //ProgressDialog
    ProgressDialog progressDialog;
    //Drawaer Layout
    DrawerLayout drawer;
    //ImageView
    ImageView img_drawer,img_icon;
    ContentDisplaceDrawerToggle mContentDisplaceToggle;
    Context context;


    HeaderControler header;
    DataFetchService dft;
    LoginDetails login_details;
    BehaviourAdapter behaviourAdpater;
    String behaviourMethodName = "GetBehavioursData";
    String clt_id,msd_id,usl_id,board_name,school_name,versionName,announcementCount,std_name,behaviourCount,isStudentresource;
    int notificationID = 1;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.template_expandable_behaviour);
        findViews();
        init();
        getIntentId();
        announcentTab();
        behaviourTab();
        AppController.dropdownSelected="false";
      //  AppController.SentEnterDropdown= "true";
        AppController.AttenAnnounceBehaviourdropdown="true";
        setDrawer();
        try {
            //Call Webservcie for Behaviour
            CallbehaviourWebservice(msd_id, clt_id, usl_id);
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
                    drawerListView.setAdapter(new CustomAccount(BehaviourActivity.this, android.R.layout.simple_list_item_1, icons_with_sibling, data_with_sibling));
                    drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if (position == 0) {
                                Intent profile = new Intent(BehaviourActivity.this, Profile_Sibling.class);
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
                                profile.putExtra("isStudentResource", isStudentresource);
                                profile.putExtra("version_name", AppController.versionName);
                                profile.putExtra("version_code", AppController.versionCode);
                                startActivity(profile);
                            } else if (position == 1) {
                                if (dft.isInternetOn() == true) {
                                    Intent dashboard = new Intent(BehaviourActivity.this, DashboardActivity.class);
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

                                Intent sibling = new Intent(BehaviourActivity.this, SiblingActivity.class);
                                AppController.clt_id = clt_id;
                                AppController.usl_id = usl_id;
                                AppController.msd_ID = msd_id;
                                sibling.putExtra("clt_id", AppController.clt_id);
                                sibling.putExtra("usl_id", AppController.usl_id);
                                sibling.putExtra("School_name", AppController.school_name);
                                sibling.putExtra("msd_ID", AppController.msd_ID);
                                sibling.putExtra("board_name", AppController.Board_name);
                                sibling.putExtra("Std Name", std_name);
                                sibling.putExtra("annpuncement_count", AppController.announcementCount);
                                sibling.putExtra("behaviour_count", behaviourCount);
                                sibling.putExtra("version_name", AppController.versionName);
                                sibling.putExtra("version_code", AppController.versionCode);
                                sibling.putExtra("isStudentResource", isStudentresource);
                                startActivity(sibling);
                            } else if (position == 3) {
                                if (dft.isInternetOn() == true) {
                                    Intent feesInfo = new Intent(BehaviourActivity.this, FeesInfoActivity.class);

                                    feesInfo.putExtra("board_name", AppController.Board_name);
                                    feesInfo.putExtra("clt_id", AppController.clt_id);
                                    feesInfo.putExtra("msd_ID", AppController.msd_ID);
                                    feesInfo.putExtra("usl_id", AppController.usl_id);
                                    feesInfo.putExtra("School_name", AppController.school_name);
                                    feesInfo.putExtra("Std Name", std_name);
                                    feesInfo.putExtra("version_name", AppController.versionName);
                                    feesInfo.putExtra("annpuncement_count", AppController.announcementCount);
                                    feesInfo.putExtra("behaviour_count", behaviourCount);
                                    feesInfo.putExtra("version_code", AppController.versionCode);
                                    feesInfo.putExtra("isStudentResource", isStudentresource);
                                    startActivity(feesInfo);
                                }
                            } else if (position == 4) {
                                if (dft.isInternetOn() == true) {
                                    Intent student_resource = new Intent(BehaviourActivity.this, StudentResourcesActivity.class);
                                    student_resource.putExtra("board_name", AppController.Board_name);
                                    student_resource.putExtra("clt_id", AppController.clt_id);
                                    student_resource.putExtra("msd_ID", AppController.msd_ID);
                                    student_resource.putExtra("usl_id", AppController.usl_id);
                                    student_resource.putExtra("School_name", AppController.school_name);
                                    student_resource.putExtra("Std Name", std_name);
                                    student_resource.putExtra("version_name", AppController.versionName);
                                    student_resource.putExtra("version_code", AppController.versionCode);
                                    student_resource.putExtra("annpuncement_count", AppController.announcementCount);
                                    student_resource.putExtra("behaviour_count", behaviourCount);
                                    student_resource.putExtra("isStudentResource", isStudentresource);
                                    startActivity(student_resource);
                                }
                            } else if (position == 5) {
                                if (dft.isInternetOn() == true) {
                                    Intent attendance = new Intent(BehaviourActivity.this, AttendanceActivity.class);
                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_id;
                                    AppController.usl_id = usl_id;
                                    attendance.putExtra("clt_id", AppController.clt_id);
                                    attendance.putExtra("msd_ID", AppController.msd_ID);
                                    attendance.putExtra("usl_id", AppController.usl_id);
                                    attendance.putExtra("board_name", AppController.Board_name);
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
                                    Intent parentinfo = new Intent(BehaviourActivity.this, ParentInformationActivity.class);
                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_id;
                                    AppController.usl_id = usl_id;
                                    parentinfo.putExtra("clt_id", AppController.clt_id);
                                    parentinfo.putExtra("msd_ID", AppController.msd_ID);
                                    parentinfo.putExtra("usl_id", AppController.usl_id);
                                    parentinfo.putExtra("board_name", AppController.Board_name);
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
                                Intent setting = new Intent(BehaviourActivity.this, SettingActivity.class);
                                AppController.usl_id = usl_id;
                                setting.putExtra("usl_id", usl_id);
                                setting.putExtra("version_name", AppController.versionName);
                                setting.putExtra("version_code", AppController.versionCode);
                                setting.putExtra("Std Name", std_name);
                                setting.putExtra("isStudentResource", isStudentresource);
                                startActivity(setting);
                            } else if (position == 8) {
                                Intent signout = new Intent(BehaviourActivity.this, LoginActivity.class);
                                Constant.SetLoginData("", "", BehaviourActivity.this);
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancel(notificationID);
                                AppController.drawerSignOut = "true";
                                LoginActivity.loggedIn = "false";
                                startActivity(signout);
                            } else if (position == 9) {
                                final Dialog alertDialog = new Dialog(BehaviourActivity.this);
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
                    int[] icons_with_sibling = {R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sib_48x48, R.drawable.payfeesonline_48x48, R.drawable.att_48x48, R.drawable.parentinformation_48x48, /*R.drawable.studentresources_48x48,*/ R.drawable.setting1_hdpi,
                            R.drawable.logout1_hdpi, R.drawable.info};
                    String[] data_with_sibling = {"Dashboard", "Messages", "Sibling", "Fees Information", "Attendance", "Student Information"/*,"Class Room Videos"*/, "Setting",
                            "Sign Out", "About"};
                    drawerListView.setAdapter(new CustomAccount(BehaviourActivity.this, android.R.layout.simple_list_item_1, icons_with_sibling, data_with_sibling));
                    drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if (position == 0) {
                                Intent profile = new Intent(BehaviourActivity.this, Profile_Sibling.class);
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
                                profile.putExtra("version_name", AppController.versionName);
                                profile.putExtra("version_code", AppController.versionCode);
                                profile.putExtra("behaviour_count", behaviourCount);
                                profile.putExtra("annpuncement_count", AppController.announcementCount);
                                startActivity(profile);
                            } else if (position == 1) {
                                if (dft.isInternetOn() == true) {
                                    Intent dashboard = new Intent(BehaviourActivity.this, DashboardActivity.class);
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
                                Intent sibling = new Intent(BehaviourActivity.this, SiblingActivity.class);
                                AppController.clt_id = clt_id;
                                AppController.usl_id = usl_id;
                                AppController.msd_ID = msd_id;
                                sibling.putExtra("clt_id", AppController.clt_id);
                                sibling.putExtra("usl_id", AppController.usl_id);
                                sibling.putExtra("School_name", AppController.school_name);
                                sibling.putExtra("msd_ID", AppController.msd_ID);
                                sibling.putExtra("board_name", AppController.Board_name);
                                sibling.putExtra("Std Name", std_name);
                                sibling.putExtra("annpuncement_count", AppController.announcementCount);
                                sibling.putExtra("behaviour_count", behaviourCount);
                                sibling.putExtra("isStudentResource", isStudentresource);
                                sibling.putExtra("version_name", AppController.versionName);
                                sibling.putExtra("version_code", AppController.versionCode);
                                startActivity(sibling);
                            } else if (position == 3) {
                                if (dft.isInternetOn() == true) {
                                    Intent feesInfo = new Intent(BehaviourActivity.this, FeesInfoActivity.class);

                                    feesInfo.putExtra("board_name", AppController.Board_name);
                                    feesInfo.putExtra("clt_id", AppController.clt_id);
                                    feesInfo.putExtra("msd_ID", AppController.msd_ID);
                                    feesInfo.putExtra("usl_id", AppController.usl_id);
                                    feesInfo.putExtra("School_name", AppController.school_name);
                                    feesInfo.putExtra("Std Name", std_name);
                                    feesInfo.putExtra("isStudentResource", isStudentresource);
                                    feesInfo.putExtra("version_name", AppController.versionName);
                                    feesInfo.putExtra("annpuncement_count", AppController.announcementCount);
                                    feesInfo.putExtra("behaviour_count", behaviourCount);
                                    feesInfo.putExtra("version_code", AppController.versionCode);
                                    startActivity(feesInfo);
                                }
                            } else if (position == 4) {
                                if (dft.isInternetOn() == true) {
                                    Intent attendance = new Intent(BehaviourActivity.this, AttendanceActivity.class);

                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_id;
                                    AppController.usl_id = usl_id;
                                    attendance.putExtra("clt_id", AppController.clt_id);
                                    attendance.putExtra("msd_ID", AppController.msd_ID);
                                    attendance.putExtra("usl_id", AppController.usl_id);
                                    attendance.putExtra("board_name", AppController.Board_name);
                                    attendance.putExtra("Std Name", std_name);
                                    attendance.putExtra("isStudentResource", isStudentresource);
                                    attendance.putExtra("School_name", AppController.school_name);
                                    attendance.putExtra("version_name", AppController.versionName);
                                    attendance.putExtra("version_code", AppController.versionCode);
                                    attendance.putExtra("behaviour_count", behaviourCount);
                                    attendance.putExtra("annpuncement_count", AppController.announcementCount);
                                    startActivity(attendance);
                                }
                            } else if (position == 5) {
                                if (dft.isInternetOn() == true) {
                                    Intent parentinfo = new Intent(BehaviourActivity.this, ParentInformationActivity.class);

                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_id;
                                    AppController.usl_id = usl_id;
                                    parentinfo.putExtra("clt_id", AppController.clt_id);
                                    parentinfo.putExtra("msd_ID", AppController.msd_ID);
                                    parentinfo.putExtra("usl_id", AppController.usl_id);
                                    parentinfo.putExtra("board_name", AppController.Board_name);
                                    parentinfo.putExtra("Std Name", std_name);
                                    parentinfo.putExtra("isStudentResource", isStudentresource);
                                    parentinfo.putExtra("School_name", AppController.school_name);
                                    parentinfo.putExtra("version_name", AppController.versionName);
                                    parentinfo.putExtra("version_code", AppController.versionCode);
                                    parentinfo.putExtra("behaviour_count", behaviourCount);
                                    parentinfo.putExtra("annpuncement_count", AppController.announcementCount);
                                    startActivity(parentinfo);
                                }
                            } else if (position == 6) {
                                Intent setting = new Intent(BehaviourActivity.this, SettingActivity.class);
                                AppController.usl_id = usl_id;
                                setting.putExtra("usl_id", usl_id);
                                setting.putExtra("version_name", AppController.versionName);
                                setting.putExtra("version_code", AppController.versionCode);
                                setting.putExtra("Std Name", std_name);
                                setting.putExtra("isStudentResource", isStudentresource);
                                setting.putExtra("annpuncement_count", AppController.announcementCount);
                                setting.putExtra("behaviour_count", behaviourCount);
                                startActivity(setting);
                            } else if (position == 7) {
                                Intent signout = new Intent(BehaviourActivity.this, LoginActivity.class);
                                Constant.SetLoginData("", "", BehaviourActivity.this);
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancel(notificationID);
                                AppController.drawerSignOut = "true";
                                LoginActivity.loggedIn = "false";
                                startActivity(signout);
                            } else if (position == 8) {
                                final Dialog alertDialog = new Dialog(BehaviourActivity.this);
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
                    drawerListView.setAdapter(new CustomAccount(BehaviourActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));
                    drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if (position == 0) {
                                Intent profile = new Intent(BehaviourActivity.this, ProfileActivity.class);

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
                                profile.putExtra("behaviour_count", behaviourCount);
                                profile.putExtra("annpuncement_count", AppController.announcementCount);
                                startActivity(profile);
                            } else if (position == 1) {
                                if (dft.isInternetOn() == true) {
                                    Intent dashboard = new Intent(BehaviourActivity.this, DashboardActivity.class);
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
                                    Intent feesInfo = new Intent(BehaviourActivity.this, FeesInfoActivity.class);

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
                                    Intent student_resource = new Intent(BehaviourActivity.this, StudentResourcesActivity.class);

                                    student_resource.putExtra("board_name", AppController.Board_name);
                                    student_resource.putExtra("clt_id", AppController.clt_id);
                                    student_resource.putExtra("msd_ID", AppController.msd_ID);
                                    student_resource.putExtra("usl_id", AppController.usl_id);
                                    student_resource.putExtra("School_name", AppController.school_name);
                                    student_resource.putExtra("version_name", AppController.versionName);
                                    student_resource.putExtra("annpuncement_count", AppController.announcementCount);
                                    student_resource.putExtra("behaviour_count", behaviourCount);
                                    student_resource.putExtra("Std Name", std_name);
                                    student_resource.putExtra("isStudentResource", isStudentresource);
                                    student_resource.putExtra("board_name", AppController.Board_name);
                                    startActivity(student_resource);
                                }
                            } else if (position == 4) {
                                if (dft.isInternetOn() == true) {
                                    Intent attendance = new Intent(BehaviourActivity.this, AttendanceActivity.class);

                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_id;
                                    AppController.usl_id = usl_id;
                                    attendance.putExtra("clt_id", AppController.clt_id);
                                    attendance.putExtra("msd_ID", AppController.msd_ID);
                                    attendance.putExtra("usl_id", AppController.usl_id);
                                    attendance.putExtra("School_name", AppController.school_name);
                                    attendance.putExtra("board_name", AppController.Board_name);
                                    attendance.putExtra("Std Name", std_name);
                                    attendance.putExtra("annpuncement_count", announcementCount);
                                    attendance.putExtra("behaviour_count", behaviourCount);
                                    attendance.putExtra("isStudentResource", isStudentresource);
                                    attendance.putExtra("version_name", AppController.versionName);
                                    startActivity(attendance);
                                }
                            } else if (position == 5) {
                                if (dft.isInternetOn() == true) {
                                    Intent parentinfo = new Intent(BehaviourActivity.this, ParentInformationActivity.class);
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
                                Intent setting = new Intent(BehaviourActivity.this, SettingActivity.class);
                                AppController.usl_id = usl_id;
                                setting.putExtra("usl_id", AppController.usl_id);
                                setting.putExtra("Std Name", std_name);
                                setting.putExtra("isStudentResource", isStudentresource);
                                setting.putExtra("version_name", AppController.versionName);
                                setting.putExtra("annpuncement_count", AppController.announcementCount);
                                setting.putExtra("behaviour_count", behaviourCount);
                                startActivity(setting);
                            } else if (position == 7) {
                                Intent signout = new Intent(BehaviourActivity.this, LoginActivity.class);
                                Constant.SetLoginData("", "", BehaviourActivity.this);
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancel(notificationID);
                                AppController.drawerSignOut = "true";
                                LoginActivity.loggedIn = "false";
                                startActivity(signout);
                            } else if (position == 8) {
                                final Dialog alertDialog = new Dialog(BehaviourActivity.this);
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
                    drawerListView.setAdapter(new CustomAccount(BehaviourActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));
                    drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if (position == 0) {
                                Intent profile = new Intent(BehaviourActivity.this, ProfileActivity.class);

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
                                profile.putExtra("isStudentResource", isStudentresource);
                                profile.putExtra("behaviour_count", behaviourCount);
                                profile.putExtra("annpuncement_count", AppController.announcementCount);
                                startActivity(profile);
                            } else if (position == 1) {
                                if (dft.isInternetOn() == true) {
                                    Intent dashboard = new Intent(BehaviourActivity.this, DashboardActivity.class);
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
                                    Intent feesInfo = new Intent(BehaviourActivity.this, FeesInfoActivity.class);
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
                                    Intent attendance = new Intent(BehaviourActivity.this, AttendanceActivity.class);

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
                                    attendance.putExtra("annpuncement_count", AppController.announcementCount);
                                    attendance.putExtra("behaviour_count", behaviourCount);
                                    attendance.putExtra("isStudentResource", isStudentresource);
                                    startActivity(attendance);
                                }
                            } else if (position == 4) {
                                if (dft.isInternetOn() == true) {
                                    Intent parentinfo = new Intent(BehaviourActivity.this, ParentInformationActivity.class);
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
                                    parentinfo.putExtra("isStudentResource", isStudentresource);
                                    parentinfo.putExtra("annpuncement_count", AppController.announcementCount);
                                    parentinfo.putExtra("behaviour_count", behaviourCount);
                                    startActivity(parentinfo);
                                }
                            } else if (position == 5) {
                                Intent setting = new Intent(BehaviourActivity.this, SettingActivity.class);
                                AppController.usl_id = usl_id;
                                setting.putExtra("usl_id", AppController.usl_id);
                                setting.putExtra("Std Name", std_name);
                                setting.putExtra("version_name", AppController.versionName);
                                setting.putExtra("isStudentResource", isStudentresource);
                                setting.putExtra("annpuncement_count", AppController.announcementCount);
                                setting.putExtra("behaviour_count", behaviourCount);
                                startActivity(setting);
                            } else if (position == 6) {
                                try {
                                    Intent signout = new Intent(BehaviourActivity.this, LoginActivity.class);
                                    Constant.SetLoginData("", "", BehaviourActivity.this);
                                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.cancel(notificationID);

                                    LoginActivity.loggedIn = "false";
                                    AppController.drawerSignOut = "true";
                                    startActivity(signout);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (position == 7) {
                                final Dialog alertDialog = new Dialog(BehaviourActivity.this);
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

                }
            }
            Constant.setDrawerData(AppController.drawerAcademic_year, AppController.drawerChild_name, AppController.drawerStd, AppController.drawerRoll_no, BehaviourActivity.this);
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
        std_name = intent.getStringExtra("Std Name");
        isStudentresource = intent.getStringExtra("isStudentResource");
        AppController.versionName = versionName;
        AppController.clt_id = clt_id;
        AppController.msd_ID = msd_id;
        AppController.usl_id = usl_id;
        AppController.school_name = school_name;
        AppController.Board_name = board_name;
        AppController.announcementCount = announcementCount;
        behaviourCount = intent.getStringExtra("behaviour_count");
    }

    private void announcentTab() {
        if(Integer.valueOf(announcementCount)>0){
            btn_announce.setVisibility(View.VISIBLE);
        }
        else{
            btn_announce.setVisibility(View.GONE);
            if(Integer.valueOf(announcementCount)== 0 && Integer.valueOf(behaviourCount)==0){
                ll_footer.setWeightSum(2.0f);
            }
            else{
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
    private void init() {
        img_drawer.setOnClickListener(this);
        btn_announce.setOnClickListener(this);
        btn_attendance.setOnClickListener(this);
        btn_behaviour.setOnClickListener(this);
        btn_dashboard.setOnClickListener(this);
        lay_back_investment.setOnClickListener(this);
        header = new HeaderControler(this,true,false,"Behaviour");
        progressDialog = Constant.getProgressDialog(this);
        dft = new DataFetchService(this);
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
        list_behaviours = (ListView) findViewById(R.id.list_behaviours);
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
        btn_attendance.setBackgroundColor(Color.parseColor("#D6D6CC"));
        btn_announce.setBackgroundColor(Color.parseColor("#D6D6CC"));
        btn_behaviour.setBackgroundColor(Color.parseColor("#2d2c23"));
        btn_dashboard.setTextColor(Color.BLACK);
        btn_behaviour.setTextColor(Color.WHITE);
        btn_announce.setTextColor(Color.BLACK);
        btn_attendance.setTextColor(Color.BLACK);

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
        int width = getResources().getDisplayMetrics().widthPixels/2 +30;
        DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) leftfgf_drawer.getLayoutParams();
        lp.width = width + 30;
        leftfgf_drawer.setLayoutParams(lp);


    }
    private void CallbehaviourWebservice(String clt_id, final String usl_id,String msd_id) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getbehaviour(clt_id, usl_id, msd_id, behaviourMethodName, Request.Method.POST,
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
                                list_behaviours.setVisibility(View.VISIBLE);
                                setUIData();

                            } else  if(login_details.Status.equalsIgnoreCase("0")){
                                tv_no_record.setText("No Records Found");
                                list_behaviours.setVisibility(View.GONE);

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
        behaviourAdpater = new BehaviourAdapter(BehaviourActivity.this,login_details.BehavioursResult);
        list_behaviours.setAdapter(behaviourAdpater);
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
        {
            if (dft.isInternetOn() == true) {
                Intent announce = new Intent(BehaviourActivity.this, AnnouncementActivity.class);
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
                announce.putExtra("School_name", AppController.school_name);
                announce.putExtra("annpuncement_count", announcementCount);
                announce.putExtra("behaviour_count", behaviourCount);
                announce.putExtra("isStudentResource", isStudentresource);
                startActivity(announce);
            }
        }
        else if(v==btn_attendance)
        {
            if (dft.isInternetOn() == true) {
                Intent attendance = new Intent(BehaviourActivity.this, AttendanceActivity.class);
                AppController.clt_id = clt_id;
                AppController.msd_ID = msd_id;
                AppController.usl_id = usl_id;
                AppController.school_name = school_name;
                attendance.putExtra("clt_id", AppController.clt_id);
                attendance.putExtra("msd_ID", AppController.msd_ID);
                attendance.putExtra("usl_id", AppController.usl_id);
                attendance.putExtra("board_name", board_name);
                attendance.putExtra("version_name", AppController.versionName);
                AppController.versionName = versionName;
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
                Intent behaviour = new Intent(BehaviourActivity.this, BehaviourActivity.class);
                AppController.clt_id = clt_id;
                AppController.msd_ID = msd_id;
                AppController.usl_id = usl_id;
                AppController.school_name = school_name;
                behaviour.putExtra("clt_id", AppController.clt_id);
                behaviour.putExtra("msd_ID", AppController.msd_ID);
                behaviour.putExtra("usl_id", AppController.usl_id);
                behaviour.putExtra("School_name", AppController.school_name);
                behaviour.putExtra("board_name", board_name);
                behaviour.putExtra("version_name", AppController.versionName);
                behaviour.putExtra("annpuncement_count", announcementCount);
                behaviour.putExtra("behaviour_count", behaviourCount);
                AppController.versionName = versionName;
                behaviour.putExtra("isStudentResource", isStudentresource);
                startActivity(behaviour);
            }
        }
        else if(v==btn_dashboard)
        {
            if (dft.isInternetOn() == true) {
                Intent dash = new Intent(BehaviourActivity.this, DashboardActivity.class);
                AppController.clt_id = clt_id;
                AppController.msd_ID = msd_id;
                AppController.usl_id = usl_id;
                AppController.school_name = school_name;
                dash.putExtra("clt_id", AppController.clt_id);
                dash.putExtra("msd_ID", AppController.msd_ID);
                dash.putExtra("usl_id", AppController.usl_id);
                dash.putExtra("board_name", board_name);
                dash.putExtra("version_name", AppController.versionName);
                AppController.versionName = versionName;
                dash.putExtra("School_name", AppController.school_name);
                dash.putExtra("annpuncement_count", announcementCount);
                dash.putExtra("behaviour_count", behaviourCount);
                dash.putExtra("isStudentResource", isStudentresource);
                startActivity(dash);
            }
        }
        else if(v==lay_back_investment)
        {
            if(AppController.SiblingActivity.equalsIgnoreCase("true"))
            {
                Intent back = new Intent(BehaviourActivity.this,Profile_Sibling.class);
                AppController.OnBackpressed = "false";
                AppController.clt_id = clt_id;
                AppController.msd_ID = msd_id;
                AppController.usl_id = usl_id;
                AppController.school_name = school_name;
                back.putExtra("clt_id", AppController.clt_id);
                back.putExtra("msd_ID", AppController.msd_ID);
                back.putExtra("usl_id", AppController.usl_id);
                back.putExtra("version_name", AppController.versionName);
                AppController.versionName = versionName;
                back.putExtra("School_name", AppController.school_name);
                back.putExtra("annpuncement_count", announcementCount);
                back.putExtra("behaviour_count",behaviourCount);
                back.putExtra("board_name",board_name);
                back.putExtra("isStudentResource",isStudentresource);

                startActivity(back);
            }
            else {
                Intent back = new Intent(BehaviourActivity.this,ProfileActivity.class);
                AppController.OnBackpressed = "false";
                AppController.clt_id = clt_id;
                AppController.msd_ID = msd_id;
                AppController.usl_id = usl_id;
                AppController.school_name = school_name;
                back.putExtra("clt_id", AppController.clt_id);
                back.putExtra("msd_ID", AppController.msd_ID);
                back.putExtra("usl_id", AppController.usl_id);
                back.putExtra("School_name", AppController.school_name);
                back.putExtra("board_name",board_name);
                back.putExtra("version_name", AppController.versionName);
                back.putExtra("annpuncement_count",announcementCount);
                back.putExtra("behaviour_count",behaviourCount);
                AppController.versionName = versionName;
                back.putExtra("isStudentResource",isStudentresource);
                startActivity(back);
            }

        }

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
        }
        else
        {
            if (AppController.SiblingActivity.equalsIgnoreCase("true")) {
                Intent back = new Intent(BehaviourActivity.this, Profile_Sibling.class);
                AppController.OnBackpressed = "false";
                AppController.clt_id = clt_id;
                AppController.msd_ID = msd_id;
                AppController.usl_id = usl_id;
                AppController.school_name = school_name;
                back.putExtra("clt_id", AppController.clt_id);
                back.putExtra("msd_ID", AppController.msd_ID);
                back.putExtra("usl_id", AppController.usl_id);
                back.putExtra("Board Name", board_name);
                back.putExtra("version_name", AppController.versionName);
                AppController.versionName = versionName;
                back.putExtra("annpuncement_count", announcementCount);
                back.putExtra("behaviour_count", behaviourCount);
                back.putExtra("isStudentResource",isStudentresource);
                back.putExtra("School_name", AppController.school_name);

                startActivity(back);
            } else {
                Intent back = new Intent(BehaviourActivity.this, ProfileActivity.class);
                AppController.OnBackpressed = "false";
                AppController.clt_id = clt_id;
                AppController.msd_ID = msd_id;
                AppController.usl_id = usl_id;
                AppController.school_name = school_name;
                back.putExtra("clt_id", AppController.clt_id);
                back.putExtra("msd_ID", AppController.msd_ID);
                back.putExtra("usl_id", AppController.usl_id);
                back.putExtra("Board Name", board_name);
                back.putExtra("version_name", AppController.versionName);
                AppController.versionName = versionName;
                back.putExtra("annpuncement_count", announcementCount);
                back.putExtra("behaviour_count", behaviourCount);
                back.putExtra("isStudentResource",isStudentresource);
                back.putExtra("School_name", AppController.school_name);
                startActivity(back);
            }
        }
    }
}
