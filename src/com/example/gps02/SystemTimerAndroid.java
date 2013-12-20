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
    private boolean started;
    private TextView AnzeigeStopuhr;
    public boolean zwanzigsec;
	
	
	
    private final Timer clockTimer;
    
    public SystemTimerAndroid(TextView Anzeige){
    	Darstellung=true;
    	AnzeigeStopuhr=Anzeige;
    	clockTimer = new Timer();
        clockTimer.schedule(new Task(), 0, 100);
       
    }
    public SystemTimerAndroid(){
    	
    	Darstellung=false;
    	clockTimer = new Timer();
        clockTimer.schedule(new Task(), 0, 100);
      
    }
    
    public void startThread()
    {
        this.startTime = System.nanoTime();
        this.started = true; 
        zwanzigsec=false;
        
    }

    private class Task extends TimerTask {
        public void run() {
            timerHandler.sendEmptyMessage(0);
            
        }
    }

    private final Handler timerHandler = new Handler() {
        public void handleMessage (Message  msg) {
            // runs in context of the main thread
            timerSignal();
        }
    };

    private List<String> clockListener = new ArrayList<String>();

    //public SystemTimerAndroid() {
    //    clockTimer = new Timer();
    //    clockTimer.schedule(new Task(), 1000, 1000);
    //}

    private void timerSignal() {
        //for(SystemTimerListener listener : clockListener)
          //  listener.onSystemTimeSignal(); 
        DecimalFormat df = new DecimalFormat("00");
        
    	long milliTime = System.nanoTime() - this.startTime;
    	
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
	        if(out[1]<=20)
	        	zwanzigsec=true;
    	}
//        try {
//			Thread.currentThread().sleep(900);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    }

    public void killTimer() {
        clockTimer.cancel();
    }
    public void StopTimer(){
    	try {
			clockTimer.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void addListener(String listener) {
        clockListener.add(listener);        
    }
    public long GetTime_sec()
    {
    	DecimalFormat df = new DecimalFormat("00");
        
    	long milliTime = System.nanoTime() - this.startTime;
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
        
    	long milliTime = System.nanoTime() - this.startTime;
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

//public class Stopwatch  implements Runnable
//{
//    private long startTime;
//    private boolean started;
//    private TextView AnzeigeStopuhr;
//
//    public Stopwatch(TextView Anzeige){
//    	AnzeigeStopuhr=Anzeige;
//    }
//    
//    
//    
//    public void startThread()
//    {
//        this.startTime = System.nanoTime();
//        this.started = true;        
//    }
//
//    public void run()
//    {
//        while (started)
//        {
//            // empty code since currentTimeMillis increases by itself
//        	 long milliTime = System.nanoTime() - this.startTime;
//             int[] out = new int[]{0, 0, 0};
//            
//             out[0]=(int)(milliTime/(1000000000))/60;		//min
//             out[1] = (int)(milliTime/1000000000)%60;		//sec
//             out[2] = (int)((milliTime/1000000)%1000);		//ms
//             String Time= out[0] + " : " + out[1] + " : " + out[2];
//             AnzeigeStopuhr.setText(Time);
//             try {
//				Thread.currentThread().sleep(900);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//        }
//    }
//
//
//    public int[] getTime()
//    {
//        long milliTime = System.nanoTime() - this.startTime;
//        int[] out = new int[]{0, 0, 0};
//       
//        out[0]=(int)(milliTime/(1000000000))/60;		//min
//        out[1] = (int)(milliTime/1000000000)%60;		//sec
//        out[2] = (int)((milliTime/1000000)%1000);		//ms
//        return out;
//    }
//    public void stopThread()
//    {
//        this.started = false;
//    }
//}
//
