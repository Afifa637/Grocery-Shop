package com.example.groceryshop01.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.groceryshop01.Activity.AllItemActivity;
import com.example.groceryshop01.Activity.DetailActivity;
import com.example.groceryshop01.Domain.ItemsModel;
import com.example.groceryshop01.R;
import com.example.groceryshop01.databinding.ViewholderItemBinding;
import java.util.ArrayList;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> {

    private final ArrayList<ItemsModel> items;
    private final Context context;

    public ListItemAdapter(ArrayList<ItemsModel> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Ensure the binding references the correct XML file
        ViewholderItemBinding binding = ViewholderItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemsModel item = items.get(position);

        if (item != null && item.getName() != null) {
            holder.binding.titleTxt.setText(item.getName());
        } else {
            holder.binding.titleTxt.setText("Unknown Item");
        }
        holder.binding.feeTxt.setText("Tk " + item.getPrice());
        
        holder.binding.scrTxt.setText(String.valueOf(item.getScore()));
        holder.binding.numberItemTxt.setText((String.valueOf((item.getQuantity()))));

        // Load item image using Glide
        String imageUrl = item.getImage();
        Glide.with(context)
                .load((imageUrl != null && !imageUrl.isEmpty()) ? imageUrl : R.drawable.add_img)
                .placeholder(R.drawable.add_img)
                .error(R.drawable.add_img)
                .into(holder.binding.pic);

        // Set item click listener
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("itemsModel", (Parcelable) item);
            context.startActivity(intent);
        });

        // Handle edit/delete button actions
        holder.binding.editDeltBtn.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, view);
            popupMenu.getMenuInflater().inflate(R.menu.edit_delete_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if (context instanceof AllItemActivity) {
                    AllItemActivity activity = (AllItemActivity) context;
                    if (menuItem.getItemId() == R.id.menu_edit) {
                        activity.showEditDialog(item);
                        return true;
                    } else if (menuItem.getItemId() == R.id.menu_delete) {
                        activity.deleteItem(item);
                        return true;
                    }
                }
                return false;
            });

            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewholderItemBinding binding;

        public ViewHolder(ViewholderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
