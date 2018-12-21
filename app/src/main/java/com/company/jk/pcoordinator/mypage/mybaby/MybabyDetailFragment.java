package com.company.jk.pcoordinator.mypage.mybaby;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.jk.pcoordinator.ParentsActivity;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.JsonParse;
import com.company.jk.pcoordinator.http.Upload;
import com.company.jk.pcoordinator.http.UrlPath;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import com.soundcloud.android.crop.Crop;

public class MybabyDetailFragment extends Fragment implements View.OnClickListener {


    static final String TAG = "MybabyDetailFragment";
    ImageView _btn_back, _profile;
    Button _btn_save, _btn_delete, _btn_parents;
    RadioButton _boy, _girl;

    EditText _name, _sex, _father, _mother, _owner;
    TextView _birthday;
    Context mContext;
    String email, baby_id;
    UrlPath urlPath = new UrlPath();
    Upload upload = new Upload();
    View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString("email");
            baby_id = getArguments().getString("baby_id");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_mybaby_detail, container, false);
        mContext = v.getContext();


        findViewsById(v);
        if(baby_id != null) {
            initLoader();
        }else{
            _btn_parents.setVisibility(View.GONE);
            _btn_delete.setVisibility(View.GONE);
        }

        _btn_back.setOnClickListener(this);
        _btn_save.setOnClickListener(this);
        _btn_delete.setOnClickListener(this);
        _btn_parents.setOnClickListener(this);

        _birthday.setOnClickListener(this);
        _profile.setOnClickListener(this);


        Log.i(TAG, "이메일은 " + email + " id는 " + baby_id);

        return v;
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onStart() {
        super.onStart();
//        initLoader();
    }

    private void initLoader() {
        //data binding start
        String server_url = new UrlPath().getUrlPath() + "Pc_baby/get_baby_info_detail";
        RequestQueue postRequestQueue = Volley.newRequestQueue(mContext);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "2");
                responseReceiveData(response);    // 결과값 받아와서 처리하는 부분
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getLocalizedMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("baby_id", baby_id);
                return params;
            }
        };
        Log.i(TAG, "1");
        postRequestQueue.add(postStringRequest);

        //data binding end

    }

    private void responseReceiveData(String response) {
        Log.i(TAG, "결과값은 " + response);

        try {
            JSONObject rs = JsonParse.getJsonObjectFromString(response, "result");
            String name = rs.getString("babyname");
            String id = rs.getString("baby_id");
            String birthday = rs.getString("birthday");
//            _birthday.setText(birthday.substring(0, 4)+"년"+birthday.substring(4, 6)+"월"+birthday.substring(6, 8)+"일");
            _birthday.setText(birthday);

            String sex = rs.getString("sex");

            String imgUrl = urlPath.getUrlBabyImg() + id + ".jpg";  //확장자 대소문자 구별함.
            Log.i(TAG, imgUrl);
            Picasso.with(mContext).load(imgUrl).into(_profile);
            _name.setText(name);
            _birthday.setText(birthday);
            if (sex.equals("1")) {
                _boy.setChecked(true);
            } else {
                _girl.setChecked(true);
            }
        } catch (JSONException e) {
            showToast("등록된 아기가 없습니다.");
            e.printStackTrace();
        }
    }

    private void findViewsById(View v) {  // 위젯 세팅

        _btn_save = v.findViewById(R.id.btn_save);
        _btn_delete = v.findViewById(R.id.btn_delete);
        _btn_parents = v.findViewById(R.id.btn_parents);
        _profile = v.findViewById(R.id.iv_profile);
        _name = v.findViewById(R.id.et_name);
        _boy = v.findViewById(R.id.rd_boy);
        _girl = v.findViewById(R.id.rd_girl);
        _birthday = v.findViewById(R.id.tv_birthday);
        _btn_back = v.findViewById(R.id.btn_exit);
    }

        @Override
    public void onClick(View v) {
        if(v==_btn_save) {
//            save_data();
            modify_data();
        }else if(v==_btn_parents){
            Intent intent = new Intent(getActivity(),ParentsActivity.class);
            intent.putExtra("baby_id", baby_id);
            startActivityForResult(intent, 1000);
        }else if(v==_birthday){
            Calendar c=Calendar.getInstance();
            int year=c.get(Calendar.YEAR);
            int month=c.get(Calendar.MONTH);
            int day=c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog=new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    _birthday.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                }
            },year, month, day);

            datePickerDialog.show();
        }else if(v==_profile){
            insert_picture();
        }else if(v==_btn_delete){
            deleteAelrtDialog();
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
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        delete_data();
//                        MybabyFragment fragment = new MybabyFragment();
//                        AppCompatActivity activity = (AppCompatActivity)getActivity();
//                        activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.enter_from_right).replace(R.id.frame, fragment).addToBackStack(null).commit();
//
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private  void insert_picture(){
        Log.i(TAG, "이미지클릭");

        final AlertDialog.Builder build = new AlertDialog.Builder( // 다이얼로그
                getActivity());
        build.setTitle("프로필 사진 등록")
                .setMessage("프로필 사진을 등록을 원하시면 \n\n'등록'을 눌러주시기 바랍니다. ")
                .setPositiveButton("등록",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_PICK);
                                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                Log.i(TAG, "사진선택1");
                                startActivityForResult(intent, 4);
                                Log.i(TAG, "사진선택완료2");

                            }
                        })
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        }).show();
    }

    private  void modify_data(){

        String server_url = new UrlPath().getUrlPath() + "Pc_baby/modifyBaby";
        Log.i(TAG, server_url);

        RequestQueue postRequestQueue = Volley.newRequestQueue(mContext);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                modifyResponse(response);    // 결과값 받아와서 처리하는 부분
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "아기아이디는 " + baby_id);
                Log.e(TAG, error.getLocalizedMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if(baby_id != null) {
                    params.put("baby_id", baby_id);
                }
                params.put("owner", email);
                Log.i(TAG, "owner 는  " + email);
                params.put("babyname", _name.getText().toString());
//        Log.i(TAG, _birthday.getText().toString());
//        Log.i(TAG, _birthday.getText().toString().substring(0, 4)+_birthday.getText().toString().substring(5, 7)+_birthday.getText().toString().substring(8, 10));
                params.put("birthday", _birthday.getText().toString());
                if(_boy.isChecked()){
                    params.put("sex", "1");
                }else if(_girl.isChecked()){
                    params.put("sex", "2");
                }
                return params;
            }
        };
        postRequestQueue.add(postStringRequest);

    }

    private void modifyResponse(String response){
        Log.i(TAG, "결과값은 " + response);
//        if (getArguments() == null) {    //아기 신규등록이면
            showToast( getString(R.string.save));
        MybabyActivity mf = new MybabyActivity();
//        }else{
//            showToast(getString(R.string.savefail));
//        }
    }



    private  void delete_data(){

        String server_url = new UrlPath().getUrlPath() + "Pc_baby/deleteBaby";
        Log.i(TAG, server_url);

        RequestQueue postRequestQueue = Volley.newRequestQueue(mContext);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                deleteResponse(response);    // 결과값 받아와서 처리하는 부분
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "아기아이디는 " + baby_id);
                Log.e(TAG, error.getLocalizedMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("baby_id", baby_id);
                params.put("email", email);

                return params;
            }
        };
        postRequestQueue.add(postStringRequest);

    }

    private void deleteResponse(String response){
        Log.i(TAG, "deleteResponse 결과값은 " + response);
        if(response.equals("true")) {
            showToast(getString(R.string.save));
//            MybabyActivity mf = new MybabyActivity();
//            AppCompatActivity activity = (AppCompatActivity) getActivity();
//            activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.enter_from_right).replace(R.id.frame, mf).addToBackStack(null).commit();
        }else{
            showToast(response);
        }
    }


