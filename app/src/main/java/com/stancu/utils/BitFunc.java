package com.stancu.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * Created by dapan on 2016-07-18.
 */
public class BitFunc {

    public static Bitmap rotateBitmap(Bitmap source, float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return  Bitmap.createBitmap(source,0,0,source.getWidth(),source.getHeight(),matrix,false);
    }

    public static Bitmap scaleImageWidth(Bitmap source, int desWidth){
        Bitmap scaledBitmap = source;
        double desHeight = ((double) desWidth/(double)source.getWidth())*(double)source.getHeight();
        scaledBitmap = Bitmap.createScaledBitmap(source,desWidth,(int)desHeight,false);
        return scaledBitmap;
    }
//    public static Bitmap scaleImageHeight(Bitmap source, int desHeight){
//
//    }
}
