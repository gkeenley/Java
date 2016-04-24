//package A5;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Iterator;
import java.util.Vector;

public class GraphFun implements ActionListener {
   // next 4 constants specify the "size" of the game
   // to change the number of vertices, set constants NUM_HORIZONTAL
   // and NUM_VERTICAL to new numbers. DO NOT CHANGE NUM_VERTICES
   // I also don't recommend to change NUM_EDGES
   private static final int NUM_HORIZONTAL = 3;
   private static final int NUM_VERTICAL  = 3;
   private static final int NUM_VERTICES = NUM_HORIZONTAL*NUM_VERTICAL;
   private static final int NUM_EDGES = NUM_VERTICES*5;
    
   // next 9 constants are for GUI
   private static final int WINDOW_WIDTH = 600;  //width of widow game
   private static final int WINDOW_HEIGHT = 600; //height of window game
   private final static int CANVAS_WIDTH = 500;  // width of graph picture in game
   private final static int CANVAS_HEIGHT = 500; // height of graph picture in game
   private static final int TEXT_WIDTH    = 5;  // width of text window
   private static final int OFFSET        = 6;   //for vertex display

   private static final FlowLayout LAYOUT_STYLE = new FlowLayout();
   
   private final static Color SKY_BLUE = new Color(217,217,255);
   private final static Color EDGE_COLOR = new Color(117,247,55);
   
   // commonly used strings
   private final static String Enter = new String("Enter");
   private final static String New = new String("New");
   private static final String EMPTY_STRING = "";   
    
   // you can use these constants to denote outcome of the next move
   // but you don't have to use these constants
   private final static int HUMAN_WON    = 1;
   private final static int COMPUTER_WON = 2;
   private final static int NOONE_WON    = 3;
  
   
   //instance variables for GUI
   private JFrame window             = new JFrame("GraphFun");
   private JLabel nextEdgeTag        = new JLabel("Next Edge");
   private JTextField firstText      = new JTextField(TEXT_WIDTH);
   private JTextField secondText     = new JTextField(TEXT_WIDTH);
   private JLabel warning           = new JLabel(EMPTY_STRING);

   private JButton enterButton = new JButton(Enter);
   private JButton startButton = new JButton(New);
   
   private Canvas graphPad = new Canvas();

   /////////////////////////////////////////////////////////////////////////////////////
   ///////// The next 4 private variables you have to use in your implementation //////
   // instance variables for graph
   private Graph<Coordinate>  graph;          // the graph representation of the game
   private Vector<Vertex<Coordinate>>  vertexLookup; // lookup from integer vertex names to Vertex<Coordinate>
   private int startVertex;                          // special start vertex
   private int endVertex;                            // special end vertex
   /////////////////////////////////////////////////////////////////////////////////////
   
   // the constructure configures GUI
   public GraphFun(){
        //first configure GUI
       window.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
       window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       window.setBackground(SKY_BLUE);
       
       graphPad.setSize(CANVAS_WIDTH,CANVAS_HEIGHT);
       graphPad.setBackground(Color.WHITE);
       enterButton.addActionListener(this);
       startButton.addActionListener(this);
       
       firstText.setEditable(false);   // these fields can't  be edited until
       secondText.setEditable(false);  //  user presses "new" button
       
       // now arrange components in GUI
       window.setLayout(LAYOUT_STYLE);
       window.add(startButton);
       window.add(nextEdgeTag);
       window.add(firstText);
       window.add(secondText);
       window.add(enterButton);
       window.add(warning);
       window.add(graphPad);      // graph vertices and edges displayed on this canvas
              
       window.setVisible(true);  // display GUI
   }
   
