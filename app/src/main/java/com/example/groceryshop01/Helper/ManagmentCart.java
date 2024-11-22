package com.example.groceryshop01.Helper;

import android.content.Context;
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
        ArrayList<ItemsModel> listpop = getListCart();
        boolean existAlready = false;
        int n = 0;

        for (int i = 0; i < listpop.size(); i++) {
            if (listpop.get(i).getTitle().equals(item.getTitle())) {
                existAlready = true;
                n = i;
                break;
            }
        }

        if (existAlready) {
            int updatedQuantity = listpop.get(n).getNumberInCart() + item.getNumberInCart();
            listpop.get(n).setNumberInCart(updatedQuantity);
        } else {
            item.setNumberInCart(1);
            listpop.add(item);
        }

        tinyDB.putListObject("CartList", listpop);
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<ItemsModel> getListCart() {
        return tinyDB.getListObject("CartList", ItemsModel.class);
    }

    public Double getTotalFee() {
        ArrayList<ItemsModel> listItem = getListCart();
        double fee = 0;

        for (ItemsModel item : listItem) {
            fee += (item.getPrice() * item.getNumberInCart());
        }

        return fee;
    }

    public void minusNumberItem(ArrayList<ItemsModel> listItem, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        ItemsModel item = listItem.get(position);

        if (item.getNumberInCart() == 1) {
            listItem.remove(position);
        } else {
            item.setNumberInCart(item.getNumberInCart() - 1);
        }

        tinyDB.putListObject("CartList", listItem);
        changeNumberItemsListener.change();
    }

    public void plusNumberItem(ArrayList<ItemsModel> listItem, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart() + 1);
        tinyDB.putListObject("CartList", listItem);
        changeNumberItemsListener.change();
    }
}