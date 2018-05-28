package com.example.android.businessplatform.DataBase;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import com.example.android.businessplatform.DataBase.DatabaseContract.StoreEntry;
import com.example.android.businessplatform.DataBase.DatabaseContract.RecipeEntry;

import com.example.android.businessplatform.R;

public class DatabaseProvider extends ContentProvider {

    private final static String LOG_TAG = DatabaseProvider.class.getName();
    //create a Uri matcher integer
    private static final int STORE = 100;
    private static final int STORE_ID = 101;
    private static final int RECIPE = 102;
    private static final int RECIPE_ID = 103;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    //initialize cursor
    Cursor cursor;

    //this will run the first time anything is called from this class
    static {
        //adding content Uri for Inventory table
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.PATH_STORE, STORE);
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.PATH_STORE_ID, STORE_ID);
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.PATH_RECIPE, RECIPE);
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.PATH_RECIPE_ID, RECIPE_ID);
    }

    public DatabaseHelper mDbHelper;
    @Override
    public boolean onCreate() {

        mDbHelper = new DatabaseHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        //create swtich statment to match Uri
        int match = sUriMatcher.match(uri);
        switch (match) {
            case STORE:
                cursor = database.query(StoreEntry.TABLE_NAME_STORE, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case STORE_ID:
                //extract data depending on the selected ID
                selection = StoreEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(StoreEntry.TABLE_NAME_STORE, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case RECIPE:
                cursor = database.query(RecipeEntry.TABLE_NAME_RECIPE, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case RECIPE_ID:
                //extract data depending on selected ID
                selection = RecipeEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(RecipeEntry.TABLE_NAME_RECIPE, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException(R.string.cannot_query_uri + " " + uri);
        }
        //set notification for the cursor to notify the query of any changes
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }


    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case STORE:
                return StoreEntry.CONTENT_LIST_TYPE;
            case STORE_ID:
                return StoreEntry.CONTENT_ITEM_TYPE;
            case RECIPE:
                return RecipeEntry.CONTENT_LIST_TYPE;
            case RECIPE_ID:
                return RecipeEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Uknown uri " + uri + "with match " + match);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case STORE:
                return insertProduct(uri, contentValues);
            case RECIPE:
                return insertRecipe(uri, contentValues);
            default:
                throw new IllegalArgumentException(R.string.insertion_not_supported + " " + uri);
        }
    }

    //create insertion helper
    public Uri insertProduct(Uri uri, ContentValues contentValues) {

        if(cursor !=null && cursor.moveToFirst()) {
            //sanity check to avoid inserting null values
            String name = contentValues.getAsString(StoreEntry.COLUMN_PRODUCT_NAME);
            if (name == null) {
                throw new IllegalArgumentException(R.string.item_needs_name + "");
            }
            String price = contentValues.getAsString(StoreEntry.COLUMN_PRODUCT_PRICE);
            if (price == null) {
                throw new IllegalArgumentException(R.string.item_needs_price + "");
            }
            String quantity = contentValues.getAsString(StoreEntry.COLUMN_PRODUCT_QUANTITY);
            if (quantity == null) {
                throw new IllegalArgumentException(R.string.item_needs_quantity + "");
            }
            float weight = contentValues.getAsFloat(StoreEntry.COLUMN_PRODUCT_WEIGHT);
            if (weight == 0) {
                throw new IllegalArgumentException(R.string.item_needs_weight + "");
            }
            String supplier = contentValues.getAsString(StoreEntry.COLUMN_SUPPLIER_NAME);
            if (supplier == null) {
                throw new IllegalArgumentException(R.string.item_needs_supplier_name + "");
            }
        }
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long id = db.insert(StoreEntry.TABLE_NAME_STORE, null, contentValues);
        //if id is negative than insertion didn't happen
        if (id == -1) {
            Log.e(LOG_TAG, R.string.failed_to_insert_data + " " + uri);
            return null;
        }
        //notify all listeners if any insertion has happened
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    //create insertion recipe
    public Uri insertRecipe(Uri uri, ContentValues contentValues) {

        if(cursor !=null && cursor.moveToFirst()) {
            //sanity check to avoid inserting null values
            String name = contentValues.getAsString(RecipeEntry.COLUMN_RECIPE_NAME);
            if (name == null) {
                throw new IllegalArgumentException(R.string.recipe_needs_name + "");
            }
            String type = contentValues.getAsString(RecipeEntry.COLUMN_RECIPE_TYPE);
            if (type == null) {
                throw new IllegalArgumentException(R.string.recipe_needs_type + "");
            }
            int servings = contentValues.getAsInteger(RecipeEntry.COLUMN_RECIPE_SERVINGS);
            if (servings == 0) {
                throw new IllegalArgumentException(R.string.recipe_needs_servings + "");
            }
            String values = contentValues.getAsString(RecipeEntry.COLUMN_RECIPE_VALUES);
            if (values == null) {
                throw new IllegalArgumentException(R.string.recipe_needs_values + "");
            }

        }
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long id = db.insert(RecipeEntry.TABLE_NAME_RECIPE, null, contentValues);
        //if id is negative than insertion didn't happen
        if (id == -1) {
            Log.e(LOG_TAG, R.string.failed_to_insert_data + " " + uri);
            return null;
        }
        //notify all listeners if any insertion has happened
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    //delete data for the given row or rows
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //get writable data
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        //check deleted rows
        int deletedRows = 0;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case STORE:
                deletedRows = db.delete(StoreEntry.TABLE_NAME_STORE, selection, selectionArgs);
                break;
            case STORE_ID:
                selection = StoreEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                deletedRows = db.delete(StoreEntry.TABLE_NAME_STORE, selection, selectionArgs);
                break;
            case RECIPE:
                deletedRows = db.delete(RecipeEntry.TABLE_NAME_RECIPE, selection, selectionArgs);
                break;
            case RECIPE_ID:
                selection = RecipeEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                deletedRows = db.delete(RecipeEntry.TABLE_NAME_RECIPE, selection, selectionArgs);
        }
        //check if rows were deleted
        if (deletedRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deletedRows;
    }


    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        //match uri
        int match = sUriMatcher.match(uri);
        switch (match) {
            case STORE:
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case STORE_ID:
                //get id in order to update the right item
                selection = StoreEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case RECIPE:
                return updateRecipe(uri, contentValues, selection, selectionArgs);
            case RECIPE_ID:
                selection = RecipeEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateRecipe(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException(R.string.failed_to_update_data + " " + uri);

        }
    }

    public int updateProduct(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        //sanity check
        if (contentValues.containsKey(StoreEntry.COLUMN_PRODUCT_NAME)) {
            String name = contentValues.getAsString(StoreEntry.COLUMN_PRODUCT_NAME);
            if (name == null) {
                throw new IllegalArgumentException(R.string.item_needs_name + "");
            }
        }
        if (contentValues.containsKey(StoreEntry.COLUMN_PRODUCT_PRICE)) {
            String price = contentValues.getAsString(StoreEntry.COLUMN_PRODUCT_PRICE);
            if (price == null) {
                throw new IllegalArgumentException(R.string.item_needs_price + "");
            }
        }
        if (contentValues.containsKey(StoreEntry.COLUMN_PRODUCT_QUANTITY)) {
            int quantity = contentValues.getAsInteger(StoreEntry.COLUMN_PRODUCT_QUANTITY);
            if (quantity == -1) {
                throw new IllegalArgumentException(R.string.item_needs_quantity + "");
            }
        }
        if(contentValues.containsKey(StoreEntry.COLUMN_PRODUCT_WEIGHT)) {
            float weight = contentValues.getAsLong(StoreEntry.COLUMN_PRODUCT_WEIGHT);
            if (weight == 0) {
                throw new IllegalArgumentException(R.string.item_needs_weight + "");
            }
        }
        if (contentValues.containsKey(StoreEntry.COLUMN_SUPPLIER_NAME)) {
            String supName = contentValues.getAsString(StoreEntry.COLUMN_SUPPLIER_NAME);
            if (supName == null) {
                throw new IllegalArgumentException(R.string.item_needs_supplier_name + "");
            }
        }
        if (contentValues.size() == 0) {
            return 0;
        }
        //get writable sql database to update edited rows
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int affectedRows = db.update(StoreEntry.TABLE_NAME_STORE, contentValues, selection, selectionArgs);
        //notify incase of changes
        if (affectedRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return affectedRows;
    }

    public int updateRecipe (Uri uri, ContentValues contentValues, String selection, String [] selectionArgs){
        //sanity check
        if (contentValues.containsKey(RecipeEntry.COLUMN_RECIPE_NAME)) {
            String name = contentValues.getAsString(RecipeEntry.COLUMN_RECIPE_NAME);
            if (name == null) {
                throw new IllegalArgumentException(R.string.recipe_needs_name + "");
            }
        }
        if (contentValues.containsKey(RecipeEntry.COLUMN_RECIPE_TYPE)) {
            String type = contentValues.getAsString(RecipeEntry.COLUMN_RECIPE_TYPE);
            if (type == null) {
                throw new IllegalArgumentException(R.string.recipe_needs_type + "");
            }
        }
        if (contentValues.containsKey(RecipeEntry.COLUMN_RECIPE_SERVINGS)) {
            int servings = contentValues.getAsInteger(RecipeEntry.COLUMN_RECIPE_SERVINGS);
            if (servings == 0) {
                throw new IllegalArgumentException(R.string.recipe_needs_servings + "");
            }
        }
        if(contentValues.containsKey(RecipeEntry.COLUMN_RECIPE_VALUES)) {
            float weight = contentValues.getAsLong(RecipeEntry.COLUMN_RECIPE_VALUES);
            if (weight == 0) {
                throw new IllegalArgumentException(R.string.recipe_needs_values + "");
            }
        }

        if (contentValues.size() == 0) {
            return 0;
        }
        //get writable sql database to update edited rows
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int affectedRows = db.update(RecipeEntry.TABLE_NAME_RECIPE, contentValues, selection, selectionArgs);
        //notify incase of changes
        if (affectedRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return affectedRows;
    }
}
