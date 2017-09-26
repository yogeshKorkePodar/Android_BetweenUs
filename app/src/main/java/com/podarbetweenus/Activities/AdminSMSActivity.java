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
import android.view.WindowManager;
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
 * Created by Gayatri on 3/15/2016.
 */
public class AdminSMSActivity extends TabActivity implements TabHost.OnTabChangeListener,View.OnClickListener {
    //UI variables
    //TextView
    TextView title, tv_child_name_drawer,tv_academic_year_drawer,tv_version_name,tv_version_code,tv_cancel;
    //Linear Layout
    LinearLayout lay_back_investment,lL_drawer;
    //ListView
    ListView drawerListView;
    //ImageView
    ImageView img_drawer;
    //DrawerLayout
    DrawerLayout drawer;
    //RelativeLayout
    RelativeLayout rl_send_sms,leftfgf_drawer,rl_profile;

    HeaderControler header;
    DataFetchService dft;
    ContentDisplaceDrawerToggle mContentDisplaceToggle;

    String org_id,clt_id,usl_id,board_name,school_name,admin_name,version_name,academic_year;
    String[] data_without_sibling;
    int[] icons_without_sibling;
    int notificationID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.admin_sms_layout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        findViews();
        init();
        getIntentId();
        setTabs();
        setDrawer();
        TabHost tabHost = getTabHost();
        if(AppController.teacher_sms.equalsIgnoreCase("true"))
        {
            tabHost.setCurrentTab(2);
        }
        else if(AppController.AdminstudentSMS.equalsIgnoreCase("true")){
            tabHost.setCurrentTab(2);
        }
        else if(AppController.AdminschoolSMS.equalsIgnoreCase("true")){
            tabHost.setCurrentTab(0);
        }
    }

    private void getIntentId() {
        Intent intent = getIntent();
        usl_id = intent.getStringExtra("usl_id");
        clt_id = intent.getStringExtra("clt_id");
        school_name = intent.getStringExtra("School_name");
        admin_name = intent.getStringExtra("Admin_name");
        version_name = intent.getStringExtra("version_name");
        org_id = intent.getStringExtra("org_id");
        academic_year = intent.getStringExtra("academic_year");
        AppController.versionName = version_name;
       // AppController.AdminstudentSMS = intent.getStringExtra("tab_student");
      //  AppController.teacher_sms = intent.getStringExtra("tab_teacher");
        board_name = intent.getStringExtra("board_name");
    }

    private void init() {
        TabHost tabHost = getTabHost();
        lay_back_investment.setOnClickListener(this);
        tabHost.setOnTabChangedListener(this);
        header = new HeaderControler(this, true, false, "SMS");
        dft = new DataFetchService(this);
        img_drawer.setOnClickListener(this);
        lL_drawer.setOnClickListener(this);
        tv_academic_year_drawer.setVisibility(View.GONE);
    }

    private void setTabs() {
        if(!drawer.isDrawerOpen(Gravity.RIGHT)) {
            addTab("School-SMS", SchoolSMSActivity.class);
            addTab("Student-SMS", StudentSMSActivity.class);
            addTab("Teacher-SMS", TeacherSMSAdminActivity.class);
        }
    }

    private void findViews() {
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
        lL_drawer = (LinearLayout) findViewById(R.id.lL_drawer);
        tv_child_name_drawer = (TextView) findViewById(R.id.tv_child_name_drawer);
        tv_academic_year_drawer = (TextView) findViewById(R.id.tv_academic_year_drawer);
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
        int width = getResources().getDisplayMetrics().widthPixels/2+30;
        DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) leftfgf_drawer.getLayoutParams();
        lp.width = width+30;
        leftfgf_drawer.setLayoutParams(lp);
    }
    private void setDrawer() {
        icons_without_sibling = new int[]{R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sms_48x48, R.drawable.announcementicon_48x48,/* R.drawable.att_48x48, R.drawable.behaviouricon_48x48, R.drawable.subjectlist_48x48,*/ R.drawable.setting1_hdpi, R.drawable.logout1_hdpi, R.drawable.info};
        data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement"/*, "Attendance", "Behaviour", "Subject List"*/, "Setting", "Sign Out", "About"};
        drawerListView.setAdapter(new CustomAccount(AdminSMSActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));

        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (dft.isInternetOn() == true) {
                        Intent adminProfileActivity = new Intent(AdminSMSActivity.this, AdminProfileActivity.class);
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
                        Intent adminMessageIntent = new Intent(AdminSMSActivity.this, AdminMessageActivity.class);
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
                        Intent adminSMSIntent = new Intent(AdminSMSActivity.this, AdminSMSActivity.class);
                        adminSMSIntent.putExtra("clt_id", clt_id);
                        adminSMSIntent.putExtra("usl_id", usl_id);
                        adminSMSIntent.putExtra("board_name", board_name);
                        adminSMSIntent.putExtra("Admin_name", admin_name);
                        adminSMSIntent.putExtra("School_name", school_name);
                        adminSMSIntent.putExtra("academic_year",academic_year);
                        adminSMSIntent.putExtra("org_id", org_id);
                        adminSMSIntent.putExtra("version_name", AppController.versionName);
                        adminSMSIntent.putExtra("verion_code", AppController.versionCode);
                        startActivity(adminSMSIntent);
                    }
                } else if (position == 3) {
                    //Announcement
                    if (dft.isInternetOn() == true) {
                        Intent adminAnnouncementIntent = new Intent(AdminSMSActivity.this, AdminAnnouncmentActivity.class);
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
                } else if (position == 4) {
                        //Setting
                        if (dft.isInternetOn() == true) {
                            Intent adminsettingIntent = new Intent(AdminSMSActivity.this, SettingActivity.class);
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
                } else if (position == 5) {
                    //Signout
                    Intent signout = new Intent(AdminSMSActivity.this, LoginActivity.class);
                    Constant.SetLoginData("", "", AdminSMSActivity.this);
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
                        final Dialog alertDialog = new Dialog(AdminSMSActivity.this);
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
        Constant.setAdminDrawerData(academic_year, admin_name, AdminSMSActivity.this);
        setDrawerData();
    }

    private void setDrawerData() {
        tv_child_name_drawer.setText(admin_name);
        tv_academic_year_drawer.setText(academic_year);
    }

    private void addTab(String labelId, Class<?> c) {
        if(!drawer.isDrawerOpen(Gravity.RIGHT)) {
            TabHost tabHost = getTabHost();
            Intent intent = new Intent(this, c);
            intent.putExtra("usl_id", usl_id);
            intent.putExtra("clt_id", clt_id);
            intent.putExtra("School_name", school_name);
            intent.putExtra("Admin_name", admin_name);
            intent.putExtra("org_id", org_id);
            intent.putExtra("academic_year", academic_year);
            intent.putExtra("version_name", AppController.versionName);
            intent.putExtra("board_name", board_name);
            tabHost.getTabWidget().setShowDividers(TabWidget.SHOW_DIVIDER_MIDDLE);
            TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);
            View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator_for_admin, getTabWidget(), false);
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
    }

    @Override
    public void onTabChanged(String tabId) {
        if(!drawer.isDrawerOpen(Gravity.RIGHT)) {
            TabHost tabHost = getTabHost();
            for (int j = 0; j < tabHost.getTabWidget().getChildCount(); j++) {
                TextView title = (TextView) tabHost.getTabWidget().getChildAt(j).findViewById(R.id.title); // for Unselected Tab
                title.setTextColor(Color.parseColor("#ffffff"));
            }
            TextView title = (TextView) tabHost.getCurrentTabView().findViewById(R.id.title); //for Selected Tab
            title.setTextColor(Color.parseColor("#000000"));
        }
    }
    @Override
    public void onClick(View v) {
        if(v==lay_back_investment){
            try {
                Intent back = new Intent(AdminSMSActivity.this, AdminProfileActivity.class);
                AppController.teacher_sms = "false";
                AppController.AdminstudentSMS = "false";
                back.putExtra("clt_id", clt_id);
                back.putExtra("usl_id", usl_id);
                back.putExtra("School_name", school_name);
                back.putExtra("Admin_name", admin_name);
                back.putExtra("version_name", version_name);
                back.putExtra("board_name", board_name);
                back.putExtra("org_id", org_id);
                back.putExtra("academic_year",academic_year);
                startActivity(back);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(v==img_drawer){
            drawer.openDrawer(Gravity.RIGHT);
        }
        else if(v==lL_drawer){
            TabHost tabHost = getTabHost();
            tabHost.setClickable(false);
        }
    }
    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        try {
            AppController.AdminDropResult.clear();
            Intent back = new Intent(AdminSMSActivity.this, AdminProfileActivity.class);
            AppController.AdminDropResult.clear();
            AppController.AdminstudentSMS = "false";
            AppController.teacher_sms = "false";
            AppController.AdminschoolSMS = "false";
            back.putExtra("usl_id", usl_id);
            back.putExtra("clt_id", clt_id);
            back.putExtra("School_name", school_name);
            back.putExtra("Admin_name", admin_name);
            back.putExtra("org_id", org_id);
            back.putExtra("academic_year",academic_year);
            back.putExtra("version_name", AppController.versionName);
            back.putExtra("board_name", board_name);
            startActivity(back);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
