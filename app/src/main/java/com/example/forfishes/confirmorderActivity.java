package com.example.forfishes;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.forfishes.Fish.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class confirmorderActivity<onActivityResult> extends AppCompatActivity
{
    private EditText name,phone,address,pincode;
    private Button placeorderbtn;
    private EditText disstate;
    private int a=0;
    private String totalamount="";
    private TextView totalpriceamountS;
    private DatabaseReference userreference;
   private DatabaseReference productReferances;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmorder);
        totalamount=getIntent().getStringExtra("Total Price");
        placeorderbtn = (Button)findViewById(R.id.placeorder);
        name=(EditText)findViewById(R.id.shimentnametext);




        totalpriceamountS=(TextView)findViewById(R.id.totalpriceamounts);
        userreference= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineuser.getPhone());
        disstate=(EditText)findViewById(R.id.displaystate);
        phone=(EditText)findViewById(R.id.shippingphonenumber);
        address=(EditText)findViewById(R.id.shippingaddress);
       productReferances= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineuser.getPhone());
        pincode=(EditText)findViewById(R.id.shippingpincode);

        displayproductinfo();
        placeorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkmethod();
            }
        });
    }


    private void displayproductinfo()
    {
        totalpriceamountS.setText(totalamount);
        productReferances.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {

                    name.setText(dataSnapshot.child("name").getValue().toString());
                    phone.setText(dataSnapshot.child("phone").getValue().toString());
                    address.setText(dataSnapshot.child("address").getValue().toString());
                    pincode.setText(dataSnapshot.child("pincode").getValue().toString());
                    disstate.setText(dataSnapshot.child("State").getValue().toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        Toast.makeText(confirmorderActivity.this, "Your order has been placed successfully", Toast.LENGTH_SHORT).show();
        Intent intent= new Intent(confirmorderActivity.this , paymentActivity.class);
        intent.putExtra("names",name.getText().toString());
        intent.putExtra("phones",phone.getText().toString());
        intent.putExtra("totalamount",totalamount);
        intent.putExtra("addresses",address.getText().toString());
        intent.putExtra("states",disstate.getText().toString());
        intent.putExtra("pincodes",pincode.getText().toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity(intent);
     // confirmation();

    }
}

/*
void confirmation()
{
   final String savecurrentDate, savecurrentTime;
    Calendar calfordate=Calendar.getInstance();
    SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, yyyy");
    savecurrentDate= currentdate.format(calfordate.getTime());


    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
    savecurrentTime= currentdate.format(calfordate.getTime());
   final String rands= savecurrentDate+savecurrentTime;

    final DatabaseReference ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders")
            .child(Prevalent.currentOnlineuser.getPhone());
    HashMap<String ,Object> ordersMap=new HashMap<>();
    ordersMap.put("totalamount", totalamount);
    ordersMap.put("name", name.getText().toString());
    ordersMap.put("phone", phone.getText().toString());
    ordersMap.put("address",address.getText().toString());
    ordersMap.put("pincode", pincode.getText().toString());
    ordersMap.put("State",disstate.getText().toString());
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
                                    Intent intent= new Intent(confirmorderActivity.this , paymentActivity.class);

                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                    startActivity(intent);
                                }
                            }
                        });
            }
        }
    });
    final DatabaseReference ordersRefs= FirebaseDatabase.getInstance().getReference().child("Orders1")
            .child(Prevalent.currentOnlineuser.getPhone());
    HashMap<String ,Object> ordersMaps=new HashMap<>();

    ordersMaps.put("totalamount", totalamount);
    ordersMaps.put("name", name.getText().toString());
    ordersMaps.put("phone", phone.getText().toString());
    ordersMaps.put("address",address.getText().toString());
    ordersMaps.put("pincode", pincode.getText().toString());
    ordersMaps.put("date", savecurrentDate);
    ordersMaps.put("time", savecurrentTime);
    ordersMaps.put("State",disstate.getText().toString());
    ordersMaps.put("state", "not shipped");
    ordersRefs.updateChildren(ordersMaps).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful())
            {
                HashMap<String ,Object> ordersMapp=new HashMap<>();
                ordersMapp.put("name", name.getText().toString());
                ordersMapp.put("phone", phone.getText().toString());
                ordersMapp.put("address",address.getText().toString());
                ordersMapp.put("pincode", pincode.getText().toString());
                ordersMapp.put("State",disstate.getText().toString());
                userreference.updateChildren(ordersMapp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                   if(task.isSuccessful())
                   {
                       Toast.makeText(confirmorderActivity.this, "Finished", Toast.LENGTH_SHORT).show();
                   }
                    }
                });

                Toast.makeText(confirmorderActivity.this, "Done", Toast.LENGTH_SHORT).show();
            }
        }
    });

}
*/



}

