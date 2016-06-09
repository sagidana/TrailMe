package depton.trailme.fragments;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import depton.net.trailme.R;
import depton.trailme.adapters.MyTrackRecyclerViewAdapter;
import depton.trailme.data.RestCaller;
import depton.trailme.data.TrailMeListener;
import depton.trailme.models.Enums;
import depton.trailme.models.Track;
import depton.trailme.models.User;

public class UserDetails extends Fragment  implements TrailMeListener{

    private TracksFragment.OnListFragmentInteractionListener mTrackListener;
    private MyTrackRecyclerViewAdapter mAdapter = null;
    private User user;
    private RestCaller mRestCaller = new RestCaller();
    private OnFragmentInteractionListener mListener;
    private ActionBar actionBar;

    private Button trackClicker;
    private Button groupClicker;
    private Button eventsClicker;

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

        /*TextView username = (TextView)v.findViewById(R.id.userName);
        username.setText(user.Email);*/

        TextView fullName = (TextView) v.findViewById(R.id.fullName);
        fullName.setText(user.FirstName + " " + user.SurName);

        TextView city = (TextView)v.findViewById(R.id.city);
        city.setText(user.City);

        /*TextView email = (TextView)v.findViewById(R.id.email);
        email.setText(user.Email);*/

        TextView birthdate = (TextView)v.findViewById(R.id.birthdate);
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH );
        String shorterDate = user.Birthdate;

        try {
            shorterDate = format.parse(user.Birthdate).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        birthdate.setText(shorterDate);

        final android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();

        final int selectedColor = Color.DKGRAY;

        groupClicker = (Button) v.findViewById(R.id.groupClicker);

        groupClicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Fragment fragment = (Fragment) GroupFragment.newInstance(1);
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                    groupClicker.setBackgroundColor(selectedColor);
                    eventsClicker.setBackgroundResource(android.R.drawable.btn_default);
                    trackClicker.setBackgroundResource(android.R.drawable.btn_default);
                }
                catch (Exception ex)
                {
                }
            }
        });

        eventsClicker = (Button) v.findViewById(R.id.eventsClicker);

        eventsClicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Fragment fragment = (Fragment) EventFragment.newInstance(1);
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                    groupClicker.setBackgroundResource(android.R.drawable.btn_default);
                    eventsClicker.setBackgroundColor(selectedColor);
                    trackClicker.setBackgroundResource(android.R.drawable.btn_default);
                }
                catch (Exception ex)
                {
                }
            }
        });

        trackClicker = (Button) v.findViewById(R.id.trackClicker);

        trackClicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Fragment fragment = (Fragment) TracksFragment.newInstance(1);
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                    groupClicker.setBackgroundResource(android.R.drawable.btn_default);
                    eventsClicker.setBackgroundResource(android.R.drawable.btn_default);
                    trackClicker.setBackgroundColor(selectedColor);
                }
                catch (Exception ex)
                {
                }
            }
        });

        /*RecyclerView trackView = (RecyclerView) v.findViewById(R.id.tracks);

        Context context = trackView.getContext();

        trackView.setLayoutManager(new LinearLayoutManager(context));

        mRestCaller.execute(this.getContext(),"getTracksByUserId", user.ID);

        trackView.setAdapter(mAdapter);
        */

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener && context instanceof TracksFragment.OnListFragmentInteractionListener) {
            mTrackListener = (TracksFragment.OnListFragmentInteractionListener)context;
            mListener = (OnFragmentInteractionListener) context;
            mAdapter = new MyTrackRecyclerViewAdapter(new ArrayList<Track>(), mTrackListener);
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
            if(response != null)
            {
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
                mAdapter.updateList(tracks);
            }
        }
        catch (Exception e){ }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
