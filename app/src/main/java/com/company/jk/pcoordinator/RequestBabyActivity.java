package com.company.jk.pcoordinator;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.company.jk.pcoordinator.common.MyActivity;

public class RequestBabyActivity extends MyActivity{

    final String TAG = "InvatationActivity";
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestbaby);

        // Toolbar를 생성한다.
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);
        myToolbar.setTitleTextAppearance(getApplicationContext(), R.style.toolbarTitle);
    }
}
