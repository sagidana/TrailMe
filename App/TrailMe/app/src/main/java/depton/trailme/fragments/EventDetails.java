package depton.trailme.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import depton.net.trailme.R;
import depton.trailme.data.RestCaller;
import depton.trailme.data.TrailMeListener;
import depton.trailme.models.Event;
import depton.trailme.models.Track;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventDetails extends Fragment implements TrailMeListener {

    private Event event;
    private EventDetails mCtx;
    private EventFragment.OnListFragmentInteractionListener mListener;

    private TracksFragment.OnListFragmentInteractionListener mTrackListener;
    private GroupFragment.OnListFragmentInteractionListener mGroupListener;

    private RestCaller restGroupsCaller = new RestCaller();
    private RestCaller restTracksCaller = new RestCaller();
    private View mTrackItem = null;
    private View mGroupItem = null;

    public EventDetails() {
        // Required empty public constructor
    }

    public static EventDetails newInstance(Event eve) {
        EventDetails fragment = new EventDetails();
        Bundle args = new Bundle();
        args.putParcelable("event", eve);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCtx = this;
            event = getArguments().getParcelable("event");


            restTracksCaller.delegate=mCtx;
            restGroupsCaller.delegate=mCtx;

            restTracksCaller.execute(mCtx.getContext(), "getTracksByEventId",event.ID);
            restGroupsCaller.execute(mCtx.getContext(), "getGroupsByEventId", event.ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.fragment_event_details, container, false);
        mTrackItem = v.findViewById(R.id.event_track);
        mGroupItem = v.findViewById(R.id.event_group);

        //mTrackItem.setOnClickListener(mListener);
        TextView EventName = (TextView)v.findViewById(R.id.event_name);
        EventName.setText(event.Name);

        TextView eventStartDate = (TextView) v.findViewById(R.id.eventItemStartDate);
        eventStartDate.setText("Start Date: " + event.EventStartDate + " ");

        TextView eventHourStart = (TextView) v.findViewById(R.id.eventHourStart);
        eventHourStart.setText(event.HourStart);

        TextView eventItemEndDate = (TextView) v.findViewById(R.id.eventItemEndDate);
        eventItemEndDate.setText("End Date: " + event.EventEndDate);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EventFragment.OnListFragmentInteractionListener && context instanceof GroupFragment.OnListFragmentInteractionListener && context instanceof TracksFragment.OnListFragmentInteractionListener) {

            mListener = (EventFragment.OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void processFinish(JSONObject response) {

        Log.d("ProcessFinishDetails", response.toString());

        try {
            if (response != null) {
                if(response.has("groups"))
                {
                    JSONArray jGroups = response.getJSONArray("groups");
                    JSONObject jGroup = jGroups.getJSONObject(0);

                    TextView groupName = (TextView)mGroupItem.findViewById(R.id.genderAndAge);
                    groupName.setText(jGroup.getString("Name"));
                }
                else if (response.has("tracks"))
                {
                    JSONArray jTracks = response.getJSONArray("tracks");
                    JSONObject jTrack = jTracks.getJSONObject(0);

                    Track track = new Track(jTrack);

                    TextView trackName = (TextView)mTrackItem.findViewById(R.id.genderAndAge);
                    trackName.setText(track.Name);

                    RatingBar ratingBar = (RatingBar) mTrackItem.findViewById(R.id.ratingBar);
                    ratingBar.setRating(track.Rating);
                    LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
                    stars.getDrawable(1).setColorFilter(Color.parseColor("#a5bc99"), PorterDuff.Mode.SRC_ATOP);
                    stars.getDrawable(2).setColorFilter(Color.parseColor("#3b802f"), PorterDuff.Mode.SRC_ATOP);

                    TextView zone = (TextView)mTrackItem.findViewById(R.id.zone);
                    zone.setText(track.Zone);

                    ImageView trackImage = (ImageView)mTrackItem.findViewById(R.id.trackImage);
                    Picasso.with(getContext()).load(track.ImageUrl).into(trackImage);
                }
            }
        }
        catch (Exception Ex)
        {
            Log.d("REST", "Rest response: Failed to init groups\tracks by event id");
        }

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
