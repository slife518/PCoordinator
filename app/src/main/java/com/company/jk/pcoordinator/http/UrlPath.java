package com.company.jk.pcoordinator.http;

/**
 * Created by 1202650 on 2016-08-09.
 */
public class UrlPath {

    private final static String urlPath = "http://192.168.0.6:8080/dev.php/giantbaby/";  //래미안
    private final static String urlImg = "http://192.168.0.6:8080/etc/img/";

//    private final static String urlPath = "http://172.21.18.187:80/dev.php/giantbaby/";  //company
//    private final static String urlImg = "http://172.21.18.187:80/etc/img/";  //company

    //private final static String urlPath = "http://slife705.cafe24.com/index.php/giantbaby/";  //cafe24
//    private final static String urlImg = "http://slife705.cafe24.com/etc/img/";  //\Bitnami\wampstack-7.1.11-0\apache2\htdocs

    private final static String uploadPath = urlImg + "UploadToServer.php/";     //사진 업로드용
    private final static String urlBabyImg = urlImg + "babyprofile/";


    public String getUrlPath(){  return  urlPath; }
    public String getImgPath(){ return  urlImg;}
    public String getUploadPath(){return  uploadPath; }   //파일업로드용
    public String getUrlBabyImg(){return urlBabyImg;}     //아기사진 읽기용




    //private final static String urlPath = "http://ec2-52-78-31-158.ap-northeast-2.compute.amazonaws.com/process.php";
//    private final static String urlPath = "http://127.0.0.1:81/process.php";  //\Bitnami\wampstack-7.1.11-0\apache2\htdocs
//    private final static String uploadPath = "http://127.0.0.1:81/UploadToServer.php/";
//    private final static String profileImgPath = "http://127.0.0.1:81/img/";
//    private final static String urlPath = "http://124.49.80.186:8080/process.php";  //\Bitnami\wampstack-7.1.11-0\apache2\htdocs
//    private final static String uploadPath = "http://124.49.80.186:8080/UploadToServer.php/";
//    private final static String profileImgPath = "http://124.49.80.186:8080/img/";


}


