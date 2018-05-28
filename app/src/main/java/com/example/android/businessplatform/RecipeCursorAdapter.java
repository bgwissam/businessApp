package com.example.android.businessplatform;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.android.businessplatform.DataBase.DatabaseContract.RecipeEntry;
import com.example.android.businessplatform.DataBase.DatabaseHelper;

public class RecipeCursorAdapter extends CursorAdapter {

    private static final String LOG_TAG = RecipeCursorAdapter.class.getName();

    public RecipeCursorAdapter (Context context, Cursor cursor1){
        super(context, cursor1, 0);
    }
    @Override
    public View newView(Context context, Cursor cursor1, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.recipe_activity, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor1) {

        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        cursor1 = db.rawQuery("SELECT * FROM " + RecipeEntry.TABLE_NAME_RECIPE, null);

        //find which fields would be populated
        TextView recipeName = view.findViewById(R.id.recipe_name);
        TextView recipeType = view.findViewById(R.id.recipe_type);
        TextView recipeServings = view.findViewById(R.id.recipe_servings);
        TextView recipeValues = view.findViewById(R.id.ingrediant_name_1);
        if(cursor1.moveToNext()) {
            //get the data from the table recipes
            String mRecipeName = cursor1.getString(cursor1.getColumnIndex(RecipeEntry.COLUMN_RECIPE_NAME));
            String mRecipeType = cursor1.getString(cursor1.getColumnIndexOrThrow(RecipeEntry.COLUMN_RECIPE_TYPE));
            String mRecipeServings = cursor1.getString(cursor1.getColumnIndexOrThrow(RecipeEntry.COLUMN_RECIPE_SERVINGS));
            String mRecipeValues = cursor1.getString(cursor1.getColumnIndexOrThrow(RecipeEntry.COLUMN_RECIPE_VALUES));

            //get current item Id
            int currenId = cursor1.getInt(cursor1.getColumnIndex(RecipeEntry._ID));
            //create a content Uri for the current Id
            final Uri contentUri = Uri.withAppendedPath(RecipeEntry.CONTENT_URI, Integer.toString(currenId));

            //populate fields with their respective data
            recipeName.setText(mRecipeName);
            recipeType.setText(mRecipeType);
            recipeServings.setText(mRecipeServings);
            recipeValues.setText(mRecipeValues);

        }
    }
}
