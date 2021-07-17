package com.macapella.foodies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminOrderDetailsRecyclerViewAdapter extends RecyclerView.Adapter<AdminOrderDetailsRecyclerViewAdapter.viewHolder> {

    private final Context mContext;
    private final List<ItemModel> mData;

    public AdminOrderDetailsRecyclerViewAdapter(Context mContext, List<ItemModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.admin_orders_details_item, parent, false);

        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminOrderDetailsRecyclerViewAdapter.viewHolder holder, int position) {

        holder.name.setText(mData.get(position).getName());
        String quantity = "X" + Integer.toString(mData.get(position).getQuantity());
        holder.quantity.setText(quantity);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView quantity;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.adminOrderDetailItem);
            quantity = itemView.findViewById(R.id.adminOrderDetailQuantity);
        }

    }
}
