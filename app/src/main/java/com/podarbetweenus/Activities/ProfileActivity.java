package com.podarbetweenus.Activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.BuildConfig;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Services.Webservice;
import com.podarbetweenus.Utility.AppController;
import com.podarbetweenus.Utility.BadgeView;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileActivity extends Activity implements View.OnClickListener {
    //UI variables
    TextView tv_view_msg, tv_view_msg_value, tv_sent_msg, tv_sent_msg_value, tv_school_name, tv_branch_name, tv_child_name,tv_cancel,
            tv_std_value, tv_version_name,tv_version_code,tv_roll_no_value,tv_academic_year_drawer,tv_child_name_drawer,tv_roll_no_value_drawer,tv_std_value_drawer;

    public static TextView tv_messge_count,tv_count;
    RelativeLayout rl_classroomVideos,rl_dashboard,rl_pay_fees_online,rl_student_resources,rl_attendance,rl_parent_info,leftfgf_drawer, rl_profile;
    LinearLayout lay_back_investment,ll_studentResource,ll_dashboard,ll_payFessOnline,ll_attendance,ll_parentInfo,ll_classroomVideos;
    DrawerLayout drawer;
    ListView drawerListView;
    ImageView img_drawer,img_icon;
    public static ImageView img_dashboard;
    //ProgressDialog
    ProgressDialog progressDialog;
    ContentDisplaceDrawerToggle mContentDisplaceToggle;
    //LayoutEntities
    HeaderControler header;
    DataFetchService dft;
    LoginDetails login_details;
    BadgeView badge;
    int viewmessageListSize,msgStatusSize,msgStatusSizeOnResume,viewmessageListSizeOnResume;
    String badge_count,msgStatus,msgStatusOnResume,isStudentresource;
    String LastViewMessageData = "GetLastViewMessageData";
    ArrayList<String> msgStatusList = new ArrayList<String>();
    ArrayList<String> msgStatusListOnResume = new ArrayList<String>();
    Context mcontext;
    //Drawer

    int[] icons_without_sibling = {R.drawable.dashboard_48x48,R.drawable.messagebox_48x48, R.drawable.payfeesonline_48x48, R.drawable.studentresources_48x48, R.drawable.att_48x48, R.drawable.parentinformation_48x48,/* R.drawable.studentresources_48x48,*/ R.drawable.setting1_hdpi,
            R.drawable.logout1_hdpi,R.drawable.info};
    final String[] data_without_sibling = {"Dashboard","Messages", "Fees Information", "Student Resources", "Attendance", "Parent Information",/* "Class Room Videos",*/ "Setting",
            "Sign Out","About"};
    int listSize,versionCode,notificationID=1;
    String clt_id, msd_ID,usl_id,child_name,month,std,sec_ID,roll_no,org_id,Brd_ID,school_logo,announcementCount,behaviourCount,
            school_name, branch_name,academic_year,board_name,pmuId,std_name,versionName,signout="",installedversionName,playstore_version;
    String StudentDetails_Method_name = "GetStundentDetails";
    String check="0",pageNo ="1",pageSize="150",cls_id,acy_id,sch_id,stud_id;
    String ViewMessageMethodName = "GetViewMessageData";
    public static SharedPreferences resultpreLoginData;
    String url = "https://play.google.com/store/apps/details?id=com.podarbetweenus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.profile_without_sibling);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        getIntentData();
        findViews();
        init();
        //find the home launcher Package
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        String currentHomePackage = resolveInfo.activityInfo.packageName;

        String appPackageName = getPackageName();
        versionCode = BuildConfig.VERSION_CODE;
        versionName = BuildConfig.VERSION_NAME;
        AppController.versionName = versionName;
        AppController.versionCode = versionCode;
        SplashScreen.killedApp = "false";
        AppController.parentMessageSent = "false";
        AppController.listItemSelected = -1;
        playstore_version = null;
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

                std = ProfileActivity.get_std(this);
                roll_no = ProfileActivity.get_rollNo(this);
                academic_year = ProfileActivity.get_academic_year(this);
                announcementCount = ProfileActivity.get_announcementCount(this);
                behaviourCount = ProfileActivity.get_behaviourCount(this);
                child_name = ProfileActivity.get_childName(this);
                branch_name = ProfileActivity.get_branch_name(this);
                isStudentresource = ProfileActivity.get_isStudentResourcer(this);
                std_name = ProfileActivity.get_std_name(this);
                org_id = ProfileActivity.get_org_id(this);
                Brd_ID = ProfileActivity.get_Brd_ID(this);
                sec_ID = ProfileActivity.get_sec_ID(this);
                school_name = ProfileActivity.get_school_name(this);
                Log.e("childname", child_name);
                setLogo();
                if (isStudentresource.equalsIgnoreCase("0")) {
                    ll_studentResource.setVisibility(View.VISIBLE);
                } else {
                    ll_studentResource.setVisibility(View.INVISIBLE);
                }

                tv_branch_name.setText(branch_name);
                tv_std_value.setText(std);
                tv_roll_no_value.setText(roll_no);
                tv_child_name.setText(child_name);
                tv_school_name.setText(school_name);
                //For Drawer
                setDrawer();
            }
            //Webservice call for studentDetails
            callWebserviceGetStudentDetails(msd_ID, clt_id, usl_id);
            //Webservcie call for View Messages
            callViewMessagesWebservice(AppController.clt_id, AppController.usl_id, AppController.msd_ID, month, check, "" + pageNo, "" + pageSize);
        }
        catch (Exception e){
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
    //Set Icon Count
    private void setIconCount() {
        if(AppController.OnBackpressed.equalsIgnoreCase("true")) {
            badge = new BadgeView(this, img_dashboard);
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
            badge = new BadgeView(this, img_dashboard);
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

    private void getIntentData() {
        try {
            Intent intent = getIntent();
            listSize = intent.getIntExtra("Sibling", 0);
            clt_id = intent.getStringExtra("clt_id");
            msd_ID = intent.getStringExtra("msd_ID");
            school_name = intent.getStringExtra("School_name");
            usl_id = intent.getStringExtra("usl_id");
            pmuId = intent.getStringExtra("PmuId");
            versionName = intent.getStringExtra("version_name");
            versionCode = intent.getIntExtra("version_id", 0);
            board_name = intent.getStringExtra("board_name");
            clt_id = LoginActivity.get_cltId(ProfileActivity.this);
            usl_id = LoginActivity.get_uslId(ProfileActivity.this);
            msd_ID = LoginActivity.get_msdId(ProfileActivity.this);
            AppController.school_name = school_name;
            AppController.clt_id = clt_id;
            AppController.usl_id = usl_id;
            AppController.Board_name = board_name;
            AppController.versionName = versionName;
            AppController.school_name = school_name;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if(AppController.fromSplashScreen.equalsIgnoreCase("true")){

                //AppController.fromSplashScreen = "false";
                AppController.killApp = "false";
                AppController.ProfileWithoutSibling = "true";
                Calendar now = Calendar.getInstance();
                Date date = now.getTime();
                SimpleDateFormat format = new SimpleDateFormat("MM");
                month = format.format(date);
                //Webservice call for studentDetails
                callWebserviceGetStudentDetails(msd_ID, clt_id,usl_id);

                if(AppController.OnBackpressed.equalsIgnoreCase("true")) {
                    //Webservcie call for View Messages
                    callViewMessagesWebservice(AppController.clt_id, AppController.usl_id, AppController.msd_ID, month, check, "" + pageNo, "" + pageSize);

                }
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
                AppController.SiblingListSize = listSize;
                intent.putExtra("usl_id",AppController.usl_id);
                intent.putExtra("msd_ID",AppController.msd_ID);
                intent.putExtra("version_name",AppController.versionName);
                intent.putExtra("clt_id",AppController.clt_id);
                intent.putExtra("School_name",AppController.school_name);
                intent.putExtra("board_name",AppController.Board_name);
                intent.putExtra("Sibling",AppController.SiblingListSize);
                intent.putExtra("isStudentResource",isStudentresource);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {

        img_drawer.setOnClickListener(this);
        ll_dashboard.setOnClickListener(this);
        ll_parentInfo.setOnClickListener(this);
        ll_attendance.setOnClickListener(this);
        ll_studentResource.setOnClickListener(this);
        ll_classroomVideos.setOnClickListener(this);
        ll_payFessOnline.setOnClickListener(this);
        progressDialog = Constant.getProgressDialog(this);
        dft = new DataFetchService(this);
        Constant constant = new Constant();
        login_details = new LoginDetails();
        Calendar now = Calendar.getInstance();
        Date date = now.getTime();
        SimpleDateFormat format = new SimpleDateFormat("MM");
        month = format.format(date);

    }

    private void findViews() {
        //Linear Layout
        ll_classroomVideos = (LinearLayout) findViewById(R.id.ll_classroomVideos);
        ll_dashboard = (LinearLayout) findViewById(R.id.ll_dashboard);
        ll_attendance = (LinearLayout) findViewById(R.id.ll_attendance);
        ll_parentInfo = (LinearLayout) findViewById(R.id.ll_parentInfo);
        ll_studentResource = (LinearLayout) findViewById(R.id.ll_studentResource);
        ll_payFessOnline = (LinearLayout) findViewById(R.id.ll_payFessOnline);

        //TextView
        tv_school_name = (TextView) findViewById(R.id.tv_school_name);
        tv_branch_name = (TextView) findViewById(R.id.tv_branch_name);
        tv_child_name = (TextView) findViewById(R.id.tv_child_name);
        tv_std_value = (TextView) findViewById(R.id.tv_std_value);
        tv_roll_no_value = (TextView) findViewById(R.id.tv_roll_no_value);
        tv_academic_year_drawer = (TextView) findViewById(R.id.tv_academic_year_drawer);
        tv_child_name_drawer = (TextView) findViewById(R.id.tv_child_name_drawer);
        tv_std_value_drawer = (TextView) findViewById(R.id.tv_std_value_drawer);
        tv_roll_no_value_drawer = (TextView) findViewById(R.id.tv_roll_no_value_drawer);
        tv_count = (TextView) findViewById(R.id.tv_count);
        //ImageView
        img_drawer = (ImageView) findViewById(R.id.img_drawer);
        img_icon = (ImageView) findViewById(R.id.img_icon);
        img_dashboard = (ImageView) findViewById(R.id.img_dashboard);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    private void callWebserviceGetStudentDetails(String msd_ID, final String clt_id,String usl_id) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getStudentDetails(msd_ID, clt_id, usl_id, StudentDetails_Method_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_details.Status.equalsIgnoreCase("1")) {

                                setUIData();
                            } else {
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Show error or whatever...
                        Log.d("ProfileActivity", "ERROR.._---" + error.getCause());
                    }
                });

    }
    public static void Set_id(String std, String roll_no, String child_name, String branch_name, String academic_year,String std_name,String isStudentResource,String announcementCount,String behaviourCount,String org_id,String Brd_ID,String sec_ID,String school_name,String cls_id,String sch_id,String acy_id,String stud_id, Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = resultpreLoginData.edit();
        editor.putString("std", std);
        editor.putString("roll_no", roll_no);
        editor.putString("child_name", child_name);
        editor.putString("branch_name", branch_name);
        editor.putString("academic_year", academic_year);
        editor.putString("std_name",std_name);
        editor.putString("isStudentResource",isStudentResource);
        editor.putString("announcementCount",announcementCount);
        editor.putString("behaviourCount",behaviourCount);
        editor.putString("org_id",org_id);
        editor.putString("Brd_ID",Brd_ID);
        editor.putString("sec_ID",sec_ID);
        editor.putString("school_name",school_name);
        editor.putString("cls_id",cls_id);
        editor.putString("sch_id",sch_id);
        editor.putString("acy_id",acy_id);
        editor.putString("stud_id",stud_id);
        editor.commit();

    }
    public static String get_stud_id(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("stud_id", "");
    }

    public static String get_acy_id(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("acy_id", "");
    }
    public static String get_sch_id(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("sch_id", "");
    }
    public static String get_cls_id(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("cls_id", "");
    }



    public static String get_std(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("std", "");
    }

    public static String get_rollNo(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("roll_no", "");
    }

    public static String get_childName(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("child_name", "");
    }

    public static String get_branch_name(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("branch_name", "");
    }

    public static String get_academic_year(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("academic_year", "");
    }
    public static String get_std_name(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("std_name", "");
    }
    public static String get_isStudentResourcer(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("isStudentResource", "");
    }
    public static String get_announcementCount(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("announcementCount", "");
    }
    public static String get_behaviourCount(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("behaviourCount", "");
    }
    public static String get_org_id(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("org_id", "");
    }

    public static String get_Brd_ID(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("Brd_ID", "");
    }

    public static String get_sec_ID(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("sec_ID", "");
    }
    public static String get_school_name(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("school_name", "");
    }


    private void setUIData() {

        for(int i=0;i<login_details.stundentListDetails.size();i++) {
            std = login_details.stundentListDetails.get(i).std_Name + "-" + login_details.stundentListDetails.get(i).div_name;
            AppController.drawerStd = std;
            roll_no = login_details.stundentListDetails.get(i).msd_RollNo;
            AppController.drawerRoll_no = roll_no;
            child_name = login_details.stundentListDetails.get(i).stu_display;
            AppController.drawerChild_name = child_name;
            branch_name = login_details.stundentListDetails.get(i).sch_Area;
            academic_year = login_details.stundentListDetails.get(i).acy_Year;
            AppController.drawerAcademic_year = academic_year;
            std_name = login_details.stundentListDetails.get(i).std_Name;
            isStudentresource = login_details.stundentListDetails.get(i).StudentResourceExist;
            AppController.isStudentResourcePresent = isStudentresource;
            school_logo = login_details.stundentListDetails.get(i).SchoolLogo;
            announcementCount = login_details.stundentListDetails.get(i).annoucementCnt;
            AppController.announcementCount = announcementCount;
            behaviourCount = login_details.stundentListDetails.get(i).behaviourcnt;
            org_id = login_details.stundentListDetails.get(i).org_id;
            Brd_ID = login_details.stundentListDetails.get(i).Brd_ID;

            Log.e("BRd ID PROFILE",Brd_ID);
            sec_ID = login_details.stundentListDetails.get(i).sec_ID;
            Log.e("SecID ID PROFILE",sec_ID);
            sch_id= login_details.stundentListDetails.get(i).sch_id;
            acy_id = login_details.stundentListDetails.get(i).acy_id;
            stud_id = login_details.stundentListDetails.get(i).stu_id;
            cls_id = login_details.stundentListDetails.get(i).cls_ID;

            if (isStudentresource.equalsIgnoreCase("0")) {
                ll_studentResource.setVisibility(View.VISIBLE);
            } else {
                ll_studentResource.setVisibility(View.INVISIBLE);
            }
            try {
                String school[] = school_name.split("-");
                String sch_name = school[0];
                tv_school_name.setText(AppController.school_name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("<< Branch name", branch_name);
            tv_branch_name.setText(branch_name);
            tv_std_value.setText(std);
            tv_roll_no_value.setText(roll_no);
            tv_child_name.setText(child_name);
            ProfileActivity.Set_id(std, roll_no, child_name, branch_name, academic_year, std_name, isStudentresource, announcementCount, behaviourCount, org_id, Brd_ID, sec_ID, school_name,cls_id,sch_id,acy_id,stud_id, ProfileActivity.this);
            setLogo();
            //setDrawerData
            setDrawer();
        }
    }
    private void setDrawer() {
        try {
            if (isStudentresource.equalsIgnoreCase("0")) {
                int[] icons_without_sibling = {R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.payfeesonline_48x48, R.drawable.studentresources_48x48, R.drawable.att_48x48, R.drawable.parentinformation_48x48,/* R.drawable.studentresources_48x48,*/ R.drawable.setting1_hdpi,
                        R.drawable.logout1_hdpi, R.drawable.info};
                final String[] data_without_sibling = {"Dashboard", "Messages", "Fees Information", "Student Resources", "Attendance", "Student Information",/* "Class Room Videos",*/ "Setting",
                        "Sign Out", "About"};
                drawerListView.setAdapter(new CustomAccount(ProfileActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));
                drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if (position == 0) {
                            Intent profile = new Intent(ProfileActivity.this, ProfileActivity.class);
                            for (int i = 0; i < login_details.stundentListDetails.size(); i++) {
                                board_name = login_details.stundentListDetails.get(i).Brd_Name;
                                announcementCount = login_details.stundentListDetails.get(i).annoucementCnt;
                                behaviourCount = login_details.stundentListDetails.get(i).behaviourcnt;
                                AppController.behaviourCount = behaviourCount;
                                AppController.announcementCount = announcementCount;
                                isStudentresource = login_details.stundentListDetails.get(i).StudentResourceExist;
                            }
                            AppController.msd_ID = msd_ID;
                            AppController.clt_id = clt_id;
                            AppController.school_name = school_name;
                            profile.putExtra("msd_ID", AppController.msd_ID);
                            profile.putExtra("clt_id", AppController.clt_id);
                            profile.putExtra("School_name", AppController.school_name);
                            profile.putExtra("usl_id", AppController.usl_id);
                            profile.putExtra("board_name", board_name);
                            profile.putExtra("Std Name", std_name);
                            profile.putExtra("version_name", AppController.versionName);
                            profile.putExtra("annpuncement_count", announcementCount);
                            profile.putExtra("behaviour_count", behaviourCount);
                            profile.putExtra("isStudentResource", isStudentresource);
                            startActivity(profile);
                        } else if (position == 1) {
                            for (int i = 0; i < login_details.stundentListDetails.size(); i++) {

                                board_name = login_details.stundentListDetails.get(i).Brd_Name;
                                announcementCount = login_details.stundentListDetails.get(i).annoucementCnt;
                                behaviourCount = login_details.stundentListDetails.get(i).behaviourcnt;
                                AppController.behaviourCount = behaviourCount;
                                isStudentresource = login_details.stundentListDetails.get(i).StudentResourceExist;
                            }
                            if (dft.isInternetOn() == true) {
                                Intent dashboard = new Intent(ProfileActivity.this, DashboardActivity.class);
                                AppController.clt_id = clt_id;
                                AppController.msd_ID = msd_ID;
                                AppController.usl_id = usl_id;
                                AppController.Board_name = board_name;
                                dashboard.putExtra("clt_id", AppController.clt_id);
                                dashboard.putExtra("msd_ID", AppController.msd_ID);
                                dashboard.putExtra("usl_id", AppController.usl_id);
                                dashboard.putExtra("board_name", board_name);
                                dashboard.putExtra("Std Name", std_name);
                                dashboard.putExtra("School_name", AppController.school_name);
                                dashboard.putExtra("version_name", AppController.versionName);
                                dashboard.putExtra("annpuncement_count", announcementCount);
                                dashboard.putExtra("behaviour_count", behaviourCount);
                                dashboard.putExtra("isStudentResource", isStudentresource);
                                startActivity(dashboard);
                            }
                        } else if (position == 2) {
                            if (dft.isInternetOn() == true) {
                                Intent feesInfo = new Intent(ProfileActivity.this, FeesInfoActivity.class);
                                for (int i = 0; i < login_details.stundentListDetails.size(); i++) {
                                    board_name = login_details.stundentListDetails.get(i).Brd_Name;
                                    announcementCount = login_details.stundentListDetails.get(i).annoucementCnt;
                                    behaviourCount = login_details.stundentListDetails.get(i).behaviourcnt;
                                    AppController.behaviourCount = behaviourCount;
                                    AppController.announcementCount = announcementCount;
                                    isStudentresource = login_details.stundentListDetails.get(i).StudentResourceExist;
                                }
                                feesInfo.putExtra("board_name", board_name);
                                feesInfo.putExtra("clt_id", AppController.clt_id);
                                feesInfo.putExtra("msd_ID", AppController.msd_ID);
                                feesInfo.putExtra("usl_id", AppController.usl_id);
                                feesInfo.putExtra("School_name", AppController.school_name);
                                feesInfo.putExtra("Std Name", std_name);
                                feesInfo.putExtra("version_name", AppController.versionName);
                                feesInfo.putExtra("annpuncement_count", announcementCount);
                                feesInfo.putExtra("behaviour_count", behaviourCount);
                                feesInfo.putExtra("isStudentResource", isStudentresource);
                                startActivity(feesInfo);
                            }
                        } else if (position == 3) {
                            if (dft.isInternetOn() == true) {
                                Intent student_resource = new Intent(ProfileActivity.this, StudentResourcesActivity.class);
                                for (int i = 0; i < login_details.stundentListDetails.size(); i++) {
                                    board_name = login_details.stundentListDetails.get(i).Brd_Name;
                                    announcementCount = login_details.stundentListDetails.get(i).annoucementCnt;
                                    behaviourCount = login_details.stundentListDetails.get(i).behaviourcnt;
                                    AppController.behaviourCount = behaviourCount;
                                    AppController.announcementCount = announcementCount;
                                    isStudentresource = login_details.stundentListDetails.get(i).StudentResourceExist;
                                }
                                student_resource.putExtra("board_name", board_name);
                                student_resource.putExtra("clt_id", AppController.clt_id);
                                student_resource.putExtra("msd_ID", AppController.msd_ID);
                                student_resource.putExtra("usl_id", AppController.usl_id);
                                student_resource.putExtra("School_name", AppController.school_name);
                                student_resource.putExtra("version_name", AppController.versionName);
                                student_resource.putExtra("annpuncement_count", announcementCount);
                                student_resource.putExtra("behaviour_count", behaviourCount);
                                student_resource.putExtra("Std Name", std_name);
                                student_resource.putExtra("isStudentResource", isStudentresource);
                                startActivity(student_resource);
                            }
                        } else if (position == 4) {
                            if (dft.isInternetOn() == true) {
                                Intent attendance = new Intent(ProfileActivity.this, AttendanceActivity.class);
                                for (int i = 0; i < login_details.stundentListDetails.size(); i++) {
                                    board_name = login_details.stundentListDetails.get(i).Brd_Name;
                                    announcementCount = login_details.stundentListDetails.get(i).annoucementCnt;
                                    behaviourCount = login_details.stundentListDetails.get(i).behaviourcnt;
                                    AppController.behaviourCount = behaviourCount;
                                    AppController.announcementCount = announcementCount;
                                    isStudentresource = login_details.stundentListDetails.get(i).StudentResourceExist;
                                }
                                AppController.clt_id = clt_id;
                                AppController.msd_ID = msd_ID;
                                AppController.usl_id = usl_id;
                                attendance.putExtra("clt_id", AppController.clt_id);
                                attendance.putExtra("msd_ID", AppController.msd_ID);
                                attendance.putExtra("usl_id", AppController.usl_id);
                                attendance.putExtra("School_name", AppController.school_name);
                                attendance.putExtra("board_name", board_name);
                                attendance.putExtra("Std Name", std_name);
                                attendance.putExtra("annpuncement_count", announcementCount);
                                attendance.putExtra("behaviour_count", behaviourCount);
                                attendance.putExtra("version_name", AppController.versionName);
                                attendance.putExtra("isStudentResource", isStudentresource);
                                startActivity(attendance);
                            }
                        } else if (position == 5) {
                            for (int i = 0; i < login_details.stundentListDetails.size(); i++) {

                                board_name = login_details.stundentListDetails.get(i).Brd_Name;
                                announcementCount = login_details.stundentListDetails.get(i).annoucementCnt;
                                behaviourCount = login_details.stundentListDetails.get(i).behaviourcnt;
                                AppController.behaviourCount = behaviourCount;
                                AppController.announcementCount = announcementCount;
                                isStudentresource = login_details.stundentListDetails.get(i).StudentResourceExist;
                            }
                            if (dft.isInternetOn() == true) {
                                Intent parentinfo = new Intent(ProfileActivity.this, ParentInformationActivity.class);
                                AppController.clt_id = clt_id;
                                AppController.msd_ID = msd_ID;
                                AppController.usl_id = usl_id;
                                AppController.Board_name = board_name;
                                parentinfo.putExtra("clt_id", AppController.clt_id);
                                parentinfo.putExtra("msd_ID", AppController.msd_ID);
                                parentinfo.putExtra("usl_id", AppController.usl_id);
                                parentinfo.putExtra("board_name", board_name);
                                parentinfo.putExtra("Std Name", std_name);
                                parentinfo.putExtra("School_name", AppController.school_name);
                                parentinfo.putExtra("version_name", AppController.versionName);
                                parentinfo.putExtra("annpuncement_count", announcementCount);
                                parentinfo.putExtra("behaviour_count", behaviourCount);
                                parentinfo.putExtra("isStudentResource", isStudentresource);
                                startActivity(parentinfo);
                            }
                        } else if (position == 6) {
                            if (dft.isInternetOn() == true) {
                                Intent setting = new Intent(ProfileActivity.this, SettingActivity.class);
                                AppController.usl_id = usl_id;
                                setting.putExtra("usl_id", AppController.usl_id);
                                setting.putExtra("version_name", AppController.versionName);
                                setting.putExtra("annpuncement_count", announcementCount);
                                setting.putExtra("behaviour_count", behaviourCount);
                                setting.putExtra("isStudentResource", isStudentresource);
                                startActivity(setting);
                            }
                        } else if (position == 7) {
                            Intent signout = new Intent(ProfileActivity.this, LoginActivity.class);
                            //  LoginActivity.Set_id("","","","","",0,ProfileActivity.this);
                            //  ProfileActivity.Set_id("","","","","","","","","","","","","",ProfileActivity.this);
                            ProfileActivity.Set_id(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, ProfileActivity.this);
                            Profile_Sibling.Set_id("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ProfileActivity.this);
                            SiblingActivity.Set_id("", "", "", "", "", ProfileActivity.this);
                            String childname = ProfileActivity.get_childName(ProfileActivity.this);
                            Log.e("ChildName Logout", childname);
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancel(notificationID);
                            Constant.SetLoginData("", "", ProfileActivity.this);
                            AppController.iconCount = 0;
                            Webservice.drwer = "true";
                            AppController.drawerSignOut = "true";
                            AppController.iconCountOnResume = 0;
                            msgStatusListOnResume.clear();
                            AppController.OnBackpressed = "false";
                            AppController.loginButtonClicked = "false";
                            try {
                                badge.hide();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            startActivity(signout);
                        } else if (position == 8) {
                            try {
                                final Dialog alertDialog = new Dialog(ProfileActivity.this);
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
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    }
                });
            } else {
                int[] icons_without_sibling = {R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.payfeesonline_48x48, R.drawable.att_48x48, R.drawable.parentinformation_48x48,/* R.drawable.studentresources_48x48,*/ R.drawable.setting1_hdpi,
                        R.drawable.logout1_hdpi, R.drawable.info};
                final String[] data_without_sibling = {"Dashboard", "Messages", "Fees Information", "Attendance", "Student Information",/* "Class Room Videos",*/ "Setting",
                        "Sign Out", "About"};
                drawerListView.setAdapter(new CustomAccount(ProfileActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));
                drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if (position == 0) {
                            Intent profile = new Intent(ProfileActivity.this, ProfileActivity.class);
                            for (int i = 0; i < login_details.stundentListDetails.size(); i++) {
                                board_name = login_details.stundentListDetails.get(i).Brd_Name;
                                announcementCount = login_details.stundentListDetails.get(i).annoucementCnt;
                                behaviourCount = login_details.stundentListDetails.get(i).behaviourcnt;
                                AppController.behaviourCount = behaviourCount;
                                AppController.announcementCount = announcementCount;
                                isStudentresource = login_details.stundentListDetails.get(i).StudentResourceExist;
                            }
                            AppController.msd_ID = msd_ID;
                            AppController.clt_id = clt_id;
                            AppController.school_name = school_name;
                            profile.putExtra("msd_ID", AppController.msd_ID);
                            profile.putExtra("clt_id", AppController.clt_id);
                            profile.putExtra("School_name", AppController.school_name);
                            profile.putExtra("usl_id", AppController.usl_id);
                            profile.putExtra("board_name", board_name);
                            profile.putExtra("Std Name", std_name);
                            profile.putExtra("version_name", AppController.versionName);
                            profile.putExtra("annpuncement_count", announcementCount);
                            profile.putExtra("behaviour_count", behaviourCount);
                            profile.putExtra("isStudentResource", isStudentresource);
                            startActivity(profile);
                        } else if (position == 1) {
                            for (int i = 0; i < login_details.stundentListDetails.size(); i++) {

                                board_name = login_details.stundentListDetails.get(i).Brd_Name;
                                announcementCount = login_details.stundentListDetails.get(i).annoucementCnt;
                                behaviourCount = login_details.stundentListDetails.get(i).behaviourcnt;
                                AppController.behaviourCount = behaviourCount;
                            }
                            if (dft.isInternetOn() == true) {
                                Intent dashboard = new Intent(ProfileActivity.this, DashboardActivity.class);
                                AppController.clt_id = clt_id;
                                AppController.msd_ID = msd_ID;
                                AppController.usl_id = usl_id;
                                AppController.Board_name = board_name;
                                dashboard.putExtra("clt_id", AppController.clt_id);
                                dashboard.putExtra("msd_ID", AppController.msd_ID);
                                dashboard.putExtra("usl_id", AppController.usl_id);
                                dashboard.putExtra("board_name", board_name);
                                dashboard.putExtra("Std Name", std_name);
                                dashboard.putExtra("School_name", AppController.school_name);
                                dashboard.putExtra("version_name", AppController.versionName);
                                dashboard.putExtra("annpuncement_count", announcementCount);
                                dashboard.putExtra("behaviour_count", behaviourCount);
                                dashboard.putExtra("isStudentResource", isStudentresource);
                                startActivity(dashboard);
                            }
                        } else if (position == 2) {
                            if (dft.isInternetOn() == true) {
                                Intent feesInfo = new Intent(ProfileActivity.this, FeesInfoActivity.class);
                                for (int i = 0; i < login_details.stundentListDetails.size(); i++) {
                                    board_name = login_details.stundentListDetails.get(i).Brd_Name;
                                    announcementCount = login_details.stundentListDetails.get(i).annoucementCnt;
                                    behaviourCount = login_details.stundentListDetails.get(i).behaviourcnt;
                                    AppController.behaviourCount = behaviourCount;
                                    AppController.announcementCount = announcementCount;
                                    isStudentresource = login_details.stundentListDetails.get(i).StudentResourceExist;
                                }
                                feesInfo.putExtra("board_name", board_name);
                                feesInfo.putExtra("clt_id", AppController.clt_id);
                                feesInfo.putExtra("msd_ID", AppController.msd_ID);
                                feesInfo.putExtra("usl_id", AppController.usl_id);
                                feesInfo.putExtra("School_name", AppController.school_name);
                                feesInfo.putExtra("Std Name", std_name);
                                feesInfo.putExtra("version_name", AppController.versionName);
                                feesInfo.putExtra("annpuncement_count", announcementCount);
                                feesInfo.putExtra("behaviour_count", behaviourCount);
                                feesInfo.putExtra("isStudentResource", isStudentresource);
                                startActivity(feesInfo);
                            }
                        } else if (position == 3) {
                            if (dft.isInternetOn() == true) {
                                Intent attendance = new Intent(ProfileActivity.this, AttendanceActivity.class);
                                for (int i = 0; i < login_details.stundentListDetails.size(); i++) {
                                    board_name = login_details.stundentListDetails.get(i).Brd_Name;
                                    announcementCount = login_details.stundentListDetails.get(i).annoucementCnt;
                                    behaviourCount = login_details.stundentListDetails.get(i).behaviourcnt;
                                    AppController.behaviourCount = behaviourCount;
                                    AppController.announcementCount = announcementCount;
                                    isStudentresource = login_details.stundentListDetails.get(i).StudentResourceExist;
                                }
                                AppController.clt_id = clt_id;
                                AppController.msd_ID = msd_ID;
                                AppController.usl_id = usl_id;
                                attendance.putExtra("clt_id", AppController.clt_id);
                                attendance.putExtra("msd_ID", AppController.msd_ID);
                                attendance.putExtra("usl_id", AppController.usl_id);
                                attendance.putExtra("School_name", AppController.school_name);
                                attendance.putExtra("board_name", board_name);
                                attendance.putExtra("Std Name", std_name);
                                attendance.putExtra("version_name", AppController.versionName);
                                attendance.putExtra("annpuncement_count", announcementCount);
                                attendance.putExtra("behaviour_count", behaviourCount);
                                attendance.putExtra("isStudentResource", isStudentresource);
                                startActivity(attendance);
                            }

                        } else if (position == 4) {
                            for (int i = 0; i < login_details.stundentListDetails.size(); i++) {

                                board_name = login_details.stundentListDetails.get(i).Brd_Name;
                                announcementCount = login_details.stundentListDetails.get(i).annoucementCnt;
                                behaviourCount = login_details.stundentListDetails.get(i).behaviourcnt;
                                AppController.behaviourCount = behaviourCount;
                                AppController.announcementCount = announcementCount;
                                isStudentresource = login_details.stundentListDetails.get(i).StudentResourceExist;
                            }
                            if (dft.isInternetOn() == true) {
                                Intent parentinfo = new Intent(ProfileActivity.this, ParentInformationActivity.class);
                                AppController.clt_id = clt_id;
                                AppController.msd_ID = msd_ID;
                                AppController.usl_id = usl_id;
                                AppController.Board_name = board_name;
                                parentinfo.putExtra("clt_id", AppController.clt_id);
                                parentinfo.putExtra("msd_ID", AppController.msd_ID);
                                parentinfo.putExtra("usl_id", AppController.usl_id);
                                parentinfo.putExtra("board_name", board_name);
                                parentinfo.putExtra("Std Name", std_name);
                                parentinfo.putExtra("School_name", AppController.school_name);
                                parentinfo.putExtra("version_name", AppController.versionName);
                                parentinfo.putExtra("annpuncement_count", announcementCount);
                                parentinfo.putExtra("behaviour_count", behaviourCount);
                                parentinfo.putExtra("isStudentResource", isStudentresource);
                                startActivity(parentinfo);
                            }
                        } else if (position == 5) {
                            if (dft.isInternetOn() == true) {
                                Intent setting = new Intent(ProfileActivity.this, SettingActivity.class);
                                AppController.usl_id = usl_id;
                                setting.putExtra("usl_id", AppController.usl_id);
                                setting.putExtra("version_name", AppController.versionName);
                                setting.putExtra("annpuncement_count", announcementCount);
                                setting.putExtra("behaviour_count", behaviourCount);
                                setting.putExtra("isStudentResource", isStudentresource);
                                startActivity(setting);
                            }
                        } else if (position == 6) {
                            try {
                                Intent signout = new Intent(ProfileActivity.this, LoginActivity.class);
                                LoginActivity.Set_id("", "", "", "", "", 0, "","","","","","","","","", ProfileActivity.this);
                                ProfileActivity.Set_id("", "", "", "", "", "", "", "", "", "", "", "", "","","","","", ProfileActivity.this);
                                Profile_Sibling.Set_id("", "", "", "", "", "", "", "", "", "", "", "", "","","","","", ProfileActivity.this);
                                SiblingActivity.Set_id("", "", "", "", "", ProfileActivity.this);
                                String childname = ProfileActivity.get_childName(ProfileActivity.this);
                                Log.e("ChildName Logout", childname);
                                Constant.SetLoginData("", "", ProfileActivity.this);
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancel(notificationID);
                                AppController.drawerSignOut = "true";
                                AppController.iconCount = 0;
                                Webservice.drwer = "true";
                                AppController.iconCountOnResume = 0;
                                AppController.OnBackpressed = "false";
                                msgStatusListOnResume.clear();
                                AppController.loginButtonClicked = "false";
                                try {
                                    badge.hide();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                startActivity(signout);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (position == 7) {
                            try {
                                final Dialog alertDialog = new Dialog(ProfileActivity.this);
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
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                    }
                });
            }
            Constant.setDrawerData(academic_year, child_name, std, roll_no, ProfileActivity.this);
            setDrawerData();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void setLogo() {
        try {
            //Org Id==2
            if (org_id.equalsIgnoreCase("2") && Brd_ID.equalsIgnoreCase("1") && Integer.valueOf(sec_ID) != 1) {
                //CBSE
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.cbse_225x225));
            } else if (org_id.equalsIgnoreCase("2") && Brd_ID.equalsIgnoreCase("2") && Integer.valueOf(sec_ID) != 1) {
                //ICSE
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.icse_225x225));
            } else if (org_id.equalsIgnoreCase("2") && Brd_ID.equalsIgnoreCase("3") && Integer.valueOf(sec_ID) != 1) {
                //CIE
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.cie_250x100));
            }
            else if(org_id.equalsIgnoreCase("2") && board_name.equalsIgnoreCase("ICSE") && sec_ID.equalsIgnoreCase("1")){
                //ICSE
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.jumbokids_184x184));
            }
            else if(org_id.equalsIgnoreCase("2") && Brd_ID.equalsIgnoreCase("5") && Integer.valueOf(sec_ID) !=1){
                //SSC
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.ssc_250x80));

            }else if(org_id.equalsIgnoreCase("2") && Brd_ID.equalsIgnoreCase("4")){
                //IB
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.podarort_225x225));
            }
            else if( org_id.equalsIgnoreCase("2") && sec_ID.equalsIgnoreCase("1")&& school_name.contains("Podar Jumbo")){
                //Jumbo kids
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.jumbokids_184x184));
            }
            else if( org_id.equalsIgnoreCase("2") && Brd_ID.equalsIgnoreCase("1")&& sec_ID.equalsIgnoreCase("1")){
                //Jumbo kids
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.jumbokids_184x184));
            }


            //Org id==4
            else if (org_id.equalsIgnoreCase("4") && Brd_ID.equalsIgnoreCase("10")) {
                //Lilavti
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.lilavati_250x125));
            } else if (org_id.equalsIgnoreCase("4") && Brd_ID.equalsIgnoreCase("1")) {
                //RN podar
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.rnpodar_225x100));
            } else if (org_id.equalsIgnoreCase("4") && Brd_ID.equalsIgnoreCase("4")) {
                //IB
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.ib_250x100));
            } else if (org_id.equalsIgnoreCase("4") && Brd_ID.equalsIgnoreCase("5")) {
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
            //org id==3

            else if(org_id.equalsIgnoreCase("3") && Brd_ID.equalsIgnoreCase("1") && Integer.valueOf(sec_ID) != 1) {
                //PWS
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.pws_300x95));

            }
            else if(org_id.equalsIgnoreCase("3") && Brd_ID.equalsIgnoreCase("1") && Integer.valueOf(sec_ID)==1){
                //Jumbo Kids
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.jumbokids_184x184));
            }
            else if(org_id.equalsIgnoreCase("3") && Brd_ID.equalsIgnoreCase("1") && Integer.valueOf(sec_ID) == 1 && school_name.contains("Podar Jumbo")) {
                //Jumbo Kids
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.jumbokids_184x184));
            }
            else if (org_id.equalsIgnoreCase("1")) {
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
    private void setDrawerData() {
        tv_academic_year_drawer.setText(Constant.GetAcademicYear(this));
        tv_child_name_drawer.setText(Constant.GetChildName(this));
        tv_std_value_drawer.setText(Constant.GetStandard(this));
        tv_roll_no_value_drawer.setText(Constant.GetRollNo(this));
    }

    @Override
    public void onClick(View v) {
        if(v==ll_dashboard){
            for(int i = 0;i<login_details.stundentListDetails.size();i++){

                board_name = login_details.stundentListDetails.get(i).Brd_Name;
                announcementCount = login_details.stundentListDetails.get(i).annoucementCnt;
                behaviourCount = login_details.stundentListDetails.get(i).behaviourcnt;
                isStudentresource = login_details.stundentListDetails.get(i).StudentResourceExist;
                AppController.behaviourCount = behaviourCount;
            }
            if(dft.isInternetOn()==true){
                Intent dashboard = new Intent(ProfileActivity.this,DashboardActivity.class);
                AppController.clt_id = clt_id;
                AppController.msd_ID = msd_ID;
                AppController.usl_id = usl_id;
                AppController.Board_name = board_name;
                dashboard.putExtra("clt_id", AppController.clt_id);
                dashboard.putExtra("msd_ID", AppController.msd_ID);
                dashboard.putExtra("usl_id", AppController.usl_id);
                dashboard.putExtra("board_name",board_name);
                dashboard.putExtra("Std Name",std_name);
                dashboard.putExtra("School_name", AppController.school_name);
                dashboard.putExtra("version_name", AppController.versionName);
                dashboard.putExtra("annpuncement_count",announcementCount);
                dashboard.putExtra("behaviour_count",behaviourCount);
                dashboard.putExtra("isStudentResource",isStudentresource);
                startActivity(dashboard);
            }
        }
        else if(v==ll_parentInfo)
        {
            for(int i = 0;i<login_details.stundentListDetails.size();i++){

                board_name = login_details.stundentListDetails.get(i).Brd_Name;
                announcementCount = login_details.stundentListDetails.get(i).annoucementCnt;
                behaviourCount = login_details.stundentListDetails.get(i).behaviourcnt;
                isStudentresource = login_details.stundentListDetails.get(i).StudentResourceExist;
                AppController.behaviourCount = behaviourCount;
            }
            if(dft.isInternetOn()==true){
                Intent parentinfo = new Intent(ProfileActivity.this,ParentInformationActivity.class);
                AppController.clt_id = clt_id;
                AppController.msd_ID = msd_ID;
                AppController.usl_id = usl_id;
                AppController.Board_name = board_name;
                parentinfo.putExtra("clt_id", AppController.clt_id);
                parentinfo.putExtra("msd_ID", AppController.msd_ID);
                parentinfo.putExtra("usl_id", AppController.usl_id);
                parentinfo.putExtra("board_name",board_name);
                parentinfo.putExtra("Std Name",std_name);
                parentinfo.putExtra("School_name", AppController.school_name);
                parentinfo.putExtra("version_name", AppController.versionName);
                parentinfo.putExtra("annpuncement_count",announcementCount);
                parentinfo.putExtra("behaviour_count",behaviourCount);
                parentinfo.putExtra("isStudentResource",isStudentresource);
                startActivity(parentinfo);
            }

        }
        else if(v==ll_attendance)
        {
            if(dft.isInternetOn()==true) {
                Intent attendance = new Intent(ProfileActivity.this, AttendanceActivity.class);
                for (int i = 0; i < login_details.stundentListDetails.size(); i++) {
                    board_name = login_details.stundentListDetails.get(i).Brd_Name;
                    announcementCount = login_details.stundentListDetails.get(i).annoucementCnt;
                    behaviourCount = login_details.stundentListDetails.get(i).behaviourcnt;
                    AppController.behaviourCount = behaviourCount;
                    isStudentresource = login_details.stundentListDetails.get(i).StudentResourceExist;
                }
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
                attendance.putExtra("annpuncement_count", announcementCount);
                attendance.putExtra("behaviour_count", behaviourCount);
                attendance.putExtra("isStudentResource", isStudentresource);
                startActivity(attendance);
            }
        }
        else if(v==ll_payFessOnline){
            if(dft.isInternetOn()==true) {
                Intent feesInfo = new Intent(ProfileActivity.this, FeesInfoActivity.class);
                for (int i = 0; i < login_details.stundentListDetails.size(); i++) {
                    board_name = login_details.stundentListDetails.get(i).Brd_Name;
                    announcementCount = login_details.stundentListDetails.get(i).annoucementCnt;
                    behaviourCount = login_details.stundentListDetails.get(i).behaviourcnt;
                    AppController.behaviourCount = behaviourCount;
                    isStudentresource = login_details.stundentListDetails.get(i).StudentResourceExist;
                }
                feesInfo.putExtra("board_name", board_name);
                feesInfo.putExtra("clt_id", AppController.clt_id);
                feesInfo.putExtra("msd_ID", AppController.msd_ID);
                feesInfo.putExtra("usl_id", AppController.usl_id);
                feesInfo.putExtra("School_name", AppController.school_name);
                feesInfo.putExtra("Std Name", std_name);
                feesInfo.putExtra("version_name", AppController.versionName);
                feesInfo.putExtra("annpuncement_count", announcementCount);
                feesInfo.putExtra("behaviour_count", behaviourCount);
                feesInfo.putExtra("isStudentResource",isStudentresource);
                startActivity(feesInfo);
            }
        }
        else if(v==ll_classroomVideos)
        {

        }
        else if(v==ll_studentResource)
        {
            if(dft.isInternetOn()==true) {
                Intent student_resource = new Intent(ProfileActivity.this, StudentResourcesActivity.class);
                for (int i = 0; i < login_details.stundentListDetails.size(); i++) {
                    board_name = login_details.stundentListDetails.get(i).Brd_Name;
                    announcementCount = login_details.stundentListDetails.get(i).annoucementCnt;
                    behaviourCount = login_details.stundentListDetails.get(i).behaviourcnt;
                    isStudentresource = login_details.stundentListDetails.get(i).StudentResourceExist;
                    AppController.behaviourCount = behaviourCount;
                }
                student_resource.putExtra("board_name", board_name);
                student_resource.putExtra("clt_id", AppController.clt_id);
                student_resource.putExtra("msd_ID", AppController.msd_ID);
                student_resource.putExtra("usl_id", AppController.usl_id);
                student_resource.putExtra("School_name", AppController.school_name);
                student_resource.putExtra("Std Name", std_name);
                student_resource.putExtra("version_name", AppController.versionName);
                student_resource.putExtra("annpuncement_count", announcementCount);
                student_resource.putExtra("behaviour_count", behaviourCount);
                student_resource.putExtra("isStudentResource",isStudentresource);
                startActivity(student_resource);
            }
        }
        else if(v==img_drawer)
        {
            drawer.openDrawer(Gravity.LEFT);
        }

    }
    public void callViewMessagesWebservice(String clt_id,String usl_id,String msd_ID,String month_id,String check,String pageNo,String pageSize) {

        dft.getviewMessages(clt_id, usl_id, msd_ID, month_id, check,pageNo,pageSize, ViewMessageMethodName, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_details.Status.equalsIgnoreCase("1")) {
                                if(AppController.OnBackpressed.equalsIgnoreCase("true")){
                                    viewmessageListSizeOnResume = login_details.ViewMessageResult.size();
                                    AppController.viewMessageListSize = viewmessageListSize;
                                    ProfileActivity.Set_ListSize(String.valueOf(viewmessageListSizeOnResume),ProfileActivity.this);
                                    Log.e("Profile On Resume Size",String.valueOf(viewmessageListSize));
                                    for (int i = 0; i < viewmessageListSizeOnResume; i++) {
                                        msgStatusOnResume = login_details.ViewMessageResult.get(i).pmu_readunreadstatus;
                                        if (msgStatusOnResume.equalsIgnoreCase("1")) {
                                            msgStatusListOnResume.add(msgStatusOnResume);
                                            msgStatusSizeOnResume = msgStatusListOnResume.size();
                                        }
                                        AppController.iconCountOnResume = msgStatusSizeOnResume;
                                    }
                                }
                                else{
                                    viewmessageListSize = login_details.ViewMessageResult.size();
                                    AppController.viewMessageListSize = viewmessageListSize;
                                    ProfileActivity.Set_ListSize(String.valueOf(viewmessageListSizeOnResume),ProfileActivity.this);
                                    for (int i = 0; i < login_details.ViewMessageResult.size(); i++) {
                                        msgStatus = login_details.ViewMessageResult.get(i).pmu_readunreadstatus;
                                        if (msgStatus.equalsIgnoreCase("1")) {
                                            msgStatusList.add(msgStatus);
                                            msgStatusSize = msgStatusList.size();
                                        }
                                        AppController.iconCount = msgStatusList.size();
                                    }
                                }


                                setIconCount();

                            }
                            else if(login_details.Status.equalsIgnoreCase("0")){
                                String check = "0", pageNo = "1", pageSize = "150";
                                callLastViewMessagesWebservice(AppController.clt_id, AppController.usl_id, AppController.msd_ID, AppController.month_id, check, "" + pageNo, "" + pageSize);
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
    public void callLastViewMessagesWebservice(String clt_id,String usl_id,String msd_ID,String month_id,String check,String pageNo,String pageSize) {

        dft.getviewMessages(clt_id, usl_id, msd_ID, month_id, check, pageNo, pageSize, LastViewMessageData, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_details.Status.equalsIgnoreCase("1")) {

                                if(AppController.OnBackpressed.equalsIgnoreCase("true")){
                                    viewmessageListSizeOnResume = login_details.ViewMessageResult.size();
                                    AppController.viewMessageListSize = viewmessageListSize;
                                    //    Log.e("Profile On Resume Size",String.valueOf(viewmessageListSize));
                                    for (int i = 0; i < viewmessageListSizeOnResume; i++) {
                                        msgStatusOnResume = login_details.ViewMessageResult.get(i).pmu_readunreadstatus;
                                        if (msgStatusOnResume.equalsIgnoreCase("1")) {
                                            msgStatusListOnResume.add(msgStatusOnResume);
                                            msgStatusSizeOnResume = msgStatusListOnResume.size();
                                        }
                                        AppController.iconCountOnResume = msgStatusSizeOnResume;
                                    }
                                }
                                else{
                                    viewmessageListSize = login_details.ViewMessageResult.size();
                                    AppController.viewMessageListSize = viewmessageListSize;
                                    for (int i = 0; i < login_details.ViewMessageResult.size(); i++) {
                                        msgStatus = login_details.ViewMessageResult.get(i).pmu_readunreadstatus;
                                        if (msgStatus.equalsIgnoreCase("1")) {
                                            msgStatusList.add(msgStatus);
                                            msgStatusSize = msgStatusList.size();
                                        }
                                        AppController.iconCount = msgStatusList.size();
                                    }
                                }

                                setIconCount();
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
    public static void Set_ListSize(String viewMessageListSize,Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = resultpreLoginData.edit();
        editor.putString("viewMessageListSize", viewMessageListSize);
        editor.commit();

    }
    public static String get_ListSize(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        Log.e("ProfilActivity", resultpreLoginData.getString("viewMessageListSize", ""));
        return resultpreLoginData.getString("viewMessageListSize", "");
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
                AppController.Board_name = board_name;
                msgStatusListOnResume.clear();
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
