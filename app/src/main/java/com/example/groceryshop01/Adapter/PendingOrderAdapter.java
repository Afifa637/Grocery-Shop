package com.example.groceryshop01.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryshop01.Domain.PendingOrderModel;
import com.example.groceryshop01.R;

import java.util.List;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.OrderViewHolder> {

    private final List<PendingOrderModel> pendingOrders;
    private final Context context;
    private OnOrderActionListener actionListener;

    public interface OnOrderActionListener {
        void onAcceptOrder(PendingOrderModel order, int position);
    }

    public PendingOrderAdapter(List<PendingOrderModel> pendingOrders, Context context , OnOrderActionListener actionListener) {
        this.pendingOrders = pendingOrders;
        this.context = context;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.viewholder_pendingitem, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        PendingOrderModel order = pendingOrders.get(position);
        holder.customerNameTxt.setText(order.getCustomerName());

        holder.acceptBtn.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onAcceptOrder(order, position);
            }
        });
        // Set up the RecyclerView for items
        OrderItemsAdapter orderItemsAdapter = new OrderItemsAdapter(order.getItemsList());
        holder.itemsRecyclerView.setAdapter(orderItemsAdapter);
        holder.itemsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public int getItemCount() {
        return pendingOrders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView customerNameTxt;
        RecyclerView itemsRecyclerView;
        AppCompatButton acceptBtn;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            customerNameTxt = itemView.findViewById(R.id.customerNameTxt);
            itemsRecyclerView = itemView.findViewById(R.id.itemsRecyclerView);
            acceptBtn = itemView.findViewById(R.id.acceptBtn);
        }
    }
}

