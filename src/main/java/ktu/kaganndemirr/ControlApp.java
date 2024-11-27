package ktu.kaganndemirr;

import java.util.ArrayList;
import java.util.List;


public class ControlApp{
    int id;
    int wcet;
    List<Integer> inIDs = new ArrayList<>();
    List<Integer> outIDs = new ArrayList<>();

    public ControlApp(int _id, int _wcet, List<Integer> _inIDs, List<Integer> _outIDs){
        id = _id;
        wcet = _wcet;
        inIDs.addAll(_inIDs);
        outIDs.addAll(_outIDs);
    }

}
