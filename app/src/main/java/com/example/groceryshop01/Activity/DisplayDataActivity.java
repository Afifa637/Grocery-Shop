package com.example.groceryshop01.Activity;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.groceryshop01.Adapter.DataAdapter;
import com.example.groceryshop01.Domain.DataModel;
import com.example.groceryshop01.R;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class DisplayDataActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DataAdapter dataAdapter;
    private List<DataModel> dataList;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        recyclerView = findViewById(R.id.dataRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dataList = new ArrayList<>();
        dataAdapter = new DataAdapter(dataList);
        recyclerView.setAdapter(dataAdapter);

        requestQueue = Volley.newRequestQueue(this);

        fetchData();
    }

    private void fetchData() {
        String url = "https://api.myjson.online/v1/records/f39c83c3-8382-41e2-89bb-a0ad48973222";

        // Make a network request using Volley to fetch the JSON data
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Access the "data" array within the JSON object
                            JSONArray categories = response.getJSONArray("data");

                            // Loop through each category
                            for (int i = 0; i < categories.length(); i++) {
                                JSONObject categoryObject = categories.getJSONObject(i);
                                String categoryName = categoryObject.getString("category");
                                JSONArray details = categoryObject.getJSONArray("details");

                                // Loop through each detail in the category
                                for (int j = 0; j < details.length(); j++) {
                                    JSONObject detail = details.getJSONObject(j);
                                    String title = detail.getString("title");

                                    // Parsing market demands
                                    JSONObject marketDemands = detail.getJSONObject("marketDemands");
                                    JSONArray lowDemandItemsArray = marketDemands.getJSONArray("lowDemandItems");
                                    JSONArray highDemandItemsArray = marketDemands.getJSONArray("highDemandItems");

                                    // Convert arrays to comma-separated strings
                                    StringBuilder lowDemandItems = new StringBuilder();
                                    for (int k = 0; k < lowDemandItemsArray.length(); k++) {
                                        lowDemandItems.append(lowDemandItemsArray.getString(k));
                                        if (k < lowDemandItemsArray.length() - 1) {
                                            lowDemandItems.append(", ");
                                        }
                                    }

                                    StringBuilder highDemandItems = new StringBuilder();
                                    for (int k = 0; k < highDemandItemsArray.length(); k++) {
                                        highDemandItems.append(highDemandItemsArray.getString(k));
                                        if (k < highDemandItemsArray.length() - 1) {
                                            highDemandItems.append(", ");
                                        }
                                    }

                                    // Extracting estimated increase and formatting correctly
                                    String estimatedIncrease = detail.getString("estimatedIncrease");

                                    // Handling estimatedIncrease formatting to avoid String formatting issues
                                    try {
                                        // If estimatedIncrease is a numeric value, parse and format it
                                        float increase = Float.parseFloat(estimatedIncrease);
                                        estimatedIncrease = String.format("%.2f", increase); // Format to 2 decimal places
                                    } catch (NumberFormatException e) {
                                        // Handle case where the value isn't a valid float (if any)
                                        Log.e("NumberFormatError", "Invalid number format for estimatedIncrease", e);
                                    }

                                    // Create and add DataModel to the list
                                    dataList.add(new DataModel(categoryName, lowDemandItems.toString(), highDemandItems.toString(), estimatedIncrease));
                                }
                            }

                            // After adding data to the list, notify the adapter
                            dataAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            Log.e("JSONParsingError", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError", error.toString());
                    }
                });

        // Add the request to the Volley request queue
        requestQueue.add(jsonObjectRequest);
    }

}
