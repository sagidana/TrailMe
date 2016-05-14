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
import depton.trailme.models.Enums;
import depton.trailme.models.Group;
import depton.trailme.models.Track;
import depton.trailme.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupDetails extends Fragment implements TrailMeListener {

    private Group group;
    private OnFragmentInteractionListener mListener;
    private RestCaller restUsersCaller = new RestCaller();
    private RestCaller restTracksCaller = new RestCaller();

    public GroupDetails() {
        // Required empty public constructor
    }

    public static GroupDetails newInstance(Group group) {
        GroupDetails fragment = new GroupDetails();
        Bundle args = new Bundle();
        args.putParcelable("group", group);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            group = getArguments().getParcelable("group");


            restTracksCaller.delegate=this;
            restUsersCaller.delegate=this;
            restTracksCaller.execute(this.getContext(), "getUsersByGroupId",group.Id);
            restUsersCaller.execute(this.getContext(), "getEventsByGroupId", group.Id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_group_details, container, false);
        TextView GroupName = (TextView)v.findViewById(R.id.GroupName);
        GroupName.setText(group.Id);
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

        Log.d("ProcessFpDetails", response.toString());

        try {
            if (response != null) {
                if(response.has("events"))
                {
                    JSONArray jEvents = response.getJSONArray("events");
                }
                else if(response.has("users")) {
                    JSONArray jUsers = response.getJSONArray("users");
                    ArrayList<User> users = new ArrayList<>(jUsers.length());

                    for (int i = 0; i < jUsers.length(); i++) {
                        User user = new User();
                        user.FirstName = jUsers.getJSONObject(i).getString("FirstName");
                        user.SurName = jUsers.getJSONObject(i).getString("LastName");
                        user.ID = jUsers.getJSONObject(i).getString("Id");

                        users.add(user);
                    }
                }
            }
        }
        catch (Exception Ex)
        {
            Log.d("REST", "Rest response: Failed to init users\tracks by group id");
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
