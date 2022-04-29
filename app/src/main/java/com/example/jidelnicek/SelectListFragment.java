package com.example.jidelnicek;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class SelectListFragment extends Fragment {

    private static final String DATABASE = "database";
    private static int btn_click_num;
    private static final List<Button> btn_list = new ArrayList<>();
    private static final List<TextView> tv_list = new ArrayList<>();
    private static final List<String> lunch_names = new ArrayList<>();

    private DatabaseController db;

    public SelectListFragment() {}

    public static SelectListFragment newInstance(DatabaseController db) {
        SelectListFragment fragment = new SelectListFragment();
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
        return inflater.inflate(R.layout.fragment_select_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (getArguments() != null) {
            db = (DatabaseController) getArguments().getSerializable(DATABASE);
        }
        tv_list.clear();
        btn_list.clear();
        lunch_names.clear();

        tv_list.add(view.findViewById(R.id.tv_mo));
        tv_list.add(view.findViewById(R.id.tv_tu));
        tv_list.add(view.findViewById(R.id.tv_we));
        tv_list.add(view.findViewById(R.id.tv_th));
        tv_list.add(view.findViewById(R.id.tv_fr));
        tv_list.add(view.findViewById(R.id.tv_sa));
        tv_list.add(view.findViewById(R.id.tv_su));

        btn_list.add(view.findViewById(R.id.btn_mo));
        btn_list.get(0).setOnClickListener((v) -> selectLunches(v, 0));
        btn_list.add(view.findViewById(R.id.btn_tu));
        btn_list.get(1).setOnClickListener((v) -> selectLunches(v, 1));
        btn_list.add(view.findViewById(R.id.btn_we));
        btn_list.get(2).setOnClickListener((v) -> selectLunches(v, 2));
        btn_list.add(view.findViewById(R.id.btn_th));
        btn_list.get(3).setOnClickListener((v) -> selectLunches(v, 3));
        btn_list.add(view.findViewById(R.id.btn_fr));
        btn_list.get(4).setOnClickListener((v) -> selectLunches(v, 4));
        btn_list.add(view.findViewById(R.id.btn_sa));
        btn_list.get(5).setOnClickListener((v) -> selectLunches(v, 5));
        btn_list.add(view.findViewById(R.id.btn_su));
        btn_list.get(6).setOnClickListener((v) -> selectLunches(v, 6));


        Button btn_take_screenshot = view.findViewById(R.id.btn_take_screenshot);
        btn_take_screenshot.setOnClickListener(this::takeScreenshot);
    }

    public void selectLunches(View view, int btn_num){
        SelectListFragment.btn_click_num = btn_num;
        ChooseLunchFragment chooseLunchFragment = ChooseLunchFragment.newInstance(db);
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fcv_listFragment, chooseLunchFragment, null)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    public void takeScreenshot(View view) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        View v1 = getActivity().getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);

        if(((MainActivity)this.getActivity()).saveImageToStorage(now.toString(), bitmap)){
            Toast.makeText(requireContext(), "Obrázek byl uložen!", Toast.LENGTH_SHORT).show();
        }

    }

    public static boolean setWidgets(List<Lunch> lunch_list, Context c, Lunch oneChosenLunch){
        Toast failToast = Toast.makeText(c, "Vybraný oběd je v jídelníčku již zastoupen!", Toast.LENGTH_SHORT);
        if(oneChosenLunch != null ){
            if(lunch_names.contains(oneChosenLunch.getName())){
                failToast.show();
                return false;
            }
            setLunch(oneChosenLunch);
        }
        else if(lunch_list != null && !lunch_list.isEmpty()){
            Random rand = new Random();

            Lunch chosenLunch = lunch_list.get(rand.nextInt(lunch_list.size()));

            int counter = 0;
            while(lunch_names.contains(chosenLunch.getName())) {
                chosenLunch = lunch_list.get(rand.nextInt(lunch_list.size()));
                // Should not happen - but rather avoid endless loop
                if(counter > 99){
                    failToast.show();
                    return false;
                }
                counter++;
            }
            if(lunch_names.contains(chosenLunch.getName())){
                return false;
            }
            setLunch(chosenLunch);
        }
        else{
            Toast.makeText(c, "Žádný oběd nenalezen!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private static void setLunch(Lunch lunch){
        String lunch_name = lunch.getName();
        if (lunch_names.size() < btn_list.size()) {
            lunch_names.add(lunch_name);
            if (lunch.getSpan() == 2 && lunch_names.size() < btn_list.size()) {
                lunch_names.add(lunch_name);
            }
        } else {
            lunch_names.set(btn_click_num, lunch_name);
            if (lunch.getSpan() == 2 && btn_click_num + 1 < lunch_names.size()) {
                lunch_names.set(btn_click_num + 1, lunch_name);
            }
        }

        tv_list.get(btn_click_num).setText(lunch_name);
        if (lunch.getSpan() == 2 && btn_click_num + 1 < btn_list.size()) {
            tv_list.get(btn_click_num + 1).setText(lunch_name);
        }
    }
}