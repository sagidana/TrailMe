package depton.trailme.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import depton.trailme.models.Event;
import depton.trailme.models.Group;
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
    private RestCaller mRestCaller = new RestCaller();


    private EventFragment.OnListFragmentInteractionListener mEventsListener;

    private Button eventsBtn;
    private Button userBtn;
    final int selectedColor = Color.DKGRAY;

    android.support.v4.app.FragmentManager fragmentManager;
    private UsersFragment.OnListFragmentInteractionListener mUsersListener;

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

        //mRestCaller.delegate = this;
        View v =inflater.inflate(R.layout.fragment_group_details, container, false);
        fragmentManager = getFragmentManager();
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

        TextView members = (TextView) v.findViewById(R.id.groupMembers);
        members.setText("Members: " + group.Members);

        /*View usersList = v.findViewById(R.id.users);
        View eventsList = v.findViewById(R.id.events);*/





        // Set the adapter
        /*if (usersList instanceof RecyclerView && eventsList instanceof RecyclerView) {

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
        }*/

        userBtn = (Button)v.findViewById(R.id.usersBtn);
        eventsBtn = (Button)v.findViewById(R.id.eventsBtn);

        userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUsersFragment();
            }
        });

        eventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadEventsFragment();
            }
        });

        loadUsersFragment();

        return v;
    }

    public void loadEventsFragment(){
        try {
            Fragment fragment = (Fragment) EventFragment.newInstance(1);
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            userBtn.setBackgroundResource(android.R.drawable.btn_default);
            eventsBtn.setBackgroundColor(selectedColor);

            RestCaller restCaller = new RestCaller();
            restCaller.delegate = (TrailMeListener)fragment;
            restCaller.execute(fragment.getContext(), "getEventsByGroupId", group.Id);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void loadUsersFragment(){
        try {
            Fragment fragment = (Fragment) EventFragment.newInstance(1);
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            userBtn.setBackgroundColor(selectedColor);
            eventsBtn.setBackgroundResource(android.R.drawable.btn_default);

            RestCaller restCaller = new RestCaller();
            restCaller.delegate = (TrailMeListener)fragment;
            restCaller.execute(fragment.getContext(), "getUsersByGroupId", group.Id);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UsersFragment.OnListFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;

            mEventsListener = (EventFragment.OnListFragmentInteractionListener) context;
            mUsersListener = (UsersFragment.OnListFragmentInteractionListener) context;

            mUsersAdapter= new MyHikerRecyclerViewAdapter(new ArrayList<User>(), mUsersListener);
            mEventsAdapter = new MyEventRecyclerViewAdapter(new ArrayList<Event>(), mEventsListener);
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

        try {
            if (response != null) {
                if(response.has("events"))
                {
                    JSONArray jEvents = response.getJSONArray("events");

                    ArrayList<Event> events = new ArrayList<>(jEvents.length());

                    for (int i = 0; i < jEvents.length(); i++) {
                        Event event = new Event(jEvents.getJSONObject(i));
                        events.add(event);
                    }

                    mEventsAdapter.updateList(events);
                }
                else if(response.has("users")) {
                    JSONArray jUsers = response.getJSONArray("users");
                    ArrayList<User> users = new ArrayList<>(jUsers.length());

                    for (int i = 0; i < jUsers.length(); i++) {
                        User user = new User(jUsers.getJSONObject(i));
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
