package com.example.thejobjunction;

import android.content.Context;
import android.content.DialogInterface;
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
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SeekerAcceptedCustomAdapter extends RecyclerView.Adapter<SeekerAcceptedCustomAdapter.ItemHolder> {

    //declaring the variables
    private Context mcontext;
    private ArrayList<JobItems> jobItems;
    private FragmentManager fragmentManager;
    private DatabaseReference reference;
    private long jobCount;

    public SeekerAcceptedCustomAdapter(Context mcontext, ArrayList<JobItems> jobItems, FragmentManager fragmentManager){
        //initialzing the variables
        this.mcontext=mcontext;
        this.jobItems=jobItems;
        this.fragmentManager=fragmentManager;
    }

    public class ItemHolder extends RecyclerView.ViewHolder{

        //declaring the views
        private TextView jobTitleView, jobLocationView, payView, workDateView, employerNameView, jobIdView, providerIdView;
        private ImageView tickButton;


        public ItemHolder(View itemView){
            super(itemView);
            //initialzing the views
            jobTitleView= itemView.findViewById(R.id.job_title);
            jobLocationView= itemView.findViewById(R.id.job_location);
            payView= itemView.findViewById(R.id.job_pay);
            workDateView=itemView.findViewById(R.id.job_work_day);
            employerNameView=itemView.findViewById(R.id.job_emp_name);
            jobIdView=itemView.findViewById(R.id.job_id);
            providerIdView=itemView.findViewById(R.id.provider_id);
            tickButton=itemView.findViewById(R.id.tick_mark_img);
        }
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating the layout
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_seeker_accepted, parent, false);
        return new SeekerAcceptedCustomAdapter.ItemHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull SeekerAcceptedCustomAdapter.ItemHolder holder, int position) {
        //setting data to views
        holder.jobTitleView.setText(jobItems.get(position).getTitle());
        holder.jobLocationView.setText(jobItems.get(position).getLocation());
        holder.payView.setText("$ "+Double.toString(jobItems.get(position).getPay()));
        holder.workDateView.setText(jobItems.get(position).getWorkDate());
        holder.employerNameView.setText(jobItems.get(position).getEmployerName());
        holder.jobIdView.setText(jobItems.get(position).getId());
        holder.providerIdView.setText(jobItems.get(position).getProviderId());
        /*DatabaseReference usernameReference=FirebaseDatabase.getInstance().getReference().child("Users").child(jobItems.get(position).getSeeker()).child("username");
        usernameReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        //jobstatus of the job will be changed to accepted in Firebase
        holder.tickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext, "seeker ID:"+ jobItems.get(position).getSeeker(), Toast.LENGTH_SHORT).show();

                AlertDialog.Builder alertDialog=new AlertDialog.Builder(mcontext);
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Do you want to complete this job?");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("provideridcheck", "provider: "+jobItems.get(position).getProviderId());
                        Log.d("provideridcheck", "jobID : "+jobItems.get(position).getId());
                        FirebaseDatabase reference=FirebaseDatabase.getInstance();
                        reference.getReference().child("Job").child(jobItems.get(position).getProviderId()).child(jobItems.get(position).getId()).child("jobStatus").setValue("completed");
                        reference.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("jobRatings").child(jobItems.get(position).getId()).setValue("NotRated");
                        reference.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("jobRatings").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //jobsCom=Integer.parseInt(snapshot.child("jobsCompleted").getValue(String.class));
                                jobCount=snapshot.getChildrenCount();
                                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("jobsCompleted").setValue(Long.toString(jobCount));
                                Log.d("jobcount", "jobcount inside"+Long.toString(jobCount));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        //update jobs completed
                    }
                });
                alertDialog.setNegativeButton("NO", null);
                alertDialog.show();
                //FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("jobsCompleted").setValue(Long.toString(jobCount));
                //Log.d("jobcount", "jobcount outside"+Long.toString(jobCount));
            }
        });

        //holder.seekerView.setText(jobItems.get(position).getSeeker());
    }


    @Override
    public int getItemCount() {
        return jobItems.size();
    }
}
