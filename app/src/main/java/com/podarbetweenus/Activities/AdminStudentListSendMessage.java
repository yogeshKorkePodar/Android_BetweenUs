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
import com.podarbetweenus.Entity.MsgStudentResult;
import com.podarbetweenus.Entity.SmsStudentResult;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.academic_year;
import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.check;
import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.checkboxClicked;
import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.class_id_data;
import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.contact_no_data_withoutduplicates;
import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.selectedCheckbox;
import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.separator;
import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.stud_id;
import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.stud_id_data;
import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.stud_id_data_withoutDuplicates;

/**
 * Created by Gayatri on 4/20/2016.
 */
public class AdminStudentListSendMessage extends Activity implements DataTransferInterface,View.OnClickListener
{
    Bundle bundleInstance;
    //ListView
    ListView lv_message_student_list;
    //TextView
    TextView tv_version_name,tv_version_code,tv_cancel,tv_error_msg,tv_no_records_further,tv_child_name_drawer,
            tv_academic_year_drawer,tv_teacher_class;
    //ProgressDialog
    ProgressDialog progressDialog;
    //CheckBox
    CheckBox select_all_checkbox;
    //ImageView
    ImageView img_drawer,img_search,img_username_close;
    //EditTExt
    EditText ed_search_name;
    //Linear Layout
    LinearLayout lay_back_investment,lL_drawer;
    //Relative Layout
    RelativeLayout leftfgf_drawer,rl_profile,rl_send_sms;
    DrawerLayout drawer;
    //ListView
    ListView drawerListView;

    DataFetchService dft;
    LoginDetails login_details;
    ShowAdminMessageStudentListAdapter showAdminMessageStudentListAdapter;
    HeaderControler header;
    ContentDisplaceDrawerToggle mContentDisplaceToggle;

    String clt_id,usl_id,msd_id,board_name,school_name,teacher_name,version_name,class_id,studIdColanseperated="",stud_id,clas_teacher,academic_year,
            selectAll = "false",studentName="",checkboxUnchecked = "",selectAllClick="",org_id,checkboxClicked="false",
            teacher_div,teacher_std,teacher_shift,admin_name,std,shift,div,section;
    String showStudents_Method_name = "GetMessageStudentList";
    String separator = ",";
    boolean selectAllCheckboxStatus,check,status;
    boolean[] selectedpos;
    boolean checkboxStatus;
    int pageIndex=1,notificationID=1;
    int pageSize=200;
    public ArrayList<String> contact_no_data = new ArrayList<String>();

    public static String selectedCheckbox = "";
    public static ArrayList<String> stud_id_data = new ArrayList<String>();
    public static Set<String> stud_id_data_withoutDuplicates = new HashSet<>();
    public static Set<String> cls_id_data_withoutDuplicates = new HashSet<>();

