package com.example.forfishes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forfishes.Fish.Prevalent.Prevalent;
import com.example.forfishes.Model.cart;
import com.example.forfishes.viewholder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView emptycart;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextprocessbtn;
    private String imageq;
    private int totalvalue=0;
    private DatabaseReference cartlistRefu;
    private TextView totalamount, txtmsg1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView=findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        emptycart=(TextView)findViewById(R.id.cartisempty);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        nextprocessbtn=(Button)findViewById(R.id.nextbutton);
        totalamount=(TextView) findViewById(R.id.totalpriceamount);
        txtmsg1=(TextView)findViewById(R.id.messageafterorder);
        cartlistRefu= FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View");
        cartlistRefu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    Toast.makeText(CartActivity.this, "Empty Cart", Toast.LENGTH_SHORT).show();
                    emptycart.setVisibility(View.VISIBLE);
                    nextprocessbtn.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        nextprocessbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalamount.setText("Total Price ="+String.valueOf(totalvalue));

                Intent intent = new Intent(CartActivity.this, confirmorderActivity.class);
                intent.putExtra("Total Price", String.valueOf(totalvalue));
                startActivity(intent);
                finish();
            }
        });


    }
    @Override
    protected  void onStart()
    {

        super.onStart();
        checkorderstate();
        final DatabaseReference cartlistRef= FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<cart>options=
                new FirebaseRecyclerOptions.Builder<cart>().setQuery(cartlistRef.child("User View")
                        .child(Prevalent.currentOnlineuser.getPhone()).child("Products"),cart.class)
                        .build();
        FirebaseRecyclerAdapter<cart, CartViewHolder>adapter
                =new FirebaseRecyclerAdapter<cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CartViewHolder holder, int position, @NonNull final cart model) {
                holder.txtproductquantity.setText("Qty: "+model.getQuantity());
                holder.txtproductid.setText("" +model.getPid());
                imageq=holder.txtproductid.getText().toString();

                final DatabaseReference imageslist = FirebaseDatabase.getInstance().getReference()
                       /* .child("Cart List")
                        .child("User View")
                        .child(Prevalent.currentOnlineuser.getPhone())*/
                        .child("Products").child(imageq);
                imageslist.addValueEventListener(new ValueEventListener()
                {@Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    String p1=dataSnapshot.child("image").getValue().toString();
                    Picasso.get().load(p1).into(holder.imageofit);
                    //=----  Toast.makeText(CartActivity.this, p1, Toast.LENGTH_SHORT).show();
                }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                holder.txtProductname.setText(model.getPname());
                holder.txtproductprice.setText("Price per unit: "+model.getPrice());

                int oneTypeproductTprice=((Integer.valueOf(model.getPrice())))* Integer.valueOf(model.getQuantity());
                totalvalue = totalvalue+oneTypeproductTprice;
                totalamount.setText("Total Price ="+String.valueOf(totalvalue));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Edit",
                                        "Remove"
                                };
                        AlertDialog.Builder builder= new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options:");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if(which==0)
                                {
                                    Intent intent = new Intent(CartActivity.this, productselect.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                if(which==1)
                                {
                                    cartlistRef.child("User View").child(Prevalent.currentOnlineuser.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if(task.isSuccessful())
                                                    {
                                                        cartlistRef.child("Admin View").child(Prevalent.currentOnlineuser.getPhone())
                                                                .child("Products")
                                                                .child(model.getPid())
                                                                .removeValue();
                                                        Toast.makeText(CartActivity.this, "Item removed from the cart successfully", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(CartActivity.this, endusers.class);

                                                        startActivity(intent);

                                                    }
                                                }
                                            });
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

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void checkorderstate()
    {
        DatabaseReference orderRef;
        orderRef= FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineuser.getPhone());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String shippingstate = dataSnapshot.child("state").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();
                    if(shippingstate.equals("shipped"))
                    {
                        totalamount.setText("Dear" + userName + "\n order is shipped successfully");
                        recyclerView.setVisibility(View.GONE);
                        txtmsg1.setVisibility(View.VISIBLE);
                        txtmsg1.setText("SORRY! You cannot add your next order until your order has shipped, Don't worry within 24-48 hours your order will be dispatched to your location.!");
                        nextprocessbtn.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "You can purchase more products, once you received your alredy placed order", Toast.LENGTH_SHORT).show();
                    }
                    else if(shippingstate.equals("not shipped"))
                    {

                        totalamount.setText("Dear" + userName + "\n order is Yet to be Dispatch");
                        recyclerView.setVisibility(View.GONE);
                        txtmsg1.setVisibility(View.VISIBLE);
                        txtmsg1.setText("SORRY! You cannot add your next order until your order has shipped, Don't worry within 24-48 hours your order will be dispatched to your location.!");
                        nextprocessbtn.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "You can purchase more products, once you received your alredy placed order", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
