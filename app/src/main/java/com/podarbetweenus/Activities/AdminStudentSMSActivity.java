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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
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
import com.podarbetweenus.Entity.SmsStudentResult;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.*;

/**
 * Created by Gayatri on 3/30/2016.
 */
public class AdminStudentSMSActivity extends Activity implements DataTransferInterface,View.OnClickListener{
    //Progress Dialog
    ProgressDialog progressDialog;
    //EditText
    EditText ed_search_name;
    //ImageView
    ImageView img_search,img_drawer,img_username_close;
    //Relative Layout
    RelativeLayout rl_send_sms,leftfgf_drawer,rl_profile;
    //TextView
    TextView tv_no_records_further,tv_child_name_drawer,tv_academic_year_drawer,tv_version_name,tv_version_code,tv_cancel;
    //ListView
    ListView lv_sms_srudent_list,drawerListView;
    //Linear Layout
    LinearLayout lay_back_investment,lL_drawer;
    //checkbox
    CheckBox select_all_checkbox;
    //DrawerLayout
    DrawerLayout drawer;

    String usl_id,clt_id,version_name,board_name,class_id,cls_id,admin_name,school_name,org_id,studentName="",selectAll="false",
            std,section,shift,div;
    String SMSStudentListMethod_name = "GetTeacherSMSStudentList";
    int pageIndex = 1;
    int pageSize = 200;
    String[] data_without_sibling;
    int[] icons_without_sibling;
    int notificationID = 1;
    boolean selectAllCheckboxStatus,status;
    public static Set<String> cls_id_data_withoutDuplicates = new HashSet<>();

    DataFetchService dft;
    HeaderControler header;
    LoginDetails login_details;
    ContentDisplaceDrawerToggle mContentDisplaceToggle;
    AdminStudentListSMSAdapter adminStudentListSMSAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        stud_id_data.clear();
        stud_id_data_withoutDuplicates.clear();
        selectedCheckbox = "";

