package com.example.forfishes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.forfishes.Fish.Prevalent.Prevalent;
import com.example.forfishes.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

  private Button Joinnowbutton, Existinguserbutton, gotopayment;
    private ProgressDialog loadingBar1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
       Joinnowbutton=(Button)findViewById(R.id.button2);
        Existinguserbutton=(Button)findViewById(R.id.button3);
        gotopayment=(Button)findViewById(R.id.paymentpage);
        loadingBar1= new ProgressDialog(this);
        Paper.init(this);
        Existinguserbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        gotopayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, paymentActivity.class);
                startActivity(intent);
            }
        });

        Joinnowbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomActivity.class);
                startActivity(intent);
            }
        });
        String userPhoneKey = Paper.book().read(Prevalent.userPhoneKey);
        String userPasswordKey= Paper.book().read(Prevalent.userPasswordKey);


        if(userPhoneKey!="" && userPasswordKey!="")
        {
            if(!TextUtils.isEmpty(userPhoneKey) && !TextUtils.isEmpty(userPasswordKey))
            {
                AllowAccess(userPhoneKey, userPasswordKey);
                loadingBar1.setTitle("Already logged in");
                loadingBar1.setMessage("Please wait");
                loadingBar1.setCanceledOnTouchOutside(false);
                loadingBar1.show();
            }
        }



    }

    private void AllowAccess(final String innphone, final String password) {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Users").child(innphone).exists())
                {
                    Users usersData=dataSnapshot.child("Users").child(innphone).getValue(Users.class);

                    if(usersData.getPhone().equals(innphone))
                    {
                        if(usersData.getPassword().equals(password))
                        {

                            /*Toast.makeText(MainActivity.this, "LoginSuccessfull", Toast.LENGTH_SHORT).show();
                            loadingBar1.dismiss();*/
                           Intent intent = new Intent(MainActivity.this, endusers.class);
                            Prevalent.currentOnlineuser=usersData;
                           startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            loadingBar1.dismiss();
                        }
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Please create a new Account, Account with this number is do not exist", Toast.LENGTH_SHORT).show();
                    loadingBar1.dismiss();
                    Toast.makeText(MainActivity.this, "You need to create a new Account", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
