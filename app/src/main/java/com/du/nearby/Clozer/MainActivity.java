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

        if(profile != null) {

            mainFragment = new mainFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, mainFragment).commit();
        }else {
            Log.d(TAG, "setContentView" );

            loginFragment = new loginFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, loginFragment).commit();

        /*
            final ListView lstView = (ListView) findViewById(R.id.listView);
            final Button AddGroupButton = (Button) findViewById(R.id.button);
            final EditText msgTxt = (EditText) findViewById(R.id.editText);
            Messages = new ArrayList<String>();
            Messages.add("asdfasdf");
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_view, Messages);
            lstView.setAdapter(adapter);

            AddGroupButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String msg = msgTxt.getText().toString();
                    Log.d(TAG, msg);
                    publish(msg);
                    Messages.add(msg);
                    adapter.notifyDataSetChanged();
                }
            });

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(Nearby.MESSAGES_API, new MessagesOptions.Builder()
                                .setPermissions(NearbyPermissions.BLE)
                                .build())
                        .addConnectionCallbacks(this)
                        .enableAutoManage(this, this)
                        .build();
            } else {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(Nearby.MESSAGES_API)
                        .addConnectionCallbacks(this)
                        .enableAutoManage(this, this)
                        .build();
            }

            mMessageListener = new MessageListener() {
                @Override
                public void onFound(Message message) {
                    String messageAsString = new String(message.getContent());
                    Log.d(TAG, "Found message: " + messageAsString);
                    Messages.add(messageAsString);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onLost(Message message) {
                    String messageAsString = new String(message.getContent());
                    Log.d(TAG, "Lost sight of message: " + messageAsString);
                }
            };
            Log.i(TAG, "Finished create ");*/
        }

    }


}

