package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.Dialog;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.Adapter.Teacher_Add_Attendence_Adapter;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Importing Teacher_Add_Attendence_Adapter to access its static variables to intialise them
import com.podarbetweenus.Adapter.Teacher_Add_Attendence_Adapter;
/**
 * Created by Gayatri on 1/31/2017.
 */
 public class TeacherAddAttendenceActivity extends Activity implements DataTransferInterface ,View.OnClickListener{
    String TAG = "<<TeacherAddAttendence";
    public static ArrayList<String> stud_present_data = new ArrayList<String>();
    public static String selectedAbsentCheckboxes = "";
    public static String selectedSMSCheckbox = "";
    public static String absentReasonEntered = "";
    public static String stud_present_data_final = "";

    public static HashMap absent_reason_map = new HashMap();

    public static String PresentMsd_id = "";
    public static String AbsentMsd_id = "";

    String date, month, year;
    String atn_date = "";
    String mode = "";
    String studentName="";
    String acy_id = "";
    String submit_Attendence_Method_name = "AddUpdateAttendance";
    String clt_id,usl_id,msd_id,board_name,school_name,teacher_name,version_name,class_id,org_id,clas_teacher,academic_year,teacher_shift,teacher_std,teacher_div;

    SharedPreferences resultpreLoginData;

    ProgressDialog progressDialog;

    String showAttendenceList_Method_name = "ViewAttendanceData";

    //UI view Variables
    Button submit_attendence_button;
    ListView add_attendence_list;
    TextView no_records_text;
    DataFetchService dft;
    LoginDetails login_details;
    Teacher_Add_Attendence_Adapter teacher_add_attendence_adapter;
    HeaderControler header;

    // Drawer components
    LinearLayout lay_back_investment;
    DrawerLayout drawer;
    ImageView btn_back,img_drawer;
    int[] icons_without_sibling;
    String[] data_without_sibling;
    //ListView
    ListView drawerListView;
    int notificationID = 1;
    RelativeLayout leftfgf_drawer, rl_profile;
    ContentDisplaceDrawerToggle mContentDisplaceToggle;
    TextView tv_version_name, tv_version_code,tv_cancel;
    TextView tv_academic_year_drawer, tv_child_name_drawer, tv_teacher_class;

    TeacherProfileActivity mTeacherProfileActivity ;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        Log.d("<<Screen touched","Event");
        //teacher_add_attendence_adapter.notify();
        return super.onTouchEvent(event);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_attendance);
        mTeacherProfileActivity = new TeacherProfileActivity();
        findViews();
        init();
        getIntentData();
        getSharedPrefData();
        progressDialog = new ProgressDialog(this);
        dft = new DataFetchService(this);
        setDrawer();
        callWsToShowAttendenceList( atn_date, clt_id, class_id, usl_id);

    }
    //data is stored in TeacherAttendanceActivity
 public void getSharedPrefData(){
     SharedPreferences pref = getApplicationContext().getSharedPreferences("AttendencePref", MODE_PRIVATE);

     clt_id = pref.getString("client_id",null);
     class_id = pref.getString("class_id",null);
     usl_id = pref.getString("usl_id",null);
     acy_id = pref.getString("academic_year",null);

     Log.d("<<TeachAddAttn clt_id", clt_id);
     Log.d("<<TeachAddAttn class_id",class_id);
     Log.d("<<TeachAddAttn usl_id", usl_id);
     Log.d("<<TeachAddAttn acy_id", acy_id);

     resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
     usl_id = mTeacherProfileActivity.get_usl_id(getApplicationContext());
     Log.d("<<usl_id",usl_id);

     clt_id = mTeacherProfileActivity.get_clt_id(getApplicationContext());
     Log.d("<<clt_id",clt_id);

     school_name = mTeacherProfileActivity.get_school_name(getApplicationContext());
     Log.d("<<school_name",school_name);

     teacher_name = mTeacherProfileActivity.get_teacher_name(getApplicationContext());
     Log.d("<<Teacher name",teacher_name);

     version_name = mTeacherProfileActivity.get_version_Name(getApplicationContext());
     Log.d("<<version_name",version_name);

     board_name = mTeacherProfileActivity.get_board_name(getApplicationContext());
     Log.d("<<board_name",board_name);

     org_id = mTeacherProfileActivity.get_org_id(getApplicationContext());
     Log.d("<<org_id",org_id);

     teacher_div = mTeacherProfileActivity.get_teacher_div(getApplicationContext());
     Log.d("<<teacher_div",teacher_div);

     teacher_std = mTeacherProfileActivity.get_teacher_std(getApplicationContext());
     Log.d("<<teacher_std",teacher_std);

     teacher_shift = mTeacherProfileActivity.get_teacher_shift(getApplicationContext());
     Log.d("<<teacher_shift",teacher_shift);

     /*clas_teacher = mTeacherProfileActivity.get_cl(getApplicationContext());
     Log.d("<<clas_teacher",clas_teacher);*/

     academic_year = mTeacherProfileActivity.get_academic_year(getApplicationContext());
     Log.d("<<academic_year",academic_year);

     AppController.versionName = version_name;

 }
    @Override
    protected void onResume() {
        super.onResume();
    }


    private void init() {
        header = new HeaderControler(this, true, false, "Attendance List");
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        progressDialog = Constant.getProgressDialog(this);
        lay_back_investment.setOnClickListener(this);
        tv_child_name_drawer = (TextView) findViewById(R.id.tv_child_name_drawer);
        tv_teacher_class = (TextView) findViewById(R.id.tv_teacher_class);
        tv_academic_year_drawer = (TextView) findViewById(R.id.tv_academic_year_drawer);

        img_drawer.setOnClickListener(this);
        submit_attendence_button.setOnClickListener(this);

    }

    private void findViews() {
        //ImageView
        // submit attendence button
        submit_attendence_button = (Button)findViewById(R.id.submit_attendence_button);
        btn_back = (ImageView) findViewById(R.id.btn_back);
        // Menu icon image
        img_drawer = (ImageView) findViewById(R.id.img_drawer);
        //Linear Layout for top back imagebutton
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerListView = (ListView) findViewById(R.id.drawerListView);
        // For attendence purpose
        add_attendence_list = (ListView) findViewById(R.id.add_attendence_list);
        no_records_text = (TextView) findViewById(R.id.no_records_text);

        // For drawer
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
    private void getIntentData() {
        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        if (mode.equalsIgnoreCase("update")){
            submit_attendence_button.setText("Update Attendance");
        }

        date = intent.getStringExtra("date");
        month = intent.getStringExtra("month");
        int month_modified = Integer.parseInt(month);
        // Since month index starts from 0 we need to add 1 to get current month

        month_modified = month_modified + 1;
        month = String.valueOf(month_modified);
        year = intent.getStringExtra("year");
        Log.d("<<TeacherAddAttn year", String.valueOf(year));

        String yearLastTwoChars = year.length() > 2 ? year.substring(year.length() - 2) : year;

        Log.d("<<yearLastTwoChars", yearLastTwoChars);
        Log.d("<<TeacherAddAttn date", String.valueOf(date));
        Log.d("<<TeacherAddAttn month", String.valueOf(month));

        atn_date ="20"+yearLastTwoChars+"-"+month+"-"+date;
        Log.d("<<TeacherAddAttnDateGen", atn_date);
        AppController.versionName = version_name;
    }

    @Override
    public void onClick(View v) {
        try {
           if (v == lay_back_investment) {
                finish();
            }
            else if (v == img_drawer){
               Log.d("<< img_drawer","Clicked!!!");
               drawer.openDrawer(Gravity.RIGHT);
           }
           else if (v == submit_attendence_button){
               Log.d("<<Submit Atten Button","button pressed");

              // teacher_add_attendence_adapter.notify();


               absent_reason_map = teacher_add_attendence_adapter.absent_reason_map;

               selectedAbsentCheckboxes = teacher_add_attendence_adapter.selectedAbsentCheckboxes;
               Log.d("<<selectAbsentCheckboxe",selectedAbsentCheckboxes);

               List<String> selectedAbsentCheckboxesItems = Arrays.asList(selectedAbsentCheckboxes.split("\\s*,\\s*"));

               stud_present_data = teacher_add_attendence_adapter.stud_present_data;
               for (int i = 0; i <selectedAbsentCheckboxesItems.size() ; i++) {
                   String absentItem = selectedAbsentCheckboxesItems.get(i);
                   if (stud_present_data.contains(absentItem)){
                       stud_present_data.remove(absentItem);
                       Log.d("<<RemovedFrmPresentdata", absentItem);
                   }

               }

               stud_present_data_final = android.text.TextUtils.join(",",stud_present_data);
               PresentMsd_id = stud_present_data_final;
               Log.d("<<present_data_final", stud_present_data_final);

               selectedSMSCheckbox = teacher_add_attendence_adapter.selectedSMSCheckbox;
               Log.d("<<selectedSMSCheckbox",selectedSMSCheckbox);

               absentReasonEntered = teacher_add_attendence_adapter.absent_reason_map.toString();
               Log.d("<<absentReasonEntered",absentReasonEntered);

               //"AbsentMsd_id":[{"Key":"565842","Value":"TT"}, {"Key":"565847","Value":"not well"}]

               ArrayList<String> temp_list_for_AbsentMsd_id = new ArrayList<>();
               // Get a set of the entries
               Set set = teacher_add_attendence_adapter.absent_reason_map.entrySet();

               // Get an iterator
               Iterator i = set.iterator();

               // Display elements
               while(i.hasNext()) {
                   Map.Entry me = (Map.Entry)i.next();
                   //{"Key":"565842","Value":"TT"}
                   //String key_value_pair = "{"+"\"Key\""+":"+"\""+me.getKey()+"\""+","+"\"Value\""+":"+"\""+me.getValue()+"\""+"}";
                   String key_value_pair = me.getKey()+":"+me.getValue();
                   temp_list_for_AbsentMsd_id.add(key_value_pair);
                   Log.d("<<key_value_pair",key_value_pair);
               }
               AbsentMsd_id = android.text.TextUtils.join("|",temp_list_for_AbsentMsd_id);
              // AbsentMsd_id = "["+AbsentMsd_id+"]";
               Log.d("<<AbsentMsd_id",AbsentMsd_id);

               callWsToSubmitAttendence(mode,absent_reason_map, AbsentMsd_id, PresentMsd_id, selectedSMSCheckbox);

           }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void callWsToSubmitAttendence(String mode, HashMap absent_reason_map, String AbsentMsd_id, String PresentMsd_id, String selectedSMSCheckbox) {
        if (dft.isInternetOn() == true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        } else {
            progressDialog.dismiss();
        }
        dft.SubmitAttendenceList(mode, absent_reason_map, atn_date, clt_id, acy_id, usl_id, AbsentMsd_id, PresentMsd_id, selectedSMSCheckbox, submit_Attendence_Method_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_details.Status.equalsIgnoreCase("0")) {
                                Log.d("<<AttendSubmitResponse",response.toString());

                                Constant.showOkPopup(TeacherAddAttendenceActivity.this, "Attendance submitted Successfully", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        Intent AttendanceActivity= new Intent(TeacherAddAttendenceActivity.this, TeacherAttendanceActivity.class);
                                        AttendanceActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        startActivity(AttendanceActivity);
                                        dialogInterface.dismiss();
                                    }
                                });


                            } else if (login_details.Status.equalsIgnoreCase("1")) {

                                Constant.showOkPopup(TeacherAddAttendenceActivity.this, "Attendance not submitted!", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        dialogInterface.dismiss();
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
                        Log.d("LoginActivity", "ERROR.._---" + error.getCause());
                    }
                });
    }

    private void callWsToShowAttendenceList(String atn_date, String clt_id, String cls_id, String usl_id) {
        //Log.d("<<WebServiceparamaters:",clt_id+"," +class_id+","+ studentName);
        if(dft.isInternetOn()==true){
            if (!progressDialog.isShowing()){
                progressDialog.show();
            }
        }else{
            progressDialog.dismiss();
        }
        // orignal atn_date, clt_id, cls_id, usl_id
        // temporary {"atn_date":"02/17/2017","clt_id":"79","cls_id":"21571","usl_id":"16501"}
        dft.getStudentAttendenceList(atn_date, clt_id, cls_id, usl_id, showAttendenceList_Method_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            Log.d("<< TeacherAddAttnAct",response.toString());
                            if (login_details.Status.equalsIgnoreCase("1")) {
                                add_attendence_list.setVisibility(View.VISIBLE);
                                no_records_text.setVisibility(View.GONE);


                                setUIData();
                               // InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                               // imm.hideSoftInputFromWindow(ed_search_name.getWindowToken(), 0);

                            } else if (login_details.Status.equalsIgnoreCase("0")) {
                                //select_all_checkbox.setChecked(false);
                                //   select_all_checkbox.setClickable(false);
                                add_attendence_list.setVisibility(View.GONE);
                                no_records_text.setVisibility(View.VISIBLE);
                                no_records_text.setText("No Records Found!");

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
        Log.d(TAG,"setUIData");

        teacher_add_attendence_adapter = new Teacher_Add_Attendence_Adapter(TeacherAddAttendenceActivity.this,login_details.AttendanceViewResult);
        add_attendence_list.setAdapter(teacher_add_attendence_adapter);

    }

    private void setDrawer(){
        try {
            Log.d("<< SetDrawer","Called");
            if(LoginActivity.get_class_teacher(TeacherAddAttendenceActivity.this).equalsIgnoreCase("1")){
                icons_without_sibling = new int[]{R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sms_48x48, R.drawable.announcementicon_48x48, R.drawable.att_48x48, R.drawable.behaviouricon_48x48,R.drawable.subjectlist_48x48, R.drawable.setting1_hdpi, R.drawable.logout1_hdpi, R.drawable.info};
                data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement", "Attendance", "Behaviour","Subject List","Setting", "Sign Out", "About"};
                drawerListView.setAdapter(new CustomAccount(TeacherAddAttendenceActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));

                drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherAddAttendenceActivity.this, TeacherProfileActivity.class);
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
                                Intent teacherProfile = new Intent(TeacherAddAttendenceActivity.this, TeacherMessageActivity.class);
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
                                Intent teacherProfile = new Intent(TeacherAddAttendenceActivity.this, TeacherSMSActivity.class);
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
                                Intent teacherannouncment = new Intent(TeacherAddAttendenceActivity.this, TeacherAnnouncementsActivity.class);
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
                                Intent teacherattendance = new Intent(TeacherAddAttendenceActivity.this, TeacherAttendanceActivity.class);
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
                                Intent teacher_behaviour = new Intent(TeacherAddAttendenceActivity.this, TeacherBehaviourActivity.class);
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
                                Intent subject_list = new Intent(TeacherAddAttendenceActivity.this, SubjectListActivity.class);
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
                            Intent setting = new Intent(TeacherAddAttendenceActivity.this, SettingActivity.class);
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
                            Intent signout = new Intent(TeacherAddAttendenceActivity.this, LoginActivity.class);
                            Constant.SetLoginData("", "", TeacherAddAttendenceActivity.this);
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
                                final Dialog alertDialog = new Dialog(TeacherAddAttendenceActivity.this);
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
                drawerListView.setAdapter(new CustomAccount(TeacherAddAttendenceActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));
                drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            if (dft.isInternetOn() == true) {
                                Intent teacherProfile = new Intent(TeacherAddAttendenceActivity.this, TeacherProfileActivity.class);
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
                                Intent teacherProfile = new Intent(TeacherAddAttendenceActivity.this, TeacherMessageActivity.class);
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
                                Intent teacherProfile = new Intent(TeacherAddAttendenceActivity.this, TeacherSMSActivity.class);
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
                                Intent teacherannouncment = new Intent(TeacherAddAttendenceActivity.this, TeacherAnnouncementsActivity.class);
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
                                Intent subject_list = new Intent(TeacherAddAttendenceActivity.this, SubjectListActivity.class);
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
                            Intent setting = new Intent(TeacherAddAttendenceActivity.this, SettingActivity.class);
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
                            Intent signout = new Intent(TeacherAddAttendenceActivity.this, LoginActivity.class);
                            Constant.SetLoginData("", "", TeacherAddAttendenceActivity.this);
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
                                final Dialog alertDialog = new Dialog(TeacherAddAttendenceActivity.this);
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
            //Constant.setTeacherDrawerData(academic_year, teacher_name,teacher_div,teacher_std,teacher_shift, TeacherAddAttendenceActivity.this);
            setDrawerData();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void setDrawerData() {
        tv_academic_year_drawer.setText(academic_year);
        tv_child_name_drawer.setText(teacher_name);
        if((LoginActivity.get_class_teacher(TeacherAddAttendenceActivity.this).equalsIgnoreCase("1")))
        {
            tv_teacher_class.setText(teacher_shift + " | " + teacher_std + " | " + teacher_div);
        }
        else{
            tv_teacher_class.setVisibility(View.GONE);
        }
    }
    @Override
    public void setValues(ArrayList<String> studData, ArrayList<String> contactData) {

    }

    @Override
    public void getValues(ArrayList<String> getStudData, ArrayList<String> contactData) {

    }

}
