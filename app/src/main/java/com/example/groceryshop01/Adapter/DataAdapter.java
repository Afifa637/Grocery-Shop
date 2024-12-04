package com.example.groceryshop01.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.groceryshop01.Domain.DataModel;
import com.example.groceryshop01.R;
import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {
    private List<DataModel> dataList;

    public DataAdapter(List<DataModel> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        DataModel dataModel = dataList.get(position);

        // Set category and demand items
        holder.tvCategory.setText("Category: " + dataModel.getCategory());
        holder.tvLowDemandItems.setText("Low Demand Items: " + dataModel.getLowDemandItems());
        holder.tvHighDemandItems.setText("High Demand Items: " + dataModel.getHighDemandItems());
        holder.tvEstimatedGrowth.setText(String.format("Estimated Increase: %.2f%%", dataModel.getEstimatedIncrease()));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvLowDemandItems, tvHighDemandItems, tvEstimatedGrowth;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvLowDemandItems = itemView.findViewById(R.id.lowDemandItems);
            tvHighDemandItems = itemView.findViewById(R.id.highDemandItems);
            tvEstimatedGrowth = itemView.findViewById(R.id.tvEstimatedGrowth);
        }
    }
}
