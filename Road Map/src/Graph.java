import java.util.*;
import java.util.Vector;
import java.util.ListIterator;

public class Graph{ // Undirected graph that represents road map, implemented as adjacency matrix.

  
  
  // START Attributes
  
  private Edge[][] adjacencyMatrix; // Adjacency matrix holding the edges of our graph. By default, all entries are 0.
  private Vector<Node> nodeList=new Vector<Node>(0); // Vector in which we store list of nodes.
  
  // END Attributes
  
  
  
  // START Constructors
  
  public Graph(int n){ // Create graph with n nodes (enumerated 0, 1, 2,...n) and no edges.
    adjacencyMatrix=new Edge[n][n]; // Initialize adjacencyMatrix to be of size nxn. All values are by default initialized to 0, so there are no edges yet.
    for (int i=0; i<n; i++){ // For each node...
      nodeList.addElement(new Node(i)); // Create all nodes sequentially and place in nodeList. Mark will by default be initialized to false.
    }  
  }
  
  // END Constructors
  
  
  
  // START Public Methods
  
  public void insertEdge(Node u, Node v, String edgeType) throws GraphException{ // Add an edge of the given type to the graph connecting u and v.
    if (u.getName()>=nodeList.size() && v.getName()>=nodeList.size()){ // If neither u nor v exists...
      System.out.println("GraphException: Cannot insert an edge between nodes "+u.getName()+" and "+v.getName()+" because neither node exists."); // Print node-doesn't-exist error message.
      throw new GraphException(""); // Throw exception.
    } else if(u.getName()>=nodeList.size()){ // If v exists but u does not exist...
      System.out.println("GraphException: Cannot insert an edge between nodes "+u.getName()+" and "+v.getName()+" because node "+u.getName()+" does not exist."); // Print node-doesn't-exist error message.
      throw new GraphException(""); // Throw exception.
    } else if(v.getName()>=nodeList.size()){ // If u exists but v does not exist...
      System.out.println("GraphException: Cannot insert an edge between nodes "+u.getName()+" and "+v.getName()+" because node "+v.getName()+" does not exist."); // Print node-doesn't-exist error message.
      throw new GraphException(""); // Throw exception.
    } else if(adjacencyMatrix[u.getName()][v.getName()]!=null){ // If both u and v exist, but there is already an edge conecting them...
      System.out.println("GraphException: Cannot insert an edge between nodes "+u.getName()+" and "+v.getName()+" because there is already an edge connecting nodes "+u.getName()+" and "+v.getName());
      throw new GraphException(""); // Throw exception.
    } else{ // If both nodes exist, but there is no edge connecting them... (create new edge connecting them)
      adjacencyMatrix[u.getName()][v.getName()]=new Edge(u,v,edgeType); // Set element of adjacencyMatrix corresponding to u,v equal to edgeType.
      adjacencyMatrix[v.getName()][u.getName()]=new Edge(u,v,edgeType); // AdjacencyMatrix must be symmetric, so set element corresponding to v,u equal to edgeType.
    }
  }
  
  public Node getNode(int name) throws GraphException{ // Returns node withspecified name. If no node with this name exists, throws GraphException.
    if (name>=0 && name<nodeList.size()){ // If 'name' corresponds to an existing element of nodeList...
      return nodeList.elementAt(name); // ...return corresponding node.
    } else{
      System.out.println("GraphException: There is no node with name "+name);
      throw new GraphException(""); // Throw exception.
    }
  }
  
  public Iterator<Edge> incidentEdges(Node u) throws GraphException{ // Returns Iterator storing all the edges incident on node u. Returns null if u does not have any edges incident on it.
    if (u!=null && u.getName()<nodeList.size()){ // If node u exists...
      Vector<Edge> incidentEdgesOnU=new Vector<Edge>(); // Create vector whose contents are of type Edge, which will contain all edges incident on u (if any).
      boolean hasIncidentEdge=false; // Flag which will be set to 'true' if we discover that u has at least one incident edge.
      for (int i=0; i<nodeList.size(); i++){ // For each node...
        if (adjacencyMatrix[u.getName()][i]!=null){ // ...if given node is incident on u...
          incidentEdgesOnU.add(adjacencyMatrix[u.getName()][i]); // ...add corresponding edge to incidentEdgesOnU.
          hasIncidentEdge=true; // Signal that there IS at least one edge incident on u.
        }
      }
      if (hasIncidentEdge==false){ // If there were no edges incident on u...
        return null;
      } else{ // If there WAS at least one edge incident on u...
        ListIterator<Edge> graphIterator=incidentEdgesOnU.listIterator(); // Create list iterator whose contents are of type Edge, containing all elements of incidentEdgesOnU.
        return graphIterator; // Return this iterator.
      }
    } else{ // If node u does NOT exist...
      System.out.println("GraphException: Cannot return edges incident on node "+u.getName()+" because node "+u.getName()+" does not exist.");
      throw new GraphException(""); // Throw exception.
    }
  }
  
  public Edge getEdge(Node u, Node v) throws GraphException{ // Returns edge connecting nodes u and v. Throws GraphException if there is no edge between u and v.
    if (u.getName()<adjacencyMatrix[0].length && v.getName()<adjacencyMatrix.length){  // If nodes are in adjacency matrix...
      if (adjacencyMatrix[u.getName()][v.getName()]!=null){ // ...if there is an edge between u and v...
        return adjacencyMatrix[u.getName()][v.getName()]; // ...return this edge.
      }
    } else{ // Otherwise, if there is NOT an edge between u and v...
      System.out.println("GraphException: Cannot return edge between nodes "+u.getName()+" and "+v.getName()+" because they do not have an edge connecting them.");
      throw new GraphException(""); // Throw exception.
    }
    return null;
  }
  
  public boolean areAdjacent(Node u, Node v) throws GraphException{ // Returns 'true' if nodes u and v are adjacent, and 'false' otherwise.
    if (u.getName()<nodeList.size() && u!=null){ // If nodes u exists...
      if (v.getName()<nodeList.size() && v!=null){ // ...if node v exists...
        if (adjacencyMatrix[u.getName()][v.getName()]!=null){ // ...if there is an edge between u and v...
          return true; // ...return this edge.
        } else{ // If both nodes u and v exist, but do not have an edge connecting them...
          return false;
        }
      } else{ // Node v does not exist.
        System.out.println("GraphException: Cannot determine whether nodes "+u.getName()+" and "+v.getName()+" are adjacent because node "+v.getName()+" does not exist.");
        throw new GraphException(""); // Throw exception.
      }
    } else{ // Node u does not exist.
      System.out.println("GraphException: Cannot determine whether nodes "+u.getName()+" and "+v.getName()+" are adjacent because node "+u.getName()+" does not exist.");
      throw new GraphException(""); // Throw exception.
    }
  }
  
  // END Public Methods
  
  
  
  // START Private Methods
  
  
  
  // END Private Methods
    
}