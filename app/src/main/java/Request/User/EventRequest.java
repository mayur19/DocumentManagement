package Request.User;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mayur on 31-12-2016.
 */

public class EventRequest extends StringRequest {
    // login url
    private static final String LOGIN_REQUEST_URL = "http://lambelltech.com/project/android/User/userEvents.php";
    private Map<String,String> params;

    public EventRequest(String user_id, Response.Listener<String> listener){

        super(Method.POST,LOGIN_REQUEST_URL,listener,null);

        params = new HashMap<>();
        params.put("user_id", user_id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

}