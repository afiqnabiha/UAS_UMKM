package com.example.uasppbganjil2022;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
public class MapsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getSupportActionBar().hide();
    }
    public void alamat(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.co.id/maps/place/Jl.+Citarum+Sel.+II+No.1,+Bugangan,+Kec.+Semarang+Tim.,+Kota+Semarang,+Jawa+Tengah+50126/@-6.9725095,110.4302365,15z/data=!3m1!4b1!4m5!3m4!1s0x2e70f34d73a9d779:0x66c9da7b170810f5!8m2!3d-6.9725309!4d110.4389698"));
        startActivity(intent);
    }
}