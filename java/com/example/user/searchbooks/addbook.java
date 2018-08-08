package com.example.user.searchbooks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.ArrayList;
import java.util.Objects;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class addbook extends AppCompatActivity {
    EditText book_name;
    EditText book_Aname;
    EditText book_prize;
    EditText contact;
    RecyclerView recyclerView;
    EditText book_discription;
    Button addbook;
    Button upload;
    String bookname;
    String authername;
    String prize;
    String contac_no;
    String discription;
    FirebaseAuth mAuth;
    public static final int REQUEST_CODE=1234;
    int itemCount=0;
    StorageReference storageReference;

    int count;
    private Uri imageUri;
    private ArrayList<String> fileNameList;
    private UploadImageAdapter uploadImageAdapter;
    private ArrayList<String> fileDoneList;
    private ArrayList<String> imagesList;
    private ArrayList<Uri> imageUriArray;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference databaseReference=database.getReference();
    DatabaseReference databaseReference1=databaseReference.child("books");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbook);
        book_name = findViewById(R.id.et1);
        book_Aname = findViewById(R.id.et2);
        book_prize = findViewById(R.id.et3);
        contact = findViewById(R.id.et5);
        book_discription = findViewById(R.id.et4);
        recyclerView = findViewById(R.id.recylerview1);
        upload=findViewById(R.id.btn3);
        fileNameList=new ArrayList<>();
        fileDoneList=new ArrayList<>();
        imageUriArray=new ArrayList<>();
        uploadImageAdapter = new UploadImageAdapter(addbook.this,fileDoneList, fileNameList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(uploadImageAdapter);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Blogger Sans-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        storageReference=FirebaseStorage.getInstance().getReference();
        addbook=findViewById(R.id.btn);
        mAuth = FirebaseAuth.getInstance();
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Intent intent=new Intent();
                intent.setType("image*//*");
*/
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");
                getIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                getIntent.setType(Intent.ACTION_GET_CONTENT);
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");
                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
                startActivityForResult(chooserIntent, REQUEST_CODE);
               /* intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setType(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"select image"),REQUEST_CODE);
              // */
            }
        });
        addbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MyPreferenceManager.getUserDetail(addbook.this)==null) {
                    Toast.makeText(addbook.this, "Please login first", Toast.LENGTH_SHORT).show();
                    return;
                }

                bookname = book_name.getText().toString();
                authername = book_Aname.getText().toString();
                prize = book_prize.getText().toString();
                discription = book_discription.getText().toString();
                contac_no=contact.getText().toString();

                if (bookname.isEmpty()) {
                    book_name.setError("enter book name");
                    return;
                }
                if (authername.isEmpty()) {
                    book_Aname.setError("enter authername");
                    return;
                }
                if (prize.isEmpty()) {
                    book_prize.setError("enter prize in rs");
                    return;
                }
                if (discription.isEmpty()) {
                    book_discription.setError("enter discription");
                    return;
                }
                if (contac_no.isEmpty()) {
                    contact.setError("enter vaid phn no");
                    return;
                }
                final String id=databaseReference1.push().getKey();
                for(int j=0;j<itemCount;j++)
                {
                    final ProgressDialog dialog=new ProgressDialog(addbook.this);
                    dialog.setTitle("Uploading Data");
                    dialog.show();
                    StorageReference fileToupload = storageReference.child("images").child(fileNameList.get(j));
                    final int finalI = j;



                    fileToupload.putFile(imageUriArray.get(j)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.dismiss();
                            // databaseReference.child(nameET).child("imageUri").child(filename).setValue(taskSnapshot.getDownloadUrl().toString());
                            if(finalI==0)
                                //imagesList.add(Objects.requireNonNull(taskSnapshot.getDownloadUrl()).toString());
                            // Toast.makeText(AddRestaurant.this, imagesList.get(0), Toast.LENGTH_SHORT).show();

                            databaseReference1.child(id).child("extraImage").push().setValue(Objects.requireNonNull(taskSnapshot.getDownloadUrl()).toString());
                            if(finalI ==0)
                                databaseReference1.child(id).child("imageUri").setValue(Objects.requireNonNull(taskSnapshot.getDownloadUrl()).toString());

                            fileDoneList.remove(finalI);
                            fileDoneList.add(finalI, "done");
                            uploadImageAdapter.notifyDataSetChanged();
                            count++;

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress=(100 * taskSnapshot.getBytesTransferred() /taskSnapshot.getTotalByteCount());
                            dialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });


                }

                        databaseReference1.child(id).child("bookname").setValue(bookname);
                        databaseReference1.child(id).child("authername").setValue(authername);
                        databaseReference1.child(id).child("prize").setValue(prize);
                        databaseReference1.child(id).child("discription").setValue(discription);
                        databaseReference1.child(id).child("sold").setValue("unsold");
                        databaseReference1.child(id).child("contact").setValue(contac_no);






                book_discription.setText("");
                book_prize.setText("");
                book_Aname.setText("");
                book_name.setText("");
                contact.setText("");



            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                count=0;
                int totalItemSelected = data.getClipData().getItemCount();
                itemCount+=totalItemSelected;
                for (int i = 0; i < totalItemSelected; i++) {
                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    final String filename = getFileName(fileUri);
                    fileNameList.add(filename);
                    fileDoneList.add("uploading");
                    imageUriArray.add(fileUri);
                    uploadImageAdapter.notifyDataSetChanged();

                   /* StorageReference fileToupload = storageReference.child("images").child(filename);
                    final int finalI = i;
                    fileToupload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // databaseReference.child(nameET).child("imageUri").child(filename).setValue(taskSnapshot.getDownloadUrl().toString());
                            imagesList.add(taskSnapshot.getDownloadUrl().toString());
                           // Toast.makeText(AddRestaurant.this, imagesList.get(0), Toast.LENGTH_SHORT).show();

                            fileDoneList.remove(finalI);
                            fileDoneList.add(finalI, "done");
                            uploadImageAdapter.notifyDataSetChanged();
                            count++;

                        }
                    });*/

                }

            } else if (data.getData() != null) {
                Uri fileUri = data.getData();
                itemCount++;
                final String filename = getFileName(fileUri);
                fileNameList.add(filename);
                imageUriArray.add(fileUri);
                fileDoneList.add("uploading");
                uploadImageAdapter.notifyDataSetChanged();

                /*StorageReference fileToupload = storageReference.child("images").child(filename);
                fileToupload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // databaseReference.child(nameET).child("imageUri").child(filename).setValue(taskSnapshot.getDownloadUrl().toString());
                        imagesList.add(taskSnapshot.getDownloadUrl().toString());

                        fileDoneList.remove(0);
                        fileDoneList.add(0, "done");
                        uploadImageAdapter.notifyDataSetChanged();

                    }
                });
*/
            }
        }


    }
    public String getFileName(Uri uri)
    {
        String result=null;
        if(uri.getScheme().equals("content"))
        {
            Cursor cursor=getContentResolver().query(uri,null,null,null,null);

            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                }
            }finally {
                cursor.close();
            }



        }

        if (result==null)
        {
            result=uri.getPath();
            int cut=result.lastIndexOf("/");
            if(cut!=-1)
            {
                result=result.substring(cut+1);

            }
        }
        return  result;
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

