package Request.User;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mayur on 21-12-2016.
 */

public class DocumentDisplayRequest extends StringRequest {

        private static final String DISPLAY_REQUEST_URL = "http://lambelltech.com/project/android/User/documentDisplay.php";
        private Map<String,String> params;

        public DocumentDisplayRequest(String event_id, String userid, Response.Listener<String> listener){
            super(Method.POST,DISPLAY_REQUEST_URL,listener,null);

            params = new HashMap<>();
            params.put("event_id", event_id);
            params.put("user_id", userid);

        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return params;
        }

    }
