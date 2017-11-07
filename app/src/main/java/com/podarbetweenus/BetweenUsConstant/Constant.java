package com.podarbetweenus.BetweenUsConstant;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Administrator on 10/16/2015.
 */
public class Constant {

    public static AlertDialog.Builder popupDialog = null;
    public static SharedPreferences resultpreLoginData;

    // Amazon WS
    public static final String Common_URL = "http://www.betweenus.in/PODARAPP/PodarApp.svc/";

    public static String emial_Id = "Enter Email Id";
    public static String mobile_Number = "Enter Mobile Number";
    public static String version_update = "true";
    public static boolean checkInternetConnection(Context _activity)
    {
        ConnectivityManager conMgr = (ConnectivityManager) _activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable())
            return true;
        else
            return false;
    }
    public static void showOkPopup(Context context, String error_msg,
                                   DialogInterface.OnClickListener listener) {
        popupDialog = new AlertDialog.Builder(context);
        popupDialog.setCancelable(false);
        popupDialog.setPositiveButton("Ok", listener);
        popupDialog.setMessage(error_msg);
        popupDialog.show();
    }
    public static void RemoveData(Context context)
    {
        resultpreLoginData =PreferenceManager.getDefaultSharedPreferences(context);
        resultpreLoginData.edit().clear().commit();
    }
    public static ProgressDialog getProgressDialog(Context context) {
        ProgressDialog progress_dialog1 = new ProgressDialog(context);
        progress_dialog1.setMessage("Loading ... ");
        progress_dialog1.setCancelable(true);
        return progress_dialog1;
    }
    public static void SetLoginData(String Username,String password,Context context)
    {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = resultpreLoginData.edit();
        editor.putString("Username", Username);
        editor.putString("Password", password);
        editor.commit();
    }
    public static void setDrawerData(String academicYear,String childName,String standard,String rollNo,Context context)
    {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = resultpreLoginData.edit();
        editor.putString("AcademicYear", academicYear);
        editor.putString("ChildName", childName);
        editor.putString("Standard", standard);
        editor.putString("RollNo", rollNo);
        editor.commit();
    }
    public static void setTeacherDrawerData(String academicYear,String name,String teacher_div,String teacher_std,String teacher_shift,Context context){
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = resultpreLoginData.edit();
        editor.putString("AcademcYear",academicYear);
        editor.putString("Name",name);
        editor.putString("Teacher_div",teacher_div);
        editor.putString("Teacher_std",teacher_std);
        editor.putString("Teacher_shift",teacher_shift);
        editor.commit();
    }
    public static void setAdminDrawerData(String academicYear,String name,Context context){
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = resultpreLoginData.edit();
        editor.putString("AcademcYear",academicYear);
        editor.putString("Name",name);
        editor.commit();
    }
    public static String GetLoginusername(Context context)
    {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        Log.e("LoginActivity", resultpreLoginData.getString("Username", ""));
        return resultpreLoginData.getString("Username", "");
    }
    public static String GetLoginPassword(Context context)
    {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        Log.e("LoginActivity", resultpreLoginData.getString("Password", ""));
        return resultpreLoginData.getString("Password", "");
    }

    public static String GetRollNo(Context context)
    {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        Log.e("ProfileActivity", resultpreLoginData.getString("RollNo", ""));
        return resultpreLoginData.getString("RollNo", "");
    }
    public static String GetStandard(Context context)
    {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        Log.e("ProfileActivity", resultpreLoginData.getString("Standard", ""));
        return resultpreLoginData.getString("Standard", "");
    }
    public static String GetChildName(Context context)
    {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        Log.e("ProfileActivity", resultpreLoginData.getString("ChildName", ""));
        return resultpreLoginData.getString("ChildName", "");
    }
    public static String GetAcademicYear(Context context)
    {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        Log.e("ProfileActivity", resultpreLoginData.getString("AcademicYear", ""));
        return resultpreLoginData.getString("AcademicYear", "");
    }
    public static void SetAuthToken(String AuthToken,Context mcontext) {
        SharedPreferences resultpre_patient = PreferenceManager
                .getDefaultSharedPreferences(mcontext);
        SharedPreferences.Editor editor = resultpre_patient.edit();
        editor.putString("AuthToken", AuthToken);
        editor.commit();
    }

    public static String GetResult_AuthToken(Context mcontext) {
        SharedPreferences resultpre_patient = PreferenceManager
                .getDefaultSharedPreferences(mcontext);
        return resultpre_patient.getString("AuthToken", "");
    }
}
