package com.example.thejobjunction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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

public class ProviderPostedFragment extends Fragment {

    //declares variables
    private Context mcontext;
    private RecyclerView recyclerView;
    private ProviderCustomAdapter providerCustomAdapter;
    private ArrayList<JobItems> jobItemList=new ArrayList<JobItems>();
    private SearchView searchView;
    private ArrayList<JobItems> searchList;
    private ImageView editJob;
    private ImageView deleteJob;
    private TextView jobIdTextView;
    private FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //inflated layoued
        View layout=inflater.inflate(R.layout.provider_posted_fragment, container, false);

        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialized views
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

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Job").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        //search view functionality
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
                                job.getEmployerName().toLowerCase().contains(newText.toLowerCase())){

                            JobItems jobModel=new JobItems();

                            jobModel.setTitle(job.getTitle());
                            jobModel.setLocation(job.getLocation());
                            jobModel.setPay(job.getPay());
                            jobModel.setWorkDate(job.getWorkDate());
                            jobModel.setEmployerName(job.getEmployerName());
                            searchList.add(jobModel);
                        }
                    }
                    //declared vertical orientation to recycler view and declared custom adapter to pass add to views
                    LinearLayoutManager verticalManager=new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(verticalManager);
                    providerCustomAdapter =new ProviderCustomAdapter(mcontext, searchList, fragmentManager);
                    recyclerView.setAdapter(providerCustomAdapter);
                }
                else {
                    //declared vertical orientation to recycler view and declared custom adapter to pass add to views
                    LinearLayoutManager verticalManager=new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(verticalManager);
                    providerCustomAdapter =new ProviderCustomAdapter(mcontext, jobItemList, fragmentManager);
                    recyclerView.setAdapter(providerCustomAdapter);
                }

                return true;
            }
        });
        //declared vertical orientation to recycler view and declared custom adapter to pass add to views
        LinearLayoutManager verticalManager=new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalManager);
        providerCustomAdapter =new ProviderCustomAdapter(mcontext, jobItemList, fragmentManager);
        recyclerView.setAdapter(providerCustomAdapter);

        //retrieves jobs whose jobstatus is posted
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobItemList.clear();
                Log.d("jobitems1", "accessing datasnaphot");
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Log.d("jobitems2", "accessing for loop");
                    JobItems job=dataSnapshot.getValue(JobItems.class);

                    if (job.getJobStatus()!=null && job.getJobStatus().equals("posted"))
                        if (job.getJobStatus() != null) {
                            Log.d("jobitems11", "Posted job" + job.getId());
                            jobItemList.add(job);
                        }

                }
                providerCustomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
