package com.podarbetweenus.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.podarbetweenus.Entity.ViewMessageResult;
import com.podarbetweenus.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 10/29/2015.
 */
public class DetailsMessageAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    Context context;
    Dialog dialog;
    private String location;
    String student_name,attachment,attachment_name,attachment_new;
    int totalSize = 0;
    private ArrayList<ViewMessageResult> message_list = new ArrayList<ViewMessageResult>();

    public DetailsMessageAdapter(Context context,
                          ArrayList<ViewMessageResult> message_list) {
        this.context = context;
        this.message_list = message_list;
        this.layoutInflater = layoutInflater.from(this.context);
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return message_list.size();
    }

    @Override
    public Object getItem(int position) {
        return message_list.get(position);
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
            showLeaveStatus = layoutInflater.inflate(R.layout.template_detail_msg, null);
            holder.tv_detail_msg = (TextView) showLeaveStatus.findViewById(R.id.tv_detail_msg);
            holder.tv_date = (TextView) showLeaveStatus.findViewById(R.id.tv_date);
            holder.img_attachment = (ImageView)showLeaveStatus.findViewById(R.id.img_attachment);


        } else
        {
            holder = (ViewHolder) showLeaveStatus.getTag();
        }
        showLeaveStatus.setTag(holder);

        if(message_list.get(position).pmg_file_path.equalsIgnoreCase("0")){
            holder.img_attachment.setVisibility(View.GONE);
        }
        else {
            holder.img_attachment.setVisibility(View.VISIBLE);
        }


        holder.tv_date.setText(message_list.get(position).pmg_date);
        holder.tv_detail_msg.setText(message_list.get(position).pmg_Message);
        final ViewHolder finalHolder = holder;
        finalHolder.img_attachment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                attachment = message_list.get(position).pmg_file_path;
                attachment_new = attachment.replace("C:\\inetpub\\","http:\\");
                Log.e("Attch", attachment_new);
                attachment_name = message_list.get(position).pmg_file_name;
                //Download the file
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(attachment_new));
                context.startActivity(i);
            }

        });
        return showLeaveStatus;
    }



    public static class ViewHolder
    {
        TextView tv_date,tv_detail_msg;
        //ImageView
        ImageView img_attachment;
    }
}

