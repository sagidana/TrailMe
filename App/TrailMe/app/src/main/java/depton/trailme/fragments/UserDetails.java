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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import depton.net.trailme.R;
import depton.trailme.adapters.MyTrackRecyclerViewAdapter;
import depton.trailme.data.RestCaller;
import depton.trailme.data.TrailMeListener;
import depton.trailme.models.Enums;
import depton.trailme.models.Track;
import depton.trailme.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserDetails extends Fragment  implements TrailMeListener{

    private TracksFragment.OnListFragmentInteractionListener mTrackListener;
    private MyTrackRecyclerViewAdapter mAdapter = null;
    private User user;
    private RestCaller mRestCaller = new RestCaller();
    private OnFragmentInteractionListener mListener;

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

        TextView username = (TextView)v.findViewById(R.id.userName);
        username.setText(user.Email);

        TextView firstname = (TextView)v.findViewById(R.id.firstname);
        firstname.setText(user.FirstName);

        TextView lastname = (TextView)v.findViewById(R.id.lastname);
        lastname.setText(user.SurName);

        TextView city = (TextView)v.findViewById(R.id.city);
        city.setText(user.City);

        TextView email = (TextView)v.findViewById(R.id.email);
        email.setText(user.Email);

        TextView birthdate = (TextView)v.findViewById(R.id.birthdate);
        birthdate.setText(user.Birthdate);

        RecyclerView trackView = (RecyclerView) v.findViewById(R.id.tracks);

        Context context = trackView.getContext();

        trackView.setLayoutManager(new LinearLayoutManager(context));

        mRestCaller.execute(this.getContext(),"getTracksByUserId", user.ID);

        trackView.setAdapter(mAdapter);

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
