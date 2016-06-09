package depton.trailme.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by CodeValueUser on 5/10/2016.
 */
public class Group implements Parcelable {
    public String Id;
    public String Name;
    public int Members;
    public String [] Languages;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Id);
        dest.writeString(this.Name);
        dest.writeInt(this.Members);
        dest.writeStringArray(Languages);
    }

    public Group(JSONObject jsonObject) {
        try {
            this.Id = jsonObject.getString("Id");
            this.Name = jsonObject.getString("Name");
            this.Languages = getLanguagesArr(jsonObject.getJSONArray("Languages"));
            this.Members = jsonObject.getInt("NumOfGroupMembers");
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

    protected Group(Parcel in) {
        this.Id = in.readString();
        this.Name = in.readString();
        this.Members = in.readInt();
        in.readStringArray(this.Languages);
    }

    public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel source) {
            return new Group(source);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };
}
