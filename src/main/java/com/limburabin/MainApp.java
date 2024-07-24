package com.limburabin;

import com.limburabin.common.AppUtil;
import com.limburabin.manager.VRPManager;

/**
 * Vorto VRP Test
 */
public class MainApp {
    public static void main(String[] args) throws Exception {
        VRPManager vrpManager = new VRPManager();
        String filePath = args[0];
        if (!AppUtil.isNullOrEmpty(filePath)) {
            vrpManager.importLoads(filePath);
            vrpManager.assignLoadsToDrivers();
        }
    }
}
