package Request.Admin;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mayur on 15-12-2016.
 */

public class AddUserRequest extends StringRequest {

    private static final String ADD_USER_REQUEST_URL = "http://lambelltech.com/dms/android/addUser.php";
    private Map<String,String> params;

    public AddUserRequest(String fullname, String username, String password, Response.Listener<String> listener){
        super(Method.POST,ADD_USER_REQUEST_URL,listener,null);

        params = new HashMap<>();
        params.put("fullname", fullname);
        params.put("username", username);
        params.put("password", password);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}
