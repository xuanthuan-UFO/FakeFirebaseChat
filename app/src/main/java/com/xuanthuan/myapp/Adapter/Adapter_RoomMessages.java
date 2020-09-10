package com.xuanthuan.myapp.Adapter;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.xuanthuan.myapp.Object.ObjectChat;
import com.xuanthuan.myapp.Object.ObjectMessenger;
import com.xuanthuan.myapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_RoomMessages extends RecyclerView.Adapter<Adapter_RoomMessages.ViewholderRoom> {

    public static final int MSG_Left = 0;
    public static final int MSG_Right = 1;
    FirebaseUser user;

    Context context;
    List<ObjectChat> list;
    String imgurrl;

    public Adapter_RoomMessages(Context context, List<ObjectChat> list, String imgurrl) {
        this.context = context;
        this.list = list;
        this.imgurrl = imgurrl;
    }

    @NonNull
    @Override
    public ViewholderRoom onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_user, parent, false);
            return new ViewholderRoom(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_master, parent, false);
            return new ViewholderRoom(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewholderRoom holder, int position) {

        ObjectChat objectChat = list.get(position);

        holder.showMessage.setText(objectChat.getMessage());
        holder.timeMessage.setText(objectChat.getDate());

        Glide.with(context).load(imgurrl).into(holder.img_sender);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class ViewholderRoom extends RecyclerView.ViewHolder{
        TextView timeMessage;
        TextView showMessage;
        CircleImageView img_sender;
        public ViewholderRoom(@NonNull View itemView) {
            super(itemView);

            timeMessage = itemView.findViewById(R.id.timeMessage);
            showMessage = itemView.findViewById(R.id.message_master);
            img_sender = itemView.findViewById(R.id.profile_img_master);

        }
    }

    @Override
    public int getItemViewType(int position) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (list.get(position).getSender().equals(user.getUid())) {
            return MSG_Right;
        } else {
            return MSG_Left;
        }
    }
}
