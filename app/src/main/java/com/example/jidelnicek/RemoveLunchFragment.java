package com.example.jidelnicek;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RemoveLunchFragment extends Fragment {

    private static final String DATABASE = "database";
    private static final String ALL = "Všechny";

    private DatabaseController db;

    private Spinner spinn_lunch_id;

    public RemoveLunchFragment() {}

    public static RemoveLunchFragment newInstance(DatabaseController db) {
        RemoveLunchFragment fragment = new RemoveLunchFragment();
        Bundle args = new Bundle();
        args.putSerializable(DATABASE, db);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_remove_lunch, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        spinn_lunch_id = view.findViewById(R.id.spinn_lunch_id);

        if (getArguments() != null) {
            db = (DatabaseController) getArguments().getSerializable(DATABASE);
        }

        Button btn_remove_confirm_lunch = view.findViewById(R.id.btn_remove_confirm_lunch);
        btn_remove_confirm_lunch.setOnClickListener(this::removeLunch);

        Button btn_remove_back = view.findViewById(R.id.btn_remove_back);
        btn_remove_back.setOnClickListener(this::back);

        setSpinnerAdapter();
    }

    public void removeLunch(View view){
        String chosenName =  spinn_lunch_id.getItemAtPosition(spinn_lunch_id.getSelectedItemPosition()).toString();

        String removedMessage = db.remove(chosenName) ? "Odebrání proběhlo úspěšně." :
                "Odebrání se nezdařilo.";
        Toast.makeText(view.getContext(), removedMessage, Toast.LENGTH_SHORT).show();

        back(view);
    }

    public void back(View view){
        ViewLunchFragment viewLunchFragment = ViewLunchFragment.newInstance(db, null);
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fcv_viewLunchFragmentContainer, viewLunchFragment, null)
                .setReorderingAllowed(true)
                .commit();
    }

    private void setSpinnerAdapter(){
        List<Lunch> lunchList = db.select(ALL, -1); // -1 -> all days

        List<String> lunchName = new ArrayList<>();

        for (Lunch lunch : lunchList) {
            if (!lunchName.contains(lunch.getName())) // Ids are unique but better safe than sorry
                lunchName.add(lunch.getName());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getContext(),
                R.layout.spinner_item, lunchName);

        spinn_lunch_id.setAdapter(spinnerArrayAdapter);
    }

}