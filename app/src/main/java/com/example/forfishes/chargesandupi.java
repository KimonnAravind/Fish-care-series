package com.example.forfishes;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class chargesandupi extends AppCompatActivity
{
    private EditText tnn,kaa,krr,uiid,pnumber,emailid;
    private Button apy;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chargesandupi);

        tnn=(EditText)findViewById(R.id.tn);
        kaa=(EditText)findViewById(R.id.ka);
        krr=(EditText)findViewById(R.id.kr);
        uiid=(EditText)findViewById(R.id.upiid);
        pnumber=(EditText)findViewById(R.id.pnn);
        emailid=(EditText)findViewById(R.id.mid);
        apy=(Button)findViewById(R.id.applying);

        final DatabaseReference adminref;
        adminref= FirebaseDatabase.getInstance().getReference().child("Admins").child("+917904168617");

        adminref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
            if(dataSnapshot.exists())
            {

                String tam = dataSnapshot.child("Tamilnadu").getValue().toString();
                String ker = dataSnapshot.child("Kerala").getValue().toString();
                String kar = dataSnapshot.child("Karnataka").getValue().toString();
                String uui = dataSnapshot.child("UPI id").getValue().toString();
                pnumber.setText(dataSnapshot.child("Pnumber").getValue().toString());
                emailid.setText(dataSnapshot.child("Emailid").getValue().toString());
                tnn.setText(tam);
                kaa.setText(kar);
                krr.setText(ker);
                uiid.setText(uui);
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        apy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatedata();
            }
        });
    }

    private void updatedata()
    {
        final DatabaseReference adminref;
        adminref= FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> adminmap= new HashMap<>();
        adminmap.put("Tamilnadu",tnn.getText().toString());
        adminmap.put("Kerala",krr.getText().toString());
        adminmap.put("Pnumber",pnumber.getText().toString());
        adminmap.put("Emailid",emailid.getText().toString());
        adminmap.put("Karnataka",kaa.getText().toString());
        adminmap.put("UPI id",uiid.getText().toString());
        adminref.child("Admins").child("+917904168617").updateChildren(adminmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(chargesandupi.this, "Updated", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



}
