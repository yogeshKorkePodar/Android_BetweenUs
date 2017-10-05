package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.AdminDropResult;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.academic_year;
import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.check;
import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.checkboxClicked;
import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.checkboxStatus;
import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.class_id_data;
import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.selectedCheckbox;
import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.selectedcheckboxClassId;
import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.separator;
import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.stud_id;
import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.stud_id_data;
import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.stud_id_data_withoutDuplicates;

/**
 * Created by Gayatri on 3/15/2016.
 */
public class SchoolSMSActivity extends Activity implements View.OnClickListener,DataTransferInterface{
    //UI Variables
    public static int level = 0;
    //EditTExt
    EditText ed_select_category,ed_select_subject,ed_select_academic_year,ed_search_name,enter_teacher_sms,enter_direct_sms;
    //ListView
    ListView lv_student_selection,lv_send_sms_list;
    //ImageView
    ImageView img_search,img_username_close,img_search_by,rl_send_sms;
    //Relative Layout
    RelativeLayout rl_student,rl_teacher,rl_direct;
    //CheckBox
    CheckBox select_all_checkbox;
    //Button
    Button btn_send_teacher_sms,btn_send_direct_sms,btn_attachment;
    //RadioButton
    RadioButton radio_button_section,radio_button_std,radio_button_shift;
    //TextView
    TextView tv_no_record,tv_charachter_length,tv_select_student,tv_select_teacher,tv_select_direct,tv_download_sample_file,tv_direct_charachter_length,tv_charachter_note_direct,tv_charachter_note;
    //Progress Dialog
    ProgressDialog progressDialog;
    //Relative Layout
    RelativeLayout rl_search_students;
    // Progress Dialog
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;

    DataFetchService dft;
    LoginDetails login_details;
    StudentDetailSMSAdapter studentDetailSMSAdapter;
    ArrayList<String> strings_category ;
    public static String usl_id,clt_id,board_name,org_id,school_name,admin_name,version_name,academic_yr,latest_aca_yr_id,latest_aca_yr,class_id,
            searchkey="",searchValue="",firstTimeCategorySelection,selectAll="false",message_content,mode,contactlist,FilePath,
            filename,base64Value,extension,entityResponse,filePath,responseStatus,dropdownSelection="false";
    long length;
    Context mcontext;

    JSONObject jsonResponse;
    String selectModule_Method_name = "GetAdminSMSTemplate";
    String AcademicYearMethodName = "GetAdminAcedmicYearList";
    String StudentDetailsMethodName = "GetAdminSMSDropDtls";
    String AdminSendSMSToTeachersMethod_name = "AdminSendSMS";
    String AdminSendDirectSMSMethod_name = "AdminSendDirectSMS";
    String UploadFileMethod_name = "UploadSmsCsvFile";
    String sample_file_url = "http://www.betweenus.in/Administrator/Files/sms_sample.csv";
    public static String[] select_academic_yr;
    public static String selectedYr = "";
    String[] select_module;
    boolean selectAllCheckboxStatus,status;
    boolean[] mCheckedState;
    ArrayList<String> strings_academic_yr ;
    ArrayList<String> strings_module = new ArrayList<String>();
    public static Set<String> cls_id_data_withoutDuplicates = new HashSet<>();

    @Override
    protected void onResume() {
        super.onResume();

        selectedcheckboxClassId = "";
        class_id_data.clear();
        cls_id_data_withoutDuplicates.clear();

        if(level==1) {

        } else{
            callWsForAcademicYear(clt_id, usl_id);
        }

        //selectAcademicYear(select_academic_yr);
        // 3rd callstudentWebservice(clt_id, usl_id, searchkey, searchValue, academic_yr);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        level = 1;
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.admin_school_sms_layout);
        findViews();
        init();
        getIntentId();
        firstTimeCategorySelection = "false";
        selectCategoryDialog();
        selectAll = "false";
        cls_id_data_withoutDuplicates.clear();
        class_id_data.clear();
        AppController.currentAcademicYr = "true";
        AppController.dropdownSelected = "false";
        AppController.AdminstudentSMS = "false";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        radio_button_std.setHighlightColor(Color.parseColor("#ffffff"));
        radio_button_section.setHighlightColor(Color.parseColor("#ffffff"));
        radio_button_shift.setHighlightColor(Color.parseColor("#ffffff"));
        try{
            StrictMode.ThreadPolicy policy1 = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy1);
            String str = android.os.Build.MODEL;
            getDeviceName();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(AppController.currentAcademicYr.equalsIgnoreCase("true")) {
            //call Ws for Academic Year
            callWsForAcademicYear(clt_id, usl_id);
        }

        try {
            //Call webservice for Module
            callAdminSMSTemplatetWebservice(mcontext);
        }
        catch (Exception e){
            e.printStackTrace();
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

    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        ed_select_category.setOnClickListener(this);
        ed_search_name.setOnClickListener(this);
        ed_select_subject.setOnClickListener(this);
        rl_send_sms.setOnClickListener(this);
        select_all_checkbox.setOnClickListener(this);
        img_search.setOnClickListener(this);
        img_search_by.setOnClickListener(this);
        radio_button_std.setOnClickListener(this);
        radio_button_section.setOnClickListener(this);
        radio_button_shift.setOnClickListener(this);
        ed_select_academic_year.setOnClickListener(this);
        progressDialog = Constant.getProgressDialog(this);
        img_username_close.setOnClickListener(this);
        btn_send_teacher_sms.setOnClickListener(this);
        btn_send_direct_sms.setOnClickListener(this);
        btn_attachment.setOnClickListener(this);
        tv_download_sample_file.setOnClickListener(this);
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        tv_charachter_note.setText("Note: Do not Use (&,+,%,=,#,' )");
        tv_charachter_note_direct.setText("Note: Do not Use (&,+,%,=,#,' )");
    }

