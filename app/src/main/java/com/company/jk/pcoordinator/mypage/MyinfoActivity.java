package com.company.jk.pcoordinator.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.login.AddressPostActivity;



//고객정보는 fragment 도 사용 가능하고 actvity 로도 사용 가능하여 둘 다 만들어 놓음.
public class MyinfoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int ADDRESS_REQUEST = 1888;
    String TAG = "MyinfoFragment";
    TextView _addressText;
    EditText _addressDetailText;
    TextInputLayout _addressDetail;
    ImageView _back;
    AppCompatImageButton _btn_findaddress;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _addressText              = (TextView) findViewById(R.id.tv_address);
        _addressDetailText      = (EditText) findViewById(R.id.et_address_detail) ;
        _addressDetail = (TextInputLayout) findViewById(R.id.input_address_detail);
        _back                  = (ImageView) findViewById(R.id.btn_exit);
        _btn_findaddress = (AppCompatImageButton) findViewById(R.id.btn_findAddress);

        _back.setOnClickListener(this);
        _btn_findaddress.setOnClickListener(this);
        _addressText.setOnClickListener(this);

        _addressText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!_addressText.getText().toString().isEmpty()){   //주소에 값이 있으면 상세주소칸 표시
                    _addressDetail.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                _addressDetailText.setVisibility(View.VISIBLE);
            }
        });
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_exit:
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                MypageFragment myFragment = new MypageFragment();
                //왼쪽에서 오른쪽 슬라이드
                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.frame, myFragment).addToBackStack(null).commit();
                break;
            case R.id.btn_findAddress : case R.id.tv_address:
                intent = new Intent(getApplicationContext(), AddressPostActivity.class);
                startActivityForResult(intent, ADDRESS_REQUEST);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADDRESS_REQUEST) {
            switch (resultCode) {
                case 1:
                    String result = data.getStringExtra("result");
                    Log.i(TAG, "result는 " + result);
                    _addressText.setText(result);
                    break;
                default:
                    break;
            }
        }
    }



}
