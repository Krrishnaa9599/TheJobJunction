package com.example.thejobjunction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class ProviderShowRequestsFragment extends Fragment {

    //declaring variables
    private Context mcontext;
    private RecyclerView recyclerView;
    private ProviderShowRequestsCustomAdapter providerShowRequestsCustomAdapter;
    private ArrayList<SeekerDetails> seekerDetailsList=new ArrayList<SeekerDetails>();
    private SearchView searchView;
    private ArrayList<SeekerDetails> searchList;
    private FragmentManager fragmentManager;
    public String seekerID;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflating layout
        View layout=inflater.inflate(R.layout.provider_showrequests_fragment, container, false);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initializing views
        searchView=view.findViewById(R.id.search_view);
        recyclerView=view.findViewById(R.id.recyclerview);
        fragmentManager=getActivity().getSupportFragmentManager();
        mcontext= getActivity();
        // user logouts when logout button clicked
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

        //initialzing sharedpreferences
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        String jobId="";
        if(sharedPreferences!=null){
            jobId=sharedPreferences.getString("jobID", "");
        }

        //retrieves all seekers who request for the job
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Job");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(jobId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("seekers")){
                    SharedPreferences sharedPreferences= getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
                    String jobId="";
                    if(sharedPreferences!=null){
                        jobId=sharedPreferences.getString("jobID", "");
                    }
                    Log.d("seekerslist","SP JobID: "+jobId);
                    DatabaseReference seekersReference= FirebaseDatabase.getInstance().getReference().child("Job").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(jobId).child("seekers");
                    seekersReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot seekersSnapshot) {
                            seekerDetailsList.clear();
                            for (DataSnapshot seekerSnapShot: seekersSnapshot.getChildren()){
                                seekerID=seekerSnapShot.getKey();
                                String seekerValue=seekerSnapShot.getValue(String.class);
                                Log.d("seekerslist","Requested Seeker JobID: "+seekerID);
                                Log.d("seekerslist","Requested Seeker EmailID: "+seekerValue);
                                addToList(seekerID);
                            }
                            //seekerDetailsList.add(seekersSnapshot.getValue(SeekerDetails.class));
                            providerShowRequestsCustomAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    Log.d("seekerdetails","outside for List: "+seekerDetailsList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                searchList=new ArrayList<>();

                if(newText.length()>0){
                    for(SeekerDetails seekerDetails: seekerDetailsList){

                        if(seekerDetails.getUsername().toLowerCase().contains(newText.toLowerCase()) ||
                                seekerDetails.getRating().toLowerCase().contains(newText.toLowerCase()) ||
                                seekerDetails.getJobsCompleted().toLowerCase().contains(newText.toLowerCase())){

                            SeekerDetails seekerDetailsModel=new SeekerDetails();

                            seekerDetailsModel.setUsername(seekerDetails.getUsername());
                            seekerDetailsModel.setRating(seekerDetails.getRating());
                            seekerDetailsModel.setContact(seekerDetails.getContact());
                            seekerDetailsModel.setAccountType(seekerDetails.getAccountType());
                            seekerDetailsModel.setJobsCompleted(seekerDetails.getJobsCompleted());
                            seekerDetailsModel.setUserID(seekerDetails.getUserID());
                            searchList.add(seekerDetailsModel);
                        }
                    }
                    LinearLayoutManager verticalManager=new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(verticalManager);
                    providerShowRequestsCustomAdapter =new ProviderShowRequestsCustomAdapter(mcontext, searchList, fragmentManager);
                    recyclerView.setAdapter(providerShowRequestsCustomAdapter);
                }
                else {
                    LinearLayoutManager verticalManager=new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(verticalManager);
                    providerShowRequestsCustomAdapter =new ProviderShowRequestsCustomAdapter(mcontext, seekerDetailsList, fragmentManager);
                    recyclerView.setAdapter(providerShowRequestsCustomAdapter);
                }
                return true;
            }
        });

        LinearLayoutManager verticalManager=new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalManager);
        providerShowRequestsCustomAdapter =new ProviderShowRequestsCustomAdapter(mcontext, seekerDetailsList, fragmentManager);
        recyclerView.setAdapter(providerShowRequestsCustomAdapter);

    }

    public void addToList(String sID){
        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot userSeekerSnapshot) {
                Log.d("seekerslist","addToList SeekerID: "+ sID);
                SeekerDetails seekerDetails=userSeekerSnapshot.child(sID).getValue(SeekerDetails.class);
                Log.d("seekerdetails","addToList accountType: "+seekerDetails.getAccountType());
                Log.d("seekerdetails","addToList contact: "+seekerDetails.getContact());
                Log.d("seekerdetails","addToList jobsCompleted: "+seekerDetails.getJobsCompleted());
                Log.d("seekerdetails","addToList rating: "+seekerDetails.getRating());
                Log.d("seekerdetails","addToList username: "+seekerDetails.getUsername());
                Log.d("seekerdetails","addToList userID: "+seekerDetails.getUserID());
                seekerDetailsList.add(seekerDetails);
                providerShowRequestsCustomAdapter.notifyDataSetChanged();
                Log.d("seekerdetails","List: "+seekerDetailsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
