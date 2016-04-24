import java.util.*;
import java.io.File;
import java.io.*;
import java.lang.*;

public class Map{ // Represents road map. Graph will be used to store map and to find path from starting point to destination.

  
  
  // START Attributes
  
  
  private int n; // Number of nodes.
  private int w; // Width: number of (possible) vertical roads per row.
  private int l; // Length: number of (possible) horizontal roads per column.
  private int k; // Maximum number of toll roads that can be traversed in a valid path from start to destination.
  private int start; // Name (index) of start node.
  private int destination; // Name (index) of destination node.
  private Graph graph; // Graph associated with road map.
  private Vector<Node> goodPath=new Vector<Node>(0); // Vector holding nodes of path. Will hold nodes as we are testing different paths, and final contents will correspond to valid path, if one exists.
  boolean flag=false; // Flag that will signal whether we have found a final path yet. Once we find a path that successfully connects s to e with <=k toll roads, flag will be set to true.
  
  // END Attributes
  
  
  
  // START Constructors
  
  public Map(String inputFile) throws MapException{ // Build road map from contents of input file.
    try{
    File input=new File(inputFile); // Create file from inputFile.    
    BufferedReader reader = new BufferedReader(new FileReader(input)); // Convert file reader to buffered reader.
      String horizLine; // String into which we will read odd-numbered lines > 4 (lies containing horizontal roads).
      String vertLine; // String into which we will read even-numbered lines > 4 (lies containing vertical roads).
      reader.readLine(); // Read in first line of file. This contains S, which we will not use.
      w=Integer.parseInt(reader.readLine()); // Read in second line of file. This contains w.
      l=Integer.parseInt(reader.readLine()); // Read in third line of file. This contains l.
      n=w*l; // Compute number of nodes n.
      k=Integer.parseInt(reader.readLine()); // Read in fourth line of file. This contains k.
      
      graph=new Graph(n); // Create graph representing road map.
      
      int rowNumber=0;
      while ((horizLine = reader.readLine()) != null) { // Until we reach last line...(and read in horizontal line)
          rowNumber++;
          for (int i=0; i<(2*w-1); i+=2){ // For each node of horizLine...
            char character=horizLine.charAt(i);
            if (character=='s'){ // Start node
              start=(rowNumber-1)*w+i/2;
            } else if(character=='e'){ // Destination node
              destination=(rowNumber-1)*w+i/2;
            }
          }
          for (int i=1; i<(2*w-2); i+=2){ // For each (possible) edge of horizLine...
            char character=horizLine.charAt(i);
            if (character=='h'){ // Horizontal toll road
              graph.insertEdge(graph.getNode((rowNumber-1)*w+(i-1)/2), graph.getNode((rowNumber-1)*w+(i-1)/2+1), "toll"); // Insert edge in test path here.
            } else if(character=='-'){ // Horizontal free road
              graph.insertEdge(graph.getNode((rowNumber-1)*w+(i-1)/2), graph.getNode((rowNumber-1)*w+(i-1)/2+1), "free"); // Insert edge in test path here.
            }
          }
          try{
            vertLine=reader.readLine(); // Read in vertical line.
            // Read vertLine data into graph.
            for (int i=0; i<(2*w-1); i+=2){ // For each (possible) edge of vertLine...
              char character=vertLine.charAt(i);
              int temp=w*rowNumber+i/2-w;
              int temp2=w*rowNumber+i/2+w;
              if (character=='v'){ // Vertical toll road.
                graph.insertEdge(graph.getNode(w*rowNumber+i/2-w), graph.getNode(w*rowNumber+i/2), "toll"); // Insert edge in test path here.
              } else if(character=='|'){ // Vertical free road.
                graph.insertEdge(graph.getNode(w*rowNumber+i/2-w), graph.getNode(w*rowNumber+i/2), "free"); // Insert edge in test path here.
              }
            }
          } catch (NullPointerException e){
          }
    } 
    reader.close(); // We have finished reading from file. Close buffered reader.
  } catch(StringIndexOutOfBoundsException r){
      System.out.println("StringIndexOutOfBoundsException: Could not create map from input file "+inputFile);
    }
    catch(FileNotFoundException m){
      System.out.println("FileNotFoundException: Could not create map from input file "+inputFile+" because "+inputFile+" does not exist.");
    }
    catch(IOException m){
      System.out.println("IOException: Could not create map from input file "+inputFile);
    }
    catch(GraphException m){
      System.out.println("GraphException: Could not create map from input file "+inputFile);
    }
  }
  
  // END Constructors
  
  
  
  // START Public Methods
  
