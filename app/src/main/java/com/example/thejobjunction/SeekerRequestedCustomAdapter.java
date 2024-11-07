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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SeekerRequestedCustomAdapter extends RecyclerView.Adapter<SeekerRequestedCustomAdapter.ItemHolder> {

    //declaring the variables
    private Context mcontext;
    private ArrayList<JobItems> jobItems;
    private FragmentManager fragmentManager;
    private DatabaseReference reference;

    public SeekerRequestedCustomAdapter(Context mcontext, ArrayList<JobItems> jobItems, FragmentManager fragmentManager){
        //initializing the variables
        this.mcontext=mcontext;
        this.jobItems=jobItems;
        this.fragmentManager=fragmentManager;
    }

    public class ItemHolder extends RecyclerView.ViewHolder{

        //initializing the views
        private TextView jobTitleView, jobLocationView, payView, workDateView, employerNameView, jobIdView, providerIdView;


        public ItemHolder(View itemView){
            super(itemView);
            //getting the view references
            jobTitleView= itemView.findViewById(R.id.job_title);
            jobLocationView= itemView.findViewById(R.id.job_location);
            payView= itemView.findViewById(R.id.job_pay);
            workDateView=itemView.findViewById(R.id.job_work_day);
            employerNameView=itemView.findViewById(R.id.job_emp_name);
            //jobIdView=itemView.findViewById(R.id.job_id);
            //providerIdView=itemView.findViewById(R.id.provider_id);
            //plusImageView=itemView.findViewById(R.id.plus_img);

        }
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating the layout
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_seeker_requested, parent, false);
        return new SeekerRequestedCustomAdapter.ItemHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull SeekerRequestedCustomAdapter.ItemHolder holder, int position) {
        //setting the data to views
        holder.jobTitleView.setText(jobItems.get(position).getTitle());
        holder.jobLocationView.setText(jobItems.get(position).getLocation());
        holder.payView.setText("$ "+Double.toString(jobItems.get(position).getPay()));
        holder.workDateView.setText(jobItems.get(position).getWorkDate());
        holder.employerNameView.setText(jobItems.get(position).getEmployerName());
        //holder.jobIdView.setText(jobItems.get(position).getId());
        //holder.providerIdView.setText(jobItems.get(position).getProviderId());

    }


    @Override
    public int getItemCount() {
        return jobItems.size();
    }
}
