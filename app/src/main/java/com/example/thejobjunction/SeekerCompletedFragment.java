package com.example.thejobjunction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SeekerCompletedFragment extends Fragment {

    //declaring the views
    private Context mcontext;
    private RecyclerView recyclerView;
    private ArrayList<JobItems> jobItemList=new ArrayList<JobItems>();
    private SearchView searchView;
    private ArrayList<JobItems> searchList;
    private FragmentManager fragmentManager;
    private SeekerCompletedCustomAdapter seekerCompletedCustomAdapter;
    private String seekerEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflating the layout
        View layout=inflater.inflate(R.layout.seeker_completed_fragment, container, false);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initializing the views
        searchView=view.findViewById(R.id.search_view);
        recyclerView=view.findViewById(R.id.recyclerview);
        fragmentManager=getActivity().getSupportFragmentManager();
        mcontext= getActivity();

        //user logouts when logout button clicked
        view.findViewById(R.id.logout_button).setOnClickListener(new View.OnClickListener() {
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

        //search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                searchList=new ArrayList<>();
                if(newText.length()>0){
                    for(JobItems job: jobItemList){

                        if(job.getTitle().toLowerCase().contains(newText.toLowerCase()) ||
                                job.getLocation().toLowerCase().contains(newText.toLowerCase()) ||
                                Double.toString(job.getPay()).toLowerCase().contains(newText.toLowerCase())||
                                job.getWorkDate().toLowerCase().contains(newText.toLowerCase())||
                                job.getEmployerName().toLowerCase().contains(newText.toLowerCase())||
                                job.getSeeker().toLowerCase().contains(newText.toLowerCase())){

                            JobItems jobModel=new JobItems();

                            jobModel.setTitle(job.getTitle());
                            jobModel.setLocation(job.getLocation());
                            jobModel.setPay(job.getPay());
                            jobModel.setWorkDate(job.getWorkDate());
                            jobModel.setEmployerName(job.getEmployerName());
                            jobModel.setSeeker(job.getSeeker());
                            searchList.add(jobModel);
                        }
                    }
                    LinearLayoutManager verticalManager=new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(verticalManager);
                    seekerCompletedCustomAdapter =new SeekerCompletedCustomAdapter(mcontext, searchList, fragmentManager);
                    recyclerView.setAdapter(seekerCompletedCustomAdapter);
                }
                else {
                    LinearLayoutManager verticalManager=new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(verticalManager);
                    seekerCompletedCustomAdapter =new SeekerCompletedCustomAdapter(mcontext, jobItemList, fragmentManager);
                    recyclerView.setAdapter(seekerCompletedCustomAdapter);
                }

                return true;
            }
        });
        //declared vertical orientation to recycler view and declared custom adapter to pass add to views
        LinearLayoutManager verticalManager=new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalManager);
        seekerCompletedCustomAdapter =new SeekerCompletedCustomAdapter(mcontext, jobItemList, fragmentManager);
        recyclerView.setAdapter(seekerCompletedCustomAdapter);

        DatabaseReference parentReference= FirebaseDatabase.getInstance().getReference().child("Job");

        //retrieves all jobs of jobstatus as completed
        parentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot parentSnapshot) {
                for(DataSnapshot parentDataSnapshot: parentSnapshot.getChildren()){
                    jobItemList.clear();
                    DatabaseReference childReference= FirebaseDatabase.getInstance().getReference().child("Job").child(parentDataSnapshot.getKey());
                    childReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot childSnapshot) {

                            for(DataSnapshot childDataSnapshot: childSnapshot.getChildren()){

                                JobItems job=childDataSnapshot.getValue(JobItems.class);

                                if(job.getJobStatus()!=null &&job.getJobStatus().equals("completed") && childDataSnapshot.hasChild("seeker") && job.getSeeker().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                    //Log.d("acceptedseekers", FirebaseAuth.getInstance().getCurrentUser().getEmail()+" Accepted job "+job.getTitle());
                                    jobItemList.add(job);
                                    seekerCompletedCustomAdapter.notifyDataSetChanged();
                                }


                            }
                            seekerCompletedCustomAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}