//    private  void save_data(){
//
//        String server_url = new UrlPath().getUrlPath() + "Pc_baby/update";
//        //request parameters
//        Map<String, String> params = new HashMap<>();
//        params.put("owner", email);
//        params.put("baby_id", baby_id);
//        params.put("babyname", _name.getText().toString());
////        Log.i(TAG, _birthday.getText().toString());
////        Log.i(TAG, _birthday.getText().toString().substring(0, 4)+_birthday.getText().toString().substring(5, 7)+_birthday.getText().toString().substring(8, 10));
//        params.put("birthday", _birthday.getText().toString());
//        if(_boy.isChecked()){
//            params.put("sex", "1");
//        }else if(_girl.isChecked()){
//            params.put("sex", "2");
//        }
//
//
//        // Inflate the layout for this fragment
//        final RequestQueue queue = MyVolley.getInstance(getActivity()).getRequestQueue();
//        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, server_url, new JSONObject(params), networkSuccessListener(), networkErrorListener());
//
//        queue.add(myReq);
//    }
//
//    private Response.Listener<JSONObject> networkSuccessListener() {
//        return new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.i(TAG, "결과값은 " + response);
//                Boolean result = null;
//                try {
//                    result = response.getBoolean("result");
//                    if(result){
//                        showToast( getString(R.string.save));
//                        MybabyFragment mf = new MybabyFragment();
//                        AppCompatActivity activity = (AppCompatActivity)getActivity();
//                        activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left,R.anim.enter_from_right).replace(R.id.frame, mf).addToBackStack(null).commit();
//                    }else {
//                        showToast( getString(R.string.savefail));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//    }
//    private Response.ErrorListener networkErrorListener() {
//        return new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.i(TAG, error.getMessage());
//                showToast("Network Error");
//            }
//        };
//    }

    // 비트맵을 원하는 폴더에 사진파일로 저장하기
