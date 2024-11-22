package com.example.groceryshop01.Adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryshop01.databinding.DeliveryItemBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder> {

    private final ArrayList<String> customerNames;
    private final ArrayList<String> moneyStatus;

    public DeliveryAdapter(ArrayList<String> customerNames, ArrayList<String> moneyStatus) {
        this.customerNames = customerNames;
        this.moneyStatus = moneyStatus;
    }

    @NonNull
    @Override
    public DeliveryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DeliveryItemBinding binding = DeliveryItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DeliveryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryViewHolder holder, int position) {
        // Use the bind method for better encapsulation
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return customerNames.size(); // Ensure the correct list size
    }

    public class DeliveryViewHolder extends RecyclerView.ViewHolder {

        private final DeliveryItemBinding binding;

        public DeliveryViewHolder(DeliveryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(int position) {
            // Map for status colors
            HashMap<String, Integer> colorMap = new HashMap<>();
            colorMap.put("received", Color.GREEN);
            colorMap.put("not received", Color.RED);

            // Set data and styles
            binding.customerName.setText(customerNames.get(position));
            binding.statusTxt.setText(moneyStatus.get(position));

            int color = colorMap.getOrDefault(moneyStatus.get(position), Color.BLACK);
            binding.statusTxt.setTextColor(color);
            binding.statusCol.setBackgroundTintList(ColorStateList.valueOf(color));
        }
    }
}
