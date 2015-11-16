package com.lonshine.test.gpuimage.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lonshine on 15/11/13 上午9:53.
 */
public class GpuHelper {

    private GpuHelper() {
    }

    private static GpuHelper sHelper = new GpuHelper();

    public static GpuHelper get(){
        return sHelper;
    }


    /**
     * 保存滤镜图片
     * @return
     */
    public String saveImage(Context context,Bitmap bitmap,MediaScannerConnection.OnScanCompletedListener listener) {
        String fileName = "temp-" + System.currentTimeMillis()+ ".gc";
        String path = getCacheFilePath(context);
        File file = new File(path + "" + fileName);
        try {
            file.getParentFile().mkdirs();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
            MediaScannerConnection.scanFile(context,new String[]{file.toString()}, null,listener);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }


    /**
     * 缓存目录
     * @param context
     * @return
     */
    public String getCacheFilePath(Context context){
        File cacheDir = context.getExternalFilesDir(null);
        String cachePath = null;
        if(null != cacheDir){
            cachePath = cacheDir.getAbsolutePath() + "/gpu_cache/";
        }

        /**
         * http://my.oschina.net/liucundong/blog/314520
         */
        if(TextUtils.isEmpty(cachePath)){
            if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
                cachePath = Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.meiyaapp.meiya/files/gpu_cache/";
            }else{
                Toast.makeText(context, "SD卡状态错误,请调整后重试哦。", Toast.LENGTH_SHORT).show();
            }
        }

        return cachePath;
    }


    /**
     * 删除滤镜缓存
     */
    public void cleanGpuCache(Context context) {
        File file = new File(getCacheFilePath(context));
        try {
            FileUtil.deleteDirectory(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
