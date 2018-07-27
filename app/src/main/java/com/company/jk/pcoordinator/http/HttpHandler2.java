package com.company.jk.pcoordinator.http;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Vector;


/**
 * Created by JK Builder Pettern 을 이용하여 재정의
 */
public class HttpHandler2 {

    private static UrlPath url = new UrlPath();
    private final static String urlPath = url.getUrlPath();
    private final static String TAG = "HttpHandler";
    private static JSONObject jObject = null; //group들로 구성된 json
    private JSONArray jsonArray = null;
    private JSONObject jsonObject1;
    Vector<NameValuePair> nameValue = new Vector<NameValuePair>();

    public static class  Builder{

        private  String mode = "";  //필수
        private  String email = "";
        private  String name = "";
        private  String pw = "";
        private  String birthday = "";
        private  String seq = "";
        private  String address = "";
        private  String mobile = "";

        public  Builder(String mode){
            this.mode = mode;
        }
        public  Builder email(String val){ this.email = val;  return  this; }
        public  Builder name(String val){ this.name = val;  return  this; }
        public  Builder pw(String val){ this.pw = val;  return  this; }
        public  Builder address(String val){ this.address = val;  return  this; }
        public  Builder mobile(String val){ this.mobile = val;  return  this; }

        public  HttpHandler2 build(){
            return  new HttpHandler2(this);
        }

        //setValue();
    }
    public HttpHandler2(Builder builder){

        nameValue.add(new BasicNameValuePair("mode", builder.mode));// DB 조회시 모드
        nameValue.add(new BasicNameValuePair("email", builder.email));// DB 조회시 모드
        nameValue.add(new BasicNameValuePair("pw", builder.pw));// DB 조회시 모드
        nameValue.add(new BasicNameValuePair("birthday", builder.birthday));// DB 조회시 모드
        nameValue.add(new BasicNameValuePair("address", builder.address));// DB 조회시 모드
        nameValue.add(new BasicNameValuePair("mobile", builder.mobile));// DB 조회시 모드
        nameValue.add(new BasicNameValuePair("seq", builder.seq));// DB 조회시 모드

    }

    public StringBuffer getData() {
        StringBuffer sb = new StringBuffer();
        Log.i(TAG, "getData: 데이터검색시작" );
        try {
            HttpPost request = new HttpPost(urlPath);   //request객체에 URL SET
            //웹 접속 - utf-8 방식으로
            HttpEntity entity = new UrlEncodedFormEntity(nameValue, HTTP.UTF_8); //Vector 값을 HttpEntity 객체로 만든다.
            request.setEntity(entity);  //HttpEntity 객체를 인자값으로 하는 HttpPost 객체 request에 담는다.
            //웹 서버에서 값을 받기 위한 객체 생성
            //HttpClient client = HttpClientBuilder.create().build();
            HttpClient client = new DefaultHttpClient();
            HttpResponse res = client.execute(request);   //URL 에 접속하고 넘겨준 인자로 실행
            //웹 서버에서 값받기******************************
            HttpEntity entityResponse = res.getEntity();   //전달받은 vector(?) 값 HttpEntity 객체에 담는다.
            InputStream im = entityResponse.getContent();  // vector 값을 InputStream 에 담고 BufferedReader 객체에 담는다.
            BufferedReader reader = new BufferedReader(new InputStreamReader(im, HTTP.UTF_8));
            String line = null;
            //readLine -> 파일내용을 줄 단위로 읽기
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            Log.i(TAG, "sb : " + sb.toString());
            im.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb;
    }
    //JSONObject 에서 필요한 정보만 jsonobject 에 담아서 리턴
    public JSONObject getNeedJSONObject(StringBuffer sb, String group) {
        try {
            jObject = new JSONObject(sb.toString());
            jsonArray = new JSONArray(jObject.getString(group));  //
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject1 = (JSONObject) jsonArray.get(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject1;
    }

    public JSONObject getJSONObject() {
        return jObject;
    }
}
