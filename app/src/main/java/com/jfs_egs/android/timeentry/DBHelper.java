package com.jfs_egs.android.timeentry;

import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.ViewParent;
import android.widget.Toast;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by JFS on 4/17/2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    public Context mContext;
    // All Static variables
    private static final int DATABASE_VERSION = 6;          // If you change the database schema, you must increment the database version.
    private static final String DATABASE_NAME = "JFSTime";
    // TABLES
    private static final String TIME_ENTRIES_TABLE = "TimeEntries";
    private static final String CM_TABLE = "CM";
    // TIME_ENTRIES_TABLE Table Columns
    private static final String CLIENT_NAME = "Client";
    private static final String TIMEBILLED = "Time";
    private static final String DESCRIPTION = "Description";
    private static final String ENTRY_DATE = "Entry_Date";
    private static final String UID = "UID";
    //CM_TABLE Table Columns
    private static final String CM_CLIENT="Client";
    private static final String CM_UID="CMUID";
    //STRINGS TO CREATE TABLES WITH THE NAMED COLUMNS
    String CREATE_TIME_ENTRIES_TABLE = "CREATE TABLE " + TIME_ENTRIES_TABLE + "("
            + CLIENT_NAME + " TEXT," + TIMEBILLED + " TEXT," + DESCRIPTION + " TEXT," + ENTRY_DATE + " DATETIME DEFAULT (DATETIME (CURRENT_TIMESTAMP,'localtime'))," + UID + " INTEGER PRIMARY KEY" + ")";
    String CREATE_CM_TABLE = "CREATE TABLE " + CM_TABLE + "(" + CM_CLIENT + " TEXT," +  CM_UID + " INTEGER PRIMARY KEY" + ")";
    //***       ***         ***
    //***   BEGIN METHODS   ***
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);  //MANDATED ANDROID METHOD
        mContext = context;
    }

    // Creating Tables                          METHODS FOR CREATING TABLES
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TIME_ENTRIES_TABLE);
        db.execSQL(CREATE_CM_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TIME_ENTRIES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CM_TABLE);
        // Create tables again
        onCreate(db);
    }

    //***       ***     ***
    //SOME CRUD METHODS -- CAN ALSO BE WRITTEN DIRECTLY IN Activity CODE, AFTER CALLING DBHelper mDbHelper = new FDBbHelper(getContext());
    void addTimeEntry(String clientname, String timebilled, String  descriptex) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CLIENT_NAME, clientname);
        values.put(TIMEBILLED, timebilled);
        values.put(DESCRIPTION, descriptex);
        db.insert(TIME_ENTRIES_TABLE, null, values);
        db.close(); // Closing database connection
    }

    public Cursor getAllEntries() {
        ArrayList<String> entriesList= new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TIME_ENTRIES_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

//***   DELETIONS FROM TIME ENTRIES
    public String DeletePrior(String datecutoff){
        String delstring ="DELETE FROM " + TIME_ENTRIES_TABLE + " WHERE ENTRY_DATE <  date('" + datecutoff + "')" ;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(delstring);
        return "All entries pror to " + datecutoff + "deleted.";
    }

    public String DeleteRows(ArrayList<Integer> delrows){
        String totaler = android.text.TextUtils.join("','", delrows);
        String delstring ="DELETE FROM " + TIME_ENTRIES_TABLE + " WHERE UID IN  ('" + totaler + "')" ;  //WITH IN OPERATOR, EACH ITEM MUST BE IN SINGLE QUOTES
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(delstring);
        return "DELETING UIDS " + totaler + "." ;
    }

//***   DELETIONS FROM AND ADDITIONS TO CM TABLE
    public String DeleteCM (String passedcm){
        SQLiteDatabase db = this.getWritableDatabase();
        String cmdelstring="DELETE FROM " + CM_TABLE + " WHERE Client = '" + passedcm + "'";
        db.execSQL(cmdelstring);
        return "Deleted " + passedcm + ".";
    }

    public void AddCM (String newcm){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CM_CLIENT, newcm);
        db.insert(CM_TABLE, null, values);
        db.close();
    }

    public Cursor getAllCMs() {
        ArrayList<String> allcms= new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + CM_TABLE + " ORDER BY " + CM_CLIENT + " ASC" ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public String InsertCMS(String[] cms){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
/* USED THIS TO POPULATE CLIENT LIST INITIALLY
        for(int x=0;x<cms.length;x++){
            values.put(CM_CLIENT, cms[x]);
            db.insert(CM_TABLE, null, values);
        }
*/
        String cntquery= "SELECT * FROM " + CM_TABLE;
        Cursor cursor = db.rawQuery(cntquery, null);
        int cnt = cursor.getCount();
        return String.valueOf(cnt);
    }

//***   ***     ***    GET DATA FROM ROWS TO BE E-MAILED ***    ***     ***

    public Cursor mailrows(ArrayList<Integer> rowstomail){
        String rowstring = android.text.TextUtils.join("','", rowstomail);
        SQLiteDatabase db = this.getWritableDatabase();
        String selxstring="SELECT * FROM " + TIME_ENTRIES_TABLE + " WHERE UID IN  ('" + rowstring + "')" ;  //WITH IN OPERATOR, EACH ITEM MUST BE IN SINGLE QUOTES
        Cursor cx = db.rawQuery(selxstring,null);
        return cx;
    }

}
