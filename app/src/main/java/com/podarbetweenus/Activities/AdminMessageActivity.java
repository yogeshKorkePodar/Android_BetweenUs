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
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

/**
 * Created by Gayatri on 3/16/2016.
 */
public class AdminMessageActivity extends TabActivity implements TabHost.OnTabChangeListener,View.OnClickListener{
    //UI variables
    //TextView
    TextView title,tv_child_name_drawer,tv_academic_year_drawer,tv_version_name,tv_version_code,tv_cancel;
    LinearLayout lay_back_investment,lL_drawer;
     //ListView
    ListView drawerListView;
    //ImageView
    ImageView img_drawer;
    //DrawerLayout
    DrawerLayout drawer;
    //RelativeLayout
    RelativeLayout rl_send_sms,leftfgf_drawer,rl_profile;

    ContentDisplaceDrawerToggle mContentDisplaceToggle;
    HeaderControler header;
    DataFetchService dft;
    LoginDetails login_details;

    String[] data_without_sibling;
    int[] icons_without_sibling;
    int notificationID = 1;
    String usl_id,clt_id,board_name,org_id,version_name,school_name,admin_name,academic_year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.admin_message_layout);
        findViews();
        init();
        getIntentId();
        setDrawer();
        setTabs();
        TabHost tabHost = getTabHost();
        tabHost.setOnTabChangedListener(this);

        if(AppController.AdminSentMessage.equalsIgnoreCase("true")){
            tabHost.setCurrentTab(2);
        }
        else if(AppController.AdminWriteMessage.equalsIgnoreCase("true")){
            tabHost.setCurrentTab(1);
        }
    }
    private void setDrawer() {
        icons_without_sibling = new int[]{R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sms_48x48, R.drawable.announcementicon_48x48, /*R.drawable.att_48x48, R.drawable.behaviouricon_48x48, R.drawable.subjectlist_48x48,*/ R.drawable.setting1_hdpi, R.drawable.logout1_hdpi, R.drawable.info};
        data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement",/* "Attendance", "Behaviour", "Subject List",*/ "Setting", "Sign Out", "About"};
        drawerListView.setAdapter(new CustomAccount(AdminMessageActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));

        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (position == 0) {
                        if (dft.isInternetOn() == true) {
                            Intent back = new Intent(AdminMessageActivity.this, AdminProfileActivity.class);
                            back.putExtra("clt_id", clt_id);
                            back.putExtra("usl_id", usl_id);
                            back.putExtra("board_name", board_name);
                            back.putExtra("Admin_name", admin_name);
                            back.putExtra("School_name", school_name);
                            back.putExtra("org_id", org_id);
                            back.putExtra("academic_year", academic_year);
                            back.putExtra("version_name", AppController.versionName);
                            back.putExtra("verion_code", AppController.versionCode);
                            startActivity(back);
                        }
                    } else if (position == 1) {
                        //Message
                        if (dft.isInternetOn() == true) {
                            Intent back = new Intent(AdminMessageActivity.this, AdminMessageActivity.class);
                            back.putExtra("clt_id", clt_id);
                            back.putExtra("usl_id", usl_id);
                            back.putExtra("board_name", board_name);
                            back.putExtra("Admin_name", admin_name);
                            back.putExtra("School_name", school_name);
                            back.putExtra("org_id", org_id);
                            back.putExtra("academic_year", academic_year);
                            back.putExtra("version_name", AppController.versionName);
                            back.putExtra("verion_code", AppController.versionCode);
                            startActivity(back);
                        }
                    } else if (position == 2) {
                        if (dft.isInternetOn() == true) {
                            Intent back = new Intent(AdminMessageActivity.this, AdminSMSActivity.class);
                            back.putExtra("clt_id", clt_id);
                            back.putExtra("usl_id", usl_id);
                            back.putExtra("board_name", board_name);
                            back.putExtra("Admin_name", admin_name);
                            back.putExtra("School_name", school_name);
                            back.putExtra("org_id", org_id);
                            back.putExtra("academic_year", academic_year);
                            back.putExtra("version_name", AppController.versionName);
                            back.putExtra("verion_code", AppController.versionCode);
                            startActivity(back);
                        }
                    } else if (position == 3) {
                        if (dft.isInternetOn() == true) {
                            Intent back = new Intent(AdminMessageActivity.this, AdminAnnouncmentActivity.class);
                            back.putExtra("clt_id", clt_id);
                            back.putExtra("usl_id", usl_id);
                            back.putExtra("board_name", board_name);
                            back.putExtra("Admin_name", admin_name);
                            back.putExtra("School_name", school_name);
                            back.putExtra("org_id", org_id);
                            back.putExtra("academic_year", academic_year);
                            back.putExtra("version_name", AppController.versionName);
                            back.putExtra("verion_code", AppController.versionCode);
                            startActivity(back);
                        }
                    } else if (position == 4) {
                        //Setting
                        if (dft.isInternetOn() == true) {
                            Intent adminsettingIntent = new Intent(AdminMessageActivity.this, SettingActivity.class);
                            adminsettingIntent.putExtra("clt_id", clt_id);
                            adminsettingIntent.putExtra("usl_id", usl_id);
                            adminsettingIntent.putExtra("board_name", board_name);
                            adminsettingIntent.putExtra("Admin_name", admin_name);
                            adminsettingIntent.putExtra("School_name", school_name);
                            adminsettingIntent.putExtra("org_id", org_id);
                            adminsettingIntent.putExtra("academic_year", academic_year);
                            adminsettingIntent.putExtra("version_name", AppController.versionName);
                            adminsettingIntent.putExtra("verion_code", AppController.versionCode);
                            startActivity(adminsettingIntent);
                        }
                    } else if (position == 5) {
                        //Signout
                        Intent signout = new Intent(AdminMessageActivity.this, LoginActivity.class);
                        Constant.SetLoginData("", "", AdminMessageActivity.this);
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancel(notificationID);
                        AppController.iconCount = 0;
                        AppController.iconCountOnResume = 0;
                        AppController.OnBackpressed = "false";
                        // teachermsgStatusListOnResume.clear();
                        AppController.loginButtonClicked = "false";
                        AppController.drawerSignOut = "true";
                        startActivity(signout);
                    } else if (position == 6) {
                        try {
                            final Dialog alertDialog = new Dialog(AdminMessageActivity.this);
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
                catch (Exception e){
                    e.printStackTrace();
                }
            }

        });
        Constant.setAdminDrawerData(academic_year, admin_name, AdminMessageActivity.this);
        setDrawerData();
    }

    private void setDrawerData() {
        tv_child_name_drawer.setText(admin_name);
        tv_academic_year_drawer.setText(academic_year);
    }

    private void getIntentId() {
        try {
            Intent intent = getIntent();
            usl_id = intent.getStringExtra("usl_id");
            clt_id = intent.getStringExtra("clt_id");
            school_name = intent.getStringExtra("School_name");
            admin_name = intent.getStringExtra("Admin_name");
            version_name = intent.getStringExtra("version_name");
            academic_year = intent.getStringExtra("academic_year");
            org_id = intent.getStringExtra("org_id");
            AppController.versionName = version_name;
            board_name = intent.getStringExtra("board_name");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setTabs() {
        addTab("View Messages", AdminViewMessage.class);
        addTab("Write Messages", AdminWriteMessage.class);
        addTab("Sent Messages", AdminSentMessage.class);
    }

    private void init() {
        dft= new DataFetchService(this);
        login_details = new LoginDetails();
        header = new HeaderControler(this, true, false, "Messages");
        img_drawer.setOnClickListener(this);
        lay_back_investment.setOnClickListener(this);
        lL_drawer.setOnClickListener(this);
        tv_academic_year_drawer.setVisibility(View.GONE);
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
        int width = getResources().getDisplayMetrics().widthPixels/2 +30;
        DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) leftfgf_drawer.getLayoutParams();
        lp.width = width + 30;
        leftfgf_drawer.setLayoutParams(lp);
    }
    private void addTab(String labelId, Class<?> c) {
        TabHost tabHost = getTabHost();
        Intent intent = new Intent(this, c);
        intent.putExtra("usl_id",usl_id);
        intent.putExtra("clt_id", clt_id);
        intent.putExtra("School_name", school_name);
        intent.putExtra("Admin_name", admin_name);
        intent.putExtra("org_id",org_id);
        intent.putExtra("academic_year",academic_year);
        intent.putExtra("version_name", AppController.versionName);
        intent.putExtra("board_name", board_name);
        intent.putExtra("Tab_Position",AppController.AdminTabPosition);
        tabHost.getTabWidget().setShowDividers(TabWidget.SHOW_DIVIDER_MIDDLE);
        TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);
        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator_for_teacher, getTabWidget(), false);
        title = (TextView) tabIndicator.findViewById(R.id.title);
        title.setText(labelId);

        spec.setIndicator(tabIndicator);
        spec.setContent(intent);
        tabHost.addTab(spec);

        TextView title = (TextView) tabHost.getCurrentTabView().findViewById(R.id.title); //for Selected Tab
        title.setTextColor(Color.parseColor("#000000"));
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
        AppController.AdminTabPosition = i;
        Log.e("appTab",String.valueOf(AppController.AdminTabPosition));
        TextView title = (TextView) tabHost.getCurrentTabView().findViewById(R.id.title); //for Selected Tab
        title.setTextColor(Color.parseColor("#000000"));
    }

    @Override
    public void onClick(View v) {
        try {
            if (v == lay_back_investment) {
                Intent back = new Intent(AdminMessageActivity.this, AdminProfileActivity.class);
                AppController.OnBackpressed = "false";
                AppController.clt_id = clt_id;
                AppController.usl_id = usl_id;
                AppController.Board_name = board_name;
                AppController.enterMessageBack = "false";
                AppController.teacher_sms = "false";
                AppController.AdminSentMessage = "false";
                back.putExtra("clt_id", clt_id);
                back.putExtra("usl_id", usl_id);
                back.putExtra("School_name", school_name);
                back.putExtra("Admin_name", admin_name);
                back.putExtra("version_name", version_name);
                back.putExtra("board_name", board_name);
                back.putExtra("academic_year", academic_year);
                back.putExtra("org_id", org_id);
                startActivity(back);
            } else if (v == img_drawer) {
                drawer.openDrawer(Gravity.RIGHT);
            } else if (v == lL_drawer) {
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
            Intent back = new Intent(AdminMessageActivity.this, AdminProfileActivity.class);
            AppController.OnBackpressed = "false";
            AppController.AdminSentMessage = "false";
            AppController.AdminWriteMessage = "false";
            AppController.clt_id = clt_id;
            AppController.usl_id = usl_id;
            AppController.Board_name = board_name;
            AppController.enterMessageBack = "false";
            back.putExtra("clt_id", AppController.clt_id);
            back.putExtra("usl_id", AppController.usl_id);
            back.putExtra("board_name", board_name);
            back.putExtra("Admin_name", admin_name);
            back.putExtra("School_name", school_name);
            back.putExtra("org_id", org_id);
            back.putExtra("academic_year", academic_year);
            back.putExtra("version_name", AppController.versionName);
            back.putExtra("verion_code", AppController.versionCode);
            startActivity(back);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
