package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
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
import com.podarbetweenus.Adapter.TeacherAttendanceAdapter;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.Entity.TeacherAttendaceResult;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;
import com.podarbetweenus.Utility.LoadMoreListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class TeacherAttendanceActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener {
// This activity shows
    //Button
    Button btn_announce,btn_dashboard,btn_attendance,btn_behaviour,btn_show_attendance,btn_addAttendance;
    //Linear Layout
    LinearLayout lay_back_investment,ll_footer;
    //RelativeLayout
    RelativeLayout rl_list,rl_title;
    //ImageView
    ImageView btn_back,img_drawer;
    //ListView
   // ListView lv_show_attendance;
    //TextView
    TextView tv_version_name,tv_version_code,tv_cancel,tv_error_msg,tv_standard_value,tv_shift_value,tv_division_value,tv_child_name_drawer,tv_academic_year_drawer,tv_teacher_class;
   //Drawer
    DrawerLayout drawer;
    //Relative Layout
    RelativeLayout leftfgf_drawer,rl_profile;
    //ListView
    ListView drawerListView;
    //ListView to display class deatils whose attendence can be shown
    ListView lv_attendance_selection_list;
    //ProgressDialog
    ProgressDialog progressDialog;
    //Floating Button
    FloatingActionButton fab;

    //LayoutEntities
    HeaderControler header;
    //DrawerLayout
    ContentDisplaceDrawerToggle mContentDisplaceToggle;
    DataFetchService dft;
    LoginDetails login_details;
    TeacherAttendanceAdapter teacher_attendance_adapter;

    String acy_id,usl_id,clt_id,school_name,teacher_name,version_name,board_name,std_id,month_id,div_id,shift_id,class_id,org_id,
            firststd,firstdiv,firstsift,firstmonth,stdDropdownSelected="false",dropDownSelection="false",teacher_div,teacher_std,teacher_shift,
            monthDropDownSelected="false",shiftDropDownSelected="false",divDropDownSelected="false",clas_teacher,academic_year;
    public String Loadmore="";
    int pageIndex=1,notificationID = 1;
    int pageSize=10;
    String Teacher_AttendanceSelectionMethod = "GetTeacherAttendanceDropDtls";
    String ShowAttendanceMethod_name = "TeacherAttendance";
    boolean isLoading = false;
    String[] select_std;
    String[] select_month;
    String[] select_div;
    String[] select_shift;
    String[] data_without_sibling;
    int[] icons_without_sibling;
    ArrayList<String> strings_std = new ArrayList<String>();
    ArrayList<String> strings_month = new ArrayList<String>();
    ArrayList<String> strings_div = new ArrayList<String>();
    ArrayList<String> strings_shift = new ArrayList<String>();

    ArrayList<TeacherAttendaceResult> teacherAttendaceResult_loadMore = new ArrayList<TeacherAttendaceResult>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.teacher_attendance);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        findViews();
        init();
        getIntentData();
        setDrawer();
        lv_attendance_selection_list.setDivider(null);
        lv_attendance_selection_list.setDividerHeight(0);
        try {
            //Call webservice for Dropdown
            callDropdownWebservice(clt_id,usl_id,board_name);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
// shared preferences
    public void setDataForAddAttendenceWS(){
        class_id = login_details.StandardResult.get(0).class_id;
        acy_id = login_details.AcedmicYearResult.get(0).acy_id;
        SharedPreferences pref = getApplicationContext().getSharedPreferences("AttendencePref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("client_id",clt_id);
        editor.putString("class_id", class_id);
        editor.putString("board_name", board_name);
        editor.putString("academic_year",acy_id);
        editor.putString("usl_id",usl_id);

        Log.d("<<setData class_id", class_id);
        Log.d("<<setData Client id", clt_id);
        Log.d("<<setData usl id", usl_id);
        Log.d("<<setData boardname",board_name );
        Log.d("<<setData academic_year",acy_id);
        editor.commit();

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
            org_id = intent.getStringExtra("org_id");
            teacher_div = intent.getStringExtra("Teacher_div");
            teacher_std = intent.getStringExtra("Tecaher_Std");
            teacher_shift = intent.getStringExtra("Teacher_Shift");
            clas_teacher = intent.getStringExtra("cls_teacher");
            academic_year = intent.getStringExtra("academic_year");
            AppController.versionName = version_name;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDrawer() {
        try {
            if(LoginActivity.get_class_teacher(TeacherAttendanceActivity.this).equalsIgnoreCase("1")){
                icons_without_sibling = new int[]{R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sms_48x48, R.drawable.announcementicon_48x48, R.drawable.att_48x48, R.drawable.behaviouricon_48x48,R.drawable.subjectlist_48x48, R.drawable.setting1_hdpi, R.drawable.logout1_hdpi, R.drawable.info};
                data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement", "Attendance", "Behaviour","Subject List","Setting", "Sign Out", "About"};
                drawerListView.setAdapter(new CustomAccount(TeacherAttendanceActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));

                drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherAttendanceActivity.this, TeacherProfileActivity.class);
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
                                Intent teacherProfile = new Intent(TeacherAttendanceActivity.this, TeacherMessageActivity.class);
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
                                Intent teacherProfile = new Intent(TeacherAttendanceActivity.this, TeacherSMSActivity.class);
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
                                Intent teacherannouncment = new Intent(TeacherAttendanceActivity.this, TeacherAnnouncementsActivity.class);
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
                                Intent teacherattendance = new Intent(TeacherAttendanceActivity.this, TeacherAttendanceActivity.class);
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
                                Intent teacher_behaviour = new Intent(TeacherAttendanceActivity.this, TeacherBehaviourActivity.class);
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
                                Intent subject_list = new Intent(TeacherAttendanceActivity.this, SubjectListActivity.class);
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
                        } else if (position == 7) {
                            Intent setting = new Intent(TeacherAttendanceActivity.this, SettingActivity.class);
                            setting.putExtra("usl_id", usl_id);
                            setting.putExtra("version_name", version_name);
                            setting.putExtra("clt_id", clt_id);
                            setting.putExtra("School_name", school_name);
                            setting.putExtra("Teacher_name", teacher_name);
                            setting.putExtra("board_name", board_name);
                            setting.putExtra("academic_year", academic_year);
                            setting.putExtra("cls_teacher", clas_teacher);
                            setting.putExtra("Teacher_div",teacher_div);
                            setting.putExtra("Teacher_Shift",teacher_shift);
                            setting.putExtra("Tecaher_Std",teacher_std);
                            setting.putExtra("org_id", org_id);
                            startActivity(setting);
                        } else if (position == 8) {
                            //Signout
                            Intent signout = new Intent(TeacherAttendanceActivity.this, LoginActivity.class);
                            Constant.SetLoginData("", "", TeacherAttendanceActivity.this);
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
                                final Dialog alertDialog = new Dialog(TeacherAttendanceActivity.this);
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
                data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement","Subject List", "Setting", "Sign Out", "About"};
                drawerListView.setAdapter(new CustomAccount(TeacherAttendanceActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));
                drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherAttendanceActivity.this, TeacherProfileActivity.class);
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
                                Intent teacherProfile = new Intent(TeacherAttendanceActivity.this, TeacherMessageActivity.class);
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
                                Intent teacherProfile = new Intent(TeacherAttendanceActivity.this, TeacherSMSActivity.class);
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
                                Intent teacherannouncment = new Intent(TeacherAttendanceActivity.this, TeacherAnnouncementsActivity.class);
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
                                Intent subject_list = new Intent(TeacherAttendanceActivity.this, SubjectListActivity.class);
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
                            Intent setting = new Intent(TeacherAttendanceActivity.this, SettingActivity.class);
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
                            Intent signout = new Intent(TeacherAttendanceActivity.this, LoginActivity.class);
                            Constant.SetLoginData("", "", TeacherAttendanceActivity.this);
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
                                final Dialog alertDialog = new Dialog(TeacherAttendanceActivity.this);
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
            Constant.setTeacherDrawerData(academic_year, teacher_name,teacher_div,teacher_std,teacher_shift, TeacherAttendanceActivity.this);
            setDrawerData();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setDrawerData() {
        tv_academic_year_drawer.setText(academic_year);
        tv_child_name_drawer.setText(teacher_name);
        if((LoginActivity.get_class_teacher(TeacherAttendanceActivity.this).equalsIgnoreCase("1")))
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
        try {
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
                intent.putExtra("org_id",org_id);
                intent.putExtra("Teacher_div",teacher_div);
                intent.putExtra("Teacher_Shift",teacher_shift);
                intent.putExtra("Tecaher_Std",teacher_std);
                intent.putExtra("cls_teacher",clas_teacher);
                intent.putExtra("academic_year", academic_year);
                intent.putExtra("Sibling", AppController.SiblingListSize);
                startActivity(intent);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void findViews() {
        //Button
        btn_announce = (Button) findViewById(R.id.btn_announce);
        btn_attendance = (Button) findViewById(R.id.btn_attendance);
        btn_behaviour = (Button) findViewById(R.id.btn_behaviour);
        btn_addAttendance = (Button) findViewById(R.id.btn_addAttendance);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#026579")));
        //ImageView
        btn_back = (ImageView) findViewById(R.id.btn_back);
        // Menu icon image
        img_drawer = (ImageView) findViewById(R.id.img_drawer);
        //Linear Layout for top back imagebutton
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
        //
        lv_attendance_selection_list = (ListView) findViewById(R.id.lv_attendance_selection_list);
        //TextView
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_version_code = (TextView) findViewById(R.id.tv_version_code);
        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
        tv_error_msg = (TextView) findViewById(R.id.tv_error_msg);
        tv_child_name_drawer = (TextView) findViewById(R.id.tv_child_name_drawer);
        tv_teacher_class = (TextView) findViewById(R.id.tv_teacher_class);
        tv_academic_year_drawer = (TextView) findViewById(R.id.tv_academic_year_drawer);

        // for drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        leftfgf_drawer = (RelativeLayout) findViewById(R.id.leftfgf_drawer);
        rl_profile = (RelativeLayout) findViewById(R.id.rl_profile);
        drawerListView = (ListView) findViewById(R.id.drawerListView);
        img_drawer = (ImageView) findViewById(R.id.img_drawer); drawerListView = (ListView) findViewById(R.id.drawerListView);
        mContentDisplaceToggle = new ContentDisplaceDrawerToggle(this, drawer,
                R.id.rl_profile, Gravity.START);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
       // int width = dm.widthPixels;
        int width = getResources().getDisplayMetrics().widthPixels/2+30;
        DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) leftfgf_drawer.getLayoutParams();
        lp.width = width + 30;
        leftfgf_drawer.setLayoutParams(lp);

    }

    private void init() {

        header = new HeaderControler(this, true, false, "Attendance");
        progressDialog = Constant.getProgressDialog(this);
        lv_attendance_selection_list.setOnItemClickListener(this);
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        //Button
        btn_announce.setOnClickListener(this);
        btn_attendance.setOnClickListener(this);
        btn_behaviour.setOnClickListener(this);
        btn_addAttendance.setOnClickListener(this);
        fab.setOnClickListener(this);
        //ImageView
        img_drawer.setOnClickListener(this);
        //Linear Layout
        lay_back_investment.setOnClickListener(this);
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        //Button
        btn_behaviour.setBackgroundColor(Color.parseColor("#D6D6CC"));
        fab.setBackgroundColor(Color.parseColor("#026579"));
        btn_attendance.setBackgroundColor(Color.parseColor("#026579"));
        btn_announce.setBackgroundColor(Color.parseColor("#D6D6CC"));
        btn_announce.setTextColor(Color.BLACK);
        btn_attendance.setTextColor(Color.WHITE);
        btn_behaviour.setTextColor(Color.BLACK);
    }
    private void callDropdownWebservice(String clt_id, String usl_id,String board_name) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getTeacherAttendanceDropdown(clt_id, usl_id, board_name, Teacher_AttendanceSelectionMethod, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_details.Status.equalsIgnoreCase("1")) {
                                Log.d("<<Response", "TeacherAttendanceActivity");
                                Log.d("<<Response", response.toString());
                                lv_attendance_selection_list.setVisibility(View.VISIBLE);
                                tv_error_msg.setVisibility(View.GONE);
                                setUIData();
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }


                            } else if (login_details.Status.equalsIgnoreCase("0")) {
                                Log.d("<<Response", "TeacherAttendanceActivity");
                                Log.d("<<Response", "No Records Found");
                                lv_attendance_selection_list.setVisibility(View.GONE);
                                tv_error_msg.setVisibility(View.VISIBLE);
                                tv_error_msg.setText("No Records Found");

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Show error or whatever...
                        Log.d("TeacherAttenActivity", "ERROR.._---" + error.getCause());
                    }
                });

    }

    private void setUIData() {
        teacher_attendance_adapter = new TeacherAttendanceAdapter(TeacherAttendanceActivity.this,login_details.StandardResult,login_details.AcedmicYearResult,login_details.DivisionResult,login_details.MonthResult,login_details.ShiftResult,login_details.SectionList);
        lv_attendance_selection_list.setAdapter(teacher_attendance_adapter);
    }

    @Override
    public void onClick(View v) {
        try {
            if (v == btn_attendance) {
                if (dft.isInternetOn() == true) {
                    Intent teacherattendance = new Intent(TeacherAttendanceActivity.this, TeacherAttendanceActivity.class);
                    teacherattendance.putExtra("clt_id", clt_id);
                    teacherattendance.putExtra("usl_id", usl_id);
                    teacherattendance.putExtra("School_name", school_name);
                    teacherattendance.putExtra("Teacher_name", teacher_name);
                    teacherattendance.putExtra("version_name", AppController.versionName);
                    teacherattendance.putExtra("board_name", board_name);
                    teacherattendance.putExtra("org_id", org_id);
                    teacherattendance.putExtra("Teacher_div",teacher_div);
                    teacherattendance.putExtra("Teacher_Shift",teacher_shift);
                    teacherattendance.putExtra("Tecaher_Std",teacher_std);
                    teacherattendance.putExtra("cls_teacher", clas_teacher);
                    teacherattendance.putExtra("academic_year", academic_year);
                    startActivity(teacherattendance);
                }
            } else if (v == btn_announce) {
                if (dft.isInternetOn() == true) {
                    Intent teacherannouncement = new Intent(TeacherAttendanceActivity.this, TeacherAnnouncementsActivity.class);

                    teacherannouncement.putExtra("clt_id", clt_id);
                    teacherannouncement.putExtra("usl_id", usl_id);
                    teacherannouncement.putExtra("School_name", school_name);
                    teacherannouncement.putExtra("Teacher_name", teacher_name);
                    teacherannouncement.putExtra("version_name", AppController.versionName);
                    teacherannouncement.putExtra("board_name", board_name);
                    teacherannouncement.putExtra("org_id", org_id);
                    teacherannouncement.putExtra("cls_teacher", clas_teacher);
                    teacherannouncement.putExtra("Teacher_div",teacher_div);
                    teacherannouncement.putExtra("Teacher_Shift",teacher_shift);
                    teacherannouncement.putExtra("Tecaher_Std",teacher_std);
                    teacherannouncement.putExtra("academic_year", academic_year);
                    startActivity(teacherannouncement);
                }
            }
            else if(v == btn_addAttendance){
                Intent calendar = new Intent(TeacherAttendanceActivity.this,CalendarActivity.class);
                startActivity(calendar);
            }
            else if (v == btn_behaviour) {
                if (dft.isInternetOn() == true) {
                    Intent teacherbehaviour = new Intent(TeacherAttendanceActivity.this, TeacherBehaviourActivity.class);
                    teacherbehaviour.putExtra("clt_id", clt_id);
                    teacherbehaviour.putExtra("usl_id", usl_id);
                    teacherbehaviour.putExtra("School_name", school_name);
                    teacherbehaviour.putExtra("Teacher_name", teacher_name);
                    teacherbehaviour.putExtra("version_name", AppController.versionName);
                    teacherbehaviour.putExtra("board_name", board_name);
                    teacherbehaviour.putExtra("org_id", org_id);
                    teacherbehaviour.putExtra("Teacher_div",teacher_div);
                    teacherbehaviour.putExtra("Teacher_Shift",teacher_shift);
                    teacherbehaviour.putExtra("Tecaher_Std",teacher_std);
                    teacherbehaviour.putExtra("cls_teacher", clas_teacher);
                    teacherbehaviour.putExtra("academic_year", academic_year);
                    startActivity(teacherbehaviour);
                }
            } else if (v == lay_back_investment) {
                Intent back = new Intent(TeacherAttendanceActivity.this, TeacherProfileActivity.class);
                back.putExtra("clt_id", clt_id);
                back.putExtra("usl_id", usl_id);
                back.putExtra("School_name", school_name);
                back.putExtra("Teacher_name", teacher_name);
                back.putExtra("version_name", AppController.versionName);
                back.putExtra("board_name", board_name);
                back.putExtra("org_id", org_id);
                back.putExtra("Teacher_div",teacher_div);
                back.putExtra("Teacher_Shift",teacher_shift);
                back.putExtra("Tecaher_Std",teacher_std);
                back.putExtra("cls_teacher", clas_teacher);
                back.putExtra("academic_year", academic_year);
                startActivity(back);
            }
            else if(v == fab){
                setDataForAddAttendenceWS();
                Intent calendar = new Intent(TeacherAttendanceActivity.this,CalendarActivity.class);
                startActivity(calendar);
            }
            else if (v == img_drawer) {
                Log.d("<< img_drawer","Clicked!!!");
                drawer.openDrawer(Gravity.RIGHT);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            Intent back = new Intent(TeacherAttendanceActivity.this, TeacherProfileActivity.class);
            back.putExtra("clt_id", clt_id);
            back.putExtra("usl_id", usl_id);
            back.putExtra("School_name", school_name);
            back.putExtra("Teacher_name", teacher_name);
            back.putExtra("version_name", AppController.versionName);
            back.putExtra("board_name", board_name);
            back.putExtra("org_id", org_id);
            back.putExtra("Teacher_div",teacher_div);
            back.putExtra("Teacher_Shift",teacher_shift);
            back.putExtra("Tecaher_Std",teacher_std);
            back.putExtra("cls_teacher", clas_teacher);
            back.putExtra("academic_year", academic_year);
            startActivity(back);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("<<Item row position",String.valueOf(position) );
        try {
            if (dft.isInternetOn() == true) {
                Intent ShowStudentListAttendance = new Intent(TeacherAttendanceActivity.this, AttendanceListActivity.class);
                class_id = login_details.StandardResult.get(position).class_id;
                ShowStudentListAttendance.putExtra("class_id", class_id);
                ShowStudentListAttendance.putExtra("clt_id", clt_id);
                ShowStudentListAttendance.putExtra("usl_id", usl_id);
                ShowStudentListAttendance.putExtra("School_name", school_name);
                ShowStudentListAttendance.putExtra("Teacher_name", teacher_name);
                ShowStudentListAttendance.putExtra("version_name", AppController.versionName);
                ShowStudentListAttendance.putExtra("board_name", board_name);
                ShowStudentListAttendance.putExtra("org_id",org_id);
                ShowStudentListAttendance.putExtra("Teacher_div",teacher_div);
                ShowStudentListAttendance.putExtra("Teacher_Shift",teacher_shift);
                ShowStudentListAttendance.putExtra("Tecaher_Std",teacher_std);
                ShowStudentListAttendance.putExtra("cls_teacher",clas_teacher);
                ShowStudentListAttendance.putExtra("academic_year", academic_year);
                startActivity(ShowStudentListAttendance);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
