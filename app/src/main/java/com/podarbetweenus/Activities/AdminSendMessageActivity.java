package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import org.simpleframework.xml.Path;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Gayatri on 4/21/2016.
 */
public class AdminSendMessageActivity extends Activity implements View.OnClickListener{

    //UI Variables
    //EditText
    EditText ed_select_subject,enter_message;
    //Button
    Button btn_send_msg,btn_attachment;
    //Progress dialog
    ProgressDialog progressDialog;
    //ImageView
    ImageView img_drawer;
    //Linear Layout
    LinearLayout lay_back_investment,lL_drawer;
    //TextView
    TextView tv_version_name,tv_version_code,tv_cancel,tv_error_msg,tv_no_records_further,tv_child_name_drawer,tv_academic_year_drawer;

    //Relative Layout
    RelativeLayout leftfgf_drawer,rl_profile;
    //Drawer
    DrawerLayout drawer;
    //ListView
    ListView drawerListView;

    DataFetchService dft;
    LoginDetails login_details;
    ContentDisplaceDrawerToggle mContentDisplaceToggle;
    HeaderControler header;

    String clt_id,usl_id,board_name,school_name,admin_name,version_name,class_id,studentName="",stud_id,contact_number,SenderUsl_ID,
            dropdownSelection="false",messageContent,org_id,clas_teacher,academic_year,subject,sender_usl_id="0",attachedfilename="0",fileName="0",filePath="0",FilePath,filename,base64Value,extension,entityResponse,responseStatus;
    String[] data_without_sibling;
    long length;
    String sendMessage_Method_name = "PostMessageToStudent";
    String sendMessageToteacher_MethodName = "PostMessageAdminToTeacher";
    String UploadFileMethod_name = "UploadAdminMsgAttachment";
    int[] icons_without_sibling;
    int notificationID = 1;
    JSONObject jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.admin_send_message);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        findViews();
        init();
        getIntentData();
        setDrawer();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if(drawer.isDrawerOpen(Gravity.RIGHT)){
            ed_select_subject.setFocusable(false);
            ed_select_subject.setClickable(false);
            ed_select_subject.setCursorVisible(false);
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(ed_select_subject.getWindowToken(), 0);
        }

        try{
            StrictMode.ThreadPolicy policy1 = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy1);
            String str = android.os.Build.MODEL;
            Log.e("dev", str);
            getDeviceName();
            Log.e("NAME", getDeviceName());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setDrawer() {
            icons_without_sibling = new int[]{R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sms_48x48, R.drawable.announcementicon_48x48, /*R.drawable.att_48x48, R.drawable.behaviouricon_48x48, R.drawable.subjectlist_48x48,*/ R.drawable.setting1_hdpi, R.drawable.logout1_hdpi, R.drawable.info};
            data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement",/* "Attendance", "Behaviour", "Subject List",*/ "Setting", "Sign Out", "About"};
            drawerListView.setAdapter(new CustomAccount(AdminSendMessageActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));

            drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        if (dft.isInternetOn() == true) {
                            Intent back = new Intent(AdminSendMessageActivity.this, AdminProfileActivity.class);
                            back.putExtra("clt_id", clt_id);
                            back.putExtra("usl_id", usl_id);
                            back.putExtra("board_name", board_name);
                            back.putExtra("Admin_name", admin_name);
                            back.putExtra("School_name", school_name);
                            back.putExtra("org_id", org_id);
                            back.putExtra("academic_year", academic_year);
                            back.putExtra("version_name", AppController.versionName);
                            back.putExtra("verion_code", AppController.versionCode);
                            startActivity(back);
                        }
                    } else if (position == 1) {
                        //Message
                        if (dft.isInternetOn() == true) {
                            Intent back = new Intent(AdminSendMessageActivity.this, AdminMessageActivity.class);
                            back.putExtra("clt_id", clt_id);
                            back.putExtra("usl_id", usl_id);
                            back.putExtra("board_name", board_name);
                            back.putExtra("Admin_name", admin_name);
                            back.putExtra("School_name", school_name);
                            back.putExtra("org_id", org_id);
                            back.putExtra("academic_year", academic_year);
                            back.putExtra("version_name", AppController.versionName);
                            back.putExtra("verion_code", AppController.versionCode);
                            startActivity(back);
                        }
                    } else if (position == 2) {
                        Intent back = new Intent(AdminSendMessageActivity.this, AdminSMSActivity.class);
                        back.putExtra("clt_id", clt_id);
                        back.putExtra("usl_id", usl_id);
                        back.putExtra("board_name", board_name);
                        back.putExtra("Admin_name", admin_name);
                        back.putExtra("School_name", school_name);
                        back.putExtra("org_id", org_id);
                        back.putExtra("academic_year", academic_year);
                        back.putExtra("version_name", AppController.versionName);
                        back.putExtra("verion_code", AppController.versionCode);
                        startActivity(back);
                    } else if (position == 3) {
                        Intent back = new Intent(AdminSendMessageActivity.this, AdminAnnouncmentActivity.class);
                        back.putExtra("clt_id", clt_id);
                        back.putExtra("usl_id", usl_id);
                        back.putExtra("board_name", board_name);
                        back.putExtra("Admin_name", admin_name);
                        back.putExtra("School_name", school_name);
                        back.putExtra("org_id", org_id);
                        back.putExtra("academic_year", academic_year);
                        back.putExtra("version_name", AppController.versionName);
                        back.putExtra("verion_code", AppController.versionCode);
                        startActivity(back);
                    } else if (position == 4) {
                        //Setting
                        if (dft.isInternetOn() == true) {
                            Intent adminsettingIntent = new Intent(AdminSendMessageActivity.this, SettingActivity.class);
                            adminsettingIntent.putExtra("clt_id", clt_id);
                            adminsettingIntent.putExtra("usl_id", usl_id);
                            adminsettingIntent.putExtra("board_name", board_name);
                            adminsettingIntent.putExtra("Admin_name", admin_name);
                            adminsettingIntent.putExtra("School_name", school_name);
                            adminsettingIntent.putExtra("org_id", org_id);
                            adminsettingIntent.putExtra("academic_year", academic_year);
                            adminsettingIntent.putExtra("version_name", AppController.versionName);
                            adminsettingIntent.putExtra("verion_code", AppController.versionCode);
                            startActivity(adminsettingIntent);
                        }
                    } else if (position == 5) {
                        //Signout
                        Intent signout = new Intent(AdminSendMessageActivity.this, LoginActivity.class);
                        Constant.SetLoginData("", "", AdminSendMessageActivity.this);
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancel(notificationID);
                        AppController.iconCount = 0;
                        AppController.iconCountOnResume = 0;
                        AppController.OnBackpressed = "false";
                        // teachermsgStatusListOnResume.clear();
                        AppController.loginButtonClicked = "false";
                        AppController.drawerSignOut = "true";
                        startActivity(signout);
                    } else if (position == 6) {
                        try {
                            final Dialog alertDialog = new Dialog(AdminSendMessageActivity.this);
                            LayoutInflater inflater = getLayoutInflater();
                            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            alertDialog.getWindow().setBackgroundDrawable(
                                    new ColorDrawable(Color.TRANSPARENT));
                            View convertView = inflater.inflate(R.layout.about_us, null);
                            alertDialog.setContentView(convertView);
                            tv_version_name = (TextView) convertView.findViewById(R.id.tv_version_name);
                            tv_version_code = (TextView) convertView.findViewById(R.id.tv_version_code);
                            tv_cancel = (TextView) convertView.findViewById(R.id.tv_cancel);
                            tv_version_name.setText("Version Name: " + AppController.versionName);
                            tv_version_code.setText("Version Code: " + AppController.versionCode);
                            alertDialog.show();

                            tv_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            Constant.setAdminDrawerData(academic_year, admin_name, AdminSendMessageActivity.this);
            setDrawerData();
        }

    private void setDrawerData() {
        tv_child_name_drawer.setText(admin_name);
        tv_academic_year_drawer.setText(academic_year);
    }

    private void getIntentData() {
        try {
            Intent intent = getIntent();
            usl_id = intent.getStringExtra("usl_id");
            clt_id = intent.getStringExtra("clt_id");
            school_name = intent.getStringExtra("School_name");
            admin_name = intent.getStringExtra("Admin_name");
            version_name = intent.getStringExtra("version_name");
            board_name = intent.getStringExtra("board_name");
            class_id = intent.getStringExtra("class_id");
            stud_id = intent.getStringExtra("stud_id");
            org_id = intent.getStringExtra("org_id");
            clas_teacher = intent.getStringExtra("cls_teacher");
            academic_year = intent.getStringExtra("academic_year");
            SenderUsl_ID = intent.getStringExtra("SenderUsl_ID");
            AppController.versionName = version_name;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        btn_send_msg.setOnClickListener(this);
        btn_attachment.setOnClickListener(this);
        header = new HeaderControler(this, true, false, "Send Message");
        progressDialog = Constant.getProgressDialog(this);
        lay_back_investment.setOnClickListener(this);
        lL_drawer.setOnClickListener(this);
        login_details = new LoginDetails();
        dft = new DataFetchService(this);
        img_drawer.setOnClickListener(this);
        ed_select_subject.setOnClickListener(this);
        tv_academic_year_drawer.setVisibility(View.GONE);

    }

    private void findViews() {
        //Button
        btn_attachment = (Button) findViewById(R.id.btn_attachment);
        btn_send_msg = (Button) findViewById(R.id.btn_send_msg);
        //EditText
        ed_select_subject = (EditText) findViewById(R.id.ed_select_subject);
        enter_message  = (EditText) findViewById(R.id.enter_message);
        //Linea Layout
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
        lL_drawer = (LinearLayout) findViewById(R.id.lL_drawer);
        //TextView
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_version_code = (TextView) findViewById(R.id.tv_version_code);
        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
        tv_error_msg = (TextView) findViewById(R.id.tv_error_msg);
        tv_child_name_drawer = (TextView) findViewById(R.id.tv_child_name_drawer);
        tv_academic_year_drawer = (TextView) findViewById(R.id.tv_academic_year_drawer);
        // for drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        leftfgf_drawer = (RelativeLayout) findViewById(R.id.leftfgf_drawer);
        rl_profile = (RelativeLayout) findViewById(R.id.rl_profile);
        drawerListView = (ListView) findViewById(R.id.drawerListView);
        img_drawer = (ImageView) findViewById(R.id.img_drawer);
        drawerListView = (ListView) findViewById(R.id.drawerListView);
        mContentDisplaceToggle = new ContentDisplaceDrawerToggle(this, drawer,
                R.id.rl_profile, Gravity.START);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //int width = dm.widthPixels;
        int width = getResources().getDisplayMetrics().widthPixels/2+30;
        DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) leftfgf_drawer.getLayoutParams();
        lp.width = width + 30;
        leftfgf_drawer.setLayoutParams(lp);

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
    @Override
    public void onClick(View v) {
        try {
            if(v==btn_send_msg){
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                if(ed_select_subject.getText().toString().equalsIgnoreCase("")){
                    Constant.showOkPopup(this, "Please Enter Subject", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }

                    });
                }
               else if(enter_message.getText().toString().equalsIgnoreCase("")){
                    Constant.showOkPopup(this, "Message can not be Blank", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }

                    });
                }
                else {
                    messageContent = enter_message.getText().toString();
                    subject = ed_select_subject.getText().toString();
                    if(AppController.AdminsendMessageToStudent.equalsIgnoreCase("true")) {
                        //Call Webservice to send Message
                        callWSToSendMessage(clt_id, class_id, subject, messageContent, usl_id, stud_id, sender_usl_id, attachedfilename, filePath);
                    }
                    else{
                        //Call webservice to Send Message
                        callWSToSendMessageToTeacher(clt_id,subject, messageContent, usl_id,SenderUsl_ID, attachedfilename, filePath);
                    }
                }

            } else if(v==img_drawer){
                drawer.openDrawer(Gravity.RIGHT);
            }
            if(v==lay_back_investment){
                try {
                if(AppController.AdminsendMessageToStudent.equalsIgnoreCase("true")){
                    AppController.AdminDropResult.clear();
                }
                AppController.AdminstudentSMS = "true";
                    AppController.teacher_sms = "false";
                    finish();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            else if(v==btn_attachment){
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
            else if(v==ed_select_subject){
               if(drawer.isDrawerOpen(Gravity.RIGHT)){
                    ed_select_subject.setFocusable(false);
                    ed_select_subject.setClickable(false);
                    ed_select_subject.setCursorVisible(false);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(ed_select_subject.getWindowToken(), 0);
                }

            }
            else if(v==lL_drawer){
                ed_select_subject.setClickable(false);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(ed_select_subject.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                            Toast.makeText(AdminSendMessageActivity.this, "Not valid file", Toast.LENGTH_LONG).show();
                        } else {
                            extension = attachedfilename.substring(attachedfilename.lastIndexOf("."));
                            Log.e("extension", extension);
                            int pos = attachedfilename.lastIndexOf(".");
                            if (pos > 0) {
                                filename = attachedfilename.substring(0, pos);
                                Log.e("ONLY NAME", filename);
                            }
                        }
                        //Upload File
                        if (length > 500) {
                            Toast.makeText(AdminSendMessageActivity.this, "File size is greater than 500KB", Toast.LENGTH_LONG).show();
                        } else if (!(extension.equalsIgnoreCase(".pdf") || extension.equalsIgnoreCase(".docx") || extension.equalsIgnoreCase(".doc"))) {
                            Toast.makeText(AdminSendMessageActivity.this, "Upload only Word or PDF Document", Toast.LENGTH_LONG).show();
                        } else {
                            new TheTask().execute("http://www.betweenus.in/PODARAPP/PodarApp.svc/UploadAdminMsgAttachment");
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
            Toast.makeText(AdminSendMessageActivity.this,responseStatus,Toast.LENGTH_LONG).show();
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
                Log.e("response new",String.valueOf(response));
                HttpEntity responseEntity = response.getEntity();
                if(responseEntity!=null)
                {
                    String abc  = EntityUtils.toString(responseEntity);
                    try {
                        jsonResponse = new JSONObject(abc);
                        Log.e("json rres", String.valueOf(jsonResponse));
                        filePath = jsonResponse.getString("Filepath");
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
    private void callWSToSendMessage(final String clt_id, final String class_id,String subject,String messageContent,final String usl_id,String stud_id,String sender_usl_id,String fileName,String filePath ) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getAdminStudentSendMessage(clt_id, class_id, subject, messageContent, usl_id, stud_id, sender_usl_id, fileName, filePath, sendMessage_Method_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);

                            if (login_details.Status.equalsIgnoreCase("1")) {
                                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                enter_message.setText("");
                                Constant.showOkPopup(AdminSendMessageActivity.this, login_details.StatusMsg, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent i = new Intent(AdminSendMessageActivity.this, AdminMessageActivity.class);

                                        AppController.AdminSentMessage = "true";
                                        AppController.AdminTabPosition=0;
                                        i.putExtra("clt_id", clt_id);
                                        i.putExtra("usl_id", usl_id);
                                        i.putExtra("School_name", school_name);
                                        i.putExtra("Admin_name", admin_name);
                                        i.putExtra("version_name", version_name);
                                        i.putExtra("board_name", board_name);
                                        i.putExtra("class_id", class_id);
                                        i.putExtra("academic_year", academic_year);
                                        i.putExtra("org_id", org_id);
                                        startActivity(i);
                                        dialog.dismiss();
                                    }

                                });
                            } else {
                                Constant.showOkPopup(AdminSendMessageActivity.this, login_details.StatusMsg, new DialogInterface.OnClickListener() {

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
                        Log.d("AdminSendSMSActivity", "ERROR.._---" + error.getCause());
                    }
                });
    }

    private void callWSToSendMessageToTeacher(final String clt_id,String subject,String messageContent,final String usl_id,String sender_usl_id,String fileName,String filePath ) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getAdminTeacherSendMessage(clt_id, subject, messageContent, usl_id, sender_usl_id, fileName, filePath, sendMessageToteacher_MethodName, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);

                            if (login_details.Status.equalsIgnoreCase("1")) {
                                Log.e("Sending", "Sending");
                                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                enter_message.setText("");
                                Constant.showOkPopup(AdminSendMessageActivity.this, login_details.StatusMsg, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent i = new Intent(AdminSendMessageActivity.this, AdminMessageActivity.class);
                                        AppController.AdminSentMessage = "true";
                                        i.putExtra("clt_id", clt_id);
                                        i.putExtra("usl_id", usl_id);
                                        i.putExtra("School_name", school_name);
                                        i.putExtra("Admin_name", admin_name);
                                        i.putExtra("version_name", version_name);
                                        i.putExtra("board_name", board_name);
                                        i.putExtra("class_id", class_id);
                                        i.putExtra("academic_year", academic_year);
                                        i.putExtra("org_id", org_id);
                                        startActivity(i);
                                        dialog.dismiss();
                                    }

                                });
                            } else {
                                Constant.showOkPopup(AdminSendMessageActivity.this, "Message not sent", new DialogInterface.OnClickListener() {

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
                        Log.d("AdminSendSMSActivity", "ERROR.._---" + error.getCause());
                    }
                });
    }
    @Override
    public void onBackPressed() {
        try {
            super.onBackPressed();
            AppController.AdminDropResult.clear();
            finish();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
