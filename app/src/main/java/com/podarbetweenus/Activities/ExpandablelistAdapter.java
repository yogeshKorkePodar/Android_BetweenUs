package com.podarbetweenus.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.Adapter.AbsentHistoryAdapter;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.AttendHistory;
import com.podarbetweenus.Entity.DateDropdownValueDetails;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;
import com.podarbetweenus.Utility.AppController;
import com.podarbetweenus.Utility.CircularProgressBar;
import com.podarbetweenus.charts.PieChart;
import com.podarbetweenus.data.Entry;
import com.podarbetweenus.data.PieData;
import com.podarbetweenus.data.PieDataSet;
import com.podarbetweenus.utils.ColorTemplate;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 9/24/2015.
 */
public class ExpandablelistAdapter extends BaseExpandableListAdapter{

    private Activity context;

    Context con;
    private Map<String, List<String>> laptopCollections;
    private List<String> grouplist;
    private List<Integer> laptopsicons;
    ArrayList<DateDropdownValueDetails> date_list;
    ArrayList<AttendHistory> AttendHistoryList;
    TextView tv_upsent,tv_present,tv_absent_history,tv_notMarked;
    ImageView img_close;
    EditText ed_notmarked,ed_0,ed_100;
    LoginDetails login_details = new LoginDetails();
    String progress,absent;
    float presentPercent,absentPercent,notMarkedPercent;
    int notMarked;
    ArrayList<Entry> entries = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<String>();
    String absentHistoryMethodname = "AttendanceHistory";
    PieDataSet dataset;
    PieData data;

