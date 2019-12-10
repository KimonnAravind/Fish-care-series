package com.example.forfishes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class HomActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    private Button getootp,verifyotpbutt,joining;
    EditText editTextphon,editTextcode,phonenumber,password,nameinput;
    FirebaseAuth mAuth;
    public String forgetpassword="";
    private ProgressDialog loadingBar;
    private Spinner spin;
   private String   text;

    public String sentcode,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hom);
        editTextphon=findViewById(R.id.phonin);
        editTextcode=findViewById(R.id.otpn);
        verifyotpbutt=findViewById(R.id.verifyotp);
        getootp=findViewById(R.id.getotp);
        mAuth=FirebaseAuth.getInstance();
        joining=findViewById(R.id.button4);
        nameinput=findViewById(R.id.editText6);
        password=findViewById(R.id.editText8);
        spin=(Spinner)findViewById(R.id.spinnerxm);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this,R.array.states,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);



        Intent intent= getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!= null)
        {
            forgetpassword=getIntent().getExtras().get("forgetpassword").toString();
        }


        joining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createaccount();
            }
        });
        findViewById(R.id.getotp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomActivity.this, forgetpassword, Toast.LENGTH_SHORT).show();
                sendverificationcode();
                editTextcode.setVisibility(View.VISIBLE);
                verifyotpbutt.setVisibility(View.VISIBLE);
                getootp.setVisibility(View.INVISIBLE);





            }
        });

        findViewById(R.id.verifyotp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                verifysignincode();
            }
        });
    }

    private void createaccount() {
        String inname=nameinput.getText().toString();
        String inphone=editTextphon.getText().toString();
        String inpass=password.getText().toString();
        loadingBar= new ProgressDialog(this);
        //nameinput
        //password
        //editTextphon



        if(TextUtils.isEmpty(inname))
        {
            Toast.makeText(this, "Please write your name", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(inphone))
        {
            Toast.makeText(this, "Please write your phone number", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(inpass))
        {
            Toast.makeText(this, "Please write your password", Toast.LENGTH_SHORT).show();
        }
        else
        {

            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait while checking your details");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            Validatephone (inname,inphone,inpass);

        }
    }

    private void Validatephone(final String inname, final String inphone, final String inpass)
    {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(inphone).exists()))
                {
                    HashMap<String,Object> userdatamap = new HashMap<>();
                    userdatamap.put("name", inname);
                    userdatamap.put("phone", inphone);
                    userdatamap.put("State",text);
                    userdatamap.put("password", inpass);
                    userdatamap.put("address", "");
                    userdatamap.put("image", "");
                    userdatamap.put("pincode","");
                    userdatamap.put("phoneOrder", "");

                    RootRef.child("Users").child(inphone).updateChildren(userdatamap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(HomActivity.this, "Your Account is created Successfully", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        Intent intent = new Intent(HomActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        loadingBar.dismiss();
                                        Toast.makeText(HomActivity.this, "Check for Network connection then try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(HomActivity.this, "This"+inphone+"already exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(HomActivity.this, "Please try using anothe phone number", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void verifysignincode()
    {

        String code=editTextcode.getText().toString();
        if(code.isEmpty())
        {
            Toast.makeText(HomActivity.this, "Enter Valid OTP", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(sentcode,code);
        signInWithPhoneAuthCredential(credential);
    }}
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            if(forgetpassword.equals("forgetpassword"))
                            {
                                phone= editTextphon.getText().toString();
                                Intent intent = new Intent(HomActivity.this, ForgotActivity.class);
                                intent.putExtra("phone",phone);
                                startActivity(intent);
                            }
                            else
                                {
                                joining.setVisibility(View.VISIBLE);
                                nameinput.setVisibility(View.VISIBLE);
                                password.setVisibility(View.VISIBLE);
                                spin.setVisibility(View.VISIBLE);
                                verifyotpbutt.setVisibility(View.INVISIBLE);
                                editTextcode.setVisibility(View.INVISIBLE);
                                joining.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                            {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                            {
                                Toast.makeText(HomActivity.this, "InvalidOtp", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
    }

    private void sendverificationcode() {
        String phoneNumber = editTextphon.getText().toString();
        if(phoneNumber.isEmpty())
        {
            editTextphon.setError("Number required");
            return;
        }
        if(phoneNumber.length()<10)
        {
            editTextphon.setError("Number required");
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            sentcode = s;



        }


    };
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