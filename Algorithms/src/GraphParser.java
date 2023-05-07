import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GraphParser {
    // parse method that takes in the filename of the input file and returns a Graph object
    public static Graph parse(String input) throws FileNotFoundException {
        File inputFile = new File(input);  // create a File object from the input filename
        Scanner scanner = null;
        try {
            scanner = new Scanner(inputFile); // try to create a scanner object from the file
        } catch (FileNotFoundException e) {
            throw e; // if the file is not found, throw the exception

        }
        int numVertices = 0;
        // count the number of vertices in the graph by iterating through each line in the input file
        while (scanner.hasNext()) {
            scanner.nextInt(); // read in a number from the input file
            numVertices++; // increment the number of vertices
        }
        scanner.close();
        // create a new Graph object with the number of vertices counted
        Graph graph = new Graph(numVertices);

        // re-open the scanner and iterate through each line again, adding directed edges to the graph
        scanner = new Scanner(inputFile);
        while (scanner.hasNext()) {
            int from = scanner.nextInt(); // read in the "from" vertex of a directed edge
            int to = scanner.nextInt(); // read in the "to" vertex of a directed edge
            graph.addDirectedEdge(from, to);
        }
        scanner.close();
        return graph;

    }
}
