package com.example.forfishes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminActivity extends AppCompatActivity {

    private String CategoryName,description,price,name,savecurrentdate, savecurrenttime,productrandomkey;
    private Button addNewProduct;
    private String downloadimageurl;
    private ImageView uploadanImage;
    private ProgressDialog loadingBar;
    private TextView productName, productDescription, productPrice;
    private Uri ImageUri;
    private long countpost=0;
    private static final int GalleryPick = 1;
    private StorageReference productImagesRef;
    private DatabaseReference productref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        productImagesRef= FirebaseStorage.getInstance().getReference().child("ProductImages");
        productref=FirebaseDatabase.getInstance().getReference().child("Products");
        CategoryName = getIntent().getExtras().get("category").toString();
        addNewProduct = (Button) findViewById(R.id.AddProduct1);
        productDescription = (TextView) findViewById(R.id.productDescription1);
        productName = (TextView) findViewById(R.id.productName1);
        productPrice = (TextView) findViewById(R.id.productPrice1);
        loadingBar = new ProgressDialog(this);
        uploadanImage = (ImageView) findViewById(R.id.selectProduct);


        uploadanImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            openGallery();

            }
        });

        addNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

              ValidateProductData();


            }
        });
    }

    private void openGallery()
    {
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
        uploadanImage.setImageURI(ImageUri);
        }
    }
  private void ValidateProductData()
    {
        description=productDescription.getText().toString();
        name=productName.getText().toString();
        price=productPrice.getText().toString();
        if(ImageUri==null)
        {
            Toast.makeText(this, "Product Image required", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(description))
        {
            Toast.makeText(this, "Product Description is required", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Product name is required", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(price))
        {
            Toast.makeText(this, "Product price is required", Toast.LENGTH_SHORT).show();
        }
        else {
            StoreProductInformation();
        }
    }



    private void saveproductinfotodb()
    {
        HashMap<String ,Object>productMap = new HashMap<>();
        productMap.put("pid",productrandomkey);
        productMap.put("date",savecurrentdate);
        productMap.put("time",savecurrenttime);
        productMap.put("description",description);
        productMap.put("image",downloadimageurl);
        productMap.put("category",CategoryName);
        productMap.put("price",price);
        productMap.put("pname",name);
        productMap.put("countpost",countpost);
        productref.child(productrandomkey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                     if(task.isSuccessful())
                     {

                         Intent intent= new Intent(AdminActivity.this,AdminProductActivity.class);
                         startActivity(intent);
                         loadingBar.dismiss();
                         Toast.makeText(AdminActivity.this, "Reached to the Destination", Toast.LENGTH_SHORT).show();


                     }
                     else
                     {
                         loadingBar.dismiss();

                         String message= task.getException().toString();
                         Toast.makeText(AdminActivity.this, "Error1"+message, Toast.LENGTH_SHORT).show();
                     }
                    }
                });

    }
    private void StoreProductInformation()
    {
        productref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
             if(dataSnapshot.exists())
             {
                countpost=dataSnapshot.getChildrenCount();
                countpost=-(countpost);
             }
             else
             {
                countpost=0;
             }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        loadingBar.setTitle("Adding new product");
        loadingBar.setMessage("Please wait while uploading the image to the DB");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        Calendar date = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        savecurrentdate=currentDate.format(date.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat(" HH:mm:ss a");
        savecurrenttime=currentTime.format(date.getTime());

        productrandomkey=savecurrentdate + savecurrenttime;

        final StorageReference filepath=productImagesRef.child(ImageUri.getLastPathSegment()+ productrandomkey + ".jpg");

        final UploadTask uploadTask= filepath.putFile(ImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message =e.toString();
                Toast.makeText(AdminActivity.this, "Error2"+ message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AdminActivity.this, "Product image uploaded successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask= uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful())
                        {

                            throw task.getException();

                        }

                        downloadimageurl= filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();


                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if(task.isSuccessful())
                        {
                            downloadimageurl=task.getResult().toString();
                            Toast.makeText(AdminActivity.this, "Getting product image url succesfully", Toast.LENGTH_SHORT).show();
                            saveproductinfotodb();

                        }
                    }
                });
            }
        });
    }
}