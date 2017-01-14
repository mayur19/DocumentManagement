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
import android.widget.ProgressBar;

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

import Request.User.SharedEventsDisplayRequest;


/**
 * Created by aishwarya on 08-Jan-17.
 */
public class SharedEventsDisplay extends Fragment {

    public static final String JSON_ARRAY = "result";

    public static final String KEY_EVENT_ID = "event_id";
    public static final String KEY_NAME = "eventname";
    public static final String KEY_DESCRIPTION = "description";

    public String[] event_ids;
    public String[] eventnames;
    public String[] eventdescriptions;

    ListView eventListView;
    ArrayAdapter<String> adapter;

    public static String event_id = "";
    public static String user_id = "";

    private ProgressDialog loading;
    ProgressBar progressBar;
    FragmentManager fm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_user_events, container, false);
        ((user_home) getActivity()).setActionBarTitle("Shared Events Display");


        fm = getActivity().getSupportFragmentManager();
        progressBar=(ProgressBar)mainView.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        eventListView = (ListView) mainView.findViewById(R.id.xEventListView);

        //  Intent intent = getIntent();
        //  event_id = intent.getStringExtra("selectedEvent");
        //Retrieve the value
        //String event_id = getArguments().getString("event_id");

        event_id = user_home.getEvent_id();
        user_id = user_home.getUser_id();
        //Toast.makeText(getActivity(), event_id+" event_id", Toast.LENGTH_SHORT).show();

        //Toast.makeText(NextActivity.this, event_id+"", Toast.LENGTH_LONG).show();

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
                //Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                JSONArray eventsArray = null;
                JSONObject jsonObject = null;

                try {


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

                        //Toast.makeText(getApplicationContext(), eventnames.length+"", Toast.LENGTH_LONG).show();

                        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, eventnames);

                        eventListView.setAdapter(adapter);
                        registerForContextMenu(eventListView);
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

        SharedEventsDisplayRequest Request = new SharedEventsDisplayRequest(user_home.getUser_id(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(Request);
    }

}