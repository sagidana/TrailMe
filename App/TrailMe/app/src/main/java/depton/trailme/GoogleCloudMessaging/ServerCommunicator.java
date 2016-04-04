package depton.trailme.GoogleCloudMessaging;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerCommunicator {

    static private String SERVER_URL = "http://93.172.17.32:9900/";

    static public void Send(Context context, String message, String operation)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = SERVER_URL + operation + "/";

        try
        {
            JSONObject requestBody = new JSONObject(message);
            JsonObjectRequest request = new JsonObjectRequest(
                    url, requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });

            queue.add(request);
        }
        catch (JSONException je)
        {}
    }
}
