package com.podarbetweenus.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.podarbetweenus.Activities.ViewTeacherReceiverList;
import com.podarbetweenus.Entity.MsgReciverResult;
import com.podarbetweenus.R;
import com.podarbetweenus.Utility.BadgeView;

import java.util.ArrayList;

/**
 * Created by Gayatri on 4/30/2016.
 */
public class ReceiverAdapter extends BaseAdapter
{
    LayoutInflater layoutInflater;
    Context context;
    String date;
    ViewTeacherReceiverList viewTeacherReceiverList = new ViewTeacherReceiverList(); ;
    int notificationId;
    String pmg_id,clt_id,usl_id,board_name,school_name,academic_year,org_id,admin_name,version_name;
    ArrayList<com.podarbetweenus.Entity.MsgReciverResult> MsgReciverResult = new ArrayList<MsgReciverResult>();
    public ReceiverAdapter(Context context,ArrayList<MsgReciverResult> MsgReciverResult) {
        this.context = context;

        this.MsgReciverResult = MsgReciverResult;
        this.layoutInflater = layoutInflater.from(this.context);
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return MsgReciverResult.size();
    }

    @Override
    public Object getItem(int position) {
        return MsgReciverResult.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.d("<<Inside","ReceiverAdapter");
        ViewHolder holder = null;
        View showLeaveStatus = convertView;

        if (showLeaveStatus == null)
        {
            holder = new ViewHolder();
            showLeaveStatus = layoutInflater.inflate(R.layout.receiver_listitem, null);
            holder.tv_name = (TextView) showLeaveStatus.findViewById(R.id.tv_name);
            holder.tv_sr_no = (TextView) showLeaveStatus.findViewById(R.id.tv_sr_no);
            holder.tv_standard_value = (TextView)showLeaveStatus.findViewById(R.id.tv_standard_value);
            holder.ll_top_layout = (LinearLayout) showLeaveStatus.findViewById(R.id.ll_top_layout);

        } else
        {
            holder = (ViewHolder) showLeaveStatus.getTag();
        }
        showLeaveStatus.setTag(holder);

        holder.tv_name.setText(MsgReciverResult.get(position).Name);
        if(MsgReciverResult.get(position).sft_name.equalsIgnoreCase("")&& MsgReciverResult.get(position).std_Name.equalsIgnoreCase("")&&MsgReciverResult.get(position).div_name.equalsIgnoreCase(""))
        {
            holder.tv_standard_value.setText("");
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT, 3.5f);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(2,0,0,0);
            holder.tv_name.setLayoutParams(params);
            holder.tv_name.setLayoutParams(param);

           // viewTeacherReceiverList.tv_standard_value.setVisibility(View.GONE);
           // viewTeacherReceiverList.tv_section_value.setVisibility(View.GONE);
            holder.tv_standard_value.setVisibility(View.GONE);
        }
        else {

           // viewTeacherReceiverList.tv_standard_value.setVisibility(View.VISIBLE);
           // viewTeacherReceiverList.tv_section_value.setVisibility(View.VISIBLE);
            holder.tv_standard_value.setVisibility(View.VISIBLE);
            holder.tv_standard_value.setText(MsgReciverResult.get(position).sft_name + "-" + MsgReciverResult.get(position).std_Name + "-" + MsgReciverResult.get(position).div_name);
        }
        holder.tv_sr_no.setText(MsgReciverResult.get(position).SrNo);
        if(position % 2 ==0){
            holder.ll_top_layout.setBackgroundResource(R.color.message_vertical_line);
            holder.tv_sr_no.setBackgroundResource(R.color.message_listview_even_row);
            holder.tv_standard_value.setBackgroundResource(R.color.message_listview_even_row);
            holder.tv_name.setBackgroundResource(R.color.message_listview_even_row);
        }
        else {
            holder.ll_top_layout.setBackgroundResource(R.color.message_vertical_line);
            holder.tv_sr_no.setBackgroundResource(R.color.message_listview_odd_row);
            holder.tv_standard_value.setBackgroundResource(R.color.message_listview_odd_row);
            holder.tv_name.setBackgroundResource(R.color.message_listview_odd_row);
        }
        return showLeaveStatus;
    }

    public static class ViewHolder
    {
        TextView tv_standard_value,tv_sr_no,tv_name;
        BadgeView badge;
        LinearLayout ll_top_layout;
    }
}
