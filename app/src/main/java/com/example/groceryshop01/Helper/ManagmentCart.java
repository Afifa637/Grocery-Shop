package com.example.groceryshop01.Helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.groceryshop01.Domain.ItemsModel;

import java.util.ArrayList;

public class ManagmentCart {
    private Context context;
    private TinyDB tinyDB;

    public ManagmentCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    public void insertFood(ItemsModel item) {
        if (item == null || item.getName() == null) {
            Toast.makeText(context, "Item information is incomplete!", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<ItemsModel> listCart = getListCart();
        boolean alreadyExists = false;
        int existingIndex = -1;

        for (int i = 0; i < listCart.size(); i++) {
            if (listCart.get(i).getName() != null && listCart.get(i).getName().equals(item.getName())) {
                alreadyExists = true;
                existingIndex = i;
                break;
            }
        }

        if (alreadyExists) {
            listCart.get(existingIndex).setQuantity(listCart.get(existingIndex).getQuantity() + 1);
        } else {
            item.setQuantity(1);
            listCart.add(item);
        }

        tinyDB.putListObject("CartList", listCart);
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
    }


    public ArrayList<ItemsModel> getListCart() {
        ArrayList<ItemsModel> list = tinyDB.getListObject("CartList", ItemsModel.class);
        if (list == null || list.isEmpty()) {
            list = new ArrayList<>();
        }
        return list;
    }


    public Double getTotalFee() {
        ArrayList<ItemsModel> listItem = getListCart();
        double fee = 0;

        for (ItemsModel item : listItem) {
            fee += (item.getPrice() * item.getQuantity());
        }

        return fee;
    }

    public void minusNumberItem(ArrayList<ItemsModel> listItem, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        if (position < 0 || position >= listItem.size()) {
            Log.e("ManagmentCart", "Invalid position: " + position);
            return;
        }

        ItemsModel item = listItem.get(position);

        if (item.getQuantity() <= 1) {
            listItem.remove(position);
            Toast.makeText(context, "Item removed from the cart", Toast.LENGTH_SHORT).show();
        } else {
            item.setQuantity(item.getQuantity() - 1);
        }

        tinyDB.putListObject("CartList", listItem);

        // Notify the listener (RecyclerView will also be updated)
        if (changeNumberItemsListener != null) {
            changeNumberItemsListener.change();
        }
    }



    public void plusNumberItem(ArrayList<ItemsModel> listItem, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        listItem.get(position).setQuantity(listItem.get(position).getQuantity() + 1);
        tinyDB.putListObject("CartList", listItem);
        changeNumberItemsListener.change();
    }
}