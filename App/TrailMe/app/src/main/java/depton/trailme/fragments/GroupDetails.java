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
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import depton.net.trailme.R;
import depton.trailme.adapters.MyEventRecyclerViewAdapter;
import depton.trailme.adapters.MyHikerRecyclerViewAdapter;
import depton.trailme.data.RestCaller;
import depton.trailme.data.TrailMeListener;
import depton.trailme.models.Enums;
import depton.trailme.models.Event;
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
    private GroupDetails mCtx;
    private String mCurrentUserId;
    private OnFragmentInteractionListener mListener;
    private RestCaller restAddUserToGroup = new RestCaller();
    private RestCaller restUsersCaller = new RestCaller();
    private RestCaller restTracksCaller = new RestCaller();
    private MyHikerRecyclerViewAdapter mUsersAdapter = null;
    private MyEventRecyclerViewAdapter mEventsAdapter = null;

    public GroupDetails() {
        // Required empty public constructor
    }

    public static GroupDetails newInstance(Group group , String userId) {
        GroupDetails fragment = new GroupDetails();
        Bundle args = new Bundle();
        args.putParcelable("group", group);
        args.putString("currentUser", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCtx = this;
        if (getArguments() != null) {
            mCurrentUserId = getArguments().getString("currentUser");
            group = getArguments().getParcelable("group");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_group_details, container, false);
        TextView GroupName = (TextView)v.findViewById(R.id.groupName);
        GroupName.setText(group.Name);

        Button joinGroup = (Button)v.findViewById(R.id.joinGroup);
        joinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restAddUserToGroup.delegate = mCtx;
                restAddUserToGroup.execute(mCtx.getContext(),"addUserToGroup", group.Id, mCurrentUserId);
            }
        });

        View usersList = v.findViewById(R.id.users);
        View eventsList = v.findViewById(R.id.events);

        // Set the adapter
        if (usersList instanceof RecyclerView && eventsList instanceof RecyclerView) {

            Context eventsCtx = eventsList.getContext();
            RecyclerView eventsRecyclerView = (RecyclerView) eventsList;
            eventsRecyclerView.setLayoutManager(new LinearLayoutManager(eventsCtx));

            Context usersCtx = usersList.getContext();
            RecyclerView usersRecyclerView = (RecyclerView) usersList;
            usersRecyclerView.setLayoutManager(new LinearLayoutManager(usersCtx));

            restTracksCaller.delegate = this;
            restUsersCaller.delegate = this;
            restTracksCaller.execute(this.getContext(), "getUsersByGroupId", group.Id);
            restUsersCaller.execute(this.getContext(), "getEventsByGroupId", group.Id);

            usersRecyclerView.setAdapter(mUsersAdapter);
            eventsRecyclerView.setAdapter(mEventsAdapter);
        }

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HikersFragment.OnListFragmentInteractionListener && context instanceof EventFragment.OnListFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            mUsersAdapter= new MyHikerRecyclerViewAdapter(new ArrayList<User>(), (HikersFragment.OnListFragmentInteractionListener) context);
            mEventsAdapter = new MyEventRecyclerViewAdapter(new ArrayList<Event>(), (EventFragment.OnListFragmentInteractionListener) context);
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

                    ArrayList<Event> events = new ArrayList<>(jEvents.length());

                    for (int i = 0; i < jEvents.length(); i++) {
                        Event event = new Event();

                        event.ID = jEvents.getJSONObject(i).getString("Id");
                        event.Name = jEvents.getJSONObject(i).getString("Name");

                        events.add(event);
                    }

                    mEventsAdapter.updateList(events);
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

                    mUsersAdapter.updateList(users);
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
