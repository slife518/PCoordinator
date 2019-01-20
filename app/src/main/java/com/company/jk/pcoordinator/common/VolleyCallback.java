package com.company.jk.pcoordinator.common;

import com.android.volley.VolleyError;

public interface VolleyCallback {

    void onSuccessResponse(String result, int method);  // 결과값, 구분

    void onFailResponse(VolleyError error);  //에러 났을 경우
}
