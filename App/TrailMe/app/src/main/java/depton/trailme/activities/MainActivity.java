package depton.trailme.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

import depton.net.trailme.R;
import depton.trailme.GoogleCloudMessaging.QuickstartPreferences;
import depton.trailme.GoogleCloudMessaging.RegistrationIntentService;
import depton.trailme.data.RestCaller;
import depton.trailme.data.TrailMeListener;
import depton.trailme.fragments.GroupDetails;
import depton.trailme.fragments.GroupFragment;
import depton.trailme.fragments.HikersFragment;
import depton.trailme.fragments.TrackDetails;
import depton.trailme.fragments.RecommendedTracksFragment;
import depton.trailme.fragments.TracksFragment;
import depton.trailme.fragments.UserDetails;
import depton.trailme.models.Group;
import depton.trailme.models.Track;
import depton.trailme.models.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        TrailMeListener,
        HikersFragment.OnListFragmentInteractionListener,
        TracksFragment.OnListFragmentInteractionListener,
        GroupFragment.OnListFragmentInteractionListener,
        TrackDetails.OnFragmentInteractionListener,
        GroupDetails.OnFragmentInteractionListener,
        UserDetails.OnFragmentInteractionListener
{

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "TrailMe";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private JSONObject mCurrentUser;
    public RestCaller restCaller = new RestCaller();
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle extras = getIntent().getExtras();
        String userId = extras.getString("currentUser");

        restCaller.delegate = this;
        restCaller.execute(this, "getUserById", userId);


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
}

    @Override
    protected void onResume() {
        super.onResume();
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
            Fragment fragment = null;
            fragment = (Fragment) UserDetails.newInstance(item);
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }
        catch (Exception ex)
        {
        }
    }
    public void onListFragmentInteraction(Track item)
    {
        try {
            Fragment fragment = null;
            fragment = (Fragment) TrackDetails.newInstance(item);
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }
        catch (Exception ex)
        {
        }
    }
    public void onListFragmentInteraction(Group item)
    {
        try {
            Fragment fragment = null;
            fragment = (Fragment) GroupDetails.newInstance(item);
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }
        catch (Exception ex)
        {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        TextView userExtendedName = (TextView)findViewById(R.id.userExtendedName);

        try{
            userExtendedName.setText(mCurrentUser.getString("MailAddress"));
        }
        catch (Exception e){ }

        getMenuInflater().inflate(R.menu.map, menu);
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

        if (id == R.id.nav_tracks) {
            fragmentClass = TracksFragment.class;
        } else if (id == R.id.nav_groups) {
            fragmentClass = GroupFragment.class;
        } else if (id == R.id.nav_hikers) {
            fragmentClass = HikersFragment.class;
        }else if (id == R.id.nav_recommended_tracks){
            fragmentClass = RecommendedTracksFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
            Bundle arguments = new Bundle();
            arguments.putString("currentUser", mCurrentUser.getString("Id"));
            fragment.setArguments(arguments);

        }catch(Exception e) {}

        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

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
        mCurrentUser = response;
        try {
            TextView displayedUserName = (TextView) findViewById(R.id.userExtendedName);
            displayedUserName.setText(mCurrentUser.getString("MailAddress"));

            Fragment fragment = TracksFragment.class.newInstance();
            Bundle arguments = new Bundle();
            arguments.putString("currentUser", mCurrentUser.getString("Id"));
            fragment.setArguments(arguments);
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        }catch (Exception e){}
    }
}
