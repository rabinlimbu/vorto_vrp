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

        Option superModeArg = new Option("sm", "super-mode", true, "super mode on/off");
        options.addOption(superModeArg);

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
        Boolean superMode = Boolean.valueOf(cmd.getOptionValue("super-mode"));
        if (!AppUtil.isNullOrEmpty(filePath)) {
            VRPManager vrpManager = new VRPManager(superMode);
            vrpManager.importLoads(filePath);
            vrpManager.assignLoadsToDrivers();
        }
    }
}
