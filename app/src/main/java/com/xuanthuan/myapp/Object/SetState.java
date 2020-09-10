package com.xuanthuan.myapp.Object;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SetState {

    public SetState() {
    }

    public void setState(String state){
        SimpleDateFormat formattime = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatdate = new SimpleDateFormat("dd MM yyyy");

        Calendar calendar = Calendar.getInstance();

        String time = formattime.format(calendar.getTime());
        String date = formatdate.format(calendar.getTime());

        ObjectStateOnline objectStateOnline = new ObjectStateOnline(date,time, state);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(userID).child("UserState").setValue(objectStateOnline);
    }
}
