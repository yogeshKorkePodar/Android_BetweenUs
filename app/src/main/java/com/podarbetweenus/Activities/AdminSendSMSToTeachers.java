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
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import org.json.JSONObject;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Gayatri on 3/18/2016.
 */
public class AdminSendSMSToTeachers extends Activity implements View.OnClickListener{
    //UI Variable
    //Button
    Button btn_attachment,btn_send_sms;
    //EditText
    EditText ed_select_subject,enter_sms;
    //TExtView
    TextView tv_charachter_length,tv_child_name_drawer,tv_academic_year_drawer,tv_version_name,tv_version_code,tv_cancel,tv_charachter_note;
    //ProgressDialog
    ProgressDialog progressDialog;
    //ImageView
    ImageView img_drawer;
    //Linear Layout
    LinearLayout lay_back_investment;
    //DrawerLayout
    DrawerLayout drawer;
    //ListView
    ListView drawerListView;
    //RelativeLayout
    RelativeLayout leftfgf_drawer,rl_profile;

    HeaderControler header;
    DataFetchService dft;
    LoginDetails login_details;
    ContentDisplaceDrawerToggle mContentDisplaceToggle;

    String usl_id,clt_id,board_name,school_name,version_name,admin_name,org_id,contact_number,chkAll,subject,sms_content,academic_year;
    String sendSMStMethodName = "SendSmsAdminToTeacher";

