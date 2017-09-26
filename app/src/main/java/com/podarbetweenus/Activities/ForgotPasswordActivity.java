package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
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

import org.json.JSONObject;

/**
 * Created by Administrator on 9/25/2015.
 */
public class ForgotPasswordActivity extends Activity implements View.OnClickListener {
    //UI Variables
    //Button
    Button btn_submit;
    //Linear Layout
    LinearLayout lay_back_investment;
    //Edit Text
    EditText ed_mobile_no,ed_emailid;
    //ProgressDialog
    ProgressDialog progressDialog;
    //TextView
    TextView tv_error_msg;

    //LayoutEntities
    HeaderControler header;
    DataFetchService dft;
    LoginDetails login_details;

    String mobile_number,email_id,type;
    String ForgotPassword_Method_Name = "ForgotPassword";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.forgot_password);
        findViews();
        init();
    }

    private void init() {
        btn_submit.setOnClickListener(this);
        lay_back_investment.setOnClickListener(this);
        header = new HeaderControler(this,true,false,"Forgot Password");
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        progressDialog = Constant.getProgressDialog(this);
    }

    private void findViews() {
        //Button
        btn_submit = (Button) findViewById(R.id.btn_submit);
        //Linear Layout
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
        //Edit Text
        ed_mobile_no = (EditText) findViewById(R.id.ed_mobile_no);
        ed_emailid = (EditText) findViewById(R.id.ed_emailid);
        //TextView
        tv_error_msg = (TextView) findViewById(R.id.tv_error_msg);
    }

    @Override
    public void onClick(View v) {
    if(v==btn_submit)
    {
        tv_error_msg.setVisibility(View.GONE);
        forgotPassword();
    }
    else if(v==lay_back_investment)
    {
        finish();
    }
    }
    private void forgotPassword() {
        mobile_number = ed_mobile_no.getText().toString();
        email_id = ed_emailid.getText().toString();

        if(mobile_number.equalsIgnoreCase("") && email_id.equalsIgnoreCase(""))
        {
            tv_error_msg.setVisibility(View.VISIBLE);
            tv_error_msg.setText("Please Enter Either Mobile Number or Email Id");
        }
        else if(mobile_number.matches("[0-9]+")){
            type = "M";
            tv_error_msg.setVisibility(View.GONE);
            callWebserviceGetForgotPassword(mobile_number, type);
        }
        else
        {
            type = "E";
            callWebserviceGetForgotPassword(email_id, type);
        }
    }

    private void callWebserviceGetForgotPassword(String credential, String type) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.forgotPassword(credential, type, ForgotPassword_Method_Name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);

                            if (login_details.Status.equalsIgnoreCase("1")) {

                                Constant.showOkPopup(ForgotPasswordActivity.this, login_details.StatusMsg, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent i = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                        startActivity(i);
                                        dialog.dismiss();
                                    }

                                });
                            }
                            else{
                                Constant.showOkPopup(ForgotPasswordActivity.this, login_details.StatusMsg, new DialogInterface.OnClickListener() {

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
                        Log.d("ForgotPasswordActivity", "ERROR.._---" + error.getCause());
                    }
                });

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //super.onBackPressed();
        finish();

    }
}
