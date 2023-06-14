package com.mobilisepakistanirfan.pdma.global;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.mobilisepakistanirfan.pdma.data.LocalDataManager;

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
import java.util.ArrayList;


public class GetComplaintCategoriesServer extends AsyncTask {


    Context mContext;
    //  ProgressDialog mDialog;
    String mUserMsg, URL;
    MyPref preferences;
    public GetComplaintCategoriesServer(Context context, String URL) {
        this.mContext = context;
        this.URL = URL;
        preferences = new MyPref(context);

        //    mDialog = new ProgressDialog(context);


    }

    @Override
    protected void onPreExecute() {
        // mDialog.setMessage("Loading Data...");
        // mDialog.setCancelable(false);
        // mDialog.show();

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

            // District.listDistrict.clear();
            // District.listDistrictid.clear();

            ArrayList<String> lstdistrict=new ArrayList<>();
            ArrayList<String> lstDistriid=new ArrayList<>();
            for (int i = 0; i < contacts.length(); i++) {

                JSONObject c = contacts.getJSONObject(i);

                lstdistrict.add(c.getString("complaint_category_title"));
                lstDistriid.add(c.getString("complaint_category_id"));

            }

            int ccc=preferences.getappcount()+1;
            preferences.setappcount(ccc);

            LocalDataManager.InsertComplaintCategories(lstdistrict,lstDistriid,mContext);

            Toast.makeText(mContext,"Application data has been updated",Toast.LENGTH_LONG).show();

            // restart app due to GPS first time installition issue


            //





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
        //  mDialog.hide();

        super.onPostExecute(o);
    }
}