    private void findViews() {
        //EditText
        ed_select_category = (EditText) findViewById(R.id.ed_select_category);
        ed_select_academic_year = (EditText) findViewById(R.id.ed_select_academic_year);
        ed_search_name = (EditText) findViewById(R.id.ed_search_name);
        enter_direct_sms = (EditText) findViewById(R.id.enter_message);
        enter_teacher_sms = (EditText) findViewById(R.id.enter_teacher_sms);
        ed_select_subject = (EditText) findViewById(R.id.ed_select_subject);
        //ListView
        lv_send_sms_list = (ListView) findViewById(R.id.lv_send_sms_list);
        //RelativeLayout
        rl_student = (RelativeLayout) findViewById(R.id.rl_student);
        rl_teacher = (RelativeLayout) findViewById(R.id.rl_teacher);
        rl_direct = (RelativeLayout) findViewById(R.id.rl_direct);
        rl_search_students = (RelativeLayout) findViewById(R.id.rl_search_students);
        //ImageView
        img_search = (ImageView) findViewById(R.id.img_search);
        img_username_close = (ImageView) findViewById(R.id.img_username_close);
        img_search_by = (ImageView) findViewById(R.id.img_search_by);
        rl_send_sms = (ImageView) findViewById(R.id.rl_send_sms);
        //CheckBox
        select_all_checkbox = (CheckBox) findViewById(R.id.select_all_checkbox);
        //Radio Button
        radio_button_shift = (RadioButton) findViewById(R.id.radio_button_shift);
        radio_button_section = (RadioButton) findViewById(R.id.radio_button_section);
        radio_button_std = (RadioButton) findViewById(R.id.radio_button_std);
        //txtView
        tv_no_record = (TextView) findViewById(R.id.tv_no_record);
        tv_charachter_length = (TextView) findViewById(R.id.tv_charachter_length);
        tv_download_sample_file = (TextView) findViewById(R.id.tv_download_sample_file);
        tv_direct_charachter_length = (TextView) findViewById(R.id.tv_direct_charachter_length);
        tv_charachter_note_direct = (TextView) findViewById(R.id.tv_charachter_note_direct);
        tv_charachter_note = (TextView) findViewById(R.id.tv_charachter_note);
        //Button
        btn_attachment = (Button) findViewById(R.id.btn_attachment);
        btn_send_direct_sms = (Button) findViewById(R.id.btn_send_direct_sms);
        btn_send_teacher_sms = (Button) findViewById(R.id.btn_send_teacher_sms);
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
                                        latest_aca_yr_id = login_details.AcedmicYearResult.get(0).acy_id;
                                        latest_aca_yr = login_details.AcedmicYearResult.get(0).acy_Year;
                                        if(AppController.currentAcademicYr.equalsIgnoreCase("true")){
                                            //Webservcie call for Student Details
                                            callstudentWebservice(clt_id, usl_id,searchkey,searchValue,latest_aca_yr_id);
                                            if (AppController.dropdownSelected.equalsIgnoreCase("false")) {

                                                ed_select_academic_year.setText(latest_aca_yr);
                                            }
                                        }
                                    }

                                } catch (Exception e){
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
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.dialog_list, null);
            alertDialog.setView(convertView);
            alertDialog.setTitle("Select Academic Year");
            final ListView select_list = (ListView) convertView.findViewById(R.id.select_list);

            alertDialog.setItems(select_academic_yr, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    // Do something with the selection
                    ed_select_academic_year.setText(select_academic_yr[item]);
                    dialog.dismiss();
                    selectedYr = select_academic_yr[item];
                    Log.e("DATE", selectedYr);
                    academic_yr = getIDfromAcademicYr(selectedYr);
                    //Webservcie call for Student Details
                    callstudentWebservice(clt_id, usl_id, searchkey, searchValue, academic_yr);
                    Log.e("ed", "ed");

                }
            });
            if (AppController.currentAcademicYr.equalsIgnoreCase("false")) {
                alertDialog.show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getIDfromAcademicYr(String selectedYr) {
        String id = "0";
        for(int i=0;i<login_details.AcedmicYearResult.size();i++) {
            try {
                if (login_details.AcedmicYearResult.get(i).acy_Year.equalsIgnoreCase(selectedYr)) {
                    id = login_details.AcedmicYearResult.get(i).acy_id;
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return id;
    }
    private void callstudentWebservice(String clt_id, String usl_id,String searchkey,String searchValue,String latest_aca_yr_id) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        } else {
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

                                lv_send_sms_list.setVisibility(View.VISIBLE);
                                tv_no_record.setVisibility(View.GONE);
                                setUIData();
                            } else if (login_details.Status.equalsIgnoreCase("0")) {

                                lv_send_sms_list.setVisibility(View.GONE);
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
                        Log.e("SchooLSMSActivity", "ERROR.._---" + error.getCause());
                    }
                });

    }
    private void setUIData() {
        studentDetailSMSAdapter = new StudentDetailSMSAdapter(SchoolSMSActivity.this, this,login_details.AdminDropResult, selectAllCheckboxStatus, selectAll, clt_id, usl_id, school_name, admin_name, version_name, board_name);
        lv_send_sms_list.setAdapter(studentDetailSMSAdapter);
    }

    private void isSelectallCheckboxchecked() {
        selectAllCheckboxStatus = select_all_checkbox.isChecked();
    }

    @Override
    public void onClick(View v) {
        if (v == ed_select_category) {
            firstTimeCategorySelection = "true";
            selectCategoryDialog();
        } else if (v == img_search) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            if (radio_button_std.isChecked() == false && radio_button_section.isChecked() == false && radio_button_shift.isChecked() == false) {
                Constant.showOkPopup(SchoolSMSActivity.this, "Please Select Search Key", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
            else if(radio_button_shift.isChecked() == true && ed_search_name.getText().toString().equalsIgnoreCase("")){
                Constant.showOkPopup(SchoolSMSActivity.this, "Please Enter Search Key", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
            else if(radio_button_section.isChecked() == true && ed_search_name.getText().toString().equalsIgnoreCase("")){
                Constant.showOkPopup(SchoolSMSActivity.this, "Please Enter Search Keyr", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
            else if(radio_button_std.isChecked() == true && ed_search_name.getText().toString().equalsIgnoreCase("")){
                Constant.showOkPopup(SchoolSMSActivity.this, "Please Enter Search Key", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
            else if (radio_button_shift.isChecked() == true && !ed_search_name.getText().toString().equalsIgnoreCase("")) {
                searchkey = "Shift";
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                searchValue = ed_search_name.getText().toString();
                if (AppController.dropdownSelected.equalsIgnoreCase("false")){
                    academic_yr = latest_aca_yr_id;
                }
                try {
                    //Webservcie call for Student Details
                    callstudentWebservice(clt_id, usl_id, searchkey, searchValue, academic_yr);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            } else if (radio_button_section.isChecked() == true && !ed_search_name.getText().toString().equalsIgnoreCase("")) {
                searchkey = "Section";
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                searchValue = ed_search_name.getText().toString();
                if (AppController.dropdownSelected.equalsIgnoreCase("false")){
                    academic_yr = latest_aca_yr_id;
                }
                try {
                    //Webservcie call for Student Details
                    callstudentWebservice(clt_id, usl_id, searchkey, searchValue, academic_yr);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            } else if (radio_button_std.isChecked() == true && !ed_search_name.getText().toString().equalsIgnoreCase("")) {
                searchkey = "Std";
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                searchValue = ed_search_name.getText().toString();
                if (AppController.dropdownSelected.equalsIgnoreCase("false")){
                    academic_yr = latest_aca_yr_id;
                }
                try {
                    //Webservcie call for Student Details
                    callstudentWebservice(clt_id, usl_id, searchkey, searchValue, academic_yr);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        } else if (v == rl_send_sms) {
            level++;
            try{
                studentDetailSMSAdapter.notifyDataSetChanged();
                if(cls_id_data_withoutDuplicates.size()==0){
                    Constant.showOkPopup(this, "Please Select Student", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }

                    });
                }
                else {
                    if (dft.isInternetOn() == true) {
                        Intent sendsms = new Intent(SchoolSMSActivity.this, AdminSendSchoolSMSStudent.class);
                        if (selectAllCheckboxStatus == true && check == false && checkboxClicked.equalsIgnoreCase("true")) {
                            stud_id = selectedCheckbox;
                            class_id = selectedcheckboxClassId;
                        } else if (selectAllCheckboxStatus == true && check == true && checkboxClicked.equalsIgnoreCase("true")) {
                            stud_id = selectedCheckbox;
                            class_id = selectedcheckboxClassId;

                        } else if (selectAllCheckboxStatus == true) {
                            stud_id = "0";
                            class_id = selectedcheckboxClassId;
                        } else {
                            stud_id = selectedCheckbox;
                            class_id = selectedcheckboxClassId;
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
                        Log.d("<<send clsid", class_id);
                        startActivity(sendsms);
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        } else if (v == radio_button_section) {

            radio_button_section.setChecked(true);

        } else if (v == radio_button_shift) {

            radio_button_shift.setChecked(true);

        } else if (v == radio_button_std) {

            radio_button_std.setChecked(true);

        } else if (v == ed_select_academic_year) {

            AppController.dropdownSelected = "true";
            select_all_checkbox.setChecked(false);
            try {
                //call Ws for Academic Year
                callWsForAcademicYear(clt_id, usl_id);
            }
            catch (Exception e){
                e.printStackTrace();
            }

        } else if (v == img_username_close) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            cls_id_data_withoutDuplicates.clear();
            class_id_data.clear();
            ed_search_name.setText("");
            searchkey = "";
            searchValue = "";
            select_all_checkbox.setChecked(false);
            img_username_close.setVisibility(View.INVISIBLE);
            if (AppController.dropdownSelected.equalsIgnoreCase("false")){
                academic_yr = latest_aca_yr_id;
            }
            try {
                //Webservcie call for Student Details
                callstudentWebservice(clt_id, usl_id, searchkey, searchValue, academic_yr);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        } else if (v == select_all_checkbox) {
            try {
                isSelectallCheckboxchecked();
                select_all_checkbox.setClickable(true);
                status = select_all_checkbox.isChecked();
                if (selectAllCheckboxStatus == false) {
                    selectAll = "false";
                    selectedcheckboxClassId = "";
                    stud_id_data.clear();
                    class_id_data.clear();
                    stud_id_data_withoutDuplicates.clear();
                    cls_id_data_withoutDuplicates.clear();
                    studentDetailSMSAdapter = new StudentDetailSMSAdapter(SchoolSMSActivity.this, this, login_details.AdminDropResult, selectAllCheckboxStatus, selectAll, clt_id, usl_id, school_name, admin_name, version_name, board_name);
                    lv_send_sms_list.setAdapter(studentDetailSMSAdapter);
                    for (int i = 0; i < login_details.AdminDropResult.size(); i++) {
                        login_details.AdminDropResult.get(i).setChecked(false);
                        boolean check = login_details.AdminDropResult.get(i).isChecked();
                        Log.e("CHECK Act", String.valueOf(check));
                    }
                    studentDetailSMSAdapter.notifyDataSetChanged();
                } else {
                    selectAll = "true";
                    checkboxClicked = "false";
                    studentDetailSMSAdapter = new StudentDetailSMSAdapter(SchoolSMSActivity.this, this, login_details.AdminDropResult, selectAllCheckboxStatus, selectAll, clt_id, usl_id, school_name, admin_name, version_name, board_name);
                    lv_send_sms_list.setAdapter(studentDetailSMSAdapter);
                    for (int i = 0; i < login_details.AdminDropResult.size(); i++) {
                        login_details.AdminDropResult.get(i).setChecked(true);
                        boolean check = login_details.AdminDropResult.get(i).isChecked();
                        Log.e("CHECK Act", String.valueOf(check));
                        class_id = login_details.AdminDropResult.get(i).cls_ID;
                        class_id_data.add(class_id);
                        cls_id_data_withoutDuplicates.addAll(class_id_data);
                        Log.e("SIZE cls", String.valueOf(cls_id_data_withoutDuplicates.size()));
                        //Class Id
                        int classIdtotal = cls_id_data_withoutDuplicates.size() * separator.length();
                        for (String s : cls_id_data_withoutDuplicates) {
                            classIdtotal += s.length();
                        }
                        StringBuilder classIdrString = new StringBuilder(classIdtotal);
                        for (String s : cls_id_data_withoutDuplicates) {
                            classIdrString.append(separator).append(s);
                        }
                        selectedcheckboxClassId = classIdrString.substring(separator.length());
                        Log.e("Class ID seperated", selectedcheckboxClassId);
                    }
                    studentDetailSMSAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(v==btn_send_teacher_sms){
            try {
                message_content = enter_teacher_sms.getText().toString();

                if (message_content.equalsIgnoreCase("")) {
                    Constant.showOkPopup(SchoolSMSActivity.this, "SMS can not be empty", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }

                    });
                } else {
                    mode = "Teacher";
                    message_content = "Dear Teacher,Kindly Note" + enter_teacher_sms.getText().toString();
                    Pattern p = Pattern.compile("[&,+,%,=,#,']");
                    Matcher m = p.matcher(enter_teacher_sms.getText().toString());
                    // boolean b = m.matches();
                    boolean b = m.find();
                    if (b == true) {
                        Toast.makeText(SchoolSMSActivity.this, "There is a special character in SMS", Toast.LENGTH_SHORT).show();
                    } else {
                        //call ws to send SMS to teachers
                        callWStoSendSMSToTeachers(clt_id, usl_id, message_content, board_name, mode);
                    }
                }
            }
          catch (Exception e){
              e.printStackTrace();
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
        else if(v==btn_send_direct_sms){
            try {
                message_content = enter_direct_sms.getText().toString();

                if (message_content.equalsIgnoreCase("")) {
                    Constant.showOkPopup(SchoolSMSActivity.this, "SMS can not be empty", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }

                    });
                } else {
                    mode = "Teacher";
                    ed_select_subject.getText().toString();
                    message_content = ed_select_subject.getText().toString() + enter_direct_sms.getText().toString();
                    Pattern p = Pattern.compile("[&,+,%,=,#,']");
                    Matcher m = p.matcher(enter_direct_sms.getText().toString());
                    // boolean b = m.matches();
                    boolean b = m.find();
                    if (b == true) {
                        Toast.makeText(SchoolSMSActivity.this, "There is a special character in SMS", Toast.LENGTH_SHORT).show();
                    } else {
                        //call ws to send SMS Direct
                        callWStoSendSMSDirect(contactlist, clt_id, message_content, usl_id);
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(v==tv_download_sample_file){
            try {
                new DownloadFileFromURL().execute(sample_file_url);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(v==btn_attachment){
            if (getDeviceName().contains("Samsung")) {
                Intent intent1 = new Intent("com.sec.android.app.myfiles.PICK_DATA");
                intent1.putExtra("CONTENT_TYPE", "*/*");
                intent1.addCategory(Intent.CATEGORY_DEFAULT);
                startActivityForResult(intent1, 1);
            } else {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                Intent i = Intent.createChooser(intent, "View Default File Manager");
                startActivityForResult(i, 1);
            }
        }
        else  if(v == ed_select_subject){
            strings_module.clear();
            dropdownSelection="true";
            //Call webservice for Module
            callAdminSMSTemplatetWebservice(mcontext);
        }
    }
    public void callAdminSMSTemplatetWebservice(Context mcontext) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getModuleList(selectModule_Method_name, Request.Method.GET,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                        if (login_details.Status.equalsIgnoreCase("1"))
                            strings_module = new ArrayList<String>();

                        if (dropdownSelection.equalsIgnoreCase("false")) {
                            ed_select_subject.setText(login_details.SMSTemplateResult.get(0).Stm_template_name);
                        }
                        try {
                            for (int i = 0; i < login_details.SMSTemplateResult.size() ; i++) {

                                strings_module.add(login_details.SMSTemplateResult.get(i).Stm_template_name);
                                select_module = new String[strings_module.size()];
                                select_module = strings_module.toArray(select_module);

                            }
                            selectModule(select_module);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("SendSMSActivity", "" + volleyError);

                    }


                }, null);
    }

    private void selectModule(final String[] select_module) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.dialog_list, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Select Module");
        final ListView select_list = (ListView) convertView.findViewById(R.id.select_list);

        alertDialog.setItems(select_module, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                ed_select_subject.setText(select_module[item]);
                dialog.dismiss();
                String selectedModule = select_module[item];
                Log.e("Module", selectedModule);
            }
        });
        if(dropdownSelection.equalsIgnoreCase("true")){
            alertDialog.show();
        }

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Fix no activity available
        if (data == null)
            return;
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    FilePath = data.getData().getPath();
                    //FilePath is your file as a string
                    Log.e("FilePath", FilePath);
                    filename = FilePath.substring(FilePath.lastIndexOf("/") + 1);
                    File file = new File(FilePath);
                    length = file.length();
                    length = length / 1024;
                    Log.e(" File size : ", length + " KB");
                    if (!filename.contains(".")) {
                        Toast.makeText(SchoolSMSActivity.this, "Not valid file", Toast.LENGTH_LONG).show();
                    }
                    else {
                        extension = filename.substring(filename.lastIndexOf("."));
                        Log.e("extension", extension);
                        Uri.fromFile(new File(FilePath));
                        Log.e("URI", String.valueOf(Uri.fromFile(new File(FilePath))));
                    }
                    //convert to BAse64
                    try {
                        InputStream inputStream = null;//You can get an inputStream using any IO API
                        inputStream = new FileInputStream(FilePath);
                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        ByteArrayOutputStream output = new ByteArrayOutputStream();
                        Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);
                        try {
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                output64.write(buffer, 0, bytesRead);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        output64.close();

                        base64Value = output.toString();
                        Log.e("base64 after", base64Value);

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    //Read a file
                    File sdcard = Environment.getExternalStorageDirectory();
                    //Get the text file
                    File file1 = new File(sdcard,filename);
                    //Read text from file
                    StringBuilder text = new StringBuilder();

                    try {
                        BufferedReader br = new BufferedReader(new FileReader(file1));
                        String line;

                        while ((line = br.readLine()) != null) {
                            text.append(line);
                            text.append(',');
                            Log.e("text",String.valueOf(text));
                            contactlist = String.valueOf(text);
                        }
                        br.close();
                    }
                    catch (IOException e) {
                        //You'll need to add proper error handling here
                        e.printStackTrace();
                    }
                    //callWebservice to upload data
                  //  WscallToUploadFile(clt_id,usl_id,base64Value);

                    //Upload File
                    if(length>500){
                        Toast.makeText(SchoolSMSActivity.this,"File size is greater than 500KB",Toast.LENGTH_LONG).show();
                    }
                    else if (!(extension.equalsIgnoreCase(".csv"))) {
                        Toast.makeText(SchoolSMSActivity.this, "Upload only CSV Document", Toast.LENGTH_LONG).show();
                    }
                    else{
                        new TheTask().execute("http://www.betweenus.in/PODARAPP/PodarApp.svc/UploadSmsCsvFile");
                    }
                }
        }
    }
    class TheTask extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // update textview here
            //  textView.setText("Server message is "+result);
            progressDialog.dismiss();
           /* Constant.showOkPopup(SchoolSMSActivity.this,"File uploaded Successfully", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }

            });*/

            Toast.makeText(SchoolSMSActivity.this,"File uploaded Successfully",Toast.LENGTH_LONG).show();

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                HttpParams param = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(param, 15000);
                HttpConnectionParams.setSoTimeout(param, 20000);
                HttpClient httpclient = new DefaultHttpClient(param);
                httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
                HttpParams httpParameters = httpclient.getParams();
                HttpConnectionParams.setTcpNoDelay(httpParameters, true);
                httpclient.getParams().setParameter("\"http.socket.timeout\"", new Integer(90000));
                // HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://www.betweenus.in/PODARAPP/PodarApp.svc/UploadSmsCsvFile");
                // Add Headers
                httppost.addHeader("usl_id", usl_id);
                httppost.addHeader("clt_id", clt_id);
                try {
                    FileEntity entity = new FileEntity(new File(FilePath), "application/octet-stream");
                    entity.setChunked(true);
                    httppost.setEntity(entity);
                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);
                    Log.e("response new", String.valueOf(response));
                    HttpEntity responseEntity = response.getEntity();
                    if (responseEntity != null) {
                        String abc = EntityUtils.toString(responseEntity);
                        Log.e("RES", abc);
                        try {
                            jsonResponse = new JSONObject(abc);
                            Log.e("json rres", String.valueOf(jsonResponse));
                            filePath = jsonResponse.getString("Filepath");
                            responseStatus = jsonResponse.getString("StatusMsg");
                            Log.e("filepath", filePath);
                            Log.e("Status", responseStatus);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }
            return "network";
        }
    }
    private void WscallToUploadFile(final String clt_id, final String usl_id,String base64value) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.uploadfile(clt_id, usl_id, base64value, UploadFileMethod_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);

                            if (login_details.Status.equalsIgnoreCase("1")) {
                                Constant.showOkPopup(SchoolSMSActivity.this, login_details.StatusMsg, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                            } else {
                                Constant.showOkPopup(SchoolSMSActivity.this, login_details.StatusMsg, new DialogInterface.OnClickListener() {

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
                        Log.e("sendSmsActivity", "ERROR.._---" + error.getCause());
                    }
                });
    }
    /**
     * Showing Dialog
     * */

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);

        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();
                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);
                // Output stream
                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        + "/sms_sample.csv");
                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
           progressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);
            Toast.makeText(SchoolSMSActivity.this,"Downloading Completed",Toast.LENGTH_SHORT).show();
        }

    }

    private void callWStoSendSMSDirect(String contactList,final String clt_id,String messageContent, final String usl_id) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getAdminSchoolSendSMSDirect(contactList,clt_id,messageContent,usl_id, AdminSendDirectSMSMethod_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);

                            if (login_details.Status.equalsIgnoreCase("1")) {
                                Constant.showOkPopup(SchoolSMSActivity.this, login_details.StatusMsg, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent i = new Intent(SchoolSMSActivity.this, AdminSMSActivity.class);
                                        i.putExtra("clt_id", clt_id);
                                        i.putExtra("usl_id", usl_id);
                                        i.putExtra("School_name", school_name);
                                        i.putExtra("Admin_name", admin_name);
                                        i.putExtra("version_name", version_name);
                                        i.putExtra("board_name", board_name);
                                        i.putExtra("class_id", class_id);
                                        i.putExtra("academic_year", academic_year);
                                        i.putExtra("org_id", org_id);
                                        startActivity(i);
                                        dialog.dismiss();
                                    }
                                });
                            } else {
                                Constant.showOkPopup(SchoolSMSActivity.this, "SMS not sent", new DialogInterface.OnClickListener() {

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
                        Log.d("sendSmsActivity", "ERROR.._---" + error.getCause());
                    }
                });
    }

    private void callWStoSendSMSToTeachers(final String clt_id, final String usl_id,String messageContent, final String board_name,String mode) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getAdminSchoolSendSMSTeacher(clt_id, usl_id, messageContent, board_name, mode, AdminSendSMSToTeachersMethod_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);

                            if (login_details.Status.equalsIgnoreCase("1")) {
                                Constant.showOkPopup(SchoolSMSActivity.this, login_details.StatusMsg, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent i = new Intent(SchoolSMSActivity.this, AdminProfileActivity.class);
                                        i.putExtra("clt_id", clt_id);
                                        i.putExtra("usl_id", usl_id);
                                        i.putExtra("School_name", school_name);
                                        i.putExtra("Admin_name", admin_name);
                                        i.putExtra("version_name", version_name);
                                        i.putExtra("board_name", board_name);
                                        i.putExtra("class_id", class_id);
                                        i.putExtra("academic_year", academic_year);
                                        i.putExtra("org_id", org_id);
                                        startActivity(i);
                                        dialog.dismiss();
                                    }
                                });
                            } else {
                                Constant.showOkPopup(SchoolSMSActivity.this, "SMS not sent", new DialogInterface.OnClickListener() {

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
                        Log.e("sendSmsActivity", "ERROR.._---" + error.getCause());
                    }
                });
    }

    private void selectCategoryDialog() {

        final String category_names[] ={"Student","Teacher","Direct"};
        strings_category = new ArrayList<String>();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        alertDialog.setTitle("Select Category");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,category_names);
        if(firstTimeCategorySelection.equalsIgnoreCase("false")) {
            ed_select_category.setText(category_names[0]);
        }
        alertDialog.setItems(category_names, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                ed_select_category.setText(category_names[item]);
                if (category_names[item].equalsIgnoreCase("Student")) {
                    rl_student.setVisibility(View.VISIBLE);
                    rl_direct.setVisibility(View.GONE);
                    rl_teacher.setVisibility(View.GONE);
                    rl_send_sms.setVisibility(View.VISIBLE);
                    img_search_by.setVisibility(View.VISIBLE);
                } else if (category_names[item].equalsIgnoreCase("Teacher")) {
                    rl_student.setVisibility(View.GONE);
                    rl_direct.setVisibility(View.GONE);
                    rl_teacher.setVisibility(View.VISIBLE);
                    rl_send_sms.setVisibility(View.GONE);
                    img_search_by.setVisibility(View.GONE);
                    //charachter lenght
                    CheckCharachterLength();
                } else if (category_names[item].equalsIgnoreCase("Direct")) {
                    rl_student.setVisibility(View.GONE);
                    rl_direct.setVisibility(View.VISIBLE);
                    rl_teacher.setVisibility(View.GONE);
                    rl_send_sms.setVisibility(View.GONE);
                    img_search_by.setVisibility(View.GONE);
                    CheckDirectCharachterLength();
                }

            }
        });
       if(firstTimeCategorySelection.equalsIgnoreCase("true")) {
            alertDialog.show();
        }
    }

    private void CheckDirectCharachterLength() {
        enter_direct_sms.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int a, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Check_SMS_Length_Direct(enter_direct_sms);
            }
        });
    }

    private void CheckCharachterLength() {
        enter_teacher_sms.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int a, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Check_SMS_Length(enter_teacher_sms);
            }
        });
    }
    public void Check_SMS_Length_Direct(EditText ed_description) throws NumberFormatException
    {
        try
        {
            if (ed_description.getText().toString().length() <= 0)
            {
                tv_direct_charachter_length.setText("0/132");
            }
            else
            {
                int valid_len = ed_description.getText().toString().length();
                tv_direct_charachter_length.setText(String.valueOf(valid_len) + "/" + 132);
            }
        }
        catch (Exception e)
        {
            Log.e("error", "" + e);
        }
    }
    public void Check_SMS_Length(EditText ed_description) throws NumberFormatException
    {
        try
        {
            if (ed_description.getText().toString().length() <= 0)
            {
                tv_charachter_length.setText("0/132");
            }
            else
            {
                int valid_len = ed_description.getText().toString().length();
                tv_charachter_length.setText(String.valueOf(valid_len) + "/" + 132);
            }
        }
        catch (Exception e)
        {
            Log.e("error", "" + e);
        }
    }
    @Override
    public void setValues(ArrayList<String> studData, ArrayList<String> contactData) {


    }

    @Override
    public void getValues(ArrayList<String> getStudData, ArrayList<String> contactData) {

    }

    public class StudentDetailSMSAdapter extends BaseAdapter {
        LayoutInflater layoutInflater;
        Context context;
        String date; DataTransferInterface dtInterface;
        String selectAll,stud_id,contact_number,class_id,clt_id,usl_id,school_name,teacher_name,version_name,board_name;
        boolean[] mCheckedState;
        boolean selectAllCheckboxStatus;
        private ArrayList<com.podarbetweenus.Entity.AdminDropResult> AdminDropResult = new ArrayList<com.podarbetweenus.Entity.AdminDropResult>();
        public StudentDetailSMSAdapter(Context context,DataTransferInterface dataTransferInterface,
                                       ArrayList<AdminDropResult> AdminDropResult,boolean selectAllCheckboxStatus,String selectAll,String clt_id,String usl_id,String school_name,String admin_name,String version_name,String board_name) {
            this.context = context;
            this.AdminDropResult = AdminDropResult;
            this.selectAllCheckboxStatus = selectAllCheckboxStatus;
            this.selectAll = selectAll;
            this.clt_id = clt_id;
            this.usl_id = usl_id;
            this.dtInterface = dataTransferInterface;
            this.school_name = school_name;
            this.teacher_name = teacher_name;
            this.version_name = version_name;
            this.board_name = board_name;
            mCheckedState = new boolean[AdminDropResult.size()];
            this.layoutInflater = layoutInflater.from(this.context);
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            View showLeaveStatus = convertView;

            if (showLeaveStatus == null)
            {
                holder = new ViewHolder();
                showLeaveStatus = layoutInflater.inflate(R.layout.sms_list_item, null);
                holder.tv_shift_value = (TextView) showLeaveStatus.findViewById(R.id.tv_shift_value);
                holder.tv_section_value = (TextView) showLeaveStatus.findViewById(R.id.tv_section_value);
                holder.tv_standard_value = (TextView) showLeaveStatus.findViewById(R.id.tv_standard_value);
                holder.ll_top = (LinearLayout) showLeaveStatus.findViewById(R.id.ll_top);
                holder.select_checkbox = (CheckBox) showLeaveStatus.findViewById(R.id.select_checkbox);

            } else
            {
                holder = (ViewHolder) showLeaveStatus.getTag();
            }
            showLeaveStatus.setTag(holder);
            if(position % 2 ==0){
                holder.ll_top.setBackgroundResource(R.color.sms_listview_even_row);

            }
            else {
                holder.ll_top.setBackgroundResource(R.color.sms_listview_odd_row);

            }
            holder.tv_section_value.setText(AdminDropResult.get(position).sec_Name);
            holder.tv_shift_value.setText(AdminDropResult.get(position).sft_name);
            holder.tv_standard_value.setText(AdminDropResult.get(position).std_Name+"-"+AdminDropResult.get(position).div_name);
            if(selectAll.equalsIgnoreCase("true"))
            {
                holder.select_checkbox.setChecked(true);
            }
            else{
                holder.select_checkbox.setChecked(false);
            }
            checkboxStatus = holder.select_checkbox.isChecked();
            Log.e("Checkbox status", String.valueOf(checkboxStatus));
            final ViewHolder finalHolder = holder;
            finalHolder.select_checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("<<Checking for:" ,AdminDropResult.get(position).cls_ID);
                    String checkFor = AdminDropResult.get(position).cls_ID;

                    if(selectedcheckboxClassId.contains(checkFor)==true){
                        Log.d("<<Element Found:","Removing!!!");
                        Log.d("<<Before removing:",selectedcheckboxClassId);

                        class_id_data.remove(checkFor);
                        cls_id_data_withoutDuplicates.remove(checkFor);


                    }
                    try {
                        if (((CheckBox) v).isChecked()) {

                            if (selectAll.equalsIgnoreCase("true")) {
                                if (finalHolder.select_checkbox.isChecked() == true) {

                                    AdminDropResult.get(position).setChecked(true);
                                    check = AdminDropResult.get(position).isChecked();
                                    Log.e("CHECK", String.valueOf(check));
                                    cls_id_data_withoutDuplicates.add(AdminDropResult.get(position).cls_ID);
                                } else {

                                    AdminDropResult.get(position).setChecked(false);
                                    check = AdminDropResult.get(position).isChecked();
                                    Log.e("CHECK", String.valueOf(check));
                                    cls_id_data_withoutDuplicates.add(AdminDropResult.get(position).cls_ID);
                                }
                                //Class Id
                                int classIdtotal = cls_id_data_withoutDuplicates.size() * separator.length();
                                for (String s : cls_id_data_withoutDuplicates) {
                                    classIdtotal += s.length();
                                }
                                StringBuilder classIdrString = new StringBuilder(classIdtotal);
                                for (String s : cls_id_data_withoutDuplicates) {
                                    classIdrString.append(separator).append(s);
                                }
                                selectedcheckboxClassId = classIdrString.substring(separator.length());
                                Log.d("<<remocls ID seperated", selectedcheckboxClassId);

                            }
                            else{
                                mCheckedState[position] = true;
                                AdminDropResult.get(position).setChecked(true);
                                check = AdminDropResult.get(position).isChecked();
                                Log.e("CHECK", String.valueOf(check));
                                finalHolder.select_checkbox.setChecked(true);
                                checkboxStatus = finalHolder.select_checkbox.isChecked();
                                class_id = AdminDropResult.get(position).cls_ID;
                                class_id_data.add(class_id);
                                cls_id_data_withoutDuplicates.addAll(class_id_data);
                                stud_id_data.add(stud_id);
                                stud_id_data_withoutDuplicates.addAll(stud_id_data);
                                //  String separator = ",";
                                //Class Id
                                int classIdtotal = cls_id_data_withoutDuplicates.size() * separator.length();
                                for (String s : cls_id_data_withoutDuplicates) {
                                    classIdtotal += s.length();
                                }
                                StringBuilder classIdrString = new StringBuilder(classIdtotal);
                                for (String s : cls_id_data_withoutDuplicates) {
                                    classIdrString.append(separator).append(s);
                                }
                                selectedcheckboxClassId = classIdrString.substring(separator.length());
                                Log.d("<<remocls ID seperated", selectedcheckboxClassId);
                                Log.e("SIZE",String.valueOf(cls_id_data_withoutDuplicates.size()));

                            }
                        }
                        else if (!((CheckBox) v).isChecked()) {
                            if (selectAll.equalsIgnoreCase("true")) {
                                if (finalHolder.select_checkbox.isChecked() == true) {

                                    AdminDropResult.get(position).setChecked(true);
                                    check = AdminDropResult.get(position).isChecked();
                                    Log.e("CHECK", String.valueOf(check));
                                    cls_id_data_withoutDuplicates.add(AdminDropResult.get(position).cls_ID);
                                } else {

                                    AdminDropResult.get(position).setChecked(false);
                                    check = AdminDropResult.get(position).isChecked();
                                    Log.e("CHECK", String.valueOf(check));
                                    cls_id_data_withoutDuplicates.remove(AdminDropResult.get(position).cls_ID);
                                }
                                //Class Id
                                int classIdtotal = cls_id_data_withoutDuplicates.size() * separator.length();
                                for (String s : cls_id_data_withoutDuplicates) {
                                    classIdtotal += s.length();
                                }
                                StringBuilder classIdrString = new StringBuilder(classIdtotal);
                                for (String s : cls_id_data_withoutDuplicates) {
                                    classIdrString.append(separator).append(s);
                                }
                                selectedcheckboxClassId = classIdrString.substring(separator.length());
                                Log.d("<<remocls ID seperated", selectedcheckboxClassId);

                            }
                            else {
                                if (finalHolder.select_checkbox.isChecked() == true) {

                                    AdminDropResult.get(position).setChecked(true);
                                    check = AdminDropResult.get(position).isChecked();
                                    Log.e("CHECK", String.valueOf(check));
                                    cls_id_data_withoutDuplicates.add(AdminDropResult.get(position).cls_ID);
                                } else {

                                    AdminDropResult.get(position).setChecked(false);
                                    check = AdminDropResult.get(position).isChecked();
                                    Log.e("CHECK", String.valueOf(check));
                                    cls_id_data_withoutDuplicates.remove(AdminDropResult.get(position).cls_ID);
                                }
                                //Class Id
                                int classIdtotal = cls_id_data_withoutDuplicates.size() * separator.length();
                                for (String s : cls_id_data_withoutDuplicates) {
                                    classIdtotal += s.length();
                                }
                                StringBuilder classIdrString = new StringBuilder(classIdtotal);
                                for (String s : cls_id_data_withoutDuplicates) {
                                    classIdrString.append(separator).append(s);
                                }
                                selectedcheckboxClassId = classIdrString.substring(separator.length());
                                Log.d("<<remocls ID seperated", selectedcheckboxClassId);

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });
            finalHolder.select_checkbox.setChecked(AdminDropResult.get(position).isChecked());
            return showLeaveStatus;
        }

        public  class ViewHolder
        {
            TextView tv_standard_value,tv_shift_value,tv_section_value;
            LinearLayout ll_top;
            CheckBox select_checkbox;
        }

    }

    @Override
    public void onBackPressed() {
        try {
            Intent back = new Intent(SchoolSMSActivity.this, AdminProfileActivity.class);
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
