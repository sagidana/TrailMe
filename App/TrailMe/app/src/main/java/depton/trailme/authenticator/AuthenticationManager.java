package depton.trailme.authenticator;

import android.content.Context;

import org.json.JSONArray;
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

    public boolean AuthUser(String username,String password)
    {
        try {
            JSONArray users = TrailMeServer.getInstance(mCtx).getUsers();

            for (int i = 0; i < users.length(); i++) {
                String currUsername = users.getJSONObject(i).getString("MailAddress");

                if (currUsername.equals(username))
                    return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }
}
