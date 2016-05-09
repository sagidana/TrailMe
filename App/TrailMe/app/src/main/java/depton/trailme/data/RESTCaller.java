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

import depton.net.trailme.R;
import depton.trailme.models.Track;

/**
 * Created by Yotam on 5/6/2016.
 */
public class RESTCaller extends AsyncTask<String, Void, String>  {
    public AsyncResponse delegate = null;

    @Override
    protected String doInBackground(String... params) {
        try {
            final String url = params[0];
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            //Track[] jsonResponse = restTemplate.getForObject(url, Track[].class);

            ResponseEntity<Object[]> responseEntity = restTemplate.getForEntity("http://ip.jsontest.com/", Object[].class);
            Object[] objects = responseEntity.getBody();
            MediaType contentType = responseEntity.getHeaders().getContentType();
            HttpStatus statusCode = responseEntity.getStatusCode();

            return "helloworld";
        } catch (Exception e) {
            Log.e("RestCaller", e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(String jsonResponse) {
        delegate.processFinish(jsonResponse);
    }
}
