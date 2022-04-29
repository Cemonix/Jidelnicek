package com.example.jidelnicek;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.Serializable;

public class AddLunchFragment extends Fragment {

    public static final String DATABASE = "database";

    private DatabaseController db;
    private EditText pt_lunch_name, pt_lunch_type;
    private RadioButton rb_two_days;

    public AddLunchFragment() {}

    public static AddLunchFragment newInstance(DatabaseController db){
        AddLunchFragment addLunchFragment = new AddLunchFragment();
        Bundle args = new Bundle();
        args.putSerializable(DATABASE, (Serializable) db);
        addLunchFragment.setArguments(args);

        return addLunchFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_lunch, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        pt_lunch_name = view.findViewById(R.id.pt_lunch_name);
        pt_lunch_type = view.findViewById(R.id.pt_lunch_type);
        rb_two_days = view.findViewById(R.id.rb_two_days);

        if (getArguments() != null) {
            db = (DatabaseController) getArguments().getSerializable(DATABASE);
        }

        Button btn_lunch_insert = view.findViewById(R.id.btn_lunch_insert);
        btn_lunch_insert.setOnClickListener(this::insertLunch);

        Button btn_add_back = view.findViewById(R.id.btn_add_back);
        btn_add_back.setOnClickListener(this::back);
    }

    public void insertLunch(View view){
        int days = rb_two_days.isChecked() ? 2 : 1;
        String lunch_name = pt_lunch_name.getText().toString();
        String lunch_type = pt_lunch_type.getText().toString();

        if(lunch_name.equals("") || lunch_type.equals("")) {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Fill in the blanks!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        }
        else {
            Lunch lunch = new Lunch(-1, lunch_name, days, lunch_type);

            if (db != null) {
                String insertedMessage = db.insert(lunch) ? "Přidání proběhlo úspěšně." :
                        "Přidání se nezdařilo.";
                Toast.makeText(view.getContext(), insertedMessage, Toast.LENGTH_SHORT).show();
            }
            else{
                Log.e("Database error", "Database is null!");
            }
            back(view);
        }
    }

    public void back(View view){
        ViewLunchFragment viewLunchFragment = ViewLunchFragment.newInstance(db, null);
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fcv_viewLunchFragmentContainer, viewLunchFragment, null)
                .setReorderingAllowed(true)
                .commit();
    }
}