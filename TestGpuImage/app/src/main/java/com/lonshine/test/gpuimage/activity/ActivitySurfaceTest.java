package com.lonshine.test.gpuimage.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

import com.lonshine.test.gpuimage.GPUImageFilterTools;
import com.lonshine.test.gpuimage.R;
import com.lonshine.test.gpuimage.widget.GpuImgRadioGroup;
import com.lonshine.test.gpuimage.widget.GpuImgSurfaceRadioButton;
import com.lonshine.test.gpuimage.widget.radio.RadioView;
import com.lonshine.test.gpuimage.widget.radio.RadioViewGroup;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * Created by lonshine on 15/9/30 上午11:44.
 */
public class ActivitySurfaceTest extends Activity implements SeekBar.OnSeekBarChangeListener {

    @InjectView(R.id.ivTest)
    GPUImageView mIvTest;
    @InjectView(R.id.crgTest)
    GpuImgRadioGroup mCrgTest;

    Uri uri;
    public GPUImageFilter mFilter;
    public GPUImageFilterTools.FilterAdjuster mFilterAdjuster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gpu_activity_test_surface);
        ButterKnife.inject(this);


        String path = "content://media/external/images/media/30846";
        uri = Uri.parse(path);

        mIvTest.setImage(uri);

        mCrgTest.setOnCheckedChangeListener(new RadioViewGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioViewGroup group, int checkedId) {
                Log.d("GpuImg--", "onCheckedChanged" + ", id = " + checkedId);

                RadioView radioView = group.findRadioViewById(checkedId);
                if (radioView != null && radioView instanceof GpuImgSurfaceRadioButton) {
                    GpuImgSurfaceRadioButton girBtn = (GpuImgSurfaceRadioButton) radioView;
                    setGpuImage(girBtn);
                }
            }
        });

        mCrgTest.setImage(uri);


        ((SeekBar) findViewById(R.id.seekBar)).setOnSeekBarChangeListener(this);
    }


    private void setGpuImage(GpuImgSurfaceRadioButton girBtn){
        if(girBtn == null){
            return;
        }
        mIvTest.setImage(uri);
        GPUImageFilter filter = girBtn.getGPUImageFilter();
        if (filter != null) {
            Log.d("GpuImg--", "filter" + " == " + filter.getClass().getSimpleName().toString());
            switchFilterTo(filter);
        }
    }


    private void switchFilterTo(final GPUImageFilter filter) {
        if (mFilter == null
                || (filter != null && !mFilter.getClass().equals(filter.getClass()))) {
            mFilter = filter;
            mIvTest.setFilter(mFilter);
            mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
        }
        mIvTest.requestRender();
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mFilterAdjuster != null) {
            mFilterAdjuster.adjust(progress);
        }
        mIvTest.requestRender();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
