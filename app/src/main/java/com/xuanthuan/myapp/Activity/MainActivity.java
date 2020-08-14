package com.xuanthuan.myapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.xuanthuan.myapp.Object.ObjectUser;
import com.xuanthuan.myapp.R;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {
    public FirebaseAuth mAuth;
EditText edtInputUser, edtInputPassword;
Button btnLogin;
TextView startActivityRegister;
    public FirebaseFirestore db;
    ObjectUser objectUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        init();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user = edtInputUser.getText().toString().trim();
                final String password = edtInputPassword.getText().toString().trim();

                if (user.isEmpty()) {
                    edtInputUser.setError("Email is Required!");
                    edtInputUser.requestFocus();
                }else if (password.isEmpty()) {
                    edtInputPassword.setError("Password is Requied!");
                    edtInputPassword.requestFocus();
                } else if (user.isEmpty() && password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();
                } else if (!(user.isEmpty() && password.isEmpty())) {
                    mAuth.signInWithEmailAndPassword(user, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser userFirebase = mAuth.getCurrentUser();

                                final String userID = mAuth.getCurrentUser().getUid();

                                reference = FirebaseDatabase.getInstance().getReference("users");

                                updateUI(userFirebase);
                            } else {
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
                }
            }
        });

        startActivityRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ActivitySignUp.class));
            }
        });
    }


    private void init() {
        edtInputUser = findViewById(R.id.inputUser);
        edtInputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.login);
        startActivityRegister = findViewById(R.id.startActivityRegister);

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            startActivity(new Intent(this, ActivityLoginSuccess.class));
        }
    }
}