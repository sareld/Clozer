package com.du.nearby.Clozer;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;


import com.facebook.Profile;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.facebook.FacebookSdk;

import java.util.List;


public class MainActivity extends FragmentActivity{


    private static final String TAG = "nearby_test";
    GoogleApiClient mGoogleApiClient;
    Message mActiveMessage;
    MessageListener mMessageListener;
    List<String> Messages;
    Adapter adapter;

    private mainFragment mainFragment;
    private loginFragment loginFragment;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        Profile profile = Profile.getCurrentProfile();

        Log.d(TAG, "setContentView" );

        loginFragment = new loginFragment();

        getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, loginFragment).commit();


    }


}

