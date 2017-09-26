package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.Adapter.ViewBehaviourAdapter;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

import org.json.JSONObject;

/**
 * Created by Gayatri on 1/19/2016.
 */
public class ViewBehaviourActivity extends Activity implements View.OnClickListener{
    //LayoutEntities
    HeaderControler header;
    //Linear Layout
    LinearLayout lay_back_investment;
    //ListView
    ListView lv_behaviour_list;
    //Text View
    TextView tv_error_msg;
    //ProgressDialog
    ProgressDialog progressDialog;

    DataFetchService dft;
    LoginDetails login_details;
    ViewBehaviourAdapter view_behaviour_adapter;

    String msd_id,clt_id,usl_id,school_name,teacher_name,version_name,board_name;
    String ViewBehaviourMethod_name = "TeacherStudBehaviourDtls";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_behaviour_layout);
        findViews();
        init();
        getIntentData();
        lv_behaviour_list.setDivider(null);
        lv_behaviour_list.setDividerHeight(0);
        try {
            //call webservice to view behaviour
            callViewBehaviourWebsrvice(msd_id, clt_id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getIntentData() {
        Intent intent = getIntent();
        usl_id = intent.getStringExtra("usl_id");
        clt_id = intent.getStringExtra("clt_id");
        school_name = intent.getStringExtra("School_name");
        teacher_name = intent.getStringExtra("Teacher_name");
        version_name = intent.getStringExtra("version_name");
        board_name = intent.getStringExtra("board_name");
        msd_id = intent.getStringExtra("msdID");
        AppController.versionName= version_name;
    }

    private void init() {
        progressDialog = Constant.getProgressDialog(this);
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        header = new HeaderControler(this, true, false, "View Behaviour");
        lay_back_investment.setOnClickListener(this);
    }
    private void findViews() {
        lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
        //ListView
        lv_behaviour_list = (ListView) findViewById(R.id.lv_behaviour_list);
        //TextView
        tv_error_msg = (TextView) findViewById(R.id.tv_error_msg);
    }

    private void callViewBehaviourWebsrvice(String msd_id,String clt_id) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getViewBehaviourList(msd_id, clt_id, ViewBehaviourMethod_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);

                            if (login_details.Status.equalsIgnoreCase("1")) {
                                tv_error_msg.setVisibility(View.GONE);
                                lv_behaviour_list.setVisibility(View.VISIBLE);
                                setUIData();

                            } else {
                                tv_error_msg.setVisibility(View.VISIBLE);
                                lv_behaviour_list.setVisibility(View.GONE);
                                tv_error_msg.setText("Data Not Found");
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

    private void setUIData() {
        view_behaviour_adapter = new ViewBehaviourAdapter(ViewBehaviourActivity.this,login_details.teacherStudBehaviourResult);
        lv_behaviour_list.setAdapter(view_behaviour_adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {

        if(v==lay_back_investment)
        {
            finish();
        }
    }
}
