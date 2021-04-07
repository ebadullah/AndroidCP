package com.mobilisepakistan.civilprotection;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.androdocs.httprequest.HttpRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mobilisepakistan.civilprotection.global.MyPref;
import com.mobilisepakistan.civilprotection.gps.ShowLocationActivity2;
import com.mobilisepakistan.civilprotection.gps.TurnOnGPS;
import com.mobilisepakistan.civilprotection.report.DailySituationReport;
import com.mobilisepakistan.civilprotection.report.DemageNeedAssesment;
import com.mobilisepakistan.civilprotection.report.EarlyWarning;
import com.mobilisepakistan.civilprotection.report.RapidNeedAssessment;
import com.mobilisepakistan.civilprotection.report.ReportDisaster;

import com.mobilisepakistan.civilprotection.signup.LogIn;
import com.getbase.floatingactionbutton.FloatingActionButton;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.mobilisepakistan.civilprotection.weather.getweather;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    String CITY;
    String lat;
    String lon;
    private FusedLocationProviderClient fusedLocationClient;
    String API = "55e1d7a613263d5aea5ff2bceda55d4a";
    TextView tMin,tTemp,tSunset,tSunris,tHuminty,tLocaiton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton action_rd = findViewById(R.id.action_rd);


        action_rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, ReportDisaster.class);
              //  intent = new Intent(MainActivity.this, ShowLocationActivity.class);
                startActivity(intent);


            }
        }
        );

        FloatingActionButton action_erw = findViewById(R.id.action_erw);
        action_erw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, EarlyWarning.class);
                startActivity(intent);


            }
        });

        FloatingActionButton action_dna = findViewById(R.id.action_dsr);
        action_dna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, DailySituationReport.class);
                startActivity(intent);


            }
        });



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        navigationView.setNavigationItemSelectedListener(this);


        // weather info

        tMin=findViewById(R.id.tempmin);
        tTemp=findViewById(R.id.temp);
        tSunris=findViewById(R.id.sunrise);
        tSunset=findViewById(R.id.sunset);
        tHuminty=findViewById(R.id.humidity);
        tLocaiton=findViewById(R.id.city);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            lat=location.getLatitude()+"";
                            lon=location.getLongitude()+"";
                            new weatherTask().execute();
                        }
                        else
                        {
                            LocationManager locationManager ;
                            boolean GpsStatus ;
                            locationManager = (LocationManager)MainActivity.this.getSystemService(MainActivity.this.LOCATION_SERVICE);
                            assert locationManager != null;
                            GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                            if(GpsStatus == false) {
                                TurnOnGPS.turnGPSOn(MainActivity.this);
                            }
                            else {
                                Intent intentt = new Intent(MainActivity.this, ShowLocationActivity2.class);
                                startActivityForResult(intentt, 22);
                            }
                        }
                    }
                });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    Intent intent = null;
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();
        if (id == R.id.nav_RD) {
       intent = new Intent(this, ReportDisaster.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_ew) {
            intent = new Intent(this, EarlyWarning.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_ds) {
            intent = new Intent(this, DailySituationReport.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_RNA) {
            intent = new Intent(this, RapidNeedAssessment.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_DNA) {
            intent = new Intent(this, DemageNeedAssesment.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_logout) {

            MyPref prefs = new MyPref(this);

            prefs.setUserId(Integer.parseInt("0"));
            intent = new Intent(this, LogIn.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 22 ) {
            if (resultCode == Activity.RESULT_OK) {
                String Lat = data.getStringExtra("Lat");
                String Long = data.getStringExtra("Long");


                lat=Lat;
                lon=Long;
                new weatherTask().execute();

            }
        }

    }
    class weatherTask extends AsyncTask<String, Void, String> {
        @Override

        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... args) {
            //String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid=" + API);
            String url="https://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&units=metric&appid=" + API;
            String response = HttpRequest.excuteGet(url);

            return response;
        }


        @Override

        protected void onPostExecute(String result) {

            try {

                JSONObject jsonObj = new JSONObject(result);

                JSONObject main = jsonObj.getJSONObject("main");

                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                JSONObject sys = jsonObj.getJSONObject("sys");

                // CALL VALUE IN API :

                String city_name = jsonObj.getString("name");

                String countryname = sys.getString("country");

                Long updatedAt = jsonObj.getLong("dt");

                String updatedAtText = "Last Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));

                String temperature = main.getString("temp");

                String cast = weather.getString("description");

                String humi_dity = main.getString("humidity");

                String temp_min = main.getString("temp_min");

                String temp_max = main.getString("temp_max");

                Long rise = sys.getLong("sunrise");

                String sunrise = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(rise * 1000));

                Long set = sys.getLong("sunset");

                String sunset = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(set * 1000));

                // SET ALL VALUES IN TEXTBOX :

                tLocaiton.setText(city_name+", "+countryname);




                tTemp.setText(temperature + "°C");



                tHuminty.setText("Humidity "+humi_dity);

                tMin.setText("Min "+temp_min+" Max "+temp_max);



                tSunris.setText("Sunrise "+sunrise);

                tSunset.setText("Sunset "+sunset);

            } catch (Exception e) {

                Toast.makeText(MainActivity.this, "Error:" + e.toString(), Toast.LENGTH_SHORT).show();

            }

        }

    }
}
