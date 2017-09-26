package com.podarbetweenus.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.Activities.TeacherViewLogActivity;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.DateDropdownValueDetails;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.Entity.TopicList;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Gayatri on 3/7/2016.
 */
public class ExpandableTopicListAdapter extends BaseExpandableListAdapter implements ExpandableListView.OnGroupClickListener {
    private List<String> grouplist;
    private Map<String, List<String>> topicCollection;
    private Activity context;
    ImageView img_add_button,img_close;
    ListView list_resource;

    LoginDetails login_details = new LoginDetails();
    ResourceAdapter resourceAdapter;
    Context con;
    String crf_id,usl_id,crl_file,clt_id,board_name,school_name,teacher_name,org_id,teacher_div,teacher_shift,teacher_std,
            clas_teacher,academic_year,topicName,subject_name,classs,class_id,totalLogsFilled;
    String resourcesMethodName = "GetTeacherResourceList";
    String updateFileAccessmethodName = "UpdateFileAccess";
    ArrayList<TopicList> topicLists = new ArrayList<TopicList>();

    public ExpandableTopicListAdapter(Activity context, List<String> grouplist, Map<String, List<String>> topicCollextion, ArrayList<TopicList> topicLists,String usl_id,String clt_id,String school_name,String teacher_name,String org_id,String clas_teacher,String academic_year,String board_name,String teacher_div,String teacher_shift,String teacher_std,String subject_name,String classs,String class_id) {
        this.context = context;
        this.topicCollection = topicCollextion;
        this.grouplist = grouplist;
        this.topicLists = topicLists;
        this.usl_id = usl_id;
        this.clt_id = clt_id;
        this.board_name = board_name;
        this.school_name = school_name;
        this.teacher_name = teacher_name;
        this.org_id = org_id;
        this.teacher_div = teacher_div;
        this.teacher_shift = teacher_shift;
        this.teacher_std = teacher_std;
        this.clas_teacher = clas_teacher;
        this.academic_year = academic_year;
        this.subject_name = subject_name;
        this.classs = classs;
        this.class_id = class_id;
    }

