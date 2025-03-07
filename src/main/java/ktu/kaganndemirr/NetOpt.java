package ktu.kaganndemirr;

import ktu.kaganndemirr.ORSolver.methods;

public class NetOpt {

	public static void main(String[] args) {
		//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		//Test Cases
        //String testcase = "src/TestCases/Jorge/JorgeCase1.xml";
        //String testcase = "src/TestCases/GMModified/GML.xml";
        //String testcase = "src/TestCases/Orion/Orig/orion.xml";
        //String testcase = "src/TestCases/GM/GM.xml";
		//String testcase = "src/TestCases/FORA/input.xml";
		//String testcase = "src/TestCases/Initial/testcase1.xml";
		//String testcase = "src/TestCases/Initial/testcase2.xml";
		//String testcase = "src/TestCases/GM/GM.xml";
		//String testcase = "src/TestCases/TII/TestCase1/test.xml";
		//String testcase = "src/TestCases/RTAS/Orion/orion.xml";
		//String msg = "src/TestCases/Luxi/TC5/msg.txt";
		//String vls = "src/TestCases/Luxi/TC5/vls.txt";
		String msg = "ExampleInput/msg.txt";
		String vls = "ExampleInput/vls.txt";

        //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// Loading Data
        DataLoader dataLoader = new DataLoader();
        //dataLoader.Load(testcase);
        //Method call for old input version
        dataLoader.Load(msg, vls);  

        //Loading Completed
        //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        //Creating Solutions
        Solution initial_Solution = new Solution(dataLoader);
         
        //Solution Created
        //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        //Creating Solver

        //Select Between Methods
        methods chosenMethods = methods.Silviu;
        
        boolean debugmode = true;
        
        ORSolver optimizer = new ORSolver(chosenMethods, initial_Solution, debugmode);
        
        
        //Run optimizer
        if(args.length != 0)
        {
            optimizer.setResultPath(args[0]);
        }

        optimizer.Run();
        
        //optimization Finished
        //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

	}

}
