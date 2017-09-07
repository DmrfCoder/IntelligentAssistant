package com.example.dmrf.intelligentassistant.Utils;

import android.text.TextPaint;
import android.text.style.UnderlineSpan;

/**
 * Created by DMRF on 2017/8/9.
 */

public class NoUnderlineSpan extends UnderlineSpan {

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(false);
    }
}
