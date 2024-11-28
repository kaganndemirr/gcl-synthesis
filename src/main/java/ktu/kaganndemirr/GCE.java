package ktu.kaganndemirr;

public class GCE {
    public int tOpen;
    public int tClose;
    public String streamName;
    public int streamIndex;

    public GCE(int tOpen, int tClose, String streamName, int streamIndex){
        this.tOpen = tOpen;
        this.tClose = tClose;
        this.streamName = streamName;
        this.streamIndex = streamIndex;
    }
}
