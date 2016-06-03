package depton.trailme.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import depton.net.trailme.R;
import depton.trailme.adapters.MyHikerRecyclerViewAdapter;
import depton.trailme.data.TrailMeListener;
import depton.trailme.data.RestCaller;
import depton.trailme.models.User;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class UsersFragment extends Fragment implements TrailMeListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private MyHikerRecyclerViewAdapter mAdapter = null;
    private RestCaller restCaller = new RestCaller();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UsersFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static UsersFragment newInstance(int columnCount) {
        UsersFragment fragment = new UsersFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        restCaller.delegate = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hiker_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            restCaller.execute(this.getContext(), "getUsers");
            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }

    @Override
    public void processFinish(JSONObject response) {
        try{
            if(response != null) {
                JSONArray jUsers = response.getJSONArray("users");
                ArrayList<User> users = new ArrayList<>(jUsers.length());

                for (int i = 0; i < jUsers.length(); i++)
                {
                    User user = new User();
                    user.FirstName = jUsers.getJSONObject(i).getString("FirstName");
                    user.SurName = jUsers.getJSONObject(i).getString("LastName");
                    user.ID = jUsers.getJSONObject(i).getString("Id");
                    user.Email = jUsers.getJSONObject(i).getString("MailAddress");
                    user.City = jUsers.getJSONObject(i).getString("City");
                    user.Birthdate = jUsers.getJSONObject(i).getString("BirthDate");
                    user.Gender = jUsers.getJSONObject(i).getString("Gender");
                    user.Languages = getLanguagesArr(jUsers.getJSONObject(i).getJSONArray("Languages"));

                    users.add(user);
                }

                mAdapter.updateList(users);
            }
            else {
                Log.d("ERROR", "processFinish: Issues Connecting to the server");
            }
        }
        catch (Exception e){
            Log.d("Sd", "processFinish: " + e.toString());
        }
    }

    public String[] getLanguagesArr(JSONArray jsonArray){
        String [] list = new String[jsonArray.length()];
        try {
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i=0;i<len;i++){
                    list[i] = jsonArray.getJSONObject(i).getString("Name");
                }
            }
        }
        catch (Exception ex){
        }

        return list;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
            mAdapter= new MyHikerRecyclerViewAdapter(new ArrayList<User>(), mListener);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(User item);
    }
}
