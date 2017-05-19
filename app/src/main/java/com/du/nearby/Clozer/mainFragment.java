package com.du.nearby.Clozer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import static com.du.nearby.Clozer.MainActivity.CurrentRoom;
import static com.du.nearby.Clozer.MainActivity.UserName;


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
    ArrayList<String> Groups;


    ListView lstView;
    Button AddGroupButton;
    Context context;
    Profile profile;
    FragmentTransaction ft;

    public mainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Bundle res = data.getExtras();
                String result = res.getString("result");
                Log.d(TAG, "New Group: " + result+" by user:  " + UserName);
                CurrentRoom = result;

                ft = getFragmentManager().beginTransaction();
                //ft.addToBackStack("xyz");
                ft.hide(mainFragment.this);
                ft.add(android.R.id.content,new ChatRoom());
                ft.commit();

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        lstView = (ListView) rootView.findViewById(R.id.listView);
        FloatingActionButton addGroupBtn = (FloatingActionButton) rootView.findViewById(R.id.fab);

        TextView title = (TextView) rootView.findViewById(R.id.title_rooms);
        Typeface custom_font = Typeface.createFromAsset(context.getAssets(),"fonts/ForoMedium.ttf");
        title.setTypeface(custom_font);

        Groups = new ArrayList<String>();
        profile = Profile.getCurrentProfile();

        //Groups.add(profile.getName());
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.list_view, Groups);
        lstView.setAdapter(adapter);

        lstView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) lstView.getItemAtPosition(position);

                CurrentRoom = item;
                publish(CurrentRoom);
                ft = getFragmentManager().beginTransaction();
                //ft.addToBackStack("xyz");
                ft.hide(mainFragment.this);
                ft.add(android.R.id.content,new ChatRoom());
                ft.commit();

            }
        });

        addGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewGroup.class);
                startActivityForResult(intent,1);


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
                if(!Groups.contains(messageAsString)) {
                    Groups.add(messageAsString);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onLost(Message message) {
                String messageAsString = new String(message.getContent());
                if(Groups.contains(messageAsString)) {
                    Groups.remove(Groups.indexOf(messageAsString));
                    adapter.notifyDataSetChanged();
                }
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
        if(CurrentRoom !="") {
            publish(CurrentRoom);
        }
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
