package com.example.thejobjunction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProviderActivity extends AppCompatActivity {

    //Button plusButton;

    //declared views
    Button homeButton;
    Button postedButton;
    Button acceptedButton;
    Button completedButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider);

        //setting Home Fragment as default when provider activity launched
        getSupportFragmentManager().beginTransaction().replace(R.id.select_fragment, new ProviderHomeFragment()).commit();

        homeButton=findViewById(R.id.home_button);
        postedButton=findViewById(R.id.posted_button);
        acceptedButton=findViewById(R.id.accepted_button);
        completedButton=findViewById(R.id.completed_button);

        //home fragment displays when home button clicked in nav bar
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.select_fragment, new ProviderHomeFragment()).commit();
            }
        });
        //posted fragment displays when posted button clicked in nav bar
        postedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.select_fragment, new ProviderPostedFragment()).commit();
            }
        });

        //accepted fragment displays when accepted button clicked in nav bar
        acceptedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.select_fragment, new ProviderAcceptedFragment()).commit();
            }
        });
        //completed fragment displays when completed button clicked in nav bar
        completedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.select_fragment, new ProviderCompletedFragment()).commit();
            }
        });
    }
}
