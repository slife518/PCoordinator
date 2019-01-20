package com.company.jk.pcoordinator.login;

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

    private String email;
    private String name;

    private String age;
    private String address1;
    private String address2;

    private int babyID;
    private String babybirthday;  //아기 생일

    public String getBabyname() {
        return babyname;
    }

    public void setBabyname(String babyname) {
        this.babyname = babyname;
    }

    private String babyname;

    private String tel;
    private String password;

    private String register_auth_code;   //이메일 인증 코드 ( 최초는 0, 인증은 1)

    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }
    public  int getBabyID(){
        return  babyID;
    }
    public String getBabyBirthday() {
        return babybirthday;
    }
    public String getName() {
        return name;
    }
    public String getAge() {
        return age;
    }
    public String getAddress1() { return address1; }
    public String getAddress2() { return address2; }
    public String getTel() { return tel; }
    public String getRegister_auth_code() {    return register_auth_code;  }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public  void  setBabyID(int babyID){ this.babyID = babyID;}
    public void setBabybirthday(String birthday) {
        this.babybirthday= birthday;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public void setAddress1(String address) { this.address1 = address1; }
    public void setAddress2(String address) { this.address2 = address2; }
    public void setTel(String tel) { this.tel = tel; }
    public void setRegister_auth_code(String register_auth_code) {    this.register_auth_code = register_auth_code; }

}
