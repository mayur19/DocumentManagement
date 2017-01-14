package Request.Admin;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mayur on 15-12-2016.
 */

public class AddEventRequest extends StringRequest {

    private static final String ADD_EVENT_REQUEST_URL = "http://lambelltech.com/dms/android/addEvent.php";
    private Map<String,String> params;

    public AddEventRequest(String eventname, String description,String parent_id, Response.Listener<String> listener){
        super(Method.POST,ADD_EVENT_REQUEST_URL,listener,null);

        params = new HashMap<>();
        params.put("eventname", eventname);
        params.put("description", description);
        params.put("parent_id", parent_id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

}
