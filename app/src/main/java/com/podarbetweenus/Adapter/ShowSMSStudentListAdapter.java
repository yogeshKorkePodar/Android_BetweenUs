package com.podarbetweenus.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.podarbetweenus.Activities.DataTransferInterface;
import com.podarbetweenus.Activities.TeacherSendSMSActivity;
import com.podarbetweenus.Entity.SmsStudentResult;
import com.podarbetweenus.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Gayatri on 2/4/2016.
 */
public class ShowSMSStudentListAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    Context context;
    DataTransferInterface dtInterface;
    String selectAll,stud_id,contact_number,class_id,clt_id,usl_id,school_name,teacher_name,version_name,board_name;
    boolean checkboxStatus;
    public ArrayList<SmsStudentResult> SmsStudentResult = new ArrayList<SmsStudentResult>();
    public static ArrayList<String> stud_id_data = new ArrayList<String>();
    public static ArrayList<String> contact_no_data = new ArrayList<String>();
    boolean selectAllCheckboxStatus;
    public static SharedPreferences resultpreLoginData;
    public ShowSMSStudentListAdapter(Context context,DataTransferInterface dataTransferInterface, ArrayList<SmsStudentResult> SmsStudentResult,boolean selectAllCheckboxStatus,String selectAll,String clt_id,String usl_id,String school_name,String teacher_name,String version_name,String board_name) {
        this.context = context;
        this.SmsStudentResult = SmsStudentResult;
        this.selectAllCheckboxStatus = selectAllCheckboxStatus;
        this.selectAll = selectAll;
        this.clt_id = clt_id;
        this.usl_id = usl_id;
        this.dtInterface = dataTransferInterface;
        this.school_name = school_name;
        this.teacher_name = teacher_name;
        this.version_name = version_name;
        this.board_name = board_name;
        this.layoutInflater = layoutInflater.from(this.context);
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return SmsStudentResult.size();
    }

    @Override
    public Object getItem(int position) {
        return SmsStudentResult.get(position);
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
            holder = new ViewHolder();
            showLeaveStatus = layoutInflater.inflate(R.layout.studentlist_item,null);
            holder.tv_student_name = (TextView) showLeaveStatus.findViewById(R.id.tv_student_name);
            holder.tv_roll_no_value = (TextView) showLeaveStatus.findViewById(R.id.tv_roll_no_value);
           // holder.tv_reg_no = (TextView)showLeaveStatus.findViewById(R.id.tv_reg_no);
            holder.checkbox = (CheckBox) showLeaveStatus.findViewById(R.id.select_checkbox);
            holder.ll_main = (LinearLayout) showLeaveStatus.findViewById(R.id.ll_main);
            holder.ll_checkbox = (LinearLayout) showLeaveStatus.findViewById(R.id.ll_checkbox);
            holder.ll_roll_no = (LinearLayout) showLeaveStatus.findViewById(R.id.ll_roll_no);

         //   holder.checkbox.setChecked(true);

        } else
        {
            holder = (ViewHolder) showLeaveStatus.getTag();
        }

        showLeaveStatus.setTag(holder);

        holder.tv_roll_no_value.setText(SmsStudentResult.get(position).Roll_No);
        holder.tv_student_name.setText(SmsStudentResult.get(position).Student_Name);


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
//        holder.tv_reg_no.setText(SmsStudentResult.get(position).Reg_No);
        if(selectAll.equalsIgnoreCase("true"))
        {
            holder.checkbox.setChecked(true);
        }
        else{
            holder.checkbox.setChecked(false);
        }
        checkboxStatus = holder.checkbox.isChecked();
        final ViewHolder finalHolder = holder;

        finalHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int position = (Integer) buttonView.getTag();
                if(isChecked){
                    finalHolder.checkbox.setChecked(true);
                    stud_id = SmsStudentResult.get(position).stu_ID;
                    contact_number =SmsStudentResult.get(position).con_MNo;
                    stud_id_data.add(position,stud_id);
                    contact_no_data.add(position, contact_number);
                    ShowSMSStudentListAdapter.Set_id(stud_id_data, contact_no_data, context);
                    ShowSMSStudentListAdapter.get_ContactData(context);
                    ShowSMSStudentListAdapter.get_studId(context);
                }
                else{
                    finalHolder.checkbox.setChecked(false);
                }
            }
        });






 /*       finalHolder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finalHolder.checkbox.setChecked(true);

                stud_id = SmsStudentResult.get(position).stu_ID;
                contact_number =SmsStudentResult.get(position).con_MNo;
                stud_id_data.add(position,stud_id);
                contact_no_data.add(position,contact_number);
              //  dtInterface.setValues(stud_id_data, contact_no_data);
                ShowSMSStudentListAdapter.Set_id(stud_id_data, contact_no_data, context);
                ShowSMSStudentListAdapter.get_ContactData(context);
                ShowSMSStudentListAdapter.get_studId(context);
              //  notifyDataSetChanged();
               *//* if(checkboxStatus==false){
                    finalHolder.checkbox.setChecked(true);
                    stud_id = SmsStudentResult.get(position).stu_ID;
                    contact_number =SmsStudentResult.get(position).con_MNo;
                    stud_id_data.add(position,stud_id);
                    contact_no_data.add(position,contact_number);
                    dtInterface.setValues(stud_id_data, contact_no_data);
                    ShowSMSStudentListAdapter.Set_id(stud_id_data, contact_no_data, context);
                    ShowSMSStudentListAdapter.get_ContactData(context);
                    ShowSMSStudentListAdapter.get_studId(context);
                    notifyDataSetChanged();
                }
                else{
                    finalHolder.checkbox.setChecked(false);
                    notifyDataSetChanged();
                }
*//*
               *//* stud_id = SmsStudentResult.get(position).stu_ID;
                contact_number =SmsStudentResult.get(position).con_MNo;
                class_id = SmsStudentResult.get(position).cls_ID;
                Intent sendSMS = new Intent(context,TeacherSendSMSActivity.class);
                sendSMS.putExtra("class_id",class_id);
                sendSMS.putExtra("clt_id",clt_id);
                sendSMS.putExtra("usl_id", usl_id);
                sendSMS.putExtra("School_name", school_name);
                sendSMS.putExtra("Teacher_name", teacher_name);
                sendSMS.putExtra("version_name", version_name);
                sendSMS.putExtra("board_name",board_name);
                sendSMS.putExtra("stud_id",stud_id);
                sendSMS.putExtra("Contact_Number",contact_number);
                context.startActivity(sendSMS);*//*

            }
        });
*/
        return showLeaveStatus;
    }
    public static void Set_id(ArrayList<String> studData,ArrayList<String> contactData,Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = resultpreLoginData.edit();
        Set<String> setStudId = new HashSet<String>();
        setStudId.addAll(studData);
        Set<String> setContactNo = new HashSet<String>();
        setContactNo.addAll(contactData);
        editor.putStringSet("StudId", setStudId);
        editor.putStringSet("ContactData", setContactNo);
        editor.commit();

    }
    public static Set<String> get_studId(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> setStudId = new HashSet<String>();
        setStudId = resultpreLoginData.getStringSet("StudId", null);
        return setStudId;
    }
    public static Set<String> get_ContactData(Context context) {
        resultpreLoginData = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> setContact = new HashSet<String>();
        setContact = resultpreLoginData.getStringSet("ContactData", null);
        return setContact;
    }

    public static class ViewHolder
    {
        TextView tv_roll_no_value,tv_student_name,tv_reg_no;
        CheckBox checkbox;
        LinearLayout ll_main,ll_checkbox,ll_roll_no;
    }

}
