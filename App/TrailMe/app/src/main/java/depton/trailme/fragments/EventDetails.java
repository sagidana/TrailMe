package depton.trailme.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import depton.net.trailme.R;
import depton.trailme.data.RestCaller;
import depton.trailme.data.TrailMeListener;
import depton.trailme.models.Event;
import depton.trailme.models.User;

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

        TextView EventName = (TextView)v.findViewById(R.id.event_name);
        EventName.setText(event.Name);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EventFragment.OnListFragmentInteractionListener) {
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

                    TextView groupName = (TextView)mGroupItem.findViewById(R.id.name);
                    groupName.setText(jGroup.getString("Name"));
                }
                else if (response.has("tracks"))
                {
                    JSONArray jTracks = response.getJSONArray("tracks");
                    JSONObject jTrack = jTracks.getJSONObject(0);

                    TextView trackName = (TextView)mTrackItem.findViewById(R.id.name);
                    trackName.setText(jTrack.getString("Name"));

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
