package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.Adapter.MessageAdpater;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.DateDropdownValueDetails;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.Entity.ViewMessageResult;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Services.ListResultReceiver;
import com.podarbetweenus.Utility.AppController;
import com.podarbetweenus.Utility.BadgeView;
import com.podarbetweenus.Utility.LoadMoreListView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 9/28/2015.
 */
public class ViewMessageActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener,ListResultReceiver.Receiver {
    //UI variables
    //EditText
    EditText ed_select_date;
    //TextView
    TextView tv_sender_name,tv_date,tv_msg,tv_no_record;
    //LinaerLayout
    LinearLayout lay_back_investment;
    //ListView
    ListView list_messages;
    //ProgressDialog
    ProgressDialog progressDialog;
    private ListView listView = null;

    private ArrayAdapter arrayAdapter = null;
    BadgeView badge;
    DataFetchService dft;
    LoginDetails login_details;
    MessageAdpater message_adapter;
    Notification notification;
    private ListResultReceiver mReceiver;
    public ArrayList<DateDropdownValueDetails> date_dropdownlist = new ArrayList<DateDropdownValueDetails>();
    public ArrayList<ViewMessageResult> message_result = new ArrayList<ViewMessageResult>();
    public ArrayList<ViewMessageResult> message_result_loadMore = new ArrayList<ViewMessageResult>();
    ArrayList<String> msgStatusList = new ArrayList<String>();

