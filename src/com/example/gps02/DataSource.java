package com.example.gps02;


import java.util.ArrayList;
import java.util.List;


import com.example.gps02.MySQLite;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;



public class DataSource {
	private SQLiteDatabase database;
	private MySQLite sqlite;
	//private String[] carcolumns = { "ID", "carname"};
	private String[] datacolumns = { "ID", "Latitude", "Longitude"};//, "full"};
	//private String[] receiptscolumns = { "ID", "CarID", "DataID", "Path"};

	public DataSource (Context context) {
		sqlite = MySQLite.getInstance(context);

		try{
				this.open();
				this.deleteAllEntries();
			} 
		catch (Exception ex) 
		{
			Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();				
		} 
		finally
		{
			this.close();
		}				
	}

	public void open() throws SQLException {
		database = sqlite.getWritableDatabase();
	}

	public void close() {
		sqlite.close();
	}

	public Entry createEntry (double Latitude, double Longitude)//, boolean full) 
	{		
		ContentValues values = new ContentValues();
		values.put ("Latitude", Latitude);
		values.put ("Longitude", Longitude);
		
		//values.put ("full", full);
		

		long insertID = database.insert ("DATA", null, values);

		Cursor cursor = database.query("DATA", datacolumns, "ID = " + insertID, null, null, null, null);
		cursor.moveToFirst();

		return cursorToEntry(cursor);
	}

	private Entry cursorToEntry (Cursor cursor){
		Entry e = new Entry();

		e.setID(cursor.getLong(0));
		
		e.setLatitude(cursor.getDouble(1));
		e.setLongitude(cursor.getDouble(2));
		
		//e.setfull(cursor.getInt(3)>0);

		return e;
	}

	private GPS_Point cursorToGPS_Point (Cursor cursor){
		GPS_Point c = new GPS_Point();

		c.setID(cursor.getLong(0));
		c.setLatitude(cursor.getDouble(1));
		c.setLongitude(cursor.getDouble(2));
		//c.setfull(cursor.getInt(3)>0);

		return c;
	}

	protected List<Entry> getAllEntries() {
		List<Entry> EntryList = new ArrayList<Entry>();

		Cursor cursor = database.query("DATA", datacolumns, null, null, null, null, null);
		cursor.moveToFirst();

		if(cursor.getCount() == 0) return EntryList;


		while (cursor.isAfterLast() == false) {
			Entry entry = cursorToEntry(cursor);
			EntryList.add(entry);
			cursor.moveToNext();
		} 	

		cursor.close();

		return EntryList;
	}



	public ArrayList<Entry> getEntrybyLatitude(String Latitude){
		ArrayList<Entry> list = new ArrayList<Entry>();

		String[] Lat = { Latitude };
		Cursor cursor = database.rawQuery("SELECT * from DATA where Latitude = ?;", Lat);
		cursor.moveToFirst();

		while(cursor.isAfterLast() == false){
			list.add(cursorToEntry(cursor));
			cursor.moveToNext();
		}

		return list;
	}


	public void deleteEntry (long ID){
		database.delete("DATA", "ID=" + ID, null);
	}
	
	public void deleteAllEntries(){		
		List<Entry> all= this.getAllEntries();
		for(int i=1;i<=all.size();i++)
		{
			this.deleteEntry(i);
		}
	}

	public void updateEntry (Entry e){
		ContentValues values = new ContentValues();
		values.put ("ID", e.getID());
		values.put ("Latitude", e.getLatitude());
		values.put ("Longitude", e.getLongitude());	
		//values.put ("full", e.getfull());

		database.update("DATA", values, "ID=+" + e.getID(), null);
	}

	public Entry getEntry (long ID){
		Cursor cursor = database.query("DATA", datacolumns, "ID=" + ID, null, null, null, null);
		cursor.moveToFirst();

		while(cursor.isAfterLast() == false){
			if(cursor.getLong(0) == ID){
				return cursorToEntry(cursor);
			}
			cursor.moveToNext();
		}
		return null;
	} 
	public int Size(){
		Cursor cursor = database.query("DATA", datacolumns, null, null, null, null, null);
		cursor.moveToLast();		
		return cursor.getCount();		
	}
	public Entry getLastEntry(){
		Cursor cursor = database.query("DATA", datacolumns, null, null, null, null, null);
		cursor.moveToLast();
		
		if(cursor.getCount() == 0) 
			return null;
		else
		{
			Entry entry=cursorToEntry(cursor);
			cursor.close();
			return entry;
		}
	}

