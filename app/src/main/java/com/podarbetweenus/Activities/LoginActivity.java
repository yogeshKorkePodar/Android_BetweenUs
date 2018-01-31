package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.BuildConfig;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.Entity.userListDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Services.ListResultReceiver;
import com.podarbetweenus.Utility.AppController;
import com.podarbetweenus.Utility.BadgeView;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 9/22/2015.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    //UI Variables
    //EditText
    //Yogesh
    EditText ed_username, ed_password;
    //Button
    Button btn_login;
    //Checkbox
    CheckBox checkbox,show_passowrd_checkbox;
    //TextView
    TextView tv_forgot_pass, tv_header, tv_error_msg;
    //Linear Layout
    LinearLayout lay_back_investment;
    //ImageView
    ImageView img_username_close;
    //ProgressDialog
    ProgressDialog progressDialog;
    //LayoutEntities
    HeaderControler header;
    DataFetchService dft;
    LoginDetails login_details;
    userListDetails userlistDetails;
    AlarmManager alarmManager;
    private ListResultReceiver mReceiver;

    String username = "", password = "", board_name, school_name, location_name, roll_no, clt_id, usl_id, msd_ID, roll_id="6",
            versionName, studentResource,installedversionName,emailID,teacher_name,org_id,name,clss_teacher,academic_year,teacher_shift,teacher_std,teacher_div,TeachAnoucementCnt;
    final String emailPattern= "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    String LoginMethod_name = "DoLogin";
    public static String loggedIn = "";
    String badge_count;
    Context context;
    private Activity mcontext;
    BadgeView badge;
    int listSize,start,end;
    int versionCode;
    public static SharedPreferences resultpreLoginData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        findViews();
        init();
        AppController.ListClicked = "false";
        AppController.SiblingActivity = "false";
        AppController.drawerSignOut = "false";
        AppController.sentMsgsTab = "false";
        AppController.AdminSentMessage = "false";
        AppController.AdminTabPosition = 0;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //find the home launcher Package
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        String currentHomePackage = resolveInfo.activityInfo.packageName;

        versionCode = BuildConfig.VERSION_CODE;
        versionName = BuildConfig.VERSION_NAME;
        AppController.versionName = versionName;
        AppController.versionCode = versionCode;
        InstalledApps();
        //versionUpdate
        if(Constant.version_update.equalsIgnoreCase("true")) {
            versionUpdate();
            Constant.version_update="false";
        }

        ed_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                img_username_close.setVisibility(View.VISIBLE);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ed_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                img_username_close.setVisibility(View.GONE);
            }
        });
    }
    public  void InstalledApps()
    {
        List<PackageInfo> PackList =  getPackageManager().getInstalledPackages(0);
        for (int i=0; i < PackList.size(); i++)
        {
            PackageInfo PackInfo = PackList.get(i);
            if (  (PackInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            {
                String AppName = PackInfo.applicationInfo.loadLabel(getPackageManager()).toString();
              //  Log.e("App № " + Integer.toString(i), AppName);
                List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
                PackageInfo p = packages.get(i);
                String packageName = "com.podarbetweenus";
                if (p.packageName.contains(packageName)){
                    installedversionName = p.versionName;
                }
            }
        }
    }
    private void versionUpdate() {
        if(!AppController.versionName.equalsIgnoreCase(installedversionName))
        {
            try {
                final Dialog alertDialog = new Dialog(this);
                //  final AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                LayoutInflater inflater = this.getLayoutInflater();
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(Color.TRANSPARENT));
                View convertView =  inflater.inflate(R.layout.version_update, null);
                alertDialog.setContentView(convertView);
                TextView tv_later,tv_update;
                tv_update = (TextView) convertView.findViewById(R.id.tv_update);
                alertDialog.show();

                tv_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                           startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            alertDialog.dismiss();
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void init() {
        btn_login.setOnClickListener(LoginActivity.this);
        tv_forgot_pass.setOnClickListener(this);
        img_username_close.setOnClickListener(this);
        show_passowrd_checkbox.setOnClickListener(this);
        header = new HeaderControler(this, false, false, "BetweenUs Login");
        progressDialog = Constant.getProgressDialog(this);
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        userlistDetails = new userListDetails();

        if (AppController.drawerSignOut.equalsIgnoreCase("true")) {
            LoginActivity.loggedIn = "false";
            ed_username.setText("");
            ed_password.setText("");
        }
        //Get Login Data
        if (!Constant.GetLoginusername(this).toString().equalsIgnoreCase("")) {
            ed_username.setText(Constant.GetLoginusername(this));
            ed_password.setText(Constant.GetLoginPassword(this));
        } else {
            ed_username.setText("");
            ed_password.setText("");
        }
    }
    private void findViews() {
        //Button
        btn_login = (Button) findViewById(R.id.btn_login);
        //EditText
        ed_username = (EditText) findViewById(R.id.ed_username);
        ed_password = (EditText) findViewById(R.id.ed_password);
        //Imageview
        img_username_close = (ImageView) findViewById(R.id.img_username_close);
        //TextView
        tv_forgot_pass = (TextView) findViewById(R.id.tv_forgot_pass);
        tv_header = (TextView) findViewById(R.id.tv_title);
        tv_error_msg = (TextView) findViewById(R.id.tv_error_msg);
        //Checkbox
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        show_passowrd_checkbox = (CheckBox) findViewById(R.id.show_passowrd_checkbox);
        //Linear Layout
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_login) {
            try {
                AppController.loginButtonClicked = "true";
                AppController.announcementActivity = "false";
                //loggedIn ="true";
                ed_username.setError(null);
                Constant.SetLoginData(ed_username.getText().toString(), ed_password.getText().toString(), this);
                if (roll_id.equalsIgnoreCase("6")) {
                    tv_error_msg.setVisibility(View.GONE);
                    dologin();
                } else {
                    tv_error_msg.setVisibility(View.VISIBLE);
                    tv_error_msg.setText("User does not exist");
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

        } else if (v == tv_forgot_pass) {
            Intent forgot_password = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(forgot_password);
        }
        else if(v==img_username_close){
            ed_username.setText("");
        }else if(v==show_passowrd_checkbox){
            if(show_passowrd_checkbox.isChecked()==true){
                start=ed_password.getSelectionStart();
                end=ed_password.getSelectionEnd();
                ed_password.setTransformationMethod(null);
                ed_password.setSelection(start,end);
            }
            else{
                start=ed_password.getSelectionStart();
                end=ed_password.getSelectionEnd();
                ed_password.setTransformationMethod(new PasswordTransformationMethod());
                ed_password.setSelection(start,end);
            }
        }
    }

    private void dologin() {

        if (ed_username.getText().toString().trim().equalsIgnoreCase("") && ed_password.getText().toString().trim().equalsIgnoreCase("")) {
            tv_error_msg.setVisibility(View.VISIBLE);
            tv_error_msg.setText("Please Enter Credentials");
        } else if (ed_username.getText().toString().equalsIgnoreCase("")) {
            tv_error_msg.setVisibility(View.VISIBLE);
            tv_error_msg.setText("Please Enter UserName");

        } else if (ed_password.getText().toString().equalsIgnoreCase("")) {
            tv_error_msg.setVisibility(View.VISIBLE);
            tv_error_msg.setText("Please Enter Password");
        } else {
            username = ed_username.getText().toString();
            password = ed_password.getText().toString();
            try {
                //Webservice call for login
                callWebserviceLogin(username, password);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private void callWebserviceLogin(final String username, final String password) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.doLogin(username, password, LoginMethod_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            Log.d("<<LoginActivityResponse", response.toString());
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            Log.d("<<Status",login_details.Status);
                           /* if(login_details.Status.equalsIgnoreCase("0")){
                                Log.d("<<Status 0","Detected");
                                loggedIn = "false";
                                // ed_username.setText("");
                                //  ed_password.setText("");
                                Constant.SetLoginData("", "", LoginActivity.this);
                                tv_error_msg.setVisibility(View.VISIBLE);
                                tv_error_msg.setText(login_details.StatusMsg);
                            }*/
                            if (login_details.Status.equalsIgnoreCase("1")) {
                                for (int i = 0; i < login_details.userListDetails.size(); i++) {

                                    if (!login_details.userListDetails.get(i).rol_id.equalsIgnoreCase("6")) {
                                        tv_error_msg.setText("User does not exist");
                                    }
                                }
                                loggedIn ="true";
                                listSize = login_details.userListDetails.size();
                                AppController.SiblingListSize = listSize;
                                for (int i = 0; i < login_details.userListDetails.size(); i++) {
                                    clt_id = login_details.userListDetails.get(i).clt_id;
                                    usl_id = login_details.userListDetails.get(i).usl_id;
                                    msd_ID = login_details.userListDetails.get(i).msd_ID;
                                    school_name = login_details.userListDetails.get(i).sch_name;

                                    Log.d("<<Login School Name",school_name);
                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("school_name", 0); // 0 - for private mode
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("school_name", school_name);
                                    editor.commit();

                                    roll_no = login_details.userListDetails.get(i).rol_id;
                                    board_name = login_details.userListDetails.get(i).Brd_Name;
                                    teacher_name = login_details.userListDetails.get(i).name;
                                    org_id = login_details.userListDetails.get(i).org_id;
                                    name = login_details.userListDetails.get(i).name;
                                    academic_year = login_details.userListDetails.get(i).acy_year;
                                    clss_teacher = login_details.userListDetails.get(i).clss_teacher;
                                    teacher_div = login_details.userListDetails.get(i).div_name;
                                    teacher_shift = login_details.userListDetails.get(i).sft_name;
                                    teacher_std = login_details.userListDetails.get(i).std_Name;
                                    TeachAnoucementCnt = login_details.userListDetails.get(i).TeachAnoucementCnt;
                                    AppController.clt_id = clt_id;
                                    AppController.usl_id = usl_id;
                                    AppController.msd_ID = msd_ID;
                                    AppController.school_name = school_name;
                                    AppController.Board_name = board_name;

                                    LoginActivity.Set_id(AppController.clt_id, AppController.msd_ID, AppController.usl_id, AppController.Board_name, AppController.school_name, AppController.SiblingListSize, roll_no,name,org_id,clss_teacher,academic_year,teacher_div,teacher_std,teacher_shift,TeachAnoucementCnt,LoginActivity.this);
                                }
                                if(roll_no.equalsIgnoreCase("6")) {     //Parent


                                    if (listSize > 1) {

                                        Intent profile = new Intent(LoginActivity.this, SiblingActivity.class);
                                        profile.putExtra("Sibling", listSize);
                                        profile.putExtra("usl_id", usl_id);
                                        profile.putExtra("clt_id", clt_id);
                                        profile.putExtra("msd_ID", msd_ID);
                                        profile.putExtra("School_name", school_name);
                                        profile.putExtra("board_name", board_name);
                                        profile.putExtra("version_name", AppController.versionName);
                                        profile.putExtra("version_code", AppController.versionCode);
                                        AppController.clt_id = clt_id;
                                        AppController.usl_id = usl_id;
                                        AppController.msd_ID = msd_ID;
                                        AppController.school_name = school_name;
                                        AppController.Board_name = board_name;
                                        startActivity(profile);
                                    } else {
                                        Intent profile = new Intent(LoginActivity.this, ProfileActivity.class);
                                        profile.putExtra("Sibling", listSize);
                                        profile.putExtra("msd_ID", msd_ID);
                                        profile.putExtra("clt_id", clt_id);
                                        profile.putExtra("usl_id", usl_id);
                                        profile.putExtra("School_name", school_name);
                                        profile.putExtra("board_name", board_name);
                                        AppController.Board_name = board_name;
                                        profile.putExtra("version_name", AppController.versionName);
                                        profile.putExtra("version_code", AppController.versionCode);
                                        startActivity(profile);
                                    }
                                }
                                else if(roll_no.equalsIgnoreCase("5"))     //Teacher
                                {
                                    Intent teacherprofile = new Intent(LoginActivity.this,TeacherProfileActivity.class);
                                    teacherprofile.putExtra("clt_id", clt_id);
                                    teacherprofile.putExtra("usl_id", usl_id);
                                    teacherprofile.putExtra("School_name", school_name);
                                    teacherprofile.putExtra("Teacher_name", teacher_name);
                                    teacherprofile.putExtra("board_name", board_name);
                                    teacherprofile.putExtra("org_id",org_id);
                                    teacherprofile.putExtra("cls_teacher",clss_teacher);
                                    teacherprofile.putExtra("academic_year",academic_year);
                                    teacherprofile.putExtra("Teacher_div",teacher_div);
                                    teacherprofile.putExtra("Teacher_Shift",teacher_shift);
                                    teacherprofile.putExtra("Tecaher_Std",teacher_std);
                                    teacherprofile.putExtra("version_name", AppController.versionName);
                                    startActivity(teacherprofile);
                                }
                                else if(roll_no.equalsIgnoreCase("2"))     //Admin
                                {




                                    Intent adminProfile = new Intent(LoginActivity.this,AdminProfileActivity.class);
                                    adminProfile.putExtra("Admin_name",name);
                                    adminProfile.putExtra("School_name",school_name);
                                    adminProfile.putExtra("board_name",board_name);
                                    adminProfile.putExtra("org_id",org_id);
                                    adminProfile.putExtra("clt_id", clt_id);
                                    adminProfile.putExtra("usl_id", usl_id);
                                    adminProfile.putExtra("academic_year",academic_year);
                                    adminProfile.putExtra("version_name", AppController.versionName);
                                    startActivity(adminProfile);
                                }

                            } else if(login_details.Status.equalsIgnoreCase("3")){
                                loggedIn = "false";
                               // ed_username.setText("");df
                              //  ed_password.setText("");
                                Constant.SetLoginData("", "", LoginActivity.this);
                                tv_error_msg.setVisibility(View.VISIBLE);
                                tv_error_msg.setText("User does not exist");
                            }
                            else if(login_details.Status.equalsIgnoreCase("0")){
                                Log.d("<<Status 0","Detected");
                                loggedIn = "false";
                                // ed_username.setText("");
                                //  ed_password.setText("");
                                Constant.SetLoginData("", "", LoginActivity.this);
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
                        Log.d("LoginActivity", "ERROR.._---" + error.getCause());

                    }
                });
    }
    public static void Set_id(String clt_id, String msd_ID, String usl_id, String board_name, String school_name,int listSize,String roll_id,String name,String org_id,String class_teacher,String academic_year,String teacher_div,String teacher_std,String teacher_shift,String teacher_announcementCount,Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = resultpreLoginData.edit();
        editor.putString("clt_id", clt_id);
        editor.putString("msd_ID", msd_ID);
        editor.putString("usl_id", usl_id);
        editor.putString("board_name", board_name);
        editor.putString("School_name", school_name);
        editor.putInt("ListSize", listSize);
        editor.putString("Roll_id", roll_id);
        editor.putString("name", name);
        editor.putString("org_id", org_id);
        editor.putString("class_teacher",class_teacher);
        editor.putString("academic_year",academic_year);
        editor.putString("teacher_div",teacher_div);
        editor.putString("teacher_std",teacher_std);
        editor.putString("teacher_shift",teacher_shift);
        editor.putString("teacher_announcementCount",teacher_announcementCount);
       // editor.putString("Username",username);
      //  editor.putString("Password",password);
        editor.commit();

    }

    public static void Set_Loginid(String username,String password,Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = resultpreLoginData.edit();
        editor.putString("Username",username);
        editor.putString("Password",password);
        editor.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static String get_password(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        Log.e("LoginActivity", resultpreLoginData.getString("Password", ""));
        return resultpreLoginData.getString("clt_id", "");
    }

    public static String get_username(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        Log.e("LoginActivity", resultpreLoginData.getString("Username", ""));
        return resultpreLoginData.getString("Username", "");
    }


    public static String get_cltId(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        Log.e("LoginActivity", resultpreLoginData.getString("clt_id", ""));
        return resultpreLoginData.getString("clt_id", "");
    }
    public static int getListSize(Context context){
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        Log.e("LoginActivity", String.valueOf(resultpreLoginData.getInt("ListSize", 0)));
        return resultpreLoginData.getInt("ListSize", 0);
    }

    public static String get_msdId(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        Log.e("LoginActivity", resultpreLoginData.getString("msd_ID", ""));
        return resultpreLoginData.getString("msd_ID", "");
    }

    public static String get_uslId(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        Log.e("LoginActivity", resultpreLoginData.getString("usl_id", ""));
        return resultpreLoginData.getString("usl_id", "");
    }

    public static String get_BoardName(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        Log.e("LoginActivity", resultpreLoginData.getString("Boatd_Name", ""));
        return resultpreLoginData.getString("board_name", "");
    }

    public static String get_SchoolName(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        Log.e("LoginActivity", resultpreLoginData.getString("School_name", ""));
        return resultpreLoginData.getString("School_name", "");
    }
    public static String get_RollId(Context context){
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        Log.e("Loginactivity",resultpreLoginData.getString("Roll_id",""));
        return resultpreLoginData.getString("Roll_id","");
    }
    public static String get_name(Context context){
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        Log.e("Loginactivity",resultpreLoginData.getString("name",""));
        return resultpreLoginData.getString("name","");
    }
    public static String get_org_id(Context context){
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        Log.e("Loginactivity",resultpreLoginData.getString("org_id",""));
        return resultpreLoginData.getString("org_id","");
    }
    public static String get_academic_year(Context context){
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        Log.e("Loginactivity",resultpreLoginData.getString("academic_year",""));
        return resultpreLoginData.getString("academic_year","");
    }

    public static String get_class_teacher(Context context){
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        Log.e("Loginactivity",resultpreLoginData.getString("class_teacher",""));
        return resultpreLoginData.getString("class_teacher","");
    }
    public static String get_teacher_div(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("teacher_div", "");
    }
    public static String get_teacher_std(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("teacher_std", "");
    }
    public static String get_teacher_shift(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("teacher_shift", "");
    }
    public static String get_teacher_announcementCount(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("teacher_announcementCount", "");
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        AppController.fromSplashScreen = "true";
        AppController.ProfileWithoutSibling="false";
        AppController.drawerSignOut = "true";
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
