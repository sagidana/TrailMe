package depton.trailme.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import depton.net.trailme.R;
import depton.trailme.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserDetails extends Fragment {
    private User user;

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
