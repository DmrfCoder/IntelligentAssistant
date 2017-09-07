package com.example.dmrf.intelligentassistant.Bean;

/**
 * Created by DMRF on 2017/8/14.
 */

public class FunctionBean {
    private String text;
    private int pic;

    public FunctionBean(String text, int pic) {
        this.text = text;
        this.pic = pic;
    }


    public String getText() {

        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }
}
