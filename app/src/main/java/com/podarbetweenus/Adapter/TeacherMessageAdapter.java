package com.podarbetweenus.Adapter;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.podarbetweenus.Activities.ViewAdminReceiverList;
import com.podarbetweenus.Activities.ViewTeacherReceiverList;
import com.podarbetweenus.Entity.ViewMessageResult;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;
import com.podarbetweenus.Utility.BadgeView;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Gayatri on 2/11/2016.
 */
public class TeacherMessageAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;
    String date;
    int notificationId;
    String pmg_id,clt_id,usl_id,board_name,school_name,academic_year,org_id,teacher_name,version_name,
            teacher_div,teacher_shift,teacher_std,msd_id,attachment,attachment_name,attachment_new,encodedUrl,filename,newFile;

    String serverAddress = "betweenus.in/Uploads/Messages";
    ArrayList<ViewMessageResult> ViewMessageResult = new ArrayList<ViewMessageResult>();
    public TeacherMessageAdapter(Context context,ArrayList<ViewMessageResult> ViewMessageResult,int notificationId,String clt_id,String usl_id,String school_name,String academic_year,String org_id,String teacher_name,String teacher_div,String teacher_shift,String teacher_std,String board_name,String version_name) {
        this.context = context;
        this.clt_id = clt_id;
        this.usl_id = usl_id;
        this.academic_year = academic_year;
        this.board_name = board_name;
        this.school_name = school_name;
        this.teacher_name = teacher_name;
        this.teacher_div = teacher_div;
        this.teacher_shift = teacher_shift;
        this.teacher_std = teacher_std;
        this.board_name = board_name;
        this.version_name = version_name;
        this.msd_id = msd_id;
        this.org_id = org_id;
        this.ViewMessageResult = ViewMessageResult;
        this.notificationId = notificationId;
        this.layoutInflater = layoutInflater.from(this.context);
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return ViewMessageResult.size();
    }

    @Override
    public Object getItem(int position) {
        return ViewMessageResult.get(position);
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
            showLeaveStatus = layoutInflater.inflate(R.layout.teacher_message_item, null);
            holder.tv_sender_name = (TextView) showLeaveStatus.findViewById(R.id.tv_sender_name);
            holder.tv_sub_name = (TextView) showLeaveStatus.findViewById(R.id.tv_sub_name);
            holder.tv_msg = (TextView)showLeaveStatus.findViewById(R.id.tv_msg);
            holder.tv_date = (TextView)showLeaveStatus.findViewById(R.id.tv_date);
            holder.tv_notifiaction_count = (TextView) showLeaveStatus.findViewById(R.id.tv_notifiaction_count);
            holder.tv_receiver_list = (TextView) showLeaveStatus.findViewById(R.id.tv_receiver_list);
            holder.img_attachment = (ImageView) showLeaveStatus.findViewById(R.id.img_attachment);
            holder.rl_receiver = (RelativeLayout) showLeaveStatus.findViewById(R.id.rl_receiver);
            holder.btn_receiver = (Button) showLeaveStatus.findViewById(R.id.btn_receiver);
            holder.badge = new BadgeView(context, holder.tv_notifiaction_count);
            holder.badge.setBadgeBackgroundColor(Color.RED);
            holder.badge.setTextColor(Color.WHITE);
            if(AppController.TeacherTabPosition==0)
            {
                holder.rl_receiver.setVisibility(View.GONE);
            }
            else if(AppController.TeacherTabPosition==2){
                holder.rl_receiver.setVisibility(View.VISIBLE);
            }

        } else
        {
            holder = (ViewHolder) showLeaveStatus.getTag();
        }
        showLeaveStatus.setTag(holder);

        holder.tv_sub_name.setText(ViewMessageResult.get(position).pmg_subject);
        holder.tv_msg.setText(ViewMessageResult.get(position).pmg_Message);
        holder.tv_date.setText(ViewMessageResult.get(position).pmg_date);
        if(ViewMessageResult.get(position).Fullname.equalsIgnoreCase("")){
            holder.tv_sender_name.setVisibility(View.GONE);
        }
        else{
            holder.tv_sender_name.setVisibility(View.VISIBLE);
            holder.tv_sender_name.setText(ViewMessageResult.get(position).Fullname);
        }
        if(ViewMessageResult.get(position).pmg_file_path.equalsIgnoreCase("0") || (ViewMessageResult.get(position).pmg_file_path.equalsIgnoreCase(""))){

            holder.img_attachment.setVisibility(View.GONE);
        }
        else if(!ViewMessageResult.get(position).pmg_file_path.equalsIgnoreCase("0")){
            holder.img_attachment.setVisibility(View.VISIBLE);
        }
        try {
            holder.img_attachment.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    attachment = ViewMessageResult.get(position).pmg_file_path;
                    attachment_new = attachment.replace("C:\\inetpub\\", "http:\\");
                    attachment_name = ViewMessageResult.get(position).pmg_file_name;
                    try {
                        //Download the file
                        try {
                            try {
                                String host = "http://" + serverAddress + "/";
                                filename = attachment_new.substring(attachment_new.lastIndexOf('/') + 1);
                                Log.e("filename",filename);
                                newFile =host+filename;
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
        if(ViewMessageResult.get(position).pmu_readunreadstatus.equalsIgnoreCase("") || ViewMessageResult.get(position).pmu_readunreadstatus.equalsIgnoreCase("0")){

            holder.tv_msg.setTypeface(Typeface.DEFAULT);
            holder.badge.hide();
        }
        else {
            holder.tv_msg.setTypeface(Typeface.DEFAULT_BOLD);
            holder.badge.setText("1");
            holder.badge.show();
        }

        if(ViewMessageResult.get(position).pmg_Message.equalsIgnoreCase(""))
        {
            holder.tv_msg.setVisibility(View.GONE);
        }
        else{

            holder.tv_msg.setVisibility(View.VISIBLE);
            holder.tv_msg.setText(ViewMessageResult.get(position).pmg_Message.trim());
        }
        final ViewHolder finalHolder = holder;
        finalHolder.btn_receiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pmg_id = ViewMessageResult.get(position).pmg_ID;
                final DataFetchService dft = new DataFetchService((Context)context);
                if (dft.isInternetOn() == true) {
                    Intent viewReceiver = new Intent(context, ViewTeacherReceiverList.class);
                    viewReceiver.putExtra("clt_id", clt_id);
                    viewReceiver.putExtra("msdID", msd_id);
                    viewReceiver.putExtra("usl_id", usl_id);
                    viewReceiver.putExtra("School_name", school_name);
                    viewReceiver.putExtra("Teacher_name", teacher_name);
                    viewReceiver.putExtra("version_name", version_name);
                    viewReceiver.putExtra("board_name", board_name);
                    viewReceiver.putExtra("Teacher_div", teacher_div);
                    viewReceiver.putExtra("Teacher_Shift",teacher_shift);
                    viewReceiver.putExtra("Tecaher_Std",teacher_std);
                    viewReceiver.putExtra("academic_year",academic_year);
                    viewReceiver.putExtra("pmg_id",pmg_id);
                    context.startActivity(viewReceiver);
                }
            }
        });
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
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().toString() +"/"+filename);
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
        TextView tv_sender_name,tv_sub_name,tv_msg,tv_date,tv_notifiaction_count,tv_receiver_list;
        BadgeView badge;
        Button btn_receiver;
        ImageView img_attachment;
        RelativeLayout rl_receiver;
    }

}
