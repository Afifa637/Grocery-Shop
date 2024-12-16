package com.example.groceryshop01.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryshop01.Domain.DataModel;
import com.example.groceryshop01.R;

import java.util.List;
import java.util.Map;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {
    private final List<DataModel> dataList;

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

        holder.tvCategory.setText(dataModel.getCategory());
        holder.tvLowDemandItems.setText("" + dataModel.getLowDemandItems());
        holder.tvHighDemandItems.setText("" + dataModel.getHighDemandItems());
        holder.tvEstimatedGrowth.setText("Estimated Growth of price: " + dataModel.getEstimatedIncrease());

        Map<String, Integer> revenue = dataModel.getRevenue();
        StringBuilder revenueText = new StringBuilder();
        for (Map.Entry<String, Integer> entry : revenue.entrySet()) {
            revenueText.append(entry.getKey()).append(": ").append(entry.getValue()).append("k\n");
        }
        holder.tvRevenue.setText(revenueText.toString().trim());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvLowDemandItems, tvHighDemandItems, tvEstimatedGrowth, tvRevenue;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCategory = itemView.findViewById(R.id.tvCategoryTitle);
            tvLowDemandItems = itemView.findViewById(R.id.tvLowDemand);
            tvHighDemandItems = itemView.findViewById(R.id.tvHighDemand);
            tvEstimatedGrowth = itemView.findViewById(R.id.tvEstimatedGrowth);
            tvRevenue = itemView.findViewById(R.id.tvRevenue);
        }
    }
}