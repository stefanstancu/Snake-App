package com.stancu.game;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import com.stancu.menu.Menu;
import com.stancu.snake.Animator;
import com.stancu.snake.Snake;
import com.stancu.snake.SnakeBlock;
import com.stancu.utils.Button;

import java.util.Random;

/**
 * Created by stefan on 31/05/16.
 *
 */
public class Main extends Activity  implements View.OnTouchListener{

    MyView view;

    public static int widthBlocks = 18;
    public static int heightBlocks = 30;

    public static int scaleFactorW = 0;
    public static int scaleFactorH = 0;

    static int widthMargin=0;
    static int heightMargin=0;

    public static int WIDTH,HEIGHT;

    private static final int FPS = 30;
    private static final int UPDATE_DELAY = 250;

    private static boolean drawCircle = false;
    private static boolean isInit = false;
    private static long dirChangeTime;
    private static long LAST_UPDATE_TIME = 0;
    private static long LAST_UPDATE_DURATION = 0;
    private static int DCcheckTime = 700;
    private static Menu menu;

    private static Animator animator;

    private float x,y;

    private float x1 =0;
    private float y1 =0;
    private float x2=0;
    private float y2=0;

    public static Snake snake;
    public static Apple apple;

    public static Paint p = new Paint();

    enum State{
        RUN,
        GAMEOVER,
        PAUSE,
        MENU
    }

    public static State state = State.MENU;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

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
                    //check menu button press
                    if(UI.getMenuButton().getCollider().intersects((int) x1, (int) y1, (int) x1+1, (int) y1+1)){
                        state = State.MENU;
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    x2 = event.getX();
                    y2 = event.getY();
                    //Compare the distance and calculate the direction of the swipe
                    if ( distanceBetweenPoints(x1,y1,x2,y2)> minDistance) {
                        double angle = Math.atan((x2 - x1) / (y2 - y1));
                        if (angle >= -0.75 && angle < 0.75) {
                            if ((y2 - y1) > 0) {
                                snake.quedDirection = Snake.DIRECTION_DOWN;
                            }
                            else {
                                snake.quedDirection = Snake.DIRECTION_UP;
                            }
                        }
                        if (angle < -0.75 || angle > 0.75) {
                            if ((x2 - x1) > 0) {
                                snake.quedDirection = Snake.DIRECTION_RIGHT;
                            } else {
                                snake.quedDirection = Snake.DIRECTION_LEFT;
                            }
                        }
                    }
                    long time = System.currentTimeMillis();
                    if((time-dirChangeTime)>DCcheckTime){
                        x1=event.getX();
                        y1=event.getY();
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    x1 = y1 = x2 =y2=0;
                    drawCircle = false;
                    break;
            }

        }

        else if(state==State.GAMEOVER){
            if(e==MotionEvent.ACTION_DOWN){
                reset();
                state=State.RUN;
            }
        }

        else if(state == State.MENU){
            switch (e){
                case MotionEvent.ACTION_DOWN:
                    x1 = event.getX();
                    y1 = event.getY();
                    //check collision with the bounding boxes from the menu
                    for (int i = 0; i < menu.buttons.length; i++) {
                        if(menu.buttons[i].getCollider().intersects((int)x1,(int)y1,(int)x1+1,(int)y1+1)){
                            if(menu.buttons[i].id == Button.PLAY_BUTTON){
                                state=State.RUN;
                            }
                        }
                    }
                    break;
            }
        }

