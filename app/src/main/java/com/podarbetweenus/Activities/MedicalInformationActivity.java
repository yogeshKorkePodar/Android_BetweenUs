package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.AlertDialog;
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

import java.util.ArrayList;

/**
 * Created by Administrator on 10/3/2015.
 */
public class MedicalInformationActivity extends Activity implements View.OnClickListener{
    //LayoutEntities
    HeaderControler header;
    LoginDetails login_Details;
    DataFetchService dft;
    //Button
    Button btn_savemedical_info,btn_medicalcancel,btn_editmedical_info,btn_medicalnext;
    //Linear Layout
    LinearLayout lay_back_investment,ll_edit_medical_info,ll_save_medical_info;
    //EditText
    EditText ed_select_bloddgroup,ed_all_allergies,ed_health_info,ed_height,ed_weight,ed_regular_medication,ed_regular_medication_with_dosage,ed_any_impairment,
            ed_exemption_from_activities;
    //ProgressDialog
    ProgressDialog progressDialog;

    KeyListener variable;
    String clt_id,msd_ID,usl_id,board_name,school_name,versionName,blood_group_id,all_allergies,health_info,height,weight,regular_medication,regular_medication_with_dosage,any_impairment,exemption_from_activities;
    String MedicalInfoMethod_name = "GetMedicalInfo";
    String methodName="GetParentInfoDropDown";
    String updateMedicalInfoMethod_name = "UpdateMedicalInfo";
    Context mcontext;
    ArrayList<String> strings_blood_group = new ArrayList<String>();
    String[] select_blood_group;
    String TAG = "Medical Information";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.medicalinformation);
        getIntentId();
        findViews();
        init();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getEdittextvariable();
        //setEditText Disable
        disableEditText();
        AppController.editButtonClicked="false";
        //Call Webservice for EmergencyContact Info
        callWebserviceforMedicaltInfo(AppController.msd_ID);
    }

    private void disableEditText() {
        ed_select_bloddgroup.setKeyListener(null);
        ed_height.setKeyListener(null);
        ed_health_info.setKeyListener(null);
        ed_weight.setKeyListener(null);
        ed_exemption_from_activities.setKeyListener(null);
        ed_all_allergies.setKeyListener(null);
        ed_any_impairment.setKeyListener(null);
        ed_regular_medication_with_dosage.setKeyListener(null);
        ed_regular_medication.setKeyListener(null);
    }
    private void getIntentId() {
        Intent intent = getIntent();
        board_name = intent.getStringExtra("board_name");
        clt_id = intent.getStringExtra("clt_id");
        msd_ID = intent.getStringExtra("msd_ID");
        usl_id = intent.getStringExtra("usl_id");
        school_name = intent.getStringExtra("School_name");
        versionName = intent.getStringExtra("version_name");
        AppController.versionName = versionName;
        AppController.dropdownSelected = intent.getStringExtra("DropDownSelected");
        AppController.school_name = school_name;
        AppController.Board_name = board_name;
        AppController.clt_id = clt_id;
        AppController.msd_ID = msd_ID;
    }

    private void getEdittextvariable() {
        variable= ed_select_bloddgroup.getKeyListener();
        variable= ed_height.getKeyListener();
        variable= ed_health_info.getKeyListener();
        variable= ed_weight.getKeyListener();
        variable= ed_exemption_from_activities.getKeyListener();
        variable= ed_all_allergies.getKeyListener();
        variable= ed_any_impairment.getKeyListener();
        variable= ed_regular_medication_with_dosage.getKeyListener();
        variable= ed_regular_medication.getKeyListener();
    }

    private void findViews() {
        //Button
        btn_medicalcancel = (Button) findViewById(R.id.btn_medicalcancel);
        btn_editmedical_info = (Button) findViewById(R.id.btn_editmedical_info);
        btn_medicalnext = (Button) findViewById(R.id.btn_medicalnext);
        btn_savemedical_info = (Button) findViewById(R.id.btn_savemedical_info);
        //Linear Layout
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
        ll_edit_medical_info = (LinearLayout) findViewById(R.id.ll_edit_medical_info);
        ll_save_medical_info = (LinearLayout) findViewById(R.id.ll_save_medical_info);
        //EditText
        ed_select_bloddgroup = (EditText) findViewById(R.id.ed_select_bloddgroup);
        ed_all_allergies = (EditText) findViewById(R.id.ed_all_allergies);
        ed_any_impairment = (EditText) findViewById(R.id.ed_any_impairment);
        ed_exemption_from_activities = (EditText) findViewById(R.id.ed_exemption_from_activities);
        ed_health_info = (EditText) findViewById(R.id.ed_health_info);
        ed_height = (EditText) findViewById(R.id.ed_height);
        ed_weight = (EditText) findViewById(R.id.ed_weight);
        ed_regular_medication = (EditText) findViewById(R.id.ed_regular_medication);
        ed_regular_medication_with_dosage = (EditText) findViewById(R.id.ed_regular_medication_with_dosage);
    }

    private void init() {

        header = new HeaderControler(this,true,false,"Parent Information");
        login_Details =new LoginDetails();
        dft =  new DataFetchService(this);
        progressDialog = Constant.getProgressDialog(this);
        btn_medicalcancel.setOnClickListener(this);
        btn_medicalnext.setOnClickListener(this);
        btn_savemedical_info.setOnClickListener(this);
        btn_editmedical_info.setOnClickListener(this);
        lay_back_investment.setOnClickListener(this);
        ed_select_bloddgroup.setOnClickListener(this);
    }
    private void callWebserviceforMedicaltInfo(String msd_id) {

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        dft.getparentInfo(msd_id, MedicalInfoMethod_name, Request.Method.POST,
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

        ed_select_bloddgroup.setText(login_Details.ParentInfoMedical.get(0).blg_name);
        ed_all_allergies.setText(login_Details.ParentInfoMedical.get(0).hlt_Allergies);
        ed_health_info.setText(login_Details.ParentInfoMedical.get(0).hlt_healthproblems);
        ed_height.setText(login_Details.ParentInfoMedical.get(0).hlt_Height);
        ed_weight.setText(login_Details.ParentInfoMedical.get(0).hlt_Weight);
        ed_regular_medication.setText(login_Details.ParentInfoMedical.get(0).hlt_regmedications);
        ed_regular_medication_with_dosage.setText(login_Details.ParentInfoMedical.get(0).hlt_regmeddosage);
        ed_any_impairment.setText(login_Details.ParentInfoMedical.get(0).hlt_impairment);
        ed_exemption_from_activities.setText(login_Details.ParentInfoMedical.get(0).hlt_expfromactivities);
        blood_group_id = login_Details.ParentInfoMedical.get(0).blg_id;
        AppController.blood_groupId = blood_group_id;
    }

    @Override
    public void onClick(View v) {
        if(v==btn_medicalcancel)
        {
            View view = this.getCurrentFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            AppController.editButtonClicked="false";
            ll_edit_medical_info.setVisibility(View.VISIBLE);
            ll_save_medical_info.setVisibility(View.GONE);
            disableEditText();
            //Call Webservice for EmergencyContact Info
            callWebserviceforMedicaltInfo(AppController.msd_ID);
        }
        else if(v==btn_editmedical_info)
        {
            ll_save_medical_info.setVisibility(View.VISIBLE);
            ll_edit_medical_info.setVisibility(View.GONE);
            AppController.editButtonClicked="true";
            //Enable Editiong
            enableEditText();
        }
        else if(v==ed_select_bloddgroup){
            if(AppController.editButtonClicked.equalsIgnoreCase("true")){
                //Call Wenservice for blood group
                callWebserviceForBloodGroup(mcontext);
            }
        }
        else if(v==btn_savemedical_info){
            saveInfo();
            callWebserviceSaveMedicalInfo(AppController.msd_ID, AppController.blood_groupId,all_allergies, health_info,height,weight,regular_medication,regular_medication_with_dosage,any_impairment,exemption_from_activities);
        }
        else if(v==lay_back_investment)
        {
            Intent back = new Intent(MedicalInformationActivity.this,EmergencyContactActivity.class);
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
            /*if(AppController.SiblingActivity.equalsIgnoreCase("true"))
            {
                Intent back = new Intent(MedicalInformationActivity.this,Profile_Sibling.class);
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
                Intent back = new Intent(MedicalInformationActivity.this,ProfileActivity.class);
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
    private void callWebserviceSaveMedicalInfo(String msd_ID,String blood_group_id,String all_allergies,String health_info,String height,String weight,String regular_medication,String regular_medication_with_dosage,String any_impairment
            ,String exemption_from_activities) {

        if(!progressDialog.isShowing()) {
            progressDialog.show();
        }
        dft.updateMedicalInfo(msd_ID, blood_group_id,all_allergies, health_info, height, weight, regular_medication, regular_medication_with_dosage, any_impairment, exemption_from_activities, updateMedicalInfoMethod_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_Details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_Details.Status.equalsIgnoreCase("1")) {

                                Constant.showOkPopup(MedicalInformationActivity.this, login_Details.StatusMsg, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                        if (AppController.SiblingActivity.equalsIgnoreCase("true")) {

                                            Intent back = new Intent(MedicalInformationActivity.this, Profile_Sibling.class);
                                            AppController.OnBackpressed = "false";
                                            AppController.editButtonClicked = "false";
                                            AppController.clt_id = clt_id;
                                         //   AppController.msd_ID = msd_ID;
                                            AppController.usl_id = usl_id;
                                            AppController.school_name = school_name;
                                            AppController.Board_name = board_name;
                                            back.putExtra("clt_id", AppController.clt_id);
                                            back.putExtra("msd_ID", AppController.msd_ID);
                                            back.putExtra("usl_id", AppController.usl_id);
                                            back.putExtra("board_name", AppController.Board_name);
                                            back.putExtra("School_name", AppController.school_name);
                                            back.putExtra("version_name", AppController.versionName);
                                            startActivity(back);
                                        } else {
                                            Intent back = new Intent(MedicalInformationActivity.this, ProfileActivity.class);
                                            AppController.OnBackpressed = "false";
                                            AppController.editButtonClicked = "false";

                                            AppController.clt_id = clt_id;
                                         //   AppController.msd_ID = msd_ID;
                                            AppController.usl_id = usl_id;
                                            AppController.Board_name = board_name;
                                            AppController.school_name = school_name;
                                            back.putExtra("clt_id", AppController.clt_id);
                                            back.putExtra("msd_ID", AppController.msd_ID);
                                            back.putExtra("usl_id", AppController.usl_id);
                                            back.putExtra("board_name", AppController.Board_name);
                                            back.putExtra("School_name", AppController.school_name);
                                            back.putExtra("version_name", AppController.versionName);
                                            startActivity(back);
                                        }

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
        all_allergies = ed_all_allergies.getText().toString();
        health_info = ed_health_info.getText().toString();
        height = ed_height.getText().toString();
        weight = ed_weight.getText().toString();
        regular_medication = ed_regular_medication.getText().toString();
        regular_medication_with_dosage = ed_regular_medication_with_dosage.getText().toString();
        any_impairment = ed_any_impairment.getText().toString();
        exemption_from_activities = ed_exemption_from_activities.getText().toString();
    }

    private void enableEditText() {
       // ed_select_bloddgroup.setKeyListener(variable);
        ed_height.setKeyListener(variable);
        ed_health_info.setKeyListener(variable);
        ed_weight.setKeyListener(variable);
        ed_exemption_from_activities.setKeyListener(variable);
        ed_all_allergies.setKeyListener(variable);
        ed_any_impairment.setKeyListener(variable);
        ed_regular_medication_with_dosage.setKeyListener(variable);
        ed_regular_medication.setKeyListener(variable);
    }

    public void callWebserviceForBloodGroup(Context mcontext) {
        if(!progressDialog.isShowing()) {
            progressDialog.show();
        }

        dft.getCountryList(methodName, Request.Method.GET,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        login_Details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                        strings_blood_group = new ArrayList<String>();

                        try {
                            for (int i = 0; i < login_Details.ParentInfoBloddGroup.size(); i++) {
                                strings_blood_group.add(login_Details.ParentInfoBloddGroup.get(i).blg_name);

                                select_blood_group = new String[strings_blood_group.size()];
                                select_blood_group = strings_blood_group.toArray(select_blood_group);

                            }
                            selectDropdown(select_blood_group);
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

        alertDialog.setTitle("Select Blood Group");


        final ListView select_list = (ListView) convertView.findViewById(R.id.select_list);

        alertDialog.setItems(select_dropdownvalue, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                ed_select_bloddgroup.setText(select_dropdownvalue[item]);
                String selectedvalue = select_dropdownvalue[item];
                Log.e("Religion", selectedvalue);
                blood_group_id = getID(selectedvalue);
                AppController.blood_groupId = blood_group_id;
                Log.e("blood_group_id", blood_group_id);


                dialog.dismiss();

            }
        });
        alertDialog.show();
    }

    private String getID(String selectedvalue) {

        String id = "0";
            for (int i = 0; i < login_Details.ParentInfoBloddGroup.size(); i++) {
                if (login_Details.ParentInfoBloddGroup.get(i).blg_name.equalsIgnoreCase(selectedvalue)) {
                    id = login_Details.ParentInfoBloddGroup.get(i).blg_id;
                }
            }  return id;
    }

    @Override
    public void onBackPressed() {
        /*if(AppController.SiblingActivity.equalsIgnoreCase("true"))
        {
            Intent back = new Intent(MedicalInformationActivity.this,Profile_Sibling.class);
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
            Intent back = new Intent(MedicalInformationActivity.this,ProfileActivity.class);
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
        Intent back = new Intent(MedicalInformationActivity.this,EmergencyContactActivity.class);
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

}
