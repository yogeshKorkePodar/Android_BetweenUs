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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
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
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.academic_year;

/**
 * Created by Gayatri on 3/31/2016.
 */
public class AdminSendSMSActivity extends Activity implements View.OnClickListener{
    //EditText
    EditText ed_select_module,enter_sms;
    //Text View
    TextView tv_charachter_length,tv_charachter_note;
    //ImageView
    ImageView img_drawer;
    //Linear Layout
    LinearLayout lay_back_investment;
    //Relative Layout
    RelativeLayout leftfgf_drawer,rl_profile;
    //TextView
    TextView tv_version_name,tv_version_code,tv_cancel,tv_error_msg,tv_no_records_further,tv_child_name_drawer,tv_academic_year_drawer;

    DrawerLayout drawer;
    //ListView
    ListView drawerListView;
    //Button
    Button btn_send_sms;

    Context mcontext;

    //ProgressDialog
    ProgressDialog progressDialog;
    ContentDisplaceDrawerToggle mContentDisplaceToggle;
    HeaderControler header;
    DataFetchService dft;
    LoginDetails login_details;

    String clt_id,usl_id,board_name,school_name,admin_name,version_name,class_id,studentName="",stud_id,contact_number,
            dropdownSelection="false",messageContent,org_id,clas_teacher,academic_year;
    String[] data_without_sibling;
    String sendMessage_Method_name = "StudentSMS";
    int[] icons_without_sibling;
    int notificationID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.send_sms_layout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        findViews();
        init();
        getIntentData();
        setDrawer();
        //charachter lenght
        CheckCharachterLength();
        Drawable img =  this.getResources().getDrawable( R.drawable.drop_arrow );
        ed_select_module.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        ed_select_module.setText("Dear Parent, Kindly Note");
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
        icons_without_sibling = new int[]{R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sms_48x48, R.drawable.announcementicon_48x48,/* R.drawable.att_48x48, R.drawable.behaviouricon_48x48, R.drawable.subjectlist_48x48, */R.drawable.setting1_hdpi, R.drawable.logout1_hdpi, R.drawable.info};
        data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement", /*"Attendance", "Behaviour", "Subject List",*/ "Setting", "Sign Out", "About"};
        drawerListView.setAdapter(new CustomAccount(AdminSendSMSActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));

        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (dft.isInternetOn() == true) {
                        Intent adminProfileActivity = new Intent(AdminSendSMSActivity.this, AdminProfileActivity.class);
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
                        Intent adminMessageActivity = new Intent(AdminSendSMSActivity.this, AdminProfileActivity.class);
                        adminMessageActivity.putExtra("clt_id", clt_id);
                        adminMessageActivity.putExtra("usl_id", usl_id);
                        adminMessageActivity.putExtra("board_name", board_name);
                        adminMessageActivity.putExtra("Admin_name", admin_name);
                        adminMessageActivity.putExtra("School_name", school_name);
                        adminMessageActivity.putExtra("org_id", org_id);
                        adminMessageActivity.putExtra("academic_year",academic_year);
                        adminMessageActivity.putExtra("version_name", AppController.versionName);
                        adminMessageActivity.putExtra("verion_code", AppController.versionCode);
                        startActivity(adminMessageActivity);
                    }
                } else if (position == 2) {
                    //SMS
                    if (dft.isInternetOn() == true) {
                        Intent adminSMSIntent = new Intent(AdminSendSMSActivity.this, AdminSMSActivity.class);
                        adminSMSIntent.putExtra("clt_id", clt_id);
                        adminSMSIntent.putExtra("usl_id", usl_id);
                        adminSMSIntent.putExtra("board_name", board_name);
                        adminSMSIntent.putExtra("Admin_name", admin_name);
                        adminSMSIntent.putExtra("School_name", school_name);
                        adminSMSIntent.putExtra("org_id", org_id);
                        adminSMSIntent.putExtra("academic_year",academic_year);
                        adminSMSIntent.putExtra("version_name", AppController.versionName);
                        adminSMSIntent.putExtra("verion_code", AppController.versionCode);
                        startActivity(adminSMSIntent);
                    }
                } else if (position == 3) {
                    //Announcement
                    if (dft.isInternetOn() == true) {
                        Intent adminAnnouncementIntent = new Intent(AdminSendSMSActivity.this, AdminAnnouncmentActivity.class);
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
                } else if(position == 4){
                    //Setting
                    if (dft.isInternetOn() == true) {
                        Intent adminsettingIntent = new Intent(AdminSendSMSActivity.this, SettingActivity.class);
                        adminsettingIntent.putExtra("clt_id", clt_id);
                        adminsettingIntent.putExtra("usl_id", usl_id);
                        adminsettingIntent.putExtra("board_name", board_name);
                        adminsettingIntent.putExtra("Admin_name", admin_name);
                        adminsettingIntent.putExtra("School_name", school_name);
                        adminsettingIntent.putExtra("org_id", org_id);
                        adminsettingIntent.putExtra("academic_year",academic_year);
                        adminsettingIntent.putExtra("academic_year",academic_year);
                        adminsettingIntent.putExtra("version_name", AppController.versionName);
                        adminsettingIntent.putExtra("verion_code", AppController.versionCode);
                        startActivity(adminsettingIntent);
                    }
                }
                else if (position == 5) {
                    //Signout
                    Intent signout = new Intent(AdminSendSMSActivity.this, LoginActivity.class);
                    Constant.SetLoginData("", "", AdminSendSMSActivity.this);
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
                        final Dialog alertDialog = new Dialog(AdminSendSMSActivity.this);
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
        Constant.setAdminDrawerData(academic_year, admin_name, AdminSendSMSActivity.this);
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
        class_id = intent.getStringExtra("class_id");
        stud_id = intent.getStringExtra("stud_id");
        org_id = intent.getStringExtra("org_id");
        clas_teacher = intent.getStringExtra("cls_teacher");
        academic_year = intent.getStringExtra("academic_year");
        Log.e("stud id SMS",stud_id);
        Log.e("cls id SMS", class_id);
        AppController.versionName = version_name;
    }

    private void init() {
        header = new HeaderControler(this, true, false, "Send SMS");
        progressDialog = Constant.getProgressDialog(this);
        lay_back_investment.setOnClickListener(this);
        login_details = new LoginDetails();
        dft = new DataFetchService(this);
        ed_select_module.setOnClickListener(this);
        btn_send_sms.setOnClickListener(this);
        img_drawer.setOnClickListener(this);
        tv_academic_year_drawer.setVisibility(View.GONE);
        tv_charachter_note.setText("Note: Do not Use (&,+,%,=,#,' )");

    }

    private void findViews() {
        //Edit Text
        ed_select_module = (EditText) findViewById(R.id.ed_select_module);
        enter_sms = (EditText) findViewById(R.id.enter_sms);
        //Button
        btn_send_sms = (Button) findViewById(R.id.btn_send_sms);
        //TExtView
        tv_charachter_length = (TextView) findViewById(R.id.tv_charachter_length);
        //Linea Layout
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
        //TextView
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_version_code = (TextView) findViewById(R.id.tv_version_code);
        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
        tv_error_msg = (TextView) findViewById(R.id.tv_error_msg);
        tv_child_name_drawer = (TextView) findViewById(R.id.tv_child_name_drawer);
        tv_academic_year_drawer = (TextView) findViewById(R.id.tv_academic_year_drawer);
        tv_charachter_note = (TextView) findViewById(R.id.tv_charachter_note);
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


    @Override
    public void onClick(View v) {
        if(v==lay_back_investment){
            try {
              //  super.onBackPressed();
                AppController.AdminDropResult.clear();
               // AppController.AdminstudentSMS = "true";
                AppController.teacher_sms = "false";
                finish();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(v==btn_send_sms){

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            if(enter_sms.getText().toString().equalsIgnoreCase("")){
                Constant.showOkPopup(this, "SMS can not be Blank", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }

                });
            }
            else {
                messageContent = "Dear Parent, Kindly Note "+ enter_sms.getText().toString();
                Pattern p = Pattern.compile("[&,+,%,=,#,']");
                Matcher m = p.matcher(enter_sms.getText().toString());
                // boolean b = m.matches();
                boolean b = m.find();
                if (b == true) {
                    Log.e("Spl char", "There is a special character in my string ");
                    Toast.makeText(AdminSendSMSActivity.this, "There is a special character in SMS", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("No Spl char", "There is a no special character in my string ");
                    //Call Webservice to send SMS
                    callWSToSendSMS(clt_id, class_id, stud_id, messageContent, usl_id);
                }

            }
        }
        else if(v==img_drawer){
            drawer.openDrawer(Gravity.RIGHT);
        }
    }
    private void callWSToSendSMS(final String clt_id, final String class_id,String stud_id,String messageContent, final String usl_id) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getAdminStudentSendSMS(clt_id, class_id, stud_id, messageContent, usl_id, sendMessage_Method_name, Request.Method.POST,
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
                                enter_sms.setText("");
                                Constant.showOkPopup(AdminSendSMSActivity.this, login_details.StatusMsg, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent i = new Intent(AdminSendSMSActivity.this, AdminSMSActivity.class);
                                       // AppController.AdminstudentSMS = "true";
                                        AppController.teacher_sms = "false";
                                        i.putExtra("clt_id", clt_id);
                                        i.putExtra("usl_id", usl_id);
                                        i.putExtra("School_name", school_name);
                                        i.putExtra("Admin_name", admin_name);
                                        i.putExtra("version_name", version_name);
                                        i.putExtra("board_name", board_name);
                                        i.putExtra("class_id", class_id);
                                        i.putExtra("academic_year", academic_year);
                                        i.putExtra("org_id", org_id);
                                      //  i.putExtra("tab_student",AppController.AdminstudentSMS);
                                        i.putExtra("tab_teacher",AppController.teacher_sms);
                                        startActivity(i);
                                        dialog.dismiss();
                                    }

                                });
                            } else {
                                Constant.showOkPopup(AdminSendSMSActivity.this, "SMS not sent", new DialogInterface.OnClickListener() {

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
          //  AppController.AdminstudentSMS = "true";
            AppController.teacher_sms = "false";
            finish();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
