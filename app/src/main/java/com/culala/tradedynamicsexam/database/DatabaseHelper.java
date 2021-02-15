package com.culala.tradedynamicsexam.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.culala.tradedynamicsexam.database.contracts.ProductMaintenanceContract.*;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 3;

    //WORKING
    private static final String DB_NAME = "tradedynamics.db";
    private Context mContext;

    public DatabaseHelper(Context context){
        super(context,DB_NAME, null, DB_VERSION);
        mContext =context;
    }


    @Override
    public void onCreate(SQLiteDatabase db){
        //Products Table --

        final String SQL_PRODUCTS_TABLE = "CREATE TABLE " +
                ProductMaintenanceEntry.TABLE_NAME + " (" +
                ProductMaintenanceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ProductMaintenanceEntry.KEY_PRODUCT_NAME + " TEXT NOT NULL, " +
                ProductMaintenanceEntry.KEY_PRODUCT_UNIT+ " TEXT NOT NULL, " +
                ProductMaintenanceEntry.KEY_PRODUCT_PRICE+ " REAL NOT NULL, " +
                ProductMaintenanceEntry.KEY_PRODUCT_DATEOFEXPIRY + " TEXT NOT NULL, " +
                ProductMaintenanceEntry.KEY_PRODUCT_AVAILABLEINVENTORY + " INTEGER NOT NULL, " +
                ProductMaintenanceEntry.KEY_PRODUCT_IMAGE + " BLOB NOT NULL " +
                ");";
        db.execSQL(SQL_PRODUCTS_TABLE);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +ProductMaintenanceEntry.TABLE_NAME);

        onCreate(db);
    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        db.disableWriteAheadLogging();
        super.onOpen(db);
    }
}
