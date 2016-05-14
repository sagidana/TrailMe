package depton.trailme.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import depton.net.trailme.R;
import depton.trailme.data.RestCaller;
import depton.trailme.data.TrailMeListener;
import depton.trailme.models.Event;

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
    private RestCaller restUsersCaller = new RestCaller();
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


            /*restTracksCaller.delegate=this;
            restUsersCaller.delegate=this;
            restTracksCaller.execute(this.getContext(), "getUsersByGroupId",group.Id);
            restUsersCaller.execute(this.getContext(), "getEventsByGroupId", group.Id);*/
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
