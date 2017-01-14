package Request.Admin;

import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.lambelltech.mayur.dms_master.R;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

/**
 * Created by mayur on 24-10-2016.
 */

public class LoginRequest extends StringRequest {
    // login url
    private static final String LOGIN_REQUEST_URL = "http://lambelltech.com/project/android/androidLogin.php";
    private Map<String,String> params;

    public LoginRequest(String username,String password,Response.Listener<String> listener){

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
