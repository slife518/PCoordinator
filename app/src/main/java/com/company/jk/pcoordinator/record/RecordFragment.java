package com.company.jk.pcoordinator.record;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.MyFragment;
import com.company.jk.pcoordinator.common.sqlite.Comment;
import com.company.jk.pcoordinator.common.sqlite.DatabaseHelper;
import com.company.jk.pcoordinator.home.RecordHistoryinfo;
import com.company.jk.pcoordinator.http.UrlPath;
import com.company.jk.pcoordinator.login.LoginInfo;
import com.company.jk.pcoordinator.mypage.mybaby.MybabyDetailActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

import static android.content.Context.MODE_PRIVATE;
import static com.company.jk.pcoordinator.MainActivity.bottomBar;
import static com.company.jk.pcoordinator.login.LoginInfo.APPLICATIONNAME;
import static com.company.jk.pcoordinator.login.LoginInfo.BABYBIRTHDAY;
import static com.company.jk.pcoordinator.login.LoginInfo.BABYID;
import static com.company.jk.pcoordinator.login.LoginInfo.BABYNAME;
import static com.company.jk.pcoordinator.login.LoginInfo.EMAIL;
import static com.company.jk.pcoordinator.login.LoginInfo.ISAUTO_LOGIN;
import static com.company.jk.pcoordinator.login.LoginInfo.NAME;
import static com.company.jk.pcoordinator.login.LoginInfo.PASSWORD;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordFragment extends MyFragment implements View.OnClickListener,
                                                                View.OnLongClickListener,
                                                                DatePickerDialog.OnDateSetListener,
                                                                TimePickerDialog.OnTimeSetListener,
                                                                View.OnFocusChangeListener {

    TextView _tx_mothermilk, _tx_milk, _tx_rice;
    EditText _milk,_mothermilk, _rice, _remainText,  _date, _time;
    Button _btn_plusMilk, _btn_minusMilk, _btn_plusRice, _btn_minusRice, _save, _delete,
            _btn_plusMotherMilk, _btn_minusMotherMilk, _btn_shortcut1, _btn_shortcut2,
            _btn_shortcut3, _btn_shortcut4,_btn_shortcut5, _btn_shortcutPlus;

    Context mContext;
    View v;
    LoginInfo loginInfo;
    RecordHistoryinfo info;
    SharedPreferences mPreference;
    Boolean milk_toggle, mothermilk_toggle, rice_toggle;
    Boolean check_tooltip_record1;

    // 간격
    final static String TAG = "RecordFragment";
    final static int mothermilk_num = 5;
    final static int milk_num = 10;
    final static int rice_num = 10;

    DatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView 시작");
        v = inflater.inflate(R.layout.activity_record, container, false);
        mContext = v.getContext();

        loginInfo = LoginInfo.getInstance(mContext);
        mPreference = PreferenceManager.getDefaultSharedPreferences(mContext);
        mothermilk_toggle = mPreference.getBoolean("mothermilk", true);
        milk_toggle = mPreference.getBoolean("milk", true);
        rice_toggle = mPreference.getBoolean("rice", true);
        check_tooltip_record1 = mPreference.getBoolean("check_tooltip_record1", true);

        Log.i(TAG, "수유는 " + mothermilk_toggle +", 분유는 "+ milk_toggle +", 이유식은 "+ rice_toggle);

        // layout_toolbar 설정1
//        setHasOptionsMenu(true);   // layout_toolbar 의 추가 메뉴
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        Toolbar myToolbar = v.findViewById(R.id.my_toolbar);
        myToolbar.setTitle(R.string.title_record);
        activity.setSupportActionBar(myToolbar);
        myToolbar.setTitleTextAppearance(activity.getApplicationContext(), R.style.toolbarTitle);

        findViewById(v);


        db =  new DatabaseHelper(v.getContext());
        select_comment_btn();
        Log.d(TAG , "loginInfo.getBabyID() " + loginInfo.getBabyID());

//        setTooltip();



//        ViewTooltip
//                .on(this, _mothermilk)
//                .autoHide(true, 5000)
//                .corner(30)
//                .clickToHide(true)
//                .align(CENTER)
//                .color(getResources().getColor(R.color.accentcolor))
//                .position(ViewTooltip.Position.RIGHT)
//                .text("터치하고 직접입력하셔도 됩니다.")
//                .show();



        return v;
    }

    private void setTooltip() {

        mPreference = mContext.getSharedPreferences(APPLICATIONNAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = mPreference.edit();


        Log.i(TAG, "말풍선"+String.valueOf( mPreference.getBoolean("check_tooltip_record1",false)));
        if(!mPreference.getBoolean("check_tooltip_record1",false)) {
            new SimpleTooltip.Builder(mContext)
                    .anchorView(_milk)
                    .text("터치하고 직접입력하셔도 됩니다.")
                    .gravity(Gravity.END)
                    .animated(true)
                    .transparentOverlay(false)
                    .textColor(Color.WHITE)
                    .build()
                    .show()
            ;
        }
        editor.commit();

    }

    @Override
    public void onResume() {
        super.onResume();
        initialize();

    }

    private  void findViewById(View v){

        _tx_mothermilk = v.findViewById(R.id.tx_mothermilk);
        _tx_milk = v.findViewById(R.id.tx_milk);
        _tx_rice = v.findViewById(R.id.tx_rice);
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
        _btn_shortcut1 = v.findViewById(R.id.shortcut1);
        _btn_shortcut2 = v.findViewById(R.id.shortcut2);
        _btn_shortcut3 = v.findViewById(R.id.shortcut3);
        _btn_shortcut4 = v.findViewById(R.id.shortcut4);
        _btn_shortcut5 = v.findViewById(R.id.shortcut5);
        _btn_shortcutPlus = v.findViewById(R.id.shortcutplus);


//        layout_shortcut = v.findViewById(R.id.layout_shortcut);

        _save = v.findViewById(R.id.btn_save);
        _delete = v.findViewById(R.id.btn_delete);
        _delete.setVisibility(v.GONE);
        _save.setText(R.string.btn_save);
        set_time();

        _btn_plusRice.setOnClickListener(this);
        _btn_plusMilk.setOnClickListener(this);
        _btn_plusMotherMilk.setOnClickListener(this);
        _btn_minusRice.setOnClickListener(this);
        _btn_minusMilk.setOnClickListener(this);
        _btn_minusMotherMilk.setOnClickListener(this);


        _btn_shortcut1.setOnClickListener(this);
        _btn_shortcut2.setOnClickListener(this);
        _btn_shortcut3.setOnClickListener(this);
        _btn_shortcut4.setOnClickListener(this);
        _btn_shortcut5.setOnClickListener(this);
        _btn_shortcutPlus.setOnClickListener(this);
        _btn_shortcut1.setOnLongClickListener(this);
        _btn_shortcut2.setOnLongClickListener(this);
        _btn_shortcut3.setOnLongClickListener(this);
        _btn_shortcut4.setOnLongClickListener(this);
        _btn_shortcut5.setOnLongClickListener(this);



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

        }else if(v == _btn_shortcut1){ _remainText.setText( _remainText.getText() + _btn_shortcut1.getText().toString() );
        }else if(v == _btn_shortcut2){ _remainText.setText( _remainText.getText() + _btn_shortcut2.getText().toString() );
        }else if(v == _btn_shortcut3){ _remainText.setText( _remainText.getText() + _btn_shortcut3.getText().toString() );
        }else if(v == _btn_shortcut4){ _remainText.setText( _remainText.getText() + _btn_shortcut4.getText().toString() );
        }else if(v == _btn_shortcut5){ _remainText.setText( _remainText.getText() + _btn_shortcut5.getText().toString() );
        }else if(v == _btn_shortcutPlus){ popup_comment();

        }else if(v == _save) {
            if(loginInfo.getBabyID() == 0) {   //선택된 또는 등록된 아기가 없으면
                Intent intent = new Intent(getContext(), MybabyDetailActivity.class);
                intent.putExtra("email", loginInfo.getEmail());
                startActivityForResult(intent, 12);
                showToast(getResources().getString(R.string.message_warnning_register_baby));
            }else {
                save_data();
            }
        }
    }


    private void popup_comment(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_comment, null))
                // Add action buttons
                .setPositiveButton(R.string.add_comment_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog dialogView = (Dialog)dialog;

                        EditText _add_text = dialogView.findViewById(R.id.et_comment);
                       add_comment_btn(_add_text.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).show();
    }
    @Override
    public boolean onLongClick(View view) {

        if(view == _btn_shortcut1){ db.delete(_btn_shortcut1.getText().toString());
        }else if(view == _btn_shortcut2){  db.delete(_btn_shortcut2.getText().toString());
        }else if(view == _btn_shortcut3){  db.delete(_btn_shortcut3.getText().toString());
        }else if(view == _btn_shortcut4){  db.delete(_btn_shortcut4.getText().toString());
        }else if(view == _btn_shortcut5) {  db.delete(_btn_shortcut5.getText().toString());
        }
        select_comment_btn();

        return true;
    }

    private  void add_comment_btn(String co){   //코맨트 버튼 추가
        Comment comment = new Comment(co);
        db.add(comment);

        select_comment_btn();
    }

    private void select_comment_btn(){

        List<Comment> listComment =  db.getAll();

        Log.d(TAG, "Comment list ==>");

        _btn_shortcut1.setVisibility(View.GONE);
        _btn_shortcut2.setVisibility(View.GONE);
        _btn_shortcut3.setVisibility(View.GONE);
        _btn_shortcut4.setVisibility(View.GONE);
        _btn_shortcut5.setVisibility(View.GONE);

        int i = 0;
        for (Comment comment : listComment) {
            switch (i){
                case 0 :
                    _btn_shortcut1.setText(comment.getComment());
                    _btn_shortcut1.setVisibility(View.VISIBLE);
                    break;
                case 1 :
                    _btn_shortcut2.setText(comment.getComment());
                    _btn_shortcut2.setVisibility(View.VISIBLE);
                    break;
                case 2 :
                    _btn_shortcut3.setText(comment.getComment());
                    _btn_shortcut3.setVisibility(View.VISIBLE);
                    break;
                case 3 :
                    _btn_shortcut4.setText(comment.getComment());
                    _btn_shortcut4.setVisibility(View.VISIBLE);
                    break;
                case 4 :
                    _btn_shortcut5.setText(comment.getComment());
                    _btn_shortcut5.setVisibility(View.VISIBLE);
                    break;
            }

            i = i + 1;
            Log.d(TAG, "comment " + comment.getId() + ": " + comment.getComment());
        }
        Log.d(TAG, "<==");
    }
    @Override
    public void onFocusChange(View v, boolean b) {
        Log.d("포커스가 변경 되었습니다.", String.valueOf(b) + _mothermilk.getText().toString());
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
        Log.d("n은" , n);
        return String.valueOf(Integer.parseInt(n) + i );
    }
