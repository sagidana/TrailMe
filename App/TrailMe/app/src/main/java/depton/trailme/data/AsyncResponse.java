package depton.trailme.data;

import org.json.JSONArray;

import java.util.LinkedHashMap;

/**
 * Created by Yotam on 5/7/2016.
 */
public interface AsyncResponse {
    public void processFinish(JSONArray response);
}
