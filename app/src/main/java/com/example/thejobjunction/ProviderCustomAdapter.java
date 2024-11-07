package com.example.thejobjunction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProviderCustomAdapter extends RecyclerView.Adapter<ProviderCustomAdapter.ItemHolder> {

    //declaring variables
    private Context mcontext;
    private ArrayList<JobItems> jobItems;
    private FragmentManager fragmentManager;

    public String userId;
    private DatabaseReference reference;

    public ProviderCustomAdapter(Context mcontext, ArrayList<JobItems> jobItems, FragmentManager fragmentManager){

        //intializing variables
        this.mcontext=mcontext;
        this.jobItems=jobItems;
        this.fragmentManager=fragmentManager;
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        //declaring views
        private TextView jobTitleView, jobLocationView, payView, workDateView, employerNameView, jobIdView;
        private ImageView editImageView, deleteImageView;
        private Button showRequestsButton;

        public ItemHolder(View itemView){
            super(itemView);

            //getting reference of views
            jobTitleView= itemView.findViewById(R.id.job_title);
            jobLocationView= itemView.findViewById(R.id.job_location);
            payView= itemView.findViewById(R.id.job_pay);
            workDateView=itemView.findViewById(R.id.job_work_day);
            employerNameView=itemView.findViewById(R.id.job_emp_name);
            jobIdView=itemView.findViewById(R.id.job_id);
            editImageView=itemView.findViewById(R.id.edit_img);
            deleteImageView=itemView.findViewById(R.id.delete_img);
            showRequestsButton=itemView.findViewById(R.id.show_requests_button);
        }
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating xml

        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_provider, parent, false);
        return new ItemHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {

        //setting values to views
        holder.jobTitleView.setText(jobItems.get(position).getTitle());
        holder.jobLocationView.setText(jobItems.get(position).getLocation());
        holder.payView.setText("$ "+jobItems.get(position).getPay());
        holder.workDateView.setText(jobItems.get(position).getWorkDate());
        holder.employerNameView.setText(jobItems.get(position).getEmployerName());
        holder.jobIdView.setText(jobItems.get(position).getId());

        //edits job details when edit button clicked
        holder.editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "ID:"+jobItems.get(position).getId(), Toast.LENGTH_SHORT).show();

                reference= FirebaseDatabase.getInstance().getReference().child("Job").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(jobItems.get(position).getId());
                reference.child("id").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            userId =snapshot.getValue(String.class);
                            Log.d("jobitem", "UserID: "+userId);
                            SharedPreferences sharedPreferences= mcontext.getSharedPreferences("sp", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("ID", userId);
                            editor.apply();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Log.d("sp", String.valueOf(mcontext));

                //calling jobproviderpostupdatefragment
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.select_fragment, new JobProviderPostUpdateFragment());
                fragmentTransaction.commit();
            }
        });

        //deletes job when delete button clicked
        holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(mcontext);
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Do you want to delete this Job?");
                //alertDialog.setMessage("Do you want to delete this Job?");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Long.toString(jobItems.get(position).getId())
                        FirebaseDatabase.getInstance().getReference().child("Job").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(jobItems.get(position).getId()).removeValue();
                        //reference.removeValue();
                    }
                });
                alertDialog.setNegativeButton("NO", null);
                alertDialog.show();
            }
        });

        //retrives all seekers who requested for the job
        holder.showRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences= mcontext.getSharedPreferences("sp", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("jobID", jobItems.get(position).getId());
                editor.apply();

                Log.d("showrequests","Show Requests for jobID: "+jobItems.get(position).getId());

                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.select_fragment, new ProviderShowRequestsFragment());
                fragmentTransaction.commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return jobItems.size();
    }

}
