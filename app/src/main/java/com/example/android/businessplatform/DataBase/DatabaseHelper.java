package com.example.android.businessplatform.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.businessplatform.DataBase.DatabaseContract.StoreEntry;
import com.example.android.businessplatform.DataBase.DatabaseContract.RecipeEntry;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = DatabaseHelper.class.getName();
    //create database name and version
    public static final String DATABASE_NAME = "Inventory.db";
    public static final int DATABASE_VERSION = 1;

    //default constructor
    public DatabaseHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = this.getWritableDatabase();
    }

    //create and execute the Inventory sql table
    String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE " + StoreEntry.TABLE_NAME_STORE + "(" +
            StoreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            StoreEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
            StoreEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL, " +
            StoreEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL, " +
            StoreEntry.COLUMN_PRODUCT_WEIGHT + " FLOAT(4,2) NOT NULL, " +
            StoreEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, " +
            StoreEntry.COLUMN_SUPPLIER_NUMBER + " INTEGER);";

    //create and execute the recipe sql table
    String SQL_CREATE_RECIPE_TABLE = "CREATE TABLE " + RecipeEntry.TABLE_NAME_RECIPE + "(" +
            RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RecipeEntry.COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
            RecipeEntry.COLUMN_RECIPE_TYPE + " TEXT NOT NULL, " +
            RecipeEntry.COLUMN_RECIPE_SERVINGS + " INTEGER NOT NULL, " +
            RecipeEntry.COLUMN_RECIPE_VALUES + " TEXT NOT NULL);";

    SQLiteDatabase mDB;

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
        db.execSQL(SQL_CREATE_RECIPE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //creating required tables
        db.execSQL("DROP TABLE IF EXISTS " + StoreEntry.TABLE_NAME_STORE );
        db.execSQL("DROP TABLE IF EXISTS " + RecipeEntry.TABLE_NAME_RECIPE);

        //create new tables
        onCreate(db);
    }

    public List<String> getProductName(){
        //create a new arraylist to get the name of all the products
        List<String> itemName = new ArrayList<String>();
        //select specific query
        String dbQuery = "SELECT product_name FROM " + StoreEntry.TABLE_NAME_STORE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(dbQuery, null);
        //loop through all rows of the table
        if(cursor.moveToFirst()){
            do{
                itemName.add(cursor.getString(cursor.getColumnIndex(StoreEntry.COLUMN_PRODUCT_NAME)));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return itemName;
    }
}
