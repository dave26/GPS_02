package com.example.gps02;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLite extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "data.db";
	private static final int DATABASE_VERSION = 1;
	private static MySQLite mInstance = null;

	private static final String TABLE_CREATE_DATA = "" + "create table DATA("
			+ "  ID integer primary key autoincrement, "
			+ "Latitude double, "
			+ "Longitude double"//, "
			//+ "full bool"
			+ ")";

//	private static final String TABLE_CREATE_CARS = "" + "create table CARS("
//			+ "  ID integer primary key autoincrement, "
//			+ "carname string"
//			+ ")";

//	private static final String TABLE_CREATE_RECEIPTS = "" + "create table RECEIPTS("
//			+ "  ID integer primary key autoincrement, "
//			+ "CarID int, "
//			+ "DataID int, "
//			+ "Path string"
//			+ ")";
//
	public static MySQLite getInstance(Context context){
		if (mInstance == null){
			mInstance = new MySQLite(context.getApplicationContext());
		}
		return mInstance;		
	}

	private MySQLite(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(TABLE_CREATE_DATA);
		//database.execSQL(TABLE_CREATE_CARS);
		//database.execSQL(TABLE_CREATE_RECEIPTS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if ((newVersion == 2) && (oldVersion == 1)){
			db.execSQL("ALTER TABLE DATA ADD COLUMN full bool DEFAULT FALSE;");
		} else if  ((newVersion == 3) && (oldVersion == 2)){
			db.execSQL(TABLE_CREATE_DATA);
		} else {

		}
	}
}