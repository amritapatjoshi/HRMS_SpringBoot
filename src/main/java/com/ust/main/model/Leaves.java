package com.ust.main.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "employee_leaves")
public class Leaves {

//    EARNED_LEAVES,
//    SICK_LEAVES,
//    BEREAVEMENT_LEAVES

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int earnedLeaves;

    private int sickLeaves;

    private int bereavementLeaves;


    public int getEarnedLeaves() {
        return earnedLeaves;
    }

    public void setEarnedLeaves(int earnedLeaves) {
        this.earnedLeaves = earnedLeaves;
    }

    public int getSickLeaves() {
        return sickLeaves;
    }

    public void setSickLeaves(int sickLeaves) {
        this.sickLeaves = sickLeaves;
    }

    public int getBereavementLeaves() {
        return bereavementLeaves;
    }

    public void setBereavementLeaves(int bereavementLeaves) {
        this.bereavementLeaves = bereavementLeaves;
    }
}
