package com.stancu.snake;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.Random;

/**
 * Created by stefan on 31/05/16.
 */
public class Main extends Activity  implements View.OnTouchListener{

    MyView view;

    public static int widthBlocks = 20;
    public static int heightBlocks = 30;

    public static int scaleFactorW = 0;
    public static int scaleFactorH = 0;

    public static int widthMargin=0;
    public static int heightMargin=0;

    public static int score = 0;

    private static boolean drawCircle = false;

    float x,y;

    float x1 =0;
    float y1 =0;
    float x2=0;
    float y2=0;

    public static Paint p = new Paint();

    private enum State{
        RUN,
        PAUSE
    }

    public static State state = State.RUN;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Snake snake = new Snake();
        Snake.snakeBlocks[0]=new SnakeBlock(7,12);
        Snake.snakeBlocks[1]=new SnakeBlock(6,12);
        Snake.snakeBlocks[2]=new SnakeBlock(5,12);

        view = new MyView(this);
        view.setOnTouchListener(this);
        x = y =64;
        setContentView(view);
    }

    protected void onPause(){
        super.onPause();
        view.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.resume();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        //check for a swipe
        int e = event.getAction();
        int minDistance = 20;

        if(state==State.RUN) {
            switch (e) {
                case MotionEvent.ACTION_DOWN:
                    //set the x1 and y1 values
                    x1 = event.getX();
                    y1 = event.getY();
                    drawCircle = true;
                    break;

                case MotionEvent.ACTION_MOVE:
                    x2 = event.getX();
                    y2 = event.getY();
                    //Compare the distance and calculate the direction of the swipe
                    if (Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)) > minDistance) {
                        double angle = Math.atan((x2 - x1) / (y2 - y1));
                        if (angle >= -0.75 && angle < 0.75) {
                            if ((y2 - y1) > 0)
                                Snake.quedDirection = 3;
                            else
                                Snake.quedDirection = 1;
                        }

                        if (angle < -0.75 || angle > 0.75) {
                            if ((x2 - x1) > 0) {
                                Snake.quedDirection = 2;
                            } else {
                                Snake.quedDirection = 4;
                            }
                        }

                    }

                    break;
                case MotionEvent.ACTION_UP:
                    x1 = y1 = x2 =y2=0;
                    drawCircle = false;
                    break;
            }

        }

        if(state==State.PAUSE){
            if(e==MotionEvent.ACTION_DOWN){
                reset();
                state=State.RUN;
            }
        }

        return true;
    }

    public class MyView extends SurfaceView implements Runnable{

        Thread t = null;
        SurfaceHolder holder;
        boolean isOK = false;
        boolean appleSpawned = false;

        public MyView(Context context){
            super(context);
            holder = getHolder();
        }

        @Override
        public void run() {

            while(isOK){
                //performs canvas drawing
                if(!holder.getSurface().isValid()){
                    continue;
                }

                long BTime = System.currentTimeMillis();
                Canvas c = holder.lockCanvas();
                Update(c);
                Draw(c);
                holder.unlockCanvasAndPost(c);

                long ATime = System.currentTimeMillis();
                long STime = 150-(ATime-BTime);
                if(STime<0)STime=10;
                try {
                    t.sleep(STime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

        public void pause(){
            isOK = false;
            while(true){
                try{
                    t.join();
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                break;
            }
            t = null;
        }

        public void resume(){
            isOK=true;
            t = new Thread(this);
            t.start();
        }
    }
    private void Update(Canvas c){

        if (scaleFactorH == 0) scaleFactorH = c.getHeight() / heightBlocks;
        if (scaleFactorW == 0) scaleFactorW = c.getWidth() / widthBlocks;
        if (widthMargin==0) widthMargin = (c.getWidth()%widthBlocks)/2;
        if (heightMargin==0) heightMargin = (c.getHeight()%heightBlocks)/2;

        if(!Snake.isInit) {
            Snake.initSnake(this);
            Snake.isInit = true;
        }

        //Create the Apple
        if (!view.appleSpawned) {
            new Apple(RandomInt(0, widthBlocks), RandomInt(0, heightBlocks));
            view.appleSpawned = true;
        }

        //The quedDirection is so that the user can input a direction change during the update
        //and have it be applied as soon as the next update occurs.
        //This prevents the input from being applied between the Update and the Draw functions.

        if(state==State.RUN) {

            if (Snake.direction != oppositeDirection(Snake.quedDirection))
                Snake.direction = Snake.quedDirection;

            moveSnakeBlock(Snake.direction);

            //collision check with the apple
            if (Snake.snakeBlocks[0].getX() == Apple.x && Snake.snakeBlocks[0].getY() == Apple.y) {
                Apple.setPosition(RandomInt(1, widthBlocks-1), RandomInt(1, heightBlocks-1));
                Snake.addBlock();
                //increase score
                score += 10;
            }

            //check exit of the play area (results in game over)
            if(Snake.snakeBlocks[0].getX()<=0||Snake.snakeBlocks[0].getX()>=widthBlocks)
                gameOver();

            else if(Snake.snakeBlocks[0].getY()<=0||Snake.snakeBlocks[0].getY()>=heightBlocks)
                gameOver();

             //collision check with the snake itself, after you move the head
            for (int i = 1; i < Snake.snakeBlocks.length; i++) {
                if (Snake.snakeBlocks[0].getPosition().equals(Snake.snakeBlocks[i].getPosition()))
                    gameOver();
            }
        }
    }

    void Draw(Canvas c){
        //The Draw function
        //must account for the width and the height borders

        c.drawARGB(225,192,168,35);

        //draw the borders
        c.drawRect(0,0,widthMargin,c.getHeight(),p);
        c.drawRect(c.getWidth()-widthMargin,0,c.getWidth(),c.getHeight(),p);
        c.drawRect(0,0,c.getWidth(),heightMargin,p);
        c.drawRect(0,c.getHeight()-heightMargin,c.getWidth(),c.getHeight(),p);

        Snake.drawSnake(c,p);
        Apple.Draw(c);

        p.setTextSize(50);

        //Draw the grid lines for debug purposes
        p.setColor(Color.BLACK);
//        for (int i = 0; i <= widthBlocks; i++) {
//            c.drawLine((i*scaleFactorW)+widthMargin,0,(i*scaleFactorW)+widthMargin,c.getHeight(),p);
//        }
//        for (int i = 0; i < heightBlocks; i++) {
//            c.drawLine(0,(i*scaleFactorH)+heightMargin,c.getWidth(),(i*scaleFactorH)+heightMargin,p);
//        }

        //c.drawText("Direction: "+Integer.toString(Snake.direction),c.getWidth()/2-150,40,p);
        c.drawText("Score: "+Integer.toString(score),c.getWidth()/40,(c.getHeight()*15/16),p);
        if(drawCircle) {
            p.setColor(Color.RED);
            c.drawCircle(x1, y1, scaleFactorW, p);
            p.setColor(Color.BLACK);
            p.setStyle(Paint.Style.STROKE);
            c.drawCircle(x1,y1,scaleFactorW*3,p);
            p.setStyle(Paint.Style.FILL);
            p.setColor(Color.BLACK);
            c.drawLine(0,y1-x1,c.getWidth(),c.getWidth()+y1-x1,p);
            c.drawLine(0,y1+x1,c.getWidth(),y1+x1-c.getWidth(),p);
        }

        if(state==State.PAUSE){
            c.drawText("YOU LOSE Score: "+Integer.toString(score),c.getWidth()/2-200,c.getHeight()/2,p);
        }
    }

    static void moveSnakeBlock(int dir){
        //Set the array order
        SnakeBlock [] temp = new SnakeBlock[Snake.snakeBlocks.length];
        for (int i = 0; i < Snake.snakeBlocks.length-1; i++) {
            temp[i+1]=Snake.snakeBlocks[i];
        }
        temp[0]=Snake.snakeBlocks[Snake.snakeBlocks.length-1];

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
        Snake.snakeBlocks=temp;
    }

    private static void reset(){
        score = 0;
        Snake.snakeBlocks=new SnakeBlock[3];
        Snake.snakeBlocks[0]=new SnakeBlock(7,12);
        Snake.snakeBlocks[1]=new SnakeBlock(6,12);
        Snake.snakeBlocks[2]=new SnakeBlock(5,12);

        Snake.direction = 2;
    }

    private static void gameOver(){
        state=State.PAUSE;
    }

    public static int RandomInt(int min, int max){
        Random rnd = new Random();
        int r = rnd.nextInt((max - min) + 1) + min;
        return r;
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
