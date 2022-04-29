package com.example.jidelnicek;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SelectLunchFragment extends Fragment {

    public static final String DATABASE = "database";
    public static final String ALL = "Všechny";

    private DatabaseController db;
    private Spinner spinn_lunch_type;
    private RadioGroup rbg_days;
    private RadioButton rb_one_day, rb_two_days;

    public SelectLunchFragment() {}

    public static SelectLunchFragment newInstance(DatabaseController db){
        SelectLunchFragment selectLunchFragment = new SelectLunchFragment();
        Bundle args = new Bundle();
        args.putSerializable(DATABASE, (Serializable) db);
        selectLunchFragment.setArguments(args);

        return selectLunchFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lunch_select, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        spinn_lunch_type = view.findViewById(R.id.spinn_lunch_type);
        rbg_days = view.findViewById(R.id.rbg_lunch_span);
        rb_one_day = view.findViewById(R.id.rb_one_day);
        rb_two_days = view.findViewById(R.id.rb_two_days);

        if (getArguments() != null) {
            db = (DatabaseController) getArguments().getSerializable(DATABASE);
        }

        Button btn_lunch_select = view.findViewById(R.id.btn_lunch_select);
        btn_lunch_select.setOnClickListener(this::selectLunch);

        Button btn_select_back = view.findViewById(R.id.btn_select_back);
        btn_select_back.setOnClickListener(this::back);

        setSpinnerAdapter();

    }

    public void selectLunch(View view){
        List<Lunch> lunchList;
        String chosenType = spinn_lunch_type.getItemAtPosition(spinn_lunch_type.getSelectedItemPosition()).toString();
        int chosenDay = -1;

        if(rbg_days.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(getContext(), "Zvolte počet dnů!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(rb_one_day.isChecked()){
                chosenDay = 1;
            }
            else if(rb_two_days.isChecked()){
                chosenDay = 2;
            }
        }

        lunchList = db.select(chosenType, chosenDay);

        if(lunchList.isEmpty()){
            Toast.makeText(getContext(), "Žádné výsledky pro vybrané hodnoty!", Toast.LENGTH_LONG).show();
        }

        ViewLunchFragment viewLunchFragment = ViewLunchFragment.newInstance(db, lunchList);
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fcv_viewLunchFragmentContainer, viewLunchFragment, null)
                .setReorderingAllowed(true)
                .commit();
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

        List<String> lunchTypes = new ArrayList<>();
        lunchTypes.add(ALL); // All types of lunch

        for (Lunch lunch : lunchList) {
            if (!lunchTypes.contains(lunch.getType()))
                lunchTypes.add(lunch.getType());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getContext(),
                R.layout.spinner_item, lunchTypes);

        spinn_lunch_type.setAdapter(spinnerArrayAdapter);
    }
}