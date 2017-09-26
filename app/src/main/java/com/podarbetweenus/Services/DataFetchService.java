package com.podarbetweenus.Services;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Utility.AppController;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 10/19/2015.
 */
public class DataFetchService {
    protected static final String TAG = "DataFetchService";
    private ProgressDialog pDialog;
    public Context mcontext;

    public DataFetchService(Context mcontext) {
        // TODO Auto-generated constructor stub
        this.mcontext = mcontext;
      //  isInternetOn();
        pDialog = new ProgressDialog(mcontext.getApplicationContext());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(true);
    }

    //Check Internet Connectivity
    public final boolean isInternetOn() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) mcontext.getSystemService(mcontext.CONNECTIVITY_SERVICE);
            // Check for network connections
            if (connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

                // if connected with internet

                return true;
            } else if (
                    connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                            connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

                Constant.showOkPopup(mcontext, "Please Check Internet Connectivity", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                });
                return false;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    /**
     *Login
     * */
    public void doLogin(String username, String password,String methodname,int callType,Response.Listener<JSONObject> listener,
                                     Response.ErrorListener errlsn){

        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("LoginId", username);
            jsonParams.put("Password", password);


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        String loginRequestUrl = Constant.Common_URL + methodname;
        makeJsonObjReq(Constant.Common_URL + methodname, callType, listener, errlsn, jsonParams);
        Log.d("<<DoLogin",loginRequestUrl);


    }
    /**
     *SiblingInfo
     * */
    public void getStudentInfo(String usl_id, String clt_id,String methodname,int callType,Response.Listener<JSONObject> listener,
                        Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("usl_id", usl_id);
            jsonParams.put("clt_id", clt_id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }
    /**
     *StudentDeatails
     * */
    public void getStudentDetails(String msd_ID, String clt_id,String usl_id,String methodname,int callType,Response.Listener<JSONObject> listener,
                               Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("msd_id", msd_ID);
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("usl_id", usl_id);


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }
    //ChangePassword
    public void getChangePassword(String usl_id, String old_password,String new_password,String methodname,int callType,Response.Listener<JSONObject> listener,
                                  Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("usl_id", usl_id);
            jsonParams.put("Password", old_password);
            jsonParams.put("NewPassword",new_password);


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }/**
     *Forgot Password
     * */
     public void forgotPassword(String credential, String type,String methodname,int callType,Response.Listener<JSONObject> listener,
                                        Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("SendTo", credential);
            jsonParams.put("type", type);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     *Forgot Password
     * */
    public void   getdatedeopdown(String msd_ID, String clt_id,String Brd_name,String methodname,int callType,Response.Listener<JSONObject> listener,
                               Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("msd_id", msd_ID);
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("brd_name", Brd_name);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     *View Messages
     * */
    public void getviewMessages(String clt_id, String usl_id,String msd_ID,String month,String check,String pageNo,String pageSize,String methodname,int callType,Response.Listener<JSONObject> listener,
                                  Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("usl_id", usl_id);
            jsonParams.put("msd_id", msd_ID);
            jsonParams.put("month", month);
            jsonParams.put("check", check);
            jsonParams.put("PageNo",pageNo);
            jsonParams.put("PageSize", pageSize);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL + methodname, callType, listener, errlsn, jsonParams);
        Log.e("Params", String.valueOf(jsonParams));
    }
    /**
     *Announcement
     * */
    public void getannaouncement(String clt_id, String usl_id,String msd_ID,String methodname,int callType,Response.Listener<JSONObject> listener,
                                Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("usl_id", usl_id);
            jsonParams.put("msd_id", msd_ID);
            Log.e("params announce", String.valueOf(jsonParams));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL + methodname, callType, listener, errlsn, jsonParams);

    }   /**
     *Announcement
     * */
    public void getbehaviour(String msd_ID, String clt_id,String usl_id,String methodname,int callType,Response.Listener<JSONObject> listener,
                                 Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("msd_id", msd_ID);
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("usl_id", usl_id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }
    /**
     *Sent Messages
     * */
    public void getsentMessages(String clt_id,String usl_id,String month,String check,String pageNo,String pageSize,String methodname,int callType,Response.Listener<JSONObject> listener,
                             Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("usl_id", usl_id);
            jsonParams.put("month", month);
            jsonParams.put("check", check);
            jsonParams.put("PageNo",pageNo);
            jsonParams.put("PageSize",pageSize);

            Log.e("LastSentmessage", String.valueOf(jsonParams));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }
    /**
     *Sent Messages
     * */
    public void getreceipentdatedeopdown(String clt_id,String msd_id,String methodname,int callType,Response.Listener<JSONObject> listener,
                                Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("msd_id", msd_id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     *Enter Message
     * */
    public void getEnterMessages(String clt_id,String msg_subject,String msg_body,String usl_id,String msd_id,String toUslId,String methodname,int callType,Response.Listener<JSONObject> listener,
                                Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("subject", msg_subject);
            jsonParams.put("MessageBody", msg_body);
            jsonParams.put("usl_id", usl_id);
            jsonParams.put("msd_id", msd_id);
            jsonParams.put("Tousl_id", toUslId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }
    /**
     *Reply Message
     * */
    public void getReplyMessage(String clt_id,String msg_subject,String reply_message,String usl_id,String msd_id,String toUslId,String methodname,int callType,Response.Listener<JSONObject> listener,
                                 Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("subject", msg_subject);
            jsonParams.put("MessageBody", reply_message);
            jsonParams.put("usl_id", usl_id);
            jsonParams.put("msd_id", msd_id);
            jsonParams.put("Tousl_id", toUslId);
            Log.e("ReplyParams", String.valueOf(jsonParams));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }
    /**
     *Update Status Message
     * */
    public void getUpdatdStatusMessages(String pmuId,String usl_id,String clt_id,String methodname,int callType,Response.Listener<JSONObject> listener,
                                Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("pmu_id", pmuId);
            jsonParams.put("usl_id", usl_id);
            jsonParams.put("clt_id", clt_id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }
    /**
     *Update Status Message
     * */
    public void getAbsentHistory(String msd_id,String clt_id,String month_id,String year,String methodname,int callType,Response.Listener<JSONObject> listener,
                                        Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("msd_id", msd_id);
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("month", month_id);
            jsonParams.put("year", year);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.e("Historyparams",String.valueOf(jsonParams));
        makeJsonObjReq(Constant.Common_URL + methodname, callType, listener, errlsn, jsonParams);

    }
    /**
     *Dropdown Cycle Test
     * */
    public void getcycleTestdeopdown(String board_name,String methodname,int callType,Response.Listener<JSONObject> listener,
                                 Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("brd_name", board_name);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }
    /**
     *Dropdown Cycle Test
     * */
    public void getsubjectdropdown(String clt_id,String msd_id,String cycletestId,String methodname,int callType,Response.Listener<JSONObject> listener,
                                     Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("msd_id", msd_id);
            jsonParams.put("cycleTest", cycletestId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     *Topic List
     * */
    public void gettopicslist(String board_name,String std_name,String cycletestId,String subject_id,String clt_id,String methodname,int callType,Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("brd_name", board_name);
            jsonParams.put("std_name", std_name);
            jsonParams.put("subject", subject_id);
            jsonParams.put("cycleTest", cycletestId);
            jsonParams.put("clt_id", clt_id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }
    /**
     *Resource List
     * */
    public void getResourcelist(String crf_id,String methodname,int callType,Response.Listener<JSONObject> listener,
                              Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("crf_id", crf_id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     *FeesInfo
     * */
    public void getFeesInfo(String msd_id,String clt_id,String methodname,int callType,Response.Listener<JSONObject> listener,
                                Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("msd_id", msd_id);
            jsonParams.put("clt_id",clt_id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     *FeesOustandingInfo
     * */
    public void getFeesOutStandingStatus(String cls_id, String sch_id, String acy_id,String brd_id, String stud_id,String methodname,int callType,Response.Listener<JSONObject> listener,
                            Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("cls_id", cls_id);
            jsonParams.put("sch_id",sch_id);
            jsonParams.put("Acy_year",acy_id);
            jsonParams.put("brd_name",brd_id);
            jsonParams.put("stud_id",stud_id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }


    /**
     *Parent Info
     * */
    public void getparentInfo(String msd_id,String methodname,int callType,Response.Listener<JSONObject> listener,
                            Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("msd_Id", msd_id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }
    /**
     *State List
     * */
    public void getStateList(String cnt_id,String methodname,int callType,Response.Listener<JSONObject> listener,
                              Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("cnt_id", cnt_id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }
    /**
     *State List
     * */
    public void getCityList(String state_id,String methodname,int callType,Response.Listener<JSONObject> listener,
                             Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("Ste_ID", state_id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }
    /**
     *UpdateInfo
     * */
    public void updateInfo(String msd_ID,String registered_emailId,String mobile_no,String building_address,String streetName,String location_area,String pincode,String state
            ,String city,String country,String tel_no,String methodname,int callType,Response.Listener<JSONObject> listener,
                            Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("msd_Id", msd_ID);
            jsonParams.put("stu_email", registered_emailId);
            jsonParams.put("stu_mobile", mobile_no);
            jsonParams.put("flat", building_address);
            jsonParams.put("Street", streetName);
            jsonParams.put("Area", location_area);
            jsonParams.put("pinNo", pincode);
            jsonParams.put("Ste_ID", state);
            jsonParams.put("cit_ID", city);
            jsonParams.put("cnt_id", "1");
            jsonParams.put("TelNo", tel_no);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }


    /**
     *UpdateFatherInfo
     * */
    public void updateFatherInfo(String msd_ID,String dateOfBirth,String qualification_id,String mother_tongue_id,String religion_id,String nationality_id,String f_PanNo,String f_ComapnyName
            ,String occupation_id,String methodname,int callType,Response.Listener<JSONObject> listener,
                           Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("msd_Id", msd_ID);
            jsonParams.put("nms_FDOB", dateOfBirth);
            jsonParams.put("qlf_Fid", qualification_id);
            jsonParams.put("mtg_Fid", mother_tongue_id);
            jsonParams.put("rel_Fid", religion_id);
            jsonParams.put("nns_Fid", nationality_id);
            jsonParams.put("nmd_FPanNO", f_PanNo);
            jsonParams.put("Fcompanyname", f_ComapnyName);
            jsonParams.put("ocp_Fid", occupation_id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }
    /**
     *UpdateEmergencyContactInfo
     * */
    public void updateEmergencyContactInfo(String msd_ID,String lastname,String firstname,String middlename,String emailid,String relationship,String mobile_no,String methodname,int callType,Response.Listener<JSONObject> listener,
                                 Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("msd_Id", msd_ID);
            jsonParams.put("nms_LName", lastname);
            jsonParams.put("nms_FName", firstname);
            jsonParams.put("nms_MName", middlename);
            jsonParams.put("emergency_email", emailid);
            jsonParams.put("nmd_relationshipe", relationship);
            jsonParams.put("em_mobile", mobile_no);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     *UpdateEmergencyContactInfo
     * */
    public void updateMedicalInfo(String msd_ID,String blood_group_id,String all_allergies,String health_info,String height,String weight,String regular_medication,String regular_medication_with_dosage,String any_impairment
            ,String exemption_from_activities,String methodname,int callType,Response.Listener<JSONObject> listener,
                                           Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("msd_Id", msd_ID);
            jsonParams.put("blg_id", blood_group_id);
            jsonParams.put("hlt_Allergies", all_allergies);
            jsonParams.put("hlt_healthproblems", health_info);
            jsonParams.put("hlt_height", height);
            jsonParams.put("hlt_weight", weight);
            jsonParams.put("medication", regular_medication);
            jsonParams.put("dosage", regular_medication_with_dosage);
            jsonParams.put("impairment", any_impairment);
            jsonParams.put("exemption", exemption_from_activities);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }
    public void getCountryList(String methodname,int callType,Response.Listener<JSONObject> listener,
                              Response.ErrorListener errlsn,Map<String, String> requestMap) {


        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, null);

    }


    /**
     * Teacher Module
     * */
    /**
     * getTeacherannaouncement
     * */
    public void getTeacherannaouncement(String clt_id,String usl_id,String methodname,int callType,Response.Listener<JSONObject> listener,
                                  Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("usl_id", usl_id);
            Log.e("Ann PAra",String.valueOf(jsonParams));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getTeachersubjectList
     * */
    public void getTeachersubjectList(String clt_id,String usl_id,String methodname,int callType,Response.Listener<JSONObject> listener,
                                        Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("usl_id", usl_id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }
    /**
     * getTeacherAttendanceDropdown
     * */
    public void getTeacherAttendanceDropdown(String clt_id,String usl_id,String board_name,String methodname,int callType,Response.Listener<JSONObject> listener,
                                      Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("usl_id", usl_id);
            jsonParams.put("brd_name",board_name);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getAttendanceList
     * */
    public void getAttendanceList(String clt_id,String month_id,String class_id,int pageIndex,int pageSize,String usl_id,String academic_year_Id,String methodname,int callType,Response.Listener<JSONObject> listener,
                                             Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("tmonth", month_id);
            jsonParams.put("cls_id",class_id);
            jsonParams.put("PageNo",pageIndex);
            jsonParams.put("PageSize",pageSize);
            jsonParams.put("usl_id",usl_id);
            jsonParams.put("Acy_year",academic_year_Id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getBehaviourList
     * */
    public void getBehaviourList(String clt_id,String class_id,int pageIndex,int pageSize,String methodname,int callType,Response.Listener<JSONObject> listener,
                                  Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("cls_id",class_id);
            jsonParams.put("PageNo",pageIndex);
            jsonParams.put("PageSize",pageSize);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getViewBehaviourList
     * */
    public void getViewBehaviourList(String msd_id,String clt_id,String methodname,int callType,Response.Listener<JSONObject> listener,
                                 Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("msdID",msd_id);
            jsonParams.put("clt_id", clt_id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getTeacherAttendanceListReason
     * */
    public void getTeacherAttendanceListReason(String msd_id,String clt_id,String month_id,String atn_valid,String academic_yearId,String methodname,int callType,Response.Listener<JSONObject> listener,
                                     Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("msdID",msd_id);
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("tmonth", month_id);
            jsonParams.put("atn_valid", atn_valid);
            jsonParams.put("Acy_year", academic_yearId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getTeacherSMSDropdown
     * */
    public void getTeacherSMSDropdown(String clt_id,String usl_id,String methodname,int callType,Response.Listener<JSONObject> listener,
                                               Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("usl_id", usl_id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getCalenderData
     */
    public void getCalenderData(String clt_id,String class_id, String Acy_year, String brd_name,String methodname,int callType,Response.Listener<JSONObject> listener,
                                      Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("brd_name", brd_name);
            jsonParams.put("cls_id",class_id );
            jsonParams.put("Acy_year",Acy_year);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getTeacherSMSStudentList
     * */
    public void getTeacherSMSStudentList(String clt_id,String cls_id,String Student_name,int pageIndex,int pageSize,String methodname,int callType,Response.Listener<JSONObject> listener,
                                      Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("cls_id", cls_id);
            jsonParams.put("studentName", Student_name);
            jsonParams.put("PageNo", pageIndex);
            jsonParams.put("PageSize",pageSize);
            Log.e("Studentlist", String.valueOf(jsonParams));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getStudentAttendenceList
     * */
    public void getStudentAttendenceList(String atn_date, String clt_id, String cls_id, String usl_id, String methodname, int callType, Response.Listener<JSONObject> listener, Response.ErrorListener errlsn ){
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("atn_date",atn_date);
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("cls_id", cls_id);
            jsonParams.put("usl_id", usl_id);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);
    }


    //Get Module List
    public void getModuleList(String methodname,int callType,Response.Listener<JSONObject> listener,
                               Response.ErrorListener errlsn,Map<String, String> requestMap) {


        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, null);

    }

    /**
     * getTeacherSendSMS
     * */
    public void getTeacherSendSMS(String clt_id,String cls_id,String stud_id,String messageContent,String usl_id,String methodname,int callType,Response.Listener<JSONObject> listener,
                                         Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("cls_id", cls_id);
            jsonParams.put("stud_id", stud_id);
            jsonParams.put("msg_message", messageContent);
            jsonParams.put("usl_id",usl_id);


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Log.e("TecherSMSParamsr",String.valueOf(jsonParams));
        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getTeacherSentMessagest
     * */
    public void getTeacherSentMessagest(String clt_id,String usl_id,String month,String check,int pageIndex,int pageSize,String methodname,int callType,Response.Listener<JSONObject> listener,
                                  Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("usl_id", usl_id);
            jsonParams.put("month", month);
            jsonParams.put("check",check);
            jsonParams.put("PageNo", pageIndex);
            jsonParams.put("PageSize",pageSize);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.e("Techermsgparameter",String.valueOf(jsonParams));
        makeJsonObjReq(Constant.Common_URL + methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getSendTeacherMessage
     * */
    public void getSendTeacherMessage(String clt_id,String class_id,String message_subject,String message_content,String usl_id,String stud_id,String toUslId,String filename,String filepath,String methodname,int callType,Response.Listener<JSONObject> listener,
                                        Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("cls_id", class_id);
            jsonParams.put("pmg_subject", message_subject);
            jsonParams.put("msg_message", message_content);
            jsonParams.put("usl_id",usl_id);
            jsonParams.put("stud_id", stud_id);
            jsonParams.put("sender_uslId",toUslId);
            jsonParams.put("filename",filename);
            jsonParams.put("filepath",filepath);
            Log.e("params teacher send",String.valueOf(jsonParams));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * SubmitAttendenceList
     * */
    //atn_date, clt_id, class_id, usl_id, selectedAbsentCheckboxes, selectedSMSCheckbox, submit_Attendence_Method_name
    public void SubmitAttendenceList(String mode, HashMap absent_reason_map, String atn_date, String clt_id,String acy_id, String usl_id,String AbsentMsd_id,String PresentMsd_id, String selectedSMSCheckbox, String methodname,int callType,Response.Listener<JSONObject> listener,
                                      Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("Mode",mode);
            jsonParams.put("atn_date",atn_date);
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("acy_id", acy_id);
            jsonParams.put("usl_id",usl_id);
            jsonParams.put("AbsentMsd_id",AbsentMsd_id);
            jsonParams.put("PresentMsd_id",PresentMsd_id);
            jsonParams.put("SmsMsd_id",selectedSMSCheckbox);

            //Log.d("<<Retrieving param ",jsonParams.getString(AbsentMsd_id));

            Log.d("<<Inside ","SubmitAttendenceList");
            Log.d("<<mode",mode);
            Log.d("<<atn_date",atn_date);
            Log.d("<<clt_id", clt_id);
            Log.d("<<acy_id", acy_id);
            Log.d("<<usl_id",usl_id);
            Log.d("<<AbsentMsd_id",AbsentMsd_id);
            Log.d("<<PresentMsd_id",PresentMsd_id);
            Log.d("<<selectedSMSCheckbox",selectedSMSCheckbox);


            Log.d("<<SubmtAttendenceList",String.valueOf(jsonParams));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getteacherTopicsList
     * */
    public void getteacherTopicsList(String class_id,String board_name,String std_name,String subject_name,String cycletest,String clt_id,String usl_id,String methodname,int callType,Response.Listener<JSONObject> listener,
                                      Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("cls_id", class_id);
            jsonParams.put("brd_name",board_name);
            jsonParams.put("std_name", std_name);
            jsonParams.put("pmg_subject", subject_name);
            jsonParams.put("cycletest", cycletest);
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("usl_id", usl_id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }
    /**
     * getTecherReplyMessage
     * */
    public void getTecherReplyMessage(String clt_id,String message_subject,String reply_msg,String usl_id,String stud_id,String toUslId,String filename,String filepath,String methodname,int callType,Response.Listener<JSONObject> listener,
                                      Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("pmg_subject", message_subject);
            jsonParams.put("msg_message", reply_msg);
            jsonParams.put("usl_id", usl_id);
            jsonParams.put("stud_id",stud_id);
            jsonParams.put("sender_uslId",toUslId);
            jsonParams.put("filename",filename);
            jsonParams.put("filepath",filepath);
            Log.e("ParameterSend", String.valueOf(jsonParams));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getTeacherResourceList
     * */
    public void getTeacherResourceList(String crf_id,String usl_id,String methodname,int callType,Response.Listener<JSONObject> listener,
                                      Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("crf_id", crf_id);
            jsonParams.put("usl_id", usl_id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

   /// Admin
    /**
     * getAdminTeacherList
     * */
    public void getAdminTeacherList(String clt_id,String board_name,String usl_id,int PageNo,int PageSize,String name,String methodname,int callType,Response.Listener<JSONObject> listener,
                                       Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("brd_name", board_name);
            jsonParams.put("usl_id", usl_id);
            jsonParams.put("PageNo", PageNo);
            jsonParams.put("PageSize", PageSize);
            jsonParams.put("Name",name);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getAdminTeacherList
     * */
    public void getAdminSendSMS(String usl_id,String clt_id,String sms_content,String contact_number,String board_name,String chkAll,String methodname,int callType,Response.Listener<JSONObject> listener,
                                    Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("usl_id", usl_id);
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("smsString", sms_content);
            jsonParams.put("mobilenumber",contact_number);
            jsonParams.put("brd_name", board_name);
            jsonParams.put("ChkAll",chkAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getAdminTeacherList
     * */
    public void getAdminMessages(String clt_id,String usl_id,String check,int PageNo,int PageSize,String methodname,int callType,Response.Listener<JSONObject> listener,
                                Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("usl_id", usl_id);
            jsonParams.put("check", check);
            jsonParams.put("PageNo",PageNo);
            jsonParams.put("PageSize", PageSize);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getAcademicYearDropdown
     * */
    public void getAcademicYearDropdown(String clt_id,String usl_id,String methodname,int callType,Response.Listener<JSONObject> listener,
                                 Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("usl_id", usl_id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getstudentDetails_SMS
     * */
    public void getstudentDetails_SMS(String clt_id,String usl_id,String searchKey,String searchvalue,String aca_yr,String methodname,int callType,Response.Listener<JSONObject> listener,
                                        Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("usl_id", usl_id);
            jsonParams.put("SearchKey",searchKey);
            jsonParams.put("SearchValue",searchvalue);
            jsonParams.put("acy_id", aca_yr);
            Log.e("Stu List",String.valueOf(jsonParams));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);
    }

    /**
     * getAdminStudentSendSMS
     * */
    public void getAdminStudentSendSMS(String clt_id,String class_id,String stud_id,String message_content,String usl_id,String methodname,int callType,Response.Listener<JSONObject> listener,
                                      Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("cls_id",class_id);
            jsonParams.put("stud_id",stud_id);
            jsonParams.put("msg_message",message_content);
            jsonParams.put("usl_id", usl_id);
           // Log.e("params",String.valueOf(jsonParams));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getAdminSchoolSendSMS
     * */
    public void getAdminSchoolSendSMS(String clt_id,String usl_id,String message_content,String class_id,String mode,String methodname,int callType,Response.Listener<JSONObject> listener,
                                       Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("usl_id", usl_id);
            jsonParams.put("message",message_content);
            jsonParams.put("cls_id",class_id);
            jsonParams.put("Mode",mode);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getAdminSchoolSendSMSTeacher
     * */
    public void getAdminSchoolSendSMSTeacher(String clt_id,String usl_id,String message_content,String board_name,String mode,String methodname,int callType,Response.Listener<JSONObject> listener,
                                      Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("usl_id", usl_id);
            jsonParams.put("message",message_content);
            jsonParams.put("brd_name",board_name);
            jsonParams.put("Mode",mode);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getaddannouncement
     * */
    public void getaddannouncement(String clt_id,String usl_id,String announcement,String methodname,int callType,Response.Listener<JSONObject> listener,
                                             Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("usl_id", usl_id);
            jsonParams.put("message",announcement);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getaddannouncement
     * */
    public void getTeacherEditannaouncement(String msd_id,String announcement,String methodname,int callType,Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("msg_Id", msd_id);
            jsonParams.put("message",announcement);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL + methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getAdminStudentSendMessage
     * */
    public void getAdminStudentSendMessage(String clt_id,String cls_id,String message_subject,String reply_msg,String usl_id,String stud_id,String toUslId,String filename,String filepath,String methodname,int callType,Response.Listener<JSONObject> listener,
                                      Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("cls_id", cls_id);
            jsonParams.put("pmg_subject", message_subject);
            jsonParams.put("msg_message", reply_msg);
            jsonParams.put("usl_id", usl_id);
            jsonParams.put("stud_id",stud_id);
            jsonParams.put("sender_uslId",toUslId);
            jsonParams.put("filename",filename);
            jsonParams.put("filepath",filepath);
            Log.e("SendParameters",String.valueOf(jsonParams));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getAdminStudentSendMessage
     * */
    public void getTeacherListMessage(String clt_id,String usl_id,String pageNo,String pageSize,String studentName,String methodname,int callType,Response.Listener<JSONObject> listener,
                                           Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("usl_id", usl_id);
            jsonParams.put("PageNo", pageNo);
            jsonParams.put("PageSize", pageSize);
            jsonParams.put("studentName", studentName);
            Log.e("Json Para",String.valueOf(jsonParams));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getAdminTeacherSendMessage
     * */
    public void getAdminTeacherSendMessage(String clt_id,String message_subject,String reply_msg,String usl_id,String toUslId,String filename,String filepath,String methodname,int callType,Response.Listener<JSONObject> listener,
                                           Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("pmg_subject", message_subject);
            jsonParams.put("msg_message", reply_msg);
            jsonParams.put("usl_id", usl_id);
            jsonParams.put("sender_uslId",toUslId);
            jsonParams.put("filename",filename);
            jsonParams.put("filepath", filepath);
            Log.e("AdminTOTeacpara",String.valueOf(jsonParams));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getAdminTeacherSendMessage
     * */
    public void getAdminSchoolSendSMSDirect(String contactList,String clt_id,String msg,String usl_id,String methodname,int callType,Response.Listener<JSONObject> listener,
                                           Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("contactList", contactList);
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("message", msg);
            jsonParams.put("usl_id", usl_id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }


    /**
     * uploadfile
     * */
    public void uploadfile(String clt_id,String usl_id,String base64Value,String methodname,int callType,Response.Listener<JSONObject> listener,
                                            Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("usl_id", usl_id);
            jsonParams.put("SmsCsvFile", base64Value);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * uploadfiletoServer
     * */
    public void uploadfiletoServer(String clt_id,String usl_id,String base64Value,String filename,String extension,String methodname,int callType,Response.Listener<JSONObject> listener,
                           Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("usl_id", usl_id);
            jsonParams.put("SmsCsvFile", base64Value);
            jsonParams.put("fileName", filename);
            jsonParams.put("extesion", extension);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getViewLog
     * */
    public void getViewLog(String crf_id,String usl_id,String rol_id,String clt_id,String academic_year,String methodname,int callType,Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("crf_id", crf_id);
            jsonParams.put("usl_id", usl_id);
            jsonParams.put("rol_id", rol_id);
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("Acy_year", academic_year);
            Log.e("View Log",String.valueOf(jsonParams));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getsaveLog
     * */
    public void getsaveLog(String crf_id,String periodNo,String usl_id,String field1,String field2,String field3,String field4,String field5,String field6,String field7,String field8,String field9,String field10,String field11,String field12,String methodname,int callType,Response.Listener<JSONObject> listener,
                           Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("crf_id", crf_id);
            jsonParams.put("periodno", periodNo);
            jsonParams.put("usl_id", usl_id);
            jsonParams.put("field1", field1);
            jsonParams.put("field2", field2);
            jsonParams.put("field3", field3);
            jsonParams.put("field4", field4);
            jsonParams.put("field5", field5);
            jsonParams.put("field6", field6);
            jsonParams.put("field7", field7);
            jsonParams.put("field8", field8);
            jsonParams.put("field9", field9);
            jsonParams.put("field10", field10);
            jsonParams.put("field11", field11);
            jsonParams.put("field12", field12);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        makeJsonObjReq(Constant.Common_URL+methodname, callType, listener, errlsn, jsonParams);

    }
    /**
     * getReceiverList
     * */
    public void getReceiverList(String pmg_id,String clt_id,String roll_id,String methodname,int callType,Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("pmg_id", pmg_id);
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("rol_id", roll_id);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        makeJsonObjReq(Constant.Common_URL + methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getannaouncementReadStatus
     * */
    public void getannaouncementReadStatus(String msg_id,String announcement_usl_id,String methodname,int callType,Response.Listener<JSONObject> listener,
                                Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("msg_id", msg_id);
            jsonParams.put("usl_id", announcement_usl_id);
            Log.e("Params AnnRead",String.valueOf(jsonParams));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        makeJsonObjReq(Constant.Common_URL + methodname, callType, listener, errlsn, jsonParams);

    }

    /**
     * getViewLogOnPeriodSelection
     * */
    public void getViewLogOnPeriodSelection(String crf_id,String selectedPeriodNo,String usl_id,String academic_year,String clt_id,String rol_id,String methodname,int callType,Response.Listener<JSONObject> listener,
                                           Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("crf_id", crf_id);
            jsonParams.put("PeriodNo", selectedPeriodNo);
            jsonParams.put("usl_id", usl_id);
            jsonParams.put("Acy_year", academic_year);
            jsonParams.put("clt_id", clt_id);
            jsonParams.put("rol_id", rol_id);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        makeJsonObjReq(Constant.Common_URL + methodname, callType, listener, errlsn, jsonParams);

    }


    /**
     * getUpdateFileAccess
     * */
    public void getUpdateFileAccess(String crl_id,String usl_id,String methodname,int callType,Response.Listener<JSONObject> listener,
                                            Response.ErrorListener errlsn){


        JSONObject jsonParams = new JSONObject();

        try
        {
            jsonParams.put("filename", crl_id);
            jsonParams.put("usl_id", usl_id);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        makeJsonObjReq(Constant.Common_URL + methodname, callType, listener, errlsn, jsonParams);

    }
    /**
     * Making json object request
     * */
    public void makeJsonObjReq(String url,int callMethodeType,Response.Listener<JSONObject> listener,
                               Response.ErrorListener errlsn,JSONObject requestMap  ) {


        JsonObjectRequest req = new JsonObjectRequest(callMethodeType,
                url,  requestMap,
                listener, errlsn){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("<<Inside","DataFetchService -> makeJsonObjReq() -> getHeaders()");
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("AuthToken", Constant.GetResult_AuthToken(mcontext));
                return headers;
            }

            @Override
            public String getBodyContentType() {

                return "application/json; charset=utf-8";
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(req);


    }

    public Object GetResponseObject(JSONObject jsonResponse,
                                    Class class1) {
        // TODO Auto-generated method stub
        Object obj=null;
        obj = new Gson().fromJson(jsonResponse.toString(),
                class1);
        return obj;
    }

}
