package depton.trailme.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import depton.net.trailme.R;
import depton.trailme.customViews.BertholdTextView;
import depton.trailme.data.RestCaller;
import depton.trailme.data.TrailMeListener;

import android.app.DatePickerDialog.OnDateSetListener;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity implements TrailMeListener {

    private String mMailAddresss;
    private RegisterActivity mCtx;
    private int mStackLevel = 1;
    private DatePickerDialog fromDatePickerDialog;
    private TextView dateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCtx = this;
        setContentView(R.layout.activity_register);

        SharedPreferences sharedPreferences = getSharedPreferences("TrailMe", Context.MODE_PRIVATE);

        String mMailAddresss = "aa";

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BertholdTextView mail = (BertholdTextView)findViewById(R.id.email_address);
        mail.setText(mMailAddresss);

        dateText = (BertholdTextView) findViewById(R.id.dateTrigger);

        final Context context = this;

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(context);
            }
        });

        dateText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return true;
            }
        });

        Button regiterBtn = (Button) findViewById(R.id.register);
        regiterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail= ((BertholdTextView) findViewById(R.id.email_address)).getText().toString();
                String firstname = ((BertholdTextView) findViewById(R.id.first_name)).getText().toString();
                String lastname = ((BertholdTextView) findViewById(R.id.last_name)).getText().toString();
                String passwordUser = ((EditText) findViewById(R.id.password_user)).getText().toString();
                String city = ((BertholdTextView) findViewById(R.id.zone)).getText().toString();
                //Date birthDate = getDateFromDatePicker(fromDatePickerDialog.getDatePicker());

                try{
                    JSONObject user = new JSONObject();
                    user.put("FirstName", firstname);
                    user.put("LastName", lastname);
                    user.put("MailAddress", mail);
                    user.put("Password",passwordUser);
                    user.put("City", city);
                    //user.put("Birthdate", birthDate);

                    user.put("Birthdate", "2016-05-12T20:04:49");

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

    public void showDatePicker(Context context){
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(context, new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                /*Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateText.setText(dateFormatter.format(newDate.getTime()));*/
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
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
