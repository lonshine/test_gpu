package cn.app.meiya.test.gpuimage.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.meiyaapp.meiya.R;
import com.meiyaapp.meiya.ui.image.loader.glide.SafeGlide;

import cn.app.meiya.test.gpuimage.GPUImageFilterTools;
import cn.app.meiya.test.gpuimage.utils.UriUtils;
import jp.co.cyberagent.android.gpuimage.GPUImageAlphaBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * 抽取滤镜模块的代码,暂无效
 * Created by lonshine on 15/11/5 下午5:22.
 */
public class CustomGPUImageView extends RelativeLayout {

    private GPUImageView ivGpuImg;
    private ImageView ivImageSrc;

    public GPUImageAlphaBlendFilter mFilter;
    public GPUImageFilterTools.FilterAdjuster mFilterAdjuster;

    public CustomGPUImageView(Context context) {
        super(context);
        init();
    }


    public CustomGPUImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_gpu_image_custom, this, true);
        findView(view);
    }

    private void findView(View view) {
        ivGpuImg = (GPUImageView) view.findViewById(R.id.ivGpuImg);
        ivImageSrc = (ImageView) view.findViewById(R.id.ivImageSrc);
    }


    /**
     * 初始化,加载原始图片
     * @param path
     */
    public void initImage(String path){
        SafeGlide.with(getContext()).load("file://" + path).dontAnimate().into(ivImageSrc);
        ivGpuImg.setImage(UriUtils.getUri(path));
    }


    /**
     * 显示原图
     */
    public void showSourceImg(){
        if(ivImageSrc.getVisibility() != View.VISIBLE){
            ivImageSrc.setVisibility(View.VISIBLE);
            ivGpuImg.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * 显示滤镜效果图
     */
    public void showGpuImg(){
        if(ivGpuImg.getVisibility() != View.VISIBLE){
            ivGpuImg.setVisibility(View.VISIBLE);
            ivImageSrc.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * 切换更新滤镜
     * @param filter
     * @param gpuImgUri
     */
    public void update(GPUImageFilter filter, Uri gpuImgUri) {
        String path = UriUtils.getRealFilePath(getContext(), gpuImgUri);

        if (filter != null) {
            showGpuImg();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            if (mFilter != null) {
                mFilter.recycleBitmap();
                mFilter = null;
            }
            mFilter = new GPUImageAlphaBlendFilter(1f);
            mFilter.setBitmap(bitmap);
            ivGpuImg.setFilter(mFilter);

            mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
            ivGpuImg.requestRender();
        } else {
            showSourceImg();
        }
    }


    /**
     * 调整强度
     * @param progress
     */
    public void onProgressChanged(int progress){
        if (mFilter != null) {
            if (mFilterAdjuster != null) {
                mFilterAdjuster.adjust(progress);
            }
            ivGpuImg.requestRender();
        }
    }


    public void onDestroy() {
        if (mFilter != null) {
            mFilter.recycleBitmap();
            mFilter.onDestroy();
            mFilter = null;
        }
    }

}
