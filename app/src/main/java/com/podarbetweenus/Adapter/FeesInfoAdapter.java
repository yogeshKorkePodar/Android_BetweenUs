package com.podarbetweenus.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.podarbetweenus.Entity.PaymentList;
import com.podarbetweenus.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 12/2/2015.
 */
public class FeesInfoAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;
    private ArrayList<PaymentList> paymentLists = new ArrayList<PaymentList>();
    String date,receiptNo;

    public FeesInfoAdapter(Context context,
                            ArrayList<PaymentList> paymentLists) {
        this.context = context;
        this.paymentLists = paymentLists;
        this.layoutInflater = layoutInflater.from(this.context);
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return paymentLists.size();
    }

    @Override
    public Object getItem(int position) {
        return paymentLists.get(position);
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
            showLeaveStatus = layoutInflater.inflate(R.layout.template_pay_history, null);
            holder.tv_payment_mode_value = (TextView) showLeaveStatus.findViewById(R.id.tv_payment_mode_value);
            holder.tv_receipt_date_value = (TextView) showLeaveStatus.findViewById(R.id.tv_receipt_date_value);
            holder.tv_receipt_value = (TextView) showLeaveStatus.findViewById(R.id.tv_receipt_value);
            holder.tv_desc_value = (TextView) showLeaveStatus.findViewById(R.id.tv_desc_value);
            holder.tv_recpt_no_value = (TextView) showLeaveStatus.findViewById(R.id.tv_recpt_no_value);
            holder.tv_amount_value = (TextView) showLeaveStatus.findViewById(R.id.tv_amount_value);

        } else
        {
            holder = (ViewHolder) showLeaveStatus.getTag();
        }
        showLeaveStatus.setTag(holder);
        holder.tv_desc_value.setText(paymentLists.get(position).trm_Name);
        receiptNo = paymentLists.get(position).Pay_ReciptNo.trim();
        String recNo = receiptNo.replaceAll(" ","");
        Log.e("Rec",recNo);
        holder.tv_recpt_no_value.setText(recNo);
        holder.tv_amount_value.setText(paymentLists.get(position).pay_Amount);
        holder.tv_payment_mode_value.setText(paymentLists.get(position).pym_Name);
        try {
            date = paymentLists.get(position).Pay_ReciptDate;
            String date_array[] = date.split(" ");
            String pay_recpt_date = date_array[0];
            String inputPattern = "MM/dd/yyyy";
            String outputPattern = "dd-MM-yyyy";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

            Date date = null;
            String formatted_date = null;

            try {
                date = inputFormat.parse(pay_recpt_date);
                formatted_date = outputFormat.format(date);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.tv_receipt_date_value.setText(formatted_date);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return showLeaveStatus;
    }

    public static class ViewHolder
    {
        TextView tv_receipt_date_value,tv_receipt_value,tv_desc_value,tv_recpt_no_value,tv_amount_value,tv_payment_mode_value;
    }
}
