package com.example.dmrf.intelligentassistant.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dmrf.intelligentassistant.Activity.MainActivity;
import com.example.dmrf.intelligentassistant.Bean.ChatMessage;
import com.example.dmrf.intelligentassistant.R;
import com.example.dmrf.intelligentassistant.Utils.BitmapAndStringUtils;
import com.example.dmrf.intelligentassistant.Utils.OpenTextViewLinkByWebViewUtils;
import com.example.dmrf.intelligentassistant.View.XCRoundImageView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by DMRF on 2017/8/7.
 */

public class ChatMessageAdapter extends BaseAdapter {
    private LayoutInflater mInflate;
    private List<ChatMessage> mDatas;
    private Context context;

    public ChatMessageAdapter(Context context, List<ChatMessage> mDatas) {
        this.mInflate = LayoutInflater.from(context);
        this.context = context;
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
                viewHolder.mIcon = view.findViewById(R.id.from_msg_icon);
                viewHolder.mName = view.findViewById(R.id.from_msg_name);
            } else {
                view = mInflate.inflate(R.layout.item_to_msg, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.mDate = view.findViewById(R.id.to_msg_data);
                viewHolder.mMsg = view.findViewById(R.id.to_msg_content);
                viewHolder.mIcon = view.findViewById(R.id.to_msg_icon);
                viewHolder.mName = view.findViewById(R.id.to_msg_name);
            }
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        boolean set = true;
        if (i > 0) {
            long before = mDatas.get(i - 1).getDate().getTime();
            long after = mDatas.get(i).getDate().getTime();

            //如果后一次发/接受消息距离前一次一分钟以内就不显示时间
            if (after - before < 60 * 1000) {
                set = false;
            }
        }

        if (set) {
            //设置数据
            SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
            viewHolder.mDate.setText(df.format(chatMessage.getDate()));
        } else {
            viewHolder.mDate.setText("");
        }

        viewHolder.mMsg.setText(chatMessage.getMsg());

        if (getItemViewType(i) == 0) {
            viewHolder.mName.setText(MainActivity.user.getIn_name());

            String inicon = MainActivity.user.getIn_icon();
            if (inicon != null) {
                Bitmap in = BitmapAndStringUtils.convertStringToIcon(inicon);
                viewHolder.mIcon.setImageBitmap(in);
            }

           /* String inair = MainActivity.user.getIn_air();
            if (inair != null) {
                Bitmap in = BitmapAndStringUtils.convertStringToIcon(inair);
                BitmapDrawable bd = new BitmapDrawable(context.getResources(), in);
                viewHolder.mMsg.setBackground(bd);
            }*/
        }else {
            viewHolder.mName.setText(MainActivity.user.getOut_name());

            String outicon = MainActivity.user.getOut_icon();
            if (outicon != null) {
                Bitmap out = BitmapAndStringUtils.convertStringToIcon(outicon);
                viewHolder.mIcon.setImageBitmap(out);
            }

           /* String outair = MainActivity.user.getOut_air();
            if (outair != null) {
                Bitmap in = BitmapAndStringUtils.convertStringToIcon(outair);
                BitmapDrawable bd = new BitmapDrawable(context.getResources(), in);
                viewHolder.mMsg.setBackground(bd);
            }*/
        }
        OpenTextViewLinkByWebViewUtils.OpenTextViewLinkByWebView(viewHolder.mMsg, context);

        return view;
    }

    private final class ViewHolder {
        TextView mDate;
        TextView mMsg;
        TextView mName;
        XCRoundImageView mIcon;
    }

}
