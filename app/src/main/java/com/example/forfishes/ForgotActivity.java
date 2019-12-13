package com.example.forfishes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ForgotActivity extends AppCompatActivity
{
    private EditText newpassword;
    private Button updatepword;
    private String pword;
    private DatabaseReference productRefers;
   private String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        updatepword=(Button)findViewById(R.id.buttonforupdate);
        newpassword=(EditText)findViewById(R.id.forgotpasswords);

        Intent intent= getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!= null)
        {

            phone = getIntent().getExtras().get("SUCCESS").toString();
        }
        productRefers= FirebaseDatabase.getInstance().getReference().child("Users").child(phone);



        updatepword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(ForgotActivity.this, phone, Toast.LENGTH_SHORT).show();
                Toast.makeText(ForgotActivity.this, "Here You are", Toast.LENGTH_SHORT).show();
            updatepassword();
            }
        });
    }

    private void updatepassword()
    {
        pword=newpassword.getText().toString();
        HashMap<String ,Object> productMap = new HashMap<>();
        productMap.put("password",pword);

        productRefers.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    Toast.makeText(ForgotActivity.this, "Password Updated Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgotActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

}
