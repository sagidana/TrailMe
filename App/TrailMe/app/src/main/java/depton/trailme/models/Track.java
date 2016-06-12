package depton.trailme.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.json.JSONArray;
import org.json.JSONObject;

import depton.trailme.models.Enums;

/**
 * Created by Yotam on 4/2/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Track implements Parcelable {

    public Track(){}

    @JsonProperty("Id")
    public String ID;
    @JsonProperty("Name")
    public String Name;
    @JsonProperty("Longitude")
    public double longitude;
    @JsonProperty("Latitude")
    public double latitude;
    @JsonProperty("DistanceKM")
    public double Length;
    @JsonProperty("Easy")
    public Enums.Difficulty Difficulty;
    @JsonProperty("Zone")
    public String Zone;
    @JsonProperty("Rating")
    public int Rating;
    @JsonProperty("Categories")
    public String [] Categories;
    @JsonProperty("ImageUrl")
    public String ImageUrl;

    @JsonProperty("TrackDescription")
    public String TrackDescription;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ID);
        dest.writeString(this.Name);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.Length);
        dest.writeInt(this.Difficulty == null ? -1 : this.Difficulty.ordinal());
        dest.writeString(this.Zone);
        dest.writeInt(this.Rating);
        dest.writeString(this.TrackDescription);
        dest.writeStringArray(this.Categories);
        dest.writeString(this.ImageUrl);
    }

    protected Track(Parcel in) {
        this.ID = in.readString();
        this.Name = in.readString();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
        this.Length = in.readDouble();
        int tmpDifficulty = in.readInt();
        this.Difficulty = tmpDifficulty == -1 ? null : Enums.Difficulty.values()[tmpDifficulty];
        this.Zone = in.readString();
        this.Rating = in.readInt();
        this.TrackDescription = in.readString();
        in.writeStringArray(this.Categories);
        this.ImageUrl = in.readString();
    }

    public Track(JSONObject jsonObject){
        try {
            this.ID = jsonObject.getString("Id");
            this.Name = jsonObject.getString("Name");
            this.latitude = Double.parseDouble(jsonObject.getString("Latitude"));
            this.Length = Double.parseDouble(jsonObject.getString("Kilometers"));
            this.longitude = Double.parseDouble(jsonObject.getString("Longitude"));
            this.Difficulty = Enums.Difficulty.valueOf(jsonObject.getString("Difficulty"));
            this.TrackDescription = jsonObject.getString("TrackDescription");
            this.Zone = jsonObject.getString("Zone");
            this.Categories = getCategoriesArr(jsonObject.getJSONArray("Categories"));
            this.Rating = jsonObject.getInt("Rating");
            this.ImageUrl = jsonObject.getString("ImageUrl");
        }
        catch (Exception ex){

        }
    }

    public String[] getCategoriesArr(JSONArray jsonArray){
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

    public static final Parcelable.Creator<Track> CREATOR = new Parcelable.Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel source) {
            return new Track(source);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };
}
