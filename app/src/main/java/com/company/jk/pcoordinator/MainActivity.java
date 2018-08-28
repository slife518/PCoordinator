package com.company.jk.pcoordinator;

import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.company.jk.pcoordinator.bossbaby.BossBabyFragment;
import com.company.jk.pcoordinator.cart.CartFragment;
import com.company.jk.pcoordinator.mypage.MyinfoFragment;
import com.company.jk.pcoordinator.mypage.MypageFragment;
import com.company.jk.pcoordinator.notice.NoticeFragment;
import com.company.jk.pcoordinator.shopping.ShoppingFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {

    BottomBar bottomBar;
    private final  static String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int menuItemId) {
                Log.i(TAG, "이번클릭한 메뉴id 는 " + menuItemId);
                if(menuItemId==R.id.bottomBarItemHome){
                    BossBabyFragment bf = new BossBabyFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, bf).commit();
                }else if(menuItemId==R.id.bottomBarItemRecord){        //기록하기
                    ShoppingFragment sf = new ShoppingFragment();   //쇼핑하기
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame,sf).commit();
                }else if(menuItemId==R.id.bottomBarItemPerson){     //내정보
                    MypageFragment mf = new MypageFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, mf).commit();
                }else if(menuItemId==R.id.bottomBarItemEtc){        //기타공지사항 등.
                    NoticeFragment nf = new NoticeFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, nf).commit();
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

