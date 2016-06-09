package depton.trailme.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yotam on 4/2/2016.
 */
public class Skill implements Parcelable {
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public Skill() {
    }

    protected Skill(Parcel in) {
    }

    public static final Parcelable.Creator<Skill> CREATOR = new Parcelable.Creator<Skill>() {
        @Override
        public Skill createFromParcel(Parcel source) {
            return new Skill(source);
        }

        @Override
        public Skill[] newArray(int size) {
            return new Skill[size];
        }
    };
}
