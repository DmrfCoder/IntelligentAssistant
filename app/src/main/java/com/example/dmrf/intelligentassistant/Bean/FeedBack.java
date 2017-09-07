package com.example.dmrf.intelligentassistant.Bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by DMRF on 2017/8/9.
 */

public class FeedBack extends BmobObject{
    private String contact;
    private String feedback;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
