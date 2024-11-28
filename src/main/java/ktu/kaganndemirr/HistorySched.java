package ktu.kaganndemirr;

import java.util.List;

public class HistorySched {
    public String source;
    public String target;
    public List<GCE> gceList;

    public HistorySched(String source, String target, List<GCE> gceList){
        this.source = source;
        this.target = target;
        this.gceList = gceList;
    }

    public void setGCEList(List<GCE> gceList) {
        this.gceList = gceList;
    }
}


