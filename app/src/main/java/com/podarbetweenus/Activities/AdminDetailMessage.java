package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Gayatri on 4/29/2016.
 */
public class AdminDetailMessage extends Activity implements View.OnClickListener{
    //Linear Layout
    LinearLayout ll_send_message_layout,lay_back_investment;
    //Progress Dialog
    ProgressDialog progressDialog;
    //TextView
    TextView tv_message_subject,tv_date,tv_detail_msg;
    //Relative Layout
    RelativeLayout rl_header;
    //ImageView
    ImageView img_send,img_attach,img_attachment;
    //EditText
    EditText ed_enter_msg;

    DataFetchService dft;
    LoginDetails login_details;
    HeaderControler header;

    String detail_msg,sender_name,clt_id,usl_id,msd_id,school_name,teacher_name,version_name,board_name,date,subject,message,academic_year,
            reply_msg,msgSubject,stud_id,toUslId,attachedfilename="0",filename = "0",filepath = "0",pmuId,org_id,teacher_div,teacher_std,teacher_shift,admin_name,
            FilePath,extension,attachement_path,attachment_name,attachment,responseStatus,encodedUrl,downloadfilename,newFile;
    String serverAddress = "betweenus.in/Uploads/Messages";
    long length;
    JSONObject jsonResponse;
    String ReplyMessageMethodName = "ReplyToStudentMessage";
    String UpdateMessageStatusMethodName = "UpdateMessageReadStatus";

    protected void onCreate(Bundle savedInstanceState) {
        Log.d("<<Inside receiver","AdminDetailMessage");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.teacher_details_message);

        findViews();
        getIntentData();
        init();
        setUIData();

