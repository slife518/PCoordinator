package com.company.jk.pcoordinator.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.http.UrlPath;

public class AddressPostActivity_bak extends AppCompatActivity {

    private WebView webView;
    private TextView result;
    private Handler handler;
    private static final int RESULT_CODE = 1;
    UrlPath urlPath = new UrlPath();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_post);

        // WebView 초기화
        init_webView();

        // 핸들러를 통한 JavaScript 이벤트 반응
        handler = new Handler();
    }

    public void init_webView() {

        // WebView 설정
        webView = (WebView) findViewById(R.id.webView);
        // JavaScript 허용
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportMultipleWindows(true);

        // JavaScript의 window.open 허용
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌

        // 두 번째 파라미터는 사용될 php에도 동일하게 사용해야함
        webView.addJavascriptInterface(new AndroidBridge(), "TestApp");

        // web client 를 chrome 으로 설정
        webView.setWebChromeClient(new WebChromeClient());
        // webview url load

//        webView.loadUrl("http://10.0.2.2:80/dev.php/giantbaby/Pc_login/addresspost");
        webView.loadUrl(urlPath.getUrlPath() +  "Pc_login/addresspost");  //회사
//        webView.loadUrl("https://slife705.cafe24.com/index.php/giantbaby/Pc_login/addresspost");  //회사
//        webView.loadUrl("https://www.google.co.kr/");  //회사
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {

                    String result = String.format("(%s) %s %s", arg1, arg2, arg3);
                    Intent intent = getIntent();
                    intent.putExtra("result", result);
                    setResult(RESULT_CODE, intent);
                    finish();
//
//                    result.setText(String.format("(%s) %s %s", arg1, arg2, arg3));
                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    init_webView();
                }
            });
        }
    }

}
