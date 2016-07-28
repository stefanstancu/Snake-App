package com.stancu.menu;

import android.content.Context;
import android.graphics.*;
import com.stancu.game.Main;
import com.stancu.snake.R;
import com.stancu.utils.BitFunc;
import com.stancu.utils.Button;

/**
 * Created by dapan on 2016-07-17.
 */
public class Menu {

    public static Bitmap leaf_image;
    public Button[] buttons;

    private Bitmap logo_image;
    private Bitmap settings_image;
    private Bitmap play_image;
    private Bitmap tree_background;

    private Leaf[] leaves;

    public void initMenu(Context context){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        //Load the images
        logo_image=BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_image,options);
        settings_image = BitmapFactory.decodeResource(context.getResources(), R.drawable.settings_image,options);
        play_image = BitmapFactory.decodeResource(context.getResources(), R.drawable.play_image,options);
        leaf_image = BitmapFactory.decodeResource(context.getResources(), R.drawable.leaf_image,options);
        tree_background = BitmapFactory.decodeResource(context.getResources(), R.drawable.tree_image,options);

        //Scale the images
        logo_image = scaleImage(logo_image,2, Main.WIDTH);
        settings_image = scaleImage(settings_image,5,Main.WIDTH);
        play_image = scaleImage(play_image,5,Main.WIDTH);
        leaf_image = Bitmap.createScaledBitmap(leaf_image,60,49,false);
        tree_background = BitFunc.scaleImageWidth(tree_background,Main.WIDTH);

        //make the buttons
        buttons = new Button[3];
        buttons[0] = new Button(
                Main.WIDTH/2-logo_image.getWidth()/2,
                Main.scaleFactorH*5,
                logo_image.getWidth(),
                logo_image.getHeight(),
                Button.AESTHETIC
        );
        buttons[0].setImage(logo_image);

        buttons[1] = new Button(
                Main.WIDTH/2-settings_image.getWidth()/2,
                Main.scaleFactorH*6+logo_image.getHeight(),
                settings_image.getWidth(),
                settings_image.getHeight(),
                Button.SETTINGS_BUTTON
        );
        buttons[1].setImage(settings_image);

        buttons[2] = new Button(
                Main.WIDTH/2-play_image.getWidth()/2,
                Main.scaleFactorH*7+logo_image.getHeight()+settings_image.getHeight(),
                play_image.getWidth(),
                play_image.getHeight(),
                Button.PLAY_BUTTON
        );
        buttons[2].setImage(play_image);

        //Create the leaves
        leaves= new Leaf[15];
        for (int i = 0; i < leaves.length; i++) {
            leaves[i]= new Leaf();
        }
    }

    public void updateMenu(){
        for (int i = 0; i < leaves.length; i++) {
            leaves[i].UpdateLeaf();
        }
    }

    public void drawMenu(Canvas c, Paint p){
        c.drawARGB(255,153,204,0);

        //Draw the tree
        c.drawBitmap(tree_background,0,0,p);

        //Draw the leaves
        for (int i = 0; i < leaves.length; i++) {
            leaves[i].DrawLeaf(c,p);
        }

        //Draw the title, settings and play buttons
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].drawButton(c,p);
        }
    }

    private static Bitmap scaleImage(Bitmap image, int numOfSpaceBlocks, int width){

        double desiredX = width-numOfSpaceBlocks*2*Main.scaleFactorW;
        double desiredY = (desiredX/image.getWidth()*image.getHeight());
        return Bitmap.createScaledBitmap(image,(int)desiredX,(int)desiredY,false);
    }
}
