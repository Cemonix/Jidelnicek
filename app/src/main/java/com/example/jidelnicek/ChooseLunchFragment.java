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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChooseLunchFragment extends Fragment {

    public static final String DATABASE = "database";
    public static final String ALL = "Všechny";

    private DatabaseController db;
    private Spinner spinn_lunch_names_list;
    private Spinner spinn_lunch_type_list;
    private Spinner spinn_day_num_list;

    public ChooseLunchFragment() {}

    public static ChooseLunchFragment newInstance(DatabaseController db){
        ChooseLunchFragment ChooseLunchFragment = new ChooseLunchFragment();
        Bundle args = new Bundle();
        args.putSerializable(DATABASE, (Serializable) db);
        ChooseLunchFragment.setArguments(args);

        return ChooseLunchFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_lunch, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        spinn_lunch_names_list = view.findViewById(R.id.spinn_lunch_names_list);
        spinn_lunch_type_list = view.findViewById(R.id.spinn_lunch_type_list);
        spinn_day_num_list = view.findViewById(R.id.spinn_day_num_list);

        if (getArguments() != null) {
            db = (DatabaseController) getArguments().getSerializable(DATABASE);
        }

        Button btn_lunch_select_list = view.findViewById(R.id.btn_lunch_select_list);
        btn_lunch_select_list.setOnClickListener(this::selectLunch);

        setSpinnerAdapters();
    }

    public void selectLunch(View view){
        List<Lunch> lunch_list;
        String chosenName = spinn_lunch_names_list.getItemAtPosition(spinn_lunch_names_list.getSelectedItemPosition()).toString();
        String chosenType = spinn_lunch_type_list.getItemAtPosition(spinn_lunch_type_list.getSelectedItemPosition()).toString();
        String chosenDay = spinn_day_num_list.getItemAtPosition(spinn_day_num_list.getSelectedItemPosition()).toString();

        Lunch oneChosenLunch = null;
        if(!chosenName.equals(ALL)){
            lunch_list = db.select(ALL, -1);
            for (Lunch lunch : lunch_list) {
                if(lunch.getName().equals(chosenName)){
                    oneChosenLunch = lunch;
                    break;
                }
            }
        }
        else {
            int chosenDayInt = -1;

            if (chosenDay.equals("")) {
                Toast.makeText(getContext(), "Zvolte počet dnů!", Toast.LENGTH_SHORT).show();
            } else {
                if (chosenDay.equals("Jeden")) {
                    chosenDayInt = 1;
                } else if (chosenDay.equals("Dva")) {
                    chosenDayInt = 2;
                }
            }

            lunch_list = db.select(chosenType, chosenDayInt);
        }


        FragmentManager fragmentManager = getParentFragmentManager();
        if (fragmentManager.getBackStackEntryCount() >= 0) {
            SelectListFragment.setWidgets(lunch_list, getContext(), oneChosenLunch);
            fragmentManager.popBackStack();
        }
    }

    private void setSpinnerAdapters(){
        List<Lunch> lunchList = db.select(ALL, -1); // -1 -> all days

        List<String> lunchNames = new ArrayList<>();
        lunchNames.add(ALL); // All names of lunch

        List<String> lunchTypes = new ArrayList<>();
        lunchTypes.add(ALL); // All types of lunch

        List<String> days = new ArrayList<>();
        days.add("Všechny");
        days.add("Jeden");
        days.add("Dva");

        for (Lunch lunch : lunchList) {
            if (!lunchNames.contains(lunch.getName()))
                lunchNames.add(lunch.getName());
            if (!lunchTypes.contains(lunch.getType()))
                lunchTypes.add(lunch.getType());
        }

        ArrayAdapter<String> spinnerArrayAdapterNames = new ArrayAdapter<>(getContext(),
                R.layout.spinner_item, lunchNames);

        ArrayAdapter<String> spinnerArrayAdapterTypes = new ArrayAdapter<>(getContext(),
                R.layout.spinner_item, lunchTypes);

        ArrayAdapter<String> spinnerArrayAdapterDays = new ArrayAdapter<>(getContext(),
                R.layout.spinner_item, days);

        spinn_lunch_names_list.setAdapter(spinnerArrayAdapterNames);
        spinn_lunch_type_list.setAdapter(spinnerArrayAdapterTypes);
        spinn_day_num_list.setAdapter(spinnerArrayAdapterDays);
    }
}