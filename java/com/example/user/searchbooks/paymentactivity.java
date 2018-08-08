package com.example.user.searchbooks;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static java.security.AccessController.getContext;

public class paymentactivity extends AppCompatActivity {
    private PackageInstaller.Session session;
    final String tag = "MainActivity";
    final String payeaddress = "sumitpandey490@upi";//upi
    final String payename = "SUMIT";//name of person to pay
    String transactionNode = "Paying for this book";//reason for paying
    String currencyunit = "INR";
    String uid;
    DatabaseReference databaseReference;
    MyRecyclerViewAdapter myRecyclerViewAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentactivity);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("books");

        Intent intent=getIntent();
        String amount=intent.getStringExtra("prize");
         uid=intent.getStringExtra("uid");
                Uri uri = Uri.parse("upi://pay?pa=" + payeaddress
                        + "&pn=" + payename
                        + "&tn=" + transactionNode
                        + "&am=" + amount
                        + "&cu=" + currencyunit);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivityForResult(i, 1);



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {
            String res = data.getStringExtra("response");
            String search = "SUCCESS";
            if (res.toLowerCase().contains(search.toLowerCase())) {
                Toast.makeText(paymentactivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                databaseReference.child(uid).child("sold").setValue("unsold");
                myRecyclerViewAdapter.notifyDataSetChanged();


            } else {
                Toast.makeText(paymentactivity.this, "Payment Failed", Toast.LENGTH_SHORT).show();
            }

        }
    }
}