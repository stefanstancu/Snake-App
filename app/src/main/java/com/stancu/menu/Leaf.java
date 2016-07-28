package com.stancu.menu;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import com.stancu.game.Main;
import com.stancu.utils.Channels;

/**
 * Created by aCamp on 7/22/2016.
 */
public class Leaf{

    public Bitmap image = Menu.leaf_image;

    private Matrix matrix = new Matrix();
    private float angle = Main.RandomInt(-10,90);
    private float deltaAngle = 2;
    private float Xdrift = 2;
    private int x,y;

    public Leaf(){
        //set a random size? scale it either way
        double desHeight = ((double)Main.scaleFactorW*2/(double)image.getWidth())*(double)image.getHeight();
        image = Bitmap.createScaledBitmap(image,Main.scaleFactorW*2,(int)desHeight,false);
        //set a random tint
        image = Channels.setChannels(image,Main.RandomInt(0,10)*10,0,0);

        x = Main.RandomInt(0,Main.WIDTH-image.getWidth());
        y = Main.RandomInt(-image.getHeight()*2,Main.HEIGHT-image.getHeight());
    }

    public void UpdateLeaf(){
        //This is where the movement and rotation are calculated
        //Change the matrix and pass it into the drawBitmap function

        setLeafAngle(angle);

        y+=2;
        if(y>Main.HEIGHT){
            y = -image.getHeight();
            x = Main.RandomInt(0,Main.WIDTH-image.getWidth());
        }
        calcLeafDrift();
        x += Xdrift;

    }

    public void DrawLeaf(Canvas c, Paint p){

        c.drawBitmap(image,matrix,p);
    }

    private void setLeafAngle(float angle){
        this.angle+=deltaAngle;
        if(angle<-10){
            deltaAngle=2;
        }
        else if(angle>90){
            deltaAngle=-2;
        }
        Matrix m = new Matrix();
        m.postRotate(angle,image.getWidth()/2,image.getHeight()/2);
        m.postTranslate(x,y);

        matrix.set(m);
    }
    private void calcLeafDrift(){
        if(angle<=40){
            Xdrift = 2;
        }
        else if(angle>=39){
            Xdrift = -2;
        }
    }
}