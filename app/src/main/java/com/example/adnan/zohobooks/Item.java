package com.example.adnan.zohobooks;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.NumberPicker;
import android.widget.TextView;

/**
 * Created by ADNAN on 8/29/2015.
 */
public class Item implements AdapterView.OnItemSelectedListener,NumberPicker.OnValueChangeListener{
    public Item(){}
    public Item(String itemID, double rate,String name){
        this.itemID = itemID;
        this.rate = rate;
        this.name = name;

    }
    private String itemID;
    private double rate = 0;
    private double quantity = 0;
    private String name;
    private int position = 0;

    public Item(Item item) {
        itemID = item.getItemID();
        name = item.getName();
        position = getPosition();
        rate = getRate();
        quantity = getQuantity();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.v("","tot in onItemSelected");
        Item selectedItem = (Item)parent.getItemAtPosition(position);

        TextView total = ((TextView)view.getRootView().findViewById(R.id.total));

        double oldPrice = (quantity * rate);
        double newTotal = Double.valueOf(total.getText().toString());
        newTotal -= oldPrice;
        double newPrice = (quantity * selectedItem.rate);
        newTotal += newPrice;


        rate = selectedItem.getRate();
        this.position = position;

        total.setText(String.valueOf(newTotal));

        //force update list
        View rowItem = (View)parent.getParent();

        TextView rateView = (TextView)rowItem.findViewById(R.id.rate);
        rateView.setText(String.valueOf(rate));

        itemID = selectedItem.getItemID();
        name = selectedItem.getName();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        Log.v("","tot in onValueChange");
        TextView total = ((TextView)picker.getRootView().findViewById(R.id.total));

        double addedRate = ((newVal - oldVal) * rate);
        double newTotal = Double.valueOf(total.getText().toString()) + addedRate;

        quantity = newVal;
        total.setText(String.valueOf(newTotal));

    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    @Override
    public String toString() {
        return name;
    }



}
