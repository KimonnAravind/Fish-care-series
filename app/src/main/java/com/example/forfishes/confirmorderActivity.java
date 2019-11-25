package com.example.forfishes;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.forfishes.Fish.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class confirmorderActivity extends AppCompatActivity {
    private EditText name,phone,address,pincode;
    private Button placeorderbtn;
    private String totalamount="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmorder);
        totalamount=getIntent().getStringExtra("Total Price");
        placeorderbtn = (Button)findViewById(R.id.placeorder);
        name=(EditText)findViewById(R.id.shimentnametext);
        phone=(EditText)findViewById(R.id.shippingphonenumber);
        address=(EditText)findViewById(R.id.shippingaddress);
        pincode=(EditText)findViewById(R.id.shippingpincode);

        placeorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkmethod();
            }
        });
    }

private void checkmethod()
{
    if(TextUtils.isEmpty(name.getText().toString()))
    {
        Toast.makeText(this, "Please enter your name for delivery", Toast.LENGTH_SHORT).show();
    }
   else if(TextUtils.isEmpty(phone.getText().toString()))
    {
        Toast.makeText(this, "Please enter your phone number for delivery", Toast.LENGTH_SHORT).show();
    }
    else if(TextUtils.isEmpty(address.getText().toString()))
    {
        Toast.makeText(this, "Please enter your address for delivery", Toast.LENGTH_SHORT).show();
    }
    else if(TextUtils.isEmpty(pincode.getText().toString()))
    {
        Toast.makeText(this, "Please enter your pincode for delivery", Toast.LENGTH_SHORT).show();
    }
    else
    {
        confirmation();
    }
}

private void confirmation()
{
   final String savecurrentDate, savecurrentTime;
    Calendar calfordate=Calendar.getInstance();
    SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, yyyy");
    savecurrentDate= currentdate.format(calfordate.getTime());


    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
    savecurrentTime= currentdate.format(calfordate.getTime());

    final DatabaseReference ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders")
            .child(Prevalent.currentOnlineuser.getPhone());
    HashMap<String ,Object> ordersMap=new HashMap<>();

    ordersMap.put("totalamount", totalamount);
    ordersMap.put("name", name.getText().toString());
    ordersMap.put("phone", phone.getText().toString());
    ordersMap.put("Address",address.getText().toString());
    ordersMap.put("Pincode", pincode.getText().toString());
    ordersMap.put("date", savecurrentDate);
    ordersMap.put("time", savecurrentTime);
    ordersMap.put("state", "not shipped");

    ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {

            if(task.isSuccessful())
            {
                FirebaseDatabase.getInstance().getReference()
                        .child("Cart List")
                        .child("User View")
                        .child(Prevalent.currentOnlineuser.getPhone())
                        .removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(confirmorderActivity.this, "Your order has been placed successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent= new Intent(confirmorderActivity.this , endusers.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                    startActivity(intent);
                                }

                            }
                        });

            }
        }
    });




}
}

