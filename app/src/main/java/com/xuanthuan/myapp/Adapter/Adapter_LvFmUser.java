package com.xuanthuan.myapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xuanthuan.myapp.Object.ObjectUser;
import com.xuanthuan.myapp.ObjectListview.Object_LvFmUser;
import com.xuanthuan.myapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_LvFmUser extends BaseAdapter {

    Context context;
    List<ObjectUser> list;
    int layout;

    public Adapter_LvFmUser(Context context, List<ObjectUser> list, int layout) {
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

    class Vholder_user{
        CircleImageView img;
        TextView txtName;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Vholder_user holder_user = new Vholder_user();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_lv_user, null);

            holder_user.txtName = view.findViewById(R.id.name_item_user);
            holder_user.img = view.findViewById(R.id.img_item_user);

            view.setTag(holder_user);
        } else {
            holder_user = (Vholder_user) view.getTag();
        }

       // Object_LvFmUser object_lvFmUser = list.get(i);

        ObjectUser objectUser = list.get(i);
        holder_user.txtName.setText(objectUser.getName());

        Glide.with(context).load(objectUser.getUrlimg()) .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground).into(holder_user.img);

        return view;
    }
}
