package com.company.jk.pcoordinator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.company.jk.pcoordinator.common.MyActivity;
import com.company.jk.pcoordinator.home.HomeFragment;
import com.company.jk.pcoordinator.login.LoginInfo;
import com.company.jk.pcoordinator.mypage.MypageFragment;
import com.company.jk.pcoordinator.mypage.mybaby.MybabyDetailActivity;
import com.company.jk.pcoordinator.record.RecordFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends MyActivity implements OnTabSelectListener {


    private String url;
    private String TAG = "MainActivity";

    public static BottomBar  bottomBar;
    private FragmentManager manager;
    private HomeFragment homeFragment;
    private RecordFragment recordFragment;
    private MypageFragment mypageFragment;
    private LoginInfo loginInfo = LoginInfo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment();
        recordFragment = new RecordFragment();
        mypageFragment = new MypageFragment();

        manager= getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.addToBackStack(null);
        ft.add(R.id.frame, homeFragment);
        ft.commit();

        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(this);

        Log.i(TAG, "아기정보는 " + loginInfo.getBabyID() );
    }


    @Override
    public void onTabSelected(int tabId) {

        Log.i(TAG, "이번클릭한 메뉴id 는 " + tabId);
        switch (tabId){
            case R.id.bottomBarItemHome:
                if (!homeFragment.isVisible()){
                    replaceFragment(homeFragment);
                }
                break;
            case R.id.bottomBarItemRecord:

                Log.i(TAG, "베이비아이디는 " + loginInfo.getBabyID() );
                if(loginInfo.getBabyID() == 0){  //loginInfo.getBabyID() 는 db 가 int 이므로 기본은 0

                    Intent intent = new Intent(this, MybabyDetailActivity.class);
                    intent.putExtra("email",loginInfo.getEmail() );
                    startActivityForResult(intent, 12);
                    replaceFragment(recordFragment);
                    showToast( getResources().getString(R.string.message_warnning_register_baby));
                }else if (!recordFragment.isVisible()){
                    replaceFragment(recordFragment);
                }
                break;
            case R.id.bottomBarItemPerson:
                if (!mypageFragment.isVisible()){
                    replaceFragment(mypageFragment);
                }
                break;
        }
    }

    // Fragment 변환을 해주기 위한 부분, Fragment의 Instance를 받아서 변경
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment).commit();
    }
//
    @Override
    public void onBackPressed() {

        moveTaskToBack(true);
    }
}


