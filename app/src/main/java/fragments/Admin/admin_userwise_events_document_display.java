package fragments.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

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

import Request.GridViewAdapter;
import Request.Admin.UserwiseDocumentRequest;

/**
 * Created by mayur on 22-12-2016.
 */

public class admin_userwise_events_document_display extends Fragment {
    public static final String JSON_ARRAY = "result";
    public static final String KEY_FLAG = "flag";

    public static final String KEY_EVENT_ID = "event_id";
    public static final String KEY_NAME = "eventname";

    public String[] event_ids;
    public String[] eventnames;
    public String[] flags;

    ListView eventListView;
    ArrayAdapter<String> adapter;

    public static final String TAG_IMAGE_URL = "url";
    public static final String TAG_NAME = "filename";
    public static String event_id ="";
    public static String admin_id ="";
    public static String user_id ="";

    //GridView Object
    private GridView gridView;

    //ArrayList for Storing image urls and titles
    private ArrayList<String> images;
    private ArrayList<String> names;

    private static String decide = "";
    private ProgressDialog loading;
    FragmentManager fm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_admin_userwise_document_display, container, false);
        ((admin_home) getActivity()).setActionBarTitle("User Document/Event Display");
        setHasOptionsMenu(true);

        eventListView = (ListView)mainView.findViewById(R.id.xEventListViewUser);
        gridView = (GridView)mainView.findViewById(R.id.xDocumentGridViewUser);


        fm = getActivity().getSupportFragmentManager();

        //  Intent intent = getIntent();
        //  event_id = intent.getStringExtra("selectedEvent");
        //Retrieve the value
        //String event_id = getArguments().getString("event_id");

        event_id = admin_home.getEvent_id();
        user_id = admin_home.getUser_id();
        //Toast.makeText(getActivity(), event_id, Toast.LENGTH_LONG).show();

        //Toast.makeText(NextActivity.this, event_id+"", Toast.LENGTH_LONG).show();

        images = new ArrayList<>();
        names = new ArrayList<>();

        //Calling the getData method
        getData();


        return mainView;
    }

    private void getData() {
        //Showing a progress dialog while our app fetches the data from url
        loading = ProgressDialog.show(getActivity(), "Please wait...", "Fetching data...", false, false);

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {

                //Toast.makeText(MainActivity.this,"hello",Toast.LENGTH_LONG).show();
                //Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                JSONArray eventsArray = null;
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response);
                    eventsArray = jsonObject.getJSONArray(JSON_ARRAY);

                    //Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();

                    //Dismissing the progressdialog on response
                    loading.dismiss();

                    flags = new String[eventsArray.length()];

                    for (int i = 0; i < eventsArray.length(); i++) {
                        //Creating a json object of the current index
                        JSONObject obj = null;
                        try {
                            //getting json object from current index
                            obj = eventsArray.getJSONObject(i);
                            flags[i] = obj.getString(KEY_FLAG);
                            if (flags[i].equalsIgnoreCase("events")) {
                                decide = "events";
                            } else {
                                decide = "documents";
                                images.add(obj.getString(TAG_IMAGE_URL));
                                names.add(obj.getString(TAG_NAME));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    //decide = flags[0].toString();
                    //Toast.makeText(NextActivity.this, temp , Toast.LENGTH_LONG).show();

                    if (decide.equalsIgnoreCase("documents")) {
                        //Creating GridViewAdapter Object
                        GridViewAdapter gridViewAdapter = new GridViewAdapter(getActivity(), images, names);

                        //Adding adapter to gridview
                        gridView.setAdapter(gridViewAdapter);
                    } else {
                        //loading = ProgressDialog.show(MainActivity.this, "Please wait...", "Fetching data...", false, false);
                        //Toast.makeText(NextActivity.this, "hello event", Toast.LENGTH_LONG).show();

                        //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        jsonObject = new JSONObject(response);
                        eventsArray = jsonObject.getJSONArray(JSON_ARRAY);

                        //Toast.makeText(NextActivity.this, response, Toast.LENGTH_LONG).show();

                        //Dismissing the progressdialog on response
                        loading.dismiss();

                        eventnames = new String[eventsArray.length()];
                        event_ids = new String[eventsArray.length()];

                        for (int i = 0; i < eventsArray.length(); i++) {
                            //Creating a json object of the current index
                            JSONObject obj = null;
                            try {
                                //getting json object from current index
                                obj = eventsArray.getJSONObject(i);
                                event_ids[i] = obj.getString(KEY_EVENT_ID);
                                eventnames[i] = obj.getString(KEY_NAME);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //Toast.makeText(getActivity(), eventnames.length+"", Toast.LENGTH_LONG).show();

                        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, eventnames);

                        eventListView.setAdapter(adapter);

                        //Toast.makeText(MainActivity.this, "hello", Toast.LENGTH_SHORT).show();
                        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                int itemPosition = position;
                                //String event_id = event_ids[itemPosition];
                                //String  itemValue    = (String) eventListView.getItemAtPosition(position);

                                String event_id_this = event_ids[itemPosition].toString();
                                //Toast.makeText(NextActivity.this, event_id, Toast.LENGTH_LONG).show();

                                admin_home.setEvent_id(event_id_this);
                                fm.beginTransaction().replace(R.id.frame_admin_home, new admin_userwise_events_document_display()).addToBackStack(null).commit();
                            }

                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        UserwiseDocumentRequest Request = new UserwiseDocumentRequest(event_id, user_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(Request);
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
