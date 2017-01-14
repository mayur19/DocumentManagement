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

import Request.Admin.DisplayUsersRequest;

/**
 * Created by mayur on 13-12-2016.
 */

public class admin_userwise_events extends Fragment {

    public static final String JSON_ARRAY = "result";

    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_FULLNAME = "fullname";

    public static String[] user_ids;
    public static String[] fullnames;

    public static String admin_id;
    public static String event_id;

    ListView userListView;
    ArrayAdapter<String> adapter;
    FragmentManager fm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_admin_userwise_events, container, false);
        ((admin_home) getActivity()).setActionBarTitle("Userwise Display Events");
        setHasOptionsMenu(true);

        userListView = (ListView)mainView.findViewById(R.id.xUserListView);

        fm = getActivity().getSupportFragmentManager();

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {

                //Toast.makeText(getActivity(),"hello",Toast.LENGTH_LONG).show();
                //Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                JSONArray usersArray = null;
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response);
                    usersArray = jsonObject.getJSONArray(JSON_ARRAY);

                    user_ids = new String[usersArray.length()];
                    fullnames = new String[usersArray.length()];

                    for (int i = 0; i < usersArray.length(); i++) {
                        JSONObject jo = usersArray.getJSONObject(i);
                        user_ids[i] = jo.getString(KEY_USER_ID);
                        fullnames[i] = jo.getString(KEY_FULLNAME);
                    }

                    //String temp = event_ids.length +"";
                    //Toast.makeText(MainActivity.this,temp , Toast.LENGTH_LONG).show();

                    adapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_dropdown ,fullnames);

                    userListView.setAdapter(adapter);

                    //Toast.makeText(MainActivity.this, "hello", Toast.LENGTH_SHORT).show();
                    userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            int itemPosition = position;
                            //String event_id = event_ids[itemPosition];
                            //String  itemValue    = (String) eventListView.getItemAtPosition(position);

                            String user_id_this = user_ids[itemPosition].toString();
                            //Toast.makeText(MainActivity.this, event_id , Toast.LENGTH_LONG).show();
                            //Toast.makeText(MainActivity.this, event_ids[1] , Toast.LENGTH_LONG).show();

                            admin_home.setUser_id(user_id_this);
                            fm.beginTransaction().replace(R.id.frame_admin_home, new admin_userwise_events_next()).addToBackStack(null).commit();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        DisplayUsersRequest Request = new DisplayUsersRequest(responseListener);
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

