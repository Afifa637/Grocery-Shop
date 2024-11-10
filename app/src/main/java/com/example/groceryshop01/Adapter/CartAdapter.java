package com.example.groceryshop01.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.groceryshop01.Domain.BestDealsDomain;
import com.example.groceryshop01.Helper.ChangeNumberItemsListener;
import com.example.groceryshop01.Helper.ManagmentCart;
import com.example.groceryshop01.databinding.ViewholderCartBinding;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Viewholder> {

    private ArrayList<BestDealsDomain> items;
    private Context context;
    private ChangeNumberItemsListener changeNumberItemsListener;
    private ManagmentCart managmentCart;

    public CartAdapter(ArrayList<BestDealsDomain> items, ManagmentCart managmentCart, ChangeNumberItemsListener changeNumberItemsListener) {
        this.items = items;
        this.managmentCart = managmentCart;
        this.changeNumberItemsListener = changeNumberItemsListener;
    }

    @NonNull
    @Override
    public CartAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderCartBinding binding = ViewholderCartBinding.inflate(LayoutInflater.from(context), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.Viewholder holder, int position) {
        BestDealsDomain item = items.get(position);

        // Set text for item name, price, and quantity
        holder.binding.titleTxt.setText(item.getTitle());
        holder.binding.feeEachItem.setText(String.format("%s Tk", item.getPrice()));
        holder.binding.totalEachItem.setText(String.format("%s Tk", Math.round(item.getNumberInCart() * item.getPrice())));
        holder.binding.numberItemTxt.setText(String.valueOf(item.getNumberInCart()));

        // Load image with Glide
        int drawableResource = context.getResources().getIdentifier(item.getImagePath(), "drawable", context.getPackageName());
        Glide.with(context)
                .load(drawableResource)
                .transform(new GranularRoundedCorners(30, 30, 0, 0))
                .into(holder.binding.menuBtn);

        // Set up plus and minus button listeners
        holder.binding.plusCartBtn.setOnClickListener(v -> {
            managmentCart.plusNumberItem(items, position, () -> {
                notifyItemChanged(position);
                changeNumberItemsListener.change();
            });
        });

        holder.binding.minusCartBtn.setOnClickListener(v -> {
            managmentCart.minusNumberItem(items, position, () -> {
                notifyItemChanged(position);
                changeNumberItemsListener.change();
            });
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        ViewholderCartBinding binding;

        public Viewholder(ViewholderCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