    public ExpandablelistAdapter(Activity context, List<String> grouplist,List<Integer>laptopsicons,
                                 Map<String, List<String>> laptopCollections, ArrayList<DateDropdownValueDetails> date_list) {
        this.context = context;
        this.laptopCollections = laptopCollections;
        this.grouplist = grouplist;
        this.laptopsicons = laptopsicons;
        this.date_list = date_list;
    }
    public Object getChild(int groupPosition, int childPosition) {
        return laptopCollections.get(grouplist.get(groupPosition)).get(childPosition);

    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String laptop = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(R.layout.template_expandable_my_attendance, null);
       // CircularProgressBar present = (CircularProgressBar) convertView.findViewById(R.id.progressBar);
        PieChart present = (PieChart) convertView.findViewById(R.id.progressBar);

        tv_present = (TextView) convertView.findViewById(R.id.tv_present);
        tv_upsent = (TextView) convertView.findViewById(R.id.tv_upsent);
        tv_absent_history = (TextView) convertView.findViewById(R.id.tv_absent_history);
        tv_notMarked = (TextView) convertView.findViewById(R.id.tv_notMarked);
        ed_notmarked = (EditText)convertView.findViewById(R.id.ed_notmarked);
        ed_100 = (EditText)convertView.findViewById(R.id.ed_100);
        ed_0 = (EditText) convertView.findViewById(R.id.ed_0);
        entries.clear();
        try{
            progress = date_list.get(groupPosition).AttnPercent;
            absent = date_list.get(groupPosition).Abper;
            Log.e("progress",String.valueOf(progress));
            Log.e("absent",String.valueOf(absent));
           // absent = date_list.get(groupPosition).Abper;
           // notMarked = String.valueOf(100 - (Integer.valueOf(progress) + (Integer.valueOf(absent))));

            if(progress.equalsIgnoreCase("100")){
                tv_present.setText(progress + "% "+"Present");
                progress = date_list.get(groupPosition).AttnPercent;
                tv_upsent.setText("0% Absent");
                presentPercent = Float.parseFloat(progress);
                Log.e("Float progress",String.valueOf(presentPercent));
                entries.add(new Entry(presentPercent, 0));
                dataset = new PieDataSet(entries, "");
                labels.add("");
                data = new PieData(labels, dataset);
                dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                present.setData(data);
                present.setDrawSliceText(false);
                present.setDrawCenterText(false);
                present.getLegend().setEnabled(false);
                dataset.setDrawValues(false);
                present.animateY(3000);
                ed_notmarked.setVisibility(View.GONE);
                tv_notMarked.setVisibility(View.GONE);
                tv_absent_history.setVisibility(View.GONE);
            }
            else if(progress.equalsIgnoreCase("") && absent.equalsIgnoreCase("0")){
                entries.add(new Entry(100f, 0));
                dataset = new PieDataSet(entries, "");
                labels.add("");
                data = new PieData(labels, dataset);
                dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                present.setData(data);
                present.setDrawSliceText(false);
                present.setDrawCenterText(false);
                present.getLegend().setEnabled(false);
                dataset.setDrawValues(false);
                present.animateY(3000);
                tv_upsent.setVisibility(View.GONE);
                tv_present.setVisibility(View.GONE);
                tv_absent_history.setVisibility(View.GONE);

                tv_notMarked.setVisibility(View.VISIBLE);
                tv_notMarked.setText("     Not Marked");
                ed_notmarked.setVisibility(View.VISIBLE);
                ed_0.setVisibility(View.GONE);
                ed_100.setVisibility(View.GONE);
                ed_notmarked.setVisibility(View.VISIBLE);
                ed_notmarked.setBackgroundResource(R.drawable.edittext_withnomarked);
            }
            else if(progress.equalsIgnoreCase("") && !absent.equalsIgnoreCase("0")){
                progress = date_list.get(groupPosition).AttnPercent;
                absent = date_list.get(groupPosition).Abper;
                int notMarked = (100 - (Integer.valueOf(absent)));
                tv_upsent.setText(absent + "% " + "Absent");
                tv_notMarked.setText(notMarked + "% "+ "Not Marked");
                tv_present.setText("0" + "% "+"Present");
                absentPercent = Float.parseFloat(date_list.get(groupPosition).Abper);
                notMarkedPercent = Float.valueOf(notMarked);
                entries.add(new Entry(0.0f, 0));
                entries.add(new Entry(absentPercent, 1));
                entries.add(new Entry(notMarkedPercent, 2));
                dataset = new PieDataSet(entries, "");
                dataset = new PieDataSet(entries, "");
                dataset = new PieDataSet(entries, "");
                labels.add("");
                labels.add("");
                labels.add("");
                labels.add("");
                data = new PieData(labels, dataset);
                dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                present.setData(data);
                present.setDrawSliceText(false);
                present.setDrawCenterText(false);
                present.getLegend().setEnabled(false);
                dataset.setDrawValues(false);
                present.animateY(3000);
                tv_absent_history.setVisibility(View.VISIBLE);
                tv_notMarked.setVisibility(View.VISIBLE);
                ed_notmarked.setVisibility(View.VISIBLE);

            }

            else if (Integer.valueOf(progress) < 100) {
                progress = date_list.get(groupPosition).AttnPercent;
                absent = date_list.get(groupPosition).Abper;
                int notMarked = (100 - (Integer.valueOf(progress) +(Integer.valueOf(absent))));
                tv_upsent.setText(absent + "% " + "Absent");
                tv_notMarked.setText(notMarked + "% "+ "Not Marked");
                tv_present.setText(progress + "% "+"Present");
                presentPercent = Float.parseFloat(date_list.get(groupPosition).AttnPercent);
                absentPercent = Float.parseFloat(date_list.get(groupPosition).Abper);
                notMarkedPercent = Float.valueOf(notMarked);
                Log.e("Float not marked", String.valueOf(notMarkedPercent));
                entries.add(new Entry(presentPercent, 0));
                entries.add(new Entry(absentPercent, 1));
                entries.add(new Entry(notMarkedPercent, 2));
                dataset = new PieDataSet(entries, "");
                dataset = new PieDataSet(entries, "");
                dataset = new PieDataSet(entries, "");
                labels.add("");
                labels.add("");
                labels.add("");
                labels.add("");
                data = new PieData(labels, dataset);
                dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                present.setData(data);
                present.setDrawSliceText(false);
                present.setDrawCenterText(false);
                present.getLegend().setEnabled(false);
                dataset.setDrawValues(false);
                present.animateY(3000);
                tv_absent_history.setVisibility(View.VISIBLE);
                tv_notMarked.setVisibility(View.VISIBLE);
                ed_notmarked.setVisibility(View.VISIBLE);
            }
            if(absent.equalsIgnoreCase("0")){
                tv_absent_history.setVisibility(View.GONE);
            }
            if (tv_absent_history.getVisibility() == View.VISIBLE) {

                tv_absent_history.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String month_id = date_list.get(groupPosition).monthid;
                        String year = date_list.get(groupPosition).years;
                        callWebsrviceForAbsentHistory(AppController.msd_ID, AppController.clt_id, month_id, year);
                    }
                });
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }
    public int getChildrenCount(int groupPosition) {
        return laptopCollections.get(grouplist.get(groupPosition)).size();
    }



    public Object getGroup(int groupPosition) {
        return grouplist.get(groupPosition);
    }

    public int getGroupCount() {
        return grouplist.size();
    }

    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String laptopName = (String) getGroup(groupPosition);
        String icons = (String)getGroup(groupPosition);

        if (convertView == null) {

            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_group,
                    null);

        }
        TextView item = (TextView) convertView.findViewById(R.id.listTitle);
        RelativeLayout rl_expandable = (RelativeLayout) convertView.findViewById(R.id.rl_expandable);

        item.setTypeface(null, Typeface.BOLD);
        item.setText(laptopName);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }


    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void callWebsrviceForAbsentHistory(String msd_ID,  String clt_id,String month_id,String year) {
        final DataFetchService dft = new DataFetchService((Context)context);
        ProgressDialog progressDialog = Constant.getProgressDialog((Context) context);

        dft.getAbsentHistory(msd_ID, clt_id, month_id, year, absentHistoryMethodname, Request.Method.POST,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        ProgressDialog progressDialog = Constant.getProgressDialog((Context) context);

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                        //    DataFetchService dft = new DataFetchService((Context)context);
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                             if (login_details.Status.equalsIgnoreCase("1")) {
                                try {
                                        final Dialog alertDialog = new Dialog(context);
                                        //  final AlertDialog alertDialog = builder.create();
                                        LayoutInflater inflater = context.getLayoutInflater();
                                        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        alertDialog.getWindow().setBackgroundDrawable(
                                                new ColorDrawable(Color.TRANSPARENT));
                                        View convertView =  inflater.inflate(R.layout.absent_history, null);
                                        alertDialog.setContentView(convertView);
                                        final ListView list_absent_history = (ListView) convertView.findViewById(R.id.list_absent_history);
                                        img_close = (ImageView) convertView.findViewById(R.id.img_close);

                                        AbsentHistoryAdapter absentHistoryAdapter = new AbsentHistoryAdapter((Context) context, login_details.AttendHistory);
                                        list_absent_history.setAdapter(absentHistoryAdapter);

                                        alertDialog.show();
                                        img_close.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                alertDialog.dismiss();

                                            }
                                        });

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            } else {
                                Constant.showOkPopup(con, login_details.StatusMsg, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                });

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Show error or whatever...
                        Log.d("ExpandableListAdapter", "ERROR.._---" + error.getCause());
                    }
                });

    }
    }
