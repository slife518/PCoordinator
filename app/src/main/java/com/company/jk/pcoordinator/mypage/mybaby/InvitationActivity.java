package com.company.jk.pcoordinator.mypage.mybaby;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;

import com.company.jk.pcoordinator.R;

public class InvitationActivity extends AppCompatActivity {

    RadioButton _btn_email, _btn_phone;
    EditText _et_person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);

        _btn_email = (RadioButton) findViewById(R.id.radioButton);
        _btn_phone = (RadioButton) findViewById(R.id.radioButton2);
        _et_person = (EditText) findViewById(R.id.search_person);

    }
}
