package com.mobilisepakistanirfan.pdma.report;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mobilisepakistanirfan.pdma.R;
import com.mobilisepakistanirfan.pdma.data.LocalDataManager;
import com.mobilisepakistanirfan.pdma.databinding.ComplaintsBinding;
import com.mobilisepakistanirfan.pdma.global.MyPref;
import com.mobilisepakistanirfan.pdma.global.UploadComplaint;
import com.mobilisepakistanirfan.pdma.gps.TurnOnGPS;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Complaints extends AppCompatActivity {
    ComplaintsBinding binding;
    int UserID = 0;
    MyPref preferences;
    ArrayList<String> listComplaintCategory;
    String sComplaintCategory = "";

    ArrayList<String> listIncidentNature;
    String sComplaintNature = "";


    ArrayList<String> listComplaintDamageType;
    String sComplaintDamageType = "";

    private Bitmap bitmapimag1 = null;
    private Bitmap bitmapimag2 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.complaints);




        listComplaintCategory = new ArrayList<>();
        listComplaintCategory = LocalDataManager.GetComplaintCategories(this);


        listIncidentNature = new ArrayList<>();
        listIncidentNature = LocalDataManager.GetIncidentNature(this);

        listComplaintDamageType = new ArrayList<>();
        listComplaintDamageType = LocalDataManager.GetComplaintDamageType(this);

        preferences = new MyPref(this);
        binding.lvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) Complaints.this).finish();
            }
        });


        binding.btnIncidentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker;
                picker = new DatePickerDialog(Complaints.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                binding.btnIncidentDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, year, month, day);


                picker.show();


            }
        });

        // when clicked on District will open new Activity for District Selection
        binding.lcomplaintcategory.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                Intent intent = new Intent(Complaints.this, RecyclerViewA.class);
                intent.putExtra("mylist", listComplaintCategory);
                startActivityForResult(intent, 11);

            }
        });


        binding.lcomplaintincidentnature.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                Intent intent = new Intent(Complaints.this, RecyclerViewA.class);
                intent.putExtra("mylist", listIncidentNature);
                startActivityForResult(intent, 12);

            }
        });


        binding.lcomplaintdamagetype.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                Intent intent = new Intent(Complaints.this, RecyclerViewA.class);
                intent.putExtra("mylist", listComplaintDamageType);
                startActivityForResult(intent, 13);

            }
        });

        binding.btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (datavalidation() == false) {
                    return;
                }


                // upload picture

                ArrayList<Bitmap> bitmapparr = new ArrayList<>();


                try {


                    if (!bitmapimag1.equals(null) || !bitmapimag1.equals("")) {

                        bitmapparr.add(bitmapimag1);


                    }
                } catch (Exception e) {

                }

                try {


                    if (!bitmapimag2.equals(null) || !bitmapimag2.equals("")) {

                        bitmapparr.add(bitmapimag2);

                    }
                } catch (Exception e) {

                }

                UploadComplaint.volleyPost(Complaints.this, UploadDate(), bitmapparr);

            }
        });


        binding.rd6LV1.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                ImgView = binding.rd6Imgv1;
                selectImage(Complaints.this);

            }
        });
        binding.rd6LV2.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                ImgView = binding.rd6Imgv2;

                selectImage(Complaints.this);

            }
        });

    }


    // upload image
    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Remove Picture", "Cancel"};

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
                    //    String filpath=android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    startActivityForResult(pickPhoto, 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                } else {
                    ImgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp));
                }
            }
        });
        builder.show();
    }


    ImageView ImgView;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 11) {
            if (resultCode == Activity.RESULT_OK) {
                sComplaintCategory = data.getStringExtra("data");
                binding.rd1Tv.setText(sComplaintCategory);
            }
        } else if (requestCode == 12) {
            if (resultCode == Activity.RESULT_OK) {
                sComplaintNature = data.getStringExtra("data");
                binding.rd1cn.setText(sComplaintNature);
            }
        } else if (requestCode == 13) {
            if (resultCode == Activity.RESULT_OK) {
                sComplaintDamageType = data.getStringExtra("data");
                binding.rd1cdt.setText(sComplaintDamageType);
            }
        }
        // upload image starts

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        ImgView.setImageBitmap(selectedImage);

                        if (ImgView == binding.rd6Imgv1) {
                            bitmapimag1 = selectedImage;
                        } else if (ImgView == binding.rd6Imgv2) {
                            bitmapimag2 = selectedImage;
                        }

                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        try {
                            final Uri imageUri = data.getData();


                            final InputStream imageStream = getContentResolver().openInputStream(imageUri);

                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            ImgView.setImageBitmap(selectedImage);

                            if (ImgView == binding.rd6Imgv1) {
                                bitmapimag1 = selectedImage;
                            } else if (ImgView == binding.rd6Imgv2) {
                                bitmapimag2 = selectedImage;
                            }


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


    public boolean datavalidation() {
        if (binding.rd3Tv.getText().equals("")) {
            Toast.makeText(this, "Please Enter Complaint Title", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.rd5Tv.getText().equals("")) {
            Toast.makeText(this, "Please Enter Complaint Detail", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    @Override
    public void onBackPressed() {

        TurnOnGPS.CloseActivityalerd(this);
    }


    public JSONObject UploadDate() {

        UserID = preferences.getUserId();

        String Title = binding.rd3Tv.getText().toString();
        String Detail = binding.rd5Tv.getText().toString();

        int complaint_category_id = LocalDataManager.GetComplaintCategoryId(binding.rd1Tv.getText().toString().trim(), Complaints.this);
        int incident_nature_id = LocalDataManager.GetIncidentNatureId(binding.rd1cn.getText().toString().trim(), Complaints.this);
        int complaint_damage_type_id = LocalDataManager.GetComplaintDamageTypeId(binding.rd1cdt.getText().toString().trim(), Complaints.this);
        String incident_date = binding.btnIncidentDate.getText().toString().trim();

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String Currentdate = df.format(c);

        JSONObject log = new JSONObject();
        try {
            log.put("Title", Title);
            log.put("Detail", Detail);
            log.put("UserId", UserID);
            log.put("complaint_category_id", complaint_category_id);
            log.put("incident_nature_id", incident_nature_id);
            log.put("complaint_damage_type_id", 0);
            log.put("incident_date", incident_date);
            log.put("Dated", "0");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return log;
    }


}




