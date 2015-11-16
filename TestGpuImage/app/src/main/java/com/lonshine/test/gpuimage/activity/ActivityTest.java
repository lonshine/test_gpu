package cn.app.meiya.test.gpuimage.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.SeekBar;

import com.meiyaapp.meiya.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.app.meiya.test.gpuimage.GPUImageFilterTools;
import cn.app.meiya.test.gpuimage.widget.GpuImgRadioGroup;
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


//        //删除缓存
//        File picPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        File file = new File(picPath, "Meiya/");
//        try {
//            FileUtil.deleteDirectory(file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        mCrgTest.setOnGpuImgUriCompeledCallback(new OnGpuImgUriCompeledCallback() {
//            @Override
//            public void onGpuImgUriCompleted(Uri uri) {
//                Loger.d("GpuImg--", "onFillterCompleted" + ", uri = " + uri.toString());
//                setGpuImage(uri);
//            }
//        });
//
//
//        String path = "content://media/external/images/media/30257";
//        final Uri uri = Uri.parse(path);
//
//
//        ((SeekBar) findViewById(R.id.seekBar)).setOnSeekBarChangeListener(this);
//
//        mCrgTest.setOnCheckedChangeListener(new RadioViewGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioViewGroup group, int checkedId) {
//                Loger.d("GpuImg--", "onCheckedChanged" + ", id = " + checkedId);
//
//                RadioView radioView = group.findRadioViewById(checkedId);
//                if (radioView != null && radioView instanceof GpuImgRadioButton) {
//                    final GpuImgRadioButton girBtn = (GpuImgRadioButton) radioView;
//                    Uri gpuImgUri = girBtn.getGpuImgUri();
//                    if (gpuImgUri != null) {
//                        Loger.d("GpuImg--", "previewUri" + " == " + gpuImgUri.toString());
//                        setGpuImage(gpuImgUri);
//                    }
//                }
//            }
//        });
//
//        new Handler(getMainLooper()).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                initGpuImageViewFilter(uri);
//            }
//        }, 100);

    }

//    private void initGpuImageViewFilter(Uri uri) {
//        mCrgTest.setImage(uri);
//        mFilter = new GPUImageAlphaBlendFilter(-1.0f);
//        mIvTest.setFilter(mFilter);
//    }


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
//
//
//        mIvTest.setImage(uri);
////        mFilter = new GPUImageAlphaBlendFilter();
////        mIvTest.setFilter(mFilter);
//        mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
//        mIvTest.requestRender();
    }


}
