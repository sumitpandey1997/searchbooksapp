package com.example.user.searchbooks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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

public class loginactivity extends AppCompatActivity {
    EditText email;
    EditText password;
    Button login;
    TextView signup;
    String your_email;
    String your_password;
    FirebaseAuth mAuth;
    DatabaseReference dbRef;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);
        email=findViewById(R.id.ete);
        password=findViewById(R.id.etp);
        login=findViewById(R.id.btn1);
        signup=findViewById(R.id.singuptv);
        mAuth = FirebaseAuth.getInstance();
        your_email=email.getText().toString();
        your_password=password.getText().toString();
        dbRef = FirebaseDatabase.getInstance().getReference();


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Blogger Sans-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(loginactivity.this, signupactivity.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                your_email = email.getText().toString();
                your_password = password.getText().toString();
                if(your_email.equals("") || your_password.equals(""))
                {
                    email.setError("empty email or password");
                    return;
                }
                final ProgressDialog progressDialog=ProgressDialog.show(loginactivity.this,"loading","wait you are getting loging",false);
                mAuth.signInWithEmailAndPassword(your_email, your_password)
                        .addOnCompleteListener(loginactivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(loginactivity.this, "u have login succsesfully", Toast.LENGTH_SHORT).show();
                                    fetchInfo(your_password,your_email);
                                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("LoggedInUser", your_email);
                                    editor.putBoolean("loggedin",true);
                                    editor.commit();
                                    startActivity(new Intent(loginactivity.this,MainActivity.class));

                                } else {

                                    progressDialog.dismiss();
                                    Toast.makeText(loginactivity.this, "invalid email or password", Toast.LENGTH_LONG).show();

                                }


                            }
                        });
                email.setText("");
                password.setText("");
            }

        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void fetchInfo(String password, final String email)
    {
         count=0;
        dbRef.child("sumit").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Toast.makeText(loginactivity.this, snapshot.child("email").getValue(String.class), Toast.LENGTH_SHORT).show();
                    count++;

                    if(email.equals(snapshot.child("email").getValue(String.class)))
                    {
                        DataSnapshot em=snapshot.child("email");
                        Toast.makeText(loginactivity.this, em.toString(), Toast.LENGTH_SHORT).show();
                       // Toast.makeText(loginactivity.this, snapshot.child()), Toast.LENGTH_SHORT).show();
                        UserDetails userDetails = new UserDetails(snapshot.child("firstname").getValue().toString(), snapshot.child("lastname").getValue().toString(), snapshot.child("email").getValue().toString(), snapshot.child("contact").getValue().toString(), snapshot.child("adress").getValue().toString(),snapshot.child("password").getValue().toString());
                        MyPreferenceManager.setUserDetail(loginactivity.this, userDetails);
                        Intent intent = new Intent(loginactivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    }
                    if(count>=dataSnapshot.getChildrenCount())
                    {
                        Toast.makeText(loginactivity.this, "no email", Toast.LENGTH_SHORT).show();
                    }



                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(loginactivity.this, ""+databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}

