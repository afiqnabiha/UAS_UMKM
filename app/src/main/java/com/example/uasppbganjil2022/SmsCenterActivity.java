package com.example.uasppbganjil2022;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
public class SmsCenterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_center);
        getSupportActionBar().hide();
    }
    public void handleSms(View view) {
        Uri uri = Uri.parse("smsto:087729359655");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", "Halo kak saya mau pesan");
        startActivity(intent);
    }
}