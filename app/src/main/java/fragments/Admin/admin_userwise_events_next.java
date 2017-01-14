package fragments.Admin;

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

import Request.Admin.UserwiseEventRequest;

/**
 * Created by mayur on 22-12-2016.
 */

public class admin_userwise_events_next extends Fragment {


    public static final String JSON_ARRAY = "result";

    public static final String KEY_EVENT_ID = "event_id";
    public static final String KEY_NAME = "eventname";

    public String[] event_ids;
    public String[] eventnames;

    ListView eventListView;
    ArrayAdapter<String> adapter;
    FragmentManager fm;

    public static String event_id ="";
    public static String admin_id ="";
    public static String user_id ="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_admin_userwise_events_view, container, false);
        ((admin_home) getActivity()).setActionBarTitle("Userwise Next Display");
        setHasOptionsMenu(true);

        eventListView = (ListView)mainView.findViewById(R.id.xEventListView);


        user_id = admin_home.getUser_id();
        //Toast.makeText(getActivity(),user_id,Toast.LENGTH_LONG).show();

        fm = getActivity().getSupportFragmentManager();

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {

                //Toast.makeText(getActivity(),"hello",Toast.LENGTH_LONG).show();
                //Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                JSONArray eventsArray = null;
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response);
                    eventsArray = jsonObject.getJSONArray(JSON_ARRAY);

                    eventnames = new String[eventsArray.length()];
                    event_ids = new String[eventsArray.length()];

                    for (int i = 0; i < eventsArray.length(); i++) {
                        JSONObject jo = eventsArray.getJSONObject(i);
                        event_ids[i] = jo.getString(KEY_EVENT_ID);
                        eventnames[i] = jo.getString(KEY_NAME);
                    }

                    //String temp = event_ids.length +"";
                    //Toast.makeText(MainActivity.this,temp , Toast.LENGTH_LONG).show();

                    adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, eventnames);

                    eventListView.setAdapter(adapter);

                    //Toast.makeText(MainActivity.this, "hello", Toast.LENGTH_SHORT).show();
                    eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            int itemPosition     = position;
                            //String event_id = event_ids[itemPosition];
                            //String  itemValue    = (String) eventListView.getItemAtPosition(position);

                            String event_id_this = event_ids[itemPosition].toString();
                            //Toast.makeText(MainActivity.this, event_id , Toast.LENGTH_LONG).show();
                            //Toast.makeText(MainActivity.this, event_ids[1] , Toast.LENGTH_LONG).show();

                            admin_home.setEvent_id(event_id_this);
                            fm.beginTransaction().replace(R.id.frame_admin_home, new admin_userwise_events_document_display()).addToBackStack(null).commit();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        UserwiseEventRequest Request = new UserwiseEventRequest(user_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(Request);


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
