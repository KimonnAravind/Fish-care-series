package com.example.forfishes;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

public class OrderStatusActivity extends AppCompatActivity
{
    DatabaseReference ordersreference,ordersreference1,ordersreference2,orderrefeerence3;
    private ImageView bille;
    private Button elabrate;
    private TextView one,two,three,four;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        ordersreference= FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevalent.currentOnlineuser.getPhone());
        ordersreference1=FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View")
                .child(Prevalent.currentOnlineuser.getPhone());
        ordersreference2=FirebaseDatabase.getInstance().getReference().child("Mine")
                .child(Prevalent.currentOnlineuser.getPhone());
        orderrefeerence3=FirebaseDatabase.getInstance().getReference().child("Mine").child(Prevalent.currentOnlineuser.getPhone());
        bille=(ImageView)findViewById(R.id.biller);


        one=(TextView)findViewById(R.id.orderHistor);
        two=(TextView)findViewById(R.id.currenthistory);
        three=(TextView)findViewById(R.id.currenthistory1);
        four=(TextView)findViewById(R.id.currenthistory2);
        ordersreference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
            if(dataSnapshot.exists())
            {
                Toast.makeText(OrderStatusActivity.this, "In mine", Toast.LENGTH_SHORT).show();
                ordersreference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.exists())
                        {
                                three.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            ordersreference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                {
                                if(dataSnapshot.exists())
                                {
                                    two.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    bille.setVisibility(View.VISIBLE);
                                    orderrefeerence3.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                        {
                                            if(dataSnapshot.child("tracking details").exists())
                                            {
                                                String image = dataSnapshot.child("tracking details").getValue().toString();
                                                Picasso.get().load(image).into(bille);
                                            }
                                            else
                                            {
                                                Toast.makeText(OrderStatusActivity.this, "Unavailable", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    four.setVisibility(View.VISIBLE);


                                }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            else
            {
                one.setVisibility(View.VISIBLE);
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
