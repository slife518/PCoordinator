package com.company.jk.pcoordinator.record;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.MyFragment;
import com.company.jk.pcoordinator.home.HomeFragment;
import com.company.jk.pcoordinator.home.RecordHistoryinfo;
import com.company.jk.pcoordinator.http.UrlPath;
import com.company.jk.pcoordinator.login.LoginInfo;
import com.company.jk.pcoordinator.mypage.mybaby.MybabyActivity;
import com.company.jk.pcoordinator.mypage.mybaby.Mybabyinfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.company.jk.pcoordinator.MainActivity.bottomBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordFragment extends MyFragment implements View.OnClickListener,
                                                                DatePickerDialog.OnDateSetListener,
                                                                TimePickerDialog.OnTimeSetListener,
                                                                View.OnFocusChangeListener {

    private String id;
    EditText _milk,_mothermilk, _rice, _remainText;
    EditText _date, _time;
    Button _btn_plusMilk, _btn_minusMilk, _btn_plusRice, _btn_minusRice, _save, _delete, _btn_plusMotherMilk, _btn_minusMotherMilk;
//    ImageView _back;
    static final String TAG = "RecordFragment";
    Context mContext; View v;
    LoginInfo loginInfo = LoginInfo.getInstance();
    RecordHistoryinfo info;

    // 간격
    final static int mothermilk_num = 5;
    final static int milk_num = 10;
    final static int rice_num = 10;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(loginInfo.getBabyID().isEmpty()){
//            Intent intent = new Intent(mContext, Mybabyinfo.class);
//            startActivityForResult(intent, 12);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(TAG, "onCreateView 시작");
        v = inflater.inflate(R.layout.activity_record, container, false);
        mContext = v.getContext();


        // toolbar 설정1
//        setHasOptionsMenu(true);   // toolbar 의 추가 메뉴
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        Toolbar myToolbar = v.findViewById(R.id.my_toolbar);
        myToolbar.setTitle(R.string.title_record);
        activity.setSupportActionBar(myToolbar);
        myToolbar.setTitleTextAppearance(activity.getApplicationContext(), R.style.toolbarTitle);


        findViewById(v);
        _btn_plusRice.setOnClickListener(this);
        _btn_plusMilk.setOnClickListener(this);
        _btn_plusMotherMilk.setOnClickListener(this);
        _btn_minusRice.setOnClickListener(this);
        _btn_minusMilk.setOnClickListener(this);
        _btn_minusMotherMilk.setOnClickListener(this);

        _milk.setOnClickListener(this);
        _mothermilk.setOnClickListener(this);
        _rice.setOnClickListener(this);

        _milk.setOnEditorActionListener(this);
        _mothermilk.setOnEditorActionListener(this);
        _rice.setOnEditorActionListener(this);

        _date.setOnClickListener(this);
        _time.setOnClickListener(this);
        _save.setOnClickListener(this);

        _milk.setOnFocusChangeListener(this);
        _mothermilk.setOnFocusChangeListener(this);
        _rice.setOnFocusChangeListener(this);

        Log.i(TAG , "loginInfo.getBabyID() " + loginInfo.getBabyID());

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        initialize();

    }


    private  void findViewById(View v){
        _date = v.findViewById(R.id.tv_date);
        _time = v.findViewById(R.id.tv_time);
        _milk = v.findViewById(R.id.et_milk);
        _mothermilk = v.findViewById(R.id.et_mothermilk);
        _rice = v.findViewById(R.id.et_rice);
        _remainText = v.findViewById(R.id.et_remain_contents);
        _btn_plusMilk = v.findViewById(R.id.btn_milk_plus);
        _btn_plusMotherMilk = v.findViewById(R.id.btn_mothermilk_plus);
        _btn_minusMilk = v.findViewById(R.id.btn_milk_minus);
        _btn_minusMotherMilk = v.findViewById(R.id.btn_mothermilk_minus);
        _btn_plusRice = v.findViewById(R.id.btn_rice_plus);
        _btn_minusRice = v.findViewById(R.id.btn_rice_minus);
        _save = v.findViewById(R.id.btn_save);
        _delete = v.findViewById(R.id.btn_delete);
        _delete.setVisibility(v.GONE);
        _save.setText(R.string.btn_save);
        set_time();
    }

    public void set_time(){   //화면 초기화

        long now = System.currentTimeMillis();

        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm");
        _date.setText(sdf.format(date));
        _time.setText(stf.format(date));

    }
    @Override
    public void onClick(View v) {
        if(v == _date) {
            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dateDialog = new DatePickerDialog(mContext, this, mYear, mMonth, mDay);
            dateDialog.show();
        }else if(v == _time){
            Calendar c = Calendar.getInstance();
            int mHH = c.get(Calendar.HOUR);
            int mMM = c.get(Calendar.MINUTE);
            TimePickerDialog timeDialog = new TimePickerDialog(mContext, this, mHH, mMM, false);
            timeDialog.show();

        }else if(v == _btn_plusMilk){
            _milk.setText(calNumber(_milk.getText().toString(),+ milk_num ));
        }else if(v == _btn_minusMilk){
            if(Integer.parseInt(_milk.getText().toString()) > 0){
                _milk.setText(calNumber(_milk.getText().toString(), - milk_num ));
            }
        }else if(v == _btn_plusMotherMilk){
            _mothermilk.setText(calNumber(_mothermilk.getText().toString(), + mothermilk_num ));
        }else if(v == _btn_minusMotherMilk){
            if(Integer.parseInt(_mothermilk.getText().toString()) > 0){
                _mothermilk.setText(calNumber(_mothermilk.getText().toString(), - mothermilk_num ));
            }
        }else if(v == _btn_plusRice){
            _rice.setText(calNumber(_rice.getText().toString(), + rice_num));

        }else if(v == _btn_minusRice){
            if(Integer.parseInt(_rice.getText().toString()) > 0) {
                _rice.setText(calNumber(_rice.getText().toString(), - rice_num));
            }
        }else if(v == _save) {
            if(loginInfo.getBabyID() == 0) {   //선택된 또는 등록된 아기가 없으면
                Intent intent = new Intent(getContext(), MybabyActivity.class);
                intent.putExtra("email", loginInfo.getEmail());
                startActivityForResult(intent, 12);
                showToast(getResources().getString(R.string.message_warnning_register_baby));
            }else {
                save_data();
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean b) {
        Log.i("포커스가 변경 되었습니다.", String.valueOf(b) + _mothermilk.getText().toString());
        if(!b){   //포커스아웃이면
                if(v ==  _mothermilk &&  _mothermilk.getText().toString().isEmpty()){
                    _mothermilk.setText("0" );
                }else  if(v ==  _milk &&  _milk.getText().toString().isEmpty()) {
                    _milk.setText("0");
                }else if(v ==  _rice &&  _rice.getText().toString().isEmpty()) {
                    _rice.setText("0");
                }
        }else{  //포커스인이면
            if(v == _rice){
                _rice.setText("");
            }else if(v == _milk) {
                _milk.setText("");
            }else if(v == _mothermilk) {
                _mothermilk.setText("");
            }
        }
    }

    private String calNumber(String n, int i){
        if(n.isEmpty()){
            n = "0";
        }
        Log.i("n은" , n);
        return String.valueOf(Integer.parseInt(n) + i );
    }

    private void saveAelrtDialog(){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(v.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(v.getContext());
        }
        builder.setTitle(R.string.btn_save)
                .setMessage(R.string.saveAlert)
                .setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        bottomBar.selectTabAtPosition(0, false);
                    }
                })
                .setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        initialize();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        month = month + 1;
        String monthString = String.valueOf(month);
        if (monthString.length() == 1) {
            monthString = "0" + monthString;
        }

        _date.setText(year + "-" + monthString + "-" + day );
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int min) {
        String hourString = String.valueOf(hour);
        if (hourString.length() == 1) {
            hourString = "0" + hourString;
        }

        String minString = String.valueOf(min);
        if (minString.length() == 1) {
            minString = "0" + minString;
        }
        _time.setText(hourString + ":" + minString);
    }

    private void saveSuccess(String response){
        Log.i(TAG, "결과값은 " + response);

        showToast(getString(R.string.save));
        bottomBar.selectTabAtPosition(0, false);


    }
    private  void save_data(){
        String server_url = new UrlPath().getUrlPath() + "Pc_record/save_record";
        Log.i(TAG, server_url);
        RequestQueue postRequestQueue = Volley.newRequestQueue(mContext);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                saveSuccess(response);    // 결과값 받아와서 처리하는 부분
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "아기아이디는 " +loginInfo.getBabyID());
                Log.e(TAG, " " + error.getLocalizedMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if(id != null) {
                    params.put("record_id", id);
                }
                params.put("baby_id", String.valueOf(loginInfo.getBabyID()));
                params.put("email", loginInfo.getEmail());
                params.put("record_date", _date.getText().toString());
                params.put("record_time", _time.getText().toString());
                params.put("milk",(_milk.getText().toString().isEmpty()?"0":_milk.getText().toString()));
                params.put("mothermilk",(_mothermilk.getText().toString().isEmpty()?"0":_mothermilk.getText().toString()));
                params.put("rice", (_rice.getText().toString().isEmpty()?"0":_rice.getText().toString()));
                params.put("description", _remainText.getText().toString());

                return params;
            }
        };
        postRequestQueue.add(postStringRequest);

    }

    private  void  initialize(){
        set_time();   //화면 초기화
//        _rice.setText("0");
//        _milk.setText("0");
//        _mothermilk.setText("0");
        _remainText.getText().clear();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.i(TAG, "클릭" + item.getItemId());
        if(item.getItemId()==android.R.id.home){
            bottomBar.selectTabAtPosition(0, false);
        }
        return super.onOptionsItemSelected(item);
    }


}
