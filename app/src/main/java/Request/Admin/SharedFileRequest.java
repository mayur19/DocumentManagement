package Request.Admin;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aishwarya on 08-Jan-17.
 */
public class SharedFileRequest  extends StringRequest {

    private static final String SEARCH_REQUEST_URL = "http://lambelltech.com/project/android/shareFile.php";
    private Map<String,String> params;

    public SharedFileRequest(String sender_id, String user_id, String file_id, Response.Listener<String> listener){
        super(Method.POST,SEARCH_REQUEST_URL,listener,null);

        params = new HashMap<>();
        params.put("sender_id", sender_id);
        params.put("user_id", user_id);
        params.put("file_id", file_id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

}