package com.company.jk.pcoordinator.mypage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.login.AddressPostActivity;


public class MyinfoFragment extends Fragment implements View.OnClickListener {

    private static final int ADDRESS_REQUEST = 1888;
    private static final String TAG = "MyinfoFragment";
    Context mContext;
    TextView _addressText;
    EditText _addressDetailText;
    TextInputLayout _addressDetail;
    ImageView _back;
    AppCompatImageButton _btn_findaddress;
    Intent intent;
    View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_myinfo, container, false);
//        mContext = getActivity();
        mContext = v.getContext();

        _addressText              = (TextView) v.findViewById(R.id.tv_address);
        _addressDetailText      = (EditText) v.findViewById(R.id.et_address_detail) ;
        _addressDetail = (TextInputLayout) v.findViewById(R.id.input_address_detail);
        _back                  = (ImageView) v.findViewById(R.id.btback);
        _btn_findaddress = (AppCompatImageButton) v.findViewById(R.id.btn_findAddress);

        _back.setOnClickListener(this);
        _btn_findaddress.setOnClickListener(this);

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

        return  v;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btback:
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                MypageFragment myFragment = new MypageFragment();
                //왼쪽에서 오른쪽 슬라이드
                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.frame, myFragment).addToBackStack(null).commit();
                break;
            case R.id.btn_findAddress:
                intent = new Intent(mContext, AddressPostActivity.class);
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
