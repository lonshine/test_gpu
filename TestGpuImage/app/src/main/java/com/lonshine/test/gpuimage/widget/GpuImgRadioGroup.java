package com.lonshine.test.gpuimage.widget;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;

import com.lonshine.test.gpuimage.utils.GpuHelper;
import com.lonshine.test.gpuimage.widget.radio.RadioView;
import com.lonshine.test.gpuimage.widget.radio.RadioViewGroup;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by lonshine on 15/10/8 下午6:01.
 */
public class GpuImgRadioGroup extends RadioViewGroup {
    public GpuImgRadioGroup(Context context) {
        super(context);
        initGirg();
    }


    public GpuImgRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGirg();
    }

    public GpuImgRadioGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGirg();
    }

    public GpuImgRadioGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initGirg();
    }


    GpuImgSettingDialog mDialog;
    String mFilterName;
    int mProgress = 100;

    /**
     * 初始化GpuImgRadioGroup
     */
    private void initGirg() {
        setupCheckdChangListener();
    }


    /**
     * 传入的uri转换成String,会在GpuImgRadioButton内部先判断有效性再转换成uri
     * @param uri
     */
    public void setImage(Activity activity,Uri uri){
        setImage(activity, uri, 0);
    }


    /**
     * 传入的uri转换成String,会在GpuImgRadioButton内部先判断有效性再转换成uri.
     * dialogHeight为显示菜单的高度，如果为0则使用默认高度.
     * @param uri
     */
    public void setImage(Activity activity,Uri uri,int dialogHeight){
        initSettingDialog(activity,dialogHeight);

        //删除之前的缓存图片
        GpuHelper.get().cleanGpuCache(activity);

        setChildViewImage(uri);
    }


    private void setChildViewImage(Uri uri) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if(view instanceof GpuImgRadioButton){
                ((GpuImgRadioButton) view).setImage(uri.toString());
            }
        }
    }


    public void resetSettingDialogHeight(Activity activity,int dialogHeight){
        initSettingDialog(activity,dialogHeight);
    }


    /**
     * 初始化滤镜强度设置菜单
     * @param activity
     */
    private void initSettingDialog(Activity activity,int dialogHeight) {
        mDialog = new GpuImgSettingDialog(activity, new GpuImgSettingDialog.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                gpuImgFilterProgressChanged(progress);
            }
        },dialogHeight);
    }



    /**
     * 滤镜转换完成，当前选中Item为该滤镜时才通知外界更新.此方法由容器内的child发起调用。
     */
    public void childGpuImageFillterCompleted(boolean isChecked,GPUImageFilter filter){
        if(isChecked){
            gpuImgFilterChanged(filter);
        }
    }


    /**
     * 滤镜设置窗口，由child对象发起调用
     * @throws Exception
     */
    public void showProgressDialog() throws Exception {
        if(mDialog != null){
            mDialog.show(mFilterName,mProgress);
        }else{
            throw  new Exception("GpuImgRadioGroup : acitivity is null.");
        }
    }


    private OnGpuImageFilterListener mOnGpuImageFilterListener;

    public void setOnGpuImageFilterListener(OnGpuImageFilterListener l){
        mOnGpuImageFilterListener = l;
    }

    private void gpuImgFilterChanged(GPUImageFilter filter){
        if(mOnGpuImageFilterListener != null){
            mOnGpuImageFilterListener.onFillterChanged(filter);
        }
    }


    private void gpuImgFilterProgressChanged(int progress){
        if(mOnGpuImageFilterListener != null){
            mOnGpuImageFilterListener.onProgressChanged(progress);
        }

        mProgress = progress;
    }

    /**
     * 每次切换将滤镜预览图和滤镜对象传给外界
     */
    private void setupCheckdChangListener() {
        this.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioViewGroup group, int checkedId) {
                RadioView radioView = findRadioViewById(checkedId);
                if (radioView != null && radioView instanceof GpuImgRadioButton) {
                    final GpuImgRadioButton girBtn = (GpuImgRadioButton) radioView;
                    gpuImgFilterChanged(girBtn.getFilter());

                    mFilterName = girBtn.getFilterName();
                    mProgress = 100;
                    if(mDialog != null){
                        mDialog.setFilterName(mFilterName);
                    }
                }
            }
        });
    }


    public void onDestroy(){
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if(view instanceof GpuImgRadioButton){
                ((GpuImgRadioButton) view).onDestroy();
            }
        }
    }


}
