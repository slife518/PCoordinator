package com.company.jk.pcoordinator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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

        manager= getSupportFragmentManager();
        homeFragment = new HomeFragment();
        recordFragment = new RecordFragment();
        chartFragment = new ChartFragment();
        mypageFragment = new MypageFragment();

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
        if (tabId == R.id.bottomBarItemHome) {
            if (!homeFragment.isVisible()){
                FragmentTransaction ft = manager.beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.frame, homeFragment);
                ft.commit();
            }

        } else if (tabId == R.id.bottomBarItemRecord) {        //기록하기
            if (!recordFragment.isVisible()){
                manager.beginTransaction().addToBackStack(null).replace(R.id.frame, recordFragment).commit();
            }
        } else if (tabId == R.id.bottomBarItemChart) {       //레포트
//            if (!chartFragment.isVisible()){
//                manager.beginTransaction().addToBackStack(null).replace(R.id.frame, chartFragment).commit();
//            }
            Intent intent = new Intent(MainActivity.this, ChartActivity.class);
            startActivityForResult(intent, 200);
        } else if (tabId == R.id.bottomBarItemPerson) {     //내정보
            if (!mypageFragment.isVisible()){
                manager.beginTransaction().addToBackStack(null).replace(R.id.frame, mypageFragment).commit();
            }

//            WebviewFragment webviewFragment = new WebviewFragment();
//            UrlPath urlPath = new UrlPath();
//            LoginInfo loginInfo = LoginInfo.getInstance();
//            url = urlPath.getUrlPath() + "native/auth/directLogin/" + loginInfo.getEmail() + "/" + loginInfo.getPassword();
//            webviewFragment.setUrl(url);
//            getSupportFragmentManager().beginTransaction().replace(R.id.frame, webviewFragment).commit();
//                }else if(menuItemId==R.id.bottomBarItemHome){       //보스베이비;
//                    CartFragment cf = new CartFragment();
//                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, cf).commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}


