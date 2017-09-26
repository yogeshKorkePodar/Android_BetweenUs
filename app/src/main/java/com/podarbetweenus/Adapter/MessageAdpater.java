package com.podarbetweenus.Adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.podarbetweenus.Entity.ViewMessageResult;
import com.podarbetweenus.R;
import com.podarbetweenus.Utility.BadgeView;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 10/26/2015.
 */
public class MessageAdpater extends BaseAdapter {
    LayoutInflater layoutInflater;
    Context context;
    Dialog dialog;
    private String location;
    String student_name,attachment,attachment_name,attachment_new,date,encodedUrl,filename,newFile;
    int totalSize = 0;
    int notificationId,msgStatusSize;
    // Progress Dialog
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
    String serverAddress = "betweenus.in/Uploads/Messages";
    private ArrayList<ViewMessageResult> message_list = new ArrayList<ViewMessageResult>();

    public  MessageAdpater(Context context,
                              ArrayList<ViewMessageResult> message_list,int notificationId) {
        this.context = context;
        this.message_list = message_list;
        this.layoutInflater = layoutInflater.from(this.context);
        this.notificationId = notificationId;
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
            showLeaveStatus = layoutInflater.inflate(R.layout.template_message, null);
            holder.tv_sender_name = (TextView) showLeaveStatus.findViewById(R.id.tv_sender_name);
            holder.tv_sub_name = (TextView) showLeaveStatus.findViewById(R.id.tv_sub_name);
            holder.tv_date = (TextView) showLeaveStatus.findViewById(R.id.tv_date);
            holder.tv_msg = (TextView) showLeaveStatus.findViewById(R.id.tv_msg);
            holder.tv_notifiaction_count = (TextView) showLeaveStatus.findViewById(R.id.tv_notifiaction_count);
            holder.img_attachment = (ImageView)showLeaveStatus.findViewById(R.id.img_attachment);
            holder.badge = new BadgeView(context, holder.tv_notifiaction_count);
            holder.badge.setBadgeBackgroundColor(Color.RED);
            holder.badge.setTextColor(Color.WHITE);


        } else
        {
            holder = (ViewHolder) showLeaveStatus.getTag();
        }
        showLeaveStatus.setTag(holder);

        if(message_list.get(position).Fullname.equalsIgnoreCase(""))
        {
          //  holder.tv_sender_name.setText(message_list.get(position).Fullname);
                holder.tv_sender_name.setVisibility(View.GONE);
        }
        else {
            holder.tv_sender_name.setText(message_list.get(position).Fullname);
            holder.tv_sender_name.setVisibility(View.VISIBLE);
        }

        holder.tv_sub_name.setText(message_list.get(position).pmg_subject);
        date = message_list.get(position).pmg_date;
      /*  String date_array[] = date.split(" ");
        String date_format = date_array[0];
        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String formatted_date = null;

        try {
            date = inputFormat.parse(date_format);
            formatted_date = outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }*/
            holder.tv_date.setText(date);

        if(message_list.get(position).pmu_readunreadstatus.equalsIgnoreCase("0")||message_list.get(position).pmu_readunreadstatus.equalsIgnoreCase(""))
        {
            holder.tv_msg.setTypeface(Typeface.DEFAULT);
            holder.tv_msg.setText(message_list.get(position).pmg_Message.trim());
            holder.badge.hide();
        }
        else {
                holder.tv_msg.setTypeface(Typeface.DEFAULT_BOLD);
                holder.tv_msg.setText(message_list.get(position).pmg_Message.trim());
                holder.badge.setText("1");
                holder.badge.show();
            }

        if(message_list.get(position).pmg_file_path.equalsIgnoreCase("0") || (message_list.get(position).pmg_file_path.equalsIgnoreCase(""))){

            holder.img_attachment.setVisibility(View.GONE);
        }
        else if(!message_list.get(position).pmg_file_path.equalsIgnoreCase("0")){
            holder.img_attachment.setVisibility(View.VISIBLE);
        }
        try {
            holder.img_attachment.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    attachment = message_list.get(position).pmg_file_path;
                    attachment_new = attachment.replace("C:\\inetpub\\", "http:\\");
                    attachment_name = message_list.get(position).pmg_file_name;
                    try {
                        //Download the file
                        try {
                            try {
                                String host = "http://" + serverAddress + "/";
                                filename = attachment_new.substring(attachment_new.lastIndexOf('/') + 1);
                                Log.e("filename",filename);
                                newFile = host+filename;
                                Log.e("NEW FILE",newFile);
                                encodedUrl = host + URLEncoder.encode(filename, "utf-8");
                                Log.e("enco url",encodedUrl);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            new DownloadFileFromURL().execute(newFile);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(newFile));
                        context.startActivity(i);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }

            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return showLeaveStatus;
    }


    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          // context.showDialog(progress_bar_type);

        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {

                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();
                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);
                // Output stream
                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        +"/"+filename);
                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
          //  progressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
          //  dismissDialog(progress_bar_type);
            Toast.makeText(context, "Downloading Completed", Toast.LENGTH_SHORT).show();
        }

    }
    public static class ViewHolder
    {
        TextView tv_sender_name,tv_sub_name,tv_date,tv_msg,tv_notifiaction_count;
        //ImageView
        ImageView img_attachment;
        BadgeView badge;
    }

}

