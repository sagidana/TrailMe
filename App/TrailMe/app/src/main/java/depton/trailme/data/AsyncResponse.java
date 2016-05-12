package depton.trailme.data;

import java.util.LinkedHashMap;

/**
 * Created by Yotam on 5/7/2016.
 */
public interface AsyncResponse {
    public void processFinish(LinkedHashMap<String,String>[] output);
}
