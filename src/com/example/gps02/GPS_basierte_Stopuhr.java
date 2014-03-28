package com.example.gps02;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



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
	private DataSource data;
	
	
	
	String Time;
	int[] curTime;
	boolean b_Button_Start=false;
	boolean GPS_Punkt=false;
	boolean Fahrzeug_am_StartPunkt=true;
	boolean StandortButton_Anzeige_Zielerreicht=false;
	
	public static boolean Standort_jetzt_festlegen=false;
	
	boolean safe_data=true;
	private double Breite_temp, Länge_temp;
	private List<Long> Treffer_IDs = new ArrayList<Long>();
	
	
	
	
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
		data=new DataSource(Main_Activity);
		
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
				Toast.makeText(Main_Activity, "onStatusChanged", Toast.LENGTH_SHORT).show();
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
				//hier Position in die Datenbank						
				double Lat=location.getLatitude();
				double Long=location.getLongitude();
				
				
				//Log.d(TAG, "onLocationChanged()");
				letztePosition="Breite: "+Lat+"Länge: "+Long;
				textview.setText(letztePosition);
				if (location != null) //Location nicht null und Button Standort_suchen gedrückt
				{	
					
					if(Standort_jetzt_festlegen==false)
					{
						Breite_temp=Lat;
						Länge_temp=Long;
					}
					
					if(GPS_Startpunkt.zwanzigsec==false&&Standort_jetzt_festlegen&&Koordinaten_Differenz_größer2m(Breite_temp, Lat, Länge_temp, Long))
					{
						GPS_Startpunkt.Set_punkt(Lat, Long);	
					}
					else if(GPS_Startpunkt.zwanzigsec&&GPS_Punkt==false)	//20s kein Standortwechsel und noch kein Punkt vorher aufgenommen
					{										
						set_Startpunkt_in_database(GPS_Startpunkt.Latitude,GPS_Startpunkt.Longitude);//Koordinaten vom vorherigen Aufruf
						safe_data=true;
						GPS_Punkt=true;
						Standort_jetzt_festlegen=false;
						
					}
					else if(GPS_Punkt)	//GPS-StartPunkt aufgenommen
					{
						//GPS Daten sind gefunden
						if(safe_data)//für nur eine Runde speichern
						{
							Insert_Coordinates_in_Database(Lat, Long);
						}
						else if(b_Button_Start)	//Start Button betätigt nachdem eine Runde aufgenommen wurde
						{
							Timemanagement(Lat,Long);		//Stoppen funktioniert noch nicht->debuggen							
						}	
						if(StandortButton_Anzeige_Zielerreicht==false)
						{
							Button_Standort_festlegen.setText("Ziel erreicht");
							StandortButton_Anzeige_Zielerreicht=true;						
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
				if(GPS_Startpunkt.zwanzigsec)
				{
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
				else
				{
					Toast.makeText(Main_Activity, "Es wurde noch kein Startpunkt aufgenommen", Toast.LENGTH_LONG).show();
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
				if(StandortButton_Anzeige_Zielerreicht==true)
				{
					Button_Standort_festlegen.setText("Standort festlegen");
					safe_data=false;
					StandortButton_Anzeige_Zielerreicht=false;
				}
				else{
					Standort_jetzt_festlegen=true;	
					GPS_Punkt=false;
					GPS_Startpunkt.Set_punkt(Breite_temp, Länge_temp);
				}
				
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
	
	
	private boolean Koordinaten_Differenz_größer2m(double Lat_alt, double Lat_neu, double Long_alt, double Long_neu)
	{
		double distBreite=Math.abs(Lat_alt-Lat_neu)*111320;
		double distLänge1=Math.abs(Long_alt-Long_neu)*111320;
		double Breiterad=Long_neu*Math.PI/180;		
		double distLänge2=distLänge1*Math.cos(Breiterad);
		double abstand=Math.sqrt(distBreite*distBreite+distLänge2*distLänge2);
		if(abstand>=2)
			return true;
		else
			return false;
					
	}

	private void Insert_Coordinates_in_Database(double Lat, double Long)
	{	
		//DecimalFormat f=new DecimalFormat("#0.0000");				
		double Latitude_r=Math.round( Lat * 10000d ) / 10000d;//Double.parseDouble(f.format(Lat));//Auf vier Stellen nach dem Komma runden
		double Longitude_r=Math.round( Long * 10000d ) / 10000d;//Double.parseDouble(f.format(Long));
		
		try{
				data.open();
				Entry e = data.getLastEntry();
				if(e!=null)
				{
					double Last_Entry_Lat=e.getLatitude();
					double Last_Entry_Long=e.getLongitude();
					if(Latitude_r!=Last_Entry_Lat||Longitude_r!=Last_Entry_Long)
					{
						data.createEntry(Latitude_r,Longitude_r);
						Toast.makeText(Main_Activity, "Eintrag erfolgreich eingefügt!", Toast.LENGTH_LONG).show();
					}
				}
				else
				{
					data.createEntry(Latitude_r,Longitude_r);
					Toast.makeText(Main_Activity, "Eintrag erfolgreich eingefügt!", Toast.LENGTH_LONG).show();
				}
				textview.setText("Latitude: "+Latitude_r+" Longitude: "+Longitude_r);
			} 
		catch (Exception ex) 
		{
				Toast.makeText(Main_Activity, ex.toString(), Toast.LENGTH_LONG).show();
				Log.d(TAG, ex.toString());
		} 
		finally
		{
			data.close();
		}				
	}
	
	private void set_Startpunkt_in_database(double Lat, double Long)
	{
		double Latitude_r=Math.round( Lat * 10000d ) / 10000d;
		double Longitude_r=Math.round( Long * 10000d ) / 10000d;
		
		try
		{
			data.open();
			data.deleteAllEntries();
			data.createEntry(Latitude_r,Longitude_r);
			Toast.makeText(Main_Activity, "Eintrag erfolgreich eingefügt!", Toast.LENGTH_LONG).show();
			textview.setText("Latitude: "+Latitude_r+" Longitude: "+Longitude_r);
		} 
		catch (Exception ex) 
		{
			Toast.makeText(Main_Activity, ex.toString(), Toast.LENGTH_LONG).show();
			Log.d(TAG, ex.toString());
		} 
		finally
		{
			data.close();
		}		
	}
	
	private void Timemanagement(double Lat, double Long)
	{		
		long ID_temp=-1;
		try
		{
			data.open();
			ID_temp = data.EntryExists(Lat, Long);	//hier ist der Fehler
		} 
		catch (Exception ex) 
		{
			Toast.makeText(Main_Activity, ex.toString(), Toast.LENGTH_LONG).show();
			Log.d(TAG, ex.toString());
		} 
		finally
		{
			data.close();
		}		
		
		if(ID_temp!=-1)//Eintrag gefunden
		{
			Treffer_IDs.add(ID_temp);		
			long last_Treffer_ID=Treffer_IDs.get(Treffer_IDs.size()-1); //letzte getroffene ID
			int last_Database_ID=data.Size();
			if(ID_temp<5 && last_Treffer_ID>(last_Database_ID-5)){
				Z.Add_Zeit(Sy.GetTime());
				Sy.startThread();		
			}
		}
		
		//das ganze noch für entryExists mit +-1
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
