package depton.trailme.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
    }

    protected Event(Parcel in) {
        this.ID = in.readString();
        this.Name = in.readString();
        this.EventTrack = in.readParcelable(Track.class.getClassLoader());
        this.EventGroup = in.readParcelable(Group.class.getClassLoader());
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
