package com.macapella.foodies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.viewHolder> {

    private final Context mContext;
    private final List<ItemModel> mData;

    public CartRecyclerViewAdapter(Context mContext, List<ItemModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.cart_item, parent, false);

        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartRecyclerViewAdapter.viewHolder holder, int position) {

        holder.name.setText(mData.get(position).getName());
        Integer pricePerItem = mData.get(position).getPrice();
        Integer quantityOfItem = mData.get(position).getQuantity();
        int priceNumber = pricePerItem * quantityOfItem;
        String price = Integer.toString(priceNumber);
        holder.price.setText(price);
        holder.quantity.setText(mData.get(position).getQuantity().toString());

        holder.reduceQuantity.setOnClickListener(
                (view) -> {
                    TextView quantityView = holder.quantity;
                    TextView priceView = holder.price;
                    int priceData = mData.get(position).getPrice();
                    int quantity = Integer.parseInt(quantityView.getText().toString());
                    if (quantity > 0) {
                        quantity -= 1;
                        int newPriceNumber = quantity * priceData;
                        String newPrice = Integer.toString(newPriceNumber);
                        priceView.setText(newPrice);
                        quantityView.setText(Integer.toString(quantity));
                        CartActivity cartActivity = new CartActivity();
                        cartActivity.changeQuantity(position, quantity);
                        if (quantity == 0) {
                            cartActivity.removeItem(position);

                            mData.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, mData.size());
                        }
                    }
                }
        );

        holder.increaseQuantity.setOnClickListener(
                (view) -> {
                    TextView quantityView = holder.quantity;
                    TextView priceView = holder.price;
                    int priceData = mData.get(position).getPrice();
                    int quantity = Integer.parseInt(quantityView.getText().toString());
                    quantity += 1;
                    int newPriceNumber = quantity * priceData;
                    String newPrice = Integer.toString(newPriceNumber);
                    priceView.setText(newPrice);
                    quantityView.setText(Integer.toString(quantity));
                    CartActivity cartActivity = new CartActivity();
                    cartActivity.changeQuantity(position, quantity);

                }
        );

        holder.removeFromCart.setOnClickListener(
                (view) -> {

                    CartActivity cartActivity = new CartActivity();
                    cartActivity.removeItem(position);

                    mData.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mData.size());
                }
        );

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView price;
        TextView quantity;
        Button reduceQuantity;
        Button increaseQuantity;
        Button removeFromCart;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            reduceQuantity = itemView.findViewById(R.id.cartReduceQuantity);
            increaseQuantity = itemView.findViewById(R.id.cartIncreaseQuantity);
            removeFromCart = itemView.findViewById(R.id.cartRemoveItem);

            name = itemView.findViewById(R.id.confirmOrderItemName);
            price = itemView.findViewById(R.id.orderItemTotalPrice);
            quantity = itemView.findViewById(R.id.orderItemQuantity);
        }

    }
}
