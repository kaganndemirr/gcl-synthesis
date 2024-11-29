package ktu.kaganndemirr;

import java.util.ArrayList;
import java.util.List;

public class Port {
    int[] affiliatedQue;
    int[] Topen;
    int[] Tclose;
    int[][] indexMap;
    int GCLSize = 0;
    boolean outPort;
    int Hyperperiod;
    int Period;
    String connectedTo;
    boolean connectedToES;
    int _microtick;
    int propagationDelay = 0;
    List<Que> ques = new ArrayList<Que>();
    int QLength = 8;
    List<Stream> AssignedStreams =  new ArrayList<Stream>();


    public Port(String sideName, boolean isOut, int microtick){
        connectedTo = sideName;
        connectedToES = !sideName.contains("SW") || !sideName.contains("B");
        outPort = isOut;

        for (int i = 0; i < QLength; i++) {
            ques.add(new Que(i));
        }
        _microtick = microtick;
        Period = 0;
        Hyperperiod = 1;

    }
    public Port(String sideName, boolean isOut, List<Stream> _assignedstreams, boolean _c_to_es, int gcl, int[] _affq, int[] _topen, int[] _tclose, int[][] _index, int microtick, int _Period, int _Hyperperiod, List<Que>_ques){
        connectedTo = sideName;
        outPort = isOut;
        Hyperperiod = 1;
        for (Que q : _ques) {
            ques.add(q.Clone());
        }



        for (Stream stream : _assignedstreams) {
            AssignStream(stream.Clone());
        }
        connectedToES = _c_to_es;
        GCLSize = gcl;
        if(_affq != null) {
            affiliatedQue = new int[_affq.length];
            System.arraycopy(_affq, 0, affiliatedQue, 0, _affq.length);
        }
        if(_topen != null ) {
            Topen = new int[_topen.length];
            System.arraycopy(_topen, 0, Topen, 0, _topen.length);
        }
        if(_tclose != null) {
            Tclose = new int[_tclose.length];
            System.arraycopy(_tclose, 0, Tclose, 0, _tclose.length);
        }
        if(_index != null) {
            indexMap = new int[_index.length][];
            for (int i = 0; i < _index.length; i++) {
                indexMap[i] = new int[_index[i].length];
                System.arraycopy(_index[i], 0, indexMap[i], 0, _index[i].length);
            }
        }
        _microtick = microtick;
        Period = _Period;
        Hyperperiod = _Hyperperiod;


    }
    int getPeriod() {
        return Period;
    }

    void setPeriod(int P) {
        Period = P;
    }

    int getUsedQ() {
        int used = 0;
        for (Que Q : ques) {
            if(!Q.assignedStreams.isEmpty()) {
                used++;
            }
        }
        return used;
    }

    void AssignStream(Stream s) {
        AssignedStreams.add(s);
        Hyperperiod = Util.lcm(Hyperperiod, s.Period);

    }

    public boolean HasStream(Stream s) {
        for (Stream stream : AssignedStreams) {
            if(stream.Id == s.Id) {
                return true;
            }
        }
        return false;
    }

    public void SetGCLs(int _size) {
        Topen = new int[_size];
        Tclose = new int[_size];
        affiliatedQue = new int[_size];
        GCLSize = _size;
    }

    void initiate() {
        int GCLsize = 0;
        for (Stream stream : AssignedStreams) {
            ques.get(stream.Priority).AssignStream(stream);
            GCLsize += stream.N_instances;

        }

        GCLSize = GCLsize;
        indexMap = new int [AssignedStreams.size()][];
        for (int i = 0; i < AssignedStreams.size(); i++) {
            indexMap[i] = new int[AssignedStreams.get(i).N_instances];
        }
        int counter = 0;
        if(outPort) {

            for (int i = 0; i < AssignedStreams.size(); i++) {
                int N_istances = AssignedStreams.get(i).Hyperperiod / AssignedStreams.get(i).Period;
                for (int j = 0; j < N_istances; j++) {
                    indexMap[i][j] = counter;
                    //indexMap[i][j] = (int) Math.round((7*j)/8.0);
                    counter++ ;
                }
            }


        }



        //int counter = 0;
        //for (int i = 0; i < AssignedStreams.size(); i++) {
        //indexMap[i] = new int[AssignedStreams.get(i).N_instances];
        //for (int j = 0; j < AssignedStreams.get(i).N_instances; j++) {
        //indexMap[i][j] = counter;
        //counter++;
        //}

        //}


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

    public Port Clone() {
        return new Port(connectedTo, outPort, AssignedStreams, connectedToES, GCLSize, affiliatedQue, Topen, Tclose, indexMap, _microtick, Period, Hyperperiod , ques);
    }

}
