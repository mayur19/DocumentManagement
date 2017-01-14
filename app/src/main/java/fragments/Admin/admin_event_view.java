package fragments.Admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ContextMenu;
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
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;

import Request.Admin.DeleteFileRequest;
import Request.Admin.DeleteRequest;
import Request.Admin.DocumentDisplayRequest;
import Request.GridViewAdapter;

/**
 * Created by mayur on 13-12-2016.
 */

public class admin_event_view extends Fragment {
    public static final String JSON_ARRAY = "result";
    public static final String KEY_FLAG = "flag";

    public static final String KEY_EVENT_ID = "event_id";
    public static final String KEY_NAME = "eventname";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_TIME = "time";

    public String[] event_ids;
    public String[] eventnames;
    public String[] eventdescriptions;
    public String[] event_time;

    public String[] flags;

    ListView eventListView;
    ArrayAdapter<String> adapter;

    public static final String TAG_FILE_ID = "file_id";
    public static final String TAG_IMAGE_URL = "url";
    public static final String TAG_NAME = "displayname";
    public static final String TAG_DESCRIPTION = "description";

    public String[] filedescriptions;
    public String[] fileids;

    public static String event_id ="";
    public static String admin_id ="";

    //GridView Object
    private GridView gridView;

    //ArrayList for Storing image urls and titles
    private ArrayList<String> images;
    private ArrayList<String> names;

