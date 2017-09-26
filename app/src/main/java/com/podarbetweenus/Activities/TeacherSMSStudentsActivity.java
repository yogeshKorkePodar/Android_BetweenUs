package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.Adapter.ShowSMSStudentListAdapter;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.Entity.SmsStudentResult;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;
import com.podarbetweenus.Utility.LoadMoreListView;

import org.apache.commons.codec.binary.StringUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Gayatri on 2/4/2016.
 */
public class TeacherSMSStudentsActivity extends Activity implements DataTransferInterface,View.OnClickListener,EditText.OnEditorActionListener,CheckBox.OnCheckedChangeListener{

    int pageIndex=1;
    int pageSize=200,listSize;
    public static  String clt_id,usl_id,board_name,school_name,teacher_name,version_name,class_id,studentName="",stud_id,
            contact_number,selectAll="false",selectedCheckboxMobileNo,org_id,checkboxClicked="false",clas_teacher,academic_year;
    String TeacherSMSStudentListMethod_name = "GetTeacherSMSStudentList";
    public String Loadmore="";
    String studIdColanseperated = "";
    static boolean checkboxStatus,check;
    boolean isLoading = false,selectAllCheckboxStatus,status ;
    static  String separator = ",";
    int notificationID = 1;
    String teacher_div,teacher_std,teacher_shift;
    //ImageView
    ImageView img_drawer,img_search,img_username_close;
    DrawerLayout drawer;
    //TextView
    TextView tv_version_name,tv_version_code,tv_cancel,tv_error_msg,tv_no_records_further,tv_child_name_drawer,tv_academic_year_drawer,tv_teacher_class;
    //Relative Layout
    RelativeLayout leftfgf_drawer,rl_profile,rl_send_sms;
    //Linear Layout
    LinearLayout lay_back_investment,lL_drawer;
    //ListView
    ListView drawerListView,lv_sms_srudent_list;
    //Edit Text
    EditText ed_search_name;
    //Checkbox
    CheckBox select_all_checkbox;

    //ProgressDialog
    ProgressDialog progressDialog;
    ContentDisplaceDrawerToggle mContentDisplaceToggle;
    HeaderControler header;
    DataFetchService dft;
    ShowSMSStudentListAdapter showSMSStudentListAdapter;
    LoginDetails login_details;

