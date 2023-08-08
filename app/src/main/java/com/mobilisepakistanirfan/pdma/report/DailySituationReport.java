package com.mobilisepakistanirfan.pdma.report;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobilisepakistanirfan.pdma.CommunityOutreach;
import com.mobilisepakistanirfan.pdma.databinding.DailysituationBinding;

//import com.example.myapplication.databinding.DailysituationBinding;
import com.mobilisepakistanirfan.pdma.R;

import com.mobilisepakistanirfan.pdma.global.JsonArray;
import com.mobilisepakistanirfan.pdma.global.ServerConfiguration;
import com.mobilisepakistanirfan.pdma.gps.TurnOnGPS;

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
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DailySituationReport extends AppCompatActivity  {
    DailysituationBinding binding ;
 public static    ArrayList<String> listshift=new ArrayList<String>();
 public static   ArrayList<String> listsDate=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.dailysituation);

        binding.lvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) DailySituationReport.this).finish();
            }
        });

        //"http://175.107.63.137/PEOCMIS/api/values/getDSR"
        new GetDataServer(DailySituationReport.this, ServerConfiguration.ServerURL+ "getDSR",binding.recycleviewR).execute();
        binding.btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final  Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker;
                picker = new DatePickerDialog(DailySituationReport.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                binding.btnDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                String url=ServerConfiguration.ServerURL+ "getDSR?Cdate="+dayOfMonth+"-"+(monthOfYear + 1)+"-"+year;


                                new GetDataServer(DailySituationReport.this, url,binding.recycleviewR).execute();

                            }
                        }, year, month, day);



                picker.show();



            }
        });




        binding.lvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DailySituationReport.this.finish();

            }
        });
    }

    public void clearData() {
        listshift.clear(); // clear list
        listsDate.clear(); // clear list
       // mAdapter.notifyDataSetChanged(); // let your adapter know about the changes and reload view.
    }

    @Override
    public void onBackPressed() {

        DailySituationReport.this.finish();
     //   TurnOnGPS.CloseActivityalerd(this);

    }
}

// get data from server
class  GetDataServer extends AsyncTask {
    ViewCustomAdapter mAdapter;
    androidx.recyclerview.widget.RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recycleviewR;
    Context mContext;
    ProgressDialog mDialog;
    String mUserMsg, URL;

    public GetDataServer(Context context, String URL,RecyclerView RV) {
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
        URL url;
        HttpURLConnection connection;

//        String urlString = "http://175.107.63.137/PEOCMIS/api/values/getDSR";
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

    @SuppressLint("SuspiciousIndentation")
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
            JSONArray contacts = jsonObj.getJSONArray("contacts");
            ArrayList<String> listDate=new ArrayList<String>();
            ArrayList<String> listShift=new ArrayList<String>();

            for (int i = 0; i < contacts.length(); i++) {
                JSONObject c = contacts.getJSONObject(i);
                String shift = c.getString("shift");
                if(shift.equals("M"))
                {
                    shift="Morning";
                }
                else if(shift.equals("E"))
                {
                    shift="Evening";
                }

                String adddate = c.getString("addedDate");


                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = dateFormat.parse(adddate);
                dateFormat.applyPattern("dd-MM-yyyy");
               adddate=dateFormat.format(date);


//                String[] date=adddate.split("T");
//                listDate.add(date[0]);
                 listDate.add(adddate);
                 listShift.add(shift);
            }

            mLayoutManager = new LinearLayoutManager(mContext);
            recycleviewR.setLayoutManager(mLayoutManager);
            mAdapter = new ViewCustomAdapter(mContext, listDate,listShift);
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
        } catch (ParseException e) {
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

// Recycle View

class  ViewCustomAdapter extends RecyclerView.Adapter {

    Context mContext;
    List<String> mListDate;
    List<String> mListShift;
    public ViewCustomAdapter(Context context, List<String> list,List<String> listshift){
        mContext = context;
        mListDate = list;
        mListShift = listshift;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dailysituationitem, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ViewHolder vh = (ViewHolder) holder;

        final String Shift=mListShift.get(position);

        vh.txtdate.setText(mListDate.get(position));


        if(Shift.equals("Morning"))
        {

            vh.txtShift.setText(R.string.Morning);
        }
        else
        {
            vh.txtShift.setText(R.string.Evening);
        }




        vh.txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtdate = vh.txtdate.getText().toString();
                String txtshift = vh.txtShift.getText().toString();

                if(Shift.equals("Morning"))
                {
                    txtshift="M";
                }
                else
                {
                    txtshift="E";
                }

            //    Toast.makeText(mContext,txtdate,Toast.LENGTH_SHORT).show();

                String url="https://rms.pdma.gov.pk/DSRs/DSR%20";
                url=url+txtdate+"%20("+txtshift+").pdf";

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mContext.startActivity(browserIntent);

            }
        });
    }



    @Override
    public int getItemCount() {
        return mListDate.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtdate;
        public TextView txtShift;
        public TextView txtView;

        public ViewHolder(View v) {
            super(v);
            txtdate = (TextView) v.findViewById(R.id.txtdate);
            txtShift = (TextView) v.findViewById(R.id.txtshift);
            txtView = (TextView) v.findViewById(R.id.txtView);
        }
    }


}
