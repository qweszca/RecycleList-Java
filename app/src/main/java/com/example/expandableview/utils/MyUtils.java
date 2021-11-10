package com.example.expandableview.utils;



import android.graphics.Color;

import java.util.Random;

public class MyUtils {
    public static int randomColor(){
        long t = System.currentTimeMillis();
        Random rand = new Random(t);
        int r=rand.nextInt(200)+33;
        rand=new Random(t+10);
        int g=rand.nextInt(170)+33;
        rand=new Random(t+20);
        int b=rand.nextInt(220)+33;
        return Color.rgb(r,g,b);
    }
}