//
//    private void saveAelrtDialog(){
//        AlertDialog.Builder builder;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            builder = new AlertDialog.Builder(v.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
//        } else {
//            builder = new AlertDialog.Builder(v.getContext());
//        }
//        builder.setTitle(R.string.btn_save)
//                .setMessage(R.string.saveAlert)
//                .setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        bottomBar.selectTabAtPosition(0, false);
//                    }
//                })
//                .setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        initialize();
//                    }
//                })
//                .setIcon(android.R.drawable.ic_dialog_info)
//                .show();
//    }


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
        Log.d(TAG, "결과값은 " + response);

        showToast(getString(R.string.save));
        bottomBar.selectTabAtPosition(0, false);


    }
    private  void save_data(){
        String server_url = new UrlPath().getUrlPath() + "Pc_record/save_record";
        Log.d(TAG, server_url);
        RequestQueue postRequestQueue = Volley.newRequestQueue(mContext);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                saveSuccess(response);    // 결과값 받아와서 처리하는 부분
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "아기아이디는 " +loginInfo.getBabyID());
                Log.e(TAG, " " + error.getLocalizedMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                if(id != null) {
//                    params.put("record_id", id);
//                }
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
        _remainText.getText().clear();

        if(mothermilk_toggle){
            set_mothermilk_visibile();
        }else {
            set_mothermilk_invisible();
        }
        if(milk_toggle){
            set_milk_visibile();
        }else {
            set_milk_invisible();
        }

        if(rice_toggle){
            set_rice_visibile();
        }else {
            set_rice_invisible();
        }

    }

    private void  set_mothermilk_visibile(){
        _mothermilk.setVisibility(View.VISIBLE);
        _tx_mothermilk.setVisibility(View.VISIBLE);
        _btn_plusMotherMilk.setVisibility(View.VISIBLE);
        _btn_minusMotherMilk.setVisibility(View.VISIBLE);
    }

    private void  set_mothermilk_invisible(){
        _mothermilk.setVisibility(View.GONE);
        _tx_mothermilk.setVisibility(View.GONE);
        _btn_plusMotherMilk.setVisibility(View.GONE);
        _btn_minusMotherMilk.setVisibility(View.GONE);
    }
    private void  set_milk_visibile(){
        _milk.setVisibility(View.VISIBLE);
        _tx_milk.setVisibility(View.VISIBLE);
        _btn_plusMilk.setVisibility(View.VISIBLE);
        _btn_minusMilk.setVisibility(View.VISIBLE);
    }

    private void  set_milk_invisible(){
        _milk.setVisibility(View.GONE);
        _tx_milk.setVisibility(View.GONE);
        _btn_plusMilk.setVisibility(View.GONE);
        _btn_minusMilk.setVisibility(View.GONE);
    }

    private void  set_rice_visibile(){
        _rice.setVisibility(View.VISIBLE);
        _tx_rice.setVisibility(View.VISIBLE);
        _btn_plusRice.setVisibility(View.VISIBLE);
        _btn_minusRice.setVisibility(View.VISIBLE);
    }

    private void  set_rice_invisible(){
        _rice.setVisibility(View.GONE);
        _tx_rice.setVisibility(View.GONE);
        _btn_plusRice.setVisibility(View.GONE);
        _btn_minusRice.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d(TAG, "클릭" + item.getItemId());
        if(item.getItemId()==android.R.id.home){
            bottomBar.selectTabAtPosition(0, false);
        }
        return super.onOptionsItemSelected(item);
    }


}
