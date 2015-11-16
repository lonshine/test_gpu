package cn.app.meiya.test.gpuimage.widget.radio;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.RelativeLayout;

import com.meiyaapp.meiya.R;

/**
 * 复选Layout
 * Created by lonshine on 15/9/30 下午2:04.
 */
public class CheckableView extends RelativeLayout implements Checkable {

    public boolean mChecked;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private OnCheckedChangeListener mOnCheckedChangeWidgetListener;

    public CheckableView(Context context) {
        this(context, null);
    }


    public CheckableView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs){
        initView(context,attrs);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CheckableView, 0, 0);
        boolean isChecked = typedArray.getBoolean(R.styleable.CheckableView_cv_ischecked, false);
        setChecked(isChecked);
        typedArray.recycle();
    }

    /**
     * View初始化固定在setAttrs之前
     * @param context
     */
    public void initView(Context context, AttributeSet attrs) {
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
    }




    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public void setChecked(boolean checked) {
        if(mChecked != checked){
            mChecked = checked;
            refreshDrawableState();

            refreshCheckableView(mChecked);
            if(mOnCheckedChangeListener != null){
                mOnCheckedChangeListener.onCheckedChanged(this,mChecked);
            }
            if (mOnCheckedChangeWidgetListener != null) {
                mOnCheckedChangeWidgetListener.onCheckedChanged(this, mChecked);
            }
        }
    }

    public void refreshCheckableView(boolean checked) {
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeWidgetListener = listener;
    }


    public static interface OnCheckedChangeListener {
        void onCheckedChanged(View CheckableView, boolean isChecked);
    }


}
