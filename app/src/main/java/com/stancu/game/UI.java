package com.stancu.game;

import android.content.Context;
import android.graphics.*;
import com.stancu.snake.R;
import com.stancu.utils.BitFunc;
import com.stancu.utils.Button;

/**
 * Created by dapan on 2016-07-17.
 */
public class UI {
    public static int score = 0;

    private static int blockHeight = 0;
    private static Typeface myFont;
    private static Button menuButton;


    public static void initUI(Context context){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        myFont = Typeface.createFromAsset(context.getAssets(), "fonts/ChelaOne-Regular.ttf");
        Bitmap menuButtonImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.menu_button_image,options);
        menuButtonImage = BitFunc.scaleImageWidth(menuButtonImage,Main.widthBlocks/4*Main.scaleFactorW);
        menuButton = new Button(
                (Main.widthBlocks-5)*Main.scaleFactorW,
                (Main.heightBlocks-2)*Main.scaleFactorH,
                menuButtonImage.getWidth(),
                menuButtonImage.getHeight(),
                Button.MENU_BUTTON
        );
        menuButton.setImage(menuButtonImage);
    }

    public static void drawUI(Canvas c, Paint p){

        p.setTypeface(myFont);

        if(Main.state==Main.State.RUN){

            //Draw the Score
            setTextSizeforWidth(p,3*Main.scaleFactorW,"Score: "+Integer.toString(score));
            Rect bounds = getTextBounds(p,Integer.toString(score));
            c.drawText(Integer.toString(score),c.getWidth()/2-bounds.width()/2,2*Main.scaleFactorH,p);

            //Draw the menu button
            menuButton.drawButton(c,p);
        }
        else if(Main.state==Main.State.GAMEOVER){

            //Draw the You Lose text with the score underneath and prompt a restart
            String youLose="YOU LOSE";
            String scoreText = "Score: "+Integer.toString(score);
            Rect bounds = setTextSizeforWidth(p,5*Main.scaleFactorW,youLose);
            c.drawText("YOU LOSE",c.getWidth()/2-bounds.width()/2,c.getHeight()/2,p);
            Rect rect = getTextBounds(p,scoreText);
            c.drawText(scoreText,c.getWidth()/2-rect.width()/2,c.getHeight()/2+bounds.height(),p);
        }
        else{

        }
    }

    public static int getBlockHeight(){
        return blockHeight;
    }

    public static Button getMenuButton(){return menuButton;}

    private static Rect  setTextSizeforWidth(Paint p, float width, String text){
        p.setTextSize(48f);
        Rect bounds = new Rect();
        p.getTextBounds(text,0,text.length(),bounds);
        float textSize = 48*(Main.scaleFactorW*5)/bounds.width();
        p.setTextSize(textSize);
        p.getTextBounds(text,0,text.length(),bounds);
        return bounds;
    }

    private static Rect getTextBounds(Paint p, String text){
        Rect bounds = new Rect();
        p.getTextBounds(text,0,text.length(),bounds);
        return bounds;
    }
}
