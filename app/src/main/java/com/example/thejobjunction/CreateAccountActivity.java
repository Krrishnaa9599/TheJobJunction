package com.example.thejobjunction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountActivity extends AppCompatActivity {

    //Declared views
    private Button createButton;
    private TextInputEditText email;
    private TextInputEditText contact;
    private TextInputEditText password;
    private TextInputEditText confirmPassword;
    private String selectedOption;
    private RadioGroup radioGroup;
    private Button createAccount;
    private String passwordText;
    private String mailText;
    private String confirmPasswordText;
    private String contactText;
    FirebaseAuth mAuth;

    public void onStart() {
        super.onStart();
        // checking if user already sign-in or not
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent toLogin=new Intent(CreateAccountActivity.this, MainActivity.class);
            startActivity(toLogin);// redirects to
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        //declared views
        mAuth= FirebaseAuth.getInstance();

        email=findViewById(R.id.mail);
        contact=findViewById(R.id.contact);
        password=findViewById(R.id.password);
        confirmPassword=findViewById(R.id.confirm_password);
        createAccount=findViewById(R.id.create_account_button);
        radioGroup=findViewById(R.id.radio_provider_seeker);
        //createAccount.setEnabled(true);

        mailText="";
        contactText="";
        passwordText="";
        confirmPasswordText="";

        //redirects to login page
        findViewById(R.id.login_hypertext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toCreateAccount=new Intent(CreateAccountActivity.this, MainActivity.class);
                startActivity(toCreateAccount);// redirects to
            }
        });
        //added listen for changing text inside edittext
        email.addTextChangedListener(textWatcher);
        contact.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        confirmPassword.addTextChangedListener(textWatcher);

        //assigning job seeker or job provider according to selection of radiobutton
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                selectedOption = radioButton.getText().toString();
                Toast.makeText(CreateAccountActivity.this, "Selected option: " + selectedOption, Toast.LENGTH_SHORT).show();
                if(!mailText.isEmpty() && !passwordText.isEmpty()&& !confirmPasswordText.isEmpty()&& !contactText.isEmpty()){
                    createAccount.setBackgroundColor(ContextCompat.getColor(CreateAccountActivity.this, R.color.enable_color));
                }
                else{
                    createAccount.setBackgroundColor(ContextCompat.getColor(CreateAccountActivity.this, R.color.disable_color));
                }
            }

        });

        //account will be create when create button clicked and authentication is validated
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("create","create button clickedddddd");

                //Toast.makeText(CreateAccountActivity.this, "m n p"+mailText+""+passwordText,
                //        Toast.LENGTH_SHORT).show();
                //code for authentication-firebase

                mAuth.createUserWithEmailAndPassword(mailText, passwordText)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(CreateAccountActivity.this, "Authentication Successful.",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("contact").setValue(contact.getText().toString());
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("accountType").setValue(selectedOption);
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userID").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    if(selectedOption.equals("Job Seeker")){
                                        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("jobsCompleted").setValue("0");
                                        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("rating").setValue("NotRated");
                                    }

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                //code for authentication-firebase

                Intent toLogin=new Intent(CreateAccountActivity.this, MainActivity.class);
                startActivity(toLogin);// redirects to

            }
        });

    }


//declared inner class for text changes in edittext
    private TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mailText=email.getText().toString();
            contactText=contact.getText().toString().trim();
            passwordText=password.getText().toString();
            confirmPasswordText=confirmPassword.getText().toString();
            //createAccount.setEnabled(true);

            createAccount.setEnabled(!mailText.isEmpty() && !passwordText.isEmpty()&& !confirmPasswordText.isEmpty()&& !contactText.isEmpty());
//!(selectedOption.equals("Job Provider")||(selectedOption.equals("Job Seeker")))
            if(!mailText.isEmpty() && !passwordText.isEmpty()&& !confirmPasswordText.isEmpty()&& !contactText.isEmpty()){
                createAccount.setBackgroundColor(ContextCompat.getColor(CreateAccountActivity.this, R.color.enable_color));
            }
            else{
                createAccount.setBackgroundColor(ContextCompat.getColor(CreateAccountActivity.this, R.color.disable_color));
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
