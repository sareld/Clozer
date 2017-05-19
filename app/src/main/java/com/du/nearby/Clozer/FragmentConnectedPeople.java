package com.du.nearby.Clozer;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arielamar123 on 18/05/2017.
 */

public class FragmentConnectedPeople extends mainFragment{
   // FragmentConnectedPeople.newInstance(Users);

    private static final String LIST = "connectedPeople";
    private ArrayList<String> connectedPeople= new ArrayList<>();

    public static FragmentConnectedPeople newInstance(List<String> connectedPeople) {

        Bundle args = new Bundle();
        args.putStringArrayList(LIST, (ArrayList<String>) connectedPeople);

        FragmentConnectedPeople fragment = new FragmentConnectedPeople();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        final RecyclerView recyclerV = (RecyclerView) view.findViewById(R.id.recyclerView);
        if(getArguments()!= null){
            connectedPeople = getArguments().getStringArrayList(LIST);
        }


        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerV.setLayoutManager(linearLayoutManager);
        connectedPeopleAdapter adapter = new connectedPeopleAdapter(connectedPeople);
        recyclerV.setAdapter(adapter);
        return view;

    }
}
