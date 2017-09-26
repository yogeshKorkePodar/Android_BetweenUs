package com.podarbetweenus.Activities;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.podarbetweenus.R;


/**
 * Created by Administrator on 9/24/2015.
 */
public class HeaderControler {

    Activity activity;
    LinearLayout lay_back_investment;
    TextView tv_title;
    ImageView img_logo;
    boolean isBackVisible;
    boolean isLogoVisible;
    String titleText;

    public HeaderControler(Activity containerActivity,boolean isBackVisible,boolean isLogoVisible,String titleText){

        activity = containerActivity;
        this.isBackVisible = isBackVisible;
        this.isLogoVisible = isLogoVisible;
        this.titleText = titleText;
        initializeUi();
    }
    public void initializeUi(){

        lay_back_investment = (LinearLayout)activity.findViewById(R.id.lay_back_investment);
        tv_title = (TextView)activity.findViewById(R.id.tv_title);
        img_logo = (ImageView)activity.findViewById(R.id.img_logo);

        if(isBackVisible){
            lay_back_investment.setVisibility(View.VISIBLE);
        }else{
            lay_back_investment.setVisibility(View.INVISIBLE);
        }
        if(isLogoVisible){
            tv_title.setVisibility(View.GONE);
            img_logo.setVisibility(View.VISIBLE);
        }else{
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setText(titleText);
            img_logo.setVisibility(View.GONE);
        }
    }
}
