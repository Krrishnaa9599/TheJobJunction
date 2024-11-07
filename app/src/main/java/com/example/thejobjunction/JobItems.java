package com.example.thejobjunction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class JobItems {
    // declared variables
    String title;
    String location;
    double pay;
    String workDate;
    String employerName;
    String id;
    String providerId;
    public String jobStatus;
    String seeker;

    //Declared all getters and setters for above variables
    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPay(double pay) {
        this.pay = pay;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public void setSeeker(String seeker) {
        this.seeker = seeker;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getWorkDate() {
        return workDate;
    }


    public String getId() {
        return id;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public double getPay() {
        return pay;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public String getSeeker() {
        return seeker;
    }

    public String getEmployerName() {
        return employerName;
    }


    //code to create a job and pass the info into Firebase
    public static void createJob(String userid, String title, String location, double pay, String employerName, String workDate, String jobStatus){

        FirebaseDatabase reference=FirebaseDatabase.getInstance();
        String uid=reference.getReference().child("Job").child(userid).push().getKey();
        reference.getReference().child("Job").child(userid).child(uid).child("id").setValue(uid);
        reference.getReference().child("Job").child(userid).child(uid).child("providerId").setValue(userid);
        reference.getReference().child("Job").child(userid).child(uid).child("title").setValue(title);
        reference.getReference().child("Job").child(userid).child(uid).child("location").setValue(location);
        reference.getReference().child("Job").child(userid).child(uid).child("pay").setValue(pay);
        reference.getReference().child("Job").child(userid).child(uid).child("employerName").setValue(employerName);
        reference.getReference().child("Job").child(userid).child(uid).child("workDate").setValue(workDate);
        reference.getReference().child("Job").child(userid).child(uid).child("jobStatus").setValue(jobStatus);

    }

}
