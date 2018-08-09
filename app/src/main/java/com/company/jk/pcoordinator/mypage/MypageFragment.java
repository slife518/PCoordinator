package com.company.jk.pcoordinator.mypage;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.company.jk.pcoordinator.R;

public class MypageFragment extends Fragment{
    TextView txCustomerinfo;
    View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_mypage, container, false);

        // BtnOnClickListener의 객체 생성.
        BtnOnClickListener onClickListener = new BtnOnClickListener() ;

        // 각 Button의 이벤트 리스너로 onClickListener 지정.
        Button btn_myinfo = (Button) v.findViewById(R.id.btn_myinfo) ;
        btn_myinfo.setOnClickListener(onClickListener) ;
        Button btn_bodyinfo = (Button) v.findViewById(R.id.btn_bodyinfo) ;
        btn_bodyinfo.setOnClickListener(onClickListener) ;
        Button btn_faq = (Button) v.findViewById(R.id.btn_faq) ;
        btn_faq.setOnClickListener(onClickListener) ;

        return v;
    }


    class BtnOnClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            switch (view.getId()) {
                case R.id.btn_myinfo :
                    MyinfoFragment myFragment = new MyinfoFragment();
                    //왼쪽에서 오른쪽 슬라이드
                    activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.frame, myFragment).addToBackStack(null).commit();
                    break;
                case R.id.btn_bodyinfo :
                    BodyFragment myFragment2 = new BodyFragment();
                    //왼쪽에서 오른쪽 슬라이드
                    activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.frame, myFragment2).addToBackStack(null).commit();
                    break;
                case R.id.btn_faq :
                    FAQFragment myFragment3 = new FAQFragment();
                    //왼쪽에서 오른쪽 슬라이드
                    activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.frame, myFragment3).addToBackStack(null).commit();
                    break;
            } } }

}
