package com.mobilisepakistanirfan.pdma.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mobilisepakistanirfan.pdma.MainActivity;
import com.mobilisepakistanirfan.pdma.R;
import com.mobilisepakistanirfan.pdma.databinding.ActivityLoginBinding;
import com.mobilisepakistanirfan.pdma.global.MyPref;
import com.mobilisepakistanirfan.pdma.global.UserPref;
import com.mobilisepakistanirfan.pdma.gps.TurnOnGPS;

import org.json.JSONException;
import org.json.JSONObject;

public class LogIn extends AppCompatActivity {

    MyPref preferences;
    UserPref userpref2;

    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login );





        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(datavalidation()==false)
                {
                    return;
                }
                boolean uploadStatus= LoginUpServer.LoginUpServer(LogIn.this, UploadDate());



            }
        });

        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LogIn.this, SignUp.class);
                LogIn.this.finish();
                startActivity(intent);



            }
        });
    }



    public  boolean  datavalidation() {
        if (binding.loginUsername.getText().equals("")) {
            Toast.makeText(this, "Please Enter User Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.loginPassword.getText().equals("")) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return  true;
    }


    public JSONObject UploadDate()
    {

        JSONObject log = new JSONObject();
        try {
            log .put("Email", binding.loginUsername.getText().toString().trim());
            log .put("Password", binding.loginPassword.getText().toString().trim());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  log;
    }

    @Override
    public void onBackPressed() {


        TurnOnGPS.CloseActivityalerd(this);

    }

}