    String[] data_without_sibling;
    int[] icons_without_sibling;
    int notificationID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.send_sms_admin_layout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        findViews();
        init();
        getIntentData();
        setDrawer();
        //charachter lenght
        CheckCharachterLength();
    }

    private void CheckCharachterLength() {
        enter_sms.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int a, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Check_SMS_Length(enter_sms);
            }
        });
    }

    public void Check_SMS_Length(EditText ed_description) throws NumberFormatException
    {
        try
        {
            if (ed_description.getText().toString().length() <= 0)
            {
                tv_charachter_length.setText("0/132");
            }
            else
            {
                int valid_len = ed_description.getText().toString().length();
                tv_charachter_length.setText(String.valueOf(valid_len) + "/" + 132);
            }
        }
        catch (Exception e)
        {
            Log.e("error", "" + e);
        }
    }
    private void setDrawer() {
        icons_without_sibling = new int[]{R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sms_48x48, R.drawable.announcementicon_48x48, /*R.drawable.att_48x48, R.drawable.behaviouricon_48x48, R.drawable.subjectlist_48x48,*/ R.drawable.setting1_hdpi, R.drawable.logout1_hdpi, R.drawable.info};
        data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement",/* "Attendance", "Behaviour", "Subject List",*/ "Setting", "Sign Out", "About"};
        drawerListView.setAdapter(new CustomAccount(AdminSendSMSToTeachers.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));

        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (dft.isInternetOn() == true) {
                        Intent adminProfileActivity = new Intent(AdminSendSMSToTeachers.this, AdminProfileActivity.class);
                        adminProfileActivity.putExtra("clt_id", clt_id);
                        adminProfileActivity.putExtra("usl_id", usl_id);
                        adminProfileActivity.putExtra("board_name", board_name);
                        adminProfileActivity.putExtra("Admin_name", admin_name);
                        adminProfileActivity.putExtra("School_name", school_name);
                        adminProfileActivity.putExtra("org_id", org_id);
                        adminProfileActivity.putExtra("academic_year",academic_year);
                        adminProfileActivity.putExtra("version_name", AppController.versionName);
                        adminProfileActivity.putExtra("verion_code", AppController.versionCode);
                        startActivity(adminProfileActivity);
                    }
                } else if (position == 1) {
                    //Message
                    if (dft.isInternetOn() == true) {
                        Intent adminMessageIntent = new Intent(AdminSendSMSToTeachers.this, AdminMessageActivity.class);
                        adminMessageIntent.putExtra("clt_id", clt_id);
                        adminMessageIntent.putExtra("usl_id", usl_id);
                        adminMessageIntent.putExtra("board_name", board_name);
                        adminMessageIntent.putExtra("Admin_name", admin_name);
                        adminMessageIntent.putExtra("School_name", school_name);
                        adminMessageIntent.putExtra("org_id", org_id);
                        adminMessageIntent.putExtra("academic_year",academic_year);
                        adminMessageIntent.putExtra("version_name", AppController.versionName);
                        adminMessageIntent.putExtra("verion_code", AppController.versionCode);
                        startActivity(adminMessageIntent);
                    }
                } else if (position == 2) {
                    //SMS
                    if (dft.isInternetOn() == true) {
                        Intent adminSMSIntent = new Intent(AdminSendSMSToTeachers.this, AdminSMSActivity.class);
                        adminSMSIntent.putExtra("clt_id", clt_id);
                        adminSMSIntent.putExtra("usl_id", usl_id);
                        adminSMSIntent.putExtra("board_name", board_name);
                        adminSMSIntent.putExtra("Admin_name", admin_name);
                        adminSMSIntent.putExtra("School_name", school_name);
                        adminSMSIntent.putExtra("academic_year",academic_year);
                        adminSMSIntent.putExtra("org_id", org_id);
                        adminSMSIntent.putExtra("version_name", AppController.versionName);
                        adminSMSIntent.putExtra("verion_code", AppController.versionCode);
                        startActivity(adminSMSIntent);
                    }
                } else if (position == 3) {
                    //Announcement
                    if (dft.isInternetOn() == true) {
                        Intent adminAnnouncementIntent = new Intent(AdminSendSMSToTeachers.this, AdminAnnouncmentActivity.class);
                        adminAnnouncementIntent.putExtra("clt_id", clt_id);
                        adminAnnouncementIntent.putExtra("usl_id", usl_id);
                        adminAnnouncementIntent.putExtra("board_name", board_name);
                        adminAnnouncementIntent.putExtra("Admin_name", admin_name);
                        adminAnnouncementIntent.putExtra("School_name", school_name);
                        adminAnnouncementIntent.putExtra("org_id", org_id);
                        adminAnnouncementIntent.putExtra("academic_year",academic_year);
                        adminAnnouncementIntent.putExtra("version_name", AppController.versionName);
                        adminAnnouncementIntent.putExtra("verion_code", AppController.versionCode);
                        startActivity(adminAnnouncementIntent);
                    }
                } else if (position == 4) {
                    //Setting
                    if (dft.isInternetOn() == true) {
                        Intent adminsettingIntent = new Intent(AdminSendSMSToTeachers.this, SettingActivity.class);
                        adminsettingIntent.putExtra("clt_id", clt_id);
                        adminsettingIntent.putExtra("usl_id", usl_id);
                        adminsettingIntent.putExtra("board_name", board_name);
                        adminsettingIntent.putExtra("Admin_name", admin_name);
                        adminsettingIntent.putExtra("School_name", school_name);
                        adminsettingIntent.putExtra("org_id", org_id);
                        adminsettingIntent.putExtra("academic_year",academic_year);
                        adminsettingIntent.putExtra("version_name", AppController.versionName);
                        adminsettingIntent.putExtra("verion_code", AppController.versionCode);
                        startActivity(adminsettingIntent);
                    }
                } else if (position == 5) {
                    //Signout
                    Intent signout = new Intent(AdminSendSMSToTeachers.this, LoginActivity.class);
                    Constant.SetLoginData("", "", AdminSendSMSToTeachers.this);
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(notificationID);
                    AppController.iconCount = 0;
                    AppController.iconCountOnResume = 0;
                    AppController.OnBackpressed = "false";
                    // teachermsgStatusListOnResume.clear();
                    AppController.loginButtonClicked = "false";
                    AppController.drawerSignOut = "true";
                    startActivity(signout);
                }

                else if (position == 6) {
                    try {
                        final Dialog alertDialog = new Dialog(AdminSendSMSToTeachers.this);
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
        Constant.setAdminDrawerData(academic_year, admin_name, AdminSendSMSToTeachers.this);
        setDrawerData();
    }

    private void setDrawerData() {
        tv_child_name_drawer.setText(admin_name);
        tv_academic_year_drawer.setText(academic_year);
    }
    private void getIntentData() {
        Intent intent = getIntent();
        usl_id = intent.getStringExtra("usl_id");
        clt_id = intent.getStringExtra("clt_id");
        school_name = intent.getStringExtra("School_name");
        admin_name = intent.getStringExtra("Admin_name");
        version_name = intent.getStringExtra("version_name");
        board_name = intent.getStringExtra("board_name");
        org_id = intent.getStringExtra("org_id");
        academic_year = intent.getStringExtra("academic_year");
        chkAll = intent.getStringExtra("chkAll");
        contact_number = intent.getStringExtra("Contact_Number");
        Log.e("contact No SMS", contact_number);
        Log.e("chkAll", chkAll);
        AppController.versionName = version_name;

    }

    private void init() {

        header = new HeaderControler(this, true, false, "Send SMS");
        btn_send_sms.setOnClickListener(this);
        btn_attachment.setOnClickListener(this);
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        progressDialog = Constant.getProgressDialog(this);
        img_drawer.setOnClickListener(this);
        lay_back_investment.setOnClickListener(this);
        tv_academic_year_drawer.setVisibility(View.GONE);
        tv_charachter_note.setText("Note: Do not Use (&,+,%,=,#,' )");

    }

    private void findViews() {
        //TExtView
        tv_charachter_length = (TextView) findViewById(R.id.tv_charachter_length);
        tv_child_name_drawer = (TextView) findViewById(R.id.tv_child_name_drawer);
        tv_academic_year_drawer = (TextView) findViewById(R.id.tv_academic_year_drawer);
        tv_charachter_note = (TextView) findViewById(R.id.tv_charachter_note);
        //Linear Layout
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
        //ImageView
        img_drawer = (ImageView) findViewById(R.id.img_drawer);
        //EditTExt
        ed_select_subject = (EditText) findViewById(R.id.ed_select_subject);
        enter_sms = (EditText) findViewById(R.id.enter_sms);
        //Button
        btn_attachment = (Button) findViewById(R.id.btn_attachment);
        btn_send_sms = (Button) findViewById(R.id.btn_send_sms);
        //Relative Layout
        // for drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        leftfgf_drawer = (RelativeLayout) findViewById(R.id.leftfgf_drawer);
        rl_profile = (RelativeLayout) findViewById(R.id.rl_profile);
        drawerListView = (ListView) findViewById(R.id.drawerListView);
        mContentDisplaceToggle = new ContentDisplaceDrawerToggle(this, drawer,
                R.id.rl_profile, Gravity.START);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //int width = dm.widthPixels;
        int width = getResources().getDisplayMetrics().widthPixels/2 +30;
        DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) leftfgf_drawer.getLayoutParams();
        lp.width = width + 30;
        leftfgf_drawer.setLayoutParams(lp);
    }

    @Override
    public void onClick(View v) {
        if(v==btn_send_sms){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(btn_send_sms.getWindowToken(), 0);
            if(ed_select_subject.getText().toString().equalsIgnoreCase("") && enter_sms.getText().toString().equalsIgnoreCase("")){
                Constant.showOkPopup(this, "Please Enter All The Fields", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }

                });
            }
           else if(ed_select_subject.getText().toString().equalsIgnoreCase("")){
                Constant.showOkPopup(this, "Please Enter subject", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }

                });
            }
            else if(enter_sms.getText().toString().equalsIgnoreCase("")){
                Constant.showOkPopup(this, "Please Enter SMS", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }

                });
            }
            else{
                subject = ed_select_subject.getText().toString();
                sms_content = enter_sms.getText().toString();
                Pattern p = Pattern.compile("[&,+,%,=,#,']");
                Matcher m = p.matcher(sms_content);
                // boolean b = m.matches();
                boolean b = m.find();
                if (b == true) {
                    Log.e("Spl char", "There is a special character in my string ");
                    Toast.makeText(AdminSendSMSToTeachers.this, "There is a special character in SMS", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("No Spl char", "There is a no special character in my string ");
                    //call Ws for send sms
                    callWsForSMS(usl_id, clt_id, sms_content,contact_number,board_name,chkAll);
                }
            }
        }
        else if(v==img_drawer){
            drawer.openDrawer(Gravity.RIGHT);
        }
        else if(v==lay_back_investment){
           /* Intent back = new Intent(AdminSendSMSToTeachers.this, AdminSMSActivity.class);
            back.putExtra("clt_id", clt_id);
            back.putExtra("usl_id", usl_id);
            back.putExtra("School_name", school_name);
            back.putExtra("Admin_name", admin_name);
            back.putExtra("version_name", version_name);
            back.putExtra("board_name", board_name);
            back.putExtra("org_id", org_id);
            startActivity(back);*/
            finish();
        }
        else if(v==btn_attachment)
        {
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
                    String FilePath = data.getData().getPath();
                    //FilePath is your file as a string
                    Log.e("FilePath", FilePath);
                    Uri.fromFile(new File(FilePath));
                    Log.e("URI", String.valueOf(Uri.fromFile(new File(FilePath))));


                }
        }
    }
    private void callWsForSMS(final String usl_id, final String clt_id,String sms_content,String contact_number, final String board_name,String chkAll) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getAdminSendSMS(usl_id, clt_id, sms_content, contact_number, board_name, chkAll, sendSMStMethodName, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            if (login_details.Status.equalsIgnoreCase("1")) {
                                Constant.showOkPopup(AdminSendSMSToTeachers.this, login_details.StatusMsg, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent i = new Intent(AdminSendSMSToTeachers.this, AdminSMSActivity.class);
                                        AppController.teacher_sms = "true";
                                        i.putExtra("clt_id", clt_id);
                                        i.putExtra("usl_id", usl_id);
                                        i.putExtra("School_name", school_name);
                                        i.putExtra("Admin_name", admin_name);
                                        i.putExtra("version_name", version_name);
                                        i.putExtra("board_name", board_name);
                                        i.putExtra("org_id", org_id);
                                        i.putExtra("academic_year",academic_year);
                                        startActivity(i);
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
                        Log.d("TecaherAdminSMSActivity", "ERROR.._---" + error.getCause());
                    }
                });
    }
}

