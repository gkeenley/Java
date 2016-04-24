//package A5;
import java.util.*;
import java.util.LinkedList;
import java.util.Iterator;

public class Graph<V> implements GraphInterface<V>{
  
  //
  
  private LinkedList<Vertex<V>> nodes; // Linked list containing vertices.
  private int numVertices; // Current number of vertices in graph.
  private int numEdges; // Current number of edges in graph.
  
  //
  
  // START Public Methods
  
  public Graph(){
    nodes=new LinkedList<Vertex<V>>(); // Create new linked list which will contain vertices.
    numVertices=0; // Initialize numVertices to 0 because none have been added yet.
    numEdges=0; // Initialize numEdges to 0 because none have been added yet.
  }
  
  public Vertex<V> insertVertex(V o){
    Vertex<V> v=new Vertex<V>(o); // Create new vertex containing object o.
    nodes.add(v); // Add new vertex to linked list of vertices.
    numVertices++; // Increment numVertices because we have added a vertex.
    return v; // Return added vertex.
  }
  
  public int getNumVertices(){
    return numVertices;
  }
  
  public int getNumEdges(){
    return numEdges;
  }
  
  public Edge<V> findEdge(Vertex<V> u, Vertex<V> v) throws GraphException{
    // Throw exception if either u of v is null.
    if (u==null||v==null){
      throw new GraphException("Cannot find edge between vertices because at least one is null.");
    }
    
    // Return null if u and v are not adjacent.
    if (!u.isAdjacent(v)){
      return null;
    }
    
    // Otherwise, u and v ARE adjacent, so we find and return corresponding edge.
    Iterator<Edge<V>> edgeIterator=u.incidentEdges(); // Iterator over adjacency list of u (could also have chosen v).
    while (edgeIterator.hasNext()){ // While there are still edges in adjacency list to consider...
      Edge<V> testEdge=edgeIterator.next(); // Consider next edge.
      Vertex<V> testEndPoint1=testEdge.getEndPoint1(); // First endpoint of testEdge.
      Vertex<V> testEndPoint2=testEdge.getEndPoint2(); // Second endpoint of testEdge.
      if (((testEndPoint1==u)&&(testEndPoint2==v))||((testEndPoint1==v)&&(testEndPoint2==u))){ // If endpoints of testEdge are (u,v) or (v,u)... (both are equivalent since graph in undirected)
        return testEdge;
      }
    }
    return null;
  }
  
  public boolean areAdjacent(Vertex <V> v, Vertex<V> u) throws GraphException{
    // Throw exception if either u of v is null.
    if (u==null||v==null){
      throw new GraphException("Cannot determine if vertices are adjacent because at least one is null.");
    }
    
    // Otherwise, neither u nor v is null, so we test for adjacency.
    if (u.isAdjacent(v)){ // If u and v are adjacent...
      return true;
    } else{ // If u and v are NOT adjacent...
      return false;
    }
  }
  
  public Edge<V> insertEdge(Vertex<V> v, Vertex<V> u) throws GraphException{
    // Throw exception if u and v are same vertex.
    if (v==u){
      throw new GraphException("Cannot insert edge connecting vertices because they are the same vertex.");
    }
    
    // Throw exception if an edge connecting u and v already exists.
    if (v.isAdjacent(u)){
      throw new GraphException("Cannot insert edge connecting vertices because this edge already exists.");
    }
    
    // Otherwise, insert edge connecting v and u.
    Edge<V> newEdge=new Edge<V>(v, u); // Create new edge 'newEdge' with endpoints v and u.
    v.addAdjacent(newEdge); // Add newEdge to adjacency list of v.
    u.addAdjacent(newEdge); // Add newEdge to adjacency list of u.
    numEdges++; // Increment numEdges because we have added an edge.
    return newEdge;
  }
  
