package depton.trailme.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import depton.net.trailme.R;
import depton.trailme.adapters.MyTrackRecyclerViewAdapter;
import depton.trailme.data.RestCaller;
import depton.trailme.data.TrailMeListener;
import depton.trailme.models.Enums;
import depton.trailme.models.Track;
import depton.trailme.models.User;

public class RecommendedTracksFragment extends Fragment implements TrailMeListener{

    private String mUserId;
    private RestCaller restCaller = new RestCaller();
    private MyTrackRecyclerViewAdapter mAdapter = null;

    private TracksFragment.OnListFragmentInteractionListener mListener;

    public RecommendedTracksFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() != null) {
            mUserId = getArguments().getString("currentUser");
            restCaller.delegate = this;
        }

        View view = inflater.inflate(R.layout.fragment_recommended_tracks, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {

            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            restCaller.execute(this.getContext(), "getRecommendations", mUserId);

            recyclerView.setAdapter(mAdapter);
        }

        return view;
    }

    public void onButtonPressed(Track track) {
        if (mListener != null) {
            mListener.onListFragmentInteraction(track);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof TracksFragment.OnListFragmentInteractionListener) {
            mListener = (TracksFragment.OnListFragmentInteractionListener) context;
            mAdapter= new MyTrackRecyclerViewAdapter(new ArrayList<Track>(), mListener);
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
        try{
            if(response != null) {
                JSONArray jTracks = response.getJSONArray("tracks");

                ArrayList<Track> tracks = new ArrayList<>(jTracks.length());

                for (int i = 0; i < jTracks.length(); i++) {
                    Track t = new Track();
                    t.ID = jTracks.getJSONObject(i).getString("Id");
                    t.Name = jTracks.getJSONObject(i).getString("Name");
                    t.Difficulty = Enums.Difficulty.valueOf(jTracks.getJSONObject(i).getString("Difficulty"));
                    tracks.add(t);
                    Log.d("Tracks", "TrackFragment - processFinish: Added track " + t.Name + " in ID" + String.valueOf(i) + " ");
                }
                mAdapter.updateList(tracks);
            }
            else {
                Log.d("ERROR", "processFinish: Issues Connecting to the server");
            }

        }
        catch (Exception e){
            Log.d("Sd", "processFinish: " + e.toString());
        }
    }
}
