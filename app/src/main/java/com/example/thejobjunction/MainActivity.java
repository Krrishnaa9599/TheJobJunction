package com.example.thejobjunction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    //declaring the variables
    private Button loginButton;
    private TextInputEditText mail;
    private TextInputEditText password;
    private Button createAccount;
    private Button otpButton;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    //code for firebase
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //Intent toProvider = new Intent(MainActivity.this, ProviderActivity.class);
            //startActivity(toProvider);
            //finish();
            //Getting current login user id
            String userID=FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase reference=FirebaseDatabase.getInstance();

            reference.getReference().child("Users").child(userID).child("accountType").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        String accountType=snapshot.getValue(String.class);
                        //if job provider selected all fragments and activites related to them will be displayed
                        if(accountType.equals("Job Provider")){
                            Intent toProvider = new Intent(MainActivity.this, ProviderActivity.class);
                            startActivity(toProvider);
                            finish();
                        }
                        //if job seeker selected all fragments and activites related to them will be displayed
                        else if(accountType.equals("Job Seeker")){
                            Intent toProvider = new Intent(MainActivity.this, SeekerActivity.class);
                            startActivity(toProvider);
                            finish();
                        }
                    }
                    else{
                        Log.d("userinfor", "Data doesnt exists");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    //code for firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting instance of FirebaseAuth
        mAuth= FirebaseAuth.getInstance();
        //initializing views
        createAccount=findViewById(R.id.create_account_button);
        loginButton= findViewById(R.id.login_button);
        mail=findViewById(R.id.mail);
        password=findViewById(R.id.password);
        //otpButton=findViewById(R.id.otp_button);
        String mailText = mail.getText().toString();
        String passwordText =password.getText().toString();

        loginButton.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.disable_color));

        user = mAuth.getCurrentUser();

        //when create account button is pressed it goes to createaccount activity
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toCreateAccount=new Intent(MainActivity.this, CreateAccountActivity.class);
                startActivity(toCreateAccount);//

            }
        });

        //below code authenticates username and password entered
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signInWithEmailAndPassword(mail.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Login Successful.",
                                            Toast.LENGTH_SHORT).show();
                                    Log.d("useriddisplay", "UserID: "+FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    Log.d("useriddisplay", "EmailID: "+FirebaseAuth.getInstance().getCurrentUser().getEmail());

                                    String username=FirebaseAuth.getInstance().getCurrentUser().getEmail();
                                    String userID=FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    FirebaseDatabase reference=FirebaseDatabase.getInstance();



                                    reference.getReference().child("Users").child(userID).child("accountType").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists())
                                            {
                                                String accountType=snapshot.getValue(String.class);

                                                //if job provider selected all fragments and activites related to them will be displayed
                                                if(accountType.equals("Job Provider")){
                                                    Intent toProvider = new Intent(MainActivity.this, ProviderActivity.class);
                                                    startActivity(toProvider);
                                                }
                                                //if job seeker selected all fragments and activites related to them will be displayed
                                                else if(accountType.equals("Job Seeker")){
                                                    Intent toProvider = new Intent(MainActivity.this, SeekerActivity.class);
                                                    startActivity(toProvider);
                                                }
                                            }
                                            else{
                                                Log.d("userinfor", "Data doesnt exists");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                } else {
                                    //validates if user already exists or not when login button pressed
                                    FirebaseAuth.getInstance().fetchSignInMethodsForEmail(mail.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                                    if (task.isSuccessful()) {
                                                        SignInMethodQueryResult result = task.getResult();
                                                        if (result != null && result.getSignInMethods() != null && !result.getSignInMethods().isEmpty()) {
                                                            // Email already exists
                                                        }
                                                        else {
                                                            Log.e("userexits", "Email does not exist");
                                                            Toast.makeText(MainActivity.this, "Email does not exist",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        Log.e("emailexists", "Error checking email existence: " + task.getException());

                                                    }
                                                }
                                            });


                                }
                            }
                        });
            }
        });




        mail.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);

    }
    private TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String usernameText=mail.getText().toString().trim();
            String passwordText=password.getText().toString().trim();

            loginButton.setEnabled(!usernameText.isEmpty() && !passwordText.isEmpty());
            if(!usernameText.isEmpty() && !passwordText.isEmpty()){
                loginButton.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.enable_color));
            }
            else{
                loginButton.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.disable_color));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


}