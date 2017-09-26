package com.podarbetweenus.Activities;

import android.app.Activity;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.podarbetweenus.Adapter.AnnouncementAdapter;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by Gayatri on 4/2/2016.
 */

//

public class AdminAnnouncmentActivity extends Activity implements View.OnClickListener,OnItemClickListener{
    //UI Variables
    //TextView
    TextView tv_child_name_drawer,title,tv_no_record,tv_academic_year_drawer,tv_version_name,tv_version_code,tv_cancel,tv_announcement_title;
    LinearLayout lay_back_investment,ll_add_announcement;
    //ListView
    ListView drawerListView,lv_admin_announcements;
    //EditText
    EditText ed_enter_announcement;
    //Button
    Button btn_update,btn_cancel;
    //ImageView
    ImageView img_drawer;
    //DrawerLayout
    DrawerLayout drawer;
    //RelativeLayout
    RelativeLayout rl_send_sms,leftfgf_drawer,rl_profile;
    //ProgressDialog
    ProgressDialog progressDialog;

    ContentDisplaceDrawerToggle mContentDisplaceToggle;
    HeaderControler header;
    DataFetchService dft;
    LoginDetails login_details;
    AnnouncementAdapter announcement_adpater;

    String[] data_without_sibling;
    int[] icons_without_sibling;
    int notificationID = 1;
    String usl_id,clt_id,board_name,admin_name,school_name,version_name,org_id,announcement,academic_year,msg_id,old_announcement;
    String AdminAnnouncementMethodName = "GetTeacherAnnouncement";
    String addAnnouncementMethod_name = "AddAdminAnnoucement";
    String AdminEditAnnouncementMethodName = "UpdateAdminAnnoucement";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.admin_announcment);
        findViews();
        init();
        getIntentData();
        setDrawer();
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        AppController.announcementActivity = "true";
        try {
            //Call Webservice for Teacher Announcment
            CallAdminAnnouncementWebservice(clt_id, usl_id);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setDrawer() {
        icons_without_sibling = new int[]{R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sms_48x48, R.drawable.announcementicon_48x48,/* R.drawable.att_48x48, R.drawable.behaviouricon_48x48, R.drawable.subjectlist_48x48,*/ R.drawable.setting1_hdpi, R.drawable.logout1_hdpi, R.drawable.info};
        data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement", /*"Attendance", "Behaviour", "Subject List",*/ "Setting", "Sign Out", "About"};
        drawerListView.setAdapter(new CustomAccount(AdminAnnouncmentActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));

        drawerListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (dft.isInternetOn() == true) {
                        Intent adminProfileIntent = new Intent(AdminAnnouncmentActivity.this, AdminProfileActivity.class);
                        adminProfileIntent.putExtra("clt_id", clt_id);
                        adminProfileIntent.putExtra("usl_id", usl_id);
                        adminProfileIntent.putExtra("board_name", board_name);
                        adminProfileIntent.putExtra("Admin_name", admin_name);
                        adminProfileIntent.putExtra("School_name", school_name);
                        adminProfileIntent.putExtra("org_id", org_id);
                        adminProfileIntent.putExtra("academic_year", academic_year);
                        adminProfileIntent.putExtra("version_name", AppController.versionName);
                        adminProfileIntent.putExtra("verion_code", AppController.versionCode);
                        startActivity(adminProfileIntent);
                    }
                } else if (position == 1) {
                    //Message
                    if (dft.isInternetOn() == true) {
                        Intent adminMessageIntent = new Intent(AdminAnnouncmentActivity.this, AdminMessageActivity.class);
                        adminMessageIntent.putExtra("clt_id", clt_id);
                        adminMessageIntent.putExtra("usl_id", usl_id);
                        adminMessageIntent.putExtra("board_name", board_name);
                        adminMessageIntent.putExtra("Admin_name", admin_name);
                        adminMessageIntent.putExtra("School_name", school_name);
                        adminMessageIntent.putExtra("org_id", org_id);
                        adminMessageIntent.putExtra("academic_year", academic_year);
                        adminMessageIntent.putExtra("version_name", AppController.versionName);
                        adminMessageIntent.putExtra("verion_code", AppController.versionCode);
                        startActivity(adminMessageIntent);
                    }
                } else if (position == 2) {
                    //SMS
                    if (dft.isInternetOn() == true) {
                        Intent adminSMSIntent = new Intent(AdminAnnouncmentActivity.this, AdminSMSActivity.class);
                        adminSMSIntent.putExtra("clt_id", clt_id);
                        adminSMSIntent.putExtra("usl_id", usl_id);
                        adminSMSIntent.putExtra("board_name", board_name);
                        adminSMSIntent.putExtra("Admin_name", admin_name);
                        adminSMSIntent.putExtra("School_name", school_name);
                        adminSMSIntent.putExtra("org_id", org_id);
                        adminSMSIntent.putExtra("academic_year", academic_year);
                        adminSMSIntent.putExtra("version_name", AppController.versionName);
                        adminSMSIntent.putExtra("verion_code", AppController.versionCode);
                        startActivity(adminSMSIntent);
                    }
                } else if (position == 3) {
                    //Announcement
                    if (dft.isInternetOn() == true) {
                        Intent adminAnnouncementIntent = new Intent(AdminAnnouncmentActivity.this, AdminAnnouncmentActivity.class);
                        adminAnnouncementIntent.putExtra("clt_id", clt_id);
                        adminAnnouncementIntent.putExtra("usl_id", usl_id);
                        adminAnnouncementIntent.putExtra("board_name", board_name);
                        adminAnnouncementIntent.putExtra("Admin_name", admin_name);
                        adminAnnouncementIntent.putExtra("School_name", school_name);
                        adminAnnouncementIntent.putExtra("org_id", org_id);
                        adminAnnouncementIntent.putExtra("academic_year", academic_year);
                        adminAnnouncementIntent.putExtra("version_name", AppController.versionName);
                        adminAnnouncementIntent.putExtra("verion_code", AppController.versionCode);
                        startActivity(adminAnnouncementIntent);
                    }
                } else if (position == 4) {
                        //Setting
                        if (dft.isInternetOn() == true) {
                            Intent adminsettingIntent = new Intent(AdminAnnouncmentActivity.this, SettingActivity.class);
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
                    Intent signout = new Intent(AdminAnnouncmentActivity.this, LoginActivity.class);
                    Constant.SetLoginData("", "", AdminAnnouncmentActivity.this);
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
                        final Dialog alertDialog = new Dialog(AdminAnnouncmentActivity.this);
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
        Constant.setAdminDrawerData(academic_year, admin_name, AdminAnnouncmentActivity.this);
        setDrawerData();
    }

    private void setDrawerData() {
        tv_child_name_drawer.setText(admin_name);
        tv_academic_year_drawer.setText(academic_year);
    }

    private void getIntentData() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        dft= new DataFetchService(this);
        login_details = new LoginDetails();
        header = new HeaderControler(this, true, false, "Announcements");
        img_drawer.setOnClickListener(this);
        lay_back_investment.setOnClickListener(this);
        ll_add_announcement.setOnClickListener(this);
        lv_admin_announcements.setOnItemClickListener(this);
        progressDialog = Constant.getProgressDialog(this);
        tv_academic_year_drawer.setVisibility(View.GONE);

    }

    private void findViews() {
        //TextView
        tv_child_name_drawer = (TextView) findViewById(R.id.tv_child_name_drawer);
        tv_no_record  =(TextView) findViewById(R.id.tv_no_record);
        //Linear Layout
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
        ll_add_announcement = (LinearLayout) findViewById(R.id.ll_add_announcement);
        tv_child_name_drawer = (TextView) findViewById(R.id.tv_child_name_drawer);
        tv_academic_year_drawer = (TextView) findViewById(R.id.tv_academic_year_drawer);
        //ImageView
        img_drawer = (ImageView) findViewById(R.id.img_drawer);

        //ListView
        lv_admin_announcements = (ListView) findViewById(R.id.lv_admin_announcements);
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

    @Override
    public void onClick(View v) {
        if(v==lay_back_investment){
            Intent back = new Intent(AdminAnnouncmentActivity.this,AdminProfileActivity.class);
            back.putExtra("clt_id", AppController.clt_id);
            back.putExtra("usl_id",AppController.usl_id);
            back.putExtra("board_name",board_name);
            back.putExtra("Admin_name", admin_name);
            back.putExtra("School_name", school_name);
            back.putExtra("org_id",org_id);
            back.putExtra("academic_year",academic_year);
            back.putExtra("version_name",AppController.versionName);
            back.putExtra("verion_code", AppController.versionCode);
            startActivity(back);
        }
        else if(v==img_drawer){
            drawer.openDrawer(Gravity.RIGHT);
        }
        else if(v==ll_add_announcement){
            try {
                if(!drawer.isDrawerOpen(Gravity.RIGHT)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    final Dialog alertDialog = new Dialog(AdminAnnouncmentActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    alertDialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(Color.TRANSPARENT));
                    View convertView = inflater.inflate(R.layout.add_an_announcement, null);
                    alertDialog.setContentView(convertView);
                    //Button
                    btn_cancel = (Button) convertView.findViewById(R.id.btn_cancel);
                    btn_update = (Button) convertView.findViewById(R.id.btn_update);
                    ed_enter_announcement = (EditText) convertView.findViewById(R.id.ed_enter_announcement);
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                    btn_update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            announcement = ed_enter_announcement.getText().toString();
                            if (announcement.equalsIgnoreCase("")) {
                                Constant.showOkPopup(AdminAnnouncmentActivity.this, "Announcement can not be blank", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        dialogInterface.dismiss();
                                    }
                                });
                            } else {
                                //callWsTo update an announcement
                                callWSToAddAnAnnouncement(clt_id, usl_id, announcement);
                                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                alertDialog.dismiss();
                            }
                        }
                    });
                    alertDialog.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void callWSToAddAnAnnouncement(final String clt_id, final String usl_id,String announcement) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getaddannouncement(clt_id, usl_id, announcement, addAnnouncementMethod_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);

                            if (login_details.Status.equalsIgnoreCase("1")) {
                                try {
                                    //Call Webservice for Teacher Announcment
                                    CallAdminAnnouncementWebservice(clt_id, usl_id);
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }

                            } else {

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

    private void CallAdminAnnouncementWebservice(String clt_id, final String usl_id) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getTeacherannaouncement(clt_id, usl_id, AdminAnnouncementMethodName, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        //  try {
                        login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                        if (login_details.Status.equalsIgnoreCase("1")) {

                            tv_no_record.setVisibility(View.GONE);
                            lv_admin_announcements.setVisibility(View.VISIBLE);
                            setUIData();
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        } else if (login_details.Status.equalsIgnoreCase("0")) {

                            tv_no_record.setText("No Records Found");
                            tv_no_record.setVisibility(View.VISIBLE);
                            lv_admin_announcements.setVisibility(View.GONE);
                        }
                    } /*catch (Exception e) {
                            e.printStackTrace();
                        }*/


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Show error or whatever...
                        Log.d("TeacherAnnouncActivity", "ERROR.._---" + error.getCause());


                    }
                });

    }

    private void setUIData() {
        announcement_adpater = new AnnouncementAdapter(AdminAnnouncmentActivity.this,login_details.AnnouncementResult);
        lv_admin_announcements.setAdapter(announcement_adpater);
        AppController.AnnouncementResult = login_details.AnnouncementResult;
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent back = new Intent(AdminAnnouncmentActivity.this,AdminProfileActivity.class);
        back.putExtra("clt_id", AppController.clt_id);
        back.putExtra("usl_id",AppController.usl_id);
        back.putExtra("board_name",board_name);
        back.putExtra("Admin_name", admin_name);
        back.putExtra("School_name", school_name);
        back.putExtra("org_id",org_id);
        back.putExtra("version_name",AppController.versionName);
        back.putExtra("verion_code", AppController.versionCode);
        back.putExtra("academic_year", academic_year);
        startActivity(back);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        msg_id = AppController.AnnouncementResult.get(position).msg_ID;
        old_announcement = AppController.AnnouncementResult.get(position).msg_Message;
        final Dialog alertDialog = new Dialog(AdminAnnouncmentActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        View convertView = inflater.inflate(R.layout.add_an_announcement, null);
        alertDialog.setContentView(convertView);
        dft = new DataFetchService(this);
        progressDialog = Constant.getProgressDialog(this);
        //Button
        btn_cancel = (Button) convertView.findViewById(R.id.btn_cancel);
        btn_update = (Button) convertView.findViewById(R.id.btn_update);
        tv_announcement_title = (TextView) convertView.findViewById(R.id.tv_announcement_title);
        btn_update.setText("Update");
        tv_announcement_title.setText("Update Announcement");
        ed_enter_announcement = (EditText) convertView.findViewById(R.id.ed_enter_announcement);
        ed_enter_announcement.setText(old_announcement);

        alertDialog.show();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                announcement = ed_enter_announcement.getText().toString();
                //call ws to edit announcement
                callWSToEditAnnouncement(msg_id, announcement);
                alertDialog.dismiss();
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
    }
    private void callWSToEditAnnouncement(String msg_id, final String announcment) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getTeacherEditannaouncement(msg_id, announcment, AdminEditAnnouncementMethodName, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_details.Status.equalsIgnoreCase("1")) {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();

                                }
                                Constant.showOkPopup(AdminAnnouncmentActivity.this,login_details.StatusMsg, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //Call Webservice for Teacher Announcment
                                        CallAdminAnnouncementWebservice(clt_id, usl_id);
                                        dialogInterface.dismiss();
                                    }
                                });
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
                        Log.d("TeacherAnnouncActivity", "ERROR.._---" + error.getCause());
                    }
                });
    }
}
