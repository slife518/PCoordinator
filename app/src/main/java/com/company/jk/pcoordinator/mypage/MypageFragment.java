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
        TextView btn_myinfo = v.findViewById(R.id.tv_myinfo) ;
        btn_myinfo.setOnClickListener(onClickListener) ;
        TextView btn_mybabyinfo = v.findViewById(R.id.tv_mybabyinfo) ;
        btn_mybabyinfo.setOnClickListener(onClickListener) ;
        TextView btn_parentslist = v.findViewById(R.id.tv_parentslist) ;
        btn_parentslist.setOnClickListener(onClickListener) ;

        TextView btn_password = v.findViewById(R.id.tv_password) ;
        btn_password.setOnClickListener(onClickListener) ;
        TextView btn_logout = v.findViewById(R.id.tv_Logout);
        btn_logout.setOnClickListener(onClickListener);

        return v;
    }


    class BtnOnClickListener implements Button.OnClickListener {

        Intent intent;
        @Override
        public void onClick(View view) {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            switch (view.getId()) {
                case R.id.tv_myinfo :
                    intent = new Intent(getActivity(), MyinfoActivity.class);
                    startActivityForResult(intent, 300);
                    break;
                case R.id.tv_password :    //비밀번호관리
                    intent = new Intent(getActivity(), PasswordActivity.class);
                    startActivityForResult(intent, 300);
                    break;
                case R.id.tv_mybabyinfo:   //아기정보관리
                    intent = new Intent(getActivity(), MybabyActivity.class);
                    startActivityForResult(intent, 300);
                    break;
                case R.id.tv_parentslist: //보호자명단
                    intent = new Intent(getActivity(), ParentsActivity.class);
                    startActivityForResult(intent, 300);
                    break;
                case R.id.tv_Logout:
                    showLogout();
                    break;
            } } }



    void showLogout()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle(R.string.logout);
        builder.setMessage(R.string.logout_message);
        builder.setPositiveButton(R.string.logout,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences mPreference =  getActivity().getSharedPreferences("pcoordinator", MODE_PRIVATE);
                        SharedPreferences.Editor editor = mPreference.edit();
                        editor.clear().commit();

                        //Toast.makeText(context.getApplicationContext(),"예를 선택했습니다.",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getContext(), LoginActivity.class );
                        getActivity().startActivity(intent);
                        getActivity().finish();;


                    }
                });
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getContext().getApplicationContext(),R.string.cancel_message,Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }
}
