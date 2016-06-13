package com.example.aditya.nanodegreeproj1;

import android.provider.BaseColumns;

/**
 * Created by Aditya on 09-06-2016.
 */
//this class defines the structure of the table to be used in our database.
public final class DatabaseContract
{
    public DatabaseContract()
    {

    }

    //define the table's structure
    public static abstract class tableDefinition implements BaseColumns
    {
        //give the structure of the table
        //primary key is automatically inherited

        // collect all column names in one place, good for organization.
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_NAME_POSTERPATH = "posterPath";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
        public static final String COLUMN_NAME_RELEASEDATE = "releasedate";
        public static final String COLUMN_NAME_ID = "id"; //to be used as the primary key
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_VOTEAVERAGE = "voteaverage";


    }
}

