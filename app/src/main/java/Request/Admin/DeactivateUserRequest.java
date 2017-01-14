package Request.Admin;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aishwarya on 11-Jan-17.
 */
public class DeactivateUserRequest extends StringRequest {

    private static final String SEARCH_REQUEST_URL = "http://lambelltech.com/project/android/deactivateUser.php";
    private Map<String,String> params;

    public DeactivateUserRequest(String user_id, Response.Listener<String> listener){
        super(Method.POST,SEARCH_REQUEST_URL,listener,null);

        params = new HashMap<>();
        params.put("user_id", user_id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

}
