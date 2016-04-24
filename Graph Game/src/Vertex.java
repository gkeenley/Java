//package A5;
import java.util.*;
import java.util.LinkedList;
import java.util.Iterator;

public class Vertex<V>{
  
  //
  
  private LinkedList <Edge<V>> adjacencyList; // Linked adjacency list of edge objects.
  private V object; // Object stored at vertex.
  private boolean marker; // Marker to indicate that we have inserted all edges that include the vertex as an endpoint into the edge linked list, when creating the edge iterator.
  
  //
  
  // START Constructors
  
  public Vertex(V objectIn){
    object=objectIn;
    adjacencyList=new LinkedList<Edge<V>>(); // Adjacency list is a linked list.
    marker=false; // Initialize marker to false
  }
  
  // END Constructors
  
  // START Public Methods
  
  public V getObject(){ // Return object stored at vertex.
    return object;
  }
  
  public void addAdjacent(Edge<V> e) throws GraphException{ // Add edge e to adjacency list, if not already present.
    
    Iterator<Edge<V>> edgeIterator=incidentEdges(); // Creates new iterator for adjacency list.
    // Get endpoints of e.
    Vertex<V> u_e=e.getEndPoint1(); // First endpoint.
    Vertex<V> v_e=e.getEndPoint2(); // Second endpoint.
    
    // Test if e is already in adjacency list.
    while (edgeIterator.hasNext()){ // While there are still edges in adjacency list to consider...
      Edge<V> testEdge=edgeIterator.next(); // Consider next edge.
      // Get endpoints of testEdge.
      Vertex<V> u_testEdge=testEdge.getEndPoint1(); // First endpoint.
      Vertex<V> v_testEdge=testEdge.getEndPoint2(); // Second endpoint.
      if ((u_e==u_testEdge&&v_e==v_testEdge)||(u_e==v_testEdge&&v_e==u_testEdge)){ // If testEdge and e have same endpoints, ie. if testEdge is the same edge as e...
        throw new GraphException("GraphException: Edge already exists in adjacency list of vertex, so it cannot be added."); // Throw exception.
      }
    }
    
    adjacencyList.add(e); // e is NOT already in adjacency list. Therefore, append e to end of adjacency list.
  }
  
  public Iterator<Edge<V>> incidentEdges(){ // Returns iterator over the edge objects in adjacency list.
    return adjacencyList.iterator();
  }
  
  public boolean isAdjacent(Vertex<V> v){
    Iterator<Edge<V>> edgeIterator=incidentEdges(); // Creates new iterator for adjacency list.
    while (edgeIterator.hasNext()){ // While iterator still contains edges to consider...
      Edge<V> testEdge=edgeIterator.next(); // Consider next edge.
      if (testEdge.getEndPoint1()==v||testEdge.getEndPoint2()==v){ // If either endpoint is v (since u could be first or second endpoint)...
        return true;
      }
    }
    return false;
  }
  
  public void removeAdjacent(Edge<V> e) throws GraphException{ // Remove edge e from adjacency list if it is present.    
    Iterator<Edge<V>> edgeIterator=incidentEdges(); // Creates new iterator for adjacency list.
    boolean flag=false; // Flag which will be set to 'true' if e is found in adjacency list. If not, exception will be thrown.
    // Get endpoints of e.
    Vertex<V> u_e=e.getEndPoint1(); // First endpoint.
    Vertex<V> v_e=e.getEndPoint2(); // Second endpoint.
    
    // Test if e is in adjacency, and if so, remove it.
    while (edgeIterator.hasNext()){ // While there are still edges in adjacency list to consider...
      Edge<V> testEdge=edgeIterator.next(); // Consider next edge.
      // Get endpoints of testEdge.
      Vertex<V> u_testEdge=e.getEndPoint1(); // First endpoint.
      Vertex<V> v_testEdge=e.getEndPoint2(); // Second endpoint.
      if ((u_e==u_testEdge&&v_e==v_testEdge)||(u_e==v_testEdge&&v_e==u_testEdge)){ // If testEdge and e have same endpoints, ie. if testEdge is the same edge as e...
        edgeIterator.remove(); // Remove e from adjacency list.
        flag=true; // Signal that e was present in adjacency list.
      }
    }
    if (flag==false){ // If e was NOT present in adjacency list...
      throw new GraphException("GraphException: Edge does not exist in adjacency list of vertex, so it cannot be removed."); // Throw exception.
    }
  }
  
  public void setMarker(boolean mark){
    marker=mark;
  }
  
  public boolean getMarker(){
    return marker;
  }
  
  // END Public Methods
}