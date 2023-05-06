import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DirectedGraph {
    private final int numVertices;
    private ArrayList<Integer>[] adjList;

    public static void main(String[] args) {
        try {
            DirectedGraph graph = DirectedGraph.fromFile("input.txt");
            if (graph.isAcyclic()) {
                System.out.println("The graph is acyclic.");
            } else {
                System.out.println("The graph is cyclic. The cycles are:");
                ArrayList<ArrayList<Integer>> cycles = graph.getAllCycles();
                for (ArrayList<Integer> cycle : cycles) {
                    System.out.println(cycle);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Input file not found.");
        }
    }

    public DirectedGraph(int numVertices) {
        this.numVertices = numVertices;
        adjList = new ArrayList[numVertices];
        for (int i = 0; i < numVertices; i++) {
            adjList[i] = new ArrayList<Integer>();
        }
    }

    public void addEdge(int from, int to) {
        adjList[from].add(to);
    }

    public boolean isAcyclic() {
        boolean[] visited = new boolean[numVertices];
        boolean[] recStack = new boolean[numVertices];
        ArrayList<Integer> sinks = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> cycles = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < numVertices; i++) {
            if (!visited[i]) {
                dfs(i, visited, recStack, sinks, cycles);
            }
        }
        return cycles.isEmpty();
    }

    private void dfs(int v, boolean[] visited, boolean[] recStack, ArrayList<Integer> sinks,
                     ArrayList<ArrayList<Integer>> cycles) {
        visited[v] = true;
        recStack[v] = true;

        for (int neighbor : adjList[v]) {
            if (!visited[neighbor]) {
                dfs(neighbor, visited, recStack, sinks, cycles);
            } else if (recStack[neighbor]) {
                // Found a cycle
                ArrayList<Integer> cycle = new ArrayList<Integer>();
                cycle.add(neighbor);
                int u = v;
                while (u != neighbor) {
                    cycle.add(u);
                    u = sinks.contains(u) ? u : adjList[u].get(0);
                }
                cycle.add(neighbor);
                cycles.add(cycle);
            }
        }

        recStack[v] = false;
        if (adjList[v].isEmpty()) {
            sinks.add(v);
        }
    }

    public static DirectedGraph fromFile(String fileName) throws FileNotFoundException {
        File inputFile = new File(fileName);
        Scanner scanner = new Scanner(inputFile);
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

    public ArrayList<ArrayList<Integer>> getAllCycles() {
        ArrayList<ArrayList<Integer>> cycles = new ArrayList<ArrayList<Integer>>();
        boolean[] visited = new boolean[numVertices];
        boolean[] recStack = new boolean[numVertices];
        ArrayList<Integer> sinks = new ArrayList<Integer>();
        for (int i = 0; i < numVertices; i++) {
            if (!visited[i]) {
                dfs(i, visited, recStack, sinks, cycles);
            }
        }
        return cycles;
    }
}