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
public final static String EXTRA_MESSAGE ="com.example.gps02.Verknüpfung"; //Verknüpfung der Activitys
//public class MainActivity extends Activity {
private LocationManager manager;

	

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
		
				
				
				//manager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
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
			((manuelle_Stopuhr) fragment).setActivity(this);
			break;
		case(1):
			fragment=new GPS_basierte_Stopuhr();
			((GPS_basierte_Stopuhr) fragment).setActivity(this);
			break;
		
		}
		
		
		
		getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
				
		return true;
	}


	@Override
	protected void onStart() {
		super.onStart();
//		Log.d(TAG, "onStart()");
//		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0,
//				GPS_basierte_Stopuhr.listener);
	}
	

	@Override
	protected void onPause() {
		super.onPause();
//		Log.d(TAG, "onPause()");
//		manager.removeUpdates(listener);
	}
}

