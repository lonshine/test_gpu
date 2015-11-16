package com.lonshine.test.gpuimage.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lonshine.test.gpuimage.R;


/**
 * 调整滤镜强度的菜单
 */
public class GpuImgSettingDialog extends Dialog {

    SeekBarIndicated siFilterProgress;
    ImageView ivCancel;
    TextView tvFilterName;
    ImageView ivChecked;
    int mHeight;

    private Activity mActivity;
    private OnSeekBarChangeListener mListener;

    private View mContentView;

    public GpuImgSettingDialog(Activity activity) {
        super(activity);
    }


    public GpuImgSettingDialog(Activity activity, OnSeekBarChangeListener listener) {
        super(activity, R.style.ButtomTransparentDialog);
        mActivity = activity;
        mListener = listener;
    }


    public GpuImgSettingDialog(Activity activity, OnSeekBarChangeListener listener,int dialogHeight) {
        super(activity, R.style.ButtomTransparentDialog);
        mActivity = activity;
        mListener = listener;
        mHeight = dialogHeight;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        initDialog();
        initUI();
    }

    private void initDialog() {
        LayoutInflater inflater = (LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView =  inflater.inflate(R.layout.dialog_gpu_filter,
                null);

        int screenWidth = mActivity.getWindowManager()
                .getDefaultDisplay().getWidth();
        this.getWindow().getDecorView().setPadding(0,0,0,0);//设置让dialog宽度充满屏幕

        WindowManager.LayoutParams localLayoutParams = this.getWindow().getAttributes();
        localLayoutParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
        localLayoutParams.x = 0;
        localLayoutParams.y = 0;
        localLayoutParams.width = screenWidth;
        mContentView.setMinimumWidth(screenWidth);


        this.getWindow().setAttributes(localLayoutParams);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);
        setContentView(mContentView);
    }

    private void initUI() {
        siFilterProgress = (SeekBarIndicated) mContentView.findViewById(R.id.siFilterProgress);
        tvFilterName = (TextView) mContentView.findViewById(R.id.tvFilterName);
        ivCancel = (ImageView) mContentView.findViewById(R.id.ivCancel);
        ivChecked = (ImageView) mContentView.findViewById(R.id.ivChecked);
        View vEmptyLayout = mContentView.findViewById(R.id.vEmptyLayout);
        View layoutGpuDialog = mContentView.findViewById(R.id.layoutGpuDialog);


        //有设定菜单高度，顶部空白区域自适应填充
        if(mHeight > 0){
            //使窗口高度刚好在预览度下面
            int screenWidth = mActivity.getWindowManager().getDefaultDisplay().getWidth();
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.height = mHeight;
            lp.width = screenWidth;
            layoutGpuDialog.setLayoutParams(lp);

            vEmptyLayout.setVisibility(View.VISIBLE);
        }else{
            vEmptyLayout.setVisibility(View.GONE);
        }


        siFilterProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mListener != null) {
                    mListener.onProgressChanged(seekBar, progress, fromUser);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siFilterProgress.setValue(100);
                dismiss();
            }
        });

        ivChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    /**
     * 显示菜单前更新UI
     */
    public void show(String name,int progress) {
        this.show();
        init(name,progress);
    }


    /**
     * 每次弹出时初始化进度条
     */
    public void init(String name,int progress){
        siFilterProgress.setValue(progress);
        setFilterName(name);
    }


    public void setFilterName(String name){
        if(tvFilterName != null ){
            tvFilterName.setText(name);
        }
    }


    public interface OnSeekBarChangeListener {
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser);
    }



}
