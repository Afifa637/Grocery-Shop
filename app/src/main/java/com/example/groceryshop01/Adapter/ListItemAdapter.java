package com.example.groceryshop01.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.groceryshop01.Activity.DetailActivity;
import com.example.groceryshop01.Domain.ItemsModel;
import com.example.groceryshop01.databinding.ViewholderItemBinding;

import java.util.ArrayList;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> {

    private final ArrayList<ItemsModel> items;
    private Context context;
    ViewholderItemBinding binding;

    public ListItemAdapter(ArrayList<ItemsModel> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        binding = ViewholderItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemsModel item = items.get(position);

        holder.binding.titleTxt.setText(item.getTitle());
        holder.binding.feeTxt.setText("Tk " + item.getPrice());
        holder.binding.scrTxt.setText(String.valueOf(item.getScore()));

        int drawableResource = holder.itemView.getResources().getIdentifier(
                items.get(position).getPicUrl(), "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(context)
                .load(item.getPicUrl()) // Assuming `getPicUrl` returns a list of image URLs
                .into(holder.binding.pic);

        // Open DetailActivity on item click
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("itemsModel", item);  // Make sure `ItemsModel` implements Serializable or Parcelable
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