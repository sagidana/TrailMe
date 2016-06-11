package depton.trailme.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import depton.net.trailme.R;
import depton.trailme.fragments.GroupFragment.OnListFragmentInteractionListener;
import depton.trailme.fragments.dummy.DummyContent.DummyItem;
import depton.trailme.models.Group;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyGroupRecyclerViewAdapter extends RecyclerView.Adapter<MyGroupRecyclerViewAdapter.ViewHolder> {

    private final List<Group> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyGroupRecyclerViewAdapter(List<Group> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_group_item, parent, false);
        return new ViewHolder(view);
    }

    public void updateList(List<Group> newlist) {
        mValues.clear();
        mValues.addAll(newlist);
        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Group group = mValues.get(position);
        holder.mItem = group;
        holder.mNameView.setText(group.Name);
        holder.mGroupMembers.setText("Members: " + group.Members);

        String allLanguages = "";

        if(group.Languages != null){
            for(int i = 0; i < group.Languages.length; i++) {
                if (i != 0)
                    allLanguages += ", ";
                allLanguages += group.Languages[i];
            }
        } else {
            allLanguages = "No Languages";
        }

        holder.mLanguages.setText(allLanguages);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public Group mItem;
        public final TextView mLanguages;
        public final TextView mGroupMembers;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.groupName);
            mGroupMembers = (TextView) view.findViewById(R.id.groupMembers);
            mLanguages = (TextView) view.findViewById(R.id.groupLanguages);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