//    public static void saveBitmaptoJpeg(Bitmap bitmap,String folder, String name){
//        String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
//
//        Log.i(TAG, "데이터 딕셔너리의 경로는 " + Environment.getDataDirectory().getAbsolutePath());
//        // Get Absolute Path in External Sdcard
////		String foler_name = "/"+folder+"/";
//        String file_name = name+".jpg";
////		String string_path = ex_storage+foler_name;
//
//        String string_path = "/data/data/kr.co.jkcompany.heartforceset/files/";
//        Log.i(TAG, "string_path는  " + (string_path+file_name));
//        File file_path;
//        try{
//            file_path = new File(string_path);
//            if(!file_path.isDirectory()){
//                file_path.mkdirs();
//            }
//            FileOutputStream out = new FileOutputStream(string_path+file_name);
//
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//            out.close();
//
//        }catch(FileNotFoundException exception){
//            Log.e("FileNotFoundException", exception.getMessage());
//        }catch(IOException exception){
//            Log.e("IOException", exception.getMessage());
//        }
//    }

    public void onActivityResult(int requestCode, int resultCode,	Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        //Intent x = getActivity().getIntent();
//		if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) { // RESULT_OK 는 동작 성공을 의미하며 수치는 -1 인데, Fragment에는 없다.
        if (requestCode == 4 && resultCode == Activity.RESULT_OK) { // RESULT_OK 는 동작 성공을 의미하며 수치는 -1 인데, Fragment에는 없다.
//		if (resultCode == Activity.RESULT_OK) { // RESULT_OK 는 동작 성공을 의미하며 수치는 -1 인데, Fragment에는 없다.
// 따라서, Activity에서 사용되는 RESULT_OK값을 가져와서 사용한다.
            Log.i("onActivityResult", "request pick");
            beginCrop(imageReturnedIntent.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {   // Crop.REQUEST_CROP = 6709
            Log.i("onActivityResult", "request crop");
            handleCrop(resultCode, imageReturnedIntent, getActivity());
        } else {
            Log.i("onActivityResult", "Activity.requestCode 는 " + String.valueOf(requestCode) + " resultCode는 " + String.valueOf(resultCode));
        }
    }


        private void beginCrop(Uri source) {
            Log.d("beginCrop", "Start" +source.toString());
            Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
            Crop.of(source, destination).asSquare().start(getActivity(),this);
            Log.d("beginCrop", "End");

        }


    private void handleCrop(int resultCode, Intent result, Context ct) {
        if (resultCode == Activity.RESULT_OK) { // Activity 의 RESULT_OK값을 사용
            Log.d("handleCrop", "RESULT_OK" + (Crop.getOutput(result).toString()));
            _profile.setImageDrawable(null);
            _profile.setImageURI(Crop.getOutput(result));
            _profile.invalidate();

            final String absolutePath = Crop.getOutput(result).getPath();   // 쓰레드 내에서 사용할 변수는 final 로 정의 되어야 함.  uri 의 절대 경로는 uri.getPath()
            //파일 업로드 시작! 파일 업로드 call 할 때는 반드시 쓰레드 이용해야 함.
            new Thread(new Runnable() {
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {	}	});
                    Log.i(TAG, "파일명은 " + baby_id + " 업로드할 사진의 절대 경로 " + absolutePath);
                    upload.uploadFile(absolutePath, baby_id, "babyprofile");
                    //			saveBitmaptoJpeg(bitmap, "",loginInfo.getEmail());
                }
            }).start();

        } else if (resultCode == Crop.RESULT_ERROR) {
            Log.d("handleCrop", "RESULT_ERROR");
            Toast.makeText(getActivity(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
//Activity에서 사용되던 this는 Fragment에서 보통 getActivity() 또는 getContext() 로 변경해서 사용한다.
        }
    }

        private void showToast(String message){
        Toast toast=Toast.makeText(mContext.getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

}
