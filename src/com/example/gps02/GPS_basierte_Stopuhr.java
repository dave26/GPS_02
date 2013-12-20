package com.example.gps02;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Region;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GPS_basierte_Stopuhr extends DialogFragment {

	private Activity Main_Activity;//=getActivity();
	private LocationManager fragment_manager;

	public void setActivity(Activity a)
	{
		Main_Activity=a;
	}
	
	private static final String TAG = MainActivity.class.getSimpleName();	
	
	private TextView textview;
	private TextView AnzeigeStopuhr;
	private Button GPSButton;
	private Button Button_Stopuhr_start;
	private Button Button_Stopuhr_stop;
	private String letztePosition;
	private TextView Stopuhrstatus;
	private Button alte_Zeiten;
	private Button Button_Standort_festlegen;
	
	
	
	String Time;
	int[] curTime;
	boolean b_Button_Start=false;
	boolean GPS_Punkt=false;
	boolean Fahrzeug_am_StartPunkt=true;
	public static boolean Standort_jetzt_festlegen=false;
	boolean erster_Aufruf=false;
	private double Breite, Länge;
	private double Breite_temp, Länge_temp;
	
	
	
	SystemTimerAndroid Sy;
	
	
	Startpunkt GPS_Startpunkt;
	Zeitenverwaltung Z;
	
	
	private LocationListener listener;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.fragment_gps_basierte_stopuhr,
				container, false);

		textview = (TextView)rootView.findViewById(R.id.textview);
		textview.setText("Um die GPS-Position zu ermitteln drücken Sie den GPS-Button");
		GPSButton=(Button)rootView.findViewById(R.id.Button_Standort);
		Button_Stopuhr_start=(Button)rootView.findViewById(R.id.Button_Stopuhr_start);
		AnzeigeStopuhr=(TextView)rootView.findViewById(R.id.AnzeigeStopuhr);
		Stopuhrstatus=(TextView)rootView.findViewById(R.id.Stopuhrstatus);		
		Button_Stopuhr_stop=(Button)rootView.findViewById(R.id.Button_Stopuhr_stop);
		Button_Standort_festlegen=(Button)rootView.findViewById(R.id.Button_Standort_suchen);
		
		alte_Zeiten=(Button)rootView.findViewById(R.id.Test);
		
		
		Z=new Zeitenverwaltung(alte_Zeiten);
		
		
		//GPS_Startpunkt= new Startpunkt(fragment);
		GPS_Startpunkt= new Startpunkt(Main_Activity);


		fragment_manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		// Provider mit grober Auflösung
		// und niedrigen Energieverbrauch
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		// Namen ausgeben
		String name = fragment_manager.getBestProvider(criteria, true);
		Log.d(TAG, name);
		
		// LocationListener-Objekt erzeugen
		
		this.listener = new LocationListener() {
			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				Log.d(TAG, "onStatusChanged()");
			}

			@Override
			public void onProviderEnabled(String provider) {
				Log.d(TAG, "onProviderEnabled()");
			}

			@Override
			public void onProviderDisabled(String provider) {
				Log.d(TAG, "onProviderDisabled()");
			}

			/**
			 * @param location
			 */
			@Override
			public void onLocationChanged(Location location) {
				Log.d(TAG, "onLocationChanged()");
				letztePosition="Breite: "+location.getLatitude()+"Länge: "+location.getLongitude();
				if (location != null) //Location nicht null und Button Standort_suchen gedrückt
				{		
					Breite_temp=location.getLatitude();
					Länge_temp=location.getLongitude();
					
					if(GPS_Startpunkt.zwanzigsec==false&&Standort_jetzt_festlegen)
					{
						GPS_Startpunkt.Set_punkt(location.getLatitude(), location.getLongitude());
					}
					else if(GPS_Punkt==false)	//20s kein Standortwechsel und noch kein Punkt vorher aufgenommen
					{
						Breite=GPS_Startpunkt.Latitude;	//Koordinaten vom vorherigen Aufruf
						Länge=GPS_Startpunkt.Longitude;
						GPS_Punkt=true;
						//Standort_jetzt_festlegen=false;
						if(Abstand_größer_5m(location.getLatitude(),location.getLongitude())) //Fahrzeug entfernt vom Startpunkt
						{
							Fahrzeug_am_StartPunkt=false;
						}
					}
					else if(GPS_Punkt)	//GPS-Punkt gefunden
					{
						//GPS Daten sind gefunden
						if(Abstand_größer_5m(location.getLatitude(),location.getLongitude())) //Fahrzeug entfernt vom Startpunkt
						{
							Fahrzeug_am_StartPunkt=false;
														
						}
						else//FAhrzeug wieder am STartpunkt
						{
							if(Fahrzeug_am_StartPunkt==false)
							{								
								//hier muss gestoppt werden
								Fahrzeug_am_StartPunkt=true;
								
								if(b_Button_Start)	//Start Button betätigt
								{
									//Sy= new SystemTimerAndroid(AnzeigeStopuhr);
									Z.Add_Zeit(Sy.GetTime());
									Sy.startThread();					
									erster_Aufruf=true;					
									Stopuhrstatus.setText("Stopuhr aktiv");
									
									
								}

							}
						}						
						
					}
					
				}
			}
		};
		
		GPSButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				
				if(letztePosition!=null)
				{
					textview.setText(letztePosition);
				}
				else
				{
					textview.setText("Leider wurden noch keine Positionsdaten empfangen");
				}
				
				
			}
		});
				
		Button_Stopuhr_start.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){			
				if(b_Button_Start==false){
					Sy= new SystemTimerAndroid(AnzeigeStopuhr);
					Sy.startThread();					
					b_Button_Start=true;					
					Stopuhrstatus.setText("Stopuhr aktiv");						
				}
				else
				{	
					Sy.startThread();
				}
				
			}
		});
		
		Button_Stopuhr_stop.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Sy.killTimer();
			}
		});
		
		Button_Standort_festlegen.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Standort_jetzt_festlegen=true;
				GPS_Punkt=false;
				GPS_Startpunkt.Set_punkt(Breite_temp, Länge_temp);
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
	
	private boolean Abstand_größer_5m(double Lat_akt, double Lon_akt)
	{
		double distBreite=Math.abs(Lat_akt-Breite)*111320;
		double distLänge1=Math.abs(Lon_akt-Länge)*111320;
		double Breiterad=Lon_akt*Math.PI/180;		
		double distLänge2=distLänge1*Math.cos(Breiterad);
		double abstand=Math.sqrt(distBreite*distBreite+distLänge2*distLänge2);
		if(abstand>=5)
			return true;
		else
			return false;
	}

	@Override
	public void onStart() {
		super.onStart();
		fragment_manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0,
				listener);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		fragment_manager.removeUpdates(listener);
	}
}
