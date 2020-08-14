package com.xuanthuan.myapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.xuanthuan.myapp.Object.ObjectChatAll;
import com.xuanthuan.myapp.ObjectListview.Object_LvConversation;
import com.xuanthuan.myapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_LvGlobalRoom extends RecyclerView.Adapter<Adapter_LvGlobalRoom.ViewHolderRoomAll> {

    List<ObjectChatAll> list;
    Context context;
    int myID =0;
    int uID = 1;

    public Adapter_LvGlobalRoom() {
    }

    public Adapter_LvGlobalRoom(List<ObjectChatAll> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderRoomAll onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_all_myid, parent, false);
            return new ViewHolderRoomAll(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_all_uid, parent, false);
            return new ViewHolderRoomAll(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderRoomAll holder, int position) {

        ObjectChatAll objectChatAll = list.get(position);

        holder.txtDate.setText(objectChatAll.getDate());
        holder.txtMessage.setText(objectChatAll.getMessage());

        holder.txtName.setText(objectChatAll.getNameUser());
        Glide.with(context).load(objectChatAll.getImgUser()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderRoomAll extends RecyclerView.ViewHolder {
        CircleImageView img;
        TextView txtName, txtMessage, txtDate;
        public ViewHolderRoomAll(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgChatAll);
            txtName  = itemView.findViewById(R.id.nameChatAll);
            txtMessage = itemView.findViewById(R.id.message_all);
            txtDate = itemView.findViewById(R.id.date_roomall);
        }
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (list.get(position).getIdUser().equals(user.getUid())) {
            return myID;
        } else {
            return uID;
        }
    }
}
