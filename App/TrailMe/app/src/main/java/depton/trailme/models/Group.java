package depton.trailme.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by CodeValueUser on 5/10/2016.
 */
public class Group implements Parcelable {
    public String Id;
    public String Name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Id);
        dest.writeString(this.Name);
    }

    public Group() {
    }

    protected Group(Parcel in) {
        this.Id = in.readString();
        this.Name = in.readString();
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
