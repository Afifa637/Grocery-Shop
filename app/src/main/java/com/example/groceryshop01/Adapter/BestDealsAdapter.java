package com.example.groceryshop01.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.groceryshop01.Activity.DetailActivity;
import com.example.groceryshop01.Domain.BestDealsDomain;
import com.example.groceryshop01.databinding.ViewholderBestlistBinding;
import java.util.ArrayList;

public class BestDealsAdapter extends RecyclerView.Adapter<BestDealsAdapter.Viewholder> {

    private ArrayList<BestDealsDomain> items;
    private Context context;
    ViewholderBestlistBinding binding;

    public BestDealsAdapter(ArrayList<BestDealsDomain> items) {

        this.items = items;
    }

    @NonNull
    @Override
    public BestDealsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ViewholderBestlistBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        context = parent.getContext();
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BestDealsAdapter.Viewholder holder, int position) {
        // Set text and price
        binding.titleTxt.setText(items.get(position).getTitle());
        binding.feeTxt.setText(items.get(position).getPrice() + " Tk");
        binding.scrTxt.setText("" + items.get(position).getScore());

        // Load image with Glide
        int drawableResource = holder.itemView.getResources().getIdentifier(
                items.get(position).getImagePath(), "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(context)
                .load(drawableResource)
                .transform(new GranularRoundedCorners(30, 30, 0, 0))
                .into(binding.pic);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("object", items.get(position));
            context.startActivity(intent);
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