package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.Adapter.SiblingListAdapter;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.Entity.StundentListDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 10/5/2015.
 */
public class SiblingActivity extends Activity implements View.OnClickListener {
    //UI Variables
    //Linear Layout
    LinearLayout lay_back_investment;
    //Relative Layout
    RelativeLayout rl_sibling1,rl_sibling2,rl_network_info;
    //ListView
    ListView sibling_list;
    //Button
    Button btn_refresh;
    //LayoutEntities
    HeaderControler header;
    //ProgressDialog
    ProgressDialog progressDialog;
    Context mcontext;

    SiblingListAdapter sibling_adapter;
    DataFetchService dft;
    LoginDetails login_details;
    public ArrayList<StundentListDetails> student_list_details = new ArrayList<StundentListDetails>();

    int listSize,versionCode;
    String clt_id,usl_id,school_name,location,msd_ID,versionName,board_name,isStudentresource;
    String StudentInfo_Method_name = "GetStundentInfo";
    public static SharedPreferences resultpreLoginData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sibling);
        findViews();
        init();
        init();
        getIntentData();
        sibling_list.setDivider(null);
        sibling_list.setDividerHeight(0);
        AppController.fromSplashScreen = "false";
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        //set Date When No Internet Connectivity
        try {
            if (connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                    connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

                clt_id = LoginActivity.get_cltId(this);
                usl_id = LoginActivity.get_uslId(this);
                sibling_list.setVisibility(View.GONE);
                rl_network_info.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try {
            //Webservice call for studentInfo
            callWebserviceGetStudentInfo(usl_id, clt_id);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    private void setUIData() {
        sibling_adapter = new SiblingListAdapter(SiblingActivity.this,login_details.stundentListDetails,location);
        sibling_list.setAdapter(sibling_adapter);
    }
    private void getIntentData() {
        Intent intent = getIntent();
        listSize = intent.getIntExtra("Sibling", 0);
        usl_id = intent.getStringExtra("usl_id");
        clt_id = intent.getStringExtra("clt_id");
        school_name = intent.getStringExtra("School_name");
        versionName = intent.getStringExtra("version_name");
        versionCode = intent.getIntExtra("version_code", 0);
        board_name = intent.getStringExtra("board_name");
        msd_ID = intent.getStringExtra("msd_ID");
        isStudentresource = intent.getStringExtra("isStudentResource");
        AppController.Board_name = board_name;
        AppController.school_name = school_name;
        AppController.versionName = versionName;
        AppController.usl_id = usl_id;
        clt_id = LoginActivity.get_cltId(this);
        usl_id = LoginActivity.get_uslId(this);
        try {
            String[] separated = school_name.split("-");
            location = separated[1];
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void findViews() {
        //Linear Layout
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
        //ListView
        sibling_list = (ListView) findViewById(R.id.sibling_list);
        //Relative Layout
        rl_network_info = (RelativeLayout) findViewById(R.id.rl_network_info);
        //Button
        btn_refresh = (Button) findViewById(R.id.btn_refresh);
    }
    private void init() {
        lay_back_investment.setOnClickListener(this);
        header = new HeaderControler(this,true,false,"Sibling");
        progressDialog = Constant.getProgressDialog(this);
        dft = new DataFetchService(this);
        btn_refresh.setOnClickListener(this);
        AppController.SiblingActivity = "true";

    }
    @Override
    public void onClick(View v) {
        if(v==lay_back_investment)
        {
            finish();
        }
        else if(v==btn_refresh) {
            //Webservice call for studentInfo
            callWebserviceGetStudentInfo(usl_id, clt_id);
        }
    }
    public static void Set_id(String clt_id,String msd_ID,String usl_id,String board_name,String school_name,Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = resultpreLoginData.edit();
        editor.putString("clt_id", clt_id);
        editor.putString("msd_ID", msd_ID);
        editor.putString("usl_id", usl_id);
        editor.putString("board_name",board_name);
        editor.putString("School_name", school_name);
        editor.commit();

    }
    public static String get_cltId(Context context){
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("clt_id", "");
    }
    public static String get_msdId(Context context){
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("msd_ID", "");
    }
    public static String get_uslId(Context context){
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("usl_id", "");
    }
    public static String get_BoardName(Context context){
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("board_name", "");
    }
    public static String get_SchoolName(Context context){
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        return resultpreLoginData.getString("School_name", "");
    }
    private void callWebserviceGetStudentInfo(final String usl_id, final String clt_id) {
        try {
            if (dft.isInternetOn() == true) {
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            } else {
                progressDialog.dismiss();
            }
            dft.getStudentInfo(usl_id, clt_id, StudentInfo_Method_name, Request.Method.POST,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            try {
                                login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                                if (login_details.Status.equalsIgnoreCase("1")) {
                                    sibling_list.setVisibility(View.VISIBLE);
                                    rl_network_info.setVisibility(View.GONE);
                                    setUIData();
                                    sibling_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                            int arraySize = login_details.stundentListDetails.size();
                                            msd_ID = login_details.stundentListDetails.get(position).msd_ID;
                                            AppController.msd_ID = msd_ID;

                                            SiblingActivity.Set_id(AppController.clt_id, AppController.msd_ID, AppController.usl_id, AppController.Board_name, AppController.school_name, SiblingActivity.this);
                                            if (AppController.ListClicked.equals("true")) {

                                            }
                                            Intent dashboard = new Intent(SiblingActivity.this, Profile_Sibling.class);
                                            dashboard.putExtra("msd_ID", msd_ID);
                                            dashboard.putExtra("clt_id", clt_id);
                                            dashboard.putExtra("usl_id", usl_id);
                                            dashboard.putExtra("School_name", school_name);
                                            dashboard.putExtra("version_name", AppController.versionName);
                                            dashboard.putExtra("version_code", AppController.versionCode);
                                            dashboard.putExtra("board_name", board_name);
                                            dashboard.putExtra("isStudentResource", isStudentresource);

                                            startActivity(dashboard);
                                        }
                                    });
                                } else {
                                    sibling_list.setVisibility(View.GONE);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }


                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Show error or whatever...
                            Log.d("SiblingActivity", "ERROR.._---" + error.getCause());

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
     super.onBackPressed();
      finish();
    }
}
