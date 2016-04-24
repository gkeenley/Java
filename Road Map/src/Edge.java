import java.util.*;

public class Edge{ // Defines edge which connects pairs of nodes in graph.

  
  
  // START Attributes
  
  Node edgeU; // First node of a pair of nodes connected by an edge. Designated 'first' because in any operation on a given edge, we will refer to it first.
  Node edgeV; // Second node of a pair of nodes connected by an edge. Designated 'second' because in any operation on a given edge, we will refer to it second.
  String edgeType; // Contains string 'Free' or 'toll'.
  String edgeLabel; // String label.
  
  // END Attributes
  
  
  
  // START Constructors
  
  public Edge(Node u, Node v, String type){  // Creates edge with associated first and second nodes u and v, respectively, as well as string type.
    edgeU=u; // First node.
    edgeV=v; // Second node.
    edgeType=type; // Edge type.
  }
  
  // END Constructors
  
  
  
  // START Methods (all public)
  
  public Node firstEndpoint(){ // Returns first node.
    return edgeU;
  }
  
  public Node secondEndpoint(){ // Returns second node.
    return edgeV;
  }
  
  public String getType(){ // Returns edge type.
    return edgeType;
  }
  
  public void setLabel(String label){ // Sets edge label.
    edgeLabel=label;
  }
  
  public String getLabel(){ // Returns edge label.
    return edgeLabel;
  }
  
  // END Methods (all public)

  
}