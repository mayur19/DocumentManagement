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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import com.lambelltech.mayur.dms_master.MainActivity;
import com.lambelltech.mayur.dms_master.R;
import com.lambelltech.mayur.dms_master.user_home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Request.User.OnSharedRequest;

/**
 * Created by aishwarya on 08-Jan-17.
 */
public class OnSharedFragment extends Fragment {
    public static final String JSON_ARRAY = "result";

    public static final String KEY_USER_ID = "sender_id";
    public static final String KEY_FULLNAME = "fullname";

    public static ArrayList<String> sender_ids;
    public static ArrayList<String> fullnames;

    public static String admin_id;
    public static String event_id;

    ListView userListView;
    ArrayAdapter<String> adapter;
    FragmentManager fm;
    ProgressBar progressBar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_user_events, container, false);
        ((user_home) getActivity()).setActionBarTitle("Userwise Shared Events");
        setHasOptionsMenu(true);

        userListView = (ListView)mainView.findViewById(R.id.xEventListView);

        fm = getActivity().getSupportFragmentManager();
        progressBar=(ProgressBar)mainView.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {

                //Toast.makeText(getActivity(),"hello",Toast.LENGTH_LONG).show();

                JSONArray usersArray = null;
                JSONObject jsonObject = null;
                progressBar.setVisibility(View.GONE);


                try {

                    jsonObject = new JSONObject(response);
                    usersArray = jsonObject.getJSONArray(JSON_ARRAY);

                    sender_ids = new ArrayList<String>();
                    fullnames = new ArrayList<String>();

                    int count = 0;
                    for (int i = 0; i < usersArray.length(); i++) {
                        JSONObject jo = usersArray.getJSONObject(i);
                        if (count==0){
                            sender_ids.add(jo.getString(KEY_USER_ID));
                            fullnames.add(jo.getString(KEY_FULLNAME));
                            count++;
                        }else {
                            if (!sender_ids.contains(jo.getString(KEY_USER_ID))){
                                sender_ids.add(jo.getString(KEY_USER_ID));
                                fullnames.add(jo.getString(KEY_FULLNAME));
                            }
                        }
                    }

                    //Toast.makeText(getActivity(), fullnames.get(0), Toast.LENGTH_LONG).show();
                    //String temp = event_ids.length +"";
                    //Toast.makeText(MainActivity.this,temp , Toast.LENGTH_LONG).show();

                    adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, fullnames);

                    userListView.setAdapter(adapter);

                    //Toast.makeText(MainActivity.this, "hello", Toast.LENGTH_SHORT).show();
                    userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            int itemPosition     = position;
                            //String event_id = event_ids[itemPosition];
                            //String  itemValue    = (String) eventListView.getItemAtPosition(position);

                            String user_id_this = sender_ids.get(itemPosition);
                            //Toast.makeText(MainActivity.this, event_id , Toast.LENGTH_LONG).show();
                            //Toast.makeText(MainActivity.this, event_ids[1] , Toast.LENGTH_LONG).show();

                            user_home.setShare_user_id(user_id_this);
                            fm.beginTransaction().replace(R.id.frame_user_home, new OnSharedNextFragment()).addToBackStack(null).commit();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        OnSharedRequest Request = new OnSharedRequest(user_home.getUser_id(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(Request);

        //setHasOptionsMenu(true);
        return mainView;
    }
}