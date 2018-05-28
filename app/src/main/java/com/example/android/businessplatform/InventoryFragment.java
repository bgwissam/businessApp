package com.example.android.businessplatform;


import android.content.ContentUris;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.android.businessplatform.DataBase.DatabaseContract.StoreEntry;
import com.example.android.businessplatform.DataBase.DatabaseHelper;

public class InventoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = InventoryFragment.class.getName();
    //create an integer for the book loader
    private static final int STOCK_LOADER = 0;
    PlatformCursorAdapter mCursorAdapter;
    SimpleCursorAdapter mSCA;
    Cursor currentCursor;

    public InventoryFragment(){}

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        getActivity().getSupportLoaderManager().initLoader(STOCK_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        final View rootView = inflater.inflate(R.layout.activity_main, container, false);
        //apply simpleCursorAdapter


        //create a database instance to get the current cursor
        DatabaseHelper db = new DatabaseHelper(getContext());
        SQLiteDatabase database = db.getReadableDatabase();

        Log.i(LOG_TAG, "the Current Cursor " + currentCursor.getColumnCount());
        //set empty activity
        ListView listView = rootView.findViewById(R.id.display_view);
        currentCursor = database.rawQuery("SELECT * FROM " + StoreEntry.TABLE_NAME_STORE, null );
        View emptyView = rootView.findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);
        //create a new cursor adapter
        mCursorAdapter = new PlatformCursorAdapter(getActivity(),currentCursor);
        listView.setAdapter(mCursorAdapter);
        //create onClick listener to inflate the editor view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //create new intent to open the editor
                Intent intent = new Intent(getActivity(), EditorActivity.class);
                //create Uri to pass the contents of the item selected to the edit view
                Uri currentItem = ContentUris.withAppendedId(StoreEntry.CONTENT_URI, id);
                intent.setData(currentItem);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(STOCK_LOADER, null, this);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        //define the projections that will be used
        String[] projections = {
                StoreEntry._ID,
                StoreEntry.COLUMN_PRODUCT_NAME,
                StoreEntry.COLUMN_PRODUCT_PRICE,
                StoreEntry.COLUMN_PRODUCT_QUANTITY,
                StoreEntry.COLUMN_PRODUCT_WEIGHT,
                StoreEntry.COLUMN_SUPPLIER_NAME,
                StoreEntry.COLUMN_SUPPLIER_NUMBER,
        };
        return new CursorLoader(getActivity(), StoreEntry.CONTENT_URI, projections, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //swap cursor
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //set Cursor to null
        mCursorAdapter.swapCursor(null);
    }
}
