package com.company.jk.pcoordinator.record;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.home.RecordHistoryinfo;
import com.company.jk.pcoordinator.http.UrlPath;
import com.company.jk.pcoordinator.login.LoginInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private String id;
    EditText _milk, _rice, _remainText;
    EditText _date, _time;
    Button _plusMilk, _minusMilk, _plusRice, _minusRice, _save;
    static final String TAG = "RecordFragment";
    Context mContext;
    LoginInfo loginInfo = LoginInfo.getInstance();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            RecordHistoryinfo info = (RecordHistoryinfo)getArguments().getSerializable("RecordHistoryinfo");
//            id = getArguments().getString("id");
//            _date.setText(getArguments().getString("date"));
//            _time.setText(getArguments().getString("time"));
//            _milk.setText(getArguments().getString("milk"));
//            _rice.setText(getArguments().getString("rice"));
//            _remainText.setText(getArguments().getString("comments"));
            id = info.getId();
            _date.setText(info.getDate());
            _time.setText(info.getTime());
            _milk.setText(info.getMilk());
            _rice.setText(info.getRice());
            _remainText.setText(info.getComments());

        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v;
        v = inflater.inflate(R.layout.fragment_record, container, false);
        mContext = v.getContext();
        findViewById(v);
        _plusRice.setOnClickListener(this);
        _plusMilk.setOnClickListener(this);
        _minusRice.setOnClickListener(this);
        _minusMilk.setOnClickListener(this);
        _date.setOnClickListener(this);
        _time.setOnClickListener(this);
        _save.setOnClickListener(this);

        long now = System.currentTimeMillis();

        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm");
        _date.setText(sdf.format(date));
        _time.setText(stf.format(date));
        return v;
    }

    private  void findViewById(View v){
        _date = v.findViewById(R.id.tv_date);
        _time = v.findViewById(R.id.tv_time);
        _milk = v.findViewById(R.id.et_milk);
        _rice = v.findViewById(R.id.et_rice);
        _remainText = v.findViewById(R.id.et_remain_contents);
        _plusMilk = v.findViewById(R.id.btn_milk_plus);
        _minusMilk = v.findViewById(R.id.btn_milk_minus);
        _plusRice   = v.findViewById(R.id.btn_rice_plus);
        _minusRice  = v.findViewById(R.id.btn_rice_minus);
        _save = v.findViewById(R.id.btn_save);
        if(!id.isEmpty()){
            _save.setText("수정하기");
        }
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

        }else if(v == _plusMilk){
            _milk.setText(String.valueOf(Integer.parseInt(_milk.getText().toString()) + 10 ));
        }else if(v == _minusMilk){
            if(Integer.parseInt(_milk.getText().toString()) > 0){
                _milk.setText(String.valueOf(Integer.parseInt(_milk.getText().toString()) - 10 ));
            }
        }else if(v == _plusRice){
            _rice.setText(String.valueOf(Integer.parseInt(_rice.getText().toString()) + 10));

        }else if(v == _minusRice){
            if(Integer.parseInt(_rice.getText().toString()) > 0) {
                _rice.setText(String.valueOf(Integer.parseInt(_rice.getText().toString()) - 10));
            }
        }else if(v == _save){
            save_data();
        }
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

    private void responseSuccess(String response){
        Log.i(TAG, "결과값은 " + response);
        if(!response.isEmpty()){
            showToast(getString(R.string.save));
        }else{
            showToast(getString(R.string.savefail));
        }
    }

    private  void save_data(){


        String server_url = new UrlPath().getUrlPath() + "Pc_baby/save_record";
        Log.i(TAG, server_url);

        RequestQueue postRequestQueue = Volley.newRequestQueue(mContext);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                responseSuccess(response);    // 결과값 받아와서 처리하는 부분
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
                params.put("baby_id", loginInfo.getBabyID());
                params.put("email", loginInfo.getEmail());
                params.put("record_date", _date.getText().toString());
                params.put("record_time", _time.getText().toString());
                params.put("milk", _milk.getText().toString());
                params.put("rice", _rice.getText().toString());
                params.put("description", _remainText.getText().toString());

                return params;
            }
        };
        postRequestQueue.add(postStringRequest);

    }


    private void showToast(String message){
        Toast toast=Toast.makeText(mContext.getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
