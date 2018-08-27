package com.company.jk.pcoordinator.bossbaby;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.login.LoginInfo;

public class BossBabyFragment extends Fragment {

    private static final String TAG = "BossBabyFragment";
    LoginInfo loginInfo = LoginInfo.getInstance();
    private WebView mWebView;
    private WebSettings mWebSettings;
    View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        v = inflater.inflate(R.layout.fragment_boss_baby, container, false);

        String url;
        mWebView = (WebView) v.findViewById(R.id.webview);  //레이어와 연결
        mWebView.setWebViewClient(new WebViewClient());  // 클릭시 새창 안뜨게
//        mWebSettings = mWebView.getSettings();   //세부세팅등록

//        mWebSettings.setJavaScriptEnabled(true);  //자바스크립트 사용 허용
        url = "http://slife705.cafe24.com/index.php/auth/directLogin/" + loginInfo.getEmail() + loginInfo.getPassword();
        Log.i(TAG, url);
//        mWebView.loadUrl("http://slife705.cafe24.com/index.php/record/index/0");   //원하는 url 입력
        mWebView.loadUrl(url);   //원하는 url 입력
//        mWebView.loadUrl("https://www.google.com");


        return v;
    }

}
