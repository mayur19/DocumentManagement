package fragments.Admin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.lambelltech.mayur.dms_master.R;
import com.lambelltech.mayur.dms_master.admin_home;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Request.Admin.ActivateUserRequest;
import Request.Admin.DeactivateUserRequest;
import Request.Admin.DisplayUsersRequest;


/**
 * Created by aishwarya on 11-Jan-17.
 */
public class ActivateUserFragment extends Fragment {

    public static final String JSON_ARRAY = "result";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_FULLNAME = "fullname";
    public static final String KEY_STATUS = "status";

    public String[] userids;
    public String[] fullnames;
    public String[] status;

    ListView list;
    Button btn;
    int itemPosition;
    FragmentManager fm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View mainView = inflater.inflate(R.layout.fragment_admin_list_user, container, false);
        ((admin_home) getActivity()).setActionBarTitle("Activate User");

        list = (ListView)mainView.findViewById(R.id.androidlist);
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

                    userids = new String[eventsArray.length()];
                    fullnames = new String[eventsArray.length()];
                    status = new String[eventsArray.length()];

                    for (int i = 0; i < eventsArray.length(); i++) {
                        JSONObject jo = eventsArray.getJSONObject(i);
                        userids[i] = jo.getString(KEY_USER_ID);
                        fullnames[i] = jo.getString(KEY_FULLNAME);
                        status[i] = jo.getString(KEY_STATUS);
                    }

                    //String temp = event_ids.length +"";
                    //Toast.makeText(MainActivity.this,temp , Toast.LENGTH_LONG).show();

                    list.setAdapter(new ArrayAdapter<String>(
                            getActivity(), R.layout.activate_user_list_view_row,
                            R.id.Itemname,fullnames){
                        @Override
                        public View getView(final int position, View convertView, ViewGroup parent) {
                            View inflatedView = super.getView(position, convertView, parent);

                            btn = (Button)inflatedView.findViewById(R.id.xActivateButton);
                            if (status[position].equalsIgnoreCase("1"))
                                btn.setText("Deactivate");
                            else
                                btn.setText("Activate");

                            // set a click listener
                            // TODO change "R.id.buttonId" to reference the ID value you set for the button's android:id attribute in foodlist.xml
                            inflatedView.findViewById(R.id.xActivateButton).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Toast.makeText(v.getContext(), "Item name : " + fullnames[position], Toast.LENGTH_SHORT).show();
                                    if (status[position].equalsIgnoreCase("1")){
                                        //Toast.makeText(getActivity(), "Deactivate", Toast.LENGTH_SHORT).show();
                                        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                                        builder.setMessage("Deactivate user "+fullnames[position]+" ?")
                                                .setNegativeButton("No",null)
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                        // Toast.makeText(getActivity(), "yes : "+eventid, Toast.LENGTH_SHORT).show();

                                                        Response.Listener<String> responseListener = new Response.Listener<String>() {

                                                            @Override
                                                            public void onResponse(String response) {
                                                                Toast.makeText(getActivity(), "User "+fullnames[position]+" deactivated", Toast.LENGTH_SHORT).show();
                                                                fm.beginTransaction().replace(R.id.frame_admin_home, new ActivateUserFragment()).addToBackStack(null).commit();
                                                            }
                                                        };

                                                        DeactivateUserRequest Request = new DeactivateUserRequest(userids[position], responseListener);
                                                        RequestQueue queue = Volley.newRequestQueue(getActivity());
                                                        queue.add(Request);

                                                    }
                                                })
                                                .create()
                                                .show();
                                    }else {
                                        //Toast.makeText(getActivity(), "Activate", Toast.LENGTH_SHORT).show();
                                        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                                        builder.setMessage("Activate user "+fullnames[position]+" ?")
                                                .setNegativeButton("No",null)
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                        // Toast.makeText(getActivity(), "yes : "+eventid, Toast.LENGTH_SHORT).show();

                                                        Response.Listener<String> responseListener = new Response.Listener<String>() {

                                                            @Override
                                                            public void onResponse(String response) {
                                                                Toast.makeText(getActivity(), "User "+fullnames[position]+" activated", Toast.LENGTH_SHORT).show();
                                                                fm.beginTransaction().replace(R.id.frame_admin_home, new ActivateUserFragment()).addToBackStack(null).commit();
                                                            }
                                                        };

                                                        ActivateUserRequest Request = new ActivateUserRequest(userids[position], responseListener);
                                                        RequestQueue queue = Volley.newRequestQueue(getActivity());
                                                        queue.add(Request);

                                                    }
                                                })
                                                .create()
                                                .show();

                                    }
                                }
                            });
                            return inflatedView;
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

        //setHasOptionsMenu(true);
        return mainView;
    }
}
