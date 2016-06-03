package depton.trailme.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import org.json.JSONArray;

import java.util.Set;

import depton.net.trailme.R;
import depton.trailme.activities.MainActivity;
import depton.trailme.models.Track;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TracksFilterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TracksFilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TracksFilterFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Set<Integer> checkedStars;

    private OnFragmentInteractionListener mListener;

    private AppCompatButton saveButton;

    public TracksFilterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TracksFilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TracksFilterFragment newInstance(String param1, String param2) {
        TracksFilterFragment fragment = new TracksFilterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void onFilterChecked(View view){
        boolean checked = ((CheckBox) view).isChecked();

        int filterToModidy = 1;

        switch (view.getId()){
            case R.id.star1_checkbox:
                filterToModidy = 1;
                break;
            case R.id.star2_checkbox:
                filterToModidy = 2;
                break;
            case R.id.star3_checkbox:
                filterToModidy = 3;
                break;
            case R.id.star4_checkbox:
                filterToModidy = 4;
                break;
            case R.id.star5_checkbox:
                filterToModidy = 5;
                break;
        }

        if(checked){
            checkedStars.add(filterToModidy);
        } else {
            checkedStars.remove(filterToModidy);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tracks_filter, container, false);
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
    public void onStart() {
        super.onStart();
        saveButton = (AppCompatButton) getView().findViewById(R.id.saveFilter);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray jsonArray = new JSONArray(checkedStars);
                MainActivity tracksFragment = (MainActivity) getActivity();
                //tracksFragment.getTracksFragment().getFilter().filter(jsonArray.toString());
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
