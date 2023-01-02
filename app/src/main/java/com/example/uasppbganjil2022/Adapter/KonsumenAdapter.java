package com.example.uasppbganjil2022.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.uasppbganjil2022.KelolaKonsumenActivity;
import com.example.uasppbganjil2022.Model.KonsumenModel;
import com.example.uasppbganjil2022.R;

import java.util.List;

public class KonsumenAdapter extends BaseAdapter {
    Activity activity;
    List<KonsumenModel> konsumen_item;
    private LayoutInflater inflater;

    public KonsumenAdapter(Activity activity, List<KonsumenModel> konsumen_item) {
        this.activity = activity;
        this.konsumen_item = konsumen_item;
    }


    @Override
    public int getCount() {
        return konsumen_item.size();
    }

    @Override
    public Object getItem(int i) {
        return konsumen_item.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null)
            view = inflater.inflate(R.layout.item_konsumen, null);

        TextView konsumen_id = (TextView) view.findViewById(R.id.konsumen_id);
        TextView konsumen_name = (TextView) view.findViewById(R.id.konsumen_name);

        KonsumenModel data = konsumen_item.get(i);

        konsumen_id.setText(data.getId());
        konsumen_name.setText(data.getName());

        return view;
    }
}
