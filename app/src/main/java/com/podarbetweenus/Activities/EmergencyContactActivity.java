package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 10/3/2015.
 */
public class EmergencyContactActivity extends Activity implements View.OnClickListener {


    //LayoutEntities
    HeaderControler header;
    Button btn_next_emergency_info,btn_editemertgency_info,btn_saveemergency_info,btn_cancel_emergency_info;
    //Linear Layout
    LinearLayout lay_back_investment,ll_edit_EmergencyContactInfo,ll_saveEmergencyContactInfo;
    //EditText
    EditText ed_emergencylastname,ed_emergencyfirstname,ed_emergencymiddlename,ed_emergencyemail_id,ed_emergency_relationship,ed_emergency_mobile_no;
     //ProgressDialog
    ProgressDialog progressDialog;

    // Entities
    LoginDetails login_Details;
    DataFetchService dft;
    KeyListener variable;

    String board_name,clt_id,msd_ID,usl_id,versionName,school_name,lastname,firstname,middlename,emailid,relationship,mobile_no;
    String emergencyContactInfoMethod_name = "GetEmergencyContactInfo";
    String updateEmergencyContactInfoMethod_name = "UpdateEmergencyContactInfo";
    int versionCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.emergencycontactinformation);
        getIntentId();
        findViews();
        init();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getEdittextvariable();
        //setEditText Disable
        disableEditText();
        AppController.editButtonClicked="false";
        //Call Webservice for EmergencyContact Info
        callWebserviceforEnergencyContactInfo(AppController.msd_ID);

    }

    private void callWebserviceforEnergencyContactInfo(String msd_id) {

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        dft.getparentInfo(msd_id, emergencyContactInfoMethod_name, Request.Method.POST,
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
        ed_emergencylastname.setText(login_Details.ParentInfoEmergencyCont.get(0).nms_LName);
        ed_emergencyfirstname.setText(login_Details.ParentInfoEmergencyCont.get(0).nms_FName);
        ed_emergencymiddlename.setText(login_Details.ParentInfoEmergencyCont.get(0).nms_MName);
        ed_emergencyemail_id.setText(login_Details.ParentInfoEmergencyCont.get(0).eml_mailID);
        ed_emergency_relationship.setText(login_Details.ParentInfoEmergencyCont.get(0).nmd_relationShip);
        ed_emergency_mobile_no.setText(login_Details.ParentInfoEmergencyCont.get(0).con_MNo);
    }


    private void disableEditText() {
        ed_emergencylastname.setKeyListener(null);
        ed_emergencyfirstname.setKeyListener(null);
        ed_emergencymiddlename.setKeyListener(null);
        ed_emergencyemail_id.setKeyListener(null);
        ed_emergency_relationship.setKeyListener(null);
        ed_emergency_mobile_no.setKeyListener(null);
    }

    private void getEdittextvariable() {
        variable= ed_emergencylastname.getKeyListener();
        variable= ed_emergencyfirstname.getKeyListener();
        variable= ed_emergencymiddlename.getKeyListener();
        variable= ed_emergencyemail_id.getKeyListener();
        variable= ed_emergency_relationship.getKeyListener();
        variable= ed_emergency_mobile_no.getKeyListener();
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
        //button
        btn_next_emergency_info = (Button) findViewById(R.id.btn_next_emergency_info);
        btn_cancel_emergency_info = (Button) findViewById(R.id.btn_cancel_emergency_info);
        btn_editemertgency_info = (Button) findViewById(R.id.btn_editemertgency_info);
        btn_saveemergency_info = (Button) findViewById(R.id.btn_saveemergency_info);
        //Linear Layout
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
        ll_edit_EmergencyContactInfo = (LinearLayout) findViewById(R.id.ll_edit_EmergencyContactInfo);
        ll_saveEmergencyContactInfo = (LinearLayout) findViewById(R.id.ll_saveEmergencyContactInfo);
        //Edittext
        ed_emergencylastname = (EditText) findViewById(R.id.ed_emergencylastname);
        ed_emergencyfirstname = (EditText) findViewById(R.id.ed_emergencyfirstname);
        ed_emergencymiddlename = (EditText) findViewById(R.id.ed_emergencymiddlename);
        ed_emergencyemail_id = (EditText) findViewById(R.id.ed_emergencyemail_id);
        ed_emergency_relationship = (EditText) findViewById(R.id.ed_emergency_relationship);
        ed_emergency_mobile_no = (EditText) findViewById(R.id.ed_emergency_mobile_no);
    }

    private void init() {

        header = new HeaderControler(this,true,false,"Parent Information");
        login_Details = new LoginDetails();
        dft = new DataFetchService(this);
        progressDialog = Constant.getProgressDialog(this);
        btn_next_emergency_info.setOnClickListener(this);
        lay_back_investment.setOnClickListener(this);
        btn_saveemergency_info.setOnClickListener(this);
        btn_cancel_emergency_info.setOnClickListener(this);
        btn_editemertgency_info.setOnClickListener(this);
        btn_next_emergency_info.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v==btn_next_emergency_info)
        {
            Intent medicalInfo = new Intent(EmergencyContactActivity.this,MedicalInformationActivity.class);
            medicalInfo.putExtra("clt_id", AppController.clt_id);
            medicalInfo.putExtra("msd_ID", AppController.msd_ID);
            medicalInfo.putExtra("usl_id", AppController.usl_id);
            medicalInfo.putExtra("School_name", AppController.school_name);
            medicalInfo.putExtra("board_name", AppController.Board_name);
            medicalInfo.putExtra("version_name", AppController.versionName);
            medicalInfo.putExtra("verion_code",AppController.versionCode);
            startActivity(medicalInfo);
        }
        else if(v==btn_cancel_emergency_info){
            View view = this.getCurrentFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            ed_emergencyemail_id.setError(null);
            ed_emergency_mobile_no.setError(null);
            AppController.editButtonClicked="false";
            ll_edit_EmergencyContactInfo.setVisibility(View.VISIBLE);
            ll_saveEmergencyContactInfo.setVisibility(View.GONE);
            disableEditText();
            callWebserviceforEnergencyContactInfo(AppController.msd_ID);
        }
        else if(v==btn_editemertgency_info){
            ll_saveEmergencyContactInfo.setVisibility(View.VISIBLE);
            ll_edit_EmergencyContactInfo.setVisibility(View.GONE);
            AppController.editButtonClicked="true";
            //Enable Editiong
            enableEditText();
        }
        else if(v==btn_saveemergency_info){
            saveInfo();

            if(!isValidEmailId(emailid) && ed_emergency_mobile_no.getText().length()!=10){
                ed_emergencyemail_id.setError("Invalid Email Id");
                ed_emergency_mobile_no.setError("Invalid Mobile Number");
            }
            else if(!isValidEmailId(emailid)){
                ed_emergencyemail_id.setError("Invalid Email Id");
            }
            else if(ed_emergency_mobile_no.getText().length()!=10){
                ed_emergency_mobile_no.setError("Invalid Mobile Number");
            }
            else {
                ed_emergencyemail_id.setError(null);
                ed_emergency_mobile_no.setError(null);
                callWebserviceSaveEmergencyContactInfo(AppController.msd_ID, lastname, firstname, middlename, emailid, relationship, mobile_no);
            }
        }
        else if(v==lay_back_investment)
        {
            /*if(AppController.SiblingActivity.equalsIgnoreCase("true"))
            {
                Intent back = new Intent(EmergencyContactActivity.this,Profile_Sibling.class);
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
                Intent back = new Intent(EmergencyContactActivity.this,ProfileActivity.class);
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
            Intent back = new Intent(EmergencyContactActivity.this,MotherInformationActivity.class);
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
        }

    }

    private boolean isValidEmailId(String emailid) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(emailid);
        return matcher.matches();
    }

    private void callWebserviceSaveEmergencyContactInfo(String msd_ID,String lastname,String firstname,String middlename,String emailid,String relationship,String mobile_no) {

        if(!progressDialog.isShowing()) {
            progressDialog.show();
        }
        dft.updateEmergencyContactInfo(msd_ID, lastname, firstname, middlename, emailid, relationship, mobile_no,updateEmergencyContactInfoMethod_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_Details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_Details.Status.equalsIgnoreCase("1")) {

                                Constant.showOkPopup(EmergencyContactActivity.this, login_Details.StatusMsg, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent motherInfo = new Intent(EmergencyContactActivity.this, EmergencyContactActivity.class);
                                        motherInfo.putExtra("clt_id", AppController.clt_id);
                                        motherInfo.putExtra("msd_ID", AppController.msd_ID);
                                        motherInfo.putExtra("usl_id", AppController.usl_id);
                                        motherInfo.putExtra("School_name", AppController.school_name);
                                        motherInfo.putExtra("board_name", AppController.Board_name);
                                        motherInfo.putExtra("version_name", AppController.versionName);
                                        motherInfo.putExtra("verion_code", AppController.versionCode);
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

    private void saveInfo() {
        lastname = ed_emergencylastname.getText().toString();
        firstname = ed_emergencyfirstname.getText().toString();
        middlename = ed_emergencymiddlename.getText().toString();
        emailid = ed_emergencyemail_id.getText().toString();
        relationship = ed_emergency_relationship.getText().toString();
        mobile_no = ed_emergency_mobile_no.getText().toString();
    }

    private void enableEditText() {

        ed_emergencylastname.setKeyListener(variable);
        ed_emergencylastname.setInputType(InputType.TYPE_CLASS_TEXT);
        ed_emergencyfirstname.setKeyListener(variable);
        ed_emergencyfirstname.setInputType(InputType.TYPE_CLASS_TEXT);
        ed_emergencymiddlename.setKeyListener(variable);
        ed_emergencymiddlename.setInputType(InputType.TYPE_CLASS_TEXT);
        ed_emergencyemail_id.setKeyListener(variable);
        ed_emergencyemail_id.setInputType(InputType.TYPE_CLASS_TEXT);
        ed_emergency_relationship.setKeyListener(variable);
        ed_emergency_relationship.setInputType(InputType.TYPE_CLASS_TEXT);
        ed_emergency_mobile_no.setKeyListener(variable);
        ed_emergencylastname.setInputType(InputType.TYPE_CLASS_PHONE);
    }

    @Override
    public void onBackPressed() {

        Intent back = new Intent(EmergencyContactActivity.this,MotherInformationActivity.class);
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
   /*     if(AppController.SiblingActivity.equalsIgnoreCase("true"))
        {
            Intent back = new Intent(EmergencyContactActivity.this,Profile_Sibling.class);
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
            Intent back = new Intent(EmergencyContactActivity.this,ProfileActivity.class);
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
