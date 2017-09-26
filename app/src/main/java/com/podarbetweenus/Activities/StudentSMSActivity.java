package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.BaseAdapter;
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
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.separator;

/**
 * Created by Gayatri on 3/15/2016.
 */
public class StudentSMSActivity extends Activity implements AdapterView.OnItemClickListener,View.OnClickListener{
    //UI Variable
    //ListView
    ListView lv_admin_student_list;
    //TextView
    TextView tv_no_record,rl_send_sms;
    //EditTExt
    EditText ed_academic_year,ed_search_name;
    //RadioButton
    RadioButton radio_button_section,radio_button_std,radio_button_shift;
    //ImageView
    ImageView img_search,img_username_close,img_search_by;
    //Progress Dialog
    ProgressDialog progressDialog;
    //Relative Layout
    RelativeLayout rl_search_students;
    //Linear Layout
    LinearLayout lay_back_investment;


    DataFetchService dft;
    LoginDetails login_details;
    StudentListSMSAdapter studentListSMSAdapter;
    String usl_id,clt_id,board_name,org_id,school_name,admin_name,version_name,academic_yr,latest_aca_yr_id,latest_aca_yr,academic_year,
            searchkey="",searchValue="",cls_id,std,section,shift,div;
    String AcademicYearMethodName = "GetAdminAcedmicYearList";
    String StudentDetailsMethodName = "GetAdminSMSDropDtls";
    String[] select_academic_yr;
    public Set<String> cls_id_data_withoutDuplicates = new HashSet<>();
    ArrayList<String> strings_academic_yr ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("<< Inside","StudentSMSActivity");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.admin_student_sms_layout);
        getIntentId();
        findViews();
        init();
        AppController.currentAcademicYr = "true";
        AppController.dropdownSelected = "false";
        if(AppController.currentAcademicYr.equalsIgnoreCase("true")) {
            //call Ws for Academic Year
            callWsForAcademicYear(clt_id, usl_id);

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
        lv_admin_student_list.setOnItemClickListener(this);
        img_search.setOnClickListener(this);
        img_username_close.setOnClickListener(this);
        ed_academic_year.setOnClickListener(this);
        ed_search_name.setOnClickListener(this);
        progressDialog = Constant.getProgressDialog(this);
        rl_send_sms.setOnClickListener(this);
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        img_search_by.setOnClickListener(this);

    }

    private void findViews() {
        tv_no_record = (TextView) findViewById(R.id.tv_no_record);
        rl_send_sms = (TextView) findViewById(R.id.rl_send_sms);
        lv_admin_student_list = (ListView) findViewById(R.id.lv_admin_student_list);
        ed_academic_year = (EditText) findViewById(R.id.ed_academic_year);
        ed_search_name = (EditText) findViewById(R.id.ed_search_name);
        //ImageView
        img_search = (ImageView) findViewById(R.id.img_search);
        img_username_close = (ImageView) findViewById(R.id.img_username_close);
        img_search_by = (ImageView) findViewById(R.id.img_search_by);
        //Radio Button
        radio_button_shift = (RadioButton) findViewById(R.id.radio_button_shift);
        radio_button_section = (RadioButton) findViewById(R.id.radio_button_section);
        radio_button_std = (RadioButton) findViewById(R.id.radio_button_std);
        //Relative Layout
        rl_search_students = (RelativeLayout) findViewById(R.id.rl_search_students);
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
            Intent show_studentList = new Intent(StudentSMSActivity.this, AdminStudentSMSActivity.class);
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
                                        if (AppController.currentAcademicYr.equalsIgnoreCase("true")) {
                                            //Webservcie call for Student Details
                                            callstudentWebservice(clt_id, usl_id, searchkey, searchValue, latest_aca_yr_id);
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
                Log.e("DATE", selectedYr);
                academic_yr = getIDfromAcademicYr(selectedYr);
                //Webservcie call for Student Details
                callstudentWebservice(clt_id, usl_id,searchkey,searchValue,academic_yr);

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
                                lv_admin_student_list.setVisibility(View.VISIBLE);
                                tv_no_record.setVisibility(View.GONE);
                                setUIData();
                            } else if (login_details.Status.equalsIgnoreCase("0")) {

                                lv_admin_student_list.setVisibility(View.GONE);
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
                        Log.d("SchooLSMSActivity", "ERROR.._---" + error.getCause());
                    }
                });

    }
    private void setUIData() {
        AppController.AdminDropResult = login_details.AdminDropResult;
        Log.e("list size",String.valueOf(AppController.AdminDropResult.size()));
        studentListSMSAdapter = new StudentListSMSAdapter(StudentSMSActivity.this,login_details.AdminDropResult);
        lv_admin_student_list.setAdapter(studentListSMSAdapter);
    }

    @Override
    public void onClick(View v) {
        try {
            if(v==img_search){
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                cls_id_data_withoutDuplicates.clear();
                if(radio_button_std.isChecked()==false && radio_button_section.isChecked()==false && radio_button_shift.isChecked()==false || ed_academic_year.getText().toString().equalsIgnoreCase("")){
                    Constant.showOkPopup(StudentSMSActivity.this, "Please Select Search Key", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
                else if(radio_button_section.isChecked()==true && ed_search_name.getText().toString().equalsIgnoreCase("")){
                    Constant.showOkPopup(StudentSMSActivity.this, "Please Enter Search Key", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
                else if(radio_button_std.isChecked()==true && ed_search_name.getText().toString().equalsIgnoreCase("")){
                    Constant.showOkPopup(StudentSMSActivity.this, "Please Enter Search Key", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
                else if(radio_button_shift.isChecked() == true && ed_search_name.getText().toString().equalsIgnoreCase("")){
                    Constant.showOkPopup(StudentSMSActivity.this, "Please Enter Search Key", new DialogInterface.OnClickListener() {

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
                        academic_yr = latest_aca_yr_id;
                    }
                    //Webservcie call for Student Details
                    callstudentWebservice(clt_id, usl_id, searchkey, searchValue, academic_yr);
                }
                else if(radio_button_section.isChecked()==true && !ed_search_name.getText().toString().equalsIgnoreCase("")){
                    searchkey = "Section";
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    searchValue = ed_search_name.getText().toString();
                    if (AppController.dropdownSelected.equalsIgnoreCase("false")){
                        academic_yr = latest_aca_yr_id;
                    }
                    //Webservcie call for Student Details
                    callstudentWebservice(clt_id, usl_id, searchkey, searchValue, academic_yr);
                }
                else if(radio_button_std.isChecked()==true && !ed_search_name.getText().toString().equalsIgnoreCase("")){
                    searchkey = "Std";
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    searchValue = ed_search_name.getText().toString();
                    if (AppController.dropdownSelected.equalsIgnoreCase("false")){
                        academic_yr = latest_aca_yr_id;
                    }
                    //Webservcie call for Student Details
                    callstudentWebservice(clt_id, usl_id, searchkey, searchValue, academic_yr);
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
                    academic_yr = latest_aca_yr_id;
                }
                //Webservcie call for Student Details
                callstudentWebservice(clt_id, usl_id, searchkey, searchValue, academic_yr);
            }
            else if(v==ed_academic_year){
                AppController.dropdownSelected = "true";
                //call Ws for Academic Year
                callWsForAcademicYear(clt_id, usl_id);
            }
            else if(v==ed_search_name){

            } else if(v==radio_button_section){
                radio_button_section.setChecked(true);
            }
            else if(v==radio_button_shift){

                radio_button_shift.setChecked(true);
            }
            else if(v==radio_button_std){

                radio_button_std.setChecked(true);
            }
            else if(v==ed_academic_year){
                AppController.dropdownSelected = "true";
                //call Ws for Academic Year
                callWsForAcademicYear(clt_id, usl_id);
            }
            else if(v==img_search_by){
                if(rl_search_students.getVisibility()==View.VISIBLE){
                    rl_search_students.setVisibility(View.GONE);
                }
                else {
                    rl_search_students.setVisibility(View.VISIBLE);
                }
            }
            else if(v==rl_send_sms){
                try {
                    AppController.showAllStudents = "true";
                    Log.e("Text",ed_academic_year.getText().toString());
                    if (ed_academic_year.getText().toString().equalsIgnoreCase("")) {
                        Constant.showOkPopup(StudentSMSActivity.this, "Please Select Academic Year", new DialogInterface.OnClickListener() {

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
                        Log.e("cls ID seperated", cls_id);

                        Intent show_studentList = new Intent(StudentSMSActivity.this, AdminStudentSMSActivity.class);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class StudentListSMSAdapter extends BaseAdapter {
        LayoutInflater layoutInflater;
        Context context;
        String date;
        private ArrayList<com.podarbetweenus.Entity.AdminDropResult> AdminDropResult = new ArrayList<com.podarbetweenus.Entity.AdminDropResult>();
        public StudentListSMSAdapter(Context context,
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
                    holder.ll_top.setBackgroundResource(R.color.sms_listview_even_row);

                } else {
                    holder.ll_top.setBackgroundResource(R.color.sms_listview_odd_row);

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

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        try {
            AppController.AdminDropResult.clear();
            Intent back = new Intent(StudentSMSActivity.this, AdminProfileActivity.class);
            AppController.AdminDropResult.clear();
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
