package com.podarbetweenus.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.podarbetweenus.Activities.DataTransferInterface;
import com.podarbetweenus.Entity.AttendanceViewResult;
import com.podarbetweenus.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;

import org.apache.commons.codec.binary.StringUtils;

/**
 * Created by Gayatri on 3/6/2017.
 */

public class Teacher_Add_Attendence_Adapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;
    Dialog dialog;
    String msd_id = "";
    public static String selectedAbsentCheckboxes = "";
    public static String selectedSMSCheckbox = "";

    public ArrayList<AttendanceViewResult> attendanceViewResult = new ArrayList<>();

    public static ArrayList<String> stud_present_data = new ArrayList<String>();

    public static ArrayList<String> stud_absent_data_final = new ArrayList<String>();
    public static Set<String> stud_absent_data_withoutDuplicates = new HashSet<>();

    public static ArrayList<String> stud_sms_data_final = new ArrayList<String>();
    public static Set<String> stud_sms_data_withoutDuplicates = new HashSet<>();

    public static HashMap absent_reason_map = new HashMap();

    public static class ViewHolder{
        TextView roll_no, student_name;
        CheckBox absent_checkbox, sms_checkbox;
        Button absent_reason;
        LinearLayout linearLayout1, linearLayout2;

    }

    public Teacher_Add_Attendence_Adapter(Context context, ArrayList<AttendanceViewResult> AttendanceViewResult) {

        this.context = context;
        this.layoutInflater = layoutInflater.from(this.context);
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.attendanceViewResult = AttendanceViewResult;

        Log.d("<<Constructor","Called!!!");

        // Initialising all variables to default in case activity resumed to avoid re-writing of data
        selectedAbsentCheckboxes = "";
        selectedSMSCheckbox = "";

        stud_present_data.clear();
        // Adding new msd data to present list

        for (int i = 0; i<attendanceViewResult.size();i++){
            String present_stud_msdId = attendanceViewResult.get(i).msd_id;
            stud_present_data.add(present_stud_msdId);
        }

        stud_absent_data_final.clear();
        stud_absent_data_withoutDuplicates.clear();

        stud_sms_data_final.clear();
        stud_sms_data_withoutDuplicates.clear();

        absent_reason_map.clear();

    }

    @Override
    public int getCount() {
        return attendanceViewResult.size();
    }

    @Override
    public Object getItem(int position) {
        return attendanceViewResult.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View convertedView = convertView;
        final ViewHolder viewHolder ;

       /* if(convertedView == null){*/
        viewHolder = new ViewHolder();
        convertedView = layoutInflater.inflate(R.layout.add_attendence_listview_item,null);
        convertedView.setTag(viewHolder);

        viewHolder.roll_no = (TextView) convertedView.findViewById(R.id.roll_no);
        viewHolder.student_name = (TextView) convertedView.findViewById(R.id.student_name);
        viewHolder.absent_checkbox = (CheckBox) convertedView.findViewById(R.id.absent_checkbox);
        viewHolder.linearLayout1 = (LinearLayout) convertedView.findViewById(R.id.linearLayout1);
        viewHolder.sms_checkbox = (CheckBox)convertedView.findViewById(R.id.sms_checkBox);
        viewHolder.linearLayout2 = (LinearLayout) convertedView.findViewById(R.id.linearLayout2);
        viewHolder.absent_reason = (Button) convertedView.findViewById(R.id.absent_reason);


        viewHolder.absent_reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("<< Textview","Clicked");
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.absent_reason_input_dialog);
                dialog.setTitle("Enter absent reason:");

                final EditText enter_reason = (EditText) dialog.findViewById(R.id.editText);
                Button btnSave = (Button) dialog.findViewById(R.id.saveButton);
                Button btnClear = (Button) dialog.findViewById(R.id.clearButton);
                Button btnCancel = (Button) dialog.findViewById(R.id.cancelButton);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        enter_reason.setText("");
                    }
                });

                if(attendanceViewResult.get(position).getAbsentStatus()==true){
                    enter_reason.setText(attendanceViewResult.get(position).getAbsentReason());
                }

                btnSave.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String reason_entered = enter_reason.getText().toString();
                        Log.d("<<Reason entered", reason_entered);

                        attendanceViewResult.get(position).setAbsentReason(reason_entered);
                        Log.d("<< Reason inserted:", attendanceViewResult.get(position).getAbsentReason());

                        msd_id = attendanceViewResult.get(position).msd_id;
                        absent_reason_map.put(msd_id, reason_entered);

                        if(reason_entered.length()>12){

                            String short_reason = reason_entered.substring(0, 12) + "...";
                            viewHolder.absent_reason.setText(short_reason);
                        }
                        else{
                            viewHolder.absent_reason.setText(reason_entered);
                        }

                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

       /* }
        else{
            viewHolder = (ViewHolder) convertedView.getTag();
        }*/

        viewHolder.roll_no.setText(attendanceViewResult.get(position).Roll_No);
        viewHolder.student_name.setText(attendanceViewResult.get(position).StudentName);
        viewHolder.absent_checkbox.setChecked(attendanceViewResult.get(position).getAbsentStatus());
        viewHolder.sms_checkbox.setChecked(attendanceViewResult.get(position).smsChecked);

        if(attendanceViewResult.get(position).getAbsentStatus()==true)
        { // if absent then set edittext focusable

            // Update case : adding of existing element
            msd_id = attendanceViewResult.get(position).msd_id;

            // obtaining hashset after adding of element
            stud_absent_data_withoutDuplicates.add(msd_id);
            //converting hashset to temporary arraylist so that we can convert it to csv string
            stud_absent_data_final = new ArrayList<String>(stud_absent_data_withoutDuplicates);
            //converting arraylist to csv string
            selectedAbsentCheckboxes = android.text.TextUtils.join(",",stud_absent_data_final );

            String reason_entered = attendanceViewResult.get(position).atnReason;
            absent_reason_map.put(msd_id, reason_entered);

            Log.d("<<Edittext","A");
            viewHolder.absent_reason.setEnabled(true);
           /* viewHolder.absent_reason.setFocusable(true);
            viewHolder.absent_reason.setFocusableInTouchMode(true);*/
            viewHolder.sms_checkbox.setEnabled(true);

            if(attendanceViewResult.get(position).atnReason.equalsIgnoreCase("")==true)
            {//if absent reason is null set hint to write reason
                Log.d("<<Edittext","B");
                viewHolder.absent_reason.setHint("Enter Reason");
            }
            else
            {// set existing reason
                Log.d("<<Edittext","C");
                reason_entered = attendanceViewResult.get(position).atnReason;
                if (reason_entered.equalsIgnoreCase(" ")){
                    viewHolder.absent_reason.setHint("Enter Reason");
                }
                else if(reason_entered.length()>12){

                    String short_reason = reason_entered.substring(0, 12) + "...";
                    viewHolder.absent_reason.setText(short_reason);
                }
                else{
                    viewHolder.absent_reason.setText(reason_entered);
                }
            }
        }
        else
        {
            Log.d("<<Edittext","D");
            viewHolder.absent_reason.setEnabled(false);
           // viewHolder.absent_reason.setClickable(false);
               /* viewHolder.absent_reason.setFocusable(false);
                viewHolder.absent_reason.setFocusableInTouchMode(false);*/
            viewHolder.sms_checkbox.setEnabled(false);
        }

        // For row colouring purpose
        if(position%2==0){
            viewHolder.roll_no.setBackgroundResource(R.color.attendance_listview_even_row);
            viewHolder.student_name.setBackgroundResource(R.color.attendance_listview_even_row);;
            viewHolder.absent_checkbox.setBackgroundResource(R.color.attendance_listview_even_row);
            viewHolder.sms_checkbox.setBackgroundResource(R.color.attendance_listview_even_row);;
            viewHolder.linearLayout1.setBackgroundResource(R.color.attendance_listview_even_row);;
            viewHolder.linearLayout2.setBackgroundResource(R.color.attendance_listview_even_row);;
            viewHolder.absent_reason.setBackgroundResource(R.color.attendance_listview_even_row);;
        }else{
            viewHolder.roll_no.setBackgroundResource(R.color.attendance_listview_odd_row);
            viewHolder.student_name.setBackgroundResource(R.color.attendance_listview_odd_row);
            viewHolder.absent_checkbox.setBackgroundResource(R.color.attendance_listview_odd_row);
            viewHolder.linearLayout1.setBackgroundResource(R.color.attendance_listview_odd_row);
            viewHolder.linearLayout2.setBackgroundResource(R.color.attendance_listview_odd_row);
            viewHolder.sms_checkbox.setBackgroundResource(R.color.attendance_listview_odd_row);
            viewHolder.absent_reason.setBackgroundResource(R.color.attendance_listview_odd_row);
        }
        // All change listener are below
        //For absent Checkbox
        viewHolder.absent_checkbox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    msd_id = attendanceViewResult.get(position).msd_id;
                    Log.d("<<Add_attendence_Adapte", "point1");
                    if(!((CheckBox) v).isChecked()){

                        Log.d("<<Box unchecked", "point2A");

                        Log.d("<<Removing item:", msd_id );
                        //Disabling focus for current row so that user cant enter text by mistake
                        viewHolder.absent_reason.setText("");
                        viewHolder.absent_reason.setHint("");
                        viewHolder.absent_reason.setEnabled(false);
                       // viewHolder.absent_reason.setClickable(false);
                        /*viewHolder.absent_reason.setFocusable(false);
                        viewHolder.absent_reason.setFocusableInTouchMode(false);*/
                        viewHolder.sms_checkbox.setChecked(false);
                        viewHolder.sms_checkbox.setEnabled(false);

                        // setting belows views (edittext and checkboxes )to default
                        attendanceViewResult.get(position).setAbsentReason("");
                        attendanceViewResult.get(position).setAbsentStatus(false);
                        attendanceViewResult.get(position).setSmsStatus(false);

                        // Modifying final strings

                        // Removing entered reason for msd id
                        absent_reason_map.remove(msd_id);

                        // obtaining hashset after removal of element
                        stud_absent_data_withoutDuplicates.remove(msd_id);
                        //converting hashset to temporary arraylist so that we can convert it to csv string
                        stud_absent_data_final = new ArrayList<>(stud_absent_data_withoutDuplicates);
                        //converting arraylist to csv string
                        selectedAbsentCheckboxes = android.text.TextUtils.join(",",stud_absent_data_final );
                        Log.d("<<Aft Remove Abnt Selct", selectedAbsentCheckboxes );
                        // Doing same procedure as above for the sms

                        stud_sms_data_withoutDuplicates.remove(msd_id);
                        stud_sms_data_final = new ArrayList<String>(stud_sms_data_withoutDuplicates);
                        selectedSMSCheckbox = android.text.TextUtils.join(",",stud_sms_data_final );
                        Log.d("<<Aft Remove SMS Selct", selectedSMSCheckbox );

                    }
                    else if (((CheckBox) v).isChecked()) {

                        Log.d("<<Box Checked", "point2B");
                        // Enabling edittext to enter reason
                        viewHolder.absent_reason.setEnabled(true);
                       /* viewHolder.absent_reason.setFocusable(true);
                        viewHolder.absent_reason.setFocusableInTouchMode(true);*/
                        viewHolder.sms_checkbox.setEnabled(true);

                        // Initialising with null reason in the case if teacher doesnt enter reason
                        absent_reason_map.put(msd_id, " ");

                        if(attendanceViewResult.get(position).atnReason.equalsIgnoreCase("")==true)
                        {//if absent reason is null set hint to write reason
                            Log.d("<<Edittext","E");
                            Log.d("<<Absent clicked ","& Hint set for:"+ msd_id);
                            viewHolder.absent_reason.setHint("Enter Reason");
                        }
                        else
                        {// set existing reason
                            Log.d("<<Edittext","F");
                            String reason_entered = attendanceViewResult.get(position).atnReason;
                            if(reason_entered.length()>12){

                                String short_reason = reason_entered.substring(0, 12) + "...";
                                viewHolder.absent_reason.setText(short_reason);
                            }
                            else{
                                viewHolder.absent_reason.setText(reason_entered);
                            }
                        }

                        attendanceViewResult.get(position).setAbsentStatus(true);
                        boolean check = attendanceViewResult.get(position).getAbsentStatus();
                        Log.d("<<Absent checkbox value", String.valueOf(check));

                        msd_id = attendanceViewResult.get(position).msd_id;
                        Log.d("<<Student msd_id",msd_id);


                        // obtaining hashset after adding of element
                        stud_absent_data_withoutDuplicates.add(msd_id);
                        //converting hashset to temporary arraylist so that we can convert it to csv string
                        stud_absent_data_final = new ArrayList<String>(stud_absent_data_withoutDuplicates);
                        //converting arraylist to csv string
                        selectedAbsentCheckboxes = android.text.TextUtils.join(",",stud_absent_data_final );

                        Log.d("<<selectedAbsenCheckbox",selectedAbsentCheckboxes);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

        });

        //For SMS Checkbox
        viewHolder.sms_checkbox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Log.d("<<Add_attendence_Adapte", "point1");

                    if(!((CheckBox) v).isChecked()){

                        Log.d("<<SMS Box Unchecked", "point2A");

                        attendanceViewResult.get(position).setSmsStatus(false);
                        // Doing same procedure as above for the sms

                        stud_sms_data_withoutDuplicates.remove(msd_id);
                        stud_sms_data_final = new ArrayList<String>(stud_sms_data_withoutDuplicates);
                        selectedSMSCheckbox = android.text.TextUtils.join(",",stud_sms_data_final );
                        Log.d("<<Aft Remove SMS Selct", selectedSMSCheckbox );

                    }

                    else if (((CheckBox) v).isChecked()) {
                        Log.d("<<SMS Box checked", "point2A");
                        Log.d("<<Add_attendence_Adapte", "point2");

                        Log.d("<<Add_attendence_Adapte", "point4");

                        attendanceViewResult.get(position).setSmsStatus(true);
                        boolean check = attendanceViewResult.get(position).getSmsStatus();
                        Log.d("<<SMS checkbox value", String.valueOf(check));

                        msd_id = attendanceViewResult.get(position).msd_id;
                        Log.d("<<Student msd Id",msd_id);

                        stud_sms_data_withoutDuplicates.add(msd_id);
                        stud_sms_data_final = new ArrayList<>(stud_sms_data_withoutDuplicates);
                        selectedSMSCheckbox = android.text.TextUtils.join(",",stud_sms_data_final );

                        Log.d("<<selectedSMSCheckbox",selectedSMSCheckbox);

                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

        });

        //For absent reason Edittext
        //viewHolder.absent_reason.clearFocus();
      /*  viewHolder.absent_reason.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

              //When focus is lost save the entered value for later use

                if (!hasFocus){
                    String reason_entered = ((EditText)v).getText().toString();
                    //absent_reasons_array[position] = reason_entered;
                   *//* if(reason_entered.isEmpty()==false) {*//*
                        attendanceViewResult.get(position).setAbsentReason(reason_entered);
                        Log.d("<< Reason inserted:", attendanceViewResult.get(position).getAbsentReason());
                        msd_id = attendanceViewResult.get(position).msd_id;
                        absent_reason_map.put(msd_id, reason_entered);
                        //absent_reason_map.remove(msd_id);
                        Log.d("<< Reason entered for:", msd_id + ":" + reason_entered);
                   *//* } else{
                        attendanceViewResult.get(position).setAbsentReason("");
                        absent_reason_map.remove(msd_id);
                    }*//*

                }
            }
        });

        // when press done on keyboard after entering reason, store entered text
        viewHolder.absent_reason.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    String reason_entered = ((EditText)v).getText().toString();
                *//*    if(reason_entered.isEmpty()==false) {*//*
                        //absent_reasons_array[position] = reason_entered;
                        attendanceViewResult.get(position).setAbsentReason(reason_entered);
                        Log.d("<< Reason inserted:", attendanceViewResult.get(position).getAbsentReason());
                        msd_id = attendanceViewResult.get(position).msd_id;
                        absent_reason_map.put(msd_id, reason_entered);
                        Log.d("<< Reason entered for:", msd_id + ":" + reason_entered);
                  *//*  }
                    else{
                        attendanceViewResult.get(position).setAbsentReason("");
                        absent_reason_map.remove(msd_id);
                    }*//*
                }
                return false;
            }
        });*/

        return convertedView;
    }


}


