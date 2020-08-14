package com.xuanthuan.myapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.xuanthuan.myapp.Object.ObjectChat;
import com.xuanthuan.myapp.ObjectListview.Object_LvConversation;
import com.xuanthuan.myapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_LvConversation extends BaseAdapter {

    FirebaseUser user;

    Context context;
    List<ObjectChat> list;
    int layout;

    public Adapter_LvConversation(Context context, List<ObjectChat> list, int layout) {
        this.context = context;
        this.list = list;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    class Viewholer_Conversation   {
        TextView txtName, txtMessage, txtdate;
        CircleImageView img_Acount;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Viewholer_Conversation holder = new Viewholer_Conversation();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_lv_conversation, null);

            holder.txtName = view.findViewById(R.id.name_profile_conversation);
            holder.txtMessage = view.findViewById(R.id.message_show);
            holder.txtdate = view.findViewById(R.id.date_show);
            holder.img_Acount = view.findViewById(R.id.img_profile_conversation);

            view.setTag(holder);
        } else {
            holder = (Viewholer_Conversation) view.getTag();
        }

        ObjectChat objectChat = list.get(i);

        user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        if (objectChat.getReceiver().equals(uid)) {
            holder.txtName.setText(objectChat.getNameuser());
            Glide.with(context).load(objectChat.getUrlimgUser()).into(holder.img_Acount);
        } else {
            holder.txtName.setText(objectChat.getName());
            Glide.with(context).load(objectChat.getUrlimgreceiver()).into(holder.img_Acount);
        }
        holder.txtMessage.setText(objectChat.getMessage());
        holder.txtdate.setText(objectChat.getDate());



        return view;
    }
}