   // "new" and "enter" button action event handler 
   public void actionPerformed(ActionEvent e) {
       // get user's response
       if (Enter.compareTo(e.getActionCommand()) == 0 ) // "enter" button pressed
       {
           try{
                Integer vertex1 = new Integer(firstText.getText());
                Integer vertex2 = new Integer(secondText.getText());
                if (vertex1 < 0 || vertex1 >= NUM_VERTICES || vertex2 < 0 || vertex2 >= NUM_VERTICES )
                    warning.setText("INVALID RANGE");
                else {
                    warning.setText(EMPTY_STRING);
                    // now process user move and generate next computer move
                    nextMove(vertex1.intValue(),vertex2.intValue()); 
                }
           }
           catch(NumberFormatException exept){warning.setText("INVALID INPUT");}
    
       }
       if (New.compareTo(e.getActionCommand()) == 0 ){ // "new" button pressed
            firstText.setEditable(true);
            secondText.setEditable(true);   
            clearBoard();
            startNewGame();
       }       
   }
   
   // this method clears the graph canvas
   private void  clearBoard(){
        int w = graphPad.getWidth();
        int h = graphPad.getHeight();
        Graphics g = graphPad.getGraphics();
        
        g.setColor(Color.WHITE);
        g.fillRect(0,0,w,h);
        
   }
 
   // selects 2 new endvertices for the new game
     private void setEndVertices()
     {
        java.util.Random rand = new java.util.Random();
  
        startVertex = rand.nextInt(NUM_VERTICES);     
        endVertex = rand.nextInt(NUM_VERTICES);     

        int iy = endVertex/NUM_HORIZONTAL;
        int ix = endVertex-iy*NUM_HORIZONTAL;

        int iy1 = startVertex/NUM_HORIZONTAL;
        int ix1 = startVertex-iy1*NUM_HORIZONTAL;
              
        // checks that the 2 endvertices are not too close to each other
       while (Math.abs(iy-iy1) + Math.abs(ix-ix1) < NUM_HORIZONTAL )
       {
    startVertex = rand.nextInt(NUM_VERTICES);     
    endVertex = rand.nextInt(NUM_VERTICES);

    iy = endVertex/NUM_HORIZONTAL;
    ix = endVertex-iy*NUM_HORIZONTAL;

    iy1 = startVertex/NUM_HORIZONTAL;
    ix1 = startVertex-iy1*NUM_HORIZONTAL;
       }
     }
    
    // generates new vertex for the game
    private Coordinate newVertex(int i) {   
       int w = graphPad.getWidth();
       int h = graphPad.getHeight();
       
       int iy = i/NUM_HORIZONTAL;
       int ix = i-iy*NUM_HORIZONTAL;
       int intervalx = CANVAS_WIDTH/(NUM_HORIZONTAL+1);
       int intervaly = CANVAS_HEIGHT/(NUM_VERTICAL+1);
       
       java.util.Random rand = new java.util.Random();
       int deltaX = rand.nextInt((intervalx*2)/3)-intervalx/3;
       int deltaY = rand.nextInt((intervaly*2)/3)-intervaly/3;
        
       int x = intervalx+ix*intervalx+deltaX;
       int y = intervaly+iy*intervaly+deltaY;
               
       return (new Coordinate(x,y,i));
   }
   
    // prints vertex with name i at Coordinate coord
    private void paintVertex( Coordinate coord ) {
       
       int w = graphPad.getWidth();
       int h = graphPad.getHeight();
       
       int x = coord.getX();
       int y = coord.getY();
       int i = coord.getIndex();
        
        Graphics g = graphPad.getGraphics();
        
        // endvertices are in different color from the other vertices
 if ( i == startVertex || i == endVertex )
     g.setColor(Color.RED);
        else g.setColor(Color.BLACK);
        
 g.fillOval(x-OFFSET/2,y-OFFSET/2,OFFSET,OFFSET);
 Integer name = new Integer(i);
        g.drawString(new String(name.toString()),x-OFFSET/2,y-OFFSET/2);
   }
 
