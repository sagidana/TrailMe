package depton.trailme.DAL;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;


public class TrailMeServer
{
    static private String SERVER_URL = "http://trailmedev.cloudapp.net:9100/";
    private static TrailMeServer mInstance;
    private RequestQueue mRequestQueue;

    private static Context mCtx;

    private TrailMeServer(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized TrailMeServer getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new TrailMeServer(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public JSONArray getUsers()
    {
        return get("users");
    }
    public JSONArray getTracks()
    {
        return get("tracks");
    }
    public JSONArray getGroups(){ return get("groups"); }
    public JSONArray getEvents()
    {
        return get("events");
    }
    public JSONArray getCategories()
    {
        return get("categories");
    }
    public JSONArray getSkills()
    {
        return get("skills");
    }
    public JSONArray getLanguages()
    {
        return get("languages");
    }

    public void addUser(JSONObject user){put("users", user);}
    public void addGroup(JSONObject group){put("groups", group);}
    public void addEvent(JSONObject event){put("events", event);}
    public void addTrack(JSONObject track){put("tracks", track);}
    public void addCategory(JSONObject category){put("categories", category);}
    public void addSkill(JSONObject skill){put("skills", skill);}
    public void addLanguage(JSONObject language){put("languages", language);}

    public void deleteUser(String id)
    {
        JSONObject json;
        try {
            json = new JSONObject().put("id", id);
        }
        catch (Exception e) {return;}
        delete("users", json);
    }
    public void deleteGroup(String id)
    {
        JSONObject json;
        try {
            json = new JSONObject().put("id", id);
        }
        catch (Exception e) {return;}
        delete("groups", json);
    }
    public void deleteTrack(String id)
    {
        JSONObject json;
        try {
            json = new JSONObject().put("id", id);
        }
        catch (Exception e) {return;}
        delete("tracks", json);
    }
    public void deleteEvent(String id)
    {
        JSONObject json;
        try {
            json = new JSONObject().put("id", id);
        }
        catch (Exception e) {return;}
        delete("events", json);
    }
    public void deleteCategory(String id)
    {
        JSONObject json;
        try {
            json = new JSONObject().put("id", id);
        }
        catch (Exception e) {return;}
        delete("categries", json);
    }
    public void deleteSkill(String id)
    {
        JSONObject json;
        try {
            json = new JSONObject().put("id", id);
        }
        catch (Exception e) {return;}
        delete("skills", json);
    }
    public void deleteLanguage(String id)
    {
        JSONObject json;
        try {
            json = new JSONObject().put("id", id);
        }
        catch (Exception e) {return;}
        delete("languages", json);
    }


    // TODO: add getById and add links (addUserToGroup, addSkillToUser , etc...)

    //TODO: add post.

    private void delete(String entity, JSONObject json)
    {
        String url = SERVER_URL + entity;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, json, null, null);

        addToRequestQueue(request);
    }

    private void put(String entity, JSONObject json)
    {
        String url = SERVER_URL + entity;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, json, null, null);

        addToRequestQueue(request);
    }

    private JSONArray get(String entity)
    {
        String url = SERVER_URL + entity;

        RequestFuture<JSONArray> future = RequestFuture.newFuture();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, future, future);

        addToRequestQueue(request);

        try {
            return  future.get(30, TimeUnit.SECONDS);
        }
        catch (Exception e) {}

        return null;
    }
}
