package ktu.kaganndemirr;


import com.google.ortools.constraintsolver.Solver;

class ORSolver {

    Solver solver;
    Solution Current;
    DataUnloader dataUnloader = new DataUnloader();
    boolean DebugMode;
    
    
	static { System.loadLibrary("jniortools");}

	public ORSolver(Solution current, boolean debugstat) {
		setSolution(current);
		DebugMode = debugstat ;
	
	}
	private void setSolution(Solution current) {
		Current = current;
	}

	public void setResultPath(String _path) {
		dataUnloader.setDirPath(_path);
	}

	public void Run() {

	    solver = new Solver("Silviu");
		SolutionMethod method = new Silviu(solver);

        method.Initialize(Current);
		method.addConstraints();	
		method.addCosts();
		method.addDecision();		
		method.addSolverLimits();
		
    	    
	    
	    Solution OptSolution = null;
	    long start=(System.currentTimeMillis());

		while (solver.nextSolution()) {
			OptSolution = method.cloneSolution();
			long now= ( System.currentTimeMillis() );
			long Tnow = now - start;
			System.out.println("Found, " +  Tnow);


			dataUnloader.CaptureSolution(OptSolution, Tnow);
			if(DebugMode) {
				dataUnloader.WriteData(OptSolution, "Silviu", method.getSolutionNumber());
			}

			if(method.Monitor(start)) {
				break;
			}

		}
		solver.endSearch();

	    long end= ( System.currentTimeMillis());
	    long duration = end - start;
	    
	    if(OptSolution != null) {
	    	if(!DebugMode) {
	    		dataUnloader.WriteData(OptSolution, "Silviu", method.getSolutionNumber());
	    	}
	    }



	    // Statistics
	    System.out.println();
	    System.out.println("Solutions: " + solver.solutions());
	    System.out.println("Failures: " + solver.failures());
	    System.out.println("Branches: " + solver.branches());
	    System.out.println("Wall time: " + solver.wallTime() + "ms");

	}
}
