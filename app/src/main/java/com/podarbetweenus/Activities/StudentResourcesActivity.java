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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.Adapter.ExpandableTopicsAdapter;
import com.podarbetweenus.Adapter.ResourceAdapter;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.CycleTest;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 11/18/2015.
 */
public class StudentResourcesActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener{
    //UI Variables
    //EditTExt
    EditText ed_cycle_test,ed_subject;
    //Expandable ListView
    ListView topics_ListView,list_resource,drawerListView;
    //ProgressDialog
    ProgressDialog progressDialog;
    //Relative Layout
    RelativeLayout leftfgf_drawer,rl_profile;
    //Linear Layout
    LinearLayout lay_back_investment;
    //ImageView
    ImageView img_close;
    //TextView
    TextView tv_no_record,tv_version_name,tv_version_code,tv_cancel,tv_academic_year_drawer,tv_child_name_drawer,tv_std_value_drawer,tv_roll_no_value_drawer;
    //Drawaer Layout
    DrawerLayout drawer;
    //ImageView
    ImageView img_drawer,img_icon;
    ContentDisplaceDrawerToggle mContentDisplaceToggle;
    Context context;
    //Entity
    LoginDetails login_details;
    DataFetchService dft;
    ExpandableTopicsAdapter expandableTopicsadapter;
    ResourceAdapter resourceAdapter;

