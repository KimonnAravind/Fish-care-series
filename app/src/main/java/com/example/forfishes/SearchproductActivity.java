package com.example.forfishes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forfishes.Model.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchproductActivity extends AppCompatActivity
{
    private Button searchBtn;
    private EditText inputtxt;
    private RecyclerView searchResult;
    private String searchinput;
    private Button secc,secc1,secc2,secc3;
    public String searchingstring;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchproduct);
        secc=(Button)findViewById(R.id.seconds);
        secc1=(Button)findViewById(R.id.secondss);
        secc2=(Button)findViewById(R.id.secondsss);
        secc3=(Button)findViewById(R.id.secondssss);
        inputtxt = (EditText)findViewById(R.id.searchproduct);
        searchBtn= (Button)findViewById(R.id.search);
        searchResult=(RecyclerView)findViewById(R.id.searchList);
        searchResult.setLayoutManager(new LinearLayoutManager(SearchproductActivity.this));
        searchingstring= getIntent().getStringExtra("search");
        Toast.makeText(this, searchingstring, Toast.LENGTH_SHORT).show();


        secc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchingstring="Combos";
                searchingfromsomewhere();
            }
        });
        secc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchingstring="Aquarium_Accessories";
                searchingfromsomewhere();
            }
        });
        secc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchingstring="Fish_Food";
                searchingfromsomewhere();
            }
        });
        secc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchingstring="Fish_Medicine";
                searchingfromsomewhere();
            }
        });


        searchBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            searchinput=inputtxt.getText().toString();
            onStart();
            }
        });
    }

    private void searchingfromsomewhere()
    {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");



        FirebaseRecyclerOptions<Products> options= new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(reference.orderByChild("category").startAt(this.searchingstring),Products.class)
                .build();



        FirebaseRecyclerAdapter<Products, com.example.forfishes.ProductViewHolder>adapter =
                new FirebaseRecyclerAdapter<Products, com.example.forfishes.ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull com.example.forfishes.ProductViewHolder holder, int position, @NonNull final Products model)
                    {

                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText(model.getPrice());
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent= new Intent(SearchproductActivity.this,productselect.class);
                                intent.putExtra("pid", model.getPid());
                                startActivity(intent);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public com.example.forfishes.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_items_layout,parent,false);
                        com.example.forfishes.ProductViewHolder holder= new com.example.forfishes.ProductViewHolder(view);

                        return holder;
                    }
                } ;
        searchResult.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options= new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(reference.orderByChild("pname").startAt(searchinput),Products.class)
                .build();
        ;


        FirebaseRecyclerAdapter<Products, com.example.forfishes.ProductViewHolder>adapter =
                new FirebaseRecyclerAdapter<Products, com.example.forfishes.ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull com.example.forfishes.ProductViewHolder holder, int position, @NonNull final Products model)
                    {

                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText(model.getPrice());
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent= new Intent(SearchproductActivity.this,productselect.class);
                                intent.putExtra("pid", model.getPid());
                                startActivity(intent);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public com.example.forfishes.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_items_layout,parent,false);
                        com.example.forfishes.ProductViewHolder holder= new com.example.forfishes.ProductViewHolder(view);

                        return holder;
                    }
                } ;
        searchResult.setAdapter(adapter);
        adapter.startListening();
    }
}
