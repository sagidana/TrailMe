package depton.trailme.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import depton.net.trailme.R;
import depton.trailme.activities.MainActivity;
import depton.trailme.adapters.MyTrackRecyclerViewAdapter;
import depton.trailme.data.TrailMeListener;
import depton.trailme.data.RestCaller;
import depton.trailme.models.Enums;
import depton.trailme.models.Track;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TracksFragment extends Fragment implements TrailMeListener{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private MyTrackRecyclerViewAdapter mAdapter = null;
    private RestCaller restCaller = new RestCaller();

    private TracksFilterFragment filterFragment;

    private View filterMenuItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TracksFragment() {
        filterFragment = TracksFilterFragment.newInstance(null, null);
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TracksFragment newInstance(int columnCount) {
        TracksFragment fragment = new TracksFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    public MyTrackRecyclerViewAdapter getAdapter(){
        return mAdapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        restCaller.delegate = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track_list, container, false);



        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            restCaller.execute(this.getContext(),"getTracks");
            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }


    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
            mAdapter= new MyTrackRecyclerViewAdapter(new ArrayList<Track>(), mListener);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }

        final MainActivity act = (MainActivity)context;

        act.setFilterAction(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                filterFragment.show(getFragmentManager(), "filterTag");
                return true;
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void processFinish(JSONObject response) {
        try{
            if(response != null) {
                JSONArray jTracks = response.getJSONArray("tracks");

                ArrayList<Track> tracks = new ArrayList<>(jTracks.length());

                for (int i = 0; i < jTracks.length(); i++) {
                    Track t = new Track();
                    t.ID = jTracks.getJSONObject(i).getString("Id");
                    t.Name = jTracks.getJSONObject(i).getString("Name");
                    t.latitude = Double.parseDouble(jTracks.getJSONObject(i).getString("Latitude"));
                    t.Length = Double.parseDouble(jTracks.getJSONObject(i).getString("Kilometers"));
                    t.longitude = Double.parseDouble(jTracks.getJSONObject(i).getString("Longitude"));
                    t.Difficulty = Enums.Difficulty.valueOf(jTracks.getJSONObject(i).getString("Difficulty"));
                    t.Zone = jTracks.getJSONObject(i).getString("Zone");
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
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onListFragmentInteraction(Track item);
    }
}
