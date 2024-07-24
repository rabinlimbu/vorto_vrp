package com.limburabin.manager;


import java.io.*;
import java.util.*;

import com.limburabin.common.AppConfig;
import com.limburabin.common.AppUtil;
import com.limburabin.model.*;

public class VRPManager {
    private Map<Integer, Load> loadMap = new HashMap<>();

    public VRPManager() {
    }

    /**
     *
     * @param filePath
     * @throws IOException
     */
    public void importLoads(String filePath) throws IOException {
        BufferedReader reader =
                new BufferedReader(new FileReader(filePath));
        reader.readLine();//skipping first line
        String line = null;
        while ((line = reader.readLine()) != null) {
            if (!AppUtil.isNullOrEmpty(line)) {
                String[] lineItems = line.trim().split("\\s+");
                Integer loadNumber = Integer.valueOf(lineItems[0]);
                Point pickUpPoint = parsePoint(lineItems[1]);
                Point dropOffPoint = parsePoint(lineItems[2]);
                Load newLoad = new Load(loadNumber, pickUpPoint, dropOffPoint);
                loadMap.put(loadNumber, newLoad);
            }
        }
        preProcessLoadData();
    }

    private Point parsePoint(String pointText) {
        pointText = pointText.replace("(", "").replace(")", "");
        String[] points = pointText.split(",");
        Double point1 = Double.parseDouble(points[0].trim());
        Double point2 = Double.parseDouble(points[1].trim());
        return new Point(point1, point2);
    }

    /**
     * preparing data with distances like depot to all loads, pick up and drop off load,
     * drop off to depot etc
     */
    private void preProcessLoadData() {
        if (loadMap.isEmpty())
            return;
        loadMap.values().stream().forEach((loadItem -> {
            Double distanceFromDepot = getDistance(new Point(AppConfig.DEPOT_POINT_X, AppConfig.DEPOT_POINT_Y), loadItem.getPickUp());
            loadItem.setDistanceFromDepot(distanceFromDepot);

            Double pickUpDropOffDistance = getDistance(loadItem.getPickUp(), loadItem.getDropOff());
            loadItem.setDistanceFromPickUpToDropOff(pickUpDropOffDistance);

            Double distanceFromDropOffToDepot = getDistance(loadItem.getDropOff(), new Point(AppConfig.DEPOT_POINT_X, AppConfig.DEPOT_POINT_Y));
            loadItem.setDistanceFromDropOffToDepot(distanceFromDropOffToDepot);
        }));

        for (Load loadItem : loadMap.values()) {
            setDropOffToAllOtherLoads(loadItem);
        }

        Map<Integer, Load> check = loadMap;
    }

    /**
     * associating all other loads by distance from source load.
     * source load is the current load that one of the driver is currently
     * working on.
     * @param source
     */
    private void setDropOffToAllOtherLoads(Load source) {
        List<Load> distanceToOtherLoads = new ArrayList<>();
        for (Load load : loadMap.values()) {
            if (!source.getLoadNumber().equals(load.getLoadNumber())) {
                Double distanceFromSourceDropOff = getDistance(source.getDropOff(), load.getPickUp());
                Load otherLoad = new Load(load, distanceFromSourceDropOff);
                distanceToOtherLoads.add(otherLoad);
            }
        }
        source.setDropOffToOtherLoads(distanceToOtherLoads);
    }

    public Double getDistance(Point point1, Point point2) {
        Double xDiff = point1.getX() - point2.getX();
        Double yDiff = point1.getY() - point2.getY();
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }

    public void assignLoadsToDrivers() {
        LoadAssignment loadAssignment = new LoadAssignment(this.loadMap);
        Result result = loadAssignment.assignLoadsToDrivers();
        for (List<Integer> loadAssigned : result.allLoadAssigned) {
            System.out.println(loadAssigned);
        }
    }
}
