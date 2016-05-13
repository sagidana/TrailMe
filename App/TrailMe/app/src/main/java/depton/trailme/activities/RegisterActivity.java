package depton.trailme.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import depton.net.trailme.R;
import depton.trailme.data.RestCaller;
import depton.trailme.data.TrailMeListener;

public class RegisterActivity extends AppCompatActivity implements TrailMeListener {

    private String mMailAddresss;
    private RegisterActivity mCtx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCtx = this;
        setContentView(R.layout.activity_register);

        Bundle extras = getIntent().getExtras();
        mMailAddresss = extras.getString("Username");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText mail = (EditText)findViewById(R.id.email_address);
        mail.setText(mMailAddresss);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail= ((EditText) findViewById(R.id.email_address)).getText().toString();
                String firstname = ((EditText) findViewById(R.id.first_name)).getText().toString();
                String lastname = ((EditText) findViewById(R.id.last_name)).getText().toString();
                String city = ((EditText) findViewById(R.id.city)).getText().toString();

                try{
                    JSONObject user = new JSONObject();
                    user.put("FirstName", firstname);
                    user.put("LastName", lastname);
                    user.put("MailAddress", mail);
                    user.put("City", city);

                    user.put("Birthdate", "2016-05-12T20:04:49");

                    RestCaller rest = new RestCaller();

                    rest.delegate = mCtx;

                    rest.execute(mCtx, "addUser", user);

                    rest.execute(mCtx, "getUsers");

                }
                catch(Exception e){ }

            }
        });
    }

    public void processFinish(JSONObject response)
    {
        try{
            JSONArray users = response.getJSONArray("users");
            String currentUserId = null;

            for (int i = 0; i < users.length(); i++)
            {
                if (users.getJSONObject(i).getString("MailAddress").equals(mMailAddresss))
                {
                    currentUserId = users.getJSONObject(i).getString("Id");
                }
            }

            if (currentUserId != null)
            {
                Intent intent = new Intent(mCtx, MapActivity.class);
                intent.putExtra("currentUser", currentUserId);
                finish();
                startActivity(intent);
            }
        }
        catch (Exception e) {}
    }
}
