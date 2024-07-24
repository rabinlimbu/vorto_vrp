package com.limburabin.common;


public class AppConfig {
    public final static Double REGULAR_HOUR = 12.0;
    public final static Double DRIVER_TOTAL_TIME_LIMIT = REGULAR_HOUR * 60;

    // this threshold is to control assigning load to driver
    // based on driver usage time
    public final static Double CHECK_OTHER_LOAD_THRESHOLD = 90.0;
}
