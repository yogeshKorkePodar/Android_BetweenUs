package com.podarbetweenus.Activities;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.podarbetweenus.R;


/**
 * Created by Administrator on 9/28/2015.
 */
public class CustomAccount extends ArrayAdapter<String> {
    Activity mcontext;
    String[] textvalues;
    String[] textvalues1;
    int[] iconsvalues;
    // ToggleButton chkState;
    // ImageButton chkState;
    int pos_value;
    // DataFetchServices dft;

    int[] buttonStates;
    AtomHolder holder = null;

    enum function {
        load_push_notification;
    };

    public CustomAccount(Activity context, int resource,
                         int[] icons,String[] data) {
        super(context, R.layout.drawer_list_item, data);
        mcontext = context;
        textvalues = data;
        // textvalues1 = data1;
        iconsvalues = icons;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;

        //	if (row == null) {
        LayoutInflater inflater = ((Activity) mcontext).getLayoutInflater();
        row = inflater.inflate(R.layout.drawer_list_item, parent, false);

        holder = new AtomHolder();
        holder.layout = (RelativeLayout) row.findViewById(R.id.layout);
        holder.icon = (ImageView) row.findViewById(R.id.icon);
        holder.title = (TextView) row.findViewById(R.id.title);
        // holder.desc = (TextView)row.findViewById(R.id.desc);
        holder.icon_logout = (ImageView) row.findViewById(R.id.icon_logout);
        row.setTag(holder);
			/*
			 * if(Login.getResult().equals("true")) {
			 * holder.chkState.setChecked(true); } else {
			 * holder.chkState.setChecked(false); }
			 */
		/*} else {
			holder = (AtomHolder) row.getTag();
		}*/


        holder.title.setText(textvalues[position]);
        holder.icon.setImageResource(iconsvalues[position]);


            ///holder.icon.setVisibility(View.GONE);


    /*    if (position == 5) {
            Log.e("pos",""+position);
            Log.e("string",""+textvalues[position]);
            holder.icon_logout.setVisibility(View.VISIBLE);
            holder.icon.setVisibility(View.INVISIBLE);

            holder.title.setPadding(5, 0, 0, 0);
        }
        // holder.desc.setText(textvalues1[position]);*/

		/*
		 * if(position==0) {
		 * holder.layout.setBackgroundColor(Color.parseColor("#1b74b6"));
		 * //holder.icon_logout.setVisibility(View.GONE); } if(position==1) {
		 * holder.layout.setBackgroundColor(Color.parseColor("#1b74b6")); }
		 * if(position==2) {
		 * holder.layout.setBackgroundColor(Color.parseColor("#1b74b6")); }
		 * if(position==3) {
		 * holder.layout.setBackgroundColor(Color.parseColor("#1b74b6")); }
		 * if(position==4) {
		 * holder.layout.setBackgroundColor(Color.parseColor("#1b74b6"));
		 * holder.icon_logout.setVisibility(View.VISIBLE);
		 * holder.icon.setVisibility(View.GONE); }
		 */

        return row;
    }

    public static class AtomHolder {
        RelativeLayout layout;
        ImageView icon, icon_logout;
        TextView title;
        ToggleButton chkState;
        TextView desc;
    }
}
