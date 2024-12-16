package com.example.groceryshop01.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryshop01.Domain.Order;
import com.example.groceryshop01.R;

import java.util.List;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder> {

    private List<Order> orders;
    private Context context;

    public DeliveryAdapter(List<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    @Override
    public DeliveryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_deliveryitem, parent, false);
        return new DeliveryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DeliveryViewHolder holder, int position) {
        Order order = orders.get(position);

        holder.customerNameTxt.setText(order.getCustomerName());
        holder.orderId.setText(order.getOrderId());

        String moneyStatus = order.getMoneyStatus();

        if ("received".equalsIgnoreCase(moneyStatus)) {
            holder.moneyStatus.setText("Received");
            holder.moneyStatus.setTextColor(ContextCompat.getColor(context, R.color.medium_sea_green));
            holder.statusCol.setCardBackgroundColor(ContextCompat.getColor(context, R.color.medium_sea_green));
        } else {
            holder.moneyStatus.setText("Not Received");
            holder.moneyStatus.setTextColor(ContextCompat.getColor(context, R.color.firebrick));
            holder.statusCol.setCardBackgroundColor(ContextCompat.getColor(context, R.color.firebrick));
        }
    }


    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class DeliveryViewHolder extends RecyclerView.ViewHolder {
        TextView customerNameTxt, orderId, moneyStatus;
        CardView statusCol;

        public DeliveryViewHolder(View itemView) {
            super(itemView);
            customerNameTxt = itemView.findViewById(R.id.customerNameTxt);
            orderId = itemView.findViewById(R.id.orderId);
            moneyStatus = itemView.findViewById(R.id.moneyStatus);
            statusCol = itemView.findViewById(R.id.statusCol);
        }
    }
}
