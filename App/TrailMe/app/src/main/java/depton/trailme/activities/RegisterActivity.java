package depton.trailme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

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

        Button regiterBtn = (Button) findViewById(R.id.register);
        regiterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail= ((EditText) findViewById(R.id.email_address)).getText().toString();
                String firstname = ((EditText) findViewById(R.id.first_name)).getText().toString();
                String lastname = ((EditText) findViewById(R.id.last_name)).getText().toString();
                String passwordUser = ((EditText) findViewById(R.id.password_user)).getText().toString();
                String city = ((EditText) findViewById(R.id.city)).getText().toString();
                Date birthDate = getDateFromDatePicker((DatePicker)findViewById(R.id.datePicker));

                try{
                    JSONObject user = new JSONObject();
                    user.put("FirstName", firstname);
                    user.put("LastName", lastname);
                    user.put("MailAddress", mail);
                    user.put("Password",passwordUser);
                    user.put("City", city);
                    user.put("Birthdate", birthDate);

                    //user.put("Birthdate", "2016-05-12T20:04:49");

                    RestCaller rest1 = new RestCaller();
                    rest1.execute(mCtx, "addUser", user);

                    RestCaller rest2 = new RestCaller();
                    rest2.delegate = mCtx;
                    rest2.execute(mCtx, "getUsers");

                }
                catch(Exception e){

                }

            }
        });
    }

    public static java.util.Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
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
                Intent intent = new Intent(mCtx, MainActivity.class);
                intent.putExtra("currentUser", currentUserId);
                finish();
                startActivity(intent);
            }
        }
        catch (Exception e) {}
    }
}
