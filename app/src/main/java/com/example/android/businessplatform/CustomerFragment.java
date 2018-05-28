package com.example.android.businessplatform;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CustomerFragment extends Fragment {

    private PlatformCursorAdapter mCursorAdapter;

    public CustomerFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        mCursorAdapter = new PlatformCursorAdapter(getActivity(), null);
        final View rootView = inflater.inflate(R.layout.recipe_activity, container, false);

        return rootView;
    }
}
