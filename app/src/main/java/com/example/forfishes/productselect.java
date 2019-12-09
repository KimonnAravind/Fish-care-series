package com.example.forfishes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.forfishes.Fish.Prevalent.Prevalent;
import com.example.forfishes.Model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class productselect extends AppCompatActivity {
    private TextView qty;
    private Button incr, decr;
    private Button addtocart;
    private ImageView productimage;
    private  TextView productname, productdescription,productprice;
    int a=0;
    private String str="";
    private String productID="";
    private String temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productselect);


        addtocart=(Button)findViewById(R.id.addproducttocart);
        productID=getIntent().getStringExtra("pid");
        productimage=(ImageView)findViewById(R.id.productImagedetails);
        productname=(TextView)findViewById(R.id.productnamedetailstext) ;
        productdescription=(TextView)findViewById(R.id.productdescriptiondetailstext) ;
        productprice=(TextView)findViewById(R.id.productpricedetailstext) ;
        qty=(TextView) findViewById(R.id.quantity);
        incr=(Button)findViewById(R.id.Increment);
        decr=(Button)findViewById(R.id.Decrement);
        temp=String.valueOf(a);
        qty.setText(temp);
        getproductdetails(productID);
        incr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a=a+1;
                incranddecr();
            }
        });
        decr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                a=a-1;
                incranddecr();
            }
        });
        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingtocartList();

            }
        });
    }

    private void addingtocartList()
    {
        String savecurrentTime, savecurrentDate;

        Calendar calfordate=Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, yyyy");
        savecurrentDate= currentdate.format(calfordate.getTime());


        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        savecurrentTime= currentdate.format(calfordate.getTime());

        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String , Object> cartMap =new HashMap<>();
        cartMap.put("pid", productID);

        cartMap.put("pname", productname.getText().toString());
        cartMap.put("price", productprice.getText().toString());
        cartMap.put("date", savecurrentDate);
        cartMap.put("time", savecurrentTime);
        cartMap.put("quantity",qty.getText());
        cartMap.put("discount", "");
        cartMap.put("imagelink",str);
        cartListRef.child("User View").child(Prevalent.currentOnlineuser.getPhone())
                .child("Products").child(productID).updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            cartListRef.child("Admin View").child(Prevalent.currentOnlineuser.getPhone())
                                    .child("Products").child(productID).updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                cartListRef.child("My Orders").child(Prevalent.currentOnlineuser.getPhone())
                                                        .child("Products").child(productID).updateChildren(cartMap)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task)
                                                            {
                                                                Toast.makeText(productselect.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(productselect.this, endusers.class);
                                                                startActivity(intent);
                                                            }
                                                        });

                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void getproductdetails(final String productID) {
        DatabaseReference produtRef= FirebaseDatabase.getInstance().getReference().child("Products");
        produtRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Products products= dataSnapshot.getValue(Products.class);
                    productname.setText(products.getPname());
                    productdescription.setText(products.getDescription());
                    productprice.setText(products.getPrice());
                    Picasso.get().load(products.getImage()).into(productimage);
                    String p1=dataSnapshot.child("image").getValue().toString();
                    str=p1;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void incranddecr() {
        temp=String.valueOf(a);
        qty.setText(temp);
    }
}
