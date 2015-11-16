package com.lonshine.test.gpuimage.widget.radio;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by lonshine on 15/10/8 下午3:13.
 */
public class RadioView extends CheckableView {
    public RadioView(Context context) {
        super(context);
    }

    public RadioView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void toggle() {
        if (!mChecked) {
            super.toggle();
        }
    }
}
