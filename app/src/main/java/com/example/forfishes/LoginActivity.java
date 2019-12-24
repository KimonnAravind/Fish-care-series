package com.example.forfishes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.forfishes.Fish.Prevalent.Prevalent;
import com.example.forfishes.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText inphone, inpass;
    private Button goin;
    private CheckBox CheckBoxRememberme;
    private TextView admin, forgotpassword;
    private ProgressDialog loadingBar1;
    public String forgetpassword = "forgetpassword";
    private String parentdbname = "Users";

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CheckBoxRememberme = findViewById(R.id.checkBox);
        forgotpassword = (TextView) findViewById(R.id.ForgetPassword);
        inphone = (EditText) findViewById(R.id.editText);
        admin = (TextView) findViewById(R.id.Admin);
        inpass = (EditText) findViewById(R.id.editText2);
        goin = (Button) findViewById(R.id.button);
        Paper.init(this);
        loadingBar1 = new ProgressDialog(this);

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, HomActivity.class);
                intent.putExtra("forgetpassword", forgetpassword);
                startActivity(intent);
            }
        });

        goin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginuser();
            }
        });
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentdbname = "Admins";


            }
        });

    }


    private void loginuser()
    {
        String innphone = inphone.getText().toString();
        String password = inpass.getText().toString();
        int a=innphone.length();
        if (innphone.startsWith("+91")&&a==13)
        {




        if (TextUtils.isEmpty(innphone)) {
            Toast.makeText(this, "Please write your phone number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please write your password", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar1.setTitle("Login Account");
            loadingBar1.setMessage("Please wait while checking your details");
            loadingBar1.setCanceledOnTouchOutside(false);
            loadingBar1.show();

            AllowAccessToAcc(innphone, password);

        }
    }
        else
        {
            Toast.makeText(this, "Enter your registered phone number with the code +91**********", Toast.LENGTH_SHORT).show();
        }

}

    private void AllowAccessToAcc(final String innphone, final String password) {
        if(CheckBoxRememberme.isChecked())
        {
            Paper.book().write(Prevalent.userPhoneKey,innphone);
            Paper.book().write(Prevalent.userPasswordKey,password);
        }
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parentdbname).child(innphone).exists())
                {
                  Users usersData=dataSnapshot.child(parentdbname).child(innphone).getValue(Users.class);
                  if(usersData.getPhone().equals(innphone))
                  {
                      if(usersData.getPassword().equals(password))
                      {

                          if(parentdbname.equals("Admins"))
                          {     loadingBar1.dismiss();
                              Toast.makeText(LoginActivity.this, "You are in", Toast.LENGTH_SHORT).show();

                              Intent intent = new Intent(LoginActivity.this, AdminProductActivity.class);
                             startActivity(intent);
                          }
                          else if(parentdbname.equals("Users"))
                          {
                              Toast.makeText(LoginActivity.this, "LoginSuccessfull", Toast.LENGTH_SHORT).show();
                              loadingBar1.dismiss();
                              Intent intent = new Intent(LoginActivity.this, endusers.class);
                              Prevalent.currentOnlineuser=usersData;
                              startActivity(intent);
                          }
                      }
                      else
                      {
                          Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                      loadingBar1.dismiss();
                      }
                  }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Please create a new Account, Account with this number is do not exist", Toast.LENGTH_SHORT).show();
                    loadingBar1.dismiss();
                    Toast.makeText(LoginActivity.this, "You need to create a new Account", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
