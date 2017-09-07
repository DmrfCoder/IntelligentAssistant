package com.example.dmrf.intelligentassistant.Utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.widget.TextView;

public class OpenTextViewLinkByWebViewUtils {

    private static TextView tv_content;
    private static Context context;

    public static void OpenTextViewLinkByWebView(TextView tv_content1, Context context1) {
       tv_content = tv_content1;
        context = context1;
        removeHyperLinkUnderline(tv_content);
        interceptHyperLink(tv_content);
    }


    /**
     * 拦截超链接
     *
     * @param tv
     */
    private static void interceptHyperLink(TextView tv) {
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = tv.getText();
        if (text instanceof Spannable) {
            int end = text.length();
            Spannable spannable = (Spannable) tv.getText();
            URLSpan[] urlSpans = spannable.getSpans(0, end, URLSpan.class);
            if (urlSpans.length == 0) {
                return;
            }

            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
            // 循环遍历并拦截 所有http://开头的链接
            for (URLSpan uri : urlSpans) {
                String url = uri.getURL();
                if (url.indexOf("http://") == 0 || url.indexOf("https://") == 0||url.indexOf("www.")==0) {
                    CustomUrlSpan customUrlSpan = new CustomUrlSpan(context, url);
                    spannableStringBuilder.setSpan(customUrlSpan, spannable.getSpanStart(uri),
                            spannable.getSpanEnd(uri), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            }
            tv.setText(spannableStringBuilder);
        }
    }

    private static void removeHyperLinkUnderline(TextView tv) {
        CharSequence text = tv.getText();
        if (text instanceof Spannable) {
            Log.i("test", "true");
            Spannable spannable = (Spannable) tv.getText();
            NoUnderlineSpan noUnderlineSpan = new NoUnderlineSpan();
            spannable.setSpan(noUnderlineSpan, 0, text.length(), Spanned.SPAN_MARK_MARK);
        }
    }
}
