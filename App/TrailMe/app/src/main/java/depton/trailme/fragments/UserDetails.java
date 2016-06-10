package depton.trailme.fragments;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import depton.net.trailme.R;
import depton.trailme.adapters.MyEventRecyclerViewAdapter;
import depton.trailme.adapters.MyGroupRecyclerViewAdapter;
import depton.trailme.adapters.MyTrackRecyclerViewAdapter;
import depton.trailme.data.RestCaller;
import depton.trailme.data.TrailMeListener;
import depton.trailme.models.Enums;
import depton.trailme.models.Event;
import depton.trailme.models.Group;
import depton.trailme.models.Track;
import depton.trailme.models.User;

public class UserDetails extends Fragment implements TrailMeListener{

    private TracksFragment.OnListFragmentInteractionListener mTrackListener;
    private MyTrackRecyclerViewAdapter mTracksFragmentAdapter = null;
    private User user;
    private RestCaller mRestCaller = new RestCaller();
    private OnFragmentInteractionListener mListener;
    private ActionBar actionBar;

    // Tabs buttons
    private Button trackClicker;
    private Button groupClicker;
    private Button eventsClicker;

    android.support.v4.app.FragmentManager fragmentManager;
    final int selectedColor = Color.DKGRAY;
    private EventFragment.OnListFragmentInteractionListener mEventsListener;
    private MyEventRecyclerViewAdapter mEventsFragmentAdapter;
    private GroupFragment.OnListFragmentInteractionListener mGroupsListener;
    private MyGroupRecyclerViewAdapter mGroupsFragmentAdapter;

    public UserDetails() { }

    public static UserDetails newInstance(User user) {
        UserDetails fragment = new UserDetails();
        Bundle args = new Bundle();
        args.putParcelable("user",user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments(). getParcelable("user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRestCaller.delegate = this;

        View v = inflater.inflate(R.layout.fragment_user_details, container, false);

        fragmentManager = getFragmentManager();

        TextView fullName = (TextView) v.findViewById(R.id.fullName);
        fullName.setText(user.FirstName + " " + user.SurName);

        TextView city = (TextView)v.findViewById(R.id.city);
        city.setText(user.City);

        TextView birthdate = (TextView)v.findViewById(R.id.birthdate);
        SimpleDateFormat destFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH );

        ImageView genderImage = (ImageView)v.findViewById(R.id.userGenderImage);

        if(user.Gender.toLowerCase().equals("female")){
            genderImage.setImageResource(R.drawable.female);
        }

        String shorterDate = user.Birthdate;

        // TODO: consider remove those lines
        try {
            shorterDate = destFormat.format(new SimpleDateFormat(shorterDate)).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        birthdate.setText(shorterDate);

        final Context context = getContext();
        final TrailMeListener fragment = this;

        groupClicker = (Button) v.findViewById(R.id.groupClicker);
        groupClicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadGroupsFragment();
            }
        });

