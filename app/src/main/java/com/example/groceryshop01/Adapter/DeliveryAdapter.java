package com.example.groceryshop01.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryshop01.Activity.OrderDispatchActivity;
import com.example.groceryshop01.R;

import java.util.ArrayList;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder> {

    private ArrayList<String> customerNames;
    private ArrayList<String> moneyStatus;
    private ArrayList<String> orderIds;

    public DeliveryAdapter(ArrayList<String> customerNames, ArrayList<String> moneyStatus, ArrayList<String> orderIds) {
        this.customerNames = customerNames;
        this.moneyStatus = moneyStatus;
        this.orderIds = orderIds;
    }

    @NonNull
    @Override
    public DeliveryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_item, parent, false);
        return new DeliveryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryViewHolder holder, int position) {
        holder.customerNameText.setText(customerNames.get(position));
        holder.moneyStatusText.setText(moneyStatus.get(position));

        // If the order is accepted, change the button's text and color
        if ("accepted".equals(moneyStatus.get(position))) {
            holder.acceptButton.setText("Accepted");
            holder.acceptButton.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.grey));  // Adjust color as needed
        } else {
            holder.acceptButton.setText("Accept");
            holder.acceptButton.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.medium_sea_green));  // Adjust color as needed
        }

        // Set the button click listener
        holder.acceptButton.setOnClickListener(v -> {
            // Update status to accepted and change the UI
            ((OrderDispatchActivity) holder.itemView.getContext()).acceptOrder(orderIds.get(position), holder.acceptButton);
        });
    }

    @Override
    public int getItemCount() {
        return customerNames.size();
    }

    public static class DeliveryViewHolder extends RecyclerView.ViewHolder {
        TextView customerNameText;
        TextView moneyStatusText;
        Button acceptButton;

        public DeliveryViewHolder(View itemView) {
            super(itemView);
            customerNameText = itemView.findViewById(R.id.customerNameTxt);
            moneyStatusText = itemView.findViewById(R.id.statusTxt);
            acceptButton = itemView.findViewById(R.id.acceptBtn);
        }
    }
}
