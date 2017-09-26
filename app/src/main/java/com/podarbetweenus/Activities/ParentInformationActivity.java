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
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.InputType;
import android.text.method.KeyListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 10/1/2015.
 */
public class ParentInformationActivity extends Activity implements View.OnClickListener {

    //LayoutEntities
    HeaderControler header;
    //Button
    Button btn_next,btn_edit_info,btn_save_info,btn_cancel;
    //Linear Layout
    LinearLayout lay_back_investment,ll_save_cancel_info,ll_edit_next_info;
    //EditText
    EditText ed_select_country,ed_username,ed_mobile_number,ed_bldg_name,ed_area,ed_location,ed_pincode,ed_select_state,ed_select_city,ed_telephone_no;
    //ProgressDialog
    ProgressDialog progressDialog;
    Context mcontext;
    //TextView
    TextView tv_no_record,tv_version_name,tv_version_code,tv_cancel,tv_academic_year_drawer,tv_child_name_drawer,tv_std_value_drawer,tv_roll_no_value_drawer;
    //Drawaer Layout
    DrawerLayout drawer;
    //ListView
    ListView drawerListView;
    //Relative Layout
    RelativeLayout leftfgf_drawer,rl_profile;
    //ImageView
    ImageView img_drawer,img_icon;
    ContentDisplaceDrawerToggle mContentDisplaceToggle;
    Context context;

