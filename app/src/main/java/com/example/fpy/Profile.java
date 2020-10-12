package com.example.fpy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class Profile extends AppCompatActivity {
    //track image request
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;


    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //user details
    EditText ename,eemail,ephone,egender;
    TextView changephoto;
    User user;
    Button bsave;
    ImageView profilepic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        //initialize data
        user = User.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        final DocumentReference docRef = db.collection("landlord").document(user.getUid());

        //initialize views
        ename = findViewById(R.id.Ename);
        eemail = findViewById(R.id.Eemail);
        ephone = findViewById(R.id.Ephone);
        egender = findViewById(R.id.Egender);
        bsave = findViewById(R.id.Bsave);
        changephoto = findViewById(R.id.question);
        profilepic = findViewById(R.id.Puser);

        //setup info
        ename.setText(user.getUsername());
        ephone.setText(user.getContact());
        egender.setText(user.getGender());
        eemail.setText(user.getEmail());
        if(user.getImageurl()!=null){
            GlideApp.with(this /* context */)
                    .load(mStorageRef.child(user.getImageurl()))
                    .into(profilepic);
        }

        //set data change listener on firestore
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed: " + e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    user.setUsername(documentSnapshot.getString("name"));
                    user.setGender(documentSnapshot.getString("gender"));
                    user.setEmail(documentSnapshot.getString("email"));
                    user.setContact(documentSnapshot.getString("contact"));
                    user.setImageurl(documentSnapshot.getString("imageurl"));
                } else {
                    System.out.print("Current data: null");
                }
            }
        });

        //on click save
        bsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Data to be updated in firestore
                Map<String, Object> update = new HashMap<>();
                if(mUploadTask != null && mUploadTask.isInProgress()){
                    Log.d("image update status: ","no image selected");
                }else{
                    uploadFile();
                    update.put("imageurl", user.getUid()+"." + getFileExtension(mImageUri));
                }

                update.put("name", ename.getText().toString());
                update.put("contact", ephone.getText().toString());
                update.put("gender", egender.getText().toString());
                update.put("email", eemail.getText().toString());

                //update firestore
                docRef.set(update, SetOptions.merge());
            }
        });

        //on click change photo
        changephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(profilepic);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(user.getUid()+"." + getFileExtension(mImageUri));
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(Profile.this, "Upload successful", Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}