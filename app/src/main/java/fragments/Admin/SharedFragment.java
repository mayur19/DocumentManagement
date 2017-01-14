package fragments.Admin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.lambelltech.mayur.dms_master.R;
import com.lambelltech.mayur.dms_master.admin_home;
import com.lambelltech.mayur.dms_master.admin_login_activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Request.Admin.DisplayUsersRequest;
import Request.Admin.SharedFileRequest;
import Request.Admin.SharedUsersRequest;

/**
 * Created by aishwarya on 08-Jan-17.
 */
public class SharedFragment extends Fragment {

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

        View mainView = inflater.inflate(R.layout.fragment_share, container, false);
        ((admin_home) getActivity()).setActionBarTitle("Shared");

        userListView = (ListView)mainView.findViewById(R.id.xUserListView);

        fm = getActivity().getSupportFragmentManager();

        Response.Listener<String> responseListener1 = new Response.Listener<String>() {

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

                    adapter = new ArrayAdapter<String>(getActivity(),R.layout.shared_fragment_listview_row,R.id.Itemname,fullnames);

                    userListView.setAdapter(adapter);



                    //Toast.makeText(MainActivity.this, "hello", Toast.LENGTH_SHORT).show();
                    userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            int itemPosition     = position;
                            //String event_id = event_ids[itemPosition];
                            //String  itemValue    = (String) eventListView.getItemAtPosition(position);

                            String user_id_this = user_ids[itemPosition].toString();
                            //Toast.makeText(MainActivity.this, event_id , Toast.LENGTH_LONG).show();
                            //Toast.makeText(MainActivity.this, event_ids[1] , Toast.LENGTH_LONG).show();
                            admin_home.setUser_id(user_id_this);
                            admin_home.setUser_id(user_id_this);

                            if (admin_home.Flag.equalsIgnoreCase("event")) {

                                Response.Listener<String> responseListener = new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(final String response) {

                                        Toast.makeText(getActivity(), "Event shared successfully.", Toast.LENGTH_SHORT).show();

                                    }
                                };

                                SharedUsersRequest Request1 = new SharedUsersRequest(admin_home.getAdmin_id(), admin_home.getUser_id(), admin_home.getEvent_id(), responseListener);
                                RequestQueue queue1 = Volley.newRequestQueue(getActivity());
                                queue1.add(Request1);
                            } else if(admin_home.Flag.equalsIgnoreCase("document")){

                                Response.Listener<String> responseListener = new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(final String response) {

                                        Toast.makeText(getActivity(), "File shared successfully.", Toast.LENGTH_SHORT).show();

                                    }
                                };

                                SharedFileRequest Request1 = new SharedFileRequest(admin_home.getAdmin_id(), admin_home.getUser_id(), admin_home.getFile_id(), responseListener);
                                RequestQueue queue1 = Volley.newRequestQueue(getActivity());
                                queue1.add(Request1);
                            }
                         }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        DisplayUsersRequest Request = new DisplayUsersRequest(responseListener1);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(Request);

        //setHasOptionsMenu(true);
        return mainView;
    }
}