    //Entities
    LoginDetails login_Details;
    DataFetchService dft;
    KeyListener variable;
    String board_name,clt_id,msd_ID,usl_id,school_name,versionName,cnt_id,state_id,city_id,country_id,registered_emailId,mobile_no,announcementCount,behaviourCount,
            building_address,streetName,location_area,pincode,state,city,country,tel_no,edittextSelector="false",firstCityId,firstCity,isStudentresource
            ,firstTimeCitySelection="true",firstStateId,std_name,cityListClicked="",regEmailIdChangeMessage="Student Information Updated Sucessfully.\n" +
            "This Registered Email Id is now your BetweenUs Login Id.",sch_id,cls_id,acy_id,stud_id;
    String methodName="GetParentInfoDropDown";
    String parentInfoMethod_name = "GetParentStudentInfo";
    String stateMethod_name = "GetParentInfoStateList";
    String cityMethod_name = "GetParentInfoCityList";
    String updateInfoMethod_name = "UpdateParentStudentInfo";
    String TAG = "ParentInformation";
    ArrayList<String> strings_country ;
    String[] select_state;
    String[] select_country;
    String[] select_city;
    ArrayList<String> strings_state = new ArrayList<String>();
    ArrayList<String> strings_city = new ArrayList<String>();
    int versionCode,notificationID=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.parentinformation);
        findViews();
        init();
        getIntentId();
        setDrawer();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getEdittextvariable();
        //setEditText Disable
        disableEditText();
        AppController.StateSelection = "false";
        ed_select_state.setInputType(InputType.TYPE_NULL);
        ed_select_city.setInputType(InputType.TYPE_NULL);
        ed_select_country.setInputType(InputType.TYPE_NULL);
        try {
            //Call Webservice for Parent Info
            calWebserviceforParentInfo(AppController.msd_ID);
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
                    drawerListView.setAdapter(new CustomAccount(ParentInformationActivity.this, android.R.layout.simple_list_item_1, icons_with_sibling, data_with_sibling));


                    drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if (position == 0) {
                                Intent profile = new Intent(ParentInformationActivity.this, Profile_Sibling.class);

                                AppController.msd_ID = msd_ID;
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
                                profile.putExtra("version_name", AppController.versionName);
                                profile.putExtra("behaviour_count", behaviourCount);
                                profile.putExtra("version_code", AppController.versionCode);
                                profile.putExtra("isStudentResource", isStudentresource);
                                startActivity(profile);
                            } else if (position == 1) {
                                if (dft.isInternetOn() == true) {
                                    Intent dashboard = new Intent(ParentInformationActivity.this, DashboardActivity.class);
                                    AppController.Board_name = board_name;
                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_ID;
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
                                    dashboard.putExtra("isStudentResource", isStudentresource);
                                    dashboard.putExtra("behaviour_count", behaviourCount);
                                    startActivity(dashboard);
                                }
                            } else if (position == 2) {
                                Intent sibling = new Intent(ParentInformationActivity.this, SiblingActivity.class);
                                AppController.clt_id = clt_id;
                                AppController.usl_id = usl_id;
                                AppController.msd_ID = msd_ID;
                                sibling.putExtra("clt_id", AppController.clt_id);
                                sibling.putExtra("usl_id", AppController.usl_id);
                                sibling.putExtra("School_name", AppController.school_name);
                                sibling.putExtra("msd_ID", AppController.msd_ID);
                                sibling.putExtra("board_name", board_name);
                                sibling.putExtra("Std Name", std_name);
                                sibling.putExtra("annpuncement_count", AppController.announcementCount);
                                sibling.putExtra("behaviour_count", behaviourCount);
                                sibling.putExtra("version_name", AppController.versionName);
                                sibling.putExtra("version_code", AppController.versionCode);
                                sibling.putExtra("isStudentResource", isStudentresource);
                                startActivity(sibling);
                            } else if (position == 3) {
                                if (dft.isInternetOn() == true) {
                                    Intent feesInfo = new Intent(ParentInformationActivity.this, FeesInfoActivity.class);

                                    feesInfo.putExtra("board_name", board_name);
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
                                    Intent student_resource = new Intent(ParentInformationActivity.this, StudentResourcesActivity.class);

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
                                    Intent attendance = new Intent(ParentInformationActivity.this, AttendanceActivity.class);

                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_ID;
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
                                    Intent parentinfo = new Intent(ParentInformationActivity.this, ParentInformationActivity.class);

                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_ID;
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
                                Intent setting = new Intent(ParentInformationActivity.this, SettingActivity.class);
                                AppController.usl_id = usl_id;
                                setting.putExtra("usl_id", usl_id);
                                setting.putExtra("version_name", AppController.versionName);
                                setting.putExtra("Std Name", std_name);
                                setting.putExtra("version_code", AppController.versionCode);
                                startActivity(setting);
                            } else if (position == 8) {
                                Intent signout = new Intent(ParentInformationActivity.this, LoginActivity.class);
                                Constant.SetLoginData("", "", ParentInformationActivity.this);
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancel(notificationID);
                                AppController.drawerSignOut = "true";
                                LoginActivity.loggedIn = "false";
                                startActivity(signout);
                            } else if (position == 9) {
                                final Dialog alertDialog = new Dialog(ParentInformationActivity.this);
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
                    drawerListView.setAdapter(new CustomAccount(ParentInformationActivity.this, android.R.layout.simple_list_item_1, icons_with_sibling, data_with_sibling));
                    drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if (position == 0) {
                                Intent profile = new Intent(ParentInformationActivity.this, Profile_Sibling.class);
                                AppController.msd_ID = msd_ID;
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
                                profile.putExtra("isStudentResource", isStudentresource);
                                profile.putExtra("behaviour_count", behaviourCount);
                                startActivity(profile);
                            } else if (position == 1) {
                                if (dft.isInternetOn() == true) {
                                    Intent dashboard = new Intent(ParentInformationActivity.this, DashboardActivity.class);
                                    AppController.Board_name = board_name;
                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_ID;
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
                                    dashboard.putExtra("isStudentResource", isStudentresource);
                                    dashboard.putExtra("behaviour_count", behaviourCount);
                                    startActivity(dashboard);
                                }
                            } else if (position == 2) {
                                Intent sibling = new Intent(ParentInformationActivity.this, SiblingActivity.class);
                                AppController.clt_id = clt_id;
                                AppController.usl_id = usl_id;
                                AppController.msd_ID = msd_ID;
                                sibling.putExtra("clt_id", AppController.clt_id);
                                sibling.putExtra("usl_id", AppController.usl_id);
                                sibling.putExtra("School_name", AppController.school_name);
                                sibling.putExtra("msd_ID", AppController.msd_ID);
                                sibling.putExtra("board_name", board_name);
                                sibling.putExtra("Std Name", std_name);
                                sibling.putExtra("annpuncement_count", AppController.announcementCount);
                                sibling.putExtra("behaviour_count", behaviourCount);
                                sibling.putExtra("version_name", AppController.versionName);
                                sibling.putExtra("version_code", AppController.versionCode);
                                sibling.putExtra("isStudentResource", isStudentresource);
                                startActivity(sibling);
                            } else if (position == 3) {
                                if (dft.isInternetOn() == true) {
                                    Intent feesInfo = new Intent(ParentInformationActivity.this, FeesInfoActivity.class);

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
                                    Intent attendance = new Intent(ParentInformationActivity.this, AttendanceActivity.class);

                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_ID;
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
                                    Intent parentinfo = new Intent(ParentInformationActivity.this, ParentInformationActivity.class);

                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_ID;
                                    AppController.usl_id = usl_id;
                                    parentinfo.putExtra("clt_id", AppController.clt_id);
                                    parentinfo.putExtra("msd_ID", AppController.msd_ID);
                                    parentinfo.putExtra("usl_id", AppController.usl_id);
                                    parentinfo.putExtra("board_name", board_name);
                                    parentinfo.putExtra("Std Name", std_name);
                                    parentinfo.putExtra("School_name", AppController.school_name);
                                    parentinfo.putExtra("version_name", AppController.versionName);
                                    parentinfo.putExtra("version_code", AppController.versionCode);
                                    parentinfo.putExtra("isStudentResource", isStudentresource);
                                    parentinfo.putExtra("annpuncement_count", AppController.announcementCount);
                                    parentinfo.putExtra("behaviour_count", behaviourCount);
                                    startActivity(parentinfo);
                                }
                            } else if (position == 6) {
                                Intent setting = new Intent(ParentInformationActivity.this, SettingActivity.class);
                                AppController.usl_id = usl_id;
                                setting.putExtra("usl_id", usl_id);
                                setting.putExtra("Std Name", std_name);
                                setting.putExtra("version_name", AppController.versionName);
                                setting.putExtra("version_code", AppController.versionCode);
                                setting.putExtra("annpuncement_count", AppController.announcementCount);
                                setting.putExtra("behaviour_count", behaviourCount);
                                setting.putExtra("isStudentResource", isStudentresource);
                                startActivity(setting);
                            } else if (position == 7) {
                                Intent signout = new Intent(ParentInformationActivity.this, LoginActivity.class);
                                Constant.SetLoginData("", "", ParentInformationActivity.this);
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancel(notificationID);
                                AppController.drawerSignOut = "true";
                                LoginActivity.loggedIn = "false";
                                startActivity(signout);
                            } else if (position == 8) {
                                final Dialog alertDialog = new Dialog(ParentInformationActivity.this);
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
                    drawerListView.setAdapter(new CustomAccount(ParentInformationActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));
                    drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if (position == 0) {
                                Intent profile = new Intent(ParentInformationActivity.this, ProfileActivity.class);

                                AppController.msd_ID = msd_ID;
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
                                    Intent dashboard = new Intent(ParentInformationActivity.this, DashboardActivity.class);
                                    AppController.Board_name = board_name;
                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_ID;
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
                                    Intent feesInfo = new Intent(ParentInformationActivity.this, FeesInfoActivity.class);

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
                                    Intent student_resource = new Intent(ParentInformationActivity.this, StudentResourcesActivity.class);

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
                                    startActivity(student_resource);
                                }
                            } else if (position == 4) {
                                if (dft.isInternetOn() == true) {
                                    Intent attendance = new Intent(ParentInformationActivity.this, AttendanceActivity.class);

                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_ID;
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
                                    Intent parentinfo = new Intent(ParentInformationActivity.this, ParentInformationActivity.class);
                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_ID;
                                    AppController.usl_id = usl_id;
                                    AppController.Board_name = board_name;
                                    parentinfo.putExtra("clt_id", AppController.clt_id);
                                    parentinfo.putExtra("msd_ID", AppController.msd_ID);
                                    parentinfo.putExtra("usl_id", AppController.usl_id);
                                    parentinfo.putExtra("board_name", AppController.Board_name);
                                    parentinfo.putExtra("Std Name", std_name);
                                    parentinfo.putExtra("School_name", AppController.school_name);
                                    parentinfo.putExtra("version_name", AppController.versionName);
                                    parentinfo.putExtra("annpuncement_count", AppController.announcementCount);
                                    parentinfo.putExtra("behaviour_count", behaviourCount);
                                    parentinfo.putExtra("isStudentResource", isStudentresource);
                                    startActivity(parentinfo);
                                }
                            } else if (position == 6) {
                                Intent setting = new Intent(ParentInformationActivity.this, SettingActivity.class);
                                AppController.usl_id = usl_id;
                                setting.putExtra("usl_id", AppController.usl_id);
                                setting.putExtra("Std Name", std_name);
                                setting.putExtra("version_name", AppController.versionName);
                                setting.putExtra("annpuncement_count", AppController.announcementCount);
                                setting.putExtra("behaviour_count", behaviourCount);
                                setting.putExtra("isStudentResource", isStudentresource);
                                startActivity(setting);
                            } else if (position == 7) {
                                Intent signout = new Intent(ParentInformationActivity.this, LoginActivity.class);
                                Constant.SetLoginData("", "", ParentInformationActivity.this);
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancel(notificationID);
                                AppController.drawerSignOut = "true";
                                LoginActivity.loggedIn = "false";
                                startActivity(signout);
                            } else if (position == 8) {
                                final Dialog alertDialog = new Dialog(ParentInformationActivity.this);
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
                    int[] icons_without_sibling = {R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.payfeesonline_48x48, R.drawable.att_48x48, R.drawable.parentinformation_48x48,/* R.drawable.studentresources_48x48,*/ R.drawable.setting1_hdpi,
                            R.drawable.logout1_hdpi, R.drawable.info};
                    final String[] data_without_sibling = {"Dashboard", "Messages", "Fees Information", "Attendance", "Student Information",/* "Class Room Videos",*/ "Setting",
                            "Sign Out", "About"};
                    drawerListView.setAdapter(new CustomAccount(ParentInformationActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));
                    drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if (position == 0) {
                                Intent profile = new Intent(ParentInformationActivity.this, ProfileActivity.class);
                                AppController.msd_ID = msd_ID;
                                AppController.clt_id = clt_id;
                                AppController.school_name = school_name;
                                profile.putExtra("msd_ID", AppController.msd_ID);
                                profile.putExtra("clt_id", AppController.clt_id);
                                profile.putExtra("School_name", AppController.school_name);
                                profile.putExtra("usl_id", AppController.usl_id);
                                profile.putExtra("board_name", AppController.Board_name);
                                profile.putExtra("Std Name", std_name);
                                profile.putExtra("version_name", AppController.versionName);
                                profile.putExtra("annpuncement_count", AppController.announcementCount);
                                profile.putExtra("behaviour_count", behaviourCount);
                                profile.putExtra("isStudentResource", isStudentresource);
                                startActivity(profile);
                            } else if (position == 1) {
                                if (dft.isInternetOn() == true) {
                                    Intent dashboard = new Intent(ParentInformationActivity.this, DashboardActivity.class);
                                    AppController.Board_name = board_name;
                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_ID;
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
                                    Intent feesInfo = new Intent(ParentInformationActivity.this, FeesInfoActivity.class);
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
                                    Intent attendance = new Intent(ParentInformationActivity.this, AttendanceActivity.class);

                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_ID;
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
                                    Intent parentinfo = new Intent(ParentInformationActivity.this, ParentInformationActivity.class);
                                    AppController.clt_id = clt_id;
                                    AppController.msd_ID = msd_ID;
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
                                Intent setting = new Intent(ParentInformationActivity.this, SettingActivity.class);
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
                                    Intent signout = new Intent(ParentInformationActivity.this, LoginActivity.class);
                                    Constant.SetLoginData("", "", ParentInformationActivity.this);
                                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.cancel(notificationID);
                                    AppController.drawerSignOut = "true";
                                    LoginActivity.loggedIn = "false";
                                    startActivity(signout);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (position == 7) {
                                final Dialog alertDialog = new Dialog(ParentInformationActivity.this);
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
            Constant.setDrawerData(AppController.drawerAcademic_year, AppController.drawerChild_name, AppController.drawerStd, AppController.drawerRoll_no, ParentInformationActivity.this);
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
                AppController.msd_ID = msd_ID;
                AppController.school_name = school_name;
                AppController.versionName = versionName;
                intent.putExtra("usl_id", AppController.usl_id);
                intent.putExtra("msd_ID", AppController.msd_ID);
                intent.putExtra("version_name", AppController.versionName);
                intent.putExtra("clt_id", AppController.clt_id);
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
    private void getEdittextvariable() {
        variable =  ed_username.getKeyListener();
        variable=  ed_select_country.getKeyListener();
        variable= ed_mobile_number.getKeyListener();
        variable= ed_bldg_name.getKeyListener();
        variable= ed_area.getKeyListener();
        variable= ed_location.getKeyListener();
        variable= ed_pincode.getKeyListener();
        variable= ed_select_state.getKeyListener();
        variable= ed_select_city.getKeyListener();
        variable= ed_telephone_no.getKeyListener();

    }

    private void disableEditText() {

        ed_username.setKeyListener(null);
        ed_select_country.setKeyListener(null);
        ed_mobile_number.setKeyListener(null);
        ed_bldg_name.setKeyListener(null);
        ed_area.setKeyListener(null);
        ed_location.setKeyListener(null);
        ed_pincode.setKeyListener(null);
        ed_select_state.setKeyListener(null);
        ed_select_city.setKeyListener(null);
        ed_telephone_no.setKeyListener(null);

        ed_username.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_mobile_number.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_bldg_name.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_area.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_location.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_pincode.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_telephone_no.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_select_city.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_select_state.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_select_country.setBackgroundResource(R.drawable.dropdown_edittext);
    }

    private void getIntentId() {
        Intent intent = getIntent();
        board_name = intent.getStringExtra("board_name");
        clt_id = intent.getStringExtra("clt_id");
        msd_ID = intent.getStringExtra("msd_ID");
        usl_id = intent.getStringExtra("usl_id");
        school_name = intent.getStringExtra("School_name");
        versionName = intent.getStringExtra("version_name");
        versionCode = intent.getIntExtra("version_code", 0);
        std_name = intent.getStringExtra("Std Name");
        announcementCount = intent.getStringExtra("annpuncement_count");
        behaviourCount = intent.getStringExtra("behaviour_count");
        isStudentresource = intent.getStringExtra("isStudentResource");
        AppController.versionName = versionName;
        AppController.dropdownSelected = intent.getStringExtra("DropDownSelected");
        AppController.school_name = school_name;
        AppController.Board_name = board_name;
        AppController.clt_id = clt_id;
        AppController.msd_ID = msd_ID;
        cls_id = ProfileActivity.get_cls_id(ParentInformationActivity.this);
        acy_id = ProfileActivity.get_acy_id(ParentInformationActivity.this);
        stud_id = ProfileActivity.get_stud_id(ParentInformationActivity.this);
        sch_id = ProfileActivity.get_sch_id(ParentInformationActivity.this);
    }

    private void findViews() {
        //Button
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_edit_info = (Button) findViewById(R.id.btn_edit_info);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_save_info = (Button) findViewById(R.id.btn_save_info);
        //Linear Layout
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
        ll_save_cancel_info = (LinearLayout) findViewById(R.id.ll_save_cancel_info);
        ll_edit_next_info = (LinearLayout) findViewById(R.id.ll_edit_next_info);

        //EditText
        ed_select_country = (EditText) findViewById(R.id.ed_select_country);
        ed_telephone_no = (EditText) findViewById(R.id.ed_telephone_no);
        ed_username = (EditText) findViewById(R.id.ed_username);
        ed_mobile_number = (EditText) findViewById(R.id.ed_mobile_number);
        ed_bldg_name = (EditText) findViewById(R.id.ed_bldg_name);
        ed_area = (EditText) findViewById(R.id.ed_area);
        ed_location = (EditText) findViewById(R.id.ed_location);
        ed_pincode = (EditText) findViewById(R.id.ed_pincode);
        ed_select_state = (EditText) findViewById(R.id.ed_select_state);
        ed_select_city = (EditText) findViewById(R.id.ed_select_city);
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
     //   int width = dm.widthPixels;
        int width = getResources().getDisplayMetrics().widthPixels/2 + 30;
        DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) leftfgf_drawer.getLayoutParams();
        lp.width = width + 30;
        leftfgf_drawer.setLayoutParams(lp);
    }

    private void init() {
        //ImageView
        img_drawer.setOnClickListener(this);
        header = new HeaderControler(this,true,false,"Student Information");
        //Button
        btn_next.setOnClickListener(this);
        btn_edit_info.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_save_info.setOnClickListener(this);
        //Prpgress Dialog
        progressDialog = Constant.getProgressDialog(this);
        //Linear Layout
        lay_back_investment.setOnClickListener(this);
        login_Details = new LoginDetails();
        dft = new DataFetchService(this);
        //Edit Text
        ed_select_country.setOnClickListener(this);
        ed_select_state.setOnClickListener(this);
        ed_select_city.setOnClickListener(this);
    }

    private void calWebserviceforParentInfo(String msd_id)
    {
        if(dft.isInternetOn()==true) {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }
    else{
        progressDialog.dismiss();
    }
        dft.getparentInfo(msd_id, parentInfoMethod_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_Details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_Details.Status.equalsIgnoreCase("1")) {
                                setUIData();

                            } else if (login_Details.Status.equalsIgnoreCase("0")) {

                            }
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Show error or whatever...
                        Log.d("ParentInfo", "ERROR.._---" + error.getCause());
                    }
                });
    }

    private void setUIData() {

        ed_username.setText(login_Details.ParentInfoStudent.get(0).eml_mailID);
        ed_mobile_number.setText(login_Details.ParentInfoStudent.get(0).con_MNo);
        ed_bldg_name.setText(login_Details.ParentInfoStudent.get(0).adr_Building);
        ed_area.setText(login_Details.ParentInfoStudent.get(0).adr_Street);
        ed_location.setText(login_Details.ParentInfoStudent.get(0).adr_Area);
        ed_pincode.setText(login_Details.ParentInfoStudent.get(0).adr_pincode);
        String std = login_Details.ParentInfoStudent.get(0).con_std.trim();
        String telephone_no = login_Details.ParentInfoStudent.get(0).con_No;
        ed_telephone_no.setText(telephone_no);
        ed_select_city.setText(login_Details.ParentInfoStudent.get(0).cit_Name);
        ed_select_state.setText(login_Details.ParentInfoStudent.get(0).Ste_Name);
        ed_select_country.setText(login_Details.ParentInfoStudent.get(0).cnt_Name);
        AppController.state = login_Details.ParentInfoStudent.get(0).ste_id;
        AppController.city = login_Details.ParentInfoStudent.get(0).cit_id;
    }

    public void calWebserviceforCountry(Context mcontext) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getCountryList(methodName, Request.Method.GET,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        login_Details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                        strings_country = new ArrayList<String>();
                        try {
                            for (int i = 0; i < login_Details.ParentInfoCountyList.size(); i++) {
                                strings_country.add(login_Details.ParentInfoCountyList.get(i).cnt_Name);
                                select_country = new String[strings_country.size()];
                                select_country = strings_country.toArray(select_country);
                            }
                            selectCountry(select_country);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "" + volleyError);
                    }
                }, null);
    }

    private void selectCountry( final String[] select_date) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.dialog_list, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Select Country");
        final ListView select_list = (ListView) convertView.findViewById(R.id.select_list);

        alertDialog.setItems(select_date, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                ed_select_country.setText(select_country[item]);
                dialog.dismiss();
                String selectedCountry = select_country[item];
                Log.e("Country", selectedCountry);
                country_id = getIDfromCountry(selectedCountry);
                Log.e("CountryId", country_id);
            }
        });
            alertDialog.show();
    }

    private String getIDfromCountry(String selectedCountry) {
            String id = "0";
            for (int i = 0; i < login_Details.ParentInfoCountyList.size(); i++) {
                try {
                    if (login_Details.ParentInfoCountyList.get(i).cnt_Name.equalsIgnoreCase(selectedCountry)) {
                        id = login_Details.ParentInfoCountyList.get(i).cnt_ID;
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            return id;
    }

    @Override
    public void onClick(View v) {
        if(v==img_drawer)
        {
            drawer.openDrawer(Gravity.RIGHT);
        }
        else if(v==btn_next)
        {
        }
        else if(v==btn_edit_info){
            try {
                ll_save_cancel_info.setVisibility(View.VISIBLE);
                ll_edit_next_info.setVisibility(View.GONE);
                AppController.editButtonClicked = "true";
                //Enable Editiong
                enableEditText();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(v==btn_save_info){
            try {
                saveInfo();
                //setEditText Disable
                disableEditText();
                AppController.editButtonClicked = "false";
                if (!isValidEmail(registered_emailId) && ed_mobile_number.getText().length() != 10) {
                    ed_username.setError("Invalid Email");
                    ed_mobile_number.setError("Invalid Mobile Number");
                } else if (!isValidEmail(registered_emailId)) {
                    ed_username.setError("Invalid Email");
                } else if (ed_mobile_number.getText().length() != 10) {
                    ed_mobile_number.setError("Invalid Mobile Number");
                } else if (!isValidBuidlsingAddress(building_address)) {
                    ed_bldg_name.setError("Invalid Building Address");
                } else if (!isValidBuidlsingAddress(streetName)) {
                    ed_area.setError("Invaild Street Name");

                } else if (!isValidBuidlsingAddress(location_area)) {
                    ed_location.setError("Invalid Area/Taluka");
                } else if (!isValidPincode(pincode)) {
                    ed_pincode.setError("Invalid PinCode");
                } else {
                    ed_username.setError(null);
                    ed_mobile_number.setError(null);
                    try {
                        callWebserviceSaveParentInfo(AppController.msd_ID, registered_emailId, mobile_no, building_address, streetName, location_area, pincode, AppController.state, AppController.city, "1", tel_no);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if(v==btn_cancel) {
            try {
                View view = this.getCurrentFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                ed_username.setError(null);
                ed_mobile_number.setError(null);
                ed_pincode.setError(null);
                ed_location.setError(null);
                ed_area.setError(null);
                ed_bldg_name.setError(null);
                AppController.editButtonClicked = "false";
                ll_edit_next_info.setVisibility(View.VISIBLE);
                ll_save_cancel_info.setVisibility(View.GONE);
                disableEditText();
                try {
                    //Call Webservice for Parent Info
                    calWebserviceforParentInfo(AppController.msd_ID);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(v==ed_select_country){
            if(AppController.editButtonClicked.equalsIgnoreCase("true")) {
                //Call Webservice for Country
                edittextSelector="true";
                try {
                    calWebserviceforCountry(mcontext);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        else if(v==ed_select_state) {
            try {
                if (AppController.editButtonClicked.equalsIgnoreCase("true")) {
                    AppController.StateSelection = "true";
                    cityListClicked = "false";

                    for (int i = 0; i < login_Details.ParentInfoStudent.size(); i++) {
                        cnt_id = login_Details.ParentInfoStudent.get(0).cnt_id;
                    }

                    edittextSelector = "true";
                    try {
                        //Call Webservice for State
                        callStateWebservice("1");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        else if(v==ed_select_city){
            try {
                if (AppController.editButtonClicked.equalsIgnoreCase("true")) {
                    AppController.StateSelection = "true";
                    edittextSelector = "true";
                    if (firstTimeCitySelection.equalsIgnoreCase("true")) {
                        try {
                            //Call Webservice for City
                            callCityWebservice(AppController.state);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (firstTimeCitySelection.equalsIgnoreCase("true") && AppController.StateSelection.equalsIgnoreCase("false")) {
                        try {
                            //Call Webservice for City
                            callCityWebservice(state_id);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(v==lay_back_investment) {
            super.onBackPressed();
            try {
                if (AppController.SiblingActivity.equalsIgnoreCase("true")) {
                    Intent back = new Intent(ParentInformationActivity.this, Profile_Sibling.class);
                    AppController.OnBackpressed = "false";
                    AppController.editButtonClicked = "false";
                    AppController.clt_id = clt_id;
                    AppController.msd_ID = msd_ID;
                    AppController.usl_id = usl_id;
                    AppController.school_name = school_name;
                    AppController.Board_name = board_name;
                    back.putExtra("clt_id", AppController.clt_id);
                    back.putExtra("msd_ID", AppController.msd_ID);
                    back.putExtra("usl_id", AppController.usl_id);
                    back.putExtra("board_name", AppController.Board_name);
                    back.putExtra("School_name", AppController.school_name);
                    back.putExtra("version_name", AppController.versionName);
                    back.putExtra("verion_code", AppController.versionCode);
                    back.putExtra("annpuncement_count", announcementCount);
                    back.putExtra("behaviour_count", behaviourCount);
                    back.putExtra("isStudentResource", isStudentresource);
                    startActivity(back);
                } else {
                    Intent back = new Intent(ParentInformationActivity.this, ProfileActivity.class);
                    AppController.OnBackpressed = "false";
                    AppController.editButtonClicked = "false";

                    AppController.clt_id = clt_id;
                    AppController.msd_ID = msd_ID;
                    AppController.usl_id = usl_id;
                    AppController.Board_name = board_name;
                    AppController.school_name = school_name;
                    back.putExtra("clt_id", AppController.clt_id);
                    back.putExtra("msd_ID", AppController.msd_ID);
                    back.putExtra("usl_id", AppController.usl_id);
                    back.putExtra("board_name", AppController.Board_name);
                    back.putExtra("School_name", AppController.school_name);
                    back.putExtra("version_name", AppController.versionName);
                    back.putExtra("verion_code", AppController.versionCode);
                    back.putExtra("annpuncement_count", announcementCount);
                    back.putExtra("behaviour_count", behaviourCount);
                    back.putExtra("isStudentResource", isStudentresource);
                    startActivity(back);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private boolean isValidPincode(String pincode) {
        String BuildingAddressPattern = "[0-9]{1,6}";
        Pattern pattern = Pattern.compile(BuildingAddressPattern);
        Matcher matcher = pattern.matcher(pincode);
        return matcher.matches();
    }

    private boolean isValidBuidlsingAddress(String building_address) {
        String BuildingAddressPattern = "[a-zA-Z0-9.(), _/-]{1,45}";
        Pattern pattern = Pattern.compile(BuildingAddressPattern);
        Matcher matcher = pattern.matcher(building_address);
        return matcher.matches();
    }

    private boolean isValidMobileNo(String mobile_no) {

        String MobilePattern = "[0-9]{10,10}";
        Pattern pattern = Pattern.compile(MobilePattern);
        Matcher matcher = pattern.matcher(mobile_no);
        return matcher.matches();
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void saveInfo() {

        registered_emailId = ed_username.getText().toString();
        mobile_no = ed_mobile_number.getText().toString();
        building_address = ed_bldg_name.getText().toString();
        streetName = ed_area.getText().toString();
        location_area = ed_location.getText().toString();
        pincode = ed_pincode.getText().toString();
        tel_no = ed_telephone_no.getText().toString();
    }

    private void callWebserviceSaveParentInfo(String msd_ID, final String registered_emailId,String mobile_no,String building_address,String streetName,String location_area,String pincode,String state_id
    ,String city_id,String country_id,String tel_no) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.updateInfo(msd_ID, registered_emailId, mobile_no, building_address, streetName, location_area, pincode, state_id, city_id, country_id, tel_no, updateInfoMethod_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_Details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_Details.Status.equalsIgnoreCase("1")) {

                                if (login_Details.StatusMsg.equalsIgnoreCase("1")) {
                                    Constant.showOkPopup(ParentInformationActivity.this, regEmailIdChangeMessage, new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            Intent fatherinfo = new Intent(ParentInformationActivity.this, LoginActivity.class);
                                            startActivity(fatherinfo);
                                        }
                                    });

                                } else {
                                    Constant.showOkPopup(ParentInformationActivity.this, "Student Information Updated Successfully", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            Intent fatherinfo = new Intent(ParentInformationActivity.this, ParentInformationActivity.class);
                                            fatherinfo.putExtra("clt_id", AppController.clt_id);
                                            fatherinfo.putExtra("msd_ID", AppController.msd_ID);
                                            fatherinfo.putExtra("usl_id", AppController.usl_id);
                                            fatherinfo.putExtra("School_name", AppController.school_name);
                                            fatherinfo.putExtra("board_name", AppController.Board_name);
                                            fatherinfo.putExtra("version_name", AppController.versionName);
                                            fatherinfo.putExtra("verion_code", AppController.versionCode);
                                            fatherinfo.putExtra("annpuncement_count", announcementCount);
                                            fatherinfo.putExtra("behaviour_count", behaviourCount);
                                            fatherinfo.putExtra("isStudentResource", isStudentresource);
                                            startActivity(fatherinfo);
                                        }

                                    });
                                }

                            } else if (login_Details.Status.equalsIgnoreCase("0")) {

                            }
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Show error or whatever...
                        Log.d("ParentInfo", "ERROR.._---" + error.getCause());
                    }
                });

    }
    private void callStateWebservice(String cnt_id) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getStateList(cnt_id, stateMethod_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_Details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            strings_state = new ArrayList<String>();
                            if (login_Details.Status.equalsIgnoreCase("1")) {

                                try {
                                    for (int i = 0; i < login_Details.ParentInfoState.size(); i++) {
                                        strings_state.add(login_Details.ParentInfoState.get(i).Ste_Name);

                                        select_state = new String[strings_state.size()];
                                        select_state = strings_state.toArray(select_state);

                                    }
                                    selectDropdown(select_state);
                                    AppController.StateSelection = "false";
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (login_Details.Status.equalsIgnoreCase("0")) {

                            }
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Show error or whatever...
                        Log.d("ParentInfo", "ERROR.._---" + error.getCause());
                    }
                });

    }

    private void callCityWebservice(String state_id) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getCityList(state_id, cityMethod_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_Details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            strings_city = new ArrayList<String>();
                            if (login_Details.Status.equalsIgnoreCase("1")) {

                                try {
                                    for (int i = 0; i < login_Details.ParentInfocity.size(); i++) {
                                        strings_city.add(login_Details.ParentInfocity.get(i).cit_Name);

                                        select_city = new String[strings_city.size()];
                                        select_city = strings_city.toArray(select_city);
                                        firstCity = login_Details.ParentInfocity.get(0).cit_Name;
                                        firstCityId = login_Details.ParentInfocity.get(0).cit_ID;
                                        AppController.city = firstCityId;
                                            if(cityListClicked.equalsIgnoreCase("false")){
                                                ed_select_city.setText(firstCity);
                                            }
                                    }
                                    selectCityDropdown(select_city);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else if (login_Details.Status.equalsIgnoreCase("0")) {

                            }
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Show error or whatever...
                        Log.d("ParentInfo", "ERROR.._---" + error.getCause());
                    }
                });

    }

    private void selectCityDropdown(final String[] select_city) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.dialog_list, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Select City");
        cityListClicked = "true";
        final ListView select_list = (ListView) convertView.findViewById(R.id.select_list);

        alertDialog.setItems(select_city, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                ed_select_city.setText(select_city[item]);
                dialog.dismiss();
                String selectedCity = select_city[item];
                Log.e("City", selectedCity);
                city_id = getIDfromCity(selectedCity);
                AppController.city = city_id;
                Log.e("CityId", city_id);
            }
        });
        if(AppController.StateSelection.equalsIgnoreCase("true")) {
            alertDialog.show();
        }
    }

    private String getIDfromCity(String selectedCity) {
        String id = "0";
        try {
            for (int i = 0; i < login_Details.ParentInfocity.size(); i++) {
                if (login_Details.ParentInfocity.get(i).cit_Name.equalsIgnoreCase(selectedCity)) {
                    id = login_Details.ParentInfocity.get(i).cit_ID;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return id;
    }

    private void selectDropdown(final String[] select_state) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.dialog_list, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Select State");
        final ListView select_list = (ListView) convertView.findViewById(R.id.select_list);

        alertDialog.setItems(select_state, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                try {
                    // Do something with the selection
                    ed_select_state.setText(select_state[item]);
                    dialog.dismiss();
                    String selectedState = select_state[item];
                    Log.e("State", selectedState);
                    state_id = getIDfromState(selectedState);
                    AppController.state = state_id;
                    callCityWebservice(AppController.state);
                    Log.e("State_ID", state_id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        if(AppController.StateSelection.equalsIgnoreCase("true")) {
            alertDialog.show();
        }
    }
    private String getIDfromState(String selecteddate) {

        String id = "0";
        try {
            for (int i = 0; i < login_Details.ParentInfoState.size(); i++) {
                if (login_Details.ParentInfoState.get(i).Ste_Name.equalsIgnoreCase(selecteddate)) {
                    id = login_Details.ParentInfoState.get(i).Ste_ID;
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return id;
    }

    private void enableEditText() {

        ed_username.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_mobile_number.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_bldg_name.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_area.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_location.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_pincode.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_telephone_no.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_select_city.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_select_state.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_select_country.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_username.setKeyListener(variable);
        ed_username.setInputType(InputType.TYPE_CLASS_TEXT);
        ed_mobile_number.setKeyListener(variable);
        ed_mobile_number.setInputType(InputType.TYPE_CLASS_PHONE);
        ed_bldg_name.setKeyListener(variable);
        ed_bldg_name.setInputType(InputType.TYPE_CLASS_TEXT);
        ed_area.setKeyListener(variable);
        ed_area.setInputType(InputType.TYPE_CLASS_TEXT);
        ed_location.setKeyListener(variable);
        ed_location.setInputType(InputType.TYPE_CLASS_TEXT);
        ed_pincode.setKeyListener(variable);
        ed_telephone_no.setKeyListener(variable);
        ed_telephone_no.setInputType(InputType.TYPE_CLASS_PHONE);
    }

    @Override
       public void onBackPressed() {
        // TODO Auto-generated method stub
        //  super.onBackPressed();

        try {
            if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                drawer.closeDrawer(Gravity.RIGHT);
            } else {
                if (AppController.SiblingActivity.equalsIgnoreCase("true")) {
                    Intent back = new Intent(ParentInformationActivity.this, Profile_Sibling.class);
                    AppController.OnBackpressed = "false";
                    AppController.editButtonClicked = "false";
                    AppController.clt_id = clt_id;
                    AppController.msd_ID = msd_ID;
                    AppController.usl_id = usl_id;
                    AppController.school_name = school_name;
                    AppController.Board_name = board_name;
                    back.putExtra("clt_id", AppController.clt_id);
                    back.putExtra("msd_ID", AppController.msd_ID);
                    back.putExtra("usl_id", AppController.usl_id);
                    back.putExtra("board_name", AppController.Board_name);
                    back.putExtra("School_name", AppController.school_name);
                    back.putExtra("version_name", AppController.versionName);
                    back.putExtra("verion_code", AppController.versionCode);
                    back.putExtra("annpuncement_count", announcementCount);
                    back.putExtra("isStudentResource", isStudentresource);
                    back.putExtra("behaviour_count", behaviourCount);
                    startActivity(back);
                } else {
                    Intent back = new Intent(ParentInformationActivity.this, ProfileActivity.class);
                    AppController.OnBackpressed = "false";
                    AppController.editButtonClicked = "false";

                    AppController.clt_id = clt_id;
                    AppController.msd_ID = msd_ID;
                    AppController.usl_id = usl_id;
                    AppController.Board_name = board_name;
                    AppController.school_name = school_name;
                    back.putExtra("clt_id", AppController.clt_id);
                    back.putExtra("msd_ID", AppController.msd_ID);
                    back.putExtra("usl_id", AppController.usl_id);
                    back.putExtra("board_name", AppController.Board_name);
                    back.putExtra("School_name", AppController.school_name);
                    back.putExtra("version_name", AppController.versionName);
                    back.putExtra("verion_code", AppController.versionCode);
                    back.putExtra("annpuncement_count", announcementCount);
                    back.putExtra("behaviour_count", behaviourCount);
                    back.putExtra("isStudentResource", isStudentresource);
                    startActivity(back);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
