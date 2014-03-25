package com.example.gps02;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class manuelle_Stopuhr extends Fragment {
	
	private Activity Main_Activity;	
	public void setActivity(Activity a)
	{
		Main_Activity=a;
	}
	
	private TextView AnzeigeStopuhr;
	private Button Button_Stopuhr_start;
	private Button Button_Stopuhr_stop;	
	private TextView Stopuhrstatus;
	private Button alte_Zeiten;	
	private SystemTimerAndroid Sy;	
	private Zeitenverwaltung Z;
	private boolean Klick=false;
	private boolean button_stop_geklickt=false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.fragment_manuelle_stopuhr,
				container, false);		
		
		Button_Stopuhr_start=(Button)rootView.findViewById(R.id.Button_Stopuhr_start);
		AnzeigeStopuhr=(TextView)rootView.findViewById(R.id.AnzeigeStopuhr);
		Stopuhrstatus=(TextView)rootView.findViewById(R.id.Stopuhrstatus);		
		Button_Stopuhr_stop=(Button)rootView.findViewById(R.id.Button_Stopuhr_stop);		
		alte_Zeiten=(Button)rootView.findViewById(R.id.Test);
		
		
		Z=new Zeitenverwaltung(alte_Zeiten);		
				
		Button_Stopuhr_start.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
			
				if(Klick==false){
					Sy= new SystemTimerAndroid(AnzeigeStopuhr);
					Sy.startThread();					
					Klick=true;					
					Stopuhrstatus.setText("Stopuhr aktiv");						
				}
				else
				{	
					if(button_stop_geklickt)
					{						
						Sy.startThread();
						button_stop_geklickt=false;
					}
					else
					{
						Z.Add_Zeit(Sy.GetTime());
						Sy.startThread();						
					}
				}
				
			}
		});
		
		Button_Stopuhr_stop.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//Sy.killTimer();
				Sy.WaitTimer();
				button_stop_geklickt=true;
			}
		});
						
		alte_Zeiten.setOnClickListener(new OnClickListener(){
		@Override
		public void onClick(View view){
			Intent intent=new Intent(Main_Activity,SecondActivity.class);			
		    intent.putExtra(MainActivity.EXTRA_MESSAGE,"Yeah hier ist die neue Activity");
			startActivity(intent);
		}
		});

		return rootView;
	}
}
