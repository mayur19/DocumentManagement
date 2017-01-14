package Request.Admin;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mayur on 15-12-2016.
 */

public class AssignEventRequest extends StringRequest {

    private static final String ASSIGN_EVENT_REQUEST_URL = "http://lambelltech.com/project/android/assignEvent.php";
    private Map<String,String> params;

    public AssignEventRequest(String user_id, String event_id, Response.Listener<String> listener){
        super(Request.Method.POST,ASSIGN_EVENT_REQUEST_URL,listener,null);

        params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("event_id", event_id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}

