package com.xuanthuan.myapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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
import com.google.gson.internal.$Gson$Preconditions;
import com.xuanthuan.myapp.Adapter.Adapter_vPager_FmUser;
import com.xuanthuan.myapp.Object.ObjectUser;
import com.xuanthuan.myapp.Object.SetState;
import com.xuanthuan.myapp.R;
import com.xuanthuan.myapp.fragment.Fragment_Conversation;
import com.xuanthuan.myapp.fragment.Fragment_Global_Room;
import com.xuanthuan.myapp.fragment.Fragment_Share;
import com.xuanthuan.myapp.fragment.Fragment_user;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityLoginSuccess extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    FrameLayout frameLayout;
    TextView txtEmail_account, txtName_account;
    CircleImageView img_Acount;
    String userID, nameAcount, urlImgAcount, email;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference databaseReference;

    ObjectUser objectUser;
    SetState setState = new SetState();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = auth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID);


        init();
        setToolbar();
        //addFm();
        setTxtEmail();

        //viewPager.setCurrentItem(0);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment_Conversation fragment_conversation = new Fragment_Conversation();
        transaction.add(R.id.framelayout, fragment_conversation);
        transaction.commit();

        openProfile();


    }

    private void openProfile() {
        txtName_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityLoginSuccess.this, ActivityProfile.class);
                Bundle bundle = new Bundle();
                bundle.putString("email", email);
                bundle.putString("name", nameAcount);
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });

        txtEmail_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityLoginSuccess.this, ActivityProfile.class);
                Bundle bundle = new Bundle();
                bundle.putString("email", email);
                bundle.putString("name", nameAcount);
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });

        img_Acount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityLoginSuccess.this, ActivityProfile.class);
                Bundle bundle = new Bundle();
                bundle.putString("email", email);
                bundle.putString("name", nameAcount);
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });
    }

    private void setTxtEmail() {
        if (user != null) {
            email = user.getEmail();
            txtEmail_account.setText(email);

        }

    }

    private void setToolbar() {
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void init() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        NavigationView navigationViewbottom = findViewById(R.id.nav_view_bottom);
        //viewPager = findViewById(R.id.view_Pager_fm);
        toolbar = findViewById(R.id.toolbar);

        frameLayout = findViewById(R.id.framelayout);
        //viewPager.setOffscreenPageLimit(2);
        navigationViewbottom.setNavigationItemSelectedListener(this);


        View headview = navigationView.getHeaderView(0);

        txtName_account = headview.findViewById(R.id.name_account);
        txtEmail_account = headview.findViewById(R.id.gmail_account);
        img_Acount = headview.findViewById(R.id.img_account);

        drawerLayout = findViewById(R.id.drawer_layout);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        /////////////////////////////////////////////////////////////////////////
        setState.setState("Offline");
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.conversations:
                toolbar.setTitle("Conversations");
                fragment = new Fragment_Conversation();
                drawerLayout.closeDrawer(GravityCompat.START);
                //viewPager.setCurrentItem(0);
                break;
            case R.id.user:
                fragment = new Fragment_user();
                drawerLayout.closeDrawer(GravityCompat.START);
                toolbar.setTitle("User");
                //viewPager.setCurrentItem(1);
                break;
            case R.id.global_room:
                fragment = new Fragment_Global_Room();
                drawerLayout.closeDrawer(GravityCompat.START);
                toolbar.setTitle("Golbal Room");
                //viewPager.setCurrentItem(2);
                break;
            case R.id.share:
                fragment = new Fragment_Share();
                drawerLayout.closeDrawer(GravityCompat.START);
                toolbar.setTitle("Share");
                //viewPager.setCurrentItem(3);
                break;
            case R.id.profile:
                Intent intent = new Intent(ActivityLoginSuccess.this, ActivityProfile.class);
                Bundle bundle = new Bundle();
                bundle.putString("email", email);
                bundle.putString("name", nameAcount);
                intent.putExtra("data", bundle);
                startActivity(intent);
                break;
            case R.id.logout: //////////////////////////////////////////////////////////
                setState.setState("Offline");
                FirebaseAuth.getInstance().signOut();
                finish();
                break;
        }

        if (item.getItemId() != R.id.profile && item.getItemId()!= R.id.logout) {
            transaction.replace(R.id.framelayout, fragment);
            transaction.commit();
        }

        return true;
    }

    @Override
    protected void onResume() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                objectUser = dataSnapshot.getValue(ObjectUser.class);
                urlImgAcount = objectUser.getUrlimg().trim();
                nameAcount = objectUser.getName().trim();
                Log.d("ccc", "onDataChange: " + nameAcount + "-------");

                Glide.with(getApplication()).load(urlImgAcount).into(img_Acount);

                if (nameAcount.isEmpty()) {
                    startActivity(new Intent(ActivityLoginSuccess.this, ActivityInputName.class));
                } else {
                    txtName_account.setText(nameAcount);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        databaseReference.addValueEventListener(valueEventListener);

        super.onResume();
    }

}