    public static ArrayList<SmsStudentResult> SmsStudentResult = new ArrayList<SmsStudentResult>();
    public static ArrayList<SmsStudentResult> SmsStudentResult_load_more = new ArrayList<SmsStudentResult>();
    public static ArrayList<String> stud_id_data = new ArrayList<String>();
    public static ArrayList<String> class_id_data = new ArrayList<String>();
    public static ArrayList<String> contact_no_data = new ArrayList<String>();
    public static Set<String> stud_id_data_withoutDuplicates = new HashSet<>();
    public static Set<String> contact_no_data_withoutduplicates = new HashSet<>();
    public static  String selectedCheckbox ="";
    public static  String selectedcheckboxClassId="";
    boolean[] mCheckedState;
    String[] data_without_sibling;
    int[] icons_without_sibling;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("<<Inside ","TeacherSMSStudentsActivity");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.teacher_sms_students_list);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        findViews();
        init();
        getIntentData();
        setDrawer();
        stud_id_data_withoutDuplicates.clear();
        stud_id_data.clear();
        class_id_data.clear();
        contact_no_data_withoutduplicates.clear();
        contact_no_data.clear();
        studentName = "";

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
            callSMSStudentsListWebservice(clt_id, class_id, studentName, pageIndex, pageSize);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void isSelectallCheckboxchecked() {

        selectAllCheckboxStatus = select_all_checkbox.isChecked();
        Log.e("checked", String.valueOf(selectAllCheckboxStatus));
    }
    private void findViews() {
        img_drawer = (ImageView) findViewById(R.id.img_drawer);
        img_search = (ImageView) findViewById(R.id.img_search);
        img_username_close = (ImageView) findViewById(R.id.img_username_close);
        //TextView
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_version_code = (TextView) findViewById(R.id.tv_version_code);
        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
        tv_error_msg = (TextView) findViewById(R.id.tv_error_msg);
        tv_no_records_further = (TextView) findViewById(R.id.tv_no_records_further);
        tv_child_name_drawer = (TextView) findViewById(R.id.tv_child_name_drawer);
        tv_academic_year_drawer = (TextView) findViewById(R.id.tv_academic_year_drawer);
        tv_teacher_class = (TextView) findViewById(R.id.tv_teacher_class);
        //EditTExt
        ed_search_name = (EditText) findViewById(R.id.ed_search_name);
        //Linear Layout
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
        lL_drawer = (LinearLayout) findViewById(R.id.lL_drawer);
        //CheckBox
        select_all_checkbox = (CheckBox) findViewById(R.id.select_all_checkbox);
        //RElative Layout
        rl_send_sms = (RelativeLayout) findViewById(R.id.rl_send_sms);
        //ListView
        lv_sms_srudent_list = (ListView) findViewById(R.id.lv_sms_srudent_list);
        // for drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        leftfgf_drawer = (RelativeLayout) findViewById(R.id.leftfgf_drawer);
        rl_profile = (RelativeLayout) findViewById(R.id.rl_profile);
        drawerListView = (ListView) findViewById(R.id.drawerListView);
        img_drawer = (ImageView) findViewById(R.id.img_drawer);
        drawerListView = (ListView) findViewById(R.id.drawerListView);
        mContentDisplaceToggle = new ContentDisplaceDrawerToggle(this, drawer,
                R.id.rl_profile, Gravity.START);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
      //  int width = dm.widthPixels;
        int width = getResources().getDisplayMetrics().widthPixels/2 +30;
        DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) leftfgf_drawer.getLayoutParams();
        lp.width = width + 30;
        leftfgf_drawer.setLayoutParams(lp);
    }
    private void init() {
        header = new HeaderControler(this, true, false, "Students List");
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        progressDialog = Constant.getProgressDialog(this);
        img_search.setOnClickListener(this);
        img_drawer.setOnClickListener(this);
        ed_search_name.setOnEditorActionListener(this);
        lay_back_investment.setOnClickListener(this);
        select_all_checkbox.setOnClickListener(this);
        rl_send_sms.setOnClickListener(this);
        img_username_close.setOnClickListener(this);
        lL_drawer.setOnClickListener(this);
    }
    private void getIntentData() {
        try {
            Intent intent = getIntent();
            usl_id = intent.getStringExtra("usl_id");
            clt_id = intent.getStringExtra("clt_id");
            school_name = intent.getStringExtra("School_name");
            teacher_name = intent.getStringExtra("Teacher_name");
            version_name = intent.getStringExtra("version_name");
            board_name = intent.getStringExtra("board_name");
            class_id = intent.getStringExtra("class_id");
            clas_teacher = intent.getStringExtra("cls_teacher");
            academic_year = intent.getStringExtra("academic_year");
            teacher_div = intent.getStringExtra("Teacher_div");
            teacher_std = intent.getStringExtra("Tecaher_Std");
            teacher_shift = intent.getStringExtra("Teacher_Shift");
            org_id = intent.getStringExtra("org_id");
            AppController.versionName = version_name;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void setDrawer() {
        try {
            if(LoginActivity.get_class_teacher(TeacherSMSStudentsActivity.this).equalsIgnoreCase("1")){
                icons_without_sibling = new int[]{R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sms_48x48, R.drawable.announcementicon_48x48, R.drawable.att_48x48, R.drawable.behaviouricon_48x48,R.drawable.subjectlist_48x48,R.drawable.setting1_hdpi, R.drawable.logout1_hdpi, R.drawable.info};
                data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement", "Attendance", "Behaviour","Subject List","Setting", "Sign Out", "About"};
                drawerListView.setAdapter(new CustomAccount(TeacherSMSStudentsActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));

                drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherSMSStudentsActivity.this, TeacherProfileActivity.class);
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
                                Intent teacherProfile = new Intent(TeacherSMSStudentsActivity.this, TeacherMessageActivity.class);
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
                                Intent teacherProfile = new Intent(TeacherSMSStudentsActivity.this, TeacherSMSActivity.class);
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
                                Intent teacherannouncment = new Intent(TeacherSMSStudentsActivity.this, TeacherAnnouncementsActivity.class);
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
                            //Attendance
                            if (dft.isInternetOn() == true) {
                                Intent teacherattendance = new Intent(TeacherSMSStudentsActivity.this, TeacherAttendanceActivity.class);
                                teacherattendance.putExtra("clt_id", clt_id);
                                teacherattendance.putExtra("usl_id", usl_id);
                                teacherattendance.putExtra("School_name", school_name);
                                teacherattendance.putExtra("Teacher_name", teacher_name);
                                teacherattendance.putExtra("version_name", version_name);
                                teacherattendance.putExtra("board_name", board_name);
                                teacherattendance.putExtra("org_id", org_id);
                                teacherattendance.putExtra("Teacher_div",teacher_div);
                                teacherattendance.putExtra("Teacher_Shift",teacher_shift);
                                teacherattendance.putExtra("Tecaher_Std",teacher_std);
                                teacherattendance.putExtra("academic_year", academic_year);
                                teacherattendance.putExtra("cls_teacher", clas_teacher);
                                startActivity(teacherattendance);
                            }
                        } else if (position == 5) {
                            //Behaviour
                            if (dft.isInternetOn() == true) {
                                Intent teacher_behaviour = new Intent(TeacherSMSStudentsActivity.this, TeacherBehaviourActivity.class);
                                teacher_behaviour.putExtra("clt_id", clt_id);
                                teacher_behaviour.putExtra("usl_id", usl_id);
                                teacher_behaviour.putExtra("School_name", school_name);
                                teacher_behaviour.putExtra("Teacher_name", teacher_name);
                                teacher_behaviour.putExtra("version_name", version_name);
                                teacher_behaviour.putExtra("board_name", board_name);
                                teacher_behaviour.putExtra("org_id", org_id);
                                teacher_behaviour.putExtra("Teacher_div",teacher_div);
                                teacher_behaviour.putExtra("Teacher_Shift",teacher_shift);
                                teacher_behaviour.putExtra("Tecaher_Std",teacher_std);
                                teacher_behaviour.putExtra("academic_year", academic_year);
                                teacher_behaviour.putExtra("cls_teacher", clas_teacher);
                                startActivity(teacher_behaviour);
                            }
                        } else if (position == 6) {
                            //Sbject List
                            if (dft.isInternetOn() == true) {
                                Intent subject_list = new Intent(TeacherSMSStudentsActivity.this, SubjectListActivity.class);
                                subject_list.putExtra("usl_id", usl_id);
                                subject_list.putExtra("version_name", version_name);
                                subject_list.putExtra("clt_id", clt_id);
                                subject_list.putExtra("School_name", school_name);
                                subject_list.putExtra("Teacher_name", teacher_name);
                                subject_list.putExtra("board_name", board_name);
                                subject_list.putExtra("academic_year", academic_year);
                                subject_list.putExtra("cls_teacher", clas_teacher);
                                subject_list.putExtra("Teacher_div",teacher_div);
                                subject_list.putExtra("Teacher_Shift",teacher_shift);
                                subject_list.putExtra("Tecaher_Std",teacher_std);
                                subject_list.putExtra("org_id", org_id);
                                startActivity(subject_list);
                            }
                        } else if (position == 7) {
                            Intent setting = new Intent(TeacherSMSStudentsActivity.this, SettingActivity.class);
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
                        } else if (position == 8) {
                            //Signout
                            Intent signout = new Intent(TeacherSMSStudentsActivity.this, LoginActivity.class);
                            Constant.SetLoginData("", "", TeacherSMSStudentsActivity.this);
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
                                final Dialog alertDialog = new Dialog(TeacherSMSStudentsActivity.this);
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
            else{
                icons_without_sibling = new int[]{R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sms_48x48, R.drawable.announcementicon_48x48,R.drawable.subjectlist_48x48,R.drawable.setting1_hdpi, R.drawable.logout1_hdpi, R.drawable.info};
                data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement","Subject List","Setting", "Sign Out", "About"};
                drawerListView.setAdapter(new CustomAccount(TeacherSMSStudentsActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));
                drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherSMSStudentsActivity.this, TeacherProfileActivity.class);
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
                                Intent teacherProfile = new Intent(TeacherSMSStudentsActivity.this, TeacherMessageActivity.class);
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
                                Intent teacherProfile = new Intent(TeacherSMSStudentsActivity.this, TeacherSMSActivity.class);
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
                                Intent teacherannouncment = new Intent(TeacherSMSStudentsActivity.this, TeacherAnnouncementsActivity.class);
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
                        }
                        else if (position == 4) {
                            //Sbject List
                            if (dft.isInternetOn() == true) {
                                Intent subject_list = new Intent(TeacherSMSStudentsActivity.this, SubjectListActivity.class);
                                subject_list.putExtra("usl_id", usl_id);
                                subject_list.putExtra("version_name", version_name);
                                subject_list.putExtra("clt_id", clt_id);
                                subject_list.putExtra("School_name", school_name);
                                subject_list.putExtra("Teacher_name", teacher_name);
                                subject_list.putExtra("board_name", board_name);
                                subject_list.putExtra("academic_year", academic_year);
                                subject_list.putExtra("cls_teacher", clas_teacher);
                                subject_list.putExtra("Teacher_div",teacher_div);
                                subject_list.putExtra("Teacher_Shift",teacher_shift);
                                subject_list.putExtra("Tecaher_Std",teacher_std);
                                subject_list.putExtra("org_id", org_id);
                                startActivity(subject_list);
                            }
                        } else if (position == 5) {
                            Intent setting = new Intent(TeacherSMSStudentsActivity.this, SettingActivity.class);
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
                            Intent signout = new Intent(TeacherSMSStudentsActivity.this, LoginActivity.class);
                            Constant.SetLoginData("", "", TeacherSMSStudentsActivity.this);
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
                                final Dialog alertDialog = new Dialog(TeacherSMSStudentsActivity.this);
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
            Constant.setAdminDrawerData(academic_year, teacher_name, TeacherSMSStudentsActivity.this);
            setDrawerData();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void setDrawerData() {
        tv_academic_year_drawer.setText(academic_year);
        tv_child_name_drawer.setText(teacher_name);
        if((LoginActivity.get_class_teacher(TeacherSMSStudentsActivity.this).equalsIgnoreCase("1")))
        {
            tv_teacher_class.setText(teacher_shift + " | " + teacher_std + " | " + teacher_div);
        }
        else{
            tv_teacher_class.setVisibility(View.GONE);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(AppController.drawerSignOut.equalsIgnoreCase("true")) {
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
            intent.putExtra("org_id",org_id);
            intent.putExtra("School_name", AppController.school_name);
            intent.putExtra("board_name", AppController.Board_name);
            intent.putExtra("Teacher_div",teacher_div);
            intent.putExtra("Teacher_Shift",teacher_shift);
            intent.putExtra("Tecaher_Std",teacher_std);
            intent.putExtra("Sibling", AppController.SiblingListSize);
            startActivity(intent);
        }
    }
    private void callSMSStudentsListWebservice(String clt_id,String class_id,String studentName,int pageIndex,int pageSize) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getTeacherSMSStudentList(clt_id, class_id, studentName, pageIndex, pageSize, TeacherSMSStudentListMethod_name, Request.Method.POST,
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
                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(ed_search_name.getWindowToken(), 0);
                            } else {
                                select_all_checkbox.setChecked(false);
                              //  select_all_checkbox.setClickable(false);
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
        showSMSStudentListAdapter = new ShowSMSStudentListAdapter(TeacherSMSStudentsActivity.this,this,login_details.SmsStudentResult,selectAllCheckboxStatus,selectAll,clt_id,usl_id,school_name,teacher_name,version_name,board_name);
        lv_sms_srudent_list.setAdapter(showSMSStudentListAdapter);
    }
    /**
     *
     * Returns the number of checked items
     */
    private int getCheckedItemCount(){
        int cnt = 0;
        SparseBooleanArray positions = lv_sms_srudent_list.getCheckedItemPositions();
        int itemCount = lv_sms_srudent_list.getCount();

        for(int i=0;i<itemCount;i++){
            if(positions.get(i))
                cnt++;
        }
        return cnt;
    }
    @Override
    public void onClick(View v) {
        if(v==img_search)
        {
            try {
            pageIndex = 1;
            pageSize = 200;
            stud_id_data_withoutDuplicates.clear();
            contact_no_data_withoutduplicates.clear();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            studentName = ed_search_name.getText().toString();
            //Call webservice for StudentList
            callSMSStudentsListWebservice(clt_id, class_id, studentName, pageIndex, pageSize);
            showSMSStudentListAdapter.notifyDataSetChanged();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        }
        else if(v==img_drawer){
            drawer.openDrawer(Gravity.RIGHT);
        }
        else if(v==lay_back_investment){
            try {
                Intent i = new Intent(TeacherSMSStudentsActivity.this, TeacherSMSActivity.class);
                i.putExtra("clt_id", clt_id);
                i.putExtra("usl_id", usl_id);
                i.putExtra("School_name", school_name);
                i.putExtra("Teacher_name", teacher_name);
                i.putExtra("version_name", version_name);
                i.putExtra("board_name", board_name);
                i.putExtra("cls_teacher", clas_teacher);
                i.putExtra("academic_year", academic_year);
                i.putExtra("org_id", org_id);
                i.putExtra("Teacher_div", teacher_div);
                i.putExtra("Teacher_Shift", teacher_shift);
                i.putExtra("Tecaher_Std", teacher_std);
                startActivity(i);
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
                stud_id_data_withoutDuplicates.clear();
                stud_id_data.clear();
                class_id_data.clear();
                contact_no_data_withoutduplicates.clear();
                contact_no_data.clear();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                studentName = ed_search_name.getText().toString();
                //Call webservice for StudentList
                callSMSStudentsListWebservice(clt_id, class_id, studentName, pageIndex, pageSize);
                img_username_close.setVisibility(View.INVISIBLE);
                showSMSStudentListAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(v==select_all_checkbox) {
            try {
                isSelectallCheckboxchecked();
                status = select_all_checkbox.isChecked();
                if (selectAllCheckboxStatus == false) {
                    selectAll = "false";
                    contact_no_data.clear();
                    stud_id_data.clear();
                    contact_no_data_withoutduplicates.clear();
                    stud_id_data_withoutDuplicates.clear();
                    showSMSStudentListAdapter = new ShowSMSStudentListAdapter(TeacherSMSStudentsActivity.this, this, login_details.SmsStudentResult, selectAllCheckboxStatus, selectAll, clt_id, usl_id, school_name, teacher_name, version_name, board_name);
                    lv_sms_srudent_list.setAdapter(showSMSStudentListAdapter);
                    for (int i = 0; i < login_details.SmsStudentResult.size(); i++) {
                        login_details.SmsStudentResult.get(i).setChecked(false);
                        boolean check = login_details.SmsStudentResult.get(i).isChecked();
                        Log.e("CHECK Act", String.valueOf(check));
                    }

                    showSMSStudentListAdapter.notifyDataSetChanged();
                } else {
                    selectAll = "true";
                    checkboxClicked = "false";
                    showSMSStudentListAdapter = new ShowSMSStudentListAdapter(TeacherSMSStudentsActivity.this, this, login_details.SmsStudentResult, selectAllCheckboxStatus, selectAll, clt_id, usl_id, school_name, teacher_name, version_name, board_name);
                    lv_sms_srudent_list.setAdapter(showSMSStudentListAdapter);
                    for (int i = 0; i < login_details.SmsStudentResult.size(); i++) {
                        login_details.SmsStudentResult.get(i).setChecked(true);
                        boolean check = login_details.SmsStudentResult.get(i).isChecked();
                        Log.e("CHECK Act", String.valueOf(check));
                        stud_id = login_details.SmsStudentResult.get(i).stu_ID;
                        contact_number = login_details.SmsStudentResult.get(i).msd_candate;
                        stud_id_data.add(stud_id);
                        stud_id_data_withoutDuplicates.addAll(stud_id_data);
                        contact_no_data.add(contact_number);
                        contact_no_data_withoutduplicates.addAll(contact_no_data);
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
                        Log.e("Stude ID seperated", selectedCheckbox);
                        //Mobile Number
                        int mobileno_total = contact_no_data_withoutduplicates.size() * separator.length();
                        for (String s : contact_no_data_withoutduplicates) {
                            mobileno_total += s.length();
                        }
                        StringBuilder mobilnoString = new StringBuilder(mobileno_total);
                        for (String s : contact_no_data_withoutduplicates) {
                            mobilnoString.append(separator).append(s);
                        }
                        selectedCheckboxMobileNo = mobilnoString.substring(separator.length());
                        Log.e("MobileNo", selectedCheckboxMobileNo);
                    }
                    showSMSStudentListAdapter.notifyDataSetChanged();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        else if(v==rl_send_sms) {
            try {

               // showSMSStudentListAdapter.notifyDataSetChanged();
                if (selectAllCheckboxStatus == false && stud_id_data_withoutDuplicates.size()==0) {
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
                        Intent sendsms = new Intent(TeacherSMSStudentsActivity.this, TeacherSendSMSActivity.class);

                        if (selectAllCheckboxStatus == true && check == false && checkboxClicked.equalsIgnoreCase("true")) {
                            stud_id = selectedCheckbox;
                            contact_number = selectedCheckboxMobileNo;
                        } else if (selectAllCheckboxStatus == true && check == true && checkboxClicked.equalsIgnoreCase("true")) {
                            stud_id = selectedCheckbox;
                            contact_number = selectedCheckboxMobileNo;
                        } else if (selectAllCheckboxStatus == true) {
                            stud_id = "0";
                            contact_number = selectedCheckboxMobileNo;
                        } else {
                            stud_id = selectedCheckbox;
                            contact_number = selectedCheckboxMobileNo;
                        }
                        sendsms.putExtra("class_id", class_id);
                        sendsms.putExtra("clt_id", clt_id);
                        sendsms.putExtra("usl_id", usl_id);
                        sendsms.putExtra("School_name", school_name);
                        sendsms.putExtra("Teacher_name", teacher_name);
                        sendsms.putExtra("version_name", AppController.versionName);
                        sendsms.putExtra("board_name", board_name);
                        sendsms.putExtra("org_id", org_id);
                        sendsms.putExtra("academic_year", academic_year);
                        sendsms.putExtra("cls_teacher", clas_teacher);
                        sendsms.putExtra("stud_id", stud_id);
                        sendsms.putExtra("Contact_Number", contact_number);
                        sendsms.putExtra("Teacher_div",teacher_div);
                        sendsms.putExtra("Teacher_Shift",teacher_shift);
                        sendsms.putExtra("Tecaher_Std",teacher_std);
                        Log.d("<<send stuid", stud_id);
                        Log.d("<<send contact", contact_number);
                        startActivity(sendsms);
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            pageIndex=1;
            pageSize=200;
            studentName = ed_search_name.getText().toString();
            try {
                //Call webservice for StudentList
                callSMSStudentsListWebservice(clt_id, class_id, studentName, pageIndex, pageSize);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return true;
        }

        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
    @Override
    public void setValues(ArrayList<String> studData, ArrayList<String> contactData) {

    }

    @Override
    public void getValues(ArrayList<String> getStudData, ArrayList<String> contactData) {

    }
    public static  class ShowSMSStudentListAdapter extends BaseAdapter {
        LayoutInflater layoutInflater;
        Context context;
        DataTransferInterface dtInterface;
        String selectAll,stud_id,contact_number,class_id,clt_id,usl_id,school_name,teacher_name,version_name,board_name;

        boolean[] mCheckedState;
        public ArrayList<SmsStudentResult> SmsStudentResult = new ArrayList<SmsStudentResult>();
        boolean selectAllCheckboxStatus;
        public ShowSMSStudentListAdapter(Context context,DataTransferInterface dataTransferInterface, ArrayList<SmsStudentResult> SmsStudentResult,boolean selectAllCheckboxStatus,String selectAll,String clt_id,String usl_id,String school_name,String teacher_name,String version_name,String board_name) {
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
                    String forCheck = SmsStudentResult.get(position).stu_ID ;
                    try {
                        Log.d("<<TeacherStudentLFM", "point1");
                        if (((CheckBox) v).isChecked()) {
                            Log.d("<<TeacherStudentLFM", "point2");
                            if (selectAll.equalsIgnoreCase("true")) {
                                Log.d("<<TeacherStudentLFM", "point3");
                                checkboxClicked = "true";
                                if (finalHolder.checkbox.isChecked() == true) {
                                    stud_id_data_withoutDuplicates.add(SmsStudentResult.get(position).stu_ID);
                                    contact_no_data_withoutduplicates.add(SmsStudentResult.get(position).msd_candate);

                                } else {
                                    stud_id_data_withoutDuplicates.remove(SmsStudentResult.get(position).stu_ID);
                                    contact_no_data_withoutduplicates.remove(SmsStudentResult.get(position).msd_candate);
                                }
                                Log.e("remove size", String.valueOf(stud_id_data_withoutDuplicates.size()));
                                Log.e("remove size mmobil", String.valueOf(contact_no_data_withoutduplicates.size()));
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
                                //Mobile Number
                                int mobileno_total = contact_no_data_withoutduplicates.size() * separator.length();
                                for (String s : contact_no_data_withoutduplicates) {
                                    mobileno_total += s.length();
                                }
                                StringBuilder mobilnoString = new StringBuilder(mobileno_total);
                                for (String s : contact_no_data_withoutduplicates) {
                                    mobilnoString.append(separator).append(s);
                                }
                                selectedCheckboxMobileNo = mobilnoString.substring(separator.length());
                                Log.e("MobileNo", selectedCheckboxMobileNo);
                            } else {
                                Log.d("<<TeacherStudentLFM", "point4");
                                mCheckedState[position] = true;
                                SmsStudentResult.get(position).setChecked(true);
                                check = SmsStudentResult.get(position).isChecked();
                                Log.e("CHECK", String.valueOf(check));
                                finalHolder.checkbox.setChecked(true);
                                checkboxStatus = finalHolder.checkbox.isChecked();
                                stud_id = SmsStudentResult.get(position).stu_ID;
                                contact_number = SmsStudentResult.get(position).msd_candate;
                                stud_id_data.add(stud_id);
                                stud_id_data_withoutDuplicates.addAll(stud_id_data);
                                contact_no_data.add(contact_number);
                                contact_no_data_withoutduplicates.addAll(contact_no_data);
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
                                //Mobile Number
                                int mobileno_total = contact_no_data_withoutduplicates.size() * separator.length();
                                for (String s : contact_no_data_withoutduplicates) {
                                    mobileno_total += s.length();
                                }
                                StringBuilder mobilnoString = new StringBuilder(mobileno_total);
                                for (String s : contact_no_data_withoutduplicates) {
                                    mobilnoString.append(separator).append(s);
                                }
                                selectedCheckboxMobileNo = mobilnoString.substring(separator.length());
                                Log.e("MobileNo", selectedCheckboxMobileNo);
                            }
                        } else {
                            Log.d("<<TeacherStudentLFM", "point5");
                            if (selectAll.equalsIgnoreCase("true")) {
                                Log.d("<<TeacherStudentLFM", "point6");
                                checkboxClicked = "true";
                                if (finalHolder.checkbox.isChecked() == true) {
                                    stud_id_data_withoutDuplicates.add(SmsStudentResult.get(position).stu_ID);
                                    contact_no_data_withoutduplicates.add(SmsStudentResult.get(position).msd_candate);
                                } else {
                                    stud_id_data_withoutDuplicates.remove(SmsStudentResult.get(position).stu_ID);
                                    contact_no_data_withoutduplicates.remove(SmsStudentResult.get(position).msd_candate);
                                }
                                Log.e("remove size", String.valueOf(stud_id_data_withoutDuplicates.size()));
                                Log.e("remove size mmobil", String.valueOf(contact_no_data_withoutduplicates.size()));
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
                                //Mobile Number
                                int mobileno_total = contact_no_data_withoutduplicates.size() * separator.length();
                                for (String s : contact_no_data_withoutduplicates) {
                                    mobileno_total += s.length();
                                }
                                StringBuilder mobilnoString = new StringBuilder(mobileno_total);
                                for (String s : contact_no_data_withoutduplicates) {
                                    mobilnoString.append(separator).append(s);
                                }
                                selectedCheckboxMobileNo = mobilnoString.substring(separator.length());
                                Log.e("MobileNo", selectedCheckboxMobileNo);
                            } else if(selectAll.equalsIgnoreCase("false")){

                                Log.d("<<TeacherStudentLFM", "point7");
                                SmsStudentResult.get(position).setChecked(false);
                                check = SmsStudentResult.get(position).isChecked();
                                Log.d("<<CHECK", String.valueOf(check));
                                mCheckedState[position] = false;
                                if (finalHolder.checkbox.isChecked() == true) {
                                    Log.d("<<TeacherStudentLFM", "point8");
                                    stud_id_data_withoutDuplicates.add(SmsStudentResult.get(position).stu_ID);
                                    contact_no_data_withoutduplicates.add(SmsStudentResult.get(position).msd_candate);
                                } else {
                                    Log.d("<<TeacherStudentLFM", "point9");
                                    if(selectedCheckbox.contains(forCheck)== true){
                                        Log.d("<<Before Removing",selectedCheckbox);
                                        // 274953 , 274955
                                        //    0   1    2
                                        int indexOfValue = selectedCheckbox.indexOf(forCheck);
                                        int indexOfComma = indexOfValue+1;
                                        Log.d("<<Unchecked checkbox", forCheck);
                                        //Log.d("<<Total length",String.valueOf(selectedCheckbox.length()));
                                        //Log.d("<<indexOfValue ",String.valueOf(indexOfValue ));
                                        //Log.d("<<indexOfComma",String.valueOf(indexOfComma));
                                        // Removing student id
                                        boolean removed_status_stud_id_data = stud_id_data.remove(forCheck);
                                        //Log.d("<<removed_status_1",String.valueOf(removed_status_stud_id_data));

                                        boolean removed_status_stud_id_data_withoutDuplicates = stud_id_data_withoutDuplicates.remove(forCheck);
                                        //Log.d("<<removed_status_1",String.valueOf(removed_status_stud_id_data_withoutDuplicates));
                                        // Removing student id
                                        selectedCheckbox = selectedCheckbox.replace(forCheck," ");
                                        Log.d("<<After Removing value",selectedCheckbox);
                                        // Removing comma id
                                        selectedCheckbox = selectedCheckbox.substring(0, indexOfComma) + selectedCheckbox.substring(indexOfComma + 1);
                                        Log.d("<<After Removing comma",selectedCheckbox);

                                    }

                                    //stud_id_data_withoutDuplicates.remove(SmsStudentResult.get(position).stu_ID);
                                    //contact_no_data_withoutduplicates.remove(SmsStudentResult.get(position).msd_candate);
                                }
                                Log.e("remove size", String.valueOf(stud_id_data_withoutDuplicates.size()));
                                Log.e("remove size mmobil", String.valueOf(contact_no_data_withoutduplicates.size()));
                                int total = stud_id_data_withoutDuplicates.size() * separator.length();
                                for (String s : stud_id_data_withoutDuplicates) {
                                    total += s.length();
                                }
                                StringBuilder rString = new StringBuilder(total);
                                for (String s : stud_id_data_withoutDuplicates) {
                                    rString.append(separator).append(s);
                                }
                                selectedCheckbox = rString.substring(separator.length());
                                Log.d("<<Stude ID seperated", selectedCheckbox);
                                //Mobile Number
                                int mobileno_total = contact_no_data_withoutduplicates.size() * separator.length();
                                for (String s : contact_no_data_withoutduplicates) {
                                    mobileno_total += s.length();
                                }
                                StringBuilder mobilnoString = new StringBuilder(mobileno_total);
                                for (String s : contact_no_data_withoutduplicates) {
                                    mobilnoString.append(separator).append(s);
                                }
                                selectedCheckboxMobileNo = mobilnoString.substring(separator.length());
                                Log.e("<< mobile numbers", selectedCheckboxMobileNo);
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
        try {
            Intent i = new Intent(TeacherSMSStudentsActivity.this, TeacherSMSActivity.class);
            i.putExtra("clt_id", clt_id);
            i.putExtra("usl_id", usl_id);
            i.putExtra("School_name", school_name);
            i.putExtra("Teacher_name", teacher_name);
            i.putExtra("version_name", version_name);
            i.putExtra("board_name", board_name);
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
