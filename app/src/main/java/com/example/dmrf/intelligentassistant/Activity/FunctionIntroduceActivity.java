package com.example.dmrf.intelligentassistant.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.example.dmrf.intelligentassistant.ActivityManager.ActivityManager;
import com.example.dmrf.intelligentassistant.Adapter.FunctionAdapter;
import com.example.dmrf.intelligentassistant.Bean.FunctionBean;
import com.example.dmrf.intelligentassistant.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DMRF on 2017/8/9.
 */

public class FunctionIntroduceActivity extends Activity {
    private ListView listView;
    private FunctionAdapter adapter;
    private List<FunctionBean> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_what_can_do);
        ActivityManager.getInstance().addActivity(FunctionIntroduceActivity.this);
        listView = findViewById(R.id.function_listview);
        list = new ArrayList<FunctionBean>();


        FunctionBean bean = new FunctionBean("  本智能助手具有智能对话、知识库、技能服务三大核心功能：\n\n  智能对话是指，本智能助手具有中文自然语言交互的能力；\n\n  知识库是指：本智能助手可为使用者导入独家内容以满足个性化及商业化需要；\n\n  技能服务是指，本智能助手打包提供超500种实用生活服务技能，涵盖生活、出行、学习、金融、购物等多个领域，一站式满足您的需求。\n\n具体功能演示如下：", -1010101222);
        list.add(bean);


        list.add(new FunctionBean("  1:笑话大全", R.mipmap.a));
        list.add(new FunctionBean("  2:故事大全", R.mipmap.b));
        list.add(new FunctionBean("  3:成语接龙", R.mipmap.c));
        list.add(new FunctionBean("  4:吉凶查询", R.mipmap.d));
        list.add(new FunctionBean("  5:新闻资讯", R.mipmap.e));
        list.add(new FunctionBean("  6:星座运势", R.mipmap.f));
        list.add(new FunctionBean("  7:生活百科", R.mipmap.g));
        list.add(new FunctionBean("  8:图片搜索", R.mipmap.h));
        list.add(new FunctionBean("  9:天气查询", R.mipmap.i));
        list.add(new FunctionBean("  10:菜谱大全", R.mipmap.j));
        list.add(new FunctionBean("  11:快递查询", R.mipmap.k));
        list.add(new FunctionBean("  12:数字计算", R.mipmap.l));
        list.add(new FunctionBean("  13:航班查询", R.mipmap.m));
        list.add(new FunctionBean("  14:列车查询", R.mipmap.n));
        list.add(new FunctionBean("  15:日期查询", R.mipmap.o));
        list.add(new FunctionBean("  16:中英互译", R.mipmap.p));
        list.add(new FunctionBean("  17:脑筋急转弯", R.mipmap.q));
        list.add(new FunctionBean("  18:果蔬报价", R.mipmap.aa));
        list.add(new FunctionBean("  19:汽油报价", R.mipmap.bb));
        list.add(new FunctionBean("  20:影视搜索", R.mipmap.cc));
        list.add(new FunctionBean("  21:歇后语", R.mipmap.ee));
        list.add(new FunctionBean("  22:绕口令", R.mipmap.tt));
        list.add(new FunctionBean("  23:顺口溜", R.mipmap.gg));
        list.add(new FunctionBean("  24:城市邮编", R.mipmap.hh));


        adapter = new FunctionAdapter(list, this);
        listView.setAdapter(adapter);

    }
}
