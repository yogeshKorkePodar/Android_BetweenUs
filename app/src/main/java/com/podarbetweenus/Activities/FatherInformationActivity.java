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
public class FatherInformationActivity extends Activity implements View.OnClickListener {

    //LayoutEntities
    HeaderControler header;
    //Button
    Button btn_next_father_info,btn_editfather_info,btn_savefather_info,btn_cancel_father_info;
    //Linear Layout
    LinearLayout lay_back_investment,ll_father_save_info,ll_father_edit_info;
    //EditText
    EditText ed_fatherlastname,ed_fatherfirstname,ed_date_of_birth,ed_qualification,ed_language,ed_religion,ed_nationality,ed_pan_no,ed_company_name,ed_occupation;
    //ProgressDialog
    ProgressDialog progressDialog;

    //Entities
    LoginDetails login_Details;
    DataFetchService dft;

    KeyListener variable;
    Context mcontext;
    String methodName="GetParentInfoDropDown";
    String updateFatherInfoMethod_name = "UpdateParentFatherInfo";
    String fatherInfoMethod_name = "GetParentFatherInfo";
    String board_name,clt_id,msd_ID,usl_id,school_name,versionName,dateOfBirth,qualification_id,religion_id,nationality_id,f_PanNo,occupation_id,
    mother_tongue_id,f_ComapnyName,edittextSelector="false",edittextselector_Religion="false",edittextSelector_Qualification="false",edittextSelector_Language="false",
    edittextselector_Nationality = "false",edittextselector_Occupation = "false",announcementCount,behaviourCount;
    String dropdown_Nationality,dropdown_Religion,dropdown_Occupation,dropdown_Language,dropdown_Qualification;
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
    String TAG = "FatherInfo";
    String occupation ="",education = "",religion = "",nationality = "",language = "";
    int myear, month, day,versionCode;
    final int DATE_DIALOG_ID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fatherinformation);
        findViews();
        init();
        getIntentId();
        AppController.editButtonClicked="false";
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getEdittextvariable();
        //setEditText Disable
        disableEditText();
        //Call Webservice for Parent Info
        calWebserviceforFatherInfo(AppController.msd_ID);
    }

    private void getEdittextvariable() {
        variable= ed_pan_no.getKeyListener();
        variable= ed_date_of_birth.getKeyListener();
        variable= ed_company_name.getKeyListener();
        variable= ed_nationality.getKeyListener();
        variable= ed_language.getKeyListener();
        variable= ed_religion.getKeyListener();
        variable= ed_occupation.getKeyListener();
        variable= ed_qualification.getKeyListener();
    }

    private void disableEditText() {
        ed_fatherfirstname.setKeyListener(null);
        ed_fatherlastname.setKeyListener(null);
        ed_pan_no.setKeyListener(null);
        ed_date_of_birth.setKeyListener(null);
        ed_company_name.setKeyListener(null);
        ed_nationality.setKeyListener(null);
        ed_language.setKeyListener(null);
        ed_religion.setKeyListener(null);
        ed_occupation.setKeyListener(null);
        ed_qualification.setKeyListener(null);
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
        announcementCount = intent.getStringExtra("annpuncement_count");
        behaviourCount = intent.getStringExtra("behaviour_count");
        AppController.versionName = versionName;
        AppController.dropdownSelected = intent.getStringExtra("DropDownSelected");
        AppController.school_name = school_name;
        AppController.Board_name = board_name;
        AppController.clt_id = clt_id;
        AppController.msd_ID = msd_ID;
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

        ed_date_of_birth.setText(new StringBuilder().append(day).append("/")
                .append(pad(month + 1)).append("/").append(pad(myear)));
    }

    // Method used for consideration of two digits
    public static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private void findViews() {
        //Button
        btn_next_father_info = (Button) findViewById(R.id.btn_next_father_info);
        btn_cancel_father_info = (Button) findViewById(R.id.btn_cancel_father_info);
        btn_savefather_info = (Button) findViewById(R.id.btn_savefather_info);
        btn_editfather_info = (Button) findViewById(R.id.btn_editfather_info);
        //Linear Layout
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
        ll_father_edit_info = (LinearLayout) findViewById(R.id.ll_father_edit_info);
        ll_father_save_info = (LinearLayout) findViewById(R.id.ll_father_save_info);
        //EditText
        ed_company_name = (EditText) findViewById(R.id.ed_company_name);
        ed_fatherlastname = (EditText) findViewById(R.id.ed_fatherlastname);
        ed_fatherfirstname = (EditText) findViewById(R.id.ed_fatherfirstname);
        ed_date_of_birth = (EditText) findViewById(R.id.ed_date_of_birth);
        ed_qualification = (EditText) findViewById(R.id.ed_qualification);
        ed_language = (EditText) findViewById(R.id.ed_language);
        ed_religion = (EditText) findViewById(R.id.ed_religion);
        ed_nationality = (EditText) findViewById(R.id.ed_nationality);
        ed_pan_no = (EditText) findViewById(R.id.ed_pan_no);
        ed_occupation = (EditText) findViewById(R.id.ed_occupation);
        final Calendar c = Calendar.getInstance();
        myear = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
    }

    private void init() {

        header = new HeaderControler(this,true,false,"Parent Information");
        login_Details = new LoginDetails();
        dft = new DataFetchService(this);
        btn_next_father_info.setOnClickListener(this);
        btn_editfather_info.setOnClickListener(this);
        btn_savefather_info.setOnClickListener(this);
        btn_cancel_father_info.setOnClickListener(this);
        lay_back_investment.setOnClickListener(this);
        ll_father_edit_info.setOnClickListener(this);
        ll_father_save_info.setOnClickListener(this);
        ed_language.setOnClickListener(this);
        ed_nationality.setOnClickListener(this);
        ed_occupation.setOnClickListener(this);
        ed_religion.setOnClickListener(this);
        ed_qualification.setOnClickListener(this);
        ed_date_of_birth.setOnClickListener(this);
        progressDialog = Constant.getProgressDialog(this);

    }

    @Override
    public void onClick(View v) {
        if(v==btn_next_father_info)
        {
            Intent motherInfo = new Intent(FatherInformationActivity.this,MotherInformationActivity.class);
            motherInfo.putExtra("clt_id", AppController.clt_id);
            motherInfo.putExtra("msd_ID", AppController.msd_ID);
            motherInfo.putExtra("usl_id", AppController.usl_id);
            motherInfo.putExtra("School_name", AppController.school_name);
            motherInfo.putExtra("board_name", AppController.Board_name);
            motherInfo.putExtra("version_name", AppController.versionName);
            startActivity(motherInfo);
        }
        else if(v==btn_editfather_info){
            ll_father_save_info.setVisibility(View.VISIBLE);
            ll_father_edit_info.setVisibility(View.GONE);
            AppController.editButtonClicked="true";
            //Enable Editiong
            enableEditText();
        }
        else if(v==btn_cancel_father_info){
            View view = this.getCurrentFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            AppController.editButtonClicked="false";
            ll_father_edit_info.setVisibility(View.VISIBLE);
            ll_father_save_info.setVisibility(View.GONE);
            disableEditText();
            //Call Webservice for Parent Info
            calWebserviceforFatherInfo(AppController.msd_ID);
        }
        else if(v==btn_savefather_info){

            saveInfo();
            callWebserviceSaveParentInfo(AppController.msd_ID,dateOfBirth,AppController.qualification,AppController.language,AppController.religion,AppController.nationality,f_PanNo,f_ComapnyName,AppController.occupation);
           /* */
        }
        else if(v==ed_language){
            if(AppController.editButtonClicked.equalsIgnoreCase("true")) {
                edittextSelector_Language="true";
                edittextSelector = "true";
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
                edittextselector_Religion="true";
                edittextSelector = "true";
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
                edittextselector_Nationality="true";
                edittextSelector = "true";
                nationality = "Nationality";
                language = "";
                religion = "";
                education = "";
                occupation = "";
                //Call webservice for  dropdown
                calWebserviceforCountry(mcontext);
            }
        }
        else if(v==ed_qualification){
            if(AppController.editButtonClicked.equalsIgnoreCase("true")) {

                edittextSelector_Qualification="true";
                edittextSelector = "true";
                education = "Education";
                language = "";
                nationality = "";
                religion = "";
                occupation = "";
                //Call webservice for  dropdown
                calWebserviceforCountry(mcontext);
            }
        }
        else if(v==ed_occupation){
            edittextselector_Occupation="true";
            edittextSelector = "true";
            occupation = "Occupation";
            education = "";
            language = "";
            nationality = "";
            religion = "";
            //Call webservice for  dropdown
            calWebserviceforCountry(mcontext);
        }
        else if(v==ed_date_of_birth){
            if(AppController.editButtonClicked.equalsIgnoreCase("true")) {
                showDialog(DATE_DIALOG_ID);
            }
        }
        else if(v==lay_back_investment)
        {
            Intent back = new Intent(FatherInformationActivity.this,ParentInformationActivity.class);
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
            back.putExtra("annpuncement_count",announcementCount);
            back.putExtra("behaviour_count",behaviourCount);
            startActivity(back);
            /*if(AppController.SiblingActivity.equalsIgnoreCase("true"))
            {
                Intent back = new Intent(FatherInformationActivity.this,Profile_Sibling.class);
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
                Intent back = new Intent(FatherInformationActivity.this,ProfileActivity.class);
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
        dateOfBirth = ed_date_of_birth.getText().toString();
        f_PanNo = ed_pan_no.getText().toString();
        f_ComapnyName = ed_company_name.getText().toString();

    }
    private void callWebserviceSaveParentInfo(String msd_ID,String dateOfBirth,String qualification_id,String mother_tongue_id,String religion_id,String nationality_id,String f_PanNo,String f_ComapnyName
            ,String occupation_id) {

        if(!progressDialog.isShowing()) {
            progressDialog.show();
        }
        dft.updateFatherInfo(msd_ID, dateOfBirth, qualification_id, mother_tongue_id, religion_id, nationality_id, f_PanNo, f_ComapnyName, occupation_id, updateFatherInfoMethod_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_Details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_Details.Status.equalsIgnoreCase("1")) {

                                Constant.showOkPopup(FatherInformationActivity.this, login_Details.StatusMsg, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent motherInfo = new Intent(FatherInformationActivity.this, FatherInformationActivity.class);
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

    private void calWebserviceforFatherInfo(String msd_id) {

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        dft.getparentInfo(msd_id, fatherInfoMethod_name, Request.Method.POST,
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

        ed_fatherlastname.setText(login_Details.ParentInfoFather.get(0).nms_lname);
        ed_fatherfirstname.setText(login_Details.ParentInfoFather.get(0).nms_fname);
        ed_date_of_birth.setText(login_Details.ParentInfoFather.get(0).nms_dob);
        ed_nationality.setText(login_Details.ParentInfoFather.get(0).nns_nationality);
        ed_pan_no.setText(login_Details.ParentInfoFather.get(0).nmd_PanNo);
        ed_company_name.setText(login_Details.ParentInfoFather.get(0).nmd_companyname);
        ed_qualification.setText(login_Details.ParentInfoFather.get(0).qlf_name);
        ed_language.setText(login_Details.ParentInfoFather.get(0).mtg_name);
        ed_religion.setText(login_Details.ParentInfoFather.get(0).rel_name);
        ed_occupation.setText(login_Details.ParentInfoFather.get(0).ocp_name);
        AppController.language = login_Details.ParentInfoFather.get(0).mtg_id;
        AppController.occupation = login_Details.ParentInfoFather.get(0).ocp_id;
        AppController.nationality = login_Details.ParentInfoFather.get(0).nns_id;
        AppController.religion = login_Details.ParentInfoFather.get(0).rel_ID;
        AppController.qualification = login_Details.ParentInfoFather.get(0).qlf_id;
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

    private void selectLanguage(final String[] select_language) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.dialog_list, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Select Language");
        final ListView select_list = (ListView) convertView.findViewById(R.id.select_list);

        alertDialog.setItems(select_language, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                ed_language.setText(select_language[item]);
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void enableEditText() {
      //  ed_fatherfirstname.setKeyListener(variable);
       // ed_fatherlastname.setKeyListener(variable);
        ed_pan_no.setKeyListener(variable);
       // ed_date_of_birth.setKeyListener(variable);
        ed_company_name.setKeyListener(variable);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent back = new Intent(FatherInformationActivity.this,ParentInformationActivity.class);
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
       /* Intent back = new Intent(ViewMessageActivity.this,ProfileActivity.class);
        startActivity(back);*/
        /*if(AppController.SiblingActivity.equalsIgnoreCase("true"))
        {
            Intent back = new Intent(FatherInformationActivity.this,Profile_Sibling.class);
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
            Intent back = new Intent(FatherInformationActivity.this,ProfileActivity.class);
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