        callStudentsSMSListWebservice(clt_id, class_id, studentName, pageIndex, pageSize);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("<< Inside","AdminStudentSMSActivity");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.teacher_sms_students_list);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getIntentData();
        findViews();
        init();
        setDrawer();
      //  cls_id_data_withoutDuplicates.clear();
      //  class_id_data.clear();
        stud_id_data.clear();
        stud_id_data_withoutDuplicates.clear();
        ed_search_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                img_username_close.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        try {
            //Call webservice for StudentList
            callStudentsSMSListWebservice(clt_id, class_id, studentName, pageIndex, pageSize);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setDrawer() {
        icons_without_sibling = new int[]{R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sms_48x48, R.drawable.announcementicon_48x48,/* R.drawable.att_48x48, R.drawable.behaviouricon_48x48, R.drawable.subjectlist_48x48,*/ R.drawable.setting1_hdpi, R.drawable.logout1_hdpi, R.drawable.info};
        data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement", /*"Attendance", "Behaviour", "Subject List",*/ "Setting", "Sign Out", "About"};
        drawerListView.setAdapter(new CustomAccount(AdminStudentSMSActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));

        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (dft.isInternetOn() == true) {
                        Intent adminProfileActivity = new Intent(AdminStudentSMSActivity.this, AdminProfileActivity.class);
                        adminProfileActivity.putExtra("clt_id", clt_id);
                        adminProfileActivity.putExtra("usl_id",usl_id);
                        adminProfileActivity.putExtra("board_name",board_name);
                        adminProfileActivity.putExtra("Admin_name", admin_name);
                        adminProfileActivity.putExtra("School_name", school_name);
                        adminProfileActivity.putExtra("org_id",org_id);
                        adminProfileActivity.putExtra("academic_year", academic_year);
                        adminProfileActivity.putExtra("version_name",AppController.versionName);
                        adminProfileActivity.putExtra("verion_code", AppController.versionCode);
                        startActivity(adminProfileActivity);
                    }
                } else if (position == 1) {
                    //Message
                    if (dft.isInternetOn() == true) {
                        Intent adminMessageActivity = new Intent(AdminStudentSMSActivity.this, AdminProfileActivity.class);
                        adminMessageActivity.putExtra("clt_id", clt_id);
                        adminMessageActivity.putExtra("usl_id",usl_id);
                        adminMessageActivity.putExtra("board_name",board_name);
                        adminMessageActivity.putExtra("Admin_name", admin_name);
                        adminMessageActivity.putExtra("School_name", school_name);
                        adminMessageActivity.putExtra("org_id",org_id);
                        adminMessageActivity.putExtra("academic_year",academic_year);
                        adminMessageActivity.putExtra("version_name",AppController.versionName);
                        adminMessageActivity.putExtra("verion_code", AppController.versionCode);
                        startActivity(adminMessageActivity);
                    }
                }
                else if(position == 2){
                    //SMS
                    if (dft.isInternetOn() == true) {
                        Intent adminSMSIntent = new Intent(AdminStudentSMSActivity.this, AdminSMSActivity.class);
                        adminSMSIntent.putExtra("clt_id", clt_id);
                        adminSMSIntent.putExtra("usl_id",usl_id);
                        adminSMSIntent.putExtra("board_name",board_name);
                        adminSMSIntent.putExtra("Admin_name", admin_name);
                        adminSMSIntent.putExtra("School_name", school_name);
                        adminSMSIntent.putExtra("org_id",org_id);
                        adminSMSIntent.putExtra("academic_year",academic_year);
                        adminSMSIntent.putExtra("version_name",AppController.versionName);
                        adminSMSIntent.putExtra("verion_code", AppController.versionCode);
                        startActivity(adminSMSIntent);
                    }
                }
                else if(position == 3){
                    //Announcement
                    if (dft.isInternetOn() == true) {
                        Intent adminAnnouncementIntent = new Intent(AdminStudentSMSActivity.this, AdminAnnouncmentActivity.class);
                        adminAnnouncementIntent.putExtra("clt_id", clt_id);
                        adminAnnouncementIntent.putExtra("usl_id",usl_id);
                        adminAnnouncementIntent.putExtra("board_name",board_name);
                        adminAnnouncementIntent.putExtra("Admin_name", admin_name);
                        adminAnnouncementIntent.putExtra("School_name", school_name);
                        adminAnnouncementIntent.putExtra("org_id",org_id);
                        adminAnnouncementIntent.putExtra("academic_year",academic_year);
                        adminAnnouncementIntent.putExtra("version_name",AppController.versionName);
                        adminAnnouncementIntent.putExtra("verion_code", AppController.versionCode);
                        startActivity(adminAnnouncementIntent);
                    }
                }
                else if(position == 4){
                    //Setting
                    if (dft.isInternetOn() == true) {
                        Intent adminsettingIntent = new Intent(AdminStudentSMSActivity.this, SettingActivity.class);
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
                    Intent signout = new Intent(AdminStudentSMSActivity.this, LoginActivity.class);
                    Constant.SetLoginData("", "", AdminStudentSMSActivity.this);
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
                        final Dialog alertDialog = new Dialog(AdminStudentSMSActivity.this);
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
        Constant.setAdminDrawerData(academic_year, admin_name, AdminStudentSMSActivity.this);
        setDrawerData();
    }

    private void setDrawerData() {
        {
            tv_child_name_drawer.setText(admin_name);
            tv_academic_year_drawer.setText(academic_year);
        }
    }

    private void getIntentData() {
        Intent intent = getIntent();
        usl_id = intent.getStringExtra("usl_id");
        clt_id = intent.getStringExtra("clt_id");
        school_name = intent.getStringExtra("School_name");
        admin_name = intent.getStringExtra("Admin_name");
        version_name = intent.getStringExtra("version_name");
        org_id = intent.getStringExtra("org_id");
        AppController.versionName = version_name;
        academic_year = intent.getStringExtra("academic_year");
        board_name = intent.getStringExtra("board_name");
        class_id = intent.getStringExtra("class_id");
        std = intent.getStringExtra("std");
        section = intent.getStringExtra("section");
        shift = intent.getStringExtra("shift");
        div = intent.getStringExtra("div");
    }

    private void init() {
        if(AppController.showAllStudents.equalsIgnoreCase("false")) {
            header = new HeaderControler(this, true, false, std + "-" + div + " >> " + shift + " >> " + section);
        }
        else if(AppController.showAllStudents.equalsIgnoreCase("true")){
            header = new HeaderControler(this, true, false, "Student List");
        }
        img_search.setOnClickListener(this);
        ed_search_name.setOnClickListener(this);
        img_drawer.setOnClickListener(this);
        progressDialog = Constant.getProgressDialog(this);
        select_all_checkbox.setOnClickListener(this);
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        lay_back_investment.setOnClickListener(this);
        rl_send_sms.setOnClickListener(this);
        lL_drawer.setOnClickListener(this);
        tv_academic_year_drawer.setVisibility(View.GONE);
        img_username_close.setOnClickListener(this);
    }

    private void findViews() {
        rl_send_sms = (RelativeLayout) findViewById(R.id.rl_send_sms);
        img_search = (ImageView) findViewById(R.id.img_search);
        img_drawer = (ImageView) findViewById(R.id.img_drawer);
        img_username_close = (ImageView) findViewById(R.id.img_username_close);
        ed_search_name = (EditText) findViewById(R.id.ed_search_name);
        tv_no_records_further = (TextView) findViewById(R.id.tv_no_records_further);
        tv_child_name_drawer = (TextView) findViewById(R.id.tv_child_name_drawer);
        tv_academic_year_drawer = (TextView) findViewById(R.id.tv_academic_year_drawer);
        lv_sms_srudent_list = (ListView) findViewById(R.id.lv_sms_srudent_list);
        lL_drawer = (LinearLayout) findViewById(R.id.lL_drawer);
        select_all_checkbox = (CheckBox) findViewById(R.id.select_all_checkbox);
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
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
    private void callStudentsSMSListWebservice(String clt_id,String class_id,String studentName,int pageIndex,int pageSize) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getTeacherSMSStudentList(clt_id, class_id, studentName, pageIndex, pageSize, SMSStudentListMethod_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);

                            if (login_details.Status.equalsIgnoreCase("1")) {
                                tv_no_records_further.setVisibility(View.GONE);
                                //   getListView().setVisibility(View.VISIBLE);
                                lv_sms_srudent_list.setVisibility(View.VISIBLE);
                                setUIDataDataForList();
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(ed_search_name.getWindowToken(), 0);
                            } else {
                                select_all_checkbox.setChecked(false);
                             //   select_all_checkbox.setClickable(false);
                                tv_no_records_further.setVisibility(View.VISIBLE);
                                tv_no_records_further.setText("No Records Found");
                                lv_sms_srudent_list.setVisibility(View.GONE);
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

    private void setUIDataDataForList() {
        adminStudentListSMSAdapter = new AdminStudentListSMSAdapter(AdminStudentSMSActivity.this,this,login_details.SmsStudentResult,selectAllCheckboxStatus,selectAll,clt_id,usl_id,school_name,admin_name,version_name,board_name);
        lv_sms_srudent_list.setAdapter(adminStudentListSMSAdapter);
    }

    @Override
    public void setValues(ArrayList<String> studData, ArrayList<String> contactData) {

    }

    @Override
    public void getValues(ArrayList<String> getStudData, ArrayList<String> contactData) {

    }

    @Override
    public void onClick(View v) {
        if(v==img_drawer){
            drawer.openDrawer(Gravity.RIGHT);
        }
        else if(v==select_all_checkbox){

            select_all_checkbox = (CheckBox) findViewById(R.id.select_all_checkbox);
            try {
                isSelectallCheckboxchecked();
                select_all_checkbox.setClickable(true);
                status = select_all_checkbox.isChecked();
                if (selectAllCheckboxStatus == false) {
                    selectedCheckbox = "";
                    selectAll = "false";
                    stud_id_data.clear();
                    class_id_data.clear();
                    stud_id_data_withoutDuplicates.clear();
                    cls_id_data_withoutDuplicates.clear();
                    adminStudentListSMSAdapter = new AdminStudentListSMSAdapter(AdminStudentSMSActivity.this, this, login_details.SmsStudentResult, selectAllCheckboxStatus, selectAll, clt_id, usl_id, school_name, admin_name, version_name, board_name);
                    lv_sms_srudent_list.setAdapter(adminStudentListSMSAdapter);
                    for (int i = 0; i < login_details.SmsStudentResult.size(); i++) {
                        login_details.SmsStudentResult.get(i).setChecked(false);
                        boolean check = login_details.SmsStudentResult.get(i).isChecked();
                        Log.e("CHECK Act", String.valueOf(check));
                    }

                    adminStudentListSMSAdapter.notifyDataSetChanged();
                } else {
                    selectAll = "true";
                    checkboxClicked = "false";
                    adminStudentListSMSAdapter = new AdminStudentListSMSAdapter(AdminStudentSMSActivity.this, this, login_details.SmsStudentResult, selectAllCheckboxStatus, selectAll, clt_id, usl_id, school_name, admin_name, version_name, board_name);
                    lv_sms_srudent_list.setAdapter(adminStudentListSMSAdapter);
                    for (int i = 0; i < login_details.SmsStudentResult.size(); i++) {
                        login_details.SmsStudentResult.get(i).setChecked(true);
                        boolean check = login_details.SmsStudentResult.get(i).isChecked();
                        Log.e("CHECK Act", String.valueOf(check));
                        stud_id = login_details.SmsStudentResult.get(i).stu_ID;
                        stud_id_data.add(stud_id);
                        stud_id_data_withoutDuplicates.addAll(stud_id_data);
                        Log.e("SIZE STU", String.valueOf(stud_id_data_withoutDuplicates.size()));
                        Log.e("SIZE cls", String.valueOf(cls_id_data_withoutDuplicates.size()));
                        //Student Id
                        int total = stud_id_data_withoutDuplicates.size() * separator.length();
                        for (String s : stud_id_data_withoutDuplicates) {
                            total += s.length();
                        }
                        StringBuilder rString = new StringBuilder(total);
                        for (String s : stud_id_data_withoutDuplicates) {
                            rString.append(separator).append(s);
                        }
                        selectedCheckbox = rString.substring(separator.length());
                        Log.e("Stude ID seperated", selectedCheckbox);
                    }
                    adminStudentListSMSAdapter.notifyDataSetChanged();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        else if(v==img_search) {
            try {
                pageIndex = 1;
                pageSize = 200;
                stud_id_data_withoutDuplicates.clear();
                contact_no_data_withoutduplicates.clear();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                studentName = ed_search_name.getText().toString();
                //Call webservice for StudentList
                callStudentsSMSListWebservice(clt_id, class_id, studentName, pageIndex, pageSize);
                adminStudentListSMSAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(v==rl_send_sms)
        {
            try{
                AppController.AdminDropResult.clear();
                if(selectAllCheckboxStatus==false && stud_id_data_withoutDuplicates.size()==0){
                    Constant.showOkPopup(this, "Please Select Student", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }

                    });
                }
                else if(stud_id_data_withoutDuplicates.size() == 0){
                    Constant.showOkPopup(this, "Please Select Student", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }

                    });
                }
                else {
                    if (dft.isInternetOn() == true) {
                        Intent sendsms = new Intent(AdminStudentSMSActivity.this, AdminSendSMSActivity.class);
                        if (selectAllCheckboxStatus == true && check == false && checkboxClicked.equalsIgnoreCase("true")) {
                            stud_id = selectedCheckbox;
                        } else if (selectAllCheckboxStatus == true && check == true && checkboxClicked.equalsIgnoreCase("true")) {
                            stud_id = selectedCheckbox;

                        } else if(selectAllCheckboxStatus == true && check==false){
                            stud_id = selectedCheckbox;
                        }
                        else if (selectAllCheckboxStatus == true) {
                            stud_id = "0";
                        } else {
                            stud_id = selectedCheckbox;
                        }
                        sendsms.putExtra("class_id", class_id);
                        sendsms.putExtra("clt_id", clt_id);
                        sendsms.putExtra("usl_id", usl_id);
                        sendsms.putExtra("School_name", school_name);
                        sendsms.putExtra("Admin_name", admin_name);
                        sendsms.putExtra("version_name", AppController.versionName);
                        sendsms.putExtra("board_name", board_name);
                        sendsms.putExtra("org_id", org_id);
                        sendsms.putExtra("academic_year", academic_year);
                        sendsms.putExtra("stud_id", stud_id);
                        Log.d("<<send stuid", stud_id);
                        Log.d("<<send clsid", class_id);
                        startActivity(sendsms);
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if(v==img_username_close){
            ed_search_name.setText("");
            try {
                pageIndex = 1;
                pageSize = 200;
                stud_id_data_withoutDuplicates.clear();
                contact_no_data_withoutduplicates.clear();
                cls_id_data_withoutDuplicates.clear();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                studentName = ed_search_name.getText().toString();
                //Call webservice for StudentList
                callStudentsSMSListWebservice(clt_id, class_id, studentName, pageIndex, pageSize);
                img_username_close.setVisibility(View.INVISIBLE);
                adminStudentListSMSAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else  if(v==lay_back_investment){
         /*   super.onBackPressed();
            AppController.AdminDropResult.clear();
            AppController.AdminstudentSMS = "true";
            AppController.teacher_sms= "false";
            finish();*/
            Intent back = new Intent(AdminStudentSMSActivity.this,AdminSMSActivity.class);
            AppController.AdminDropResult.clear();
            AppController.AdminstudentSMS = "true";
            AppController.teacher_sms= "false";
            back.putExtra("usl_id",usl_id);
            back.putExtra("clt_id", clt_id);
            back.putExtra("School_name", school_name);
            back.putExtra("Admin_name", admin_name);
            back.putExtra("org_id",org_id);
            back.putExtra("academic_year",academic_year);
            back.putExtra("version_name", AppController.versionName);
            back.putExtra("board_name", board_name);
            startActivity(back);
        }
        else if(v==lL_drawer){
            ed_search_name.setClickable(false);
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(ed_search_name.getWindowToken(), 0);
        }
    }
    private void isSelectallCheckboxchecked() {

        selectAllCheckboxStatus = select_all_checkbox.isChecked();
        Log.e("checked", String.valueOf(selectAllCheckboxStatus));
    }

    public static  class AdminStudentListSMSAdapter extends BaseAdapter {
        LayoutInflater layoutInflater;
        Context context;
        DataTransferInterface dtInterface;
        String selectAll,stud_id,contact_number,class_id,clt_id,usl_id,school_name,teacher_name,version_name,board_name;

        boolean[] mCheckedState;
        public ArrayList<com.podarbetweenus.Entity.SmsStudentResult> SmsStudentResult = new ArrayList<SmsStudentResult>();
        boolean selectAllCheckboxStatus;
        public AdminStudentListSMSAdapter(Context context,DataTransferInterface dataTransferInterface, ArrayList<SmsStudentResult> SmsStudentResult,boolean selectAllCheckboxStatus,String selectAll,String clt_id,String usl_id,String school_name,String admin_name,String version_name,String board_name) {
            this.context = context;
            this.SmsStudentResult = SmsStudentResult;
            this.selectAllCheckboxStatus = selectAllCheckboxStatus;
            this.selectAll = selectAll;
            this.clt_id = clt_id;
            this.usl_id = usl_id;
            this.dtInterface = dataTransferInterface;
            this.school_name = school_name;
            this.teacher_name = teacher_name;
            this.version_name = version_name;
            this.board_name = board_name;
            mCheckedState = new boolean[SmsStudentResult.size()];
            this.layoutInflater = layoutInflater.from(this.context);
            layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return SmsStudentResult.size();
        }

        @Override
        public Object getItem(int position) {
            return SmsStudentResult.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            View showLeaveStatus = convertView;
            if (showLeaveStatus == null)
            {
                CheckBox result = (CheckBox)showLeaveStatus;
                holder = new ViewHolder();
                showLeaveStatus = layoutInflater.inflate(R.layout.studentlist_item,null);
                holder.tv_student_name = (TextView) showLeaveStatus.findViewById(R.id.tv_student_name);
                holder.tv_roll_no_value = (TextView) showLeaveStatus.findViewById(R.id.tv_roll_no_value);
                // holder.tv_reg_no = (TextView)showLeaveStatus.findViewById(R.id.tv_reg_no);
                holder.checkbox = (CheckBox) showLeaveStatus.findViewById(R.id.select_all_checkbox);
                holder.ll_main = (LinearLayout) showLeaveStatus.findViewById(R.id.ll_main);
                holder.ll_checkbox = (LinearLayout) showLeaveStatus.findViewById(R.id.ll_checkbox);
                holder.ll_roll_no = (LinearLayout) showLeaveStatus.findViewById(R.id.ll_roll_no);
                result = new CheckBox(context);
            } else
            {
                holder = (ViewHolder) showLeaveStatus.getTag();
            }

            showLeaveStatus.setTag(holder);

            holder.tv_roll_no_value.setText(SmsStudentResult.get(position).Roll_No);
            holder.tv_student_name.setText(SmsStudentResult.get(position).Student_Name);


            if(position % 2 ==0){
                holder.ll_roll_no.setBackgroundResource(R.color.sms_listview_even_row);
                holder.ll_main.setBackgroundColor(Color.parseColor("#dbb5ab"));
                holder.ll_checkbox.setBackgroundResource(R.color.sms_listview_even_row);
                holder.tv_student_name.setBackgroundResource(R.color.sms_listview_even_row);
            }
            else {
                holder.ll_main.setBackgroundColor(Color.parseColor("#dbb5ab"));
                holder.ll_roll_no.setBackgroundResource(R.color.sms_listview_odd_row);
                holder.tv_student_name.setBackgroundResource(R.color.sms_listview_odd_row);
                holder.ll_checkbox.setBackgroundResource(R.color.sms_listview_odd_row);
            }
            if(selectAll.equalsIgnoreCase("true"))
            {
                holder.checkbox.setChecked(true);
            }
            else{
                holder.checkbox.setChecked(false);
            }
            checkboxStatus = holder.checkbox.isChecked();
            Log.e("Checkbox status", String.valueOf(checkboxStatus));
            final ViewHolder finalHolder = holder;
            finalHolder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String checkfor = SmsStudentResult.get(position).stu_ID;
                    if (selectedCheckbox.contains(checkfor)){
                        Log.d("<<Before removing", selectedCheckbox);
                        stud_id_data.remove(checkfor);
                        stud_id_data_withoutDuplicates.remove(checkfor);
                        selectedCheckbox = android.text.TextUtils.join(",", stud_id_data_withoutDuplicates);
                        Log.d("<<After removing", selectedCheckbox);

                    }
                    try {
                        if (((CheckBox) v).isChecked()) {

                            if (selectAll.equalsIgnoreCase("true")) {
                                if (finalHolder.checkbox.isChecked() == true) {
                                    SmsStudentResult.get(position).setChecked(true);
                                    check = SmsStudentResult.get(position).isChecked();
                                    Log.e("CHECK", String.valueOf(check));
                                    stud_id_data_withoutDuplicates.add(SmsStudentResult.get(position).stu_ID);
                                } else {
                                    SmsStudentResult.get(position).setChecked(false);
                                    check = SmsStudentResult.get(position).isChecked();
                                    Log.e("CHECK", String.valueOf(check));
                                    stud_id_data_withoutDuplicates.remove(SmsStudentResult.get(position).stu_ID);
                                }
                                Log.e("remove size", String.valueOf(stud_id_data_withoutDuplicates.size()));
                                int total = stud_id_data_withoutDuplicates.size() * separator.length();
                                for (String s : stud_id_data_withoutDuplicates) {
                                    total += s.length();
                                }
                                StringBuilder rString = new StringBuilder(total);
                                for (String s : stud_id_data_withoutDuplicates) {
                                    rString.append(separator).append(s);
                                }
                                selectedCheckbox = rString.substring(separator.length());
                                Log.e("remoStude ID seperated", selectedCheckbox);

                            }
                            else{
                                mCheckedState[position] = true;
                                SmsStudentResult.get(position).setChecked(true);
                                check = SmsStudentResult.get(position).isChecked();
                                Log.e("CHECK", String.valueOf(check));
                                finalHolder.checkbox.setChecked(true);
                                checkboxStatus = finalHolder.checkbox.isChecked();
                                stud_id = SmsStudentResult.get(position).stu_ID;
                                stud_id_data.add(stud_id);
                                stud_id_data_withoutDuplicates.addAll(stud_id_data);
                                //  String separator = ",";
                                //Student Id
                                int total = stud_id_data_withoutDuplicates.size() * separator.length();
                                for (String s : stud_id_data_withoutDuplicates) {
                                    total += s.length();
                                }
                                StringBuilder rString = new StringBuilder(total);
                                for (String s : stud_id_data_withoutDuplicates) {
                                    rString.append(separator).append(s);
                                }
                                selectedCheckbox = rString.substring(separator.length());
                                Log.e(" un allStude ID check", selectedCheckbox);
                            }
                        }
                        else if (!((CheckBox) v).isChecked()) {
                            if (selectAll.equalsIgnoreCase("true")) {
                                if (finalHolder.checkbox.isChecked() == true) {
                                    SmsStudentResult.get(position).setChecked(true);
                                    check = SmsStudentResult.get(position).isChecked();
                                    Log.e("CHECK", String.valueOf(check));
                                    stud_id_data_withoutDuplicates.add(SmsStudentResult.get(position).stu_ID);
                                } else {
                                    SmsStudentResult.get(position).setChecked(false);
                                    check = SmsStudentResult.get(position).isChecked();
                                    Log.e("CHECK", String.valueOf(check));
                                    stud_id_data_withoutDuplicates.remove(SmsStudentResult.get(position).stu_ID);
                                }
                                Log.e("remove size unc", String.valueOf(stud_id_data_withoutDuplicates.size()));
                                int total = stud_id_data_withoutDuplicates.size() * separator.length();
                                for (String s : stud_id_data_withoutDuplicates) {
                                    total += s.length();
                                }
                                StringBuilder rString = new StringBuilder(total);
                                for (String s : stud_id_data_withoutDuplicates) {
                                    rString.append(separator).append(s);
                                }
                                selectedCheckbox = rString.substring(separator.length());
                                Log.e("unremoStudeID seperated", selectedCheckbox);

                            }
                            else{
                                if (finalHolder.checkbox.isChecked() == true) {
                                    SmsStudentResult.get(position).setChecked(true);
                                    check = SmsStudentResult.get(position).isChecked();
                                    Log.e("CHECK", String.valueOf(check));
                                    stud_id_data_withoutDuplicates.add(SmsStudentResult.get(position).stu_ID);
                                } else {
                                    SmsStudentResult.get(position).setChecked(false);
                                    check = SmsStudentResult.get(position).isChecked();
                                    Log.e("CHECK", String.valueOf(check));
                                    stud_id_data_withoutDuplicates.remove(SmsStudentResult.get(position).stu_ID);
                                }
                                Log.e("remove size unc", String.valueOf(stud_id_data_withoutDuplicates.size()));
                                int total = stud_id_data_withoutDuplicates.size() * separator.length();
                                for (String s : stud_id_data_withoutDuplicates) {
                                    total += s.length();
                                }
                                StringBuilder rString = new StringBuilder(total);
                                for (String s : stud_id_data_withoutDuplicates) {
                                    rString.append(separator).append(s);
                                }
                                selectedCheckbox = rString.substring(separator.length());
                                Log.e("unremoStudeID seperated", selectedCheckbox);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });
            finalHolder.checkbox.setChecked(SmsStudentResult.get(position).isChecked());
            return showLeaveStatus;
        }
    }
    public static class ViewHolder
    {
        TextView tv_roll_no_value,tv_student_name,tv_reg_no;
        CheckBox checkbox;
        LinearLayout ll_main,ll_checkbox,ll_roll_no;
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
       /* super.onBackPressed();
        AppController.AdminDropResult.clear();
        AppController.AdminstudentSMS = "true";
        AppController.teacher_sms= "false";

        AppController.AdminschoolSMS = "false";
        finish();*/


        Intent back = new Intent(AdminStudentSMSActivity.this,AdminSMSActivity.class);
        AppController.AdminDropResult.clear();
        AppController.AdminstudentSMS = "true";
        AppController.teacher_sms= "false";
        AppController.AdminschoolSMS = "false";
        back.putExtra("usl_id", usl_id);
        back.putExtra("clt_id", clt_id);
        back.putExtra("School_name", school_name);
        back.putExtra("Admin_name", admin_name);
        back.putExtra("org_id",org_id);
        back.putExtra("academic_year",academic_year);
        back.putExtra("version_name", AppController.versionName);
        back.putExtra("board_name", board_name);
        startActivity(back);
        finish();
                
    }
}
