package com.du.nearby.nearbytest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.facebook.Profile;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.MessagesOptions;
import com.google.android.gms.nearby.messages.NearbyPermissions;
import com.google.android.gms.nearby.messages.SubscribeCallback;
import com.google.android.gms.nearby.messages.SubscribeOptions;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link) interface
 * to handle interaction events.
 * Use the {@link mainFragment#} factory method to
 * create an instance of this fragment.
 */
public class mainFragment extends Fragment
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "nearby_test";
    GoogleApiClient mGoogleApiClient;
    Message mActiveMessage;
    MessageListener mMessageListener;
    List<String> Messages;

    ListView lstView;
    Button sndButton;
    EditText msgTxt;
    Context context;
    Profile profile;

    public mainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        lstView = (ListView) rootView.findViewById(R.id.listView);
        sndButton = (Button) rootView.findViewById(R.id.button);
        msgTxt = (EditText) rootView.findViewById(R.id.editText);

        Messages = new ArrayList<String>();
        profile = Profile.getCurrentProfile();

        //Messages.add(profile.getName());
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.list_view, Messages);
        lstView.setAdapter(adapter);

        sndButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String msg = msgTxt.getText().toString();
                Log.d(TAG, msg);
                publish(msg);
                Messages.add(msg);
                adapter.notifyDataSetChanged();
                Log.d(TAG, "Button  click with message: " + msg);
            }
        });
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(Nearby.MESSAGES_API, new MessagesOptions.Builder()
                            .setPermissions(NearbyPermissions.BLE)
                            .build())
                    .addConnectionCallbacks(this)
                    .enableAutoManage(getActivity(), this)
                    .build();
        } else {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(Nearby.MESSAGES_API)
                    .addConnectionCallbacks(this)
                    .enableAutoManage(getActivity(), this)
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
        Log.i(TAG, "Finished create ");


        // Inflate the layout for this fragment
        Log.d(TAG, "on create view: " );
        //return inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;


    }

    @Override
    public void onStop() {
        unpublish();
        unsubscribe();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        publish(profile.getName());
        subscribe();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void publish(String message) {
        Log.i(TAG, "Publishing message: " + message);

        mActiveMessage = new Message(message.getBytes());
        Nearby.Messages.publish(mGoogleApiClient, mActiveMessage);
    }

    private void unpublish() {
        Log.i(TAG, "Unpublishing.");
        if (mActiveMessage != null) {
            Nearby.Messages.unpublish(mGoogleApiClient, mActiveMessage);
            mActiveMessage = null;
        }
    }


    private void subscribe() {
        Log.i(TAG, "Subscribing.");


        SubscribeCallback SubCallback = new SubscribeCallback() {

            @Override
            public void onExpired() {
                super.onExpired();
            }
        };

        SubscribeOptions options = new SubscribeOptions.Builder()
                .setCallback(SubCallback)
                .build();

        Nearby.Messages.subscribe(mGoogleApiClient, mMessageListener, options);

    }

    private void unsubscribe() {
        Log.i(TAG, "Unsubscribing.");
        Nearby.Messages.unsubscribe(mGoogleApiClient, mMessageListener);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
