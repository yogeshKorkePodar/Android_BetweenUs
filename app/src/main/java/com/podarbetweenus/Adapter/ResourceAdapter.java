package com.podarbetweenus.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.podarbetweenus.Entity.ResourceList;
import com.podarbetweenus.R;
import java.util.ArrayList;

/**
 * Created by Administrator on 11/26/2015.
 */
public class ResourceAdapter extends BaseAdapter{

    private Context context;
    LayoutInflater layoutInflater;
    Context con;
    ArrayList<ResourceList> resource_list;

    public ResourceAdapter(Context context,ArrayList<ResourceList> resource_list) {

        this.context = context;
        this.resource_list = resource_list;
        this.layoutInflater = layoutInflater.from(this.context);
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return resource_list.size();
    }

    @Override
    public Object getItem(int position) {
        return resource_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View showLeaveStatus = convertView;

        if (showLeaveStatus == null) {
            holder = new ViewHolder();
            showLeaveStatus = layoutInflater.inflate(R.layout.template_child_resource, null);
            holder.tv_resource = (TextView) showLeaveStatus.findViewById(R.id.tv_resource);
            holder.img_resource_icon = (ImageView) showLeaveStatus.findViewById(R.id.img_resource_icon);

        } else {
            holder = (ViewHolder) showLeaveStatus.getTag();
        }
        showLeaveStatus.setTag(holder);
        try {
            if (resource_list.get(position).crl_descp.equalsIgnoreCase("Audio/Video")) {

                holder.img_resource_icon.setImageResource(R.drawable.audio);
            } else if (resource_list.get(position).crl_descp.equalsIgnoreCase("Presentation")) {
                holder.img_resource_icon.setImageResource(R.drawable.ppt1_32);
            } else if (resource_list.get(position).crl_descp.equalsIgnoreCase("Video")) {
                holder.img_resource_icon.setImageResource(R.drawable.videos);
            } else if (resource_list.get(position).crl_descp.equalsIgnoreCase("Worksheet")) {
                holder.img_resource_icon.setImageResource(R.drawable.pdf_file_format_symbol);
            } else {
                holder.img_resource_icon.setImageResource(R.drawable.pdf_file_format_symbol);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

         holder.tv_resource.setText(resource_list.get(position).crl_discription);


        return showLeaveStatus;
    }

    public static class ViewHolder
    {
        TextView tv_resource;
        //ImageView
        ImageView img_resource_icon;
    }

}
