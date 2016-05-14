package depton.trailme.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import depton.net.trailme.R;
import depton.trailme.data.RestCaller;
import depton.trailme.data.TrailMeListener;
import depton.trailme.models.Track;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrackDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrackDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackDetails extends Fragment implements TrailMeListener{

    private Track track;
    private TrackDetails mCtx;
    private RestCaller addUserToTrack = new RestCaller();
    private String mCurrentUserId;

    private OnFragmentInteractionListener mListener;

    public TrackDetails() { }

    public static TrackDetails newInstance(Track track, String userId) {
        TrackDetails fragment = new TrackDetails();
        Bundle args = new Bundle();
        args.putParcelable("track", track);
        args.putString("currentUser", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCtx = this;
            track = getArguments().getParcelable("track");
            mCurrentUserId = getArguments().getString("currentUser");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        addUserToTrack.delegate = mCtx;

        View v = inflater.inflate(R.layout.fragment_track_details, container, false);

        TextView TrackName = (TextView) v.findViewById(R.id.trackName);
        TrackName.setText(track.Name);

        TextView kilometers = (TextView) v.findViewById(R.id.kilometers);
        kilometers.setText(Double.toString(track.Length));

        TextView difficulty = (TextView) v.findViewById(R.id.difficulty);
        difficulty.setText(track.Difficulty.toString());

        TextView zone = (TextView) v.findViewById(R.id.zone);
        zone.setText(track.Zone);

        TextView latitude = (TextView) v.findViewById(R.id.latitude);
        latitude.setText(Double.toString(track.latitude));

        TextView longitude = (TextView) v.findViewById(R.id.longitude);
        longitude.setText(Double.toString(track.longitude));

        Button addUserToTrackbtn = (Button) v.findViewById(R.id.addUserToTrackBtn);
        addUserToTrackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserToTrack.execute(mCtx.getContext(), "addUserToTrack", mCurrentUserId, track.ID);
            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    public void processFinish(JSONObject response) {
        // TODO:
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
