package com.example.dmrf.intelligentassistant.Bean;

/**
 * Created by DMRF on 2017/8/7.
 */

public class Result {
    private int code;
    private String text;
    private String url;

    private String source;


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public StandardList[] getList() {
        return list;
    }

    public void setList(StandardList[] list) {
        this.list = list;
    }

    private StandardList[] list;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
