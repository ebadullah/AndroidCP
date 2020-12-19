package com.example.myapplication.report;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.R;
import com.example.myapplication.databinding.RecycleviewBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class RecyclerViewA extends AppCompatActivity {

    RecycleviewBinding binding ;

   RecyclerView.Adapter mAdapter;
   androidx.recyclerview.widget.RecyclerView.LayoutManager mLayoutManager;
    ArrayList<String> list;
    String sHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.recycleview);
        list=(ArrayList<String>) getIntent().getSerializableExtra("mylist");
        sHeader=getIntent().getStringExtra("header");
        binding.header.setText(sHeader);


        Collections.sort(list);


        mLayoutManager = new LinearLayoutManager(this);
        binding.recycleview.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewCustomAdapter(this, list);
        binding.recycleview.setAdapter(mAdapter);



    }




}

class  RecyclerViewCustomAdapter extends RecyclerView.Adapter {

    Context mContext;
    List<String> mList;
    public RecyclerViewCustomAdapter(Context context, List<String> list){
        mContext = context;
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycleviewdesign, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ViewHolder vh = (ViewHolder) holder;

        vh.txtrcv.setText(mList.get(position));


        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtrcv = vh.txtrcv.getText().toString();

                Intent data = new Intent();

                data.putExtra("data",txtrcv);

                ((Activity) mContext).setResult(Activity.RESULT_OK, data);
                ((Activity) mContext).finish();

            }
        });
    }



    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtrcv;

        public ViewHolder(View v) {
            super(v);
            txtrcv = (TextView) v.findViewById(R.id.txtrcv);
        }
    }


}
