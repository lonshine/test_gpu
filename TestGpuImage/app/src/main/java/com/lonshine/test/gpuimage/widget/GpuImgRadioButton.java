package com.lonshine.test.gpuimage.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lonshine.test.gpuimage.R;
import com.lonshine.test.gpuimage.utils.UriUtils;
import com.lonshine.test.gpuimage.widget.radio.RadioView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLookupFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePixelationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;


/**
 * 滤镜选择按钮
 * 图片保存：
 * File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
 * File file = new File(path, folderName + "/" + fileName);//folderName="Meiya"
 * 进入滤镜预览页面时可先删除缓存目录里的已有图片，减少缓存占用.
 * Created by lonshine on 15/9/30 上午11:44.
 */

public class GpuImgRadioButton extends RadioView {

    public final static String CACHE_DIR_NAME = "GpuImageCache";

    //加载状态
    private final static int STATE_UN_LOAD = 0;
    private final static int STATE_LOADING = 1;
    private final static int STATE_LOADED = 2;

    private int mState = STATE_UN_LOAD;

    private int mFilterMode;
    private String mFilterName;


    ImageView ivGrbImg;
    View vGrbTag;
    View mLayoutGrbLoading;
    View mLayoutGrbImg;
    TextView tvGrbName;
    View mLayoutGrbText;

    Uri mUri;
    Uri mGpuImgUri;
    GPUImageFilter mFilter;


    public GpuImgRadioButton(Context context) {
        super(context);
    }

    public GpuImgRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initView(Context context,AttributeSet attrs) {
        init(context, attrs);
    }

