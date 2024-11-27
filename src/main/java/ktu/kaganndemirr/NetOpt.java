package ktu.kaganndemirr;

public class NetOpt {

	public static void main(String[] args) {
//		String msg = "in_TIE_TT+AVB_Orion/in/msg.txt";
//		String vls = "in_TIE_TT+AVB_Orion/in/vls.txt";
//
//        String msg = "in_TIE_TT+AVB_Orion_Only_TT/in/msg.txt";
//        String vls = "in_TIE_TT+AVB_Orion_Only_TT/in/vls.txt";
//
//        String msg = "debug_scenario/msg.txt";
//        String vls = "debug_scenario/vls.txt";

        String msg = "mustafa_industrial_topology/in/msg.txt";
        String vls = "mustafa_industrial_topology/in/vls.txt";

        DataLoader dataLoader = new DataLoader();
        dataLoader.Load(msg, vls);  

        Solution initial_Solution = new Solution(dataLoader);
        
        boolean debugmode = false;
        
        ORSolver optimizer = new ORSolver(initial_Solution, debugmode);
        
        
        //Run optimizer
        if(args.length != 0)
        {
            optimizer.setResultPath(args[0]);
        }

        optimizer.Run();

	}

}
