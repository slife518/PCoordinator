package com.company.jk.pcoordinator;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.company.jk.pcoordinator.bossbaby.WebviewFragment;
import com.company.jk.pcoordinator.http.UrlPath;
import com.company.jk.pcoordinator.login.LoginInfo;
import com.company.jk.pcoordinator.mypage.MypageFragment;
import com.company.jk.pcoordinator.notice.NoticeFragment;
import com.company.jk.pcoordinator.record.RecordFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";
    BottomBar bottomBar;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int menuItemId) {
                Log.i(TAG, "이번클릭한 메뉴id 는 " + menuItemId);
                if (menuItemId == R.id.bottomBarItemHome) {
                    WebviewFragment bf = new WebviewFragment();
                    UrlPath urlPath = new UrlPath();
                    LoginInfo loginInfo = LoginInfo.getInstance();
                    url = urlPath.getUrlPath() + "auth/directLogin/" + loginInfo.getEmail() + "/" + loginInfo.getPassword();
                    bf.setUrl(url);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, bf).commit();
                } else if (menuItemId == R.id.bottomBarItemRecord) {        //기록하기
//                    WebviewFragment bf = new WebviewFragment();
//                    UrlPath urlPath = new UrlPath();
//                    LoginInfo loginInfo = LoginInfo.getInstance();
//                    url = urlPath.getUrlPath() + "native/record/newRecord";
//                    bf.setUrl(url);
//                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, bf).commit();
                    RecordFragment rf = new RecordFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, rf).commit();
                } else if (menuItemId == R.id.bottomBarItemPerson) {     //내정보
                    MypageFragment mf = new MypageFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, mf).commit();
                } else if (menuItemId == R.id.bottomBarItemChart) {        //기타공지사항 등.
                    WebviewFragment bf = new WebviewFragment();
                    UrlPath urlPath = new UrlPath();
                    LoginInfo loginInfo = LoginInfo.getInstance();
                    url = urlPath.getUrlPath() + "native/auth/directLogin/" + loginInfo.getEmail() + "/" + loginInfo.getPassword();
                    bf.setUrl(url);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, bf).commit();
//                }else if(menuItemId==R.id.bottomBarItemHome){       //보스베이비;
//                    CartFragment cf = new CartFragment();
//                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, cf).commit();
                }
            }

        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
            }
        });
    }
}

