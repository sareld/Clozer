package com.du.nearby.Clozer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by arielamar123 on 18/05/2017.
 */

public class ConnectedPeopleHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.person_name)
    TextView textView;

    public ConnectedPeopleHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


    public void setData(String txt){
        textView.setText(txt);
    }
}
