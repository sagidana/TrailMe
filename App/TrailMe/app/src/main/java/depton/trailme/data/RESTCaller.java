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
import depton.trailme.DAL.TrailMeRequest;
import depton.trailme.DAL.TrailMeServer;
import depton.trailme.models.Track;

/**
 * Created by Yotam on 5/6/2016.
 */
public class RestCaller extends AsyncTask<Object, Void, JSONObject>  {
    public TrailMeListener delegate = null;

        @Override
    protected JSONObject doInBackground(Object... params) {

        Context ctx = (Context)params[0];
        JSONObject response = new JSONObject();

        switch ((String)params[1])
        {
            case ("getTracks"):
            {
                JSONArray tracks = TrailMeServer.getInstance(ctx).getTracks();
                try {
                    response.put("tracks", tracks);
                }catch(Exception e){ }
                break;
            }
            case ("getUsers"):
            {
                JSONArray users = TrailMeServer.getInstance(ctx).getUsers();
                try {
                    response.put("users", users);
                }catch(Exception e){ }
                break;
            }
            case ("getGroups"):
            {
                JSONArray groups = TrailMeServer.getInstance(ctx).getGroups();
                try {
                    response.put("groups", groups);
                }catch(Exception e){ }
                break;
            }
            case ("getEvents"):
            {
                JSONArray events = TrailMeServer.getInstance(ctx).getEvents();
                try {
                    response.put("events", events);
                }catch(Exception e){ }
                break;
            }
            case ("getSkills"):
            {
                JSONArray skills = TrailMeServer.getInstance(ctx).getSkills();
                try {
                    response.put("skills", skills);
                }catch(Exception e){ }
                break;
            }
            case ("getLanguages"):
            {
                JSONArray languages = TrailMeServer.getInstance(ctx).getLanguages();
                try {
                    response.put("languages", languages);
                }catch(Exception e){ }
                break;
            }
            case ("getCategories"):
            {
                JSONArray categories = TrailMeServer.getInstance(ctx).getCategories();
                try {
                    response.put("categories", categories);
                }catch(Exception e){ }
                break;
            }
            case ("getUserById"):
            {
                response = TrailMeServer.getInstance(ctx).getUserById((String)params[2]);
                break;
            }
            case ("getGroupById"):
            {
                response = TrailMeServer.getInstance(ctx).getGroupById((String)params[2]);
                break;
            }
            case ("getTrackById"):
            {
                response = TrailMeServer.getInstance(ctx).getTrackById((String)params[2]);
                break;
            }
            case ("getTracksByUserId"):
            {
                JSONArray tracks = TrailMeServer.getInstance(ctx).getTracksByUserId((String)params[2]);
                try {
                    response.put("tracks", tracks);
                }catch(Exception e){ }
                break;
            }
            case ("getTracksByEventId"):
            {
                JSONArray tracks = TrailMeServer.getInstance(ctx).getTracksByEventId((String)params[2]);
                try {
                    response.put("tracks", tracks);
                }catch(Exception e){ }
                break;
            }
            case ("getEventsByGroupId"):
            {
                JSONArray events = TrailMeServer.getInstance(ctx).getEventsByGroupId((String)params[2]);
                try {
                    response.put("events", events);
                }catch(Exception e){ }
                break;
            }
            case ("getEventsByTrackId"):
            {
                JSONArray events = TrailMeServer.getInstance(ctx).getEventsByTrackId((String)params[2]);
                try {
                    response.put("events", events);
                }catch(Exception e){ }
                break;
            }
            case ("getUsersByGroupId"):
            {
                JSONArray users = TrailMeServer.getInstance(ctx).getUsersByGroupId((String)params[2]);
                try {
                    response.put("users", users);
                }catch(Exception e){ }
                break;
            }
            case ("getGroupsByUserId"):
            {
                JSONArray groups = TrailMeServer.getInstance(ctx).getGroupsByUserId((String)params[2]);
                try {
                    response.put("groups", groups);
                }catch(Exception e){ }
                break;
            }
            case ("getGroupsByEventId"):
            {
                JSONArray groups = TrailMeServer.getInstance(ctx).getGroupsByEventId((String)params[2]);
                try {
                    response.put("groups", groups);
                }catch(Exception e){ }
                break;
            }
            case ("addUser"):
            {
                TrailMeServer.getInstance(ctx).addUser((JSONObject)params[2]);
                break;
            }
            case ("addTrack"):
            {
                TrailMeServer.getInstance(ctx).addTrack((JSONObject)params[2]);
                break;
            }
            case ("addEvent"):
            {
                TrailMeServer.getInstance(ctx).addEvent((JSONObject)params[2]);
                break;
            }
            case ("addGroup"):
            {
                TrailMeServer.getInstance(ctx).addGroup((JSONObject)params[2]);
                break;
            }
            case ("addSkill"):
            {
                TrailMeServer.getInstance(ctx).addSkill((JSONObject)params[2]);
                break;
            }
            case ("addLanguage"):
            {
                TrailMeServer.getInstance(ctx).addLanguage((JSONObject)params[2]);
                break;
            }
            case ("addCategory"):
            {
                TrailMeServer.getInstance(ctx).addCategory((JSONObject)params[2]);
                break;
            }
            case ("addUserToGroup"):
            {
                String sourceId = (String)params[2];
                String destinationId = (String)params[3];
                TrailMeServer.getInstance(ctx).addUserToGroup(sourceId, destinationId);
            }
            case ("addUserToTrack"):
            {
                String sourceId = (String)params[2];
                String destinationId = (String)params[3];
                TrailMeServer.getInstance(ctx).addUserToTrack(sourceId, destinationId);
            }
            case ("addSkillToUser"):
            {
                String sourceId = (String)params[2];
                String destinationId = (String)params[3];
                TrailMeServer.getInstance(ctx).addSkillToUser(sourceId, destinationId);
            }
            case ("addLanguageToUser"):
            {
                String sourceId = (String)params[2];
                String destinationId = (String)params[3];
                TrailMeServer.getInstance(ctx).addLanguageToUser(sourceId, destinationId);
            }
            case ("addCategoryToTrack"):
            {
                String sourceId = (String)params[2];
                String destinationId = (String)params[3];
                TrailMeServer.getInstance(ctx).addCategoryToTrack(sourceId, destinationId);
            }
            case ("deleteUser"):
            {
                TrailMeServer.getInstance(ctx).deleteUser((String)params[2]);
                break;
            }
            case ("deleteTrack"):
            {
                TrailMeServer.getInstance(ctx).deleteTrack((String)params[2]);
                break;
            }
            case ("deleteEvent"):
            {
                TrailMeServer.getInstance(ctx).deleteEvent((String)params[2]);
                break;
            }
            case ("deleteGroup"):
            {
                TrailMeServer.getInstance(ctx).deleteGroup((String)params[2]);
                break;
            }
            case ("deleteSkill"):
            {
                TrailMeServer.getInstance(ctx).deleteSkill((String)params[2]);
                break;
            }
            case ("deleteLanguage"):
            {
                TrailMeServer.getInstance(ctx).deleteLanguage((String)params[2]);
                break;
            }
            case ("deleteCategory"):
            {
                TrailMeServer.getInstance(ctx).deleteCategory((String)params[2]);
                break;
            }
        }

        return response;
    }

    @Override
    protected void onPostExecute(JSONObject jsonResponse) {
        if (jsonResponse != null && jsonResponse.length() != 0 && delegate != null)
            delegate.processFinish(jsonResponse);
    }
}
