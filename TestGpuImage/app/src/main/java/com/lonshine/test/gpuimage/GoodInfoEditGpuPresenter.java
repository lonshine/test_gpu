package com.lonshine.test.gpuimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.view.View;

import com.meiyaapp.meiya.R;
import com.meiyaapp.meiya.library.base.ViewSize;
import com.meiyaapp.meiya.library.list.base.ViewRect;
import com.meiyaapp.meiya.ui.good.gpuimage.GPUImageFilterTools;
import com.meiyaapp.meiya.ui.good.gpuimage.utils.GpuHelper;
import com.meiyaapp.meiya.ui.good.gpuimage.utils.UriUtils;
import com.meiyaapp.meiya.ui.good.gpuimage.widget.selector.GpuImgRadioGroup;
import com.meiyaapp.meiya.ui.good.gpuimage.widget.selector.OnGpuImageFilterListener;
import com.meiyaapp.meiya.ui.image.camera.SquareImageView;
import com.meiyaapp.meiya.ui.image.loader.glide.SafeGlide;

import java.io.File;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import cn.app.meiya.aa.util.Loger;
import cn.app.meiya.aa.util.TextUtil;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageAlphaBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * 抽取滤镜编辑代码
 * Created by lonshine on 15/11/13 下午3:19.
 */
public class GoodInfoEditGpuPresenter {

    public static final String TAG = "GoodInfoEditGpuPresenter";


    private String mImagePath;
    SquareImageView ivImageSrc;
    GpuImgRadioGroup girgGpuFilters;
    GPUImageView ivImage;
    GoodInfoEditFragment mGoodInfoEditFragment;

    public GPUImageAlphaBlendFilter mFilter;
    public GPUImageFilterTools.FilterAdjuster mFilterAdjuster;


    public GoodInfoEditGpuPresenter(View view,String imagePath,GoodInfoEditFragment fragment) {
        mImagePath = imagePath;
        mGoodInfoEditFragment = fragment;


        initView(view);
        initImageView();
        initGpuFilterSelector(view);
    }

    private void initView(View view) {
        ivImageSrc = (SquareImageView) view.findViewById(R.id.ivImageSrc);
        ivImage = (GPUImageView) view.findViewById(R.id.ivImage);
        girgGpuFilters = (GpuImgRadioGroup) view.findViewById(R.id.girgGpuFilters);


    }

    public void initImageView(){
        //图片有两层，原图与滤镜
        ivImage.setVisibility(View.INVISIBLE);
        ivImage.setImage(UriUtils.getUri(mImagePath));

        ViewRect.getSquare(ViewSize.getScreenWidth()).setToView(ivImageSrc);
        SafeGlide.with(this).load("file://" + mImagePath).dontAnimate().into(ivImageSrc);
    }


    /**
     * 滤镜界面，选择器初始化
     */
    private void initGpuFilterSelector(View view) {
        //测算高度
        final View flEditLayout = view.findViewById(R.id.flEditLayout);
        flEditLayout.post(new Runnable() {
            @Override
            public void run() {
                girgGpuFilters.resetSettingDialogHeight(mGoodInfoEditFragment.getActivity(), flEditLayout.getHeight());
            }
        });

        Uri uri = Uri.parse(mImagePath);
        girgGpuFilters.setImage(mGoodInfoEditFragment.getActivity(), uri);

        girgGpuFilters.setOnGpuImageFilterListener(new OnGpuImageFilterListener() {
            @Override
            public void onFillterChanged(GPUImageFilter filter) {
                updateGpuImagePath(filter);
            }

            @Override
            public void onProgressChanged(int progress) {
                if (mFilter != null) {
                    if (mFilterAdjuster != null) {
                        mFilterAdjuster.adjust(progress);
                    }
                    ivImage.requestRender();
                }
            }
        });
    }


    /**
     * 切换滤镜效果后更新图片和uri
     */
    private void updateGpuImagePath(GPUImageFilter filter) {
        if (filter != null) {
            if(ivImage.getVisibility() != View.VISIBLE){
                ivImage.setVisibility(View.VISIBLE);
                ivImageSrc.setVisibility(View.INVISIBLE);
            }

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            Bitmap bitmap = BitmapFactory.decodeFile(mImagePath, options);

            GPUImage gpuImage = new GPUImage(mGoodInfoEditFragment.getActivity().getApplicationContext());
            gpuImage.setFilter(filter);
            Bitmap filterBitmap = gpuImage.getBitmapWithFilterApplied(bitmap);
            bitmap.recycle();

            if (mFilter != null) {
                mFilter.recycleBitmap();
                mFilter = null;
            }
            mFilter = new GPUImageAlphaBlendFilter(1f);
            mFilter.setBitmap(filterBitmap);
            ivImage.setFilter(mFilter);

            mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
            ivImage.requestRender();
        } else {
            if(ivImageSrc.getVisibility() != View.VISIBLE){
                ivImageSrc.setVisibility(View.VISIBLE);
                ivImage.setVisibility(View.INVISIBLE);
            }
        }
    }


    /**
     * 进入下一步,保存滤镜效果图
     */
    public void editGoodInfoCompleted() {
        if(mFilter != null && ivImage.getVisibility() == View.VISIBLE){

            Task.call(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    //原图不经过此方法
                    mGoodInfoEditFragment.showProgressDialog("正在保存图片...");
                    return null;
                }
            }, Task.UI_THREAD_EXECUTOR).onSuccess(new Continuation<Object, String>() {
                @Override
                public String then(Task<Object> task) throws Exception {

                    Loger.d("GpuImg-startNextProcess-onSuccess", "mImagePath" + " == " + mImagePath);
                    return saveGpuImageFile();
                }
            },Task.BACKGROUND_EXECUTOR).continueWith(new Continuation<String, Task<Object>>() {
                @Override
                public Task<Object> then(Task<String> task) throws Exception {
                    if (task.isCompleted()) {
                        String path = task.getResult();
                        if(!TextUtil.isEmptyOrNull(path) && new File(path).exists()) {
                            mImagePath = path;
                        }
                    }

                    Loger.d("GpuImg-startNextProcess-continueWith", "mImagePath" + " == " + mImagePath);
                    mGoodInfoEditFragment.hideProgressDialog();
                    mGoodInfoEditFragment.outInfoEditPage(mImagePath);
                    return null;
                }
            }, Task.UI_THREAD_EXECUTOR);
        }else{
            mGoodInfoEditFragment.outInfoEditPage(mImagePath);
        }

    }


    /**
     * 保存滤镜效果图
     */
    private String saveGpuImageFile() {
        try {
            final Bitmap bitmap = ivImage.capture();
            return saveBitmap2File(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return mImagePath;
        }
    }



    private String saveBitmap2File(final Bitmap bitmap) {
        String path = GpuHelper.get().saveImage(mGoodInfoEditFragment.getContext(), bitmap, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(final String path, final Uri uri) {
                mImagePath = UriUtils.getRealFilePath(mGoodInfoEditFragment.getActivity(), uri);
                bitmap.recycle();

                Loger.d("GpuImg--saveImage", "uri" + " == " + uri.toString());
                Loger.d("GpuImg--saveImage", "path" + " == " + path);
                Loger.d("GpuImg--saveImage", "mImagePath" + " == " + mImagePath);
            }
        });

        return path;
    }


    public void onDestroy() {
        if (mFilter != null) {
            mFilter.recycleBitmap();
            mFilter.onDestroy();
            mFilter = null;
        }

        if(girgGpuFilters != null){
            girgGpuFilters.onDestroy();
        }
    }


}
