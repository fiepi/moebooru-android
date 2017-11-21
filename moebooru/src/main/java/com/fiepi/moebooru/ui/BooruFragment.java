package com.fiepi.moebooru.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.fiepi.moebooru.R;

import java.util.ArrayList;
import java.util.List;


public class BooruFragment extends Fragment {
    private static final String TAG = BooruFragment.class.getSimpleName();

    public BooruFragment() {
        // Required empty public constructor
    }

    public static BooruFragment newInstance(String param1, String param2) {
        BooruFragment fragment = new BooruFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_booru_view, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private List<String> initData(){
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            data.add("Booru: " + i);
            Log.i(TAG, data.get(i));
        }
        return data;
    }
}
