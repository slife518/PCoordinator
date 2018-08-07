package com.company.jk.pcoordinator.mypage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.company.jk.pcoordinator.R;

import java.util.ArrayList;
import java.util.List;


public class MyinfoFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Context mContext;
    View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_myinfo, container, false);
        mContext = getActivity();

        // Spinner element
        Spinner spinner_sizetop = (Spinner) v.findViewById(R.id.spinner_size_top);
        Spinner spinner_sex = (Spinner) v.findViewById(R.id.spinner_sex);

        // Spinner click listener
        spinner_sizetop.setOnItemSelectedListener(this);
        spinner_sex.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> mantopsize = new ArrayList <String>();
        List<String> sex = new ArrayList <String>();
        //categories.add("90");
        mantopsize.add("95");
        mantopsize.add("100");
        mantopsize.add("105");
        mantopsize.add("110");

        sex.add("남");
        sex.add("여");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, mantopsize);
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, sex);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_sizetop.setAdapter(dataAdapter);
        spinner_sex.setAdapter(dataAdapter1);


        return  v;
    }


    @Override
    public void onItemSelected(AdapterView parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
            Toast.makeText(mContext, id+"선택하신건은" + item , Toast.LENGTH_SHORT).show();


    }

    public void onNothingSelected(AdapterView arg0) {
            Toast.makeText(mContext, "선택하지 않았습니다.", Toast.LENGTH_SHORT).show();

    }

}
