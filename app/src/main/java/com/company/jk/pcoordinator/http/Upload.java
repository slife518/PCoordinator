package com.company.jk.pcoordinator.http;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 1202650 on 2017-02-16.
 * 스마트폰에 있는 파일을 서버에 업로드 한다.
 */
public class Upload{

    final static String TAG = "Upload";
    int serverResponseCode = 0;
    UrlPath urlPath = new UrlPath();
    String upLoadServerUrl = urlPath.getUploadPath();

    // (업로드 절대경로, 신규파일명, 신규파일이 위치할 폴더명)
    public int uploadFile(String sourceFileUri, String newFileName, String folderName) {

        Log.d(TAG, "업로드 시작 ");

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
//        int maxBufferSize = 1 * 1024 * 1024;
        int maxBufferSize = 1 * 102 * 102;

        File sourceFile = new File(sourceFileUri);
//        String upLoadServerUrl = urlPath.getImgPath();

        if (!sourceFile.isFile()) {
            Log.e("uploadFile", "Source File not exist :"
                    + sourceFileUri);
            return 0;
        }
        else{
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                Log.d(TAG, "업로드할 서버패스는 " + upLoadServerUrl);

                String filename = newFileName + ".jpg";  // jpg 로 저장(png, jpg, gif 는 서로 호완 됨

                URL url = new URL(upLoadServerUrl);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", sourceFileUri);
                conn.setRequestProperty("newFileName", filename);
                Log.d(TAG, "업로드 파일명은 " + filename);

                /// 변수 전달
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd); //필드 구분자 시작
                dos.writeBytes( "Content-Disposition: form-data; name=\"" + "newFileName" + "\""+ lineEnd ) ;  // 파라미터 key
                dos.writeBytes( lineEnd ) ;
                dos.writeBytes(filename) ;  // 파라미터 value
                dos.writeBytes( lineEnd ) ;

                /// 변수 전달
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd); //필드 구분자 시작
                dos.writeBytes( "Content-Disposition: form-data; name=\"" + "folderName" + "\""+ lineEnd ) ;  // 파라미터 key
                dos.writeBytes( lineEnd ) ;
                dos.writeBytes(folderName) ;  // 파라미터 value
                dos.writeBytes( lineEnd ) ;

                /// 파일 전달
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + sourceFileUri + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                Log.d(TAG, "sourceFileUri  은 " + sourceFileUri);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.d("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return serverResponseCode;

        } // End else block
    }
}
