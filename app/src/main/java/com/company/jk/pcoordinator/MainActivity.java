package com.company.jk.pcoordinator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.company.jk.pcoordinator.cart.CartFragment;
import com.company.jk.pcoordinator.myinfo.MyinfoFragment;
import com.company.jk.pcoordinator.notice.NoticeFragment;
import com.company.jk.pcoordinator.shopping.ShoppingFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.OnMenuTabClickListener;

public class MainActivity extends AppCompatActivity {

    BottomBar bottomBar;
    private final  static String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomBar = BottomBar.attach(this,savedInstanceState);
        bottomBar.setItemsFromMenu(R.menu.menu_main, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(int menuItemId) {
                Log.i(TAG, "이번클릭한 메뉴id 는 " + menuItemId);
                if(menuItemId==R.id.bottomBarItemHome){
                    ShoppingFragment sf = new ShoppingFragment();   //쇼핑하기
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame,sf).commit();
                }else if(menuItemId==R.id.bottomBarItemCart){        //배송정보
                    CartFragment cf = new CartFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, cf).commit();
                }else if(menuItemId==R.id.bottomBarItemPerson){     //내정보
                    MyinfoFragment mf = new MyinfoFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, mf).commit();
                }else if(menuItemId==R.id.bottomBarItemEtc){        //기타공지사항 등.
                    NoticeFragment nf = new NoticeFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, nf).commit();
                }
            }

            @Override
            public void onMenuTabReSelected(int menuItemId) {
                Log.i(TAG, "다시 클릭한 메뉴id 는 " + menuItemId);
            }

        });

        bottomBar.mapColorForTab(0, "#CCCCCC");
        bottomBar.mapColorForTab(1, "#CCCCCC");
        bottomBar.mapColorForTab(2, "#CCCCCC");
        bottomBar.mapColorForTab(3, "#CCCCCC");
        bottomBar.setActiveTabColor("#000000");
        BottomBarBadge unread;
        unread = bottomBar.makeBadgeForTabAt(3, "#FF0000", 13);
        unread.show();

    }
}
