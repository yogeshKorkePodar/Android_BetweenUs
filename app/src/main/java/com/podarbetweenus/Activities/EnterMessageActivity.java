package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.Entity.RecipentResult;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 9/28/2015.
 */
public class EnterMessageActivity extends Activity implements View.OnClickListener{
    //Linear Layout
    LinearLayout lay_back_investment;
    //EditText
    EditText ed_category,ed_message_sub,ed_message_content;
    //Button
    Button btn_send_msg;
    //ProgressDialog
    ProgressDialog progressDialog;
    //TextView
    TextView tv_error_msg;

    LoginDetails login_details;
    DataFetchService dft;

    public ArrayList<RecipentResult> receipent_dropdown_result = new ArrayList<RecipentResult>();

    String msd_ID,clt_id,board_name,usl_id,check="1",toUslId,message,attachment_path,attachment_name,sender_name,date,
            school_name,msg_subject,msg_body,receipent,versionName,announcementCount,behaviourCount,isStudentresource;
    String[] select_receipent;
    ArrayList<String> strings_receipent;
    String ReceipentDropdownMethodName = "GetRecipentDropDownValue";
    String EnterMessageMethod_name = "PostNewMessage";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_expandable_enter_message);
        findViews();
        init();
        getIntentId();
        AppController.listItemSelected = -1;
        AppController.SentEnterDropdown = "true";
    }

    private void init() {
        btn_send_msg.setOnClickListener(this);
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        progressDialog = Constant.getProgressDialog(this);
        ed_category.setOnClickListener(this);
    }

    private void findViews() {
        //EditText
        ed_category = (EditText) findViewById(R.id.ed_category);
        ed_message_content = (EditText) findViewById(R.id.ed_message_content);
        ed_message_sub = (EditText) findViewById(R.id.ed_message_sub);
        //TextView
        tv_error_msg = (TextView) findViewById(R.id.tv_error_msg);
        //Button
        btn_send_msg = (Button) findViewById(R.id.btn_send_msg);

    }
    private void getIntentId() {
        Intent intent = getIntent();
        board_name = intent.getStringExtra("board_name");
        clt_id = intent.getStringExtra("clt_id");
        msd_ID = intent.getStringExtra("msd_ID");
        usl_id = intent.getStringExtra("usl_id");
        school_name = intent.getStringExtra("School_name");
        versionName = intent.getStringExtra("version_name");
        announcementCount = intent.getStringExtra("annpuncement_count");
        behaviourCount = intent.getStringExtra("behaviour_count");
        isStudentresource = intent.getStringExtra("isStudentResource");
        AppController.versionName = versionName;
        AppController.clt_id = clt_id;
        AppController.msd_ID = msd_ID;
        AppController.usl_id = usl_id;
        AppController.school_name = school_name;
    }

    @Override
    public void onClick(View v) {
        if(v==lay_back_investment)
        {
            if(AppController.SiblingActivity.equalsIgnoreCase("true"))
            {
                Intent back = new Intent(EnterMessageActivity.this,Profile_Sibling.class);
                AppController.OnBackpressed = "false";
                AppController.clt_id = clt_id;
                AppController.msd_ID = msd_ID;
                AppController.usl_id = usl_id;
                AppController.school_name = school_name;
                back.putExtra("clt_id", AppController.clt_id);
                back.putExtra("msd_ID", AppController.msd_ID);
                back.putExtra("usl_id", AppController.usl_id);
                back.putExtra("board_name",board_name);
                back.putExtra("isStudentResource",isStudentresource);
                back.putExtra("School_name", AppController.school_name);
                back.putExtra("version_name", AppController.versionName);
                startActivity(back);
            }
            else {
                Intent back = new Intent(EnterMessageActivity.this,ProfileActivity.class);
                AppController.OnBackpressed = "false";
                AppController.clt_id = clt_id;
                AppController.msd_ID = msd_ID;
                AppController.usl_id = usl_id;
                AppController.school_name = school_name;
                back.putExtra("clt_id", AppController.clt_id);
                back.putExtra("msd_ID", AppController.msd_ID);
                back.putExtra("usl_id", AppController.usl_id);
                back.putExtra("board_name",board_name);
                back.putExtra("isStudentResource",isStudentresource);
                back.putExtra("School_name", AppController.school_name);
                back.putExtra("version_name", AppController.versionName);
                startActivity(back);
            }
        }
        else if(v==ed_category){
            //Call Webservice for Receipent dropdown
            CallReceipentDropdownWebservice(clt_id, msd_ID);
        }
        else if (v==btn_send_msg){
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            //Webservcie call for view Messages
            enterMessage();
        }
    }
    private void CallReceipentDropdownWebservice(String clt_id,String msd_ID) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getreceipentdatedeopdown(clt_id, msd_ID, ReceipentDropdownMethodName, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_details.Status.equalsIgnoreCase("1")) {

                                strings_receipent = new ArrayList<String>();

                                try {
                                    for (int i = 0; i < login_details.RecipentResult.size(); i++) {
                                        strings_receipent.add(login_details.RecipentResult.get(i).name.toString());
                                        select_receipent = new String[strings_receipent.size()];
                                        select_receipent = strings_receipent.toArray(select_receipent);

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                selectReceipent(select_receipent);

                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            } else {
                                Constant.showOkPopup(EnterMessageActivity.this, "Please Check Internet Connectivity", new DialogInterface.OnClickListener() {

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
                        Log.d("LoginActivity", "ERROR.._---" + error.getCause());
                    }
                });
    }

    private void selectReceipent( final String[] select_receipent) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.dialog_list, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Select Recipient");
        final ListView select_list = (ListView) convertView.findViewById(R.id.select_list);

        alertDialog.setItems(select_receipent, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                ed_category.setText(select_receipent[item]);
                dialog.dismiss();
                String selectedreceipent = select_receipent[item];
                Log.e("RECEIPENT", selectedreceipent);
                toUslId = getIDfromReceipent(selectedreceipent);
               Log.e("TOUSLID", toUslId);

            }
        });
        alertDialog.show();
    }

    private void enterMessage() {
        msg_subject = ed_message_sub.getText().toString();
        msg_body = ed_message_content.getText().toString();
        receipent = ed_category.getText().toString();
        if(receipent.equalsIgnoreCase("")){
            tv_error_msg.setText("Please Select Receipent");
            tv_error_msg.setVisibility(View.VISIBLE);
        }
        else if(msg_subject.equalsIgnoreCase("")){
            tv_error_msg.setText("Please Enter Message Subject");
            tv_error_msg.setVisibility(View.VISIBLE);
        }
        else if(msg_body.equalsIgnoreCase("")){
            tv_error_msg.setText("Please Enter Message");
            tv_error_msg.setVisibility(View.VISIBLE);
        }
        else {
        callEnterMessagewebservcie(clt_id, msg_subject,msg_body,usl_id, msd_ID, toUslId);
            ed_message_content.setText("");
            ed_message_sub.setText("");
            ed_category.setText("");
        }
    }

    private String getIDfromReceipent(String selectedUslId) {
        String id = "0";
        for(int i=0;i<login_details.RecipentResult.size();i++) {
            if(login_details.RecipentResult.get(i).name.equalsIgnoreCase(selectedUslId)){
                id= login_details.RecipentResult.get(i).usl_Id;
            }
        }
        return id;
    }

    private void callEnterMessagewebservcie(String clt_id,String msg_subject,String msg_body,String usl_id,String msd_ID,String toUslId) {

        if(!progressDialog.isShowing()) {
            progressDialog.show();
        }
        dft.getEnterMessages(clt_id,msg_subject,msg_body,usl_id, msd_ID,toUslId, EnterMessageMethod_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            tv_error_msg.setVisibility(View.GONE);
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_details.Status.equalsIgnoreCase("1")) {
                                tv_error_msg.setTextColor(getResources().getColor(R.color.green));
                                tv_error_msg.setText(login_details.StatusMsg);
                                tv_error_msg.setVisibility(View.VISIBLE);


                                Intent i = new  Intent(EnterMessageActivity.this,DashboardActivity.class);
                                Intent intent = getIntent();
                                board_name = intent.getStringExtra("board_name");
                                String clt_id = intent.getStringExtra("clt_id");
                                String  msd_ID = intent.getStringExtra("msd_ID");
                                String usl_id = intent.getStringExtra("usl_id");
                                String school_name = intent.getStringExtra("School_name");
                                announcementCount = intent.getStringExtra("annpuncement_count");
                                behaviourCount = intent.getStringExtra("behaviour_count");
                                AppController.clt_id = clt_id;
                                AppController.msd_ID = msd_ID;
                                AppController.usl_id = usl_id;
                                AppController.school_name = school_name;
                                AppController.clt_id = clt_id;
                                AppController.msd_ID = msd_ID;
                                i.putExtra("isStudentResource",isStudentresource);
                                AppController.usl_id = usl_id;
                                AppController.school_name = school_name;
                                AppController.parentMessageSent = "true";
                                i.putExtra("clt_id", AppController.clt_id);
                                i.putExtra("msd_ID", AppController.msd_ID);
                                i.putExtra("usl_id", AppController.usl_id);
                                i.putExtra("board_name", board_name);
                                i.putExtra("version_name", AppController.versionName);
                                i.putExtra("School_name", AppController.school_name);
                                i.putExtra("annpuncement_count",announcementCount);
                                i.putExtra("behaviour_count",behaviourCount);
                                startActivity(i);

                            } else if (login_details.Status.equalsIgnoreCase("0")) {
                                tv_error_msg.setText("Error!!!");
                                tv_error_msg.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Show error or whatever...
                        Log.d("EnterMessageActivity", "ERROR.._---" + error.getCause());
                    }
                });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(AppController.SiblingActivity.equalsIgnoreCase("true"))
        {
            Intent back = new Intent(EnterMessageActivity.this,Profile_Sibling.class);
            AppController.OnBackpressed = "false";
            AppController.clt_id = clt_id;
            AppController.msd_ID = msd_ID;
            AppController.usl_id = usl_id;
            AppController.school_name = school_name;
            back.putExtra("clt_id", AppController.clt_id);
            back.putExtra("msd_ID", AppController.msd_ID);
            back.putExtra("usl_id", AppController.usl_id);
            back.putExtra("board_name",board_name);
            back.putExtra("version_name", AppController.versionName);
            back.putExtra("School_name", AppController.school_name);
            back.putExtra("isStudentResource",isStudentresource);
            startActivity(back);
        }
        else {
            Intent back = new Intent(EnterMessageActivity.this,ProfileActivity.class);
            AppController.OnBackpressed = "false";
            AppController.clt_id = clt_id;
            AppController.msd_ID = msd_ID;
            AppController.usl_id = usl_id;
            AppController.school_name = school_name;
            back.putExtra("clt_id", AppController.clt_id);
            back.putExtra("msd_ID", AppController.msd_ID);
            back.putExtra("usl_id", AppController.usl_id);
            back.putExtra("board_name",board_name);
            back.putExtra("version_name", AppController.versionName);
            back.putExtra("School_name", AppController.school_name);
            back.putExtra("isStudentResource",isStudentresource);
            startActivity(back);
        }
    }

}
