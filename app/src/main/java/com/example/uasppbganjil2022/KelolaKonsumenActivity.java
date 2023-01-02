package com.example.uasppbganjil2022;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.uasppbganjil2022.Adapter.KonsumenAdapter;
import com.example.uasppbganjil2022.Model.KonsumenModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KelolaKonsumenActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    SharedPreferences sharedPreferences;
    ListView list;
    SwipeRefreshLayout swipe;
    List<KonsumenModel> konsumenlist = new ArrayList<KonsumenModel>();
    KonsumenAdapter konsumenAdapter;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelola_konsumen);
        swipe = findViewById(R.id.swipe_kelolakonsumen);
        list = findViewById(R.id.admin_list_konsumen);
        sharedPreferences = getSharedPreferences("aplikasi-saya", MODE_PRIVATE);
        konsumenAdapter = new KonsumenAdapter(KelolaKonsumenActivity.this, konsumenlist);
        list.setAdapter(konsumenAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name =  konsumenlist.get(i).getName();
                String id = konsumenlist.get(i).getId();
                Intent intent = new Intent(KelolaKonsumenActivity.this, UpdateKonsumenActivity.class);
                intent.putExtra("nama",name);
                intent.putExtra("konsumenid",id);
                startActivity(intent);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                deleteKonsumen(konsumenlist.get(i).getId());
                return false;
            }
        });
        swipe.setOnRefreshListener(this);
        swipe.post(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(true);
                konsumenlist.clear();
                konsumenAdapter.notifyDataSetChanged();
                callVolley();
            }
        });
    }

    @Override
    public void onRefresh() {
        konsumenlist.clear();
        konsumenAdapter.notifyDataSetChanged();
        callVolley();
    }

    public void deleteKonsumen(String id){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.URI + "/konsumen-delete",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);

                            if (obj.getString("message").equals("success")) {
                                Toast.makeText(KelolaKonsumenActivity.this, "Konsumen dihapus", Toast.LENGTH_SHORT).show();
                                callVolley();
                            } else {
                                Toast.makeText(KelolaKonsumenActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(KelolaKonsumenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(KelolaKonsumenActivity.this, "Gagal" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("konsumenid", id);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + sharedPreferences.getString("access_token", null));
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void callVolley() {
        konsumenlist.clear();
        konsumenAdapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

//        Make JSON Request
        JsonArrayRequest jArr = new JsonArrayRequest(Url.URI + "/konsumen", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
//                Parse Json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        KonsumenModel item = new KonsumenModel();

                        item.setId(obj.getString("id"));
                        item.setName(obj.getString("name"));

                        konsumenlist.add(item);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                konsumenAdapter.notifyDataSetChanged();
                swipe.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(KelolaKonsumenActivity.this, "Error! " + error.getMessage(), Toast.LENGTH_SHORT).show();
                swipe.setRefreshing(false);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer "+sharedPreferences.getString("access_token",""));
                return headers;
            }
        };

        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mRequestQueue.add(jArr);
    }
}