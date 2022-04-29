package com.example.jidelnicek;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewLunchFragment extends Fragment {

    public static final String DATABASE = "database";
    private static final String LUNCHLIST = "lunchList";
    private static final String ALL = "Všechny";

    private List<Lunch> lunchList;

    private LinearLayout llv_lunchView;

    public ViewLunchFragment() {}

    public static ViewLunchFragment newInstance(DatabaseController db, List<Lunch> lunchList) {
        ViewLunchFragment viewLunchFragment = new ViewLunchFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(LUNCHLIST, (ArrayList<Lunch>) lunchList);
        args.putSerializable(DATABASE, (Serializable) db);
        viewLunchFragment.setArguments(args);

        return viewLunchFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_lunch, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        if (getArguments() != null) {
            DatabaseController db = (DatabaseController) getArguments().getSerializable(DATABASE);
            lunchList = getArguments().getParcelableArrayList(LUNCHLIST);

            if(lunchList == null){
                lunchList = db.select(ALL, -1);
            }
            else if (lunchList.isEmpty()) {
                lunchList = db.select(ALL, -1);
            }
        }

        llv_lunchView = view.findViewById(R.id.llv_lunchView);

        // Create layout header
        List<String> header = Arrays.asList("Id", "Název", "Počet dnů", "Typ");
        createHorizontalLayout(header, true);

        // Create row for each lunch
        for (int i = 0; i < lunchList.size(); i++){
            List<String> columns = Arrays.asList(String.valueOf(lunchList.get(i).getLunch_id()),
                                                lunchList.get(i).getName(),
                                                String.valueOf(lunchList.get(i).getSpan()),
                                                lunchList.get(i).getType());
            createHorizontalLayout(columns, false);
        }

    }

    private void createHorizontalLayout(List<String> columns, boolean isHeader){
        LinearLayout llh_lunchView = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        if(isHeader) {
            lp.setMargins(0, 0, 55, 30);
        }
        else{
            lp.setMargins(0, 0, 55, 10);
        }
        llh_lunchView.setLayoutParams(lp);
        llh_lunchView.setOrientation(LinearLayout.HORIZONTAL);
        llv_lunchView.addView(llh_lunchView);

        for(int i = 0; i < columns.size(); i++){
            TextView tv = new TextView(getContext());
            tv.setText(columns.get(i));
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(20.0f);
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
            lp.setMargins(8,0,8,0);
            tv.setLayoutParams(lp);
            llh_lunchView.addView(tv);
        }
    }
}