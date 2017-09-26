package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.AdminDropResult;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.Entity.MsgTeacherResult;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.separator;
import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.studentName;
import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.usl_id;

/**
 * Created by Gayatri on 3/16/2016.
 */
public class AdminWriteMessage extends Activity implements View.OnClickListener,OnItemClickListener{
    //UI Variables
    //Linear Layout
    LinearLayout ll_select_teachers,ll_select_students,ll_send_message;
    //Textview
    TextView tv_no_record,tv_teacher,tv_student,rl_send_sms;
    //ListView
    ListView lv_select_students,lv_select_teachers;
    //EditText
    EditText ed_academic_year,ed_search_name,ed_search_teachers;
    //RadioButton
    RadioButton radio_button_section,radio_button_std,radio_button_shift;
    //ImageView
    ImageView img_search,img_username_teacher_close,img_username_close,img_search_teachers,img_search_by;
    //Relative Layout
    RelativeLayout rl_search_students;
    //CheckBox
    CheckBox select_all_checkbox;

    //Progress dialog
    ProgressDialog progressDialog;

    String clt_id,usl_id,academic_yr, searchkey="",searchValue="",latest_aca_yr,school_name,admin_name,academic_year,board_name,org_id,sender_usl_id,
            version_name,cls_id,std,shift,div,section,pageNo="1",pageSize="200",studentName="",selectAll = "false",selectAllClick="",checkboxClicked="false",curent_aca_yr;
    String AcademicYearMethodName = "GetAdminAcedmicYearList";
    String StudentDetailsMethodName = "GetAdminSMSDropDtls";
    String teacherListMethodName = "GetMessageTeacherList";
    String[] select_academic_yr;
    public static String selectedCheckbox="";
    boolean selectAllCheckboxStatus,check,checkboxStatus;
    public static Set<String> cls_id_data_withoutDuplicates = new HashSet<>();
    ArrayList<String> strings_academic_yr ;

    public static ArrayList<String> sender_usl_ids = new ArrayList<String>();
    public static Set<String> sender_usl_ids_withoutDuplicates = new HashSet<>();
    DataFetchService dft;
    LoginDetails login_details;
    StudentListMessageAdapter studentListMessageAdapter;
    TeacherListAdapter teacherListAdapter;

