package com.example.groceryshop01.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.groceryshop01.Domain.ItemsModel;
import com.example.groceryshop01.Helper.ChangeNumberItemsListener;
import com.example.groceryshop01.Helper.ManagmentCart;
import com.example.groceryshop01.R;
import com.example.groceryshop01.databinding.ViewholderCartBinding;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Viewholder> {

    private final ArrayList<ItemsModel> itemsModels;
    private Context context;
    private ChangeNumberItemsListener changeNumberItemsListener;
    private ManagmentCart managmentCart;

    public CartAdapter(ArrayList<ItemsModel> itemsModels, ManagmentCart managmentCart, ChangeNumberItemsListener changeNumberItemsListener) {
        this.itemsModels = itemsModels;
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
        ItemsModel item = itemsModels.get(position);

        holder.binding.titleTxt.setText(item.getName());
        holder.binding.feeEachItem.setText(String.format("%s Tk", item.getPrice()));
        holder.binding.totalEachItem.setText(String.format("%s Tk", Math.round(item.getQuantity() * item.getPrice())));
        holder.binding.numberItemTxt.setText(String.valueOf(item.getQuantity()));

        if (item.getImage() != null && !item.getImage().isEmpty()) {
            int drawableResource = context.getResources().getIdentifier(item.getImage(), "drawable", context.getPackageName());

            if (drawableResource != 0) {
                Glide.with(context)
                        .load(drawableResource)
                        .transform(new GranularRoundedCorners(30, 30, 0, 0))
                        .into(holder.binding.menuBtn);
                Log.d("CartAdapter", "Loaded drawable: " + item.getImage());
            } else {
                Glide.with(context)
                        .load(item.getImage())
                        .transform(new GranularRoundedCorners(30, 30, 0, 0))
                        .into(holder.binding.menuBtn);
                Log.d("CartAdapter", "Loaded URL: " + item.getImage());
            }
        } else {
            holder.binding.menuBtn.setImageResource(R.drawable.add_img);
            Log.d("CartAdapter", "Image not found, using placeholder");
        }

        holder.binding.plusCartBtn.setOnClickListener(v -> managmentCart.plusNumberItem(itemsModels, position, () -> {
            notifyItemChanged(position);
            changeNumberItemsListener.change();
        }));

        holder.binding.minusCartBtn.setOnClickListener(v -> managmentCart.minusNumberItem(itemsModels, position, () -> {
            if (itemsModels.size() >= position) {
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, itemsModels.size());
            }
            changeNumberItemsListener.change();
        }));
    }

    @Override
    public int getItemCount() {
        return itemsModels.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        ViewholderCartBinding binding;

        public Viewholder(ViewholderCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
