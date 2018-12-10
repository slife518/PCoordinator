package com.company.jk.pcoordinator.chart;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.company.jk.pcoordinator.R;

public class ChartFragment extends Fragment implements View.OnClickListener{

    View v;
    Context mContext;
    Button lineBtn;
    Button barBtn;
    WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_chart, container, false);
        mContext = v.getContext();

        webView=(WebView)v.findViewById(R.id.webview);
        lineBtn=(Button)v.findViewById(R.id.btn_chart_line);
        barBtn=(Button)v.findViewById(R.id.btn_chart_bar);

        lineBtn.setOnClickListener(this);
        barBtn.setOnClickListener(this);

        WebSettings settings=webView.getSettings();
        settings.setJavaScriptEnabled(true);

        webView.loadUrl("file:///android_asset/test2.html");

        webView.addJavascriptInterface(new JavascriptTest(), "android");
        webView.setWebViewClient(new MyWebClient());
        webView.setWebChromeClient(new MyWebChrome());

        return inflater.inflate(R.layout.fragment_chart, container, false);
    }



    class JavascriptTest {
        @JavascriptInterface
        public String getChartData(){
            StringBuffer buffer=new StringBuffer();
            buffer.append("[");
            for(int i=0; i<14; i++){
                buffer.append("["+i+","+Math.sin(i)+"]");
                if(i<13) buffer.append(",");
            }
            buffer.append("]");
            Log.i("data는 ", buffer.toString());
            return buffer.toString();
        }
    }

    class MyWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Toast t=Toast.makeText(mContext, url, Toast.LENGTH_SHORT) ;
            t.show();
            return true;
        }
    }

    class MyWebChrome extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Toast t=Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
            t.show();
            result.confirm();
            return true;
        }
    }


    @Override
    public void onClick(View v) {
        if(v==lineBtn){
            webView.loadUrl("javascript:lineChart()");
        }else if(v==barBtn){
            webView.loadUrl("javascript:barChart()");
        }
    }

}
