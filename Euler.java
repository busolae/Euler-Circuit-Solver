/**
 * The Euler class contains static methods to find Eulerian circuits in Graph objects.
 * This class cannot be instantiated.
 * <BR>THIS CLASS SHOULD BE MODIFIED 
 * @author Sophie Quigley
 * @author PUT YOUR NAMES HERE
 */
public class Euler {

    /**
     * Stops class from being instantiated.
     */
    private Euler() {}

    /**
     * Graph for which an Eulerian circuit is sought.
     */
    private static Graph g = null;

    /**
     * Total number of vertices in the graph.
     */
    private static int totalV;

    /**
     * Total number of edges in the graph.
     */
    private static int totalE;

    /**
     * Eulerian circuit being built.
     */
    private static Walk result = null;

    /**
     * Matrix to track unvisited edges.
     */
    private static int[][] unvisitedE;

    /**
     * Finds an Eulerian circuit in the graph if there is one.
     * <br> DO NOT MODIFY THE METHOD SIGNATURE AND RETURN TYPE OF THIS METHOD
     * @param graph graph for which an Eulerian circuit is sought
     * @return A Eulerian circuit for the graph if there is one, or null otherwise.
     */
    public static Walk findEuler(Graph graph) {
        g = graph;
        totalV = g.getTotalVertices();
        totalE = g.getTotalEdges();

        // Check if the graph has an Eulerian circuit (all vertices must have even degree)
        for (int i = 0; i < totalV; i++) {
            if (g.getDegree(i) % 2 != 0) {
                return null; // Graph is not Eulerian
            }
        }

        // Initialize walk with enough capacity
        result = new Walk(totalE + 1); // Walk should have space for all edges + 1 to close the circuit

        // Initialize adjacency matrix for unvisited edges
        unvisitedE = new int[totalV][totalV];

        int[][] graphEdges = g.getAllEdges();
        for (int i = 0; i < totalV; i++) {
            for (int j = 0; j < totalV; j++) {
                unvisitedE[i][j] = graphEdges[i][j];
            }
        }

        // Start building the Eulerian circuit from vertex 0
        if (buildEuler(0, 0)) {
            return result;
        }

        return null;
    }

    /**
     * Recursively builds the Eulerian circuit.
     * @param vertex current vertex in the circuit
     * @param totalVisited total number of edges visited so far in the graph
     * @return true if a circuit path has been found, false otherwise
     */
    private static boolean buildEuler(int vertex, int totalVisited) {
        // Add current vertex to the walk
        result.addVertex(vertex);

        // If all edges have been visited and we're back at the start, we found a circuit
        if (totalVisited == totalE && vertex == result.getVertex(0)) {
            return true;
        }

        // Explore all possible paths
        for (int nextV = 0; nextV < totalV; nextV++) {
            if (unvisitedE[vertex][nextV] > 0) { // Edge exists and hasn't been fully traversed
                unvisitedE[vertex][nextV]--;
                unvisitedE[nextV][vertex]--;

                if (buildEuler(nextV, totalVisited + 1)) {
                    return true;
                }

                // Backtrack
                unvisitedE[vertex][nextV]++;
                unvisitedE[nextV][vertex]++;
                result.removeLastVertex();
            }
        }

        return false;
    }
}
