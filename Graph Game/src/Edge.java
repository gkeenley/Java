//package A5;
import java.util.*;
import java.util.LinkedList;
import java.util.Iterator;

public class Edge<V>{
  
  
  // START Member Variables
  
  private boolean marker;
  private Vertex<V> endPoint1;
  private Vertex<V> endPoint2;
  
  // END Member Variables
  
  
  // START Constructors
  
  Edge(Vertex<V> u, Vertex<V> v){ // Create new edge with endpoints u and v.
    marker=false; // Initialize marker to false. Will be set to true only when frozen by computer.
    endPoint1=u;
    endPoint2=v;
  }
  
  // END Constructors
  
  
  // START Public Methods
  
  public Vertex<V> getEndPoint1(){ // Return first endpoint.
    return endPoint1;
  }
  
  public Vertex<V> getEndPoint2(){ // Return second endpoint.
    return endPoint2;
  }
  
  public void setMarker(boolean mark){ // Set marker to 'mark'.
    marker=mark;
  }
  
  public boolean getMarker(){ // Return marker.
    return marker;
  }
  
  // END Public Methods
}