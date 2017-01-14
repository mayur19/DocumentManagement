package fragments.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.lambelltech.mayur.dms_master.LocalDbHelper;
import com.lambelltech.mayur.dms_master.R;
import com.lambelltech.mayur.dms_master.SplashEntry;
import com.lambelltech.mayur.dms_master.admin_home;
import com.lambelltech.mayur.dms_master.admin_login_activity;

import org.json.JSONException;
import org.json.JSONObject;

import Request.Admin.AddUserRequest;

/**
 * Created by mayur on 13-12-2016.
 */

public class admin_add_user extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_add_user, container, false);
        ((admin_home) getActivity()).setActionBarTitle("Add User");
        //setHasOptionsMenu(true);
        final EditText FullNameEditText = (EditText)mainView.findViewById(R.id.xFullNameEditText);
        final EditText UsernameEditText = (EditText)mainView.findViewById(R.id.xUsernameEditText);
        final EditText PasswordEditText = (EditText)mainView.findViewById(R.id.xPasswordEditText);
        final Button SubmitButton = (Button)mainView.findViewById(R.id.xSubmitButton);

        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String fullname=FullNameEditText.getText().toString();
                final String username=UsernameEditText.getText().toString();
                final String password=PasswordEditText.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success){
                                Toast.makeText(getActivity(), "User added successfully.", Toast.LENGTH_LONG).show();
                            }else{
                                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                                builder.setMessage("Add User Failed")
                                        .setNegativeButton("Retry",null)
                                        .create()
                                        .show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                AddUserRequest registerRequest = new AddUserRequest(fullname,username,password,responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(registerRequest);
            }
        });


        setHasOptionsMenu(true);
        return mainView;
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.common_search_logout_menu, menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {

            /** EDIT **/
            case R.id.logout:
                //openEditProfile(); //Open Edit Profile Fragment
                LocalDbHelper mydb = new LocalDbHelper(getActivity());
                mydb.executeQuery("root","root",0);
                Intent intent = new Intent(getActivity(),SplashEntry.class);
                getActivity().startActivity(intent);
                return true;

            case R.id.search:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }//end switch
    }



}
