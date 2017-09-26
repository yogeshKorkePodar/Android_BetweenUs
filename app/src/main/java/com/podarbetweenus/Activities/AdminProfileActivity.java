package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class AdminProfileActivity extends Activity implements View.OnClickListener
{
    //UI Variable
    //Linear Layout
    LinearLayout ll_sms,ll_messages,ll_announcement;
    //ImageView
    ImageView img_drawer,img_icon,img_sms,img_announcement;
    //DrawerLayout
    DrawerLayout drawer;
    //TextView
    TextView tv_admin_name,tv_school_name,tv_child_name_drawer,tv_teacher_name,tv_academic_year_drawer,tv_version_name,tv_version_code,tv_cancel;
    //Relative Layout
    RelativeLayout leftfgf_drawer,rl_profile,rl_school_logo;
    //ProgressDialog
    ProgressDialog progressDialog;
    //BageView
    BadgeView badge;
    //ListView
    ListView drawerListView;

    public static ImageView img_message;

    DataFetchService dft;
    LoginDetails login_details;
    ContentDisplaceDrawerToggle mContentDisplaceToggle;
    public static SharedPreferences resultpreLoginData;


    String clt_id,usl_id,org_id,admin_name,academic_year,board_name,school_name,version_name,check = "0",adminmsgStatusOnResume,adminmsgStatus,badge_count,playstore_version;
    String ShowViewMessagesMethod_name = "ViewAdminMessageDetails";

    ArrayList<String> adminmsgStatusList = new ArrayList<String>();
    ArrayList<String> adminmsgStatusListOnResume = new ArrayList<String>();
    String[] data_without_sibling;
    int[] icons_without_sibling;
    int notificationID = 1,adminMsgstatusSizeOnResume,adminMsgstatusSize;
    int pageNo = 1;
    int pageSize = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.adminprofilactivity);
        findViews();
        init();
        getIntentID();
        setProfileData();
        AppController.Notification_send = "true";
        AppController.AdminSentMessage = "false";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String appPackageName = getPackageName();
        version_name = BuildConfig.VERSION_NAME;
        AppController.versionName = version_name;
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
        try {
            //versionUpdate
            if (Constant.version_update.equalsIgnoreCase("true") && playstore_version != null) {
                versionUpdate();
            }
            AppController.SentEnterDropdown = "false";
            AppController.dropdownSelected = "false";
            AppController.Notification_send = "true";
            AppController.ProfileWithoutSibling = "true";
            AppController.AdminSentMessage = "false";
            ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
            //set Date When No Internet Connectivity
            if (connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                    connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
                clt_id = AdminProfileActivity.get_clt_id(this);
                usl_id = AdminProfileActivity.get_usl_id(this);
                admin_name = AdminProfileActivity.get_teacher_name(this);
                board_name = AdminProfileActivity.get_board_name(this);
                school_name = AdminProfileActivity.get_school_name(this);
                org_id = LoginActivity.get_org_id(this);
                tv_school_name.setText(school_name);
                tv_teacher_name.setText(admin_name);
                //For Drawer
                setDrawer();
                //setLogo
                setLogo();
            }
            setDrawer();
            setLogo();
            AppController.teacher_sms = "false";
            AppController.AdminstudentSMS = "false";
            //call ws to view messages
            callWsToViewMessages(clt_id, usl_id, check, pageNo, pageSize);
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
    private void setLogo() {
        try {
            //Org id==2
            if(org_id.equalsIgnoreCase("2") && board_name.equalsIgnoreCase("CBSE") && !school_name.contains("Podar Jumbo")){
                img_icon.setVisibility(View.VISIBLE);
           //     rl_school_logo.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.cbse_225x225));
            }
            else if(org_id.equalsIgnoreCase("2") && board_name.equalsIgnoreCase("ICSE")){
                img_icon.setVisibility(View.VISIBLE);
             //   rl_school_logo.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.icse_225x225));
            }
            else if(org_id.equalsIgnoreCase("2") && board_name.equalsIgnoreCase("CIE")){
                //CIE
                img_icon.setVisibility(View.VISIBLE);
              //  rl_school_logo.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.cie_250x100));
            }
            else if(org_id.equalsIgnoreCase("2") && board_name.equalsIgnoreCase("SSC")){
                //SSC
                img_icon.setVisibility(View.VISIBLE);
              //  rl_school_logo.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.ssc_250x80));
            }
            else if(org_id.equalsIgnoreCase("2") && board_name.equalsIgnoreCase("IB")){
                //IB
                img_icon.setVisibility(View.VISIBLE);
             //   rl_school_logo.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.podarort_225x225));
            }
            else if(org_id.equalsIgnoreCase("2") && school_name.contains("Podar Jumbo")){
                //Jumbo kids
                img_icon.setVisibility(View.VISIBLE);
             //   rl_school_logo.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.jumbokids_184x184));
            }
            //Org id==4
            else if(org_id.equalsIgnoreCase("4") && board_name.contains("ICSE")){
                //Lilavti
                img_icon.setVisibility(View.VISIBLE);
              //  rl_school_logo.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.lilavati_250x125));
            }
            else if(org_id.equalsIgnoreCase("4") && board_name.equalsIgnoreCase("CBSE")){
                //RN podar
                img_icon.setVisibility(View.VISIBLE);
           //     rl_school_logo.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.rnpodar_225x100));
            }
            else if (org_id.equalsIgnoreCase("4") && board_name.equalsIgnoreCase("IB")) {
                //IB
                img_icon.setVisibility(View.VISIBLE);
           //     rl_school_logo.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.ib_250x100));
            } else if (org_id.equalsIgnoreCase("4") && board_name.equalsIgnoreCase("SSC")) {
                //SSC
                img_icon.setVisibility(View.VISIBLE);
            //    rl_school_logo.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.ssc_250x100));
            }
            else if(org_id.equalsIgnoreCase("4") && clt_id.equalsIgnoreCase("39")){
                //PWC
                img_icon.setVisibility(View.VISIBLE);
            //    rl_school_logo.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.pwc_225x225));
            }
            else if(org_id.equalsIgnoreCase("4") && clt_id.equalsIgnoreCase("38")){
                //PIC
                img_icon.setVisibility(View.VISIBLE);
            //    rl_school_logo.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.pic_225x225));
            }
            //Org id==3
            else if(org_id.equalsIgnoreCase("3") && board_name.equalsIgnoreCase("CBSE")) {
                //PWS
                img_icon.setVisibility(View.VISIBLE);
           //     rl_school_logo.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.pws_300x95));

            }
            else if(org_id.equalsIgnoreCase("3") && board_name.equalsIgnoreCase("CBSE") && school_name.contains("Podar Jumbo")){
                //Jumbo Kids
                img_icon.setVisibility(View.VISIBLE);
            //    rl_school_logo.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.jumbokids_184x184));
            }
            //Org id==1
            else if(org_id.equalsIgnoreCase("1")){
                img_icon.setVisibility(View.VISIBLE);
          //      rl_school_logo.setVisibility(View.VISIBLE);
                img_icon.setImageDrawable(getResources().getDrawable(R.drawable.cbse_225x225));
            } else {
                img_icon.setVisibility(View.INVISIBLE);
            //    rl_school_logo.setVisibility(View.GONE);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setProfileData() {
      //  tv_admin_name.setText("Welcome, "+admin_name);
     //   tv_school_name.setText(school_name);
        tv_school_name.setText(school_name);
        tv_teacher_name.setText(admin_name);
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
    private void setDrawer() {
        icons_without_sibling = new int[]{R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sms_48x48, R.drawable.announcementicon_48x48/*, R.drawable.att_48x48, R.drawable.behaviouricon_48x48, R.drawable.subjectlist_48x48*/, R.drawable.setting1_hdpi, R.drawable.logout1_hdpi, R.drawable.info};
        data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement"/*, "Attendance", "Behaviour", "Subject List"*/, "Setting", "Sign Out", "About"};
        drawerListView.setAdapter(new CustomAccount(AdminProfileActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));

        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (dft.isInternetOn() == true) {
                        Intent adminProfileActivity = new Intent(AdminProfileActivity.this, AdminProfileActivity.class);
                        adminProfileActivity.putExtra("clt_id", clt_id);
                        adminProfileActivity.putExtra("usl_id", usl_id);
                        adminProfileActivity.putExtra("board_name", board_name);
                        adminProfileActivity.putExtra("Admin_name", admin_name);
                        adminProfileActivity.putExtra("School_name", school_name);
                        adminProfileActivity.putExtra("org_id", org_id);
                        adminProfileActivity.putExtra("academic_year",academic_year);
                        adminProfileActivity.putExtra("version_name", AppController.versionName);
                        adminProfileActivity.putExtra("verion_code", AppController.versionCode);
                        startActivity(adminProfileActivity);
                    }
                } else if (position == 1) {
                    //Message
                    if (dft.isInternetOn() == true) {
                        Intent adminMessageIntent = new Intent(AdminProfileActivity.this, AdminMessageActivity.class);
                        adminMessageIntent.putExtra("clt_id", clt_id);
                        adminMessageIntent.putExtra("usl_id", usl_id);
                        adminMessageIntent.putExtra("board_name", board_name);
                        adminMessageIntent.putExtra("Admin_name", admin_name);
                        adminMessageIntent.putExtra("School_name", school_name);
                        adminMessageIntent.putExtra("org_id", org_id);
                        adminMessageIntent.putExtra("academic_year",academic_year);
                        adminMessageIntent.putExtra("version_name", AppController.versionName);
                        adminMessageIntent.putExtra("verion_code", AppController.versionCode);
                        startActivity(adminMessageIntent);
                    }
                } else if (position == 2) {
                    //SMS
                    if (dft.isInternetOn() == true) {
                        Intent adminSMSIntent = new Intent(AdminProfileActivity.this, AdminSMSActivity.class);
                        adminSMSIntent.putExtra("clt_id", clt_id);
                        adminSMSIntent.putExtra("usl_id", usl_id);
                        adminSMSIntent.putExtra("board_name", board_name);
                        adminSMSIntent.putExtra("Admin_name", admin_name);
                        adminSMSIntent.putExtra("School_name", school_name);
                        adminSMSIntent.putExtra("org_id", org_id);
                        adminSMSIntent.putExtra("academic_year",academic_year);
                        adminSMSIntent.putExtra("version_name", AppController.versionName);
                        adminSMSIntent.putExtra("verion_code", AppController.versionCode);
                        startActivity(adminSMSIntent);
                    }
                } else if (position == 3) {
                    //Announcement
                    if (dft.isInternetOn() == true) {
                        Intent adminAnnouncementIntent = new Intent(AdminProfileActivity.this, AdminAnnouncmentActivity.class);
                        adminAnnouncementIntent.putExtra("clt_id", clt_id);
                        adminAnnouncementIntent.putExtra("usl_id", usl_id);
                        adminAnnouncementIntent.putExtra("board_name", board_name);
                        adminAnnouncementIntent.putExtra("Admin_name", admin_name);
                        adminAnnouncementIntent.putExtra("School_name", school_name);
                        adminAnnouncementIntent.putExtra("org_id", org_id);
                        adminAnnouncementIntent.putExtra("academic_year",academic_year);
                        adminAnnouncementIntent.putExtra("version_name", AppController.versionName);
                        adminAnnouncementIntent.putExtra("verion_code", AppController.versionCode);
                        startActivity(adminAnnouncementIntent);
                    }
                }
                else if(position == 4){
                    //Setting
                    if (dft.isInternetOn() == true) {
                        Intent adminsettingIntent = new Intent(AdminProfileActivity.this, SettingActivity.class);
                        adminsettingIntent.putExtra("clt_id", clt_id);
                        adminsettingIntent.putExtra("usl_id", usl_id);
                        adminsettingIntent.putExtra("board_name", board_name);
                        adminsettingIntent.putExtra("Admin_name", admin_name);
                        adminsettingIntent.putExtra("School_name", school_name);
                        adminsettingIntent.putExtra("org_id", org_id);
                        adminsettingIntent.putExtra("academic_year",academic_year);
                        adminsettingIntent.putExtra("version_name", AppController.versionName);
                        adminsettingIntent.putExtra("verion_code", AppController.versionCode);
                        startActivity(adminsettingIntent);
                    }
                }

                else if (position == 5) {
                    //Signout
                    Intent signout = new Intent(AdminProfileActivity.this, LoginActivity.class);
                    Constant.SetLoginData("", "", AdminProfileActivity.this);
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(notificationID);
                    AppController.iconCount = 0;
                    AppController.iconCountOnResume = 0;
                    AppController.OnBackpressed = "false";
                    // teachermsgStatusListOnResume.clear();
                    AppController.loginButtonClicked = "false";
                    AppController.drawerSignOut = "true";
                    startActivity(signout);
                }
                else if (position == 6) {
                    try {
                        final Dialog alertDialog = new Dialog(AdminProfileActivity.this);
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
        Constant.setAdminDrawerData(academic_year, admin_name, AdminProfileActivity.this);
        setDrawerData();
    }

    private void setDrawerData() {
        tv_child_name_drawer.setText(admin_name);
        tv_academic_year_drawer.setText(academic_year);
    }

    private void getIntentID() {
        try {
            Intent intent = getIntent();
            usl_id = intent.getStringExtra("usl_id");
            clt_id = intent.getStringExtra("clt_id");
            school_name = intent.getStringExtra("School_name");
            admin_name = intent.getStringExtra("Admin_name");
            version_name = intent.getStringExtra("version_name");
            org_id = intent.getStringExtra("org_id");
            academic_year = intent.getStringExtra("academic_year");
            AppController.versionName = version_name;
            board_name = intent.getStringExtra("board_name");
            AdminProfileActivity.Set_id(clt_id, usl_id, admin_name, school_name, board_name, org_id, AdminProfileActivity.this);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void init() {
        ll_sms.setOnClickListener(this);
        ll_messages.setOnClickListener(this);
        ll_announcement.setOnClickListener(this);
        img_drawer.setOnClickListener(this);
        img_sms.setOnClickListener(this);
        img_announcement.setOnClickListener(this);
        img_message.setOnClickListener(this);
        progressDialog = Constant.getProgressDialog(this);
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        tv_academic_year_drawer.setVisibility(View.GONE);
    }

    private void findViews() {
        //Linaear Layout
        ll_sms = (LinearLayout) findViewById(R.id.ll_sms);
        ll_messages = (LinearLayout) findViewById(R.id.ll_messages);
        ll_announcement = (LinearLayout) findViewById(R.id.ll_announcement);
        //ImageView
        img_drawer = (ImageView) findViewById(R.id.img_drawer);
        img_icon = (ImageView) findViewById(R.id.img_icon);
        img_message = (ImageView) findViewById(R.id.img_message);
        img_announcement = (ImageView) findViewById(R.id.img_announcement);
        img_sms = (ImageView) findViewById(R.id.img_sms);
        //TextView
        tv_admin_name = (TextView) findViewById(R.id.tv_admin_name);
        tv_school_name = (TextView) findViewById(R.id.tv_school_name);
        tv_child_name_drawer = (TextView) findViewById(R.id.tv_child_name_drawer);
        tv_teacher_name = (TextView) findViewById(R.id.tv_teacher_name);
        tv_academic_year_drawer = (TextView) findViewById(R.id.tv_academic_year_drawer);
        //Relative Layout
        rl_school_logo = (RelativeLayout) findViewById(R.id.rl_school_logo);
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
    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (AppController.fromSplashScreen.equalsIgnoreCase("true")) {

                //AppController.fromSplashScreen = "false";
                AppController.killApp = "false";
                AppController.ProfileWithoutSibling = "true";
                if (AppController.OnBackpressed.equalsIgnoreCase("true")) {
                    //call ws to view messages
                    callWsToViewMessages(clt_id, usl_id, check, pageNo,pageSize);
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
                intent.putExtra("academic_year",academic_year);
                intent.putExtra("Admin_name", admin_name);
                startActivity(intent);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void Set_id(String clt_id,String usl_id,String admin_name,String school_name,String board_name,String org_id, Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = resultpreLoginData.edit();
        editor.putString("clt_id", clt_id);
        editor.putString("usl_id", usl_id);
        editor.putString("Admin_name", admin_name);
        editor.putString("school_name", school_name);
        editor.putString("board_name", board_name);
        editor.putString("org_id", org_id);
        editor.commit();
    }
    public static String get_clt_id(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("clt_id", "");
    }
    public static String get_usl_id(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("usl_id", "");
    }
    public static String get_teacher_name(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("Admin_name", "");
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

    private void callWsToViewMessages(String clt_id,String usl_id,String check,int pageNo,int pageSize) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getAdminMessages(clt_id, usl_id, check, pageNo, pageSize, ShowViewMessagesMethod_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);

                            if (login_details.Status.equalsIgnoreCase("1")) {

                                if(AppController.OnBackpressed.equalsIgnoreCase("true")){
                                    AppController.viewAdminMessageListSize = login_details.ViewMessageResult.size();
                                    for (int i = 0; i < login_details.ViewMessageResult.size(); i++) {
                                        adminmsgStatusOnResume = login_details.ViewMessageResult.get(i).pmu_readunreadstatus;
                                        if (adminmsgStatusOnResume.equalsIgnoreCase("1")) {
                                            adminmsgStatusListOnResume.add(adminmsgStatusOnResume);
                                            adminMsgstatusSizeOnResume = adminmsgStatusListOnResume.size();
                                        }
                                        AppController.iconCountOnResume = adminMsgstatusSizeOnResume;
                                        Log.e("Admin Profile onRes", String.valueOf(AppController.iconCountOnResume));
                                    }
                                }
                                else {
                                    AppController.viewAdminMessageListSize = login_details.ViewMessageResult.size();
                                    for (int i = 0; i < login_details.ViewMessageResult.size(); i++) {
                                        adminmsgStatus = login_details.ViewMessageResult.get(i).pmu_readunreadstatus.toString();
                                        if (adminmsgStatus.equalsIgnoreCase("1")) {
                                            adminmsgStatusList.add(adminmsgStatus);
                                            adminMsgstatusSize = adminmsgStatusList.size();
                                        }
                                        AppController.iconCount = adminMsgstatusSize;
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
                        Log.d("AdminProfileActivity", "ERROR.._---" + error.getCause());
                    }
                });
    }
    @Override
    public void onClick(View v) {
        if(v==img_sms){
            if (dft.isInternetOn() == true) {
                Intent sms = new Intent(AdminProfileActivity.this, AdminSMSActivity.class);
                AppController.AdminstudentSMS = "false";
                AppController.teacher_sms = "false";
                sms.putExtra("Admin_name", admin_name);
                sms.putExtra("clt_id", clt_id);
                sms.putExtra("usl_id", usl_id);
                sms.putExtra("board_name", board_name);
                sms.putExtra("org_id", org_id);
                sms.putExtra("School_name", school_name);
                sms.putExtra("academic_year",academic_year);
                sms.putExtra("tab_student", AppController.AdminstudentSMS);
                sms.putExtra("tab_teacher",AppController.teacher_sms);
                sms.putExtra("version_name", AppController.versionName);
                startActivity(sms);
            }
        }
        else if(v==img_message){
            if (dft.isInternetOn() == true) {
                Intent message = new Intent(AdminProfileActivity.this, AdminMessageActivity.class);
                message.putExtra("Admin_name", admin_name);
                message.putExtra("clt_id", clt_id);
                message.putExtra("usl_id", usl_id);
                message.putExtra("board_name", board_name);
                message.putExtra("org_id", org_id);
                message.putExtra("School_name", school_name);
                message.putExtra("academic_year",academic_year);
                message.putExtra("version_name", AppController.versionName);
                startActivity(message);
            }
        }
        else if(v==img_announcement){
            if(dft.isInternetOn() == true){
                Intent announcement = new Intent(AdminProfileActivity.this, AdminAnnouncmentActivity.class);
                announcement.putExtra("Admin_name", admin_name);
                announcement.putExtra("clt_id", clt_id);
                announcement.putExtra("usl_id", usl_id);
                announcement.putExtra("board_name", board_name);
                announcement.putExtra("org_id", org_id);
                announcement.putExtra("School_name", school_name);
                announcement.putExtra("academic_year",academic_year);
                announcement.putExtra("version_name", AppController.versionName);
                startActivity(announcement);
            }

        }
        else if(v==img_drawer){
            drawer.openDrawer(Gravity.LEFT);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.closeDrawer(Gravity.LEFT);
        }
        else {
            try {
                AppController.OnBackpressed = "true";
                AppController.iconCount = 0;
                AppController.iconCountOnResume = 0;
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
