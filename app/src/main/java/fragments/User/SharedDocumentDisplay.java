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

import Request.GridViewAdapter;
import Request.User.SharedDocumentDisplayRequest;


/**
 * Created by aishwarya on 08-Jan-17.
 */
public class SharedDocumentDisplay extends Fragment {

    public static final String JSON_ARRAY = "result";

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

    private ProgressDialog loading;
    FragmentManager fm, fm1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_shared_document_display, container, false);
        ((user_home) getActivity()).setActionBarTitle("Shared Document Display");

        fm = getActivity().getSupportFragmentManager();
        fm1 = getActivity().getSupportFragmentManager();

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

                    filedescriptions = new String[eventsArray.length()];
                    fileids = new String[eventsArray.length()];

                    for (int i = 0; i < eventsArray.length(); i++) {
                        //Creating a json object of the current index
                        JSONObject obj = null;
                        try {
                            //getting json object from current index
                            obj = eventsArray.getJSONObject(i);
                                fileids[i] = obj.getString(TAG_FILE_ID);
                                images.add(obj.getString(TAG_IMAGE_URL));
                                names.add(obj.getString(TAG_NAME));
                                filedescriptions[i] = obj.getString(TAG_DESCRIPTION);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    //decide = flags[0].toString();
                    //Toast.makeText(NextActivity.this, temp , Toast.LENGTH_LONG).show();


                    //Creating GridViewAdapter Object
                    GridViewAdapter gridViewAdapter = new GridViewAdapter(getActivity(), images, names);

                    //Adding adapter to gridview
                    gridView.setAdapter(gridViewAdapter);
                    registerForContextMenu(gridView);
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        SharedDocumentDisplayRequest Request = new SharedDocumentDisplayRequest(user_home.getUser_id(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(Request);
    }

}
