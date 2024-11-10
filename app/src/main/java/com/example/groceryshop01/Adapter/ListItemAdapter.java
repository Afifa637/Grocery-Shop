package com.example.groceryshop01.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.groceryshop01.Activity.DetailActivity;
import com.example.groceryshop01.Domain.ItemsDomain;
import com.example.groceryshop01.databinding.ViewholderItemBinding;

import java.util.ArrayList;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> {

    private final ArrayList<ItemsDomain> items;
    private Context context;

    public ListItemAdapter(ArrayList<ItemsDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderItemBinding binding = ViewholderItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemsDomain item = items.get(position);

        holder.binding.titleTxt.setText(item.getTitle());
        holder.binding.feeTxt.setText("Tk " + item.getPrice());
        holder.binding.scrTxt.setText(String.valueOf(item.getScore())); // Assuming getScore() exists

        Glide.with(context)
                .load(item.getPicUrl().get(0))  // Assuming the first image in the list
                .into(holder.binding.pic);  // Assuming 'pic' is the ImageView ID

        // Setting onClick to launch DetailActivity
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("object", item);  // Assuming 'ItemsDomain' is Parcelable or Serializable
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ViewholderItemBinding binding;

        public ViewHolder(ViewholderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
