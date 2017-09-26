package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 10/13/2015.
 */
public class DetailMessageActivity extends Activity implements View.OnClickListener{

    String detail_msg,date,sender_name,attachement_path,attachment_name,attachment,message_subject,reply_msg,toUslId,isStudentresource,
            clt_id,usl_id,msd_ID,board_name,school_name,pmuId,versionName,announcementCount,behaviourCount,message,msgSubject,encodedUrl,downloadfilename,newFile;
    String serverAddress = "betweenus.in/Uploads/Messages";
    String ReplyMessageMethodName = "ReplyToMessage";
    String UpdateMessageStatusMethodName = "UpdateMessageReadStatus";
    int versionCode;
    //ImageView
    ImageView sender_img,img_attachment,img_send;
    //TextView
    TextView tv_date,tv_detail_msg,tv_message_subject;
    //LinearLayout
    LinearLayout lay_back_investment,ll_send_message_layout;

    //Relative Layout
    RelativeLayout rl_header;
    //EditText
    EditText ed_enter_msg;
    //ProgressDialog
    ProgressDialog progressDialog;
    //LayoutEntities
    HeaderControler header;
    DataFetchService dft;
    LoginDetails login_details;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.details_message);

        findViews();
        getIntentData();
        init();
        ed_enter_msg.setSelection(0);
        if(LoginActivity.get_name(DetailMessageActivity.this).equalsIgnoreCase("6"))
        {

        }

        if(AppController.SendMessageLayout.equalsIgnoreCase("true")){
            ll_send_message_layout.setVisibility(View.VISIBLE);
        }
        else {
            ll_send_message_layout.setVisibility(View.GONE);
        }
        setUIData();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //Webservice Call for UpdateMessageStatus
        callWebserviceUpdateMessageStatus(pmuId, usl_id, clt_id);
    }

    private void callWebserviceUpdateMessageStatus(String pmuId, String usl_id, String clt_id) {
        try{
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getUpdatdStatusMessages(pmuId, usl_id, clt_id, UpdateMessageStatusMethodName, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_details.Status.equalsIgnoreCase("1")) {
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
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUIData() {
        tv_detail_msg.setText(detail_msg);

      /*  String date_array[] = date.split(" ");
        String date_format = date_array[0];
        Log.e("DATE:- ", date_format);

        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String formatted_date = null;

        try {
            date = inputFormat.parse(date_format);
            formatted_date = outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        tv_date.setText(date);

        tv_message_subject.setText(message_subject);
        if(attachement_path.equalsIgnoreCase("0")|| attachement_path.equalsIgnoreCase(""))
        {
            img_attachment.setVisibility(View.GONE);
        }
        else {
            img_attachment.setVisibility(View.VISIBLE);
        }
    }

    private void getIntentData() {
        try {
            Intent intent = getIntent();
            detail_msg = intent.getStringExtra("Message");
            date = intent.getStringExtra("Date");
            sender_name = intent.getStringExtra("Sender Name");
            message_subject = intent.getStringExtra("Message Subject");
            school_name = intent.getStringExtra("School_name");
            toUslId = intent.getStringExtra("ToUslId");
            clt_id = intent.getStringExtra("clt_id");
            msd_ID = intent.getStringExtra("msd_ID");
            usl_id = intent.getStringExtra("usl_id");
            pmuId = intent.getStringExtra("PmuId");
            board_name = intent.getStringExtra("board_name");
            attachement_path = intent.getStringExtra("Attachment Path");
            attachment_name = intent.getStringExtra("Attachment Name");
            attachment = attachement_path.replace("C:\\inetpub\\", "http:\\");
            announcementCount = intent.getStringExtra("annpuncement_count");
            behaviourCount = intent.getStringExtra("behaviour_count");
            isStudentresource = intent.getStringExtra("isStudentResource");
            AppController.clt_id = clt_id;
            AppController.msd_ID = msd_ID;
            AppController.usl_id = usl_id;
            AppController.school_name = school_name;
            AppController.Board_name = board_name;
            versionName = intent.getStringExtra("version_name");
            versionCode = intent.getIntExtra("version_code", 0);
            AppController.versionName = versionName;
            AppController.versionCode = versionCode;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void init() {
        header = new HeaderControler(this,true,false,sender_name);
        lay_back_investment.setOnClickListener(this);
        img_attachment.setOnClickListener(this);
        img_send.setOnClickListener(this);
        progressDialog = Constant.getProgressDialog(this);
        login_details = new LoginDetails();
        dft = new DataFetchService(this);
        rl_header.setBackgroundColor(Color.parseColor("#00829C"));
        lay_back_investment.setBackgroundColor(Color.parseColor("#00829C"));
    }
    private  void findViews(){
        //TextView
        tv_detail_msg = (TextView) findViewById(R.id.tv_detail_msg);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_message_subject = (TextView) findViewById(R.id.tv_message_subject);
        //ImageView
        sender_img = (ImageView) findViewById(R.id.sender_img);
        img_attachment = (ImageView) findViewById(R.id.img_attachment);
        img_send = (ImageView) findViewById(R.id.img_send);
        //Linear Layout
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
        ll_send_message_layout = (LinearLayout)findViewById(R.id.ll_send_message_layout);
        //Relative Layout
        rl_header = (RelativeLayout) findViewById(R.id.rl_header);
        //Edit Text
        ed_enter_msg = (EditText) findViewById(R.id.ed_enter_msg);
    }

    @Override
    public void onClick(View v) {
        if(v==lay_back_investment)
        {
            AppController.ListClicked = "true";
            super.onBackPressed();
            finish();
        }
        else if(v==img_attachment){
            try {
                //download file
                try {
                    String host = "http://" + serverAddress + "/";
                    downloadfilename = attachment.substring(attachment.lastIndexOf('/') + 1);
                    Log.e("filename", downloadfilename);
                    newFile = host+downloadfilename;
                    encodedUrl = host + URLEncoder.encode(downloadfilename, "utf-8");
                    Log.e("enco url", encodedUrl);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                new DownloadFileFromURL().execute(newFile);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(newFile));
                startActivity(i);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(v==img_send){
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);

            sendMessage();
            ed_enter_msg.setText("");
        }
    }
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showDialog(progress_bar_type);

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
                        + "/"+downloadfilename);
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
            //  dismissDialog(progress_bar_type);
            Toast.makeText(DetailMessageActivity.this, "Downloading Completed", Toast.LENGTH_SHORT).show();
        }

    }
    private void sendMessage() {
        message = ed_enter_msg.getText().toString();
        reply_msg = message+"\n\n"+"--Old Message--"+date+"\n"+detail_msg;
        if(message_subject.contains("Re: ")){
            msgSubject = message_subject;
        }
        else {
            msgSubject = "Re: "+message_subject;
        }
        //Webservice call for Reply Message
        if(message.equalsIgnoreCase("")){
            Constant.showOkPopup(this, "Please Enter Message", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }

            });
        }
        else {
            callReplyMessageswebservcie(clt_id, msgSubject, reply_msg, usl_id, msd_ID, toUslId);
        }
    }
    private void callReplyMessageswebservcie(String clt_id,String message_subject,String reply_msg,String usl_id,String msd_ID,String toUslId) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getReplyMessage(clt_id, message_subject, reply_msg, usl_id, msd_ID, toUslId, ReplyMessageMethodName, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_details.Status.equalsIgnoreCase("1")) {
                                Constant.showOkPopup(DetailMessageActivity.this, login_details.StatusMsg, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent i = new Intent(DetailMessageActivity.this, DashboardActivity.class);
                                        Intent intent = getIntent();
                                        String clt_id = intent.getStringExtra("clt_id");
                                        String msd_ID = intent.getStringExtra("msd_ID");
                                        String usl_id = intent.getStringExtra("usl_id");
                                        school_name = intent.getStringExtra("School_name");
                                        board_name = intent.getStringExtra("board_name");
                                        versionName = intent.getStringExtra("version_name");
                                        versionCode = intent.getIntExtra("version_code", 0);
                                        isStudentresource = intent.getStringExtra("isStudentResource");
                                        announcementCount = intent.getStringExtra("annpuncement_count");
                                        behaviourCount = intent.getStringExtra("behaviour_count");
                                        AppController.versionName = versionName;
                                        AppController.versionCode = versionCode;
                                        AppController.clt_id = clt_id;
                                        AppController.msd_ID = msd_ID;
                                        AppController.usl_id = usl_id;
                                        AppController.school_name = school_name;

                                        i.putExtra("clt_id", AppController.clt_id);
                                        i.putExtra("msd_ID", AppController.msd_ID);
                                        i.putExtra("usl_id", AppController.usl_id);
                                        i.putExtra("board_name", AppController.Board_name);
                                        i.putExtra("School_name", AppController.school_name);
                                        i.putExtra("version_name", AppController.versionName);
                                        i.putExtra("version_code", AppController.versionCode);
                                        i.putExtra("annpuncement_count", announcementCount);
                                        i.putExtra("behaviour_count", behaviourCount);
                                        i.putExtra("isStudentResource", isStudentresource);
                                        startActivity(i);
                                        finish();
                                        dialog.dismiss();
                                    }
                                });

                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            } else if (login_details.Status.equalsIgnoreCase("0")) {

                                Constant.showOkPopup(DetailMessageActivity.this, login_details.StatusMsg, new DialogInterface.OnClickListener() {

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
                        Log.d("DetailMessageActivity", "ERROR.._---" + error.getCause());
                    }
                });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppController.ListClicked = "true";
        AppController.school_name = school_name;
        AppController.Board_name = board_name;
        AppController.clt_id = clt_id;
        AppController.msd_ID = msd_ID;
        AppController.usl_id = usl_id;
        finish();

    }
}
