package com.du.nearby.Clozer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment_users must implement the
 * {@link loginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link loginFragment#newInstance} factory method to
 * create an instance of this fragment_users.
 */
public class loginFragment extends Fragment {

    private static final String TAG = "nearby_test";
    private LoginButton loginButton;
    CallbackManager callbackManager;
    ProfileTracker profileTracker;
    Context context;
    FragmentTransaction ft;

    public loginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();

    }



    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        Log.d("loginFragment", "on create view: " );
        View view = inflater.inflate(R.layout.fragment_login, container, false);



        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        // If using in a fragment_users
        loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback <LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "login success");

                ft = getFragmentManager().beginTransaction();
                //ft.addToBackStack("xyz");
                ft.hide(loginFragment.this);
                ft.add(android.R.id.content,new mainFragment());
                ft.commit();
            }

            @Override
            public void onCancel() {

                Log.d(TAG, "login Cancel");
            }

            @Override
            public void onError(FacebookException exception) {

                Log.d(TAG, "login Error"+ exception.getLocalizedMessage());
            }
        });


        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        Log.d(TAG, "Activity result");
        callbackManager.onActivityResult(requestCode, responseCode, intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
