package com.mobilisepakistanirfan.pdma;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.androdocs.httprequest.HttpRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;
import com.mobilisepakistanirfan.WeatherAdvisOption;
import com.mobilisepakistanirfan.pdma.global.GetComplaintCategoriesServer;
import com.mobilisepakistanirfan.pdma.global.GetComplaintDamageTypeServer;
import com.mobilisepakistanirfan.pdma.global.GetDistrictServer;
import com.mobilisepakistanirfan.pdma.global.GetIncidentNatureServer;
import com.mobilisepakistanirfan.pdma.global.GetTehsilServer;
import com.mobilisepakistanirfan.pdma.global.MyPref;
import com.mobilisepakistanirfan.pdma.global.MyReceiver;
import com.mobilisepakistanirfan.pdma.global.ServerConfiguration;
import com.mobilisepakistanirfan.pdma.global.UserPref;
import com.mobilisepakistanirfan.pdma.gps.TurnOnGPS;
import com.mobilisepakistanirfan.pdma.report.Complaints;
import com.mobilisepakistanirfan.pdma.report.DailySituationReport;
import com.mobilisepakistanirfan.pdma.report.DemageNeedAssesment;
import com.mobilisepakistanirfan.pdma.report.EmergencyContact;
import com.mobilisepakistanirfan.pdma.report.EvacuationCenter;
import com.mobilisepakistanirfan.pdma.report.News;
import com.mobilisepakistanirfan.pdma.report.QuickLink;
import com.mobilisepakistanirfan.pdma.report.RapidNeedAssessment;
import com.mobilisepakistanirfan.pdma.report.ReportDisaster;
import com.mobilisepakistanirfan.pdma.report.RiskAssesment;
import com.mobilisepakistanirfan.pdma.signup.LogIn;
import com.mobilisepakistanirfan.pdma.utilities.Policy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    private AppBarConfiguration mAppBarConfiguration;
    String CITY;
    String lat;
    String lon;
    private FusedLocationProviderClient fusedLocationClient;
    String API = "55e1d7a613263d5aea5ff2bceda55d4a";
    TextView tTemp, tHuminty, tLocaiton, tDisciption;
    ImageView img;
    LinearLayout lnwa, lnew, lnds, lnrd, lnercon, lnqevc;
    int userId;

    private BroadcastReceiver MyReceiver = null;

    MyPref preferences;
    UserPref userpref2;
    String language;
    String country;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = new MyPref(MainActivity.this);
        userpref2 = new UserPref(MainActivity.this);

        methodRequiresTwoPermission();

        // Firebase Notifcaiton

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }


        country = preferences.getCountry();
        language = preferences.getLanguage();

        Locale locale = new Locale(language, country);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // call intenet check
        MyReceiver = new MyReceiver();
        broadcastIntent();

        userId = preferences.getUserId();

        lnwa = findViewById(R.id.lnwa);
        lnew = findViewById(R.id.lnew);
        lnds = findViewById(R.id.lnds);
        lnrd = findViewById(R.id.lnrd);

        lnercon = findViewById(R.id.lnercon);
        lnqevc = findViewById(R.id.lnqevc);
        lnwa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, News.class);
                startActivity(intent);
            }
        });

        lnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                intent = new Intent(MainActivity.this, EarlyWarning.class);
                intent = new Intent(MainActivity.this, WeatherAdvisOption.class);
                startActivity(intent);


            }
        });

        lnds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(MainActivity.this, DailySituationReport.class);
                startActivity(intent);


            }
        });

        lnercon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(MainActivity.this, EmergencyContact.class);
                startActivity(intent);


            }
        });
        lnqevc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(MainActivity.this, EvacuationCenter.class);
                startActivity(intent);


            }
        });

        lnrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userId == 0) {
                    //mainIntent=new Intent(Splash.this, LogIn.class);

                    TurnOnGPS.LoginActivityalert(MainActivity.this);

                } else {

                    //  mainIntent=new Intent(Splash.this,MainActivity.class);

                    if (userpref2.getUserStatus().equals("Active")) {
                        intent = new Intent(MainActivity.this, ReportDisaster.class);
                        startActivity(intent);
                    } else {
                        TurnOnGPS.PDMAStaffLoginalret(MainActivity.this);
                    }
                }
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


        tTemp = findViewById(R.id.temp);


        tHuminty = findViewById(R.id.humidity);
        tLocaiton = findViewById(R.id.city);

        tDisciption = findViewById(R.id.discp);
        img = findViewById(R.id.img);


        if (preferences.getappcount() == 0) {
            try {
                new GetTehsilServer(MainActivity.this, ServerConfiguration.ServerURL + "GetTehsilAction").execute().get();
                new GetDistrictServer(MainActivity.this, ServerConfiguration.ServerURL + "GetDistrictsAction").execute();
                new GetComplaintCategoriesServer(MainActivity.this, ServerConfiguration.ServerURL + "GetComplaintCategoriesAction").execute().get();
                new GetIncidentNatureServer(MainActivity.this, ServerConfiguration.ServerURL + "GetIncidentNatureAction").execute().get();
                new GetComplaintDamageTypeServer(MainActivity.this, ServerConfiguration.ServerURL + "GetComplaintDamageTypeAction").execute().get();

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        boolean GpsStatus;
        locationManager = (LocationManager) MainActivity.this.getSystemService(MainActivity.this.LOCATION_SERVICE);
        assert locationManager != null;
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (GpsStatus == false) {
            TurnOnGPS.turnGPSOn(MainActivity.this);
        }

        getLocation();

//        else {
//
//            Intent intentt = new Intent(MainActivity.this, ShowlocaitonActivityNew.class);
//            startActivityForResult(intentt, 22);
//        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        // Handle item selection
        switch (item.getItemId()) {
            case R.id.nav_about:

                intent = new Intent(this, AboutApp.class);
                startActivity(intent);
                // this.finish();

                return true;

            case R.id.nav_userprofile:

                intent = new Intent(this, UserProfile.class);
                startActivity(intent);
                // this.finish();

                return true;

            case R.id.nav_english:

                preferences.setlanguage("en");
                preferences.setcountry("US");

                Intent intent = new Intent(this, Splash.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);


                this.startActivity(intent);
                if (this instanceof Activity) {
                    ((Activity) this).finish();
                }
                return true;

            case R.id.nav_urdu:

                preferences.setlanguage("ur");
                preferences.setcountry("PK");

                intent = new Intent(this, Splash.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);


                this.startActivity(intent);
                if (this instanceof Activity) {
                    ((Activity) this).finish();
                }


                return true;


            case R.id.st_login:

                intent = new Intent(this, LogIn.class);
                startActivity(intent);
                // this.finish();

                return true;
//            case R.id.st_sigup:
//
//                intent = new Intent(this, SignUp.class);
//                startActivity(intent);
//                //   this.finis h();
//
//                return true;


            case R.id.nav_getdate:

                new GetTehsilServer(MainActivity.this, ServerConfiguration.ServerURL + "GetTehsilAction").execute();

                new GetDistrictServer(MainActivity.this, ServerConfiguration.ServerURL + "GetDistrictsAction").execute();

                return true;

            case R.id.st_logout:

                MyPref prefs = new MyPref(this);
                UserPref userpre = new UserPref(this);
                userId = 0;
                prefs.setUserId(0);
                prefs.setUserDistrict("");
                prefs.setFirebaseVolnt("0");

                userpre.setUserUserStatus("");
                userpre.setUserTypel("");
                userpre.setUserEmail("");
                userpre.setUserMobileNo("");

                intent = new Intent(this, LogIn.class);
                startActivity(intent);
                //     this.finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
            if (userId == 0) {
                TurnOnGPS.LoginActivityalert(MainActivity.this);

            } else {
                //  mainIntent=new Intent(Splash.this,MainActivity.class);

                if (userpref2.getUserStatus().equals("Active")) {
                    intent = new Intent(MainActivity.this, ReportDisaster.class);
                    startActivity(intent);
                } else {
                    TurnOnGPS.PDMAStaffLoginalret(MainActivity.this);
                }


            }
        } else if (id == R.id.nav_ew) {
            //  intent = new Intent(this, EarlyWarning.class);
            intent = new Intent(this, WeatherAdvisOption.class);
            startActivity(intent);
        } else if (id == R.id.nav_ds) {
            intent = new Intent(this, DailySituationReport.class);
            startActivity(intent);
        } else if (id == R.id.nav_new) {
            intent = new Intent(this, News.class);
            startActivity(intent);
        } else if (id == R.id.emgncyContact) {
            intent = new Intent(this, EmergencyContact.class);
            startActivity(intent);
        } else if (id == R.id.nav_co) {
            intent = new Intent(this, CommunityOutreach.class);
            startActivity(intent);
        } else if (id == R.id.evactioncenter) {
            intent = new Intent(this, EvacuationCenter.class);
            startActivity(intent);
        } else if (id == R.id.nav_quick_links) {
            intent = new Intent(this, QuickLink.class);
            startActivity(intent);
        } else if (id == R.id.nav_policy_links) {
            intent = new Intent(this, Policy.class);
            startActivity(intent);
        }
//        else if (id == R.id.nav_wf) {
//            intent = new Intent(this, WeatherForecast.class);
//            startActivity(intent);
//        }

        else if (id == R.id.nav_risk) {
            intent = new Intent(this, RiskAssesment.class);
            startActivity(intent);
        } else if (id == R.id.nav_complaint) {


            if (userId == 0) {
                //mainIntent=new Intent(Splash.this, LogIn.class);

                TurnOnGPS.LoginActivityalert(MainActivity.this);

            } else {


                intent = new Intent(this, Complaints.class);
                startActivity(intent);


            }
        } else if (id == R.id.nav_RNA) {


            if (userId == 0) {
                TurnOnGPS.LoginActivityalert(MainActivity.this);

            } else {
                //  mainIntent=new Intent(Splash.this,MainActivity.class);


                if (userpref2.getUserStatus().equals("Active")) {
                    intent = new Intent(MainActivity.this, RapidNeedAssessment.class);
                    startActivity(intent);
                } else {
                    TurnOnGPS.PDMAStaffLoginalret(MainActivity.this);
                }


            }
        } else if (id == R.id.nav_DNA) {

            if (userId == 0) {
                TurnOnGPS.LoginActivityalert(MainActivity.this);

            } else {
                //  mainIntent=new Intent(Splash.this,MainActivity.class);


                if (userpref2.getUserStatus().equals("Active")) {
                    intent = new Intent(MainActivity.this, DemageNeedAssesment.class);
                    startActivity(intent);
                } else {
                    TurnOnGPS.PDMAStaffLoginalret(MainActivity.this);
                }


            }
        }
//        else if (id == R.id.nav_LD) {
//            intent = new Intent(this, DemagesLosses.class);
//            startActivity(intent);
//        }
//        else if (id == R.id.nav_logout) {
//
//            MyPref prefs = new MyPref(this);
//
//            prefs.setUserId(0);
//            intent = new Intent(this, LogIn.class);
//            startActivity(intent);
//            this.finish();
//        }
//        else if (id == R.id.nav_login) {
//
//
//            intent = new Intent(this, LogIn.class);
//            startActivity(intent);
//            this.finish();
//        }

//        else if (id == R.id.nav_about) {
//
//
//            intent = new Intent(this, AboutApp.class);
//            startActivity(intent);
//
//        }

        // change langue
//
//        else if (id == R.id.nav_urdu) {
//
//
//            preferences.setlanguage("ur");
//            preferences.setcountry("PK");
//
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
//
//
//
//            this.startActivity(intent);
//            if (this instanceof Activity) {
//                ((Activity) this).finish();
//            }
//        }
//        else if (id == R.id.nav_english) {
//
//            preferences.setlanguage("en");
//            preferences.setcountry("US");
//
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
//
//
//
//            this.startActivity(intent);
//            if (this instanceof Activity) {
//                ((Activity) this).finish();
//            }
//        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 22) {
            if (resultCode == Activity.RESULT_OK) {
                String Lat = data.getStringExtra("Lat");
                String Long = data.getStringExtra("Long");

                lat = Lat;
                lon = Long;

                preferences.setlat(lat);
                preferences.setlong(lon);

                new weatherTask().execute();
            }
        }

    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(255)
    private boolean methodRequiresTwoPermission() {
        String[] perms = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            perms = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.POST_NOTIFICATIONS};
        } else {
            perms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        }

        if (EasyPermissions.hasPermissions(this, perms)) {
            return true;
        }
        EasyPermissions.requestPermissions(this, getString(R.string.app_name), 45, perms);
        return false;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        Log.e("latitude", "Latitude:" + location.getLatitude());
        Log.e("Longitude", "Longitude:" + location.getLongitude());

        lat = String.valueOf(location.getLatitude());
        lon = String.valueOf(location.getLongitude());

        preferences.setlat(lat);
        preferences.setlong(lon);

        new weatherTask().execute();
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }


    class weatherTask extends AsyncTask<String, Void, String> {
        @Override

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
//            String url = "https://api.openweathermap.org/data/2.5/weather?lat=44.34&lon=10.99&appid=" + API;

            //String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid=" + API);
            String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&units=metric&appid=" + API;
            String response = HttpRequest.excuteGet(url);

            Log.e("###", response);

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
                String icon = weather.getString("icon");
                String mainn = weather.getString("main");

                String humi_dity = main.getString("humidity");

                String temp_min = main.getString("temp_min");

                String temp_max = main.getString("temp_max");

                Long rise = sys.getLong("sunrise");

                String sunrise = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(rise * 1000));

                Long set = sys.getLong("sunset");

                String sunset = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(set * 1000));

                // SET ALL VALUES IN TEXTBOX :

                //  tLocaiton.setText(city_name+", "+countryname);
                tLocaiton.setText("");


                tTemp.setText(temperature + "°C");


                tHuminty.setText("Humidity " + humi_dity);


                tDisciption.setText(cast.toUpperCase());


                String img_url = "http://openweathermap.org/img/wn/" + icon + "@2x.png";

                Picasso.get().load(img_url).into(img);


            } catch (Exception e) {

                Toast.makeText(MainActivity.this, "Error:" + e.toString(), Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public void onBackPressed() {
        TurnOnGPS.CloseActivityalerd(this);
    }


    // check internet connection

    public void broadcastIntent() {
        registerReceiver(MyReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(MyReceiver);
    }

}
