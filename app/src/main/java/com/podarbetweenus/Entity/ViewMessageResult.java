package com.podarbetweenus.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 10/26/2015.
 */
public class ViewMessageResult implements Serializable {

    public String Clt_ID = "";
    public String Fullname = "";
    public String pmg_Message = "";
    public String pmg_date = "";
    public String pmg_file_name = "";
    public String pmg_file_path = "";
    public String pmg_subject = "";
    public String pmu_ID = "";
    public String usl_ID = "";
    public String msg_Message = "";
    public String msg_date = "";
    public String msg_Message1 = "";
    public String msg_type = "";
    public String stu_id = "";
    public String pmg_ID = "";
    public String pmu_readunreadstatus = "";
    public ViewMessageResult (String Fullname, String pmg_Message,String pmg_date ,String pmg_file_name,String pmg_file_path,String pmg_subject,String pmu_ID,String usl_ID,String pmu_readunreadstatus){
        this.Fullname   = Fullname;
        this.pmg_Message = pmg_Message;
        this.pmg_date = pmg_date;
        this.pmg_file_name = pmg_file_name;
        this.pmg_file_path = pmg_file_path;
        this.pmg_subject = pmg_subject;
        this.pmu_ID = pmu_ID;
        this.usl_ID = usl_ID;
        this.pmu_readunreadstatus = pmu_readunreadstatus;
    }
}