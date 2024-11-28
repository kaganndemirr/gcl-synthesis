package ktu.kaganndemirr;

public class Messages{
    int id;
    String name;
    int period;
    int deadline;
    int size;
    int priority;
    int offset;

    public Messages(int _id, String name, int _period,int _deadline, int _size, int _priority, int _offset){
        id = _id;
        this.name = name;
        period = _period;
        deadline = _deadline;
        size = _size;
        priority = _priority;
        offset = _offset;

    }


}
