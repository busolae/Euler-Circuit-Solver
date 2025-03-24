/**
 * Walk objects can be used to build walks from Graph objects.
 * <br>A Walk is simply a list of vertices in the order in which they 
 occur in the walk.  The edges are not listed.
 * <br>If the walk is closed, the first and last vertex will be the same,
 * and will end up being counted twice.
 * <br>Note that this class does not verify the validity of the walk,
 * i.e. it does not verify whether there are valid edges between two 
 * adjacent vertices in the walk.
 * <BR>DO NOT MODIFY THIS CLASS
 * @author Sophie Quigley
 *
 */
public class Walk {
    /**
     * Marker for no vertex.
     */
    public static final int NOVERTEX = -1;

    /**
     * Maximum number of vertices in the walk.
     */
    int maxV = 0;

    /**
     * Actual number of vertices in walk.
     */
    int totalV = 0;
    
    /**
     * The vertices are listed in their order of traversal.
     * <br>Edges are not stored in this representation of walks.
     */
    int[] vertices = null;
   
    /**
     * Creates a new empty Walk with room for a specified maximum number of vertices.
     * @param maxVertices maximum number of vertices in walk
     */
    public Walk(int maxVertices) {
        maxV = (maxVertices<0) ? 0: maxVertices;
        vertices = new int[maxV];
        clear();
    }

   /**
    * Returns a String representation of the walk:
    * a list of the vertices separated by blanks.
    * @return The list of vertices in the walk separated by blanks
    * 
    */
    @Override
    public String toString() {
        String result = "";
        if (totalV == 0)  return result;
        result += vertices[0];
        for (int i=1; i<totalV; i++) {
            result += " ";
            result += vertices[i];
       }
       return result;
    }

    /**
     * Clears the walk of all vertices.
     */
    public void clear() {
        for (int i=0; i<totalV; i++)    
            vertices[i] = NOVERTEX;
        totalV = 0;        
    }
    
    /**
     * Decides whether walk is empty (with no vertices).
     * @return True iff walk is empty
     */
    public boolean isEmpty() {
        return (totalV == 0);   
    } 
    
    /**
     * Decides whether walk is trivial (with a single vertex).
     * @return True iff walk is trivial
     */
    public boolean isTrivial() {
        return (totalV == 1);   
    } 
    
    /**
     * Decides whether walk is closed (with same first and last vertex).
     * @return True iff the walk is closed
     */
    public boolean isClosed() {
        return (totalV == 0) ? false : (vertices[0] == vertices[totalV-1]); 
    }

   /**
    * Gets the number of vertices in the walk.
    * <br>Note that in closed walks the starting vertex will be counted twice.
    * @return The number of vertices in the walk
    *
    */  
    public int getTotalVertices() {
        return totalV;
    }

   /**
    * Gets the length of the walk, i.e. the number of edges in the walk.
    * <br>Note: empty and trivial walks both have a length of 0
    * @return The number of edges in the walk
    *
    */  
    public int getLength() {
        return (totalV == 0) ? 0 : totalV - 1;
    }
    
    /**
     * Gets a specific vertex in the walk.
     * @param n The position of the vertex to be returned, starting at 0.
     * @return the vertex at position n in the walk
     * or Walk.NOVERTEX if the walk doesn't have n vertices.
     */
    public int getVertex(int n) {
        return (n>=0 && n<totalV) ? vertices[n]: NOVERTEX;      
    }
    
   /** 
    * Adds another vertex to the end of the walk if possible.
    * @param vertex Vertex to be added to walk
    * @return True iff the vertex could be added, i.e maximum capacity was not exceeded
    * 
    */  
    public boolean addVertex(int vertex) {
        if (totalV == maxV)
            return false;
        vertices[totalV++] = vertex;
        return true;
    }
    
   /** 
    * Removes the last vertex added to the walk if possible.
    * This is used for backtracking.
    * @return True iff the last vertex could be removed, i.e walk was not empty
    * 
    */  
    public boolean removeLastVertex() {
        if (totalV == 0)
            return false;
        vertices[--totalV] = NOVERTEX;
        return true;
    }
    
}
