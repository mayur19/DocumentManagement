package com.lambelltech.mayur.dms_master;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.renderscript.Script;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import Request.Admin.LoginRequest;
import Request.RegisterRequest;

/**
 * Created by mayur on 06-01-2017.
 */

public class Register extends AppCompatActivity {
    EditText fullname;
    EditText password;
    EditText passwordAgain;
    EditText username;
    Button btn_register;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        fullname = (EditText)findViewById(R.id.your_full_name);
        password = (EditText)findViewById(R.id.your_password);
        username = (EditText)findViewById(R.id.your_user_name);
        passwordAgain = (EditText)findViewById(R.id.your_password_again);
        btn_register = (Button)findViewById(R.id.btn_register);
        progressBar = (ProgressBar) findViewById(R.id.register_progressbar);
        progressBar.setVisibility(View.GONE);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkOnline()) {

                    progressBar.setVisibility(View.VISIBLE);
                    final String etfullname = fullname.getText().toString();
                    final String etpassword = password.getText().toString();
                    final String etusername = username.getText().toString();
                    final String etpasswordAgain = passwordAgain.getText().toString();

                    if (!etpassword.equalsIgnoreCase(etpasswordAgain)) {
                        progressBar.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                        builder.setMessage("Password does not match")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    } else {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                    JSONObject jsonResponse = new JSONObject(response);

                                    boolean success = jsonResponse.getBoolean("success");

                                    if (success) {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                        builder.setMessage("Register Success. Your account is under review. Please login later")
                                                .setNegativeButton("Ok", null)
                                                .create()
                                                .show();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        RegisterRequest registerRequest = new RegisterRequest(etfullname, etusername, etpassword, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(Register.this);
                        queue.add(registerRequest);
                    }
                }else {
                    //Toast.makeText(getApplicationContext(),"Network Is Not Connected",Toast.LENGTH_LONG).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setMessage("Network is not available")
                            .setPositiveButton("OK", null)
                            .create()
                            .show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }
    public boolean isNetworkOnline() {
        boolean status=false;
        try{
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
                status= true;
            }else {
                netInfo = cm.getNetworkInfo(1);
                if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
                    status= true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return status;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        moveTaskToBack(true);

    }
}
