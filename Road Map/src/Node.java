import java.util.*;

public class Node{ // Defines node in graph, which can be either the intersection of two roads or the ends of one road.
  
  
  // START Attributes
  
  private int nodeName; // Index of node.
  private boolean nodeMark; // 'true' if node has been visited, 'false' otherwise.
  
  // END Attributes  
  
  
  
  // START Constructors
  
  public Node(int name){ // Creates unmarked node with name 'name'.
    nodeName=name; // Initialize nodeName to be input 'name'.
    nodeMark=false; // Initialize nodeMark to false because we have not visited it yet.
  }
  
  // END Constructors
  
  
  
  // START Methods (all public)
  
  public void setMark(boolean mark){ // Sets the mark of given node.
    nodeMark=mark;
  }
  
  public boolean getMark(){ // Returns the mark of given node.
    return nodeMark; 
  }
  
  public int getName(){ // Returns the name of given node.
    return nodeName;
  }
  
  // END Methods (all public)
  

  
}