import java.io.File;
import java.util.Scanner;

/**
 * The Test class is the main class used to test and grade the CPS420 assignment.
 * <br> DO NOT MODIFY THIS CLASS.
 * @author Sophie Quigley
 */
public class Test {

    /** 
    * Input stream.
    */
    private static Scanner input = null;
    
    /** 
    * Path of directory containing test files.
    */
    public static final String testDirPath = "/misc/cps420/public_html/doc/Now/A/Tests/";
    
    /**
    * Main test program calculates score for program.
    * <br>It first tests the getEulerian method on graphs in test files.
    * <br>Then it tests it on randomly generated undirected simple graphs.
    * @param args the command line arguments
    */

    public static void main(String[] args) {

      int result;
      int score=0;

      // Run test files
      System.out.println("\n=======================  TEST CORRECTNESS  =======================");
  
      score += 6 - testFile("noEulerian", 0, true);
      System.out.println("Score so far: " + score);

      score += testFile("small", 7, true);
      System.out.println("Score so far: " + score);
      
      score += testFile("medium", 5, true);
      System.out.println("Score so far: " + score);
 
      score += 2*testFile("large", 6, true);
      System.out.println("Score so far: " + score);
 
      score += 5*testFile("verylarge", 1, true);
      System.out.println("Score so far: " + score + " out of 35.");

      System.out.println("=======================  TEST EFFICIENCY   =======================");

      score += 4*testFile("huge1", 1, true);
      System.out.println("Score so far: " + score);
 
      score += 4*testFile("huge2", 1, true);
      System.out.println("Score so far: " + score);
 
      score += 4*testFile("huge3", 1, true);
      System.out.println("Score so far: " + score);
 
      score += 4*testFile("huge4", 1, true);
      System.out.println("Score so far: " + score);
 
      score += 4*testFile("huge5", 1, true);
      System.out.println("Score so far: " + score);
 
  
      // Print final score   
      System.out.println("Total score: " + score + " out of 55.");
      
    }

    /**
    * Reads graphs described in a test file and tests the findEuler method on these graphs.
    * @param filename name of test file. The directory containing that file is hard coded in testDirPath
    * @param expected number of graphs in file which have a Eulerian circult
    * @param showcircuit when this is true, the Eulerian circuits found will be printed.
    * @return The number of valid Eulerian circuits found for the graphs in the file.
    */
    public static int testFile(String filename, int expected, boolean showcircuit) {
        String filepath = testDirPath + filename;

        System.out.println("\n=======  Test " + filepath +"  =======");
        try {
            input = new Scanner(new File(filepath));
        } catch (Exception ex) {
            System.out.println(ex);
            System.exit(0);
        }
        
        int found = 0;
        Graph graph = null;
          
        while (input.hasNext())    {
            // Read and print input Graph
            try {
                graph = new Graph(input);
            } catch (InstantiationException ex) {
                System.out.println(ex);
                System.exit(0);
            } catch (Exception ex) {
                System.out.println(ex);
                System.exit(0);
            }
            found += testGraph(graph,showcircuit);
        }
        System.out.println("\nEulerian circuits expected: " + expected + ", found: " + found);
        return found;
    }

    /**
    * Tests the findEuler method for a graph and prints the results.
    * @param graph graph on which the findEuler method will be tested
    * @param showcircuit when this is true, the Eulerian circuit found will be printed.
    * @return 1 if a valid Eulerian circuit has been found and 0 otherwise
    */      
    public static int testGraph(Graph graph, boolean showcircuit) {
        // Print graph
        System.out.println("\nGraph has " + graph.getTotalVertices() 
            + " vertices, and " + graph.getTotalEdges() + " edges.");
                    
        // Try to find a Eulerian circuit
        Walk circuit = Euler.findEuler(graph);
        if (circuit == null) {
            System.out.println("Graph has no Eulerian circuit");
            return 0;
        }
        if (showcircuit)
          System.out.println("The following circuit was found:\n" + circuit);
        if (isValidEulerian(graph,circuit)) {
            System.out.println("Valid Eulerian Circuit");
            return 1;
        }
        else  {
            System.out.println("Invalid Eulerian Circuit");
            return 0;
        }
    }

    /**
    * Verifies whether a walk is a valid Eulerian circuit for a graph.
    * @param graph original graph
    * @param walk potential Eulerian circuit for graph
    * @return true iff walk is a valid Eulerian circuit for graph, and false otherwise.
    */  
    public static boolean isValidEulerian(Graph graph, Walk walk) {
        // First check if the walk is a circuit
        if (!walk.isClosed()) {
            System.out.println("Error: This walk is not a circuit");
            return false;
        }

        // Then check if the circuit has the correct number of edges
        int totalEdges = graph.getTotalEdges();
        int walklength = walk.getLength();
        if (walklength < totalEdges)  {
            System.out.println("Error: Some edges have not been visited");
            return false;
        }
        if (walklength > totalEdges)  {
            System.out.println("Error: Some edges have not been visited more than once");
            return false;
        }

        // Now check whether the circuit encompasses the same number of vertices as the graph
        // By finding the highest vertex index in the circuit
        int maxvertex = 0;
        for (int i=walk.getTotalVertices()-1; i>=0; i--)
            if (walk.getVertex(i) > maxvertex)  maxvertex = walk.getVertex(i);
        if (maxvertex != graph.getTotalVertices()-1) {
            System.out.println("Error: The circuit does not have the same number of vertices as the original graph");
            return false;
        }      
         
        // Finally, check whether the graph formed by this circuit is the same as the original graph:
        // The edges counts between vertices should be identical.
        // Because the circuit and the graph do have the same number of edges at this point,
        // then if these edges are not the identical,
        // there will be n>0 graph edges that are unvisited in the circuit
        // matched by n>0 graph edges that are visited too much in the circuit.
        // This algorithm checks the second condition. 
        graph.resetVisitation();
        int i, v1, v2;
        v1 = walk.getVertex(0);
        for (i=1; i<walklength; i++) {
            v2 = walk.getVertex(i);
            if (graph.getEdgeCount(v1,v2) == 0) {
                System.out.println("Error: The original graph has no edge between " + v1 + " and " + v2);
                graph.resetVisitation();
                return false;
            }
            graph.visitE(v1,v2);
            if (graph.getUnvisitedE(v1,v2) < 0) {
                System.out.println("Error: same edge visited twice between " + v1 + " and "+ v2 );
                graph.resetVisitation();
                return false;
            }
            v1 = v2;
        }
        graph.resetVisitation();
        return true;
    }

}

