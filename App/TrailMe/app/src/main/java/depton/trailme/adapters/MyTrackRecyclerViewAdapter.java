package depton.trailme.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.internal.zzir;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import depton.net.trailme.R;
import depton.trailme.fragments.TracksFragment.OnListFragmentInteractionListener;
import depton.trailme.models.Track;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

public class MyTrackRecyclerViewAdapter
        extends RecyclerView.Adapter<MyTrackRecyclerViewAdapter.ViewHolder>
        implements Filterable {

    private List<Track> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    @Override
    public Filter getFilter() {
        return new TracksFilter();
    }

    public MyTrackRecyclerViewAdapter(List<Track> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        setHasStableIds(true);
    }

    public void updateList(List<Track> newlist) {
        mValues.clear();
        mValues.addAll(newlist);
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_track_item, parent, false);
        context = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Track currentTrack = mValues.get(holder.getAdapterPosition());
        holder.mContainer.setAlpha(0.75f);
        holder.mItem = currentTrack;
        holder.mIdView.setText(currentTrack.Name);
        holder.mRatingBar.setRating(currentTrack.Rating);
        holder.zone.setText(currentTrack.Zone);

        /* TODO: get the url from the db */
        String url = "https://lh5.ggpht.com/wytWRnRbbGxX7aI1U8w3tQM1PRdLOZ3LO7ZfdUtwIyCG44nYiWdmvthaXkzUQJ7Cg7Q=w300";
        Picasso.with(context).load(currentTrack.ImageUrl).into(holder.mTrackImg);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

        LayerDrawable stars = (LayerDrawable) holder.mRatingBar.getProgressDrawable();
        stars.getDrawable(1).setColorFilter(Color.parseColor("#a5bc99"), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(2).setColorFilter(Color.parseColor("#3b802f"), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        /*public final TextView mDescriptionView;*/
        public final RatingBar mRatingBar;
        public Track mItem;
        public final ImageView mTrackImg;
        public final LinearLayout mContainer;
        public final TextView zone;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.genderAndAge);/*
            mDescriptionView = (TextView) view.findViewById(R.id.Description);*/
            mRatingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            mTrackImg = (ImageView) view.findViewById(R.id.trackImage);
            mContainer = (LinearLayout) view.findViewById(R.id.whiteContainer);
            zone = (TextView) view.findViewById(R.id.zone);
        }

        @Override
        public String toString() {
            /*return super.toString() + " '" + mDescriptionView.getText() + "'";*/
            return super.toString();
        }
    }

    private class TracksFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults filterResults = new FilterResults();
            JSONArray stars = null;

            ArrayList<Integer> starsList = new ArrayList<>();

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(constraint.toString());
                stars = jsonObject.getJSONArray("stars");

                for(int i = 0; i < stars.length(); i++)
                {
                    starsList.add(stars.getInt(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(starsList.size() > 0){
                filterResults.values = mValues;
                filterResults.count = mValues.size();
            } else {
                List<Track> filteredList = new ArrayList<>();

                for(Track track : mValues){
                    if(starsList.contains(track.Rating)){
                        filteredList.add(track);
                    }
                }

                filterResults.values = filteredList;
                filterResults.count = filteredList.size();
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count != 0){
                mValues = (List<Track>)results.values;
                notifyDataSetChanged();
            }
        }
    }
}
