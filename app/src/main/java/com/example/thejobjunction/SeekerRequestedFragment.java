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

public class SeekerRequestedFragment extends Fragment {

    //declaring the variables
    private Context mcontext;
    private RecyclerView recyclerView;
    private ArrayList<JobItems> jobItemList=new ArrayList<JobItems>();
    private SearchView searchView;
    private ArrayList<JobItems> searchList;
    private FragmentManager fragmentManager;
    private SeekerRequestedCustomAdapter seekerRequestedCustomAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflating the layout
        View layout=inflater.inflate(R.layout.seeker_requested_fragment, container, false);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initializing the variables
        searchView=view.findViewById(R.id.search_view);
        recyclerView=view.findViewById(R.id.recyclerview);
        fragmentManager=getActivity().getSupportFragmentManager();
        mcontext= getActivity();

        //user logout when logout button clicked
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



        //search view funcitonality
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
                    //retrieves all jobs which are posted by different job providers
                    LinearLayoutManager verticalManager=new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(verticalManager);
                    seekerRequestedCustomAdapter =new SeekerRequestedCustomAdapter(mcontext, searchList, fragmentManager);
                    recyclerView.setAdapter(seekerRequestedCustomAdapter);
                }
                else {
                    //retrieves all jobs which are posted by different job providers
                    LinearLayoutManager verticalManager=new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(verticalManager);
                    seekerRequestedCustomAdapter =new SeekerRequestedCustomAdapter(mcontext, jobItemList, fragmentManager);
                    recyclerView.setAdapter(seekerRequestedCustomAdapter);
                }

                return true;
            }
        });
        //retrieves all jobs which are posted by different job providers
        LinearLayoutManager verticalManager=new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalManager);
        seekerRequestedCustomAdapter =new SeekerRequestedCustomAdapter(mcontext, jobItemList, fragmentManager);
        recyclerView.setAdapter(seekerRequestedCustomAdapter);

        DatabaseReference parentReference= FirebaseDatabase.getInstance().getReference().child("Job");

        //retrieves all jobs requested by the seeker
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
                                //Log.d("jobitemsff", "accessing child for loop");
                                //JobItems job=childDataSnapshot.getValue(JobItems.class);

                                if(childDataSnapshot.hasChild("seekers")){
                                    //Log.d("checkseeker", "seekers exists");
                                    if(childDataSnapshot.child("seekers").hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                        Log.d("checkseeker",FirebaseAuth.getInstance().getCurrentUser().getUid()+" present");
                                        Log.d("checkseeker", "job: "+childDataSnapshot.child("title").getValue(String.class));
                                        Log.d("checkseeker", "jobID: "+ childDataSnapshot.child("id").getValue(String.class));
                                        Log.d("checkseeker","seeker name: "+FirebaseAuth.getInstance().getCurrentUser().getEmail());

                                        Log.d("checkseeker", "object: "+childDataSnapshot);
                                        Log.d("checkseeker", "");

                                        JobItems job=childDataSnapshot.getValue(JobItems.class);
                                        jobItemList.add(job);
                                        seekerRequestedCustomAdapter.notifyDataSetChanged();
                                    }
                                }

                            }
                            seekerRequestedCustomAdapter.notifyDataSetChanged();
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