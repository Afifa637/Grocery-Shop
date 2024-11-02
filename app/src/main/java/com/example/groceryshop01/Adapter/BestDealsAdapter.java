package com.example.groceryshop01.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.groceryshop01.Domain.BestDealsDomain;
import com.example.groceryshop01.databinding.ViewholderBestlistBinding;

import java.util.ArrayList;

public class BestDealsAdapter extends RecyclerView.Adapter<BestDealsAdapter.Viewholder> {

    private ArrayList<BestDealsDomain> items;
    private Context context;

    public BestDealsAdapter(Context context, ArrayList<BestDealsDomain> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public BestDealsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderBestlistBinding binding = ViewholderBestlistBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BestDealsAdapter.Viewholder holder, int position) {
        BestDealsDomain currentItem = items.get(position);

        // Set text and price
        holder.binding.titleTxt.setText(currentItem.getTitle());
        holder.binding.feeTxt.setText("Tk." + currentItem.getPrice());

        // Load image with Glide
        int drawableResource = context.getResources().getIdentifier(currentItem.getImagePath(),
                "drawable", context.getPackageName());
        Glide.with(context)
                .load(drawableResource)
                .transform(new GranularRoundedCorners(30, 30, 0, 0))
                .into(holder.binding.pic);

        // Set onClickListener for item
        holder.itemView.setOnClickListener(v -> {
            // Implement intent or action here
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        ViewholderBestlistBinding binding;

        public Viewholder(ViewholderBestlistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
