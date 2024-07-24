package com.limburabin.model;

import java.util.List;

public class Load {
    private final Integer loadNumber;
    private final Point pickUp;
    private final Point dropOff;
    private Double distanceFromDepot;
    private Double distanceFromPickToDropOff;
    private Double distanceFromDropOffToDepot;
    private Double distanceFromSourceDropOff;
    private List<Load> dropOffToOtherLoads;
    private LoadState state;

    public Load(Integer loadNumber, Point pickUp, Point dropOff) {
        this.loadNumber = loadNumber;
        this.pickUp = pickUp;
        this.dropOff = dropOff;
        this.state = LoadState.NOT_STARTED;
    }

    public Load(Load existingLoad, Double distanceFromSourceDropOff) {
        this(existingLoad.loadNumber, existingLoad.getPickUp(), existingLoad.getDropOff());
        this.setDistanceFromPickToDropOff(existingLoad.getDistanceFromPickToDropOff());
        this.setDistanceFromDropOffToDepot(existingLoad.getDistanceFromDropOffToDepot());
        this.distanceFromSourceDropOff = distanceFromSourceDropOff;
    }

    public Integer getLoadNumber() {
        return this.loadNumber;
    }

    public Point getPickUp() {
        return this.pickUp;
    }

    public Point getDropOff() {
        return this.dropOff;
    }

    public Double getDistanceFromDepot() {
        return this.distanceFromDepot;
    }

    public void setDistanceFromDepot(Double distance) {
        this.distanceFromDepot = distance;
    }

    public Double getDistanceFromPickToDropOff() {
        return this.distanceFromPickToDropOff;
    }

    public void setDistanceFromPickToDropOff(Double distance) {
        this.distanceFromPickToDropOff = distance;
    }

    public Double getDistanceFromDropOffToDepot() {
        return this.distanceFromDropOffToDepot;
    }

    public void setDistanceFromDropOffToDepot(Double distanceFromDropOffToDepot) {
        this.distanceFromDropOffToDepot = distanceFromDropOffToDepot;
    }

    public Double getDistanceFromSourceDropOff() {
        return distanceFromSourceDropOff;
    }

    public void setDistanceFromSourceDropOff(Double distanceFromSourceDropOff) {
        this.distanceFromSourceDropOff = distanceFromSourceDropOff;
    }

    public List<Load> getDropOffToOtherLoads() {
        return this.dropOffToOtherLoads;
    }

    public void setDropOffToOtherLoads(List<Load> dropOffToOtherLoads) {
        this.dropOffToOtherLoads = dropOffToOtherLoads;
    }

    public LoadState getState() {
        return state;
    }

    public void setState(LoadState state) {
        this.state = state;
    }
}