        if(AppController.SendMessageLayout.equalsIgnoreCase("true")){
            ll_send_message_layout.setVisibility(View.VISIBLE);
        }
        else {
            ll_send_message_layout.setVisibility(View.GONE);
        }
        setUIData();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        try {
            //Webservice Call for UpdateMessage
            callWebserviceUpdateMessageStatus(pmuId, usl_id, clt_id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setUIData() {
        tv_date.setText(date);
        tv_message_subject.setText(subject);
        tv_detail_msg.setText(detail_msg);
        if(attachement_path.equalsIgnoreCase("0")|| attachement_path.equalsIgnoreCase(""))
        {
            img_attachment.setVisibility(View.GONE);
        }
        else {
            img_attachment.setVisibility(View.VISIBLE);
        }

    }

    private void findViews() {
        ll_send_message_layout = (LinearLayout) findViewById(R.id.ll_send_message_layout);
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
        //TextView
        tv_detail_msg = (TextView) findViewById(R.id.tv_detail_msg);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_message_subject = (TextView) findViewById(R.id.tv_message_subject);
        //Relative Layout
        rl_header = (RelativeLayout) findViewById(R.id.rl_header);
        //EditText
        ed_enter_msg = (EditText) findViewById(R.id.ed_enter_msg);
        //ImageView
        img_send = (ImageView) findViewById(R.id.img_send);
        img_attach = (ImageView) findViewById(R.id.img_attach);
        img_attachment = (ImageView) findViewById(R.id.img_attachment);
    }
    private void init() {
        header = new HeaderControler(this,true,false,sender_name);
        lay_back_investment.setOnClickListener(this);
        progressDialog = Constant.getProgressDialog(this);
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        img_send.setOnClickListener(this);
        img_attach.setOnClickListener(this);
        img_attachment.setOnClickListener(this);
        rl_header.setBackgroundColor(Color.parseColor("#00829C"));
        lay_back_investment.setBackgroundColor(Color.parseColor("#00829C"));
    }
    private void getIntentData() {
        try {
            Intent intent = getIntent();
            usl_id = intent.getStringExtra("usl_id");
            clt_id = intent.getStringExtra("clt_id");
            school_name = intent.getStringExtra("School_name");
            teacher_name = intent.getStringExtra("Teacher_name");
            version_name = intent.getStringExtra("version_name");
            board_name = intent.getStringExtra("board_name");
            sender_name = intent.getStringExtra("Sender Name");
            academic_year = intent.getStringExtra("academic_year");
            detail_msg = intent.getStringExtra("Detail Message");
            admin_name = intent.getStringExtra("Admin_name");
            attachement_path = intent.getStringExtra("Attachment Path");
            attachment_name = intent.getStringExtra("Attachment Name");
            attachment = attachement_path.replace("C:\\inetpub\\", "http:\\");
            date = intent.getStringExtra("Date");
            subject = intent.getStringExtra("Subject");
            toUslId = intent.getStringExtra("toUslId");
            stud_id = intent.getStringExtra("stud_id");
            org_id = intent.getStringExtra("org_id");
            pmuId = intent.getStringExtra("Pmu_Id");
            AppController.versionName = version_name;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        try {
            if (v == lay_back_investment) {
                finish();
            } else if (v == img_send) {
                InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);

                sendMessage();
                ed_enter_msg.setText("");
            }
            else if(v==img_attach){
                if (getDeviceName().contains("Samsung")) {
                    Intent intent1 = new Intent("com.sec.android.app.myfiles.PICK_DATA");
                    intent1.putExtra("CONTENT_TYPE", "*/*");
                    intent1.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivityForResult(intent1, 1);
                } else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    Intent i = Intent.createChooser(intent, "View Default File Manager");
                    startActivityForResult(i, 1);
                }
            }
            else if(v==img_attachment) {
                //download file
                try {
                    String host = "http://" + serverAddress + "/";
                    downloadfilename = attachment.substring(attachment.lastIndexOf('/') + 1);
                    Log.e("filename", downloadfilename);
                    newFile = host+downloadfilename;
                    Log.e("NEWFILE",newFile);
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

            }
        catch (Exception e){
            e.printStackTrace();
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
            Toast.makeText(AdminDetailMessage.this,"Downloading Completed",Toast.LENGTH_SHORT).show();
        }

    }
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Fix no activity available
        if (data == null)
            return;
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    try {
                        FilePath = data.getData().getPath();
                        //FilePath is your file as a string
                        Log.e("FilePath", FilePath);
                        File file = new File(FilePath);
                        attachedfilename = file.getName();
                        Log.e("NAME", attachedfilename);
                        length = file.length();
                        length = length / 1024;
                        Log.e(" File size : ", length + " KB");
                        if (!attachedfilename.contains(".")) {
                            Toast.makeText(AdminDetailMessage.this, "Not valid file", Toast.LENGTH_LONG).show();
                        } else {
                            extension = attachedfilename.substring(attachedfilename.lastIndexOf("."));
                            Log.e("extension", extension);
                            int pos = attachedfilename.lastIndexOf(".");
                            if (pos > 0) {
                                filename = attachedfilename.substring(0, pos);
                                Log.e("ONLY NAME", filename);
                            }
                            //Upload File
                            if (length > 500) {
                                Toast.makeText(AdminDetailMessage.this, "File size is greater than 500KB", Toast.LENGTH_LONG).show();
                            } else if (!(extension.equalsIgnoreCase(".pdf") || extension.equalsIgnoreCase(".docx") || extension.equalsIgnoreCase(".doc"))) {
                                Toast.makeText(AdminDetailMessage.this, "Upload only Word or PDF Document", Toast.LENGTH_LONG).show();
                            } else {
                                new TheTask().execute("http://www.betweenus.in/PODARAPP/PodarApp.svc/UploadAdminMsgAttachment");
                            }
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
        }

    }

    class TheTask extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDialog.dismiss();
           /* Constant.showOkPopup(AdminDetailMessage.this, responseStatus, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }

            });*/
            Toast.makeText(AdminDetailMessage.this,responseStatus,Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            HttpParams param = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(param, 15000);
            HttpConnectionParams.setSoTimeout(param, 20000);
            HttpClient httpclient = new DefaultHttpClient(param);
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpParams httpParameters = httpclient.getParams();
            HttpConnectionParams.setTcpNoDelay(httpParameters, true);
            httpclient.getParams().setParameter("\"http.socket.timeout\"", new Integer(90000));
            // HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://www.betweenus.in/PODARAPP/PodarApp.svc/UploadAdminMsgAttachment");
            // Add Headers
            httppost.addHeader("Filename", filename);
            httppost.addHeader("usl_id", usl_id);
            httppost.addHeader("clt_id", clt_id);
            httppost.addHeader("extension", extension);
            try {
                FileEntity entity = new FileEntity(new File(FilePath), "application/octet-stream");
                entity.setChunked(true);
                httppost.setEntity(entity);
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity responseEntity = response.getEntity();
                if(responseEntity!=null)
                {
                    String abc  = EntityUtils.toString(responseEntity);
                    try {
                        jsonResponse = new JSONObject(abc);
                        Log.e("json rres", String.valueOf(jsonResponse));
                        filepath = jsonResponse.getString("Filepath");
                        responseStatus = jsonResponse.getString("StatusMsg");
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
            return "network";
        }

    }
    private void callWebserviceUpdateMessageStatus(String pmuId, String usl_id, String clt_id) {
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
                        Log.d("DetailMessageActivity", "ERROR.._---" + error.getCause());
                    }
                });
    }
    private void sendMessage() {
        message = ed_enter_msg.getText().toString();
        reply_msg = message+"\n\n"+"--Old Message--"+date+"\n"+detail_msg;
        if(subject.contains("Re: ")){
            msgSubject = subject;
        }
        else {
            msgSubject = "Re: "+subject;
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
            try {
                callReplyMessageswebservcie(clt_id, msgSubject, reply_msg, usl_id, stud_id, toUslId, attachedfilename, filepath);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void callReplyMessageswebservcie(String clt_id,String message_subject,String reply_msg,String usl_id,String stud_id,String toUslId,String filename,String filepath) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getTecherReplyMessage(clt_id, message_subject, reply_msg, usl_id, stud_id, toUslId, filename, filepath, ReplyMessageMethodName, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_details.Status.equalsIgnoreCase("1")) {
                                Constant.showOkPopup(AdminDetailMessage.this, login_details.StatusMsg, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent i = new Intent(AdminDetailMessage.this, AdminMessageActivity.class);
                                        AppController.AdminSentMessage = "true";
                                        Intent intent = getIntent();
                                        String clt_id = intent.getStringExtra("clt_id");
                                        String msd_ID = intent.getStringExtra("msd_ID");
                                        String usl_id = intent.getStringExtra("usl_id");
                                        school_name = intent.getStringExtra("School_name");
                                        board_name = intent.getStringExtra("board_name");
                                        version_name = intent.getStringExtra("version_name");

                                        i.putExtra("clt_id", clt_id);
                                        i.putExtra("msd_ID", msd_ID);
                                        i.putExtra("usl_id",usl_id);
                                        i.putExtra("School_name", school_name);
                                        i.putExtra("Admin_name",admin_name);
                                        i.putExtra("version_name", AppController.versionName);
                                        i.putExtra("academic_year",academic_year);
                                        i.putExtra("org_id",org_id);
                                        startActivity(i);
                                        finish();
                                        dialog.dismiss();
                                    }
                                });

                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            } else if (login_details.Status.equalsIgnoreCase("0")) {

                                Constant.showOkPopup(AdminDetailMessage.this, login_details.StatusMsg, new DialogInterface.OnClickListener() {

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
}
