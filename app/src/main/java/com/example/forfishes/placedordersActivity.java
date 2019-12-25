package com.example.forfishes;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forfishes.Model.Adminorders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class placedordersActivity extends AppCompatActivity {

    private RecyclerView orderList;
    private DatabaseReference orderRef,adminviewreere;
    private Uri ImageUri;
    private String myUrl="";
    private DatabaseReference trackorderref;
    private StorageReference storageProfilePictureRef;
    private StorageTask uploadTask;
    private String coms;
    private static final int GalleryPick = 1;
    private ImageView bills;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placedorders);
        trackorderref=FirebaseDatabase.getInstance().getReference().child("Mine");
        orderRef= FirebaseDatabase.getInstance().getReference().child("Orders");
        orderList=findViewById(R.id.cartlists);
        orderList.setLayoutManager(new LinearLayoutManager(this));
        storageProfilePictureRef= FirebaseStorage.getInstance().getReference().child("Bill images");

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
                    protected void onBindViewHolder(@NonNull final AdminOrdersViewHolder holder, final int position, @NonNull final Adminorders model)
                    {
                    holder.userName.setText("Name: "+ model.getName());
                        holder.userPhoneNumber.setText("Phone number: "+ model.getPhone());
                        holder.userTotalprice.setText("Cost: "+ model.getTotalamount());
                        holder.userDate.setText("Date: "+ model.getDate());
                        holder.usertime.setText("Time: "+ model.getTime());
                        holder.userShippingaddress.setText("Address: "+ model.getAddress());
                        holder.userpincode.setText("Pincode: "+ model.getPincode());
                        final String phonu=model.getPhone();


                        holder.uploadimg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                Toast.makeText(placedordersActivity.this, phonu, Toast.LENGTH_SHORT).show();
                                openGallery();
                                coms=phonu;


                            }
                        });

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
                                        adminviewreere=FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View")
                                                .child(phonu);
                                        if(i==0)
                                        {
                                            adminviewreere.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                    if(dataSnapshot.exists())
                                                    {
                                                        Toast.makeText(placedordersActivity.this, "Update shipping details, before closing the order.!", Toast.LENGTH_SHORT).show();

                                                    }
                                                    else
                                                    {

                                                        FirebaseDatabase.getInstance().getReference()
                                                                .child("Orders")
                                                                .child(phonu)
                                                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task)
                                                            {
                                                                if(task.isSuccessful())
                                                                {

                                                                    Toast.makeText(placedordersActivity.this, "Order closed", Toast.LENGTH_SHORT).show();
                                                                    finish();
                                                                    startActivity(getIntent());


                                                                }
                                                            }
                                                        });
                                                       /* String uiid= getRef(position).getKey();
                                                        Removeorder(uiid);
                                                        */
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
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
        public ImageView uploadimg;
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
            uploadimg=itemView.findViewById(R.id.uploadpic);


        }
    }

    private void openGallery() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode== RESULT_OK && data != null)
        {
            ImageUri=data.getData();
            uploadimage();
        }


    }

    private void uploadimage()
    {
        final ProgressDialog progressDialog= new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, While we are updating your Account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        String savecurrentdate,savecurrenttime,productrandomkey;
        Calendar date = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        savecurrentdate=currentDate.format(date.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat(" HH:mm:ss a");
        savecurrenttime=currentTime.format(date.getTime());

        productrandomkey=savecurrentdate + savecurrenttime;
        if(ImageUri !=null )
        {
            final StorageReference fileRef= storageProfilePictureRef
                    .child(ImageUri.getLastPathSegment()+ productrandomkey + ".jpg");
            uploadTask= fileRef.putFile(ImageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if(!task.isSuccessful())
                    {
                        Toast.makeText(placedordersActivity.this, "ssssssssss", Toast.LENGTH_SHORT).show();
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if(task.isSuccessful()) {
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();
                        progressDialog.dismiss();
                        Toast.makeText(placedordersActivity.this, myUrl, Toast.LENGTH_SHORT).show();
                        HashMap<String,Object> userMap= new HashMap<>();

                        userMap.put("tracking details",myUrl);
                        Toast.makeText(placedordersActivity.this, coms, Toast.LENGTH_SHORT).show();
                        trackorderref.child(coms).updateChildren(userMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Toast.makeText(placedordersActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            });

        /*.addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {

                    if(task.isSuccessful())
                    {
                        Uri downloadUrl=task.getResult();
                        myUrl=downloadUrl.toString();

                        Toast.makeText(placedordersActivity.this, myUrl, Toast.LENGTH_SHORT).show();
                        DatabaseReference billref= FirebaseDatabase.getInstance().getReference().child("Mine");
                        HashMap<String,Object> userMap= new HashMap<>();
                        userMap.put("image",myUrl);
                        billref.updateChildren(userMap);
                        progressDialog.dismiss();
                        startActivity(new Intent(placedordersActivity.this,endusers.class));

                        Toast.makeText(placedordersActivity.this, "Profile info updated successfully", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(placedordersActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
                    }

                }
            });*/
        }
        else
        {
            Toast.makeText(this, "Image is not selected.", Toast.LENGTH_SHORT).show();
        }


    }
}
