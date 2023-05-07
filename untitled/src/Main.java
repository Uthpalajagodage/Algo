import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Graph {
    private final int numNodes;
    private ArrayList<Integer>[] adjacencyList;

    public static void main(String[] args) {
        try {
            Graph graph = Graph.fromFile("input.txt");
            if (graph.isAcyclic()) {
                System.out.println("This is an acyclic graph.");
            } else {
                System.out.println("This is a cyclic graph. The cycles are:");
                ArrayList<ArrayList<Integer>> cycles = graph.getAllCycles();
                for (ArrayList<Integer> cycle : cycles) {
                    System.out.println(cycle);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Input file not found.");
        }
    }

    public Graph(int numNodes) {
        this.numNodes = numNodes;
        adjacencyList = new ArrayList[numNodes];
        for (int i = 0; i < numNodes; i++) {
            adjacencyList[i] = new ArrayList<Integer>();
        }
    }

    public void addDirectedEdge(int from, int to) {
        adjacencyList[from].add(to);
    }

    public boolean isAcyclic() {
        boolean[] visited = new boolean[numNodes];
        boolean[] recStack = new boolean[numNodes];
        ArrayList<Integer> sinks = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> cycles = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < numNodes; i++) {
            if (!visited[i]) {
                dfs(i, visited, recStack, sinks, cycles);
            }
        }
        return cycles.isEmpty();
    }

    private void dfs(int node, boolean[] visited, boolean[] recStack, ArrayList<Integer> sinks,
                     ArrayList<ArrayList<Integer>> cycles) {
        visited[node] = true;
        recStack[node] = true;

        for (int neighbor : adjacencyList[node]) {
            if (!visited[neighbor]) {
                dfs(neighbor, visited, recStack, sinks, cycles);
            } else if (recStack[neighbor]) {
                // Found a cycle
                ArrayList<Integer> cycle = new ArrayList<Integer>();
                cycle.add(neighbor);
                int u = node;
                while (u != neighbor) {
                    cycle.add(u);
                    u = sinks.contains(u) ? u : adjacencyList[u].get(0);
                }
                cycle.add(neighbor);
                cycles.add(cycle);
            }
        }

        recStack[node] = false;
        if (adjacencyList[node].isEmpty()) {
            sinks.add(node);
        }
    }

    public static Graph fromFile(String fileName) throws FileNotFoundException {
        File inputFile = new File(fileName);
        Scanner scanner = new Scanner(inputFile);
        int numNodes = 0;
        while (scanner.hasNext()) {
            scanner.nextInt();
            numNodes++;
        }
        scanner.close();
        Graph graph = new Graph(numNodes);
        scanner = new Scanner(inputFile);
        while (scanner.hasNext()) {
            int from = scanner.nextInt();
            int to = scanner.nextInt();
            graph.addDirectedEdge(from, to);
        }
        scanner.close();
        return graph;
    }

    public ArrayList<ArrayList<Integer>> getAllCycles() {
        ArrayList<ArrayList<Integer>> cycles = new ArrayList<ArrayList<Integer>>();
        boolean[] visited = new boolean[numNodes];
        boolean[] recStack = new boolean[numNodes];
        ArrayList<Integer> sinks = new ArrayList<Integer>();
        for (int i = 0; i < numNodes; i++) {
            if (!visited[i]) {
                dfs(i, visited, recStack, sinks, cycles);
            }
        }
        return cycles;
    }
}