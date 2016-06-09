package depton.trailme.filters;

import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import depton.trailme.models.Track;

/**
 * Created by win on 6/2/2016.
 */
public class TracksFilter extends Filter {
    private List<Track> trackList;
    private BaseAdapter listViewAdapter;

    public TracksFilter(List<Track> items, BaseAdapter list){
        listViewAdapter = list;
        trackList = items;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        FilterResults filterResults = new FilterResults();
        JSONArray stars = null;

        ArrayList<Integer> starsList = new ArrayList<>();

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(constraint.toString());
            stars = jsonObject.getJSONArray("stars");

            for(int i = 0; i < stars.length(); i++)
            {
                starsList.add(stars.getInt(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(starsList.size() > 0){
            filterResults.values = trackList;
            filterResults.count = trackList.size();
        } else {
            List<Track> filteredList = new ArrayList<>();

            for(Track track : trackList){
                if(starsList.contains(track.Rating)){
                    filteredList.add(track);
                }
            }

            filterResults.values = filteredList;
            filterResults.count = filteredList.size();
        }

        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        if (results.count == 0)
            listViewAdapter.notifyDataSetInvalidated();
        else {
            listViewAdapter.notifyDataSetChanged();
        }
    }
}
