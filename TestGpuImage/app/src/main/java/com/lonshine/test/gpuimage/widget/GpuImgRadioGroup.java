package com.lonshine.test.gpuimage.widget;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;

import com.lonshine.test.gpuimage.utils.FileUtil;
import com.lonshine.test.gpuimage.widget.radio.RadioViewGroup;

import java.io.File;
import java.io.IOException;


/**
 * Created by lonshine on 15/10/8 下午6:01.
 */
public class GpuImgRadioGroup extends RadioViewGroup {
    public GpuImgRadioGroup(Context context) {
        super(context);
    }

    public GpuImgRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GpuImgRadioGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GpuImgRadioGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void setImage(Uri uri){
        //删除之前的缓存图片
        cleanGpuCache();

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if(view instanceof GpuImgRadioButton){
                ((GpuImgRadioButton) view).setImage(uri);
            }
            if(view instanceof GpuImgSurfaceRadioButton){
                ((GpuImgSurfaceRadioButton) view).setImage(uri);
            }
        }
    }


    public OnGpuImageUriListener mOnGpuImageUriListener;

    /**
     * 此方法最好在setImage之前调用,避免滤镜加载完毕时mOnGpuImageFillterListener为空
     * @param l
     */
    public void setOnGpuImageUriListener(OnGpuImageUriListener l){
        mOnGpuImageUriListener = l;
    }


    /**
     * 滤镜转换完成，当前选中Item为该滤镜时才通知外界更新
     * @param uri
     */
    public void setGpuImageFillterCompleted(Uri uri,boolean isChecked){
        if(mOnGpuImageUriListener != null && isChecked){
            mOnGpuImageUriListener.onFillterCompleted(uri);
        }
    }



    //删除滤镜缓存
    private void cleanGpuCache() {
        File picPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(picPath, GpuImgRadioButton.CACHE_DIR_NAME);
        try {
            FileUtil.deleteDirectory(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
