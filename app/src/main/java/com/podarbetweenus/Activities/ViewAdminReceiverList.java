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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.Adapter.ReceiverAdapter;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

import org.json.JSONObject;

/**
 * Created by Gayatri on 4/30/2016.
 */
public class ViewAdminReceiverList extends Activity implements View.OnClickListener{
    //UI Variables
    //ListView
    ListView lv_view_receiver_list,drawerListView;
    //ProgressDialog
    ProgressDialog progressDialog;
    //Linear Layout
    LinearLayout lay_back_investment,lL_drawer;
    //Relative Layout
    RelativeLayout leftfgf_drawer,rl_profile;
    //TextView
    TextView tv_child_name_drawer,tv_academic_year_drawer,tv_version_name,tv_version_code,tv_cancel,tv_standard_value,tv_section_value;
    //ImageView
    ImageView img_drawer;
    //DrawerLayout
    DrawerLayout drawer;

    DataFetchService dft;
    LoginDetails login_details;
    ReceiverAdapter receiverAdapter;
    ContentDisplaceDrawerToggle mContentDisplaceToggle;
    HeaderControler header;

    String[] data_without_sibling;
    int[] icons_without_sibling;
    int notificationID = 1;
    String clt_id,usl_id,admin_name,board_name,roll_id,org_id,academic_year,school_name,version_name,pmg_id;
    String ShowReceiverListMethod_name = "AdminSentMsgReciverList";
    String ShowTeacherReceiverListMethod_name = "TeacherSentMsgReciverList";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.viewreciverlist);
        findViews();
        init();
        getIntentId();
        setDrawer();
        roll_id = LoginActivity.get_RollId(this);
        if(AppController.TeacherTabPosition==2){
            //call ws to view receiverList
            callTeacherWsToViewReceiver(pmg_id, clt_id, roll_id);
        }
        else if(AppController.AdminTabPosition ==2){
            //call ws to view receiverList
            callWsToViewReceiver(pmg_id, clt_id, roll_id);
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
        board_name = intent.getStringExtra("board_name");
        pmg_id = intent.getStringExtra("pmg_id");
    }

    private void init() {
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        progressDialog = Constant.getProgressDialog(this);
        lay_back_investment.setOnClickListener(this);
        img_drawer.setOnClickListener(this);
        header = new HeaderControler(this, true, false, "Receiver List");
        tv_academic_year_drawer.setVisibility(View.GONE);
    }

    private void findViews() {
        //ListView
        lv_view_receiver_list = (ListView) findViewById(R.id.lv_view_receiver_list);
        //Linear Layout
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
        lL_drawer = (LinearLayout) findViewById(R.id.lL_drawer);
        tv_child_name_drawer = (TextView) findViewById(R.id.tv_child_name_drawer);
        tv_academic_year_drawer = (TextView) findViewById(R.id.tv_academic_year_drawer);
        tv_standard_value = (TextView) findViewById(R.id.tv_standard_value);
        tv_section_value = (TextView) findViewById(R.id.tv_section_value);
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

    private void setDrawer() {
        icons_without_sibling = new int[]{R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sms_48x48, R.drawable.announcementicon_48x48, /*R.drawable.att_48x48, R.drawable.behaviouricon_48x48, R.drawable.subjectlist_48x48,*/ R.drawable.setting1_hdpi, R.drawable.logout1_hdpi, R.drawable.info};
        data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement",/* "Attendance", "Behaviour", "Subject List",*/ "Setting", "Sign Out", "About"};
        drawerListView.setAdapter(new CustomAccount(ViewAdminReceiverList.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));

        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (dft.isInternetOn() == true) {
                        Intent back = new Intent(ViewAdminReceiverList.this, AdminProfileActivity.class);
                        back.putExtra("clt_id", clt_id);
                        back.putExtra("usl_id", usl_id);
                        back.putExtra("board_name", board_name);
                        back.putExtra("Admin_name", admin_name);
                        back.putExtra("School_name", school_name);
                        back.putExtra("org_id", org_id);
                        back.putExtra("academic_year",academic_year);
                        back.putExtra("version_name", AppController.versionName);
                        back.putExtra("verion_code", AppController.versionCode);
                        startActivity(back);
                    }
                } else if (position == 1) {
                    //Message
                    if (dft.isInternetOn() == true) {
                        Intent back = new Intent(ViewAdminReceiverList.this, AdminMessageActivity.class);
                        back.putExtra("clt_id", clt_id);
                        back.putExtra("usl_id", usl_id);
                        back.putExtra("board_name", board_name);
                        back.putExtra("Admin_name", admin_name);
                        back.putExtra("School_name", school_name);
                        back.putExtra("org_id", org_id);
                        back.putExtra("academic_year",academic_year);
                        back.putExtra("version_name", AppController.versionName);
                        back.putExtra("verion_code", AppController.versionCode);
                        startActivity(back);
                    }
                } else if (position == 2) {
                    if (dft.isInternetOn() == true) {
                        Intent back = new Intent(ViewAdminReceiverList.this, AdminSMSActivity.class);
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
                        Intent back = new Intent(ViewAdminReceiverList.this, AdminAnnouncmentActivity.class);
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
                } else if(position == 4){
                    //Setting
                    if (dft.isInternetOn() == true) {
                        Intent adminsettingIntent = new Intent(ViewAdminReceiverList.this, SettingActivity.class);
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
                    Intent signout = new Intent(ViewAdminReceiverList.this, LoginActivity.class);
                    Constant.SetLoginData("", "", ViewAdminReceiverList.this);
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
                        final Dialog alertDialog = new Dialog(ViewAdminReceiverList.this);
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
        Constant.setAdminDrawerData(academic_year, admin_name, ViewAdminReceiverList.this);
        setDrawerData();
    }

    private void setDrawerData() {
        tv_child_name_drawer.setText(admin_name);
        tv_academic_year_drawer.setText(academic_year);
    }
    private void callTeacherWsToViewReceiver(String pmg_id,String clt_id,String roll_id) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getReceiverList(pmg_id, clt_id, roll_id, ShowTeacherReceiverListMethod_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);

                            if (login_details.Status.equalsIgnoreCase("1")) {

                                lv_view_receiver_list.setVisibility(View.VISIBLE);
                                Log.e("Array Size",String.valueOf(login_details.MsgReciverResult.size()));
                                for(int i = 0;i< login_details.MsgReciverResult.size();i++){

                                    Log.e("Inside","Inside");
                                    if(login_details.MsgReciverResult.get(i).sft_name.equalsIgnoreCase("") && login_details.MsgReciverResult.get(i).std_Name.equalsIgnoreCase("") && login_details.MsgReciverResult.get(i).div_name.equalsIgnoreCase("")){
                                        tv_standard_value.setVisibility(View.GONE);

                                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                                0,
                                                LinearLayout.LayoutParams.MATCH_PARENT, 3.5f);
                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        params.setMargins(2,0,0,0);
                                        //tv_section_value.setLayoutParams(params);
                                        //tv_section_value.setLayoutParams(param);

                                    }
                                    else{
                                        tv_standard_value.setVisibility(View.VISIBLE);

                                    }

                                }
                                setUIForList();

                            } else {
                                lv_view_receiver_list.setVisibility(View.GONE);
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

    private void setUIForList() {
        receiverAdapter = new ReceiverAdapter(ViewAdminReceiverList.this,login_details.MsgReciverResult);
        lv_view_receiver_list.setAdapter(receiverAdapter);
    }
    private void callWsToViewReceiver(String pmg_id,String clt_id,String roll_id) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getReceiverList(pmg_id, clt_id, roll_id, ShowReceiverListMethod_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);

                            if (login_details.Status.equalsIgnoreCase("1")) {

                                lv_view_receiver_list.setVisibility(View.VISIBLE);
                                Log.e("Array Size",String.valueOf(login_details.MsgReciverResult.size()));
                                for(int i = 0;i< login_details.MsgReciverResult.size();i++){

                                    Log.e("Inside","Inside");
                                    if(login_details.MsgReciverResult.get(i).sft_name.equalsIgnoreCase("") && login_details.MsgReciverResult.get(i).std_Name.equalsIgnoreCase("") && login_details.MsgReciverResult.get(i).div_name.equalsIgnoreCase("")){
                                        tv_standard_value.setVisibility(View.GONE);

                                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                                0,
                                                LinearLayout.LayoutParams.MATCH_PARENT, 3.5f);
                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        params.setMargins(2,0,0,0);
                                        //tv_section_value.setLayoutParams(params);
                                        //tv_section_value.setLayoutParams(param);

                                    }
                                    else{
                                        tv_standard_value.setVisibility(View.VISIBLE);

                                    }

                                }
                                setUIForList();

                            } else {
                                lv_view_receiver_list.setVisibility(View.GONE);
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

    @Override
    public void onClick(View v) {
        if(v==lay_back_investment){
            finish();
        }
        else if(v==img_drawer){
            drawer.openDrawer(Gravity.RIGHT);
        }
    }
}
