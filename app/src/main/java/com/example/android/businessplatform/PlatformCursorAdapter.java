package com.example.android.businessplatform;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.android.businessplatform.DataBase.DatabaseContract.StoreEntry;

public class PlatformCursorAdapter extends CursorAdapter {

    private static final String LOG_TAG = PlatformCursorAdapter.class.getName();

    public PlatformCursorAdapter(Context context, Cursor cursor){
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_view, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, Cursor cursor) {
        //find which fields should be populated
        TextView itemName = (TextView) view.findViewById(R.id.item_name);
        TextView itemPrice = (TextView) view.findViewById(R.id.item_price);
        final TextView itemQuantity = (TextView) view.findViewById(R.id.item_quantity);
        final TextView itemWeight = view.findViewById(R.id.item_weight);
        TextView supplierName = (TextView) view.findViewById(R.id.item_supplier_name);
        TextView supplierNumber = (TextView) view.findViewById(R.id.item_supplier_number);

        //find the columns of pets we're interested in
        final String mItemName = cursor.getString(cursor.getColumnIndex(StoreEntry.COLUMN_PRODUCT_NAME));
        String mItemPrice = cursor.getString(cursor.getColumnIndex(StoreEntry.COLUMN_PRODUCT_PRICE));
        final String mItemQuantity = cursor.getString(cursor.getColumnIndex(StoreEntry.COLUMN_PRODUCT_QUANTITY));
        final String mItemWeight = cursor.getString(cursor.getColumnIndex(StoreEntry.COLUMN_PRODUCT_WEIGHT));
        String mSupplierName = cursor.getString(cursor.getColumnIndex(StoreEntry.COLUMN_SUPPLIER_NAME));
        final String mSupplierNumber = cursor.getString(cursor.getColumnIndex(StoreEntry.COLUMN_SUPPLIER_NUMBER));

        //get current item id
        int currentId = cursor.getInt(cursor.getColumnIndex(StoreEntry._ID));
        //Create the content Uri for the current Id
        final Uri contentUri = Uri.withAppendedPath(StoreEntry.CONTENT_URI, Integer.toString(currentId));

        //populate fields RecipeFragment
        itemName.setText(mItemName);
        itemPrice.setText(mItemPrice);
        itemQuantity.setText(mItemQuantity);
        itemWeight.setText(mItemWeight);
        supplierName.setText(mSupplierName);
        supplierNumber.setText(mSupplierNumber);


    }
}
