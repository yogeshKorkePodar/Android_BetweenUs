package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.AdmTechList;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.class_id_data;
import static com.podarbetweenus.Activities.TeacherSMSStudentsActivity.contact_no_data_withoutduplicates;

/**
 * Created by Gayatri on 3/15/2016.
 */
public class TeacherSMSAdminActivity extends Activity implements View.OnClickListener {
    //UI
    //ListView
    ListView lv_admin_teacher_list,drawerListView;
    //TextView
    TextView tv_no_record,tv_child_name_drawer,tv_academic_year_drawer;
    //EditTExt
    EditText ed_search_name;
    //CheckBox
    CheckBox select_all_checkbox;
    //ImageView
    ImageView img_search,img_username_close;
    //ProgressDialog
    ProgressDialog progressDialog;
    //DrawerLayout
    DrawerLayout drawer;
    //RelativeLayout
    RelativeLayout rl_send_sms,leftfgf_drawer,rl_profile;


    DataFetchService dft;
    LoginDetails login_details;
    ContentDisplaceDrawerToggle mContentDisplaceToggle;
    boolean checkboxStatus,check,selectAllCheckboxStatus,status;
    AdminSendSMSToTeachersAdpater adminSendSMSToTeachersAdpater;

    String usl_id,org_id,clt_id,board_name,version_name,school_name,admin_name,selectAll="false",contact_number,checkboxClicked="false",
           chkAll,name="",academic_year;
    String getTeacherListMethodName = "GetAdminTeacherList";
    String separator = ",";
    int PageNo = 1;
    int PageSize=200;
    String[] data_without_sibling;
    int[] icons_without_sibling;
    int notificationID = 1;
    public static String  selectedCheckboxMobileNo = "";
    public static ArrayList<String> contact_no_data = new ArrayList<String>();
    public static Set<String> contact_no_data_withoutduplicates = new HashSet<>();

