package com.example.forfishes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.forfishes.Fish.Prevalent.Prevalent;
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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{

    private CircleImageView profileimageview;
    private EditText fullname,userphone, useraddress,pinc;
    private TextView profileChangetxtBtn,closeTextBtn,saveTextbtn;
    private StorageTask uploadTask;
    private Uri imageUri;
    private String myUrl="";
    private String text;
    private StorageReference storageProfilePictureRef;
    private String checker = "";
    private Spinner spin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfilePictureRef= FirebaseStorage.getInstance().getReference().child("Profile pictures");
        profileimageview= (CircleImageView)findViewById(R.id.setting_profileimage);
        fullname=(EditText)findViewById(R.id.settingfullname);
        pinc=(EditText)findViewById(R.id.pinn);
        userphone=(EditText)findViewById(R.id.settingPhonenumber);
        useraddress=(EditText)findViewById(R.id.settingaddress);
        spin=(Spinner)findViewById(R.id.spinnerxm);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this,R.array.states,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);
        profileChangetxtBtn=(TextView)findViewById(R.id.profile_img_change_btn);
        closeTextBtn=(TextView)findViewById(R.id.close_settings);
        saveTextbtn=(TextView)findViewById(R.id.update_settings);

        userinfodisplay(profileimageview,fullname,userphone,useraddress);
    closeTextBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    });
    saveTextbtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(checker.equals("clicked"))
            {
            userInfosaved();
            }
            else
            {
                updateOnlyuserInfo();
            }
        }
    });
        profileChangetxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker= "clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);


            }
        });



            }

    private void updateOnlyuserInfo()
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String,Object> userMap= new HashMap<>();

        userMap.put("name",fullname.getText().toString());
        userMap.put("address",useraddress.getText().toString());
        userMap.put("phoneOrder",userphone.getText().toString());
        userMap.put("State",text);
        userMap.put("pincode",pinc.getText().toString());


        ref.child(Prevalent.currentOnlineuser.getPhone()).updateChildren(userMap);

        startActivity(new Intent(SettingsActivity.this,endusers.class));
        Toast.makeText(SettingsActivity.this, "Profile info updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            CropImage.ActivityResult result= CropImage.getActivityResult(data);
            imageUri= result.getUri();

            profileimageview.setImageURI(imageUri);
        }
        else
        {

            Toast.makeText(this, "Error try again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
        }

    }

    private void userInfosaved()
    {

        if(TextUtils.isEmpty(fullname.getText().toString()))
        {
            Toast.makeText(this, "Name is mandatory", Toast.LENGTH_SHORT).show();
        }
       else if(TextUtils.isEmpty(useraddress.getText().toString()))
        {
            Toast.makeText(this, "Address is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(userphone.getText().toString()))
        {
            Toast.makeText(this, "Name is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked"))
        {
         uploadImage();
        }
    }


    private void uploadImage()
    {
     final ProgressDialog progressDialog= new ProgressDialog(this);
     progressDialog.setTitle("Update Profile");
     progressDialog.setMessage("Please wait, While we are updating your Account information");
     progressDialog.setCanceledOnTouchOutside(false);
     progressDialog.show();
     if(imageUri != null)
     {
         final StorageReference fileRef= storageProfilePictureRef
                 .child(Prevalent.currentOnlineuser.getPhone() + ".jpg");
         uploadTask= fileRef.putFile(imageUri);
         uploadTask.continueWithTask(new Continuation() {
             @Override
             public Object then(@NonNull Task task) throws Exception
             {
                 if(!task.isSuccessful())
                 {
                     throw task.getException();
                 }
                 return fileRef.getDownloadUrl();

             }
         })
         .addOnCompleteListener(new OnCompleteListener<Uri>() {
             @Override
             public void onComplete(@NonNull Task<Uri> task)
             {

                 if(task.isSuccessful())
                 {
                     Uri downloadUrl=task.getResult();
                     myUrl=downloadUrl.toString();

                     DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users");
                     HashMap<String,Object> userMap= new HashMap<>();

                     userMap.put("name",fullname.getText().toString());
                     userMap.put("address",useraddress.getText().toString());
                     userMap.put("phoneOrder",userphone.getText().toString());
                     userMap.put("image",myUrl);
                     userMap.put("State",text);
                     userMap.put("pincode",pinc.getText().toString());


                     ref.child(Prevalent.currentOnlineuser.getPhone()).updateChildren(userMap);
                     progressDialog.dismiss();
                    startActivity(new Intent(SettingsActivity.this,endusers.class));
                     Toast.makeText(SettingsActivity.this, "Profile info updated successfully", Toast.LENGTH_SHORT).show();
                     finish();

                 }
                 else
                 {
                     progressDialog.dismiss();
                     Toast.makeText(SettingsActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
                 }

             }
         });
     }
     else
     {
         Toast.makeText(this, "Image is not selected.", Toast.LENGTH_SHORT).show();
     }


    }

    private void userinfodisplay(final CircleImageView profileimageview, final EditText fullname, final EditText userphone, final EditText useraddress)
    {
        DatabaseReference UsersRef= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineuser.getPhone());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.child("image").exists())
                    {
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();
                        Picasso.get().load(image).into(profileimageview);
                        fullname.setText(name);
                        userphone.setText(phone);
                        useraddress.setText(address);
                        String pinss= dataSnapshot.child("pincode").getValue().toString();
                        pinc.setText(pinss);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
         text = parent.getItemAtPosition(position).toString();
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
