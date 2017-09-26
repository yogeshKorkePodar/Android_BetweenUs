package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

/**
 * Created by Administrator on 9/28/2015.
 */
public class SettingActivity extends Activity implements View.OnClickListener {
    //UIVariable
    //EditText
    EditText ed_old_password,ed_new_password,ed_confirm_password;
    //Button
    Button btn_change_pass_submit;
    //Linear Layout
    LinearLayout lay_back_investment;
    //TextView
    TextView tv_error_msg;

    HeaderControler header;
    DataFetchService dft;
    LoginDetails login_details;
    //ProgressDialog
    ProgressDialog progressDialog;

    String old_password,new_password,confirm_password,usl_id,versionName,isStudentresource;

    String changePassword_Method_name = "ChangePassword";
    int versionCode;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.setting);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getIntentData();
        findViews();
        init();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        usl_id = intent.getStringExtra("usl_id");
        versionName = intent.getStringExtra("version_name");
        versionCode = intent.getIntExtra("version_code", 0);
        isStudentresource = intent.getStringExtra("isStudentResource");
        AppController.versionName = versionName;
        AppController.versionCode = versionCode;
    }

    private void findViews() {
        //Linear Layout
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
        //EditText
        ed_confirm_password = (EditText) findViewById(R.id.ed_confirm_password);
        ed_new_password = (EditText) findViewById(R.id.ed_new_password);
        ed_old_password = (EditText) findViewById(R.id.ed_old_password);
        //Button
        btn_change_pass_submit = (Button) findViewById(R.id.btn_change_pass_submit);
        //TextView
        tv_error_msg = (TextView) findViewById(R.id.tv_error_msg);
    }

    private void init() {

        header = new HeaderControler(this,true,false,"Change Password");
        btn_change_pass_submit.setOnClickListener(this);
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        progressDialog = Constant.getProgressDialog(this);
        lay_back_investment.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v==btn_change_pass_submit){
            tv_error_msg.setVisibility(View.GONE);
            changePassword();
        }
        else if(v==lay_back_investment){
            finish();
        }
    }
    private void changePassword() {
        old_password = ed_old_password.getText().toString();
        new_password = ed_new_password.getText().toString();
        confirm_password = ed_confirm_password.getText().toString();

        if(old_password.equalsIgnoreCase("") && new_password.equalsIgnoreCase("")&& confirm_password.equalsIgnoreCase("")){
            tv_error_msg.setVisibility(View.VISIBLE);
            tv_error_msg.setText("Please Enter All Fields");
        }
        else if(old_password.equalsIgnoreCase(""))
        {
            tv_error_msg.setVisibility(View.VISIBLE);
            tv_error_msg.setText("Please Enter Old Password");
        }
        else if(new_password.equalsIgnoreCase("")){
            tv_error_msg.setVisibility(View.VISIBLE);
            tv_error_msg.setText("Please Enter New Password");
        }
        else if(!new_password.equalsIgnoreCase(confirm_password)){
            tv_error_msg.setVisibility(View.VISIBLE);
            tv_error_msg.setText("New Password and Confirm Password does not match!!");
        }
        else
        {
            try {
                //Webservice call for changePassword
                tv_error_msg.setVisibility(View.GONE);
                callWebserviceChangePassword(usl_id, old_password, new_password);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //Student Details
    private void callWebserviceChangePassword(String usl_id, String old_password,String new_password) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getChangePassword(usl_id, old_password, new_password, changePassword_Method_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if(dft.isInternetOn()==true) {
                            if (!progressDialog.isShowing()) {
                                progressDialog.show();
                            }
                        }
                        else{
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_details.Status.equalsIgnoreCase("1")) {

                                Constant.showOkPopup(SettingActivity.this, login_details.StatusMsg, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent login = new Intent(SettingActivity.this, LoginActivity.class);
                                        startActivity(login);
                                        dialog.dismiss();
                                    }
                                });
                            } else {
                                Constant.showOkPopup(SettingActivity.this, login_details.StatusMsg, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                tv_error_msg.setVisibility(View.VISIBLE);
                                tv_error_msg.setText(login_details.StatusMsg);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Show error or whatever...
                        Log.d("SettingActivity", "ERROR.._---" + error.getCause());
                    }
                });

    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
          super.onBackPressed();
        finish();
    }
}
