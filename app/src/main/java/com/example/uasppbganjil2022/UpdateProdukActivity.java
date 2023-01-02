package com.example.uasppbganjil2022;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateProdukActivity extends AppCompatActivity {
    EditText nama, deskripsi, gambar, harga, ongkir;
    Button button1;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_produk);
        Bundle extras = getIntent().getExtras();
        nama = findViewById(R.id.admin_update_nama_produk);
        deskripsi = findViewById(R.id.admin_update_deskripsi_produk);
        gambar = findViewById(R.id.admin_update_gambar_produk);
        harga = findViewById(R.id.admin_update_harga_produk);
        ongkir = findViewById(R.id.admin_update_ongkir_produk);
        sharedPreferences = getSharedPreferences("aplikasi-saya", MODE_PRIVATE);

            nama.setText(extras.getString("name"));
            deskripsi.setText(extras.getString("description"));
            ongkir.setText(extras.getString("ongkir"));
            harga.setText(extras.getString("price"));
            gambar.setText(extras.getString("image"));
            String produkid = extras.getString("productid");

        button1 = findViewById(R.id.admin_produk_btn);

        button1.setOnClickListener(view -> {
            postData(produkid);
        });
    }

    private void postData(String produkid) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.URI + "/produk-modif",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);

                            if (obj.getString("message").equals("success")) {
                                Toast.makeText(UpdateProdukActivity.this, "Produk diubah", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(UpdateProdukActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(UpdateProdukActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateProdukActivity.this, "Gagal" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("nama", nama.getText().toString());
                params.put("deskripsi", deskripsi.getText().toString());
                params.put("ongkir", ongkir.getText().toString());
                params.put("harga", harga.getText().toString());
                params.put("gambar", gambar.getText().toString());
                params.put("produkid", produkid);

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
}