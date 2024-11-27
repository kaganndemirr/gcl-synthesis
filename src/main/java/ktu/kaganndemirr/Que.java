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

    // GCD (Greatest Common Divisor) hesaplama fonksiyonu
    public static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // LCM (Least Common Multiple) hesaplama fonksiyonu
    public static int LCM(int a, int b) {
        return (a * b) / gcd(a, b);
    }

    public Que Clone() {
        return new Que(id, Priority, assignedStreams, period);
    }
}
