package com.company.jk.pcoordinator.mypage;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.company.jk.pcoordinator.MainActivity;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.MyFragment;
import com.company.jk.pcoordinator.http.NetworkUtil;
import com.company.jk.pcoordinator.login.LoginActivity;
import com.company.jk.pcoordinator.mypage.mybaby.MybabyActivity;
import com.company.jk.pcoordinator.mypage.mybaby.MybabyFragment;

import static android.content.Context.MODE_PRIVATE;

public class MypageFragment extends MyFragment {

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
        Button btn_mybabyinfo = (Button) v.findViewById(R.id.btn_mybabyinfo) ;
        btn_mybabyinfo.setOnClickListener(onClickListener) ;
        Button btn_password = (Button) v.findViewById(R.id.btn_password) ;
        btn_password.setOnClickListener(onClickListener) ;
//        Button btn_bodyinfo = (Button) v.findViewById(R.id.btn_bodyinfo) ;
//        btn_bodyinfo.setOnClickListener(onClickListener) ;
        Button btn_logout = (Button) v.findViewById(R.id.btn_Logout);
        btn_logout.setOnClickListener(onClickListener);

        return v;
    }


    class BtnOnClickListener implements Button.OnClickListener {

        Intent intent;
        @Override
        public void onClick(View view) {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            switch (view.getId()) {
                case R.id.btn_myinfo :
                    intent = new Intent(getActivity(), MyinfoActivity.class);
                    startActivityForResult(intent, 300);
//                    MyinfoFragment myFragment = new MyinfoFragment();
//                    //왼쪽에서 오른쪽 슬라이드
//                    activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.frame, myFragment).addToBackStack(null).commit();
                    break;
                case R.id.btn_password :    //비밀번호관리
                    PasswordFragment myFragment2 = new PasswordFragment();
                    //왼쪽에서 오른쪽 슬라이드
                    activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.frame, myFragment2).addToBackStack(null).commit();
                    break;
                case R.id.btn_mybabyinfo:   //아기정보관리
                    intent = new Intent(getActivity(), MybabyActivity.class);
                    startActivityForResult(intent, 300);
//                    MybabyFragment mybabyFragment = new MybabyFragment();
//                    activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.frame, mybabyFragment).addToBackStack(null).commit();
                    break;
                case R.id.btn_bodyinfo :
                    BodyFragment myFragment3 = new BodyFragment();
                    //왼쪽에서 오른쪽 슬라이드
                    activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.frame, myFragment3).addToBackStack(null).commit();
                    break;
                case R.id.btn_Logout:
                    showLogout();
                    break;
            } } }



    void showLogout()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("로그아웃");
        builder.setMessage("로그아웃 하시겠습니까");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences mPreference =  getActivity().getSharedPreferences("pcoordinator", MODE_PRIVATE);
                        SharedPreferences.Editor editor = mPreference.edit();
                        editor.clear().commit();

                        //Toast.makeText(context.getApplicationContext(),"예를 선택했습니다.",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getContext(), LoginActivity.class );
                        getActivity().startActivity(intent);
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext().getApplicationContext(),"취소되었습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }
}
