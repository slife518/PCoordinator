package com.company.jk.pcoordinator.mypage;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.company.jk.pcoordinator.ParentsActivity;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.MyFragment;
import com.company.jk.pcoordinator.login.LoginActivity;
import com.company.jk.pcoordinator.mypage.mybaby.MybabyActivity;

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


        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        Toolbar myToolbar = (Toolbar)v.findViewById(R.id.my_toolbar);
        activity.setSupportActionBar(myToolbar);
        myToolbar.setTitle(R.string.mypage);


        // BtnOnClickListener의 객체 생성.
        BtnOnClickListener onClickListener = new BtnOnClickListener() ;

        // 각 Button의 이벤트 리스너로 onClickListener 지정.
        Button btn_myinfo = (Button) v.findViewById(R.id.btn_myinfo) ;
        btn_myinfo.setOnClickListener(onClickListener) ;
        Button btn_mybabyinfo = (Button) v.findViewById(R.id.btn_mybabyinfo) ;
        btn_mybabyinfo.setOnClickListener(onClickListener) ;
        Button btn_parentslist = (Button) v.findViewById(R.id.btn_parentslist) ;
        btn_parentslist.setOnClickListener(onClickListener) ;

        Button btn_password = (Button) v.findViewById(R.id.btn_password) ;
        btn_password.setOnClickListener(onClickListener) ;
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
                    break;
                case R.id.btn_password :    //비밀번호관리
                    intent = new Intent(getActivity(), PasswordActivity.class);
                    startActivityForResult(intent, 300);
                    break;
                case R.id.btn_mybabyinfo:   //아기정보관리
                    intent = new Intent(getActivity(), MybabyActivity.class);
                    startActivityForResult(intent, 300);
                    break;
                case R.id.btn_parentslist: //보호자명단
                    intent = new Intent(getActivity(), ParentsActivity.class);
                    startActivityForResult(intent, 300);
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
