package com.example.groceryshop01.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.groceryshop01.Domain.PendingOrderModel;
import com.example.groceryshop01.R;

import java.util.ArrayList;
import java.util.List;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.ItemViewHolder> {

    private final List<PendingOrderModel.Item> itemsList;

    public OrderItemsAdapter(List<PendingOrderModel.Item> itemsList) {
        this.itemsList = new ArrayList<>(itemsList);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_row, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        PendingOrderModel.Item item = itemsList.get(position);
        holder.itemNameTxt.setText(item.getName());
        holder.itemQuantityTxt.setText(String.valueOf(item.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTxt, itemQuantityTxt;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTxt = itemView.findViewById(R.id.itemNameTxt);
            itemQuantityTxt = itemView.findViewById(R.id.itemQuantityTxt);
        }
    }
}
