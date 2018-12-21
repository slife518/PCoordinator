package com.company.jk.pcoordinator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.company.jk.pcoordinator.bossbaby.WebviewFragment;
import com.company.jk.pcoordinator.chart.ChartActivity;
import com.company.jk.pcoordinator.chart.ChartFragment;
import com.company.jk.pcoordinator.home.HomeFragment;
import com.company.jk.pcoordinator.http.UrlPath;
import com.company.jk.pcoordinator.login.LoginInfo;
import com.company.jk.pcoordinator.mypage.MypageFragment;
import com.company.jk.pcoordinator.notice.NoticeFragment;
import com.company.jk.pcoordinator.record.RecordFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity implements OnTabSelectListener {


    private String url;
    private String TAG = "MainActivity";

    public static BottomBar  bottomBar;
    FragmentManager manager;
    HomeFragment homeFragment;
    RecordFragment recordFragment;
    MypageFragment mypageFragment;
    ChartFragment chartFragment;
    ChartActivity chartActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        homeFragment = new HomeFragment();
        recordFragment = new RecordFragment();
        chartFragment = new ChartFragment();
        mypageFragment = new MypageFragment();

        manager= getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.addToBackStack(null);
        ft.add(R.id.frame, homeFragment);
        ft.commit();

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(this);
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
                if (!recordFragment.isVisible()){
                    replaceFragment(recordFragment);
                }
                break;
            case R.id.bottomBarItemChart:
                Intent intent = new Intent(MainActivity.this, ChartActivity.class);
                startActivityForResult(intent, 200);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}