    String date,msg,sender_name,msd_ID,clt_id,board_name,usl_id,check="0",versionName,month_id,latest_month_id,latest_month,message,attachment_path,attachment_name,
            school_name,message_subject,toUslId,pmuId,month_year,msgStatus,announcementCount,behaviourCount,currentmonth,isStudentresource;
    String url ="http://www.betweenus.in/PODARAPP/PodarApp.svc/GetViewMessageData";
    int pageNo= 1,pageSize = 300,preLast,viewmessageListSize,notificationId,msgStatusSize,notificationID = 1;
    public String Loadmore="";
    boolean mHasRequestedMore;
    boolean isLoading = false;
    String DropdownMethodName = "GetDateDropdownValue";
    String ViewMessageMethodName = "GetViewMessageData";
    String LastViewMessageData = "GetLastViewMessageData";
    String[] select_date;
    ArrayList<String> strings_date ;
    LoadMoreListView list;
    AlarmManager alarmManager;
    AlarmReceiver alaram_receiver;
    ArrayList<ViewMessageResult> results;
    public static SharedPreferences resultpreLoginData;
    int versionCode;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.template_expandable_view_message);
        findViews();
        init();
        getIntentId();
        tv_no_record.setVisibility(View.GONE);
        list_messages.setVisibility(View.GONE);
        AppController.LatestMonth_View_Messages = "true";
        AppController.dropdownSelected = "false";
        onNewIntent(getIntent());
        Calendar now = Calendar.getInstance();
        Date date = now.getTime();
        SimpleDateFormat format = new SimpleDateFormat("M");
        currentmonth = format.format(date);
        if(AppController.LatestMonth_View_Messages.equalsIgnoreCase("true"))
        {
            CallDropdownWebservice(msd_ID,clt_id, AppController.Board_name);
        }
        AppController.setPendingNotificationsCount(0);

    }
    private void registerReceiver() {
		/*create filter for exact intent what we want from other intent*/
        IntentFilter intentFilter =new IntentFilter(AlarmReceiver.ACTION_TEXT_CAPITALIZED);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);

		/* create new broadcast receiver*/
        alaram_receiver=new AlarmReceiver();

		/* registering our Broadcast receiver to listen action*/
        registerReceiver(alaram_receiver, intentFilter);
    }
    public void addBadge(Context context, int count) {
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", this.getClass().getName());
        context.sendBroadcast(intent);
    }
    private void CallDropdownWebservice(String msd_ID, String clt_id, String board_name) {
        try {
            if (dft.isInternetOn() == true) {
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            } else {
                progressDialog.dismiss();
            }
            dft.getdatedeopdown(msd_ID, clt_id, board_name, DropdownMethodName, Request.Method.POST,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            try {
                                login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                                if (login_details.Status.equalsIgnoreCase("1")) {

                                    strings_date = new ArrayList<String>();

                                    try {
                                        for (int i = 0; i < login_details.DateDropdownValueDetails.size(); i++) {
                                            strings_date.add(login_details.DateDropdownValueDetails.get(i).MonthYear.toString());

                                            select_date = new String[strings_date.size()];
                                            select_date = strings_date.toArray(select_date);

                                            if (AppController.LatestMonth_View_Messages.equalsIgnoreCase("true")) {

                                                latest_month_id = login_details.DateDropdownValueDetails.get(0).monthid;
                                                latest_month = login_details.DateDropdownValueDetails.get(0).MonthYear;
                                                month_year = login_details.DateDropdownValueDetails.get(i).MonthYear;
                                                AppController.month_year = month_year;
                                                AppController.month_id = latest_month_id;
                                                ViewMessageActivity.Set_Monthid(AppController.month_id, ViewMessageActivity.this);

                                                //Webservcie call for View Messages
                                                callViewMessagesWebservice(AppController.clt_id, AppController.usl_id, AppController.msd_ID, AppController.month_id, check, "" + pageNo, "" + pageSize);
                                            }
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    selectDate(select_date);
                                    AppController.LatestMonth_View_Messages = "false";

                                } else if (login_details.Status.equalsIgnoreCase("0")) {

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            AppController.school_name = school_name;
            AppController.Board_name = board_name;
            AppController.clt_id = clt_id;
            AppController.msd_ID = msd_ID;
            AppController.usl_id = usl_id;
            Log.e("OnResum", "onResume()");
		/* we register BroadcastReceiver here*/
            registerReceiver();

            if (AppController.dropdownSelected.equalsIgnoreCase("false") & AppController.ListClicked.equalsIgnoreCase("true")) {
                //Webservcie call for View Messages
                callViewMessagesWebservice(AppController.clt_id, AppController.usl_id, AppController.msd_ID, AppController.month_id, check, "" + pageNo, "" + pageSize);
                //CallDropdownWebservice(msd_ID,clt_id,board_name);
            } else if (AppController.ListClicked.equalsIgnoreCase("true")) {
                callViewMessagesWebservice(AppController.clt_id, AppController.usl_id, AppController.msd_ID, month_id, check, "" + pageNo, "" + pageSize);
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onPause() {
        Log.i("onPause", "onPause()");
		/* we should unregister BroadcastReceiver here*/
        unregisterReceiver(alaram_receiver);
        super.onPause();
    }


    private void selectDate( final String[] select_date) {
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.dialog_list, null);
            alertDialog.setView(convertView);
            alertDialog.setTitle("Select Month");
            final ListView select_list = (ListView) convertView.findViewById(R.id.select_list);

            alertDialog.setItems(select_date, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    // Do something with the selection
                    ed_select_date.setText(select_date[item]);
                    dialog.dismiss();
                    String selecteddate = select_date[item];
                    Log.e("DATE", selecteddate);
                    month_id = getIDfromMonth(selecteddate);
                    AppController.selectedMonth = month_id;

                    Log.e("MONTH _ID", month_id);
                    //Webservcie call for View Messages
                    callViewMessagesWebservice(AppController.clt_id, AppController.usl_id, AppController.msd_ID, month_id, check, "" + pageNo, "" + pageSize);
                }
            });
            if (AppController.LatestMonth_View_Messages.equalsIgnoreCase("false")) {
                alertDialog.show();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void callViewMessagesWebservice(String clt_id,String usl_id,String msd_ID,String month_id,String check,String pageNo,String pageSize) {
        try {
            if (dft.isInternetOn() == true) {
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            } else {
                progressDialog.dismiss();
            }
            dft.getviewMessages(clt_id, usl_id, msd_ID, month_id, check, pageNo, pageSize, ViewMessageMethodName, Request.Method.POST,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                                if (login_details.Status.equalsIgnoreCase("1")) {
                                    if (AppController.dropdownSelected.equalsIgnoreCase("false")) {

                                        ed_select_date.setText(latest_month);
                                    }
                                    tv_no_record.setVisibility(View.GONE);
                                    list_messages.setVisibility(View.VISIBLE);
                                    setUIData();
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                    list_messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            AppController.SendMessageLayout = "true";
                                            try {
                                                if (login_details.ViewMessageResult.get(position).pmu_readunreadstatus.equalsIgnoreCase("1")) {
                                                    AppController.UnreadMessage = "true";
                                                    if (position == 0) {
                                                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                                        notificationManager.cancel(notificationID);
                                                    }
                                                } else {
                                                    AppController.UnreadMessage = "false";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            message = login_details.ViewMessageResult.get(position).pmg_Message;
                                            attachment_name = login_details.ViewMessageResult.get(position).pmg_file_name;
                                            attachment_path = login_details.ViewMessageResult.get(position).pmg_file_path;
                                            sender_name = login_details.ViewMessageResult.get(position).Fullname;
                                            date = login_details.ViewMessageResult.get(position).pmg_date;
                                            message_subject = login_details.ViewMessageResult.get(position).pmg_subject;
                                            toUslId = login_details.ViewMessageResult.get(position).usl_ID;
                                            pmuId = login_details.ViewMessageResult.get(position).pmu_ID;
                                            Intent detail_message = new Intent(ViewMessageActivity.this, DetailMessageActivity.class);
                                            Intent intent = getIntent();
                                            String clt_id = intent.getStringExtra("clt_id");
                                            String msd_ID = intent.getStringExtra("msd_ID");
                                            String usl_id = intent.getStringExtra("usl_id");
                                            announcementCount = intent.getStringExtra("annpuncement_count");
                                            behaviourCount = intent.getStringExtra("behaviour_count");
                                            isStudentresource = intent.getStringExtra("isStudentResource");
                                            AppController.clt_id = clt_id;
                                            AppController.msd_ID = msd_ID;
                                            AppController.usl_id = usl_id;
                                            AppController.school_name = school_name;
                                            detail_message.putExtra("clt_id", AppController.clt_id);
                                            detail_message.putExtra("msd_ID", AppController.msd_ID);
                                            detail_message.putExtra("usl_id", AppController.usl_id);
                                            detail_message.putExtra("School_name", AppController.school_name);
                                            detail_message.putExtra("Message", message);
                                            detail_message.putExtra("board_name", AppController.Board_name);
                                            detail_message.putExtra("Attachment Name", attachment_name);
                                            detail_message.putExtra("Attachment Path", attachment_path);
                                            detail_message.putExtra("Sender Name", sender_name);
                                            detail_message.putExtra("Date", date);
                                            detail_message.putExtra("Message Subject", message_subject);
                                            detail_message.putExtra("ToUslId", toUslId);
                                            detail_message.putExtra("version_name", AppController.versionName);
                                            detail_message.putExtra("annpuncement_count", announcementCount);
                                            detail_message.putExtra("behaviour_count", behaviourCount);
                                            detail_message.putExtra("PmuId", pmuId);
                                            detail_message.putExtra("isStudentResource", isStudentresource);
                                            startActivity(detail_message);
                                        }
                                    });


                                } else if (login_details.Status.equalsIgnoreCase("0")) {
                                    if (AppController.dropdownSelected.equalsIgnoreCase("true")) {
                                        tv_no_record.setText("No Records Found");
                                        tv_no_record.setVisibility(View.VISIBLE);
                                        list_messages.setVisibility(View.GONE);
                                    } else if (AppController.dropdownSelected.equalsIgnoreCase("false")) {
                                        //call top10 records webservice
                                        String check = "0", pageNo = "1", pageSize = "300";
                                        callLastViewMessagesWebservice(AppController.clt_id, AppController.usl_id, AppController.msd_ID, AppController.month_id, check, "" + pageNo, "" + pageSize);
                                    }
                                }

                                if (AppController.dropdownSelected.equalsIgnoreCase("true") || AppController.SentEnterDropdown.equalsIgnoreCase("true")) {
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setUIData()
    {
        if (progressDialog.isShowing()) {
            progressDialog.show();
        }
        viewmessageListSize= login_details.ViewMessageResult.size();

        if(AppController.dropdownSelected.equalsIgnoreCase("false")){
            Log.e("ViewMessage List Size", String.valueOf(viewmessageListSize));
            AppController.viewMessageListSize = viewmessageListSize;
            // AppController.Notification_send = "true";
        }

        message_result_loadMore = login_details.ViewMessageResult;
        message_adapter = new MessageAdpater(ViewMessageActivity.this, message_result_loadMore,notificationId);
        list_messages.setAdapter(message_adapter);
    }

    public void callLastViewMessagesWebservice(String clt_id,String usl_id,String msd_ID,String month_id,String check,String pageNo,String pageSize) {
        try {
            if (dft.isInternetOn() == true) {
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            } else {
                progressDialog.dismiss();
            }
            dft.getviewMessages(clt_id, usl_id, msd_ID, month_id, check, pageNo, pageSize, LastViewMessageData, Request.Method.POST,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {


                            try {
                                login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                                tv_no_record.setText("Loading...");
                                if (login_details.Status.equalsIgnoreCase("1")) {

                                    tv_no_record.setVisibility(View.GONE);
                                    list_messages.setVisibility(View.VISIBLE);
                                    // ViewMessageActivity.this.getListView().setVisibility(View.VISIBLE);

                                    setUIData();
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                    list_messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            try {
                                                AppController.SendMessageLayout = "true";
                                                if (login_details.ViewMessageResult.get(position).pmu_readunreadstatus.equalsIgnoreCase("1")) {
                                                    AppController.UnreadMessage = "true";
                                                    if (position == 0) {
                                                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                                        notificationManager.cancel(notificationID);
                                                    }
                                                } else {
                                                    AppController.UnreadMessage = "false";
                                                }
                                                message = login_details.ViewMessageResult.get(position).pmg_Message;
                                                attachment_name = login_details.ViewMessageResult.get(position).pmg_file_name;
                                                attachment_path = login_details.ViewMessageResult.get(position).pmg_file_path;
                                                sender_name = login_details.ViewMessageResult.get(position).Fullname;
                                                date = login_details.ViewMessageResult.get(position).pmg_date;
                                                message_subject = login_details.ViewMessageResult.get(position).pmg_subject;
                                                toUslId = login_details.ViewMessageResult.get(position).usl_ID;
                                                pmuId = login_details.ViewMessageResult.get(position).pmu_ID;
                                                Intent detail_message = new Intent(ViewMessageActivity.this, DetailMessageActivity.class);
                                                Intent intent = getIntent();
                                                String clt_id = intent.getStringExtra("clt_id");
                                                String msd_ID = intent.getStringExtra("msd_ID");
                                                String usl_id = intent.getStringExtra("usl_id");
                                                announcementCount = intent.getStringExtra("annpuncement_count");
                                                behaviourCount = intent.getStringExtra("behaviour_count");
                                                isStudentresource = intent.getStringExtra("isStudentResource");
                                                AppController.clt_id = clt_id;
                                                AppController.msd_ID = msd_ID;
                                                AppController.usl_id = usl_id;
                                                AppController.school_name = school_name;
                                                detail_message.putExtra("clt_id", AppController.clt_id);
                                                detail_message.putExtra("msd_ID", AppController.msd_ID);
                                                detail_message.putExtra("usl_id", AppController.usl_id);
                                                detail_message.putExtra("School_name", AppController.school_name);
                                                detail_message.putExtra("Message", message);
                                                detail_message.putExtra("Attachment Name", attachment_name);
                                                detail_message.putExtra("Attachment Path", attachment_path);
                                                detail_message.putExtra("Sender Name", sender_name);
                                                detail_message.putExtra("Date", date);
                                                detail_message.putExtra("isStudentResource", isStudentresource);
                                                detail_message.putExtra("Message Subject", message_subject);
                                                detail_message.putExtra("ToUslId", toUslId);
                                                detail_message.putExtra("annpuncement_count", announcementCount);
                                                detail_message.putExtra("behaviour_count", behaviourCount);
                                                detail_message.putExtra("board_name", AppController.Board_name);
                                                detail_message.putExtra("PmuId", pmuId);
                                                detail_message.putExtra("version_name", AppController.versionName);
                                                startActivity(detail_message);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    });
                                } else if (login_details.Status.equalsIgnoreCase("0")) {
                                    tv_no_record.setVisibility(View.VISIBLE);
                                    list_messages.setVisibility(View.GONE);
                                    tv_no_record.setText("No Records Found");
                                }
                                if (AppController.dropdownSelected.equalsIgnoreCase("false") || AppController.SentEnterDropdown.equalsIgnoreCase("true")) {
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                } else if (AppController.dropdownSelected.equalsIgnoreCase("false") || AppController.SentEnterDropdown.equalsIgnoreCase("false")) {
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String getIDfromMonth(String selecteddate) {

        String id = "0";
        for(int i=0;i<login_details.DateDropdownValueDetails.size();i++) {
            if(login_details.DateDropdownValueDetails.get(i).MonthYear.equalsIgnoreCase(selecteddate)){
                id= login_details.DateDropdownValueDetails.get(i).monthid;
                latest_month_id = null;
            }

        }
        return id;
    }
    @Override
    public void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        extras.containsKey("clt_id");
        extras.containsKey("msd_ID");
        extras.containsKey("usl_id");
        extras.containsKey("board_name");
        clt_id = extras.getString("clt_id");
        msd_ID = extras.getString("msd_ID");
        usl_id = extras.getString("usl_id");
        board_name = extras.getString("board_name");
        AppController.clt_id = clt_id;
        AppController.msd_ID = msd_ID;
        AppController.usl_id = usl_id;
        AppController.Board_name = board_name;
    }

    private void findViews() {

        //EditTExt
        ed_select_date = (EditText) findViewById(R.id.ed_select_date);
        //TextView
        tv_sender_name = (TextView) findViewById(R.id.tv_sender_name);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_no_record = (TextView) findViewById(R.id.tv_no_record);
        //ListView
        list_messages = (ListView) findViewById(R.id.list_messages);

    }


    private void getIntentId(){
        Intent intent = getIntent();
        board_name = intent.getStringExtra("board_name");
        clt_id = intent.getStringExtra("clt_id");
        msd_ID = intent.getStringExtra("msd_ID");
        usl_id = intent.getStringExtra("usl_id");
        school_name = intent.getStringExtra("School_name");
        versionName = intent.getStringExtra("version_name");
        versionCode = intent.getIntExtra("version_code", 0);
        announcementCount = intent.getStringExtra("annpuncement_count");
        behaviourCount = intent.getStringExtra("behaviour_count");
        isStudentresource = intent.getStringExtra("isStudentResource");
        AppController.versionName = versionName;
        AppController.versionCode = versionCode;
        AppController.dropdownSelected = intent.getStringExtra("DropDownSelected");
        AppController.school_name = school_name;
        AppController.Board_name = board_name;
        AppController.clt_id = clt_id;
        AppController.msd_ID = msd_ID;
        Bundle bundle=intent.getExtras();
        results =  (ArrayList<ViewMessageResult>)bundle.getSerializable("results");
        notificationId = getIntent().getExtras().getInt("notificationID");
        Log.e("Notification id_Dash...",String.valueOf(notificationId));

    }
    private  void init(){
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        progressDialog = Constant.getProgressDialog(this);
        ed_select_date.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            if (v == ed_select_date) {
                AppController.dropdownSelected = "true";
                CallDropdownWebservice(msd_ID, clt_id, AppController.Board_name);
            } else if (v == lay_back_investment) {

                if (AppController.SiblingActivity.equalsIgnoreCase("true")) {
                    Intent back = new Intent(ViewMessageActivity.this, Profile_Sibling.class);
                    AppController.OnBackpressed = "false";
                    AppController.parentMessageSent = "false";
                    AppController.clt_id = clt_id;
                    AppController.msd_ID = msd_ID;
                    AppController.usl_id = usl_id;
                    AppController.school_name = school_name;
                    AppController.Board_name = board_name;
                    back.putExtra("clt_id", AppController.clt_id);
                    back.putExtra("msd_ID", AppController.msd_ID);
                    back.putExtra("usl_id", AppController.usl_id);
                    back.putExtra("School_name", AppController.school_name);
                    back.putExtra("board_name", AppController.Board_name);
                    back.putExtra("version_name", AppController.versionName);
                    back.putExtra("verion_code", AppController.versionCode);
                    back.putExtra("isStudentResource", isStudentresource);
                    startActivity(back);
                } else {
                    Intent back = new Intent(ViewMessageActivity.this, ProfileActivity.class);
                    AppController.OnBackpressed = "false";
                    AppController.parentMessageSent = "false";
                    AppController.clt_id = clt_id;
                    AppController.msd_ID = msd_ID;
                    AppController.usl_id = usl_id;
                    AppController.school_name = school_name;
                    AppController.Board_name = board_name;
                    back.putExtra("clt_id", AppController.clt_id);
                    back.putExtra("msd_ID", AppController.msd_ID);
                    back.putExtra("usl_id", AppController.usl_id);
                    back.putExtra("School_name", AppController.school_name);
                    back.putExtra("board_name", AppController.Board_name);
                    back.putExtra("version_name", AppController.versionName);
                    back.putExtra("verion_code", AppController.versionCode);
                    back.putExtra("isStudentResource", isStudentresource);
                    startActivity(back);
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {

            AppController.ListClicked = "true";
            AppController.SendMessageLayout = "true";
            if(login_details.ViewMessageResult.get(position).pmu_readunreadstatus.equalsIgnoreCase("1")){
                AppController.UnreadMessage = "true";
                if(position==0) {
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(notificationID);
                }
            }
            else{
                AppController.UnreadMessage = "false";
            }
            message = login_details.ViewMessageResult.get(position).pmg_Message;
            attachment_name = login_details.ViewMessageResult.get(position).pmg_file_name;
            attachment_path = login_details.ViewMessageResult.get(position).pmg_file_path;
            sender_name = login_details.ViewMessageResult.get(position).Fullname;
            date = login_details.ViewMessageResult.get(position).pmg_date;
            message_subject = login_details.ViewMessageResult.get(position).pmg_subject;
            toUslId = login_details.ViewMessageResult.get(position).usl_ID;
            pmuId = login_details.ViewMessageResult.get(position).pmu_ID;
            Intent detail_message = new Intent(ViewMessageActivity.this, DetailMessageActivity.class);
            Intent intent = getIntent();
            String clt_id = intent.getStringExtra("clt_id");
            String msd_ID = intent.getStringExtra("msd_ID");
            String usl_id = intent.getStringExtra("usl_id");
            announcementCount = intent.getStringExtra("annpuncement_count");
            behaviourCount = intent.getStringExtra("behaviour_count");
            isStudentresource = intent.getStringExtra("isStudentResource");
            AppController.clt_id = clt_id;
            AppController.msd_ID = msd_ID;
            AppController.usl_id = usl_id;
            AppController.school_name = school_name;
            detail_message.putExtra("clt_id", AppController.clt_id);
            detail_message.putExtra("msd_ID", AppController.msd_ID);
            detail_message.putExtra("usl_id", AppController.usl_id);
            detail_message.putExtra("School_name", AppController.school_name);
            detail_message.putExtra("board_name", AppController.Board_name);
            detail_message.putExtra("Message", message);
            detail_message.putExtra("isStudentResource",isStudentresource);
            detail_message.putExtra("Attachment Name", attachment_name);
            detail_message.putExtra("Attachment Path", attachment_path);
            detail_message.putExtra("Sender Name", sender_name);
            detail_message.putExtra("Date", date);
            detail_message.putExtra("Message Subject",message_subject);
            detail_message.putExtra("ToUslId",toUslId);
            detail_message.putExtra("annpuncement_count",announcementCount);
            detail_message.putExtra("behaviour_count",behaviourCount);
            detail_message.putExtra("version_name", AppController.versionName);
            detail_message.putExtra("verion_code", AppController.versionCode);
            detail_message.putExtra("PmuId",pmuId);
            startActivity(detail_message);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void Set_Monthid(String month_id,Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = resultpreLoginData.edit();
        editor.putString("Month", month_id);
        editor.commit();

    }
    public static String get_MonthId(Context context){
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        Log.e("LoginActivity", resultpreLoginData.getString("Month", ""));
        return resultpreLoginData.getString("Month", "");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            if (AppController.SiblingActivity.equalsIgnoreCase("true")) {
                Intent back = new Intent(ViewMessageActivity.this, Profile_Sibling.class);
                AppController.OnBackpressed = "false";
                AppController.clt_id = clt_id;
                AppController.msd_ID = msd_ID;
                AppController.usl_id = usl_id;
                AppController.school_name = school_name;
                AppController.Board_name = board_name;
                AppController.parentMessageSent = "false";
                back.putExtra("clt_id", AppController.clt_id);
                back.putExtra("msd_ID", AppController.msd_ID);
                back.putExtra("usl_id", AppController.usl_id);
                back.putExtra("board_name", AppController.Board_name);
                back.putExtra("School_name", AppController.school_name);
                back.putExtra("version_name", AppController.versionName);
                back.putExtra("verion_code", AppController.versionCode);
                back.putExtra("isStudentResource", isStudentresource);
                startActivity(back);
            } else {
                Intent back = new Intent(ViewMessageActivity.this, ProfileActivity.class);
                AppController.OnBackpressed = "false";
                AppController.parentMessageSent = "false";
                AppController.clt_id = clt_id;
                AppController.msd_ID = msd_ID;
                AppController.usl_id = usl_id;
                AppController.Board_name = board_name;
                AppController.school_name = school_name;
                back.putExtra("clt_id", AppController.clt_id);
                back.putExtra("msd_ID", AppController.msd_ID);
                back.putExtra("usl_id", AppController.usl_id);
                back.putExtra("board_name", AppController.Board_name);
                back.putExtra("School_name", AppController.school_name);
                back.putExtra("version_name", AppController.versionName);
                back.putExtra("verion_code", AppController.versionCode);
                back.putExtra("isStudentResource", isStudentresource);
                startActivity(back);
            }
            // finish();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

    }
    public class AlarmReceiver extends BroadcastReceiver {
        /**
         * action string for our broadcast receiver to get notified
         */
        public final static String ACTION_TEXT_CAPITALIZED = "com.android.guide.exampleintentservice.intent.action.ACTION_TEXT_CAPITALIZED";

        @Override
        public void onReceive(Context context, Intent intent) {

            ///    AppController.Notification_send = "true";
            try {
                Bundle bundle = intent.getExtras();
                results = (ArrayList<ViewMessageResult>) bundle.getSerializable("results");
                viewmessageListSize = AppController.results.size();
                AppController.viewMessageListSize = viewmessageListSize;
                if (latest_month_id.equalsIgnoreCase(currentmonth)) {
                    ed_select_date.setText(latest_month);
                    message_adapter = new MessageAdpater(ViewMessageActivity.this, AppController.results, notificationId);
                    list_messages.setAdapter(message_adapter);
                } else if (!month_id.equalsIgnoreCase(currentmonth)) {
                    message_adapter = new MessageAdpater(ViewMessageActivity.this, login_details.ViewMessageResult, notificationId);
                    list_messages.setAdapter(message_adapter);
                } else if (month_id.equalsIgnoreCase(currentmonth)) {
                    message_adapter = new MessageAdpater(ViewMessageActivity.this, AppController.results, notificationId);
                    list_messages.setAdapter(message_adapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                list_messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AppController.ListClicked = "true";
                        AppController.SendMessageLayout = "true";
                        try {
                            if (login_details.ViewMessageResult.get(position).pmu_readunreadstatus.equalsIgnoreCase("1")) {
                                AppController.UnreadMessage = "true";
                                if (position == 0) {
                                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.cancel(notificationID);
                                }
                            } else {
                                AppController.UnreadMessage = "false";
                            }
                            message = login_details.ViewMessageResult.get(position).pmg_Message;
                            attachment_name = login_details.ViewMessageResult.get(position).pmg_file_name;
                            attachment_path = login_details.ViewMessageResult.get(position).pmg_file_path;
                            sender_name = login_details.ViewMessageResult.get(position).Fullname;
                            date = login_details.ViewMessageResult.get(position).pmg_date;
                            message_subject = login_details.ViewMessageResult.get(position).pmg_subject;
                            toUslId = login_details.ViewMessageResult.get(position).usl_ID;
                            pmuId = login_details.ViewMessageResult.get(position).pmu_ID;
                            Intent detail_message = new Intent(ViewMessageActivity.this, DetailMessageActivity.class);
                            Intent intent = getIntent();
                            String clt_id = intent.getStringExtra("clt_id");
                            String msd_ID = intent.getStringExtra("msd_ID");
                            String usl_id = intent.getStringExtra("usl_id");
                            isStudentresource = intent.getStringExtra("isStudentResource");
                            announcementCount = intent.getStringExtra("annpuncement_count");
                            behaviourCount = intent.getStringExtra("behaviour_count");
                            AppController.clt_id = clt_id;
                            AppController.msd_ID = msd_ID;
                            AppController.usl_id = usl_id;
                            AppController.school_name = school_name;
                            detail_message.putExtra("clt_id", AppController.clt_id);
                            detail_message.putExtra("msd_ID", AppController.msd_ID);
                            detail_message.putExtra("usl_id", AppController.usl_id);
                            detail_message.putExtra("School_name", AppController.school_name);
                            detail_message.putExtra("Message", message);
                            detail_message.putExtra("Attachment Name", attachment_name);
                            detail_message.putExtra("Attachment Path", attachment_path);
                            detail_message.putExtra("Sender Name", sender_name);
                            detail_message.putExtra("isStudentResource", isStudentresource);
                            detail_message.putExtra("Date", date);
                            detail_message.putExtra("Message Subject", message_subject);
                            detail_message.putExtra("ToUslId", toUslId);
                            detail_message.putExtra("PmuId", pmuId);
                            detail_message.putExtra("board_name", AppController.Board_name);
                            detail_message.putExtra("version_name", AppController.versionName);
                            detail_message.putExtra("verion_code", AppController.versionCode);
                            detail_message.putExtra("annpuncement_count", announcementCount);
                            detail_message.putExtra("behaviour_count", behaviourCount);
                            startActivity(detail_message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