    @Override
    public int getGroupCount() {
        return grouplist.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return topicCollection.get(grouplist.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return grouplist.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return topicCollection.get(grouplist.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        topicName = (String) getGroup(groupPosition);
        String sr_no =  topicLists.get(groupPosition).srNo;
        String icons = (String) getGroup(groupPosition);

        if (convertView == null) {

            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_topic_name,
                    null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.tv_topicname);
        // hiding log view
        if(academic_year.equalsIgnoreCase("2017-2018")){
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    0,
                    80,
                    2.7f
            );
            item.setLayoutParams(param);
        }
        TextView tv_srNo = (TextView) convertView.findViewById(R.id.tv_srNo);
        img_add_button = (ImageView) convertView.findViewById(R.id.img_add_button);

        int imageResourceId = isExpanded ? R.drawable.minus : R.drawable.add_plus_button;
        img_add_button.setImageResource(imageResourceId);

        img_add_button.setVisibility(View.VISIBLE);

        item.setText(topicName);
        tv_srNo.setText(sr_no);

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crf_id = AppController.TopicsListsize.get(groupPosition).crf_id;
                Log.e("CRF ID",crf_id);
                //CallResourceListWs
                callResourceListWs(crf_id,usl_id);
            }
        });
        return convertView;
    }
    private void callResourceListWs(String crf_id,String usl_id) {

        final DataFetchService dft = new DataFetchService((Context)context);
        final ProgressDialog progressDialog = Constant.getProgressDialog((Context) context);
        dft.getTeacherResourceList(crf_id, usl_id, resourcesMethodName, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);

                            if (login_details.Status.equalsIgnoreCase("1")) {
                                setResourceIDData();
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

    }

    private void setResourceIDData() {

        final Dialog alertDialog = new Dialog(context);
        //  final AlertDialog alertDialog = builder.create();
        LayoutInflater inflater = context.getLayoutInflater();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        View convertView = (View) inflater.inflate(R.layout.template_resource_list, null);
        list_resource = (ListView)convertView.findViewById(R.id.list_resource);
        img_close = (ImageView) convertView.findViewById(R.id.img_close);
        alertDialog.setContentView(convertView);

        Log.e("ResourceList", String.valueOf(login_details.ResourceList.size()));
        resourceAdapter = new ResourceAdapter(context,login_details.ResourceList);
        list_resource.setAdapter(resourceAdapter);
        alertDialog.setCancelable(false);
        alertDialog.show();

        list_resource.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    //Open Google Drive File
                    crl_file = login_details.ResourceList.get(position).crl_file;
                    String file_name[] = crl_file.split(":");
                    String file_id = file_name[1];
                    Log.e("filename",file_id);
                    String resourcefile_url = "https://drive.google.com/a/podar.org/file/d/"+file_id;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(resourcefile_url));
                    context.startActivity(i);
                    //CallWs to update file access
                    callWsToUpdateFileAccess(crl_file,usl_id);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }
    private void callWsToUpdateFileAccess(String crl_file,String usl_id) {

        final DataFetchService dft = new DataFetchService(context);
        final ProgressDialog progressDialog = Constant.getProgressDialog(context);;
        final LoginDetails login_details = new LoginDetails();
        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }
        dft.getUpdateFileAccess(crl_file, usl_id, updateFileAccessmethodName, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            LoginDetails login_details = new LoginDetails();
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Show error or whatever...
                        Log.d("TeacherBehavrActivity", "ERROR.._---" + error.getCause());
                    }
                });
    }
    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Log.d("<<getChildView","Called!!!");
        Log.d("<<academic_year",academic_year);
       /* if(academic_year.equalsIgnoreCase("2016-2017")){
            return null;
        }*/
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(R.layout.topic_child, null);

        TextView tv_total_logs_value = (TextView) convertView.findViewById(R.id.tv_total_logs_value);
        TextView tv_log_filled_value = (TextView) convertView.findViewById(R.id.tv_log_filled_value);
        TextView tv_view_log = (TextView) convertView.findViewById(R.id.tv_view_log);
        if(topicLists.get(groupPosition).No_of_logs_filled.equalsIgnoreCase("1")){
            totalLogsFilled = "Yes";
        }
        else{
            totalLogsFilled = "No";
        }
      //  tv_log_filled_value.setText("Total Logs - " + topicLists.get(groupPosition).total_logs);
        tv_log_filled_value.setText("Logs Filled - "+totalLogsFilled);
     //   tv_total_logs_value.setText("Logs Filled - " + topicLists.get(groupPosition).No_of_logs_filled);
        tv_view_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewLogs = new Intent(context,TeacherViewLogActivity.class);
                crf_id = AppController.TopicsListsize.get(groupPosition).crf_id;
                topicName =(String) getGroup(groupPosition);
                Log.e("CRFID",crf_id);
                viewLogs.putExtra("clt_id", clt_id);
                viewLogs.putExtra("usl_id", usl_id);
                viewLogs.putExtra("School_name", school_name);
                viewLogs.putExtra("Teacher_name", teacher_name);
                viewLogs.putExtra("org_id", org_id);
                viewLogs.putExtra("cls_teacher", clas_teacher);
                viewLogs.putExtra("academic_year", academic_year);
                viewLogs.putExtra("version_name", AppController.versionName);
                viewLogs.putExtra("board_name", board_name);
                viewLogs.putExtra("Teacher_div",teacher_div);
                viewLogs.putExtra("Teacher_Shift",teacher_shift);
                viewLogs.putExtra("Tecaher_Std",teacher_std);
                viewLogs.putExtra("crf_id",crf_id);
                viewLogs.putExtra("topic_name",topicName);
                viewLogs.putExtra("subject_name",subject_name);
                viewLogs.putExtra("Classs",classs);
                viewLogs.putExtra("class_id",class_id);
                context.startActivity(viewLogs);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return true;
    }
}
