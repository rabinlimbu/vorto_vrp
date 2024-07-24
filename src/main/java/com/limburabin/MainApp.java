package com.limburabin;

import com.limburabin.common.AppUtil;
import com.limburabin.manager.VRPManager;
import org.apache.commons.cli.*;

/**
 * Vorto VRP Test
 */
public class MainApp {
    public static void main(String[] args) throws Exception {
        Options options = new Options();
        Option vrpFilePath = new Option("vrp", "vehicle-route-problem", true, "vrp file path");
        vrpFilePath.setRequired(true);
        options.addOption(vrpFilePath);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(1);
        }

        String filePath = cmd.getOptionValue("vehicle-route-problem");
        if (!AppUtil.isNullOrEmpty(filePath)) {
            VRPManager vrpManager = new VRPManager();
            vrpManager.importLoads(filePath);
            vrpManager.assignLoadsToDrivers();
        }
    }
}
