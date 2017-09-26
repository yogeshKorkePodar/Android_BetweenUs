package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.text.InputType;
import android.text.method.KeyListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
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
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Gayatri on 5/5/2016.
 */
public class TeacherViewLogActivity extends Activity implements View.OnClickListener{

    //UI Variables
    //TextView
    TextView tv_cancel, tv_academic_year_drawer,tv_child_name_drawer,tv_teacher_class,tv_version_code,tv_version_name
            ,tv_subject,sub_teacher_name,tv_std,tv_feedback_of_assessment,tv_date_on_which_conducted,tv_type_of_assessment;
    //ImageView
    ImageView img_drawer;
    //DrawerLayout
    DrawerLayout drawer;
    //Linear Layout
    LinearLayout lay_back_investment,ll_below_academic_yr_16,ll_last_period,ll_grater_than_16;
    //Relative Layout
    RelativeLayout leftfgf_drawer,rl_profile;
    //ListView
    ListView drawerListView;
    //EditText
    EditText ed_period_no,ed_sub_topic_completed,ed_describe_lesson_highlights,ed_teaching_strategies,ed_challenges_faced,ed_work_carried_forward,ed_reson_for_same,ed_all_of_my_students_can,
            ed_most_of_my_students_can,ed_some_of_my_students_can,ed_few_of_my_students_can,ed_the_topic_was_started_on,ed_the_topic_was_completed_on,
            ed_yes_no,ed_type_of_assessment,ed_date_on_which_conducted,ed_feedback_of_assesment,ed_challenges_faced_by_students,ed_suggested_correction_in_text_book,ed_suggested_correction_in_work_book,
            ed_suggested_correction_in_lesson_plan,ed_suggested_correction_in_internal_assement,ed_remark;
    //Button
    Button btn_edit_log,btn_save_log,btn_cancel_log;
    LoginDetails login_details;
    DataFetchService dft;
    KeyListener variable;
    //LayoutEntities
    HeaderControler header;
    //ProgressDialog
    ProgressDialog progressDialog;
    ContentDisplaceDrawerToggle mContentDisplaceToggle;

