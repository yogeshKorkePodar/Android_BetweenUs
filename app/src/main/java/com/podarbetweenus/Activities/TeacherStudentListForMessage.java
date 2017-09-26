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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.Adapter.TeacherEnterMessageAdapter;
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

/**
 * Created by Gayatri on 2/22/2016.
 */
public class TeacherStudentListForMessage extends Activity implements DataTransferInterface,AdapterView.OnItemClickListener,View.OnClickListener{

    //ListView to display list of student to whom teacher can send message
    ListView lv_message_student_list;
    //TextView
    TextView tv_version_name,tv_version_code,tv_cancel,tv_error_msg,tv_no_records_further,tv_child_name_drawer,tv_academic_year_drawer,tv_teacher_class;
    //ProgressDialog
    ProgressDialog progressDialog;
    //CheckBox
    CheckBox select_all_checkbox;
    //ImageView
    ImageView img_drawer,img_search,img_username_close;
    //EditTExt
    EditText ed_search_name;
    //Linear Layout
    LinearLayout lay_back_investment;
    //Relative Layout
    RelativeLayout leftfgf_drawer,rl_profile,rl_send_sms;
    DrawerLayout drawer;
    //ListView
    ListView drawerListView;

    DataFetchService dft;
    LoginDetails login_details;
    ShowMessageStudentListAdapter showMessageStudentListAdapter;
    HeaderControler header;
    ContentDisplaceDrawerToggle mContentDisplaceToggle;

