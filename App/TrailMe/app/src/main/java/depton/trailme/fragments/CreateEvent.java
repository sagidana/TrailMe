package depton.trailme.fragments;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import depton.net.trailme.R;
import depton.trailme.adapters.MyEventRecyclerViewAdapter;
import depton.trailme.adapters.MyGroupRecyclerViewAdapter;
import depton.trailme.data.RestCaller;
import depton.trailme.data.TrailMeListener;
import depton.trailme.models.Event;
import depton.trailme.models.Group;
import depton.trailme.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateEvent.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateEvent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEvent extends Fragment implements TrailMeListener,AdapterView.OnItemSelectedListener {
    private OnFragmentInteractionListener mListener;

    private RestCaller restUsersCaller = new RestCaller();
    private RestCaller restTracksCaller = new RestCaller();
    private RestCaller restEventCreateCaller = new RestCaller();
    private MyGroupRecyclerViewAdapter mGroupAdapter = null;
    private String mCurrentUserId;
    private String mChosenTrackId;
    private String mChosenGroupId;
    private String mChosenName;
    private CreateEvent mCtx;


    private List<String> GroupIdList;
    private List<String> GroupNameList;
    private List<String> TrackIdList;
    private List<String> TrackNameList;

    private ArrayAdapter<String> dataGroupAdapter;
    private ArrayAdapter<String> dataTrackAdapter;

    public CreateEvent() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CreateEvent newInstance(String userId) {
        CreateEvent fragment = new CreateEvent();
        Bundle args = new Bundle();
        args.putString("currentUser", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentUserId = getArguments().getString("currentUser");
        }
        restTracksCaller.delegate=this;
        restUsersCaller.delegate=this;
        restEventCreateCaller.delegate=this;
        mCtx=this;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_event, container, false);
        restUsersCaller.execute(this.getContext(), "getGroupsByUserId",mCurrentUserId);
        restTracksCaller.execute(this.getContext(), "getTracks");

        Button joinGroup = (Button) v.findViewById(R.id.btnCreateEvent);
        final EditText edt = ((EditText) v.findViewById(R.id.groupEditName));
        joinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChosenName = edt.getText().toString();
                try {
                    JSONObject event = new JSONObject();
                    event.put("Name", mChosenName);
                    event.put("GroupId", mChosenGroupId);
                    event.put("TrackId", mChosenTrackId);
                    restEventCreateCaller.execute(mCtx.getContext(), "addEvent", event);
                } catch (Exception ex) {
                }
            }
        });


        Spinner spinner = (Spinner) v.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        dataGroupAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,  new ArrayList<String>());
        dataGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataGroupAdapter);

        Spinner spinner2 = (Spinner) v.findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener(this);
        dataTrackAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,  new ArrayList<String>());
        dataTrackAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataTrackAdapter);

        return v;
    }
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
            mGroupAdapter= new MyGroupRecyclerViewAdapter(new ArrayList<Group>(), (GroupFragment.OnListFragmentInteractionListener) context);
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
        try{
            if(response != null) {
                if (response.has("groups")) {
                    JSONArray jGroups = response.getJSONArray("groups");

                    GroupIdList= new ArrayList<>(jGroups.length());
                    GroupNameList= new ArrayList<>(jGroups.length());
                    for (int i = 0; i < jGroups.length(); i++) {
                        GroupIdList.add(jGroups.getJSONObject(i).getString("Id"));
                        GroupNameList.add(jGroups.getJSONObject(i).getString("Name"));
                    }

                    dataGroupAdapter.clear();
                    dataGroupAdapter.addAll(GroupNameList);
                    dataGroupAdapter.notifyDataSetChanged();

                } else if (response.has("tracks")) {
                    JSONArray jTracks = response.getJSONArray("tracks");

                    TrackIdList= new ArrayList<>(jTracks.length());
                    TrackNameList= new ArrayList<>(jTracks.length());

                    for (int i = 0; i < jTracks.length(); i++) {
                        TrackIdList.add(jTracks.getJSONObject(i).getString("Id"));
                        TrackNameList.add(jTracks.getJSONObject(i).getString("Name"));
                    }

                    dataTrackAdapter.clear();
                    dataTrackAdapter.addAll(TrackNameList);
                    dataTrackAdapter.notifyDataSetChanged();

                } else {
                    Log.d("ERROR", "processFinish: Issues Connecting to the server");
                }
            }

        }
        catch (Exception e){
            Log.d("Sd", "processFinish: " + e.toString());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getTag().equals("groups")) {
            mChosenGroupId = GroupIdList.get(position);
        }
        if(parent.getTag().equals("tracks")) {
            mChosenTrackId = TrackIdList.get(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

