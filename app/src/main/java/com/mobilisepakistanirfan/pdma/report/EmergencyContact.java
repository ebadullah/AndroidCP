package com.mobilisepakistanirfan.pdma.report;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobilisepakistanirfan.pdma.R;
import com.mobilisepakistanirfan.pdma.databinding.RecycleviewBinding;
import com.mobilisepakistanirfan.pdma.global.JsonArray;
import com.mobilisepakistanirfan.pdma.global.ServerConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class EmergencyContact extends AppCompatActivity {

    RecycleviewBinding binding ;
    EmergencyContactCustomAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    String sHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.recycleview);







        sHeader=getString(R.string.s_erc);
        binding.header.setText(sHeader);
        binding.edSearch.setVisibility(View.GONE);
        new GetDataServerEmrContact(EmergencyContact.this, ServerConfiguration.ServerURL+ "GetEmergencyContact",binding.recycleview).execute();





        binding.lvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) EmergencyContact.this).finish();
            }
        });




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
//        for (String s : arrayListall) {
//            //if the existing elements contains the search input
//            if (s.toLowerCase().contains(text.toLowerCase())) {
//                //adding the element to filtered list
//                filterdNames.add(s);
//            }
     //   }

        //calling a method of the adapter class and passing the filtered list
   //     mAdapter.filterList(filterdNames);
    }




}

class  EmergencyContactCustomAdapter extends RecyclerView.Adapter {

    Context mContext;
    List<String> ListDistrict;
    List<String> ListDept;
    List<String> ListCnt;

    public EmergencyContactCustomAdapter(Context context,List<String> LDist,List<String> LDpt,List<String> LCnt ){
        mContext = context;
        ListDistrict = LDist;
        ListDept = LDpt;
        ListCnt = LCnt;

    }

    // filter

    public void filterList(ArrayList<String> filterdNames) {
       // this.arylistall = filterdNames;
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

//        vh.txtrcv.setText(mList.get(position));
//        vh.txtconcat.setText(cList.get(position));
//        vh.txtheader.setText(hList.get(position));


        try {
            vh.txtdst.setText(ListDistrict.get(position));
            vh.txtdpt.setText(ListDept.get(position));
            vh.txtcnt.setText(ListCnt.get(position));
        }
        catch (IndexOutOfBoundsException e)
        {
            String a="hiii";
        }
        vh.lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtrcv = vh.txtcnt.getText().toString();


                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+txtrcv));
            //    callIntent.setData(Uri.parse(txtrcv));
                mContext.startActivity(intent);



            }
        });
    }



    @Override
    public int getItemCount() {
        return ListDistrict.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtdst,txtdpt,txtcnt;

        public ImageButton lv;

        public ViewHolder(View v) {
            super(v);
            txtdst = (TextView) v.findViewById(R.id.txtdst);
            txtdpt = (TextView) v.findViewById(R.id.txtdpt);
            txtcnt = (TextView) v.findViewById(R.id.txtcnt);

            lv = (ImageButton) v.findViewById(R.id.img);
        }
    }








}


class  GetDataServerEmrContact extends AsyncTask {
    EmergencyContactCustomAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recycleviewR;
    Context mContext;
    ProgressDialog mDialog;
    String mUserMsg, URL;

    public GetDataServerEmrContact(Context context, String URL,RecyclerView RV) {
        this.mContext = context;
        this.URL = URL;
        this.recycleviewR=RV;
        mDialog = new ProgressDialog(context);


    }

    @Override
    protected void onPreExecute() {
        mDialog.setMessage("Loading Data...");
        mDialog.setCancelable(false);
        mDialog.show();

        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        java.net.URL url;
        HttpURLConnection connection;

        String urlString = URL;


        try {
            url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            OutputStream os = connection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));



            bw.flush();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String data = "", line;
                while ((line = br.readLine()) != null) {
                    data += line;
                }

                return data;
            } else {
                mUserMsg = "Server Couldn't process the request";
            }
        } catch (IOException e) {
            mUserMsg = "Please make sure that Internet connection is available," +
                    " and server IP is inserted in settings";
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        try {

            //connection isn't available or something is wrong with server address
            if(mUserMsg != null)
                throw  new IOException();


            String resp = (String)o;

            JsonArray.ArrayString=resp;

            JSONObject jsonObj = new JSONObject(resp);

            // Getting JSON Array node
            JSONArray contacts = jsonObj.getJSONArray("result");
            ArrayList<String> listDpt=new ArrayList<String>();
            ArrayList<String> listCnt=new ArrayList<String>();
            ArrayList<String> listDst=new ArrayList<String>();

            for (int i = 0; i < contacts.length(); i++) {
                JSONObject c = contacts.getJSONObject(i);
                String department = c.getString("department");

                String contact = c.getString("contact");
                String district = c.getString("district");




                listDpt.add(department);
                listCnt.add(contact);
                listDst.add(district);
            }

            mLayoutManager = new LinearLayoutManager(mContext);
            recycleviewR.setLayoutManager(mLayoutManager);
            mAdapter = new EmergencyContactCustomAdapter(mContext,listDst,listDpt,listCnt);
            recycleviewR.setAdapter(mAdapter);




            if ( resp == null || resp.equals(""))
                throw new NullPointerException("Server response is empty");
            else if(resp.equals("-1")){
                mUserMsg = "Incorrect username or password";
            } else {
                mUserMsg = null;


            }

        }  catch (IOException e) {
            //if connection was available via connecting but
            //we can't get data from server..
            if(mUserMsg == null)
                mUserMsg = "Please check connection";
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
            mUserMsg = e.getMessage();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (mUserMsg != null)
                Toast.makeText(mContext, mUserMsg, Toast.LENGTH_SHORT).show();
        }
        // hide the progressDialog
        mDialog.hide();

        super.onPostExecute(o);
    }
    private Date parseDate(String date, String format) throws ParseException
    {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.parse(date);
    }
}