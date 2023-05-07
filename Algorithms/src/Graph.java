import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Graph {
    private final int verticesNo; //the number of vertices
    private ArrayList<Integer>[] adjacencyList; //representation of adjacency list

    public static void main(String[] args) { //main method
        try {
            Graph graph = Graph.fromFile("inputFile.txt"); //create graph from inputfile
            //check if graph is acyclic
            if (graph.isAcyclic()) {
                System.out.println("This is an acyclic graph.");
            } else {
                System.out.println("This is a cyclic graph. The cycles are:");
                ArrayList<ArrayList<Integer>> cycles = graph.getAllCycles();// get and print all the cycles in the graph
                for (ArrayList<Integer> cycle : cycles) {
                    for (int index=0; index<cycle.size();index++){
                        System.out.print(cycle.get(index)+1);
                        if (index<cycle.size()-1){
                            System.out.print(" > ");
                        }
                    }
                    System.out.println();
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Input file not found.");
        }
    }

    public Graph(int verticesNo) {   //constructor to create an empty graph with the given number of vertices
        this.verticesNo = verticesNo;
        adjacencyList = new ArrayList[verticesNo];
        for (int i = 0; i < verticesNo; i++) {
            adjacencyList[i] = new ArrayList<Integer>();
        }
    }

    public void addDirectedEdge(int from, int to) { //add a directed edge from one vertex to another
        adjacencyList[from-1].add(to-1);
    }

    public boolean isAcyclic() {  //check if the graph is acyclic
        boolean[] visited = new boolean[verticesNo];
        boolean[] recStack = new boolean[verticesNo];
        ArrayList<Integer> sinks = new ArrayList<Integer>();  // list of sink vertices
        ArrayList<ArrayList<Integer>> cycles = new ArrayList<ArrayList<Integer>>();  // list of cycles in the graph
        for (int i = 0; i < verticesNo; i++) {
            if (!visited[i]) {
                dfs(i, visited, recStack, sinks, cycles);  // perform DFS on unvisited nodes
            }
        }
        return cycles.isEmpty(); // if there are no cycles, the graph is acyclic

    }

    // perform a depth-first search to find cycles in the graph
    private void dfs(int ver, boolean[] visited, boolean[] recStack, ArrayList<Integer> sinks,
                     ArrayList<ArrayList<Integer>> cycles) {
        visited[ver] = true;  // mark vertice as visited
        recStack[ver] = true;  // add vertice to recursion stack


        for (int neighbor : adjacencyList[ver]) {
            if (!visited[neighbor]) {
                // explore unvisited neighbors recursively
                dfs(neighbor, visited, recStack, sinks, cycles);
            } else if (recStack[neighbor]) {
                // Found a cycle
                ArrayList<Integer> cycle = new ArrayList<Integer>();
                cycle.add(neighbor);
                int u = ver;
                while (u != neighbor) {
                    cycle.add(u);
                    // if u is a sink, set u to the first adjacent vertex (if any)
                    u = sinks.contains(u) ? u : adjacencyList[u].get(0);
                }
                cycle.add(neighbor);
                cycles.add(cycle);
            }
        }

        recStack[ver] = false;  // if u is a sink, set u to the first adjacent vertex (if any)
        if (adjacencyList[ver].isEmpty()) {
            // add sink nodes to the list of sinks
            sinks.add(ver);
        }
    }

    public static Graph fromFile(String fileName) throws FileNotFoundException {
        // create a new File object from the file name
        File inputFile = new File(fileName);
        // create a new Scanner object to read from the file
        Scanner scanner = new Scanner(inputFile);
        int verticesNo = 0;
        // loop through the file to count the number of vertices
        while (scanner.hasNext()) {
            int number = scanner.nextInt();
            if (number>verticesNo){
                verticesNo=number;
            }
        }

        System.out.println("Total number of vertices : " +verticesNo);  // print the number of vertices to the console

        scanner.close();
        // create a new Graph object with the number of vertices
        Graph graph = new Graph(verticesNo);
        scanner = new Scanner(inputFile);// create a new Scanner to read the file again
        // loop through the file to add edges to the graph
        while (scanner.hasNext()) {
            int from = scanner.nextInt();
            int to = scanner.nextInt();
            graph.addDirectedEdge(from, to); // add the directed edge to the graph

        }
        scanner.close();
        return graph;
    }

    public ArrayList<ArrayList<Integer>> getAllCycles() {
        ArrayList<ArrayList<Integer>> cycles = new ArrayList<ArrayList<Integer>>();
        boolean[] visited = new boolean[verticesNo];
        boolean[] recStack = new boolean[verticesNo];
        ArrayList<Integer> sinks = new ArrayList<Integer>();

        for (int i = 0; i < verticesNo; i++) {
            // if the vertex has not been visited yet, run the depth-first search
            if (!visited[i]) {
                dfs(i, visited, recStack, sinks, cycles);
            }
        }
        return cycles;
    }
}