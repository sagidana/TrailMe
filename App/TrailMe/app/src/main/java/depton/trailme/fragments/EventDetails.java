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
    private OnFragmentInteractionListener mListener;
    private RestCaller restGroupsCaller = new RestCaller();
    private RestCaller restTracksCaller = new RestCaller();


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
            event = getArguments().getParcelable("event");


            restTracksCaller.delegate=this;
            restGroupsCaller.delegate=this;
            restTracksCaller.execute(this.getContext(), "getTracksByEventId",event.ID);
            restGroupsCaller.execute(this.getContext(), "getGroupsByEventId", event.ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.fragment_event_details, container, false);
        TextView EventName = (TextView)v.findViewById(R.id.EventName);
        EventName.setText(event.Name);
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

    @Override
    public void processFinish(JSONObject response) {

        Log.d("ProcessFinishDetails", response.toString());

        try {
            if (response != null) {
                if(response.has("groups"))
                {
                    JSONArray jGroups = response.getJSONArray("groups");
                }
                else if (response.has("tracks"))
                {
                    JSONArray jTracks = response.getJSONArray("tracks");
                }
                /*else if(response.has("users")) {
                    JSONArray jUsers = response.getJSONArray("users");
                    ArrayList<User> users = new ArrayList<>(jUsers.length());

                    for (int i = 0; i < jUsers.length(); i++) {
                        User user = new User();
                        user.FirstName = jUsers.getJSONObject(i).getString("FirstName");
                        user.SurName = jUsers.getJSONObject(i).getString("LastName");
                        user.ID = jUsers.getJSONObject(i).getString("Id");

                        users.add(user);
                    }
                }*/
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
