package cn.app.meiya.test.gpuimage.widget;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;

import cn.app.meiya.test.gpuimage.widget.radio.RadioView;
import cn.app.meiya.test.gpuimage.widget.radio.RadioViewGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by lonshine on 15/10/8 下午6:01.
 */
public class GpuImgSurfaceRadioGroup extends RadioViewGroup {
    public GpuImgSurfaceRadioGroup(Context context) {
        super(context);
        initGirg();
    }


    public GpuImgSurfaceRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGirg();
    }

    public GpuImgSurfaceRadioGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGirg();
    }

    public GpuImgSurfaceRadioGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
//        cleanGpuCache();

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
     * @param uri
     */
//    public void childGpuImageFillterCompleted(Uri uri, boolean isChecked,GPUImageFilter filter){
//        if(isChecked){
//            gpuImgFilterChanged(filter,uri);
//        }
//    }


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


    /**
     * 删除滤镜缓存
     */
//    private void cleanGpuCache() {
//        File picPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        File file = new File(picPath, GpuImgRadioButton.CACHE_DIR_NAME);
//        try {
//            FileUtil.deleteDirectory(file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


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
                if (radioView != null && radioView instanceof GpuImgSurfaceRadioButton) {
                    final GpuImgSurfaceRadioButton girBtn = (GpuImgSurfaceRadioButton) radioView;
//                    Uri gpuImgUri = girBtn.getGpuImgUri();
//                    if (gpuImgUri != null) {
//                        Loger.d("GpuImg--", "previewUri" + " == " + gpuImgUri.toString());
//                    }
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

}
