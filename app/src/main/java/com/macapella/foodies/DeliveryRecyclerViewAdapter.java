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

public class DeliveryRecyclerViewAdapter extends RecyclerView.Adapter<DeliveryRecyclerViewAdapter.viewHolder> {

    private final Context mContext;
    private final List<OrderModel> mData;

    public DeliveryRecyclerViewAdapter(Context mContext, List<OrderModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.delivery_item, parent, false);

        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryRecyclerViewAdapter.viewHolder holder, int position) {

        holder.name.setText(mData.get(position).getName());
        holder.phone.setText(mData.get(position).getPhone());
        holder.status.setText(mData.get(position).getStatus());
        holder.number.setText(mData.get(position).getOrderNumber());
        String address = mData.get(position).getLatitude() + ", " + mData.get(position).getLongitude();
        holder.address.setText(address);

        DeliveryActivity deliveryActivity = new DeliveryActivity();


        holder.startDelivery.setOnClickListener(
                (view) -> {
                    deliveryActivity.startDelivery(mData.get(position).getOrderNumber());
                    holder.status.setText("Sent out for delivery");

                }
        );

        holder.confirmDelivery.setOnClickListener(
                (view) -> {
                    deliveryActivity.confirmDelivery(mData.get(position).getOrderNumber());

                    mData.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mData.size());
                }
        );

        holder.unableToDeliver.setOnClickListener(
                (view) -> {
                    deliveryActivity.unableToDeliver(mData.get(position).getOrderNumber());
                    holder.status.setText("Delivery attempt made: Unable to deliver");
                }
        );

        holder.goToAddress.setOnClickListener(
                (view) -> {
                    deliveryActivity.goToAddress(mData.get(position).getLatitude(), mData.get(position).getLongitude(), mContext);
                }
        );

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView number;
        TextView status;
        TextView phone;
        TextView address;
        Button startDelivery;
        Button confirmDelivery;
        Button unableToDeliver;
        Button goToAddress;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            startDelivery = itemView.findViewById(R.id.deliveryStartDelivery);
            confirmDelivery = itemView.findViewById(R.id.deliveryConfirmDelivery);
            unableToDeliver = itemView.findViewById(R.id.deliveryUnable);
            goToAddress = itemView.findViewById(R.id.deliveryGoTo);

            name = itemView.findViewById(R.id.deliveryName);
            number = itemView.findViewById(R.id.deliveryNumber);
            status = itemView.findViewById(R.id.deliveryStatus);
            phone = itemView.findViewById(R.id.deliveryPhone);
            address = itemView.findViewById(R.id.deliveryAddress);

        }

    }
}
