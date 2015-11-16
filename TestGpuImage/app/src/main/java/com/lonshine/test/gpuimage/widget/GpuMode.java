package com.lonshine.test.gpuimage.widget;

/**
 * 滤镜类型
 * Created by lonshine on 15/10/10 下午6:22.
 */
public final class GpuMode {
    public final static int NULL = 0x00;
    public final static int CONTRAST = 0x01;
    public final static int INVERT = 0x02;
    public final static int PIXELATION = 0x03;
    public final static int HUE = 0x04;
    public final static int GAMMA = 0x05;
    public final static int BRIGHTNESS = 0x06;
    public final static int SEPIA = 0x07;
    public final static int GRAYSCALE = 0x08;
    public final static int SHARPNESS = 0x09;

    public final static int LOOKUP = 0x20;
    public final static int CUSTOME = 0x21;

    public final static int LOOKUP_COLD = 0x32;
    public final static int LOOKUP_FUJICOLOR = 0x33;
    public final static int LOOKUP_INDUSTRIAL = 0x34;
    public final static int LOOKUP_JAPANESE = 0x35;
    public final static int LOOKUP_LIGHT = 0x36;
    public final static int LOOKUP_LOMO = 0x37;
    public final static int LOOKUP_SUPRA = 0x38;
}
