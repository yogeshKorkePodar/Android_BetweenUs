package com.podarbetweenus.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.podarbetweenus.Entity.StundentListDetails;
import com.podarbetweenus.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 10/20/2015.
 */
public class SiblingListAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    Context  context;
     private String location;
    String student_name;
    private ArrayList<StundentListDetails> student_list_details = new ArrayList<StundentListDetails>();

    public SiblingListAdapter(Context context,
                       ArrayList<StundentListDetails> student_list_details,String location) {
        this.context = context;
        this.student_list_details = student_list_details;
        this.location = location;
        this.layoutInflater = layoutInflater.from(this.context);
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return student_list_details.size();
    }

    @Override
    public Object getItem(int position) {
        return student_list_details.get(position);
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
            showLeaveStatus = layoutInflater.inflate(R.layout.template_sibling_details, null);
            holder.tv_child_name = (TextView) showLeaveStatus.findViewById(R.id.tv_child_name);
            holder.tv_child_std = (TextView) showLeaveStatus.findViewById(R.id.tv_child_std);

        } else
        {
            holder = (ViewHolder) showLeaveStatus.getTag();
        }
        showLeaveStatus.setTag(holder);
        try{
        student_name = student_list_details.get(position).StudentName;
       /* String name[] = student_name.split(" ");
        String surname = name[0];
        String firstname = name[1];*/
      //  String middlename = name[2];
        holder.tv_child_name.setText(student_name);
        //holder.tv_child_std.setText("Standard >> "+student_list_details.get(position).standard+", "+"Div "+student_list_details.get(position).Division+" "+"("+student_list_details.get(position).AcadmicYear+")");
        holder.tv_child_std.setText("Std : "+student_list_details.get(position).standard+", "+"Div : "+student_list_details.get(position).Division);
        //holder.tv_location.setText(location+" "+"("+student_list_details.get(position).Board+")");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return showLeaveStatus;
    }

    public static class ViewHolder
    {
        TextView tv_child_name,tv_child_std,tv_location;
    }

}
