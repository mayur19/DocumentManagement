package fragments.User;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.lambelltech.mayur.dms_master.MainActivity;
import com.lambelltech.mayur.dms_master.R;
import com.lambelltech.mayur.dms_master.user_home;

import Request.GridViewAdapter;
import Request.User.DocumentDisplayRequest;

/**
 * Created by aishwarya on 08-Jan-17.
 */
public class SharedEvents  extends Fragment {

    public static final String JSON_ARRAY = "result";
    public static final String KEY_FLAG = "flag";

    public static final String KEY_EVENT_ID = "event_id";
    public static final String KEY_NAME = "eventname";
    public static final String KEY_DESCRIPTION = "description";

    public String[] event_ids;
    public String[] eventnames;
    public String[] eventdescriptions;
    public String[] flags;

    ListView eventListView;
    ArrayAdapter<String> adapter;

    public static final String TAG_FILE_ID = "file_id";
    public static final String TAG_IMAGE_URL = "url";
    public static final String TAG_NAME = "displayname";
    public static final String TAG_DESCRIPTION = "description";

    public String[] filedescriptions;
    public String[] fileids;
    public static String event_id = "";
    public static String user_id = "";

    //GridView Object
    private GridView gridView;

    //ArrayList for Storing image urls and titles
    private ArrayList<String> images;
    private ArrayList<String> names;

    private static String decide = "";
    private ProgressDialog loading;
    FragmentManager fm, fm1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_user_event_view, container, false);
        ((user_home) getActivity()).setActionBarTitle("Shared Events");

        fm = getActivity().getSupportFragmentManager();
        fm1 = getActivity().getSupportFragmentManager();

        eventListView = (ListView) mainView.findViewById(R.id.xEventListView1);
        gridView = (GridView) mainView.findViewById(R.id.xDocumentGridView);

        //  Intent intent = getIntent();
        //  event_id = intent.getStringExtra("selectedEvent");
        //Retrieve the value
        //String event_id = getArguments().getString("event_id");

        event_id = user_home.getEvent_id();
        user_id = user_home.getUser_id();
        //Toast.makeText(getActivity(), event_id+" event_id", Toast.LENGTH_SHORT).show();

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
        loading = ProgressDialog.show(getActivity(), "Please wait...", "Fetching data...", false, false);

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {

                //Toast.makeText(MainActivity.this,"hello",Toast.LENGTH_LONG).show();
                //Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                JSONArray eventsArray = null;
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response);
                    eventsArray = jsonObject.getJSONArray(JSON_ARRAY);

                    //Toast.makeText(NextActivity.this, response, Toast.LENGTH_LONG).show();

                    //Dismissing the progressdialog on response
                    loading.dismiss();

                    flags = new String[eventsArray.length()];
                    filedescriptions = new String[eventsArray.length()];
                    fileids = new String[eventsArray.length()];

                    for (int i = 0; i < eventsArray.length(); i++) {
                        //Creating a json object of the current index
                        JSONObject obj = null;
                        try {
                            //getting json object from current index
                            obj = eventsArray.getJSONObject(i);
                            flags[i] = obj.getString(KEY_FLAG);
                            if (flags[i].equalsIgnoreCase("events")) {
                                decide = "events";
                            } else if (flags[i].equalsIgnoreCase("documents")) {
                                decide = "documents";
                                fileids[i] = obj.getString(TAG_FILE_ID);
                                images.add(obj.getString(TAG_IMAGE_URL));
                                names.add(obj.getString(TAG_NAME));
                                filedescriptions[i] = obj.getString(TAG_DESCRIPTION);
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

                        //set on item click
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                // Get the GridView selected/clicked item text
                                String selectedItem = parent.getItemAtPosition(position).toString();
                                user_home.Description = filedescriptions[position];

                                // Display the selected/clicked item text and position on TextView
                                //Toast.makeText(getActivity(), "GridView item clicked : " +selectedItem + "\nAt index position : " + position, Toast.LENGTH_LONG).show();

                                user_home.setDocumentURL(selectedItem);
                                fm1.beginTransaction().replace(R.id.frame_user_home, new user_document_display()).addToBackStack(null).commit();
                            }
                        });
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
                        eventdescriptions = new String[eventsArray.length()];

                        for (int i = 0; i < eventsArray.length(); i++) {
                            //Creating a json object of the current index
                            JSONObject obj = null;
                            try {
                                //getting json object from current index
                                obj = eventsArray.getJSONObject(i);
                                event_ids[i] = obj.getString(KEY_EVENT_ID);
                                eventnames[i] = obj.getString(KEY_NAME);
                                eventdescriptions[i] = obj.getString(KEY_DESCRIPTION);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //Toast.makeText(getApplicationContext(), eventnames.length+"", Toast.LENGTH_LONG).show();

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

                                user_home.setEvent_id(event_id_this);
                                fm.beginTransaction().replace(R.id.frame_user_home, new SharedEvents()).addToBackStack(null).commit();
                            }

                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        DocumentDisplayRequest Request = new DocumentDisplayRequest(event_id, user_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(Request);
    }

}
