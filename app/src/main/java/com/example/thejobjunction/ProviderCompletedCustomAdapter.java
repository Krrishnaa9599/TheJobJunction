package com.example.thejobjunction;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProviderCompletedCustomAdapter extends RecyclerView.Adapter<ProviderCompletedCustomAdapter.ItemHolder> {

    //declared variables
    private Context mcontext;
    private ArrayList<JobItems> jobItems;
    private FragmentManager fragmentManager;
    private DatabaseReference reference;
    LayoutInflater inflater;

    public ProviderCompletedCustomAdapter(Context mcontext, ArrayList<JobItems> jobItems, FragmentManager fragmentManager, LayoutInflater inflater){
        //initialzing the variables
        this.mcontext=mcontext;
        this.jobItems=jobItems;
        this.fragmentManager=fragmentManager;
        this.inflater=inflater;
    }

    public class ItemHolder extends RecyclerView.ViewHolder{

        //declaring views
        private TextView jobTitleView, jobLocationView, payView, workDateView, employerNameView, jobIdView, providerIdView, seekerView, ratingView;
        private Button giveRatingButton;


        public ItemHolder(View itemView){
            super(itemView);
            //declaring views
            jobTitleView= itemView.findViewById(R.id.job_title);
            jobLocationView= itemView.findViewById(R.id.job_location);
            payView= itemView.findViewById(R.id.job_pay);
            workDateView=itemView.findViewById(R.id.job_work_day);
            employerNameView=itemView.findViewById(R.id.job_emp_name);
            jobIdView=itemView.findViewById(R.id.job_id);
            providerIdView=itemView.findViewById(R.id.provider_id);
            seekerView=itemView.findViewById(R.id.seeker_email);
            giveRatingButton=itemView.findViewById(R.id.give_rating_button);
            ratingView=itemView.findViewById(R.id.rating_text);
        }
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating layout
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_provider_completed, parent, false);
        return new ProviderCompletedCustomAdapter.ItemHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ProviderCompletedCustomAdapter.ItemHolder holder, int position) {
        //setting values to views
        holder.jobTitleView.setText(jobItems.get(position).getTitle());
        holder.jobLocationView.setText(jobItems.get(position).getLocation());
        holder.payView.setText("$ "+Double.toString(jobItems.get(position).getPay()));
        holder.workDateView.setText(jobItems.get(position).getWorkDate());
        holder.employerNameView.setText(jobItems.get(position).getEmployerName());
        holder.jobIdView.setText(jobItems.get(position).getId());
        holder.providerIdView.setText(jobItems.get(position).getProviderId());

        //pushing rating to the Firebase
        if(jobItems.get(position).getSeeker()!=null){
            DatabaseReference usernameReference=FirebaseDatabase.getInstance().getReference().child("Users").child(jobItems.get(position).getSeeker());
            usernameReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    holder.seekerView.setText(snapshot.child("username").getValue(String.class));
                    String jobRatingsGiven=snapshot.child("jobRatings").child(jobItems.get(position).getId()).getValue(String.class);
                    Log.d("displayratings", "jobID: "+jobRatingsGiven);
                    if(jobRatingsGiven.equals("NotRated")){
                        holder.ratingView.setVisibility(View.GONE);
                    }
                    else{
                        holder.giveRatingButton.setVisibility(View.GONE);
                        holder.ratingView.setText("Rating - "+jobRatingsGiven);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        //pushing the data to Firebase if rating given
        //holder.seekerView.setText(jobItems.get(position).getSeeker());
        holder.giveRatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext, "Rating Clicked", Toast.LENGTH_SHORT).show();
                //FirebaseDatabase.getInstance().getReference().child("Users").child(jobItems.get(position).getSeeker()).child("jobRatings").child(jobItems.get(position).getId()).setValue();
                AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
                View dialogView = inflater.inflate(R.layout.dialog_rating, null);
                builder.setView(dialogView);

                RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);

                builder.setTitle("Rate this seeker");
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle rating submission here
                        float rating = ratingBar.getRating();

                            FirebaseDatabase.getInstance().getReference().child("Users").child(jobItems.get(position).getSeeker()).child("jobRatings").child(jobItems.get(position).getId()).setValue(Float.toString(rating));
                            // You can do something with the rating, like send it to a server
                            holder.giveRatingButton.setVisibility(View.GONE);
                            holder.ratingView.setVisibility(View.VISIBLE);
                            Toast.makeText(mcontext, "Rating submitted: " + rating, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();


                    }
                });
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }


    @Override
    public int getItemCount() {
        return jobItems.size();
    }
}