    private static String decide = "";
    private ProgressDialog loading;
    FragmentManager fm;
    ProgressBar progressBar;
    TextView destv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_admin_event_view, container, false);
        ((admin_home) getActivity()).setActionBarTitle("Event Display");

        eventListView = (ListView)mainView.findViewById(R.id.xEventListView1);
        gridView = (GridView)mainView.findViewById(R.id.xDocumentGridView);


        fm = getActivity().getSupportFragmentManager();
        progressBar=(ProgressBar)mainView.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        //  Intent intent = getIntent();
        //  event_id = intent.getStringExtra("selectedEvent");
        //Retrieve the value
        //String event_id = getArguments().getString("event_id");

        event_id = admin_home.getEvent_id();
        admin_id = admin_home.getAdmin_id();
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
               // Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                JSONArray eventsArray = null;
                JSONObject jsonObject = null;
                progressBar.setVisibility(View.GONE);

                try {

                    jsonObject = new JSONObject(response);
                    eventsArray = jsonObject.getJSONArray(JSON_ARRAY);

                    //Toast.makeText(NextActivity.this, response, Toast.LENGTH_LONG).show();

                    //Dismissing the progressdialog on response
                   // loading.dismiss();

                    flags = new String[eventsArray.length()];
                    filedescriptions = new String[eventsArray.length()];
                    fileids = new String[eventsArray.length()];
                    event_time = new String[eventsArray.length()];
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
                                fileids[i] = obj.getString(TAG_FILE_ID);
                                images.add(obj.getString(TAG_IMAGE_URL));
                                names.add(obj.getString(TAG_NAME));
                                filedescriptions[i] = obj.getString(TAG_DESCRIPTION);
                                event_time[i] = obj.getString(KEY_TIME);
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
                        registerForContextMenu(gridView);

                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String selectedItem = parent.getItemAtPosition(position).toString();
                                admin_home.Description = filedescriptions[position];

                                admin_home.setDocumentURL(selectedItem);
                                fm.beginTransaction().replace(R.id.frame_admin_home,new admin_document_display()).addToBackStack(null).commit();
                            }
                        });
                    } else {
                        //loading = ProgressDialog.show(MainActivity.this, "Please wait...", "Fetching data...", false, false);
                        //Toast.makeText(NextActivity.this, "hello event", Toast.LENGTH_LONG).show();

                        //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        jsonObject = new JSONObject(response);
                        eventsArray = jsonObject.getJSONArray(JSON_ARRAY);


                        //Dismissing the progressdialog on response
                       // loading.dismiss();

                        //Toast.makeText(getActivity(), eventsArray.length()+"", Toast.LENGTH_LONG).show();

                        eventnames = new String[eventsArray.length()];
                        event_ids = new String[eventsArray.length()];
                        eventdescriptions = new String[eventsArray.length()];
                        event_time = new String[eventsArray.length()];
                        for (int i = 0; i < eventsArray.length(); i++) {
                            //Creating a json object of the current index
                            JSONObject obj;
                            try {
                                //getting json object from current index
                                obj = eventsArray.getJSONObject(i);
                                event_ids[i] = obj.getString(KEY_EVENT_ID);
                                eventnames[i] = obj.getString(KEY_NAME);
                                eventdescriptions[i] = obj.getString(KEY_DESCRIPTION);
                                event_time[i] = obj.getString(KEY_TIME);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //Toast.makeText(getApplicationContext(), eventnames.length+"", Toast.LENGTH_LONG).show();
                        registerForContextMenu(eventListView);
                        eventListView.setAdapter(new ArrayAdapter<String>(
                                getActivity(), R.layout.view_row,
                                R.id.Itemname,eventnames) {
                            @Override
                            public View getView(final int position, View convertView, ViewGroup parent) {
                                View inflatedView = super.getView(position, convertView, parent);

                                destv = (TextView)inflatedView.findViewById(R.id.description);
                                destv.setText(eventdescriptions[position]);

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
                                //Toast.makeText(NextActivity.this, event_id, Toast.LENGTH_LONG).show();

                                admin_home.setEvent_id(event_id_this);
                                fm.beginTransaction().replace(R.id.frame_admin_home, new admin_event_view()).addToBackStack(null).commit();
                            }

                        });
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

        DocumentDisplayRequest Request = new DocumentDisplayRequest(event_id, admin_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(Request);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId()==R.id.xEventListView1 || v.getId()==R.id.xDocumentGridView) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle("Select action");

            for (int i = 0; i < admin_home.contextMenu.size(); i++) {
                menu.add(Menu.NONE, i, i, admin_home.contextMenu.get(i));
            }
        }
    }
    String eventid="";
    String fileid="";
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String menuItemName = admin_home.contextMenu.get(menuItemIndex);

        if (decide.equalsIgnoreCase("events")) {
            admin_home.Name = eventnames[info.position];
            admin_home.Description = eventdescriptions[info.position];
            eventid = event_ids[info.position];
            admin_home.Flag = "event";

            if(menuItemName.equalsIgnoreCase("Delete")){
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                // Toast.makeText(getActivity(), "yes : "+eventid, Toast.LENGTH_SHORT).show();

                                Response.Listener<String> responseListener = new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {
                                        fm.beginTransaction().replace(R.id.frame_admin_home, new admin_event_view()).addToBackStack(null).commit();
                                    }
                                };

                                DeleteRequest Request = new DeleteRequest(eventid, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(getActivity());
                                queue.add(Request);

                            }
                        })
                        .create()
                        .show();
            } else if (menuItemName.equalsIgnoreCase("Share")){
                admin_home.setEvent_id(event_ids[info.position]);
                fm.beginTransaction().replace(R.id.frame_admin_home, new SharedFragment()).addToBackStack(null).commit();
            }
        }
        else if (decide.equalsIgnoreCase("documents")) {
            admin_home.Name = names.get(info.position);
            admin_home.Description = filedescriptions[info.position];
            admin_home.Flag = "document";
            admin_home.time = event_time[info.position];
            fileid = fileids[info.position];

            if(menuItemName.equalsIgnoreCase("Delete")){
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //Toast.makeText(getActivity(), "yes : "+fileid, Toast.LENGTH_SHORT).show();

                                Response.Listener<String> responseListener = new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {
                                        fm.beginTransaction().replace(R.id.frame_admin_home, new admin_event_view()).addToBackStack(null).commit();
                                    }
                                };

                                DeleteFileRequest Request = new DeleteFileRequest(fileid, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(getActivity());
                                queue.add(Request);

                            }
                        })
                        .create()
                        .show();
            } else if (menuItemName.equalsIgnoreCase("Share")){
                admin_home.setFile_id(fileids[info.position]);
                fm.beginTransaction().replace(R.id.frame_admin_home, new SharedFragment()).addToBackStack(null).commit();
            }
        }
        if (menuItemName.equalsIgnoreCase("Details")){
            fm.beginTransaction().replace(R.id.frame_admin_home, new DetailsFragment()).addToBackStack(null).commit();
        }

        // Toast.makeText(getActivity(), "Selected "+ menuItemName+" for item " + listItemName, Toast.LENGTH_LONG).show();
        return true;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.event_view_menu, menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {

            /** EDIT **/
            case R.id.logout:
                LocalDbHelper mydb = new LocalDbHelper(getActivity());
                mydb.executeQuery("root","root",0);
                Intent intent = new Intent(getActivity(),SplashEntry.class);
                getActivity().startActivity(intent);

            case R.id.search:
                fm.beginTransaction().replace(R.id.frame_admin_home,new OnSearchFragment()).addToBackStack(null).commit();
                return true;

            case R.id.upload:
                fm.beginTransaction().replace(R.id.frame_admin_home,new OnUploadFragment()).addToBackStack(null).commit();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }//end switch
    }

}
