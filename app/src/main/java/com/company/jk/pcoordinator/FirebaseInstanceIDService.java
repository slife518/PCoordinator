package com.company.jk.pcoordinator;


import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;


public class FirebaseInstanceIDService extends FirebaseMessagingService {

    private  static  final String TAG = "FIDService";


    @Override
    public void onNewToken(String token) {
        sendRegisterationToServer(token);
    }

    private void sendRegisterationToServer(String token){
            Log.i(TAG, "해당 토큰은 "+token);
    }

}
