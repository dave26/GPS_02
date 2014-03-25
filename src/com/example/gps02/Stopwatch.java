package com.example.gps02;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Stopwatch  implements Runnable
{
    private long startTime;
    private boolean started;
    private ImageView Anzeige;

    public Stopwatch(ImageView Anzeige){
    	this.Anzeige=Anzeige;
    }
    
    
    
    public void startThread()
    {
        this.startTime = System.nanoTime();
        this.started = true; 
        Anzeige.setVisibility(0);
    }

    public void run()
    {
        while (started)
        {
            // empty code since currentTimeMillis increases by itself
        	 long milliTime = System.nanoTime() - this.startTime;
             int[] out = new int[]{0, 0, 0};
            
             out[0]=(int)(milliTime/(1000000000))/60;		//min
             out[1] = (int)(milliTime/1000000000)%60;		//sec
             out[2] = (int)((milliTime/1000000)%1000);		//ms
             String Time= out[0] + " : " + out[1] + " : " + out[2];
             if(out[1]>2)
             {
             Anzeige.setVisibility(4); 
             this.stopThread();
             }           
        }
    }


    public int[] getTime()
    {
        long milliTime = System.nanoTime() - this.startTime;
        int[] out = new int[]{0, 0, 0};
       
        out[0]=(int)(milliTime/(1000000000))/60;		//min
        out[1] = (int)(milliTime/1000000000)%60;		//sec
        out[2] = (int)((milliTime/1000000)%1000);		//ms
        return out;
    }
    public void stopThread()
    {
        this.started = false;
    }
}

