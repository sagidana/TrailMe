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
import com.android.volley.toolbox.StringRequest;
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

    public JSONArray getRcommendations(String userId)
    {
        JSONObject request = new JSONObject();

        try {
            request.put("UserId", userId);

            return postArray("recommendations", request);
        }
        catch (Exception e) {
            return null;
        }
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

    public boolean isUser(String username, String password)
    {
        JSONObject userData = new JSONObject();
        try {
            userData.put("username",username);
            userData.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject result = postObject("login", userData);

        boolean isUser = false;

        try {
            isUser = result.getBoolean("isAutorizeUser");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return isUser;
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

    public JSONArray getTracksByUserId(String id)
    {
        JSONObject request = new JSONObject();

        try {
            request.put("Method", "getTracksByUserId");
            request.put("Id", id);

            return postArray("methods", request);
        }
        catch (Exception e) {
            return null;
        }
    }

    public JSONArray getTracksByEventId(String id)
    {
        JSONObject request = new JSONObject();

        try {
            request.put("Method", "getTracksByEventId");
            request.put("Id", id);

            return postArray("methods", request);
        }
        catch (Exception e) {
            return null;
        }
    }

    public JSONArray getUsersByGroupId(String id)
    {
        JSONObject request = new JSONObject();

        try {
            request.put("Method", "getUsersByGroupId");
            request.put("Id", id);

            return postArray("methods", request);
        }
        catch (Exception e) {
            return null;
        }
    }

    public JSONArray getUsersByTrackId(String id)
    {
        JSONObject request = new JSONObject();

        try {
            request.put("Method", "getUsersByTrackId");
            request.put("Id", id);

            return postArray("methods", request);
        }
        catch (Exception e) {
            return null;
        }
    }

    public JSONArray getGroupsByUserId(String id)
    {
        JSONObject request = new JSONObject();

        try {
            request.put("Method", "getGroupsByUserId");
            request.put("Id", id);

            return postArray("methods", request);
        }
        catch (Exception e) {
            return null;
        }
    }

    public JSONArray getGroupsByEventId(String id)
    {
        JSONObject request = new JSONObject();

        try {
            request.put("Method", "getGroupsByEventId");
            request.put("Id", id);

            return postArray("methods", request);
        }
        catch (Exception e) {
            return null;
        }
    }

    public JSONArray getEventsByGroupId(String id)
    {
        JSONObject request = new JSONObject();

        try {
            request.put("Method", "getEventsByGroupId");
            request.put("Id", id);

            return postArray("methods", request);
        }
        catch (Exception e) {
            return null;
        }
    }

    public JSONArray getEventsByTrackId(String id)
    {
        JSONObject request = new JSONObject();

        try {
            request.put("Method", "getEventsByTrackId");
            request.put("Id", id);

            return postArray("methods", request);
        }
        catch (Exception e) {
            return null;
        }
    }

    public void addUserToGroup(String userId, String groupId)
    {
        JSONObject request = new JSONObject();

        try
        {
            request.put("Method", "addUserToGroup");
            request.put("SourceId", userId);
            request.put("DestinationId", groupId);

            postObject("methods", request);
        }
        catch (Exception e) { }
    }

    public void addUserToTrack(String userId, String trackId)
    {
        JSONObject request = new JSONObject();

        try
        {
            request.put("Method", "addUserToTrack");
            request.put("SourceId", userId);
            request.put("DestinationId", trackId);

            postObject("methods", request);
        }
        catch (Exception e) { }
    }

    public void addSkillToUser(String skillId, String userId)
    {
        JSONObject request = new JSONObject();

        try
        {
            request.put("Method", "addSkillToUser");
            request.put("SourceId", skillId);
            request.put("DestinationId", userId);

            postObject("methods", request);
        }
        catch (Exception e) { }
    }

    public void addLanguageToUser(String languageId, String userId)
    {
        JSONObject request = new JSONObject();

        try
        {
            request.put("Method", "addLanguageToUser");
            request.put("SourceId", languageId);
            request.put("DestinationId", userId);

            postObject("methods", request);
        }
        catch (Exception e) { }
    }

    public void addCategoryToTrack(String categoryId, String trackId)
    {
        JSONObject request = new JSONObject();

        try
        {
            request.put("Method", "addCategoryToTrack");
            request.put("SourceId", categoryId);
            request.put("DestinationId", trackId);

            postObject("methods", request);
        }
        catch (Exception e) { }
    }

    public JSONObject getUserById(String id)
    {
        JSONObject request = new JSONObject();
        try {
            request.put("id", id);

            return postObject("users", request);
        }catch(Exception e){
            return null;
        }
    }

    public JSONObject getGroupById(String id)
    {
        JSONObject request = new JSONObject();
        try {
            request.put("id", id);

            return postObject("groups", request);
        }catch(Exception e){
            return null;
        }
    }

    public JSONObject getTrackById(String id)
    {
        JSONObject request = new JSONObject();
        try {
            request.put("id", id);

            return postObject("tracks", request);
        }catch(Exception e){
            return null;
        }
    }

    private JSONArray postArray(String entity, JSONObject json)
    {
        String url = SERVER_URL + entity;

        RequestFuture<JSONArray> future = RequestFuture.newFuture();
        TrailMeRequest request = new TrailMeRequest(Request.Method.POST, url, json, future, future);
        addToRequestQueue(request);

        try {
            return future.get(30, TimeUnit.SECONDS);
        }
        catch (Exception e) {}

        return null;
    }

    private JSONObject postObject(String entity, JSONObject json)
    {
        String url = SERVER_URL + entity;

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json, future, future);
        addToRequestQueue(request);

        try {
            return  future.get(30, TimeUnit.SECONDS);
        }
        catch (Exception e) {}

        return null;
    }

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
