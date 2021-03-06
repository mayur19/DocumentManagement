package Request.Admin;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mayur on 22-12-2016.
 */

public class UserwiseDocumentRequest extends StringRequest {

    private static final String DISPLAY_REQUEST_URL = "http://lambelltech.com/project/android/userDocumentDisplay.php";
    private Map<String,String> params;

    public UserwiseDocumentRequest( String event_id, String user_id, Response.Listener<String> listener){
        super(Request.Method.POST,DISPLAY_REQUEST_URL,listener,null);

        params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("event_id", event_id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

}
