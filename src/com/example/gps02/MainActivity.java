package com.example.gps02;

import android.app.ActionBar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

public class MainActivity extends FragmentActivity implements
ActionBar.OnNavigationListener {

/**
* The serialization (saved instance state) Bundle key representing the
* current dropdown position.
*/
	
	
private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
public final static String EXTRA_MESSAGE ="com.example.gps02.Verkn�pfung"; //Verkn�pfung der Activitys
//public class MainActivity extends Activity {
//private LocationManager manager;

	

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
		case R.id.menue_beenden:
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			 
			builder.setTitle("Beenden");
			builder.setMessage("Wollen Sie GPS2 wirklich beenden?");
			 
			 
			builder.setPositiveButton("JA", new DialogInterface.OnClickListener() {
			 
			   @Override
			   public void onClick(DialogInterface dialog, int which) {
			         
			        // Code der ausgef�hrt wird wenn JA geklickt wurde
				   System.exit(0);
			 
			        dialog.dismiss();
			   }
			 
			});
			 
			 
			builder.setNegativeButton("NEIN", new DialogInterface.OnClickListener() {
			 
			   @Override
			   public void onClick(DialogInterface dialog, int which) {
			 
			        // Code der ausgef�hrt wird wenn NEIN geklickt wurde
			 
			        dialog.dismiss();
			   }
			 
			});
			 
			 
			AlertDialog alert = builder.create();
			alert.show();
			return true;
			
		case R.id.menue_portrait:
			setOrientation_portrait();
			return true;
			
		case R.id.menue_landscape:
			setOrientation_landscape();
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
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		
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
	}
	

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	   
	}
	
	protected void setOrientation_portrait() {
	    int current = getRequestedOrientation();
	    // only switch the orientation if not in portrait
	    if ( current != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ) {
	        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
	    }
	}
	protected void setOrientation_landscape() {
	    int current = getRequestedOrientation();
	    // only switch the orientation if not in portrait
	    if ( current != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE ) {
	        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );
	    }
	}
}

