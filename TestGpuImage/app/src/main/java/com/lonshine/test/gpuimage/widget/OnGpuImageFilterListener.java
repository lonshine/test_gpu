package com.lonshine.test.gpuimage.widget;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by lonshine on 15/11/2 下午5:03.
 */
public interface OnGpuImageFilterListener {
    public void onFillterChanged(GPUImageFilter filter);
    public void onProgressChanged(int progress);
}
