package com.example.adnan.zohobooks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ADNAN on 8/29/2015.
 */
public class DetailedItemAdapter extends ArrayAdapter<Item> {

    ArrayAdapter<Item> allItemAdapter =null;
    String[] rates = new String[200];
    public DetailedItemAdapter(Context context, ArrayList<Item> items, ArrayList<Item> itemsForSpinner) {
        super(context, 0, items);

        allItemAdapter = new ArrayAdapter<Item>(getContext(), android.R.layout.simple_spinner_item, itemsForSpinner);
        allItemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for(Integer i = 0 ;i < rates.length; ++i)
            rates[i] = i.toString();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = getItem(position);

        if(null == convertView){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_layout,parent,false);
        }

        Spinner spinner= (Spinner) convertView.findViewById(R.id.spinner);

        spinner.setAdapter(allItemAdapter);
        spinner.setSelection(item.getPosition());

        NumberPicker quantity = (NumberPicker) convertView.findViewById(R.id.quantity);
        quantity.setValue((int)item.getQuantity());
        quantity.setMinValue(0);
        quantity.setMaxValue(1000);

        TextView rate = (TextView)convertView.findViewById(R.id.rate);
        rate.setText(String.valueOf(item.getRate()));

        spinner.setOnItemSelectedListener(item);
        quantity.setOnValueChangedListener(item);

//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Item selectedItem = getItem(position);
//                View rowItem = (View)parent.getParent();
//
//                TextView rate = (TextView)rowItem.findViewById(R.id.rate);
//                NumberPicker quantity = (NumberPicker) rowItem.findViewById(R.id.quantity);
//                TextView total = ((TextView)view.getRootView().findViewById(R.id.total));
//
//                double oldPrice = (quantity.getValue() * Double.valueOf(rate.getText().toString()));
//                double newTotal = Double.valueOf(total.getText().toString()) - oldPrice;
//
//                rate.setText(String.valueOf(selectedItem.getRate()));
//                quantity.setValue(0);
//                total.setText(String.valueOf(newTotal));
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

//        quantity.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                View rowItem = (View)picker.getParent();
//
//                TextView rate = (TextView)rowItem.findViewById(R.id.rate);
//                TextView total = ((TextView)picker.getRootView().findViewById(R.id.total));
//
//                double addedRate = ((newVal - oldVal) * Double.valueOf(rate.getText().toString()));
//                double newTotal = Double.valueOf(total.getText().toString()) + addedRate;
//
//                total.setText(String.valueOf(newTotal));
//
//            }
//        });

        return  convertView;
    }

}
