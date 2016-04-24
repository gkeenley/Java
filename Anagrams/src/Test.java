// Compile this program with  your code by running javac Test.java, and then run it with java Test 
// Each failed tests 1-10 are 4  marks. Test 11 is 10 marks
//package calculatenr;

//package findkeywords;
import java.util.Iterator;
import java.util.Random;

public class Test{


public static int computeHeight(Position<String,Integer> n, AVLTree<String, Integer>  t)
    {
 if ( t.external(n) ) return(0);
 else return( 1+Math.max(computeHeight(t.left(n),t),computeHeight(t.right(n),t)) );
    }

public static boolean checkTree(Position<String,Integer> n, AVLTree<String, Integer>  t)
    {
 boolean balanced = true;
 giveHeight(n,balanced,t);
 return(balanced);
    }

public static int giveHeight(Position<String,Integer> n, boolean balanced,AVLTree<String, Integer>  t)
    {
 if ( t.external(n)  ) return(0);
 else
     {
  int  l = giveHeight(t.left(n),balanced,t);
  int r = giveHeight(t.right(n),balanced,t);   
  if (Math.abs(l-r)> 1) {
                    balanced = false;
                
                }
  return( 1+Math.max(l,r) );
     }
    }

// returns null  if there is an element in the left subtree of node p
// such that its value is larger than the element stored at p
// If this method returns a non-null String, then it is the largest key in the 
// subtree rooted at p
public static String checkLeftSubtree(AVLTree<String, Integer>  t,Position<String,Integer> p, StringComparator comp)
{
    if (t.external(p)) 
        return(new String("whatever"));
    else{
        
        String resultLeft = checkLeftSubtree(t,t.left(p),comp);
        if (resultLeft == null) return null;
        
        if (comp.compare(resultLeft,"whatever") != 0 ) {
            if ( comp.compare(resultLeft,(((DictEntry<String,Integer>) (p.element())).key())) > 0 ) return null;
        }

        String resultRight = checkLeftSubtree(t,t.right(p),comp);
        if (  resultRight == null ) return(null);
        
        if (comp.compare(resultRight,"whatever") != 0) {
            if ( comp.compare(resultRight,(((DictEntry<String,Integer>) (p.element())).key())) > 0  )
            return(resultRight);
            else return (  p.element().key()  );
        }
        else return(p.element().key());
        
    }
    
}

// returns null  if there is an element in the right subtree of node p
// such that its value is smaller than the element stored at p
// If this method returns a non-null String, then it is the smallest key in the 
// subtree rooted at p
public static String checkRightSubtree(AVLTree<String, Integer>  t,Position<String,Integer> p, StringComparator comp)
{
    if (t.external(p)) 
        return(new String("whatever"));
    else{
        
        String resultLeft = checkRightSubtree(t,t.left(p),comp);
        if (resultLeft == null) return null;
        
        //if (comp.compare(resultLeft,"whatever") != 0 ) {
        //    if ( comp.compare(resultLeft,(((DictEntry) (p.element())).key())) > 0 ) return null;
        //}

        String resultRight = checkRightSubtree(t,t.right(p),comp);
        if (  resultRight == null ) return(null);
        
        if (comp.compare(resultRight,"whatever") != 0 ) {
            if ( comp.compare(resultRight, p.element().key()) < 0 ) return null;
        }        
        
        if (comp.compare(resultLeft,"whatever") != 0) {
            if ( comp.compare(resultLeft,p.element().key()) < 0  )
            return(resultLeft);
            else return ( p.element().key() );
        }
        else return(p.element().key());
    }
    
}



public static boolean checkIfBST(AVLTree<String, Integer>  treeI, StringComparator comp){
    return( (checkLeftSubtree(treeI,treeI.giveRoot(),comp) != null ) && (checkRightSubtree(treeI,treeI.giveRoot(),comp)!= null)  );

} 

public static void main(String[] args) throws AVLTreeException{
   
    StringComparator stringComp = new StringComparator(); 
    int startTest = 1;
    
    if ( args.length == 1  ) // run starting at some test
        startTest = (new Integer(args[0])).intValue();
    
    switch(startTest){ 
    case 1: // Test 1: add and find a few entries
        try {
            AVLTree<String, Integer>  tree = new AVLTree<String, Integer> (stringComp);
            String st;

            st = new String("hello");
            tree.insert(st,new Integer(0));
            st = new String("hello6");
            tree.insert(st,new Integer(1));
         
            if ( tree.find(new String("hello")) == null  || tree.find(new String("hello6")) == null  )
                System.out.println("*****Test 1: Failed");
            else System.out.println("Test 1: Passed");
        }
        catch (AVLTreeException e){
            System.out.println("*****Test 1: Failed");
        }
 
    case 2: //Test 2: find nonexistant entry
        try {
            AVLTree<String, Integer>  tree = new AVLTree<String, Integer> (stringComp);
            String st;

            st = new String("hello");
            tree.insert(st,new Integer(0));
            st = new String("hello6");
            tree.insert(st,new Integer(1));
         
            if ( tree.find(new String("hello7")) != null )
            System.out.println("*****Test 2: Failed");
            else System.out.println("Test 2: Passed");
        }
        catch (AVLTreeException e){
            System.out.println("*****Test 2: Failed");
        }    
    
 
    case 3: //Test 3: try to change value
        try {
            AVLTree<String, Integer>  tree = new AVLTree<String, Integer> (stringComp);
            String st;

            st = new String("hello");
            tree.insert(st,new Integer(0));
            st = new String("hello6");
            tree.insert(st,new Integer(1));
        
            tree.modifyValue(new String("hello" ),new Integer(5) );
            DictEntry<String,Integer> e = tree.find( new String("hello" ) );
            if ( ((Integer) e.value()).intValue() != 5 )
                System.out.println("*****Test 3: Failed");
            else  System.out.println("Test 3: Passed");
        }
        catch (AVLTreeException e){
            System.out.println("*****Test 3: Failed");
        }
  
    case 4 :// Test 4: insert many entries
        try {
            boolean fail = false;
            AVLTree<String, Integer>  t = new AVLTree<String, Integer> (stringComp);
            String st;
            for ( int i = 0; i < 50000;i++ )  {
                st = new String(Integer.toString(i));
                t.insert(st,new Integer(i));
            }
            for ( int i = 0; i < 50000;i++ )   {
             st = new String(Integer.toString(i));
  DictEntry<String,Integer> e = t.find(st);
  if ( e == null ) fail = true;
            }
            if ( fail )
                System.out.println("*****Test 4: Failed");
            else System.out.println("Test 4: Passed");
        }
        catch (AVLTreeException e){
            System.out.println("*****Test 4: Failed");
    }

    case 5: //Test 5: try to change value in non-existing entry
        try {
            AVLTree<String, Integer>  tree = new AVLTree<String, Integer> (stringComp);
            String st;

            st = new String("hello");
            tree.insert(st,new Integer(0));
            st = new String("hello6");
            tree.insert(st,new Integer(1));
                
            tree.modifyValue(new String("hello454646" ),new Integer(5) );
            System.out.println("*****Test 5: Failed");
        }
        catch (AVLTreeException e){
            System.out.println("Test 5: Passed");
    }


    case 6: //Test 6: try to remove an entry
        try {
            AVLTree<String, Integer>  tree = new AVLTree<String, Integer> (stringComp);
            String st;

            st = new String("hello");
            tree.insert(st,new Integer(0));
            st = new String("hello6");
            tree.insert(st,new Integer(1));
        
            tree.remove(new String("hello6" ));
            DictEntry<String,Integer> e= tree.find(new String("hello6" ));
            if (e != null ) System.out.println("*****Test 6: Failed");
            else System.out.println("Test 6: Passed");
        }
        catch (AVLTreeException e){
            System.out.println("*****Test 6: Failed");
        }

    case 7: // Test 7: Check in order iterator 
        try {
            boolean fail = false;
            AVLTree<Integer, Integer>  t = new AVLTree<Integer, Integer> (new IntegerComparator());
            int num = 1000,i;
            int[] table = new int[num];
       
            for ( i = 0; i < num; i++) table[i] = i;
       
            Random r = new Random();
       
            for ( i = 0; i < num; i++){
                int t1 = r.nextInt(num);
                int t2 = r.nextInt(num);
                int temp = table[t1];
                table[t1]= table[t2];
                table[t2] = temp;
            }
           
              
            for ( i = 0; i < num;i++ )   {
                t.insert(new Integer(i), new Integer(0));
            }

            Iterator<DictEntry<Integer,Integer> > it = t.inOrder( );
            int count = 0;
            while (it.hasNext())
            {
                DictEntry<Integer,Integer> next = it.next();
                if ( ((Integer) next.key()).intValue() != count )
                {
                    fail = true;
                    break;
                }
                count++;
            }        
            if ( count != num) fail = true;
            if ( fail )
                System.out.println("*****Test 7: Failed");
            else System.out.println("Test 7: Passed");
        }
        catch (AVLTreeException e){
            System.out.println("*****Test 7: Failed");
        }

    case 8: // Test 8: Insert and remove lots of entries
        try {
            boolean fail = false;
            AVLTree<Integer, Integer> t = new AVLTree<Integer, Integer>(new IntegerComparator());
            
            int num = 10000;
            int i;
            int[] table = new int[num];
       
            for (i = 0; i < num; i++) table[i] = i;

       
            Random r = new Random();
       
            for ( i = 0; i < num; i++){
                int t1 = r.nextInt(num);
                int t2 = r.nextInt(num);
                int temp = table[t1];
                table[t1]= table[t2];
                table[t2] = temp;
            }
              
            for ( i = 0; i < num; i++ )   {
                t.insert(new Integer(table[i]), new Integer(i));
            }
            
            for ( i = 0; i < num/2;i++ )   {
                t.remove(new Integer(table[i]));
            }
            
            for ( i = 0; i < num/2;i++ )   {
                if (t.find(new Integer(table[i])) != null){
                    fail = true;
                    break;
                }
           }
            for ( i = num/2; i < num;i++ )   {
              
                if (t.find(new Integer(table[i])) == null){
                    fail = true;
                    break;
                }
           }
            
            if ( fail )
                System.out.println("*****Test 8: Failed" );
            else System.out.println("Test 8: Passed");
        }
        catch (AVLTreeException e){
            System.out.println("*****Test 8: Failed exe");
        } 
    
    case 9: // Test 9: test "findAll"  method
        try {
            AVLTree<String, Integer>  tree = new AVLTree<String, Integer> (stringComp);
            String st = new String("hello");
            String st1 = new String("hello1");
            tree.insert(st,new Integer(0));
            tree.insert(st1,new Integer(0));
            tree.insert(st,new Integer(1));
            tree.insert(st1,new Integer(0));
            
            tree.insert(st,new Integer(2));
            Iterator<DictEntry<String,Integer>> it = tree.findAll(st);
            
            int count = 0;
            while (it.hasNext())
            {
             DictEntry<String,Integer> e = it.next();
             if (! (e.value().intValue() == 0  ||e.value().intValue() == 1 || e.value().intValue() == 2) )
              count = count+20;
             else count = count+1;
            }
            if ( count == 3 )
             System.out.println("Test 9: passed");
            else  System.out.println("*****Test 9: failed");
        }
        catch (AVLTreeException e){
            System.out.println("*****Test 9: failed");
        }
          
    
        case 10:  // Test 10: Check if the tree is indeed a binary search tree
            try{
                StringComparator comp = new StringComparator();
                AVLTree<String, Integer> treeI = new AVLTree<String, Integer> (comp);
                int numItems = 1000;
                for ( int i = 0; i < numItems;i++ ){
                    treeI.insert(new String((new Integer(i).toString())),new Integer(i));
                }
                if ( checkIfBST(treeI,comp)) System.out.println("Test 10: Passed");
                else System.out.println("*****Test 10: Failed");
            }
            catch (AVLTreeException e){
                System.out.println("*****Test 10: Failed");
            }

        case 11: // Test 11: check height-balance property
            try{
                AVLTree<String, Integer>  treeI = new AVLTree<String, Integer>  (new StringComparator());
                int numItems = 1000;
                for ( int i = 0; i < numItems;i++ ){
                    treeI.insert(new String((new Integer(i).toString())),new Integer(i));
                }
  
                int  height = computeHeight(treeI.giveRoot(),treeI);
                boolean fail = false;
                int size = numItems;

                int bound = (int) (Math.log(size)/Math.log(2)) ;
                if ( height <   bound-2 || height > 2*bound+2 )
                    fail = true;
                //System.out.println("Height is: "+ height + " Bound is: " + bound);
 
                boolean balanced   = checkTree(treeI.giveRoot(),treeI);
    
                if ( fail || !balanced  ) System.out.println("*****Test 11: Failed ");
                else System.out.println("Test 11: Passed");
            }
        catch (AVLTreeException e){
            System.out.println("*****Test 11: Failed");
        }

}
}
}
