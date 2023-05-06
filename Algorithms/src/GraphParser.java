import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GraphParser {
    public static DirectedGraph parse(String input) throws FileNotFoundException {
        File inputFile = new File("input.txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(inputFile);
        } catch (FileNotFoundException e) {

            System.out.println("Input file not found.");
            return null;
        }
//        Scanner scanner = new Scanner(inputFile);
        int numVertices = 0;
        while (scanner.hasNext()) {
            scanner.nextInt();
            numVertices++;
        }
        scanner.close();
        DirectedGraph graph = new DirectedGraph(numVertices);
        scanner = new Scanner(inputFile);
        while (scanner.hasNext()) {
            int from = scanner.nextInt();
            int to = scanner.nextInt();
            graph.addEdge(from, to);
        }
        scanner.close();
        return graph;

    }
}