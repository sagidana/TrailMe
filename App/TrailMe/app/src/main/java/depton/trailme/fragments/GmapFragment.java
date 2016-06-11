package depton.trailme.fragments;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import depton.net.trailme.R;
import depton.trailme.data.RestCaller;
import depton.trailme.data.TrailMeListener;
import depton.trailme.models.Track;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Gmap.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Gmap#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GmapFragment extends Fragment implements OnMapReadyCallback,TrailMeListener {

    private GoogleMap mMap;
    private RestCaller restCaller = new RestCaller();
    private OnFragmentInteractionListener mListener;

    public GmapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Gmap.
     */
    // TODO: Rename and change types and number of parameters
    public static GmapFragment newInstance() {
        GmapFragment fragment = new GmapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        restCaller.delegate = this;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_gmap, container, false);
        //SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
          //      .findFragmentById(R.id.gomap);
        SupportMapFragment mSupportMapFragment;
        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.gomap);
        if(mSupportMapFragment == null)
        {
            android.support.v4.app.FragmentManager fm = getFragmentManager();
            android.support.v4.app.FragmentTransaction ft =  fm.beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.gomap, mSupportMapFragment).commit();

        }
        if(mSupportMapFragment != null)
        {
            mSupportMapFragment.getMapAsync(this);
        }

        //SupportMapFragment mapFragment = (SupportMapFragment) v.findViewById(R.id.gomap);
        //mapFragment.getMapAsync(this);
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        restCaller.execute(this.getContext(), "getTracks");
    }

    @Override
    public void processFinish(JSONObject response) {
        try {
            if (response != null) {
                JSONArray jTracks = response.getJSONArray("tracks");

                ArrayList<Track> tracks = new ArrayList<>(jTracks.length());

                for (int i = 0; i < jTracks.length(); i++) {
                    Track t = new Track(jTracks.getJSONObject(i));
                    float[] results = new float[1];
                    Location.distanceBetween(32.181281, 34.934381,t.latitude,t.longitude,results);
                    mMap.addMarker(new MarkerOptions().position(new LatLng(t.latitude, t.longitude)).title(t.Name + ", Distance:" + Float.toString((results[0]/1000) )));
                        tracks.add(t);
                    Log.d("Tracks", "TrackFragment - processFinish: Added track " + t.Name + " in ID" + String.valueOf(i) + " ");
                }
            } else {
                Log.d("ERROR", "processFinish: Issues Connecting to the server");
            }

        } catch (Exception e) {
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
