//package cheatdetect;

import java.io.File;
import java.io.*;
import java.util.Iterator;

public class TestHashDictionary {
  
    public static void main(String[] args) {

 // Test 1: try empty constructor
 try
 {
  HashDictionary h = new HashDictionary();
  System.out.println("***Test 1 failed");
 }
 catch (DictionaryException e) {System.out.println("   Test 1 succeeded");}

       
 StringHashCode sH = new StringHashCode();
     
 float lF = (float) 0.5;
 HashDictionary h = new HashDictionary(sH,lF);

 // Test 2: add and find an entry .
 Integer i = new Integer(1);
        Pair    anyPair = new Pair(0,0);
 h.insert("R3C1",anyPair);
 if (h.find("R3C1") == null ) 
     System.out.println("***Test 2 failed");
 else System.out.println("   Test 2 succeeded");

 // Test 3: look for an inexistent key
 if (h.find("R4C4") != null) 
   System.out.println("***Test 3 failed");
 else System.out.println("   Test 3 succeeded");

 // Test 4: try to delete a nonexistent entry.
 // Should throw an exception.
 try {
     Entry temp = h.remove("R6C8");
     System.out.println("***Test 4 failed");
 } catch (DictionaryException e) {
     System.out.println("   Test 4 succeeded");
 }

 // Test 5: delete an actual entry.
 // Should not throw an exception.
 try {
     Entry temp = h.remove( "R3C1");
            if (h.find("R3C1") == null && temp.Value().End() == 0 && temp.Value().Start() == 0 ){
          System.out.println("   Test 5 succeeded");
            }
            else  System.out.println("***Test 5 failed");
 } catch (DictionaryException e) {
     System.out.println("***Test 5 failed");
 }

 String s;

 // Test 6: insert 200 different values into the Dictionary
 // and check that all these values are in the Dictionary       
        h = new HashDictionary(sH,lF);
        
  Integer temp = new Integer(0);
  for (int k = 0; k < 200; ++k) {
      s = (new Integer(k)).toString();
      h.insert("R"+s+"C"+s,anyPair);
  }
 boolean fail = false;
 for (int k = 0; k < 200; ++k) {
     s = (new Integer(k)).toString();
     if (h.find("R"+s+"C"+s) == null) {
   fail = true;
     }
 }
 if ( fail ) System.out.println("***Test 6 failed");
 else System.out.println("   Test 6 succeeded");
        
        // Test 7, check dictionary size
         if ( h.size() == 200 ) System.out.println("   Test 7 succeeded");
         else System.out.println("***Test 7 failed");

 // Test 8: Delete a lot of entries from the  Dictionary
 fail = false;
 try
 {
     for ( int k = 0; k < 200; ++k )
     {
  s = (new Integer(k)).toString();
  h.remove("R"+s+"C"+s);
     }
    for (int k = 0; k < 200; ++k) {
       s = (new Integer(k)).toString();
       if (h.find("R"+s+"C"+s) != null) {
    fail = true; }     
    }
 }
 catch (DictionaryException e) 
 {
  System.out.println("***Test 8 failed");
  fail = true;
 }
 if ( !fail) System.out.println("   Test 8 succeeded");
 else  System.out.println("***Test 8 failed");


        
        
        
 /*// Test 8: Get iterator over all keys
 fail = false;
        h = new HashDictionary(sH,lF);
 
 for (int k = 0; k < 100; k++) 
 {
           s = (new Integer(k)).toString();        
           h.insert(s,0);
 }
 try
 {
     for (int k = 10; k < 30; ++k) 
     {
                s = (new Integer(k)).toString();
  h.remove(s);
     }
 }
 catch(DictionaryException e)
 {
     fail = true;
 }
        Iterator it = h.elements();
 int count = 0;

 while (it.hasNext()){
     count= count+1;
     it.next();
 }
 if ( count == 80 && ! fail ) 
     System.out.println("   Test 8 succeeded");
 else  System.out.println("****Test 8 failed ");*/


        
        
 lF = (float) 0.05;

 long startTime,finishTime;

 double time;

 while (lF < 0.99 ){
            startTime = System.currentTimeMillis();
     h = new HashDictionary(sH,lF);
 
     for (int k = 0; k < 10000; ++k) {
  s = (new Integer(k)).toString();
  h.insert(s+"C"+s,anyPair);
     }
     try{
  for ( int k = 0; k < 10000; ++k ){
   s = (new Integer(k)).toString();
          h.remove(s+"C"+s);
  }
     }
     catch (DictionaryException e) { System.out.print("Failure");return;}
            finishTime = System.currentTimeMillis();
            time = finishTime - startTime;
     System.out.println("For load factor "+lF+ " , average num. of  probes is " + h.averNumProbes()
                               + " time in milseconds is "+ time);
     lF = lF+ (float) 0.05;
 }

    }
    
}
    



