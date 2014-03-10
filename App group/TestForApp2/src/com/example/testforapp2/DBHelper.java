package com.example.testforapp2;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
	private final static int DBVERSION = 1;
	private final static String DB_NAME = "TestForApp.db";
	private final static String TABLE_NAME = "directory";
	private final static String FIELD_NAME1 = "_id";
	private final static String FIELD_NAME2 = "directory_path";
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DBVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		final String SQL = "CREATE TABLE IF NOT EXISTS " + getTableName() + "( " +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"directory_path VARCHAR(100) " +
				");";
		db.execSQL(SQL);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		final String SQL = "DROP TABLE " + getTableName();
		db.execSQL(SQL); 
		
	}

	public String getTableName() {
		return TABLE_NAME;
	}

	public String getFieldName2() {
		return FIELD_NAME2;
	}

}
