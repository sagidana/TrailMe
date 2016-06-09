package depton.trailme.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import depton.net.trailme.R;
import depton.trailme.fragments.UsersFragment.OnListFragmentInteractionListener;
import depton.trailme.fragments.dummy.DummyContent.DummyItem;
import depton.trailme.models.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyHikerRecyclerViewAdapter extends RecyclerView.Adapter<MyHikerRecyclerViewAdapter.ViewHolder> {

    private final List<User> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyHikerRecyclerViewAdapter(List<User> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_hiker_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String allLangs = "";

        final User currentUser = mValues.get(position);

        for(int i = 0; i < currentUser.Languages.length; i++){
            if(i != 0)
                allLangs += ", ";
            allLangs += currentUser.Languages[i];
        }

        long age = 30;

        holder.mItem = mValues.get(position);
        holder.mNameView.setText(currentUser.FirstName + " " + currentUser.SurName);
        holder.mLanguages.setText(allLangs);
        holder.mGenderAndAge.setText(currentUser.Gender + ", " + age);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(currentUser);
                }
            }
        });
    }

    /* TODO: make it work */
    public long getUserAge(String dateStr) throws ParseException {
        DateFormat format = new SimpleDateFormat("dd MM yyyy", Locale.US);
        Date d1 = format.parse(dateStr);
        Date now = new Date();
        long diffInMiliseconds = now.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diffInMiliseconds, TimeUnit.MILLISECONDS) / 365;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void updateList(List<User> newlist) {
        mValues.clear();
        mValues.addAll(newlist);
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mAboutView;
        public final ImageView mProfilePic;
        public final TextView mLanguages;
        public final TextView mGenderAndAge;
        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.fullName);
            mGenderAndAge = (TextView) view.findViewById(R.id.genderAndAge);
            mAboutView = (TextView) view.findViewById(R.id.languages);
            mProfilePic = (ImageView) view.findViewById(R.id.ProfileImage);
            mLanguages =(TextView) view.findViewById(R.id.languages);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
