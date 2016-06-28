package com.stancu.snake;

import android.graphics.Bitmap;
import android.graphics.Point;

/**
 * Created by stefan on 03/06/16.
 */
public class SnakeBlock {

    public int x,y;
    public Bitmap image;

    public SnakeBlock(int x, int y){
        this.x = x;
        this.y = y;
        //image = Snake.snakeBodyV;
    }

    public void setXY(int x, int y){
        this.x=x;
        this.y=y;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public Point getPosition(){
        return new Point(x,y);
    }

    public static void setSprites(){
        //Called after the move logic, befor the draw
        //This function will be called the most, as the logic for the adjacent blocks needs to be done.
        //However this must on be called on the second block and the tail as all others remain the same

        //The head
        Snake.snakeBlocks[0].image=Snake.snakeHead[Snake.direction-1];

        //The second block
        SnakeBlock head = Snake.snakeBlocks[0];
        SnakeBlock neck = Snake.snakeBlocks[1];
        SnakeBlock third = Snake.snakeBlocks[2];

        int bodyDir = 0;
        int dir = Snake.direction;

        switch (neck.getX()-third.getX()){
            case -1:
                bodyDir = 2;
                break;
            case 0:
                switch (neck.getY()-third.getY()){
                    case -1:
                        bodyDir=3;
                        break;
                    case 1:
                        bodyDir=1;
                        break;
                }
                break;
            case 1:
                bodyDir = 4;
                break;
        }
        if((bodyDir==1&&dir==3)||(bodyDir==3&&dir==1)){
            neck.image=Snake.snakeBodyV;
        }
        if((bodyDir==2&&dir==4)||(bodyDir==4&&dir==2)){
            neck.image=Snake.snakeBodyH;
        }
        if((bodyDir==1&&dir==2)||(bodyDir==2&&dir==1)){
            neck.image=Snake.snakeBend[3];
        }
        if((bodyDir==1&&dir==4)||(bodyDir==4&&dir==1)){
            neck.image=Snake.snakeBend[2];
        }
        if((bodyDir==2&&dir==3)||(bodyDir==3&&dir==2)){
            neck.image=Snake.snakeBend[0];
        }
        if((bodyDir==4&&dir==3)||(bodyDir==3&&dir==4)){
            neck.image=Snake.snakeBend[1];
        }

        //The Tail

        SnakeBlock tail = Snake.snakeBlocks[Snake.snakeBlocks.length-1];
        SnakeBlock preTail = Snake.snakeBlocks[Snake.snakeBlocks.length-2];
        switch (tail.getX()-preTail.getX()){
            case 1:
                tail.image = Snake.snakeTail[1];
                break;
            case 0:
                switch (tail.getY()-preTail.getY()){
                    case 1:
                        tail.image = Snake.snakeTail[2];
                        break;
                    case -1:
                        tail.image = Snake.snakeTail[0];
                        break;
                }
                break;
            case -1:
                tail.image = Snake.snakeTail[3];
                break;
        }

    }
}
