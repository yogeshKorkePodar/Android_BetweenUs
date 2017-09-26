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

import com.podarbetweenus.Entity.AcedmicYearResult;
import com.podarbetweenus.Entity.DivisionResult;
import com.podarbetweenus.Entity.MonthResult;
import com.podarbetweenus.Entity.SectionList;
import com.podarbetweenus.Entity.ShiftResult;
import com.podarbetweenus.Entity.StandardResult;
import com.podarbetweenus.Entity.TeacherAttendaceResult;
import com.podarbetweenus.R;

import java.util.ArrayList;

/**
 * Created by Gayatri on 1/30/2016.
 */
public class TeacherAttendanceAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    Context context;
    ArrayList<TeacherAttendaceResult> TeacherAttendacelist = new ArrayList<TeacherAttendaceResult>();
    public ArrayList<AcedmicYearResult> AcedmicYearResult = new ArrayList<AcedmicYearResult>();
    public ArrayList<DivisionResult> DivisionResult = new ArrayList<DivisionResult>();
    public ArrayList<ShiftResult> ShiftResult = new ArrayList<ShiftResult>();
    public ArrayList<SectionList> SectionList = new ArrayList<SectionList>();
    public ArrayList<StandardResult> StandardResult = new ArrayList<StandardResult>();
    public ArrayList<MonthResult> MonthResult = new ArrayList<MonthResult>();

    public TeacherAttendanceAdapter(Context context,ArrayList<StandardResult>StandardResult,
                               ArrayList<AcedmicYearResult> AcedmicYearResult,ArrayList<DivisionResult>DivisionResult,ArrayList<MonthResult>MonthResult,ArrayList<ShiftResult>ShiftResult,ArrayList<SectionList> SectionList) {
        this.context = context;
        this.TeacherAttendacelist = TeacherAttendacelist;
        this.AcedmicYearResult = AcedmicYearResult;
        this.DivisionResult = DivisionResult;
        this.MonthResult = MonthResult;
        this.StandardResult = StandardResult;
        this.ShiftResult = ShiftResult;
        this.SectionList = SectionList;
        this.layoutInflater = layoutInflater.from(this.context);
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {

        return StandardResult.size();
    }

    @Override
    public Object getItem(int position) {

        return StandardResult.get(position);
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
            showLeaveStatus = layoutInflater.inflate(R.layout.attendance_selection_list_item, null);
            holder.tv_section_value = (TextView) showLeaveStatus.findViewById(R.id.tv_section_value);
            holder.tv_shift_value = (TextView) showLeaveStatus.findViewById(R.id.tv_shift_value);
            holder.tv_division_value = (TextView) showLeaveStatus.findViewById(R.id.tv_division_value);
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
        holder.rl_next.setBackgroundColor(Color.parseColor("#026579"));
        holder.ll_content.setBackgroundColor(Color.parseColor("#1f899e"));
        holder.ll_top.setBackgroundColor(Color.parseColor("#026579"));
        holder.rl_teacher_img.setBackgroundColor(Color.parseColor("#026579"));
        holder.view_vertical_line.setBackgroundColor(Color.parseColor("#026579"));

        holder.tv_section_value.setText(SectionList.get(position).sec_Name);
        holder.tv_shift_value.setText(ShiftResult.get(position).sft_name);
        holder.tv_division_value.setText(StandardResult.get(position).std_Name+" - "+DivisionResult.get(position).div_name);
        /*Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roman_SD.TTF");
        holder.tv_section_value.setTypeface(tf);*/
        return showLeaveStatus;
    }

    public static class ViewHolder
    {
        TextView tv_section_value,tv_shift_value,tv_division_value;
        RelativeLayout rl_next,rl_teacher_img;
        LinearLayout ll_content,ll_top;
        View view_vertical_line;
    }

}
