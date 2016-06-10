package depton.trailme.fragments;

import android.content.Context;
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
import depton.trailme.adapters.MyEventRecyclerViewAdapter;
import depton.trailme.adapters.MyTrackRecyclerViewAdapter;
import depton.trailme.data.RestCaller;
import depton.trailme.data.TrailMeListener;
import depton.trailme.fragments.dummy.DummyContent;
import depton.trailme.fragments.dummy.DummyContent.DummyItem;
import depton.trailme.models.Enums;
import depton.trailme.models.Event;
import depton.trailme.models.Track;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class EventFragment extends Fragment implements TrailMeListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private MyEventRecyclerViewAdapter mAdapter = null;
    private RestCaller restCaller = new RestCaller();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static EventFragment newInstance(int columnCount) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            //restCaller.execute(this.getContext(),"getEvents");
            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
            mAdapter= new MyEventRecyclerViewAdapter(new ArrayList<Event>(), mListener);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
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
                JSONArray jEvents = response.getJSONArray("events");

                ArrayList<Event> events = new ArrayList<>(jEvents.length());

                for (int i = 0; i < jEvents.length(); i++) {
                    Event eve = new Event();
                    eve.ID = jEvents.getJSONObject(i).getString("Id");
                    eve.Name = jEvents.getJSONObject(i).getString("Name");
                    //t.Difficulty = Enums.Difficulty.valueOf(jEvents.getJSONObject(i).getString("Difficulty"));
                    events.add(eve);
                    Log.d("Events", "EventFragment - processFinish: Added Event " + eve.Name + " in ID" + String.valueOf(i) + " ");
                }
                mAdapter.updateList(events);
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
        public void onListFragmentInteraction(Event item);
    }
}
