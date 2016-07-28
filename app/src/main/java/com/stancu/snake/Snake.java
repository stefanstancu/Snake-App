package com.stancu.snake;

import android.content.Context;
import android.graphics.*;
import com.stancu.game.*;

import java.util.logging.Filter;

/**
 * Created by stefan on 31/05/16.
 */
public class Snake{

    public static final int DIRECTION_UP = 1;
    public static final int DIRECTION_RIGHT = 2;
    public static final int DIRECTION_DOWN = 3;
    public static final int DIRECTION_LEFT = 4;

    public int direction=DIRECTION_RIGHT;
    public int tailDirection=DIRECTION_LEFT;
    public int quedDirection=direction;

    public static Bitmap snake_graphics;

    public Bitmap[] snakeHead = new Bitmap[4];

    public Bitmap snakeBodyH;
    public Bitmap snakeBodyV;

    public Bitmap[] snakeBend = new Bitmap[4];
    public Bitmap[] snakeTail = new Bitmap[4];

    public SnakeBlock[] snakeBlocks = new SnakeBlock[3];

    public Snake() {

    }

    public void init(Context context){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        snake_graphics=BitmapFactory.decodeResource(context.getResources(),R.drawable.snake_graphic,options);

        //Head
        for (int i = 0; i < 4; i++) {
            snakeHead[i]= Bitmap.createBitmap(snake_graphics,i*64,0,64,64);
            snakeHead[i]=Bitmap.createScaledBitmap(snakeHead[i], Main.scaleFactorW,Main.scaleFactorH,false);
        }

        //Body
        snakeBodyH = Bitmap.createBitmap(snake_graphics,0,64,64,64);
        snakeBodyH = Bitmap.createScaledBitmap(snakeBodyH,Main.scaleFactorW,Main.scaleFactorH,false);
        snakeBodyV = Bitmap.createBitmap(snake_graphics,64,64,64,64);
        snakeBodyV = Bitmap.createScaledBitmap(snakeBodyV,Main.scaleFactorW,Main.scaleFactorH,false);

        //Tail
        for (int i = 0; i <4; i++) {
            snakeTail[i]=Bitmap.createBitmap(snake_graphics,i*64,128,64,64);
            snakeTail[i]=Bitmap.createScaledBitmap(snakeTail[i],Main.scaleFactorW,Main.scaleFactorH,false);
        }

        //Bends
        for (int i = 0; i < 4; i++) {
            snakeBend[i]= Bitmap.createBitmap(snake_graphics,i*64,192,64,64);
            snakeBend[i]=Bitmap.createScaledBitmap(snakeBend[i],Main.scaleFactorW,Main.scaleFactorH,false);
        }

        //Set the original snakeblocks
        snakeBlocks[0]=new SnakeBlock(7,12);
        snakeBlocks[1]=new SnakeBlock(6,12);
        snakeBlocks[2]=new SnakeBlock(5,12);
    }

    public void update(){

        /**
         * The quedDirection is so that the user can input a direction change during the update
         * and have it be applied as soon as the next update occurs.
         * This prevents the input from being applied between the Update and the Draw functions.
         */
        if (direction != oppositeDirection(quedDirection)&&direction!=quedDirection) {
            //This is called only on a direction change. The time that it pauses will be used for the animation for the head to turn
            direction = quedDirection;
            Main.setDTC();
        }

        moveSnakeBlock(direction);

        //Start of collision checking

        //collision check with the apple
        if (snakeBlocks[0].getX() == Main.apple.x && snakeBlocks[0].getY() == Main.apple.y) {
            Main.apple.setPosition(Main.RandomInt(1, Main.widthBlocks-1), Main.RandomInt(1, Main.heightBlocks-(UI.getBlockHeight()+1)));
            addBlock();
            //increase score
            UI.score ++;
        }

        //check exit of the play area (results in game over)
        if(snakeBlocks[0].getX()<=0||snakeBlocks[0].getX()>=Main.widthBlocks)
            Main.gameOver();

        else if(snakeBlocks[0].getY()<=0||snakeBlocks[0].getY()>=(Main.heightBlocks-UI.getBlockHeight()))
            Main.gameOver();

        //collision check with the snake itself, after you move the head
        for (int i = 1; i < snakeBlocks.length; i++) {
            if (snakeBlocks[0].getPosition().equals(snakeBlocks[i].getPosition()))
                Main.gameOver();
        }

        //End of collision checking

        SnakeBlock.setSprites();
    }

    public void draw(Canvas c, Paint p){

        for (SnakeBlock block : snakeBlocks) {
            if(block.image!=null)
            c.drawBitmap(block.image,block.getX()*Main.scaleFactorW,block.getY()*Main.scaleFactorH,p);
        }
    }

    public void addBlock(){
        //Resize the array and add a block
        //The block is added at the end of the chain, on top of the existing tail
        SnakeBlock[] temp = new SnakeBlock[snakeBlocks.length+1];
        for (int i = 0; i <snakeBlocks.length; i++) {
            temp[i]=snakeBlocks[i];
        }
        temp[temp.length-1]= new SnakeBlock(temp[temp.length-2].getX(),temp[temp.length-2].getY());
        snakeBlocks = temp;
    }

     private void moveSnakeBlock(int dir){
        //Set the array order
        SnakeBlock [] temp = new SnakeBlock[snakeBlocks.length];
        for (int i = 0; i < snakeBlocks.length-1; i++) {
            temp[i+1]=snakeBlocks[i];
        }
        temp[0]=snakeBlocks[snakeBlocks.length-1];

        //Set the x,y values relative to the second one
        switch (dir){
            case 1:
                temp[0].setXY(temp[1].getX(),temp[1].getY()-1);
                break;
            case 2:
                temp[0].setXY(temp[1].getX()+1,temp[1].getY());
                break;
            case 3:
                temp[0].setXY(temp[1].getX(),temp[1].getY()+1);
                break;
            case 4:
                temp[0].setXY(temp[1].getX()-1,temp[1].getY());
                break;
        }
        snakeBlocks=temp;
    }

    private int oppositeDirection(int dir){
        int d = 0;
        switch (dir){
            case 1:
                d = 3;
                break;
            case 2:
                d = 4;
                break;
            case 3:
                d = 1;
                break;
            case 4:
                d = 2;
                break;

        }
        return d;
    }
}
