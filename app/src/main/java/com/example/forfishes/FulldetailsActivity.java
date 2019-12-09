package com.example.forfishes;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forfishes.Model.cart;
import com.example.forfishes.viewholder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class FulldetailsActivity extends AppCompatActivity {

    private RecyclerView cartList;
    private String userID= "";
    private Button shipped;
    private String uuid="";
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartlistRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fulldetails);

        shipped=(Button)findViewById(R.id.remove);
        userID=getIntent().getStringExtra("uid");

        cartList= findViewById(R.id.placedorders);
        cartList.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        cartList.setLayoutManager(layoutManager);

        cartlistRef= FirebaseDatabase.getInstance().getReference()
                .child("Cart List").child("My Orders").child(userID).child("Products");


    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<cart>options=
                new FirebaseRecyclerOptions.Builder<cart>()
                        .setQuery(cartlistRef,cart.class)
                        .build();
        FirebaseRecyclerAdapter<cart, CartViewHolder> adapter= new FirebaseRecyclerAdapter<cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, final int position, @NonNull cart model)
            {

                holder.txtproductid.setText(""+ model.getImagelink());
                String newss= holder.txtproductid.getText().toString();
                Toast.makeText(FulldetailsActivity.this, newss, Toast.LENGTH_SHORT).show();
                Picasso.get().load(newss).into(holder.imageofit);
                holder.txtproductquantity.setText("Quantity ="+model.getQuantity());
                holder.txtProductname.setText(model.getPname());
                holder.txtproductprice.setText("Price "+model.getPrice());


                shipped.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Yes",
                                        "No"

                                };
                        AlertDialog.Builder builder= new AlertDialog.Builder(FulldetailsActivity.this);
                        builder.setTitle("Have you shipped?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i)
                            {
                                if(i==0)
                                {
                                    FirebaseDatabase.getInstance().getReference("Cart List").child("Admin View").child(userID).setValue(null);
                                    Toast.makeText(FulldetailsActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                }

                                else
                                {
                                    finish();

                                }
                            }
                        });

                        builder.show();

                    }
                });

            }



            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }

        };


        shipped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });





        cartList.setAdapter(adapter);
        adapter.startListening();
    }




}

