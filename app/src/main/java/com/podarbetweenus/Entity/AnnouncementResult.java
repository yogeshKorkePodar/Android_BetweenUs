package com.podarbetweenus.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 10/26/2015.
 */
public class AnnouncementResult implements Serializable {
    public String Clt_id = "";
    public String msg_ID = "";
    public String msg_Message = "";
    public String msg_date = "";
    public String msg_type = "";
    public String usl_id = "";
    public String pmu_readunreadstatus = "";
    public AnnouncementResult (String Clt_id, String msg_ID,String msg_Message ,String msg_date,String msg_type,String usl_id,String pmu_readunreadstatus){
        this.Clt_id   = Clt_id;
        this.msg_ID = msg_ID;
        this.msg_Message = msg_Message;
        this.msg_date = msg_date;
        this.msg_type = msg_type;
        this.usl_id = usl_id;
        this.pmu_readunreadstatus = pmu_readunreadstatus;
    }
}
