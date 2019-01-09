package com.company.jk.pcoordinator.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.jk.pcoordinator.http.UrlPath;
import com.company.jk.pcoordinator.login.LoginInfo;

import java.util.HashMap;
import java.util.Map;

public class MyDataTransaction {

    Context mContext;
    String server_url;
    String res;
    Map<String, String>  setMapParam;

    public MyDataTransaction(Context context, String url) {
        mContext = context;
        server_url = new UrlPath().getUrlPath() + url;
    }

    public String queryExecute(final Map<String, String> param) {

        setMapParam = param;

        RequestQueue postReqeustQueue = Volley.newRequestQueue(mContext);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                send_response(response);   //비동기로 결과값이 넘어온다.

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(mContext.getPackageName(), "에러발생 원인은 " + error.getLocalizedMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params = setMapParam;
                return params;
            }
        };
        postReqeustQueue.add(postStringRequest);
        //data binding end

        return res;
    }

    private String send_response(String res){
        return  res;
    }

}


