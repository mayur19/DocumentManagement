package com.lambelltech.mayur.dms_master;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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

import Request.Admin.AdminLoginRequest;

/**
 * Created by mayur on 26-12-2016.
 */

public class admin_login_activity extends AppCompatActivity {

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login_activity);
        final EditText UsernameEditText = (EditText) findViewById(R.id.etUsername);
        final EditText PasswordEditText = (EditText) findViewById(R.id.etUsername);
        final Button LoginButton = (Button) findViewById(R.id.btn_login);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);


        progressBar.setVisibility(View.GONE);

        //final TextView RegisterLinkTextView = (TextView)findViewById(R.id.xRegisterTextView);


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkOnline()) {

                    progressBar.setVisibility(View.VISIBLE);
                    final String username = UsernameEditText.getText().toString();
                    final String password = PasswordEditText.getText().toString();

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                JSONObject jsonResponse = new JSONObject(response);

                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    String name = jsonResponse.getString("fullname");
                                    int admin_id = jsonResponse.getInt("admin_id");
                                    String adminid = admin_id+"";

                                    Intent intent = new Intent(admin_login_activity.this, admin_home.class);
                                    intent.putExtra("fullname", name);
                                    intent.putExtra("username", username);
                                    intent.putExtra("adminid",adminid);

                                    admin_login_activity.this.startActivity(intent);
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(admin_login_activity.this);
                                    builder.setMessage("Login Failed")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    AdminLoginRequest loginRequest = new AdminLoginRequest(username, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(admin_login_activity.this);
                    queue.add(loginRequest);
                } else {
                    //Toast.makeText(getApplicationContext(),"Network Is Not Connected",Toast.LENGTH_LONG).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(admin_login_activity.this);
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

        Intent intent = new Intent(this,MainActivity.class);
        admin_login_activity.this.startActivity(intent);

    }
}
