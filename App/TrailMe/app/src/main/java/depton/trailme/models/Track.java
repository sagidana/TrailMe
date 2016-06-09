package depton.trailme.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
