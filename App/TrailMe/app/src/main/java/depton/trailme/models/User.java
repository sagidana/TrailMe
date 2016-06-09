package depton.trailme.models;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import depton.trailme.activities.LoginActivity;

/**
 * Created by Yotam on 4/2/2016.
 */
public class User implements Parcelable {
    public String ID;
    public String FirstName;
    public String SurName;
    public String Email;
    public String City;
    public String Birthdate;
    public Image[] Images;
    public Skill[] Skills;
    public String[] Languages;
    public String Gender;
    public int Age;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ID);
        dest.writeString(this.FirstName);
        dest.writeString(this.SurName);
        dest.writeString(this.Email);
        dest.writeString(this.City);
        dest.writeString(this.Birthdate);
        dest.writeString(this.Gender);
        dest.writeInt(this.Age);
        dest.writeStringArray(this.Languages);
        //dest.writeParcelable(this.Images, flags);
        //dest.writeParcelable(this.Skills, flags);
        //dest.writeParcelable(this.Languages, flags);
    }

    public User(){

    }

    public User(JSONObject jsonUser)
    {
        try {
            this.Email = jsonUser.getString("MailAddress");
            this.Gender = jsonUser.getString("Gender");
            this.Birthdate = jsonUser.getString("BirthDate");
            this.FirstName = jsonUser.getString("FirstName");
            this.SurName = jsonUser.getString("LastName");
            this.ID = jsonUser.getString("Id");
            this.City = jsonUser.getString("City");
            this.Age = Integer.parseInt(jsonUser.getString("Age"));
            this.Languages = getLanguagesArr(jsonUser.getJSONArray("Languages"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String[] getLanguagesArr(JSONArray jsonArray){
        String [] list = new String[jsonArray.length()];
        try {
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i=0;i<len;i++){
                    list[i] = jsonArray.getJSONObject(i).getString("Name");
                }
            }
        }
        catch (Exception ex){
        }

        return list;
    }

    public static User getUserFromSharedPref(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("TrailMe", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("loggedInUser", "");
        return gson.fromJson(json, User.class);
    }

    private static void saveUserNameInSharedPref(Context context, User user){
        SharedPreferences sharedPreferences = context.getSharedPreferences("TrailMe", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString("loggedInUser", json);
        prefsEditor.commit();
    }

    public static void checkUsernameInPref(Context context){
        if(!(context instanceof LoginActivity) && context instanceof Activity){
            if(getUserFromSharedPref(context) == null){
                Intent intent = new Intent(context, LoginActivity.class);
                Activity activity = (Activity) context;
                activity.finish();
                activity.startActivity(intent);
            }
        }
    }

    protected User(Parcel in) {
        this.ID = in.readString();
        this.FirstName = in.readString();
        this.SurName = in.readString();
        this.Email = in.readString();
        this.City = in.readString();
        this.Birthdate = in.readString();
        this.Gender = in.readString();
        this.Age = in.readInt();
        in.readStringArray(this.Languages);

        //this.Images = in.readParcelable(Image[].class.getClassLoader());
        //this.Skills = in.readParcelable(Skill[].class.getClassLoader());
        //this.Languages = in.readParcelable(Enums.Languages[].class.getClassLoader());
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
