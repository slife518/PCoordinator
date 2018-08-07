package com.company.jk.pcoordinator.myinfo;

import android.content.Context;
import android.net.Uri;
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
        Spinner spinner = (Spinner) v.findViewById(R.id.spinner_size_top);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
//            public void onItemSelected(AdapterView<?> parent, View view,
//                                       int position, long id) {
//                Toast.makeText(mContext, "선택하신건은" + position, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                Toast.makeText(mContext, "선택하지 않았습니다.", Toast.LENGTH_SHORT).show();
//            }


        // Spinner Drop down elements
        List<String> categories = new ArrayList <String>();
        categories.add("90");
        categories.add("95");
        categories.add("100");
        categories.add("105");
        categories.add("110");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        return  v;
    }


    @Override
    public void onItemSelected(AdapterView parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
            Toast.makeText(mContext, "선택하신건은" + item , Toast.LENGTH_SHORT).show();


    }

    public void onNothingSelected(AdapterView arg0) {
            Toast.makeText(mContext, "선택하지 않았습니다.", Toast.LENGTH_SHORT).show();

    }

}
