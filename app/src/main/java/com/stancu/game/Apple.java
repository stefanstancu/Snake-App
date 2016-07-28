package com.stancu.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.stancu.snake.Snake;

/**
 * Created by stefan on 09/06/16.
 */
public class Apple {

    public int x,y;

    private Bitmap image;

    public Apple(int x, int y){
        image = Bitmap.createBitmap(Snake.snake_graphics,128,64,64,64);
        image = Bitmap.createScaledBitmap(image,Main.scaleFactorW,Main.scaleFactorH,false);
        this.x=x;
        this.y=y;
    }

    public void setPosition(int x, int y){
       this.x = x;
       this.y =y;
    }

    public void draw(Canvas c, Paint p){
        c.drawBitmap(image,x*Main.scaleFactorW+Main.widthMargin,y*Main.scaleFactorH+Main.heightMargin,p);
    }

}
