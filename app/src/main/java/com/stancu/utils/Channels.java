package com.stancu.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by dapan on 2016-07-18.
 */
public class Channels {

    public static Bitmap setChannels(Bitmap source, int red, int green, int blue){
        Bitmap result = source;
        for (int i = 0; i < source.getWidth(); i++) {
            for (int j = 0; j < source.getHeight(); j++) {
                int p = source.getPixel(i,j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);

                r = red + r;
                if(r>255)r=255;
                g = green + g;
                if(g>255)g=255;
                b = blue + b;
                if(b>255)b=255;
                result.setPixel(i,j,Color.argb(Color.alpha(p),r,g,b));
            }
        }
        return result;
    }
}
