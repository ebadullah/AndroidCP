package com.mobilisepakistan.pdma.report;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobilisepakistan.pdma.R;
import com.mobilisepakistan.pdma.databinding.RecycleviewBinding;
import com.mobilisepakistan.pdma.global.emrConacts;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewEC extends AppCompatActivity {

    RecycleviewBinding binding ;
    RecyclerViewCustomAdapterEC mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<String> list;
    ArrayList<String> listheader;
    ArrayList<String> listconacts;
    String sHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.recycleview);
        //list=(ArrayList<String>) getIntent().getSerializableExtra("mylist");
        list= emrConacts.getDistricts();
        listconacts=emrConacts.getcontacts();
        listheader=emrConacts.getheaders();
        sHeader=getIntent().getStringExtra("header");
        binding.header.setText(sHeader);

        binding.lvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) RecyclerViewEC.this).finish();
            }
        });


     //   Collections.sort(list);


        mLayoutManager = new LinearLayoutManager(this);
        binding.recycleview.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewCustomAdapterEC(this, list,listconacts,listheader);
        binding.recycleview.setAdapter(mAdapter);



        // RecycleView Text Search View Filter

        ((EditText) findViewById(R.id.ed_search)).addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });


    }

    // filter method

    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<String> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (String s : list) {
            //if the existing elements contains the search input
            if (s.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        mAdapter.filterList(filterdNames);
    }




}

class  RecyclerViewCustomAdapterEC extends RecyclerView.Adapter {

    Context mContext;
    List<String> mList;
    List<String> cList;
    List<String> hList;
    public RecyclerViewCustomAdapterEC(Context context, List<String> list,List<String> contactlist,List<String> headerlist){
        mContext = context;
        mList = list;
        cList = contactlist;
        hList = headerlist;
    }

    // filter

    public void filterList(ArrayList<String> filterdNames) {
        this.mList = filterdNames;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycleviewdesignec, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    Intent data = new Intent();
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ViewHolder vh = (ViewHolder) holder;

        vh.txtrcv.setText(mList.get(position));
        vh.txtconcat.setText(cList.get(position));
        vh.txtheader.setText(hList.get(position));


        vh.lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtrcv = vh.txtconcat.getText().toString();


                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+txtrcv));
            //    callIntent.setData(Uri.parse(txtrcv));
                mContext.startActivity(intent);



            }
        });
    }



    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtrcv;
        public TextView txtconcat;
        public TextView txtheader;
        public ImageButton lv;

        public ViewHolder(View v) {
            super(v);
            txtrcv = (TextView) v.findViewById(R.id.txtrcv);
            txtconcat = (TextView) v.findViewById(R.id.txtrcvconact);
            txtheader = (TextView) v.findViewById(R.id.txthdr);
            lv = (ImageButton) v.findViewById(R.id.img);
        }
    }








}