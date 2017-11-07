package com.podarbetweenus.Utility;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.podarbetweenus.Activities.LoginActivity;
import com.podarbetweenus.Activities.ViewMessageActivity;
import com.podarbetweenus.Entity.AdminDropResult;
import com.podarbetweenus.Entity.AnnouncementResult;
import com.podarbetweenus.Entity.SubjectResult;
import com.podarbetweenus.Entity.TeacherAttendaceResult;
import com.podarbetweenus.Entity.TopicList;
import com.podarbetweenus.Entity.ViewMessageResult;
import com.podarbetweenus.Services.ListResultReceiver;
import com.podarbetweenus.Services.Webservice;
import java.util.ArrayList;

public class AppController extends Application implements ListResultReceiver.Receiver {

	public static String std_value_drawer="";
	public static String roll_no_value_drawer = "";
	public static String child_name_drawer = "";
	public static String academic_year_drawer = "";
	public static String msd_ID = "";
	public static String clt_id = "";
	public static String usl_id = "";
	public static String school_name = "";
	public static String Board_name="";
	public static String SiblingActivity = "";
	public static int listItemSelected=-1;
	public static String LatestMonthResult = "";
	public static String LatestMonth_View_Messages = "";
	public static String month_id = "";
	public static String month_year = "";
	public static String CycleTest = "";
	public static String Subject = "";
	public static String dropdownSelected = "";
	public static int viewMessageListSize;
	public static int viewAnnouncementListSize;
	public static int viewTeacherAnnouncementListSize;
	public static int viewTeacherMessageListSize;
	public static int viewAdminMessageListSize;
	public static int pendingNotificationsCount = 0;
	public static int unreadMesList;
	public static int unreadAnnouncementList;
	public static String ListClicked="";
	public static String labelId = "";
	public static String CycleTestID = "";
	public static String SubjectId="";
	public static String SentEnterDropdown="";
	public static String AttenAnnounceBehaviourdropdown="";
	public static String selectedMonth = "";
	public static String UnreadMessage = "";
	public static String Notification_send = "";
	public static String SendMessageLayout = "";
	public static String OnBackpressed = "";
	public static String versionName = "";
	public static int versionCode;
	public static int iconCount;
	public static int iconCountOnResume;
	public static int iconAnnouncementCountOnResume;
	public static int iconAnnouncementCount;
	public static String editButtonClicked = "";
	public static String nationality = "";
	public static String occupation = "";
	public static String qualification = "";
	public static String language = "";
	public static String religion = "";
	public static String state="";
	public static String city = "";
	public static String StateSelection = "";
	public static String blood_groupId = "";
	public static String firstView = "firstView";
	public static int SiblingListSize;
	public static ArrayList<TopicList> TopicsListsize;
	public static ArrayList<ViewMessageResult> results;
	public static ArrayList<AnnouncementResult> announcement_result;
	public static ArrayList<TeacherAttendaceResult> teacherAttendaceResult;
	public static ArrayList<com.podarbetweenus.Entity.SubjectResult> SubjectResult;
	public static String fromSplashScreen="";
	public static String killApp = "";
	public static String isStudentResourcePresent = "";
	public static String announcementCount = "";
	public static String drawerAcademic_year = "";
	public static String drawerChild_name = "";
	public static String drawerStd = "";
	public static String drawerRoll_no = "";
	public static String std_name = "";
	public static String drawerSignOut = "";
	public static String ProfileWithoutSibling = "";
	public static String behaviourCount = "";
	public static String loginButtonClicked = "";
	public static String Adapter = "";
	public static String sentMsgsTab = "";
	public static String enterMessageBack = "";
	public static String teacher_sms= "";
	public static String currentAcademicYr = "";
	public static ArrayList<com.podarbetweenus.Entity.AdminDropResult> AdminDropResult;
	public static ArrayList<AnnouncementResult> AnnouncementResult;
	public static String showAllStudents = "";
	public static String AdminstudentSMS = "";
	public static String AdminschoolSMS = "";
	public static String announcementActivity = "";
	public static String parentMessageSent = "";
	public static String AdminSentMessage = "";
	public static String AdminWriteMessage = "";
	public static String AdminsendMessageToStudent = "";
	public static String firstPeriodNo = "";
	public static int AdminTabPosition;
	public static int TeacherTabPosition;

