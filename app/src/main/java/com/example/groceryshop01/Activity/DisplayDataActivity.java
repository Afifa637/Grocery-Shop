package com.example.groceryshop01.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.groceryshop01.Adapter.DataAdapter;
import com.example.groceryshop01.Domain.DataModel;
import com.example.groceryshop01.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DisplayDataActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DataAdapter dataAdapter;
    private List<DataModel> dataList;
    private RequestQueue requestQueue;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        recyclerView = findViewById(R.id.dataRecyclerView);
        backBtn = findViewById(R.id.backBtn);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dataList = new ArrayList<>();
        dataAdapter = new DataAdapter(dataList);
        recyclerView.setAdapter(dataAdapter);

        requestQueue = Volley.newRequestQueue(this);

        fetchData();
        statusBarColor();

        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(DisplayDataActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void statusBarColor() {
        Window window = DisplayDataActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(DisplayDataActivity.this, R.color.dark_green));
    }

    //JSON PARSE
    private void fetchData() {
        String url = "https://api.myjson.online/v1/records/f39c83c3-8382-41e2-89bb-a0ad48973222";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray categories = response.getJSONArray("data");

                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject categoryObject = categories.getJSONObject(i);
                            String categoryName = categoryObject.getString("category");
                            JSONArray details = categoryObject.getJSONArray("details");

                            for (int j = 0; j < details.length(); j++) {
                                JSONObject detail = details.getJSONObject(j);
                                String title = detail.getString("title");

                                JSONObject marketDemands = detail.getJSONObject("marketDemands");
                                String lowDemandItems = TextUtils.join(", ", getStringArray(marketDemands.getJSONArray("lowDemandItems")));
                                String highDemandItems = TextUtils.join(", ", getStringArray(marketDemands.getJSONArray("highDemandItems")));

                                String estimatedIncrease = detail.getString("estimatedIncrease");

                                JSONObject revenueObject = detail.getJSONObject("revenue");
                                Map<String, Integer> revenue = new HashMap<>();
                                Iterator<String> keys = revenueObject.keys();
                                while (keys.hasNext()) {
                                    String store = keys.next();
                                    int revenueValue = revenueObject.getInt(store);
                                    revenue.put(store, revenueValue);
                                }

                                dataList.add(new DataModel(categoryName, lowDemandItems, highDemandItems, estimatedIncrease, revenue));
                            }
                        }

                        dataAdapter.notifyDataSetChanged();
                        Toast.makeText(DisplayDataActivity.this, "Data loaded successfully!", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(DisplayDataActivity.this, "Error parsing data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(DisplayDataActivity.this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        requestQueue.add(jsonObjectRequest);
    }

    private List<String> getStringArray(JSONArray jsonArray) throws JSONException {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getString(i));
        }
        return list;
    }
}
