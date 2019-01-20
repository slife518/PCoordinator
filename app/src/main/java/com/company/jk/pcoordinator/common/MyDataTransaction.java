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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyDataTransaction{

    Context mContext;
    private final static String TAG = "MyDataTransaction";


    public MyDataTransaction(Context context) {
        mContext = context;
    }

    public void queryExecute(final int method, final Map<String, String> param, String url, final VolleyCallback callback) {

        url = new UrlPath().getUrlPath() + url;

        RequestQueue postReqeustQueue = Volley.newRequestQueue(mContext);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccessResponse(response, method);   //비동기로 결과값이 넘어온다.

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "에러발생 원인은 " + error.getLocalizedMessage());
                callback.onFailResponse(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return param;
            }
        };
        postReqeustQueue.add(postStringRequest);
        //data binding end

    }

}


