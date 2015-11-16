package com.lonshine.test.gpuimage.widget.radio;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


/**
 * Created by lonshine on 15/10/8 下午2:12.
 */
public class RadioViewGroup extends LinearLayout {

    private  CheckedStateTracker mChildOnCheckedChangeListener;
    private RadioViewGroup.OnCheckedChangeListener mOnCheckedChangeListener;
    private int mCheckedId = -1;

    public RadioViewGroup(Context context) {
        super(context);
        init();
    }

    public RadioViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RadioViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RadioViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {
        mChildOnCheckedChangeListener = new CheckedStateTracker();
    }


    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);

        if(child != null && child instanceof RadioView){

            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            ((RadioView) child).setOnCheckedChangeWidgetListener(
                    mChildOnCheckedChangeListener);

            if(((RadioView) child).isChecked()){
                if(mCheckedId != -1){
                    setCheckedStateForView(mCheckedId,false);
                }
                setCheckedId(child.getId());
            }
        }

    }

    public int getCheckedId(){
        return mCheckedId;
    }


    private void setCheckedId(int id) {
        mCheckedId = id;
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
        }
    }


    public void checkedChild(int id) {
        findRadioViewById(id).setChecked(true);
    }



    private void setCheckedStateForView(int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        if (checkedView != null && checkedView instanceof RadioView) {
            ((RadioView) checkedView).setChecked(checked);
        }
    }


    /**
     * 通过id获取RadioView对象
     * @param viewId
     * @return
     */
    public RadioView findRadioViewById(int viewId){
        View checkedView = findViewById(viewId);
        if (checkedView != null && checkedView instanceof RadioView) {
            return ((RadioView) checkedView);
        }else{
            return null;
        }
    }



    public interface OnCheckedChangeListener {
        public void onCheckedChanged(RadioViewGroup group, int checkedId);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }


    private class CheckedStateTracker implements RadioView.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(View CheckableView, boolean isChecked) {
            if (mCheckedId != -1) {
                setCheckedStateForView(mCheckedId, false);
            }
            int id = CheckableView.getId();
            if(isChecked){
                setCheckedId(id);
            }
        }
    }

}
