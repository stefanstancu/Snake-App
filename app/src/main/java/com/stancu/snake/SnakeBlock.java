package com.stancu.snake;

import android.graphics.Bitmap;
import android.graphics.Point;
import com.stancu.game.Main;

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
        /**
         * Called at the end of the snake update
         * This function only affects the tail,
         * the neck and the head as the rest of snakeBlocks do not change per update
         **/

        //Setting the head sprite in the correct direction
        //Main.snake.snakeBlocks[0].image=Main.snake.snakeHead[Main.snake.direction-1];
        Main.snake.snakeBlocks[0].image=null;

        //Setting the neck sprite in the correct direction
        SnakeBlock head = Main.snake.snakeBlocks[0];
        SnakeBlock neck = Main.snake.snakeBlocks[1];
        SnakeBlock third = Main.snake.snakeBlocks[2];

        int bodyDir = 0;
        int dir = Main.snake.direction;

        switch (neck.getX()-third.getX()){
            case -1:
                bodyDir = Snake.DIRECTION_RIGHT;
                break;
            case 0:
                switch (neck.getY()-third.getY()){
                    case -1:
                        bodyDir=Snake.DIRECTION_DOWN;
                        break;
                    case 1:
                        bodyDir=Snake.DIRECTION_UP;
                        break;
                }
                break;
            case 1:
                bodyDir = Snake.DIRECTION_LEFT;
                break;
        }
        if((bodyDir==Snake.DIRECTION_UP&&dir==Snake.DIRECTION_DOWN)||(bodyDir==Snake.DIRECTION_DOWN&&dir==Snake.DIRECTION_UP)){
            neck.image=Main.snake.snakeBodyV;
        }
         else if((bodyDir==Snake.DIRECTION_RIGHT&&dir==Snake.DIRECTION_LEFT)||(bodyDir==Snake.DIRECTION_LEFT&&dir==Snake.DIRECTION_RIGHT)){
            neck.image=Main.snake.snakeBodyH;
        }
        else if((bodyDir==Snake.DIRECTION_UP&&dir==Snake.DIRECTION_RIGHT)||(bodyDir==Snake.DIRECTION_RIGHT&&dir==Snake.DIRECTION_UP)){
            neck.image=Main.snake.snakeBend[3];
        }
        else if((bodyDir==Snake.DIRECTION_UP&&dir==Snake.DIRECTION_LEFT)||(bodyDir==Snake.DIRECTION_LEFT&&dir==Snake.DIRECTION_UP)){
            neck.image=Main.snake.snakeBend[2];
        }
        else if((bodyDir==Snake.DIRECTION_RIGHT&&dir==Snake.DIRECTION_DOWN)||(bodyDir==Snake.DIRECTION_DOWN&&dir==Snake.DIRECTION_RIGHT)){
            neck.image=Main.snake.snakeBend[0];
        }
        else if((bodyDir==Snake.DIRECTION_LEFT&&dir==Snake.DIRECTION_DOWN)||(bodyDir==Snake.DIRECTION_DOWN&&dir==Snake.DIRECTION_LEFT)){
            neck.image=Main.snake.snakeBend[1];
        }

        //Setting the tail sprite in the correct direction

        SnakeBlock tail = Main.snake.snakeBlocks[Main.snake.snakeBlocks.length-1];
        SnakeBlock preTail = Main.snake.snakeBlocks[Main.snake.snakeBlocks.length-2];
        switch (tail.getX()-preTail.getX()){
            case 1:
                tail.image = Main.snake.snakeTail[1];
                Main.snake.tailDirection = Snake.DIRECTION_RIGHT;
                break;
            case 0:
                switch (tail.getY()-preTail.getY()){
                    case 1:
                        tail.image = Main.snake.snakeTail[2];
                        Main.snake.tailDirection = Snake.DIRECTION_DOWN;
                        break;
                    case -1:
                        tail.image = Main.snake.snakeTail[0];
                        Main.snake.tailDirection = Snake.DIRECTION_UP;
                        break;
                }
                break;
            case -1:
                tail.image = Main.snake.snakeTail[3];
                Main.snake.tailDirection = Snake.DIRECTION_LEFT;
                break;
        }
        //temporary disabling
        Main.snake.snakeBlocks[Main.snake.snakeBlocks.length-1].image = null;
    }
}
