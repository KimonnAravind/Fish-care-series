package com.example.forfishes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminProductActivity extends AppCompatActivity {
    private ImageView fish,accessories,food,Medicine;
    private Button placedorderbtn;
    private Button alterdata,LogoutBTN,schargesandupi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);

        schargesandupi=(Button)findViewById(R.id.schargesandupi);
        placedorderbtn=(Button)findViewById(R.id.placedorder1);
        fish=(ImageView) findViewById(R.id.fish);
        accessories=(ImageView) findViewById(R.id.aquariumaccessories);
        food=(ImageView)findViewById(R.id.food);
        Medicine=(ImageView)findViewById(R.id.medicine);
        alterdata=(Button)findViewById(R.id.alterdata);
        LogoutBTN=(Button)findViewById(R.id.LogoutBTN);

        schargesandupi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(AdminProductActivity.this,chargesandupi.class);
            startActivity(intent);
            }
        });
        alterdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminProductActivity.this , endusers.class );
                intent.putExtra("Admin","Admin");

                startActivity(intent);

            }
        });

        LogoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminProductActivity.this , MainActivity.class );
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        fish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminProductActivity.this, AdminActivity.class);
                intent.putExtra("category", "Combos");
                startActivity(intent);

            }
        });

        Medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminProductActivity.this, AdminActivity.class);
                intent.putExtra("category", "Fish_Medicine");
                startActivity(intent);
            }
            });

        accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminProductActivity.this, AdminActivity.class);
                intent.putExtra("category", "Aquarium_Accessories");
                startActivity(intent);

            }
        });

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminProductActivity.this, AdminActivity.class);
                intent.putExtra("category", "Fish_Food");
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
