package com.example.jidelnicek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LunchList extends AppCompatActivity {

    private DatabaseController db;
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_list);

        db = DatabaseController.getInstance(getApplicationContext());
        fragmentManager = getSupportFragmentManager();

        Button btn_addLunch = findViewById(R.id.btn_add_lunch);
        btn_addLunch.setOnClickListener(this::addLunch); // Calling method addLunch

        Button btn_selectLunch = findViewById(R.id.btn_select_lunch);
        btn_selectLunch.setOnClickListener(this::selectLunch); // Calling method selectLunch

        Button btn_removeLunch = findViewById(R.id.btn_remove_lunch);
        btn_removeLunch.setOnClickListener(this::removeLunch); // Calling method selectLunch

        // Set viewLunchFragment as a first fragment in lunchListActivity
        ViewLunchFragment viewLunchFragment = ViewLunchFragment.newInstance(db, null);
        fragmentManager.beginTransaction()
                .replace(R.id.fcv_viewLunchFragmentContainer, viewLunchFragment, null)
                .setReorderingAllowed(true)
                .commit();
    }

    public void addLunch(View view){
        AddLunchFragment addLunchFragment = AddLunchFragment.newInstance(db);
        fragmentManager.beginTransaction()
                .replace(R.id.fcv_viewLunchFragmentContainer, addLunchFragment, null)
                .setReorderingAllowed(true)
                .commit();
    }

    public void selectLunch(View view){
        SelectLunchFragment selectLunchFragment = SelectLunchFragment.newInstance(db);
        fragmentManager.beginTransaction()
                .replace(R.id.fcv_viewLunchFragmentContainer, selectLunchFragment, null)
                .setReorderingAllowed(true)
                .commit();
    }

    public void removeLunch(View view){
        RemoveLunchFragment removeLunchFragment = RemoveLunchFragment.newInstance(db);
        fragmentManager.beginTransaction()
                .replace(R.id.fcv_viewLunchFragmentContainer, removeLunchFragment, null)
                .setReorderingAllowed(true)
                .commit();
    }
}