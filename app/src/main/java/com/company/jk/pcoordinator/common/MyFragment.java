package com.company.jk.pcoordinator.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.company.jk.pcoordinator.http.NetworkUtil;

public class MyFragment extends Fragment {
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
}
