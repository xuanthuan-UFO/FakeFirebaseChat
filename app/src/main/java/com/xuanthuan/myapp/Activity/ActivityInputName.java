package com.xuanthuan.myapp.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.xuanthuan.myapp.R;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityInputName extends AppCompatActivity {
    EditText edtName;
    ImageView img_inputImg;
    Button btnStart;
    String checkName, userID;

    FirebaseAuth auth;
    FirebaseFirestore firestore;
    int PICK_IMAGE_REQUEST = 684;
    Uri urlimg;
    ActivityLoginSuccess activityLoginSuccess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_name);
        //activityLoginSuccess.finish();
        init();
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userID = auth.getCurrentUser().getUid();

        checkName = edtName.getText().toString().trim();


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkName != null) {
                    checkName = edtName.getText().toString().trim();
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("users");

                    reference1.child(userID).child("name").setValue(checkName);

                    finish();
                } else {
                    Toast.makeText(ActivityInputName.this, "Input Name!", Toast.LENGTH_SHORT).show();
                }

                if (urlimg != null) {
                    final StorageReference storageReference = FirebaseStorage.getInstance().getReference("users").child(userID);
                    storageReference.putFile(urlimg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String img = uri.toString();

                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(userID);
                                    reference.child("urlimg").setValue(img);
                                }
                            });
                        }
                    });
                }
            }
        });

        img_inputImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent();
                intent1.setAction(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1, PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null)
            urlimg = data.getData();
        Glide.with(getApplicationContext()).load(urlimg).into(img_inputImg);

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init() {
        edtName = findViewById(R.id.inputname);
        img_inputImg = findViewById(R.id.input_img);
        btnStart = findViewById(R.id.start);
    }
}