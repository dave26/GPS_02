/**
 * 
 */
package com.example.gps02;

/**
 * @author Dave
 *
 */
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.gps02.R.string;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class SystemTimerAndroid {
	
	
	private boolean Darstellung;
	private long startTime;
	private long stopTime;
	private long waitingTime=0;
    private boolean started;
    private TextView AnzeigeStopuhr;
    //public boolean zwanzigsec;	
    private final Timer clockTimer;
    private boolean wait;
    
    
    public SystemTimerAndroid(TextView Anzeige){
    	Darstellung=true;
    	AnzeigeStopuhr=Anzeige;
    	clockTimer = new Timer();
        clockTimer.schedule(new Task(), 0, 100);
        wait=false;
    }
    public SystemTimerAndroid(){
    	
    	Darstellung=false;
    	clockTimer = new Timer();
        clockTimer.schedule(new Task(), 0, 100);
        wait=false;
    }
    
    public void startThread()
    {
    	if(wait)
    	{         
        //zwanzigsec=false;        
        	wait=false;
        	waitingTime=waitingTime+(System.nanoTime()-stopTime);
        }
    	else
    	{
    		this.startTime = System.nanoTime();
    		this.started = true;
    		waitingTime=0;
    	}
        
    }

    private class Task extends TimerTask {
        public void run() {
            timerHandler.sendEmptyMessage(0);
            
        }
    }

    private final Handler timerHandler = new Handler() {
        public void handleMessage (Message  msg) {
            // runs in context of the main thread
        	if(wait==false)
        		timerSignal();
        }
    };

    private List<String> clockListener = new ArrayList<String>();

    private void timerSignal() {
         
        DecimalFormat df = new DecimalFormat("00");
        
      
        long milliTime = System.nanoTime() - this.startTime-waitingTime;
       
        
    	if(Darstellung)
    	{
	        int[] out = new int[]{00, 00, 00};
	       
	        out[0]=(int)(milliTime/(1000000000))/60;		//min
	        //out[0]("%03d", 1);
	        //System.out.printf("%03d", 1);
	        out[1] = (int)(milliTime/1000000000)%60;		//sec
	        out[2] = (int)((milliTime/10000000)%100);		//ms
	        String Time= df.format(out[0]) + " : " + df.format(out[1]) + " : " + df.format(out[2]);
	        AnzeigeStopuhr.setText(Time);
//	        if(out[1]<=20)
//	        	zwanzigsec=true;
    	}
    }

    public void killTimer() {
        clockTimer.cancel();
    }
    public void WaitTimer(){
    	if(wait==false){
    	wait=true;
    	stopTime=System.nanoTime();
    	}
    }

    public void addListener(String listener) {
        clockListener.add(listener);        
    }
    public long GetTime_sec()
    {
    	DecimalFormat df = new DecimalFormat("00");
        
    	long milliTime = System.nanoTime() - this.startTime-waitingTime;
        int[] out = new int[]{00, 00, 00};
       
        out[0]=(int)(milliTime/(1000000000))/60;		//min
        //out[0]("%03d", 1);
        //System.out.printf("%03d", 1);
        out[1] = (int)(milliTime/1000000000)%60;		//sec
        out[2] = (int)((milliTime/10000000)%100);		//ms
        //String Time= df.format(out[0]) + " : " + df.format(out[1]) + " : " + df.format(out[2]);
        return out[1];
    }
    public String GetTime()
    {
    	DecimalFormat df = new DecimalFormat("00");
        
    	long milliTime = System.nanoTime() - this.startTime-waitingTime;
        int[] out = new int[]{00, 00, 00};
       
        out[0]=(int)(milliTime/(1000000000))/60;		//min
        //out[0]("%03d", 1);
        //System.out.printf("%03d", 1);
        out[1] = (int)(milliTime/1000000000)%60;		//sec
        out[2] = (int)((milliTime/10000000)%100);		//ms
        String Time= df.format(out[0]) + " : " + df.format(out[1]) + " : " + df.format(out[2]);
        return Time;
    }
    public boolean IsRunning()
    {
    	return started;
  
    }
}
