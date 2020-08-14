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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.xuanthuan.myapp.Object.ObjectUser;
import com.xuanthuan.myapp.R;

import java.util.HashMap;
import java.util.Map;

public class ActivitySignUp extends AppCompatActivity {
    EditText edtInputUserRegis, edtInputPasswordRegis, edtInputPasswordRegisAgain;
    Button btnLoginRegis;
    TextView startActivityLogin;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        init();
        btnLoginRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user = edtInputUserRegis.getText().toString().trim();
                final String password = edtInputPasswordRegis.getText().toString().trim();
                String inputAgainPassword = edtInputPasswordRegisAgain.getText().toString().trim();
                if (user.isEmpty()) {
                    edtInputUserRegis.setError("Email is Required!");
                    edtInputUserRegis.requestFocus();
                } else if (password.isEmpty()) {
                    edtInputPasswordRegis.setError("Password is Requied!");
                    edtInputPasswordRegis.requestFocus();
                } else if (inputAgainPassword.isEmpty()) {
                    edtInputPasswordRegisAgain.setError("Password is Requied!");
                    edtInputPasswordRegisAgain.requestFocus();
                } else if (user.isEmpty() && password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();
                } else if (!(user.isEmpty() && password.isEmpty() && inputAgainPassword.isEmpty()) && password.equals(inputAgainPassword)) {
                    mAuth.createUserWithEmailAndPassword(user, password)
                            .addOnCompleteListener(ActivitySignUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        //Log.d("logthuan", "createUserWithEmail:success");
                                        final String userID = mAuth.getCurrentUser().getUid();
                                       //Log.d("hello", "onComplete: " + userID);

                                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("users");

                                        ObjectUser objectUser = new ObjectUser(userID, "", user, "");

                                        reference1.child(userID).setValue(objectUser);

                                        startActivity(new Intent(getApplicationContext(), ActivityLoginSuccess.class));
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(getApplicationContext(), "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }

            }
        });

        startActivityLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivitySignUp.this.finish();
            }
        });

    }

    private void init() {
        edtInputUserRegis = findViewById(R.id.inputUserRegis);
        edtInputPasswordRegis = findViewById(R.id.inputPasswordRegis);
        edtInputPasswordRegisAgain = findViewById(R.id.inputPasswordAgain);
        btnLoginRegis = findViewById(R.id.register);
        startActivityLogin = findViewById(R.id.startActivityLogin);
    }
}

//    DocumentReference reference = db.collection("users").document(userID);
//                                        reference.set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                Log.d("hello", "onSuccess: " + userID);
//                                            }
//                                        });