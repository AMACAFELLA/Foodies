package com.macapella.foodies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.viewHolder> {

    private Context mContext;
    private List<ItemModel> mData;

    public RecyclerViewAdapter(Context mContext, List<ItemModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.menu_item, parent, false);

        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.viewHolder holder, int position) {

        holder.name.setText(mData.get(position).getName());
        holder.description.setText(mData.get(position).getDescription());
        holder.price.setText(mData.get(position).getPrice());

        Glide.with(mContext)
                .load(mData.get(position).getImg())
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView description;
        TextView price;
        ImageView img;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.itemName);
            description = itemView.findViewById(R.id.itemDescription);
            price = itemView.findViewById(R.id.itemPrice);
            img = itemView.findViewById(R.id.itemImage);
        }
    }
}
