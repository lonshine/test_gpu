package com.lonshine.test.gpuimage.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lonshine.test.gpuimage.R;
import com.lonshine.test.gpuimage.widget.radio.RadioView;

import java.util.LinkedList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageAlphaBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLookupFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePixelationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;


/**
 * 滤镜选择按钮
 * 图片保存：
 * File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
 * File file = new File(path, folderName + "/" + fileName);//folderName="Meiya"
 * 进入滤镜预览页面时可先删除缓存目录里的已有图片，减少缓存占用.
 * Created by lonshine on 15/9/30 上午11:44.
 */

public class GpuImgSurfaceRadioButton extends RadioView {

    //加载状态
    private final static int STATE_UN_LOAD = 0;
    private final static int STATE_LOADING = 1;
    private final static int STATE_LOADED = 2;

    private int mState = STATE_UN_LOAD;

    private int mFilterMode;
    private String mFilterName;


    GPUImageView ivGrbImg;
    View vGrbTag;
    View mLayoutGrbLoading;
    SquareLayout mLayoutGrbImg;
    TextView tvGrbName;
    View mLayoutGrbText;

    Uri mUri;
    GPUImageFilter mFilter;


    public GpuImgSurfaceRadioButton(Context context) {
        super(context);
    }

    public GpuImgSurfaceRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initView(Context context, AttributeSet attrs) {
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_gpusurface_radio_btn, this, true);
        findView(view);
        getAttrs(context, attrs);
        setupFilter();
        setName();
        setSelectedTag();
    }


    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GpuImgRadioButton, 0, 0);

        if (typedArray.hasValue(R.styleable.GpuImgRadioButton_grb_filter_mode)) {
            mFilterMode = typedArray.getInt(R.styleable.GpuImgRadioButton_grb_filter_mode, GpuMode.NULL);
        }

        if (typedArray.hasValue(R.styleable.GpuImgRadioButton_grb_filter_name)) {
            mFilterName = typedArray.getString(R.styleable.GpuImgRadioButton_grb_filter_name);
        }

        typedArray.recycle();
    }

    private void findView(View view) {

        ivGrbImg = (GPUImageView) view.findViewById(R.id.ivGrbImg);
        vGrbTag = view.findViewById(R.id.ivGrbTag);
        mLayoutGrbLoading = view.findViewById(R.id.layoutGrbLoading);
        mLayoutGrbImg = (SquareLayout) view.findViewById(R.id.layoutGrbImg);
        tvGrbName = (TextView) view.findViewById(R.id.tvGrbName);
        mLayoutGrbText = view.findViewById(R.id.layoutGrbText);

        vGrbTag.setVisibility(View.INVISIBLE);
        mLayoutGrbLoading.setVisibility(View.VISIBLE);

    }


    private void setupFilter() {
        switch (mFilterMode) {
            case GpuMode.CONTRAST:
                mFilter = new GPUImageContrastFilter(range(75, 0, 2));
                setFilterName("对比度");
                break;

            case GpuMode.INVERT:
                mFilter = new GPUImageColorInvertFilter();
                setFilterName("反色");
                break;

            case GpuMode.PIXELATION:
                mFilter = new GPUImagePixelationFilter();
                ((GPUImagePixelationFilter) mFilter).setPixel(range(20, 1, 100));
                setFilterName("像素");
                break;

            case GpuMode.HUE:
                mFilter = new GPUImageHueFilter();
                ((GPUImageHueFilter) mFilter).setHue(range(30, 0, 360));
                setFilterName("色度");
                break;

            case GpuMode.GAMMA:
                mFilter = new GPUImageGammaFilter();
                ((GPUImageGammaFilter) mFilter).setGamma(range(80, 0, 3));
                setFilterName("伽马线");
                break;

            case GpuMode.BRIGHTNESS:
                mFilter = new GPUImageBrightnessFilter();
                ((GPUImageBrightnessFilter) mFilter).setBrightness(range(65, -1, 1));
                setFilterName("亮度");
                break;

            case GpuMode.SEPIA:
                mFilter = new GPUImageSepiaFilter();
                ((GPUImageSepiaFilter) mFilter).setIntensity(range(20, 0, 2));
                setFilterName("怀旧");
                break;

            case GpuMode.GRAYSCALE:
                mFilter = new GPUImageGrayscaleFilter();
                setFilterName("灰度");
                break;

            case GpuMode.SHARPNESS:
                mFilter = new GPUImageSharpenFilter();
                ((GPUImageSharpenFilter) mFilter).setSharpness(range(20, -4, 4));
                setFilterName("锐化");
                break;

            case GpuMode.LOOKUP:
                mFilter = new GPUImageLookupFilter();
                ((GPUImageLookupFilter) mFilter).setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.gpu_lookup_amatorka));
                setFilterName("自定义MAP");
                break;


            case GpuMode.CUSTOME:
                List<GPUImageFilter> filters = new LinkedList<GPUImageFilter>();
                GPUImageLookupFilter lookupFilter = new GPUImageLookupFilter();
                lookupFilter.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.gpu_lookup_amatorka));
                filters.add(lookupFilter);
                filters.add(new GPUImageAlphaBlendFilter(range(0, 0, 1)));

                mFilter =  new GPUImageFilterGroup(filters);
                setFilterName("自定义");
                break;

            default:
                mFilter = null;
                setFilterName("原图");
                break;
        }
    }

    private void setFilterName(String name) {
        if (TextUtils.isEmpty(mFilterName)) {
            mFilterName = name;
        }
    }

    private void setSelectedTag() {
        vGrbTag.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(this,"调节强度",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setName() {
        if (!TextUtils.isEmpty(mFilterName)) {
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


    public void setImage(Uri uri) {
        mUri = uri;

        if (mState == STATE_UN_LOAD) {
            updateSelectedGpuImage();
        }
    }


    /**
     * 更新滤镜预览图界面
     */
    private void updateSelectedGpuImage() {
        Log.d("GpuImg--" + mState, "updateSelectedGpuImage");
        setCheckTagView();

        if (mUri == null) {
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
        if (isChecked()) {
            vGrbTag.setVisibility(View.VISIBLE);
        } else {
            vGrbTag.setVisibility(View.INVISIBLE);
        }
    }

    private void loadGpuImage() {
        if (mUri != null) {
            mState = STATE_LOADING;
            setLoadingView();
            getGpuImage();
        }
    }

    private void setLoadingView() {
        mLayoutGrbLoading.setVisibility(View.VISIBLE);
    }

    private void setLoadedView() {
        mLayoutGrbLoading.setVisibility(View.INVISIBLE);
    }

    /**
     * 获取并加载滤镜效果
     */
    private void getGpuImage() {
        Log.d("GpuImg--state:" + mState, "getGpuImageCompleted");
        if (mUri == null) {
            Log.d("GpuImg", "getGpuImageCompleted" + ":  uri = null");
            return;
        }

        mState = STATE_LOADED;

        ivGrbImg.setImage(mUri);
        if (mFilter != null) {
            ivGrbImg.setFilter(mFilter);
            ivGrbImg.requestRender();
        }

        updateSelectedGpuImage();
    }


    /**
     * 原图的uri
     *
     * @return
     */
    public Uri getUri() {
        return mUri;
    }


    public GPUImageFilter getGPUImageFilter() {
        return mFilter;
    }


    public int getFilterMode(){
        return mFilterMode;
    }


}
