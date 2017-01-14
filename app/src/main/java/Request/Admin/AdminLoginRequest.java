package Request.Admin;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mayur on 26-12-2016.
 */

public class AdminLoginRequest extends StringRequest {
    // login url
    private static final String LOGIN_REQUEST_URL = "http://lambelltech.com/project/android/admin_login.php";
    private Map<String,String> params;

    public AdminLoginRequest(String username,String password,Response.Listener<String> listener){

        super(Method.POST,LOGIN_REQUEST_URL,listener,null);

        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

}
