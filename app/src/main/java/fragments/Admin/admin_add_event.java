package fragments.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import Request.Admin.AddEventRequest;
import Request.Admin.DisplayEventRequest;

/**
 * Created by mayur on 13-12-2016.
 */

public class admin_add_event extends Fragment {
    EditText eventNameEditText;
    EditText descriptionEditText;
    Spinner parentEventSpinner;
    Button submitButton;

    public static final String JSON_ARRAY = "result";
    public static final String KEY_ID = "event_id";
    public static final String KEY_NAME = "eventname";
    public static final String KEY_DESCRIPTION = "description";

    public static String[] ids;
    public static ArrayList<String> names;
    public static String[] descriptions;

    public static int eventItemSelected;
    String parent_id;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_admin_add_event, container, false);
        eventNameEditText = (EditText)mainView.findViewById(R.id.xEventnameEditText);
        descriptionEditText = (EditText)mainView.findViewById(R.id.xDescriptionEditText);
        parentEventSpinner = (Spinner)mainView.findViewById(R.id.xParentEventSpinner);
        submitButton = (Button)mainView.findViewById(R.id.xSubmitButton);

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                JSONArray eventsArray = null;
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response);
                    eventsArray = jsonObject.getJSONArray(JSON_ARRAY);

                    ids = new String[eventsArray.length()];
                    names = new ArrayList<>();
                    names.add("Please select");
                    descriptions = new String[eventsArray.length()];


                    for (int i = 0; i < eventsArray.length(); i++) {
                        JSONObject jo = eventsArray.getJSONObject(i);
                        ids[i] = jo.getString(KEY_ID);
                        names.add(jo.getString(KEY_NAME));
                        descriptions[i] = jo.getString(KEY_DESCRIPTION);
                    }

                    //String temp = names.length +"";
                    //Toast.makeText(AssignEventActivity.this,temp , Toast.LENGTH_LONG).show();

                    parentEventSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                          //  Toast.makeText(getActivity(),"Selected item : "+i,Toast.LENGTH_LONG).show();
                            eventItemSelected=i;
                            if (i!=0) {
                                i--;
                                parent_id = ids[i];
                            }
                            else
                                parent_id="0";
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_dropdown, names);
                    parentEventSpinner.setAdapter(adapter);

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        DisplayEventRequest eventRequest = new DisplayEventRequest(responseListener);
        RequestQueue eventQueue = Volley.newRequestQueue(getActivity());
        eventQueue.add(eventRequest);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String eventname=eventNameEditText.getText().toString();
                final String description=descriptionEditText.getText().toString();
                //final String parent_id=eventItemSelected+"";

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success){
                                Toast.makeText(getActivity(), "Event added successfully.", Toast.LENGTH_SHORT).show();
                            }else{
                                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                                builder.setMessage("Event Add Failed.")
                                        .setNegativeButton("Retry",null)
                                        .create()
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                AddEventRequest registerRequest = new AddEventRequest(eventname,description,parent_id,responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(registerRequest);
            }
        });


        ((admin_home) getActivity()).setActionBarTitle("Add Event");
        //setHasOptionsMenu(true);

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

            case R.id.search:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }//end switch
    }




}
