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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class chargesandupi extends AppCompatActivity
{
    private EditText tnn,kaa,krr,uiid;
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
        apy=(Button)findViewById(R.id.applying);


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
