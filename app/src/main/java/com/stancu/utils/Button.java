package com.stancu.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by aCamp on 7/22/2016.
 */
public class Button {

    public static final int PLAY_BUTTON=1;
    public static final int SETTINGS_BUTTON=2;
    public static final int MENU_BUTTON=2;
    public static final int AESTHETIC=4;

    public int id;

    private int x,y;
    private int width,height;
    private Bitmap image;
    private Rect collider;

    public Button(int x, int y, int width, int height, int id){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.id = id;

        collider = new Rect(x, y, x+width, y+height);
    }

    public void drawButton(Canvas c, Paint p){
        c.drawBitmap(image,x,y,p);
    }

    public void setImage(Bitmap image){
        this.image = image;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getWidth(){
        return width;
    }
    public  int getHeight(){
        return height;
    }
    public Rect getCollider(){return collider;}
}
