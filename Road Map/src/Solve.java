import java.util.*;
import java.io.*;

public class Solve { // Draw map of input file, compute solution using 'findPath', and draw this solution on the map.

    public static void main (String[] args) {
 Node u, v;
 DrawMap display;
 int delay = 0;


 if (args.length != 1 && args.length != 2)
     System.out.println("Usage: java Solve inputFile");
 else {
     if (args.length == 2) delay = Integer.parseInt(args[1]);
     display = new DrawMap(args[0]); // 'display' is object that will be used to draw map from file args[0] (input file name) to screen.
     try {
  Map streetMap = new Map(args[0]); // Create map from same input file.
  // At this point, we have map and image of map.
  BufferedReader in = new BufferedReader(
     new InputStreamReader(System.in));
  System.out.println("Press a key to continue");
  String line = in.readLine(); // Read user input

  Iterator solution = streetMap.findPath(); // 'solution' will contain vector of nodes corresponding to solution.

  if (solution != null) { // If solution exists...
      if (solution.hasNext()) u = (Node)solution.next();
      else return;
      while (solution.hasNext()) { // For each element of 'solution'...
   v = (Node)solution.next();
   Thread.sleep(delay);
   display.drawEdge(u,v); // Draw edge for this part of solution.
   u = v;
      }
  }
  else {
      System.out.println("No solution was found");
      System.out.println("");
  }

  in = new BufferedReader(
     new InputStreamReader(System.in));
  System.out.println("Press a key to finish");
         line = in.readLine();

     }
     catch (MapException e) {
  System.out.println("Error reading input file");
     }
     catch (IOException in) {
  System.out.println("Error reading from keyboard");
     }
     catch (Exception ex) {
  System.out.println(ex.getMessage());
     }

     display.dispose();
     System.exit(0);
 }
    }
}