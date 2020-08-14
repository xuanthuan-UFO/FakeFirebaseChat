package com.xuanthuan.myapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.internal.$Gson$Preconditions;
import com.xuanthuan.myapp.Activity.ActivityMessage;
import com.xuanthuan.myapp.Adapter.Adapter_LvConversation;
import com.xuanthuan.myapp.Object.ObjectChat;
import com.xuanthuan.myapp.Object.ObjectUser;
import com.xuanthuan.myapp.ObjectListview.Object_LvConversation;
import com.xuanthuan.myapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fragment_Conversation extends Fragment {
    FirebaseUser user;

    ArrayList<ObjectChat> arrayList;
    Adapter_LvConversation adapter_lvConversation;
    ArrayList<String> checkId , imgReceiver, nameCusstomer;
    ListView lv;

    String uID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversations, container, false);

        lv = view.findViewById(R.id.lvConversation);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uID = user.getUid();
        openMessage();

        return view;
    }

    private void openMessage() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String idCustomer = checkId.get(i);
                String urlimgCustomer = imgReceiver.get(i);
                String nameCustomer = nameCusstomer.get(i);

                Intent intent = new Intent(getActivity(), ActivityMessage.class);
                Bundle bundle = new Bundle();
                bundle.putString("idUser", uID);
                bundle.putString("idCusstomer", idCustomer);
                bundle.putString("urlimgCustomer", urlimgCustomer);
                bundle.putString("nameCustomer", nameCustomer);
                intent.putExtra("data2", bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        nameCusstomer = new ArrayList<>();
        imgReceiver = new ArrayList<>();
        checkId = new ArrayList<>();
        arrayList = new ArrayList<>();
        DatabaseReference laydata = FirebaseDatabase.getInstance().getReference("Chats");
        laydata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imgReceiver.clear();
                nameCusstomer.clear();
                checkId.clear();
                arrayList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ObjectChat objectChat = ds.getValue(ObjectChat.class);

                    String idReceiver = objectChat.getReceiver();
                    String idSend = objectChat.getSender();

                    if (idSend.equals(uID)) {
                        if ((!checkId.contains(idReceiver))) {
                            imgReceiver.add(0, objectChat.getUrlimgreceiver());
                            nameCusstomer.add(0, objectChat.getName());
                            checkId.add(0, idReceiver);
                            arrayList.add(0, objectChat);
                        } else{
                            int a = checkId.indexOf(idReceiver);
                            imgReceiver.remove(a);
                            nameCusstomer.remove(a);
                            arrayList.remove(a);
                            checkId.remove(a);

                            arrayList.add(0, objectChat);
                            checkId.add(0, idReceiver);
                            imgReceiver.add(0, objectChat.getUrlimgreceiver());
                            nameCusstomer.add(0, objectChat.getName());
                        }
                    }

                    if (idReceiver.equals(uID)) {
                        if (!checkId.contains(objectChat.getSender())) {
                            imgReceiver.add(0, objectChat.getUrlimgUser());
                            nameCusstomer.add(0, objectChat.getNameuser());

                            checkId.add(0, objectChat.getSender());
                            arrayList.add(0 ,objectChat);

                        } else if (checkId.contains(objectChat.getSender())) {
                            int b = checkId.indexOf(idSend);
                            checkId.remove(b);
                            arrayList.remove(b);
                            arrayList.add(0, objectChat);
                            checkId.add(0, idSend);
                        }
                    }
                }
                adapter_lvConversation = new Adapter_LvConversation(getActivity(), arrayList, R.layout.item_lv_conversation);
                lv.setAdapter(adapter_lvConversation);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        super.onResume();
    }

}
