package com.company.jk.pcoordinator.mypage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.company.jk.pcoordinator.MainActivity;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.login.AddressPostActivity;
import com.company.jk.pcoordinator.login.LoginActivity;
import com.company.jk.pcoordinator.login.SignupActivity;

import java.util.ArrayList;
import java.util.List;


public class MyinfoFragment extends Fragment implements View.OnClickListener {

    Context mContext;
    Intent intent;
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


        ImageView iv_back = (ImageView) v.findViewById(R.id.btback);
        EditText et_address = (EditText)v.findViewById(R.id.et_address);



        iv_back.setOnClickListener(this);
        et_address.setOnClickListener(this);




        return  v;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btback:
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                MypageFragment myFragment = new MypageFragment();
                //왼쪽에서 오른쪽 슬라이드
                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.frame, myFragment).addToBackStack(null).commit();
                break;
            case R.id.et_address:
                intent = new Intent(mContext, AddressPostActivity.class);
                startActivity(intent);
                break;
        }
    }
}
