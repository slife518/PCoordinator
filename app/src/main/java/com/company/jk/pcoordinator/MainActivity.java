package com.company.jk.pcoordinator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.company.jk.pcoordinator.board.talk.TalkActivity;
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
    public static FloatingActionButton fab;
    private FragmentManager manager;
    private HomeFragment homeFragment;
    private RecordFragment recordFragment;
    private MypageFragment mypageFragment;
    private LoginInfo loginInfo = LoginInfo.getInstance();
    SharedPreferences mPreference;

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

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TalkActivity.class);
                startActivityForResult(intent, 300);
            }
        });

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
                fab.show();
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
                fab.hide();
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

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Log.i(TAG, "다시 시작되서 로깅정보가져옴");
//        auto_login();
//    }
//
//    private void auto_login(){
//
//        mPreference = getSharedPreferences("pcoordinator", MODE_PRIVATE);
//        String id = mPreference.getString("Email", "");
//        String pass = mPreference.getString("Password", "");
//
//        loginInfo.setEmail(id);
//        loginInfo.setPassword(pass);
//
//        MyDataTransaction dataTransaction = new MyDataTransaction(getApplicationContext());
//        Map<String, String> params = new HashMap<>();
//        params.put("email", loginInfo.getEmail());
//        params.put("password", loginInfo.getPassword());
//
//        VolleyCallback callback = new VolleyCallback() {
//            @Override
//            public void onSuccessResponse(String result, int method) {
//                if (result == null || result.isEmpty()){
//
//                    showToast(getResources().getString( R.string.message_confirm_id_password));
//                }else {
//                    success_login(result);
//                }
//            }
//
//            @Override
//            public void onFailResponse(VolleyError error) {
//
//            }
//        };
//        dataTransaction.queryExecute(1, params, "Pc_login/signin", callback);
//    }



//    private void success_login(String response){
//
//
//        String name = null,  babyBirthday  = null, babyName = null;
//        int babyID = 0;
//
//        //
//        JSONObject memberinfo = JsonParse.getJsonObecjtFromString(response, "memberinfo");
//        JSONObject babyinfo = JsonParse.getJsonObecjtFromString(response, "babyinfo");
//
//        try {
//            name = memberinfo.getString("nickname");
//            babyID = memberinfo.getInt("baby_id");
//
//            if(babyID != 0) {   // 매핑된 아기정보가 있으면
//                babyName = babyinfo.getString("babyname");
//                babyBirthday = babyinfo.getString("birthday");
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        loginInfo.setName(name);
//        loginInfo.setBabyID(babyID);
//        loginInfo.setBabybirthday(babyBirthday);
//        loginInfo.setBabyname(babyName);
//
//
//
//    }

}


