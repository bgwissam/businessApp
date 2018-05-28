package com.example.android.businessplatform;

import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private static final String LOG_TAG = MainActivity.class.getName();
    //create a drawer layout
    private DrawerLayout mDrawerLayout;
    //create a view pager
    private ViewPager mViewPager;
    //create a variable to find the fragemnt position
    private int position = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setNavigationViewListener();

        //set the drawer layout and navigation view
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //set item selected persist highlight
                menuItem.setChecked(true);
                //close drawer
                mDrawerLayout.closeDrawers();
                //set action as per the item selected
                return true;
            }
        });
        //create an adapter that knows where each fragment is
        final LocationFragmentAdapter adapter = new LocationFragmentAdapter(this, getSupportFragmentManager());
        mViewPager = findViewById(R.id.viewPager);
        mViewPager.setAdapter(adapter);
        adapter.getItem(position);
        //add the page listener
        mViewPager.addOnPageChangeListener(listener);
        //set the function for the floating button to add a new item
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(position == 0) {
                        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                        startActivity(intent);
                    }
                    else if (position == 1) {
                        Intent intent = new Intent(MainActivity.this, RecipeEditor.class);
                        startActivity(intent);
                    }
            }
        });

    }


    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int mPosition) {
            position = mPosition;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void setNavigationViewListener(){
        NavigationView navigationView = findViewById(R.id.nav_view);

        if(navigationView!=null){
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        //set Fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();
        //set a switch statement to allow navigation between items of the drawer
        switch (item.getItemId()){
            case R.id.nav_inventory: {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new InventoryFragment()).commit();
                break;
            }
            case R.id.nav_product:{
                fragmentManager.beginTransaction().replace(R.id.content_frame, new RecipeFragment()).commit();
            }
            case R.id.nav_customer: {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new CustomerFragment()).commit();
                break;
            }
            case R.id.nav_order:{
                fragmentManager.beginTransaction().replace(R.id.content_frame, new OrderFragment()).commit();
                break;
            }
        }
        return false;
    }

}
