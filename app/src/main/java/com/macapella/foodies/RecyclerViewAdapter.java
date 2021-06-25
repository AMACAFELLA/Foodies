package com.macapella.foodies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.viewHolder> {

    private final Context mContext;
    private final List<ItemModel> mData;

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
        holder.price.setText(mData.get(position).getPrice().toString());
        holder.quantity.setText(mData.get(position).getQuantity().toString());

        holder.reduceQuantity.setOnClickListener(
                (view) -> {
                    TextView quantityView = holder.quantity;
                    int quantity = Integer.parseInt(quantityView.getText().toString());
                    if (quantity > 0) {
                        quantity -= 1;
                        quantityView.setText(Integer.toString(quantity));
                    }
                }
        );

        holder.increaseQuantity.setOnClickListener(
                (view) -> {
                    TextView quantityView = holder.quantity;
                    int quantity = Integer.parseInt(quantityView.getText().toString());
                        quantity += 1;
                        quantityView.setText(Integer.toString(quantity));

                }
        );

        holder.addToCart.setOnClickListener(
                (view) -> {
                    HomeActivity homeActivity = new HomeActivity();
                    TextView quantityView = holder.quantity;
                    TextView priceView = holder.price;
                    TextView nameView = holder.name;
                    homeActivity.addToCart(nameView.getText().toString(), Integer.parseInt(priceView.getText().toString()), Integer.parseInt(quantityView.getText().toString()));

                }
        );

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
        TextView quantity;
        Button reduceQuantity;
        Button increaseQuantity;
        Button addToCart;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            reduceQuantity = itemView.findViewById(R.id.reduceQuantity);
            increaseQuantity = itemView.findViewById(R.id.increaseQuantity);
            addToCart = itemView.findViewById(R.id.addToCart);

            name = itemView.findViewById(R.id.itemName);
            description = itemView.findViewById(R.id.itemDescription);
            price = itemView.findViewById(R.id.itemPrice);
            img = itemView.findViewById(R.id.itemImage);
            quantity = itemView.findViewById(R.id.quantity);
        }

    }
}
