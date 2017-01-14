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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.lambelltech.mayur.dms_master.LocalDbHelper;
import com.lambelltech.mayur.dms_master.R;
import com.lambelltech.mayur.dms_master.SplashEntry;
import com.lambelltech.mayur.dms_master.admin_home;
import com.lambelltech.mayur.dms_master.admin_login_activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Request.Admin.AssignEventRequest;
import Request.Admin.DisplayEventRequest;
import Request.Admin.DisplayUsersRequest;

/**
 * Created by mayur on 13-12-2016.
 */

public class admin_event_assign extends Fragment {
    public static final String JSON_ARRAY = "result";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_FULLNAME = "fullname";
    public static final String KEY_USERNAME = "username";

    public static String[] user_ids;
    public static String[] fullnames;
    public static String[] usernames;

    public static final String KEY_ID = "event_id";
    public static final String KEY_NAME = "eventname";
    public static final String KEY_DESCRIPTION = "description";

    public static String[] ids;
    public static String[] names;
    public static String[] descriptions;

    public static int userItemSelected;
    public static int eventItemSelected;
    String userid,eventid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_admin_event_assign, container, false);
        ((admin_home) getActivity()).setActionBarTitle("Assign Event");
        //setHasOptionsMenu(true);
        final Spinner userSpinner = (Spinner)mainView.findViewById(R.id.xUserSpinner);
        final Spinner eventSpinner = (Spinner)mainView.findViewById(R.id.xEventSpinner);
        final Button assignButton = (Button)mainView.findViewById(R.id.xAssignButton);

        Response.Listener<String> userResponseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Toast.makeText(AssignEventActivity.this, response, Toast.LENGTH_LONG).show();
                JSONArray usersArray = null;
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response);
                    usersArray = jsonObject.getJSONArray(JSON_ARRAY);

                    user_ids = new String[usersArray.length()];
                    fullnames = new String[usersArray.length()];
                    usernames = new String[usersArray.length()];

                    //String temp = usersArray.length() +"";
                    //Toast.makeText(AssignEventActivity.this,temp , Toast.LENGTH_LONG).show();

                    for (int i = 0; i < usersArray.length(); i++) {
                        JSONObject jo = usersArray.getJSONObject(i);
                        user_ids[i] = jo.getString(KEY_USER_ID);
                        fullnames[i] = jo.getString(KEY_FULLNAME);
                        usernames[i] = jo.getString(KEY_USERNAME);
                    }
                    //String temp = fullnames.length +"";
                    //Toast.makeText(AssignEventActivity.this,temp , Toast.LENGTH_LONG).show();

                    userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            //Toast.makeText(getApplicationContext(),"Selected item : "+i,Toast.LENGTH_LONG).show();
                            userItemSelected = i;
                            userid=user_ids[i];
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, fullnames);
                    userSpinner.setAdapter(adapter);

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        DisplayUsersRequest userRequest = new DisplayUsersRequest(userResponseListener);
        RequestQueue userQueue = Volley.newRequestQueue(getActivity());
        userQueue.add(userRequest);

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                JSONArray eventsArray = null;
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response);
                    eventsArray = jsonObject.getJSONArray(JSON_ARRAY);

                    ids = new String[eventsArray.length()];
                    names = new String[eventsArray.length()];
                    descriptions = new String[eventsArray.length()];

                    for (int i = 0; i < eventsArray.length(); i++) {
                        JSONObject jo = eventsArray.getJSONObject(i);
                        ids[i] = jo.getString(KEY_ID);
                        names[i] = jo.getString(KEY_NAME);
                        descriptions[i] = jo.getString(KEY_DESCRIPTION);
                    }

                    //String temp = names.length +"";
                    //Toast.makeText(AssignEventActivity.this,temp , Toast.LENGTH_LONG).show();

                    eventSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            //Toast.makeText(getApplicationContext(),"Selected item : "+i,Toast.LENGTH_LONG).show();
                            eventItemSelected = i;
                            eventid = ids[i];
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, names);
                    eventSpinner.setAdapter(adapter);

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        DisplayEventRequest eventRequest = new DisplayEventRequest(responseListener);
        RequestQueue eventQueue = Volley.newRequestQueue(getActivity());
        eventQueue.add(eventRequest);


        assignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //String temp = ""+userItemSelected+"   "+eventItemSelected;
                //Toast.makeText(getApplicationContext(),temp,Toast.LENGTH_LONG).show();

                final String user_id=userItemSelected+"";
                final String event_id=eventItemSelected+"";

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success){
                                Toast.makeText(getActivity(), "Event assigned successfully.", Toast.LENGTH_LONG).show();
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

                AssignEventRequest Request = new AssignEventRequest(userid,eventid,responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(Request);

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
