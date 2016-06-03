package depton.trailme.models;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

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
        dest.writeStringArray(this.Languages);
        //dest.writeParcelable(this.Images, flags);
        //dest.writeParcelable(this.Skills, flags);
        //dest.writeParcelable(this.Languages, flags);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.ID = in.readString();
        this.FirstName = in.readString();
        this.SurName = in.readString();
        this.Email = in.readString();
        this.City = in.readString();
        this.Birthdate = in.readString();
        this.Gender = in.readString();
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
