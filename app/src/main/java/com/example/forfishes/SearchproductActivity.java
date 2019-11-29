package com.example.forfishes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchproduct);

        inputtxt = (EditText)findViewById(R.id.searchproduct);
        searchBtn= (Button)findViewById(R.id.search);
        searchResult=(RecyclerView)findViewById(R.id.searchList);
        searchResult.setLayoutManager(new LinearLayoutManager(SearchproductActivity.this));

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