	public boolean LatitudeExists(String Latitude){

		String[] Lat = { Latitude };
        Cursor cursor = database.rawQuery("SELECT * from DATA where Latitude = ?;", Lat);
        cursor.moveToFirst();

        return cursor.getCount() > 1;
	}
	
	public long EntryExists(double Latitude, double Longitude){
		String[] s= {String.valueOf(Latitude),String.valueOf(Longitude)};		
		 Cursor cursor = database.rawQuery("SELECT * from DATA where Latitude = ? AND Longitude = ?;",s);
	     cursor.moveToFirst();
	     if(cursor.getCount() > 1)
	     {
	    	 //Entry e = new Entry();
		     //e= cursorToEntry(cursor);
		     //long id=e.getID();
		     long id =cursor.getLong(0);
		     return id;
	     }
	     else
	    	 return -1;
	}
//	public ArrayList<Entry> getEntrySortedByDate(String carname){
//	ArrayList<Entry> list = new ArrayList<Entry>();
//
//	String[] car = { carname };
//	Cursor cursor = database.rawQuery("SELECT * from DATA where carname = ? order by date(date);", car);
//	cursor.moveToFirst();
//
//	while(cursor.isAfterLast() == false){
//		list.add(cursorToEntry(cursor));
//		cursor.moveToNext();
//	}
//
//	return list;
//}

//	public void createCar (String carname){
//	ContentValues values = new ContentValues();
//	values.put ("carname", carname);
//
//	long insertID = database.insert ("CARS", null, values);
//
//	Cursor cursor = database.query("CARS", carcolumns, "ID = " + insertID, null, null, null, null);
//	cursor.moveToFirst();
//}

//protected List<String> getAllCars(){
//	List<String> CarList = new ArrayList<String>();
//	Cursor cursor = database.query("CARS", carcolumns, null, null, null, null, null);
//	cursor.moveToFirst();
//
//	if(cursor.getCount() == 0) return CarList;
//
//	while (cursor.isAfterLast() == false){
//		String car = cursor.getString(1);
//		CarList.add(car);
//		cursor.moveToNext();
//	}
//
//	cursor.close();
//
//	return CarList;
//}

//protected ArrayList<Car> getAllCarsWithDescription(){
//	ArrayList<Car> CarList = new ArrayList<Car>();
//	Cursor carcursor = database.query("CARS", carcolumns, null, null, null, null, null);
//	carcursor.moveToFirst();
//
//	Cursor entrycursor = database.query("DATA", datacolumns, null, null, null, null, null);
//	entrycursor.moveToFirst();
//
//	if(carcursor.getCount() == 0) return CarList;
//
//	while (carcursor.isAfterLast() == false){
//		String carname = carcursor.getString(1);
//		double liter = 0.0;
//
//		if (entrycursor.getCount () != 0){
//			while (entrycursor.isAfterLast() == false){
//				if (entrycursor.getString(1).equals(carname)){
//					liter = liter + entrycursor.getDouble(2);
//				}
//				entrycursor.moveToNext();
//			}
//			entrycursor.moveToFirst();
//		}
//
//		Car c = cursorToCar(carcursor);
//		c.setDescription(Double.toString(liter));
//
//		CarList.add(c);
//		carcursor.moveToNext();
//	}
//
//	entrycursor.close();
//	carcursor.close();
//
//	return CarList;
//}
//	public void addReceipt (String path, long DataID, long CarID){
//		ContentValues values = new ContentValues();
//		values.put ("CarID", CarID);
//		values.put ("DataID", DataID);
//		values.put ("Path", path);
//
//		long insertID = database.insert ("RECEIPTS", null, values);
//
//		Cursor cursor = database.query("RECEIPTS", receiptscolumns, "ID = " + insertID, null, null, null, null);
//		cursor.moveToFirst();
//
//	}
}