package com.company.jk.pcoordinator.record;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.company.jk.pcoordinator.home.HomeFragment;
import com.company.jk.pcoordinator.home.RecordHistoryinfo;
import com.company.jk.pcoordinator.http.UrlPath;
import com.company.jk.pcoordinator.login.LoginInfo;
import com.roughike.bottombar.BottomBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.company.jk.pcoordinator.MainActivity.bottomBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private String id;
    EditText _milk, _rice, _remainText;
    EditText _date, _time;
    Button _plusMilk, _minusMilk, _plusRice, _minusRice, _save, _delete;
    ImageView _cancel;
    static final String TAG = "RecordFragment";
    Context mContext; View v;
    LoginInfo loginInfo = LoginInfo.getInstance();
    RecordHistoryinfo info;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_record, container, false);
        mContext = v.getContext();
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        android.support.v7.app.ActionBar actionBar = activity.getSupportActionBar();

//        actionBar.setDisplayShowHomeEnabled(true);
        if (getArguments() != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }else {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
        }


        findViewById(v);
        _plusRice.setOnClickListener(this);
        _plusMilk.setOnClickListener(this);
        _minusRice.setOnClickListener(this);
        _minusMilk.setOnClickListener(this);
        _date.setOnClickListener(this);
        _time.setOnClickListener(this);
        _save.setOnClickListener(this);
        _delete.setOnClickListener(this);
//        _cancel.setOnClickListener(this);


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
        _delete = v.findViewById(R.id.btn_delete);
//        _cancel = v.findViewById(R.id.btn_back);


        if (getArguments() != null) {

            info = (RecordHistoryinfo)getArguments().getSerializable("RecordHistoryinfo");
            Log.i(TAG, info.getDate());
            id = info.getId();
            _date.setText(info.getYearDate());
            _time.setText(info.getTime());
            _milk.setText(info.getMilk());
            _rice.setText(info.getRice());
            _remainText.setText(info.getComments());

            _save.setText("수정하기");



        }else{

            long now = System.currentTimeMillis();

            Date date = new Date(now);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat stf = new SimpleDateFormat("HH:mm");
            _date.setText(sdf.format(date));
            _time.setText(stf.format(date));
            _cancel.setVisibility(v.GONE);
            _delete.setVisibility(v.GONE);

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
        }else if(v == _save) {
            save_data();
        }else if(v == _delete){
            deleteAelrtDialog();

        }else if(v == _cancel){
//            getActivity().onBackPressed();
            bottomBar.selectTabAtPosition(0, false);
        }
    }


    private void deleteAelrtDialog(){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(v.getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(v.getContext());
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

    private void deleteSuccess(String response){
        Log.i(TAG, "결과값은 " + response);
        showToast(getString(R.string.save));
        HomeFragment hf = new HomeFragment();
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.enter_from_left).replace(R.id.frame, hf).addToBackStack(null).commit();
    }

    private void saveSuccess(String response){
        Log.i(TAG, "결과값은 " + response);
        if (getArguments() == null) {    //입력하기 전용화면이면
            saveAelrtDialog();
        }else{
            showToast(getString(R.string.save));
            HomeFragment hf = new HomeFragment();
            AppCompatActivity activity = (AppCompatActivity)getActivity();
            activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.enter_from_left).replace(R.id.frame, hf).addToBackStack(null).commit();
        }
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
        RequestQueue postRequestQueue = Volley.newRequestQueue(mContext);
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

    private  void  initialize(){
        _rice.setText("0");
        _milk.setText("0");
        _remainText.getText().clear();

    }

    private void showToast(String message){
        Toast toast=Toast.makeText(mContext.getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
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