	public static String version_update = "true";
	public static final String TAG = AppController.class
			.getSimpleName();

	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	Context context;
	public int mNotificationCounter = 0;
	int viewmessageListSize;
	String roll_id;
	private ListResultReceiver mReceiver;
	ViewMessageActivity.AlarmReceiver alaram_receiver;

	private static AppController mInstance;
	LoginActivity loginActivity= new LoginActivity();
	//http://www.betweenus.in/PODARAPP/PodarApp.svc/
	String url ="http://www.betweenus.in/PODARAPP//PodarApp.svc/GetViewMessageData";
	String teacherUrl = "http://www.betweenus.in/PODARAPP/PodarApp.svc/GetSentMessageDataTeacher";
	public static String board_name;
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;

		/*clt_id=LoginActivity.get_cltId(this);
		msd_ID=LoginActivity.get_msdId(this);
		usl_id=LoginActivity.get_uslId(this);
		month_id =ViewMessageActivity.get_MonthId(this);
*/

		if(AppController.SiblingActivity.equalsIgnoreCase("true")){
			clt_id = com.podarbetweenus.Activities.SiblingActivity.get_cltId(this);
			msd_ID= com.podarbetweenus.Activities.SiblingActivity.get_msdId(this);
			usl_id= com.podarbetweenus.Activities.SiblingActivity.get_uslId(this);
			board_name = com.podarbetweenus.Activities.SiblingActivity.get_BoardName(this);
			roll_id = LoginActivity.get_RollId(this);
		}
		else{
			clt_id=LoginActivity.get_cltId(this);
			msd_ID=LoginActivity.get_msdId(this);
			usl_id=LoginActivity.get_uslId(this);
			board_name = LoginActivity.get_BoardName(this);
			roll_id = LoginActivity.get_RollId(this);

		}
		mReceiver = new ListResultReceiver(new android.os.Handler());
		mReceiver.setReceiver(AppController.this);

			Intent i = new Intent(AppController.this, Webservice.class);
			AppController.clt_id = clt_id;
			AppController.msd_ID = msd_ID;
			AppController.usl_id = usl_id;
			AppController.school_name = school_name;
			Log.e("Webservice","Webservice calling");
			i.putExtra("clt_id", clt_id);
			i.putExtra("msd_ID", msd_ID);
			i.putExtra("usl_id", usl_id);
			i.putExtra("School_name", school_name);
			i.putExtra("Roll_id",roll_id);
			//	intent.putExtra("Board Name",board_name);
			i.putExtra("Month", month_id);
			i.putExtra("Url", url);
			i.putExtra("receiver", mReceiver);
			i.putExtra("requestId", 101);

			PendingIntent pintent = PendingIntent.getService(AppController.this, 0, i, 0);
			AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 30 * 1000, pintent);
			startService(i);

	}

	private void registerReceiver() {
		/*create filter for exact intent what we want from other intent*/
		IntentFilter intentFilter =new IntentFilter(ViewMessageActivity.AlarmReceiver.ACTION_TEXT_CAPITALIZED);
		intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
		/* create new broadcast receiver*/
	//	alaram_receiver = new ViewMessageActivity.AlarmReceiver();
		/* registering our Broadcast receiver to listen action*/
		registerReceiver(alaram_receiver, intentFilter);
	}
	public static int getPendingNotificationsCount() {
		return pendingNotificationsCount;
	}

	public static void setPendingNotificationsCount(int pendingNotifications) {
		pendingNotificationsCount = pendingNotifications;
	}
	public static synchronized AppController getInstance() {
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.mRequestQueue,
					new LruBitmapCache());
		}
		return this.mImageLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {

	}

}