    @Override
    protected void onResume() {
        super.onResume();
        selectedCheckboxMobileNo = "";
        contact_no_data.clear();
        contact_no_data_withoutduplicates.clear();

        callWebserviceForTeachers(clt_id,board_name,usl_id,PageNo,PageSize,name);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.admin_teacher_sms_layout);
        findViews();
        init();
        getIntentId();
        //Call Ws for teacher
        try{
            callWebserviceForTeachers(clt_id,board_name,usl_id,PageNo,PageSize,name);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        ed_search_name.addTextChangedListener(new TextWatcher() {
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
    }

    private void init() {
        dft = new DataFetchService(this);
        login_details = new LoginDetails();
        progressDialog = Constant.getProgressDialog(this);
        rl_send_sms.setOnClickListener(this);
        select_all_checkbox.setOnClickListener(this);
        img_search.setOnClickListener(this);
        img_username_close.setOnClickListener(this);
    }

    private void getIntentId() {
        Intent intent = getIntent();
        usl_id = intent.getStringExtra("usl_id");
        clt_id = intent.getStringExtra("clt_id");
        school_name = intent.getStringExtra("School_name");
        admin_name = intent.getStringExtra("Admin_name");
        version_name = intent.getStringExtra("version_name");
        academic_year = intent.getStringExtra("academic_year");
        org_id = intent.getStringExtra("org_id");
        AppController.versionName = version_name;
        board_name = intent.getStringExtra("board_name");
    }

    private void findViews() {
        lv_admin_teacher_list = (ListView) findViewById(R.id.lv_admin_teacher_list);
        tv_no_record = (TextView) findViewById(R.id.tv_no_record);
        tv_child_name_drawer = (TextView) findViewById(R.id.tv_child_name_drawer);
        tv_academic_year_drawer = (TextView) findViewById(R.id.tv_academic_year_drawer);
        ed_search_name = (EditText) findViewById(R.id.ed_search_name);
        select_all_checkbox = (CheckBox) findViewById(R.id.select_all_checkbox);
        //Relative Layout
        rl_send_sms = (RelativeLayout) findViewById(R.id.rl_send_sms);
        img_search = (ImageView) findViewById(R.id.img_search);
        img_username_close = (ImageView) findViewById(R.id.img_username_close);
    }
    private void setDrawer() {
        icons_without_sibling = new int[]{R.drawable.dashboard_48x48, R.drawable.messagebox_48x48, R.drawable.sms_48x48, R.drawable.announcementicon_48x48, R.drawable.att_48x48, R.drawable.behaviouricon_48x48, R.drawable.subjectlist_48x48, R.drawable.setting1_hdpi, R.drawable.logout1_hdpi, R.drawable.info};
        data_without_sibling = new String[]{"Dashboard", "Messages", "SMS", "Announcement", "Attendance", "Behaviour", "Subject List", "Setting", "Sign Out", "About"};
        drawerListView.setAdapter(new CustomAccount(TeacherSMSAdminActivity.this, android.R.layout.simple_list_item_1, icons_without_sibling, data_without_sibling));

        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (dft.isInternetOn() == true) {

                    }
                } else if (position == 1) {
                    //Message
                    if (dft.isInternetOn() == true) {

                    }
                }
                else if(position == 8){
                    //Signout
                    Intent signout = new Intent(TeacherSMSAdminActivity.this, LoginActivity.class);
                    Constant.SetLoginData("", "", TeacherSMSAdminActivity.this);
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
            }
        });
        Constant.setAdminDrawerData(academic_year, admin_name, TeacherSMSAdminActivity.this);
        setDrawerData();
    }

    private void setDrawerData() {
        tv_child_name_drawer.setText(admin_name);
        tv_academic_year_drawer.setText(academic_year);
    }

    @Override
    public void onClick(View v) {
        if(v==select_all_checkbox) {
            try {
                isSelectallCheckboxchecked();
                status = select_all_checkbox.isChecked();
                if (selectAllCheckboxStatus == false) {
                    selectedCheckboxMobileNo = "";
                    selectAll = "false";
                    contact_no_data.clear();
                    contact_no_data_withoutduplicates.clear();
                    adminSendSMSToTeachersAdpater = new AdminSendSMSToTeachersAdpater(TeacherSMSAdminActivity.this, login_details.AdmTechList, selectAllCheckboxStatus, selectAll, clt_id, usl_id, school_name, admin_name, version_name, board_name);
                    lv_admin_teacher_list.setAdapter(adminSendSMSToTeachersAdpater);
                    for (int i = 0; i < login_details.AdmTechList.size(); i++) {
                        login_details.AdmTechList.get(i).setChecked(false);
                        boolean check = login_details.AdmTechList.get(i).isChecked();
                        Log.e("CHECK Act", String.valueOf(check));
                    }
                    adminSendSMSToTeachersAdpater.notifyDataSetChanged();
                } else {
                    selectAll = "true";
                    checkboxClicked = "false";
                    adminSendSMSToTeachersAdpater = new AdminSendSMSToTeachersAdpater(TeacherSMSAdminActivity.this, login_details.AdmTechList, selectAllCheckboxStatus, selectAll, clt_id, usl_id, school_name, admin_name, version_name, board_name);
                    lv_admin_teacher_list.setAdapter(adminSendSMSToTeachersAdpater);
                    for (int i = 0; i < login_details.AdmTechList.size(); i++) {
                        login_details.AdmTechList.get(i).setChecked(true);
                        boolean check = login_details.AdmTechList.get(i).isChecked();
                        Log.e("CHECK Act", String.valueOf(check));
                        contact_number = login_details.AdmTechList.get(i).stf_Mno;
                        contact_no_data.add(contact_number);
                        contact_no_data_withoutduplicates.addAll(contact_no_data);
                        Log.e("SIZE Contact", String.valueOf(contact_no_data_withoutduplicates.size()));
                        //Mobile Number
                        int mobileno_total = contact_no_data_withoutduplicates.size() * separator.length();
                        for (String s : contact_no_data_withoutduplicates) {
                            mobileno_total += s.length();
                        }
                        StringBuilder mobilnoString = new StringBuilder(mobileno_total);
                        for (String s : contact_no_data_withoutduplicates) {
                            mobilnoString.append(separator).append(s);
                        }
                        selectedCheckboxMobileNo = mobilnoString.substring(separator.length());
                        Log.e("MobileNo", selectedCheckboxMobileNo);
                    }
                    adminSendSMSToTeachersAdpater.notifyDataSetChanged();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(v==rl_send_sms){
            try {
                //adminSendSMSToTeachersAdpater.notifyDataSetChanged();
                if (selectAllCheckboxStatus == false && contact_no_data_withoutduplicates.size()==0) {
                    Constant.showOkPopup(this, "Please Select Teacher", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                }

            });
                }else if(contact_no_data_withoutduplicates.size() == 0){
                    Constant.showOkPopup(this, "Please Select Teacher", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }

                    });
                }
                else {
                    if (dft.isInternetOn() == true) {
                        Intent sendsms = new Intent(TeacherSMSAdminActivity.this, AdminSendSMSToTeachers.class);

                        if (selectAllCheckboxStatus == true && check == false && checkboxClicked.equalsIgnoreCase("true")) {
                            chkAll = "0";
                            contact_number = selectedCheckboxMobileNo;
                        } else if (selectAllCheckboxStatus == true && check == true && checkboxClicked.equalsIgnoreCase("true")) {
                            chkAll = "0";
                            contact_number = selectedCheckboxMobileNo;
                        } else if (selectAllCheckboxStatus == true) {
                            chkAll = "1";
                            contact_number = selectedCheckboxMobileNo;
                        } else {
                            chkAll = "0";
                            contact_number = selectedCheckboxMobileNo;
                        }
                        sendsms.putExtra("clt_id", clt_id);
                        sendsms.putExtra("usl_id", usl_id);
                        sendsms.putExtra("School_name", school_name);
                        sendsms.putExtra("Admin_name", admin_name);
                        sendsms.putExtra("version_name", AppController.versionName);
                        sendsms.putExtra("board_name", board_name);
                        sendsms.putExtra("org_id", org_id);
                        sendsms.putExtra("academic_year",academic_year);
                        sendsms.putExtra("chkAll", chkAll);
                        sendsms.putExtra("Contact_Number", contact_number);
                        Log.e("send chek", chkAll);
                        Log.d("<<send contact", contact_number);
                        startActivity(sendsms);
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(v==img_search){
            try {
                PageNo = 1;
                PageSize = 200;
                contact_no_data_withoutduplicates.clear();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                name = ed_search_name.getText().toString();
                //Call webservice for StudentList
                callWebserviceForTeachers(clt_id, board_name, usl_id, PageNo, PageSize, name);
                adminSendSMSToTeachersAdpater.notifyDataSetChanged();
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        else if(v==img_username_close){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            contact_no_data.clear();
            contact_no_data_withoutduplicates.clear();
            ed_search_name.setText("");
            name = ed_search_name.getText().toString();
            select_all_checkbox.setChecked(false);
            img_username_close.setVisibility(View.INVISIBLE);
            //Call webservice for StudentList
            callWebserviceForTeachers(clt_id, board_name, usl_id, PageNo, PageSize, name);
            adminSendSMSToTeachersAdpater.notifyDataSetChanged();
        }

    }

    private void isSelectallCheckboxchecked() {
        selectAllCheckboxStatus = select_all_checkbox.isChecked();
        Log.e("checked", String.valueOf(selectAllCheckboxStatus));
    }

    private void callWebserviceForTeachers(String clt_id,String board_name,String usl_id,int pageIndex,int pageSize,String name) {
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getAdminTeacherList(clt_id, board_name, usl_id, pageIndex, pageSize,name, getTeacherListMethodName, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);

                            if (login_details.Status.equalsIgnoreCase("1")) {
                                tv_no_record.setVisibility(View.GONE);
                                //   getListView().setVisibility(View.VISIBLE);
                                lv_admin_teacher_list.setVisibility(View.VISIBLE);
                                setUIDataForList();
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(ed_search_name.getWindowToken(), 0);
                            } else {
                                select_all_checkbox.setChecked(false);
                               // select_all_checkbox.setClickable(false);
                                tv_no_record.setVisibility(View.VISIBLE);
                                tv_no_record.setText("No Records Found");
                                lv_admin_teacher_list.setVisibility(View.GONE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Show error or whatever...
                        Log.d("TecaherAdminSMSActivity", "ERROR.._---" + error.getCause());
                    }
                });
    }
    private void setUIDataForList() {
        adminSendSMSToTeachersAdpater = new AdminSendSMSToTeachersAdpater(TeacherSMSAdminActivity.this,login_details.AdmTechList,selectAllCheckboxStatus,selectAll,clt_id,usl_id,school_name,admin_name,version_name,board_name);
        lv_admin_teacher_list.setAdapter(adminSendSMSToTeachersAdpater);
    }
    public class AdminSendSMSToTeachersAdpater extends BaseAdapter {
        LayoutInflater layoutInflater;
        Context context;
        DataTransferInterface dtInterface;
        String selectAll,stud_id,contact_number,class_id,clt_id,usl_id,school_name,teacher_name,version_name,board_name;
        boolean[] mCheckedState;
        public ArrayList<AdmTechList> AdmTechList = new ArrayList<AdmTechList>();
        boolean selectAllCheckboxStatus;
        public AdminSendSMSToTeachersAdpater(Context context,ArrayList<AdmTechList> AdmTechList,boolean selectAllCheckboxStatus,String selectAll,String clt_id,String usl_id,String school_name,String admin_name,String version_name,String board_name) {
            this.context = context;
            this.AdmTechList = AdmTechList;
            this.selectAllCheckboxStatus = selectAllCheckboxStatus;
            this.selectAll = selectAll;
            this.clt_id = clt_id;
            this.usl_id = usl_id;
            this.school_name = school_name;
            this.teacher_name = teacher_name;
            this.version_name = version_name;
            this.board_name = board_name;
            mCheckedState = new boolean[AdmTechList.size()];
            this.layoutInflater = layoutInflater.from(this.context);
            layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return AdmTechList.size();
        }

        @Override
        public Object getItem(int position) {
            return AdmTechList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            View showLeaveStatus = convertView;
            if (showLeaveStatus == null)
            {
                CheckBox result = (CheckBox)showLeaveStatus;
                holder = new ViewHolder();
                showLeaveStatus = layoutInflater.inflate(R.layout.studentlist_item,null);
                holder.tv_student_name = (TextView) showLeaveStatus.findViewById(R.id.tv_student_name);
                holder.tv_roll_no_value = (TextView) showLeaveStatus.findViewById(R.id.tv_roll_no_value);
                // holder.tv_reg_no = (TextView)showLeaveStatus.findViewById(R.id.tv_reg_no);
                holder.checkbox = (CheckBox) showLeaveStatus.findViewById(R.id.select_all_checkbox);
                holder.ll_main = (LinearLayout) showLeaveStatus.findViewById(R.id.ll_main);
                holder.ll_checkbox = (LinearLayout) showLeaveStatus.findViewById(R.id.ll_checkbox);
                holder.ll_roll_no = (LinearLayout) showLeaveStatus.findViewById(R.id.ll_roll_no);
                result = new CheckBox(context);
            } else
            {
                holder = (ViewHolder) showLeaveStatus.getTag();
            }

            showLeaveStatus.setTag(holder);

            holder.tv_roll_no_value.setText(AdmTechList.get(position).SrNo);
            holder.tv_student_name.setText(AdmTechList.get(position).fullname);


            if(position % 2 ==0){
                holder.ll_roll_no.setBackgroundResource(R.color.sms_listview_even_row);
                holder.ll_main.setBackgroundColor(Color.parseColor("#dbb5ab"));
                holder.ll_checkbox.setBackgroundResource(R.color.sms_listview_even_row);
                holder.tv_student_name.setBackgroundResource(R.color.sms_listview_even_row);
            }
            else {
                holder.ll_main.setBackgroundColor(Color.parseColor("#dbb5ab"));
                holder.ll_roll_no.setBackgroundResource(R.color.sms_listview_odd_row);
                holder.tv_student_name.setBackgroundResource(R.color.sms_listview_odd_row);
                holder.ll_checkbox.setBackgroundResource(R.color.sms_listview_odd_row);
            }
            if(selectAll.equalsIgnoreCase("true"))
            {
                holder.checkbox.setChecked(true);
            }
            else{
                holder.checkbox.setChecked(false);
            }
            checkboxStatus = holder.checkbox.isChecked();
            Log.e("Checkbox status", String.valueOf(checkboxStatus));
            final ViewHolder finalHolder = holder;
            finalHolder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        String name = AdmTechList.get(position).fullname;
                        String check_for = AdmTechList.get(position).stf_Mno;
                        Log.d("<<Teacher & Mob no" ,name+":"+check_for);
                        Log.d("<<checking for:",check_for);
                        if(selectedCheckboxMobileNo.contains(check_for)==true){
                            Log.d("<<Before removing",selectedCheckboxMobileNo);
                            contact_no_data.remove(check_for);
                            contact_no_data_withoutduplicates.remove(check_for);
                            selectedCheckboxMobileNo = android.text.TextUtils.join(",", contact_no_data_withoutduplicates);
                            Log.d("<<After removing",selectedCheckboxMobileNo);

                        }
                        if (((CheckBox) v).isChecked()) {
                            if (selectAll.equalsIgnoreCase("true")) {
                                checkboxClicked = "true";
                                if (finalHolder.checkbox.isChecked() == true) {
                                    contact_no_data_withoutduplicates.add(AdmTechList.get(position).stf_Mno);
                                } else {
                                    contact_no_data_withoutduplicates.remove(AdmTechList.get(position).stf_Mno);
                                }
                                Log.d("<<point 1", String.valueOf(contact_no_data_withoutduplicates.size()));
                                //Mobile Number
                                int mobileno_total = contact_no_data_withoutduplicates.size() * separator.length();
                                for (String s : contact_no_data_withoutduplicates) {
                                    mobileno_total += s.length();
                                }
                                StringBuilder mobilnoString = new StringBuilder(mobileno_total);
                                for (String s : contact_no_data_withoutduplicates) {
                                    mobilnoString.append(separator).append(s);
                                }
                                selectedCheckboxMobileNo = mobilnoString.substring(separator.length());
                                Log.d("<<point 2 MobileNo", selectedCheckboxMobileNo);
                            } else {
                                if(finalHolder.checkbox.isChecked() == true){
                                    contact_no_data_withoutduplicates.add(AdmTechList.get(position).stf_Mno);
                                }
                                else {
                                    contact_no_data_withoutduplicates.remove(AdmTechList.get(position).stf_Mno);
                                }
                                mCheckedState[position] = true;
                                AdmTechList.get(position).setChecked(true);
                                check = AdmTechList.get(position).isChecked();
                                Log.e("CHECK", String.valueOf(check));
                                finalHolder.checkbox.setChecked(true);
                                checkboxStatus = finalHolder.checkbox.isChecked();
                                Log.d("<<point 3", String.valueOf(contact_no_data_withoutduplicates.size()));
                                //Mobile Number
                                int mobileno_total = contact_no_data_withoutduplicates.size() * separator.length();
                                for (String s : contact_no_data_withoutduplicates) {
                                    mobileno_total += s.length();
                                }
                                StringBuilder mobilnoString = new StringBuilder(mobileno_total);
                                for (String s : contact_no_data_withoutduplicates) {
                                    mobilnoString.append(separator).append(s);
                                }
                                selectedCheckboxMobileNo = mobilnoString.substring(separator.length());
                                Log.d("<<point 4 MobileNo", selectedCheckboxMobileNo);
                            }
                        } else {
                            if (selectAll.equalsIgnoreCase("true")) {
                                checkboxClicked = "true";
                                if (finalHolder.checkbox.isChecked() == true) {
                                    contact_no_data_withoutduplicates.add(AdmTechList.get(position).stf_Mno);
                                } else {

                                    contact_no_data_withoutduplicates.remove(AdmTechList.get(position).stf_Mno);
                                }
                                Log.e("<<point 5", String.valueOf(contact_no_data_withoutduplicates.size()));
                                //Mobile Number
                                int mobileno_total = contact_no_data_withoutduplicates.size() * separator.length();
                                for (String s : contact_no_data_withoutduplicates) {
                                    mobileno_total += s.length();
                                }
                                StringBuilder mobilnoString = new StringBuilder(mobileno_total);
                                for (String s : contact_no_data_withoutduplicates) {
                                    mobilnoString.append(separator).append(s);
                                }
                                selectedCheckboxMobileNo = mobilnoString.substring(separator.length());
                                Log.d("<<point 6 MobileNo", selectedCheckboxMobileNo);
                            } else if(selectAll.equalsIgnoreCase("false")){
                                AdmTechList.get(position).setChecked(false);
                                check = AdmTechList.get(position).isChecked();
                                Log.e("CHECK", String.valueOf(check));
                                mCheckedState[position] = false;

                                if (finalHolder.checkbox.isChecked() == true) {
                                    contact_no_data_withoutduplicates.add(AdmTechList.get(position).stf_Mno);
                                } else {

                                    contact_no_data_withoutduplicates.remove(AdmTechList.get(position).stf_Mno);
                                }
                                Log.d("<<point 7", String.valueOf(contact_no_data_withoutduplicates.size()));
                                //Mobile Number
                                int mobileno_total = contact_no_data_withoutduplicates.size() * separator.length();
                                for (String s : contact_no_data_withoutduplicates) {
                                    mobileno_total += s.length();
                                }
                                StringBuilder mobilnoString = new StringBuilder(mobileno_total);
                                for (String s : contact_no_data_withoutduplicates) {
                                    mobilnoString.append(separator).append(s);
                                }
                                selectedCheckboxMobileNo = mobilnoString.substring(separator.length());
                                Log.d("<<point 8 MobileNo", selectedCheckboxMobileNo);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });
            finalHolder.checkbox.setChecked(AdmTechList.get(position).isChecked());
            return showLeaveStatus;
        }
    }
    public static class ViewHolder
    {
        TextView tv_roll_no_value,tv_student_name,tv_reg_no;
        CheckBox checkbox;
        LinearLayout ll_main,ll_checkbox,ll_roll_no;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        try {
            AppController.AdminDropResult.clear();
            Intent back = new Intent(TeacherSMSAdminActivity.this, AdminProfileActivity.class);
            AppController.AdminDropResult.clear();
            AppController.AdminstudentSMS = "false";
            AppController.teacher_sms = "false";
            AppController.AdminschoolSMS = "false";
            back.putExtra("usl_id", usl_id);
            back.putExtra("clt_id", clt_id);
            back.putExtra("School_name", school_name);
            back.putExtra("Admin_name", admin_name);
            back.putExtra("org_id", org_id);
            back.putExtra("academic_year",academic_year);
            back.putExtra("version_name", AppController.versionName);
            back.putExtra("board_name", board_name);
            startActivity(back);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
