package com.mobilisepakistanirfan.pdma;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mobilisepakistanirfan.pdma.databinding.CommunityoutreachBinding;
import com.mobilisepakistanirfan.pdma.gps.TurnOnGPS;
import com.mobilisepakistanirfan.pdma.report.PublicAwareness;

public class CommunityOutreach extends AppCompatActivity {
    CommunityoutreachBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.communityoutreach);

        binding.imgfbk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/PDMAmediacell"));
                startActivity(browserIntent);
            }
        });



        binding.twiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://x.com/PDMAKP?t=vzvNftnYZOlRTov6beihZA&s=09"));
                startActivity(browserIntent);
            }
        });


        binding.imgyoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/channel/UCCfpxBgGJqV4phk_aqdfdSg"));
                startActivity(browserIntent);
            }
        });


        binding.lnk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/company/provincial-disaster-management-authority-pdma-peshawar/"));
                startActivity(browserIntent);
            }
        });


        binding.web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(CommunityOutreach.this, PublicAwareness.class);
                startActivity(browserIntent);
            }
        });

        binding.lvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                CommunityOutreach.this.finish();

            }
        });

    }


    @Override
    public void onBackPressed() {

        CommunityOutreach.this.finish();
      //  TurnOnGPS.CloseActivityalerd(this);
    }


}

