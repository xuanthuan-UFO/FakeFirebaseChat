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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.xuanthuan.myapp.Activity.ActivityMessage;
import com.xuanthuan.myapp.Adapter.Adapter_LvFmUser;
import com.xuanthuan.myapp.Adapter.Adapter_vPager_FmUser;
import com.xuanthuan.myapp.Object.ObjectMessenger;
import com.xuanthuan.myapp.Object.ObjectUser;
import com.xuanthuan.myapp.ObjectListview.Object_LvFmUser;
import com.xuanthuan.myapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Fragment_user extends Fragment {
    ListView listView;
    ArrayList<ObjectUser> arrayList;
    Adapter_LvFmUser adapter_lvFmUser;
    ArrayList<String> listUserID, listName, listImg;

    FirebaseAuth auth;
    FirebaseUser user;
    String idCusstomer, userID, nameCustomer, urlimgCustomer;
    DatabaseReference reference, reference1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        userID = auth.getCurrentUser().getUid();

        listUserID = new ArrayList<>();
        arrayList = new ArrayList<>();
        listName = new ArrayList<>();
        listImg = new ArrayList<>();
        listView = view.findViewById(R.id.lv_FmUser);
        addUser();

        createRoom();

        return view;


    }

    private void createRoom() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int vitri, long l) {
                idCusstomer = listUserID.get(vitri);
                nameCustomer = listName.get(vitri);
                urlimgCustomer = listImg.get(vitri);

                Intent intent = new Intent(getActivity(), ActivityMessage.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", idCusstomer);
                bundle.putString("idMaster", userID);
                bundle.putString("nameCustomer", nameCustomer);
                bundle.putString("urlimgCustomer", urlimgCustomer);
                intent.putExtra("data1", bundle);
                startActivity(intent);

                //edt message gui tin nhắn, thời gian lấy ở datetime của máy, ngày cũg thế,, set toolbar lấy id khách từ idroom. 

            }
        });
    }

    private void addUser() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ObjectUser objectUser = ds.getValue(ObjectUser.class);

                    if (!(objectUser.getId().equals(userID))){
                        listUserID.add(objectUser.getId());
                        listName.add(objectUser.getName());
                        listImg.add(objectUser.getUrlimg());
                        arrayList.add(objectUser);
                       // Log.d("user", "onDataChange: " + arrayList.get(0).getName());
                    }

                    adapter_lvFmUser = new Adapter_LvFmUser(getActivity(),arrayList, R.layout.item_lv_user);
                    listView.setAdapter(adapter_lvFmUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("user", "onCancelled: " + databaseError.getMessage());
            }
        });

    }


}
