package com.lonshine.test.gpuimage.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.SeekBar;

import com.lonshine.test.gpuimage.GPUImageFilterTools;
import com.lonshine.test.gpuimage.R;
import com.lonshine.test.gpuimage.utils.FileUtil;
import com.lonshine.test.gpuimage.widget.GpuImgRadioButton;
import com.lonshine.test.gpuimage.widget.GpuImgRadioGroup;
import com.lonshine.test.gpuimage.widget.OnGpuImageUriListener;
import com.lonshine.test.gpuimage.widget.radio.RadioView;
import com.lonshine.test.gpuimage.widget.radio.RadioViewGroup;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jp.co.cyberagent.android.gpuimage.GPUImageAlphaBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * Created by lonshine on 15/9/30 上午11:44.
 */
public class ActivityTest extends Activity implements SeekBar.OnSeekBarChangeListener {

    @InjectView(R.id.ivTest)
    GPUImageView mIvTest;
    @InjectView(R.id.crgTest)
    GpuImgRadioGroup mCrgTest;


    public GPUImageFilter mFilter;
    public GPUImageFilterTools.FilterAdjuster mFilterAdjuster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gpu_activity_test);
        ButterKnife.inject(this);


        //删除缓存
        File picPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(picPath, "Meiya/");
        try {
            FileUtil.deleteDirectory(file);
        } catch (IOException e) {
            e.printStackTrace();
        }


        mCrgTest.setOnGpuImageUriListener(new OnGpuImageUriListener() {
            @Override
            public void onFillterCompleted(Uri uri) {
                Log.d("GpuImg--", "onFillterCompleted" + ", uri = " + uri.toString());
                setGpuImage(uri);
            }
        });


        String path = "content://media/external/images/media/30846";
        final Uri uri = Uri.parse(path);


        ((SeekBar) findViewById(R.id.seekBar)).setOnSeekBarChangeListener(this);

        mCrgTest.setOnCheckedChangeListener(new RadioViewGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioViewGroup group, int checkedId) {
                Log.d("GpuImg--", "onCheckedChanged" + ", id = " + checkedId);

                RadioView radioView = group.findRadioViewById(checkedId);
                if (radioView != null && radioView instanceof GpuImgRadioButton) {
                    final GpuImgRadioButton girBtn = (GpuImgRadioButton) radioView;
                    Uri gpuImgUri = girBtn.getGpuImgUri();
                    if (gpuImgUri != null) {
                        Log.d("GpuImg--", "previewUri" + " == " + gpuImgUri.toString());
                        setGpuImage(gpuImgUri);
                    }
                }
            }
        });

        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                initGpuImageViewFilter(uri);
            }
        }, 100);

    }

    private void initGpuImageViewFilter(Uri uri) {
        mCrgTest.setImage(uri);
        mFilter = new GPUImageAlphaBlendFilter(-1.0f);
        mIvTest.setFilter(mFilter);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mFilter != null) {
            if (mFilterAdjuster != null) {
                mFilterAdjuster.adjust(100 - progress);
            }
            mIvTest.requestRender();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    private void setGpuImage(Uri uri) {
//        String realPath = UriUtils.getRealFilePath(this, uri);
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 2;
//        Bitmap bitmap = BitmapFactory.decodeFile(realPath, options);


        mIvTest.setImage(uri);
//        mFilter = new GPUImageAlphaBlendFilter();
//        mIvTest.setFilter(mFilter);
        mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
        mIvTest.requestRender();
    }


}
