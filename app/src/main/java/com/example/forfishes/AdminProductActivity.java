package com.example.forfishes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminProductActivity extends AppCompatActivity {
    private ImageView fish,accessories,food;
    private Button placedorderbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);


        placedorderbtn=(Button)findViewById(R.id.placedorder1);
        fish=(ImageView) findViewById(R.id.fish);
        accessories=(ImageView) findViewById(R.id.aquariumaccessories);
        food=(ImageView)findViewById(R.id.food);
        fish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminProductActivity.this, AdminActivity.class);
                intent.putExtra("category", "fish");
                startActivity(intent);

            }
        });

        accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminProductActivity.this, AdminActivity.class);
                intent.putExtra("category", "accessoories");
                startActivity(intent);

            }
        });

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminProductActivity.this, AdminActivity.class);
                intent.putExtra("category", "food");
                startActivity(intent);

            }
        });
        placedorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminProductActivity.this, placedordersActivity.class);
                        startActivity(intent);
            }
        });


    }
}
