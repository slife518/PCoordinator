package com.company.jk.pcoordinator.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.http.Upload;
import com.yalantis.ucrop.UCrop;

import java.io.File;

public class MyPhotoCrop extends AppCompatActivity {

    private String TAG = "MyPhotoCrop";
    Upload upload = new Upload();

    public void beginCrop(Uri uri, Context context, Activity activity, String title) {
        Uri destination = Uri.fromFile(new File(activity.getCacheDir(), "cropped"));
        UCrop uCrop = UCrop.of(uri, destination);
        uCrop.withAspectRatio(1, 1);
        uCrop.withMaxResultSize(450, 450);
        uCrop.withOptions(getCropOptions(context, title));
        uCrop.start(activity);
    }

    private  UCrop.Options getCropOptions(Context context, String title){
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(70);

        //CompressType
        //options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        //options.setCompressionFormat(Bitmap.CompressFormat.JPEG);

        //UI
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);

        //Colors
        options.setStatusBarColor(context.getResources().getColor(R.color.primaryColorDark));
        options.setToolbarColor(context.getResources().getColor(R.color.primaryColor));

        options.setToolbarTitle(title);  //title

        return options;
    }

    //
    public Uri handleCropAndUpload(int resultCode, Intent result, final String  folderName, final String fileName) {
        if (resultCode == Activity.RESULT_OK) { // Activity 의 RESULT_OK값을 사용

            final String absolutePath = UCrop.getOutput(result).getPath();   // 쓰레드 내에서 사용할 변수는 final 로 정의 되어야 함.  uri 의 절대 경로는 uri.getPath()
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

        } else if (resultCode == UCrop.RESULT_ERROR) {
//            Log.d("handleCrop", "RESULT_ERROR");
            Toast.makeText(this, UCrop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
//Activity에서 사용되던 this는 Fragment에서 보통 getActivity() 또는 getContext() 로 변경해서 사용한다.
        }

        return UCrop.getOutput(result);
    }

    public Uri handleCrop(Intent result){
          if(result==null){
              return null;
          }
          return UCrop.getOutput(result);
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
