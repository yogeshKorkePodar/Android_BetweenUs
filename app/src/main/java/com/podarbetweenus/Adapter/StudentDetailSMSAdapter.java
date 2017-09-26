package com.podarbetweenus.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.podarbetweenus.Entity.AdminDropResult;
import com.podarbetweenus.R;

import java.util.ArrayList;

/**
 * Created by Gayatri on 3/29/2016.
 */
public class StudentDetailSMSAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    Context context;
    String date;
    private ArrayList<com.podarbetweenus.Entity.AdminDropResult> AdminDropResult = new ArrayList<AdminDropResult>();
    public StudentDetailSMSAdapter(Context context,
                                      ArrayList<AdminDropResult> AdminDropResult) {
        this.context = context;
        this.AdminDropResult = AdminDropResult;
        this.layoutInflater = layoutInflater.from(this.context);
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return AdminDropResult.size();
    }

    @Override
    public Object getItem(int position) {
        return AdminDropResult.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View showLeaveStatus = convertView;

        if (showLeaveStatus == null)
        {
            holder = new ViewHolder();
            showLeaveStatus = layoutInflater.inflate(R.layout.sms_list_item, null);
            holder.tv_shift_value = (TextView) showLeaveStatus.findViewById(R.id.tv_shift_value);
            holder.tv_section_value = (TextView) showLeaveStatus.findViewById(R.id.tv_section_value);
            holder.tv_standard_value = (TextView) showLeaveStatus.findViewById(R.id.tv_section_value);
            holder.ll_top = (LinearLayout) showLeaveStatus.findViewById(R.id.ll_top);
            holder.select_checkbox = (CheckBox) showLeaveStatus.findViewById(R.id.select_checkbox);

        } else
        {
            holder = (ViewHolder) showLeaveStatus.getTag();
        }
        showLeaveStatus.setTag(holder);
        if(position % 2 ==0){
            holder.ll_top.setBackgroundResource(R.color.sms_listview_even_row);

        }
        else {
            holder.ll_top.setBackgroundResource(R.color.sms_listview_odd_row);

        }
        holder.tv_section_value.setText(AdminDropResult.get(position).sec_Name);
        holder.tv_shift_value.setText(AdminDropResult.get(position).sft_name);
        holder.tv_standard_value.setText(AdminDropResult.get(position).std_Name+"-"+AdminDropResult.get(position).div_name);
        return showLeaveStatus;
    }

    public static class ViewHolder
    {
        TextView tv_standard_value,tv_shift_value,tv_section_value;
        LinearLayout ll_top;
        CheckBox select_checkbox;
    }

}
