package com.du.nearby.Clozer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by arielamar123 on 18/05/2017.
 */

public class connectedPeopleAdapter extends RecyclerView.Adapter<ConnectedPeopleHolder>{


    private List<String> connectedPeople;

    public connectedPeopleAdapter(List<String> connectedPeople){
        this.connectedPeople = connectedPeople;
    }


    @Override
    public ConnectedPeopleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.one_person, parent, false);
        return new ConnectedPeopleHolder(view);
    }

    @Override
    public void onBindViewHolder(ConnectedPeopleHolder holder, int position) {
        holder.setData(connectedPeople.get(position));
    }

    @Override
    public int getItemCount() {
        return connectedPeople.size();
    }

}
