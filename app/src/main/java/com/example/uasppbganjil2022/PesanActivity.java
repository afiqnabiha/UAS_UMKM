package com.example.uasppbganjil2022;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class PesanActivity extends AppCompatActivity {

    EditText alamat, konsumen;
    TextView price, ongkir, total;
    SharedPreferences sharedPreferences;
    Button btn_pesan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan);
        price = findViewById(R.id.pesan_price);
        ongkir = findViewById(R.id.pesan_ongkir);
        total = findViewById(R.id.pesan_finalprice);
        btn_pesan = findViewById(R.id.btn_pesan);
        alamat = findViewById(R.id.alamat_lengkap);
        konsumen = findViewById(R.id.nama_konsumen);
        sharedPreferences = getSharedPreferences("aplikasi-saya", MODE_PRIVATE);
        Bundle extras = getIntent().getExtras();
        btn_pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pesan();
            }
        });
        price.setText(sharedPreferences.getString("harga", "0"));
        ongkir.setText(sharedPreferences.getString("ongkir", "0"));
        String a = price.getText().toString();
        String b = ongkir.getText().toString();
        int sum = Integer.parseInt(a) + Integer.parseInt(b);
        String finalprice = String.valueOf(sum);
        total.setText(finalprice);
    }

    private void pesan() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.URI + "/pesan",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);

                            if (obj.getString("message").equals("success")) {
                                removeCart();
                                Toast.makeText(PesanActivity.this, "Pesanan ditambahkan", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(PesanActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(PesanActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PesanActivity.this, "Gagal" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("produk_id", sharedPreferences.getString("product_id",""));
                params.put("alamat", alamat.getText().toString());
                params.put("konsumen", konsumen.getText().toString());
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

    private void removeCart() {
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("product_id",null);
        editor.putString("ongkir","0");
        editor.putString("harga","0");
        editor.commit();
    }

}