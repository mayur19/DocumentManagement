package Request.Admin;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aishwarya on 05-Jan-17.
 */
public class SearchRequest extends StringRequest {

    private static final String SEARCH_REQUEST_URL = "http://lambelltech.com/project/android/search.php";
    private Map<String,String> params;

    public SearchRequest(String admin_id,String event_id,String key, Response.Listener<String> listener){
        super(Method.POST,SEARCH_REQUEST_URL,listener,null);

        params = new HashMap<>();
        params.put("admin_id", admin_id);
        params.put("event_id", event_id);
        params.put("key", key);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

}
