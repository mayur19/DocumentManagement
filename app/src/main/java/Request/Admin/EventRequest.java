package Request.Admin;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by mayur on 21-12-2016.
 */

public class EventRequest extends StringRequest {

    private static final String DISPLAY_REQUEST_URL = "http://lambelltech.com/project/android/event.php";

    public EventRequest(Response.Listener<String> listener) {
        super(Method.POST, DISPLAY_REQUEST_URL, listener, null);
    }
}
