package com.lambelltech.mayur.dms_master;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import Request.Admin.LoginRequest;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;
    public static String username ="";
    public static String password ="";
    public String newUsername;
    public String newPassword;
    public static String uname;
    public static String pass;
    LocalDbHelper mydb;

    TextView click;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText UsernameEditText = (EditText) findViewById(R.id.etUsername);
        final EditText PasswordEditText = (EditText) findViewById(R.id.etUsername);
        final Button LoginButton = (Button) findViewById(R.id.btn_login);
        //adminTextview = (TextView) findViewById(R.id.admin_login);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        click = (TextView) findViewById(R.id.text_register);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                MainActivity.this.startActivity(intent);
            }
        });

        Intent intent = getIntent();
        if (intent.getStringExtra("username").equalsIgnoreCase("root") && intent.getStringExtra("password").equalsIgnoreCase("root")) {
            progressBar.setVisibility(View.GONE);

            //final TextView RegisterLinkTextView = (TextView)findViewById(R.id.xRegisterTextView);
            LoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isNetworkOnline()) {

                        progressBar.setVisibility(View.VISIBLE);

                        username = UsernameEditText.getText().toString();
                        password = PasswordEditText.getText().toString();

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
                                        pass = jsonResponse.getString("password");
                                        uname = jsonResponse.getString("username");
                                        int _status =1;
                                        int user_id = jsonResponse.getInt("user_id");
                                        int status = jsonResponse.getInt("status");
                                        String role = jsonResponse.getString("role");
                                        String userid = user_id + "";

                                        LocalDbHelper mydb = new LocalDbHelper(MainActivity.this);
                                        mydb.executeQuery(uname,pass,_status);

                                        if (status != 0) {
                                            if (!role.equalsIgnoreCase("hod")) {
                                                Intent intent = new Intent(MainActivity.this, user_home.class);
                                                intent.putExtra("fullname", name);
                                                intent.putExtra("username", username);
                                                intent.putExtra("userid", userid);
                                                MainActivity.this.startActivity(intent);
                                            } else {
                                                Intent intent = new Intent(MainActivity.this, admin_home.class);
                                                intent.putExtra("fullname", name);
                                                intent.putExtra("username", username);
                                                intent.putExtra("adminid", userid);
                                                MainActivity.this.startActivity(intent);
                                            }
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                            builder.setMessage("Your account is not activated.")
                                                    .setNegativeButton("Retry", null)
                                                    .create()
                                                    .show();
                                        }


                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                        LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                        queue.add(loginRequest);
                    } else {
                        //Toast.makeText(getApplicationContext(),"Network Is Not Connected",Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Network is not available")
                                .setPositiveButton("OK", null)
                                .create()
                                .show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }else {
            newUsername = intent.getStringExtra("username");
            newPassword = intent.getStringExtra("password");
           // Toast.makeText(getApplication(),"true"+username +password,Toast.LENGTH_LONG).show();
            if (isNetworkOnline()) {

                progressBar.setVisibility(View.VISIBLE);

                username = newUsername;
                password = newPassword;

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
                                int user_id = jsonResponse.getInt("user_id");
                                int status = jsonResponse.getInt("status");
                                String role = jsonResponse.getString("role");
                                String userid = user_id + "";
                                if (status != 0) {
                                    if (!role.equalsIgnoreCase("hod")) {
                                        Intent intent = new Intent(MainActivity.this, user_home.class);
                                        intent.putExtra("fullname", name);
                                        intent.putExtra("username", username);
                                        intent.putExtra("userid", userid);
                                        MainActivity.this.startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(MainActivity.this, admin_home.class);
                                        intent.putExtra("fullname", name);
                                        intent.putExtra("username", username);
                                        intent.putExtra("adminid", userid);
                                        MainActivity.this.startActivity(intent);
                                    }
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setMessage("Your account is not activated.")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }


                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(loginRequest);
            } else {
                //Toast.makeText(getApplicationContext(),"Network Is Not Connected",Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Network is not available")
                        .setPositiveButton("OK", null)
                        .create()
                        .show();
                progressBar.setVisibility(View.GONE);
            }
        }

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

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