  public void deleteEdge(Edge<V> e) throws GraphException{
    int counter=0; // Incremented once if e is located in and removed from adjacency list of u, and once for v. If counter!=2 afterwards, throw exception. 
    
    // Throw exception if e is null.
    if (e==null){
      throw new GraphException("Cannot delete edge because edge does not exist.");
    }
    
    // Otherwise, search adjacency lists of endpoints for e and delete from both lists.
    
    // Get endpoints of e.
    Vertex<V> u=e.getEndPoint1(); // First endpoint.
    Vertex<V> v=e.getEndPoint2(); // Second endpoint.
    
    // Create new iterators for adjacency lists of u and v, in order to delete e from each.
    Iterator<Edge<V>> edgeIterator_u=u.incidentEdges();
    Iterator<Edge<V>> edgeIterator_v=v.incidentEdges();
    
    // Delete e from adjacency list of u.
    while (edgeIterator_u.hasNext()){ // While there are still edges in adjacency list to consider...
      Edge<V> testEdge_u=edgeIterator_u.next(); // Consider next edge.
      // Get endpoints of testEdge_u.
      Vertex<V> testEdge_u_endPoint1=testEdge_u.getEndPoint1(); // First endpoint.
      Vertex<V> testEdge_u_endPoint2=testEdge_u.getEndPoint2(); // Second endpoint.
      if(((testEdge_u_endPoint1==u)&&(testEdge_u_endPoint2==v))||((testEdge_u_endPoint1==v)&&(testEdge_u_endPoint2==u))){ // If testEdge_u and e have same endpoints, ie. if testEdge_u is the same edge as e...
        edgeIterator_u.remove(); // Remove testEdge_u from adjacency list of u.
        counter++; // Increment counter to show that deletion of e from adjacency list of u was successful.
      }
    }
    
    // Delete e from adjacency list of v.
    while (edgeIterator_v.hasNext()){ // While there are still edges in adjacency list to consider...
      Edge<V> testEdge_v=edgeIterator_v.next(); // Consider next edge.
      // Get endpoints of testEdge_v.
      Vertex<V> testEdge_v_endPoint1=testEdge_v.getEndPoint1(); // First endpoint.
      Vertex<V> testEdge_v_endPoint2=testEdge_v.getEndPoint2(); // Second endpoint.
      if(((testEdge_v_endPoint1==v)&&(testEdge_v_endPoint2==u))||((testEdge_v_endPoint1==u)&&(testEdge_v_endPoint2==v))){ // If testEdge_u and e have same endpoints, ie. if testEdge_u is the same edge as e...
        edgeIterator_v.remove(); // Remove testEdge_u from adjacency list of u.
        counter++; // Increment counter to show that deletion of e from adjacency list of v was successful.
      }
    }
    
    // If counter!=2, e was not located in and removed from adjacency lists of both u and v. Throw exception.
    if (counter!=2){
      throw new GraphException("Edge was not found in adjacency list of at least one of u and v.");
    }
    numEdges--; // Decrement numEdges because we have removed an edge.
  }
  
  public Vertex<V> giveOpposite(Vertex<V> v, Edge<V> e){
    Vertex<V> endPoint1=e.getEndPoint1(); // Get first endpoint of e.
    Vertex<V> endPoint2=e.getEndPoint2(); // Get second endpoint of e.
    if (endPoint1==v){ // If v is first endpoint... 
      return endPoint2; // ...then endpoint opposite v is endPoint2.
    } else{ // Otherwise, v must be second endpoint.
      return endPoint1; // ...and endpoint opposite v is endPoint1.
    }
  }
  
  public Iterator<Vertex<V>> vertices(){
    return nodes.iterator();
  }
  
  public Iterator<Edge<V>> edges(){
    
    LinkedList <Edge<V>> L=new LinkedList<Edge<V>>(); // Create empty linked list L in which to store edges.
    
    // Set markers for all nodes to false.
    clearVertexMarkers();
    
    Iterator<Vertex<V>> graphVertices=vertices();
    while (graphVertices.hasNext()){ // While there are still vertices left to consider...
      Vertex<V> testVertex=graphVertices.next(); // ...consider next vertex.
      Iterator<Edge<V>> graphEdges=testVertex.incidentEdges();
      while (graphEdges.hasNext()){ // While there are still edges left in adjacency list of testVertex to consider...
        Edge<V> testEdge=graphEdges.next(); // ...consider next edge in adjacency list.
        // Get endpoints of testEdge.
        Vertex<V> u=testEdge.getEndPoint1(); // First endpoint.
        Vertex<V> v=testEdge.getEndPoint2(); // Second endpoint.
        if (u.getMarker()==false&&v.getMarker()==false){ // If markers of both endpoints are false, we have not added testEdge to L.
          L.add(testEdge); // Add testEdge to L.
        }
      }
      testVertex.setMarker(true); // Mark testVertex true so that edges incident on it will not be double-counted.
    }
    return L.iterator();
  }
  
  // END Public Methods
  
  // START Private Methods
  
  private void clearVertexMarkers(){ // Set markers for all nodes to false.
    Iterator<Vertex<V>> graphVertices=vertices();
    while (graphVertices.hasNext()){ // While there are still vertices left to consider...
      Vertex<V> testVertex=graphVertices.next(); // ...consider next vertex.
      testVertex.setMarker(false);
    }
  }
  
  // END Private Methods
}