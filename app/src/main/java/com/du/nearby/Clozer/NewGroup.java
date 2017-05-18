package com.du.nearby.Clozer;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NewGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        final Button CreateBtn = (Button)findViewById(R.id.button2);
        final EditText GroupNameEdit = (EditText) findViewById(R.id.editText2);

        CreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String GroupName = GroupNameEdit.getText().toString();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",GroupName);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });


    }
}
