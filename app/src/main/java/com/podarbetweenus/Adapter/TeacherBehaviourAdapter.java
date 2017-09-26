package com.podarbetweenus.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.podarbetweenus.Activities.ViewBehaviourActivity;
import com.podarbetweenus.Entity.TeacherBehavioutStudResult;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;

import java.util.ArrayList;

/**
 * Created by Gayatri on 2/1/2016.
 */
public class TeacherBehaviourAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    Context context;
    String clt_id,msd_id,board_name,school_name,usl_id,teacher_name,version_name;
    ArrayList<TeacherBehavioutStudResult> TeacherBehaviourlist = new ArrayList<TeacherBehavioutStudResult>();
    public TeacherBehaviourAdapter(Context context,ArrayList<TeacherBehavioutStudResult> TeacherBehaviourlist,String clt_id,String board_name,String usl_id,String version_name,String teacher_name,String school_name) {
        this.context = context;
        this.TeacherBehaviourlist = TeacherBehaviourlist;
        this.clt_id = clt_id;
        this.board_name = board_name;
        this.usl_id = usl_id;
        this.version_name = version_name;
        this.teacher_name = teacher_name;
        this.school_name = school_name;
        this.layoutInflater = layoutInflater.from(this.context);
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return TeacherBehaviourlist.size();
    }

    @Override
    public Object getItem(int position) {
        return TeacherBehaviourlist.get(position);
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
            showLeaveStatus = layoutInflater.inflate(R.layout.teacher_behaviour_list_item, null);
            holder.tv_student_name = (TextView) showLeaveStatus.findViewById(R.id.tv_student_name);
            holder.student_roll_no = (TextView) showLeaveStatus.findViewById(R.id.student_roll_no);
            holder.ll_view_behaviour = (LinearLayout)showLeaveStatus.findViewById(R.id.ll_view_behaviour);
            holder.ll_main = (LinearLayout) showLeaveStatus.findViewById(R.id.ll_main);
            holder.ll_view_behaviour = (LinearLayout) showLeaveStatus.findViewById(R.id.ll_view_behaviour);
            holder.ll_roll_no = (LinearLayout) showLeaveStatus.findViewById(R.id.ll_roll_no);

        } else
        {
            holder = (ViewHolder) showLeaveStatus.getTag();
        }
        showLeaveStatus.setTag(holder);

        holder.student_roll_no.setText(TeacherBehaviourlist.get(position).Roll_No);
        holder.tv_student_name.setText(TeacherBehaviourlist.get(position).stu_display);

        if(position % 2==0){
            holder.ll_main.setBackgroundColor(Color.parseColor("#000000"));
            holder.ll_roll_no.setBackgroundResource(R.color.behaviour_listview_even_row);
            holder.ll_view_behaviour.setBackgroundResource(R.color.behaviour_listview_even_row);
            holder.tv_student_name.setBackgroundResource(R.color.behaviour_listview_even_row);
        }
        else{
            holder.ll_main.setBackgroundColor(Color.parseColor("#000000"));
            holder.ll_roll_no.setBackgroundResource(R.color.behaviour_listview_odd_row);
            holder.ll_view_behaviour.setBackgroundResource(R.color.behaviour_listview_odd_row);
            holder.tv_student_name.setBackgroundResource(R.color.behaviour_listview_odd_row);
        }

        final ViewHolder finalHolder = holder;
        finalHolder.ll_view_behaviour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msd_id = TeacherBehaviourlist.get(position).msd_id;
                final DataFetchService dft = new DataFetchService((Context)context);
                if (dft.isInternetOn() == true) {
                    Intent viewBehaviour = new Intent(context, ViewBehaviourActivity.class);

                    viewBehaviour.putExtra("clt_id", clt_id);
                    viewBehaviour.putExtra("msdID", msd_id);
                    viewBehaviour.putExtra("usl_id", usl_id);
                    viewBehaviour.putExtra("School_name", school_name);
                    viewBehaviour.putExtra("Teacher_name", teacher_name);
                    viewBehaviour.putExtra("version_name", version_name);
                    viewBehaviour.putExtra("board_name", board_name);
                    context.startActivity(viewBehaviour);
                }
            }
        });
        return showLeaveStatus;
    }

    public static class ViewHolder
    {
        TextView student_roll_no,tv_student_name;
        LinearLayout ll_view_behaviour,ll_main,ll_roll_no;
    }
}