    String clt_id,usl_id,msd_id,board_name,school_name,teacher_name,version_name,class_id,studIdColanseperated="",stud_id,clas_teacher,academic_year,
            selectAll = "false",studentName="",checkboxUnchecked = "",selectAllClick="",org_id,checkboxClicked="false",teacher_div,teacher_std,teacher_shift;
    public static String selectedCheckbox = "";
    String showStudents_Method_name = "GetMessageStudentList";
    String separator = ",";
    boolean selectAllCheckboxStatus,check;
    boolean[] selectedpos;
    boolean checkboxStatus;
    int pageIndex=1,notificationID=1;
    int pageSize=200;
    public static ArrayList<String> stud_id_data = new ArrayList<String>();
    public ArrayList<String> contact_no_data = new ArrayList<String>();
    public static Set<String> stud_id_data_withoutDuplicates = new HashSet<>();
    String[] data_without_sibling;
    int[] icons_without_sibling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.teacher_studentlist_message);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        findViews();
        init();
        getIntentData();
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
    private void getIntentData() {
        Intent intent = getIntent();
        usl_id = intent.getStringExtra("usl_id");
        clt_id = intent.getStringExtra("clt_id");
        school_name = intent.getStringExtra("School_name");
        teacher_name = intent.getStringExtra("Teacher_name");
        version_name = intent.getStringExtra("version_name");
        board_name = intent.getStringExtra("board_name");
        class_id = intent.getStringExtra("class_id");
        org_id = intent.getStringExtra("org_id");
        clas_teacher = intent.getStringExtra("cls_teacher");
        teacher_div = intent.getStringExtra("Teacher_div");
        teacher_std = intent.getStringExtra("Tecaher_Std");
        teacher_shift = intent.getStringExtra("Teacher_Shift");
        academic_year = intent.getStringExtra("academic_year");
        Log.e("cls_id", class_id);
        AppController.versionName = version_name;
    }
    private void init() {
        header = new HeaderControler(this, true, false, "Students List");
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        progressDialog = Constant.getProgressDialog(this);
        lv_message_student_list.setOnItemClickListener(this);
        select_all_checkbox.setOnClickListener(this);
        rl_send_sms.setOnClickListener(this);
        lay_back_investment.setOnClickListener(this);
        img_search.setOnClickListener(this);
        img_drawer.setOnClickListener(this);
        img_username_close.setOnClickListener(this);
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
        try {
            if(LoginActivity.get_class_teacher(TeacherStudentListForMessage.this).equalsIgnoreCase("1")) {
                icons_without_sibling = new int[]{R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sms_48x48, R.drawable.announcementicon_48x48, R.drawable.att_48x48, R.drawable.behaviouricon_48x48,R.drawable.subjectlist_48x48,R.drawable.setting1_hdpi, R.drawable.logout1_hdpi, R.drawable.info};
                data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement", "Attendance", "Behaviour","Subject List","Setting", "Sign Out", "About"};
                drawerListView.setAdapter(new CustomAccount(TeacherStudentListForMessage.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));
                drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherStudentListForMessage.this, TeacherProfileActivity.class);
                                teacherProfile.putExtra("clt_id", clt_id);
                                teacherProfile.putExtra("usl_id", usl_id);
                                teacherProfile.putExtra("School_name", school_name);
                                teacherProfile.putExtra("Teacher_name", teacher_name);
                                teacherProfile.putExtra("org_id", org_id);
                                teacherProfile.putExtra("cls_teacher", clas_teacher);
                                teacherProfile.putExtra("academic_year", academic_year);
                                teacherProfile.putExtra("Teacher_div",teacher_div);
                                teacherProfile.putExtra("Teacher_Shift", teacher_shift);
                                teacherProfile.putExtra("Tecaher_Std",teacher_std);
                                teacherProfile.putExtra("version_name", AppController.versionName);
                                teacherProfile.putExtra("board_name", board_name);
                                startActivity(teacherProfile);
                            }
                        } else if (position == 1) {
                            //Message
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherStudentListForMessage.this, TeacherMessageActivity.class);
                                teacherProfile.putExtra("clt_id", clt_id);
                                teacherProfile.putExtra("usl_id", usl_id);
                                teacherProfile.putExtra("School_name", school_name);
                                teacherProfile.putExtra("Teacher_name", teacher_name);
                                teacherProfile.putExtra("org_id", org_id);
                                teacherProfile.putExtra("Teacher_div",teacher_div);
                                teacherProfile.putExtra("Teacher_Shift",teacher_shift);
                                teacherProfile.putExtra("Tecaher_Std",teacher_std);
                                teacherProfile.putExtra("cls_teacher", clas_teacher);
                                teacherProfile.putExtra("academic_year", academic_year);
                                teacherProfile.putExtra("version_name", AppController.versionName);
                                teacherProfile.putExtra("board_name", board_name);
                                startActivity(teacherProfile);
                            }
                        } else if (position == 2) {
                            //SMS
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherStudentListForMessage.this, TeacherSMSActivity.class);
                                teacherProfile.putExtra("clt_id", clt_id);
                                teacherProfile.putExtra("usl_id", usl_id);
                                teacherProfile.putExtra("School_name", school_name);
                                teacherProfile.putExtra("Teacher_name", teacher_name);
                                teacherProfile.putExtra("org_id", org_id);
                                teacherProfile.putExtra("Teacher_div",teacher_div);
                                teacherProfile.putExtra("Teacher_Shift",teacher_shift);
                                teacherProfile.putExtra("Tecaher_Std",teacher_std);
                                teacherProfile.putExtra("cls_teacher", clas_teacher);
                                teacherProfile.putExtra("academic_year", academic_year);
                                teacherProfile.putExtra("version_name", AppController.versionName);
                                teacherProfile.putExtra("board_name", board_name);
                                startActivity(teacherProfile);
                            }
                        } else if (position == 3) {
                            //announcement
                            if (dft.isInternetOn() == true) {
                                Intent teacherannouncment = new Intent(TeacherStudentListForMessage.this, TeacherAnnouncementsActivity.class);
                                teacherannouncment.putExtra("clt_id", clt_id);
                                teacherannouncment.putExtra("usl_id", usl_id);
                                teacherannouncment.putExtra("School_name", school_name);
                                teacherannouncment.putExtra("Teacher_name", teacher_name);
                                teacherannouncment.putExtra("version_name", AppController.versionName);
                                teacherannouncment.putExtra("board_name", board_name);
                                teacherannouncment.putExtra("cls_teacher", clas_teacher);
                                teacherannouncment.putExtra("academic_year", academic_year);
                                teacherannouncment.putExtra("Teacher_div",teacher_div);
                                teacherannouncment.putExtra("Teacher_Shift",teacher_shift);
                                teacherannouncment.putExtra("Tecaher_Std",teacher_std);
                                teacherannouncment.putExtra("org_id", org_id);
                                startActivity(teacherannouncment);
                            }
                        } else if (position == 4) {
                            //Attendance
                            if (dft.isInternetOn() == true) {
                                Intent teacherattendance = new Intent(TeacherStudentListForMessage.this, TeacherAttendanceActivity.class);
                                teacherattendance.putExtra("clt_id", clt_id);
                                teacherattendance.putExtra("usl_id", usl_id);
                                teacherattendance.putExtra("School_name", school_name);
                                teacherattendance.putExtra("Teacher_name", teacher_name);
                                teacherattendance.putExtra("version_name", AppController.versionName);
                                teacherattendance.putExtra("board_name", board_name);
                                teacherattendance.putExtra("cls_teacher", clas_teacher);
                                teacherattendance.putExtra("academic_year", academic_year);
                                teacherattendance.putExtra("org_id", org_id);
                                teacherattendance.putExtra("Teacher_div",teacher_div);
                                teacherattendance.putExtra("Teacher_Shift",teacher_shift);
                                teacherattendance.putExtra("Tecaher_Std",teacher_std);
                                startActivity(teacherattendance);
                            }
                        } else if (position == 5) {
                            //Behaviour
                            if (dft.isInternetOn() == true) {
                                Intent teacher_behaviour = new Intent(TeacherStudentListForMessage.this, TeacherBehaviourActivity.class);
                                teacher_behaviour.putExtra("clt_id", clt_id);
                                teacher_behaviour.putExtra("usl_id", usl_id);
                                teacher_behaviour.putExtra("School_name", school_name);
                                teacher_behaviour.putExtra("Teacher_name", teacher_name);
                                teacher_behaviour.putExtra("version_name", AppController.versionName);
                                teacher_behaviour.putExtra("board_name", board_name);
                                teacher_behaviour.putExtra("org_id", org_id);
                                teacher_behaviour.putExtra("Teacher_div",teacher_div);
                                teacher_behaviour.putExtra("Teacher_Shift",teacher_shift);
                                teacher_behaviour.putExtra("Tecaher_Std",teacher_std);
                                teacher_behaviour.putExtra("cls_teacher", clas_teacher);
                                teacher_behaviour.putExtra("academic_year", academic_year);
                                startActivity(teacher_behaviour);
                            }
                        } else if (position == 6) {
                            //Sbject List
                            if (dft.isInternetOn() == true) {
                                Intent subject_list = new Intent(TeacherStudentListForMessage.this, SubjectListActivity.class);
                                subject_list.putExtra("usl_id", usl_id);
                                subject_list.putExtra("version_name", AppController.versionName);
                                subject_list.putExtra("clt_id", clt_id);
                                subject_list.putExtra("School_name", school_name);
                                subject_list.putExtra("Teacher_name", teacher_name);
                                subject_list.putExtra("board_name", board_name);
                                subject_list.putExtra("org_id", org_id);
                                subject_list.putExtra("Teacher_div",teacher_div);
                                subject_list.putExtra("Teacher_Shift",teacher_shift);
                                subject_list.putExtra("Tecaher_Std",teacher_std);
                                subject_list.putExtra("cls_teacher", clas_teacher);
                                subject_list.putExtra("academic_year", academic_year);
                                startActivity(subject_list);
                            }
                        } else if (position == 7) {
                            Intent setting = new Intent(TeacherStudentListForMessage.this, SettingActivity.class);
                            setting.putExtra("usl_id", usl_id);
                            setting.putExtra("version_name", AppController.versionName);
                            setting.putExtra("clt_id", clt_id);
                            setting.putExtra("School_name", school_name);
                            setting.putExtra("Teacher_name", teacher_name);
                            setting.putExtra("board_name", board_name);
                            setting.putExtra("org_id", org_id);
                            setting.putExtra("Teacher_div",teacher_div);
                            setting.putExtra("Teacher_Shift",teacher_shift);
                            setting.putExtra("Tecaher_Std",teacher_std);
                            setting.putExtra("cls_teacher", clas_teacher);
                            setting.putExtra("academic_year", academic_year);
                            startActivity(setting);
                        } else if (position == 8) {
                            //Signout
                            Intent signout = new Intent(TeacherStudentListForMessage.this, LoginActivity.class);
                            Constant.SetLoginData("", "", TeacherStudentListForMessage.this);
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancel(notificationID);
                            AppController.iconCount = 0;
                            AppController.iconCountOnResume = 0;
                            AppController.iconAnnouncementCount = 0;
                            AppController.iconAnnouncementCountOnResume = 0;
                            AppController.OnBackpressed = "false";
                            AppController.loginButtonClicked = "false";
                            AppController.drawerSignOut = "true";
                            AppController.announcementActivity = "false";
                            startActivity(signout);
                        } else if (position == 9) {
                            try {
                                final Dialog alertDialog = new Dialog(TeacherStudentListForMessage.this);
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
            }
            else {
                icons_without_sibling = new int[]{R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sms_48x48, R.drawable.announcementicon_48x48,R.drawable.subjectlist_48x48,R.drawable.setting1_hdpi, R.drawable.logout1_hdpi, R.drawable.info};
                data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement","Subject List","Setting", "Sign Out", "About"};
                drawerListView.setAdapter(new CustomAccount(TeacherStudentListForMessage.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));
                drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherStudentListForMessage.this, TeacherProfileActivity.class);
                                teacherProfile.putExtra("clt_id", clt_id);
                                teacherProfile.putExtra("usl_id", usl_id);
                                teacherProfile.putExtra("School_name", school_name);
                                teacherProfile.putExtra("Teacher_name", teacher_name);
                                teacherProfile.putExtra("version_name", version_name);
                                teacherProfile.putExtra("board_name", board_name);
                                teacherProfile.putExtra("org_id", org_id);
                                teacherProfile.putExtra("Teacher_div",teacher_div);
                                teacherProfile.putExtra("Teacher_Shift",teacher_shift);
                                teacherProfile.putExtra("Tecaher_Std",teacher_std);
                                teacherProfile.putExtra("academic_year", academic_year);
                                teacherProfile.putExtra("cls_teacher", clas_teacher);
                                startActivity(teacherProfile);
                            }
                        } else if (position == 1) {
                            //Message
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherStudentListForMessage.this, TeacherMessageActivity.class);
                                teacherProfile.putExtra("clt_id", clt_id);
                                teacherProfile.putExtra("usl_id", usl_id);
                                teacherProfile.putExtra("School_name", school_name);
                                teacherProfile.putExtra("Teacher_name", teacher_name);
                                teacherProfile.putExtra("version_name", version_name);
                                teacherProfile.putExtra("board_name", board_name);
                                teacherProfile.putExtra("org_id", org_id);
                                teacherProfile.putExtra("Teacher_div",teacher_div);
                                teacherProfile.putExtra("Teacher_Shift",teacher_shift);
                                teacherProfile.putExtra("Tecaher_Std",teacher_std);
                                teacherProfile.putExtra("academic_year", academic_year);
                                teacherProfile.putExtra("cls_teacher", clas_teacher);
                                startActivity(teacherProfile);
                            }
                        } else if (position == 2) {
                            //SMS
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherStudentListForMessage.this, TeacherSMSActivity.class);
                                teacherProfile.putExtra("clt_id", clt_id);
                                teacherProfile.putExtra("usl_id", usl_id);
                                teacherProfile.putExtra("School_name", school_name);
                                teacherProfile.putExtra("Teacher_name", teacher_name);
                                teacherProfile.putExtra("version_name", version_name);
                                teacherProfile.putExtra("board_name", board_name);
                                teacherProfile.putExtra("org_id", org_id);
                                teacherProfile.putExtra("Teacher_div",teacher_div);
                                teacherProfile.putExtra("Teacher_Shift",teacher_shift);
                                teacherProfile.putExtra("Tecaher_Std",teacher_std);
                                teacherProfile.putExtra("academic_year", academic_year);
                                teacherProfile.putExtra("cls_teacher", clas_teacher);
                                startActivity(teacherProfile);
                            }
                        } else if (position == 3) {
                            //announcement
                            if (dft.isInternetOn() == true) {
                                Intent teacherannouncment = new Intent(TeacherStudentListForMessage.this, TeacherAnnouncementsActivity.class);
                                teacherannouncment.putExtra("clt_id", clt_id);
                                teacherannouncment.putExtra("usl_id", usl_id);
                                teacherannouncment.putExtra("School_name", school_name);
                                teacherannouncment.putExtra("Teacher_name", teacher_name);
                                teacherannouncment.putExtra("version_name", version_name);
                                teacherannouncment.putExtra("board_name", board_name);
                                teacherannouncment.putExtra("org_id", org_id);
                                teacherannouncment.putExtra("Teacher_div",teacher_div);
                                teacherannouncment.putExtra("Teacher_Shift",teacher_shift);
                                teacherannouncment.putExtra("Tecaher_Std",teacher_std);
                                teacherannouncment.putExtra("academic_year", academic_year);
                                teacherannouncment.putExtra("cls_teacher", clas_teacher);
                                startActivity(teacherannouncment);
                            }
                        } else if (position == 4) {
                            //Sbject List
                            if (dft.isInternetOn() == true) {
                                Intent subject_list = new Intent(TeacherStudentListForMessage.this, SubjectListActivity.class);
                                subject_list.putExtra("usl_id", usl_id);
                                subject_list.putExtra("version_name", version_name);
                                subject_list.putExtra("clt_id", clt_id);
                                subject_list.putExtra("School_name", school_name);
                                subject_list.putExtra("Teacher_name", teacher_name);
                                subject_list.putExtra("board_name", board_name);
                                subject_list.putExtra("academic_year", academic_year);
                                subject_list.putExtra("cls_teacher", clas_teacher);
                                subject_list.putExtra("org_id", org_id);
                                subject_list.putExtra("Teacher_div",teacher_div);
                                subject_list.putExtra("Teacher_Shift",teacher_shift);
                                subject_list.putExtra("Tecaher_Std",teacher_std);
                                startActivity(subject_list);
                            }
                        } else if (position == 5) {
                            Intent setting = new Intent(TeacherStudentListForMessage.this, SettingActivity.class);
                            setting.putExtra("usl_id", usl_id);
                            setting.putExtra("version_name", version_name);
                            setting.putExtra("clt_id", clt_id);
                            setting.putExtra("School_name", school_name);
                            setting.putExtra("Teacher_name", teacher_name);
                            setting.putExtra("board_name", board_name);
                            setting.putExtra("academic_year", academic_year);
                            setting.putExtra("cls_teacher", clas_teacher);
                            setting.putExtra("org_id", org_id);
                            setting.putExtra("Teacher_div",teacher_div);
                            setting.putExtra("Teacher_Shift",teacher_shift);
                            setting.putExtra("Tecaher_Std",teacher_std);
                            startActivity(setting);
                        } else if (position == 6) {
                            //Signout
                            Intent signout = new Intent(TeacherStudentListForMessage.this, LoginActivity.class);
                            Constant.SetLoginData("", "", TeacherStudentListForMessage.this);
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancel(notificationID);
                            AppController.iconCount = 0;
                            AppController.iconCountOnResume = 0;
                            AppController.iconAnnouncementCount = 0;
                            AppController.iconAnnouncementCountOnResume = 0;
                            AppController.OnBackpressed = "false";
                            AppController.loginButtonClicked = "false";
                            AppController.drawerSignOut = "true";
                            AppController.announcementActivity = "false";
                            startActivity(signout);
                        } else if (position == 7) {
                            try {
                                final Dialog alertDialog = new Dialog(TeacherStudentListForMessage.this);
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
            }
            Constant.setTeacherDrawerData(academic_year, teacher_name,teacher_div,teacher_std,teacher_shift, TeacherStudentListForMessage.this);
            setDrawerData();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void setDrawerData() {
        tv_academic_year_drawer.setText(academic_year);
        tv_child_name_drawer.setText(teacher_name);
        if((LoginActivity.get_class_teacher(TeacherStudentListForMessage.this).equalsIgnoreCase("1")))
        {
            tv_teacher_class.setText(teacher_shift + " | " + teacher_std + " | " + teacher_div);
        }
        else{
            tv_teacher_class.setVisibility(View.GONE);
        }
    }
    @Override
    protected void onResume() {
        stud_id_data.clear();
        stud_id_data_withoutDuplicates.clear();
        selectedCheckbox = "";
        try {
            super.onResume();
            if (AppController.drawerSignOut.equalsIgnoreCase("true")) {
                Intent intent = new Intent(getApplicationContext(), SplashScreen.class);

                AppController.ProfileWithoutSibling = "false";
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                AppController.Board_name = board_name;
                AppController.school_name = school_name;
                AppController.versionName = version_name;
                AppController.usl_id = usl_id;
                AppController.clt_id = clt_id;
                AppController.school_name = school_name;
                AppController.versionName = version_name;
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

    private void callWebserviceForShowStudentList(String clt_id,String class_id,String studentName,int pageIndex,int pageSize) {
        Log.d("<<WebServiceparamaters:",clt_id+"," +class_id+","+ studentName);
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
                            Log.d("<< TeacherStuListFoMes",login_details.Status);
                            if (login_details.Status.equalsIgnoreCase("1")) {
                                lv_message_student_list.setVisibility(View.VISIBLE);
                                tv_no_records_further.setVisibility(View.GONE);
                                setUIData();
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(ed_search_name.getWindowToken(), 0);

                            } else if (login_details.Status.equalsIgnoreCase("0")) {
                                select_all_checkbox.setChecked(false);
                             //   select_all_checkbox.setClickable(false);
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
        showMessageStudentListAdapter = new ShowMessageStudentListAdapter(TeacherStudentListForMessage.this,this,login_details.MsgStudentResult,selectAllCheckboxStatus,selectAll,clt_id,usl_id,school_name,teacher_name,version_name,board_name);
        lv_message_student_list.setAdapter(showMessageStudentListAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void setValues(ArrayList<String> studData, ArrayList<String> contactData) {

    }

    @Override
    public void getValues(ArrayList<String> getStudData, ArrayList<String> contactData) {

    }

    @Override
    public void onClick(View v) {
        if(v==select_all_checkbox){
            try {

                select_all_checkbox.setClickable(true);
                isSelectallCheckboxchecked();
                if (selectAllCheckboxStatus == false) {
                    selectAll = "false";
                    selectAllClick = "false";
                    stud_id_data.clear();
                    stud_id_data_withoutDuplicates.clear();
                    showMessageStudentListAdapter = new ShowMessageStudentListAdapter(TeacherStudentListForMessage.this, this, login_details.MsgStudentResult, selectAllCheckboxStatus, selectAll, clt_id, usl_id, school_name, teacher_name, version_name, board_name);
                    lv_message_student_list.setAdapter(showMessageStudentListAdapter);
                    for (int i = 0; i < login_details.MsgStudentResult.size(); i++) {
                        login_details.MsgStudentResult.get(i).setChecked(false);
                        boolean check = login_details.MsgStudentResult.get(i).isChecked();
                        Log.e("CHECK Act", String.valueOf(check));
                    }
                    showMessageStudentListAdapter.notifyDataSetChanged();
                } else {
                    selectAll = "true";
                    pageSize = 200;
                    selectAllClick = "true";
                    checkboxClicked = "false";
                    showMessageStudentListAdapter = new ShowMessageStudentListAdapter(TeacherStudentListForMessage.this, this, login_details.MsgStudentResult, selectAllCheckboxStatus, selectAll, clt_id, usl_id, school_name, teacher_name, version_name, board_name);
                    lv_message_student_list.setAdapter(showMessageStudentListAdapter);
                    for (int i = 0; i < login_details.MsgStudentResult.size(); i++) {
                        login_details.MsgStudentResult.get(i).setChecked(true);
                        boolean check = login_details.MsgStudentResult.get(i).isChecked();
                        Log.e("CHECK Act", String.valueOf(check));
                        stud_id = login_details.MsgStudentResult.get(i).stu_ID;
                        stud_id_data.add(stud_id);
                        stud_id_data_withoutDuplicates.addAll(stud_id_data);
                        Log.e("SIZE STU", String.valueOf(stud_id_data_withoutDuplicates.size()));
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
                        Log.d("<<Student ID seperated", selectedCheckbox);
                    }
                    showMessageStudentListAdapter.notifyDataSetChanged();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        else if(v==rl_send_sms) {

            try {
              //  showMessageStudentListAdapter.notifyDataSetChanged();

                if (selectAllCheckboxStatus == false && stud_id_data_withoutDuplicates.size()==0) {
                    Constant.showOkPopup(this, "Please Select Student", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }

                    });
                }
                else if(selectAllCheckboxStatus== true && stud_id_data_withoutDuplicates.size() == 0){
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
                        Intent send_message = new Intent(TeacherStudentListForMessage.this, TeacherSendMessageActivity.class);

                        if (selectAllCheckboxStatus == true && check == false && checkboxClicked.equalsIgnoreCase("true")) {
                            stud_id = selectedCheckbox;
                            Log.d("<<SendMessageButton","case1");

                        } else if (selectAllCheckboxStatus == true && check == true && checkboxClicked.equalsIgnoreCase("true")) {
                            stud_id = selectedCheckbox;
                            Log.d("<<SendMessageButton","case2");
                        } else if (selectAllCheckboxStatus == true) {
                            //Selected all (Will not be valid for attendence)
                            stud_id = "0";
                            Log.d("<<SendMessageButton","case3");
                        } else {
                            // some selected
                            stud_id = selectedCheckbox;
                            Log.d("<<submitButtonclicked:", stud_id);
                            Log.d("<<SendMessageButton","case4");
                        }
                        send_message.putExtra("class_id", class_id);
                        send_message.putExtra("clt_id", clt_id);
                        send_message.putExtra("usl_id", usl_id);
                        send_message.putExtra("School_name", school_name);
                        send_message.putExtra("Teacher_name", teacher_name);
                        send_message.putExtra("version_name", AppController.versionName);
                        send_message.putExtra("board_name", board_name);
                        send_message.putExtra("stud_id", stud_id);
                        send_message.putExtra("org_id", org_id);
                        send_message.putExtra("cls_teacher", clas_teacher);
                        send_message.putExtra("Teacher_div",teacher_div);
                        send_message.putExtra("Teacher_Shift",teacher_shift);
                        send_message.putExtra("Tecaher_Std",teacher_std);
                        send_message.putExtra("academic_year", academic_year);
                        Log.d("<<Stud id send", stud_id);
                        startActivity(send_message);
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(v==img_search){
            try {
                pageIndex = 1;
                pageSize = 200;
                studentName = ed_search_name.getText().toString();
                stud_id_data_withoutDuplicates.clear();
                //  call ws for student selection
                callWebserviceForShowStudentList(clt_id, class_id, studentName, pageIndex, pageSize);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(v==img_username_close){
            ed_search_name.setText("");
            try {
                pageIndex = 1;
                pageSize = 200;
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                studentName = ed_search_name.getText().toString();
                //  call ws for student selection
                callWebserviceForShowStudentList(clt_id, class_id, studentName, pageIndex, pageSize);
                img_username_close.setVisibility(View.INVISIBLE);
                showMessageStudentListAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(v==lay_back_investment){
            try {
                Intent i = new Intent(TeacherStudentListForMessage.this, TeacherMessageActivity.class);
                AppController.enterMessageBack = "true";
                i.putExtra("clt_id", clt_id);
                i.putExtra("usl_id", usl_id);
                i.putExtra("School_name", school_name);
                i.putExtra("Teacher_name", teacher_name);
                i.putExtra("version_name", version_name);
                i.putExtra("board_name", board_name);
                i.putExtra("class_id", class_id);
                i.putExtra("cls_teacher", clas_teacher);
                i.putExtra("academic_year", academic_year);
                i.putExtra("org_id", org_id);
                i.putExtra("Teacher_div",teacher_div);
                i.putExtra("Teacher_Shift",teacher_shift);
                i.putExtra("Tecaher_Std",teacher_std);
                startActivity(i);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(v==img_drawer){
            drawer.openDrawer(Gravity.RIGHT);
        }
    }

    private void isSelectallCheckboxchecked() {
        selectAllCheckboxStatus = select_all_checkbox.isChecked();
        Log.e("checked", String.valueOf(selectAllCheckboxStatus));

    }

    public static class ViewHolder
    {
        TextView tv_roll_no_value,tv_student_name,tv_reg_no;
        CheckBox checkbox;
        LinearLayout ll_main,ll_checkbox,ll_roll_no;
    }

    public class ShowMessageStudentListAdapter extends BaseAdapter {
        LayoutInflater layoutInflater;
        Context context;
        DataTransferInterface dtInterface;
        String selectAll,stud_id,contact_number,class_id,clt_id,usl_id,school_name,teacher_name,version_name,board_name;
        String checkboxClick = "";
        boolean[] mCheckedState;
        int newPosition;
        boolean[] mSelectAllCheckedState;

        public ArrayList<com.podarbetweenus.Entity.MsgStudentResult> MsgStudentResult = new ArrayList<MsgStudentResult>();

        //   public static ArrayList<String> stud_id_data = new ArrayList<String>();
        //   public static ArrayList<String> contact_no_data = new ArrayList<String>();
        boolean selectAllCheckboxStatus;
        //   public static SharedPreferences resultpreLoginData;
        public ShowMessageStudentListAdapter(Context context,DataTransferInterface dataTransferInterface, ArrayList<MsgStudentResult> MsgStudentResult,boolean selectAllCheckboxStatus,String selectAll,String clt_id,String usl_id,String school_name,String teacher_name,String version_name,String board_name) {
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
            //false as default value from constructor
            if(selectAll.equalsIgnoreCase("true"))
            {   //set current row checkbox value as true
                holder.checkbox.setChecked(true);
                //set current row checkbox postion  as true in mCheckedState array
                mCheckedState[position] = true;
            }
            else{
                //set current row checkbox position as false in mCheckedState array
                holder.checkbox.setChecked(false);
            }
            // get the value of check box set from above code
            checkboxStatus = holder.checkbox.isChecked();
            // final viewholder object to create list of students from checkboxes clicked data
            final ViewHolder finalHolder = holder;

            finalHolder.checkbox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String forCheck = MsgStudentResult.get(position).stu_ID ;
                    try {
                        Log.d("<<TeacherStudentLFM", "point1");


                        if (((CheckBox) v).isChecked()) {
                                  Log.d("<<TeacherStudentLFM", "point2");

                            if (selectAll.equalsIgnoreCase("true")) {
                                Log.d("<<TeacherStudentLFM", "point3");
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
                                Log.d("<<remoStudeIDseperated", selectedCheckbox);
                            }
                            else {
                                Log.d("<<TeacherStudentLFM", "point4");
                                mCheckedState[position] = true;
                                checkboxClick = "true";
                                MsgStudentResult.get(position).setChecked(true);
                                check = MsgStudentResult.get(position).isChecked();
                                Log.d("<<CHECK", String.valueOf(check));

                                checkboxStatus = finalHolder.checkbox.isChecked();
                                Log.d("<<checkboxStatus", String.valueOf(checkboxStatus));

                                stud_id = MsgStudentResult.get(position).stu_ID;
                                stud_id_data.add(stud_id);
                                Log.d("<<Student Id",stud_id);
                                stud_id_data_withoutDuplicates.addAll(stud_id_data);

                                // No use of below line
                                contact_no_data.add(contact_number);


                                String separator = ",";
                                int total = stud_id_data_withoutDuplicates.size() * separator.length();
                                Log.d("<<total1", String.valueOf(total));
                                for (String s : stud_id_data_withoutDuplicates) {
                                    total += s.length();
                                    Log.d("<<total2", String.valueOf(total));
                                }
                                StringBuilder rString = new StringBuilder(total);
                                for (String s : stud_id_data_withoutDuplicates) {
                                    rString.append(separator).append(s);
                                    Log.d("<<rstring", rString.toString());
                                }
                                selectedCheckbox = rString.substring(separator.length());
                                Log.d("<<p4selectedCheckbox",selectedCheckbox);
                                // finalHolder.checkbox.setTag(position);
                            }
                        }
                        else {
                            Log.d("<<TeacherStudentLFM", "point5");


                            if (selectAll.equalsIgnoreCase("true")) {
                                Log.d("<<TeacherStudentLFM", "point6");
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
                                Log.e("remoStude ID seperated", selectedCheckbox);
                            } else if(selectAll.equalsIgnoreCase("true")){
                                Log.d("<<TeacherStudentLFM", "point7");
                                MsgStudentResult.get(position).setChecked(false);
                                check = MsgStudentResult.get(position).isChecked();
                                Log.e("CHECK", String.valueOf(check));
                                mCheckedState[position] = false;
                                checkboxClick = "true";
                                newPosition = position;

                                if (finalHolder.checkbox.isChecked() == true) {
                                    Log.d("<<TeacherStudentLFM", "point8");
                                    stud_id_data_withoutDuplicates.add(MsgStudentResult.get(position).stu_ID);
                                } else {
                                    Log.d("<<TeacherStudentLFM", "point9");
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
                                Log.e("remoStude ID seperated", selectedCheckbox);
                            }

                            // check if selectedCheckbox variable already contain unchecked value and if it contains then remove it
                            else if(selectedCheckbox.contains(forCheck)== true){
                                // 274953 , 274955
                                //    0   1    2
                                int indexOfValue = selectedCheckbox.indexOf(forCheck);
                                int indexOfComma = indexOfValue+1;
                                Log.d("<<Unchecked checkbox", forCheck);
                                Log.d("<<Total length",String.valueOf(selectedCheckbox.length()));
                                Log.d("<<indexOfValue ",String.valueOf(indexOfValue ));
                                Log.d("<<indexOfComma",String.valueOf(indexOfComma));
                                // Removing student id
                                boolean removed_status_stud_id_data = stud_id_data.remove(forCheck);
                                Log.d("<<removed_status_1",String.valueOf(removed_status_stud_id_data));

                                boolean removed_status_stud_id_data_withoutDuplicates = stud_id_data_withoutDuplicates.remove(forCheck);
                                Log.d("<<removed_status_1",String.valueOf(removed_status_stud_id_data_withoutDuplicates));
                                // Removing student id
                                selectedCheckbox = selectedCheckbox.replace(forCheck," ");
                                Log.d("<<After Removing value",selectedCheckbox);
                                // Removing comma id
                                selectedCheckbox = selectedCheckbox.substring(0, indexOfComma) + selectedCheckbox.substring(indexOfComma + 1);
                                Log.d("<<After Removing comma",selectedCheckbox);

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

    @Override
    public void onBackPressed() {
        try {
            Intent i = new Intent(TeacherStudentListForMessage.this, TeacherMessageActivity.class);
            AppController.enterMessageBack = "true";
            i.putExtra("clt_id", clt_id);
            i.putExtra("usl_id", usl_id);
            i.putExtra("School_name", school_name);
            i.putExtra("Teacher_name", teacher_name);
            i.putExtra("version_name", version_name);
            i.putExtra("board_name", board_name);
            i.putExtra("class_id", class_id);
            i.putExtra("cls_teacher", clas_teacher);
            i.putExtra("academic_year", academic_year);
            i.putExtra("org_id", org_id);
            i.putExtra("Teacher_div",teacher_div);
            i.putExtra("Teacher_Shift",teacher_shift);
            i.putExtra("Tecaher_Std",teacher_std);
            startActivity(i);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
