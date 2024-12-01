package com.example.groceryshop01.Adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryshop01.R;
import com.example.groceryshop01.databinding.DeliveryItemBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder> {

    private final ArrayList<String> customerNames;
    private final ArrayList<String> moneyStatus;

    public DeliveryAdapter(ArrayList<String> customerNames, ArrayList<String> moneyStatus) {
        this.customerNames = customerNames != null ? customerNames : new ArrayList<>();
        this.moneyStatus = moneyStatus != null ? moneyStatus : new ArrayList<>();
    }

    @NonNull
    @Override
    public DeliveryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        DeliveryItemBinding binding = DeliveryItemBinding.inflate(inflater, parent, false);
        return new DeliveryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return customerNames.size();
    }

    public class DeliveryViewHolder extends RecyclerView.ViewHolder {
        private final DeliveryItemBinding binding;

        public DeliveryViewHolder(DeliveryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(int position) {
            HashMap<String, Integer> colorMap = new HashMap<>();
            colorMap.put("received", ContextCompat.getColor(binding.getRoot().getContext(), R.color.dark_green));
            colorMap.put("not received", ContextCompat.getColor(binding.getRoot().getContext(), R.color.dark_red));

            binding.customerName.setText(customerNames.get(position));
            binding.statusTxt.setText(moneyStatus.get(position));

            int color = colorMap.getOrDefault(moneyStatus.get(position),
                    ContextCompat.getColor(binding.getRoot().getContext(), R.color.black));
            binding.statusTxt.setTextColor(color);
            binding.statusCol.setBackgroundTintList(ColorStateList.valueOf(color));
        }
    }
}
