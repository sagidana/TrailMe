package depton.trailme.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import depton.net.trailme.R;
import depton.trailme.DAL.TrailMeServer;
import depton.trailme.authenticator.AuthenticationManager;
import depton.trailme.data.TrailMeListener;
import depton.trailme.data.RestCaller;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements TrailMeListener,LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private RestCaller restCaller = new RestCaller();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        restCaller.delegate=this;

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);



        try{
            JSONObject jsonObject = new JSONObject("{Tracks=[{Id=23e47bbd-a47f-4005-b603-00d67bbe842a, Name=River Hamud, Latitude=32.874865, Longitude=35.515653, Zone=Kineret, Difficulty=Medium, DistanceKM=4, Events=[{Id=d88a9255-be10-4b17-a12e-cc79491776b9, Name=My Family, StartDate=2016-04-01T00:00:00, EndDate=2016-04-02T00:00:00}, {Id=0b62264d-cfd0-4373-b3a7-e35274f42770, Name=Passover 2016, StartDate=2016-02-05T00:00:00, EndDate=2016-05-05T00:00:00}]}, {Id=e8eecbea-5a47-45f9-b6da-b6e220dfaa0d, Name=River Shofet, Latitude=32.633706, Longitude=35.103318, Zone=Yokneam, Difficulty=Easy, DistanceKM=2, Events=[]}]}");
        }
        catch(Exception e){}

        try{
            JSONObject jsonObject = new JSONObject("{Tracks:[{Id:23e47bbd-a47f-4005-b603-00d67bbe842a, Name:River Hamud, Latitude:32.874865, Longitude:35.515653, Zone:Kineret, Difficulty:Medium, DistanceKM:4, Events:[{Id:d88a9255-be10-4b17-a12e-cc79491776b9, Name:My Family, StartDate:2016-04-01T00:00:00, EndDate:2016-04-02T00:00:00}, {Id:0b62264d-cfd0-4373-b3a7-e35274f42770, Name:Passover 2016, StartDate:2016-02-05T00:00:00, EndDate:2016-05-05T00:00:00}]}, {Id:e8eecbea-5a47-45f9-b6da-b6e220dfaa0d, Name:River Shofet, Latitude:32.633706, Longitude:35.103318, Zone:Yokneam, Difficulty:Easy, DistanceKM:2, Events:[]}]}");
        }
        catch(Exception e){}

        try{
            JSONObject jsonObject = new JSONObject("{\"ip\": \"109.64.76.125\"}");
        }
        catch(Exception e){}
        try{
            JSONObject jsonObject = new JSONObject("{\"Tracks=[{\"Id\"=\"23e47bbd-a47f-4005-b603-00d67bbe842a\", \"Name=River Hamud\", \"Latitude\"=\"32.874865\", \"Longitude\"=\"35.515653\", \"Zone\"=\"Kineret\", \"Difficulty\"=\"Medium\", \"DistanceKM\"=\"4\", \"Events\"=[{\"Id\"=\"d88a9255-be10-4b17-a12e-cc79491776b9\", \"Name\"=\"My Family\", \"StartDate\"=\"2016-04-01T00:00:00\", \"EndDate=2016-04-02T00:00:00\"}, {\"Id\"=\"0b62264d-cfd0-4373-b3a7-e35274f42770\", \"Name\"=\"Passover 2016\", \"StartDate\"=\"2016-02-05T00:00:00\", \"EndDate\"=\"2016-05-05T00:00:00\"}]}, {\"Id\"=\"e8eecbea-5a47-45f9-b6da-b6e220dfaa0d\", \"Name\"=\"River Shofet\", \"Latitude\"=\"32.633706\", \"Longitude\"=\"35.103318\", \"Zone\"=\"Yokneam\", \"Difficulty\"=\"Easy\", \"DistanceKM\"=\"2\", \"Events\"=[]}]}");
        }
        catch(Exception e){}
        try{
            JSONObject jsonObject = new JSONObject("{\"Tracks:[{\"Id\":\"23e47bbd-a47f-4005-b603-00d67bbe842a\", \"Name:River Hamud\", \"Latitude\":\"32.874865\", \"Longitude\":\"35.515653\", \"Zone\":\"Kineret\", \"Difficulty\":\"Medium\", \"DistanceKM\":\"4\", \"Events\":[{\"Id\":\"d88a9255-be10-4b17-a12e-cc79491776b9\", \"Name\":\"My Family\", \"StartDate\":\"2016-04-01T00:00:00\", \"EndDate:2016-04-02T00:00:00\"}, {\"Id\":\"0b62264d-cfd0-4373-b3a7-e35274f42770\", \"Name\":\"Passover 2016\", \"StartDate\":\"2016-02-05T00:00:00\", \"EndDate\":\"2016-05-05T00:00:00\"}]}, {\"Id\":\"e8eecbea-5a47-45f9-b6da-b6e220dfaa0d\", \"Name\":\"River Shofet\", \"Latitude\":\"32.633706\", \"Longitude\":\"35.103318\", \"Zone\":\"Yokneam\", \"Difficulty\":\"Easy\", \"DistanceKM\":\"2\", \"Events\":[]}]}");
        }
        catch(Exception e){}

    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }


    public void processFinish(JSONObject output){
        Log.d("Called from Activity",
                "Hey");
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean IsAuth;
            AuthenticationManager authMgr = new AuthenticationManager();

            try {
                IsAuth = authMgr.AuthUser(mEmail, mPassword);
            } catch (Exception e) {
                IsAuth=false;
            }

            // TODO: register the new account here.
            return IsAuth;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                finish();
                startActivity(intent);
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

