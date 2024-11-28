package ktu.kaganndemirr;

import org.apache.commons.cli.*;

import java.io.File;

public class NetOpt {

    private static final String MSG_ARG = "msg";
    private static final String VLS_ARG = "vls";

    public static void main(String[] args) throws ParseException {
        Option msgFile = Option.builder(MSG_ARG).required().argName("file").hasArg().desc("Use given file as msg").build();
        Option vlsFile = Option.builder(VLS_ARG).required().argName("file").hasArg().desc("Use given file as vls").build();

        Options options = new Options();
        options.addOption(msgFile);
        options.addOption(vlsFile);

        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(options, args);
        File msg = new File(line.getOptionValue(msgFile));
        File vls = new File(line.getOptionValue(vlsFile));

        DataLoader dataLoader = new DataLoader();
        dataLoader.Load(msg.toString(), vls.toString());

        Solution initial_Solution = new Solution(dataLoader);

        boolean debugmode = false;

        ORSolver optimizer = new ORSolver(initial_Solution, debugmode);

        optimizer.Run();

    }

}
