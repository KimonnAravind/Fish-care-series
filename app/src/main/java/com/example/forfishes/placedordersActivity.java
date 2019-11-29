package com.example.forfishes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forfishes.Model.Adminorders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class placedordersActivity extends AppCompatActivity {

    private RecyclerView orderList;
    private DatabaseReference orderRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placedorders);

        orderRef= FirebaseDatabase.getInstance().getReference().child("Orders");
        orderList=findViewById(R.id.cartlists);
        orderList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Adminorders> options=
                new FirebaseRecyclerOptions.Builder<Adminorders>()
                .setQuery(orderRef, Adminorders.class)
                .build();
        FirebaseRecyclerAdapter<Adminorders,AdminOrdersViewHolder>  adapter=
                new FirebaseRecyclerAdapter<Adminorders, AdminOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int position, @NonNull final Adminorders model)

                    {
                    holder.userName.setText("Name: "+ model.getName());
                        holder.userPhoneNumber.setText("Phone number: "+ model.getPhone());
                        holder.userTotalprice.setText("Cost: "+ model.getTotalamount());
                        holder.userDate.setText("Date: "+ model.getDate());
                        holder.usertime.setText("Time: "+ model.getTime());
                        holder.userShippingaddress.setText("Address: "+ model.getAddress());
                        holder.userpincode.setText("Pincode: "+ model.getPincode());


                        holder.showordersbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                String uiid= getRef(position).getKey();
                                Intent intent =new Intent(placedordersActivity.this, UserplacedActivity.class);
                                intent.putExtra("uid",uiid);
                                startActivity(intent);

                            }
                        });
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                             CharSequence options[] = new CharSequence[]
                                     {
                                             "Yes",
                                             "No"

                                     };
                                AlertDialog.Builder builder= new AlertDialog.Builder(placedordersActivity.this);
                                builder.setTitle("Have you shipped?");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i)
                                    {
                                        if(i==0)
                                        {
                                            String uiid= getRef(position).getKey();
                                            Removeorder(uiid);
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
                    public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ordersplacedlayout,parent,false);
                        return  new AdminOrdersViewHolder(view);
                    }
                };

        orderList.setAdapter(adapter);
        adapter.startListening();
    }

    private void Removeorder(String uiid)
    {
        orderRef.child(uiid).removeValue();
    }

    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder
    {
        public TextView userName, userPhoneNumber,userTotalprice,userDate,usertime,userShippingaddress,userpincode;
        public Button showordersbtn;
        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);


            userName=itemView.findViewById(R.id.orderedusername);
            userPhoneNumber=itemView.findViewById(R.id.usersphonenumber);
            userTotalprice=itemView.findViewById(R.id.costoforder);
            userDate=itemView.findViewById(R.id.dateoforder);
            usertime=itemView.findViewById(R.id.timeoforder);
            userShippingaddress=itemView.findViewById(R.id.Addressofthecus);
            userpincode=itemView.findViewById(R.id.pincodeofuser);
            showordersbtn=itemView.findViewById(R.id.showplacedproducts);


        }
    }
}
