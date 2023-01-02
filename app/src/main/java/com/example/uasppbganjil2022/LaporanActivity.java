package com.example.uasppbganjil2022;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LaporanActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    SharedPreferences sharedPreferences;
    ListView list;
    SwipeRefreshLayout swipe;
    List<PesananModel> pesananlist = new ArrayList<PesananModel>();
    PesananAdapter pesananAdapter;
    LayoutInflater inflater;
    Button button1, button2, button3;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);
        swipe = findViewById(R.id.swipe_laporan);
        list = findViewById(R.id.list_laporan);
        button1 = findViewById(R.id.button_thismonth);
        button2 = findViewById(R.id.button_thisweek);
        button3 = findViewById(R.id.button_cetak);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callVolley("month");
            }
        });

        button2.setOnClickListener(view -> {
            callVolley("week");
        });

        button3.setOnClickListener(view -> {
            bitmap = LoadBitmap(list, list.getWidth(), list.getHeight());
            createPdf();
        });

        sharedPreferences = getSharedPreferences("aplikasi-saya", MODE_PRIVATE);
        pesananAdapter = new PesananAdapter(LaporanActivity.this, pesananlist);
        list.setAdapter(pesananAdapter);

        swipe.setOnRefreshListener(this);
        swipe.post(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(true);
                pesananlist.clear();
                pesananAdapter.notifyDataSetChanged();
                callVolley("");
            }
        });
    }


    private Bitmap LoadBitmap(ListView list, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        list.draw(canvas);
        return bitmap;
    }

    private void createPdf() {
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float width = displayMetrics.widthPixels;
        float height = displayMetrics.heightPixels;
        int convertwidth=(int)width, convertedheight=(int)height;

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertwidth, convertedheight, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);
        bitmap=Bitmap.createScaledBitmap(bitmap,convertwidth, convertedheight,true);
        canvas.drawBitmap(bitmap,0,0,null);
        document.finishPage(page);

        String targetPdf = "/sdcard/page.pdf";
        File file;
        file = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(file));
        } catch (IOException e){
            e.printStackTrace();

            document.close();
            Toast.makeText(LaporanActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRefresh() {
        pesananlist.clear();
        pesananAdapter.notifyDataSetChanged();
        callVolley("");
    }
    private void callVolley(String period) {
        if (period.equals("")){
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
                    Toast.makeText(LaporanActivity.this, "Error! " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
        } else if(period.equals("month")){
            pesananlist.clear();
            pesananAdapter.notifyDataSetChanged();
            swipe.setRefreshing(true);

//        Make JSON Request
            JsonArrayRequest jArr = new JsonArrayRequest(Url.URI + "/pesanan/"+period, new Response.Listener<JSONArray>() {
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
                    Toast.makeText(LaporanActivity.this, "Error! " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
        } else if(period.equals("week")){
            pesananlist.clear();
            pesananAdapter.notifyDataSetChanged();
            swipe.setRefreshing(true);

//        Make JSON Request
            JsonArrayRequest jArr = new JsonArrayRequest(Url.URI + "/pesanan/"+period, new Response.Listener<JSONArray>() {
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
                    Toast.makeText(LaporanActivity.this, "Error! " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
}