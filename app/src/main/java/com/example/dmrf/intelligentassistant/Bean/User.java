package com.example.dmrf.intelligentassistant.Bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by DMRF on 2017/8/8.
 */

public class User extends BmobUser {


    private String in_icon;
    private String out_icon;
    private String background;
    private String in_name;
    private String out_name;
    private String out_air;
    private String in_air;
    private String opid;
    private String login_type;

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public String getOpid() {
        return opid;
    }

    public void setOpid(String opid) {
        this.opid = opid;
    }

    public String getIn_icon() {
        return in_icon;
    }

    public String getOut_icon() {
        return out_icon;
    }

    public String getBackground() {
        return background;
    }

    public String getIn_name() {
        return in_name;
    }

    public String getOut_name() {
        return out_name;
    }


    public String getOut_air() {
        return out_air;
    }

    public String getIn_air() {
        return in_air;
    }


    public void setIn_icon(String in_icon) {
        this.in_icon = in_icon;
    }

    public void setOut_icon(String out_icon) {
        this.out_icon = out_icon;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public void setIn_name(String in_name) {
        this.in_name = in_name;
    }

    public void setOut_name(String out_name) {
        this.out_name = out_name;
    }

    public void setOut_air(String out_air) {
        this.out_air = out_air;
    }

    public void setIn_air(String in_air) {
        this.in_air = in_air;
    }

}
