package com.company.jk.pcoordinator.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 1202650 on 2016-07-30.
 */
public class LoginInfo{

    private static LoginInfo loginInfo = new LoginInfo();
    static SharedPreferences mPreference;
    SharedPreferences.Editor editor;
    public static final String APPLICATIONNAME = "pcoordinator";
    public static final String ISAUTO_LOGIN = "AutoChecked";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String BABYID = "babyID";
    public static final String BABYBIRTHDAY = "babybirthday";
    public static final String BABYNAME = "babyname";
    public static final String NAME = "name";


    public static LoginInfo getInstance(Context mContext){
        Log.d("LoginInfo","객체호출");
        mPreference = mContext.getSharedPreferences(APPLICATIONNAME, MODE_PRIVATE);
        return  loginInfo;
    }

    private String register_auth_code;   //이메일 인증 코드 ( 최초는 0, 인증은 1)

    public String getPassword() {
        return mPreference.getString(PASSWORD, null);
    }
    public String getEmail() {
       return mPreference.getString(EMAIL, null);
    }
    public String getName() {
        return mPreference.getString(NAME, null);
    }
    public  int getBabyID(){
        return mPreference.getInt(BABYID, 0);
    }
    public String getBabyname() {
        return mPreference.getString(BABYNAME, null);
    }
    public String getBabyBirthday() {
        return mPreference.getString(BABYBIRTHDAY, null);
    }


    public void setPassword(String password) {
        editor = mPreference.edit();
        editor.putString(PASSWORD, password);
        editor.commit();
    }

    public void setEmail(String email) {
        editor = mPreference.edit();
        editor.putString(EMAIL, email);
        editor.commit();
    }
    public void setName(String name) {
        editor = mPreference.edit();
        editor.putString(NAME, name);
        editor.commit();
    }

    public void setBabyID(int babyID){
        editor = mPreference.edit();
        editor.putInt(BABYID, babyID);
        editor.commit();
    }
    public  void setBabyname(String babyname){
        editor = mPreference.edit();
        editor.putString(BABYNAME, babyname);
        editor.commit();
    }
    public void setBabybirthday(String birthday) {
        editor = mPreference.edit();
        editor.putString(BABYBIRTHDAY, birthday);
        editor.commit();
    }

}
