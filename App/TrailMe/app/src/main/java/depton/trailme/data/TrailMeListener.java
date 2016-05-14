package depton.trailme.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashMap;

/**
 * Created by Yotam on 5/7/2016.
 */
public interface TrailMeListener {
    public void processFinish(JSONObject response);
}
