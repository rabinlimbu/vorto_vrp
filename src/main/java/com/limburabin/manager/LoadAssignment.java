package com.limburabin.manager;


import java.util.*;
import java.util.stream.Collectors;

import com.limburabin.common.AppConfig;
import com.limburabin.model.*;

public class LoadAssignment {
    private Map<Integer, Load> loadMap = null;

    LoadAssignment(Map<Integer, Load> loadMap) {
        this.loadMap = loadMap;
    }

    /**
     * drivers are assigned to loads based on closest distance.
     * @return Result
     */
    public Result assignLoadsToDrivers() {
        List<List<Integer>> allLoadAssigned = new ArrayList<>();
        Integer totalDrivers = 0, totalLoads = 0;
        Double totalTimeOfAllDrivers = 0.0;

        //sorting loads by distance to depot
        List<Load> loadList = this.loadMap.values().stream().sorted((a, b) -> Double.compare(a.getDistanceFromDepot(), b.getDistanceFromDepot()))
                .collect(Collectors.toList());

        for (Load load : loadList) {
            Stack<Load> loadStack = new Stack<>();
            Stack<Double> driverCurrentTotalTimeStack = new Stack<>();
            List<Integer> loadAssigned = new ArrayList<>();

            Double currentTotalTime = 0.0;
            if (this.loadMap.get(load.getLoadNumber()).getState().equals(LoadState.NOT_STARTED)) {
                load.setState(LoadState.IN_PROGRESS);
                totalDrivers += 1;
                loadStack.push(load);
                currentTotalTime += load.getDistanceFromDepot() + load.getDistanceFromPickUpToDropOff();
                driverCurrentTotalTimeStack.push(currentTotalTime);

                Double driverUsedTimeInPercent = getDriverUsedTimeInPercent(currentTotalTime);
                if (driverUsedTimeInPercent < AppConfig.CHECK_OTHER_LOAD_THRESHOLD) {
                    if (load.getLoadNumber() == 73 || load.getLoadNumber() == 24) {
                        String breakpoint = "";
                    }

                    Load nextLoad = getNextClosestLoadProspect(load, currentTotalTime);
                    while (nextLoad != null) {
                        Load parentLoad = loadMap.get(nextLoad.getLoadNumber());

                        loadStack.push(parentLoad);
                        Double newCurrentTotalTimeToDropOff = getTotalTimeToDropOffLoad(nextLoad, currentTotalTime);
                        driverCurrentTotalTimeStack.push(newCurrentTotalTimeToDropOff);
                        currentTotalTime = newCurrentTotalTimeToDropOff;

                        //flag this load
                        parentLoad.setState(LoadState.IN_PROGRESS);

                        //repeat on the new parent load
                        nextLoad = getNextClosestLoadProspect(parentLoad, newCurrentTotalTimeToDropOff);
                    }
                }

                Boolean totalTimeAdded = false;
                while (!loadStack.isEmpty()) {
                    Load currentLoad = loadStack.peek();
                    Double newCurrentTotalTimeToDropOff = driverCurrentTotalTimeStack.peek();
                    Double currentTotalTimeToDepot = newCurrentTotalTimeToDropOff + currentLoad.getDistanceFromDropOffToDepot();
                    if (currentTotalTimeToDepot > AppConfig.DRIVER_TOTAL_TIME_LIMIT) {
                        currentLoad.setState(LoadState.NOT_STARTED);
                    } else {
                        loadAssigned.add(currentLoad.getLoadNumber());
                        currentLoad.setState(LoadState.COMPLETE);
                        totalLoads += 1;
                        if (!totalTimeAdded) {
                            totalTimeOfAllDrivers += currentTotalTimeToDepot;
                            totalTimeAdded = true;
                        }
                    }
                    loadStack.pop();
                    driverCurrentTotalTimeStack.pop();
                }
                allLoadAssigned.add(loadAssigned);
            }
        }

        Result result = new Result(allLoadAssigned, totalLoads, totalDrivers, totalTimeOfAllDrivers);
        return result;
    }

    private Load getNextClosestLoadProspect(Load parentLoad, Double currentTotalTime) {
        List<Load> otherLoadList = parentLoad.getDropOffToOtherLoads().stream()
                .sorted((a, b) -> Double.compare(a.getDistanceFromSourceDropOff(), b.getDistanceFromSourceDropOff()))
                .collect(Collectors.toList());

        for (Load nextLoad : otherLoadList) {
            Load nextParentLoad = this.loadMap.get(nextLoad.getLoadNumber());
            if (nextParentLoad.getState().equals(LoadState.NOT_STARTED)) {
                Double newCurrentTotalTimeToDropOff = getTotalTimeToDropOffLoad(nextLoad, currentTotalTime);
                Double newUsedTimeToDropOffInPercent = getDriverUsedTimeInPercent(newCurrentTotalTimeToDropOff);

                if (newUsedTimeToDropOffInPercent < AppConfig.CHECK_OTHER_LOAD_THRESHOLD)
                    return nextLoad;
                else
                    return null;
            }
        }
        return null;
    }

    public Double getNewCurrentTotalTimeToDepot(Load nextLoad, Double currentTotalTime) {
        Double newCurrentTotalTime = nextLoad.getDistanceFromSourceDropOff() +
                nextLoad.getDistanceFromPickUpToDropOff() +
                nextLoad.getDistanceFromDropOffToDepot() + currentTotalTime;
        return newCurrentTotalTime;
    }

    public Double getTotalTimeToDropOffLoad(Load nextLoad, Double currentTotalTime) {
        Double totalTimeToDropOffLoad = nextLoad.getDistanceFromSourceDropOff() +
                nextLoad.getDistanceFromPickUpToDropOff() + currentTotalTime;
        return totalTimeToDropOffLoad;
    }

    public Double getDriverUsedTimeInPercent(Double currentDriverAssignedTime) {
        Double usedPercent = (currentDriverAssignedTime / AppConfig.DRIVER_TOTAL_TIME_LIMIT) * 100;
        return usedPercent;
    }
}
