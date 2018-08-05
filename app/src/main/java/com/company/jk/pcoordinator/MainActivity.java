package com.company.jk.pcoordinator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.company.jk.pcoordinator.cart.CartFragment;
import com.company.jk.pcoordinator.myinfo.MyinfoFragment;
import com.company.jk.pcoordinator.notice.NoticeFragment;
import com.company.jk.pcoordinator.shopping.ShoppingFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.OnMenuTabClickListener;

public class MainActivity extends AppCompatActivity {

    BottomBar bottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomBar = BottomBar.attach(this,savedInstanceState);
        bottomBar.setItemsFromMenu(R.menu.menu_main, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(int menuItemId) {
                if(menuItemId==R.id.bottomBarItemHome){
                    ShoppingFragment nf = new ShoppingFragment();   //쇼핑하기
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame,nf).commit();
                }else if(menuItemId==R.id.bottomBarItemCart){        //배송정보
                    CartFragment nf = new CartFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, nf).commit();
                }else if(menuItemId==R.id.bottomBarItemPerson){     //내정보
                    MyinfoFragment nf = new MyinfoFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, nf).commit();
                }else if(menuItemId==R.id.bottomBarItemEtc){        //기타공지사항 등.
                    NoticeFragment nf = new NoticeFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, nf).commit();
                }
            }

            @Override
            public void onMenuTabReSelected(int menuItemId) {

            }
        });

        bottomBar.mapColorForTab(0, "#F44336");
        bottomBar.mapColorForTab(1, "#D1C4E9");
        bottomBar.mapColorForTab(2, "#E91E63");
        BottomBarBadge unread;
        unread = bottomBar.makeBadgeForTabAt(3, "#FF0000", 13);
        unread.show();

    }
}
