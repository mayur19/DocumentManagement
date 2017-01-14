package fragments.User;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.lambelltech.mayur.dms_master.LocalDbHelper;
import com.lambelltech.mayur.dms_master.MainActivity;
import com.lambelltech.mayur.dms_master.R;
import com.lambelltech.mayur.dms_master.SplashEntry;
import com.lambelltech.mayur.dms_master.user_home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Request.GridViewAdapter;
import Request.User.DocumentDisplayRequest;

import static com.lambelltech.mayur.dms_master.user_home.user_id;

/**
 * Created by mayur on 13-12-2016.
 */

public class user_event_view extends Fragment {
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

    //GridView Object
    private GridView gridView;

    //ArrayList for Storing image urls and titles
    private ArrayList<String> images;
    private ArrayList<String> names;

    private static String decide = "";
    FragmentManager fm;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_user_event_view, container, false);
        ((user_home) getActivity()).setActionBarTitle("Event Display");

        eventListView = (ListView)mainView.findViewById(R.id.xEventListView1);
        gridView = (GridView)mainView.findViewById(R.id.xDocumentGridView);


        fm = getActivity().getSupportFragmentManager();
        progressBar=(ProgressBar)mainView.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        //  Intent intent = getIntent();
        //  event_id = intent.getStringExtra("selectedEvent");
        //Retrieve the value
        //String event_id = getArguments().getString("event_id");

        event_id = user_home.getEvent_id();

        //Toast.makeText(getActivity(), event_id, Toast.LENGTH_LONG).show();

        //Toast.makeText(NextActivity.this, event_id+"", Toast.LENGTH_LONG).show();

        images = new ArrayList<>();
        names = new ArrayList<>();

        //Calling the getData method
        getData();

        setHasOptionsMenu(true);
        return mainView;
    }

    private void getData() {
        //Showing a progress dialog while our app fetches the data from url
        // loading = ProgressDialog.show(getActivity(), "Please wait...", "Fetching data...", false, false);

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {

                //Toast.makeText(MainActivity.this,"hello",Toast.LENGTH_LONG).show();
                //Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                JSONArray eventsArray = null;
                JSONObject jsonObject = null;
                progressBar.setVisibility(View.GONE);

                try {

                    jsonObject = new JSONObject(response);
                    eventsArray = jsonObject.getJSONArray(JSON_ARRAY);

                    //Toast.makeText(NextActivity.this, response, Toast.LENGTH_LONG).show();

                    flags = new String[eventsArray.length()];

                    for (int i = 0; i < eventsArray.length(); i++) {
                        //Creating a json object of the current index
                        JSONObject obj;
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
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String selectedItem = parent.getItemAtPosition(position).toString();
                                user_home.setDocumentURL(selectedItem);
                                fm.beginTransaction().replace(R.id.frame_user_home,new user_document_display()).addToBackStack(null).commit();
                            }
                        });
                    } else {
                        //loading = ProgressDialog.show(MainActivity.this, "Please wait...", "Fetching data...", false, false);
                        //Toast.makeText(NextActivity.this, "hello event", Toast.LENGTH_LONG).show();

                        //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        jsonObject = new JSONObject(response);
                        eventsArray = jsonObject.getJSONArray(JSON_ARRAY);

                        //Toast.makeText(NextActivity.this, response, Toast.LENGTH_LONG).show();

                        eventnames = new String[eventsArray.length()];
                        event_ids = new String[eventsArray.length()];

                        for (int i = 0; i < eventsArray.length(); i++) {
                            //Creating a json object of the current index
                            JSONObject obj;
                            try {
                                //getting json object from current index
                                obj = eventsArray.getJSONObject(i);
                                event_ids[i] = obj.getString(KEY_EVENT_ID);
                                eventnames[i] = obj.getString(KEY_NAME);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //Toast.makeText(getApplicationContext(), eventnames.length+"", Toast.LENGTH_LONG).show();

                        adapter = new ArrayAdapter<String>(((user_home)getActivity()), android.R.layout.simple_list_item_1, eventnames);

                        eventListView.setAdapter(adapter);

                        //Toast.makeText(MainActivity.this, "hello", Toast.LENGTH_SHORT).show();
                        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                int itemPosition = position;
                                //String event_id = event_ids[itemPosition];
                                //String  itemValue    = (String) eventListView.getItemAtPosition(position);

                                String event_id_this = event_ids[itemPosition].toString();
                                //Toast.makeText(NextActivity.this, event_id, Toast.LENGTH_LONG).show();

                                user_home.setEvent_id(event_id_this);
                                fm.beginTransaction().replace(R.id.frame_user_home, new user_event_view()).addToBackStack(null).commit();
                            }

                        });
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        //Toast.makeText(getActivity(), user_home.getUser_id()+"", Toast.LENGTH_SHORT).show();
        DocumentDisplayRequest Request = new DocumentDisplayRequest(event_id, user_home.getUser_id(), responseListener);
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
