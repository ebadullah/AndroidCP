package com.mobilisepakistanirfan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mobilisepakistanirfan.pdma.R;
import com.mobilisepakistanirfan.pdma.databinding.CommunityoutreachBinding;
import com.mobilisepakistanirfan.pdma.databinding.WeatheroptionBinding;
import com.mobilisepakistanirfan.pdma.gps.TurnOnGPS;
import com.mobilisepakistanirfan.pdma.report.EarlyWarning;
import com.mobilisepakistanirfan.pdma.report.WeatherForecast;

public class WeatherAdvisOption extends AppCompatActivity {
    WeatheroptionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.weatheroption);





        binding.lltwter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              Intent  intt = new Intent(WeatherAdvisOption.this, EarlyWarning.class);
                startActivity(intt);
            }
        });


        binding.SDw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent  intt = new Intent(WeatherAdvisOption.this, WeatherForecast.class);
                startActivity(intt);
            }
        });

        binding.weatherforcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intt = new Intent(WeatherAdvisOption.this, PMDwebView.class);
                intt.putExtra("header", getString(R.string.wadvpmd));
                intt.putExtra("link", "https://rmckpk.pmd.gov.pk/Kp-W-W.html");
                startActivity(intt);
            }
        });

        binding.Glof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intt = new Intent(WeatherAdvisOption.this, PMDwebView.class);
                intt.putExtra("header", getString(R.string.glof_alert));
                intt.putExtra("link", "http://www.pmd.gov.pk/rnd/rndweb/rnd_new/glof_alerts_advisories.php");
                startActivity(intt);
            }
        });


        binding.earthqucik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intt = new Intent(WeatherAdvisOption.this, PMDwebView.class);
                intt.putExtra("header", getString(R.string.earthquake));
                intt.putExtra("link", "https://seismic.pmd.gov.pk/events.php");
                startActivity(intt);
            }
        });


        binding.drought.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intt = new Intent(WeatherAdvisOption.this, PMDwebView.class);
                intt.putExtra("header", getString(R.string.drought_alert));
                intt.putExtra("link", "https://ndmc.pmd.gov.pk/new/");
                startActivity(intt);
            }
        });

        binding.Flood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intt = new Intent(WeatherAdvisOption.this, PMDwebView.class);
                intt.putExtra("header", getString(R.string.flood_alert));
                intt.putExtra("link", "https://ffd.pmd.gov.pk/home/");
                startActivity(intt);
            }
        });


        binding.lvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                WeatherAdvisOption.this.finish();

            }
        });

    }


    @Override
    public void onBackPressed() {

        WeatherAdvisOption.this.finish();

    }


}

