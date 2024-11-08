package com.example.groceryshop01.Helper;

import android.content.Context;
import android.widget.Toast;

import com.example.groceryshop01.Domain.BestDealsDomain;
import java.util.ArrayList;

public class ManagmentCart {
    private Context context;
    private TinyDB tinyDB;

    public ManagmentCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    public void insertFood(BestDealsDomain item) {
        ArrayList<BestDealsDomain> listpop = getListCart();
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
            listpop.add(item);
        }

        tinyDB.putListObject("CartList", listpop);
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<BestDealsDomain> getListCart() {
        return tinyDB.getListObject("CartList", BestDealsDomain.class);
    }

    public Double getTotalFee() {
        ArrayList<BestDealsDomain> listItem = getListCart();
        double fee = 0;

        for (BestDealsDomain item : listItem) {
            fee += (item.getPrice() * item.getNumberInCart());
        }

        return fee;
    }

    public void minusNumberItem(ArrayList<BestDealsDomain> listItem, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        BestDealsDomain item = listItem.get(position);

        if (item.getNumberInCart() == 1) {
            listItem.remove(position);
        } else {
            item.setNumberInCart(item.getNumberInCart() - 1);
        }

        tinyDB.putListObject("CartList", listItem);
        changeNumberItemsListener.change();
    }

    public void plusNumberItem(ArrayList<BestDealsDomain> listItem, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart() + 1);
        tinyDB.putListObject("CartList", listItem);
        changeNumberItemsListener.change();
    }
}
