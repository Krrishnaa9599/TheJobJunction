package com.example.thejobjunction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ProviderShowRequestsCustomAdapter extends RecyclerView.Adapter<ProviderShowRequestsCustomAdapter.ItemHolder>{

    //declared variables
    private Context mcontext;
    private ArrayList<SeekerDetails> seekerDetailsList;
    private FragmentManager fragmentManager;
    private DatabaseReference reference;

    public ProviderShowRequestsCustomAdapter(Context mcontext, ArrayList<SeekerDetails> seekerDetailsList, FragmentManager fragmentManager){
        //initializing variables
        this.mcontext=mcontext;
        this.seekerDetailsList=seekerDetailsList;
        this.fragmentManager=fragmentManager;
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        //declared views
        private TextView seekerUsername, seekerRatings, seekerJobsCompleted;
        private ImageView tickImageView;
        public ItemHolder(View itemView){
            super(itemView);

            //initializing views
            seekerUsername=itemView.findViewById(R.id.seeker_username);
            seekerRatings=itemView.findViewById(R.id.seeker_ratings);
            seekerJobsCompleted=itemView.findViewById(R.id.seeker_job_completed);
            tickImageView=itemView.findViewById(R.id.tick_img);

        }
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating layout
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_provider_showrequests, parent, false);
        return new ProviderShowRequestsCustomAdapter.ItemHolder(layout);
    }



    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {

        holder.seekerUsername.setText(seekerDetailsList.get(position).getUsername());
        holder.seekerRatings.setText(seekerDetailsList.get(position).getRating());
        holder.seekerJobsCompleted.setText(seekerDetailsList.get(position).getJobsCompleted());

        Log.d("customad","Username: "+seekerDetailsList.get(position).getUsername());
        Log.d("customad","Rating: "+seekerDetailsList.get(position).getRating());
        Log.d("customad","Jobs completed: "+seekerDetailsList.get(position).getJobsCompleted());

        //jobstatus will change to accepted and job is given to accepted seeker
        holder.tickImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext, "Ticked "+seekerDetailsList.get(position).getUsername(), Toast.LENGTH_SHORT).show();
                Log.d("ticked", "Ticked "+seekerDetailsList.get(position).getUsername());
                Log.d("ticked", "Ticked "+seekerDetailsList.get(position).getUsername());

                AlertDialog.Builder alertDialog=new AlertDialog.Builder(mcontext);
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Do you want to Accept?");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //FirebaseDatabase reference=FirebaseDatabase.getInstance();
                        SharedPreferences sharedPreferences= mcontext.getSharedPreferences("sp", Context.MODE_PRIVATE);
                                //getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
                        String jobId=sharedPreferences.getString("jobID", "");
                        Log.d("displaystatus", "UserID: "+seekerDetailsList.get(position).getUserID());
                        Log.d("displaystatus: ", jobId);
                        FirebaseDatabase reference=FirebaseDatabase.getInstance();
                        reference.getReference().child("Job").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(jobId).child("jobStatus").setValue("accepted");
                        reference.getReference().child("Job").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(jobId).child("seekers").removeValue();
                        reference.getReference().child("Job").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(jobId).child("seeker").setValue(seekerDetailsList.get(position).getUserID());

                        Toast.makeText(mcontext, "Accepted: "+seekerDetailsList.get(position).getUsername(), Toast.LENGTH_SHORT).show();

                        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.select_fragment, new ProviderPostedFragment());
                        fragmentTransaction.commit();
                    }
                });
                alertDialog.setNegativeButton("NO", null);
                alertDialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return seekerDetailsList.size();
    }
}
