package com.macapella.foodies;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.graph.ImmutableNetwork;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

public class RVAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    ArrayList<User> list = new ArrayList<>();
    public RVAdapter(Context ctx)
    {
        this.context = ctx;
    }
    public void setItems(ArrayList<User> u)
    {
        list.addAll(u);
    }
    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item,parent,false);
        return new UserVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        UserVH vh = (UserVH) holder;
        User u = list.get(position);
        vh.txt_name.setText(u.getFullname());
        vh.txt_phone.setText(u.getPhone());
        vh.txt_email.setText(u.getEmail());
        vh.txt_option.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, vh.txt_option);
            popupMenu.inflate(R.menu.option_menu);
            popupMenu.setOnMenuItemClickListener(item ->
            {
                switch (item.getItemId())
                {
                    case R.id.menu_edit:
                        Intent intent =new Intent(context,AdminActivity.class);
                        intent.putExtra("EDIT", u);
                        context.startActivity(intent);
                        break;
                    case R.id.menu_remove:
                        DAOAdmin dao = new DAOAdmin();
                        dao.delete(u.getKey()).addOnSuccessListener(suc ->
                        {
                            Toast.makeText(context,"User was updated", Toast.LENGTH_SHORT).show();
                            notifyItemRemoved(position);
                        }).addOnFailureListener(er->
                        {
                            Toast.makeText(context, ""+er.getMessage(),Toast.LENGTH_SHORT).show();

                        });
                        break;
                }
                return false;
            });
            popupMenu.show();
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
