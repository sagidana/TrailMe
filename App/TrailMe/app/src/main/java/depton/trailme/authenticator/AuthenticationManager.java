package depton.trailme.authenticator;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import depton.trailme.DAL.TrailMeServer;

/**
 * Created by Yotam on 5/5/2016.
 */
public class AuthenticationManager {

    private Context mCtx;
    public AuthenticationManager(Context ctx)
    {
        mCtx = ctx;
    }

    /**
     * Changed by alon
     * @param username
     * @param password
     * @return
     */
    public boolean AuthUser(String username,String password)
    {
        return TrailMeServer.getInstance(mCtx).isUser(username, password);
    }
}
