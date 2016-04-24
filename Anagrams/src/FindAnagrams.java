import java.io.*;
import java.util.LinkedList;
import java.util.Iterator;

public class  FindAnagrams{
  
  public static void main(String[] args) throws AVLTreeException, java.io.IOException{
    try{
      long number=0;
      // Set up input and output streams
      File outputFile = new File("out.txt");
      FileWriter fileWriter = new FileWriter(outputFile);     
      
      BufferedReader inputStreamFromDictionary = new BufferedReader(new FileReader(args[0]));
      BufferedReader inputStreamFromWordsFile = new BufferedReader(new FileReader(args[1]));
      
      // Set up variables to be used throughout implementation
      String dictionaryWord;
      StringComparator stringComp = new StringComparator();
      AVLTree<String, String>  temp;
      AVLTree<String, String>  dictTree=new AVLTree<String, String> (stringComp);
      int i;
      
      // Read words from dictionary into dictTree
      while ((dictionaryWord = inputStreamFromDictionary.readLine()) != null){ // For each line in dictionary...
        String sortedDictionaryWord=""; // Initialize empty string in which to store sorted word from dictionary.
        temp = new AVLTree<String, String> (stringComp); // Initialize temp.
        for (i = 0; i < dictionaryWord.length();i++ ){ // For each character in word...
          temp.insert(dictionaryWord.substring(i,(i+1)), dictionaryWord); // ...insert character into temp.
        }
        Iterator<DictEntry<String,String> > inOrderOverWord = temp.inOrder(); // Set up inOrder iterator over characters in word.
        while (inOrderOverWord.hasNext()){ // For each character in word...
          DictEntry<String,String> inOrderCharacter = inOrderOverWord.next(); // ...read character inOrder...
          sortedDictionaryWord+=inOrderCharacter.key(); // ...add character inOrder to sortedDictionaryWord.
        }
        dictTree.insert(sortedDictionaryWord, dictionaryWord); // Insert sorted word into dictionary.
      }
      
      // Read words from words file, and save all anagrams from dictionary into out.txt
      String w;
      while ((w = inputStreamFromWordsFile.readLine()) != null){ // While we have not yet reached end of words file...
        String wSorted="";
        temp = new AVLTree<String, String> (stringComp);
        for (i = 0; i < w.length();i++ ){ // For each character in word...
          temp.insert(w.substring(i,(i+1)), w);
        }
        Iterator<DictEntry<String,String> > iteratorInOrder = temp.inOrder();
        while (iteratorInOrder.hasNext()){
          DictEntry<String,String> nextInOrder = iteratorInOrder.next();
          wSorted+=nextInOrder.key();
        }
        Iterator<DictEntry<String,String> > iteratorFindAll = dictTree.findAll(wSorted);
        
        while (iteratorFindAll.hasNext()){
          DictEntry<String,String> nextFindAll = iteratorFindAll.next();
          fileWriter.write(nextFindAll.value()+"\n");
        }
        fileWriter.write("\n");
      }
      
    // Flush and close output stream.
    fileWriter.flush();
    fileWriter.close();  
      
    } catch (AVLTreeException e1){
      System.out.println("AVLTreeException");
    } catch (java.io.IOException e2){
      System.out.println("IOException");
    }
 }
  
}