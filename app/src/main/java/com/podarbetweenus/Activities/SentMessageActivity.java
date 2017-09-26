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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.Adapter.MessageAdpater;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.DateDropdownValueDetails;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 9/28/2015.
 */
public class SentMessageActivity extends Activity implements View.OnClickListener{
    //EditText
    EditText ed_select_date_sent_msg;
    //LinearLayout
    LinearLayout lay_back_investment;
    //ProgressDialog
    ProgressDialog progressDialog;
    //ListView
    ListView list_messages;
    //TextView
    TextView tv_no_record;

    DataFetchService dft;
    LoginDetails login_details;
    MessageAdpater message_adapter;
    String msd_ID,clt_id,board_name,usl_id,message_subject,check="1",month_id,versionName,message,attachment_path,attachment_name,
            std_name,latest_month,sender_name,date,latest_month_id,school_name,month_year,toUslId,pmuId,isStudentresource,pageNo="1",pageSize="300";
    String DropdownMethodName = "GetDateDropdownValue";
    String SentMessageMethodName = "GetSentMessageData";
    String LastSentMessageData = "GetLastSentMessageData";
    String[] select_date;
    ArrayList<String> strings_date ;
    int notificationId,versionCode;
    public ArrayList<DateDropdownValueDetails> date_dropdownlist = new ArrayList<DateDropdownValueDetails>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.template_expandable_sent_messages);
        findViews();
        init();
        getIntentId();
        tv_no_record.setVisibility(View.GONE);
        list_messages.setVisibility(View.GONE);
        AppController.LatestMonthResult = "true";
        AppController.dropdownSelected = "false";
        AppController.SentEnterDropdown = "true";
        AppController.listItemSelected = -1;
        if(AppController.LatestMonthResult.equalsIgnoreCase("true")){
        CallDropdownWebservice(msd_ID, clt_id, AppController.Board_name);
        }
    }

    private void getIntentId() {
        try {
            Intent intent = getIntent();
            board_name = intent.getStringExtra("board_name");
            clt_id = intent.getStringExtra("clt_id");
            msd_ID = intent.getStringExtra("msd_ID");
            usl_id = intent.getStringExtra("usl_id");
            school_name = intent.getStringExtra("School_name");
            versionName = intent.getStringExtra("version_name");
            versionCode = intent.getIntExtra("version_code", 0);
            std_name = intent.getStringExtra("Std Name");
            isStudentresource = intent.getStringExtra("isStudentResource");
            AppController.versionName = versionName;
            AppController.versionCode = versionCode;
            AppController.school_name = school_name;
            AppController.msd_ID = msd_ID;
            AppController.clt_id = clt_id;
            AppController.Board_name = board_name;
            AppController.usl_id = usl_id;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        ed_select_date_sent_msg.setOnClickListener(this);
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        progressDialog = Constant.getProgressDialog(this);
    }
    private void findViews() {
        //EditText
        ed_select_date_sent_msg = (EditText) findViewById(R.id.ed_select_date_sent_msg);
        //ListView
        list_messages = (ListView) findViewById(R.id.list_messages);
        //TextView
        tv_no_record = (TextView) findViewById(R.id.tv_no_record);
    }
    @Override
    public void onClick(View v) {
        if(v==ed_select_date_sent_msg)
        {
            AppController.dropdownSelected = "true";
            AppController.SentEnterDropdown = "true";
            CallDropdownWebservice(msd_ID,clt_id, AppController.Board_name);
        }
        else if(v==lay_back_investment)
        { if(AppController.SiblingActivity.equalsIgnoreCase("true"))
        {
            Intent back = new Intent(SentMessageActivity.this,Profile_Sibling.class);
            AppController.OnBackpressed = "false";
            AppController.clt_id = clt_id;
            AppController.msd_ID = msd_ID;
            AppController.usl_id = usl_id;
            AppController.Board_name = board_name;
            back.putExtra("clt_id", AppController.clt_id);
            back.putExtra("msd_ID", AppController.msd_ID);
            back.putExtra("usl_id", AppController.usl_id);
            back.putExtra("isStudentResource",isStudentresource);
            back.putExtra("board_name", AppController.Board_name);
            back.putExtra("version_name", AppController.versionName);
            back.putExtra("verion_code", AppController.versionCode);
            AppController.versionName = versionName;
            back.putExtra("School_name", AppController.school_name);
            startActivity(back);
        }
        else {
            Intent back = new Intent(SentMessageActivity.this,ProfileActivity.class);
            AppController.OnBackpressed = "false";
            AppController.clt_id = clt_id;
            AppController.msd_ID = msd_ID;
            AppController.usl_id = usl_id;
            AppController.Board_name = board_name;
            back.putExtra("clt_id", AppController.clt_id);
            back.putExtra("msd_ID", AppController.msd_ID);
            back.putExtra("usl_id", AppController.usl_id);
            back.putExtra("board_name", AppController.Board_name);
            back.putExtra("version_name", AppController.versionName);
            back.putExtra("verion_code", AppController.versionCode);
            back.putExtra("isStudentResource",isStudentresource);
            AppController.versionName = versionName;
            back.putExtra("School_name", AppController.school_name);
            startActivity(back);
        }
        }

    }
    private void CallDropdownWebservice(String msd_ID,  String clt_id,String Brd_name) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }

        }
        else{
            progressDialog.dismiss();
        }
        dft.getdatedeopdown(msd_ID, clt_id, Brd_name, DropdownMethodName, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_details.Status.equalsIgnoreCase("1")) {

                                strings_date = new ArrayList<String>();

                                try {
                                    for (int i = 0; i < login_details.DateDropdownValueDetails.size(); i++) {
                                        strings_date.add(login_details.DateDropdownValueDetails.get(i).MonthYear.toString());
                                        month_year = login_details.DateDropdownValueDetails.get(i).MonthYear;
                                        AppController.month_year = month_year;
                                        select_date = new String[strings_date.size()];
                                        select_date = strings_date.toArray(select_date);
                                        if(AppController.LatestMonthResult.equalsIgnoreCase("true")) {
                                            latest_month_id = login_details.DateDropdownValueDetails.get(0).monthid;
                                            latest_month = login_details.DateDropdownValueDetails.get(0).MonthYear;
                                            AppController.month_id = latest_month_id;
                                            //Webservcie call for Sent Messages
                                            callSentMessageswebservcie(AppController.clt_id, usl_id, latest_month_id, check,pageNo,pageSize);
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                    selectDate(select_date);
                                    AppController.LatestMonthResult = "false";
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            } else if(login_details.Status.equalsIgnoreCase("0")) {

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
    private void selectDate( final String[] select_date) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.dialog_list, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Select Month");
        final ListView select_list = (ListView) convertView.findViewById(R.id.select_list);

        alertDialog.setItems(select_date, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                ed_select_date_sent_msg.setText(select_date[item]);
                dialog.dismiss();
                String selecteddate = select_date[item];
                Log.e("DATE", selecteddate);
                month_id = getIDfromMonth(selecteddate);
                Log.e("MONTH _ID", month_id);
                //Webservcie call for Sent Messages
                callSentMessageswebservcie(clt_id, usl_id, month_id, check,pageNo,pageSize);
            }
        });
        if(AppController.LatestMonthResult.equalsIgnoreCase("false")){
            alertDialog.show();
        }
    }
    private String getIDfromMonth(String selecteddate) {
        String id = "0";
        for(int i=0;i<login_details.DateDropdownValueDetails.size();i++) {
            if(login_details.DateDropdownValueDetails.get(i).MonthYear.equalsIgnoreCase(selecteddate)){
                id= login_details.DateDropdownValueDetails.get(i).monthid;
            }
        }
        return id;
    }

    private void callSentMessageswebservcie(String clt_id,String usl_id,String month_id,String check, final String pageNo, final String pageSize) {

        if(!progressDialog.isShowing()) {
            progressDialog.show();
        }
        dft.getsentMessages(clt_id, usl_id, month_id, check,pageNo,pageSize,SentMessageMethodName, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_details.Status.equalsIgnoreCase("1")) {
                                if(AppController.dropdownSelected.equalsIgnoreCase("false")){

                                    ed_select_date_sent_msg.setText(latest_month);
                                }
                                tv_no_record.setVisibility(View.GONE);
                                list_messages.setVisibility(View.VISIBLE);
                                setUIData();
                                list_messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        AppController.SendMessageLayout = "false";
                                        message = login_details.ViewMessageResult.get(position).pmg_Message;
                                        attachment_name = login_details.ViewMessageResult.get(position).pmg_file_name;
                                        attachment_path = login_details.ViewMessageResult.get(position).pmg_file_path;
                                        sender_name = login_details.ViewMessageResult.get(position).Fullname;
                                        date = login_details.ViewMessageResult.get(position).pmg_date;
                                        toUslId = login_details.ViewMessageResult.get(position).usl_ID;
                                        pmuId = login_details.ViewMessageResult.get(position).pmu_ID;
                                        message_subject = login_details.ViewMessageResult.get(position).pmg_subject;
                                        Intent detail_message = new Intent(SentMessageActivity.this, DetailMessageActivity.class);

                                        AppController.school_name = school_name;
                                        detail_message.putExtra("Message", message);
                                        detail_message.putExtra("Attachment Name", attachment_name);
                                        detail_message.putExtra("Attachment Path", attachment_path);
                                        detail_message.putExtra("Sender Name", sender_name);
                                        detail_message.putExtra("Message Subject", message_subject);
                                        detail_message.putExtra("School_name", AppController.school_name);
                                        detail_message.putExtra("board_name", AppController.Board_name);
                                        detail_message.putExtra("Date", date);
                                        detail_message.putExtra("ToUslId",toUslId);
                                        detail_message.putExtra("PmuId",pmuId);
                                        detail_message.putExtra("isStudentResource",isStudentresource);
                                        detail_message.putExtra("version_name", AppController.versionName);
                                        detail_message.putExtra("verion_code", AppController.versionCode);
                                        AppController.versionName = versionName;
                                        Intent intent = getIntent();
                                        String clt_id = intent.getStringExtra("clt_id");
                                        String msd_ID = intent.getStringExtra("msd_ID");
                                        String usl_id = intent.getStringExtra("usl_id");
                                        AppController.clt_id = clt_id;
                                        AppController.msd_ID = msd_ID;
                                        AppController.usl_id = usl_id;
                                        AppController.school_name = school_name;
                                        detail_message.putExtra("clt_id", AppController.clt_id);
                                        detail_message.putExtra("msd_ID", AppController.msd_ID);
                                        detail_message.putExtra("usl_id", AppController.usl_id);
                                        detail_message.putExtra("School_name", AppController.school_name);
                                        detail_message.putExtra("board_name", AppController.Board_name);
                                        detail_message.putExtra("version_name", AppController.versionName);
                                        detail_message.putExtra("verion_code", AppController.versionCode);
                                        AppController.versionName = versionName;
                                        detail_message.putExtra("isStudentResource",isStudentresource);
                                        startActivity(detail_message);
                                    }
                                });
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                            } else if(login_details.Status.equalsIgnoreCase("0")) {
                                if(AppController.dropdownSelected.equalsIgnoreCase("true")) {

                                    tv_no_record.setText("No Records Found");
                                    tv_no_record.setVisibility(View.VISIBLE);
                                    list_messages.setVisibility(View.GONE);
                                }
                                else if(AppController.dropdownSelected.equalsIgnoreCase("false")){
                                    String check = "1";
                                    callLastSentMessageswebservcie(AppController.clt_id, AppController.usl_id, AppController.month_id, check,pageNo,pageSize);
                                }
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
    private void setUIData() {
        message_adapter = new MessageAdpater(SentMessageActivity.this,login_details.ViewMessageResult,notificationId);
        list_messages.setAdapter(message_adapter);
    }
    private void callLastSentMessageswebservcie(String clt_id,String usl_id,String month_id,String check,String pageNo,String pageSize) {

        if(!progressDialog.isShowing()) {
            progressDialog.show();
        }
        dft.getsentMessages(clt_id, usl_id, month_id, check,pageNo,pageSize, LastSentMessageData, Request.Method.POST,
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
                                list_messages.setVisibility(View.VISIBLE);
                                setUIData();
                                list_messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        AppController.SendMessageLayout = "false";
                                        message = login_details.ViewMessageResult.get(position).pmg_Message;
                                        attachment_name = login_details.ViewMessageResult.get(position).pmg_file_name;
                                        attachment_path = login_details.ViewMessageResult.get(position).pmg_file_path;
                                        sender_name = login_details.ViewMessageResult.get(position).Fullname;
                                        date = login_details.ViewMessageResult.get(position).pmg_date;
                                        message_subject = login_details.ViewMessageResult.get(position).pmg_subject;
                                        toUslId = login_details.ViewMessageResult.get(position).usl_ID;
                                        pmuId = login_details.ViewMessageResult.get(position).pmu_ID;
                                        Intent detail_message = new Intent(SentMessageActivity.this, DetailMessageActivity.class);

                                        AppController.school_name = school_name;
                                        detail_message.putExtra("Message", message);
                                        detail_message.putExtra("Attachment Name", attachment_name);
                                        detail_message.putExtra("Attachment Path", attachment_path);
                                        detail_message.putExtra("Sender Name", sender_name);
                                        detail_message.putExtra("Message Subject", message_subject);
                                        detail_message.putExtra("School_name", AppController.school_name);
                                        detail_message.putExtra("Date", date);
                                        detail_message.putExtra("ToUslId",toUslId);
                                        detail_message.putExtra("PmuId",pmuId);
                                        detail_message.putExtra("version_name", AppController.versionName);
                                        detail_message.putExtra("verion_code", AppController.versionCode);
                                        AppController.versionName = versionName;
                                        detail_message.putExtra("isStudentResource",isStudentresource);
                                        detail_message.putExtra("board_name", AppController.Board_name);
                                        Intent intent = getIntent();
                                        String clt_id = intent.getStringExtra("clt_id");
                                        String msd_ID = intent.getStringExtra("msd_ID");
                                        String usl_id = intent.getStringExtra("usl_id");
                                        AppController.clt_id = clt_id;
                                        AppController.msd_ID = msd_ID;
                                        AppController.usl_id = usl_id;
                                        AppController.school_name = school_name;
                                        detail_message.putExtra("clt_id", AppController.clt_id);
                                        detail_message.putExtra("msd_ID", AppController.msd_ID);
                                        detail_message.putExtra("usl_id", AppController.usl_id);
                                        detail_message.putExtra("School_name", AppController.school_name);
                                        detail_message.putExtra("version_name", AppController.versionName);
                                        AppController.versionName = versionName;
                                        detail_message.putExtra("board_name", AppController.Board_name);
                                        startActivity(detail_message);
                                    }
                                });
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                            } else if (login_details.Status.equalsIgnoreCase("0")) {
                                tv_no_record.setText("No Records Found");
                                tv_no_record.setVisibility(View.VISIBLE);
                                list_messages.setVisibility(View.GONE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Show error or whatever...
                        Log.d("Send MessageActivity", "ERROR.._---" + error.getCause());
                    }
                });
    }
    @Override
    public void onBackPressed() {
        if(AppController.SiblingActivity.equalsIgnoreCase("true"))
        {
            Intent back = new Intent(SentMessageActivity.this,Profile_Sibling.class);
            AppController.OnBackpressed = "false";
            AppController.clt_id = clt_id;
            AppController.msd_ID = msd_ID;
            AppController.usl_id = usl_id;
            AppController.Board_name = board_name;
            back.putExtra("clt_id", AppController.clt_id);
            back.putExtra("msd_ID", AppController.msd_ID);
            back.putExtra("usl_id", AppController.usl_id);
            back.putExtra("board_name", AppController.Board_name);
            back.putExtra("School_name", AppController.school_name);
            back.putExtra("version_name", AppController.versionName);
            back.putExtra("verion_code", AppController.versionCode);
            back.putExtra("isStudentResource",isStudentresource);
            AppController.versionName = versionName;
            startActivity(back);
        }
        else {
            Intent back = new Intent(SentMessageActivity.this,ProfileActivity.class);
            AppController.OnBackpressed = "false";
            AppController.clt_id = clt_id;
            AppController.msd_ID = msd_ID;
            AppController.usl_id = usl_id;
            AppController.Board_name = board_name;
            back.putExtra("clt_id", AppController.clt_id);
            back.putExtra("msd_ID", AppController.msd_ID);
            back.putExtra("usl_id", AppController.usl_id);
            back.putExtra("board_name", AppController.Board_name);
            back.putExtra("School_name", AppController.school_name);
            back.putExtra("version_name", AppController.versionName);
            back.putExtra("verion_code", AppController.versionCode);
            back.putExtra("isStudentResource",isStudentresource);
            AppController.versionName = versionName;
            startActivity(back);
        }
    }
}
