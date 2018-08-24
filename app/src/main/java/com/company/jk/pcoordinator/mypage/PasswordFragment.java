package com.company.jk.pcoordinator.mypage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.http.HttpHandler2;
import com.company.jk.pcoordinator.login.AddressPostActivity;
import com.company.jk.pcoordinator.login.LoginInfo;

import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class PasswordFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "PasswordFragment";
    Context mContext;
    LoginInfo loginInfo = LoginInfo.getInstance();
    private static String tcode;
    private JSONObject jObject = null; //group들로 구성된 json
    private StringBuffer sb = new StringBuffer();
    private static final String Controller = "Pc_Login";
    private String toastMessage = " ";
    EditText  _old_password, _password, _repassword;
    ImageView _back;

    Button _btn_save_pw;
    View v;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_password, container, false);
//        mContext = getActivity();
        mContext = v.getContext();

        _old_password = (EditText) v.findViewById(R.id.et_old_password);
        _password = (EditText) v.findViewById(R.id.et_new_password);
        _repassword = (EditText) v.findViewById(R.id.et_new_password2);
        _back                  = (ImageView) v.findViewById(R.id.btback);
        _btn_save_pw = (Button) v.findViewById(R.id.btn_password_save);

        _back.setOnClickListener(this);
        _btn_save_pw.setOnClickListener(this);
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
            case R.id.btn_password_save:  //비밀번호 변경 저장
                if(check_validation_pw()){
                    tcode = "save_customer_pw";
                    new HttpTaskSignIn().execute(loginInfo.getEmail(), _old_password.getText().toString(),  _password.getText().toString(), _repassword.getText().toString());
                }
                break;
        }
    }

    public boolean check_validation_pw(){
        if(_password.getText().toString().length() < 5){
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage(R.string.message_confirm_password_count)
                    .setPositiveButton(R.string.message_OK, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
            AlertDialog alert =  builder.create();
//            alert.setTitle("확인바랍니다.");
            alert.show();

            return  false;
        }
        if(!_password.getText().toString().equals(_repassword.getText().toString())){
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage(R.string.message_confirm_password)
                   .setPositiveButton(R.string.message_OK, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
            AlertDialog alert =  builder.create();
//            alert.setTitle("확인바랍니다.");
            alert.show();
            return  false;
        }

        return  true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    /////// 여기서 부터 DB 쓰레드 작업
    //  DB 쓰레드 작업
    class HttpTaskSignIn extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... args) {
            String result = "";
            int i = 0;
            HttpHandler2 httpHandler = null;
            switch (tcode) {
                case "save_customer_pw":
                    httpHandler = new HttpHandler2.Builder(Controller, tcode).email(args[i++]).oldpassword(args[i++]).password(args[i++]).repassword(args[i++]).build();
                    break;
            }
            sb = httpHandler.getData();
            try {
                //결과값에 jsonobject 가 두건 이상인 경우 한건 조회
//				JSONObject jsonObject = httpHandler.getNeedJSONObject(sb, "result");
//				result = jsonObject.getString("name");
                jObject = new JSONObject(sb.toString());
                switch (tcode) {
                    case "save_customer_pw":
                        result = jObject.getString("result");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, "onPostExecute: result "+ result + " ,tcode) " + tcode);
            super.onPostExecute(result);
            try {
                switch (tcode) {
                    case "save_customer_pw":
                        if (result.equals("true")){
                            toastMessage = "저장되었습니다.";
                            break;
                        }else{
                            toastMessage = result;
                            break;
                        }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(v.getContext(), toastMessage, Toast.LENGTH_SHORT).show();
            tcode = "";   //초기화
        }
    }
}