   // chooses new vertices for the game 
   private Coordinate [] getVertices( ){
        
        Coordinate [] toReturn = new Coordinate[NUM_VERTICES];
        for ( int i = 0; i < NUM_VERTICES; i++ )
            toReturn[i] = newVertex(i);
        return toReturn;
   }   
   // chooses the possible set of edges for the game
   // NOTE that it may choose a repeating edge for the game
   private Pair [] getEdges(Coordinate [] vertices){
       Pair [] toReturn = new Pair[NUM_EDGES];
       java.util.Random rand = new java.util.Random();
       
       int w = graphPad.getWidth();
       int h = graphPad.getHeight();
               
       for (int i = 0; i < NUM_EDGES; i++){
            int n1 = rand.nextInt(NUM_VERTICES);
            int n2 = rand.nextInt(NUM_VERTICES);           
                        
            int iy = n1/NUM_HORIZONTAL;
            int ix = n1-iy*NUM_HORIZONTAL;
            boolean done = false; 
            int temp = 0;

            int intervalx = CANVAS_WIDTH/(NUM_HORIZONTAL+1);
            int intervaly = CANVAS_HEIGHT/(NUM_VERTICAL+1);

            // try to get edges that are not too long, so that display
            // of the graph looks reasonable
            while( ! done ){
                n2 = rand.nextInt(NUM_VERTICES);
                
                int iy2 = n2/NUM_HORIZONTAL;
                int ix2 = n2-iy2*NUM_HORIZONTAL;
                    
              //  if ( Math.abs(ix-ix2) <= 1 &&Math.abs(iy-iy2) <= 2 && n1 != n2)
                if ( Math.abs(vertices[n1].getX()-vertices[n2].getX()) <= 2*intervalx &&
                     Math.abs(vertices[n1].getY()-vertices[n2].getY()) <= 2*intervaly && n1 != n2 )
                     done = true;
                temp++;
                if (temp > 20) done = true;
            }
            
            toReturn[i] = new Pair(n1,n2);
        }

        return(toReturn);
   }

   // draws current board in the game window. Takes the iterator over vertices
   // and iterator over edges
   private void drawBoard(Iterator<Vertex<Coordinate>> vertexIt, Iterator<Edge<Coordinate>> edgeIt)
   {
       clearBoard();
       int w = graphPad.getWidth();
       int h = graphPad.getHeight();
       
       Graphics g = graphPad.getGraphics();
       
       // first draw edges
       while (edgeIt.hasNext() ){
            Edge<Coordinate> e = edgeIt.next();
            if (e.getMarker()) g.setColor(Color.RED);
            else g.setColor(EDGE_COLOR);
            Vertex<Coordinate> u = e.getEndPoint1();
            Vertex<Coordinate> v = e.getEndPoint2();
            g.drawLine(u.getObject().getX(),u.getObject().getY(),v.getObject().getX(),v.getObject().getY());
       }
       
       // now draw vertices
       while (vertexIt.hasNext() ){
            Vertex<Coordinate> u =  vertexIt.next();
            paintVertex(u.getObject());
       }
   }
   
   // stops the game by setting input fields to be nonEditable
   private void stopGame()
   {
        firstText.setEditable(false);
        secondText.setEditable(false);
   }   
   // displays a message on the screen
   private void displayMessage(String message)
   {
        warning.setText(message);
   }

