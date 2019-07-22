import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        if (! args[0].equals("-h") && args.length != 2 && args.length != 3) {
            System.err.println("Wrong number of arguments. " +
                    "Run with '-h' for usage info.");
            System.exit(1);
        } else if (args[0].equals("-h")) {
            System.out.println("Usage: java Main [-bhi] samplesize inputfile");
            System.out.println("\t-b: run the -BASE version of TRIEST (default)");
            System.out.println("\t-h: print this help message and exit");
            System.out.println("\t-i: print the -IMPR version of TRIEST");
            System.out.println("Only one of '-b', '-h', and '-i' can be specified.");
            System.exit(0);
        }
        boolean impr = false; // by default, we run the -BASE version.
        if (args.length == 3) {
            // In recent Java (>= 7), we could do the following check with a
            // switch statement, but let's be conservative as people may run old
            // versions.
            if (args[0].length() != 2) {
                System.err.println("Invalid argument '" + args[0] +
                        "'. Run with '-h' for usage info.");
                System.exit(1);
            }
            char flag = args[0].charAt(1);
            switch (flag) {
                case 'b':
                    impr = false;
                    break;
                case 'i':
                    impr = true;
                    break;
                default:
                    System.err.println("Invalid argument '" + args[0] +
                            "'. Run with '-h' for usage info.");
            }
        }
        // Read the sample size argument.
        int sampleSize = 0;
        try {
            sampleSize = Integer.parseInt(args[args.length - 2]);
            if (sampleSize <= 0)
                throw new NumberFormatException();
        } catch(NumberFormatException e) {
            System.err.println("The 'samplesize' argument must be a positive integer.");
            System.exit(1);
        }

        // Create a reader for the input file.
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(args[args.length - 1])));
        } catch(IOException e) {
            System.err.println("File '" + args[args.length - 1] +
                    "' not found or not readable.");
            System.exit(1);
        }

        DataStreamAlgo algo;
        if (impr)
            algo = new TriestImpr(sampleSize);
        else
            algo = new TriestBase(sampleSize);

        // Iterate over the lines of the input file: read the edge on the line,
        // pass the edge to the algorithm to handle it, and print the new
        // estimation of the number of triangles.
        try {
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                algo.handleEdge(new Edge(line.trim()));
                System.out.println(algo.getEstimate()+" ");
            }
        } catch(IOException io) {
            System.err.println("Error reading the file:" + io);
            System.exit(1);
        }
    }
}
