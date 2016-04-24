import java.io.*;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Random;


public class WordPuzzle{
  public static void main(String[] args) throws DictionaryException, java.io.IOException{
    
    // Declare and initialize private variables
    int puzzleWidth=0;
    int puzzleHeight=0;
    String inputLine; // string which will hold a horizontal row read from puzzle.
    int i, j, k, m, n, x; // indices
    float maxLoadFactor = (float) 0.5; // Initialize max load factor
    
    try{
      StringHashCode hC = new StringHashCode(); // Create new hash code object that computes code for string based on polynomial accumulation
      
      HashDictionary dict=new HashDictionary(hC, maxLoadFactor); // Create dictionary table
      HashDictionary puzzleWords=new HashDictionary(hC, maxLoadFactor); // Create table containing words found
      
      // Create output stream to write found words to file.
      File file = new File("output.txt");
      FileWriter fileWriter = new FileWriter(file);
      
      // Read all dictionary words from file dictionary.txt and store them in the hash table
      BufferedReader inputStream = null; // Create input stream from which to read form files
      inputStream = new BufferedReader(new FileReader(args[1])); // Set input stream to read from dictionary.txt
      while ((inputLine = inputStream.readLine()) != null){ // For each line of dictionary...
        for (i=1; i<=inputLine.length(); i++){ // ...for each character in line...
          if (i==inputLine.length()){ // ...if we have reached the end of the word...
            if ((dict.find(inputLine)!=null)&&(dict.find(inputLine).Value()==Integer.valueOf(0))){ // If word already exists in dict and is a prefix...
              dict.remove(inputLine); // ...remove prefix in preparation for replacing it with full word.
            }
            dict.insert(inputLine, 1); // Insert as a word rather than a prefix.
          } else{ // If we have not yet reached the end of the word, and the string thus far is therefore a prefix...
            if (dict.find(inputLine.substring(0, i))==null){ // If this prefix doesn't already exist in dict...
              dict.insert(inputLine.substring(0, i), null); // ...insert this prefix in dict.
            }
          }
        }
      }
      
      
      // Read the puzzle from puzzle.txt and store it in a two-dimensional array of type char. 
      
      //Calculate puzzle width.
      inputStream = new BufferedReader(new FileReader(args[0])); // Set input stream to read from puzzle.
      inputLine=inputStream.readLine(); // Read first line from puzzle (assume all lines have same width as first).
      i=0; // Index for scanning through input line.
      while(Character.isLetter(inputLine.charAt(i))){ // While current character is a letter (because puzzleSmall.txt was terminated with null character)...
        puzzleWidth++; // Increment puzzleWidth.
        i++; // Increment index.
        if (i==inputLine.length()) // If we have reached the end of the line (because puzzleMedium terminated with its last letter).
          break;
      }
      
      // Calculate puzzle height
      inputStream = new BufferedReader(new FileReader(args[0])); // Set input stream to read from puzzle.
      while ((inputLine = inputStream.readLine()) != null){ // For each line in puzzle...
        puzzleHeight++; // Increment puzzleHeight.
      }
      
      char[][] puzzleArray=new char[puzzleHeight][puzzleWidth]; // Create puzzle array.
      
      inputStream = new BufferedReader(new FileReader(args[0])); // Set input stream to read from puzzle.
      for (i=0; i<puzzleHeight; i++){ // For each row...
        inputLine=inputStream.readLine(); // Read row from puzzle file.
        for (j=0; j<puzzleWidth; j++){ // For each element of row...
          puzzleArray[i][j]=inputLine.charAt(j); // Write corresponding character to puzzleArray.
        }
      }
      
      // Define orientation array
      int[][] o={{-1,0,1,1,1,0,-1,-1}, {1,1,1,0,-1,-1,-1,0}};
      for (i=0; i<puzzleHeight; i++){ // For each row of puzzle...
        for (j=0; j<puzzleWidth; j++){ // ...and for each column...
          String testString=""; // Create new test string.
          for (k=0; k<8; k++){ // For each orientation
            m=i; // vertical index
            n=j; // horizontal index
            while((m>=0)&&(n>=0)&&(m<puzzleHeight)&&(n<puzzleWidth)){ // While indices are within array bounds...
              testString+=puzzleArray[m][n]; // ...add next character to testString.
              if (dict.find(testString)==null) // If string is not found in dict (ie. neither word NOR prefix)...
                break; // Abandon this orientation.
              if ((testString.length()>3)&&(dict.find(testString).Value()==Integer.valueOf(1))&&(puzzleWords.find(testString)==null)){ // If testString corresponds to a WORD (not a prefix) in dict...
                puzzleWords.insert(testString, null); // ...insert testString into puzzlewords so that we won't put potential duplicate in output.txt
                System.out.println(testString);
                fileWriter.write(testString+"\n"); // Write testString to output.txt
              }
              m+=o[0][k]; // Increment vertical index.
              n+=o[1][k]; // Increment horizontal index.
            }
            testString=""; // Finished with orientation, so re-set testString to be empty in preparation for next orientation.
          }
        }
      }
      
      // Flush and close output stream.
      fileWriter.flush();
      fileWriter.close();
      
    } catch (DictionaryException e1){
      System.out.println("DictionaryException");
    } catch (java.io.IOException e2){
      System.out.println("IOException");
    }
  }
}