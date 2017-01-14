package Request.Admin;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by mayur on 14-12-2016.
 */

public class DisplayUsersRequest extends StringRequest {

    private static final String DISPLAY_REQUEST_URL = "http://lambelltech.com/project/android/displayUsers.php";

    public DisplayUsersRequest( Response.Listener<String> listener){
        super(Method.POST,DISPLAY_REQUEST_URL,listener,null);
    }
}

