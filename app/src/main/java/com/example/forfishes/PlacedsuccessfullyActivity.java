package com.example.forfishes;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PlacedsuccessfullyActivity extends AppCompatActivity {
    private Button lastbut;
    private TextView simpy;
    private Layout first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placedsuccessfully);

        lastbut=(Button)findViewById(R.id.ioi);
        simpy=(TextView)findViewById(R.id.textview);

        lastbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickings();
            }
        });
        simpy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickings();
            }
        });



    }

    private void clickings()
    {
        Intent intent = new Intent(PlacedsuccessfullyActivity.this, endusers.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clickings();
    }
}
