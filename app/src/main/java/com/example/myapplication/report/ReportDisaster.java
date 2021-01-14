package com.example.myapplication.report;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ReportdisasterBinding;
import com.example.myapplication.global.District;
import com.example.myapplication.global.Tehsil;
import com.example.myapplication.global.TypeDisaster;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ReportDisaster extends AppCompatActivity  {
    ReportdisasterBinding binding ;
    ArrayList<String> listDistrict;
    ArrayList<String> listTehsil;
    ArrayList<String> listDisaster;
    String sDistrict="";
    String sTehsil="";
    String sDisasterType="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.reportdisaster);
        listDistrict= District.getDistricts();
        listDisaster= TypeDisaster.getDisaster();



        // when clicked on District will open new Activity for District Selection
        binding.rd1LV.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                Intent  intent = new Intent(ReportDisaster.this, RecyclerViewA.class);
                intent.putExtra("mylist",listDistrict);
                intent.putExtra("header","Select District");
                startActivityForResult(intent,11);

            }
        });

        binding.rd2LV.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {

                if(sDistrict.isEmpty())
                {
                    Toast.makeText(ReportDisaster.this,"Please Select District First",Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent  intent = new Intent(ReportDisaster.this, RecyclerViewA.class);
                listTehsil= Tehsil.get(sDistrict);
                intent.putExtra("mylist",listTehsil);
                intent.putExtra("header","Select Tehsil");
                startActivityForResult(intent,12);

            }
        });

        binding.rd4LV.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                Intent  intent = new Intent(ReportDisaster.this, RecyclerViewA.class);
                intent.putExtra("mylist",listDisaster);
                intent.putExtra("header","Select Type of Disaster");
                startActivityForResult(intent,13);

            }
        });
        binding.rd6LV1.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                ImgView=binding.rd6Imgv1;
                selectImage(ReportDisaster.this);

            }
        });
        binding.rd6LV2.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                ImgView=binding.rd6Imgv2;
                selectImage(ReportDisaster.this);

            }
        });
        binding.rd6LV3.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                ImgView=binding.rd6Imgv3;
                selectImage(ReportDisaster.this);

            }
        });

        binding.btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) ReportDisaster.this).finish();
            }
        });
    }

    ImageView ImgView;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 11) {
            if (resultCode == Activity.RESULT_OK) {
                sDistrict=data.getStringExtra("data");
                binding.rd1Tv.setText(sDistrict);
                binding.rd2Tv.setText(R.string.rd2);

            }
        }
        else if(requestCode == 12 ) {
            if (resultCode == Activity.RESULT_OK) {
                sTehsil = data.getStringExtra("data");
                binding.rd2Tv.setText(sTehsil);

            }
        }

        else if(requestCode == 13 ) {
            if (resultCode == Activity.RESULT_OK) {
                sDisasterType = data.getStringExtra("data");
                binding.rd4Tv.setText(sDisasterType);

            }
        }

        // upload image starts

        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        ImgView.setImageBitmap(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        try {
                            final Uri imageUri = data.getData();
                            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            ImgView.setImageBitmap(selectedImage);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Something went wrong, Unable to select the immage ", Toast.LENGTH_LONG).show();
                        }

                    }

                    break;
            }
        }


        // upload image Ends

    }


    // upload image
    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Remove Picture","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }else
                {
                    ImgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp));
                }
            }
        });
        builder.show();
    }


}