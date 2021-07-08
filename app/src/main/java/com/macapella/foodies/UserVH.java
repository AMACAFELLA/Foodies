package com.macapella.foodies;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class UserVH  extends RecyclerView.ViewHolder {
    public TextView txt_name, txt_phone, txt_email, txt_acc, txt_option;
    public UserVH(@NonNull @NotNull View itemView) {
        super(itemView);
        txt_name = itemView.findViewById(R.id.txt_name);
        txt_phone = itemView.findViewById(R.id.txt_phone);
        txt_email = itemView.findViewById(R.id.txt_email);
        txt_acc = itemView.findViewById(R.id.txt_acc);
        txt_option = itemView.findViewById(R.id.txt_option);
    }
}
