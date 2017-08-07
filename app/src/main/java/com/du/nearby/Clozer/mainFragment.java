package com.du.nearby.Clozer;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Profile;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.lang.reflect.Method;
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
public class mainFragment extends Fragment {

    private static final String TAG = "nearby_test";
    public static int REQUEST_BLUETOOTH = 1;
    public static int REQUEST_NEW_ROOM = 2;

    GoogleApiClient mGoogleApiClient;
    BluetoothAdapter BTAdapter;
    BluetoothLeScanner BTscanner;

    Message mActiveMessage;
    MessageListener mMessageListener;
    ArrayList<String> Rooms;
    ArrayAdapter<String> adapter;
    IntentFilter intFilter;


    ListView lstView;
    Button AddGroupButton;
    Context context;
    Profile profile;
    FragmentTransaction ft;
    Boolean BTWasOn;

    public mainFragment() {
        // Required empty public constructor
    }

    private void init_bluetooth(){
        BTAdapter = BluetoothAdapter.getDefaultAdapter();
        // Phone does not support Bluetooth so let the user know and exit.
        if (BTAdapter == null) {
            new AlertDialog.Builder(context)
                    .setTitle("Not compatible")
                    .setMessage("Your phone does not support Bluetooth")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }else {


            intFilter = new IntentFilter();
            intFilter.addAction(BluetoothDevice.ACTION_FOUND);
            intFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            intFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            intFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            getActivity().registerReceiver(bReciever, intFilter);

            getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number

            getActivity().registerReceiver(bReciever, intFilter);





            if (!BTAdapter.isEnabled()) {
                BTWasOn = false;
                BTAdapter.enable();
            }else {
                BTWasOn = true;
            }

            if(BTAdapter.getScanMode() == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
                BTAdapter.getBluetoothLeAdvertiser()
            }
        }

    }

    private void add_group(String GroupName){
        if (!Rooms.contains(GroupName)){
            Rooms.add(GroupName);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_NEW_ROOM) {
            if(resultCode == Activity.RESULT_OK){
                Bundle res = data.getExtras();
                String newRoom = res.getString("result");
                Log.d(TAG, "New Group: " + newRoom+" by user:  " + UserName);
                //TODO: create_room_in_server(BT, RoomName);
                Rooms.add(newRoom);

                if (BTAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                    Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
                    startActivity(discoverableIntent);
                }

                //CurrentRoom = result;

                //ft = getFragmentManager().beginTransaction();
                //ft.addToBackStack("xyz");
                //ft.hide(mainFragment.this);
                //ft.add(android.R.id.content,new ChatRoom());
                //ft.commit();

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult


    private final BroadcastReceiver bReciever = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Create a new device item
                String newDevice = device.getAddress();
                //todo: Get room name by BT device Addrees from server.
                // Add it to our adapter
                if(!Rooms.contains(newDevice)) {
                    Rooms.add(newDevice);
                    adapter.notifyDataSetChanged();
                }


            }
            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                    adapter.notifyDataSetChanged();
                    scan_bluetooth();

            }
            if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
            {
                        Rooms.clear();
            }
            if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
                            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                                    BluetoothAdapter.ERROR);
                            switch (state) {
                                case BluetoothAdapter.STATE_ON:
                                    scan_bluetooth();
                                    break;
                        }
            }



        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        lstView = (ListView) rootView.findViewById(R.id.listView);
        FloatingActionButton addGroupBtn = (FloatingActionButton) rootView.findViewById(R.id.fab);

        TextView title = (TextView) rootView.findViewById(R.id.title_rooms);
        Typeface custom_font = Typeface.createFromAsset(context.getAssets(),"fonts/ForoMedium.ttf");
        title.setTypeface(custom_font);

        Rooms = new ArrayList<String>();
        //profile = Profile.getCurrentProfile();

        //Rooms.add(profile.getName());
        adapter = new ArrayAdapter<String>(context, R.layout.list_view, Rooms);
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
                startActivityForResult(intent,REQUEST_NEW_ROOM);
            }
        });

        init_bluetooth();

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
    public void onResume() {
        super.onResume();

        //init_bluetooth();
        if(BTAdapter.isEnabled()){
            BTWasOn = true;
            scan_bluetooth();
        }else {
            BTWasOn = false;
            BTAdapter.enable();
        }


    }


    @Override
    public void onStop() {
        unpublish();
        unsubscribe();
        super.onStop();
    }



    public void publish(String message) {
        Log.i(TAG, "Publishing message: " + message);

        mActiveMessage = new Message(message.getBytes());
        Nearby.Messages.publish(mGoogleApiClient, mActiveMessage);
    }

    private void unpublish() {
        Log.i(TAG, "Unpublishing.");
        //if (mActiveMessage != null) {
        //    Nearby.Messages.unpublish(mGoogleApiClient, mActiveMessage);
         //   mActiveMessage = null;
       // }
    }


    private void scan_bluetooth() {
        if(BTAdapter.isEnabled() && !BTAdapter.isDiscovering() ) {
            if (BTAdapter.startDiscovery()) {
                Log.d(TAG, "start Discovery");
            }
        }

    }

    private void unsubscribe() {
        Log.i(TAG, "Unsubscribing.");
        BTAdapter.cancelDiscovery();
        if(!BTWasOn){
            BTAdapter.disable();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unregisterReceiver(bReciever);
    }


}
