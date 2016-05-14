package depton.trailme.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import depton.net.trailme.R;
import depton.trailme.data.RestCaller;
import depton.trailme.data.TrailMeListener;
import depton.trailme.models.Event;
import depton.trailme.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateGroupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateGroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateGroupFragment extends Fragment implements TrailMeListener{

    private boolean isBeforeAddTopGroup = true;
    private String mCurrentUserId;
    private CreateGroupFragment mCtx;
    private RestCaller mCreateGroup = new RestCaller();
    private RestCaller mGetGroups= new RestCaller();
    private RestCaller mAddUserToGroup = new RestCaller();
    private View mGroupName;

    private GroupFragment.OnListFragmentInteractionListener mListener;

    public CreateGroupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateGroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateGroupFragment newInstance(String userId)
    {
        CreateGroupFragment fragment = new CreateGroupFragment();

        Bundle args = new Bundle();
        args.putString("currentUser", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCtx = this;

        if (getArguments() != null)
            mCurrentUserId = getArguments().getString("currentUser");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mCreateGroup.delegate = mCtx;
        mGetGroups.delegate = mCtx;
        mAddUserToGroup.delegate = mCtx;

        View view = inflater.inflate(R.layout.fragment_create_group, container, false);
        mGroupName = view.findViewById(R.id.groupName);

        Button createGroup = (Button) view.findViewById(R.id.createGroupBtn);

        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject groupToAdd = new JSONObject();
                    groupToAdd.put("Name", ((EditText) mGroupName).getText().toString());
                    mCreateGroup.execute(mCtx.getContext(), "addGroup", groupToAdd);

                    mGetGroups.execute(mCtx.getContext(), "getGroups");
                }catch (Exception e) { }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GroupFragment.OnListFragmentInteractionListener) {
            mListener = (GroupFragment.OnListFragmentInteractionListener) context;
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

        if (response == null )
            return;

        try {
            if (response.length() != 0){

                JSONArray groups = response.getJSONArray("groups");

                for (int i = 0; i < groups.length(); i++)
                {
                    String groupName = ((EditText)mGroupName).getText().toString();
                    JSONObject currGroup = groups.getJSONObject(i);

                    if (currGroup.getString("Name").equals(groupName))
                    {
                        String groupId = currGroup.getString("Id");
                        mAddUserToGroup.execute(mCtx.getContext(), "addUserToGroup", groupId, mCurrentUserId);
                    }
                }

            }
        }
        catch (Exception e) { }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