  public Graph getGraph() throws MapException{ // Return reference to graph representing road map.
    if (graph==null){ // If graph is not defined...
      System.out.println("MapException: Could not retrieve graph because graph is not defined.");
      throw new MapException("");
    }
    else{ // If graph IS defined.
      return graph;
    }
  }
  
  public Iterator findPath(){ // Return iterator containing nodes along path from starting point to destination, if such a path exists. If path does not exist, return null.
    try{
      Node currentNode=graph.getNode(start); // Set first node on which to apply 'search' to start node.
      int sum=0; // Total number of toll roads traversed.
      Vector<Node> pathAttempt=new Vector<Node>(0); // Vector that will hold nodes in path that we are testing.
      pathAttempt.addElement(graph.getNode(start)); // pathAttempt will always contain start node, so insert it at the beginning.
      search(currentNode, sum, pathAttempt); // Search start node to see if there can be a valid path starting from it.
        if (goodPath.size()>0){ // If we find such a successful path...
        ListIterator<Node> finalPath=goodPath.listIterator(); // Put pathAttempt into an iterator.
        return finalPath; // Return this iterator.
      } else{ // Otherwise, if we did NOT find such a successful path...
        return null;
      }
    } catch (GraphException e){
      System.out.println("GraphException: Could not complete execution of finding valid path from start node "+start+" to destination node "+destination);
      return null;
    }
  }
  
  // END Public Methods
  
  
  
  // START Private Methods
  
  private void search(Node currentNode, int sum, Vector<Node> pathAttempt){ // Determine whether a valid path can be found starting from input node 'currentNode'.
    try{
      if (currentNode.getName()==destination){ // If node is destination...
        goodPath=pathAttempt; // Save our path attempt as final 'good' path.
        flag=true; // Signal that we have found a successful path.
        return; // Terminate function.
      }
      if (flag==true){ // If we have found a successful path...
        return; // Break out of function
      }
      Iterator<Edge> itr=graph.incidentEdges(currentNode); // Define iterator containing all edges incident on currentNode.
      while (itr.hasNext()){ // For each edge incident on currentNode...
        Edge currentEdge=itr.next(); // Define 'current' edge.
        int currentSum=sum; // Define 'current' sum, to distinguish from 'sum' used in findPath function.
        Vector<Node> currentPathAttempt=new Vector<Node>(0); // Define 'current' pathAttempt, to distinguish from 'pathAttempt' used in findPath function.
        for (int h=0; h<pathAttempt.size(); h++){ // Set currentPathAttempt equal to pathAttempt.
          currentPathAttempt.add(pathAttempt.elementAt(h));
        }
        Node nextNode; // Define 'next' endPoint as the node at the opposite end of the current edge from currentNode.
        if (currentEdge.firstEndpoint().getName()==currentNode.getName()){ // If first end of current edge is currentNode...
          nextNode=currentEdge.secondEndpoint(); // Set nextNode to secondEndpoint of currentEdge.
        } else{ // If second end of current edge is currentNode...
          nextNode=currentEdge.firstEndpoint(); // Set nextNode to firstEndpoint of currentEdge.
        }
        if (currentEdge.getType()=="toll"){ // If currentEdge is toll road...
          currentSum++; // Increment total number of toll roads traversed.
        }
        if (currentSum<=k){ // If we have not yet traversed too many toll roads...
          
          boolean alreadyInPath=false; // Define variable alreadyInPath with which we will test whether or not a given node that we are considering adding to path, is already part of the earlier segment of the path.
          for (int o=0; o<currentPathAttempt.size(); o++){ // For each node already visited...
            Integer nodeA=currentPathAttempt.elementAt(o).getName(); // Define this node as nodeA.
            Integer nodeB=nextNode.getName(); // Define node that we are testing to see if it is already in path as nodeB.
            if (nodeA.equals(nodeB)){ // If they are equal (ie. node under consideration is already in path)...
              alreadyInPath=true; // Signal that this node is already in our path, and that we should therefore not add it again. Otherwise we would end up in an infinite loop.
            }
          }
          if (alreadyInPath==false){ // If nextNode is NOT already in path...
            currentPathAttempt.addElement(nextNode); // Add nextNode to currentPathAttempt.
            // Copy currentPathAttempt into new vector pathAttempt3 (so that they do not point to the same memory).
            Vector<Node> pathAttempt3=new Vector<Node>(0);
            for (int h=0; h<currentPathAttempt.size(); h++){
              pathAttempt3.add(currentPathAttempt.elementAt(h));
            }
              search(nextNode, currentSum, pathAttempt3); // Since we have determined that it is permissible to traverse to next node, recursively search this node.
          }
        }
      }
      return;
    } catch (GraphException e){
      System.out.println("exception");
    }
  }
  
  // END Private Methods
    
}