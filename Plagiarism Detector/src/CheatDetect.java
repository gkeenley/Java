/*
 * CheatDetect.java
 *
 *
 */

//package cheatdetect;
import java.io.*;
import java.util.LinkedList;
import java.util.Iterator;

 
/**
 *
 * @author olga
 */
public class CheatDetect {
               
 // Input: file name, start and end positions in the file
 // Output: file contents between start and end positions, inclusively, stored as a String
    private static String getStringfromFile(String name,int start,int end) throws java.io.IOException
     {
        
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(name));
        
        int ch;
        int  position = 0;
        //System.out.println("NOW STARTING "+name);
    
        while ( position != start )
        {
            ch = in.read();
            //System.out.println("1ch="+Character.toString ((char) ch));
            position++;
            if (ch == -1) throw new IOException("Invalid position range");
         }
        
        StringBuffer buf = new StringBuffer();
        
        while ( position <= end )
        {
            ch = in.read();
            //System.out.println("2ch="+Character.toString ((char) ch));
            position++;
            
            
            if (ch == -1) throw new IOException("Invalid position range");
            buf.append( (char) ch);  
        }
        
        in.close();
        return buf.toString();
    }

 public static void main(String[] args) throws java.io.IOException
 {
   
   int desiredMatchLength = Integer.parseInt(args[3]); // Define desiredMatchLength, length of matching string between file1 and file2 to find and return.
   
   // Step 1) Create hash-based dictionary containing all entries from keywords.txt
   StringHashCode hashCode = new StringHashCode(); // Create new StringHashCode object with which to compute hash codes of individual entries from keywords.txt.
   float maxLoadFactor = (float) 0.65; // Initialize maximum load factor for hash table. Value based on trial and error attempt to minimize average number of probes per operation.
   HashDictionary keywordsTable=new HashDictionary(hashCode,maxLoadFactor); // Create new hash table in which to store entries from keywords.txt.
   FileTokenRead keywordsTokenReader=new FileTokenRead(args[0]); // Create new file token reader object with which to read entries from keywords.txt.
   Iterator<Token> keywordsIterator=keywordsTokenReader.getIterator(); // Obtain iterator over tokens from keywords.txt.
   while (keywordsIterator.hasNext()){ // While there are still remaining tokens in iterator for keywords.txt...
     Token next = keywordsIterator.next(); // ...extract next token.
     keywordsTable.insert(next.Value(),null); // Insert entry with given token as key. This entry has value null because we are not interested in the start and positions of the tokens in keywords.txt.
   }
   
   // Step 2) Parse both input files into sequences of tokens, replacing user-defined identifiers with '#'.
   // Input file 1:
   LinkedList<Token> S1=new LinkedList<Token>(); // Create new linked list S1 in which to store tokens from file1.java
   FileTokenRead file1TokenReader=new FileTokenRead(args[1]); // Create new file token reader object with which to read tokens from file1 to store in S1.
   Iterator<Token> file1Iterator=file1TokenReader.getIterator(); // Obtain iterator over tokens from file1.
   while (file1Iterator.hasNext()){ // While there are still tokens remaining from file1 to consider...
     Token next=file1Iterator.next(); // ...obtain next token from file1.
     String tokenValue=next.Value(); // Extract word associated with this token.
     char firstCharacter=tokenValue.charAt(0); // Extract first character from this word.
     if ((keywordsTable.find(tokenValue)==null)&&((firstCharacter>='a'&&firstCharacter<='z')||(firstCharacter>='A'&&firstCharacter<='Z'))){ // If word is not found in keywords.txt, and its first character is a letter...
       next=new Token("#", next.startPosition(), next.endPosition()); // ...Replace 'next' with new token whose value is identical (same start and end positions), but whose key is replaced by '#'.
     }
     S1.add(next); // Add 'next' to linked list of tokens. At this point, all user-defined identifiers have been replaced by '#', and all other tokens have been unchanged.
   }
   if (desiredMatchLength>S1.size()){
     System.out.println("");
     System.out.println("Desired match length is too large.");
     System.out.println("");
     return;
   }
   // Input file 2:
   LinkedList<Token> S2=new LinkedList<Token>(); // Create new linked list S2 in which to store tokens from file1.java
   FileTokenRead file2TokenReader=new FileTokenRead(args[2]); // Create new file token reader object with which to read tokens from file2 to store in S2.
   Iterator<Token> file2Iterator=file2TokenReader.getIterator(); // Obtain iterator over tokens from file2.
   while (file2Iterator.hasNext()){ // While there are still tokens remaining from file2 to consider...
     Token next=file2Iterator.next(); // ...obtain next token from file2.
     String tokenValue=next.Value(); // Extract word associated with this token.
     char firstCharacter=tokenValue.charAt(0); // Extract first character from this word.
     if ((keywordsTable.find(tokenValue)==null)&&((firstCharacter>='a'&&firstCharacter<='z')||(firstCharacter>='A'&&firstCharacter<='Z'))){ // If word is not found in keywords.txt, and its first character is a letter...
       next=new Token("#", next.startPosition(), next.endPosition()); // ...Replace 'next' with new token whose value is identical (same start and end positions), but whose key is replaced by '#'.
     }
     S2.add(next); // Add 'next' to linked list of tokens. At this point, all user-defined identifiers have been replaced by '#', and all other tokens have been unchanged.
   }
   if (desiredMatchLength>S2.size()){
     System.out.println("");
     System.out.println("Desired match length is too large.");
     System.out.println("");
     return;
   }
   
   // Step 3) Insert all subsequences of input length desiredMatchLength from S1 into hash-based dictionary D.
   StringHashCode hashCode_D = new StringHashCode(); // Create new StringHashCode object with which to compute hash codes of individual entries from file1.
   float maxLoadFactor_D = (float) 0.75; // Initialize maximum load factor for hash table. Value based on trial and error attempt to minimize average number of probes per operation.
   HashDictionary D=new HashDictionary(hashCode,maxLoadFactor); // Create new hash table in which to store tokens from file1.
   for (int i=0; i<(S1.size()-desiredMatchLength+1); i++){ // For each token in S1 that will serve as the beginning of a sequence of tokens of desiredMatchLength, ie. from the first token to the token that is (desiredMatchLength-1) number of tokens from the end...
     // Extract corresponding token subsequence of length desiredMatchLength.
     String subsequenceKeyS1=S1.get(i).Value(); // ...extract key (obtained from 'Value()' method) of first token in subsequence.
     for (int j=i+1; j<(i+desiredMatchLength); j++){ // For each of the remaining tokens in subsequence...
       subsequenceKeyS1+=(S1.get(j).Value()); // ...concatenate corresponding token's key onto key of subsequence.
     }
     D.insert(subsequenceKeyS1,new Pair(S1.get(i).startPosition(), S1.get(i+desiredMatchLength-1).endPosition())); // Insert into D new entry with key equal to concatenated subsequence string, start position equal to start position of first concatenated token, and end position equal to end position of last concatenated token.
   }
   
   // Step4) Extract consecutive sebsequences of desiredMatchLength from S2 and attempt to find each in D. Return as soon as one is found, and return null if none is found.
   for (int i=0; i<(S2.size()-desiredMatchLength+1); i++){ // For each token in S2 that will serve as the beginning of a sequence of tokens of desiredMatchLength, ie. from the first token to the token that is (desiredMatchLength-1) number of tokens from the end...
     // Extract corresponding token subsequence of length desiredMatchLength.
     String subsequenceKeyS2=S2.get(i).Value(); // ...extract key (obtained from 'Value()' method) of first token in subsequence.
     for (int j=i+1; j<(i+desiredMatchLength); j++){ // For each of the remaining tokens in subsequence...
       subsequenceKeyS2+=(S2.get(j).Value()); // ...concatenate corresponding token's key onto key of subsequence.
     }
     // At this point, we have the i-th string (subsequenceKeyS2) of length desiredMatchLength from S2. We now search for it in dictionary D containing subsequences of S1.
     Entry foundMatch=D.find(subsequenceKeyS2); // Attempt to find subsequenceKeyS2 in D.
     if (foundMatch!=null){ // If subsequenceKeyS2 was found in D (then we are done)...
       // Extract strings from S1 and S2 corresponding to foundMatch and subsequenceKeyS2, respectively.
       String matchS1=getStringfromFile(args[1], foundMatch.Value().Start(), foundMatch.Value().End());
       String matchS2=getStringfromFile(args[2], S2.get(i).startPosition(), S2.get(i+desiredMatchLength-1).endPosition());
       // Print results to standard output and return, since our task is complete.
       System.out.println("Found in the first file:");
       System.out.println("");
       System.out.println(matchS1);
       System.out.println("");
       System.out.println("Found in the second file:");
       System.out.println("");
       System.out.println(matchS2);
       System.out.println("");
       return;
     }
   }
 }       
}
