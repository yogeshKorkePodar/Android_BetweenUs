package com.podarbetweenus.Services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.ResultReceiver;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.podarbetweenus.Activities.AdminMessageActivity;
import com.podarbetweenus.Activities.AdminViewMessage;
import com.podarbetweenus.Activities.AnnouncementActivity;
import com.podarbetweenus.Activities.DashboardActivity;
import com.podarbetweenus.Activities.LoginActivity;
import com.podarbetweenus.Activities.ProfileActivity;
import com.podarbetweenus.Activities.Profile_Sibling;
import com.podarbetweenus.Activities.SiblingActivity;
import com.podarbetweenus.Activities.SplashScreen;
import com.podarbetweenus.Activities.TeacherAnnouncementsActivity;
import com.podarbetweenus.Activities.TeacherMessageActivity;
import com.podarbetweenus.Activities.TeacherProfileActivity;
import com.podarbetweenus.Activities.TeacherViewMessageActivity;
import com.podarbetweenus.Activities.ViewMessageActivity;
import com.podarbetweenus.Entity.AnnouncementResult;
import com.podarbetweenus.Entity.ViewMessageResult;
import com.podarbetweenus.R;
import com.podarbetweenus.Utility.AppController;
import com.podarbetweenus.Utility.BadgeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 11/4/2015.
 */
