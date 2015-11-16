package cn.app.meiya.test.gpuimage.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.meiyaapp.meiya.R;

import cn.app.meiya.aa.widget.CheckableImageView;
import cn.app.meiya.test.gpuimage.widget.radio.RadioView;

/**
 * Created by lonshine on 15/11/12 下午1:48.
 */
public class GoodEditTabRadioButton extends RadioView {

    TextView rbTabName;
    CheckableImageView ivTabIndicator;


    public GoodEditTabRadioButton(Context context) {
        super(context);
    }

    public GoodEditTabRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void initView(Context context, AttributeSet attrs) {
        init(context, attrs);
    }


    private void init(Context context,AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_good_edit_tab_radio_btn, this, true);
        findView(view);
        getAttrs(context, attrs);
    }

    private void findView(View view) {
        rbTabName = (TextView) view.findViewById(R.id.rbTabName);
        ivTabIndicator = (CheckableImageView) view.findViewById(R.id.ivTabIndicator);
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GoodEditTabRadioButton, 0, 0);

        String tabName = typedArray.getString(R.styleable.GoodEditTabRadioButton_tab_name);
        rbTabName.setText(tabName);
        typedArray.recycle();
    }


    @Override
    public void refreshCheckableView(boolean checked) {
        ivTabIndicator.setChecked(checked);

        if(checked){
            rbTabName.setTextColor(getResources().getColor(R.color.pink));
        }else{
            rbTabName.setTextColor(getResources().getColor(R.color.text_third));
        }
    }
}
