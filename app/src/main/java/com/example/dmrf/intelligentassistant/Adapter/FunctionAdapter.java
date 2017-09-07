package com.example.dmrf.intelligentassistant.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dmrf.intelligentassistant.Bean.FunctionBean;
import com.example.dmrf.intelligentassistant.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by DMRF on 2017/8/14.
 */

public class FunctionAdapter extends BaseAdapter {
    private List<FunctionBean> list;
    private Context context;
    private LayoutInflater layoutInflater;

    public FunctionAdapter(List<FunctionBean> list, Context context) {
        this.list = list;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        viewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new viewHolder();
            view = layoutInflater.inflate(R.layout.function_item_layout, null);
            viewHolder.imageView = view.findViewById(R.id.imageview_pic);
            viewHolder.textView = view.findViewById(R.id.text_title);
            view.setTag(viewHolder);
        } else {
            viewHolder = (FunctionAdapter.viewHolder) view.getTag();
        }

        if (list.get(i).getText()!=null){
            viewHolder.textView.setText(list.get(i).getText());
        }

        if (list.get(i).getPic()!=-1010101222){
            viewHolder.imageView.setBackgroundResource(list.get(i).getPic());
        }

        return view;

    }

    class viewHolder {
        TextView textView;
        ImageView imageView;
    }
}
