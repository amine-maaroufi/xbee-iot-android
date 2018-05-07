package iot.iotxbee.utils;

import java.util.regex.Pattern;

public class Utils {

    public static String ip="52.143.129.210";
    public static String token="A1_TEST_TOKEN";
    public static String server="http://"+ip+":8080"+"/api/v1/"+token+"/attributes?clientKeys=temperature,mouvement";
    private static final Pattern PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public static boolean validateIP(final String ip) {
        return PATTERN.matcher(ip).matches();
    }
}
