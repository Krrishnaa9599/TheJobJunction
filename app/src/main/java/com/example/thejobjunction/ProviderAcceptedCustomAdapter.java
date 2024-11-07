package com.example.thejobjunction;

import android.content.Context;
import android.content.DialogInterface;
import android.provider.ContactsContract;
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

public class ProviderAcceptedCustomAdapter extends RecyclerView.Adapter<ProviderAcceptedCustomAdapter.ItemHolder> {

    //declared variables
    private Context mcontext;
    private ArrayList<JobItems> jobItems;
    private FragmentManager fragmentManager;
    private DatabaseReference reference;

    public ProviderAcceptedCustomAdapter(Context mcontext, ArrayList<JobItems> jobItems, FragmentManager fragmentManager){
        //initialized variables
        this.mcontext=mcontext;
        this.jobItems=jobItems;
        this.fragmentManager=fragmentManager;
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        //declared views
        private TextView jobTitleView, jobLocationView, payView, workDateView, employerNameView, jobIdView, providerIdView, seekerView;


        public ItemHolder(View itemView){
            super(itemView);
            //initialized views
            jobTitleView= itemView.findViewById(R.id.job_title);
            jobLocationView= itemView.findViewById(R.id.job_location);
            payView= itemView.findViewById(R.id.job_pay);
            workDateView=itemView.findViewById(R.id.job_work_day);
            employerNameView=itemView.findViewById(R.id.job_emp_name);
            jobIdView=itemView.findViewById(R.id.job_id);
            providerIdView=itemView.findViewById(R.id.provider_id);
            seekerView=itemView.findViewById(R.id.seeker_email);
        }
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflated layout
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_provider_accepted, parent, false);
        return new ProviderAcceptedCustomAdapter.ItemHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ProviderAcceptedCustomAdapter.ItemHolder holder, int position) {
        //assigning values to views
        holder.jobTitleView.setText(jobItems.get(position).getTitle());
        holder.jobLocationView.setText(jobItems.get(position).getLocation());
        holder.payView.setText("$ "+Double.toString(jobItems.get(position).getPay()));
        holder.workDateView.setText(jobItems.get(position).getWorkDate());
        holder.employerNameView.setText(jobItems.get(position).getEmployerName());
        holder.jobIdView.setText(jobItems.get(position).getId());
        holder.providerIdView.setText(jobItems.get(position).getProviderId());
        DatabaseReference usernameReference=FirebaseDatabase.getInstance().getReference().child("Users").child(jobItems.get(position).getSeeker()).child("username");
        usernameReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.seekerView.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //holder.seekerView.setText(jobItems.get(position).getSeeker());
    }


    @Override
    public int getItemCount() {
        return jobItems.size();
    }
}
