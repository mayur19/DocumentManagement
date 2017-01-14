package fragments.Admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
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

import java.util.ArrayList;

import Request.Admin.SearchRequest;
import Request.GridViewAdapter;


/**
 * Created by aishwarya on 05-Jan-17.
 */
public class OnSearchFragment extends Fragment {

    public static final String JSON_ARRAY = "result";

    public static final String KEY_FILE_ID = "file_id";
    public static final String KEY_FILE_NAME = "filename";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_URL = "url";

    public String[] file_ids;
    public String[] filenames;
    public String[] descriptions;
    public String[] urls;

    //ArrayList for Storing image urls and titles
    private ArrayList<String> images;
    private ArrayList<String> names;

    private ProgressDialog loading;
    FragmentManager fm;

    EditText searchEditText;
    View mainView;
    Button search;

    //GridView Object
    private GridView searchGridView;
    String searchText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_on_search, container, false);
        ((admin_home) getActivity()).setActionBarTitle("On Search");

        fm = getActivity().getSupportFragmentManager();
        //Toast.makeText(getActivity(), MainActivity.getEvent_id()+"", Toast.LENGTH_SHORT).show();
        searchEditText = (EditText)mainView.findViewById(R.id.xSearchEditText);
        searchGridView =  (GridView) mainView.findViewById(R.id.xSearchGridView);
        search = (Button)mainView.findViewById(R.id.btn_search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchText = searchEditText.getText().toString();
                Toast.makeText(getActivity(), searchText, Toast.LENGTH_SHORT).show();

                images = new ArrayList<>();
                names = new ArrayList<>();

                //Calling the getData method
                getData();

            }
        });

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
                JSONArray filesArray = null;
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response);
                    filesArray = jsonObject.getJSONArray(JSON_ARRAY);

                    //Toast.makeText(NextActivity.this, response, Toast.LENGTH_LONG).show();

                    //Dismissing the progressdialog on response
                     loading.dismiss();

                    for (int i = 0; i < filesArray.length(); i++) {
                        //Creating a json object of the current index
                        JSONObject obj = null;
                        try {
                            //getting json object from current index
                            obj = filesArray.getJSONObject(i);
                            images.add(obj.getString(KEY_URL));
                            names.add(obj.getString(KEY_FILE_NAME));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    //decide = flags[0].toString();
                    //Toast.makeText(NextActivity.this, temp , Toast.LENGTH_LONG).show();


                        //Creating GridViewAdapter Object
                        GridViewAdapter gridViewAdapter = new GridViewAdapter(getActivity(), images, names);

                        //Adding adapter to gridview
                        searchGridView.setAdapter(gridViewAdapter);

                        //set on item click
                        searchGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                // Get the GridView selected/clicked item text
                                String selectedItem = parent.getItemAtPosition(position).toString();

                                // Display the selected/clicked item text and position on TextView
                                //Toast.makeText(getActivity(), "GridView item clicked : " +selectedItem + "\nAt index position : " + position, Toast.LENGTH_LONG).show();

                                admin_home.setDocumentURL(selectedItem);
                                fm.beginTransaction().replace(R.id.frame_admin_home, new admin_document_display()).addToBackStack(null).commit();
                            }
                        });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        SearchRequest Request = new SearchRequest(admin_home.getAdmin_id(),admin_home.getEvent_id(),searchText, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(Request);
    }
}
