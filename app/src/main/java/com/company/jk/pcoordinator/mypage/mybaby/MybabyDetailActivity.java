package com.company.jk.pcoordinator.mypage.mybaby;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.http.Upload;
import com.company.jk.pcoordinator.http.UrlPath;

public class MybabyDetailActivity extends AppCompatActivity {


    static final String TAG = "MybabyDetailFragment";
    ImageView _btn_back, _profile;
    Button _btn_save, _btn_delete, _btn_parents;
    RadioButton _boy, _girl;

    EditText _name, _sex, _father, _mother, _owner;
    TextView _birthday;
    Context mContext;
    String email, baby_id;
    UrlPath urlPath = new UrlPath();
    Upload upload = new Upload();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybaby_detail);


    }
}