        eventsClicker = (Button) v.findViewById(R.id.eventsClicker);
        eventsClicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadEventsFragment();
            }
        });

        trackClicker = (Button) v.findViewById(R.id.trackClicker);
        trackClicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTracksFragment();
            }
        });

        loadTracksFragment();

        return v;
    }

    public void loadEventsFragment(){
        try {
            Fragment fragment = (Fragment) EventFragment.newInstance(1);
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            groupClicker.setBackgroundResource(android.R.drawable.btn_default);
            eventsClicker.setBackgroundColor(selectedColor);
            trackClicker.setBackgroundResource(android.R.drawable.btn_default);

            RestCaller restCaller = new RestCaller();
            restCaller.delegate = (TrailMeListener)fragment;
            restCaller.execute(fragment.getContext(), "getEventsByUserId", user.ID);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void loadGroupsFragment(){
        try {
            Fragment fragment = (Fragment) GroupFragment.newInstance(1);
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            groupClicker.setBackgroundColor(selectedColor);
            eventsClicker.setBackgroundResource(android.R.drawable.btn_default);
            trackClicker.setBackgroundResource(android.R.drawable.btn_default);

            RestCaller restCaller = new RestCaller();
            restCaller.delegate = (TrailMeListener)fragment;
            restCaller.execute(fragment.getContext(), "getGroupsByUserId", user.ID);
        }
        catch (Exception ex)
        {
        }
    }

    public void loadTracksFragment(){
        try {
            Fragment fragment = (Fragment) TracksFragment.newInstance(1);
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            groupClicker.setBackgroundResource(android.R.drawable.btn_default);
            eventsClicker.setBackgroundResource(android.R.drawable.btn_default);
            trackClicker.setBackgroundColor(selectedColor);

            RestCaller restCaller = new RestCaller();
            restCaller.delegate = (TrailMeListener)fragment;
            restCaller.execute(fragment.getContext(), "getTracksByUserId", user.ID);
        }
        catch (Exception ex)
        {
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener && context instanceof TracksFragment.OnListFragmentInteractionListener) {
            mTrackListener = (TracksFragment.OnListFragmentInteractionListener)context;
            mListener = (OnFragmentInteractionListener) context;
            mTracksFragmentAdapter = new MyTrackRecyclerViewAdapter(new ArrayList<Track>(), mTrackListener);

            mEventsListener = (EventFragment.OnListFragmentInteractionListener)context;
            mEventsFragmentAdapter = new MyEventRecyclerViewAdapter(new ArrayList<Event>(), mEventsListener);

            mGroupsListener = (GroupFragment.OnListFragmentInteractionListener)context;
            mGroupsFragmentAdapter = new MyGroupRecyclerViewAdapter(new ArrayList<Group>(), mGroupsListener);
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
        if(response != null)
        {
            if(response.has("tracks")){
                populateTracksToFragment(response);
            } else if(response.has("events")){
                populateEventsToFragment(response);
            } else if(response.has("groups")){
                populateGroupsToFragment(response);
            }
        }
    }

    public void populateTracksToFragment(JSONObject response){
        try {
            JSONArray jTracks = response.getJSONArray("tracks");
            ArrayList<Track> tracks = new ArrayList<>(jTracks.length());

            for (int i = 0; i < jTracks.length(); i++)
            {
                Track t = new Track();

                t.ID = jTracks.getJSONObject(i).getString("Id");
                t.Name = jTracks.getJSONObject(i).getString("Name");
                t.latitude = Double.parseDouble(jTracks.getJSONObject(i).getString("Latitude"));
                t.Length = Double.parseDouble(jTracks.getJSONObject(i).getString("Kilometers"));
                t.longitude = Double.parseDouble(jTracks.getJSONObject(i).getString("Longitude"));
                t.Difficulty = Enums.Difficulty.valueOf(jTracks.getJSONObject(i).getString("Difficulty"));

                tracks.add(t);
            }
            mTracksFragmentAdapter.updateList(tracks);
        }
        catch (Exception ex){

        }
    }

    public void populateGroupsToFragment(JSONObject response){
        try {
            ArrayList<Group> groupArrayList = new ArrayList<>();
            JSONArray jGroups = response.getJSONArray("groups");

            for(int i = 0; i < jGroups.length(); i++){
                groupArrayList.add(new Group(jGroups.getJSONObject(i)));
            }

            mGroupsFragmentAdapter.updateList(groupArrayList);
        }
        catch (Exception ex){

        }
    }

    public void populateEventsToFragment(JSONObject response){
        try {
            ArrayList<Event> eventsArrayList = new ArrayList<>();
            JSONArray jEvents = response.getJSONArray("events");

            Event tempEvent = null;

            for(int i = 0; i < jEvents.length(); i++){
                eventsArrayList.add(new Event(jEvents.getJSONObject(i)));
            }

            mEventsFragmentAdapter.updateList(eventsArrayList);
        }
        catch (Exception ex){

        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
