package com.example.android.businessplatform;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

import com.example.android.businessplatform.DataBase.DatabaseContract;
import com.example.android.businessplatform.DataBase.DatabaseContract.RecipeEntry;
import com.example.android.businessplatform.DataBase.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class RecipeEditor extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public final static String LOG_TAG = RecipeEditor.class.getName();

    private static final int RECIPE_LOADER = 1;
    //will check if data is saved
    public boolean dataSaved;
    //edit text for book name
    private EditText mRecipeName;
    //edit text for book price
    private Spinner mRecipeType;
    //edit text for book quantity
    private EditText mRecipeServings;
    //edit text for book weight
    private Spinner mRecipeValuesName;
    private TextView mRecipeValuesQuantity;
    //create an adapter to hold the types of recipies
    private final ArrayList<String> recipeList = new ArrayList<>();
    //create an intent variable to populate another activity
    private Intent intent;
    //set id for ingrediante values Edit Text
    private int ingrediante_weight = 1;
    private int ingrediante_type = 1;
    //create a global Uri variable
    private Uri currentRecipeUri;
    //check for changes in the TextViews
    private boolean RecipeHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            RecipeHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_editor);
        //check for the listener from BookStoreActivity if it's calling to add a new item or edit one
        //change the page title according to the request performed
        intent = getIntent();
        currentRecipeUri = intent.getData();
        //first check the content of the uri
        if (currentRecipeUri == null) {
            setTitle(R.string.add_new_product);
        } else {
            setTitle(R.string.edit_current_product);
            //initialize the loader
            getLoaderManager().initLoader(RECIPE_LOADER, null, this);
        }
        //create value for recipe type list
        recipeList.add("Cake");
        recipeList.add("Cookies");
        recipeList.add("Cup Cake");
        recipeList.add("Cake Pops");
        recipeList.add("Marchmello");
        recipeList.add("Others");

        //load ingredients
        loadIngredientData();


        //find which fields the user will be editing
        mRecipeName = findViewById(R.id.recipe_name_spinner);
        mRecipeType = findViewById(R.id.recipe_type_spinner);
        mRecipeServings = findViewById(R.id.recipe_servings_spinner);
        mRecipeValuesName = findViewById(R.id.ingrediant_name_1);
        mRecipeValuesQuantity = findViewById(R.id.ingrediant_value_1);

        //set type spinner list
        ArrayAdapter<String> listRecipe = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, recipeList);
        listRecipe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRecipeType.setAdapter(listRecipe);

        //set onclick listener for the add ingredient button
        final Button addRow = findViewById(R.id.button_add_row);
        addRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRow();
            }
        });

        //set listener to know when the views were edited
        mRecipeName.setOnTouchListener(mTouchListener);
        mRecipeType.setOnTouchListener(mTouchListener);
        mRecipeServings.setOnTouchListener(mTouchListener);
        mRecipeValuesName.setOnTouchListener(mTouchListener);
        mRecipeValuesQuantity.setOnTouchListener(mTouchListener);
    }

    private void loadIngredientData (){

        mRecipeValuesName = findViewById(R.id.ingrediant_name_1);
        //open a new database
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        //spinner drop down element
        List<String> items = db.getProductName();
        ArrayAdapter<String> ingredients = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,items );
        ingredients.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if(ingredients!=null) {
            mRecipeValuesName.setAdapter(ingredients);
        }
        else {
            mRecipeValuesName.setAdapter(null);
        }
    }


    public void addRow() {
        LinearLayout ingrediantLayout = findViewById(R.id.ingrediant_linear_layout);
        //add edit text
        EditText ingrediantEditText = new EditText(this);
        Spinner ingrediantSpinner = new Spinner(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        ingrediantEditText.setLayoutParams(params);
        ingrediantSpinner.setLayoutParams(params);
        ingrediantEditText.setText("");
        ingrediantEditText.setHint(R.string.ingrediant_value);
        ingrediantSpinner.setSelection(0);
        //create an id for each view
        ingrediantEditText.setId(ingrediante_weight + 1);
        ingrediantSpinner.setId(ingrediante_type + 1);
        //add the views in order of appearance
        ingrediantLayout.addView(ingrediantSpinner);
        ingrediantLayout.addView(ingrediantEditText);

        ingrediante_weight++;
        ingrediante_type++;

    }

    private void save() {
        String name = mRecipeName.getText().toString().trim();
        String type = mRecipeType.getSelectedItem().toString();
        String servings = mRecipeServings.getText().toString().trim();
        String recipeValues = mRecipeValuesName.getSelectedItem().toString();
        //check if the user is adding a new pet of editing a current one
        if (currentRecipeUri == null && TextUtils.isEmpty(name) && TextUtils.isEmpty(type)) {
            return;
        }
        if (name.isEmpty() || type.isEmpty() || recipeValues.isEmpty()) {
            Toast.makeText(this, R.string.missing_entry_error, Toast.LENGTH_SHORT).show();
            dataSaved = false;
        } else {
            //start a content value instance
            ContentValues values = new ContentValues();
            values.put(RecipeEntry.COLUMN_RECIPE_NAME, name);
            values.put(RecipeEntry.COLUMN_RECIPE_TYPE, type);
            values.put(RecipeEntry.COLUMN_RECIPE_SERVINGS, servings);
            values.put(RecipeEntry.COLUMN_RECIPE_VALUES, recipeValues);


            //determine if this is a new book or an edited one
            if (currentRecipeUri == null) {
                //insert data into a new table row
                Uri newUri = getContentResolver().insert(RecipeEntry.CONTENT_URI, values);

                if (newUri == null) {
                    Toast.makeText(this, R.string.error_inserting_new_product, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.new_product_added, Toast.LENGTH_SHORT).show();
                    dataSaved = true;
                }
            } else {
                //update the columns of the current selected row
                int rowsAffected = getContentResolver().update(currentRecipeUri, values, null, null);

                if (rowsAffected == 0) {
                    Toast.makeText(this, R.string.error_updating_current_product, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.current_product_updated, Toast.LENGTH_SHORT).show();
                    dataSaved = true;
                }
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu from the res/menu/menu_editor.xml
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //user clicks on a selected item in the options menu
        switch (item.getItemId()) {
            case R.id.action_save:
                save();
                //check if the data was saved before closing the editor
                if (dataSaved) {
                    finish();
                }
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case R.id.home:
                //navigate back to main page
                if (!RecipeHasChanged) {
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                } else {
                    DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            NavUtils.navigateUpFromSameTask(RecipeEditor.this);
                        }
                    };
                    showUnsavedChnageDialog(discardButtonClickListener);
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        //if this is a new pet then high the delete button
        if (currentRecipeUri == null) {
            MenuItem item = menu.findItem(R.id.action_delete);
            item.setVisible(false);
        }
        return true;
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

        //define the projections that will be used
        String[] projections = {RecipeEntry._ID,
                RecipeEntry.COLUMN_RECIPE_NAME,
                RecipeEntry.COLUMN_RECIPE_TYPE,
                RecipeEntry.COLUMN_RECIPE_SERVINGS,
                RecipeEntry.COLUMN_RECIPE_VALUES,
                };

        //this code will execute the content provided
        return new CursorLoader(this, currentRecipeUri, projections, null, null, null);
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null || cursor.getCount() < 0) {
            return;
        }
        //move cursor to first position
        if (cursor.moveToFirst()) {
            //find the attributes that we require
            int name = cursor.getColumnIndex(RecipeEntry.COLUMN_RECIPE_NAME);
            int type = cursor.getColumnIndex(RecipeEntry.COLUMN_RECIPE_TYPE);
            int servings = cursor.getColumnIndex(RecipeEntry.COLUMN_RECIPE_SERVINGS);
            int values = cursor.getColumnIndex(RecipeEntry.COLUMN_RECIPE_VALUES);
            //extract the value
            String recipeName = cursor.getString(name);
            String recipeType = cursor.getString(type);
            String recipeServings = cursor.getString(servings);
            String recipeValues = cursor.getString(values);
            //update the edit text views with the current values obtained
            mRecipeName.setText(recipeName);
            //set type spinner list
            if(recipeType != null){
                for(int index = 0; index <= recipeList.size(); index++ ){
                if (recipeType == recipeList.get(index))
                mRecipeType.setSelection(index);
                }
            }

            mRecipeServings.setText(recipeServings);
            mRecipeValuesName.setSelection(values);
        }
    }
    @Override
    public void onLoaderReset(Loader loader) {

        //reset all values
        mRecipeName.setText("");
        mRecipeType.setSelection(0);
        mRecipeServings.setText("");
        mRecipeValuesName.setSelection(0);

    }
    //show dialog to warn the user of unsaved changes
    private void showUnsavedChnageDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        //create an alert dialog box and set a message for the user
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_message);
        builder.setPositiveButton(R.string.discard_button, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //user has clicked the keep editing button
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });
        //create dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {
        //in case book hasn't changed continue handing the back press
        if (!RecipeHasChanged) {
            super.onBackPressed();
            return;
        } else {
            DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            };
            //show dialog that there are unsaved changes
            showUnsavedChnageDialog(discardButtonClickListener);
        }
    }

    private void showDeleteConfirmationDialog() {
        //create an alert dialog to warn the user before deleting any record
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_book_warning);
        builder.setPositiveButton(R.string.delete_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deletebook();
            }
        });
        builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });

        //show alert dialog upon deletion
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    //create delete method
    private void deletebook() {
        //delete current book
        if (currentRecipeUri != null) {
            int rowsAffected = getContentResolver().delete(currentRecipeUri, null, null);

            if (rowsAffected == 0) {
                Toast.makeText(this, R.string.error_deleting_product, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.current_product_deleted, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
