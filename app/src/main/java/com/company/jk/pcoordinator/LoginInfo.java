package com.company.jk.pcoordinator;

import android.util.Log;

/**
 * Created by 1202650 on 2016-07-30.
 */
public class LoginInfo {

    private static LoginInfo loginInfo = new LoginInfo();

    public static LoginInfo getInstance(){
        Log.i("LoginInfo","객체호출");



        return  loginInfo;
    }

//    private static String email;
//    private static String name;
//    private static String birthday;
//    private static String age;
//    private static String level;
//    private static String site;
//    private static String password;

    private String email;
    private String name;
    private String birthday;
    private String age;
    private String level;
    private String site;
    private String password;

    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }
    public String getBirthday() {
        return birthday;
    }
    public String getName() {
        return name;
    }
    public String getLevel() {
        return level;
    }
    public String getSite() {
        return site;
    }
    public String getAge() {
        return age;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setBirthday(String birthday) {
        this.birthday= birthday;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public void setSite(String site) {
        this.site = site;
    }
    public void setAge(String age) {
        this.age = age;
    }
}
