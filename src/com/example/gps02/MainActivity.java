package com.example.gps02;

import java.util.List;

import android.app.ActionBar;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
//import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements
ActionBar.OnNavigationListener {

/**
* The serialization (saved instance state) Bundle key representing the
* current dropdown position.
*/
private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

//public class MainActivity extends Activity {

	private static final String TAG = MainActivity.class.getSimpleName();	
	
	private Activity Main_Activity=this;	
	public final static String EXTRA_MESSAGE ="com.example.gps02.Verknüpfung"; //Verknüpfung der Activitys
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
	boolean Klick=false;
	boolean GPS_Punkt=false;
	boolean Fahrzeug_am_StartPunkt=true;
	boolean Standort_jetzt_festlegen=false;
	boolean erster_Aufruf=false;
	private double Breite, Länge;
	private double Breite_temp, Länge_temp;
	
	
	
	SystemTimerAndroid Sy;
	
	
	Startpunkt GPS_Startpunkt;
	Zeitenverwaltung Z;
	
	private LocationManager manager;
	private LocationListener listener;

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.optionsmenu, menu);
		return super.onCreateOptionsMenu(menu);
		
	}
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.item1:
			//textview.setText(item.getTitle());
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			 
			builder.setTitle("Beenden");
			builder.setMessage("Wollen Sie GPS2 wirklich beenden?");
			 
			 
			builder.setPositiveButton("JA", new DialogInterface.OnClickListener() {
			 
			   @Override
			   public void onClick(DialogInterface dialog, int which) {
			         
			        // Code der ausgeführt wird wenn JA geklickt wurde
				   System.exit(0);
			 
			        dialog.dismiss();
			   }
			 
			});
			 
			 
			builder.setNegativeButton("NEIN", new DialogInterface.OnClickListener() {
			 
			   @Override
			   public void onClick(DialogInterface dialog, int which) {
			 
			        // Code der ausgeführt wird wenn NEIN geklickt wurde
			 
			        dialog.dismiss();
			   }
			 
			});
			 
			 
			AlertDialog alert = builder.create();
			alert.show();
			return true;
		case R.id.item2:
			textview.setText(item.getTitle());
			return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void invalidateOptionsMenu(){
	 
	}
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		// Set up the action bar to show a dropdown list.
				final ActionBar actionBar = getActionBar();
				actionBar.setDisplayShowTitleEnabled(false);
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

				// Set up the dropdown list navigation in the action bar.
				actionBar.setListNavigationCallbacks(
				// Specify a SpinnerAdapter to populate the dropdown list.
						new ArrayAdapter<String>(actionBar.getThemedContext(),
								android.R.layout.simple_list_item_1,
								android.R.id.text1, new String[] {
										getString(R.string.title_section1),
										getString(R.string.title_section2),
										/*getString(R.string.title_section3),*/ }), this);
		
		
		
		textview = (TextView) findViewById(R.id.textview);
		textview.setText("Um die GPS-Position zu ermitteln drücken Sie den GPS-Button");
		GPSButton=(Button)findViewById(R.id.Button_Standort);
		Button_Stopuhr_start=(Button)findViewById(R.id.Button_Stopuhr_start);
		AnzeigeStopuhr=(TextView)findViewById(R.id.AnzeigeStopuhr);
		Stopuhrstatus=(TextView)findViewById(R.id.Stopuhrstatus);
		
		Button_Stopuhr_stop=(Button)findViewById(R.id.Button_Stopuhr_stop);
		Button_Standort_festlegen=(Button)findViewById(R.id.Button_Standort_suchen);
		
		alte_Zeiten=(Button)findViewById(R.id.Test);
		
		
		Z=new Zeitenverwaltung(alte_Zeiten);
		
		
		GPS_Startpunkt= new Startpunkt(Main_Activity);
		
		
		// LocationManager-Instanz ermitteln
		manager = (LocationManager) getSystemService(LOCATION_SERVICE);
		// Liste mit Namen aller Provider erfragen
