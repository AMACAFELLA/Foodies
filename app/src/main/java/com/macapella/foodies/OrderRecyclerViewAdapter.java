package com.macapella.foodies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.viewHolder> {

    private final Context mContext;
    private final List<ItemModel> mData;

    public OrderRecyclerViewAdapter(Context mContext, List<ItemModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.order_item, parent, false);

        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderRecyclerViewAdapter.viewHolder holder, int position) {

        holder.name.setText(mData.get(position).getName());
        Integer pricePerItem = mData.get(position).getPrice();
        Integer quantityOfItem = mData.get(position).getQuantity();
        int priceNumber = pricePerItem * quantityOfItem;
        String price = Integer.toString(priceNumber);
        holder.price.setText(price);
        holder.quantity.setText(mData.get(position).getQuantity().toString());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView price;
        TextView quantity;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.confirmOrderItemName);
            price = itemView.findViewById(R.id.orderItemTotalPrice);
            quantity = itemView.findViewById(R.id.orderItemQuantity);
        }

    }
}
