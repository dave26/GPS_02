/**
 * 
 */
package com.example.gps02;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.TextView;
import android.widget.Toast;



/**
 * @author Dave
 *
 */
public class Startpunkt /*extends Activity*/{
	
	private Activity Activity;
	private DialogFragment fragment;
	public boolean IsRunning;
	private long startTime;   
    public boolean zwanzigsec;
    public double Latitude, Longitude;
    private final Timer clockTimer;
	
	private double Latitude_temp,Longitude_temp;	
	
	public Startpunkt(Activity a)
	{		
    	clockTimer = new Timer();
        clockTimer.schedule(new Task(), 0, 100);
        IsRunning=false;
        this.Activity=a;
        //fragment=a;
	}
	
	public void Set_punkt(double Latitude, double Longitude)
	{
		this.Latitude_temp=Latitude;
		this.Longitude_temp=Longitude;
		//abfrage ob 20s diese Funktion nicht mehr aufgerufen wurde und wenn nicht Punkte festlegen
			startThread();
	}
	    
    private void startThread()
    {
        this.startTime = System.nanoTime();
        this.IsRunning = true; 
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
        	if(IsRunning)
        		timerSignal();
        }
    };

    private List<String> clockListener = new ArrayList<String>();   

    private void timerSignal() {               
    		long milliTime = System.nanoTime() - this.startTime;	
    	    int ms=(int)(milliTime/1000000000)%60;		//sec	         
	        if(ms>=20)
	        {
	        	zwanzigsec=true;  
	        	GPS_basierte_Stopuhr.Standort_jetzt_festlegen=false;
	        	Latitude=Latitude_temp;
	        	Longitude=Longitude_temp;
	        	clockTimer.cancel();
	        	//ANzeige Standort aufgenommen!!
	        	//Toast.makeText(this, "Standort aufgenommen", Toast.LENGTH_SHORT).show();
	        	
	        	AlertDialog.Builder builder = new AlertDialog.Builder(Activity);				 
				builder.setTitle("Startpunkt");
				builder.setMessage("Der Startpunkt wurde gefunden");				 
				builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {				 
				   @Override
				   public void onClick(DialogInterface dialog, int which) {	     
				   }				 
				});
				AlertDialog alert = builder.create();
				alert.show();
	        }    	
    }

    public void killTimer() {
        clockTimer.cancel();
    }
//    public void StopTimer(){
//    	try {
//			clockTimer.wait();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }

    public void addListener(String listener) {
        clockListener.add(listener);        
    }
}
