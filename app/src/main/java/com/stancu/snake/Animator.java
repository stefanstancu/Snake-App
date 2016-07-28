package com.stancu.snake;

import android.graphics.Canvas;
import android.graphics.Paint;
import com.stancu.game.Main;

/**
 * Created by dapan on 2016-07-24.
 */
public class Animator {

    private double speed,tailSpeed;
    private int x,y;
    private int tail_x,tail_y;
    private int direction,tailDirection;

    public Animator(int updateDelay){

        direction = Main.snake.direction;
        tailDirection = Main.snake.tailDirection;

        //set the position of where the animation will start
        x = Main.snake.snakeBlocks[0].getX()*Main.scaleFactorW;
        y = Main.snake.snakeBlocks[0].getY()*Main.scaleFactorH;
        tail_x = Main.snake.snakeBlocks[Main.snake.snakeBlocks.length-1].getX()*Main.scaleFactorW;
        tail_y = Main.snake.snakeBlocks[Main.snake.snakeBlocks.length-1].getY()*Main.scaleFactorH;

        //set the speed of the head animation
        if(direction==Snake.DIRECTION_LEFT||direction==Snake.DIRECTION_RIGHT) {
            speed = (double) Main.scaleFactorW / ((double) updateDelay / (1000.0 / 30.0));
        }
        else{
            speed = (double) Main.scaleFactorH / ((double) updateDelay / (1000.0 / 30.0));
        }
        //set the speed of the tail animation
        if(tailDirection==Snake.DIRECTION_LEFT||tailDirection==Snake.DIRECTION_RIGHT) {
            tailSpeed = (double) Main.scaleFactorW / ((double) updateDelay / (1000.0 / 30.0));
        }
        else{
            tailSpeed = (double) Main.scaleFactorH / ((double) updateDelay / (1000.0 / 30.0));
        }

    }

    public void update(){

        //Move the head
        switch (direction){
            case Snake.DIRECTION_UP:
                y-=speed;
                break;
            case Snake.DIRECTION_DOWN:
                y+=speed;
                break;
            case Snake.DIRECTION_LEFT:
                x-=speed;
                break;
            case Snake.DIRECTION_RIGHT:
                x+=speed;
                break;
        }

        //Move the tail
        switch (tailDirection){
            case Snake.DIRECTION_UP:
                tail_y+=tailSpeed;
                break;
            case Snake.DIRECTION_DOWN:
                tail_y-=tailSpeed;
                break;
            case Snake.DIRECTION_LEFT:
                tail_x+=tailSpeed;
                break;
            case Snake.DIRECTION_RIGHT:
                tail_x-=tailSpeed;
                break;
        }
    }

    public void draw(Canvas c, Paint p){
        //Draw the head and the neck
        c.drawBitmap(Main.snake.snakeHead[Main.snake.direction-1],x,y,p);

        switch (direction){
            case Snake.DIRECTION_UP:
                c.drawBitmap(Main.snake.snakeBodyV,x,y+Main.scaleFactorH,p);
                break;
            case Snake.DIRECTION_DOWN:
                c.drawBitmap(Main.snake.snakeBodyV,x,y-Main.scaleFactorH,p);
                break;
            case Snake.DIRECTION_LEFT:
                c.drawBitmap(Main.snake.snakeBodyH,x+Main.scaleFactorW,y,p);
                break;
            case Snake.DIRECTION_RIGHT:
                c.drawBitmap(Main.snake.snakeBodyH,x-Main.scaleFactorW,y,p);
                break;
        }

        //Draw the tail
        c.drawBitmap(Main.snake.snakeTail[tailDirection-1],tail_x,tail_y,p);
    }
}
