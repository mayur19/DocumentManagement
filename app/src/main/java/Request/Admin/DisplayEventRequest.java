package Request.Admin;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by mayur on 14-12-2016.
 */

public class DisplayEventRequest extends StringRequest {

    private static final String DISPLAY_REQUEST_URL = "http://lambelltech.com/project/android/displayEvents.php";

    public DisplayEventRequest( Response.Listener<String> listener){
        super(Request.Method.POST,DISPLAY_REQUEST_URL,listener,null);
    }
}

