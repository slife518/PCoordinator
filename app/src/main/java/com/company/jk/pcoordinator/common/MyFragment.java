package com.company.jk.pcoordinator.common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.company.jk.pcoordinator.http.NetworkUtil;

public class MyFragment extends Fragment implements EditText.OnEditorActionListener{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        NetworkUtil.getConnectivityStatusBoolean(getContext());
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        NetworkUtil.getConnectivityStatusBoolean(getContext());
        super.onResume();
    }

    protected void showToast(String message){
        Toast toast=Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if(actionId== EditorInfo.IME_ACTION_DONE){
            //Clear focus here from edittext
            textView.clearFocus();
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow( textView.getWindowToken(), 0);
        }
        return false;
    }




}
