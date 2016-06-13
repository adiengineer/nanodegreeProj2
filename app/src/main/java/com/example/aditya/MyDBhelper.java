package com.example.aditya.nanodegreeproj1;

/**
 * Created by Aditya on 09-06-2016.
 */
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Aditya on 29-12-2015.
 */
public class MyDBhelper extends SQLiteOpenHelper
{
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Movies.db";

    public MyDBhelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i("dbhelperconst", "db helper is created");
    }

    public void onCreate(SQLiteDatabase db) //imp- this is called only when database is first created,
    //it is not called when helper is created. If db exists this is ignored
    {
        Log.i("oncreatecall","inside_on_create");
        //sql query to create table
        db.execSQL("CREATE TABLE " + DatabaseContract.tableDefinition.TABLE_NAME + " (" +
                        DatabaseContract.tableDefinition.COLUMN_NAME_ID + " INTEGER PRIMARY KEY, " +
                        DatabaseContract.tableDefinition.COLUMN_NAME_POSTERPATH + " TEXT, " +
                        DatabaseContract.tableDefinition.COLUMN_NAME_OVERVIEW + " TEXT, " +
                        DatabaseContract.tableDefinition.COLUMN_NAME_RELEASEDATE + " TEXT, " +
                        DatabaseContract.tableDefinition.COLUMN_NAME_TITLE + " TEXT, " +
                        DatabaseContract.tableDefinition.COLUMN_NAME_VOTEAVERAGE + " DOUBLE" +
                        " );"
        );  //check if DOUBLE data type works.

       /* try
        {
            db.execSQL("CREATE TABLE " + DatabaseContract.tableDefinition.TABLE_NAME + " (" +
                            DatabaseContract.tableDefinition.COLUMN_NAME_ID + " INTEGER PRIMARY KEY, " +
                            DatabaseContract.tableDefinition.COLUMN_NAME_POSTERPATH + " TEXT, " +
                            DatabaseContract.tableDefinition.COLUMN_NAME_OVERVIEW + " TEXT, " +
                            DatabaseContract.tableDefinition.COLUMN_NAME_RELEASEDATE + " TEXT, " +
                            DatabaseContract.tableDefinition.COLUMN_NAME_TITLE + " TEXT" +
                            " );"
            );
        }
        catch (SQLException e)
        {
            Log.i("sqlerror",e.getMessage());
        } */
    // let us

    }

    //for upgrade, wipe of the data base and start afresh
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over

        Log.i("oncreatecall","onupgradecalled");
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.tableDefinition.TABLE_NAME);
        onCreate(db);

        Toast.makeText(null,"upgrade is called",Toast.LENGTH_SHORT).show();
        //basically delete and start over.
    }
}