public class Webservice extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;
    String clt_id,msd_id,usl_id,check="0",pageNo ="1",month,school_name,board_name,academic_yr;
    private static final String TAG = "DownloadService";
    int preSize= 1;
    int new_Sixe,newMessageList,teacherStatusSize,newTeacherMessageList,adminStatusSize,newAdminMessageList,newParentAnnouncementList,newTeacherAnnouncementList,parentAnnouncementStatusSize,teacherAnnouncementStatusSize;
    String loantitle="",RepaymentWay="",LoanPurpose ="",AmountStart ="",AmountEnd = "",sort = "",status = "",BiddingStratTime ="",pageSize="300";
    String currentDateandTime,pmg_file_name,pmg_file_path,pmu_ID,usl_ID,pmu_readunreadstatus,isStudentresource,roll_id,Teachermonth="0",admin_name,msg_Message,msg_date,msg_type,firstAnnouncement;
    String LoginId = "studentneeta@podar.org",Password = "exwom0jh1m3";
    NotificationManager notificationManager;
    PendingIntent pendingIntent;
    String Fullname,pmg_subject,pmg_message,pmg_date,firstmsgreadStatus,firstmsgannouncementreadStatus,firstmsgSub,firstmsgContent,msgStatus,msgUnreadStatus,badge_count,
            behaviourCount,announcrmentCount,teacherMsgstatus,org_id,academic_year,class_teacher,teacher_name,AdminMsgstatus,teacher_div,teacher_std,teacher_shift,
            announcementStatus,msg_id,TeachAnoucementCnt;
    Date cuurdate,msgdate,firstmsgDate;
    int notificationID = 1,msgStatusSize,viewmessageListSize;
    Notification notification;
    ArrayList<String> msgStatusList = new ArrayList<String>();
    ArrayList<String> teachermsgStatusList = new ArrayList<String>();
    ArrayList<String> adminmsgStatusList = new ArrayList<String>();
    ArrayList<String> parentAnnouncementStatusList = new ArrayList<String>();
    ArrayList<String> teacherAnnouncementStatusList = new ArrayList<String>();

    ArrayList<ViewMessageResult> results;
    ArrayList<AnnouncementResult> announcementResults;
    BadgeView badge;

    public static String drwer="";


    public Webservice() {

        super(Webservice.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Calendar now = Calendar.getInstance();
        Date date = now.getTime();
        SimpleDateFormat format = new SimpleDateFormat("MM");
        month = format.format(date);
        Log.e("WEbservice Month", month);

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        roll_id = LoginActivity.get_RollId(this);

        if (AppController.SiblingActivity.equalsIgnoreCase("true") && roll_id.equalsIgnoreCase("6")) {
            clt_id = SiblingActivity.get_cltId(this);
            msd_id = SiblingActivity.get_msdId(this);
            usl_id = SiblingActivity.get_uslId(this);
            board_name = SiblingActivity.get_BoardName(this);
            school_name = SiblingActivity.get_SchoolName(this);
            behaviourCount = Profile_Sibling.get_behaviourCount(this);
            announcrmentCount = Profile_Sibling.get_announcementCount(this);
            isStudentresource = Profile_Sibling.get_isStudentResourcer(this);
            TeachAnoucementCnt = LoginActivity.get_teacher_announcementCount(this);
            AppController.isStudentResourcePresent = isStudentresource;
        } else if (AppController.SiblingActivity.equalsIgnoreCase("false") && roll_id.equalsIgnoreCase("6")) {
            clt_id = LoginActivity.get_cltId(this);
            msd_id = LoginActivity.get_msdId(this);
            usl_id = LoginActivity.get_uslId(this);
            board_name = LoginActivity.get_BoardName(this);
            school_name = LoginActivity.get_SchoolName(this);
            behaviourCount = ProfileActivity.get_behaviourCount(this);
            announcrmentCount = ProfileActivity.get_announcementCount(this);
            isStudentresource = ProfileActivity.get_isStudentResourcer(this);
            TeachAnoucementCnt = LoginActivity.get_teacher_announcementCount(this);
            roll_id = LoginActivity.get_RollId(this);
            Log.e("WS uslidlog", usl_id);
            Log.e("WS cltidlog",clt_id);
            Log.e("WS msdidlog",msd_id);
        }
        else if(roll_id.equalsIgnoreCase("5")){
            clt_id = LoginActivity.get_cltId(this);
            msd_id = LoginActivity.get_msdId(this);
            usl_id = LoginActivity.get_uslId(this);
            board_name = LoginActivity.get_BoardName(this);
            school_name = LoginActivity.get_SchoolName(this);
            roll_id = LoginActivity.get_RollId(this);
            class_teacher = LoginActivity.get_class_teacher(this);
            org_id = LoginActivity.get_org_id(this);
            teacher_name = LoginActivity.get_name(this);
            academic_year = LoginActivity.get_academic_year(this);
            teacher_div = LoginActivity.get_teacher_div(this);
            teacher_std = LoginActivity.get_teacher_std(this);
            teacher_shift = LoginActivity.get_teacher_shift(this);
            TeachAnoucementCnt = LoginActivity.get_teacher_announcementCount(this);
            Log.e("Announc count",TeachAnoucementCnt);
        }
        else {
            clt_id = LoginActivity.get_cltId(this);
            usl_id = LoginActivity.get_uslId(this);
            board_name = LoginActivity.get_BoardName(this);
            school_name = LoginActivity.get_SchoolName(this);
            roll_id = LoginActivity.get_RollId(this);
            org_id = LoginActivity.get_org_id(this);
            admin_name = LoginActivity.get_name(this);
            TeachAnoucementCnt = LoginActivity.get_teacher_announcementCount(this);
            academic_year = LoginActivity.get_academic_year(this);
            msd_id =  LoginActivity.get_msdId(this);
        }

        //String url = "http://115.124.127.250:8021/PodarApp.svc/GetViewMessageData";
        String url = "http://www.betweenus.in/PODARAPP/PodarApp.svc/GetViewMessageData";
        String teacherUrl = "http://www.betweenus.in/PODARAPP/PodarApp.svc/GetSentMessageDataTeacher";
        String adminUrl = "http://www.betweenus.in/PODARAPP/PodarApp.svc/ViewAdminMessageDetails";
        Log.e(TAG, "Service Calling...!");
        Bundle bundle = new Bundle();
        try {
            if (roll_id.equalsIgnoreCase("6")) {
                if (!TextUtils.isEmpty(url)) {
            /* Update UI: Download Service is Running */
                    receiver.send(STATUS_RUNNING, Bundle.EMPTY);

                    try {
                        ArrayList<ViewMessageResult> results = downloadData(url);
                        AppController.results = results;

                /* Sending result back to activity */
                        if (null != results && results.size() > 0) {
                            //    bundle.putStringArrayList("result", results);
                            intent.putExtra("results", results);
                            Intent resultBroadCastIntent = new Intent();
                    /*set action here*/
                            resultBroadCastIntent.setAction(ViewMessageActivity.AlarmReceiver.ACTION_TEXT_CAPITALIZED);
		            /*set intent category as default*/
                            resultBroadCastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    /*add data to intent*/
                            resultBroadCastIntent.putExtra("results", results);
		            /*send broadcast */
                            sendBroadcast(resultBroadCastIntent);
                            receiver.send(STATUS_FINISHED, bundle);
                            newMessageList = results.size();
                            Log.e("Webservice List Size", String.valueOf(newMessageList));

                            if ((AppController.viewMessageListSize != newMessageList) & (firstmsgreadStatus.equalsIgnoreCase("1"))/*& (firstmsgreadStatus.equalsIgnoreCase("1")) & (AppController.viewMessageListSize <= 30)*/) {
                                Context context = this.getApplicationContext();
                                notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                                if (AppController.viewMessageListSize == newMessageList) {
                                    try {
                                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                        r.stop();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else if (AppController.viewMessageListSize != newMessageList && AppController.Notification_send.equalsIgnoreCase("true") && LoginActivity.loggedIn.equalsIgnoreCase("true")) {
                                    try {
                                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                        r.play();
                                        AppController.Notification_send = "false";

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else if (AppController.viewMessageListSize != newMessageList && AppController.Notification_send.equalsIgnoreCase("true") && LoginActivity.loggedIn.equalsIgnoreCase("false")) {
                                    try {
                                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                        r.stop();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (LoginActivity.loggedIn.equalsIgnoreCase("true")) {
                                    Intent notificationIntent = new Intent(this, DashboardActivity.class);
                                    notificationIntent.putExtra("results", results);
                                    notificationIntent.putExtra("clt_id", clt_id);
                                    notificationIntent.putExtra("msd_ID", msd_id);
                                    notificationIntent.putExtra("usl_id", usl_id);
                                    notificationIntent.putExtra("board_name", board_name);
                                    notificationIntent.putExtra("School_name", school_name);
                                    notificationIntent.putExtra("notificationID", notificationID);
                                    notificationIntent.putExtra("version_name", AppController.versionName);
                                    notificationIntent.putExtra("behaviour_count", behaviourCount);
                                    notificationIntent.putExtra("annpuncement_count", announcrmentCount);
                                    notificationIntent.putExtra("isStudentResource", isStudentresource);
                                    Log.e("Stud res dash ws", isStudentresource);

                                    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                    Resources res = this.getResources();

                                    if (AppController.viewMessageListSize != newMessageList) {
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

                                        builder.setContentIntent(pendingIntent)
                                                .setSmallIcon(R.drawable.appiconnew)
                                                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.appiconnew))
                                                .setTicker("New Message")
                                                .setAutoCancel(true)
                                                .setContentTitle(firstmsgSub)
                                                .setContentText(firstmsgContent);
                                        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        notificationManager.notify(notificationID, builder.build());
                                    }
                                } else if (LoginActivity.loggedIn.equalsIgnoreCase("false")) {

                                } else {
                                    Intent notificationIntent = new Intent(this, DashboardActivity.class);
                                    notificationIntent.putExtra("results", results);
                                    notificationIntent.putExtra("clt_id", clt_id);
                                    notificationIntent.putExtra("msd_ID", msd_id);
                                    notificationIntent.putExtra("usl_id", usl_id);
                                    notificationIntent.putExtra("board_name", board_name);
                                    notificationIntent.putExtra("School_name", school_name);
                                    notificationIntent.putExtra("notificationID", notificationID);
                                    notificationIntent.putExtra("version_name", AppController.versionName);
                                    notificationIntent.putExtra("behaviour_count", behaviourCount);
                                    notificationIntent.putExtra("annpuncement_count", announcrmentCount);
                                    notificationIntent.putExtra("isStudentResource", isStudentresource);

                                    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                    Resources res = this.getResources();

                                    if (AppController.viewMessageListSize != newMessageList) {
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

                                        builder.setContentIntent(pendingIntent)
                                                .setSmallIcon(R.drawable.appiconnew)
                                                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.appiconnew))
                                                .setTicker("New Message")
                                                .setAutoCancel(true)
                                                .setContentTitle(firstmsgSub)
                                                .setContentText(firstmsgContent);
                                        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        notificationManager.notify(notificationID, builder.build());
                                    }
                                }

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                /* Sending error message back to activity */
                        bundle.putString(Intent.EXTRA_TEXT, e.toString());
                        receiver.send(STATUS_ERROR, bundle);
                    }

                }
                Log.d(TAG, "Service Stopping!");
                this.stopSelf();
            } else if (roll_id.equalsIgnoreCase("5")) {
                if (!TextUtils.isEmpty("http://www.betweenus.in/PODARAPP/PodarApp.svc/GetSentMessageDataTeacher")) {
            /* Update UI: Download Service is Running */
                    receiver.send(STATUS_RUNNING, Bundle.EMPTY);

                    try {
                        ArrayList<ViewMessageResult> results = downloadTeacherData("http://www.betweenus.in/PODARAPP/PodarApp.svc/GetSentMessageDataTeacher");
                        AppController.results = results;

                /* Sending result back to activity */
                        if (null != results && results.size() > 0) {
                            //    bundle.putStringArrayList("result", results);
                            intent.putExtra("results", results);
                            Intent resultBroadCastIntent = new Intent();
                    /*set action here*/
                            resultBroadCastIntent.setAction(TeacherViewMessageActivity.AlarmReceiver.ACTION_TEXT_CAPITALIZED);
		            /*set intent category as default*/
                            resultBroadCastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    /*add data to intent*/
                            resultBroadCastIntent.putExtra("results", results);
		            /*send broadcast */
                            sendBroadcast(resultBroadCastIntent);
                            receiver.send(STATUS_FINISHED, bundle);
                            newTeacherMessageList = results.size();
                            Log.e("Teacher Ws List Size", String.valueOf(newTeacherMessageList));

                            if ((AppController.viewTeacherMessageListSize != newTeacherMessageList) & (firstmsgreadStatus.equalsIgnoreCase("1"))/*& (firstmsgreadStatus.equalsIgnoreCase("1")) & (AppController.viewMessageListSize <= 30)*/) {
                                Context context = this.getApplicationContext();
                                notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                                if (AppController.viewTeacherMessageListSize == newTeacherMessageList) {
                                    try {
                                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                        r.stop();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else if (AppController.viewTeacherMessageListSize != newTeacherMessageList && AppController.Notification_send.equalsIgnoreCase("true") && LoginActivity.loggedIn.equalsIgnoreCase("true")) {
                                    try {
                                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                        r.play();
                                        AppController.Notification_send = "false";

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else if (AppController.viewTeacherMessageListSize != newTeacherMessageList && AppController.Notification_send.equalsIgnoreCase("true") && LoginActivity.loggedIn.equalsIgnoreCase("false")) {
                                    try {
                                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                        r.stop();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (LoginActivity.loggedIn.equalsIgnoreCase("true")) {
                                    Intent notificationIntent = new Intent(this, TeacherMessageActivity.class);
                                    notificationIntent.putExtra("results", results);
                                    notificationIntent.putExtra("clt_id", clt_id);
                                    notificationIntent.putExtra("msd_ID", msd_id);
                                    notificationIntent.putExtra("usl_id", usl_id);
                                    notificationIntent.putExtra("board_name", board_name);
                                    notificationIntent.putExtra("School_name", school_name);
                                    notificationIntent.putExtra("notificationID", notificationID);
                                    notificationIntent.putExtra("version_name", AppController.versionName);
                                    notificationIntent.putExtra("org_id",org_id);
                                    notificationIntent.putExtra("Teacher_div",teacher_div);
                                    notificationIntent.putExtra("Teacher_Shift",teacher_shift);
                                    notificationIntent.putExtra("Tecaher_Std",teacher_std);
                                    notificationIntent.putExtra("Teacher_name",teacher_name);
                                    notificationIntent.putExtra("academic_year",academic_year);
                                    notificationIntent.putExtra("cls_teacher",class_teacher);
                                 //   Log.e("Stud res dash ws", isStudentresource);

                                    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                    Resources res = this.getResources();

                                    if (AppController.viewTeacherMessageListSize != newTeacherMessageList) {
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

                                        builder.setContentIntent(pendingIntent)
                                                .setSmallIcon(R.drawable.appiconnew)
                                                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.appiconnew))
                                                .setTicker("New Message")
                                                .setAutoCancel(true)
                                                .setContentTitle(firstmsgSub)
                                                .setContentText(firstmsgContent);
                                        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        notificationManager.notify(notificationID, builder.build());
                                    }
                                } else if (LoginActivity.loggedIn.equalsIgnoreCase("false")) {

                                } else {
                                    Intent notificationIntent = new Intent(this, TeacherMessageActivity.class);
                                    notificationIntent.putExtra("results", results);
                                    notificationIntent.putExtra("clt_id", clt_id);
                                    notificationIntent.putExtra("msd_ID", msd_id);
                                    notificationIntent.putExtra("usl_id", usl_id);
                                    notificationIntent.putExtra("board_name", board_name);
                                    notificationIntent.putExtra("School_name", school_name);
                                    notificationIntent.putExtra("notificationID", notificationID);
                                    notificationIntent.putExtra("org_id",org_id);
                                    notificationIntent.putExtra("Teacher_div",teacher_div);
                                    notificationIntent.putExtra("Teacher_Shift",teacher_shift);
                                    notificationIntent.putExtra("Tecaher_Std",teacher_std);
                                    notificationIntent.putExtra("academic_year",academic_year);
                                    notificationIntent.putExtra("cls_teacher",class_teacher);
                                    notificationIntent.putExtra("Teacher_name",teacher_name);
                                    notificationIntent.putExtra("version_name", AppController.versionName);

                                    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                    Resources res = this.getResources();

                                    if (AppController.viewTeacherMessageListSize != newTeacherMessageList) {
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

                                        builder.setContentIntent(pendingIntent)
                                                .setSmallIcon(R.drawable.appiconnew)
                                                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.appiconnew))
                                                .setTicker("New Message")
                                                .setAutoCancel(true)
                                                .setContentTitle(firstmsgSub)
                                                .setContentText(firstmsgContent);
                                        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        notificationManager.notify(notificationID, builder.build());

                                    }
                                }

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                /* Sending error message back to activity */
                        bundle.putString(Intent.EXTRA_TEXT, e.toString());
                        receiver.send(STATUS_ERROR, bundle);
                    }

                }
                Log.d(TAG, "Service Stopping!");
                this.stopSelf();
            }
            else if(roll_id.equalsIgnoreCase("2"))
            {
                if (!TextUtils.isEmpty("http://www.betweenus.in/PODARAPP/PodarApp.svc/ViewAdminMessageDetails")) {
            /* Update UI: Download Service is Running */
                    receiver.send(STATUS_RUNNING, Bundle.EMPTY);

                    try {
                        ArrayList<ViewMessageResult> results = downloadAdminData("http://www.betweenus.in/PODARAPP/PodarApp.svc/ViewAdminMessageDetails");
                        AppController.results = results;

                /* Sending result back to activity */
                        if (null != results && results.size() > 0) {
                            //    bundle.putStringArrayList("result", results);
                            intent.putExtra("results", results);
                            Intent resultBroadCastIntent = new Intent();
                    /*set action here*/
                            resultBroadCastIntent.setAction(AdminViewMessage.AlarmReceiver.ACTION_TEXT_CAPITALIZED);
		            /*set intent category as default*/
                            resultBroadCastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    /*add data to intent*/
                            resultBroadCastIntent.putExtra("results", results);
		            /*send broadcast */
                            sendBroadcast(resultBroadCastIntent);
                            receiver.send(STATUS_FINISHED, bundle);
                            newAdminMessageList = results.size();
                            Log.e("Admin Ws List Size", String.valueOf(newAdminMessageList));

                            if ((AppController.viewAdminMessageListSize != newAdminMessageList) & (firstmsgreadStatus.equalsIgnoreCase("1"))/*& (firstmsgreadStatus.equalsIgnoreCase("1")) & (AppController.viewMessageListSize <= 30)*/) {
                                Context context = this.getApplicationContext();
                                notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                                if (AppController.viewAdminMessageListSize == newAdminMessageList) {
                                    try {
                                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                        r.stop();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else if (AppController.viewAdminMessageListSize != newAdminMessageList && AppController.Notification_send.equalsIgnoreCase("true") && LoginActivity.loggedIn.equalsIgnoreCase("true")) {
                                    try {
                                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                        r.play();
                                        AppController.Notification_send = "false";

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else if (AppController.viewAdminMessageListSize != newAdminMessageList && AppController.Notification_send.equalsIgnoreCase("true") && LoginActivity.loggedIn.equalsIgnoreCase("false")) {
                                    try {
                                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                        r.stop();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (LoginActivity.loggedIn.equalsIgnoreCase("true")) {
                                    Intent notificationIntent = new Intent(this, AdminMessageActivity.class);
                                    AppController.AdminWriteMessage = "false";
                                    AppController.AdminSentMessage = "false";
                                    notificationIntent.putExtra("results", results);
                                    notificationIntent.putExtra("clt_id", clt_id);
                                    notificationIntent.putExtra("msd_ID", msd_id);
                                    notificationIntent.putExtra("usl_id", usl_id);
                                    notificationIntent.putExtra("board_name", board_name);
                                    notificationIntent.putExtra("School_name", school_name);
                                    notificationIntent.putExtra("notificationID", notificationID);
                                    notificationIntent.putExtra("version_name", AppController.versionName);
                                    notificationIntent.putExtra("academic_year",academic_year);
                                    notificationIntent.putExtra("org_id",org_id);
                                    notificationIntent.putExtra("Admin_name",admin_name);
                                    //   Log.e("Stud res dash ws", isStudentresource);

                                    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                    Resources res = this.getResources();

                                    if (AppController.viewAdminMessageListSize != newAdminMessageList) {
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

                                        builder.setContentIntent(pendingIntent)
                                                .setSmallIcon(R.drawable.appiconnew)
                                                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.appiconnew))
                                                .setTicker("New Message")
                                                .setAutoCancel(true)
                                                .setContentTitle(firstmsgSub)
                                                .setContentText(firstmsgContent);
                                        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        notificationManager.notify(notificationID, builder.build());
                                    }
                                } else if (LoginActivity.loggedIn.equalsIgnoreCase("false")) {

                                } else {
                                    Intent notificationIntent = new Intent(this, AdminMessageActivity.class);
                                    notificationIntent.putExtra("results", results);
                                    notificationIntent.putExtra("clt_id", clt_id);
                                    notificationIntent.putExtra("msd_ID", msd_id);
                                    notificationIntent.putExtra("usl_id", usl_id);
                                    notificationIntent.putExtra("board_name", board_name);
                                    notificationIntent.putExtra("School_name", school_name);
                                    notificationIntent.putExtra("notificationID", notificationID);
                                    notificationIntent.putExtra("org_id",org_id);
                                    notificationIntent.putExtra("academic_year",academic_year);
                                    notificationIntent.putExtra("Admin_name",admin_name);
                                    notificationIntent.putExtra("version_name", AppController.versionName);

                                    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                    Resources res = this.getResources();

                                    if (AppController.viewTeacherMessageListSize != newTeacherMessageList) {
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

                                        builder.setContentIntent(pendingIntent)
                                                .setSmallIcon(R.drawable.appiconnew)
                                                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.appiconnew))
                                                .setTicker("New Message")
                                                .setAutoCancel(true)
                                                .setContentTitle(firstmsgSub)
                                                .setContentText(firstmsgContent);
                                        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        notificationManager.notify(notificationID, builder.build());

                                    }
                                }

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                /* Sending error message back to activity */
                        bundle.putString(Intent.EXTRA_TEXT, e.toString());
                        receiver.send(STATUS_ERROR, bundle);
                    }

                }
                Log.d(TAG, "Service Stopping!");
                this.stopSelf();
            }
            if(!TeachAnoucementCnt.equalsIgnoreCase("0") && !TeachAnoucementCnt.equalsIgnoreCase("")){
                if(!TextUtils.isEmpty("http://www.betweenus.in/PODARAPP/PodarApp.svc/GetTeacherAnnouncement")) {
                    receiver.send(STATUS_RUNNING, Bundle.EMPTY);
                    try{
                        ArrayList<AnnouncementResult> announcementResults = downloadTeacherAnnouncementData("http://www.betweenus.in/PODARAPP/PodarApp.svc/GetTeacherAnnouncement");
                        AppController.announcement_result = announcementResults;
                        // Sending result back to activity
                        if (null != announcementResults && announcementResults.size() > 0) {
                            {
                                //bundle.putStringArrayList("result", results);
                                intent.putExtra("results", announcementResults);
                                Intent resultBroadCastIntent = new Intent();
                                //set action here
                                resultBroadCastIntent.setAction(AnnouncementActivity.AlarmReceiver.ACTION_TEXT_CAPITALIZED);
                               //set intent category as default
                                resultBroadCastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                                //add data to intent
                                resultBroadCastIntent.putExtra("results", announcementResults);
                                //send broadcast
                                sendBroadcast(resultBroadCastIntent);
                                receiver.send(STATUS_FINISHED, bundle);
                                newTeacherAnnouncementList = announcementResults.size();

                                Log.e("Ws teachAnuncList Size", String.valueOf(newTeacherAnnouncementList));
                                if ((AppController.viewTeacherAnnouncementListSize != newTeacherAnnouncementList) && (firstmsgannouncementreadStatus.equalsIgnoreCase("1"))){
                                    {
                                        Context context = this.getApplicationContext();
                                        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                                        if (AppController.viewTeacherAnnouncementListSize == newTeacherAnnouncementList) {
                                            try {
                                                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                                r.stop();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        } else if (AppController.viewTeacherAnnouncementListSize != newTeacherAnnouncementList && AppController.Notification_send.equalsIgnoreCase("true") && LoginActivity.loggedIn.equalsIgnoreCase("true")) {
                                            try {
                                                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                                r.play();
                                                AppController.Notification_send = "false";

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        } else if (AppController.viewTeacherAnnouncementListSize != newTeacherAnnouncementList && AppController.Notification_send.equalsIgnoreCase("true") && LoginActivity.loggedIn.equalsIgnoreCase("false")) {
                                            try {
                                                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                                r.stop();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        if (LoginActivity.loggedIn.equalsIgnoreCase("true")) {
                                            Intent notificationIntent = new Intent(this, TeacherAnnouncementsActivity.class);
                                            usl_id = LoginActivity.get_uslId(this);
                                            notificationIntent.putExtra("results", announcementResults);
                                            notificationIntent.putExtra("clt_id", clt_id);
                                            notificationIntent.putExtra("msd_ID", msd_id);
                                            notificationIntent.putExtra("usl_id", usl_id);
                                            notificationIntent.putExtra("board_name", board_name);
                                            notificationIntent.putExtra("School_name", school_name);
                                            notificationIntent.putExtra("notificationID", notificationID);
                                            notificationIntent.putExtra("org_id", org_id);
                                            notificationIntent.putExtra("Teacher_div", teacher_div);
                                            notificationIntent.putExtra("Teacher_Shift", teacher_shift);
                                            notificationIntent.putExtra("Tecaher_Std", teacher_std);
                                            notificationIntent.putExtra("academic_year", academic_year);
                                            notificationIntent.putExtra("cls_teacher", class_teacher);
                                            notificationIntent.putExtra("Teacher_name", teacher_name);
                                            notificationIntent.putExtra("version_name", AppController.versionName);
                                            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                            Resources res = this.getResources();

                                            if (AppController.viewTeacherAnnouncementListSize != newTeacherAnnouncementList) {
                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

                                                builder.setContentIntent(pendingIntent)
                                                        .setSmallIcon(R.drawable.appiconnew)
                                                        .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.appiconnew))
                                                        .setTicker("New Message")
                                                        .setAutoCancel(true)
                                                        .setContentTitle(firstAnnouncement);

                                                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                notificationManager.notify(notificationID, builder.build());
                                            }
                                        } else if (LoginActivity.loggedIn.equalsIgnoreCase("false")) {

                                        } else {
                                            Intent notificationIntent = new Intent(this, TeacherAnnouncementsActivity.class);
                                            notificationIntent.putExtra("results", announcementResults);
                                            usl_id = LoginActivity.get_uslId(this);
                                            notificationIntent.putExtra("results", results);
                                            notificationIntent.putExtra("clt_id", clt_id);
                                            notificationIntent.putExtra("msd_ID", msd_id);
                                            notificationIntent.putExtra("usl_id", usl_id);
                                            notificationIntent.putExtra("board_name", board_name);
                                            notificationIntent.putExtra("School_name", school_name);
                                            notificationIntent.putExtra("notificationID", notificationID);
                                            notificationIntent.putExtra("org_id", org_id);
                                            notificationIntent.putExtra("Teacher_div", teacher_div);
                                            notificationIntent.putExtra("Teacher_Shift", teacher_shift);
                                            notificationIntent.putExtra("Tecaher_Std", teacher_std);
                                            notificationIntent.putExtra("academic_year", academic_year);
                                            notificationIntent.putExtra("cls_teacher", class_teacher);
                                            notificationIntent.putExtra("Teacher_name", teacher_name);
                                            notificationIntent.putExtra("version_name", AppController.versionName);
                                            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                            Resources res = this.getResources();

                                            if (AppController.viewTeacherAnnouncementListSize != newTeacherAnnouncementList) {
                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

                                                builder.setContentIntent(pendingIntent)
                                                        .setSmallIcon(R.drawable.appiconnew)
                                                        .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.appiconnew))
                                                        .setTicker("New Message")
                                                        .setAutoCancel(true)
                                                        .setContentTitle(firstAnnouncement);
                                                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                notificationManager.notify(notificationID, builder.build());
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        // Sending error message back to activity
                        bundle.putString(Intent.EXTRA_TEXT, e.toString());
                        receiver.send(STATUS_ERROR, bundle);
                    }
                }
                Log.d(TAG, "Service Stopping!");
                this.stopSelf();
            }
           else if(!announcrmentCount.equalsIgnoreCase("0") && roll_id.equalsIgnoreCase("6")){
                if(!TextUtils.isEmpty("http://www.betweenus.in/PODARAPP/PodarApp.svc/GetAnnouncementData")){
                    receiver.send(STATUS_RUNNING, Bundle.EMPTY);
                    try {
                        ArrayList<AnnouncementResult> announcementResults = downloadParentData("http://www.betweenus.in/PODARAPP/PodarApp.svc/GetAnnouncementData");
                        AppController.announcement_result = announcementResults;
                        //* Sending result back to activity *//*
                        if (null != announcementResults && announcementResults.size() > 0) {
                            //    bundle.putStringArrayList("result", results);
                            intent.putExtra("results", announcementResults);
                            Intent resultBroadCastIntent = new Intent();
                            //*set action here*//*
                            resultBroadCastIntent.setAction(AnnouncementActivity.AlarmReceiver.ACTION_TEXT_CAPITALIZED);
                            //*set intent category as default*//*
                            resultBroadCastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                            //*add data to intent*//*
                            resultBroadCastIntent.putExtra("results", announcementResults);
                            //*send broadcast *//*
                            sendBroadcast(resultBroadCastIntent);
                            receiver.send(STATUS_FINISHED, bundle);
                            newParentAnnouncementList = announcementResults.size();

                            Log.e("Ws AnnouncemntList Size", String.valueOf(newParentAnnouncementList));
                            if ((AppController.viewAnnouncementListSize != newParentAnnouncementList) && (firstmsgannouncementreadStatus.equalsIgnoreCase("1"))){//*& (firstmsgreadStatus.equalsIgnoreCase("1")) & (AppController.viewMessageListSize <= 30)*//*) {
                                Context context = this.getApplicationContext();
                                notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                                if (AppController.viewAnnouncementListSize == newParentAnnouncementList) {
                                    try {
                                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                        r.stop();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else if (AppController.viewAnnouncementListSize != newParentAnnouncementList && AppController.Notification_send.equalsIgnoreCase("true") && LoginActivity.loggedIn.equalsIgnoreCase("true")) {
                                    try {
                                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                        r.play();
                                        AppController.Notification_send = "false";

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else if (AppController.viewAnnouncementListSize != newParentAnnouncementList && AppController.Notification_send.equalsIgnoreCase("true") && LoginActivity.loggedIn.equalsIgnoreCase("false")) {
                                    try {
                                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                        r.stop();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (LoginActivity.loggedIn.equalsIgnoreCase("true")) {
                                    Intent notificationIntent = new Intent(this, AnnouncementActivity.class);
                                    usl_id = LoginActivity.get_uslId(this);
                                    notificationIntent.putExtra("results", announcementResults);
                                    notificationIntent.putExtra("clt_id", clt_id);
                                    notificationIntent.putExtra("msd_ID", msd_id);
                                    notificationIntent.putExtra("usl_id", usl_id);
                                    notificationIntent.putExtra("board_name", board_name);
                                    notificationIntent.putExtra("School_name", school_name);
                                    notificationIntent.putExtra("notificationID", notificationID);
                                    notificationIntent.putExtra("version_name", AppController.versionName);
                                    notificationIntent.putExtra("behaviour_count", behaviourCount);
                                    notificationIntent.putExtra("annpuncement_count", announcrmentCount);
                                    notificationIntent.putExtra("isStudentResource", isStudentresource);
                                    Log.e("Stud res dash ws", isStudentresource);
                                    Log.e("WS uslid", usl_id);
                                    Log.e("WS cltid",clt_id);
                                    Log.e("WS msdid",msd_id);

                                    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                    Resources res = this.getResources();

                                    if (AppController.viewAnnouncementListSize != newParentAnnouncementList) {
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

                                        builder.setContentIntent(pendingIntent)
                                                .setSmallIcon(R.drawable.appiconnew)
                                                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.appiconnew))
                                                .setTicker("New Message")
                                                .setAutoCancel(true)
                                                .setContentTitle(firstAnnouncement);

                                        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        notificationManager.notify(notificationID, builder.build());
                                    }
                                } else if (LoginActivity.loggedIn.equalsIgnoreCase("false")) {

                                } else {
                                    Intent notificationIntent = new Intent(this, AnnouncementActivity.class);
                                    notificationIntent.putExtra("results", announcementResults);
                                    usl_id = LoginActivity.get_uslId(this);
                                    notificationIntent.putExtra("clt_id", clt_id);
                                    notificationIntent.putExtra("msd_ID", msd_id);
                                    notificationIntent.putExtra("usl_id", usl_id);
                                    notificationIntent.putExtra("board_name", board_name);
                                    notificationIntent.putExtra("School_name", school_name);
                                    notificationIntent.putExtra("notificationID", notificationID);
                                    notificationIntent.putExtra("version_name", AppController.versionName);
                                    notificationIntent.putExtra("behaviour_count", behaviourCount);
                                    notificationIntent.putExtra("annpuncement_count", announcrmentCount);
                                    notificationIntent.putExtra("isStudentResource", isStudentresource);

                                    Log.e("WS uslid", usl_id);
                                    Log.e("WS cltid", clt_id);
                                    Log.e("WS msdid", msd_id);
                                    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                    Resources res = this.getResources();

                                    if (AppController.viewAnnouncementListSize != newParentAnnouncementList) {
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

                                        builder.setContentIntent(pendingIntent)
                                                .setSmallIcon(R.drawable.appiconnew)
                                                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.appiconnew))
                                                .setTicker("New Message")
                                                .setAutoCancel(true)
                                                .setContentTitle(firstmsgSub)
                                                .setContentText(firstmsgContent);
                                        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        notificationManager.notify(notificationID, builder.build());
                                    }
                                }
                            }
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        //* Sending error message back to activity *//*
                        bundle.putString(Intent.EXTRA_TEXT, e.toString());
                        receiver.send(STATUS_ERROR, bundle);
                    }
                }
                Log.d(TAG, "Service Stopping!");
                this.stopSelf();
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private ArrayList<AnnouncementResult> downloadTeacherAnnouncementData(String requestedTeacherAnnouncementUrl) throws IOException,DownloadException{
        String data = "{"+"\"clt_id\""+":"+clt_id+","+"\"usl_id\""+":"+usl_id+"}";
        InputStream inputStream = null;

        HttpURLConnection urlConnection = null;

        /* forming th java.net.URL object */
        URL url = new URL(requestedTeacherAnnouncementUrl);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
        urlConnection.setRequestProperty("Accept", "*/*");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setRequestProperty("Content-Type", "application/json");

        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setInstanceFollowRedirects(false);
        urlConnection.connect();
        Writer writer = new OutputStreamWriter(urlConnection.getOutputStream());

        writer.write(data);
        writer.flush();
        writer.close();
        //Get Response

        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        BufferedReader rd = new BufferedReader(new InputStreamReader(in));
        int statusCode = urlConnection.getResponseCode();


        /* 200 represents HTTP OK */
        if (statusCode == 200) {
            inputStream = new BufferedInputStream(urlConnection.getInputStream());

            String response  = convertInputStreamToString(inputStream);

            ArrayList<AnnouncementResult> announcementResults = parseTeacherAnnouncementResult(response);

            return announcementResults;
        } else {
            throw new DownloadException("Failed to fetch data!!");
        }
    }

    private ArrayList<AnnouncementResult> parseTeacherAnnouncementResult(String announcementResult) {
        ArrayList<AnnouncementResult> blogTitles = new ArrayList<AnnouncementResult>();
        ArrayList<String> blog = new ArrayList<String>();
        try {
            JSONObject response = new JSONObject(announcementResult);

            JSONArray LoanList = response.optJSONArray("AnnouncementResult");
            int size = LoanList.length();
            for (int i = 0; i < LoanList.length(); i++) {
                JSONObject post = LoanList.optJSONObject(i);
                clt_id = post.optString("Clt_id");
                msg_id = post.optString("msg_ID");
                msg_Message = post.optString("msg_Message");
                msg_date = post.optString("msg_date");
                msg_type = post.optString("msg_type");
                usl_id = post.optString("usl_id");
                pmu_readunreadstatus = post.optString("pmu_readunreadstatus");
                announcementStatus  = LoanList.getJSONObject(i).getString("pmu_readunreadstatus");
                if(announcementStatus.equalsIgnoreCase("1"))
                {
                    teacherAnnouncementStatusList.add(announcementStatus );
                    teacherAnnouncementStatusSize  = teacherAnnouncementStatusList.size();
                }
                AppController.unreadAnnouncementList = teacherAnnouncementStatusList.size();

                blogTitles.add ( new AnnouncementResult (clt_id,msg_id,msg_Message,msg_date,msg_type,usl_id,pmu_readunreadstatus));

            }
            firstmsgannouncementreadStatus = LoanList.getJSONObject(0).getString("pmu_readunreadstatus");
            firstAnnouncement = LoanList.getJSONObject(0).getString("msg_Message");
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            try {
                msgdate = format.parse(pmg_date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Calendar c = Calendar.getInstance();
            int date = c.get(Calendar.DATE);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            currentDateandTime = sdf.format(new Date());
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            try {
                cuurdate = format1.parse(currentDateandTime);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return blogTitles;
    }

    private ArrayList<AnnouncementResult> downloadParentData(String requestedParentAnnouncementUrl) throws IOException,DownloadException{

        String data = "{"+"\"clt_id\""+":"+clt_id+","+"\"usl_id\""+":"+usl_id+","+"\"msd_id\""+":"+msd_id+"}";
        InputStream inputStream = null;

        HttpURLConnection urlConnection = null;

        /* forming th java.net.URL object */
        URL url = new URL(requestedParentAnnouncementUrl);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
        urlConnection.setRequestProperty("Accept", "*/*");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setRequestProperty("Content-Type", "application/json");

        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setInstanceFollowRedirects(false);
        urlConnection.connect();
        Writer writer = new OutputStreamWriter(urlConnection.getOutputStream());

        writer.write(data);
        writer.flush();
        writer.close();
        //Get Response

        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        BufferedReader rd = new BufferedReader(new InputStreamReader(in));
        int statusCode = urlConnection.getResponseCode();


        /* 200 represents HTTP OK */
        if (statusCode == 200) {
            inputStream = new BufferedInputStream(urlConnection.getInputStream());

            String response  = convertInputStreamToString(inputStream);

            ArrayList<AnnouncementResult> announcementResults = parseParentAnnouncementResult(response);

            return announcementResults;
        } else {
            throw new DownloadException("Failed to fetch data!!");
        }
    }

    private ArrayList<AnnouncementResult> parseParentAnnouncementResult(String announcementResult) {

        ArrayList<AnnouncementResult> blogTitles = new ArrayList<AnnouncementResult>();
        ArrayList<String> blog = new ArrayList<String>();
        try {
            JSONObject response = new JSONObject(announcementResult);

            JSONArray LoanList = response.optJSONArray("AnnouncementResult");
            int size = LoanList.length();
            for (int i = 0; i < LoanList.length(); i++) {
                JSONObject post = LoanList.optJSONObject(i);
                clt_id = post.optString("Clt_id");
                msg_id = post.optString("msg_ID");
                msg_Message = post.optString("msg_Message");
                msg_date = post.optString("msg_date");
                msg_type = post.optString("msg_type");
                usl_id = post.optString("usl_id");
                pmu_readunreadstatus = post.optString("pmu_readunreadstatus");
                announcementStatus  = LoanList.getJSONObject(i).getString("pmu_readunreadstatus");
                if(announcementStatus.equalsIgnoreCase("1"))
                {
                    parentAnnouncementStatusList.add(announcementStatus );
                    parentAnnouncementStatusSize  = parentAnnouncementStatusList.size();
                }
                AppController.unreadAnnouncementList = parentAnnouncementStatusList.size();

                blogTitles.add ( new AnnouncementResult (clt_id,msg_id,msg_Message,msg_date,msg_type,usl_id,pmu_readunreadstatus));

            }
            firstmsgannouncementreadStatus = LoanList.getJSONObject(0).getString("pmu_readunreadstatus");
            firstAnnouncement = LoanList.getJSONObject(0).getString("msg_Message");
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            try {
                msgdate = format.parse(pmg_date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Calendar c = Calendar.getInstance();
            int date = c.get(Calendar.DATE);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            currentDateandTime = sdf.format(new Date());
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            try {
                cuurdate = format1.parse(currentDateandTime);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return blogTitles;
    }

    //Admin
    private ArrayList<ViewMessageResult> downloadAdminData(String requestedAdminUrl) throws IOException,DownloadException{

        String data = "{"+"\"clt_id\""+":"+clt_id+","+"\"usl_id\""+":"+usl_id+","+"\"check\""+":"+check+","+"\"PageNo\""+":"+pageNo+","+"\"PageSize\""+":"+pageSize+"}";
        InputStream inputStream = null;

        HttpURLConnection urlConnection = null;

        /* forming th java.net.URL object */
        URL url = new URL(requestedAdminUrl);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
        urlConnection.setRequestProperty("Accept", "*/*");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setRequestProperty("Content-Type", "application/json");

        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setInstanceFollowRedirects(false);
        urlConnection.connect();
        Writer writer = new OutputStreamWriter(urlConnection.getOutputStream());

        writer.write(data);
        writer.flush();
        writer.close();
        //Get Response

        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        BufferedReader rd = new BufferedReader(new InputStreamReader(in));
        int statusCode = urlConnection.getResponseCode();


        /* 200 represents HTTP OK */
        if (statusCode == 200) {
            inputStream = new BufferedInputStream(urlConnection.getInputStream());

            String response  = convertInputStreamToString(inputStream);

            ArrayList<ViewMessageResult> results = parseAdminResult(response);

            return results;
        } else {
            throw new DownloadException("Failed to fetch data!!");
        }
    }

    private ArrayList<ViewMessageResult> parseAdminResult(String result) {
        ArrayList<ViewMessageResult> blogTitles = new ArrayList<ViewMessageResult>();
        ArrayList<String> blog = new ArrayList<String>();
        try {
            JSONObject response = new JSONObject(result);

            JSONArray LoanList = response.optJSONArray("ViewMessageResult");
            int size = LoanList.length();
            for (int i = 0; i < LoanList.length(); i++) {
                JSONObject post = LoanList.optJSONObject(i);
                Fullname = post.optString("Fullname");
                pmg_subject = post.optString("pmg_subject");
                pmg_message = post.optString("pmg_Message");
                pmg_date = post.optString("pmg_date");
                pmg_file_name = post.optString("pmg_file_name");
                pmg_file_path = post.optString("pmg_file_path");
                pmu_ID = post.optString("pmu_ID");
                usl_ID = post.optString("usl_ID");
                pmu_readunreadstatus = post.optString("pmu_readunreadstatus");
                AdminMsgstatus  = LoanList.getJSONObject(i).getString("pmu_readunreadstatus");
                if(AdminMsgstatus.equalsIgnoreCase("1"))
                {
                    adminmsgStatusList.add(AdminMsgstatus );
                    adminStatusSize  = adminmsgStatusList.size();
                }
                AppController.unreadMesList = adminmsgStatusList.size();

                blogTitles.add ( new ViewMessageResult (Fullname,pmg_message,pmg_date,pmg_file_name,pmg_file_path,pmg_subject,pmu_ID,usl_ID,pmu_readunreadstatus));

            }
            firstmsgreadStatus = LoanList.getJSONObject(0).getString("pmu_readunreadstatus");
            firstmsgSub = LoanList.getJSONObject(0).getString("pmg_subject");
            firstmsgContent = LoanList.getJSONObject(0).getString("pmg_Message");
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            try {
                msgdate = format.parse(pmg_date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Calendar c = Calendar.getInstance();
            int date = c.get(Calendar.DATE);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            currentDateandTime = sdf.format(new Date());
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            try {
                cuurdate = format1.parse(currentDateandTime);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return blogTitles;
    }

    //Teacher
    private ArrayList<ViewMessageResult> downloadTeacherData(String requestedTeacherUrl) throws IOException,DownloadException{

        String data = "{"+"\"clt_id\""+":"+clt_id+","+"\"usl_id\""+":"+usl_id+","+"\"month\""+":"+Teachermonth+","+"\"check\""+":"+check+","+"\"PageNo\""+":"+pageNo+","+"\"PageSize\""+":"+pageSize+"}";
        InputStream inputStream = null;

        HttpURLConnection urlConnection = null;

        /* forming th java.net.URL object */
        URL url = new URL(requestedTeacherUrl);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
        urlConnection.setRequestProperty("Accept", "*/*");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setRequestProperty("Content-Type", "application/json");

        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setInstanceFollowRedirects(false);
        urlConnection.connect();
        Writer writer = new OutputStreamWriter(urlConnection.getOutputStream());

        writer.write(data);
        writer.flush();
        writer.close();
        //Get Response

        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        BufferedReader rd = new BufferedReader(new InputStreamReader(in));
        int statusCode = urlConnection.getResponseCode();


        /* 200 represents HTTP OK */
        if (statusCode == 200) {
            inputStream = new BufferedInputStream(urlConnection.getInputStream());

            String response  = convertInputStreamToString(inputStream);

            ArrayList<ViewMessageResult> results = parseTeacherResult(response);

            return results;
        } else {
            throw new DownloadException("Failed to fetch data!!");
        }
    }
    //Parent
    private ArrayList<ViewMessageResult> parseTeacherResult(String result) {

        ArrayList<ViewMessageResult> blogTitles = new ArrayList<ViewMessageResult>();
        ArrayList<String> blog = new ArrayList<String>();
        try {
            JSONObject response = new JSONObject(result);

            JSONArray LoanList = response.optJSONArray("ViewMessageResult");
            int size = LoanList.length();
            for (int i = 0; i < LoanList.length(); i++) {
                JSONObject post = LoanList.optJSONObject(i);
                Fullname = post.optString("Fullname");
                pmg_subject = post.optString("pmg_subject");
                pmg_message = post.optString("pmg_Message");
                pmg_date = post.optString("pmg_date");
                pmg_file_name = post.optString("pmg_file_name");
                pmg_file_path = post.optString("pmg_file_path");
                pmu_ID = post.optString("pmu_ID");
                usl_ID = post.optString("usl_ID");
                pmu_readunreadstatus = post.optString("pmu_readunreadstatus");
                teacherMsgstatus  = LoanList.getJSONObject(i).getString("pmu_readunreadstatus");
                if(teacherMsgstatus.equalsIgnoreCase("1"))
                {
                    teachermsgStatusList.add(teacherMsgstatus );
                    teacherStatusSize  = teachermsgStatusList.size();
                }

                // Log.e("UnreadSize", String.valueOf(msgStatusSize));
                AppController.unreadMesList = teachermsgStatusList.size();

                blogTitles.add ( new ViewMessageResult (Fullname,pmg_message,pmg_date,pmg_file_name,pmg_file_path,pmg_subject,pmu_ID,usl_ID,pmu_readunreadstatus));

            }
            firstmsgreadStatus = LoanList.getJSONObject(0).getString("pmu_readunreadstatus");
            firstmsgSub = LoanList.getJSONObject(0).getString("pmg_subject");
            firstmsgContent = LoanList.getJSONObject(0).getString("pmg_Message");
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            try {
                msgdate = format.parse(pmg_date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Calendar c = Calendar.getInstance();
            int date = c.get(Calendar.DATE);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            currentDateandTime = sdf.format(new Date());
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            try {
                cuurdate = format1.parse(currentDateandTime);
                //  System.out.println("Curr Date"+cuurdate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return blogTitles;
    }

    private ArrayList<ViewMessageResult> downloadData(String requestUrl) throws IOException, DownloadException {

        String data = "{"+"\"clt_id\""+":"+clt_id+","+"\"usl_id\""+":"+usl_id+","+"\"msd_id\""+":"+msd_id+","+"\"month\""+":"+month+","+"\"check\""+":"+check+","+"\"PageNo\""+":"+pageNo+","+"\"PageSize\""+":"+pageSize+"}";

        InputStream inputStream = null;

        HttpURLConnection urlConnection = null;

        /* forming th java.net.URL object */
        URL url = new URL(requestUrl);

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
        urlConnection.setRequestProperty("Accept", "*/*");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setInstanceFollowRedirects(false);
        urlConnection.connect();
        Writer writer = new OutputStreamWriter(urlConnection.getOutputStream());

        writer.write(data);
        writer.flush();
        writer.close();

        //Get Response

        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        BufferedReader rd = new BufferedReader(new InputStreamReader(in));
        int statusCode = urlConnection.getResponseCode();


        /* 200 represents HTTP OK */
        if (statusCode == 200) {
            inputStream = new BufferedInputStream(urlConnection.getInputStream());

            String response = convertInputStreamToString(inputStream);

            ArrayList<ViewMessageResult> results = parseResult(response);

            return results;
        } else {
            throw new DownloadException("Failed to fetch data!!");
        }
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine())!=null){
            // stringBuilder.append(line);
            result += line;
        }
        //  Close Stream
        if (null != inputStream) {
            inputStream.close();
        }

        return result;
    }

    private ArrayList<ViewMessageResult> parseResult(String result) {

        ArrayList<ViewMessageResult> blogTitles = new ArrayList<ViewMessageResult>();
        ArrayList<String> blog = new ArrayList<String>();
        try {
            JSONObject response = new JSONObject(result);

            JSONArray LoanList = response.optJSONArray("ViewMessageResult");
            int size = LoanList.length();
            for (int i = 0; i < LoanList.length(); i++) {
                JSONObject post = LoanList.optJSONObject(i);
                Fullname = post.optString("Fullname");
                pmg_subject = post.optString("pmg_subject");
                pmg_message = post.optString("pmg_Message");
                pmg_date = post.optString("pmg_date");
                pmg_file_name = post.optString("pmg_file_name");
                pmg_file_path = post.optString("pmg_file_path");
                pmu_ID = post.optString("pmu_ID");
                usl_ID = post.optString("usl_ID");
                pmu_readunreadstatus = post.optString("pmu_readunreadstatus");
                msgStatus = LoanList.getJSONObject(i).getString("pmu_readunreadstatus");
                if(msgStatus.equalsIgnoreCase("1"))
                {
                    msgStatusList.add(msgStatus);
                    msgStatusSize = msgStatusList.size();
                }

                // Log.e("UnreadSize", String.valueOf(msgStatusSize));
                AppController.unreadMesList = msgStatusList.size();

                blogTitles.add ( new ViewMessageResult (Fullname,pmg_message,pmg_date,pmg_file_name,pmg_file_path,pmg_subject,pmu_ID,usl_ID,pmu_readunreadstatus));

            }
            firstmsgreadStatus = LoanList.getJSONObject(0).getString("pmu_readunreadstatus");
            firstmsgSub = LoanList.getJSONObject(0).getString("pmg_subject");
            firstmsgContent = LoanList.getJSONObject(0).getString("pmg_Message");
           /* Log.e("First Date",firstmsgreadStatus);
            Log.e("firstmsgSub",firstmsgSub);
            Log.e("firstmsgContent",firstmsgContent);*/
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            try {
                msgdate = format.parse(pmg_date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Calendar c = Calendar.getInstance();
            int date = c.get(Calendar.DATE);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            currentDateandTime = sdf.format(new Date());
            //  Log.e("Current Time",currentDateandTime);

            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            try {
                cuurdate = format1.parse(currentDateandTime);
                //  System.out.println("Curr Date"+cuurdate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return blogTitles;
    }

    public class DownloadException extends Exception {

        public DownloadException(String message) {
            super(message);
        }

        public DownloadException(String message, Throwable cause) {
            super(message, cause);
        }
    }


}
