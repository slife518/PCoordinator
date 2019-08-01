package com.company.jk.pcoordinator.bossbaby;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import com.company.jk.pcoordinator.R;

public class WebviewFragment extends Fragment {

    private static final String TAG = "BossBabyFragment";
    private WebView mWebView;
    private WebSettings mWebSettings;
    View v;

    public void setUrl(String url) {
        this.url = url;
    }

    private  String url ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_boss_baby, container, false);


        mWebView = (WebView) v.findViewById(R.id.webview);  //레이어와 연결
        mWebView.setWebViewClient(new WebViewClient());  // 클릭시 새창 안뜨게
        mWebSettings = mWebView.getSettings();   //세부세팅등록

        mWebSettings.setJavaScriptEnabled(true);  //자바스크립트 사용 허용

        Log.d(TAG, url);

        mWebView.loadUrl(url);   //원하는 url 입력
//        mWebView.loadUrl("https://www.google.com");


        return v;
    }

}
