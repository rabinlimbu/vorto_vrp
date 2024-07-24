package com.limburabin.model;

import java.util.List;

public class Result {
    public List<List<Integer>> allLoadAssigned;
    public Integer totalDrivers;
    public Double totalTime;
    public Integer totalLoads;

    public Result(List<List<Integer>> loadAssigned, Integer totalLoads, Integer totalDrivers, Double totalTime) {
        this.allLoadAssigned = loadAssigned;
        this.totalLoads = totalLoads;
        this.totalDrivers = totalDrivers;
        this.totalTime = totalTime;
    }
}
