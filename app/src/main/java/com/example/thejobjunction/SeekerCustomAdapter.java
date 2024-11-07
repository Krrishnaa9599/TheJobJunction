package com.example.thejobjunction;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SeekerCustomAdapter extends RecyclerView.Adapter<SeekerCustomAdapter.ItemHolder> {
    //declaring the variables

    private Context mcontext;
    private ArrayList<JobItems> jobItems;
    private FragmentManager fragmentManager;
    private DatabaseReference reference;

    public SeekerCustomAdapter(Context mcontext, ArrayList<JobItems> jobItems, FragmentManager fragmentManager){
        //initializing the variables
        this.mcontext=mcontext;
        this.jobItems=jobItems;
        this.fragmentManager=fragmentManager;
    }

    public class ItemHolder extends RecyclerView.ViewHolder{

        //initializing the views
        private TextView jobTitleView, jobLocationView, payView, workDateView, employerNameView, jobIdView, providerIdView;
        private ImageView plusImageView;


        public ItemHolder(View itemView){
            super(itemView);
            //declaring th views
            jobTitleView= itemView.findViewById(R.id.job_title);
            jobLocationView= itemView.findViewById(R.id.job_location);
            payView= itemView.findViewById(R.id.job_pay);
            workDateView=itemView.findViewById(R.id.job_work_day);
            employerNameView=itemView.findViewById(R.id.job_emp_name);
            jobIdView=itemView.findViewById(R.id.job_id);
            providerIdView=itemView.findViewById(R.id.provider_id);
            plusImageView=itemView.findViewById(R.id.plus_img);

        }
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating the layout
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_seeker, parent, false);
        return new SeekerCustomAdapter.ItemHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull SeekerCustomAdapter.ItemHolder holder, int position) {
        //setting the views with data
        holder.jobTitleView.setText(jobItems.get(position).getTitle());
        holder.jobLocationView.setText(jobItems.get(position).getLocation());
        holder.payView.setText(Double.toString(jobItems.get(position).getPay()));
        holder.workDateView.setText(jobItems.get(position).getWorkDate());
        holder.employerNameView.setText(jobItems.get(position).getEmployerName());
        holder.jobIdView.setText(jobItems.get(position).getId());
        holder.providerIdView.setText(jobItems.get(position).getProviderId());

        //job request will be sent when plus symbol is clicked
        holder.plusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "ID:"+jobItems.get(position).getId(), Toast.LENGTH_SHORT).show();

                AlertDialog.Builder alertDialog=new AlertDialog.Builder(mcontext);
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Do you want to request this job?");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase reference=FirebaseDatabase.getInstance();
                        Log.d("seekercustom","Provider ID: "+jobItems.get(position).getProviderId());
                        Log.d("seekercustom", "Job ID: "+jobItems.get(position).getId());
                        Log.d("seekercustom", "Current User ID: "+FirebaseAuth.getInstance().getCurrentUser().getUid());
                        Log.d("seekercustom", "Current User Email: "+FirebaseAuth.getInstance().getCurrentUser().getEmail());
                        reference.getReference().child("Job").child(jobItems.get(position).getProviderId()).child(jobItems.get(position).getId()).child("seekers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                        Toast.makeText(mcontext,"Request Sent Successfully",Toast.LENGTH_SHORT);
                    }
                });
                alertDialog.setNegativeButton("NO", null);
                alertDialog.show();

            }
        });



    }


    @Override
    public int getItemCount() {
        return jobItems.size();
    }
}
