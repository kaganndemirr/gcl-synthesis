package ktu.kaganndemirr;

import java.util.ArrayList;
import java.util.List;

public class Que{
    int id;
    int Priority;
    int period;
    List<Stream> assignedStreams = new ArrayList<Stream>();
    int Hyperperiod = -1;

    public Que(int per){
        setID(per);
        setPriority(per);
        period = 1;
    }

    public Que(int _id, int _Pri , List<Stream> streams, int _period) {
        id = _id;
        Priority = _Pri;
        for (Stream s : streams) {
            AssignStream(s);
        }
        setPeriod(_period);
    }

    private void setID(int _id) {
        id = _id;
    }

    private void setPriority(int per) {
        Priority = per;
    }

    public void setPeriod(int p) {
        period = p;
    }

    public int getPeriod() {
        return period;
    }

    public void AssignStream(Stream s) {
        assignedStreams.add(s);
        if(Hyperperiod == -1) {
            Hyperperiod = 1;
        }
        Hyperperiod = LCM(Hyperperiod, s.Period);
    }

    public boolean isUsed() {
        return !assignedStreams.isEmpty();
    }

    public boolean HasStream(Stream s) {
        for (Stream stream : assignedStreams) {
            if(stream.Id == s.Id) {
                return true;
            }
        }
        return false;
    }

    // LCM (Least Common Multiple) hesaplama fonksiyonu
    public static int LCM(int a, int b) {
        // Checking for the largest
        // Number between them
        int ans = Math.max(a, b);

        // Checking for a smallest number that
        // can de divided by both numbers
        while (ans % a != 0 || ans % b != 0) {
            ans++;
        }

        return ans;
    }

    public Que Clone() {
        return new Que(id, Priority, assignedStreams, period);
    }
}
