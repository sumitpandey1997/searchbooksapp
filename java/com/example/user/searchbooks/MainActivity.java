package com.example.user.searchbooks;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Pattern;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,MyRecyclerViewAdapter.ItemClickListener {
    MyRecyclerViewAdapter adapter;
    MyRecyclerViewAdapter myRecyclerViewAdapter;
    Spinner spinner;
    EditText searchbox;
    RecyclerView recyclerView;

    LinearLayoutManager managerRecyler;
    String spinnerItem;
    int first,last;
    CheckBox stagCheckBox, openCheckBox;
    ProgressBar progressBar;
    ArrayAdapter<CharSequence> arrayAdapter;
    DatabaseReference databaseReference, myRef;
    FirebaseUser firebaseUser;
    ArrayList<String> nameArray, prizeArray, imageurlArray,authernamearray,discriptionArray,uidArray,soldArray,contactArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.rvAnimals);
        searchbox=findViewById(R.id.search_edit);
        progressBar=findViewById(R.id.progessBar1);
        setSupportActionBar(toolbar);
        openCheckBox = findViewById(R.id.openNowCheck);
        stagCheckBox = findViewById(R.id.stagCheck);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        nameArray = new ArrayList<>();
        authernamearray = new ArrayList<>();
        prizeArray = new ArrayList<>();
        uidArray = new ArrayList<>();
        contactArray = new ArrayList<>();
        soldArray = new ArrayList<>();
        imageurlArray = new ArrayList<>();
        discriptionArray = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("books");
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Blogger Sans-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        progressBar.setVisibility(View.VISIBLE);
        if(MyPreferenceManager.getUserDetail(MainActivity.this)!=null)
        {
            UserDetails userDetails=MyPreferenceManager.getUserDetail(MainActivity.this);
            Toast.makeText(this, userDetails.getEmail()+"@@@"+userDetails.getFirstName(), Toast.LENGTH_SHORT).show();
        }

        managerRecyler=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(managerRecyler);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                             @Override
                                                             public void onDataChange(DataSnapshot dataSnapshot) {
                                                                 for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                                                     String uid = snapshot.getKey();
                                                                     String bookname = snapshot.child("bookname").getValue(String.class);
                                                                     String authername = snapshot.child("authername").getValue(String.class);
                                                                     String prize = snapshot.child("prize").getValue(String.class);
                                                                     String imageurl = snapshot.child("imageUri").getValue(String.class);
                                                                     String description = snapshot.child("discription").getValue(String.class);
                                                                     String sold = snapshot.child("sold").getValue(String.class);
                                                                     String contact = snapshot.child("contact").getValue(String.class);
                                                                     nameArray.add(bookname);
                                                                     authernamearray.add(authername);
                                                                     prizeArray.add(prize);
                                                                     imageurlArray.add(imageurl);
                                                                     discriptionArray.add(description);
                                                                     uidArray.add(uid);
                                                                     soldArray.add(sold);
                                                                     contactArray.add(contact);
                                                                 }
                                                                 recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                                                                 progressBar.setVisibility(View.GONE);
                                                                 adapter = new MyRecyclerViewAdapter(MainActivity.this, nameArray,authernamearray,prizeArray,imageurlArray,soldArray);
                                                                 adapter.setClickListener(MainActivity.this);
                                                                 recyclerView.setAdapter(adapter);



                                                             }


                                                             @Override
                                                             public void onCancelled(DatabaseError databaseError) {
                                                                 Toast.makeText(MainActivity.this, "error occurred", Toast.LENGTH_SHORT).show();

                                                             }


                                                         });
       /* ArrayList<String> animalNames = new ArrayList<>();
        animalNames.add("BOOK 1");
        animalNames.add("BOOK 2");
        animalNames.add("BOOK 3");
        ArrayList<Integer> animalimages = new ArrayList<>();
        animalimages.add(R.drawable.background2);
        animalimages.add(R.drawable.background);
        animalimages.add(R.drawable.background3);
        ArrayList<String> prize = new ArrayList<>();
        prize.add("rs::100");
        prize.add("rs::200");
        prize.add("rs::300");*/
        spinner = findViewById(R.id.spinnerSort);
        arrayAdapter = ArrayAdapter.createFromResource(this, R.array.sortArray, R.layout.support_simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerItem = adapterView.getItemAtPosition(i).toString();

                  if (spinnerItem.equals("Cost-Low to High")) {
                      Toast.makeText(MainActivity.this, "low to high", Toast.LENGTH_SHORT).show();
                } else if (spinnerItem.equals("Cost-High to Low")) {
                      Toast.makeText(MainActivity.this, "hight to low", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        searchbox.addTextChangedListener(new TextWatcher(){

                                             @Override
                                             public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                                 nameArray.clear();
                                                 prizeArray.clear();
                                                 discriptionArray.clear();
                                                 authernamearray.clear();
                                                 uidArray.clear();
                                                 imageurlArray.clear();
                                                 soldArray.clear();
                                                 contactArray.clear();
                                                 recyclerView.removeAllViews();


                                             }

                                             @Override
                                             public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                                 nameArray.clear();
                                                 prizeArray.clear();
                                                 discriptionArray.clear();
                                                 authernamearray.clear();
                                                 uidArray.clear();
                                                 imageurlArray.clear();
                                                 soldArray.clear();
                                                 contactArray.clear();
                                                 recyclerView.removeAllViews();

                                             }

                                             @Override
                                             public void afterTextChanged(Editable editable) {
                                                 recyclerView.removeAllViews();
                                                 if (!editable.toString().isEmpty()){
                                                     nameArray.clear();
                                                     prizeArray.clear();
                                                     imageurlArray.clear();
                                                     discriptionArray.clear();
                                                     authernamearray.clear();
                                                     uidArray.clear();
                                                     soldArray.clear();
                                                     contactArray.clear();
                                                     recyclerView.removeAllViews();
                                                     setAdapter(editable.toString());

                                                 }
                                                 else{
                                                     nameArray.clear();
                                                     prizeArray.clear();
                                                     imageurlArray.clear();
                                                     discriptionArray.clear();
                                                     authernamearray.clear();
                                                     uidArray.clear();
                                                     soldArray.clear();
                                                     contactArray.clear();
                                                     recyclerView.removeAllViews();

                                                 }

                                             }
                                         });








        // set up the RecyclerView

        /*//FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            startActivity(new Intent(MainActivity.this,loginactivity.class));

        } else if (id == R.id.nav_signup) {
            startActivity(new Intent(MainActivity.this,signupactivity.class));

        }
         else if(id==R.id.nav_signout)
        {
            MyPreferenceManager.deleteAllData(MainActivity.this);
        }
        else if (id == R.id.nav_addbooks) {
            startActivity(new Intent(MainActivity.this,addbook.class));

        } else if (id == R.id.nav_feedback) {
            startActivity(new Intent(MainActivity.this,feedbackactivity.class));

        } else if (id == R.id.nav_share) {
            String message = "funtionality still not added";
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(share, "Title of the dialog the system will open"));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void CheckMethods(View view) {

        if (stagCheckBox.isChecked()) {
            Toast.makeText(this, "list of sold books", Toast.LENGTH_SHORT).show();
        }
        if (openCheckBox.isChecked()) {
            Toast.makeText(this, "list of unsold books", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(View view, final int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialogboxlayout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView text = (TextView) dialog.findViewById(R.id.dtv);
        String mystring = discriptionArray.get(position);
        text.setText(mystring);
        TextView text1 = (TextView) dialog.findViewById(R.id.tvdes);
        text1.setText("ABOUT");
        TextView text8 = (TextView) dialog.findViewById(R.id.tvdes1);
        text8.setText("description");
        TextView text4 = (TextView) dialog.findViewById(R.id.tvdelevery);
        text4.setText(" free delevery on purchases of rs 500");
        TextView text6 = (TextView) dialog.findViewById(R.id.tvprize);
        String prize = prizeArray.get(position);
        text6.setText("# "+"$"+prize);
        TextView text7 = (TextView) dialog.findViewById(R.id.tvname);
        String name = nameArray.get(position);
        text7.setText("# "+name);
        ImageView imageView=dialog.findViewById(R.id.imv1);
        //myRef = FirebaseDatabase.getInstance().getReference().child("books").child(uidArray.get(position)).child("extraImage");
        Glide.with(MainActivity.this).load(imageurlArray.get(position)).into(imageView);

        TextView text3 = (TextView) dialog.findViewById(R.id.tvinfo);
        text3.setText("#  please contact first to seller before proceed to pay");
        TextView text2 = (TextView) dialog.findViewById(R.id.tvcontact);
        String contact = contactArray.get(position);
        text2.setText("sellers contact no::"+contact);
        Button dialogButton = (Button) dialog.findViewById(R.id.Dbtn);
        Button dialogButton1 = (Button) dialog.findViewById(R.id.Dbtn1);
        // if button is clicked, close the custom dialog

final UserDetails userDetail=MyPreferenceManager.getUserDetail(MainActivity.this);

            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(MyPreferenceManager.getUserDetail(MainActivity.this)==null)
                    {
                        Toast.makeText(MainActivity.this, "Please Login first to pay!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    UserDetails userDetails=MyPreferenceManager.getUserDetail(MainActivity.this);

                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Please Verify your Address!!! "+"\n\n"+userDetails.getAdress())
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                    //  startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                   // startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
                                    if(soldArray.get(position).equals("unsold")) {
                                        Toast.makeText(MainActivity.this, "item in stock", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, paymentactivity.class);
                                        intent.putExtra("prize", prizeArray.get(position));
                                        intent.putExtra("uid", uidArray.get(position));
                                        startActivity(intent);
                                        dialog.dismiss();
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this, "sorry item sold out already", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                    dialog.cancel();
                                }
                            });

                    final AlertDialog alert = builder.create();
                    alert.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.rating4));
                            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.rating5));

                        }
                    });
                    alert.show();


                }
            });

        dialog.show();
    }
    public void setAdapter(final String searchString) {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            final LocationManager manager1 = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            int flag = 0;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long co;
                nameArray.clear();
                prizeArray.clear();
                discriptionArray.clear();
                authernamearray.clear();
                uidArray.clear();
                imageurlArray.clear();
                soldArray.clear();
                contactArray.clear();
               // recyclerView.removeAllViews();

                int i = 1;

                first = 101;
                last = 200;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    i++;
                    first++;
                    co = dataSnapshot.getChildrenCount();
                    String uid = snapshot.getKey();
                    String bookname = snapshot.child("bookname").getValue(String.class);
                    String authername = snapshot.child("authername").getValue(String.class);
                    String prize = snapshot.child("prize").getValue(String.class);
                    String imageurl = snapshot.child("imageUri").getValue(String.class);
                    String description = snapshot.child("discription").getValue(String.class);
                    String sold = snapshot.child("sold").getValue(String.class);
                    String contact = snapshot.child("contact").getValue(String.class);
                    assert bookname != null;
                    if (Pattern.compile(Pattern.quote(searchString), Pattern.CASE_INSENSITIVE).matcher(bookname).find()) {
                        flag = 1;

                        nameArray.add(bookname);
                        authernamearray.add(authername);
                        prizeArray.add(prize);
                        imageurlArray.add(imageurl);
                        discriptionArray.add(description);
                        uidArray.add(uid);
                        soldArray.add(sold);
                        contactArray.add(contact);

                     //   myRecyclerViewAdapter.setClickListener(MainActivity.this);


                    }

                    assert authername != null;
                    if (Pattern.compile(Pattern.quote(searchString), Pattern.CASE_INSENSITIVE).matcher(authername).find()) {
                        flag = 1;

                        nameArray.add(bookname);
                        authernamearray.add(authername);
                        prizeArray.add(prize);
                        imageurlArray.add(imageurl);
                        discriptionArray.add(description);
                        uidArray.add(uid);
                        soldArray.add(sold);
                        contactArray.add(contact);

                        //   myRecyclerViewAdapter.setClickListener(MainActivity.this);


                    }
                }
                progressBar.setVisibility(View.GONE);
                if(nameArray.size()==0)
                {
                    Toast.makeText(MainActivity.this, "No Books Found!!!", Toast.LENGTH_LONG).show();
                }
                myRecyclerViewAdapter = new MyRecyclerViewAdapter(MainActivity.this, nameArray, authernamearray, prizeArray,imageurlArray, soldArray );
                recyclerView.setAdapter(myRecyclerViewAdapter);
                myRecyclerViewAdapter.setClickListener(MainActivity.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}






