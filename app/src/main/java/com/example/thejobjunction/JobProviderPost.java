package com.example.thejobjunction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Locale;
import java.util.Random;
public class JobProviderPost extends AppCompatActivity implements LocationListener {

    //declared views
    private EditText dateEdt;
    LocationManager locationManager;

    EditText jobTitle;
    EditText jobLocation;
    EditText jobPay;
    EditText employerName;

    String jobTitleText;

    Button postButton;
    String employerNameText;
    String jobPayText;
    String jobLocationText;
    String dateText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jobprovider_post);
        //initailized all views
        dateEdt = findViewById(R.id.idEdtDate);

        jobTitle=findViewById(R.id.job_title);
        jobPay=findViewById(R.id.job_pay);
        employerName=findViewById(R.id.employee_name);
        jobLocation=findViewById(R.id.job_location);
        postButton=findViewById(R.id.post_button);

        jobTitleText=jobTitle.getText().toString();


        if(jobPay.getText().toString().equals("")){
            jobPayText=Double.toString(0.0);
        }
        else{
            jobPayText=jobPay.getText().toString();
        }
        //Double jobPayText=Double.parseDouble(jobPay.getText().toString());
        employerNameText=employerName.getText().toString();
        jobLocationText=jobLocation.getText().toString();
        dateText=dateEdt.getText().toString();


        //declared onclick for job location editext to get current location
        jobLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });



        dateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);



                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        JobProviderPost.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our edit text.
                                dateEdt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
                jobTitleText=jobTitle.getText().toString();
                jobPayText=jobPay.getText().toString();
                employerNameText=employerName.getText().toString();
                jobLocationText=jobLocation.getText().toString();
                dateText=dateEdt.getText().toString();
                //below code is for enabling and disabling post button
                if(!jobTitleText.isEmpty() && !jobPayText.isEmpty()&& !employerNameText.isEmpty()&& !jobLocationText.isEmpty() && !dateText.isEmpty()){

                    postButton.setBackgroundColor(ContextCompat.getColor(JobProviderPost.this, R.color.enable_color));
                }
                else{
                    postButton.setBackgroundColor(ContextCompat.getColor(JobProviderPost.this, R.color.disable_color));
                }

                if (ContextCompat.checkSelfPermission(JobProviderPost.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(JobProviderPost.this,new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },100);
                }



            }
        });

        // declaring text change listener
        dateEdt.addTextChangedListener(textWatcher);
        jobTitle.addTextChangedListener(textWatcher);
        jobLocation.addTextChangedListener(textWatcher);
        jobPay.addTextChangedListener(textWatcher);
        employerName.addTextChangedListener(textWatcher);
        dateEdt.addTextChangedListener(textWatcher);


        //job will be posted and data will be pushed to Firebase
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ProviderActions jobManager = new ProviderActions();
                //jobManager.createJob("1", jobTitleText, jobLocationText, jobPayText, employerNameText, dateText, "posted");

                //Random random=new Random();
                //int userID=random.nextInt(100);

                //jobManager.createJob(Integer.toString(userID), jobTitle.getText().toString(), jobLocation.getText().toString(), Double.parseDouble(jobPay.getText().toString()), employerName.getText().toString(), dateEdt.getText().toString(),"posted");

                JobItems.createJob(FirebaseAuth.getInstance().getCurrentUser().getUid(), jobTitle.getText().toString(), jobLocation.getText().toString(), Double.parseDouble(jobPay.getText().toString()), employerName.getText().toString(), dateEdt.getText().toString(),"posted");

                Toast.makeText(JobProviderPost.this, "Inserted Successfully", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(JobProviderPost.this, ProviderActivity.class);
                startActivity(intent);
            }
        });

    }
    @SuppressLint("MissingPermission")
    private void getLocation(){
        try {
            //declaring the location manager
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,JobProviderPost.this);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        //getting latitude and longitude of the android device
        Toast.makeText(this, ""+location.getLatitude()+","+location.getLongitude(), Toast.LENGTH_SHORT).show();
        try {
            Geocoder geocoder = new Geocoder(JobProviderPost.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);

            jobLocation.setText(address);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        //LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        //LocationListener.super.onProviderDisabled(provider);
    }

    TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //this method will execute when we add or remove a letter in the edittext
            jobTitleText=jobTitle.getText().toString();
            jobPayText=jobPay.getText().toString();
            employerNameText=employerName.getText().toString();
            jobLocationText=jobLocation.getText().toString();
            dateText=dateEdt.getText().toString();
            postButton.setEnabled(!jobTitleText.isEmpty() && !jobPayText.isEmpty()&& !employerNameText.isEmpty()&& !jobLocationText.isEmpty() && !dateText.isEmpty());

            Log.d("postbutton", "jobTitle: "+!jobTitleText.isEmpty());
            Log.d("postbutton", "jobPay: "+!jobPayText.isEmpty());
            Log.d("postbutton", "employerName: "+!employerNameText.isEmpty());
            Log.d("postbutton", "jobLocation: "+!jobLocationText.isEmpty());
            Log.d("postbutton", "jobTitle: "+!dateText.isEmpty());

            //login for enabling and disabling the postbutton
            if(!jobTitleText.isEmpty() && !jobPayText.isEmpty()&& !employerNameText.isEmpty()&& !jobLocationText.isEmpty() && !dateText.isEmpty()){
                postButton.setBackgroundColor(ContextCompat.getColor(JobProviderPost.this, R.color.enable_color));
            }
            else{
                postButton.setBackgroundColor(ContextCompat.getColor(JobProviderPost.this, R.color.disable_color));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
