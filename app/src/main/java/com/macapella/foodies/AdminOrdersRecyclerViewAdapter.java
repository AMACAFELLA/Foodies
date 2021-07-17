package com.macapella.foodies;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminOrdersRecyclerViewAdapter extends RecyclerView.Adapter<AdminOrdersRecyclerViewAdapter.viewHolder> {

    private final Context mContext;
    private final List<OrderModel> mData;


    public AdminOrdersRecyclerViewAdapter(Context mContext, List<OrderModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.admin_orders_item, parent, false);

        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminOrdersRecyclerViewAdapter.viewHolder holder, int position) {

        holder.name.setText(mData.get(position).getName());
        holder.orderNumber.setText(mData.get(position).getOrderNumber());
        holder.status.setText(mData.get(position).getStatus());
        String total = "E " + mData.get(position).getTotalToPay() + "0";
        holder.total.setText(total);

        holder.viewDetails.setOnClickListener(
                (view) -> {
                    Intent intent = new Intent(holder.context, AdminOrderDetailsActivity.class);
                    intent.putExtra("orderNumber", mData.get(position).getOrderNumber());
                    holder.context.startActivity(intent);
                }
        );

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView orderNumber;
        TextView status;
        TextView total;
        Button viewDetails;
        private final Context context;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.adminOrderName);
            orderNumber = itemView.findViewById(R.id.adminOrderNumber);
            status = itemView.findViewById(R.id.adminOrderStatus);
            total = itemView.findViewById(R.id.adminOrderTotal);
            viewDetails = itemView.findViewById(R.id.adminViewOrderDetails);
            context = itemView.getContext();
        }

    }
}
