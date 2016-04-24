//package A5;


import java.util.Iterator;


public interface GraphInterface<V> {
    // returns number of vertices in the graph
    public int getNumVertices();
    
    // returns number of edges in the graph
    public int getNumEdges();
    
    // inserts a new vertex storing object O
    public Vertex<V> insertVertex( V o );
    
    // returns the edge object with specified endpoints 
    public Edge<V> findEdge(Vertex<V> u, Vertex<V> v) throws GraphException;
    
    // true if u and v are adjacent, false otherwise
    public boolean areAdjacent(Vertex<V> v, Vertex<V> u) throws GraphException;
    
    // inserts an edge between u and v. Throws exception if edge is already there
    // or u and v are the same vertx
    public Edge<V> insertEdge(Vertex<V> v, Vertex<V> u)  throws GraphException;
    
    // deletes edge e
    public void deleteEdge(Edge<V> e)  throws GraphException;
    
    // the endpoint of e other than v 
    public Vertex<V> giveOpposite(Vertex<V> v, Edge<V> e);
  
    // iterator over vertice objects
    public Iterator<Vertex<V>> vertices(); 
    
    // iterator over edge objects
    public Iterator<Edge<V>> edges(); 
    
    
}