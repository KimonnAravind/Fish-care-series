package com.example.forfishes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class paymentActivity extends AppCompatActivity
{

    EditText name,id,amount;
    TextView trnID;
    Button paywithgl;
    int GOOGLE_PAY_REQUEST_CODE = 123;
    String GOOGLE_PAY_PACKAGE_NAME= "com.google.android.apps.nbu.paisa.user";
    String TAG= "main";
    final int UPI_PAYMENT=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        trnID=(TextView)findViewById(R.id.transactionID);
        paywithgl=(Button)findViewById(R.id.paywithgl);
        name=(EditText) findViewById(R.id.payeename);
        id=(EditText)findViewById(R.id.payeeID);
        amount=(EditText)findViewById(R.id.payment);

        paywithgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(name.getText()))
                {
                    Toast.makeText(paymentActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(id.getText()))
                {
                    Toast.makeText(paymentActivity.this, "Enter id", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(amount.getText()))
                {
                    Toast.makeText(paymentActivity.this, "Enter amount", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    payusingUPI("Aravind Kimonn", "pl.7904168617@icici",
                            amount.getText().toString());
                }
            }
        });

    }

    void payusingUPI(String name, String id, String amount)
    {
        Log.e("main", "name"+ name + "--up--"+id+"--"+amount);
      Uri uri=
              new Uri.Builder()
                      .scheme("upi")
                      .authority("pay")
              .appendQueryParameter("pa",id)
              .appendQueryParameter("pn",name)
              .appendQueryParameter("am",amount)
              .appendQueryParameter("cu","INR")
              .build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
        startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);


      /*Intent opengpay= getPackageManager().getLaunchIntentForPackage(GOOGLE_PAY_PACKAGE_NAME);
        Intent choose = Intent.createChooser(opengpay, "Pay with");
        startActivityForResult(choose, UPI_PAYMENT);
      startActivity(opengpay);*/

        /*Intent upipayIntent = new Intent(Intent.ACTION_VIEW);
        upipayIntent.setData(uri);

        Intent choose = Intent.createChooser(upipayIntent, "Pay with");
        if (null!=choose.resolveActivity(getPackageManager()))
        {
            startActivityForResult(choose, UPI_PAYMENT);
        }else {
            Toast.makeText(this, "No UPI app found", Toast.LENGTH_SHORT).show();
        }*/
    }

    protected void onActivityResult(int requestCode, int resultcode, Intent data)
    {
        super.onActivityResult(requestCode, resultcode, data);
        String trnsID = data.getStringExtra("response");
        Log.e("UPI", "onActivityResultttt:" + trnsID);

        trnID.setText(trnsID);

        /*Log.e("main", "requestCode is" +requestCode+ "UPI_PAYMENT IS "+UPI_PAYMENT +" resultcode is "+RESULT_OK+ "data is" +data);

        switch (requestCode)
        {
            case UPI_PAYMENT:
                if((RESULT_OK==resultcode)|| (resultcode==11))
                {
                    if (data != null)
                    {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResultttt:" + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        Toast.makeText(this, "Transaction is success", Toast.LENGTH_SHORT).show();
                      // upiPaymentDataOperation(dataList);
                    } else

                        {
                        Log.e("UPI", "onActivityResult" + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                     // upiPaymentDataOperation(dataList);
                    }
                }
                else
                    {
                        Log.e("UPI", "OnActivityResult" + "Return data is null");
                        ArrayList<String>dataList = new ArrayList<>();
                        dataList.add("nothing");
                      //  upiPaymentDataOperation(dataList);

                }
                break;
        }*/


    }





}
