package com.example.groceryshop01.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.groceryshop01.Activity.DetailActivity;
import com.example.groceryshop01.databinding.ViewholderItemBinding;

import java.util.ArrayList;
import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> {

    private final List<com.example.groceryshop01.Adapter.ItemsModel> items;
    private Context context;

    public ListItemAdapter(List<com.example.groceryshop01.Adapter.ItemsModel> items) {
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
        com.example.groceryshop01.Adapter.ItemsModel item = items.get(position);

        holder.binding.titleTxt.setText(item.getTitle());
        holder.binding.feeTxt.setText("Tk " + item.getPrice());
        holder.binding.scrTxt.setText(String.valueOf(item.getScore()));

        Glide.with(context)
                .load(item.getPicUrl().get(0)) // Assuming `getPicUrl` returns a list of image URLs
                .into(holder.binding.pic);

        // Open DetailActivity on item click
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("object", item);  // Make sure `ItemsModel` implements Serializable or Parcelable
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
