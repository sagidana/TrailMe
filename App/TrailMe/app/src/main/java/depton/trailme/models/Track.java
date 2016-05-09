package depton.trailme.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import depton.trailme.models.Enums;

/**
 * Created by Yotam on 4/2/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Track {

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
}
