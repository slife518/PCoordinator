package com.company.jk.pcoordinator.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.company.jk.pcoordinator.http.Upload;
import com.soundcloud.android.crop.Crop;

import java.io.File;

public class MyPhotoCrop extends AppCompatActivity {

    private String TAG = "MyPhotoCrop";
    Upload upload = new Upload();

    public void beginCrop(Uri source, Context context, Activity activity) {
//        Log.d("beginCrop", "Start" +source.toString());
        Uri destination = Uri.fromFile(new File(context.getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(activity);
//        Log.d("beginCrop", "End");
    }


    //
    public Uri handleCropAndUpload(int resultCode, Intent result, final String  folderName, final String fileName) {
        if (resultCode == Activity.RESULT_OK) { // Activity 의 RESULT_OK값을 사용
//            Log.d("handleCrop", "RESULT_OK" + (Crop.getOutput(result).toString()));
//            _profile.setImageDrawable(null);
//            _profile.setImageURI(Crop.getOutput(result));
//            _profile.invalidate();


            final String absolutePath = Crop.getOutput(result).getPath();   // 쓰레드 내에서 사용할 변수는 final 로 정의 되어야 함.  uri 의 절대 경로는 uri.getPath()
            //파일 업로드 시작! 파일 업로드 call 할 때는 반드시 쓰레드 이용해야 함.
            new Thread(new Runnable() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {	}	});
                    Log.d(TAG, "파일명은 " + " 업로드할 사진의 절대 경로 " + absolutePath);
                    upload.uploadFile(absolutePath, fileName, folderName);
                    //			saveBitmaptoJpeg(bitmap, "",loginInfo.getEmail());
                }
            }).start();

        } else if (resultCode == Crop.RESULT_ERROR) {
//            Log.d("handleCrop", "RESULT_ERROR");
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
//Activity에서 사용되던 this는 Fragment에서 보통 getActivity() 또는 getContext() 로 변경해서 사용한다.
        }

        return Crop.getOutput(result);
    }

    public Uri handleCrop(Intent result){
          if(result==null){
              return null;
          }
          return Crop.getOutput(result);
    }

    public void uploadPhoto(final String absolutePath, final String  folderName, final String fileName) {


            //파일 업로드 시작! 파일 업로드 call 할 때는 반드시 쓰레드 이용해야 함.
            new Thread(new Runnable() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {	}	});
//                    Log.d(TAG, "파일명은 " + loginInfo.getEmail() + " 업로드할 사진의 절대 경로 " + absolutePath);
                    upload.uploadFile(absolutePath, fileName, folderName);
                    //			saveBitmaptoJpeg(bitmap, "",loginInfo.getEmail());
                }
            }).start();
    }

}
