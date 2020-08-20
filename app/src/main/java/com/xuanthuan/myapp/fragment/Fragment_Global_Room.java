package com.xuanthuan.myapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xuanthuan.myapp.Adapter.Adapter_LvGlobalRoom;
import com.xuanthuan.myapp.Object.ObjectChatAll;
import com.xuanthuan.myapp.Object.ObjectUser;
import com.xuanthuan.myapp.Object.VerticalSpaceItemDecoration;
import com.xuanthuan.myapp.R;

import java.util.ArrayList;

public class Fragment_Global_Room extends Fragment {

    EditText edtMessage;
    ImageButton btnSendAll;

    String uID, nameUser, imgUser;

    RecyclerView recyclerView;
    Adapter_LvGlobalRoom adapter_lvGlobalRoom;
    ArrayList<ObjectChatAll> arrayList;
    View view;

    FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_global_room, container, false);

        init();

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);


        //setkhoảng cách
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(20));


        readMessage();

        btnSendAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = edtMessage.getText().toString().trim();

                if (message.isEmpty()) {
                    Toast.makeText(getActivity(), "Input message", Toast.LENGTH_SHORT).show();
                } else {
                    sendMessage(nameUser, uID, imgUser, String.valueOf(System.currentTimeMillis()), message);
                    edtMessage.setText("");
                }
            }
        });

        return view;
    }

    private void sendMessage(String nameUser, String idUser, String imgUser, String date, String message) {
        DatabaseReference createMessage = FirebaseDatabase.getInstance().getReference();
        ObjectChatAll objectChatAll = new ObjectChatAll(nameUser, idUser, imgUser, date, message);
        createMessage.child("ChatAll").push().setValue(objectChatAll);

    }

    private void init() {
        recyclerView = view.findViewById(R.id.viewChatAll);
        btnSendAll = view.findViewById(R.id.btn_send_All);
        edtMessage = view.findViewById(R.id.text_send_All);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uID = user.getUid();

        DatabaseReference getDataUser = FirebaseDatabase.getInstance().getReference("users").child(uID);
        getDataUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ObjectUser objectUser = dataSnapshot.getValue(ObjectUser.class);
                nameUser = objectUser.getName();
                imgUser = objectUser.getUrlimg();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void readMessage(){
        arrayList = new ArrayList<>();
        DatabaseReference chat_All = FirebaseDatabase.getInstance().getReference("ChatAll");
        chat_All.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ObjectChatAll objectChatAll = ds.getValue(ObjectChatAll.class);
                    arrayList.add(objectChatAll);
                }
                if (arrayList != null) {
                    adapter_lvGlobalRoom = new Adapter_LvGlobalRoom(arrayList, getActivity());
                    recyclerView.setAdapter(adapter_lvGlobalRoom);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
