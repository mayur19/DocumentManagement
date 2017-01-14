package fragments.Admin;

import android.app.AlertDialog;
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
import android.widget.ListView;
import android.widget.ProgressBar;

import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.lambelltech.mayur.dms_master.LocalDbHelper;

import com.lambelltech.mayur.dms_master.R;
import com.lambelltech.mayur.dms_master.SplashEntry;
import com.lambelltech.mayur.dms_master.admin_home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import Request.Admin.EventRequest;

/**
 * Created by mayur on 13-12-2016.
 */

public class admin_list_events extends Fragment {

    public static final String JSON_ARRAY = "result";
    public static final String KEY_EVENT_ID = "event_id";
    public static final String KEY_NAME = "eventname";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_TIME = "time";

    public String[] names;
    public String[] description;
    public String[] event_ids;
    public String[] event_time;

    public static String admin_id;
    public static String event_id;

    private static String decide = "";
    ListView eventListView;
    ArrayAdapter<String> adapter;
    FragmentManager fm;
    ProgressBar progressBar;
    ArrayList<String> myList = new ArrayList<String>();
    TextView destv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_admin_events, container, false);
        ((admin_home) getActivity()).setActionBarTitle("Events");

        eventListView = (ListView)mainView.findViewById(R.id.xEventListView);
        fm = getActivity().getSupportFragmentManager();
        progressBar=(ProgressBar)mainView.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {

                //Toast.makeText(getActivity(),"hello",Toast.LENGTH_LONG).show();
                //Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                JSONArray eventsArray = null;
                JSONObject jsonObject = null;
                progressBar.setVisibility(View.GONE);

                try {

                    jsonObject = new JSONObject(response
                    );
                    eventsArray = jsonObject.getJSONArray(JSON_ARRAY);

                    names = new String[eventsArray.length()];
                    description = new String[eventsArray.length()];
                    event_ids = new String[eventsArray.length()];
                    event_time = new String[eventsArray.length()];
                    for (int i = 0; i < eventsArray.length(); i++) {
                        JSONObject jo = eventsArray.getJSONObject(i);
                        event_ids[i] = jo.getString(KEY_EVENT_ID);
                        names[i] = jo.getString(KEY_NAME);
                        description[i]= jo.getString(KEY_DESCRIPTION);
                        event_time[i] = jo.getString(KEY_TIME);
                        //myList.addAll(Arrays.asList(new String[] {names[i],description[i]}));
                    }


                    //String temp = event_ids.length +"";
                    //Toast.makeText(MainActivity.this,temp , Toast.LENGTH_LONG).show();
                    registerForContextMenu(eventListView);
                    eventListView.setAdapter(new ArrayAdapter<String>(
                            getActivity(), R.layout.view_row,
                            R.id.Itemname,names) {
                        @Override
                        public View getView(final int position, View convertView, ViewGroup parent) {
                            View inflatedView = super.getView(position, convertView, parent);

                            destv = (TextView)inflatedView.findViewById(R.id.description);
                            destv.setText(description[position]);

                            return inflatedView;

                        }
                    });

                    //Toast.makeText(MainActivity.this, "hello", Toast.LENGTH_SHORT).show();
                    eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            int itemPosition = position;


                            //String event_id = event_ids[itemPosition];
                            //String  itemValue    = (String) eventListView.getItemAtPosition(position);

                            String event_id_this = event_ids[itemPosition].toString();
                            //Toast.makeText(getActivity(), event_id_this , Toast.LENGTH_LONG).show();
                            //Toast.makeText(MainActivity.this, event_ids[1] , Toast.LENGTH_LONG).show();

                            admin_home.setEvent_id(event_id_this);
                            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                            builder.setMessage(description[position])
                                    .setNegativeButton("Back", null).create().show();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        EventRequest Request = new EventRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(Request);

        setHasOptionsMenu(true);

        return mainView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_admin_events, menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        FragmentManager fm = getActivity().getSupportFragmentManager();
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

            case R.id.add_event:
                fm.beginTransaction().replace(R.id.frame_admin_home, new admin_add_event()).addToBackStack(null).commit();

            case R.id.delete_event:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }//end switch
    }
}