//		List<String> providers = manager.getAllProviders();
//		// Infos zu Location Providern ausgeben
//		for (String name : providers) {
//			LocationProvider lp = manager.getProvider(name);
//			Log.d(TAG,
//					lp.getName() + " --- isProviderEnabled(): "
//							+ manager.isProviderEnabled(name));
//			Log.d(TAG, "requiresCell(): " + lp.requiresCell());
//			Log.d(TAG, "requiresNetwork(): " + lp.requiresNetwork());
//			Log.d(TAG, "requiresSatellite(): " + lp.requiresSatellite());
//		}
		// Provider mit grober Auflösung
		// und niedrigen Energieverbrauch
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		// Namen ausgeben
		String name = manager.getBestProvider(criteria, true);
		Log.d(TAG, name);
		
		// LocationListener-Objekt erzeugen
		
		listener = new LocationListener() {
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
					else if(GPS_Punkt==false&&Standort_jetzt_festlegen)	//20s kein Standortwechsel und noch kein Punkt vorher aufgenommen
					{
						Breite=GPS_Startpunkt.Latitude;	//Koordinaten vom vorherigen Aufruf
						Länge=GPS_Startpunkt.Longitude;
						GPS_Punkt=true;
						Standort_jetzt_festlegen=false;
					}
					else if(GPS_Punkt)	//GPS-Punkt gefunden
					{
						//GPS Daten sind gefunden
						double distBreite=Math.abs(location.getLatitude()-Breite)*111320;
						double distLänge1=Math.abs(location.getLongitude()-Länge)*111320;
						double Breiterad=location.getLatitude()*Math.PI/180;
						//double Breiteneu1=Math.cos(Breiterad);
						double distLänge2=distLänge1*Math.cos(Breiterad);
						double abstand=Math.sqrt(distBreite*distBreite+distLänge2*distLänge2);
						if(abstand>=5)//Fahrzeug entfernt vom Startpunkt
						{
							Fahrzeug_am_StartPunkt=false;
														
						}
						else//FAhrzeug wieder am STartpunkt
						{
							if(Fahrzeug_am_StartPunkt==false)
							{								
								//hier muss gestoppt werden
								Fahrzeug_am_StartPunkt=true;
								
								if(erster_Aufruf==false)	//erster Aufruf
								{
									Sy= new SystemTimerAndroid(AnzeigeStopuhr);
									Sy.startThread();					
									erster_Aufruf=true;					
									Stopuhrstatus.setText("Stopuhr aktiv");
									
									
								}
								else
								{
									Z.Add_Zeit(Sy.GetTime());
									Sy.startThread();	
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
			
				if(Klick==false){
					Sy= new SystemTimerAndroid(AnzeigeStopuhr);
					Sy.startThread();					
					Klick=true;					
					Stopuhrstatus.setText("Stopuhr aktiv");
					
						
				}
				else
				{	
					//alte_Zeiten.setText(Sy.GetTime());
					Z.Add_Zeit(Sy.GetTime());
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



//		Location locNuernberg = new Location(LocationManager.GPS_PROVIDER);
//		double latitude = Location.convert("49:27");
//		locNuernberg.setLatitude(latitude);
//		double longitude = Location.convert("11:5");
//		locNuernberg.setLongitude(longitude);
//		Log.d(TAG, "latitude: " + locNuernberg.getLatitude());
//		Log.d(TAG, "longitude: " + locNuernberg.getLongitude());
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	
	
	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
		Fragment fragment = null;//new DummySectionFragment();

		switch(position)
		{
		case(0):
			fragment=new manuelle_Stopuhr();
			break;
		case(1):
			fragment=new GPS_basierte_Stopuhr();
			break;
		
		}
		
		
		
		getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
				
		return true;
	}

	
	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onStart()");
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0,
				listener);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause()");
		manager.removeUpdates(listener);
	}
}

