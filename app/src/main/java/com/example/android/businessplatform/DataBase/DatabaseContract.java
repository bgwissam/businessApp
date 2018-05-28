package com.example.android.businessplatform.DataBase;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract implements BaseColumns {

    //set fixed Uri content authority
    public static final String CONTENT_AUTHORITY = "com.example.android.businessplatform.DataBase";
    public static final Uri BASE_CONTENT_AUTHORITY = Uri.parse("content://" + CONTENT_AUTHORITY);
    //create path for the inventory table
    public static final String PATH_STORE = "store";
    public static final String PATH_STORE_ID = "store/#";
    //create path for the recipe table
    public static final String PATH_RECIPE = "recipies";
    public static final String PATH_RECIPE_ID = "recipies/#";

    public static abstract class StoreEntry implements BaseColumns {

        public static final String TABLE_NAME_STORE = "store";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME = "product_name";
        public static final String COLUMN_PRODUCT_PRICE = "product_price";
        public static final String COLUMN_PRODUCT_QUANTITY = "product_quantity";
        public static final String COLUMN_PRODUCT_WEIGHT = "product_weight";
        public static final String COLUMN_SUPPLIER_NAME = "supplier_name";
        public static final String COLUMN_SUPPLIER_NUMBER = "supplier_number";

        //declaration of MIME types constants
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STORE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STORE_ID;

        //Content authority will call the Inventory table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_AUTHORITY, PATH_STORE);

    }
    //create a database to store the receip's ingrediants
    public static abstract class RecipeEntry implements BaseColumns {

        public static final String TABLE_NAME_RECIPE = "recipies";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_RECIPE_NAME = "recipe_name";
        public static final String COLUMN_RECIPE_TYPE = "recipe_type";
        public static final String COLUMN_RECIPE_SERVINGS = "recipe_servings";
        public static final String COLUMN_RECIPE_VALUES = "recipe_values";

        //declaration of MIME types constants
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECIPE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECIPE_ID;

        //Content authority will call the Recipe table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_AUTHORITY, PATH_RECIPE);
    }


}
