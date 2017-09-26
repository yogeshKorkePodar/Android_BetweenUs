package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.BuildConfig;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

import org.json.JSONObject;

/**
 * Created by Gayatri on 12/18/2015.
 */
public class SplashScreen extends Activity {

    String usl_id,clt_id,board_name,school_name,versionName,msd_ID,username,password,roll_no,isStudentResource,teacher_name,
            org_id,name,clss_teacher,academic_year,admin_name,teacher_div,teacher_std,teacher_shift,TeachAnoucementCnt;
    String LoginMethod_name = "DoLogin";
    int versionCode,listSize;
    //ProgressDialog
    ProgressDialog progressDialog;

    DataFetchService dft;
    LoginDetails login_details;
    public static String killedApp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_sceen);
        init();
        getIntentId();
        int secondsDelayed = 1;

        password = Constant.GetLoginPassword(SplashScreen.this);
        username = Constant.GetLoginusername(SplashScreen.this);
        listSize = LoginActivity.getListSize(SplashScreen.this);
        roll_no = LoginActivity.get_RollId(SplashScreen.this);
        clt_id = LoginActivity.get_cltId(SplashScreen.this);
        usl_id = LoginActivity.get_uslId(SplashScreen.this);
        teacher_name = LoginActivity.get_name(SplashScreen.this);
        admin_name = LoginActivity.get_name(SplashScreen.this);
        school_name = LoginActivity.get_SchoolName(SplashScreen.this);
        org_id = LoginActivity.get_org_id(SplashScreen.this);
        academic_year = LoginActivity.get_academic_year(SplashScreen.this);
        clss_teacher = LoginActivity.get_class_teacher(SplashScreen.this);
        board_name = LoginActivity.get_BoardName(SplashScreen.this);
        teacher_div = LoginActivity.get_teacher_div(SplashScreen.this);
        teacher_shift = LoginActivity.get_teacher_shift(SplashScreen.this);
        teacher_std = LoginActivity.get_teacher_std(SplashScreen.this);
        TeachAnoucementCnt = LoginActivity.get_teacher_announcementCount(SplashScreen.this);
        Log.e("LIST Size",String.valueOf(listSize));
        //WebserviceCall
        callWebserviceLogin(username, password);
        // AppController.killApp = "true";
        new Handler().postDelayed(new Runnable() {
            public void run() {
                try {
                    if (AppController.firstView.equalsIgnoreCase("firstView") && Constant.GetLoginusername(getBaseContext()).toString().equalsIgnoreCase("")) {
                        startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                        finish();
                    } else if (AppController.firstView.equalsIgnoreCase("firstView") && AppController.SiblingActivity.equalsIgnoreCase("true") && AppController.fromSplashScreen.equalsIgnoreCase("true")) {
                        Intent profile = new Intent(SplashScreen.this, Profile_Sibling.class);
                        AppController.fromSplashScreen = "false";
                        profile.putExtra("Sibling", AppController.SiblingListSize);
                        profile.putExtra("msd_ID", AppController.msd_ID);
                        profile.putExtra("clt_id", AppController.clt_id);
                        profile.putExtra("usl_id", AppController.usl_id);
                        profile.putExtra("School_name", AppController.school_name);
                        profile.putExtra("board_name", AppController.Board_name);
                        profile.putExtra("version_name", AppController.versionName);
                        profile.putExtra("version_code", AppController.versionCode);
                        profile.putExtra("isStudentResource", isStudentResource);
                        startActivity(profile);
                        finish();
                    } else if (AppController.drawerSignOut.equalsIgnoreCase("true") && AppController.SiblingActivity.equalsIgnoreCase("false")) {
                        startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                        AppController.drawerSignOut = "false";
                        AppController.fromSplashScreen = "false";
                        AppController.ProfileWithoutSibling = "false";
                        finish();
                    } else if (AppController.SiblingActivity.equalsIgnoreCase("false") && AppController.fromSplashScreen.equalsIgnoreCase("true") && roll_no.equalsIgnoreCase("6")/*&& AppController.ProfileWithoutSibling.equalsIgnoreCase("true")*/) {//*if(AppController.firstView.equalsIgnoreCase("firstView") && AppController.SiblingActivity.equalsIgnoreCase("false"))*/ {
                        Intent profile = new Intent(SplashScreen.this, ProfileActivity.class);
                        AppController.fromSplashScreen = "false";
                        profile.putExtra("Sibling", AppController.SiblingListSize);
                        profile.putExtra("msd_ID", AppController.msd_ID);
                        profile.putExtra("clt_id", AppController.clt_id);
                        profile.putExtra("usl_id", AppController.usl_id);
                        profile.putExtra("School_name", AppController.school_name);
                        profile.putExtra("board_name", AppController.Board_name);
                        profile.putExtra("version_name", AppController.versionName);
                        profile.putExtra("version_code", AppController.versionCode);
                        profile.putExtra("isStudentResource", isStudentResource);
                        startActivity(profile);
                        finish();
                    } else if (AppController.fromSplashScreen.equalsIgnoreCase("true") && roll_no.equalsIgnoreCase("5")) {
                        Intent profile = new Intent(SplashScreen.this, TeacherProfileActivity.class);
                        AppController.fromSplashScreen = "false";
                        profile.putExtra("clt_id", clt_id);
                        profile.putExtra("usl_id", usl_id);
                        profile.putExtra("School_name", school_name);
                        profile.putExtra("board_name", board_name);
                        profile.putExtra("Teacher_name", teacher_name);
                        profile.putExtra("version_name", AppController.versionName);
                        profile.putExtra("version_code", versionCode);
                        profile.putExtra("name",name);
                        profile.putExtra("org_id",org_id);
                        profile.putExtra("Teacher_div",teacher_div);
                        profile.putExtra("Teacher_Shift",teacher_shift);
                        profile.putExtra("Tecaher_Std",teacher_std);
                        profile.putExtra("academic_year",academic_year);
                        profile.putExtra("cls_teacher",clss_teacher);
                        profile.putExtra("School_name",school_name);
                        startActivity(profile);
                        finish();
                    }
                    else if(AppController.fromSplashScreen.equalsIgnoreCase("true") && roll_no.equalsIgnoreCase("2")){
                        Intent profile = new Intent(SplashScreen.this, AdminProfileActivity.class);
                        AppController.fromSplashScreen = "false";
                        profile.putExtra("clt_id", clt_id);
                        profile.putExtra("usl_id", usl_id);
                        profile.putExtra("School_name", school_name);
                        profile.putExtra("board_name", board_name);
                        profile.putExtra("Admin_name", admin_name);
                        profile.putExtra("version_name", AppController.versionName);
                        profile.putExtra("version_code", versionCode);
                        profile.putExtra("name",name);
                        profile.putExtra("org_id",org_id);
                        profile.putExtra("academic_year",academic_year);
                        startActivity(profile);
                        finish();
                    }
                    else if (LoginActivity.loggedIn.equalsIgnoreCase("false")) {
                        startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                        finish();
                    } else {
                        if (LoginActivity.loggedIn.equalsIgnoreCase("false")) {
                            startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                            finish();
                        } else if (!Constant.GetLoginusername(getBaseContext()).toString().equalsIgnoreCase("") && listSize == 1 && roll_no.equalsIgnoreCase("6")) {
                            AppController.drawerSignOut = "false";
                            AppController.fromSplashScreen = "false";
                            killedApp = "true";
                            AppController.SiblingActivity = "false";
                            LoginActivity.loggedIn = "true";
                            Intent profile = new Intent(SplashScreen.this, ProfileActivity.class);
                            AppController.fromSplashScreen = "false";
                            profile.putExtra("Sibling", listSize);
                            profile.putExtra("msd_ID", msd_ID);
                            profile.putExtra("clt_id", clt_id);
                            profile.putExtra("usl_id", usl_id);
                            profile.putExtra("School_name", school_name);
                            profile.putExtra("board_name", board_name);
                            profile.putExtra("version_name", versionName);
                            profile.putExtra("version_code", versionCode);
                            startActivity(profile);
                            finish();
                        } else if (!Constant.GetLoginusername(getBaseContext()).toString().equalsIgnoreCase("") && roll_no.equalsIgnoreCase("5")) {
                            AppController.drawerSignOut = "false";
                            AppController.fromSplashScreen = "false";
                            //   callWebserviceLogin(username, password);
                            killedApp = "true";
                            LoginActivity.loggedIn = "true";
                            Intent profile = new Intent(SplashScreen.this, TeacherProfileActivity.class);
                            AppController.fromSplashScreen = "false";
                            profile.putExtra("clt_id", clt_id);
                            profile.putExtra("usl_id", usl_id);
                            profile.putExtra("School_name", school_name);
                            profile.putExtra("board_name", board_name);
                            profile.putExtra("Teacher_name", teacher_name);
                            profile.putExtra("version_name", AppController.versionName);
                            profile.putExtra("version_code", versionCode);
                            profile.putExtra("School_name",school_name);
                            profile.putExtra("name",name);
                            profile.putExtra("org_id",org_id);
                            profile.putExtra("Teacher_div",teacher_div);
                            profile.putExtra("Teacher_Shift",teacher_shift);
                            profile.putExtra("Tecaher_Std",teacher_std);
                            profile.putExtra("academic_year",academic_year);
                            profile.putExtra("cls_teacher",clss_teacher);
                            startActivity(profile);
                            finish();
                        }else if(!Constant.GetLoginusername(getBaseContext()).toString().equalsIgnoreCase("") && roll_no.equalsIgnoreCase("2")){
                            Intent profile = new Intent(SplashScreen.this, AdminProfileActivity.class);
                            AppController.fromSplashScreen = "false";
                            profile.putExtra("clt_id", clt_id);
                            profile.putExtra("usl_id", usl_id);
                            profile.putExtra("School_name", school_name);
                            profile.putExtra("board_name", board_name);
                            profile.putExtra("Admin_name", admin_name);
                            profile.putExtra("version_name", AppController.versionName);
                            profile.putExtra("version_code", versionCode);
                            profile.putExtra("name",name);
                            profile.putExtra("org_id",org_id);
                            profile.putExtra("academic_year",academic_year);
                            startActivity(profile);
                            finish();
                        }

                        else {
                            killedApp = "true";
                            LoginActivity.loggedIn = "true";
                            AppController.drawerSignOut = "false";
                            AppController.fromSplashScreen = "false";
                            AppController.SiblingActivity = "true";
                            Intent profile = new Intent(SplashScreen.this, Profile_Sibling.class);
                            AppController.fromSplashScreen = "false";
                            profile.putExtra("Sibling", listSize);
                            profile.putExtra("msd_ID", msd_ID);
                            profile.putExtra("clt_id", clt_id);
                            profile.putExtra("usl_id", usl_id);
                            profile.putExtra("School_name", school_name);
                            profile.putExtra("board_name", board_name);
                            profile.putExtra("version_name", versionName);
                            profile.putExtra("version_code", versionCode);
                            startActivity(profile);
                            finish();
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }


        }, secondsDelayed * 2000);
    }

    private void init() {
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        progressDialog = Constant.getProgressDialog(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (AppController.killApp.equalsIgnoreCase("true"))
            startActivity(new Intent(SplashScreen.this, LoginActivity.class));
        finish();
    }
    private void getIntentId() {
        Intent intent = getIntent();
        listSize = intent.getIntExtra("Sibling", 0);
        usl_id = intent.getStringExtra("usl_id");
        clt_id = intent.getStringExtra("clt_id");
        msd_ID = intent.getStringExtra("msd_ID");
        school_name = intent.getStringExtra("School_name");
        versionName = intent.getStringExtra("version_name");
        versionCode = intent.getIntExtra("version_code", 0);
        board_name = intent.getStringExtra("board_name");
        org_id = intent.getStringExtra("org_id");
        academic_year = intent.getStringExtra("");
        clss_teacher = intent.getStringExtra("cls_teacher");
        isStudentResource = intent.getStringExtra("isStudentResource");
        teacher_name = intent.getStringExtra("Teacher_name");
        admin_name = intent.getStringExtra("Admin_name");
        teacher_div = intent.getStringExtra("Teacher_div");
        teacher_std = intent.getStringExtra("Tecaher_Std");
        teacher_shift = intent.getStringExtra("Teacher_Shift");
        versionName = BuildConfig.VERSION_NAME;
        AppController.Board_name = board_name;
        AppController.school_name = school_name;
        AppController.versionName = versionName;
        AppController.versionCode = versionCode;
        AppController.usl_id = usl_id;
        AppController.clt_id = clt_id;
        AppController.msd_ID = msd_ID;
    }
    private void callWebserviceLogin(final String username, String password) {
        dft.doLogin(username, password, LoginMethod_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);

                            if (login_details.Status.equalsIgnoreCase("1")) {
                                LoginActivity.loggedIn = "true";
                                listSize = login_details.userListDetails.size();
                                AppController.SiblingListSize = listSize;
                                for (int i = 0; i < login_details.userListDetails.size(); i++) {
                                    clt_id = login_details.userListDetails.get(i).clt_id;
                                    usl_id = login_details.userListDetails.get(i).usl_id;
                                    msd_ID = login_details.userListDetails.get(i).msd_ID;
                                    school_name = login_details.userListDetails.get(i).sch_name;
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
                                    versionName = BuildConfig.VERSION_NAME;
                                    AppController.versionName= versionName;
                                    AppController.clt_id = clt_id;
                                    AppController.usl_id = usl_id;
                                    AppController.msd_ID = msd_ID;
                                    AppController.school_name = school_name;
                                    AppController.Board_name = board_name;
                                    AppController.SiblingListSize = listSize;
                                    LoginActivity.Set_id(AppController.clt_id, AppController.msd_ID, AppController.usl_id, AppController.Board_name, AppController.school_name, AppController.SiblingListSize,roll_no,name,org_id,clss_teacher,academic_year,teacher_div,teacher_std,teacher_shift,TeachAnoucementCnt,SplashScreen.this);
                                }

                            } else {
                                LoginActivity.loggedIn = "false";
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

}
