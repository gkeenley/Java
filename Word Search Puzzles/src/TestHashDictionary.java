
public class TestHashDictionary {
  
    public static void main(String[] args) {

    ///////////////////////////////////////////////////////////////////////////  
 // Test 1: add a few items

    StringHashCode sH = new StringHashCode();
         
    float lF = (float) 0.5;
    HashDictionary<String,Integer> h = new HashDictionary<String,Integer>(sH,lF);

    try
 {
  h.insert(new String("5"),new Integer(6));
  h.insert(new String("6"),new Integer(6));
  System.out.println("***Test 1 succeeded");
 }
 catch (DictionaryException e) {
  System.out.println("   Test 1 failed");
  }

   
 ///////////////////////////////////////////////////////////////////////////
 // Test 2: add and find an entry 
    h = new HashDictionary<String,Integer>(sH,lF);

 
 Integer i = new Integer(1);

 try{
  h.insert("R3C1",i);
  if (h.find("R3C1") == null ) 
   System.out.println("***Test 2 failed");
  else System.out.println("   Test 2 succeeded");
 }
 catch (DictionaryException e) {
  System.out.println("   Test 2 failed");
 }

 ///////////////////////////////////////////////////////////////////////////
 // Test 3: look for an non-existent key
 
 if (h.find("R4C4") != null) 
   System.out.println("***Test 3 failed");
 else System.out.println("   Test 3 succeeded");

 ///////////////////////////////////////////////////////////////////////////
 // Test 4: try to delete a nonexistent entry.
 // Should throw an exception.
 try {
     h.remove("R6C8");
     System.out.println("***Test 4 failed");
 } catch (DictionaryException e) {
     System.out.println("   Test 4 succeeded");
 }

 ///////////////////////////////////////////////////////////////////////////
 // Test 5: delete an actual entry.
 // Should not throw an exception.
 try {
     h.remove( "R3C1");
            if (h.find("R3C1") == null )
          System.out.println("   Test 5 succeeded");
            else  System.out.println("***Test 5 failed");
 } catch (DictionaryException e) {
     System.out.println("***Test 5 failed");
 }

 
 ///////////////////////////////////////////////////////////////////////////
 // Test 6: insert 200 different values into the Dictionary
 // and check that all these values are in the Dictionary       
 String s;

 
 h = new HashDictionary<String,Integer>(sH,lF);
     
 try{
  Integer temp = new Integer(0);
  for (int k = 0; k < 200; ++k) {
      s = (new Integer(k)).toString();
      h.insert("R"+s+"C"+s,temp);
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
 }
  catch (DictionaryException e) {
      System.out.println("***Test 6 failed");
  }

 ///////////////////////////////////////////////////////////////////////////
    // Test 7, check dictionary size
         if ( h.size() == 200 ) System.out.println("   Test 7 succeeded");
         else System.out.println("***Test 7 failed");

    //////////////////////////////////////////////////////////////////////////     
 // Test 8: Delete a lot of entries from the  Dictionary
 boolean fail = false;
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
     
        
 ///////////////////////////////////////////////////////////////////////////
 // Test 9: try to add a duplicate entry 
    h = new HashDictionary<String,Integer>(sH,lF);

 
 i = new Integer(1);

 try{
  h.insert("R3C1",i);
  h.insert("R3C1",i);
  System.out.println("***Test 9 failed");
 }
 catch (DictionaryException e) {
  System.out.println("   Test 9 succeded");
 }

    //////////////////////////////////////////////////////////////////////////    
        
 lF = (float) 0.05;

 long startTime,finishTime;

 double time;

 while (lF < 0.99 )
 {
  startTime = System.currentTimeMillis();
     h = new HashDictionary<String,Integer>(sH,lF);
 
     try
     {
         for (int k = 0; k < 10000; ++k) {
       s = (new Integer(k)).toString();
       h.insert(s+"C"+s,new Integer(0));
      }
  
      for ( int k = 0; k < 10000; ++k )
      {
       s = (new Integer(k)).toString();
    h.remove(s+"C"+s);
      }
     }
     catch (DictionaryException e) 
     { 
      System.out.print("Failure");
      return;
     }
            finishTime = System.currentTimeMillis();
            time = finishTime - startTime;
            System.out.println("For load factor "+lF+ " , average num. of  probes is " + h.averNumProbes()
                               + " time in milseconds is "+ time);
     lF = lF+ (float) 0.05;
 }

    }
    
}
    



