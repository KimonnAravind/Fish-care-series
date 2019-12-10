package com.example.forfishes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forfishes.Fish.Prevalent.Prevalent;
import com.example.forfishes.Model.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class endusers extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private String type="";
    private TextView ssbc;
    private ImageView combos,second,third,fourt;
    
    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endusers);

        Intent intent= getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!= null)
        {
            type=getIntent().getExtras().get("Admin").toString();
        }

        ssbc=(TextView)findViewById(R.id.sbc);
        combos= (ImageView)findViewById(R.id.combos);
        second=(ImageView)findViewById(R.id.sec);
        third=(ImageView)findViewById(R.id.thr);
        fourt=(ImageView)findViewById(R.id.frr);
        productRef= FirebaseDatabase.getInstance().getReference().child("Products");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        Paper.init(this);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!type.equals("Admin"))
                {
                    Intent intent= new Intent(endusers.this, CartActivity.class);
                    startActivity(intent);
                }
                }




        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle( this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);
        TextView userNamTextView= headerView.findViewById(R.id.user_profile_name);
        CircleImageView userProfilePicture= headerView.findViewById(R.id.user_profile_image);

        if(!type.equals("Admin"))
        {
            userNamTextView.setText(Prevalent.currentOnlineuser.getName());
            Picasso .get().load(Prevalent.currentOnlineuser.getImage()).placeholder(R.drawable.profile).into(userProfilePicture);
        }
        recyclerView= findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        combos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(endusers.this, SearchproductActivity.class);
                intent.putExtra("search", "Combos");
                startActivity(intent);
            }
        });
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(endusers.this, SearchproductActivity.class);
                intent.putExtra("search", "Aquarium_Accessories");
                startActivity(intent);
            }
        });
        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(endusers.this, SearchproductActivity.class);
                intent.putExtra("search", "Fish_Food");
                startActivity(intent);
            }
        });
        fourt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(endusers.this, SearchproductActivity.class);
                intent.putExtra("search", "Fish_Medicine");
                startActivity(intent);
            }
        });
        ssbc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(endusers.this, SearchproductActivity.class);
                intent.putExtra("search", "Fish_Medicine");
                startActivity(intent);
            }
        });


    }


    @Override
    protected void onStart()
    {
        super.onStart();

        Query sortposterinDecendingOrder= productRef.orderByChild("countpost");

        FirebaseRecyclerOptions<Products> options=
                new FirebaseRecyclerOptions.Builder<Products>().setQuery(sortposterinDecendingOrder, Products.class)
                        .build();



        FirebaseRecyclerAdapter<Products, com.example.forfishes.ProductViewHolder>adapter=new FirebaseRecyclerAdapter<Products, com.example.forfishes.ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull com.example.forfishes.ProductViewHolder holder, int position, @NonNull final Products model) {
                holder.txtProductName.setText(model.getPname());
                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText(model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        if(type.equals("Admin"))
                        {
                            Intent intent= new Intent(endusers.this,AdminAlterActivity.class);
                            intent.putExtra("pid", model.getPid());
                            startActivity(intent);
                        }
                        else
                        {
                            Intent intent= new Intent(endusers.this,productselect.class);
                            intent.putExtra("pid", model.getPid());
                            startActivity(intent);
                        }


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

        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        /*int id = item.getItemId();
        if(id == R.id.action_settings)
        {
            return true;

        }*/
        return super.onOptionsItemSelected(item);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.endusers, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id=menuItem.getItemId();

        if(id== R.id.nav_cart)
        {

            if(!type.equals("Admin"))
            {
                Intent intent= new Intent(endusers.this, CartActivity.class);
                startActivity(intent);

            }


        }
        else  if(id== R.id.nav_search)
        {
            if(!type.equals("Admin"))
            {
                Intent intent = new Intent(endusers.this,SearchproductActivity.class);
                startActivity(intent);

            }


        }
        else if(id== R.id.nav_category)
        {
            if(!type.equals("Admin"))
            {
            Intent intent =new Intent(endusers.this, MyordersActivity.class);
            startActivity(intent);
            }

        }
        else  if(id== R.id.nav_setting)
        {
            if(!type.equals("Admin"))
            {Intent intent = new Intent(endusers.this,SettingsActivity.class);
                startActivity(intent);


            }

        }
        else if(id== R.id.nav_logout)
        {
            if(!type.equals("Admin"))
            {
                Toast.makeText(this, "Here", Toast.LENGTH_SHORT).show();
                Paper.book().destroy();
                Intent intent = new Intent(endusers.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

}
