//package A5;
import java.util.*;
import java.util.LinkedList;
import java.util.Iterator;

public class FindPath<V>{
  
  // START Member Variables
  
  private boolean frozenPathExists; // Set to true if frozen path is found by markedPathExists().
  private boolean somePathExists; // Set to true if any path is found by givePath().
  LinkedList <Vertex<V>> pathVertexList; // Linked list containing vertices in path found by givePath(). givePath() returns iterator over elements of this list.
  
  // END Member Variables
  
  // START Constructors
  
  public FindPath(){ // Creates new path object with which to invoke findPath methods on graph.
    frozenPathExists=false; // Set to true only if path is found by markedPathExists().
    somePathExists=false; // Set to true only if path is found by anyPath().
    pathVertexList=new LinkedList <Vertex<V>>(); // Initialize list.
  }
  
  // END Constructors
  
  // START Public Methods
  
  public boolean markedPathExists(Graph<V> g, Vertex<V> v, Vertex<V> u){
    
    // Prepare graph for search for new path.
    clearVertexMarkers(g); // Reset to false any vertices that have been marked true during previous calls to markedPathExists().
    
    DFS_frozen(g,v,u); // Execute DFS_frozen(), which will set frozenPathExists to true if frozen path exists between v and u.
    
    // Return results of DFS search for path.
    if (frozenPathExists){ // If DFS_frozen successfully found frozen path between v and u...
      frozenPathExists=false; // Re-set frozenPathExists to false for next time.
      return true;
    } else{ // If DFS_frozen did NOT successfully find frozen path between v and u...
      return false;
    }
  }
  
  public Iterator<Vertex<V>> givePath(Graph<V> g, Vertex<V> v, Vertex<V> u){
    
    // Prepare graph for search for new path.
    pathVertexList.clear(); // Clear pathVertexList of any elements placed in it during previous calls to givePath().
    clearVertexMarkers(g); // Reset to false any vertices that have been marked true during previous calls to givePath().
    
    DFS_anyPath(g,v,u); // Execute DFS_anyPath, which will set somePathExists to true if some path exists between v and u.
    
    // Return results of DFS search for path.
    if (somePathExists){ // If DFS_anyPath successfully found path between v and u...
      somePathExists=false; // Re-set somePathExists to false for next time.
      return pathVertexList.descendingIterator(); // Return iterator over pathVertexList (in reverse order, since vertices were added to pathVertexList starting from endVertex u).
    } else{
      return null;
    }
  }
  
  // END Public Methods
  
  // START Private Methods
  
  private void DFS_frozen(Graph<V> g, Vertex<V> v, Vertex<V> u){ // Recursive DFS search for frozen path in g from v to u.
    v.setMarker(true); // Mark v (start of path) as VISITED.
    Iterator<Edge<V>> graphEdges=v.incidentEdges(); // Get iterator over all edges in graph.
    while (graphEdges.hasNext()){ // While there are still edges left to consider...
      Edge<V> testEdge=graphEdges.next(); // ...consider next edge.
      Vertex<V> opposite=g.giveOpposite(v, testEdge); // Get opposite vertex.
      if (testEdge.getMarker()==true){ // If testEdge is un-frozen...
        if (opposite==u){ // ...if testEdge successfully completes the path...
          frozenPathExists=true; // ...indicate that computer has successfully frozen a path.
          break;
        }
        if (opposite.getMarker()==false){ // ...if testEdge does not successfully complete path, but opposite vertex is still UNVISITED...
          DFS_frozen(g,opposite,u); // Recursively perform DFS search from opposite to u.
        }
      }
    }
  }
  
  private void DFS_anyPath(Graph<V> g, Vertex<V> v, Vertex<V> u){ // Recursive DFS search for any path in g from v to u.
    v.setMarker(true); // Mark v (start of path) as VISITED.
    Iterator<Edge<V>> graphEdges=v.incidentEdges(); // Get iterator over all edges in graph.
    while ((graphEdges.hasNext())&&(!somePathExists)){ // While there are still edges left to consider...
      Edge<V> testEdge=graphEdges.next(); // ...consider next edge.
      Vertex<V> opposite=g.giveOpposite(v, testEdge); // Get opposite vertex.
      if (v==u){ // If current vertex under consideration IS final vertex in path...
        somePathExists=true; // ...indicate that we have successfully found a path from v to u.
        break;
      }
      if (opposite.getMarker()==false){ // If current vertex under consideration is NOT final vertex in path, but next vertex is still UNVISITED...
        DFS_anyPath(g,opposite,u); // Recursively perform DFS search from opposite to u.
      }
    }
    if (somePathExists){ // If current vertex under consideration is part of final successful path...
      pathVertexList.add(v); // ...add this vertex to pathVertexList, over which anyPath() will create an iterator.
    }
  }
  
  private void clearVertexMarkers(Graph<V> g){ // Set markers of all vertices in graph to false.
    Iterator<Vertex<V>> graphVertices=g.vertices(); // Get iterator over all vertices in graph.
    while (graphVertices.hasNext()){ // While there are still vertices left to consider...
      Vertex<V> testVertex=graphVertices.next(); // ...consider next vertex.
      testVertex.setMarker(false); // Set vertex marker to false.
    }
  }
  
  // END Private Methods
}