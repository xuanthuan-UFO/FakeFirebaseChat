package com.xuanthuan.myapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xuanthuan.myapp.Adapter.Adapter_RoomMessages;
import com.xuanthuan.myapp.Object.ObjectChat;
import com.xuanthuan.myapp.Object.ObjectMessenger;
import com.xuanthuan.myapp.Object.ObjectUser;
import com.xuanthuan.myapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityMessage extends AppCompatActivity {
    DrawerLayout drawerLayout;
Toolbar toolbar;
TextView txtName, txtTimeUserOn;
CircleImageView imgAcout;
EditText edt_textSend;
ImageButton bnt_send;

String idCusstomer, idMasser, nameCusstomer, imgCusstomer, nameUser, imgUser;
DatabaseReference getImgCusstomer, getInforUser;
FirebaseUser user1;

RecyclerView recyclerView;
Adapter_RoomMessages adapter_roomMessages;
ArrayList<ObjectChat> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        init();
        takedata();
        dataFragment_Conversation();
        setToolbar();
        user1 = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);



        bnt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = edt_textSend.getText().toString().trim();

                if (text.isEmpty()) {
                    Toast.makeText(ActivityMessage.this, "input message", Toast.LENGTH_SHORT).show();
                } else {
                    sentMessage(idMasser, idCusstomer, text, nameCusstomer, imgCusstomer, String.valueOf(System.currentTimeMillis()), nameUser, imgUser);
                    edt_textSend.setText("");
                }
            }
        });

    }

    private void sentMessage(String sender, String receiver, String message, String nameReceiver, String urlImgReceiver, String date, String nameUser, String urlimgUser){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        ObjectChat objectChat = new ObjectChat(sender, receiver, message, nameReceiver, urlImgReceiver, date,nameUser, urlimgUser);
        reference.child("Chats").push().setValue(objectChat);
        Toast.makeText(ActivityMessage.this, "sented", Toast.LENGTH_SHORT).show();
    }

    private void readMessage(final String myid, final String userid, final String imgurl){
        arrayList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ObjectChat objectChat = snapshot.getValue(ObjectChat.class);

                   if (objectChat.getReceiver().equals(myid) && objectChat.getSender().equals(userid) ||
                            objectChat.getSender().equals(myid) && objectChat.getReceiver().equals(userid)) {
                    arrayList.add(objectChat);
                    }

                }
                adapter_roomMessages = new Adapter_RoomMessages(getApplicationContext(), arrayList,imgurl);
                recyclerView.setAdapter(adapter_roomMessages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void takedata() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data1");
        if (bundle != null) {
            idCusstomer = bundle.getString("id");
            idMasser = bundle.getString("idMaster");
            nameCusstomer = bundle.getString("nameCustomer");
            imgCusstomer = bundle.getString("urlimgCustomer");

            getImgCusstomer = FirebaseDatabase.getInstance().getReference("users").child(idCusstomer);
            getImgCusstomer.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ObjectUser user = dataSnapshot.getValue(ObjectUser.class);

                    txtName.setText(user.getName().trim());
                    Glide.with(getApplicationContext()).load(user.getUrlimg()).into(imgAcout);

                    Log.d("TAG", "onDataChange: " + idMasser + "  " + user.getId() + user.getUrlimg());
                    readMessage(user1.getUid(), idCusstomer , user.getUrlimg());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

        private void dataFragment_Conversation(){
        Intent intent1 = getIntent();
        Bundle bundle2 = intent1.getBundleExtra("data2");
        if (bundle2 != null) {
            idCusstomer = bundle2.getString("idCusstomer");
            idMasser = bundle2.getString("idUser");
            nameCusstomer = bundle2.getString("nameCustomer");
            imgCusstomer = bundle2.getString("urlimgCustomer");

            getImgCusstomer = FirebaseDatabase.getInstance().getReference("users").child(idCusstomer);
            getImgCusstomer.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ObjectUser user = dataSnapshot.getValue(ObjectUser.class);

                    txtName.setText(user.getName().trim());
                    Glide.with(getApplicationContext()).load(user.getUrlimg()).into(imgAcout);

                    Log.d("TAG", "onDataChange: " + idMasser + "  " + user.getId() + user.getUrlimg());
                    readMessage(user1.getUid(), idCusstomer , user.getUrlimg());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void init() {
        toolbar = findViewById(R.id.toolbarmessage);
        txtTimeUserOn = findViewById(R.id.timeUserOn);
        txtName = findViewById(R.id.name_acount_toolbar);
        imgAcout = findViewById(R.id.imgAcounttoolbar);
        edt_textSend = findViewById(R.id.text_send);
        bnt_send = findViewById(R.id.btn_send);
        recyclerView = findViewById(R.id.recyclerView);

        drawerLayout = findViewById(R.id.drawer_layout);
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    @Override
    protected void onStart() {
        getInforUser= FirebaseDatabase.getInstance().getReference("users").child(idMasser);
        getInforUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ObjectUser user2 = dataSnapshot.getValue(ObjectUser.class);

                nameUser = user2.getName();
                imgUser = user2.getUrlimg();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        super.onStart();
    }
}