    String usl_id,clt_id,board_name,school_name,teacher_name,version_name,org_id,teacher_div,teacher_std,teacher_shift,clas_teacher,academic_year,
            subject_name,classs,class_id,std,topic_name,crf_id,rol_id,selectedPeriodNo,field1,field2,field3,field4,field5,field6,field7,field8,field9,field10,field11,field12;
    String ViewLogMethod_name = "TopicViewlog";
    String ViewLogOnPeriodSelectionMethod_name = "TopicContentViewlog";
    String SaveLogInfoMethod_name = "InsertContentLog";
    String[] select_period;
    ArrayList<String> strings_period_no ;
    int notificationID = 1,periodsize;
    String[] data_without_sibling;
    int[] icons_without_sibling;
    ArrayList<String> strings_yes_no ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.teacher_view_logs);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getIntentData();
        findViews();
        init();
        setDrawer();
        getEdittextvariable();
        //setEditText Disable
        disableEditText();
        //CallWs to view log
        rol_id = LoginActivity.get_RollId(TeacherViewLogActivity.this);
        btn_cancel_log.setVisibility(View.GONE);
        btn_save_log.setVisibility(View.GONE);
        AppController.firstPeriodNo = "true";
        try {
            if(AppController.firstPeriodNo.equalsIgnoreCase("true")) {
                CallWsToViewLog(crf_id, usl_id, rol_id, clt_id, academic_year);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void disableEditText() {
        ed_sub_topic_completed.setKeyListener(null);
        ed_describe_lesson_highlights.setKeyListener(null);
        ed_teaching_strategies.setKeyListener(null);
        ed_challenges_faced.setKeyListener(null);
        ed_work_carried_forward.setKeyListener(null);
        ed_reson_for_same.setKeyListener(null);
        ed_all_of_my_students_can.setKeyListener(null);
        ed_most_of_my_students_can.setKeyListener(null);
        ed_some_of_my_students_can.setKeyListener(null);
        ed_few_of_my_students_can.setKeyListener(null);
        ed_the_topic_was_started_on.setKeyListener(null);
        ed_the_topic_was_completed_on.setKeyListener(null);
       // ed_yes_no.setOnClickListener(null);
        ed_type_of_assessment.setKeyListener(null);
        ed_date_on_which_conducted.setKeyListener(null);
        ed_feedback_of_assesment.setKeyListener(null);
        ed_challenges_faced_by_students.setKeyListener(null);
        ed_suggested_correction_in_text_book.setKeyListener(null);
        ed_suggested_correction_in_work_book.setKeyListener(null);
        ed_suggested_correction_in_lesson_plan.setKeyListener(null);
        ed_suggested_correction_in_internal_assement.setKeyListener(null);
        ed_remark.setKeyListener(null);

        ed_sub_topic_completed.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_describe_lesson_highlights.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_teaching_strategies.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_challenges_faced.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_work_carried_forward.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_reson_for_same.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_yes_no.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_all_of_my_students_can.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_most_of_my_students_can.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_some_of_my_students_can.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_few_of_my_students_can.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_the_topic_was_started_on.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_the_topic_was_completed_on.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_type_of_assessment.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_date_on_which_conducted.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_feedback_of_assesment.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_challenges_faced_by_students.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_suggested_correction_in_text_book.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_suggested_correction_in_work_book.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_suggested_correction_in_lesson_plan.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_suggested_correction_in_internal_assement.setBackgroundResource(R.drawable.dropdown_edittext);
        ed_remark.setBackgroundResource(R.drawable.dropdown_edittext);
    }

    private void getEdittextvariable() {
        variable =  ed_sub_topic_completed.getKeyListener();
        variable=  ed_describe_lesson_highlights.getKeyListener();
        variable= ed_teaching_strategies.getKeyListener();
        variable= ed_challenges_faced.getKeyListener();
        variable= ed_work_carried_forward.getKeyListener();
        variable= ed_reson_for_same.getKeyListener();
        variable= ed_all_of_my_students_can.getKeyListener();
        variable= ed_most_of_my_students_can.getKeyListener();
        variable= ed_some_of_my_students_can.getKeyListener();
        variable= ed_few_of_my_students_can.getKeyListener();
        variable= ed_the_topic_was_started_on.getKeyListener();
        variable= ed_the_topic_was_completed_on.getKeyListener();
       // variable= ed_yes_no.getKeyListener();
        variable= ed_type_of_assessment.getKeyListener();
        variable= ed_few_of_my_students_can.getKeyListener();
        variable= ed_date_on_which_conducted.getKeyListener();
        variable= ed_feedback_of_assesment.getKeyListener();
        variable= ed_challenges_faced_by_students.getKeyListener();
        variable= ed_suggested_correction_in_text_book.getKeyListener();
        variable= ed_suggested_correction_in_work_book.getKeyListener();
        variable= ed_suggested_correction_in_lesson_plan.getKeyListener();
        variable= ed_suggested_correction_in_internal_assement.getKeyListener();
        variable= ed_remark.getKeyListener();
    }

    private void setDrawer() {
        try {
            if(LoginActivity.get_class_teacher(TeacherViewLogActivity.this).equalsIgnoreCase("1")) {
                icons_without_sibling = new int[]{R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sms_48x48, R.drawable.announcementicon_48x48, R.drawable.att_48x48, R.drawable.behaviouricon_48x48, R.drawable.subjectlist_48x48, R.drawable.setting1_hdpi, R.drawable.logout1_hdpi, R.drawable.info};
                data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement", "Attendance", "Behaviour", "Subject List", "Setting", "Sign Out", "About"};
                drawerListView.setAdapter(new CustomAccount(TeacherViewLogActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));
                drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherViewLogActivity.this, TeacherProfileActivity.class);
                                teacherProfile.putExtra("clt_id", clt_id);
                                teacherProfile.putExtra("usl_id", usl_id);
                                teacherProfile.putExtra("School_name", school_name);
                                teacherProfile.putExtra("Teacher_name", teacher_name);
                                teacherProfile.putExtra("org_id", org_id);
                                teacherProfile.putExtra("cls_teacher", clas_teacher);
                                teacherProfile.putExtra("academic_year", academic_year);
                                teacherProfile.putExtra("version_name", AppController.versionName);
                                teacherProfile.putExtra("board_name", board_name);
                                teacherProfile.putExtra("Teacher_div",teacher_div);
                                teacherProfile.putExtra("Teacher_Shift",teacher_shift);
                                teacherProfile.putExtra("Tecaher_Std",teacher_std);
                                startActivity(teacherProfile);
                            }
                        } else if (position == 1) {
                            //Message
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherViewLogActivity.this, TeacherMessageActivity.class);
                                teacherProfile.putExtra("clt_id", clt_id);
                                teacherProfile.putExtra("usl_id", usl_id);
                                teacherProfile.putExtra("School_name", school_name);
                                teacherProfile.putExtra("Teacher_name", teacher_name);
                                teacherProfile.putExtra("org_id", org_id);
                                teacherProfile.putExtra("cls_teacher", clas_teacher);
                                teacherProfile.putExtra("academic_year", academic_year);
                                teacherProfile.putExtra("version_name", AppController.versionName);
                                teacherProfile.putExtra("board_name", board_name);
                                teacherProfile.putExtra("Teacher_div",teacher_div);
                                teacherProfile.putExtra("Teacher_Shift",teacher_shift);
                                teacherProfile.putExtra("Tecaher_Std",teacher_std);
                                startActivity(teacherProfile);
                            }
                        } else if (position == 2) {
                            //SMS
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherViewLogActivity.this, TeacherSMSActivity.class);
                                teacherProfile.putExtra("clt_id", clt_id);
                                teacherProfile.putExtra("usl_id", usl_id);
                                teacherProfile.putExtra("School_name", school_name);
                                teacherProfile.putExtra("Teacher_name", teacher_name);
                                teacherProfile.putExtra("org_id", org_id);
                                teacherProfile.putExtra("cls_teacher", clas_teacher);
                                teacherProfile.putExtra("academic_year", academic_year);
                                teacherProfile.putExtra("version_name", AppController.versionName);
                                teacherProfile.putExtra("board_name", board_name);
                                teacherProfile.putExtra("Teacher_div",teacher_div);
                                teacherProfile.putExtra("Teacher_Shift",teacher_shift);
                                teacherProfile.putExtra("Tecaher_Std",teacher_std);
                                startActivity(teacherProfile);
                            }
                        } else if (position == 3) {
                            //announcement
                            if (dft.isInternetOn() == true) {
                                Intent teacherannouncment = new Intent(TeacherViewLogActivity.this, TeacherAnnouncementsActivity.class);
                                teacherannouncment.putExtra("clt_id", clt_id);
                                teacherannouncment.putExtra("usl_id", usl_id);
                                teacherannouncment.putExtra("School_name", school_name);
                                teacherannouncment.putExtra("Teacher_name", teacher_name);
                                teacherannouncment.putExtra("version_name", AppController.versionName);
                                teacherannouncment.putExtra("board_name", board_name);
                                teacherannouncment.putExtra("cls_teacher", clas_teacher);
                                teacherannouncment.putExtra("academic_year", academic_year);
                                teacherannouncment.putExtra("org_id", org_id);
                                teacherannouncment.putExtra("Teacher_div",teacher_div);
                                teacherannouncment.putExtra("Teacher_Shift",teacher_shift);
                                teacherannouncment.putExtra("Tecaher_Std",teacher_std);
                                startActivity(teacherannouncment);
                            }
                        } else if (position == 4) {
                            //Attendance
                            if (dft.isInternetOn() == true) {
                                Intent teacherattendance = new Intent(TeacherViewLogActivity.this, TeacherAttendanceActivity.class);
                                teacherattendance.putExtra("clt_id", clt_id);
                                teacherattendance.putExtra("usl_id", usl_id);
                                teacherattendance.putExtra("School_name", school_name);
                                teacherattendance.putExtra("Teacher_name", teacher_name);
                                teacherattendance.putExtra("version_name", AppController.versionName);
                                teacherattendance.putExtra("board_name", board_name);
                                teacherattendance.putExtra("cls_teacher", clas_teacher);
                                teacherattendance.putExtra("academic_year", academic_year);
                                teacherattendance.putExtra("org_id", org_id);
                                teacherattendance.putExtra("Teacher_div", teacher_div);
                                teacherattendance.putExtra("Teacher_Shift",teacher_shift);
                                teacherattendance.putExtra("Tecaher_Std",teacher_std);
                                startActivity(teacherattendance);
                            }
                        } else if (position == 5) {
                            //Behaviour
                            if (dft.isInternetOn() == true) {
                                Intent teacher_behaviour = new Intent(TeacherViewLogActivity.this, TeacherBehaviourActivity.class);
                                teacher_behaviour.putExtra("clt_id", clt_id);
                                teacher_behaviour.putExtra("usl_id", usl_id);
                                teacher_behaviour.putExtra("School_name", school_name);
                                teacher_behaviour.putExtra("Teacher_name", teacher_name);
                                teacher_behaviour.putExtra("version_name", AppController.versionName);
                                teacher_behaviour.putExtra("board_name", board_name);
                                teacher_behaviour.putExtra("org_id", org_id);
                                teacher_behaviour.putExtra("cls_teacher", clas_teacher);
                                teacher_behaviour.putExtra("Teacher_div",teacher_div);
                                teacher_behaviour.putExtra("Teacher_Shift",teacher_shift);
                                teacher_behaviour.putExtra("Tecaher_Std",teacher_std);
                                teacher_behaviour.putExtra("academic_year", academic_year);
                                startActivity(teacher_behaviour);
                            }
                        } else if (position == 6) {
                            //Sbject List
                            if (dft.isInternetOn() == true) {
                                Intent subject_list = new Intent(TeacherViewLogActivity.this, SubjectListActivity.class);
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
                            Intent setting = new Intent(TeacherViewLogActivity.this, SettingActivity.class);
                            setting.putExtra("usl_id", usl_id);
                            setting.putExtra("version_name", AppController.versionName);
                            setting.putExtra("clt_id", clt_id);
                            setting.putExtra("School_name", school_name);
                            setting.putExtra("Teacher_name", teacher_name);
                            setting.putExtra("board_name", board_name);
                            setting.putExtra("org_id", org_id);
                            setting.putExtra("Teacher_div",teacher_div);
                            setting.putExtra("Teacher_Shift",teacher_shift);
                            setting.putExtra("Tecaher_Std", teacher_std);
                            setting.putExtra("cls_teacher", clas_teacher);
                            setting.putExtra("academic_year", academic_year);
                            startActivity(setting);
                        } else if (position == 8) {
                            //Signout
                            Intent signout = new Intent(TeacherViewLogActivity.this, LoginActivity.class);
                            Constant.SetLoginData("", "", TeacherViewLogActivity.this);
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancel(notificationID);
                            AppController.iconCount = 0;
                            AppController.iconCountOnResume = 0;
                            AppController.OnBackpressed = "false";
                            AppController.loginButtonClicked = "false";
                            AppController.drawerSignOut = "true";
                            startActivity(signout);
                        } else if (position == 9) {
                            try {
                                final Dialog alertDialog = new Dialog(TeacherViewLogActivity.this);
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
                icons_without_sibling = new int[]{R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sms_48x48, R.drawable.announcementicon_48x48, R.drawable.subjectlist_48x48, R.drawable.setting1_hdpi, R.drawable.logout1_hdpi, R.drawable.info};
                data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement", "Subject List", "Setting", "Sign Out", "About"};
                drawerListView.setAdapter(new CustomAccount(TeacherViewLogActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));
                drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherViewLogActivity.this, TeacherProfileActivity.class);
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
                                Intent teacherProfile = new Intent(TeacherViewLogActivity.this, TeacherMessageActivity.class);
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
                                Intent teacherProfile = new Intent(TeacherViewLogActivity.this, TeacherSMSActivity.class);
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
                                Intent teacherannouncment = new Intent(TeacherViewLogActivity.this, TeacherAnnouncementsActivity.class);
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
                                Intent subject_list = new Intent(TeacherViewLogActivity.this, SubjectListActivity.class);
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
                            Intent setting = new Intent(TeacherViewLogActivity.this, SettingActivity.class);
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
                            Intent signout = new Intent(TeacherViewLogActivity.this, LoginActivity.class);
                            Constant.SetLoginData("", "", TeacherViewLogActivity.this);
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancel(notificationID);
                            AppController.iconCount = 0;
                            AppController.iconCountOnResume = 0;
                            AppController.OnBackpressed = "false";
                            AppController.loginButtonClicked = "false";
                            AppController.drawerSignOut = "true";
                            startActivity(signout);
                        } else if (position == 7) {
                            try {
                                final Dialog alertDialog = new Dialog(TeacherViewLogActivity.this);
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
            Constant.setTeacherDrawerData(academic_year, teacher_name,teacher_div,teacher_std,teacher_shift, TeacherViewLogActivity.this);
            setDrawerData();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setDrawerData() {
        tv_academic_year_drawer.setText(academic_year);
        tv_child_name_drawer.setText(teacher_name);
        if((LoginActivity.get_class_teacher(TeacherViewLogActivity.this).equalsIgnoreCase("1")))
        {
            tv_teacher_class.setText(teacher_shift + " | " + teacher_std + " | " + teacher_div);
        }
        else{
            tv_teacher_class.setVisibility(View.GONE);
        }
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
            subject_name = intent.getStringExtra("Subject_name");
            classs = intent.getStringExtra("Classs");
            org_id = intent.getStringExtra("org_id");
            teacher_div = intent.getStringExtra("Teacher_div");
            teacher_std = intent.getStringExtra("Tecaher_Std");
            teacher_shift = intent.getStringExtra("Teacher_Shift");
            class_id = intent.getStringExtra("class_id");
            clas_teacher = intent.getStringExtra("cls_teacher");
            academic_year = intent.getStringExtra("academic_year");
            crf_id = intent.getStringExtra("crf_id");
            topic_name = intent.getStringExtra("topic_name");
            subject_name = intent.getStringExtra("subject_name");
            classs = intent.getStringExtra("Classs");
            class_id = intent.getStringExtra("class_id");
            AppController.versionName = version_name;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        progressDialog = Constant.getProgressDialog(this);
        header = new HeaderControler(this, true, false, topic_name);
        lay_back_investment.setOnClickListener(this);
        img_drawer.setOnClickListener(this);
        btn_save_log.setOnClickListener(this);
        btn_edit_log.setOnClickListener(this);
        btn_cancel_log.setOnClickListener(this);
        ed_period_no.setOnClickListener(this);
        ed_yes_no.setOnClickListener(this);
    }

    private void findViews() {
        //Linear Layout
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
        ll_grater_than_16 = (LinearLayout) findViewById(R.id.ll_grater_than_16);
        ll_below_academic_yr_16 = (LinearLayout) findViewById(R.id.ll_below_academic_yr_16);
        ll_last_period = (LinearLayout) findViewById(R.id.ll_last_period);
        //Button
        btn_cancel_log = (Button) findViewById(R.id.btn_cancel_log);
        btn_edit_log = (Button) findViewById(R.id.btn_edit_log);
        btn_save_log = (Button) findViewById(R.id.btn_save_log);
        //EditText
        ed_sub_topic_completed = (EditText) findViewById(R.id.ed_sub_topic_completed);
        ed_describe_lesson_highlights = (EditText) findViewById(R.id.ed_describe_lesson_highlights);
        ed_teaching_strategies = (EditText) findViewById(R.id.ed_teaching_strategies);
        ed_challenges_faced = (EditText) findViewById(R.id.ed_challenges_faced);
        ed_work_carried_forward = (EditText) findViewById(R.id.ed_work_carried_forward);
        ed_reson_for_same = (EditText) findViewById(R.id.ed_reson_for_same);
        ed_all_of_my_students_can = (EditText) findViewById(R.id.ed_all_of_my_students_can);
        ed_most_of_my_students_can = (EditText) findViewById(R.id.ed_most_of_my_students_can);
        ed_some_of_my_students_can = (EditText) findViewById(R.id.ed_some_of_my_students_can);
        ed_few_of_my_students_can = (EditText) findViewById(R.id.ed_few_of_my_students_can);
        ed_the_topic_was_started_on = (EditText) findViewById(R.id.ed_the_topic_was_started_on);
        ed_the_topic_was_completed_on = (EditText) findViewById(R.id.ed_the_topic_was_completed_on);
        ed_yes_no = (EditText) findViewById(R.id.ed_yes_no);
        ed_date_on_which_conducted = (EditText) findViewById(R.id.ed_date_on_which_conducted);
        ed_type_of_assessment = (EditText) findViewById(R.id.ed_type_of_assessment);
        ed_feedback_of_assesment = (EditText) findViewById(R.id.ed_feedback_of_assesment);
        ed_challenges_faced_by_students = (EditText) findViewById(R.id.ed_challenges_faced_by_students);
        ed_suggested_correction_in_text_book = (EditText) findViewById(R.id.ed_suggested_correction_in_text_book);
        ed_suggested_correction_in_work_book = (EditText) findViewById(R.id.ed_suggested_correction_in_work_book);
        ed_suggested_correction_in_lesson_plan = (EditText) findViewById(R.id.ed_suggested_correction_in_lesson_plan);
        ed_suggested_correction_in_internal_assement = (EditText) findViewById(R.id.ed_suggested_correction_in_internal_assement);
        ed_remark = (EditText) findViewById(R.id.ed_remark);
        ed_period_no = (EditText) findViewById(R.id.ed_period_no);
        //ImageView
        img_drawer = (ImageView) findViewById(R.id.img_drawer);
        //TextView
        tv_version_code = (TextView) findViewById(R.id.tv_version_code);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_child_name_drawer = (TextView) findViewById(R.id.tv_child_name_drawer);
        tv_academic_year_drawer = (TextView) findViewById(R.id.tv_academic_year_drawer);
        tv_teacher_class = (TextView) findViewById(R.id.tv_teacher_class);
        tv_version_code = (TextView) findViewById(R.id.tv_version_code);
        tv_std = (TextView) findViewById(R.id.tv_std);
        tv_subject = (TextView) findViewById(R.id.tv_subject);
        tv_type_of_assessment = (TextView) findViewById(R.id.tv_type_of_assessment);
        tv_date_on_which_conducted = (TextView) findViewById(R.id.tv_date_on_which_conducted);
        tv_feedback_of_assessment = (TextView) findViewById(R.id.tv_feedback_of_assessment);
        sub_teacher_name = (TextView) findViewById(R.id.teacher_name);
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
        lp.width = width + 10;
        leftfgf_drawer.setLayoutParams(lp);
    }

    @Override
    public void onClick(View v) {
        if(v==btn_edit_log){
            //Enable Editiong
            enableEditText();
            btn_save_log.setVisibility(View.VISIBLE);
            btn_cancel_log.setVisibility(View.VISIBLE);
        }
        else if(v==btn_cancel_log){
            disableEditText();
            btn_cancel_log.setVisibility(View.GONE);
            btn_save_log.setVisibility(View.GONE);
            CallWsToViewLog(crf_id, usl_id, rol_id, clt_id, academic_year);
        }
        else if(v==ed_period_no){
          /*  //CallWsToViewLogData
            callWsToViewLogOnPeriodSelection(crf_id, selectedPeriodNo, usl_id, academic_year);*/
            setPeriodDropDown();
        }
        else if(v==ed_yes_no){
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(ed_yes_no.getWindowToken(), 0);
            selectYesNoDialog();
        }
        else if(v==img_drawer){
            drawer.openDrawer(Gravity.RIGHT);
        }
        else if(v==btn_save_log) {
            if (login_details.TopicInfoAcyGrtSixteenResult.size() == 0) {

                field1 = ed_sub_topic_completed.getText().toString();
                field2 = ed_describe_lesson_highlights.getText().toString();
                field3 = ed_teaching_strategies.getText().toString();
                field4 = ed_challenges_faced.getText().toString();
                field5 = ed_work_carried_forward.getText().toString();
                field6 = ed_reson_for_same.getText().toString();
                field7 = ed_all_of_my_students_can.getText().toString();
                field8 = ed_most_of_my_students_can.getText().toString();
                field9 = ed_some_of_my_students_can.getText().toString();
                field10 = ed_few_of_my_students_can.getText().toString();
            } else if (login_details.TopicinfoResult.size() == 0) {
                if (ed_yes_no.getText().toString().equalsIgnoreCase("No")) {
                    field1 = ed_the_topic_was_started_on.getText().toString();
                    field2 = ed_the_topic_was_completed_on.getText().toString();
                    field3 = ed_yes_no.getText().toString();
                    field4 = ed_challenges_faced_by_students.getText().toString();
                    field5 = ed_suggested_correction_in_text_book.getText().toString();
                    field6 = ed_suggested_correction_in_work_book.getText().toString();
                    field7 = ed_suggested_correction_in_lesson_plan.getText().toString();
                    field8 = ed_suggested_correction_in_internal_assement.getText().toString();
                    field9 = ed_remark.getText().toString();
                } else {
                    field1 = ed_the_topic_was_started_on.getText().toString();
                    field2 = ed_the_topic_was_completed_on.getText().toString();
                    field3 = ed_yes_no.getText().toString();
                    field4 = ed_type_of_assessment.getText().toString();
                    field5 = ed_date_on_which_conducted.getText().toString();
                    field6 = ed_feedback_of_assesment.getText().toString();
                    field7 = ed_challenges_faced_by_students.getText().toString();
                    field8 = ed_suggested_correction_in_text_book.getText().toString();
                    field9 = ed_suggested_correction_in_work_book.getText().toString();
                    field10 = ed_suggested_correction_in_lesson_plan.getText().toString();
                    field11 = ed_suggested_correction_in_internal_assement.getText().toString();
                    field12 = ed_remark.getText().toString();
                }
            }///

            if(login_details.TopicinfoResult.size()==0){
                if(ed_yes_no.getText().toString().equalsIgnoreCase("No")){
                    if (field1.equalsIgnoreCase("") && field2.equalsIgnoreCase("") && field4.equalsIgnoreCase("") && field5.equalsIgnoreCase("") && field6.equalsIgnoreCase("") && field7.equalsIgnoreCase("") && field8.equalsIgnoreCase("") && field9.equalsIgnoreCase("")) {
                        Constant.showOkPopup(TeacherViewLogActivity.this, "Please Enter Atleast one Field", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }

                        });
                    }
                    else {
                        if (AppController.firstPeriodNo.equalsIgnoreCase("false")) {
                            //call save log ws
                            callWsToSaveLog(crf_id, selectedPeriodNo, usl_id, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12);
                        } else {
                            //call save log ws
                            callWsToSaveLog(crf_id, selectedPeriodNo, usl_id, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12);
                        }
                    }

                } else if(ed_yes_no.getText().toString().equalsIgnoreCase("Yes")){
                    if (field1.equalsIgnoreCase("") && field2.equalsIgnoreCase("") && field4.equalsIgnoreCase("") && field5.equalsIgnoreCase("") && field6.equalsIgnoreCase("") && field7.equalsIgnoreCase("") && field8.equalsIgnoreCase("") && field9.equalsIgnoreCase("") && field10.equalsIgnoreCase("") && field11.equalsIgnoreCase("") && field12.equalsIgnoreCase("")) {
                        Constant.showOkPopup(TeacherViewLogActivity.this, "Please Enter Atleast one Field", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }

                        });
                    }
                    else {
                        if(AppController.firstPeriodNo.equalsIgnoreCase("false")) {
                            //call save log ws
                            callWsToSaveLog(crf_id, selectedPeriodNo, usl_id, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12);

                        } else {
                            //call save log ws
                            callWsToSaveLog(crf_id, selectedPeriodNo, usl_id, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12);
                        }
                    }
                }
                else{
                    if(AppController.firstPeriodNo.equalsIgnoreCase("false")) {
                        //call save log ws
                        callWsToSaveLog(crf_id, selectedPeriodNo, usl_id, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12);

                    } else {
                        //call save log ws
                        callWsToSaveLog(crf_id, selectedPeriodNo, usl_id, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12);
                    }
                }
            }
            else if(login_details.TopicInfoAcyGrtSixteenResult.size()==0){
                if (field1.equalsIgnoreCase("") && field2.equalsIgnoreCase("") && field3.equalsIgnoreCase("") && field4.equalsIgnoreCase("") && field5.equalsIgnoreCase("") && field6.equalsIgnoreCase("") && field7.equalsIgnoreCase("") && field8.equalsIgnoreCase("") && field9.equalsIgnoreCase("") && field10.equalsIgnoreCase("")) {
                    Constant.showOkPopup(TeacherViewLogActivity.this, "Please Enter Atleast one Field", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }

                    });
                }
                else{
                    if(AppController.firstPeriodNo.equalsIgnoreCase("false")) {
                        //call save log ws
                        callWsToSaveLog(crf_id, selectedPeriodNo, usl_id, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12);

                    } else {
                        //call save log ws
                        callWsToSaveLog(crf_id, selectedPeriodNo, usl_id, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12);
                    }
                }
            }
        }

        else if(v==lay_back_investment){
            Intent back = new Intent(TeacherViewLogActivity.this,SubjectTopicsActivity.class);
            back.putExtra("clt_id", clt_id);
            back.putExtra("usl_id",usl_id);
            back.putExtra("School_name", school_name);
            back.putExtra("Teacher_name", teacher_name);
            back.putExtra("version_name",version_name);
            back.putExtra("board_name", board_name);
            back.putExtra("org_id",org_id);
            back.putExtra("Teacher_div",teacher_div);
            back.putExtra("Teacher_Shift",teacher_shift);
            back.putExtra("Tecaher_Std",teacher_std);
            back.putExtra("cls_teacher", clas_teacher);
            back.putExtra("academic_year", academic_year);
            back.putExtra("Subject_name",subject_name);
            back.putExtra("Classs",classs);
            back.putExtra("class_id",class_id);
            startActivity(back);
        }
    }

    private void selectYesNoDialog() {
        final String yes_no[] ={"Yes","No"};
        strings_yes_no = new ArrayList<String>();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        alertDialog.setTitle("Select Category");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,yes_no);
        alertDialog.setItems(yes_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                ed_yes_no.setText(yes_no[item]);
                if(yes_no[item].equalsIgnoreCase("No")){
                    ed_type_of_assessment.setVisibility(View.GONE);
                    ed_date_on_which_conducted.setVisibility(View.GONE);
                    ed_feedback_of_assesment.setVisibility(View.GONE);
                    tv_type_of_assessment.setVisibility(View.GONE);
                    tv_date_on_which_conducted.setVisibility(View.GONE);
                    tv_feedback_of_assessment.setVisibility(View.GONE);
                }
                else if(yes_no[item].equalsIgnoreCase("Yes")){
                    ed_type_of_assessment.setVisibility(View.VISIBLE);
                    ed_date_on_which_conducted.setVisibility(View.VISIBLE);
                    ed_feedback_of_assesment.setVisibility(View.VISIBLE);
                    tv_type_of_assessment.setVisibility(View.VISIBLE);
                    tv_date_on_which_conducted.setVisibility(View.VISIBLE);
                    tv_feedback_of_assessment.setVisibility(View.VISIBLE);
                }
            }
        });
        alertDialog.show();
    }

    private void enableEditText() {

        ed_sub_topic_completed.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_describe_lesson_highlights.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_teaching_strategies.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_challenges_faced.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_work_carried_forward.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_reson_for_same.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_all_of_my_students_can.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_most_of_my_students_can.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_some_of_my_students_can.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_few_of_my_students_can.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_yes_no.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_the_topic_was_started_on.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_the_topic_was_completed_on.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_type_of_assessment.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_date_on_which_conducted.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_feedback_of_assesment.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_challenges_faced_by_students.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_suggested_correction_in_text_book.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_suggested_correction_in_work_book.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_suggested_correction_in_lesson_plan.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_suggested_correction_in_internal_assement.setBackgroundResource(R.drawable.simple_editext_dark_bg);
        ed_remark.setBackgroundResource(R.drawable.simple_editext_dark_bg);

        ed_sub_topic_completed.setKeyListener(variable);
        ed_describe_lesson_highlights.setKeyListener(variable);
        ed_teaching_strategies.setKeyListener(variable);
        ed_challenges_faced.setKeyListener(variable);
        ed_work_carried_forward.setKeyListener(variable);
        ed_reson_for_same.setKeyListener(variable);
        ed_all_of_my_students_can.setKeyListener(variable);
        ed_most_of_my_students_can.setKeyListener(variable);
        ed_some_of_my_students_can.setKeyListener(variable);
        ed_few_of_my_students_can.setKeyListener(variable);
        ed_the_topic_was_started_on.setKeyListener(variable);
        ed_the_topic_was_completed_on.setKeyListener(variable);
        ed_yes_no.setKeyListener(variable);
        ed_type_of_assessment.setKeyListener(variable);
        ed_date_on_which_conducted.setKeyListener(variable);
        ed_feedback_of_assesment.setKeyListener(variable);
        ed_challenges_faced_by_students.setKeyListener(variable);
        ed_suggested_correction_in_text_book.setKeyListener(variable);
        ed_suggested_correction_in_work_book.setKeyListener(variable);
        ed_suggested_correction_in_lesson_plan.setKeyListener(variable);
        ed_suggested_correction_in_internal_assement.setKeyListener(variable);
        ed_remark.setKeyListener(variable);
    }

    private void CallWsToViewLog(String crf_id, final String usl_id,String rol_id, final String clt_id, final String academic_year) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getViewLog(crf_id, usl_id, rol_id, clt_id, academic_year, ViewLogMethod_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);

                            if (login_details.Status.equalsIgnoreCase("1")) {

                                if (AppController.firstPeriodNo.equalsIgnoreCase("true")) {
                                    ed_period_no.setText(login_details.PeriodNoList.get(0).id.toString());
                                    selectedPeriodNo = login_details.PeriodNoList.get(0).id.toString();
                                }

                                setPeriodDropDown();
                                setUIData();

                            } else if (login_details.Status.equalsIgnoreCase("0")) {
                                Constant.showOkPopup(TeacherViewLogActivity.this, login_details.StatusMsg, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent back = new Intent(TeacherViewLogActivity.this,SubjectTopicsActivity.class);
                                        back.putExtra("clt_id", clt_id);
                                        back.putExtra("usl_id",usl_id);
                                        back.putExtra("School_name", school_name);
                                        back.putExtra("Teacher_name", teacher_name);
                                        back.putExtra("version_name",version_name);
                                        back.putExtra("board_name", board_name);
                                        back.putExtra("org_id",org_id);
                                        back.putExtra("Teacher_div",teacher_div);
                                        back.putExtra("Teacher_Shift",teacher_shift);
                                        back.putExtra("Tecaher_Std",teacher_std);
                                        back.putExtra("cls_teacher", clas_teacher);
                                        back.putExtra("academic_year", academic_year);
                                        back.putExtra("Subject_name",subject_name);
                                        back.putExtra("Classs",classs);
                                        back.putExtra("class_id",class_id);
                                        startActivity(back);
                                        dialog.dismiss();
                                    }

                                });
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Show error or whatever...
                        Log.d("TeacherBehavrActivity", "ERROR.._---" + error.getCause());
                    }
                });
    }
    private void callWsToViewLogOnPeriodSelection(String crf_id,String selectedPeriodNo,String usl_id,String academic_year,String clt_id,String rol_id) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getViewLogOnPeriodSelection(crf_id, selectedPeriodNo, usl_id, academic_year, clt_id, rol_id, ViewLogOnPeriodSelectionMethod_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);

                            if (login_details.Status.equalsIgnoreCase("1")) {
                                setUIData();

                            } else if (login_details.Status.equalsIgnoreCase("0")) {
                                Constant.showOkPopup(TeacherViewLogActivity.this, login_details.StatusMsg, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }

                                });
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Show error or whatever...
                        Log.d("TeacherViewLogActivity", "ERROR.._---" + error.getCause());
                    }
                });
    }
    private void callWsToSaveLog(final String crf_id, final String selectedPeriodNo, final String usl_id,String field1,String field2,String field3,String field4,String field5,String field6,String field7,String field8,String field9,String field10,String field11,String field12) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getsaveLog(crf_id,selectedPeriodNo, usl_id, field1,field2,field3,field4,field5,field6,field7,field8,field9,field10,field11,field12,SaveLogInfoMethod_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);

                            if (login_details.Status.equalsIgnoreCase("1")) {

                                Constant.showOkPopup(TeacherViewLogActivity.this, login_details.StatusMsg, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        AppController.firstPeriodNo = "true";
                                       // CallWsToViewLog(crf_id, usl_id, rol_id, clt_id, academic_year);
                                        //CallWsToViewLogData
                                        callWsToViewLogOnPeriodSelection(crf_id, selectedPeriodNo, usl_id, academic_year,clt_id,rol_id);
                                        disableEditText();
                                        btn_cancel_log.setVisibility(View.GONE);
                                        btn_save_log.setVisibility(View.GONE);
                                    }

                                });
                            } else if (login_details.Status.equalsIgnoreCase("0")) {
                                Constant.showOkPopup(TeacherViewLogActivity.this, login_details.StatusMsg, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }

                                });
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Show error or whatever...
                        Log.d("TeacherViewLogActivity", "ERROR.._---" + error.getCause());
                    }
                });
    }

    private void setUIData() {
        sub_teacher_name.setText(login_details.TeacherName);
        tv_std.setText(classs);
        tv_subject.setText(subject_name);

        if(login_details.TopicinfoResult.size()==0){
            ll_grater_than_16.setVisibility(View.VISIBLE);
            ll_below_academic_yr_16.setVisibility(View.GONE);
            ed_the_topic_was_started_on.setText(login_details.TopicInfoAcyGrtSixteenResult.get(0).top_started_on);
            ed_the_topic_was_completed_on.setText(login_details.TopicInfoAcyGrtSixteenResult.get(0).top_completed_on);
            ed_yes_no.setText(login_details.TopicInfoAcyGrtSixteenResult.get(0).Int_Ass_based_on_top);
            ed_type_of_assessment.setText(login_details.TopicInfoAcyGrtSixteenResult.get(0).Typ_of_assessment);
            ed_date_on_which_conducted.setText(login_details.TopicInfoAcyGrtSixteenResult.get(0).Dt_which_it_conducted);
            ed_feedback_of_assesment.setText(login_details.TopicInfoAcyGrtSixteenResult.get(0).Feed_of_assessment);
            ed_challenges_faced_by_students.setText(login_details.TopicInfoAcyGrtSixteenResult.get(0).chal_Faced_stud_lern_top);
            ed_suggested_correction_in_text_book.setText(login_details.TopicInfoAcyGrtSixteenResult.get(0).Sugg_corr_chngs_in_txtbok);
            ed_suggested_correction_in_work_book.setText(login_details.TopicInfoAcyGrtSixteenResult.get(0).Sugg_corr_chngs_in_wrkbok);
            ed_suggested_correction_in_lesson_plan.setText(login_details.TopicInfoAcyGrtSixteenResult.get(0).Sugg_corr_chngs_in_Leson_Pln);
            ed_suggested_correction_in_internal_assement.setText(login_details.TopicInfoAcyGrtSixteenResult.get(0).Sugg_corr_chngs_inter_assess);
            ed_remark.setText(login_details.TopicInfoAcyGrtSixteenResult.get(0).Any_other_Remarks);
            if(ed_yes_no.getText().toString().equalsIgnoreCase("No")){
                ed_type_of_assessment.setVisibility(View.GONE);
                ed_feedback_of_assesment.setVisibility(View.GONE);
                ed_date_on_which_conducted.setVisibility(View.GONE);
                tv_type_of_assessment.setVisibility(View.GONE);
                tv_date_on_which_conducted.setVisibility(View.GONE);
                tv_feedback_of_assessment.setVisibility(View.GONE);
            }
            else {
                ed_type_of_assessment.setVisibility(View.VISIBLE);
                ed_date_on_which_conducted.setVisibility(View.VISIBLE);
                ed_feedback_of_assesment.setVisibility(View.VISIBLE);
                tv_type_of_assessment.setVisibility(View.VISIBLE);
                tv_date_on_which_conducted.setVisibility(View.VISIBLE);
                tv_feedback_of_assessment.setVisibility(View.VISIBLE);
            }

        }
        else if(login_details.TopicInfoAcyGrtSixteenResult.size()==0){
            ll_grater_than_16.setVisibility(View.GONE);
            ll_below_academic_yr_16.setVisibility(View.VISIBLE);
            ed_reson_for_same.setText(login_details.TopicinfoResult.get(0).Reason_Fr_Same);
            ed_work_carried_forward.setText(login_details.TopicinfoResult.get(0).Wrk_Carrd_Fward);
            ed_challenges_faced.setText(login_details.TopicinfoResult.get(0).chal_Faced_During_Les);
            ed_describe_lesson_highlights.setText(login_details.TopicinfoResult.get(0).des_ur_Lesson);
            ed_sub_topic_completed.setText(login_details.TopicinfoResult.get(0).sub_Topic_Comp);
            ed_teaching_strategies.setText(login_details.TopicinfoResult.get(0).teaching_Strategies);
            if(selectedPeriodNo.equalsIgnoreCase(String.valueOf(periodsize))){
                ll_last_period.setVisibility(View.VISIBLE);
                ed_all_of_my_students_can.setText(login_details.TopicinfoResult.get(0).All_of_my_stu_can);
                ed_most_of_my_students_can.setText(login_details.TopicinfoResult.get(0).Mst_of_my_stud_can);
                ed_some_of_my_students_can.setText(login_details.TopicinfoResult.get(0).Some_of_my_stud_can);
                ed_few_of_my_students_can.setText(login_details.TopicinfoResult.get(0).Few_of_my_stud_can);
            }
            else {
                ll_last_period.setVisibility(View.GONE);
            }
        }
    }

    private void setPeriodDropDown() {
        try {
            strings_period_no = new ArrayList<String>();
            for (int i = 0; i < login_details.PeriodNoList.size(); i++) {
                strings_period_no.add(login_details.PeriodNoList.get(i).id.toString());

                select_period = new String[strings_period_no.size()];
                select_period = strings_period_no.toArray(select_period);
                selectedPeriodNo = login_details.PeriodNoList.get(0).id;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        selectPeriod(select_period);
        AppController.firstPeriodNo="false";
    }

    private void selectPeriod(final String[] select_period) {
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.dialog_list, null);
            alertDialog.setView(convertView);
            alertDialog.setTitle("Select Period");
            final ListView select_list = (ListView) convertView.findViewById(R.id.select_list);

            alertDialog.setItems(select_period, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    // Do something with the selection
                    ed_period_no.setText(select_period[item]);
                    dialog.dismiss();
                    selectedPeriodNo = select_period[item];
                    periodsize = select_period.length;
                    int lastpos = select_period.length;
                    try {
                        //CallWsToViewLogData
                        callWsToViewLogOnPeriodSelection(crf_id, selectedPeriodNo, usl_id, academic_year,clt_id,rol_id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            if(AppController.firstPeriodNo.equalsIgnoreCase("false")){
                alertDialog.show();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        try {
            Intent back = new Intent(TeacherViewLogActivity.this,SubjectTopicsActivity.class);
            back.putExtra("clt_id", clt_id);
            back.putExtra("usl_id",usl_id);
            back.putExtra("School_name", school_name);
            back.putExtra("Teacher_name", teacher_name);
            back.putExtra("version_name",version_name);
            back.putExtra("board_name", board_name);
            back.putExtra("org_id",org_id);
            back.putExtra("Teacher_div",teacher_div);
            back.putExtra("Teacher_Shift",teacher_shift);
            back.putExtra("Tecaher_Std",teacher_std);
            back.putExtra("cls_teacher", clas_teacher);
            back.putExtra("academic_year", academic_year);
            back.putExtra("Subject_name",subject_name);
            back.putExtra("Classs",classs);
            back.putExtra("class_id",class_id);
            startActivity(back);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
