package com.example.thejobjunction;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class JobProviderPostUpdateFragment extends Fragment {

    //declaring the variables
    Button updateButton;
    TextInputEditText jobTitleText;
    TextInputEditText jobLocationText;
    TextInputEditText jobPayText;
    TextInputEditText employerNameText;
    TextInputEditText dateText;
    private EditText dateEdt;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflating the view
        View layout=inflater.inflate(R.layout.jobprovider_post_update_fragment, container, false);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialzing the views
        dateEdt=view.findViewById(R.id.idEdtDate);

        updateButton=view.findViewById(R.id.update_button);
        updateButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.enable_color));

        //functionality for datepicker
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
                        getActivity(),
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
/*                if (ContextCompat.checkSelfPermission(JobProviderPost.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(JobProviderPost.this,new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },100);
                }


 */


            }
        });


/*        view.findViewById(R.id.logout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth authm=FirebaseAuth.getInstance();
                authm.signOut();
                Toast.makeText(getActivity(), "logout", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

 */
        //declaring the variable views
        TextInputEditText jobTitle=view.findViewById(R.id.job_title);
        TextInputEditText jobLocation=view.findViewById(R.id.job_location);
        TextInputEditText jobPay=view.findViewById(R.id.job_pay);
        TextInputEditText employerName=view.findViewById(R.id.employee_name);
        EditText workDate=view.findViewById(R.id.idEdtDate);

        //calling the sharedpreference
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);



        if(sharedPreferences!=null){
            Log.d("showerror", "JPUF: "+sharedPreferences.getString("ID", "NO DATA"));
        }

        //getting title value from Firebase
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Job").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(sharedPreferences.getString("ID", "NO DATA"));

        reference.child("title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               // Log.d("cAdapter", snapshot.getValue(String.class));
                jobTitle.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //getting location value from Firebase
        reference.child("location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    //Log.d("cAdapter", snapshot.getValue(String.class));
                   jobLocation.setText(snapshot.getValue(String.class));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //getting pay value from Firebase
        reference.child("pay").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    //Log.d("cAdapter", Long.toString(snapshot.getValue(long.class)));
                    jobPay.setText(Long.toString(snapshot.getValue(long.class)));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //getting employee name value from Firebase
        reference.child("employerName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    //Log.d("cAdapter", snapshot.getValue(String.class));
                    employerName.setText(snapshot.getValue(String.class));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //getting workdate value from Firebase
        reference.child("workDate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    //Log.d("cAdapter", snapshot.getValue(String.class));
                    workDate.setText(snapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //below logic will update the job deta in Firebase database
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child("employerName").setValue(employerName.getText().toString());
                reference.child("location").setValue(jobLocation.getText().toString());
                reference.child("pay").setValue(Long.parseLong(jobPay.getText().toString()));
                reference.child("title").setValue(jobTitle.getText().toString());
                reference.child("workDate").setValue(workDate.getText().toString());

                AlertDialog.Builder alertDialog=new AlertDialog.Builder(getActivity());
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Successfully Updated");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FragmentTransaction fm=getActivity().getSupportFragmentManager().beginTransaction();
                        fm.replace(R.id.select_fragment,new ProviderPostedFragment()).commit();
                    }
                });
                alertDialog.show();

            }
        });

    }

}
