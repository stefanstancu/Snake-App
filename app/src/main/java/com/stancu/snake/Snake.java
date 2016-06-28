package com.stancu.snake;

import android.content.Context;
import android.graphics.*;

import java.util.logging.Filter;

/**
 * Created by stefan on 31/05/16.
 */
public class Snake{

    public static int direction=2;
    public static int quedDirection=direction;

    public static Bitmap snake_graphics;
    public static Boolean isInit = false;

    public static Bitmap[] snakeHead = new Bitmap[4];

    public static Bitmap snakeBodyH;
    public static Bitmap snakeBodyV;

    public static Bitmap[] snakeBend = new Bitmap[4];
    public static Bitmap[] snakeTail = new Bitmap[4];

    public static SnakeBlock[] snakeBlocks = new SnakeBlock[3];

    public Snake() {

    }

    public static void drawSnake(Canvas c, Paint p){

        SnakeBlock.setSprites();

        for (SnakeBlock block : snakeBlocks) {
            if(block.image!=null)
            c.drawBitmap(block.image,block.getX()*Main.scaleFactorW+Main.widthMargin,block.getY()*Main.scaleFactorH+Main.heightMargin,p);
        }
    }

    public static void addBlock(){
        //Resize the array and add a block
        //The block is added at the end of the chain, on top of the existing tail
        SnakeBlock[] temp = new SnakeBlock[Snake.snakeBlocks.length+1];
        for (int i = 0; i < Snake.snakeBlocks.length; i++) {
            temp[i]=Snake.snakeBlocks[i];
        }
        temp[temp.length-1]= new SnakeBlock(temp[temp.length-2].getX(),temp[temp.length-2].getY());
        Snake.snakeBlocks = temp;
    }

    public static void initSnake(Context context){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        snake_graphics=BitmapFactory.decodeResource(context.getResources(),R.drawable.snake_graphics,options);

        snakeHead[0] = Bitmap.createBitmap(snake_graphics,194,2,62,64);
        snakeHead[1] = Bitmap.createBitmap(snake_graphics,256,2,62,62);
        snakeHead[3] = Bitmap.createBitmap(snake_graphics,194,66,62,62);
        snakeHead[2] = Bitmap.createBitmap(snake_graphics,258,64,62,64);

        for (int i = 0; i < snakeHead.length; i++) {
            snakeHead[i]=Bitmap.createScaledBitmap(snakeHead[i],Main.scaleFactorW,Main.scaleFactorH,false);
        }

        snakeBodyH = Bitmap.createBitmap(snake_graphics,64,6,64,64);
        snakeBodyH = Bitmap.createScaledBitmap(snakeBodyH,Main.scaleFactorW,Main.scaleFactorH,false);
        snakeBodyV = Bitmap.createBitmap(snake_graphics,128,64,64,64);
        snakeBodyV = Bitmap.createScaledBitmap(snakeBodyV,Main.scaleFactorW,Main.scaleFactorH,false);

        snakeBend[0] = Bitmap.createBitmap(snake_graphics,0,6,64,64);
        snakeBend[1] = Bitmap.createBitmap(snake_graphics,128,6,64,64);
        snakeBend[2] = Bitmap.createBitmap(snake_graphics,128,134,64,64);
        snakeBend[3] = Bitmap.createBitmap(snake_graphics,0,72,64,64);

        for (int i = 0; i < snakeBend.length; i++) {
            snakeBend[i]=Bitmap.createScaledBitmap(snakeBend[i],Main.scaleFactorW,Main.scaleFactorH,false);
        }

        snakeTail[2] = Bitmap.createBitmap(snake_graphics,193,128,60,60);
        snakeTail[3] = Bitmap.createBitmap(snake_graphics,256,134,63,52);
        snakeTail[1] = Bitmap.createBitmap(snake_graphics,192,198,60,52);
        snakeTail[0] = Bitmap.createBitmap(snake_graphics,255,187,60,64);

        for (int i = 0; i < snakeBend.length; i+=2) {
            snakeTail[i]=Bitmap.createScaledBitmap(snakeTail[i],Main.scaleFactorW,Main.scaleFactorH,false);
        }
        snakeTail[1]=Bitmap.createScaledBitmap(snakeTail[1],Main.scaleFactorW,(int)(Main.scaleFactorH*0.8),false);
        snakeTail[3]=Bitmap.createScaledBitmap(snakeTail[3],Main.scaleFactorW,(int)(Main.scaleFactorH*0.8),false);
    }
}
