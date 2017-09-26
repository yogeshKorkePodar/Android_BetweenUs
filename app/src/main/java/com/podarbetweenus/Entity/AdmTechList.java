package com.podarbetweenus.Entity;

/**
 * Created by Gayatri on 3/18/2016.
 */
public class AdmTechList {
    public String Rol_ID = "";
    public String fullname = "";
    public String stf_Mno = "";
    public String usl_Id = "";
    public String SrNo = "";
    private boolean checked = false ;

    public boolean isChecked() {
        return checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
