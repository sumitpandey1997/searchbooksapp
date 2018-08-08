package com.example.user.searchbooks;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class signupactivity extends AppCompatActivity {
    EditText your_fname;
    EditText your_lname;
    EditText your_email;
    EditText your_contact;
    EditText your_adress;
    EditText your_password;
    Button register;
    String firstname;
    String lastname;
    String email;
    String contact;
    String adress;
    String password;
    FirebaseAuth mAuth;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference databaseReference=database.getReference();
    DatabaseReference databaseReference1=databaseReference.child("sumit");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupactivity);
        your_fname = findViewById(R.id.your_fname);
        your_lname = findViewById(R.id.your_lname);
        your_email = findViewById(R.id.your_email);
        your_contact = findViewById(R.id.your_contact);
        your_adress = findViewById(R.id.your_adress);
        your_password = findViewById(R.id.your_password);
        register = findViewById(R.id.btn);
        mAuth = FirebaseAuth.getInstance();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Blogger Sans-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstname = your_fname.getText().toString();
                lastname = your_lname.getText().toString();
                email = your_email.getText().toString();
                contact = your_contact.getText().toString();
                password = your_password.getText().toString();
                adress = your_adress.getText().toString();
                if (firstname.isEmpty()) {
                    your_fname.setError("first name can not be empty");
                    return;
                }
                if (lastname.isEmpty()) {
                    your_lname.setError("last name can not be empty");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
                    your_email.setError("enter valid emailid");
                    return;
                }
                if (password.isEmpty() || password.length() <= 5) {
                    your_password.setError("min 5 char required");
                    return;
                }

                if (contact.isEmpty() || contact.length() != 10) {
                    your_contact.setError("invalid phn no");
                    return;
                }
                if (adress.isEmpty()) {
                    your_adress.setError("enter adress");
                    return;
                }
               // final ProgressDialog progressDialog=ProgressDialog.show(signupactivity.this,"registering","wait you are getting register",false);

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(signupactivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    /*Toast.makeText(Main2Activity.this, "u have register succesfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Main2Activity.this,MainActivity.class));*/
                                    final String id=databaseReference1.push().getKey();
                                    databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            databaseReference1.child(id).child("firstname").setValue(firstname);
                                            databaseReference1.child(id).child("lastname").setValue(lastname);
                                            databaseReference1.child(id).child("email").setValue(email);
                                            databaseReference1.child(id).child("contact").setValue(contact);
                                            databaseReference1.child(id).child("password").setValue(password);
                                            databaseReference1.child(id).child("adress").setValue(adress);
                                           // progressDialog.dismiss();
                                            Toast.makeText(signupactivity.this, "YOU HAVE REGISTER SUCCESFULLY", Toast.LENGTH_SHORT).show();


                                        }


                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Toast.makeText(signupactivity.this, "RE-try", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                } else {

                                    Toast.makeText(signupactivity.this, task.getException().toString(),
                                            Toast.LENGTH_SHORT).show();

                                }


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(signupactivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });



                your_adress.setText("");
                your_contact.setText("");
                your_fname.setText("");
                your_lname.setText("");
                your_password.setText("");
                your_email.setText("");


            }

        });
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}