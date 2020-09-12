package com.xuanthuan.myapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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
import com.xuanthuan.myapp.Object.ObjectStateOnline;
import com.xuanthuan.myapp.Object.ObjectUser;
import com.xuanthuan.myapp.Object.SetState;
import com.xuanthuan.myapp.R;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {
    public FirebaseAuth mAuth;
    EditText edtInputUser, edtInputPassword;
    Button btnLogin;
    TextView startActivityRegister, txtForgotPass;
    LoginButton loginFaceBook;
    DatabaseReference reference;
    SignInButton logingoogle;
    String userID;
    int RC_SIGN_IN = 3253;

    SetState setState = new SetState();
    CallbackManager callbackManager;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //lấy keyhasd facebook

//        try {
//            PackageInfo info = null;
//            try {
//                info = getPackageManager().getPackageInfo(
//                        "com.xuanthuan.myapp",                  //Insert your own package name.
//                        PackageManager.GET_SIGNATURES);
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            }
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (NoSuchAlgorithmException e) {
//
//        } //

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("users");


        init();

        txtForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forGotPass();
            }
        });


//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
////                .requestIdToken(getString(R.string.default_web_client_id))
////                .requestEmail()
////                .build();
////        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
////
////        logingoogle.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                signInGG();
////            }
////        });

        callbackManager = CallbackManager.Factory.create();
        loginFaceBook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user = edtInputUser.getText().toString().trim();
                final String password = edtInputPassword.getText().toString().trim();

                if (user.isEmpty()) {
                    edtInputUser.setError("Email is Required!");
                    edtInputUser.requestFocus();
                } else if (password.isEmpty()) {
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

                                userID = mAuth.getCurrentUser().getUid();
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

    private void forGotPass() {
        String nameuser = edtInputUser.getText().toString().trim();
        FirebaseAuth.getInstance().sendPasswordResetEmail(nameuser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Vui lòng Kiểm tra Email!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

//        private void signInGG() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK. khi bấm signIn fb, gọi 1 request đến fb, xong trả về callbackMânger rồi nó mới vào hàm onSuccess bên trên
        callbackManager.onActivityResult(requestCode, resultCode, data);

//
//        if (requestCode == RC_SIGN_IN) {
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//           handleSignInResult(task);
//        }
    }

//    private void handleSignInResult(Task<GoogleSignInAccount> task) {
//        try {
//            GoogleSignInAccount account = task.getResult(ApiException.class);
//
//            // Signed in successfully, show authenticated UI.
//            firebaseAuthWithGoogle(account);
//        } catch (ApiException e) {
//        }
//    }
//
//    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d("logingg", "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUIgg(user);
//                        } else {
//                            Log.d("logingg", "signInWithCredential:error");
//                            updateUIgg(null);
//                        }
//                    }
//                });
//    }


    private void init() {
        edtInputUser = findViewById(R.id.inputUser);
        edtInputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.login);
        startActivityRegister = findViewById(R.id.startActivityRegister);
        loginFaceBook = findViewById(R.id.login_faceBook);
        txtForgotPass = findViewById(R.id.txtFogotPassword);
      //  logingoogle = findViewById(R.id.login_google);


        loginFaceBook.setReadPermissions("email", "public_profile");
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("fb", "handleFacebookAccessToken:" + token);

        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("fb", "signInWithCredential:success");
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            userID = currentUser.getUid();

                            ObjectUser objectUser = new ObjectUser(userID, currentUser.getDisplayName(), currentUser.getEmail(), currentUser.getPhotoUrl() + "");
                            reference.child(userID).setValue(objectUser);

                            updateUI(currentUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("fb", "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
        }

    }


    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            setState.setState("Online");
            startActivity(new Intent(this, ActivityLoginSuccess.class));
        }
    }

    private void updateUIgg(FirebaseUser user) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account != null) {
            startActivity(new Intent(this, ActivityLoginSuccess.class));
        }
    }
}