 // you need to implement this method. It is called automatically when the user presses 
 // the "enter" button
 private void nextMove(int i, int j)
 {
   FindPath<Coordinate> path=new FindPath<Coordinate>(); // Create new path object with which to invoke findPath methods on graph.
   // Get vertices corresponding to input indices.
   Vertex<Coordinate> endPoint1=vertexLookup.elementAt(i); // First endpoint.
   Vertex<Coordinate> endPoint2=vertexLookup.elementAt(j); // Second endpoint.
   
   // Check that there exists an edge between vertices i and j.
   try{
     if (!graph.areAdjacent(endPoint1, endPoint2)){ // If endpoints currently under consideration are not adjacent.
       displayMessage("Sorry, "+i+" and "+j+" are not adjacent.");
       return;
     }
   } catch(GraphException e){
     return;
   }

   // Check that this edge is not frozen (if it IS frozen, notify user).
   try{
     Edge<Coordinate> edge1=graph.findEdge(endPoint1, endPoint2); // Get edge connecting ednpoints.
     if (edge1==null){
       displayMessage("Sorry, no self-loops.");
       return;
     }
     if (edge1.getMarker()){ // If edge is frozen...
       displayMessage("Sorry, this edge is frozen.");
       return;
     }
   } catch(GraphException e){
     return;
   }
   // Otherwise, delete this edge from the graph.
   try{
     graph.deleteEdge(graph.findEdge(endPoint1, endPoint2));
   } catch(GraphException e){}
   
   drawBoard(graph.vertices(), graph.edges()); // Re-draw board showing deletion of edge.
   
   // Check if user has won by deleting this edge.
   Iterator<Vertex<Coordinate>> pathVertices=path.givePath(graph,vertexLookup.elementAt(startVertex),vertexLookup.elementAt(endVertex)); // Search for any path from v to u.
   
   if (pathVertices==null){ // If no such path exists...
     displayMessage("User wins.");
     stopGame();
     return;
   }
   
   // At this point, user has deleted an edge but has not yet won the game, so computer takes turn.
   // Does so using the 'dumb' method of searching for any path connecting v and u, and freezing the first edge in this path that is not already frozen.
   
   // Computer's move
   Vertex<Coordinate> currentVertex=pathVertices.next(); // Get first vertex in path (startVertex).
   while(pathVertices.hasNext()){ // While there are still vertices remaining in iterator to consider...
     Vertex<Coordinate> nextVertex=pathVertices.next(); // ...consider next vertex.
     try{
       if(graph.findEdge(currentVertex, nextVertex).getMarker()==false){ // If edge connecting currentVertex to nextVertex is un-frozen...
         graph.findEdge(currentVertex, nextVertex).setMarker(true); // Freeze this edge.
         break;
       }
     } catch(GraphException e){}
     currentVertex=nextVertex; // At this point, edge under consideration WAS frozen, so move to opposite end of edge, in order to consider NEXT edge.
   }
   
   drawBoard(graph.vertices(), graph.edges()); // Re-draw board, in order to show new frozen edge.
   
   // Check if computer has won by freezing this edge.
   if (path.markedPathExists(graph,vertexLookup.elementAt(startVertex),vertexLookup.elementAt(endVertex))==true){ // If graph now contains a frozen path from v to u...
     displayMessage("Computer wins.");
     stopGame();
   }
   
 }

 // you will need to finish implementation of this method
   private void startNewGame()
   {
       Coordinate [] vertices = getVertices();       // gets the new vertices for the game
       Pair[]           edges = getEdges(vertices);  // gets the new edges for the game
       setEndVertices();                             // chooses the start and the end vertices of the game
       graph = new Graph<Coordinate>();
       vertexLookup = new Vector<Vertex<Coordinate>>(NUM_VERTICES);

       for ( int i = 0; i < NUM_VERTICES; i++ ){
     // inserts the vertex with name "i" into the graph, and stores the Vertex object
     // corresponding to this vertex at the ith location of Vector vertexLookup
        vertexLookup.add(i,graph.insertVertex(vertices[i]));
       }
    // At this point I have inserted all the vertices. For vertex with index i,
    // vertexLookup.elementAt(i) will return the reference to the vertex object
    // created by the graph class for the vertex with index i.

    // you need to insert the edges, making sure you don't insert duplicate edges.
    // After you implement this method, call drawBoard() method which draws the current
    // graph.
       for ( int i = 0; i < NUM_EDGES; i++ ){ // For each edge in 'edges'...
         
         // Get endpoints of given edge.
         Vertex<Coordinate> endPoint1=vertexLookup.elementAt(edges[i].getFirst()); // First endpoint.
         Vertex<Coordinate> endPoint2=vertexLookup.elementAt(edges[i].getSecond()); // Second endpoint.
         
         try{if (!graph.areAdjacent(endPoint1, endPoint2)){ // If vertices are adjacent...
           graph.insertEdge(endPoint1, endPoint2); // ...insert corresponding edge into graph.
         }
         } catch(GraphException e){}
       }
       
       // Draw board with vertices and edges.
       drawBoard(graph.vertices(), graph.edges());
   
   }
   
   public static void main(String[] args) throws GraphException {
      GraphFun gui = new GraphFun();}
    
   
}
