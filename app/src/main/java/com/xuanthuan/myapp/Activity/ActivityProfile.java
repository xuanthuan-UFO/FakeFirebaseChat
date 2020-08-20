package com.xuanthuan.myapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.xuanthuan.myapp.Object.ObjectUser;
import com.xuanthuan.myapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityProfile extends AppCompatActivity {
    EditText edt_gmail_profile;
    TextView txt_name_profile, txt_change_password;
    CircleImageView img_Profile;
    Toolbar toolbar;

    FirebaseAuth auth;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseStorage storage;
    FirebaseUser user;

    String useriid, img;
    int PICK_IMAGE_REQUEST = 983;
    Uri uriImg;
    ObjectUser objectUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = findViewById(R.id.toolbar);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        useriid = auth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(useriid);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getInstance().getReference("users");

        final Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        String email = bundle.getString("email");
        String name = bundle.getString("name");
        customtoolbar();
        init();

        txt_name_profile.setText(name);
        edt_gmail_profile.setText(email);
        edt_gmail_profile.setEnabled(false);

        txt_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPass();
            }
        });


        //chọn ảnh
        img_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent();
                intent1.setAction(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1, PICK_IMAGE_REQUEST);
            }
        });

        //đổi tên
        txt_name_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog();
            }
        });


    }


    private void dialogPass() {
        final Dialog dialog1 = new Dialog(this);
        dialog1.setContentView(R.layout.dialog_change_password);
        final EditText edtPass = dialog1.findViewById(R.id.change_name);
        final EditText edtPassagain = dialog1.findViewById(R.id.againpass);
        TextView btnok = dialog1.findViewById(R.id.ok);
        TextView btncanel = dialog1.findViewById(R.id.cancel);

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textPass = edtPass.getText().toString().trim();
                String textPassagain = edtPassagain.getText().toString().trim();

                if (textPass.equals(textPassagain)) {
                    user.updatePassword(textPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ActivityProfile.this, "Change Password Success", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    edtPassagain.setError("Không trùng khớp");
                }
            }
        });

        btncanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });

        dialog1.show();
    }
    @Override
    protected void onResume() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                objectUser = dataSnapshot.getValue(ObjectUser.class);
                img = objectUser.getUrlimg();
                Log.d("logimg", "onDataChange:bbb " + img);
                Glide.with(getApplicationContext()).load(img).into(img_Profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        databaseReference.addValueEventListener(valueEventListener);
        super.onResume();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uriImg = data.getData();

            final StorageReference upload = storageReference.child("image/" + useriid);
            upload.putFile(uriImg)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(ActivityProfile.this, "ok", Toast.LENGTH_SHORT).show();

                            // lay url hinh anh
                            upload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String urlImgAcount = uri.toString();

                                    //luu vao realtime
                                    databaseReference.child("urlimg").setValue(urlImgAcount);
                                    Log.d("logimg", "onSuccess:aaaa " + urlImgAcount);
                                }
                            });
                        }
                    });

        }
    }


    private void dialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_save_name);
        final EditText edtchangeName = dialog.findViewById(R.id.change_name);
        TextView txtok = dialog.findViewById(R.id.ok);
        final TextView cancel = dialog.findViewById(R.id.cancel);

        txtok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = edtchangeName.getText().toString().trim();
                if (a != null) {
                    a = edtchangeName.getText().toString().trim();
                    databaseReference.child(useriid).child("name").setValue(a);
                    txt_name_profile.setText(edtchangeName.getText().toString().trim());
                } else {
                    Toast.makeText(ActivityProfile.this, "Input", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void customtoolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //toolbar.setNavigationIcon(R.drawable.back);
    }

    private void init() {
        edt_gmail_profile = findViewById(R.id.gmail_profile);
        txt_name_profile = findViewById(R.id.name_profile);
        txt_change_password = findViewById(R.id.change_password);
        img_Profile = findViewById(R.id.add_img_profile);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }
}