    @Override
    protected void onResume() {
        Log.d("Inside OnResume", "");
        super.onResume();

        selectedCheckbox="";
        cls_id_data_withoutDuplicates = new HashSet<>();
        sender_usl_ids = new ArrayList<String>();
        sender_usl_ids_withoutDuplicates = new HashSet<>();

        if(ll_select_teachers.getVisibility()==View.VISIBLE) {
            callTeacherListWs(clt_id,usl_id,pageNo,pageSize,studentName);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Inside onRestart", "");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.admin_writemessage_layout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        findViews();
        init();
        getIntentId();
        AppController.currentAcademicYr = "true";
        AppController.dropdownSelected = "false";
        if(AppController.currentAcademicYr.equalsIgnoreCase("true")) {
            //call Ws for Academic Year
            callWsForAcademicYear(clt_id, usl_id);
            //Webservcie call for Student Details
            //callstudentWebservice(clt_id, usl_id, searchkey, searchValue, latest_aca_yr_id);
        }
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

        ed_search_teachers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                img_username_teacher_close.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
    private void init() {
        ed_academic_year.setOnClickListener(this);
        tv_student.setOnClickListener(this);
        tv_teacher.setOnClickListener(this);
        ed_search_name.setOnClickListener(this);
        img_username_teacher_close.setOnClickListener(this);
        img_username_close.setOnClickListener(this);
        img_search_teachers.setOnClickListener(this);
        img_search.setOnClickListener(this);
        img_search_by.setOnClickListener(this);
        rl_send_sms.setOnClickListener(this);
        select_all_checkbox.setOnClickListener(this);
        progressDialog = Constant.getProgressDialog(this);
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        lv_select_students.setOnItemClickListener(this);
        ll_send_message.setOnClickListener(this);
        rl_send_sms.setOnClickListener(this);
    }

    private void findViews() {
        //TextView
        tv_no_record = (TextView) findViewById(R.id.tv_no_record);
        tv_teacher = (TextView) findViewById(R.id.tv_teacher);
        tv_student = (TextView) findViewById(R.id.tv_student);
        rl_send_sms = (TextView) findViewById(R.id.rl_send_sms);
        //Radio Button
        radio_button_shift = (RadioButton) findViewById(R.id.radio_button_shift);
        radio_button_section = (RadioButton) findViewById(R.id.radio_button_section);
        radio_button_std = (RadioButton) findViewById(R.id.radio_button_std);
        //ListView
        lv_select_teachers = (ListView) findViewById(R.id.lv_select_teachers);
        lv_select_students = (ListView) findViewById(R.id.lv_select_students);
        //EditText
        ed_academic_year = (EditText) findViewById(R.id.ed_academic_year);
        ed_search_name = (EditText) findViewById(R.id.ed_search_name);
        ed_search_teachers = (EditText) findViewById(R.id.ed_search_teachers);
        //ImageView
        img_search = (ImageView) findViewById(R.id.img_search);
        img_username_close = (ImageView) findViewById(R.id.img_username_close);
        img_username_teacher_close = (ImageView) findViewById(R.id.img_username_teacher_close);
        img_search_teachers = (ImageView) findViewById(R.id.img_search_teachers);
        img_search_by = (ImageView) findViewById(R.id.img_search_by);
        //LinearLayout
        ll_select_students = (LinearLayout) findViewById(R.id.ll_select_students);
        ll_select_teachers = (LinearLayout) findViewById(R.id.ll_select_teachers);
        ll_send_message = (LinearLayout) findViewById(R.id.ll_send_message);
        //CheckBox
        select_all_checkbox = (CheckBox) findViewById(R.id.select_all_checkbox);
        //Relative layout
        rl_search_students = (RelativeLayout) findViewById(R.id.rl_search_students);
    }
    private void callWsForAcademicYear(final String clt_id, final String usl_id) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getAcademicYearDropdown(clt_id, usl_id, AcademicYearMethodName, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_details.Status.equalsIgnoreCase("1")) {
                                strings_academic_yr = new ArrayList<String>();
                                try {
                                    for (int i = 0; i < login_details.AcedmicYearResult.size(); i++) {
                                        strings_academic_yr.add(login_details.AcedmicYearResult.get(i).acy_Year.toString());
                                        select_academic_yr = new String[strings_academic_yr.size()];
                                        select_academic_yr = strings_academic_yr.toArray(select_academic_yr);
                                        latest_aca_yr = login_details.AcedmicYearResult.get(0).acy_Year;
                                        curent_aca_yr = login_details.AcedmicYearResult.get(0).acy_id;
                                        if(AppController.currentAcademicYr.equalsIgnoreCase("true")) {
                                            //Webservcie call for Student Details
                                            callstudentWebservice(clt_id, usl_id, searchkey, searchValue, curent_aca_yr);
                                            if (AppController.dropdownSelected.equalsIgnoreCase("false")) {

                                                ed_academic_year.setText(latest_aca_yr);
                                            }
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                selectAcademicYear(select_academic_yr);
                                AppController.currentAcademicYr = "false";
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
                        Log.d("SchooLSMSActivity", "ERROR.._---" + error.getCause());
                    }
                });

    }

    private void selectAcademicYear(final String[] select_academic_yr) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.dialog_list, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Select Academic Year");
        final ListView select_list = (ListView) convertView.findViewById(R.id.select_list);

        alertDialog.setItems(select_academic_yr, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                ed_academic_year.setText(select_academic_yr[item]);
                dialog.dismiss();
                String selectedYr = select_academic_yr[item];
                academic_yr = getIDfromAcademicYr(selectedYr);
                try {
                    //Webservcie call for Student Details
                    callstudentWebservice(clt_id, usl_id, searchkey, searchValue, academic_yr);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        if(AppController.currentAcademicYr.equalsIgnoreCase("false")) {
            alertDialog.show();
        }
    }

    private String getIDfromAcademicYr(String selectedYr) {
        String id = "0";
        for(int i=0;i<login_details.AcedmicYearResult.size();i++) {
            if(login_details.AcedmicYearResult.get(i).acy_Year.equalsIgnoreCase(selectedYr)){
                id= login_details.AcedmicYearResult.get(i).acy_id;
            }

        }
        return id;
    }
    private void callstudentWebservice(String clt_id, String usl_id,String searchkey,String searchValue,String latest_aca_yr_id) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getstudentDetails_SMS(clt_id, usl_id, searchkey, searchValue, latest_aca_yr_id, StudentDetailsMethodName, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_details.Status.equalsIgnoreCase("1")) {

                                lv_select_students.setVisibility(View.VISIBLE);
                                tv_no_record.setVisibility(View.GONE);
                                setUIData();
                            } else if (login_details.Status.equalsIgnoreCase("0")) {

                                lv_select_students.setVisibility(View.GONE);
                                tv_no_record.setVisibility(View.VISIBLE);
                                tv_no_record.setText("No Records Found");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Show error or whatever...
                        Log.d("AdminWriteMsgActivity", "ERROR.._---" + error.getCause());
                    }
                });

    }
    private void setUIData() {
        Log.d("<<setUIData", "Called");
        AppController.AdminDropResult = login_details.AdminDropResult;
        studentListMessageAdapter = new StudentListMessageAdapter(AdminWriteMessage.this,login_details.AdminDropResult);
        lv_select_students.setAdapter(studentListMessageAdapter);
    }
    @Override
    public void onClick(View v) {
        if(v==ed_academic_year){
            AppController.dropdownSelected = "true";
            //call Ws for Academic Year
            callWsForAcademicYear(clt_id, usl_id);
        }
        else if(v==tv_student){

            tv_student.setBackgroundResource(R.color.message_listview_odd_row);
            tv_teacher.setBackgroundResource(R.color.message_listview_even_row);
            tv_student.setTextColor(getResources().getColor(R.color.white));
            tv_teacher.setTextColor(getResources().getColor(R.color.black));
            ll_select_teachers.setVisibility(View.GONE);
            ll_select_students.setVisibility(View.VISIBLE);
            rl_search_students.setVisibility(View.GONE);
        }
        else if(v==tv_teacher)
        {
            tv_teacher.setBackgroundResource(R.color.message_listview_odd_row);
            tv_student.setBackgroundResource(R.color.message_listview_even_row);
            tv_teacher.setTextColor(getResources().getColor(R.color.white));
            tv_student.setTextColor(getResources().getColor(R.color.black));
            ll_select_teachers.setVisibility(View.VISIBLE);
            ll_select_students.setVisibility(View.GONE);
            //Call Show Teachers List
            callTeacherListWs(clt_id,usl_id,pageNo,pageSize,studentName);
        }
        if(v==img_search){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            cls_id_data_withoutDuplicates.clear();
            if(radio_button_std.isChecked()==false && radio_button_section.isChecked()==false && radio_button_shift.isChecked()==false || ed_academic_year.getText().toString().equalsIgnoreCase("")){
                Constant.showOkPopup(AdminWriteMessage.this, "Please Select Search Key", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
            if(radio_button_section.isChecked()==true && ed_search_name.getText().toString().equalsIgnoreCase("")){
                Constant.showOkPopup(AdminWriteMessage.this, "Please Enter Search Key", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
            else if(radio_button_std.isChecked()==true && ed_search_name.getText().toString().equalsIgnoreCase("")){
                Constant.showOkPopup(AdminWriteMessage.this, "Please Enter Search Key", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
            else if(radio_button_shift.isChecked() == true && ed_search_name.getText().toString().equalsIgnoreCase("")){
                Constant.showOkPopup(AdminWriteMessage.this, "Please Enter Search Key", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
            else if(radio_button_shift.isChecked()==true && !ed_search_name.getText().toString().equalsIgnoreCase("")){
                searchkey="Shift";
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                searchValue = ed_search_name.getText().toString();
                if (AppController.dropdownSelected.equalsIgnoreCase("false")){
                    academic_yr = curent_aca_yr;
                }
                try {
                    //Webservcie call for Student Details
                    callstudentWebservice(clt_id, usl_id, searchkey, searchValue, academic_yr);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            else if(radio_button_section.isChecked()==true && !ed_search_name.getText().toString().equalsIgnoreCase("")){
                searchkey = "Section";
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                searchValue = ed_search_name.getText().toString();
                if (AppController.dropdownSelected.equalsIgnoreCase("false")){
                    academic_yr = curent_aca_yr;
                }
                try {
                    //Webservcie call for Student Details
                    callstudentWebservice(clt_id, usl_id, searchkey, searchValue, academic_yr);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
            else if(radio_button_std.isChecked()==true && !ed_search_name.getText().toString().equalsIgnoreCase("")){
                searchkey = "Std";
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                searchValue = ed_search_name.getText().toString();
                if (AppController.dropdownSelected.equalsIgnoreCase("false")){
                    academic_yr = curent_aca_yr;
                }

                try {
                    //Webservcie call for Student Details
                    callstudentWebservice(clt_id, usl_id, searchkey, searchValue, academic_yr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        else if(v==img_search_by){
            if(rl_search_students.getVisibility()==View.VISIBLE){
                rl_search_students.setVisibility(View.GONE);
            }
            else {
                rl_search_students.setVisibility(View.VISIBLE);
            }
        }
        else if(v==img_search_teachers){
            select_all_checkbox.setChecked(false);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            studentName = ed_search_teachers.getText().toString();
            try {
                //Call Show Teachers List
                callTeacherListWs(clt_id,usl_id,pageNo,pageSize,studentName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(v==img_username_teacher_close){

            lv_select_teachers.setVisibility(View.VISIBLE);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            ed_search_teachers.setText("");
            studentName = ed_search_teachers.getText().toString();
            img_username_teacher_close.setVisibility(View.INVISIBLE);
            try {
                //Call Show Teachers List
                callTeacherListWs(clt_id, usl_id, pageNo, pageSize, studentName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(v==img_username_close){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            ed_search_name.setText("");
            searchkey = "";
            searchValue = "";
            img_username_close.setVisibility(View.INVISIBLE);
            if (AppController.dropdownSelected.equalsIgnoreCase("false")){
                academic_yr = curent_aca_yr;
            }
            try {
                //Webservcie call for Student Details
                callstudentWebservice(clt_id, usl_id, searchkey, searchValue, academic_yr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(v==rl_send_sms){
            try {
                AppController.showAllStudents = "true";
                Log.e("Text", ed_academic_year.getText().toString());
                if (ed_academic_year.getText().toString().equalsIgnoreCase("")) {
                    Constant.showOkPopup(AdminWriteMessage.this, "Please Select Academic Year", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }

                    });
                } else {
                    for (int i = 0; i < AppController.AdminDropResult.size(); i++) {
                        cls_id = AppController.AdminDropResult.get(i).cls_ID;
                        cls_id_data_withoutDuplicates.add(cls_id);
                    }

                    //Class Id
                    int total = cls_id_data_withoutDuplicates.size() * separator.length();
                    for (String s : cls_id_data_withoutDuplicates) {
                        total += s.length();
                    }
                    StringBuilder rString = new StringBuilder(total);
                    for (String s : cls_id_data_withoutDuplicates) {
                        rString.append(separator).append(s);
                    }
                    cls_id = rString.substring(separator.length());
                    Log.e("cls ID Adminseperated", cls_id);

                    Intent show_studentList = new Intent(AdminWriteMessage.this, AdminStudentListSendMessage.class);
                    show_studentList.putExtra("class_id", cls_id);
                    show_studentList.putExtra("usl_id", usl_id);
                    show_studentList.putExtra("clt_id", clt_id);
                    show_studentList.putExtra("School_name", school_name);
                    show_studentList.putExtra("Admin_name", admin_name);
                    show_studentList.putExtra("org_id", org_id);
                    show_studentList.putExtra("version_name", AppController.versionName);
                    show_studentList.putExtra("board_name", board_name);
                    show_studentList.putExtra("std", std);
                    show_studentList.putExtra("shift", shift);
                    show_studentList.putExtra("section", section);
                    show_studentList.putExtra("academic_year",academic_year);
                    show_studentList.putExtra("div", div);
                    startActivity(show_studentList);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(v==select_all_checkbox){
            try {
                Log.e("Coming","Coming");
                isSelectallCheckboxchecked();
                if (selectAllCheckboxStatus == false) {
                    selectAll = "false";
                    selectAllClick = "false";
                    sender_usl_ids.clear();
                    sender_usl_ids_withoutDuplicates.clear();
                    teacherListAdapter = new TeacherListAdapter(AdminWriteMessage.this, login_details.MsgTeacherResult, selectAllCheckboxStatus, selectAll);
                    lv_select_teachers.setAdapter(teacherListAdapter);
                    for (int i = 0; i < login_details.MsgTeacherResult.size(); i++) {
                        login_details.MsgTeacherResult.get(i).setChecked(false);
                        boolean check = login_details.MsgTeacherResult.get(i).isChecked();
                        Log.e("CHECK Act", String.valueOf(check));
                    }
                    teacherListAdapter.notifyDataSetChanged();
                } else {
                    selectAll = "true";
                    pageSize = "200";
                    selectAllClick = "true";
                    checkboxClicked = "false";
                    teacherListAdapter = new TeacherListAdapter(AdminWriteMessage.this, login_details.MsgTeacherResult, selectAllCheckboxStatus, selectAll);
                    lv_select_teachers.setAdapter(teacherListAdapter);
                    for (int i = 0; i < login_details.MsgTeacherResult.size(); i++) {
                        login_details.MsgTeacherResult.get(i).setChecked(true);
                        boolean check = login_details.MsgTeacherResult.get(i).isChecked();
                        Log.e("CHECK Act", String.valueOf(check));
                        sender_usl_id = login_details.MsgTeacherResult.get(i).usl_Id;
                        sender_usl_ids.add(sender_usl_id);
                        sender_usl_ids_withoutDuplicates.addAll(sender_usl_ids);
                        Log.e("SIZE SenderUslId", String.valueOf(sender_usl_ids_withoutDuplicates.size()));
                        //  String separator = ",";
                        //Student Id
                        int total = sender_usl_ids_withoutDuplicates.size() * separator.length();
                        for (String s : sender_usl_ids_withoutDuplicates) {
                            total += s.length();
                        }
                        StringBuilder rString = new StringBuilder(total);
                        for (String s : sender_usl_ids_withoutDuplicates) {
                            rString.append(separator).append(s);
                        }
                        selectedCheckbox = rString.substring(separator.length());
                        Log.e("SenderUsl ID seperated", selectedCheckbox);
                    }
                    teacherListAdapter.notifyDataSetChanged();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(v==ll_send_message) {
            try {
                teacherListAdapter.notifyDataSetChanged();

                if (selectAllCheckboxStatus == false && sender_usl_ids_withoutDuplicates.size()==0) {
                    Constant.showOkPopup(this, "Please Select Teacher", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }


                    });
                }
                else if(selectAllCheckboxStatus == true && sender_usl_ids_withoutDuplicates.size() == 0){
                    Constant.showOkPopup(this, "Please Select Teacher", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }


                    });
                }
                else {
                    if (dft.isInternetOn() == true) {
                        Intent show_teacherList = new Intent(AdminWriteMessage.this, AdminSendMessageActivity.class);
                        if (selectAllCheckboxStatus == true && check == false && checkboxClicked.equalsIgnoreCase("true")) {
                            sender_usl_id = selectedCheckbox;

                        } else if (selectAllCheckboxStatus == true && check == true && checkboxClicked.equalsIgnoreCase("true")) {
                            sender_usl_id = selectedCheckbox;
                        } else if (selectAllCheckboxStatus == true) {
                            sender_usl_id = "";
                        } else {
                            sender_usl_id = selectedCheckbox;
                        }

                        AppController.AdminsendMessageToStudent = "false";
                        show_teacherList.putExtra("class_id", cls_id);
                        show_teacherList.putExtra("usl_id", usl_id);
                        show_teacherList.putExtra("academic_year",academic_year);
                        show_teacherList.putExtra("clt_id", clt_id);
                        show_teacherList.putExtra("School_name", school_name);
                        show_teacherList.putExtra("Admin_name", admin_name);
                        show_teacherList.putExtra("org_id", org_id);
                        show_teacherList.putExtra("version_name", AppController.versionName);
                        show_teacherList.putExtra("board_name", board_name);
                        show_teacherList.putExtra("std", std);
                        show_teacherList.putExtra("shift", shift);
                        show_teacherList.putExtra("section", section);
                        show_teacherList.putExtra("div", div);
                        show_teacherList.putExtra("SenderUsl_ID",sender_usl_id);
                        Log.e("senderUslId",sender_usl_id);
                        startActivity(show_teacherList);
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void isSelectallCheckboxchecked() {
        selectAllCheckboxStatus = select_all_checkbox.isChecked();
        Log.e("checked", String.valueOf(selectAllCheckboxStatus));

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            AppController.showAllStudents = "false";
            cls_id = AppController.AdminDropResult.get(position).cls_ID;
            std = AppController.AdminDropResult.get(position).std_Name;
            shift = AppController.AdminDropResult.get(position).sft_name;
            section = AppController.AdminDropResult.get(position).sec_Name;
            div = AppController.AdminDropResult.get(position).div_name;
            Intent show_studentList = new Intent(AdminWriteMessage.this, AdminStudentListSendMessage.class);
            show_studentList.putExtra("class_id", cls_id);
            show_studentList.putExtra("usl_id", usl_id);
            show_studentList.putExtra("clt_id", clt_id);
            show_studentList.putExtra("School_name", school_name);
            show_studentList.putExtra("Admin_name", admin_name);
            show_studentList.putExtra("org_id", org_id);
            show_studentList.putExtra("version_name", AppController.versionName);
            show_studentList.putExtra("board_name", board_name);
            show_studentList.putExtra("std", std);
            show_studentList.putExtra("academic_year",academic_year);
            show_studentList.putExtra("shift", shift);
            show_studentList.putExtra("section", section);
            show_studentList.putExtra("div", div);
            startActivity(show_studentList);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void callTeacherListWs(String clt_id, String usl_id,String pageNo,String pageSize,String searchValue) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getTeacherListMessage(clt_id, usl_id, pageNo, pageSize, searchValue, teacherListMethodName, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_details.Status.equalsIgnoreCase("1")) {
                                tv_no_record.setVisibility(View.GONE);
                                {
                                    setTeacherUIData();
                                }
                            } else if (login_details.Status.equalsIgnoreCase("0")) {

                                lv_select_teachers.setVisibility(View.GONE);
                                tv_no_record.setVisibility(View.VISIBLE);
                                tv_no_record.setText("No Records Found");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Show error or whatever...
                        Log.d("AdminWriteMsgActivity", "ERROR.._---" + error.getCause());
                    }
                });

    }

    private void setTeacherUIData() {

        teacherListAdapter = new TeacherListAdapter(AdminWriteMessage.this,login_details.MsgTeacherResult,selectAllCheckboxStatus,selectAll);
        lv_select_teachers.setAdapter(teacherListAdapter);
    }

    public class StudentListMessageAdapter extends BaseAdapter {
        LayoutInflater layoutInflater;
        Context context;
        String date;
        private ArrayList<com.podarbetweenus.Entity.AdminDropResult> AdminDropResult = new ArrayList<com.podarbetweenus.Entity.AdminDropResult>();
        public StudentListMessageAdapter(Context context,
                                     ArrayList<com.podarbetweenus.Entity.AdminDropResult> AdminDropResult) {
            this.context = context;
            this.AdminDropResult = AdminDropResult;
            this.layoutInflater = layoutInflater.from(this.context);
            layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return AdminDropResult.size();
        }

        @Override
        public Object getItem(int position) {
            return AdminDropResult.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            View showLeaveStatus = convertView;

            if (showLeaveStatus == null) {
                holder = new ViewHolder();
                showLeaveStatus = layoutInflater.inflate(R.layout.student_sms_list_item, null);
                holder.tv_shift_value = (TextView) showLeaveStatus.findViewById(R.id.tv_shift_value);
                holder.tv_section_value = (TextView) showLeaveStatus.findViewById(R.id.tv_section_value);
                holder.tv_standard_value = (TextView) showLeaveStatus.findViewById(R.id.tv_standard_value);
                holder.ll_top = (LinearLayout) showLeaveStatus.findViewById(R.id.ll_top);

            } else {
                holder = (ViewHolder) showLeaveStatus.getTag();
            }
            showLeaveStatus.setTag(holder);
            if (position % 2 == 0) {
                holder.ll_top.setBackgroundResource(R.color.message_listview_even_row);

            } else {
                holder.ll_top.setBackgroundResource(R.color.message_listview_odd_row);

            }
            try {
                holder.tv_section_value.setText(AdminDropResult.get(position).sec_Name);
                holder.tv_shift_value.setText(AdminDropResult.get(position).sft_name);
                holder.tv_standard_value.setText(AdminDropResult.get(position).std_Name + "-" + AdminDropResult.get(position).div_name);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            final ViewHolder finalHolder = holder;
            return showLeaveStatus;
        }

        public  class ViewHolder
        {
            TextView tv_standard_value,tv_shift_value,tv_section_value;
            LinearLayout ll_top;
        }

    }

    public class TeacherListAdapter extends BaseAdapter {
        LayoutInflater layoutInflater;
        Context context;
        String date;
        String checkboxClick = "";
        int newPosition;
        boolean[] mCheckedState;
        boolean[] mSelectAllCheckedState;
        private ArrayList<MsgTeacherResult> MsgTeacherResult = new ArrayList<MsgTeacherResult>();
        public TeacherListAdapter(Context context,
                                         ArrayList<MsgTeacherResult> MsgTeacherResult,boolean selectAllCheckboxStatus,String selectAll) {
            this.context = context;
            this.MsgTeacherResult = MsgTeacherResult;
            mCheckedState = new boolean[MsgTeacherResult.size()];
            mSelectAllCheckedState = new boolean[MsgTeacherResult.size()];
            this.layoutInflater = layoutInflater.from(this.context);
            layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return MsgTeacherResult.size();
        }

        @Override
        public Object getItem(int position) {
            return MsgTeacherResult.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            View showLeaveStatus = convertView;

            if (showLeaveStatus == null) {
                holder = new ViewHolder();
                showLeaveStatus = layoutInflater.inflate(R.layout.admin_teacher_list_item, null);
                holder.tv_teacher_name = (TextView) showLeaveStatus.findViewById(R.id.tv_teacher_name);
                holder.ll_top = (LinearLayout) showLeaveStatus.findViewById(R.id.ll_top);
                holder.select_checkbox = (CheckBox) showLeaveStatus.findViewById(R.id.select_checkbox);

            } else {
                holder = (ViewHolder) showLeaveStatus.getTag();
            }
            showLeaveStatus.setTag(holder);
            if (position % 2 == 0) {
                holder.ll_top.setBackgroundResource(R.color.message_listview_even_row);

            } else {
                holder.ll_top.setBackgroundResource(R.color.message_listview_odd_row);

            }

            if(selectAll.equalsIgnoreCase("true"))
            {
                holder.select_checkbox.setChecked(true);
                mCheckedState[position] = true;
            }
            else{
                holder.select_checkbox.setChecked(false);
            }
            try {
                holder.tv_teacher_name.setText(MsgTeacherResult.get(position).StaffName);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            checkboxStatus = holder.select_checkbox.isChecked();
            final ViewHolder finalHolder = holder;
            finalHolder.select_checkbox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d("<<Checking for:" ,MsgTeacherResult.get(position).usl_Id);
                    String checkFor = MsgTeacherResult.get(position).usl_Id;
                    if(selectedCheckbox.contains(checkFor)){
                        Log.d("<<Element Found:","Removing!!!");
                        sender_usl_ids.remove(checkFor);
                        sender_usl_ids_withoutDuplicates.remove(checkFor);
                        mCheckedState[position] = false;
                        MsgTeacherResult.get(position).setChecked(false);
                        selectedCheckbox = android.text.TextUtils.join(",",sender_usl_ids);
                        Log.d("<<After removing", selectedCheckbox);
                    }
                    try {
                        if (((CheckBox) v).isChecked()) {

                            if (selectAll.equalsIgnoreCase("true")) {

                                checkboxClicked = "true";
                                if (finalHolder.select_checkbox.isChecked() == true) {
                                    sender_usl_ids_withoutDuplicates.add(MsgTeacherResult.get(position).usl_Id);
                                } else {
                                    sender_usl_ids_withoutDuplicates.remove(MsgTeacherResult.get(position).usl_Id);
                                    Log.d("<<remoUsl ID seperated", selectedCheckbox);
                                }
                                Log.e("remove size", String.valueOf(sender_usl_ids_withoutDuplicates.size()));
                                int total = sender_usl_ids_withoutDuplicates.size() * separator.length();
                                for (String s : sender_usl_ids_withoutDuplicates) {
                                    total += s.length();
                                }
                                StringBuilder rString = new StringBuilder(total);
                                for (String s : sender_usl_ids_withoutDuplicates) {
                                    rString.append(separator).append(s);
                                }
                                selectedCheckbox = rString.substring(separator.length());
                                Log.e("remoUsl ID seperated", selectedCheckbox);
                                Log.d("<<remoUsl ID seperated", selectedCheckbox);
                            } else {
                                mCheckedState[position] = true;
                                checkboxClick = "true";
                                MsgTeacherResult.get(position).setChecked(true);
                                check = MsgTeacherResult.get(position).isChecked();
                                Log.e("CHECK", String.valueOf(check));
                                checkboxStatus = finalHolder.select_checkbox.isChecked();
                                sender_usl_id = MsgTeacherResult.get(position).usl_Id;
                                sender_usl_ids.add(sender_usl_id);
                                sender_usl_ids_withoutDuplicates.addAll(sender_usl_ids);
                                String separator = ",";
                                int total = sender_usl_ids_withoutDuplicates.size() * separator.length();
                                for (String s : sender_usl_ids_withoutDuplicates) {
                                    total += s.length();
                                }
                                StringBuilder rString = new StringBuilder(total);
                                for (String s : sender_usl_ids_withoutDuplicates) {
                                    rString.append(separator).append(s);
                                }
                                selectedCheckbox = rString.substring(separator.length());
                                // finalHolder.checkbox.setTag(position);
                                Log.d("<<remoUsl ID seperated", selectedCheckbox);
                            }
                        } else {
                            if (selectAll.equalsIgnoreCase("true")) {
                                checkboxClicked = "true";
                                if (finalHolder.select_checkbox.isChecked() == true) {
                                    sender_usl_ids_withoutDuplicates.add(MsgTeacherResult.get(position).usl_Id);
                                } else {
                                    sender_usl_ids_withoutDuplicates.remove(MsgTeacherResult.get(position).usl_Id);
                                }
                                Log.e("remove size", String.valueOf(sender_usl_ids_withoutDuplicates.size()));
                                int total = sender_usl_ids_withoutDuplicates.size() * separator.length();
                                for (String s : sender_usl_ids_withoutDuplicates) {
                                    total += s.length();
                                }
                                StringBuilder rString = new StringBuilder(total);
                                for (String s : sender_usl_ids_withoutDuplicates) {
                                    rString.append(separator).append(s);
                                }
                                selectedCheckbox = rString.substring(separator.length());
                                Log.e("remousl ID seperated", selectedCheckbox);
                                Log.d("<<remoUsl ID seperated", selectedCheckbox);
                            } else if(selectAll.equalsIgnoreCase("true")){
                                MsgTeacherResult.get(position).setChecked(false);
                                check = MsgTeacherResult.get(position).isChecked();
                                Log.e("CHECK", String.valueOf(check));
                                mCheckedState[position] = false;
                                checkboxClick = "true";
                                newPosition = position;
                                if (finalHolder.select_checkbox.isChecked() == true) {
                                    sender_usl_ids_withoutDuplicates.add(MsgTeacherResult.get(position).usl_Id);
                                } else {
                                    sender_usl_ids_withoutDuplicates.remove(MsgTeacherResult.get(position).usl_Id);
                                }
                                Log.e("remove size", String.valueOf(sender_usl_ids_withoutDuplicates.size()));
                                int total = sender_usl_ids_withoutDuplicates.size() * separator.length();
                                for (String s : sender_usl_ids_withoutDuplicates) {
                                    total += s.length();
                                }
                                StringBuilder rString = new StringBuilder(total);
                                for (String s : sender_usl_ids_withoutDuplicates) {
                                    rString.append(separator).append(s);
                                }
                                selectedCheckbox = rString.substring(separator.length());
                                Log.d("<<remoUsl ID seperated", selectedCheckbox);
                            }
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }

            });
            finalHolder.select_checkbox.setChecked(MsgTeacherResult.get(position).isChecked());
            return showLeaveStatus;
        }
        }

        public  class ViewHolder
        {
            TextView tv_teacher_name;
            LinearLayout ll_top,ll_checkbox;
            CheckBox select_checkbox;
        }

    @Override
    public void onBackPressed() {
        try {
            Intent back = new Intent(AdminWriteMessage.this, AdminProfileActivity.class);
            AppController.AdminstudentSMS = "false";
            AppController.teacher_sms = "false";
            AppController.AdminschoolSMS = "false";
            back.putExtra("usl_id", usl_id);
            back.putExtra("clt_id", clt_id);
            back.putExtra("School_name", school_name);
            back.putExtra("Admin_name", admin_name);
            back.putExtra("org_id", org_id);
            back.putExtra("academic_year",academic_year);
            back.putExtra("version_name", AppController.versionName);
            back.putExtra("board_name", board_name);
            startActivity(back);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
