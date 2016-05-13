package depton.trailme.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import depton.net.trailme.R;
import depton.trailme.DAL.TrailMeServer;
import depton.trailme.models.Track;

/**
 * Created by Yotam on 5/6/2016.
 */
public class RestCaller extends AsyncTask<Object, Void, JSONArray>  {
    public AsyncResponse delegate = null;

        @Override
    protected JSONArray doInBackground(Object... params) {

        Context ctx = (Context)params[0];

        switch ((String)params[1])
        {
            case ("tracks"):
            {
                return TrailMeServer.getInstance(ctx).getTracks();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONArray jsonResponse) {
        delegate.processFinish(jsonResponse);
    }
}
