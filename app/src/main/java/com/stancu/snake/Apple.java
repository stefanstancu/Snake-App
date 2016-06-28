package com.stancu.snake;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by stefan on 09/06/16.
 */
public class Apple {
    static int x,y;
    static Bitmap image;
    static Paint p = new Paint();

    public Apple(int inx, int iny){
        image = Bitmap.createBitmap(Snake.snake_graphics,0,193,61,62);
        x=inx;
        y=iny;
    }

    public static void setPosition(int inx, int iny){
        x = inx;
        y = iny;
    }

    public static void Draw(Canvas c){
        c.drawBitmap(image,x*Main.scaleFactorW+Main.widthMargin,y*Main.scaleFactorH+Main.heightMargin,p);
    }

}
