package com.company.jk.pcoordinator.record;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.MyActivity;
import com.company.jk.pcoordinator.home.RecordHistoryinfo;
import com.company.jk.pcoordinator.http.UrlPath;
import com.company.jk.pcoordinator.login.LoginInfo;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RecordActivity extends MyActivity implements View.OnClickListener,
                                                                DatePickerDialog.OnDateSetListener,
                                                                TimePickerDialog.OnTimeSetListener,
                                                                View.OnFocusChangeListener {

    private String id;
    EditText _milk,_mothermilk, _rice, _remainText;
    EditText _date, _time;
    Button _btn_plusMilk, _btn_minusMilk, _btn_plusMotherMilk, _btn_minusMotherMilk, _btn_plusRice, _btn_minusRice, _save, _delete;
    static final String TAG = "RecordFragment";
    LoginInfo loginInfo = LoginInfo.getInstance();
    RecordHistoryinfo info;
    Toolbar myToolbar;
    final static int mothermilk_num = 5;
    final static int milk_num = 10;
    final static int rice_num = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        Log.i(TAG, "onCreateView 시작");

// Toolbar를 생성한다.
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById();
        _btn_plusRice.setOnClickListener(this);
        _btn_plusMilk.setOnClickListener(this);
        _btn_plusMotherMilk.setOnClickListener(this);
        _btn_minusRice.setOnClickListener(this);
        _btn_minusMilk.setOnClickListener(this);
        _btn_minusMotherMilk.setOnClickListener(this);
        _milk.setOnClickListener(this);
        _mothermilk.setOnClickListener(this);
        _rice.setOnClickListener(this);
        _date.setOnClickListener(this);
        _time.setOnClickListener(this);
        _save.setOnClickListener(this);
        _delete.setOnClickListener(this);

        _milk.setOnFocusChangeListener(this);
        _mothermilk.setOnFocusChangeListener(this);
        _rice.setOnFocusChangeListener(this);

    }


    private  void findViewById(){
        _date = findViewById(R.id.tv_date);
        _time = findViewById(R.id.tv_time);
        _milk = findViewById(R.id.et_milk);
        _mothermilk = findViewById(R.id.et_mothermilk);
        _rice = findViewById(R.id.et_rice);
        _remainText = findViewById(R.id.et_remain_contents);
        _btn_plusMilk = findViewById(R.id.btn_milk_plus);
        _btn_plusMotherMilk = findViewById(R.id.btn_mothermilk_plus);
        _btn_minusMilk = findViewById(R.id.btn_milk_minus);
        _btn_minusMotherMilk = findViewById(R.id.btn_mothermilk_minus);
        _btn_plusRice = findViewById(R.id.btn_rice_plus);
        _btn_minusRice = findViewById(R.id.btn_rice_minus);
        _save = findViewById(R.id.btn_save);
        _delete = findViewById(R.id.btn_delete);

        Bundle loadInfo = getIntent().getExtras();
         info = (RecordHistoryinfo)loadInfo.getSerializable("RecordHistoryinfo");
            Log.i(TAG, info.getDate());
            id = info.getId();
            _date.setText(info.getYearDate());
            _time.setText(info.getTime());
            _milk.setText(info.getMilk());
            _mothermilk.setText(info.getMothermilk());
            _rice.setText(info.getRice());
            _remainText.setText(info.getComments());
    }
    @Override
    public void onClick(View v) {
        if(v == _date) {
            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dateDialog = new DatePickerDialog(this, this, mYear, mMonth, mDay);
            dateDialog.show();
        }else if(v == _time){
            Calendar c = Calendar.getInstance();
            int mHH = c.get(Calendar.HOUR);
            int mMM = c.get(Calendar.MINUTE);
            TimePickerDialog timeDialog = new TimePickerDialog(this, this, mHH, mMM, false);
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
            save_data();
        }else if(v == _delete){
            deleteAelrtDialog();
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
        Log.i("n은" , n);
        if(n.isEmpty()){
            n = "0";
        }
        Log.i("n은~" , n);
        return String.valueOf(Integer.parseInt(n) + i );
    }
    private void deleteAelrtDialog(){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(R.string.btn_delete)
                .setMessage(R.string.deleteAlert)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        delete_data();
//                        HomeFragment hf = new HomeFragment();
//                        AppCompatActivity activity = (AppCompatActivity)getActivity();
//                        activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.enter_from_right).replace(R.id.frame, hf).addToBackStack(null).commit();

                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
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

    private void deleteSuccess(String response){
        onBackPressed();
    }

    private void saveSuccess(String response){
        Log.i(TAG, "결과값은 " + response);
            showToast(getString(R.string.save));
            onBackPressed();
    }
    private  void save_data(){
        String server_url = new UrlPath().getUrlPath() + "Pc_record/save_record";
        Log.i(TAG, server_url);
        RequestQueue postRequestQueue = Volley.newRequestQueue(this);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                saveSuccess(response);    // 결과값 받아와서 처리하는 부분
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "아기아이디는 " +loginInfo.getBabyID());
                Log.e(TAG, error.getLocalizedMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if(id != null) {
                    params.put("record_id", id);
                }
                params.put("baby_id", loginInfo.getBabyID());
                params.put("email", loginInfo.getEmail());
                params.put("record_date", _date.getText().toString());
                params.put("record_time", _time.getText().toString());
                params.put("milk", _milk.getText().toString());
                params.put("mothermilk", _mothermilk.getText().toString());
                params.put("rice", _rice.getText().toString());
                params.put("description", _remainText.getText().toString());

                return params;
            }
        };
        postRequestQueue.add(postStringRequest);

    }

    private  void delete_data(){
        String server_url = new UrlPath().getUrlPath() + "Pc_record/delete_record";
        Log.i(TAG, server_url);
        RequestQueue postRequestQueue = Volley.newRequestQueue(this);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                deleteSuccess(response);    // 결과값 받아와서 처리하는 부분
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "아기아이디는 " +loginInfo.getBabyID());
                Log.e(TAG, error.getLocalizedMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("record_id", id);

                return params;
            }
        };
        postRequestQueue.add(postStringRequest);

    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        Log.i(TAG, "클릭" + item.getItemId());
//        if(item.getItemId()==android.R.id.home){
//            bottomBar.selectTabAtPosition(0, false);
//        }
//        return super.onOptionsItemSelected(item);
//    }


}
