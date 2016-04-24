//package A5;

import java.util.*;

public class TestPath {
   public static void main(String[] args) throws GraphException {
       
       int startTest = 1;
       if (args.length > 0) startTest = (new Integer(args[0])).intValue();
       
       switch(startTest){
      
       case 1: /////////////////////Test 1, test markedPathExists method
       {
           int size = 100;
           Graph<Integer> g = new Graph<Integer>();
           Vector<Vertex<Integer>> lookup  = new Vector<Vertex<Integer>>(10);
               
           for (int i = 0;i < size; i++ )
                lookup.add(i,g.insertVertex(new Integer(i)));
            
            for (int i = 0;i < size; i++ )
                 for (int j = i+1; j < size; j++ ){
                     g.insertEdge(lookup.elementAt(i),lookup.elementAt(j));
                     Edge<Integer> e = g.findEdge(lookup.elementAt(i),lookup.elementAt(i+1));
                     e.setMarker(false);
                 }

           for ( int i = 5; i < 70; i++){
               Edge<Integer> e = g.findEdge(lookup.elementAt(i),lookup.elementAt(i+1));
               e.setMarker(true);
           }
           boolean fail = false;
           
           FindPath<Integer> fp = new FindPath<Integer>();
           int i = 5;
           int j = 70;
           while ( j > i){
               fail = !fp.markedPathExists(g,lookup.elementAt(i),lookup.elementAt(j));
               if ( fail ){
                   System.out.println("**********Test 1 failed *** ");
                   break;
               }
               i++;
               j--;
               
           }
           
           if ( ! fail ){
               i = 70;
               j = size-1;
               while ( j > i){
                    fail = fp.markedPathExists(g,lookup.elementAt(i),lookup.elementAt(j));
                    i++;
                    j--;
                    if ( fail ){
                        System.out.println("**********Test 1 failed");
                    break;
                    }
               }
            if ( ! fail ) System.out.println("Test 1 passed");
           }
       }
       
        case 2: /////////////////////Test 2, test givePath method
       {
           int size = 100;
           Graph<Integer> g = new Graph<Integer>();
           Vector<Vertex<Integer>> lookup  = new Vector<Vertex<Integer>>(10);
               
           for (int i = 0;i < size; i++ )
                lookup.add(i,g.insertVertex(new Integer(i)));
            
            /*for (int i = 0;  i < size/2; i++ )
                 for (int j = i+1; j < size/2; j++ ){
                     g.insertEdge(lookup.elementAt(i),lookup.elementAt(j));
                 }*/
           
           for (int i = 0;  i < size/2-1; i++ ){
              g.insertEdge(lookup.elementAt(i),lookup.elementAt(i+1));
           }
           
            for (int i = 0;  i < size/2-3; i++ ){
              g.insertEdge(lookup.elementAt(i),lookup.elementAt(i+3));
           }

            for (int i = 5;  i < size/2-5; i++ ){
              g.insertEdge(lookup.elementAt(i),lookup.elementAt(i+5));
           }
           
            for (int i = size/2+1;i < size; i++ )
                 for (int j = i+1; j < size; j++ ){
                     g.insertEdge(lookup.elementAt(i),lookup.elementAt(j));
                 }

           boolean fail = false;
           
           FindPath<Integer> fp = new FindPath<Integer>();
           

           for ( int i = 0; i < size/2; i++ )
           {
               if ( fail ) break;
               
               for ( int j = i+1; j < size/2; j++)
               {
                   Iterator<Vertex<Integer>> it = fp.givePath(g,lookup.elementAt(i),lookup.elementAt(j));
                   if ( !it.hasNext() ){
                       System.out.println("**********Test 2 failed " );
                       fail = true;
                       i = j = size;
                       break;
                    }
                   
                   Vertex<Integer> next = it.next();
               
                   if ( next != lookup.elementAt(i) ){
                       System.out.println("**********Test 2 failed");
                       fail = true;
                       i = j = size;
                       break;
                   }
               
                   Vertex<Integer> prev;
                   while (it.hasNext()){
                        prev = next;
                        next = it.next();
                        if ( !g.areAdjacent(prev,next) ){
                            System.out.println("**********Test 2 failed ");
                            fail = true;
                            i = j = size;
                        }       
                   }
               
                   if ( !fail )
                    if ( next != lookup.elementAt(j) ){
                            System.out.println("**********Test 2 failed "  );
                            fail = true;
                            i = j = size;
                    }
               }
           }
        
           
           if ( ! fail ){
                int i = 0;
                int j = size/2+1;
                while ( i < size/2-3){
                     Iterator<Vertex<Integer>> it = fp.givePath(g,lookup.elementAt(i),lookup.elementAt(j));               
                     if ( it != null  )
                     {
                         System.out.println("**********Test 2 failed");
                         break;
                     }                    
                   i++;
                   j++;
               }
           }
          if (!fail ) System.out.println("Test 2 Passed");     
        }
       }       
   }
}
  

   

