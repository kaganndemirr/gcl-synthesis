package ktu.kaganndemirr;

import java.util.HashMap;

public class Constant {

    public static HashMap<String, String> trafficTypePriorityMap;

    static {
        trafficTypePriorityMap = new HashMap<>();
        trafficTypePriorityMap.put("CLASS_F", "1");
        trafficTypePriorityMap.put("CLASS_E", "2");
        trafficTypePriorityMap.put("CLASS_D", "3");
        trafficTypePriorityMap.put("CLASS_C", "4");
        trafficTypePriorityMap.put("CLASS_B", "5");
        trafficTypePriorityMap.put("CLASS_A", "6");
        trafficTypePriorityMap.put("TT", "7");
    }
}