    String clt_id,msd_id,usl_id,school_name,board_name,std_name,firstcycle_test,versionName,crl_file_name,firstSubject,subjectId,cycleTest_id,firstTimeSubjectSelection="true",topic_name,crf_id,crl_file,
            behaviourCount,announcementCount,subjectListClicked="",firstTypetopic="",firstTimeCycle="",noResultFound="false",isStudentresource;
    String cycleTestMethodName = "CycleTestDropDown";
    String subjectMethodName = "SubjectDropdown";
    String topicsMethodName = "GetTopicList";
    String resourceMethodName = "GetResourceList";
    int TopicSize,versionCode,notificationID = 1;;
    public ArrayList<CycleTest> CycleTest_dropdownlist = new ArrayList<CycleTest>();
    ArrayList<String> strings_cycleTest;
    ArrayList<String> strings_subject;
    String[] select_cycleTest;
    String[] select_subject;
    List<String> groupTopicList;
    List<String> childResourceList;
    Map<String, List<String>> laptopCollection;
    //LayoutEntity
    HeaderControler header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.studentresources);
        findViews();
        init();
        getIntentId();
        setDrawer();

        subjectListClicked = "false";
        firstTimeCycle = "false";
        //Call Webservice For Cycle Test
        CallCycleTestDropdownWebservice(AppController.Board_name);
        AppController.CycleTest = "false";

    }

    private void setDrawer() {
        try {
            if (AppController.SiblingActivity.equalsIgnoreCase("true")) {
                if (AppController.isStudentResourcePresent.equalsIgnoreCase("0")) {
                    int[] icons_with_sibling = {R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sib_48x48, R.drawable.payfeesonline_48x48, R.drawable.studentresources_48x48, R.drawable.att_48x48, R.drawable.parentinformation_48x48, /*R.drawable.studentresources_48x48,*/ R.drawable.setting1_hdpi,
                            R.drawable.logout1_hdpi, R.drawable.info};
                    String[] data_with_sibling = {"Dashboard", "Messages", "Sibling", "Fees Information", "Student Resources", "Attendance", "Student Information"/*,"Class Room Videos"*/, "Setting",
                            "Sign Out", "About"};
                    drawerListView.setAdapter(new CustomAccount(StudentResourcesActivity.this, android.R.layout.simple_list_item_1, icons_with_sibling, data_with_sibling));
                    drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if (position == 0) {
                                Intent profile = new Intent(StudentResourcesActivity.this, Profile_Sibling.class);

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
                                profile.putExtra("behaviour_count", AppController.behaviourCount);
                                profile.putExtra("version_name", AppController.versionName);
                                profile.putExtra("version_code", AppController.versionCode);
                                profile.putExtra("isStudentResource", isStudentresource);
                                startActivity(profile);
                            } else if (position == 1) {
                                if (dft.isInternetOn() == true) {
                                    Intent dashboard = new Intent(StudentResourcesActivity.this, DashboardActivity.class);
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
                                Intent sibling = new Intent(StudentResourcesActivity.this, SiblingActivity.class);
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
                                sibling.putExtra("behaviour_count", AppController.behaviourCount);
                                sibling.putExtra("version_name", AppController.versionName);
                                sibling.putExtra("version_code", AppController.versionCode);
                                sibling.putExtra("isStudentResource", isStudentresource);
                                startActivity(sibling);
                            } else if (position == 3) {
                                if (dft.isInternetOn() == true) {
                                    Intent feesInfo = new Intent(StudentResourcesActivity.this, FeesInfoActivity.class);

                                    feesInfo.putExtra("board_name", board_name);
                                    feesInfo.putExtra("clt_id", AppController.clt_id);
                                    feesInfo.putExtra("msd_ID", AppController.msd_ID);
                                    feesInfo.putExtra("usl_id", AppController.usl_id);
                                    feesInfo.putExtra("School_name", AppController.school_name);
                                    feesInfo.putExtra("Std Name", std_name);
                                    feesInfo.putExtra("version_name", AppController.versionName);
                                    feesInfo.putExtra("behaviour_count", AppController.behaviourCount);
                                    feesInfo.putExtra("annpuncement_count", AppController.announcementCount);
                                    feesInfo.putExtra("version_code", AppController.versionCode);
                                    feesInfo.putExtra("isStudentResource", isStudentresource);
                                    startActivity(feesInfo);
                                }
                            } else if (position == 4) {
                                if (dft.isInternetOn() == true) {
                                    Intent student_resource = new Intent(StudentResourcesActivity.this, StudentResourcesActivity.class);

                                    student_resource.putExtra("board_name", board_name);
                                    student_resource.putExtra("clt_id", AppController.clt_id);
                                    student_resource.putExtra("msd_ID", AppController.msd_ID);
                                    student_resource.putExtra("usl_id", AppController.usl_id);
                                    student_resource.putExtra("School_name", AppController.school_name);
                                    student_resource.putExtra("Std Name", std_name);
                                    student_resource.putExtra("version_name", AppController.versionName);
                                    student_resource.putExtra("version_code", AppController.versionCode);
                                    student_resource.putExtra("behaviour_count", AppController.behaviourCount);
                                    student_resource.putExtra("annpuncement_count", AppController.announcementCount);
                                    student_resource.putExtra("isStudentResource", isStudentresource);
                                    startActivity(student_resource);
                                }
                            } else if (position == 5) {
                                if (dft.isInternetOn() == true) {
                                    Intent attendance = new Intent(StudentResourcesActivity.this, AttendanceActivity.class);

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
                                    attendance.putExtra("annpuncement_count", AppController.announcementCount);
                                    attendance.putExtra("behaviour_count", AppController.behaviourCount);
                                    attendance.putExtra("isStudentResource", isStudentresource);
                                    startActivity(attendance);
                                }
                            } else if (position == 6) {
                                if (dft.isInternetOn() == true) {
                                    Intent parentinfo = new Intent(StudentResourcesActivity.this, ParentInformationActivity.class);

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
                                    parentinfo.putExtra("annpuncement_count", AppController.announcementCount);
                                    parentinfo.putExtra("behaviour_count", AppController.behaviourCount);
                                    parentinfo.putExtra("isStudentResource", isStudentresource);
                                    startActivity(parentinfo);
                                }
                            } else if (position == 7) {

                                Intent setting = new Intent(StudentResourcesActivity.this, SettingActivity.class);
                                AppController.usl_id = usl_id;
                                setting.putExtra("usl_id", usl_id);
                                setting.putExtra("version_name", AppController.versionName);
                                setting.putExtra("Std Name", std_name);
                                setting.putExtra("version_code", AppController.versionCode);
                                setting.putExtra("isStudentResource", isStudentresource);
                                startActivity(setting);
                            } else if (position == 8) {
                                Intent signout = new Intent(StudentResourcesActivity.this, LoginActivity.class);
                                Constant.SetLoginData("", "", StudentResourcesActivity.this);
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancel(notificationID);
                                AppController.drawerSignOut = "true";
                                AppController.fromSplashScreen = "false";
                                LoginActivity.loggedIn = "false";
                                startActivity(signout);
                            } else if (position == 9) {
                                final Dialog alertDialog = new Dialog(StudentResourcesActivity.this);
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
                    String[] data_with_sibling = {"Dashboard", "Messages", "Sibling", "Fess Info", "Attendance", "Student Information"/*,"Class Room Videos"*/, "Setting",
                            "Sign Out", "About"};
                    drawerListView.setAdapter(new CustomAccount(StudentResourcesActivity.this, android.R.layout.simple_list_item_1, icons_with_sibling, data_with_sibling));
                    drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if (position == 0) {
                                Intent profile = new Intent(StudentResourcesActivity.this, Profile_Sibling.class);
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
                                profile.putExtra("version_name", AppController.versionName);
                                profile.putExtra("version_code", AppController.versionCode);
                                profile.putExtra("annpuncement_count", AppController.announcementCount);
                                profile.putExtra("behaviour_count", AppController.behaviourCount);
                                profile.putExtra("isStudentResource", isStudentresource);
                                startActivity(profile);
                            } else if (position == 1) {
                                if (dft.isInternetOn() == true) {
                                    Intent dashboard = new Intent(StudentResourcesActivity.this, DashboardActivity.class);
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
                                Intent sibling = new Intent(StudentResourcesActivity.this, SiblingActivity.class);
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
                                sibling.putExtra("version_name", AppController.versionName);
                                sibling.putExtra("version_code", AppController.versionCode);
                                sibling.putExtra("isStudentResource", isStudentresource);
                                startActivity(sibling);
                            } else if (position == 3) {
                                if (dft.isInternetOn() == true) {
                                    Intent feesInfo = new Intent(StudentResourcesActivity.this, FeesInfoActivity.class);

                                    feesInfo.putExtra("board_name", board_name);
                                    feesInfo.putExtra("clt_id", AppController.clt_id);
                                    feesInfo.putExtra("msd_ID", AppController.msd_ID);
                                    feesInfo.putExtra("usl_id", AppController.usl_id);
                                    feesInfo.putExtra("School_name", AppController.school_name);
                                    feesInfo.putExtra("Std Name", std_name);
                                    feesInfo.putExtra("version_name", AppController.versionName);
                                    feesInfo.putExtra("annpuncement_count", AppController.announcementCount);
                                    feesInfo.putExtra("behaviour_count", AppController.behaviourCount);
                                    feesInfo.putExtra("version_code", AppController.versionCode);
                                    feesInfo.putExtra("isStudentResource", isStudentresource);
                                    startActivity(feesInfo);
                                }
                            } else if (position == 4) {
                                if (dft.isInternetOn() == true) {
                                    Intent attendance = new Intent(StudentResourcesActivity.this, AttendanceActivity.class);

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
                                    attendance.putExtra("behaviour_count", AppController.behaviourCount);
                                    attendance.putExtra("annpuncement_count", AppController.announcementCount);
                                    attendance.putExtra("isStudentResource", isStudentresource);
                                    startActivity(attendance);
                                }
                            } else if (position == 5) {
                                if (dft.isInternetOn() == true) {
                                    Intent parentinfo = new Intent(StudentResourcesActivity.this, ParentInformationActivity.class);

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
                                    parentinfo.putExtra("behaviour_count", AppController.behaviourCount);
                                    parentinfo.putExtra("annpuncement_count", AppController.announcementCount);
                                    parentinfo.putExtra("isStudentResource", isStudentresource);
                                    startActivity(parentinfo);
                                }
                            } else if (position == 6) {
                                Intent setting = new Intent(StudentResourcesActivity.this, SettingActivity.class);
                                AppController.usl_id = usl_id;
                                setting.putExtra("usl_id", usl_id);
                                setting.putExtra("Std Name", std_name);
                                setting.putExtra("version_name", AppController.versionName);
                                setting.putExtra("version_code", AppController.versionCode);
                                setting.putExtra("annpuncement_count", AppController.announcementCount);
                                setting.putExtra("behaviour_count", AppController.behaviourCount);
                                setting.putExtra("isStudentResource", isStudentresource);
                                startActivity(setting);
                            } else if (position == 7) {
                                Intent signout = new Intent(StudentResourcesActivity.this, LoginActivity.class);
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancel(notificationID);
                                Constant.SetLoginData("", "", StudentResourcesActivity.this);
                                AppController.drawerSignOut = "true";
                                AppController.fromSplashScreen = "false";
                                LoginActivity.loggedIn = "false";
                                startActivity(signout);
                            } else if (position == 8) {
                                final Dialog alertDialog = new Dialog(StudentResourcesActivity.this);
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
                    drawerListView.setAdapter(new CustomAccount(StudentResourcesActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));
                    drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if (position == 0) {
                                Intent profile = new Intent(StudentResourcesActivity.this, ProfileActivity.class);

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
                                    Intent dashboard = new Intent(StudentResourcesActivity.this, DashboardActivity.class);
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
                                if (dft.isInternetOn() == true) {
                                    Intent feesInfo = new Intent(StudentResourcesActivity.this, FeesInfoActivity.class);

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
                                }
                            } else if (position == 3) {
                                if (dft.isInternetOn() == true) {
                                    Intent student_resource = new Intent(StudentResourcesActivity.this, StudentResourcesActivity.class);

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
                                    Intent attendance = new Intent(StudentResourcesActivity.this, AttendanceActivity.class);

                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_id;
                                    AppController.usl_id = usl_id;
                                    attendance.putExtra("clt_id", AppController.clt_id);
                                    attendance.putExtra("msd_ID", AppController.msd_ID);
                                    attendance.putExtra("usl_id", AppController.usl_id);
                                    attendance.putExtra("School_name", AppController.school_name);
                                    attendance.putExtra("board_name", AppController.Board_name);
                                    attendance.putExtra("Std Name", std_name);
                                    attendance.putExtra("annpuncement_count", AppController.announcementCount);
                                    attendance.putExtra("behaviour_count", behaviourCount);
                                    attendance.putExtra("isStudentResource", isStudentresource);
                                    attendance.putExtra("version_name", AppController.versionName);
                                    startActivity(attendance);
                                }
                            } else if (position == 5) {
                                if (dft.isInternetOn() == true) {
                                    Intent parentinfo = new Intent(StudentResourcesActivity.this, ParentInformationActivity.class);
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
                                Intent setting = new Intent(StudentResourcesActivity.this, SettingActivity.class);
                                AppController.usl_id = usl_id;
                                setting.putExtra("usl_id", AppController.usl_id);
                                setting.putExtra("Std Name", std_name);
                                setting.putExtra("version_name", AppController.versionName);
                                setting.putExtra("annpuncement_count", AppController.announcementCount);
                                setting.putExtra("behaviour_count", behaviourCount);
                                setting.putExtra("isStudentResource", isStudentresource);
                                startActivity(setting);
                            } else if (position == 7) {
                                Intent signout = new Intent(StudentResourcesActivity.this, LoginActivity.class);
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancel(notificationID);
                                Constant.SetLoginData("", "", StudentResourcesActivity.this);
                                AppController.drawerSignOut = "true";
                                AppController.fromSplashScreen = "false";
                                LoginActivity.loggedIn = "false";
                                startActivity(signout);
                            } else if (position == 8) {
                                final Dialog alertDialog = new Dialog(StudentResourcesActivity.this);
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
                    int[] icons_without_sibling = {R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.payfeesonline_48x48, R.drawable.att_48x48, R.drawable.parentinformation_48x48,/* R.drawable.studentresources_48x48,*/ R.drawable.setting1_hdpi,
                            R.drawable.logout1_hdpi, R.drawable.info};
                    final String[] data_without_sibling = {"Dashboard", "Messages", "Fees Information", "Attendance", "Student Information",/* "Class Room Videos",*/ "Setting",
                            "Sign Out", "About"};
                    drawerListView.setAdapter(new CustomAccount(StudentResourcesActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));
                    drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if (position == 0) {
                                Intent profile = new Intent(StudentResourcesActivity.this, ProfileActivity.class);

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
                                    Intent dashboard = new Intent(StudentResourcesActivity.this, DashboardActivity.class);
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
                                    Intent feesInfo = new Intent(StudentResourcesActivity.this, FeesInfoActivity.class);

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
                                    Intent attendance = new Intent(StudentResourcesActivity.this, AttendanceActivity.class);

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
                                    Intent parentinfo = new Intent(StudentResourcesActivity.this, ParentInformationActivity.class);
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
                                Intent setting = new Intent(StudentResourcesActivity.this, SettingActivity.class);
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
                                    Intent signout = new Intent(StudentResourcesActivity.this, LoginActivity.class);
                                    Constant.SetLoginData("", "", StudentResourcesActivity.this);
                                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.cancel(notificationID);
                                    AppController.drawerSignOut = "true";
                                    AppController.fromSplashScreen = "false";
                                    LoginActivity.loggedIn = "false";

                                    startActivity(signout);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (position == 7) {
                                final Dialog alertDialog = new Dialog(StudentResourcesActivity.this);
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
            Constant.setDrawerData(AppController.drawerAcademic_year, AppController.drawerChild_name, AppController.drawerStd, AppController.drawerRoll_no, StudentResourcesActivity.this);
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
    private void getIntentId() {

        Intent intent = getIntent();
        board_name = intent.getStringExtra("board_name");
        clt_id = intent.getStringExtra("clt_id");
        AppController.clt_id = clt_id;
        msd_id = intent.getStringExtra("msd_ID");
        usl_id = intent.getStringExtra("usl_id");
        school_name = intent.getStringExtra("School_name");
        std_name = intent.getStringExtra("Std Name");
        announcementCount = intent.getStringExtra("annpuncement_count");
        behaviourCount = intent.getStringExtra("behaviour_count");
        AppController.std_name = std_name;
        isStudentresource = intent.getStringExtra("isStudentResource");
        versionName = intent.getStringExtra("version_name");
        versionCode = intent.getIntExtra("version_code", 0);
        AppController.versionName = versionName;
        AppController.versionCode = versionCode;
        AppController.school_name = school_name;
        AppController.Board_name = board_name;
        AppController.usl_id = usl_id;
    }

    private void init() {

        img_drawer.setOnClickListener(this);
        header = new HeaderControler(this,true,false,"Student Resources");
        dft = new DataFetchService(this);
        progressDialog = Constant.getProgressDialog(this);
        ed_cycle_test.setOnClickListener(this);
        ed_subject.setOnClickListener(this);
        lay_back_investment.setOnClickListener(this);
        topics_ListView.setOnItemClickListener(this);
        login_details = new LoginDetails();

    }

    private void findViews() {
        //EditText
        ed_cycle_test = (EditText) findViewById(R.id.ed_cycle_test);
        ed_subject = (EditText) findViewById(R.id.ed_subject);
        //ListView
        topics_ListView = (ListView) findViewById(R.id.topics_ListView);
        //Linear Layout
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
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

    @Override
    public void onClick(View v) {
        if(v==img_drawer)
        {
            drawer.openDrawer(Gravity.RIGHT);
        }
       else if(v==ed_cycle_test)
        {
            AppController.CycleTest="true";
            subjectListClicked = "false";
            firstTimeCycle = "true";
            //Call Webservice For Cycle Test
            CallCycleTestDropdownWebservice(AppController.Board_name);
        }
        else if(v==ed_subject){
            AppController.CycleTest="true";

            if(firstTimeSubjectSelection.equalsIgnoreCase("true") && noResultFound.equalsIgnoreCase("false"))
            {
                try {
                    // Call Webservice For Subject
                    CallSubjectDropdownWebservice(clt_id, msd_id, firstcycle_test);
                    firstTimeSubjectSelection = "false";
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            else{
                CallSubjectDropdownWebservice(clt_id,msd_id,cycleTest_id);
            }
        }
        else if(v==lay_back_investment) {

                if (AppController.SiblingActivity.equalsIgnoreCase("true")) {
                    Intent back = new Intent(StudentResourcesActivity.this, Profile_Sibling.class);
                    AppController.OnBackpressed = "false";
                    AppController.clt_id = clt_id;
                    AppController.msd_ID = msd_id;
                    AppController.usl_id = usl_id;
                    back.putExtra("clt_id", AppController.clt_id);
                    back.putExtra("msd_ID", AppController.msd_ID);
                    back.putExtra("usl_id", AppController.usl_id);
                    back.putExtra("board_name", board_name);
                    back.putExtra("version_name", AppController.versionName);
                    back.putExtra("verion_code", AppController.versionCode);
                    back.putExtra("School_name", AppController.school_name);
                    back.putExtra("isStudentResource", isStudentresource);
                    AppController.Board_name = board_name;
                    startActivity(back);
                }
            else{
                Intent back = new Intent(StudentResourcesActivity.this, ProfileActivity.class);
                AppController.OnBackpressed = "false";
                AppController.clt_id = clt_id;
                AppController.msd_ID = msd_id;
                AppController.usl_id = usl_id;
                back.putExtra("clt_id", AppController.clt_id);
                back.putExtra("msd_ID", AppController.msd_ID);
                back.putExtra("usl_id", AppController.usl_id);
                back.putExtra("board_name", board_name);
                back.putExtra("version_name", AppController.versionName);
                back.putExtra("verion_code", AppController.versionCode);
                back.putExtra("School_name", AppController.school_name);
                AppController.Board_name = board_name;
                back.putExtra("isStudentResource",isStudentresource);
                startActivity(back);
            }
        }

    }

    private void  CallCycleTestDropdownWebservice(String Brd_name) {
        try {
            if (dft.isInternetOn() == true) {
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            } else {
                progressDialog.dismiss();
            }
            dft.getcycleTestdeopdown(Brd_name, cycleTestMethodName, Request.Method.POST,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                                if (login_details.Status.equalsIgnoreCase("1")) {
                                    firstTypetopic = "true";
                                    strings_cycleTest = new ArrayList<String>();

                                    try {
                                        for (int i = 0; i < login_details.CycleTest.size(); i++) {
                                            strings_cycleTest.add(login_details.CycleTest.get(i).cyc_name.toString());
                                            select_cycleTest = new String[strings_cycleTest.size()];
                                            select_cycleTest = strings_cycleTest.toArray(select_cycleTest);
                                            firstcycle_test = login_details.CycleTest.get(0).cyc_name;
                                            AppController.CycleTestID = firstcycle_test;
                                            if (firstTimeCycle.equalsIgnoreCase("false")) {
                                                ed_cycle_test.setText(firstcycle_test);
                                            }
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    selectCycleTest(select_cycleTest);
                                    AppController.CycleTest = "false";
                                    // Call Webservice For Subject
                                    if (noResultFound.equalsIgnoreCase("false") && firstTimeSubjectSelection.equalsIgnoreCase("true")) {
                                        CallSubjectDropdownWebservice(clt_id, msd_id, firstcycle_test);
                                    }
                                } else if (login_details.Status.equalsIgnoreCase("0")) {
                                    ed_subject.setText("");
                                    topics_ListView.setVisibility(View.GONE);
                                    noResultFound = "true";
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Show error or whatever...
                            Log.d("StudentResourceActivity", "ERROR.._---" + error.getCause());


                        }
                    });


        } catch (Exception e) {
            e.printStackTrace();
        }
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
                //Call Webservice for Subject DropDown
                CallSubjectDropdownWebservice(clt_id, msd_id, AppController.CycleTestID);
            }
        });
            if (AppController.CycleTest.equalsIgnoreCase("true")) {
                alertDialog.show();
            }
    }

    private String getIDfromCycleTest(String selectedcycleTest) {
        String id = "0";
        for(int i=0;i<login_details.CycleTest.size();i++) {
            if(login_details.CycleTest.get(i).cyc_name.equalsIgnoreCase(selectedcycleTest)){
                id= login_details.CycleTest.get(i).cyc_name;
            }

        }
        return id;
    }

    private void CallSubjectDropdownWebservice(final String clt_id,String msd_id, final String cycleTest_id) {
        try {

            dft.getsubjectdropdown(clt_id, msd_id, cycleTest_id, subjectMethodName, Request.Method.POST,
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
                                    strings_subject = new ArrayList<String>();

                                    try {
                                        for (int i = 0; i < login_details.Subject.size(); i++) {
                                            strings_subject.add(login_details.Subject.get(i).sbj_name.toString());
                                            select_subject = new String[strings_subject.size()];
                                            select_subject = strings_subject.toArray(select_subject);
                                            firstSubject = login_details.Subject.get(0).sbj_name;
                                            if (subjectListClicked.equalsIgnoreCase("false")) {
                                                ed_subject.setText(firstSubject);
                                            }
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    selectSubjectTest(select_subject);
                                    if (firstTypetopic.equalsIgnoreCase("true")) {
                                        try {
                                            //call webservice for topics
                                            CallTopicsWebservice(AppController.Board_name, std_name, cycleTest_id, firstSubject, clt_id);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else if (login_details.Status.equalsIgnoreCase("0")) {
                                    ed_subject.setText("");
                                    topics_ListView.setVisibility(View.GONE);
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
                            Log.d("LoginActivity", "ERROR.._---" + error.getCause());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectSubjectTest( final String[] select_subject) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.dialog_list, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Select Subject");
        subjectListClicked = "true";
        final ListView select_list = (ListView) convertView.findViewById(R.id.select_list);

        alertDialog.setItems(select_subject, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection

                firstTypetopic = "false";
                ed_subject.setText(select_subject[item]);
                dialog.dismiss();
                String selectedSubject = select_subject[item];
                subjectId = selectedSubject;
                AppController.SubjectId = subjectId;
                try {
                    //call webservice for topics
                    CallTopicsWebservice(board_name, std_name, AppController.CycleTestID, AppController.SubjectId,clt_id);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
            if(AppController.CycleTest.equalsIgnoreCase("true")) {
                alertDialog.show();
            }
    }

    private String getIDfromSubject(String selectedcycleTest) {
        String id = "0";
        for(int i=0;i<login_details.CycleTest.size();i++) {
            if(login_details.CycleTest.get(i).cyc_name.equalsIgnoreCase(selectedcycleTest)){
                id= login_details.CycleTest.get(i).cyc_id;
            }

        }
        return id;
    }

    private void CallTopicsWebservice(String board_name,String std_name,String cycleTest_id,String subjectId,String clt_id) {
        try {
            dft.gettopicslist(board_name, std_name, cycleTest_id, subjectId, clt_id, topicsMethodName, Request.Method.POST,
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
                                    topics_ListView.setVisibility(View.VISIBLE);
                                    setUIData();

                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                    TopicSize = login_details.TopicList.size();
                                    AppController.TopicsListsize = login_details.TopicList;

                                } else if (login_details.Status.equalsIgnoreCase("0")) {
                                    tv_no_record.setVisibility(View.VISIBLE);
                                    tv_no_record.setText("No Records Found");
                                    topics_ListView.setVisibility(View.GONE);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }


                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Show error or whatever...
                            Log.d("StudentResourceActivity", "ERROR.._---" + error.getCause());

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUIData() {
        expandableTopicsadapter = new ExpandableTopicsAdapter(StudentResourcesActivity.this,login_details.TopicList);
        topics_ListView.setAdapter(expandableTopicsadapter);
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
    private void callResourceListwebservcie(String crf_id) {
        try {
            if (dft.isInternetOn() == true) {
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            } else {
                progressDialog.dismiss();
            }
            dft.getResourcelist(crf_id, resourceMethodName, Request.Method.POST,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            try {
                                login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);

                                if (login_details.Status.equalsIgnoreCase("1")) {

                                    setResourceIDData();

                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                } else if (login_details.Status.equalsIgnoreCase("0")) {

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
        } catch (Exception e) {
            e.printStackTrace();
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

        Log.e("ResourceList", String.valueOf(login_details.ResourceList.size()));
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
                 //   String resourcefile_url = "https://drive.google.com/drive/folders/"+file_id;
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
    public void onBackPressed() {
        // super.onBackPressed();

        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
        }
        else {
            if (AppController.SiblingActivity.equalsIgnoreCase("true")) {
                Intent back = new Intent(StudentResourcesActivity.this, Profile_Sibling.class);
                AppController.OnBackpressed = "false";
                AppController.clt_id = clt_id;
                AppController.msd_ID = msd_id;
                AppController.usl_id = usl_id;
                back.putExtra("clt_id", AppController.clt_id);
                back.putExtra("msd_ID", AppController.msd_ID);
                back.putExtra("usl_id", AppController.usl_id);
                back.putExtra("board_name", board_name);
                back.putExtra("version_name", AppController.versionName);
                back.putExtra("verion_code", AppController.versionCode);
                back.putExtra("School_name", AppController.school_name);
                AppController.Board_name = board_name;
                back.putExtra("isStudentResource",isStudentresource);
                startActivity(back);
            } else {
                Intent back = new Intent(StudentResourcesActivity.this, ProfileActivity.class);
                AppController.OnBackpressed = "false";
                AppController.clt_id = clt_id;
                AppController.msd_ID = msd_id;
                AppController.usl_id = usl_id;
                back.putExtra("clt_id", AppController.clt_id);
                back.putExtra("msd_ID", AppController.msd_ID);
                back.putExtra("usl_id", AppController.usl_id);
                back.putExtra("board_name", board_name);
                back.putExtra("version_name", AppController.versionName);
                back.putExtra("verion_code", AppController.versionCode);
                back.putExtra("School_name", AppController.school_name);
                AppController.Board_name = board_name;
                back.putExtra("isStudentResource",isStudentresource);
                startActivity(back);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            crf_id = AppController.TopicsListsize.get(position).crf_id;
            callResourceListwebservcie(crf_id);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