    private void init(Context context,AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_gpuimg_radio_btn, this, true);
        findView(view);
        getAttrs(context, attrs);
        setupFilter();
        setName();
        setSelectedTag();
    }


    private void getAttrs(Context context,AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GpuImgRadioButton, 0, 0);

        if(typedArray.hasValue(R.styleable.GpuImgRadioButton_grb_filter_mode)){
            mFilterMode = typedArray.getInt(R.styleable.GpuImgRadioButton_grb_filter_mode, GpuMode.NULL);
        }

        if(typedArray.hasValue(R.styleable.GpuImgRadioButton_grb_filter_name)){
            mFilterName = typedArray.getString(R.styleable.GpuImgRadioButton_grb_filter_name);
        }

        typedArray.recycle();
    }

    private void findView(View view) {

        ivGrbImg = (ImageView) view.findViewById(R.id.ivGrbImg);
        vGrbTag = view.findViewById(R.id.ivGrbTag);
        mLayoutGrbLoading = view.findViewById(R.id.layoutGrbLoading);
        mLayoutGrbImg = view.findViewById(R.id.layoutGrbImg);
        tvGrbName = (TextView) view.findViewById(R.id.tvGrbName);
        mLayoutGrbText = view.findViewById(R.id.layoutGrbText);

        vGrbTag.setVisibility(View.INVISIBLE);
        mLayoutGrbLoading.setVisibility(View.VISIBLE);
    }

    private void setupFilter() {
        switch (mFilterMode){
            case GpuMode.CONTRAST:
                mFilter = new GPUImageContrastFilter(range(75,0,2));
                setFilterName("对比度");
                break;

            case GpuMode.INVERT:
                mFilter = new GPUImageColorInvertFilter();
                setFilterName("反色");
                break;

            case GpuMode.PIXELATION:
                mFilter = new GPUImagePixelationFilter();
                ((GPUImagePixelationFilter)mFilter).setPixel(range(20,1,100));
                setFilterName("像素");
                break;

            case GpuMode.HUE:
                mFilter = new GPUImageHueFilter();
                ((GPUImageHueFilter)mFilter).setHue(range(30, 0, 360));
                setFilterName("色度");
                break;

            case GpuMode.GAMMA:
                mFilter = new GPUImageGammaFilter();
                ((GPUImageGammaFilter)mFilter).setGamma(range(80,0,3));
                setFilterName("伽马线");
                break;

            case GpuMode.BRIGHTNESS:
                mFilter = new GPUImageBrightnessFilter();
                ((GPUImageBrightnessFilter)mFilter).setBrightness(range(65,-1,1));
                setFilterName("亮度");
                break;

            case GpuMode.SEPIA:
                mFilter = new GPUImageSepiaFilter();
                ((GPUImageSepiaFilter)mFilter).setIntensity(range(20,0,2));
                setFilterName("怀旧");
                break;

            case GpuMode.GRAYSCALE:
                mFilter = new GPUImageGrayscaleFilter();
                setFilterName("灰度");
                break;

            case GpuMode.SHARPNESS:
                mFilter = new GPUImageSharpenFilter();
                ((GPUImageSharpenFilter)mFilter).setSharpness(range(20,-4,4));
                setFilterName("锐化");
                break;


            case GpuMode.LOOKUP:
                mFilter = new GPUImageLookupFilter();
                ((GPUImageLookupFilter)mFilter).setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.gpu_lookup_amatorka));
                setFilterName("自定义");
                break;

            default:
                mFilter = null;
                setFilterName("原图");
                break;
        }
    }

    private void setFilterName(String name){
        if(TextUtils.isEmpty(mFilterName)){
            mFilterName = name;
        }
    }

    private void setSelectedTag() {
        vGrbTag.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtil.showMessage("调节强度");
            }
        });
    }

    private void setName() {
        if(!TextUtils.isEmpty(mFilterName)){
            tvGrbName.setText(mFilterName);
        }
    }



    protected int range(final int percentage, final int start, final int end) {
        return (end - start) * percentage / 100 + start;
    }


    @Override
    public void refreshCheckableView(boolean checked) {
        setCheckTagView();
    }


    public void setImage(Uri uri){
        mUri = uri;

        if(mState == STATE_UN_LOAD){
            updateSelectedGpuImage();
        }
    }


    /**
     * 更新滤镜预览图界面
     */
    private void updateSelectedGpuImage() {
        Log.d("GpuImg--" + mState, "updateSelectedGpuImage");
        setCheckTagView();

        if(mUri == null){
            return;
        }

        switch (mState) {
            case STATE_UN_LOAD:
                loadGpuImage();
                break;

            case STATE_LOADING:

                break;

            case STATE_LOADED:
                setLoadedView();
                break;

        }
    }

    private void setCheckTagView() {
        if(isChecked()){
            vGrbTag.setVisibility(View.VISIBLE);
        }else{
            vGrbTag.setVisibility(View.INVISIBLE);
        }
    }

    private void loadGpuImage() {
        if(mUri != null){
            mState = STATE_LOADING;
            setLoadingView();
            getGpuImage();
        }
    }

    private void setLoadingView() {
        mLayoutGrbLoading.setVisibility(View.VISIBLE);
    }


    /**
     * 获取并加载滤镜效果
     */
    private void getGpuImage() {
        if (mUri != null) {
            if(mFilter != null){
                new FilterSaveTask(new OnPictureSavedListener() {
                    @Override
                    public void onPictureSaved(Uri uri) {
                        getGpuImageCompleted(uri);
                    }
                }).execute();
            }else{
                //原图效果
                getGpuImageCompleted(mUri);
            }

        }
    }

    /**
     * 滤镜转换和缓存
     */
    private class FilterSaveTask extends AsyncTask<Void,Void,Void>{

        private  Handler mHandler;
        private  OnPictureSavedListener mListener;
        public FilterSaveTask(OnPictureSavedListener l) {
            mListener = l;
            mHandler = new Handler();
        }

        @Override
        protected Void doInBackground(Void... params) {
            filterGpuImageAndSave();
            return null;
        }

        /**
         * 滤镜转换并保存为图片
         */
        private void filterGpuImageAndSave() {
            String realPath = UriUtils.getRealFilePath(getContext(), mUri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 16;
            Bitmap bitmap = BitmapFactory.decodeFile(realPath, options);
            Log.d("GpuImg--", "realPath" + " == " + realPath);

            if (bitmap != null) {
                //滤镜转换
                GPUImage gpuImage = new GPUImage(getContext());
                gpuImage.setImage(bitmap);
                gpuImage.setFilter(mFilter);
                bitmap = gpuImage.getBitmapWithFilterApplied();

                saveBitmap2File(bitmap);
            }
        }

        private void saveBitmap2File(final Bitmap bitmap) {
            String fileName = System.currentTimeMillis() + "-" + getId() + ".gc";
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File file = new File(path, CACHE_DIR_NAME + "/" + fileName);
            try {
                file.getParentFile().mkdirs();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
                MediaScannerConnection.scanFile(getContext(),
                        new String[]{
                                file.toString()
                        }, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(final String path,final  Uri uri) {
                                Log.d("GpuImg--uri", "onScanCompleted" + " == " + uri.toString());
                                Log.d("GpuImg--path", "onScanCompleted" + " == " + path);

                                if (mListener != null) {
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mListener.onPictureSaved(uri);
                                        }
                                    });
                                }

                                bitmap.recycle();
                            }
                        });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    private interface OnPictureSavedListener {
        void onPictureSaved(Uri uri);
    }

    private void getGpuImageCompleted(Uri uri) {
        Log.d("GpuImg--" + mState, "getGpuImageCompleted");
        if(uri == null){
            Log.d("GpuImg", "getGpuImageCompleted" + ":  uri = null");
            return;
        }

        mState = STATE_LOADED;
        mGpuImgUri = uri;
        setImageViewFromUri();
        updateSelectedGpuImage();

        //通知父布局
        if(getParent() instanceof GpuImgRadioGroup) {
            ((GpuImgRadioGroup)getParent()).setGpuImageFillterCompleted(uri,isChecked());
        }
    }

    /**
     * 加载预览图，尽量缩小图片容量
     */
    private void setImageViewFromUri() {
        String realPath = UriUtils.getRealFilePath(getContext(), mGpuImgUri);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 16;
        Bitmap bitmap = BitmapFactory.decodeFile(realPath, options);
//        byte[] bytes = BitmapUtils.Bitmap2Bytes(bitmap);


        ivGrbImg.setImageBitmap(bitmap);

//        GlideImageLoader imageLoader = (GlideImageLoader) APP.getInstance().createImageLoader(getContext());
//        imageLoader.displayAvatar(bytes, ivGrbImg);
//        bitmap.recycle();
    }


    private void setLoadedView() {
        mLayoutGrbLoading.setVisibility(View.INVISIBLE);
    }


    /**
     * 原图的uri
     * @return
     */
    public Uri getUri(){
        return mUri;
    }

    /**
     * 预览图缓存图片的uri
     * @return
     */
    public Uri getGpuImgUri(){
        return mGpuImgUri;
    }

}
