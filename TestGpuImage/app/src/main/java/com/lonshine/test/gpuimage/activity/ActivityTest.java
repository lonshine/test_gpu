package com.lonshine.test.gpuimage.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.lonshine.test.gpuimage.GPUImageFilterTools;
import com.lonshine.test.gpuimage.R;
import com.lonshine.test.gpuimage.utils.UriUtils;
import com.lonshine.test.gpuimage.widget.GpuImgRadioGroup;
import com.lonshine.test.gpuimage.widget.OnGpuImageFilterListener;

import butterknife.ButterKnife;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageAlphaBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageTwoInputFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * Created by lonshine on 15/9/30 上午11:44.
 */
public class ActivityTest extends Activity  {

    private static final int REQUEST_PICK_IMAGE = 1;

    public GPUImageFilter mFilter;
    public GPUImageFilterTools.FilterAdjuster mFilterAdjuster;
    private GPUImageView ivImage;
    private GpuImgRadioGroup girgGpuFilters;
    private String mImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gpu_activity_test);
        ButterKnife.inject(this);


        String path = "content://media/external/images/media/3742";
        final Uri uri = Uri.parse(path);

        mImagePath = UriUtils.getRealFilePath(this,uri);
        
        
        ivImage = (GPUImageView) findViewById(R.id.ivImage);
        girgGpuFilters = (GpuImgRadioGroup) findViewById(R.id.girgGpuFilters);


        ivImage.setImage(uri);
        girgGpuFilters.setImage(this,uri);


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


//        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//        photoPickerIntent.setType("image/*");
//        startActivityForResult(photoPickerIntent, REQUEST_PICK_IMAGE);


    }


    /**
     * 切换滤镜效果后更新图片和uri
     */
    private void updateGpuImagePath(GPUImageFilter filter) {
        if (filter != null) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            Bitmap bitmap = BitmapFactory.decodeFile(mImagePath, options);

            if(bitmap == null){
                return;
            }
            GPUImage gpuImage = new GPUImage(this);
            gpuImage.setFilter(filter);
            Bitmap filterBitmap = gpuImage.getBitmapWithFilterApplied(bitmap);
            bitmap.recycle();

            if (mFilter != null) {
                if(mFilter instanceof GPUImageTwoInputFilter){
                    ((GPUImageTwoInputFilter)mFilter).recycleBitmap();
                }
                mFilter = null;
            }
            mFilter = new GPUImageAlphaBlendFilter(1f);
            ((GPUImageAlphaBlendFilter)mFilter).setBitmap(filterBitmap);
            ivImage.setFilter(mFilter);

            mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
            ivImage.requestRender();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFilter != null) {
            if(mFilter instanceof GPUImageTwoInputFilter){
                ((GPUImageTwoInputFilter)mFilter).recycleBitmap();
            }
            mFilter.onDestroy();
            mFilter = null;
        }

        if(girgGpuFilters != null){
            girgGpuFilters.onDestroy();
        }
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case REQUEST_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    mImagePath = UriUtils.getRealFilePath(this,data.getData());
                    Log.d("图片", "" + mImagePath);


                    ivImage.setImage(Uri.parse(mImagePath));
                    girgGpuFilters.setImage(ActivityTest.this, Uri.parse(mImagePath));

                    ivImage.requestRender();
                } else {
                    finish();
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }


}
