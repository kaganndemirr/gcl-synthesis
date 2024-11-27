package ktu.kaganndemirr;

import java.util.ArrayList;
import java.util.List;

public class Routes{
    int id;
    List<String> nodes = new ArrayList<>();
    List<Integer> messsageIDs = new ArrayList<>();

    public Routes(int _id, List<String> _nodes, List<Integer> _IDs){
        id = _id;
        nodes.addAll(_nodes);
        messsageIDs.addAll(_IDs);

    }

}
