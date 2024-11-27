package ktu.kaganndemirr;

import java.util.HashMap;
import java.util.Map;

public class NetSwitch{
    String Name;
    Map<Integer, Integer> delayTable = new HashMap<>();

    public NetSwitch(String name){
        Name = name;
    }

}
