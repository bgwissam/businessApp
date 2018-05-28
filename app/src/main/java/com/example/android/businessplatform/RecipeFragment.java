package com.example.android.businessplatform;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.android.businessplatform.DataBase.DatabaseContract.RecipeEntry;
import com.example.android.businessplatform.DataBase.DatabaseHelper;

public class RecipeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = RecipeFragment.class.getName();
    //create an integer for the book loader
    private static final int RECIPE_LOADER = 0;
    RecipeCursorAdapter mCursorAdapterRecipe;
    private Cursor currentCursor;
    public RecipeFragment(){

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        getActivity().getSupportLoaderManager().initLoader(RECIPE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        final View rootView = inflater.inflate(R.layout.activity_main, container, false);
        DatabaseHelper helper = new DatabaseHelper(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        currentCursor = db.rawQuery("SELECT * FROM " + RecipeEntry.TABLE_NAME_RECIPE, null);
        Log.i(LOG_TAG, "Current Column count " + currentCursor.getColumnCount());
        //set empty activity
        ListView listView = rootView.findViewById(R.id.recipe_view);
        listView.setAdapter(null);
        View emptyView = rootView.findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);
        //create a new cursor adapter
        mCursorAdapterRecipe = new RecipeCursorAdapter(getActivity(),currentCursor);
        listView.setAdapter(mCursorAdapterRecipe);

        //create onClick listener to inflate the editor view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //create new intent to opened the editor
                Intent intent = new Intent(getActivity(), EditorActivity.class);
                //create Uri to pass the contents of the item selected to the edit view
                Uri currentItem = ContentUris.withAppendedId(RecipeEntry.CONTENT_URI, id);
                intent.setData(currentItem);
                startActivity(intent);
            }
        });

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
                RecipeEntry._ID,
                RecipeEntry.COLUMN_RECIPE_NAME,
                RecipeEntry.COLUMN_RECIPE_TYPE,
                RecipeEntry.COLUMN_RECIPE_SERVINGS,
                RecipeEntry.COLUMN_RECIPE_VALUES,

        };
        return new CursorLoader(getActivity(), RecipeEntry.CONTENT_URI, projections, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //swap cursor
        mCursorAdapterRecipe.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //set Cursor to null
        mCursorAdapterRecipe.swapCursor(null);
    }
}
