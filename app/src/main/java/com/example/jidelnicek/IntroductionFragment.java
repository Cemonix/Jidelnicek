package com.example.jidelnicek;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.Serializable;

public class IntroductionFragment extends Fragment {

    public static final String DATABASE = "database";

    private DatabaseController db;

    public IntroductionFragment() { }

    public static IntroductionFragment newInstance(DatabaseController db) {
        IntroductionFragment introductionFragment = new IntroductionFragment();
        Bundle args = new Bundle();
        args.putSerializable(DATABASE, (Serializable) db);
        introductionFragment.setArguments(args);
        return introductionFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_introduction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        if (getArguments() != null) {
            db = (DatabaseController) getArguments().getSerializable(DATABASE);
        }
        Button btn_create_list = view.findViewById(R.id.btn_create_list);
        btn_create_list.setOnClickListener(this::createList); // Calling method createList

        Button btn_lunch_db_activity = view.findViewById(R.id.btn_lunch_list);
        btn_lunch_db_activity.setOnClickListener(this::lunchDbActivity);

    }

    public void createList(View view){
        SelectListFragment selectListFragment = SelectListFragment.newInstance(db);
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fcv_listFragment, selectListFragment, null)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    public void lunchDbActivity(View view){
        Intent intent = new Intent(getContext(), LunchList.class);
        startActivity(intent);
    }
}