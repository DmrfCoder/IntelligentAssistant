package com.example.dmrf.intelligentassistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dmrf.intelligentassistant.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by DMRF on 2017/8/7.
 */

class ChatMessageAdapter extends BaseAdapter {
    private LayoutInflater mInflate;
    private List<ChatMessage> mDatas;

    public ChatMessageAdapter(Context context, List<ChatMessage> mDatas) {
        this.mInflate = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public int getItemViewType(int position) {

        ChatMessage chatMessage = mDatas.get(position);
        if (chatMessage.getType() == ChatMessage.Type.INCOMING) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ChatMessage chatMessage = mDatas.get(i);
        ViewHolder viewHolder = null;
        if (view == null) {
            //通过ItemType设置不同的布局
            if (getItemViewType(i) == 0) {
                view = mInflate.inflate(R.layout.item_from_msg, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.mDate = view.findViewById(R.id.from_msg_data);
                viewHolder.mMsg = view.findViewById(R.id.from_msg_content);
            } else {
                view = mInflate.inflate(R.layout.item_to_msg, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.mDate = view.findViewById(R.id.to_msg_data);
                viewHolder.mMsg = view.findViewById(R.id.to_msg_content);
            }
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        //设置数据
        SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        viewHolder.mDate.setText(df.format(chatMessage.getDate()));
        viewHolder.mMsg.setText(chatMessage.getMsg());
        return view;
    }

    private final class ViewHolder {
        TextView mDate;
        TextView mMsg;
    }

}
