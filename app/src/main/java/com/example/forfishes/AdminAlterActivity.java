package com.example.forfishes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminAlterActivity extends AppCompatActivity
{

    private Button applychanges,deleteproduct;
    private EditText name,price,description;
    private ImageView imageView;
    private String productID="";

    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_alter);

        deleteproduct=(Button)findViewById(R.id.deleteproduct);
        productID=getIntent().getStringExtra("pid");
        imageView=(ImageView)findViewById(R.id.product_images);
        productRef= FirebaseDatabase.getInstance().getReference().child("Products").child(productID);
        applychanges=(Button)findViewById(R.id.applychanges);
        name=(EditText)findViewById(R.id.product_names);
        price=(EditText)findViewById(R.id.product_prices);
        description=(EditText)findViewById(R.id.product_descriptions);

        displayproductinfo();

        deleteproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) 
            {
             deletethisproduct();   
            }
        });
        
        applychanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminAlterActivity.this, "Fine", Toast.LENGTH_SHORT).show();
                applychangesall();
            }
        });
    }

    private void deletethisproduct()
    {

        HashMap<String,Object> proMap= new HashMap<>();

        proMap.put("description","Temporarily unavailable");
        proMap.put("pname","Temporarily unavailable");
        proMap.put("price","000");
        productRef.updateChildren(proMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(AdminAlterActivity.this, "Product Deleted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminAlterActivity.this, AdminProductActivity.class);
                    startActivity(intent);
                }
            }
        });


    }


    private void applychangesall()
    {
        String uname=name.getText().toString();
        String uprice= price.getText().toString();
        String udescription = description.getText().toString();

        if(uname.equals(""))
        {
            Toast.makeText(this, "Enter Product name", Toast.LENGTH_SHORT).show();
        }
        else if (uprice.equals(""))
        {
            Toast.makeText(this, "Enter Product price", Toast.LENGTH_SHORT).show();
        }
        else  if(udescription.equals(""))
        {
            Toast.makeText(this, "Enter Product description", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String ,Object> productMap = new HashMap<>();
            productMap.put("pid",productID);
            productMap.put("description",udescription);
            productMap.put("price",uprice);
            productMap.put("pname",uname);

            productRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                if(task.isSuccessful())
                {
                    Toast.makeText(AdminAlterActivity.this, "Changes applied successfully", Toast.LENGTH_SHORT).show();

                    Intent intent= new Intent(AdminAlterActivity.this,AdminProductActivity.class);
                    startActivity(intent);
                    finish();
                }
                }
            });

        }

    }


    private void displayproductinfo()
    {
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String p1name= dataSnapshot.child("pname").getValue().toString();
                    String p1price= dataSnapshot.child("price").getValue().toString();
                    String p1descripion= dataSnapshot.child("description").getValue().toString();
                    String p1image= dataSnapshot.child("image").getValue().toString();

                    name.setText(p1name);
                    price.setText(p1price);
                    description.setText(p1descripion);
                    Picasso.get().load(p1image).into(imageView);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
