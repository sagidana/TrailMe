package depton.trailme.data;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

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
import depton.trailme.models.Track;

/**
 * Created by Yotam on 5/6/2016.
 */
public class RESTCaller extends AsyncTask<String, Void, LinkedHashMap<String,String>[]>  {
    public AsyncResponse delegate = null;

        @Override
    protected LinkedHashMap<String,String>[] doInBackground(String... params) {

            LinkedHashMap<String,String>[] lhm = null;
            try {
            final String url = params[0];
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            //Track[] jsonResponse = restTemplate.getForObject(url, Track[].class);

            ResponseEntity<Object[]> responseEntity = restTemplate.getForEntity("http://trailmedev.cloudapp.net:9100/tracks", Object[].class);
            Object[] objects = responseEntity.getBody();
            MediaType contentType = responseEntity.getHeaders().getContentType();
            HttpStatus statusCode = responseEntity.getStatusCode();


            lhm = new LinkedHashMap[objects.length];

            for(int i=0; i<objects.length; i++)
            {
                lhm[i]=(LinkedHashMap)objects[i];
            }

            return lhm;
        } catch (Exception e) {
            Log.e("RestCaller", e.getMessage(), e);
        }

        return lhm;
    }

    @Override
    protected void onPostExecute(LinkedHashMap<String,String>[] jsonResponse) {
        delegate.processFinish(jsonResponse);
    }
}
