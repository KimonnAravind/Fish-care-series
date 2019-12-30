package com.example.forfishes;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SupportsActivity extends AppCompatActivity
{
private DatabaseReference supportref;
private TextView pnn,maill;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supports);

        pnn=(TextView)findViewById(R.id.phonnumber);
        maill=(TextView)findViewById(R.id.mailid);

        supportref= FirebaseDatabase.getInstance().getReference().child("Admins").child("+917904168617");
        supportref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
            if(dataSnapshot.exists())
            {
                pnn.setText(dataSnapshot.child("Emailid").getValue().toString());
                maill.setText(dataSnapshot.child("phone").getValue().toString());
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
