package depton.trailme.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONObject;

import depton.net.trailme.R;
import depton.trailme.DAL.TrailMeServer;
import depton.trailme.GoogleCloudMessaging.QuickstartPreferences;
import depton.trailme.GoogleCloudMessaging.RegistrationIntentService;
import depton.trailme.data.RestCaller;
import depton.trailme.data.TrailMeListener;
import depton.trailme.fragments.CreateEvent;
import depton.trailme.fragments.CreateGroupFragment;
import depton.trailme.fragments.EventDetails;
import depton.trailme.fragments.EventFragment;
import depton.trailme.fragments.GmapFragment;
import depton.trailme.fragments.GmapFragment;
import depton.trailme.fragments.GroupDetails;
import depton.trailme.fragments.GroupFragment;
import depton.trailme.fragments.UsersFragment;
import depton.trailme.fragments.TrackDetails;
import depton.trailme.fragments.RecommendedTracksFragment;
import depton.trailme.fragments.TracksFilterFragment;
import depton.trailme.fragments.TracksFragment;
import depton.trailme.fragments.UserDetails;
import depton.trailme.models.Event;
import depton.trailme.models.Group;
import depton.trailme.models.Track;
import depton.trailme.models.User;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        TrailMeListener,
        UsersFragment.OnListFragmentInteractionListener,
        TracksFragment.OnListFragmentInteractionListener,
        GroupFragment.OnListFragmentInteractionListener,
        EventFragment.OnListFragmentInteractionListener,
        TrackDetails.OnFragmentInteractionListener,
        GroupDetails.OnFragmentInteractionListener,
        CreateEvent.OnFragmentInteractionListener,
        EventDetails.OnFragmentInteractionListener,
        UserDetails.OnFragmentInteractionListener,
        TracksFilterFragment.OnFragmentInteractionListener,
        GmapFragment.OnFragmentInteractionListener
{

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "TrailMe";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private User mCurrentUser;
    public RestCaller restCaller = new RestCaller();
    FragmentManager fragmentManager = getSupportFragmentManager();

    public MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //Bundle extras = getIntent().getExtras();
        //String userId = extras.getString("currentUser");

        restCaller.delegate = this;
        TrailMeServer.getInstance(this);

        mCurrentUser = User.getUserFromSharedPref(this);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.nav_header);




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //startGCMRegistration();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Berthold.otf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
}

    @Override
    protected void onStart(){
        super.onStart();
        TrailMeServer.getInstance(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        TrailMeServer.getInstance(this);
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void onFragmentInteraction(Uri uri)
    {
    }

    public void onListFragmentInteraction(User item)
    {
        try {
            Fragment fragment = (Fragment) UserDetails.newInstance(item);
            fragmentManager.beginTransaction()
                    .add(fragment, fragment.getClass().getName())
                    .addToBackStack(fragment.getClass().getName())
                    .replace(R.id.flContent, fragment).commit();
        }
        catch (Exception ex)
        {
        }
    }
    public void onListFragmentInteraction(Track item)
    {
        try {
            Fragment fragment = (Fragment) TrackDetails.newInstance(item, mCurrentUser.ID);
            fragmentManager.beginTransaction()
                    .add(fragment, fragment.getClass().getName())
                    .addToBackStack(fragment.getClass().getName())
                    .replace(R.id.flContent, fragment).commit();
        }
        catch (Exception ex)
        {
            Log.e("MainActivity", "cant get tracks details fragment");
        }
    }
    public void onListFragmentInteraction(Group item)
    {
        try {
            Fragment fragment = (Fragment) GroupDetails.newInstance(item, mCurrentUser.ID);

            fragmentManager.beginTransaction()
                    .add(fragment, fragment.getClass().getName())
                    .addToBackStack(fragment.getClass().getName())
                    .replace(R.id.flContent, fragment).commit();
        }
        catch (Exception ex)
        {
        }
    }

    public void onListFragmentInteraction(Event item)
    {
        try {
            Fragment fragment = (Fragment) EventDetails.newInstance(item);
            fragmentManager.beginTransaction()
                    .add(fragment, fragment.getClass().getName())
                    .addToBackStack(fragment.getClass().getName())
                    .replace(R.id.flContent, fragment).commit();
        }
        catch (Exception ex)
        {
        }
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        TextView userExtendedName = (TextView)findViewById(R.id.userExtendedName);
        final ImageView imageView = (ImageView) findViewById(R.id.profileCircle);


        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Bitmap bitmap1 = getRoundedCornerBitmap(bitmap, 300);
                imageView.setImageBitmap(bitmap1);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        if(imageView != null && mCurrentUser.ImageUrl.length() > 0)
            Picasso.with(this).load(mCurrentUser.ImageUrl).into(target);

        userExtendedName.setText(mCurrentUser.FirstName + " " + mCurrentUser.SurName);



        getMenuInflater().inflate(R.menu.map, menu);
        menuItem = menu.findItem(R.id.filter);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;
        Class fragmentClass = null;

        // Handle navigation view item clicks here.
        int id = item.getItemId();


        String restAction = "";

        if (id == R.id.nav_tracks) {
            fragmentClass = TracksFragment.class;
            restAction = "getTracks";
        } else if (id == R.id.nav_groups) {
            restAction = "getGroups";
            fragmentClass = GroupFragment.class;
        } else if (id == R.id.nav_hikers) {
            restAction = "getUsers";
            fragmentClass = UsersFragment.class;
        }else if (id == R.id.nav_events) {
            restAction = "getEvents";
            fragmentClass = EventFragment.class;
        }else if (id == R.id.nav_recommended_tracks){
            restAction = "getRcommendations";
            fragmentClass = RecommendedTracksFragment.class;
        }else if (id == R.id.nav_create_event) {
            fragmentClass= CreateEvent.class;
        }else if (id == R.id.nav_create_group){
            fragmentClass = CreateGroupFragment.class;
        } else if(id == R.id.nav_logout){
            User.userLogout(this);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        else if (id==R.id.nav_map){
            fragmentClass = GmapFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
            Bundle arguments = new Bundle();
            arguments.putString("currentUser", mCurrentUser.ID);
            fragment.setArguments(arguments);

            if(restAction.length() > 0 && fragment instanceof TrailMeListener){
                RestCaller restCaller = new RestCaller();
                restCaller.delegate = (TrailMeListener)fragment;
                restCaller.execute(fragment.getContext(), restAction);
            }

        }catch(Exception e) {}

        fragmentManager.beginTransaction()
                .add(fragment, fragmentClass.getName())
                .addToBackStack(fragmentClass.getName())
                .replace(R.id.flContent, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void startGCMRegistration()
    {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {

                } else {

                }
            }
        };

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    public void setFilterAction(MenuItem.OnMenuItemClickListener action){
        menuItem.setOnMenuItemClickListener(action);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    public void processFinish(JSONObject response)
    {
        int x = 5;
        /*try {
            TextView displayedUserName = (TextView) findViewById(R.id.userExtendedName);
            displayedUserName.setText(mCurrentUser.Email);

            Fragment fragment = TracksFragment.class.newInstance();
            Bundle arguments = new Bundle();
            arguments.putString("currentUser", mCurrentUser.getString("Id"));
            fragment.setArguments(arguments);
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        }catch (Exception e){}*/
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
