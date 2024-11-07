package com.example.thejobjunction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SeekerCompletedCustomAdapter extends RecyclerView.Adapter<SeekerCompletedCustomAdapter.ItemHolder> {

    //declares variables
    private Context mcontext;
    private ArrayList<JobItems> jobItems;
    private FragmentManager fragmentManager;
    private DatabaseReference reference;

    public SeekerCompletedCustomAdapter(Context mcontext, ArrayList<JobItems> jobItems, FragmentManager fragmentManager){
        //initialzing variables
        this.mcontext=mcontext;
        this.jobItems=jobItems;
        this.fragmentManager=fragmentManager;
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        //declaring views

        private TextView jobTitleView, jobLocationView, payView, workDateView, employerNameView, jobIdView, providerIdView, seekerView, ratingView;
        //private RatingBar ratingBar;


        public ItemHolder(View itemView){
            super(itemView);
            //initialzing views
            jobTitleView= itemView.findViewById(R.id.job_title);
            jobLocationView= itemView.findViewById(R.id.job_location);
            payView= itemView.findViewById(R.id.job_pay);
            workDateView=itemView.findViewById(R.id.job_work_day);
            employerNameView=itemView.findViewById(R.id.job_emp_name);
            jobIdView=itemView.findViewById(R.id.job_id);
            providerIdView=itemView.findViewById(R.id.provider_id);
            ratingView=itemView.findViewById(R.id.rating_given);
            //ratingBar=itemView.findViewById(R.id.ratingBar);
        }
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating layout
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_seeker_completed, parent, false);
        return new SeekerCompletedCustomAdapter.ItemHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull SeekerCompletedCustomAdapter.ItemHolder holder, int position) {
        //setting values to views
        holder.jobTitleView.setText(jobItems.get(position).getTitle());
        holder.jobLocationView.setText(jobItems.get(position).getLocation());
        holder.payView.setText("$ "+Double.toString(jobItems.get(position).getPay()));
        holder.workDateView.setText(jobItems.get(position).getWorkDate());
        holder.employerNameView.setText(jobItems.get(position).getEmployerName());
        holder.jobIdView.setText(jobItems.get(position).getId());
        holder.providerIdView.setText(jobItems.get(position).getProviderId());
        //holder.ratingBar.setVisibility(View.GONE);

        //retrieving ratings values of the seeker
        if(jobItems.get(position).getSeeker()!=null && jobItems.get(position).getId()!=null && FirebaseDatabase.getInstance().getReference().child("Users").child(jobItems.get(position).getSeeker())!=null && (FirebaseDatabase.getInstance().getReference().child("Users").child(jobItems.get(position).getSeeker()).child("jobRatings")!=null
            && FirebaseDatabase.getInstance().getReference().child("Users").child(jobItems.get(position).getSeeker()).child("jobRatings").child(jobItems.get(position).getId())!=null)){
            DatabaseReference usernameReference=FirebaseDatabase.getInstance().getReference().child("Users").child(jobItems.get(position).getSeeker()).child("jobRatings").child(jobItems.get(position).getId());
            usernameReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    holder.ratingView.setText(snapshot.getValue(String.class));
                    if(!(snapshot.getValue(String.class).equals("NotRated"))){
                        //holder.ratingBar.setRating(Float.parseFloat(snapshot.getValue(String.class)));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        //holder.seekerView.setText(jobItems.get(position).getSeeker());
    }


    @Override
    public int getItemCount() {
        return jobItems.size();
    }
}