    String[] data_without_sibling;
    int[] icons_without_sibling;

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("<<onResume","onResume");
        selectedCheckbox = "";
        stud_id_data = new ArrayList<String>();
        stud_id_data_withoutDuplicates = new HashSet<>();
        cls_id_data_withoutDuplicates = new HashSet<>();
        try {
            //  call ws for student selection
            callWebserviceForShowStudentList(clt_id, class_id, studentName, pageIndex, pageSize);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        //setUIData();
        //showAdminMessageStudentListAdapter.notifyDataSetChanged();
        /*selectedCheckbox = "";
        stud_id_data = new ArrayList<String>();
        stud_id_data_withoutDuplicates = new HashSet<>();
        cls_id_data_withoutDuplicates = new HashSet<>();*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bundleInstance = savedInstanceState;
        selectedCheckbox = "";
        Log.d("<<Inside", "AdminStudentListSendMessage");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.teacher_studentlist_message);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        findViews();
        getIntentData();
        init();
        setDrawer();
        AppController.enterMessageBack = "false";
        lv_message_student_list.setDivider(null);
        lv_message_student_list.setDividerHeight(0);

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
            //  call ws for student selection
            callWebserviceForShowStudentList(clt_id, class_id, studentName, pageIndex, pageSize);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void init() {
    if(AppController.showAllStudents.equalsIgnoreCase("false")) {
        header = new HeaderControler(this, true, false, std + "-" + div + " >> " + shift + " >> " + section);
    }
    else if(AppController.showAllStudents.equalsIgnoreCase("true")){
        header = new HeaderControler(this, true, false, "Student List");
    }
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        progressDialog = Constant.getProgressDialog(this);
        select_all_checkbox.setOnClickListener(this);
        rl_send_sms.setOnClickListener(this);
        lay_back_investment.setOnClickListener(this);
        img_search.setOnClickListener(this);
        img_drawer.setOnClickListener(this);
        img_username_close.setOnClickListener(this);
        lL_drawer.setOnClickListener(this);
        tv_academic_year_drawer.setVisibility(View.GONE);
    }
    private void getIntentData() {
        try {
            Intent intent = getIntent();
            usl_id = intent.getStringExtra("usl_id");
            clt_id = intent.getStringExtra("clt_id");
            school_name = intent.getStringExtra("School_name");
            admin_name = intent.getStringExtra("Admin_name");
            version_name = intent.getStringExtra("version_name");
            board_name = intent.getStringExtra("board_name");
            class_id = intent.getStringExtra("class_id");
            org_id = intent.getStringExtra("org_id");
            academic_year = intent.getStringExtra("academic_year");
            std = intent.getStringExtra("std");
            section = intent.getStringExtra("section");
            shift = intent.getStringExtra("shift");
            div = intent.getStringExtra("div");
            AppController.versionName = version_name;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void findViews() {
        tv_no_records_further = (TextView) findViewById(R.id.tv_no_records_further);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_version_code = (TextView) findViewById(R.id.tv_version_code);
        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
        tv_teacher_class = (TextView) findViewById(R.id.tv_teacher_class);
        tv_child_name_drawer = (TextView) findViewById(R.id.tv_child_name_drawer);
        tv_academic_year_drawer = (TextView) findViewById(R.id.tv_academic_year_drawer);
        lv_message_student_list = (ListView) findViewById(R.id.lv_message_student_list);
        select_all_checkbox = (CheckBox) findViewById(R.id.select_all_checkbox);
        lay_back_investment  = (LinearLayout) findViewById(R.id.lay_back_investment);
        lL_drawer = (LinearLayout) findViewById(R.id.lL_drawer);
        rl_send_sms = (RelativeLayout) findViewById(R.id.rl_send_sms);
        ed_search_name = (EditText) findViewById(R.id.ed_search_name);
        img_username_close = (ImageView) findViewById(R.id.img_username_close);
        // for drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        leftfgf_drawer = (RelativeLayout) findViewById(R.id.leftfgf_drawer);
        rl_profile = (RelativeLayout) findViewById(R.id.rl_profile);
        drawerListView = (ListView) findViewById(R.id.drawerListView);
        img_drawer = (ImageView) findViewById(R.id.img_drawer);
        img_search = (ImageView) findViewById(R.id.img_search);
        drawerListView = (ListView) findViewById(R.id.drawerListView);
        mContentDisplaceToggle = new ContentDisplaceDrawerToggle(this, drawer,
                R.id.rl_profile, Gravity.START);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //  int width = dm.widthPixels;
        int width = getResources().getDisplayMetrics().widthPixels/2+30;
        DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) leftfgf_drawer.getLayoutParams();
        lp.width = width + 30;
        leftfgf_drawer.setLayoutParams(lp);
    }
    private void setDrawer() {
        icons_without_sibling = new int[]{R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sms_48x48, R.drawable.announcementicon_48x48, /*R.drawable.att_48x48, R.drawable.behaviouricon_48x48, R.drawable.subjectlist_48x48,*/ R.drawable.setting1_hdpi, R.drawable.logout1_hdpi, R.drawable.info};
        data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement",/* "Attendance", "Behaviour", "Subject List",*/ "Setting", "Sign Out", "About"};
        drawerListView.setAdapter(new CustomAccount(AdminStudentListSendMessage.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));

        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (dft.isInternetOn() == true) {
                        Intent back = new Intent(AdminStudentListSendMessage.this, AdminProfileActivity.class);
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
                        Intent back = new Intent(AdminStudentListSendMessage.this, AdminMessageActivity.class);
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
                    Intent back = new Intent(AdminStudentListSendMessage.this, AdminSMSActivity.class);
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
                } else if (position == 3) {
                    Intent back = new Intent(AdminStudentListSendMessage.this, AdminAnnouncmentActivity.class);
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
                } else if(position == 4){
                    //Setting
                    if (dft.isInternetOn() == true) {
                        Intent adminsettingIntent = new Intent(AdminStudentListSendMessage.this, SettingActivity.class);
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
                    Intent signout = new Intent(AdminStudentListSendMessage.this, LoginActivity.class);
                    Constant.SetLoginData("", "", AdminStudentListSendMessage.this);
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
                        final Dialog alertDialog = new Dialog(AdminStudentListSendMessage.this);
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
        Constant.setAdminDrawerData(academic_year, admin_name, AdminStudentListSendMessage.this);
        setDrawerData();
    }

    private void setDrawerData() {
        tv_child_name_drawer.setText(admin_name);
        tv_academic_year_drawer.setText(academic_year);
    }
    @Override
    public void onClick(View v) {
        Log.e("Coming","Coming");

        select_all_checkbox = (CheckBox) findViewById(R.id.select_all_checkbox);
        if(v == select_all_checkbox){
            try {
                Log.d("<<select_all_checkbox","Clicked");
                Log.e("Coming1","Coming1");
                isSelectallCheckboxchecked();
                status = select_all_checkbox.isChecked();
                if (selectAllCheckboxStatus == false) {
                    Log.d("<<select_all_checkbox","Unchecked all");
                    selectAll = "false";
                    stud_id_data.clear();
                    class_id_data.clear();
                    stud_id_data_withoutDuplicates.clear();
                    cls_id_data_withoutDuplicates.clear();
                    selectedCheckbox = "";
                    showAdminMessageStudentListAdapter = new ShowAdminMessageStudentListAdapter(AdminStudentListSendMessage.this, this, login_details.MsgStudentResult, selectAllCheckboxStatus, selectAll, clt_id, usl_id, school_name, admin_name, version_name, board_name);
                    lv_message_student_list.setAdapter(showAdminMessageStudentListAdapter);
                    for (int i = 0; i < login_details.MsgStudentResult.size(); i++) {
                        login_details.MsgStudentResult.get(i).setChecked(false);
                        boolean check = login_details.MsgStudentResult.get(i).isChecked();
                        Log.e("CHECK Act", String.valueOf(check));
                    }

                    showAdminMessageStudentListAdapter.notifyDataSetChanged();
                } else {
                    Log.d("<<select_all_checkbox","checked all");
                    selectAll = "true";
                    checkboxClicked = "false";
                    showAdminMessageStudentListAdapter = new ShowAdminMessageStudentListAdapter(AdminStudentListSendMessage.this, this, login_details.MsgStudentResult, selectAllCheckboxStatus, selectAll, clt_id, usl_id, school_name, admin_name, version_name, board_name);
                    lv_message_student_list.setAdapter(showAdminMessageStudentListAdapter);
                    for (int i = 0; i < login_details.MsgStudentResult.size(); i++) {
                        login_details.MsgStudentResult.get(i).setChecked(true);
                        boolean check = login_details.MsgStudentResult.get(i).isChecked();
                        Log.e("CHECK Act", String.valueOf(check));
                        stud_id = login_details.MsgStudentResult.get(i).stu_ID;
                        //   class_id = login_details.SmsStudentResult.get(i).cls_ID;
                        stud_id_data.add(stud_id);
                        //    class_id_data.add(class_id);
                        //    cls_id_data_withoutDuplicates.addAll(class_id_data);
                        stud_id_data_withoutDuplicates.addAll(stud_id_data);
                        Log.e("SIZE STU", String.valueOf(stud_id_data_withoutDuplicates.size()));
                        Log.e("SIZE cls", String.valueOf(cls_id_data_withoutDuplicates.size()));
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
                        Log.e("Stude ID seperated", selectedCheckbox);
                    }
                    showAdminMessageStudentListAdapter.notifyDataSetChanged();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(v==rl_send_sms)
        {
            Log.d("<< Send button","Called!!!");
            if(!drawer.isDrawerOpen(Gravity.RIGHT)) {
                try {
                    AppController.AdminDropResult.clear();
                    // adminStudentListSMSAdapter.notifyDataSetChanged();
                    if (selectAllCheckboxStatus == false && stud_id_data_withoutDuplicates.size() == 0) {
                        Constant.showOkPopup(this, "Please Select Student", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }

                        });
                    }
                    else  if(selectAllCheckboxStatus == true && stud_id_data_withoutDuplicates.size() == 0){
                        Constant.showOkPopup(this, "Please Select Student", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }

                        });
                    }
                    else {
                        if (dft.isInternetOn() == true) {
                            Intent sendsms = new Intent(AdminStudentListSendMessage.this, AdminSendMessageActivity.class);
                            //  stud_id_data_withoutDuplicates.clear();
                            //cls_id_data_withoutDuplicates.clear();
                            if (selectAllCheckboxStatus == true && check == false && checkboxClicked.equalsIgnoreCase("true")) {
                                stud_id = selectedCheckbox;
                                //  class_id = selectedcheckboxClassId;
                            } else if (selectAllCheckboxStatus == true && check == true && checkboxClicked.equalsIgnoreCase("true")) {
                                stud_id = selectedCheckbox;
                                //   class_id = selectedcheckboxClassId;

                            } else if (selectAllCheckboxStatus == true && check == false) {
                                stud_id = selectedCheckbox;
                                //    class_id = selectedcheckboxClassId;
                            } else if (selectAllCheckboxStatus == true) {
                                stud_id = "0";
                                //  class_id = selectedcheckboxClassId;
                            } else {
                                stud_id = selectedCheckbox;
                                //     class_id = selectedcheckboxClassId;
                            }
                            AppController.AdminsendMessageToStudent = "true";
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
                            Log.e("send stuid", stud_id);
                            Log.e("send clsid", class_id);
                            startActivity(sendsms);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                //  call ws for student selection
                callWebserviceForShowStudentList(clt_id, class_id, studentName, pageIndex, pageSize);
                showAdminMessageStudentListAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else  if(v==lay_back_investment){
            Intent back = new Intent(AdminStudentListSendMessage.this,AdminMessageActivity.class);
            AppController.AdminDropResult.clear();
            AppController.AdminWriteMessage = "true";
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
        else if(v==img_drawer){
            drawer.openDrawer(Gravity.RIGHT);
        }
        else if(v==img_username_close){
            ed_search_name.setText("");

            select_all_checkbox.setClickable(true);
            try {
                pageIndex = 1;
                pageSize = 200;
                stud_id_data_withoutDuplicates.clear();
                contact_no_data_withoutduplicates.clear();
                cls_id_data_withoutDuplicates.clear();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                studentName = ed_search_name.getText().toString();
                //  call ws for student selection
                callWebserviceForShowStudentList(clt_id, class_id, studentName, pageIndex, pageSize);
                showAdminMessageStudentListAdapter.notifyDataSetChanged();
                img_username_close.setVisibility(View.INVISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(v==lL_drawer){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(ed_search_name.getWindowToken(), 0);
        }
    }

    private void isSelectallCheckboxchecked() {

        selectAllCheckboxStatus = select_all_checkbox.isChecked();
        Log.e("checked", String.valueOf(selectAllCheckboxStatus));
    }
    private void callWebserviceForShowStudentList(String clt_id,String class_id,String studentName,int pageIndex,int pageSize) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getTeacherSMSStudentList(clt_id, class_id, studentName, pageIndex, pageSize, showStudents_Method_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_details.Status.equalsIgnoreCase("1")) {
                                lv_message_student_list.setVisibility(View.VISIBLE);
                                tv_no_records_further.setVisibility(View.GONE);
                                setUIData();
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(ed_search_name.getWindowToken(), 0);

                            } else if (login_details.Status.equalsIgnoreCase("0")) {
                                select_all_checkbox.setChecked(false);
                                select_all_checkbox.setClickable(false);
                                lv_message_student_list.setVisibility(View.GONE);
                                tv_no_records_further.setVisibility(View.VISIBLE);
                                tv_no_records_further.setText("No Records Found");

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Show error or whatever...
                        Log.d("TeacherStudentsrMessage", "ERROR.._---" + error.getCause());
                    }
                });
    }

    private void setUIData() {
        Log.d("<<setUIData", "Called");
        showAdminMessageStudentListAdapter = new ShowAdminMessageStudentListAdapter(AdminStudentListSendMessage.this,this,login_details.MsgStudentResult,selectAllCheckboxStatus,selectAll,clt_id,usl_id,school_name,teacher_name,version_name,board_name);
        lv_message_student_list.setAdapter(showAdminMessageStudentListAdapter);
    }

    @Override
    public void setValues(ArrayList<String> studData, ArrayList<String> contactData) {

    }

    @Override
    public void getValues(ArrayList<String> getStudData, ArrayList<String> contactData) {

    }

    public class ShowAdminMessageStudentListAdapter extends BaseAdapter {
        LayoutInflater layoutInflater;
        Context context;
        DataTransferInterface dtInterface;
        String selectAll,stud_id,contact_number,class_id,clt_id,usl_id,school_name,teacher_name,version_name,board_name;
        String checkboxClick = "";
        boolean[] mCheckedState;
        int newPosition;
        boolean[] mSelectAllCheckedState;
        public ArrayList<MsgStudentResult> MsgStudentResult = new ArrayList<MsgStudentResult>();
        //   public static ArrayList<String> stud_id_data = new ArrayList<String>();
        //   public static ArrayList<String> contact_no_data = new ArrayList<String>();
        boolean selectAllCheckboxStatus;
        //   public static SharedPreferences resultpreLoginData;
        public ShowAdminMessageStudentListAdapter(Context context,DataTransferInterface dataTransferInterface, ArrayList<MsgStudentResult> MsgStudentResult,boolean selectAllCheckboxStatus,String selectAll,String clt_id,String usl_id,String school_name,String teacher_name,String version_name,String board_name) {
            this.context = context;
            this.MsgStudentResult = MsgStudentResult;
            this.selectAllCheckboxStatus = selectAllCheckboxStatus;
            this.selectAll = selectAll;
            this.clt_id = clt_id;
            this.usl_id = usl_id;
            this.dtInterface = dataTransferInterface;
            this.school_name = school_name;
            this.teacher_name = teacher_name;
            this.version_name = version_name;
            this.board_name = board_name;
            mCheckedState = new boolean[MsgStudentResult.size()];
            mSelectAllCheckedState = new boolean[MsgStudentResult.size()];
            this.layoutInflater = layoutInflater.from(this.context);
            layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return MsgStudentResult.size();
        }

        @Override
        public Object getItem(int position) {
            return MsgStudentResult.get(position);
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
                holder = new ViewHolder();
                showLeaveStatus = layoutInflater.inflate(R.layout.studentlistmessage_item,null);
                holder.tv_student_name = (TextView) showLeaveStatus.findViewById(R.id.tv_student_name);
                holder.tv_roll_no_value = (TextView) showLeaveStatus.findViewById(R.id.tv_roll_no_value);
                // holder.tv_reg_no = (TextView)showLeaveStatus.findViewById(R.id.tv_reg_no);
                holder.checkbox = (CheckBox) showLeaveStatus.findViewById(R.id.select_checkbox);
                holder.ll_main = (LinearLayout) showLeaveStatus.findViewById(R.id.ll_main);
                holder.ll_checkbox = (LinearLayout) showLeaveStatus.findViewById(R.id.ll_checkbox);
                holder.ll_roll_no = (LinearLayout) showLeaveStatus.findViewById(R.id.ll_roll_no);

            } else
            {
                holder = (ViewHolder) showLeaveStatus.getTag();
            }
            showLeaveStatus.setTag(holder);

            holder.tv_roll_no_value.setText(MsgStudentResult.get(position).Roll_No);
            holder.tv_student_name.setText(MsgStudentResult.get(position).Student_Name);

            if(position % 2 ==0){
                holder.ll_roll_no.setBackgroundResource(R.color.message_listview_even_row);
                holder.ll_main.setBackgroundColor(Color.parseColor("#04314a"));
                holder.ll_checkbox.setBackgroundResource(R.color.message_listview_even_row);
                holder.tv_student_name.setBackgroundResource(R.color.message_listview_even_row);
            }
            else {
                holder.ll_main.setBackgroundColor(Color.parseColor("#04314a"));
                holder.ll_roll_no.setBackgroundResource(R.color.message_listview_odd_row);
                holder.tv_student_name.setBackgroundResource(R.color.message_listview_odd_row);
                holder.ll_checkbox.setBackgroundResource(R.color.message_listview_odd_row);
            }
            if(selectAll.equalsIgnoreCase("true"))
            {
                holder.checkbox.setChecked(true);
                mCheckedState[position] = true;
            }
            else{
                holder.checkbox.setChecked(false);
            }

            checkboxStatus = holder.checkbox.isChecked();
            final ViewHolder finalHolder = holder;
            finalHolder.checkbox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        String checkfor = MsgStudentResult.get(position).stu_ID;

                        Log.d("<<ddChecking for",checkfor);
                        Log.d("<<selectedCheckbox",selectedCheckbox);

                        if(selectedCheckbox.contains(checkfor)==true){
                            Log.d("<<Before removing",selectedCheckbox);
                            stud_id_data.remove(checkfor);
                            stud_id_data_withoutDuplicates.remove(checkfor);
                            mCheckedState[position] = false;
                            MsgStudentResult.get(position).setChecked(false);
                            selectedCheckbox = android.text.TextUtils.join(",", stud_id_data_withoutDuplicates);
                            Log.d("<<After removing",selectedCheckbox);
                        }
                        if (((CheckBox) v).isChecked()) {

                             if (selectAll.equalsIgnoreCase("true")) {

                                checkboxClicked = "true";
                                if (finalHolder.checkbox.isChecked() == true) {
                                    stud_id_data_withoutDuplicates.add(MsgStudentResult.get(position).stu_ID);
                                } else {
                                    stud_id_data_withoutDuplicates.remove(MsgStudentResult.get(position).stu_ID);
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
                                Log.d("<<point 1", selectedCheckbox);
                            }
                            else {
                                Log.d("<<single box ", "checked");
                                mCheckedState[position] = true;
                                checkboxClick = "true";
                                MsgStudentResult.get(position).setChecked(true);
                                check = MsgStudentResult.get(position).isChecked();
                                Log.d("<<CHECK", String.valueOf(check));
                                checkboxStatus = finalHolder.checkbox.isChecked();
                                stud_id = MsgStudentResult.get(position).stu_ID;
                                stud_id_data.add(stud_id);
                                stud_id_data_withoutDuplicates.addAll(stud_id_data);
                                String separator = ",";
                                int total = stud_id_data_withoutDuplicates.size() * separator.length();
                                for (String s : stud_id_data_withoutDuplicates) {
                                    total += s.length();
                                }
                                StringBuilder rString = new StringBuilder(total);
                                for (String s : stud_id_data_withoutDuplicates) {
                                    rString.append(separator).append(s);
                                }
                                selectedCheckbox = rString.substring(separator.length());
                                Log.d("<<point 2", selectedCheckbox);
                                // finalHolder.checkbox.setTag(position);
                            }
                        } else {
                            if (selectAll.equalsIgnoreCase("true")) {
                                checkboxClicked = "true";
                                if (finalHolder.checkbox.isChecked() == true) {
                                    stud_id_data_withoutDuplicates.add(MsgStudentResult.get(position).stu_ID);
                                } else {
                                    stud_id_data_withoutDuplicates.remove(MsgStudentResult.get(position).stu_ID);
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
                                Log.d("<<point 3", selectedCheckbox);
                            } else if(selectAll.equalsIgnoreCase("true")){
                                MsgStudentResult.get(position).setChecked(false);
                                check = MsgStudentResult.get(position).isChecked();
                                Log.e("CHECK", String.valueOf(check));
                                mCheckedState[position] = false;
                                checkboxClick = "true";
                                newPosition = position;
                                if (finalHolder.checkbox.isChecked() == true) {
                                    stud_id_data_withoutDuplicates.add(MsgStudentResult.get(position).stu_ID);
                                } else {
                                    stud_id_data_withoutDuplicates.remove(MsgStudentResult.get(position).stu_ID);
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
                                Log.d("<<point 4", selectedCheckbox);
                            }
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }

            });
            finalHolder.checkbox.setChecked(MsgStudentResult.get(position).isChecked());
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
      //  super.onBackPressed();
        Intent back = new Intent(AdminStudentListSendMessage.this,AdminMessageActivity.class);
        AppController.AdminDropResult.clear();
        AppController.AdminWriteMessage = "true";
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
}
