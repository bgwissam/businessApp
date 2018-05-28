package com.example.android.businessplatform;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class LocationFragmentAdapter extends FragmentPagerAdapter {

    final int count = 4;
    private int [] tabTitle = {R.string.inventory_platform, R.string.product_platform, R.string.customer_platform, R.string.order_platform};
    private Context mContext;

    public LocationFragmentAdapter(Context context, FragmentManager fm){
        super(fm);
        mContext = context;
    }
    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            return new InventoryFragment();
        }else if(position == 1) {
            return new RecipeFragment();
        }else if(position == 2) {
            return new CustomerFragment();
        }else {
            return new OrderFragment();
        }

    }

    @Override
    public int getCount() {
        return count;
    }

    //to place the title of each tab when selected
    @Override
    public CharSequence getPageTitle(int position){
        if(position == 0) {
            return mContext.getString(R.string.inventory_platform);
        }else if(position == 1) {
            return mContext.getString(R.string.product_platform);
        }else if(position == 2) {
            return mContext.getString(R.string.customer_platform);
        }else {
            return mContext.getString(R.string.order_platform);
        }
    }
}
