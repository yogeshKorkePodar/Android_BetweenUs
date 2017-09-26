package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by Administrator on 10/1/2015.
 */
public class MotherInformationActivity extends Activity implements View.OnClickListener{

    //LayoutEntities
    HeaderControler header;
    //Button
    Button btn_next_mother_info,btn_editmother_info,btn_savemother_info,btn_cancel_mother_info;
    //Linear Layout
    LinearLayout lay_back_investment,ll_saveMotherInfo,ll_editMotherInfo;
    //EditText
    EditText ed_motherrlastname,ed_motherfirstname,ed_dob,ed_qualification,ed_language,ed_religion,ed_nationality,ed_mother_pan_no,ed_mother_company_name,
            ed_occupation;
    //ProgressDialog
    ProgressDialog progressDialog;
    //Date Picker
    DatePicker datePicker;
    Context mcontext;

    //Entities
    LoginDetails login_Details;
    DataFetchService dft;

    String motherInfoMethod_name = "GetParentMotherInfo";
    String methodName="GetParentInfoDropDown";
    String updateMotherInfoMethod_name = "UpdateParentMotherInfo";
    ArrayList<String> strings_language = new ArrayList<String>();
    ArrayList<String> strings_religion = new ArrayList<String>();
    ArrayList<String> strings_nationality = new ArrayList<String>();
    ArrayList<String> strings_education = new ArrayList<String>();
    ArrayList<String> strings_occupation = new ArrayList<String>();
    String[] select_language;
    String[] select_religion;
    String[] select_nationality;
    String[] select_education;
    String[] select_occupation;
    String occupation ="",education = "",religion = "",nationality = "",language = "",qualification_id,religion_id,nationality_id,occupation_id,
    mother_tongue_id,dateOfBirth,m_PanNo,m_ComapnyName;
    KeyListener variable;
    String TAG = "MotherInformation";
    int myear, month, day;
    Calendar calendar;
    String board_name,clt_id,msd_ID,usl_id,versionName,school_name;
    final int DATE_DIALOG_ID = 0;
    int versionCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.motherinformation);
        getIntentId();
        findViews();
        init();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getEdittextvariable();
        //setEditText Disable
        disableEditText();
        AppController.editButtonClicked="false";
        //Call Webservice for Mother Info
        calWebserviceforMotherInfo(AppController.msd_ID);


    }

    private void getEdittextvariable() {

        variable= ed_mother_pan_no.getKeyListener();
        variable= ed_dob.getKeyListener();
        variable= ed_mother_company_name.getKeyListener();
        variable= ed_nationality.getKeyListener();
        variable= ed_language.getKeyListener();
        variable= ed_religion.getKeyListener();
        variable= ed_occupation.getKeyListener();
        variable= ed_qualification.getKeyListener();
    }

    private void disableEditText() {

        ed_motherfirstname.setKeyListener(null);
        ed_motherrlastname.setKeyListener(null);
        ed_mother_pan_no.setKeyListener(null);
        ed_dob.setKeyListener(null);
        ed_mother_company_name.setKeyListener(null);
        ed_nationality.setKeyListener(null);
        ed_language.setKeyListener(null);
        ed_religion.setKeyListener(null);
        ed_occupation.setKeyListener(null);
        ed_qualification.setKeyListener(null);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, myear, month,
                        day);

        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myear = year;
            month = monthOfYear;
            day = dayOfMonth;
            try {
                DateDisplay();
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    };
    private void DateDisplay() throws ParseException {
        // Month is 0 based so add 1

        ed_dob.setText(new StringBuilder().append(day).append("/")
                .append(pad(month + 1)).append("/").append(pad(myear)));
    }

    // Method used for consideration of two digits
    public static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }


    private void getIntentId() {
        Intent intent = getIntent();
        board_name = intent.getStringExtra("board_name");
        clt_id = intent.getStringExtra("clt_id");
        msd_ID = intent.getStringExtra("msd_ID");
        usl_id = intent.getStringExtra("usl_id");
        school_name = intent.getStringExtra("School_name");
        versionName = intent.getStringExtra("version_name");
        versionCode = intent.getIntExtra("version_code", 0);
        AppController.versionName = versionName;
        AppController.versionCode = versionCode;
        AppController.dropdownSelected = intent.getStringExtra("DropDownSelected");
        AppController.school_name = school_name;
        AppController.Board_name = board_name;
        AppController.clt_id = clt_id;
        AppController.msd_ID = msd_ID;
    }

    private void findViews() {
        //Button
        btn_next_mother_info = (Button) findViewById(R.id.btn_next_mother_info);
        btn_editmother_info = (Button) findViewById(R.id.btn_editmother_info);
        btn_savemother_info = (Button) findViewById(R.id.btn_savemother_info);
        btn_cancel_mother_info = (Button) findViewById(R.id.btn_cancel_mother_info);
        //Edit Text
        ed_motherrlastname = (EditText) findViewById(R.id.ed_motherrlastname);
        ed_motherfirstname = (EditText) findViewById(R.id.ed_motherfirstname);
        ed_dob = (EditText) findViewById(R.id.ed_dob);
        ed_qualification = (EditText) findViewById(R.id.ed_qualification);
        ed_language = (EditText) findViewById(R.id.ed_language);
        ed_religion = (EditText) findViewById(R.id.ed_religion);
        ed_nationality = (EditText) findViewById(R.id.ed_nationality);
        ed_mother_pan_no = (EditText) findViewById(R.id.ed_mother_pan_no);
        ed_mother_company_name = (EditText) findViewById(R.id.ed_mother_company_name);
        ed_occupation = (EditText) findViewById(R.id.ed_occupation);
        //Linear Layout
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
        ll_saveMotherInfo = (LinearLayout) findViewById(R.id.ll_saveMotherInfo);
        ll_editMotherInfo = (LinearLayout) findViewById(R.id.ll_editMotherInfo);

        final Calendar c = Calendar.getInstance();
        myear = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
    }

    private void init() {

        header = new HeaderControler(this,true,false,"Parent Information");
        login_Details = new LoginDetails();
        dft = new DataFetchService(this);
        progressDialog = Constant.getProgressDialog(this);
        btn_cancel_mother_info.setOnClickListener(this);
        btn_editmother_info.setOnClickListener(this);
        btn_next_mother_info.setOnClickListener(this);
        btn_savemother_info.setOnClickListener(this);
        ed_language.setOnClickListener(this);
        ed_nationality.setOnClickListener(this);
        ed_occupation.setOnClickListener(this);
        ed_religion.setOnClickListener(this);
        ed_qualification.setOnClickListener(this);
        ed_dob.setOnClickListener(this);
        lay_back_investment.setOnClickListener(this);
    }
    private void calWebserviceforMotherInfo(String msd_id) {

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        dft.getparentInfo(msd_id, motherInfoMethod_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_Details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_Details.Status.equalsIgnoreCase("1")) {

                                setUIData();


                            } else if (login_Details.Status.equalsIgnoreCase("0")) {

                            }
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Show error or whatever...
                        Log.d("ParentInfo", "ERROR.._---" + error.getCause());
                    }
                });

    }

    private void setUIData() {
        ed_motherrlastname.setText(login_Details.ParentInfoMother.get(0).nms_lname);
        ed_motherfirstname.setText(login_Details.ParentInfoMother.get(0).nms_fname);
        ed_dob.setText(login_Details.ParentInfoMother.get(0).nms_dob);
        ed_qualification.setText(login_Details.ParentInfoMother.get(0).qlf_name);
        ed_language.setText(login_Details.ParentInfoMother.get(0).mtg_name);
        ed_religion.setText(login_Details.ParentInfoMother.get(0).rel_name);
        ed_nationality.setText(login_Details.ParentInfoMother.get(0).nns_nationality);
        ed_mother_pan_no.setText(login_Details.ParentInfoMother.get(0).nmd_PanNo);
        ed_mother_company_name.setText(login_Details.ParentInfoMother.get(0).nmd_companyname);
        ed_occupation.setText(login_Details.ParentInfoMother.get(0).ocp_name);
        AppController.language = login_Details.ParentInfoMother.get(0).mtg_id;
        AppController.occupation = login_Details.ParentInfoMother.get(0).ocp_id;
        AppController.nationality = login_Details.ParentInfoMother.get(0).nns_id;
        AppController.religion = login_Details.ParentInfoMother.get(0).rel_ID;
        AppController.qualification = login_Details.ParentInfoMother.get(0).qlf_id;

    }

    @Override
    public void onClick(View v) {
        if(v==btn_next_mother_info)
        {
            Intent emergencyContactInfo = new Intent(MotherInformationActivity.this,EmergencyContactActivity.class);
            emergencyContactInfo.putExtra("clt_id", AppController.clt_id);
            emergencyContactInfo.putExtra("msd_ID", AppController.msd_ID);
            emergencyContactInfo.putExtra("usl_id", AppController.usl_id);
            emergencyContactInfo.putExtra("School_name", AppController.school_name);
            emergencyContactInfo.putExtra("board_name", AppController.Board_name);
            emergencyContactInfo.putExtra("version_name", AppController.versionName);
            emergencyContactInfo.putExtra("verion_code",AppController.versionCode);
            startActivity(emergencyContactInfo);
        }
        else if(v==btn_savemother_info){
            saveInfo();
            callWebserviceSaveParentInfo(AppController.msd_ID,dateOfBirth,AppController.qualification,AppController.language,AppController.religion,AppController.nationality,m_PanNo,m_ComapnyName,AppController.occupation);
        }
        else if(v==btn_editmother_info){
            ll_saveMotherInfo.setVisibility(View.VISIBLE);
            ll_editMotherInfo.setVisibility(View.GONE);
            AppController.editButtonClicked="true";
            //Enable Editiong
            enableEditText();
        }
        else if(v==btn_cancel_mother_info){
            View view = this.getCurrentFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            AppController.editButtonClicked="false";
            ll_editMotherInfo.setVisibility(View.VISIBLE);
            ll_saveMotherInfo.setVisibility(View.GONE);
            disableEditText();
            //Call Webservice for Mother Info
            calWebserviceforMotherInfo(AppController.msd_ID);
        }
        else if(v==ed_qualification){
            if(AppController.editButtonClicked.equalsIgnoreCase("true")) {


                education = "Education";
                language = "";
                nationality = "";
                religion = "";
                occupation = "";
                //Call webservice for  dropdown
                calWebserviceforCountry(mcontext);
            }
        }
        else if(v==ed_language){
            if(AppController.editButtonClicked.equalsIgnoreCase("true")) {
                language = "Language";
                religion = "";
                nationality = "";
                education = "";
                occupation = "";
                //Call webservice for  dropdown
                calWebserviceforCountry(mcontext);
            }
        }
        else if(v==ed_religion){
            if(AppController.editButtonClicked.equalsIgnoreCase("true")) {
                religion = "Religion";
                language = "";
                nationality = "";
                education = "";
                occupation = "";
                //Call webservice for  dropdown
                calWebserviceforCountry(mcontext);
            }
        }
        else if(v==ed_nationality){
            if(AppController.editButtonClicked.equalsIgnoreCase("true")) {
                nationality = "Nationality";
                language = "";
                religion = "";
                education = "";
                occupation = "";
                //Call webservice for  dropdown
                calWebserviceforCountry(mcontext);
            }
        }
        else if(v==ed_occupation){
            occupation = "Occupation";
            education = "";
            language = "";
            nationality = "";
            religion = "";
            //Call webservice for  dropdown
            calWebserviceforCountry(mcontext);
        }
        else if(v==ed_dob){
            if(AppController.editButtonClicked.equalsIgnoreCase("true")) {
                showDialog(DATE_DIALOG_ID);
            }
        }
        else if(v==lay_back_investment){
            Intent back = new Intent(MotherInformationActivity.this,FatherInformationActivity.class);
            AppController.OnBackpressed = "false";
            AppController.editButtonClicked = "false";
            AppController.clt_id = clt_id;
            AppController.msd_ID = msd_ID;
            AppController.usl_id = usl_id;
            AppController.school_name = school_name;
            AppController.Board_name = board_name;
            back.putExtra("clt_id", AppController.clt_id);
            back.putExtra("msd_ID",AppController.msd_ID);
            back.putExtra("usl_id",AppController.usl_id);
            back.putExtra("board_name",AppController.Board_name);
            back.putExtra("School_name",AppController.school_name);
            back.putExtra("version_name",AppController.versionName);
            back.putExtra("verion_code",AppController.versionCode);
            startActivity(back);
            /*if(AppController.SiblingActivity.equalsIgnoreCase("true"))
            {
                Intent back = new Intent(MotherInformationActivity.this,Profile_Sibling.class);
                AppController.OnBackpressed = "false";
                AppController.editButtonClicked = "false";
                AppController.clt_id = clt_id;
                AppController.msd_ID = msd_ID;
                AppController.usl_id = usl_id;
                AppController.school_name = school_name;
                AppController.Board_name = board_name;
                back.putExtra("clt_id", AppController.clt_id);
                back.putExtra("msd_ID",AppController.msd_ID);
                back.putExtra("usl_id",AppController.usl_id);
                back.putExtra("board_name",AppController.Board_name);
                back.putExtra("School_name",AppController.school_name);
                back.putExtra("version_name",AppController.versionName);
                startActivity(back);
            }
            else {
                Intent back = new Intent(MotherInformationActivity.this,ProfileActivity.class);
                AppController.OnBackpressed = "false";
                AppController.editButtonClicked = "false";

                AppController.clt_id = clt_id;
                AppController.msd_ID = msd_ID;
                AppController.usl_id = usl_id;
                AppController.Board_name = board_name;
                AppController.school_name = school_name;
                back.putExtra("clt_id",AppController.clt_id);
                back.putExtra("msd_ID",AppController.msd_ID);
                back.putExtra("usl_id",AppController.usl_id);
                back.putExtra("board_name",AppController.Board_name);
                back.putExtra("School_name",AppController.school_name);
                back.putExtra("version_name",AppController.versionName);
                startActivity(back);
            }*/
        }


    }

    private void saveInfo() {
        dateOfBirth = ed_dob.getText().toString();
        m_PanNo = ed_mother_pan_no.getText().toString();
        m_ComapnyName = ed_mother_company_name.getText().toString();
    }
    private void callWebserviceSaveParentInfo(String msd_ID,String dateOfBirth,String qualification_id,String mother_tongue_id,String religion_id,String nationality_id,String f_PanNo,String f_ComapnyName
            ,String occupation_id) {

        if(!progressDialog.isShowing()) {
            progressDialog.show();
        }
        dft.updateFatherInfo(msd_ID, dateOfBirth, qualification_id, mother_tongue_id, religion_id, nationality_id, f_PanNo, f_ComapnyName, occupation_id, updateMotherInfoMethod_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_Details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_Details.Status.equalsIgnoreCase("1")) {

                                Constant.showOkPopup(MotherInformationActivity.this, login_Details.StatusMsg, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent motherInfo = new Intent(MotherInformationActivity.this, MotherInformationActivity.class);
                                        motherInfo.putExtra("clt_id", AppController.clt_id);
                                        motherInfo.putExtra("msd_ID", AppController.msd_ID);
                                        motherInfo.putExtra("usl_id", AppController.usl_id);
                                        motherInfo.putExtra("School_name", AppController.school_name);
                                        motherInfo.putExtra("board_name", AppController.Board_name);
                                        motherInfo.putExtra("version_name", AppController.versionName);
                                        motherInfo.putExtra("verion_code",AppController.versionCode);
                                        startActivity(motherInfo);
                                    }

                                });


                            } else if (login_Details.Status.equalsIgnoreCase("0")) {

                            }
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Show error or whatever...
                        Log.d("ParentInfo", "ERROR.._---" + error.getCause());
                    }
                });

    }
    public void calWebserviceforCountry(Context mcontext) {
        if(!progressDialog.isShowing()) {
            progressDialog.show();
        }

        dft.getCountryList(methodName, Request.Method.GET,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        login_Details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                        strings_language = new ArrayList<String>();
                        strings_nationality = new ArrayList<String>();
                        strings_religion = new ArrayList<String>();
                        strings_education = new ArrayList<String>();
                        strings_occupation = new ArrayList<String>();

                        if (language.equalsIgnoreCase("Language")) {
                            try {
                                for (int i = 0; i < login_Details.ParentInfoMotherTongue.size(); i++) {
                                    strings_language.add(login_Details.ParentInfoMotherTongue.get(i).mtg_name);

                                    select_language = new String[strings_language.size()];
                                    select_language = strings_language.toArray(select_language);

                                }
                                selectDropdown(select_language);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else if (religion.equalsIgnoreCase("Religion")) {
                            try {
                                for (int i = 0; i < login_Details.ParentInfoReligion.size(); i++) {
                                    strings_religion.add(login_Details.ParentInfoReligion.get(i).rel_Name);

                                    select_religion = new String[strings_religion.size()];
                                    select_religion = strings_religion.toArray(select_religion);

                                }
                                selectDropdown(select_religion);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (nationality.equalsIgnoreCase("Nationality")) {
                            try {
                                for (int i = 0; i < login_Details.ParentInfoNationality.size(); i++) {
                                    strings_nationality.add(login_Details.ParentInfoNationality.get(i).nns_nationality);

                                    select_nationality = new String[strings_nationality.size()];
                                    select_nationality = strings_nationality.toArray(select_nationality);

                                }
                                selectDropdown(select_nationality);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (education.equalsIgnoreCase("Education")) {
                            try {
                                for (int i = 0; i < login_Details.ParentInfoQulification.size(); i++) {
                                    strings_education.add(login_Details.ParentInfoQulification.get(i).qlf_name);

                                    select_education = new String[strings_education.size()];
                                    select_education = strings_education.toArray(select_education);

                                }
                                selectDropdown(select_education);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (occupation.equalsIgnoreCase("Occupation")) {
                            try {
                                for (int i = 0; i < login_Details.ParentInfoOccupation.size(); i++) {
                                    strings_occupation.add(login_Details.ParentInfoOccupation.get(i).ocp_name);

                                    select_occupation = new String[strings_occupation.size()];
                                    select_occupation = strings_occupation.toArray(select_occupation);

                                }
                                selectDropdown(select_occupation);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "" + volleyError);


                       /* Constant.showOkPopup(HomeActivity.this, volleyError.toString(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.dismiss();
                            }
                        });*/
                    }


                }, null);
    }

    private void selectDropdown(final String[] select_dropdownvalue) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.dialog_list, null);
        alertDialog.setView(convertView);

        if(religion.equalsIgnoreCase("Religion")){

            alertDialog.setTitle("Select Religion");
        }
        else if(nationality.equalsIgnoreCase("nationality")){

            alertDialog.setTitle("Select Nationality");
        }
        else if(language.equalsIgnoreCase("Language")){
            alertDialog.setTitle("Select Language");
        }
        else if(education.equalsIgnoreCase("Education")){

            alertDialog.setTitle("Select Education");
        }
        else if(occupation.equalsIgnoreCase("Occupation")){
            alertDialog.setTitle("Select Occupation");
        }
        final ListView select_list = (ListView) convertView.findViewById(R.id.select_list);

        alertDialog.setItems(select_dropdownvalue, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                if(religion.equalsIgnoreCase("Religion")) {

                    ed_religion.setText(select_dropdownvalue[item]);
                    String selectedvalue = select_dropdownvalue[item];
                    Log.e("Religion", selectedvalue);
                    religion_id = getID(selectedvalue);
                    AppController.religion = religion_id;
                    Log.e("Rel_id", religion_id);
                }
                else if(nationality.equalsIgnoreCase("nationality")) {

                    ed_nationality.setText(select_dropdownvalue[item]);
                    String selectedvalue = select_dropdownvalue[item];
                    Log.e("nationality", selectedvalue);
                    nationality_id = getID(selectedvalue);
                    AppController.nationality = nationality_id;
                    Log.e("nal_id", nationality_id);
                }
                else if(language.equalsIgnoreCase("Language")) {

                    ed_language.setText(select_dropdownvalue[item]);
                    String selectedvalue = select_dropdownvalue[item];
                    Log.e("lang", selectedvalue);
                    mother_tongue_id = getID(selectedvalue);
                    AppController.language = mother_tongue_id;
                    Log.e("lang_id", mother_tongue_id);
                }
                else if(education.equalsIgnoreCase("Education")) {
                    ed_qualification.setText(select_dropdownvalue[item]);
                    String selectedvalue = select_dropdownvalue[item];
                    Log.e("education", selectedvalue);
                    qualification_id = getID(selectedvalue);
                    AppController.qualification = qualification_id;
                    Log.e("edu_id", qualification_id);
                }
                else if(occupation.equalsIgnoreCase("Occupation")) {
                    ed_occupation.setText(select_dropdownvalue[item]);
                    String selectedvalue = select_dropdownvalue[item];
                    Log.e("occupation", selectedvalue);
                    occupation_id = getID(selectedvalue);
                    AppController.occupation = occupation_id;
                    Log.e("occ_id", occupation_id);
                }

                dialog.dismiss();

            }
        });
        alertDialog.show();
    }

    private String getID(String selectedvalue) {

        String id = "0";
        if(religion.equalsIgnoreCase("Religion")) {
            for (int i = 0; i < login_Details.ParentInfoReligion.size(); i++) {
                if (login_Details.ParentInfoReligion.get(i).rel_Name.equalsIgnoreCase(selectedvalue)) {
                    id = login_Details.ParentInfoReligion.get(i).rel_ID;
                }

            }
            //  return id;
        }
        else if(nationality.equalsIgnoreCase("nationality")) {
            for (int i = 0; i < login_Details.ParentInfoNationality.size(); i++) {
                if (login_Details.ParentInfoNationality.get(i).nns_nationality.equalsIgnoreCase(selectedvalue)) {
                    id = login_Details.ParentInfoNationality.get(i).nns_id;
                }

            }

            //   return id;
        }
        else if(language.equalsIgnoreCase("Language")){
            for (int i = 0; i < login_Details.ParentInfoMotherTongue.size(); i++) {
                if (login_Details.ParentInfoMotherTongue.get(i).mtg_name.equalsIgnoreCase(selectedvalue)) {
                    id = login_Details.ParentInfoMotherTongue.get(i).mtg_id;
                }

            }
            //    return id;
        }
        else if(education.equalsIgnoreCase("Education")){
            for (int i = 0; i < login_Details.ParentInfoQulification.size(); i++) {
                if (login_Details.ParentInfoQulification.get(i).qlf_name.equalsIgnoreCase(selectedvalue)) {
                    id = login_Details.ParentInfoQulification.get(i).qlf_id;
                }

            }
        }
        else if(occupation.equalsIgnoreCase("Occupation")){
            for (int i = 0; i < login_Details.ParentInfoOccupation.size(); i++) {
                if (login_Details.ParentInfoOccupation.get(i).ocp_name.equalsIgnoreCase(selectedvalue)) {
                    id = login_Details.ParentInfoOccupation.get(i).ocp_Id;
                }

            }
        }
        return id;
    }
    private void enableEditText() {
       // ed_motherfirstname.setKeyListener(variable);
      //  ed_motherrlastname.setKeyListener(variable);
        ed_mother_pan_no.setKeyListener(variable);
     //   ed_dob.setKeyListener(variable);
        ed_mother_company_name.setKeyListener(variable);
    }

    @Override
    public void onBackPressed() {
        Intent back = new Intent(MotherInformationActivity.this,FatherInformationActivity.class);
        AppController.OnBackpressed = "false";
        AppController.editButtonClicked = "false";
        AppController.clt_id = clt_id;
        AppController.msd_ID = msd_ID;
        AppController.usl_id = usl_id;
        AppController.school_name = school_name;
        AppController.Board_name = board_name;
        back.putExtra("clt_id", AppController.clt_id);
        back.putExtra("msd_ID",AppController.msd_ID);
        back.putExtra("usl_id",AppController.usl_id);
        back.putExtra("board_name",AppController.Board_name);
        back.putExtra("School_name",AppController.school_name);
        back.putExtra("version_name",AppController.versionName);
        back.putExtra("verion_code",AppController.versionCode);
        startActivity(back);
     /*   if(AppController.SiblingActivity.equalsIgnoreCase("true"))
        {
            Intent back = new Intent(MotherInformationActivity.this,Profile_Sibling.class);
            AppController.OnBackpressed = "false";
            AppController.editButtonClicked = "false";
            AppController.clt_id = clt_id;
            AppController.msd_ID = msd_ID;
            AppController.usl_id = usl_id;
            AppController.school_name = school_name;
            AppController.Board_name = board_name;
            back.putExtra("clt_id", AppController.clt_id);
            back.putExtra("msd_ID",AppController.msd_ID);
            back.putExtra("usl_id",AppController.usl_id);
            back.putExtra("board_name",AppController.Board_name);
            back.putExtra("School_name",AppController.school_name);
            back.putExtra("version_name",AppController.versionName);
            startActivity(back);
        }
        else {
            Intent back = new Intent(MotherInformationActivity.this,ProfileActivity.class);
            AppController.OnBackpressed = "false";
            AppController.editButtonClicked = "false";

            AppController.clt_id = clt_id;
            AppController.msd_ID = msd_ID;
            AppController.usl_id = usl_id;
            AppController.Board_name = board_name;
            AppController.school_name = school_name;
            back.putExtra("clt_id",AppController.clt_id);
            back.putExtra("msd_ID",AppController.msd_ID);
            back.putExtra("usl_id",AppController.usl_id);
            back.putExtra("board_name",AppController.Board_name);
            back.putExtra("School_name",AppController.school_name);
            back.putExtra("version_name",AppController.versionName);
            startActivity(back);
        }*/
    }

}
