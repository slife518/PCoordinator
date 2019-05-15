package com.company.jk.pcoordinator.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by JK on 2017-03-02.
 * 서버에 있는 사진을  url만 알면 bitmap 형식으로 다운로드하여 돌려준다.
 */

public class ImageDownloader {

    final static String TAG = "ImageDownloader";
    URL profileImgUrl = null;

    public Bitmap getImage(String url){

        Bitmap bitmap = null;

        try{
            Log.d(TAG, "이미지다운로드 시작 " + url);
            profileImgUrl = new URL(url);

            //주어진 url 에 연결한다.
            URLConnection conn = profileImgUrl.openConnection();
            conn.connect();

            //이미지를 가지고 와서 decodeStream()메소드로 Bitmap 이미지를 만든다.
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            bitmap = BitmapFactory.decodeStream(bis);
            Log.d(TAG, "이미지다운로드 끝");
            bis.close();




        }catch (Exception e){
            e.printStackTrace();
        }
        return  bitmap;
    }
}
