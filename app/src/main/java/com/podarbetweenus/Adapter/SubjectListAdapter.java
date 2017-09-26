package com.podarbetweenus.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.podarbetweenus.Entity.SubjectResult;
import com.podarbetweenus.R;

import java.util.ArrayList;

/**
 * Created by Gayatri on 1/29/2016.
 */
public class SubjectListAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    Context context;
    String classs,std,div;
    private ArrayList<SubjectResult> subjectResult_list = new ArrayList<SubjectResult>();

    public SubjectListAdapter(Context context,ArrayList<SubjectResult> subjectResult_list) {
        this.context = context;
        this.subjectResult_list = subjectResult_list;
        this.layoutInflater = layoutInflater.from(this.context);
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return subjectResult_list.size();
    }

    @Override
    public Object getItem(int position) {
        return subjectResult_list.get(position);
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
            showLeaveStatus = layoutInflater.inflate(R.layout.subject_list_item, null);
            holder.tv_standard_value = (TextView) showLeaveStatus.findViewById(R.id.tv_section_value);
            holder.tv_shift_value = (TextView) showLeaveStatus.findViewById(R.id.tv_shift_value);
          //  holder.tv_division_value = (TextView) showLeaveStatus.findViewById(R.id.tv_division_value);
            holder.rl_next = (RelativeLayout) showLeaveStatus.findViewById(R.id.rl_next);
            holder.rl_teacher_img = (RelativeLayout) showLeaveStatus.findViewById(R.id.rl_teacher_img);
            holder.ll_content = (LinearLayout) showLeaveStatus.findViewById(R.id.ll_content);
            holder.ll_top = (LinearLayout) showLeaveStatus.findViewById(R.id.ll_top);
            holder.view_vertical_line = (View) showLeaveStatus.findViewById(R.id.view_vertical_line);
        } else
        {
            holder = (ViewHolder) showLeaveStatus.getTag();
        }
        showLeaveStatus.setTag(holder);
        holder.rl_next.setBackgroundColor(Color.parseColor("#bd8e15"));
        holder.ll_content.setBackgroundColor(Color.parseColor("#e4a502"));
        holder.ll_top.setBackgroundColor(Color.parseColor("#bd8e15"));
        holder.rl_teacher_img.setBackgroundColor(Color.parseColor("#bd8e15"));
        holder.view_vertical_line.setBackgroundColor(Color.parseColor("#bd8e15"));
        try {
            classs = subjectResult_list.get(position).classs;
            String[] split = classs.split("-");
            std = split[0];
            div = split[1];
        }
        catch (Exception e){
            e.printStackTrace();
        }

       // holder.tv_division_value.setText(div);
        holder.tv_standard_value.setText(classs);
        holder.tv_shift_value.setText(subjectResult_list.get(position).sbj_name);
        return showLeaveStatus;
    }
    public static class ViewHolder
    {
        TextView tv_standard_value,tv_shift_value,tv_division_value;
        RelativeLayout rl_next,rl_teacher_img;
        LinearLayout ll_content,ll_top;
        View view_vertical_line;
    }

}
