package depton.trailme.fragments;

import android.content.Context;
import android.media.Image;
import android.media.Rating;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import depton.net.trailme.R;
import depton.trailme.data.RestCaller;
import depton.trailme.data.TrailMeListener;
import depton.trailme.models.Track;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrackDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrackDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackDetails extends Fragment implements TrailMeListener{

    private Track track;
    private TrackDetails mCtx;
    private RestCaller addUserToTrack = new RestCaller();
    private String mCurrentUserId;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private OnFragmentInteractionListener mListener;

    public TrackDetails() { }

    public static TrackDetails newInstance(Track track, String userId) {
        TrackDetails fragment = new TrackDetails();
        Bundle args = new Bundle();
        args.putParcelable("track", track);
        args.putString("currentUser", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCtx = this;
            track = getArguments().getParcelable("track");
            mCurrentUserId = getArguments().getString("currentUser");
        }

        /*viewPager = (ViewPager) getView().findViewById(R.id.viewpager);
        setupViewPager(viewPager);*/

        /*tabLayout = (TabLayout) getView().findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);*/
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new TracksFragment(), "Tracks");
        adapter.addFragment(new TracksFragment(), "Tracks");
        adapter.addFragment(new TracksFragment(), "Tracks");
        viewPager.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        addUserToTrack.delegate = mCtx;

        View v = inflater.inflate(R.layout.fragment_track_details, container, false);

        TextView TrackName = (TextView) v.findViewById(R.id.trackName);
        TrackName.setText(track.Name);

        TextView trackDesc = (TextView) v.findViewById(R.id.trackDesc);
        trackDesc.setText(track.TrackDescription);

        TextView kilometers = (TextView) v.findViewById(R.id.kilometers);
        kilometers.setText(Double.toString(track.Length) + " KM");

        TextView trackCategories = (TextView) v.findViewById(R.id.categories);

        RatingBar ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        ratingBar.setRating(track.Rating);

        String allCategories = "";

        if(track.Categories != null){
            for(int i = 0; i < track.Categories.length; i++) {
                if (i != 0)
                    allCategories += ", ";
                allCategories += track.Categories[i];
            }
        } else {
            allCategories = "No Categories";
        }
        trackCategories.setText(allCategories);

        ShowBoots(track.Difficulty.ordinal(), v);

        TextView zone = (TextView) v.findViewById(R.id.zone);
        zone.setText(track.Zone);

/*        TextView latitude = (TextView) v.findViewById(R.id.latitude);
        latitude.setText(Double.toString(track.latitude));

        TextView longitude = (TextView) v.findViewById(R.id.longitude);
        longitude.setText(Double.toString(track.longitude));*/

        Button addUserToTrackbtn = (Button) v.findViewById(R.id.addUserToTrackBtn);
        addUserToTrackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserToTrack.execute(mCtx.getContext(), "addUserToTrack", track.ID, mCurrentUserId);
            }
        });

        return v;
    }

    public void ShowBoots(int level, View view){
        LinearLayout bootContainer = (LinearLayout)view.findViewById(R.id.bootContainer);

        ImageView tempImageView;

        LinearLayout.LayoutParams layoutParams = new AppBarLayout.LayoutParams(70, 70);

        final int MAX_LEVEL = 5;

        // Create imageView with full boot
        for(int i = 1; i < MAX_LEVEL - level; i++){
            tempImageView = new ImageView(getContext());
            tempImageView.setImageResource(R.drawable.full_boot);
            tempImageView.setLayoutParams(layoutParams);
            tempImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            bootContainer.addView(tempImageView);
        }

        for(int i = MAX_LEVEL - level; i < MAX_LEVEL + 1; i++){
            tempImageView = new ImageView(getContext());
            tempImageView.setImageResource(R.drawable.empty_boot);
            tempImageView.setLayoutParams(layoutParams);
            tempImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            bootContainer.addView(tempImageView);
        }
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

    public void processFinish(JSONObject response) {
        // TODO:
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
