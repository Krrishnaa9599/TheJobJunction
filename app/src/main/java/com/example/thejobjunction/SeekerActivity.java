package com.example.thejobjunction;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.nio.BufferUnderflowException;
import java.util.ArrayList;

public class SeekerActivity extends AppCompatActivity {

    //declaring views
    Button requestedButton;
    Button homeButton, acceptedButton, completedButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker);
        getSupportFragmentManager().beginTransaction().replace(R.id.select_fragment_seeker, new SeekerHomeFragment()).commit();

        //initialzing the views
        requestedButton=findViewById(R.id.requested_button);
        homeButton=findViewById(R.id.home_button);
        acceptedButton=findViewById(R.id.accepted_button);
        completedButton=findViewById(R.id.completed_button);

        //home fragment displays when home nav button clicked
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.select_fragment_seeker, new SeekerHomeFragment()).commit();
            }
        });
        //requested fragment displays when requested nav button clicked
        requestedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.select_fragment_seeker, new SeekerRequestedFragment()).commit();
            }
        });
        //accepted fragment displays when accepted nav button clicked
        acceptedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.select_fragment_seeker, new SeekerAcceptedFragment()).commit();
            }
        });
        //completed fragment displays when completed nav button clicked
        completedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.select_fragment_seeker, new SeekerCompletedFragment()).commit();
            }
        });
    }
}