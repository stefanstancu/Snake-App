package com.stancu.game;

import android.content.Context;
import android.graphics.*;
import com.stancu.snake.R;

import static com.stancu.utils.BitFunc.rotateBitmap;

/**
 * Created by dapan on 2016-07-16.
 */
public class Background {

    private static Bitmap background_image;
    private static Bitmap grass_borderL,grass_borderR;

    public static void initBackground(Context context, int width, int height){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        background_image = BitmapFactory.decodeResource(context.getResources(), R.drawable.background,options);
        grass_borderL = BitmapFactory.decodeResource(context.getResources(),R.drawable.grass_border,options);

        grass_borderL = rotateBitmap(grass_borderL,90);
        grass_borderR = rotateBitmap(grass_borderL,-180);

    }

    public static void DrawBackground(Canvas c, Paint p){
        int width_repetions = c.getWidth()/background_image.getWidth();
        int height_repetions = c.getHeight()/background_image.getHeight();
        for (int i = 0; i <= width_repetions; i++) {
            for (int j = 0; j <=height_repetions ; j++) {
                c.drawBitmap(background_image,i*background_image.getWidth(),j*background_image.getHeight(),p);
            }
        }
        c.drawBitmap(background_image,0,0,p);
        int repetitions = c.getHeight()/grass_borderL.getHeight();
        for (int i = 0; i <= repetitions; i++) {
            c.drawBitmap(grass_borderL,0,i*grass_borderL.getHeight(),p);
            c.drawBitmap(grass_borderR,c.getWidth()-grass_borderR.getWidth(),i*grass_borderR.getHeight(),p);
        }
    }
}