        return true;
    }

    private void Update(Canvas c){

        initGame(c);

        if(state==State.RUN) {
            if(LAST_UPDATE_TIME+(UPDATE_DELAY-LAST_UPDATE_DURATION)<System.currentTimeMillis()) {
                //update the snake
                long a = System.currentTimeMillis();
                snake.update();
                LAST_UPDATE_DURATION = System.currentTimeMillis()-a;
                LAST_UPDATE_TIME=System.currentTimeMillis();
                //create a new animator for the snake
                animator = new Animator(UPDATE_DELAY);
            }
            else{
                //Update the animator for the snake
                if(animator!=null)
                animator.update();
            }
        }

        else if(state==State.MENU){
            menu.updateMenu();
        }
    }

    void Draw(Canvas c){

        //The Draw function must account for the width and the height borders
        if(state==State.MENU){
            menu.drawMenu(c,p);
        }
        else {
            //Clear the background
            c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            //Draw the background
            Background.DrawBackground(c, p);

            //Draw the borders
            c.drawRect(0, 0, widthMargin, c.getHeight(), p);
            c.drawRect(c.getWidth() - widthMargin, 0, c.getWidth(), c.getHeight(), p);
            c.drawRect(0, 0, c.getWidth(), heightMargin, p);
            c.drawRect(0, c.getHeight() - heightMargin, c.getWidth(), c.getHeight(), p);

            animator.draw(c,p);
            snake.draw(c, p);
            apple.draw(c,p);

            UI.drawUI(c, p);
            p.setTextSize(50);

            if (drawCircle) {
                p.setColor(Color.RED);
                c.drawCircle(x1, y1, scaleFactorW, p);
                p.setColor(Color.BLACK);
                p.setStyle(Paint.Style.STROKE);
                c.drawCircle(x1, y1, scaleFactorW * 3, p);
                p.setStyle(Paint.Style.FILL);
                p.setColor(Color.BLACK);
                c.drawLine(0, y1 - x1, c.getWidth(), c.getWidth() + y1 - x1, p);
                c.drawLine(0, y1 + x1, c.getWidth(), y1 + x1 - c.getWidth(), p);
            }
        }
    }

    public static int RandomInt(int min, int max){
        Random rnd = new Random();
        int r = rnd.nextInt((max - min) + 1) + min;
        return r;
    }

    private void initGame(Canvas c){
        if(!isInit) {
            scaleFactorH = c.getHeight() / heightBlocks;
            scaleFactorW = c.getWidth() / widthBlocks;
            widthMargin = (c.getWidth() % widthBlocks) / 2;
            heightMargin = (c.getHeight() % heightBlocks) / 2;
            WIDTH = c.getWidth();
            HEIGHT = c.getHeight();
            snake = new Snake();
            snake.init(this);
            apple = new Apple(RandomInt(1, widthBlocks-1), RandomInt(1, heightBlocks-(UI.getBlockHeight()+1)));
            Background.initBackground(this, c.getWidth(), c.getHeight());
            UI.initUI(this);
            menu = new Menu();
            menu.initMenu(this);
            p.setFlags(Paint.ANTI_ALIAS_FLAG);

            isInit = true;
        }
    }

    private static void reset(){
        UI.score = 0;
        snake.snakeBlocks=new SnakeBlock[3];
        snake.snakeBlocks[0]=new SnakeBlock(7,12);
        snake.snakeBlocks[1]=new SnakeBlock(6,12);
        snake.snakeBlocks[2]=new SnakeBlock(5,12);

        snake.direction = 2;
    }

    public static void gameOver(){
        state=State.GAMEOVER;
    }

    private static double distanceBetweenPoints(float x1, float y1, float x2, float y2){
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public static void setDTC(){
        dirChangeTime = System.currentTimeMillis();
    }

    private static long getSleepTime(long ATime, long BTime) {
        long sTime = 0;
        sTime = 1000 / FPS;
        sTime = sTime - (ATime - BTime);
        if (sTime < 0)
            sTime = 1;
        return sTime;
    }

    public class MyView extends SurfaceView implements Runnable{

        Thread t = null;
        SurfaceHolder holder;
        boolean isOK = false;

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
                System.out.println(getSleepTime(ATime,BTime));
                try {
                    t.sleep(getSleepTime(ATime,BTime));
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

}
