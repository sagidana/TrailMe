package depton.trailme.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.json.JSONObject;

/**
 * Created by ShaiMedad on 5/14/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event implements Parcelable {



    public Event(){}

    @JsonProperty("Id")
    public String ID;
    @JsonProperty("Name")
    public String Name;
    @JsonProperty("EventTrack")
    public Track EventTrack;
    @JsonProperty("EventGroup")
    public Group EventGroup;
    @JsonProperty("EventStartDate")
    public String EventStartDate;
    @JsonProperty("EventEndDate")
    public String EventEndDate;
    @JsonProperty("EventDays")
    public int EventDays;
    @JsonProperty("HourStart")
    public String HourStart;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ID);
        dest.writeString(this.Name);
        dest.writeParcelable(this.EventTrack, flags);
        dest.writeParcelable(this.EventGroup, flags);
        dest.writeString(this.EventStartDate);
        dest.writeString(this.EventEndDate);
        dest.writeInt(this.EventDays);
        dest.writeString(this.HourStart);
    }

    protected Event(Parcel in) {
        this.ID = in.readString();
        this.Name = in.readString();
        this.EventTrack = in.readParcelable(Track.class.getClassLoader());
        this.EventGroup = in.readParcelable(Group.class.getClassLoader());
        this.EventStartDate = in.readString();
        this.EventEndDate = in.readString();
        this.EventDays = in.readInt();
        this.HourStart = in.readString();
    }

    public Event(JSONObject jsonObject){
        try {
            this.ID = jsonObject.getString("Id");
            this.Name = jsonObject.getString("Name");
            this.EventStartDate = jsonObject.getString("EventStartDate");
            this.EventEndDate = jsonObject.getString("EventEndDate");
            this.EventDays = jsonObject.getInt("EventDays");
            this.HourStart = jsonObject.getString("HourStart");
            /*this.EventGroup = new Group(jsonObject.getJSONObject("Group"));
            this.EventTrack = new Track();*/
        }
        catch (Exception ex){

        }
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
