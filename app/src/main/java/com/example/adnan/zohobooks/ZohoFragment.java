package com.example.adnan.zohobooks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class ZohoFragment extends Fragment {
    String LOG_TAG =  ZohoFragment.class.getSimpleName();
   // MainActivity mainActivity;
    DetailedItemAdapter selectedItemsArrayAdapter;
    ArrayList<Item> itemsForSpinner = new ArrayList<Item>();
    ListView listView;
    // AllItemAdapter allItemAdapter;
    public ZohoFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        itemsForSpinner = new ArrayList<Item>();
//        itemsForSpinner.add(new Item("123", 10, "HP"));
//        itemsForSpinner.add(new Item("124", 15, "ASUS"));
//        itemsForSpinner.add(new Item("125", 20, "HP1"));
//        items.add(new Item("126", 10, "HP2"));
//        items.add(new Item("127", 10, "ASUS2"));
//        items.add(new Item("1", 10, "HP"));
//        items.add(new Item("2", 10, "ASUS"));
//        items.add(new Item("3", 10, "HP1"));
//        items.add(new Item("4", 10, "HP2"));
//        items.add(new Item("5", 10, "ASUS2"));

        //allItemAdapter = new AllItemAdapter(getActivity(),items);
        
        selectedItemsArrayAdapter = new DetailedItemAdapter(getActivity(), new ArrayList<Item>(), itemsForSpinner/*itemsForSpinner*/);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setAdapter(selectedItemsArrayAdapter);
        Log.v(LOG_TAG, "zoho before addListenerToButtons");
        addListenerToButtons(rootView);
        Log.v(LOG_TAG, "zoho after addListenerToButtons");
        Log.v(LOG_TAG, "zoho end of onCreateView");


        return rootView;
    }

    public void addListenerToButtons(View rootView) {
        Button addITemButton = (Button) rootView.findViewById(R.id.addButton);
        addITemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(itemsForSpinner.size() > 0){
                            selectedItemsArrayAdapter.add(new Item(itemsForSpinner.get(0)));
                            selectedItemsArrayAdapter.notifyDataSetChanged();
                        }
                    }
                });


//                   Item temp = new Item();
//                   selectedItemsArrayAdapter.add(temp);
//                   selectedItemsArrayAdapter.remove(temp);
//                   Item temp = selectedItemsArrayAdapter.getItem(selectedItemsArrayAdapter.getCount() -1);
//                   temp.setName(new String(temp.getName()));
//



            }
        });

        Button submitButton = (Button) rootView.findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createInvoice();
            }
        });
    }

    private void createInvoice() {
        if(!isOnline()){
            Toast.makeText(getActivity(),"No internet connection",Toast.LENGTH_LONG).show();
            return;
        }
        new CreateInvoiceTask().execute();
    }

    void loadData(){
        if(!isOnline()){
            Toast.makeText(getActivity(),"No internet connection",Toast.LENGTH_LONG).show();
            return;
        }
        new UpdateDateTask().execute();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    public class CreateInvoiceTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String resultJsonStr = null;

            String authtoken = "7176f959137817131b4d522d84e08112";
            String organization_id = "57761035";
            String JSONStr = getSelectedItemJSON();
            Log.v("", "api JSONStr\n" + JSONStr);

            try {
                final String FORECAST_BASE_URL =
                        "https://books.zoho.com/api/v3/invoices?";
                final String AUTHTOKEN_PARAM = "authtoken";
                final String ORGNAIZATION_ID_PARAM = "organization_id";
                final String JSONSTRING = "JSONString";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(AUTHTOKEN_PARAM, authtoken)
                        .appendQueryParameter(ORGNAIZATION_ID_PARAM, organization_id)
                        .appendQueryParameter(JSONSTRING, JSONStr)
                        .build();

                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "url : \n" + url.toString());


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                resultJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }


         return resultJsonStr;

        }

        private String getSelectedItemJSON() {
            JSONObject invoice = new JSONObject();
            JSONArray items = new JSONArray();

            try {
                invoice.put("customer_id", "158550000000041156");
                int length = selectedItemsArrayAdapter.getCount();
                for(int i = 0 ; i < length;++i){
//                    View rowItem = listView.getChildAt(i);
//                    double quantity = ((NumberPicker) rowItem.findViewById(R.id.quantity)).getValue();
//                    String itemID = ((Item)((Spinner) rowItem.findViewById(R.id.spinner)).getSelectedItem()).getItemID();
                    Item rowItem = selectedItemsArrayAdapter.getItem(i);
                    double quantity = rowItem.getQuantity();
                    String itemID = rowItem.getItemID();
                    JSONObject item = new JSONObject();
                    item.put("item_id",itemID);
                    item.put("quantity",quantity);
                    items.put(item);
                }

                invoice.put("line_items",items);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return invoice.toString();
        }

        @Override
        protected void onPostExecute(String resultJsonStr) {
            Log.v("", "api resultJsonStr for create invoice ");
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(resultJsonStr);

            final String CODE = "code";
            int success = 0;
            if(jsonObject.getInt(CODE) == success)
                Toast.makeText(getActivity(), "Invoice created successfully", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getActivity(), "Failed invoice creation try again", Toast.LENGTH_LONG).show();

            loadData();
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error in parse Json", e);
                return;
            }
        }
    }

    public class UpdateDateTask extends AsyncTask<Void, Void, ArrayList<Item>>{
        @Override
        protected ArrayList<Item> doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String resultJsonStr = null;

            String authtoken = "7176f959137817131b4d522d84e08112";
            String organization_id = "57761035";

            try {
                final String FORECAST_BASE_URL =
                        "https://books.zoho.com/api/v3/items?";
                final String AUTHTOKEN_PARAM = "authtoken";
                final String ORGNAIZATION_ID_PARAM = "organization_id";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(AUTHTOKEN_PARAM, authtoken)
                        .appendQueryParameter(ORGNAIZATION_ID_PARAM, organization_id)
                        .build();

                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "api url : \n" + url.toString());


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                resultJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                Log.v("","api resultJsonStr \n" + resultJsonStr);
                return getItemsFromJson(resultJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Item> items) {
            selectedItemsArrayAdapter.clear();
            if(items.size() > 0)

                selectedItemsArrayAdapter.add(new Item(items.get(0)));
            itemsForSpinner.clear();
            itemsForSpinner.addAll(items);
            ((TextView)getActivity().findViewById(R.id.total)).setText("0");
        }

        private ArrayList<Item> getItemsFromJson(String resultJsonStr)throws JSONException{

            // These are the names of the JSON objects that need to be extracted.
            final  String ITEMS = "items";
            final String ITEM_ID = "item_id";
            final String ITEM_NAME = "item_name";
            final String RATE = "rate";

            JSONObject itemsJason = new JSONObject(resultJsonStr);
            JSONArray itemArray = itemsJason.getJSONArray(ITEMS);

            ArrayList<Item> itemsObject = new ArrayList<Item>();
            for(int i = 0 ; i < itemArray.length();++i){
                JSONObject itemJason = itemArray.getJSONObject(i);
                Item itemObject = new Item(itemJason.getString(ITEM_ID), itemJason.getDouble(RATE), itemJason.getString(ITEM_NAME).trim());
                itemsObject.add(itemObject);
            }
            return itemsObject;
        }
    }
}

