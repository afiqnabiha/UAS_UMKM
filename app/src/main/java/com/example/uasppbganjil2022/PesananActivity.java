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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.uasppbganjil2022.Adapter.PesananAdapter;
import com.example.uasppbganjil2022.Model.PesananModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PesananActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    SharedPreferences sharedPreferences;
    ListView list;
    SwipeRefreshLayout swipe;
    List<PesananModel> pesananlist = new ArrayList<PesananModel>();
    PesananAdapter pesananAdapter;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan);
        swipe = findViewById(R.id.swipe_pesanan);
        list = findViewById(R.id.pesanan_listview);
        sharedPreferences = getSharedPreferences("aplikasi-saya", MODE_PRIVATE);

        pesananAdapter = new PesananAdapter(PesananActivity.this, pesananlist);
        list.setAdapter(pesananAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id =pesananlist.get(i).getId();
                String total =pesananlist.get(i).getHarga();
                String bukti =pesananlist.get(i).getBukti();
                String alamat =pesananlist.get(i).getAlamat();
                String ongkir =pesananlist.get(i).getOngkir();
                String produk =pesananlist.get(i).getProduk();

                Intent intent = new Intent(PesananActivity.this, DetailPesananActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("total",total);
                intent.putExtra("bukti",bukti);
                intent.putExtra("alamat",alamat);
                intent.putExtra("ongkir",ongkir);
                intent.putExtra("produk",produk);
                startActivity(intent);
            }
        });
        swipe.setOnRefreshListener(this);
        swipe.post(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(true);
                pesananlist.clear();
                pesananAdapter.notifyDataSetChanged();
                callVolley();
            }
        });
    }
    @Override
    public void onRefresh() {
        pesananlist.clear();
        pesananAdapter.notifyDataSetChanged();
        callVolley();
    }
    public void callVolley() {
        pesananlist.clear();
        pesananAdapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

//        Make JSON Request
        JsonArrayRequest jArr = new JsonArrayRequest(Url.URI + "/pesanan", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
//                Parse Json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        PesananModel item = new PesananModel();

                        item.setId(obj.getString("id"));
                        item.setHarga(obj.getString("harga"));
                        item.setBukti(obj.getString("bukti"));
                        item.setAlamat(obj.getString("alamat"));
                        item.setOngkir(obj.getString("ongkir"));
                        item.setProduk(obj.getString("produk"));

                        pesananlist.add(item);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                pesananAdapter.notifyDataSetChanged();
                swipe.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PesananActivity.this, "Error! " + error.getMessage(), Toast.LENGTH_SHORT).show();
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