//package A5;


import java.util.*;

public class TestGraph  {
   public static void main(String[] args) throws GraphException {
       
       int startTest = 1;
       if (args.length > 0) startTest = (new Integer(args[0])).intValue();
       
       switch(startTest){
      
       case 1: /////////////////////Test 1, insert some nodes
       {
           int size = 10;
           Graph<Integer> g = new Graph<Integer>();
           Vector<Vertex<Integer>> lookup  = new Vector<Vertex<Integer>>(10);
               
           for (int i = 0;i < size; i++ )
                lookup.add(i,g.insertVertex(new Integer(i)));
            
            
            if ( g.getNumVertices() == size )
                System.out.println("Test 1 passed");
            else System.out.println("**********Test 1 failed");
       }
       
  
       case 2: /////////////////////Test 2, insert some nodes and edges
       try{
           int size = 10;
           Graph<Integer> g = new Graph<Integer>();
           Vector<Vertex<Integer>> lookup  = new Vector<Vertex<Integer>>(10);
           
           for (int i = 0;i < size; i++ )
                lookup.add(i,g.insertVertex(new Integer(i)));
            
            for (int i = 0;i < size; i++ )
                 for (int j = i+1; j < size; j++ )
                     g.insertEdge(lookup.elementAt(i),lookup.elementAt(j));
           
            if ( g.getNumEdges() == size*(size-1)/2 )
                System.out.println("Test 2 passed");
            else  System.out.println("**********Test 2 failed");
       }
       catch(GraphException e){
           System.out.println("**********Test 2 failed");
       }
  
       
       case 3: /////////////////////Test 3, try to insert  self loops and multiple edges
       try{
           int size = 10;
           Graph<Integer> g = new Graph<Integer>();
           Vector<Vertex<Integer>> lookup  = new Vector<Vertex<Integer>>(10);
           
           for (int i = 0;i < size; i++ )
                lookup.add(i,g.insertVertex(new Integer(i)));
            
            for (int i = 0;i < size; i++ )
                 for (int j = 0; j < size; j++ )
                     g.insertEdge(lookup.elementAt(i),lookup.elementAt(j));
            System.out.println("**********Test 3 failed");
       }
       catch(GraphException e){
           System.out.println("Test 3 passed");
       }
  
      
       
       case 4:  /////////////////////Test 4, test for adjacency
       try{
           int size = 10;
           Graph<Integer> g = new Graph<Integer>();
           Vector<Vertex<Integer>> lookup  = new Vector<Vertex<Integer>>(10);
           
           for (int i = 0;i < size; i++ )
                lookup.add(i,g.insertVertex(new Integer(i)));
            
            g.insertEdge(lookup.elementAt(3),lookup.elementAt(5));
           
            if ( g.areAdjacent(lookup.elementAt(5),lookup.elementAt(3))  )
                System.out.println("Test 4 passed");
            else  System.out.println("**********Test 4 failed");
            
       }
       catch(GraphException e){
           System.out.println("**********Test 4 failed");
       }      
       
      
       case 5: /////////////////////Test 5, test for non-existant adjacency
       try{
           
           int size = 10;
           Graph<Integer> g = new Graph<Integer>();
           Vector<Vertex<Integer>> lookup  = new Vector<Vertex<Integer>>(10);
           
           for (int i = 0;i < size; i++ )
                lookup.add(i,g.insertVertex(new Integer(i)));
                     
            g.insertEdge(lookup.elementAt(3),lookup.elementAt(5));
            g.insertEdge(lookup.elementAt(2),lookup.elementAt(5));
            
            boolean fail = false;
            
            for (int i = 0; i < size; i++ )
                 for (int j = i+1; j < size; j++ )
                     if   ( g.areAdjacent(lookup.elementAt(i),lookup.elementAt(j)) && 
                             ! ( (i == 3 && j == 5) || (i == 5 && j == 3)|| (i == 2 && j == 5) || (i == 5 && j == 2) ) 
                             ) 
                         fail = true;
           
           if ( !fail )
                System.out.println("Test 5 passed");
            else  System.out.println("**********Test 5 failed");
            
       }
       catch(GraphException e){
           System.out.println("**********Test 5 failed");
       }      
       
       
       case 6:  /////////////////////Test 6, delete an edge
       try{
           int size = 10;
           Graph<Integer> g = new Graph<Integer>();
           Vector<Vertex<Integer>> lookup  = new Vector<Vertex<Integer>>(10);
           
           for (int i = 0;i < size; i++ )
                lookup.add(i,g.insertVertex(new Integer(i)));
            
            for (int i = 0;i < size; i++ )
                 for (int j = i+1; j < size; j++ )
                     g.insertEdge(lookup.elementAt(i),lookup.elementAt(j));
           Edge<Integer> e = g.findEdge(lookup.elementAt(3),lookup.elementAt(5));
           g.deleteEdge(e);
           if ( !g.areAdjacent( lookup.elementAt(3),lookup.elementAt(5)  )  ) System.out.println("Test 6 passed");
           else System.out.println("**********Test 6 failed");
       }
       catch(GraphException e){
           System.out.println("**********Test 6 failed");
       }

      
       case 7: /////////////////////Test 7, try to delete a non-existant edge
       try{
           int size = 10;
           Graph<Integer> g = new Graph<Integer>();
           Vector<Vertex<Integer>> lookup  = new Vector<Vertex<Integer>>(10);
           
           for (int i = 0;i < size; i++ )
                lookup.add(i,g.insertVertex(new Integer(i)));
            
           g.insertEdge(lookup.elementAt(3),lookup.elementAt(7));
           boolean fail = false;
                     
            for (int i = 0;i < size; i++ )
                 for (int j = i+1; j < size; j++ )
                     if ( i != 3 && j != 7 )
                     {
                         Edge<Integer> e = g.findEdge(lookup.elementAt(i),lookup.elementAt(j));
                        if (e != null ){
                           fail = true;
                           break;
                        }
                     }
            if ( fail) System.out.println("**********Test 7 failed");
            else System.out.println("Test 7 passed");
       }
       catch(GraphException e){
           System.out.println("Test 7 passed");
       }
       
       case 8: ///////////////////// Test 8: get incident edges
       try{
           
            int size = 10;
           Graph<Integer> g = new Graph<Integer>();
           Vector<Vertex<Integer>> lookup  = new Vector<Vertex<Integer>>(10);
           
           for (int i = 0;i < size; i++ )
                lookup.add(i,g.insertVertex(new Integer(i)));
            
            for (int i = 0;i < size; i++ ){
                 if ( i != 1) g.insertEdge(lookup.elementAt(1),lookup.elementAt(i));
                 if ( i != 7 && i != 0 && i != 1) g.insertEdge(lookup.elementAt(7),lookup.elementAt(i));                 
            }
                    
           
            Iterator<Edge<Integer>> it = lookup.elementAt(1).incidentEdges();
            int count = 0;
            while (it.hasNext()) 
            {
                count++;
                it.next();
            }
            if ( count != 9 ) System.out.println("**********Test 8 failed " );
            else{
                it = lookup.elementAt(7).incidentEdges();
                count = 0;
                while (it.hasNext()) 
                {
                    count++;
                    it.next();
                }
                if ( count != 8 ) System.out.println("**********Test 8 failed " );
                else System.out.println("Test 8 passed");
            }
       }
       catch(GraphException e){
           System.out.println("**********Test 8 failed");
       }      

        
       case 9: ///////////////////// Test 9: check findEdge
       try{
           
           int size = 10;
           Graph<Integer> g = new Graph<Integer>();
           Vector<Vertex<Integer>> lookup  = new Vector<Vertex<Integer>>(10);
           
           for (int i = 0;i < size; i++ )
                lookup.add(i,g.insertVertex(new Integer(i)));
            
            for (int i = 0;i < size; i++ ){
                 if ( i != 8) g.insertEdge(lookup.elementAt(8),lookup.elementAt(i));
            }
           Edge<Integer> e = g.findEdge(lookup.elementAt(3),lookup.elementAt(8));
           if ( e == null) System.out.println("**********Test 9 failed " );
           else  if ( e.getEndPoint1() == lookup.elementAt(3) &&  e.getEndPoint2() == lookup.elementAt(8) || 
                      e.getEndPoint1() == lookup.elementAt(8) &&  e.getEndPoint2() == lookup.elementAt(3))
                System.out.println("Test 9 passed");
           else System.out.println("**********Test 9 failed");
       }
       catch(GraphException e){
           System.out.println("**********Test 9 failed");
       }

       case 10: ///////////////////// Test 9: check edges() iterator
       try{
           
           int size = 10;
           Graph<Integer> g = new Graph<Integer>();
           Vector<Vertex<Integer>> lookup  = new Vector<Vertex<Integer>>(10);
           
           for (int i = 0;i < size; i++ )
                lookup.add(i,g.insertVertex(new Integer(i)));

           g.insertEdge(lookup.elementAt(1),lookup.elementAt(2));
           g.insertEdge(lookup.elementAt(2),lookup.elementAt(4));
           g.insertEdge(lookup.elementAt(4),lookup.elementAt(5));
           
           Iterator<Edge<Integer>> it = g.edges();
           
           boolean fail = false;
           int count = 0;
           
           while (it.hasNext() ){
               count++;
               Edge<Integer> e = it.next();
               
               if ( e.getEndPoint1() != lookup.elementAt(1) && e.getEndPoint2() != lookup.elementAt(2)  &&
                    e.getEndPoint1() != lookup.elementAt(2) && e.getEndPoint2() != lookup.elementAt(1)  &&                     
                    e.getEndPoint1() != lookup.elementAt(2) && e.getEndPoint2() != lookup.elementAt(4)  &&
                    e.getEndPoint1() != lookup.elementAt(4) && e.getEndPoint2() != lookup.elementAt(2)  &&                                          
                    e.getEndPoint1() != lookup.elementAt(4) && e.getEndPoint2() != lookup.elementAt(5)  &&
                    e.getEndPoint1() != lookup.elementAt(5) && e.getEndPoint2() != lookup.elementAt(4)                                           
                       )
                   fail = true;
           }
           if ( count != 3 ) fail = true;
           
           if ( !fail )     System.out.println("Test 10 passed");
           else System.out.println("**********Test 10 failed ");
       }
       catch(GraphException e){
           System.out.println("**********Test 10 failed");
       }
       
       
       }
       
   }
}
  

   

