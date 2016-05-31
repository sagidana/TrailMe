package depton.trailme.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import depton.net.trailme.R;
import depton.trailme.fragments.TracksFragment.OnListFragmentInteractionListener;
import depton.trailme.models.Track;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MyTrackRecyclerViewAdapter extends RecyclerView.Adapter<MyTrackRecyclerViewAdapter.ViewHolder> {

    private final List<Track> mValues;
    private final OnListFragmentInteractionListener mListener;

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                connection.disconnect();
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if(result != null)
                imageView.setImageBitmap(result);
        }

    }

    public MyTrackRecyclerViewAdapter(List<Track> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void updateList(List<Track> newlist) {
        mValues.clear();
        mValues.addAll(newlist);
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_track_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Drawable d = holder.mRatingBar.getProgressDrawable();
        d.setColorFilter(Color.parseColor("#3a802f"), PorterDuff.Mode.SRC_ATOP);
        holder.mContainer.setAlpha(0.75f);
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).Name);
        holder.mRatingBar.setRating(mValues.get(position).Rating);

        /* TODO: get the url from the db */
        /*String url = "https://www.imperial.ac.uk/ImageCropToolT4/imageTool/uploaded-images/LONDON_shutterstock_229478404--tojpeg_1417791048879_x1.jpg";
        ImageLoadTask imageLoadTask = new ImageLoadTask(url,holder.mTrackImg);
        imageLoadTask.execute();*/


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
        public final TextView mIdView;
        /*public final TextView mDescriptionView;*/
        public final RatingBar mRatingBar;
        public Track mItem;
        public final ImageView mTrackImg;
        public final LinearLayout mContainer;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.name);/*
            mDescriptionView = (TextView) view.findViewById(R.id.Description);*/
            mRatingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            mTrackImg = (ImageView) view.findViewById(R.id.ProfileImage);
            mContainer = (LinearLayout) view.findViewById(R.id.whiteContainer);
        }

        @Override
        public String toString() {
            /*return super.toString() + " '" + mDescriptionView.getText() + "'";*/
            return super.toString();
        }
    }
}
