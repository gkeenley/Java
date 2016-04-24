import java.io.File;
import java.io.*;
import java.util.Iterator;
import java.util.StringTokenizer;



public class Query{

  
  // START Main
  public static void main(String[] args) {
    OrderedDictionary d=new OrderedDictionary(); // Create dictionary object.
    SoundPlayer soundPlayer=new SoundPlayer(); // Create sound player object.
    PictureViewer pictureViewer=new PictureViewer(); // Create picture viewer object.
    StringReader keyboard = new StringReader(); // Create string reader object.
    
    File inputFile=new File(args[0]); // Save input file in file format.
    try{
    readFileIntoDictionary(inputFile,d); // Read lines from input file into dictionary.
    }
    catch(IOException e){
      System.out.println("IO Exception reading words from input into dictionary.");
    }
    // At this point, we have successfully inserted all words from input file into dictionary.
    while(true){ // Until user inputs 'end'...
      System.out.println(""); // Skip line
      String line = keyboard.read("Enter command: "); // Prompt user for input.
      StringTokenizer tokenizer = new StringTokenizer(line); // Tokenize input command.
      String inputOperation = tokenizer.nextToken(); // First word in input is operation (add, remove, etc).
      
      
      // Possible inputs:
      
      // DEFINE
      if(inputOperation.equals("define")){
        String word=tokenizer.nextToken(); // Word to define.
        String definition=d.findWord(word); // Definition of this word.
        if(definition==null){ // If word was not in the dictionary... (error message will already have been displayed)
          break;
        }
        int type=d.findType(word); // Type of this word.
        if (type==1){ // Text
          System.out.println(definition); // Print text definition.
        } else if(type==2){ // Audio
          try{
            soundPlayer.play(definition); // Play .wav or .mid file
          }
          catch(MultimediaException e){
            System.out.println("The file "+definition+" either cannot be found or cannot be processed.");
          }
        } else{ // Image
          try{
            pictureViewer.show(definition); // Display .jpg file
          }
          catch(MultimediaException e){
            System.out.println("The file "+definition+" either cannot be found or cannot be processed.");
          }
        }
        
      // DELETE  
      } else if (inputOperation.equals("delete")){
        String str=tokenizer.nextToken(); // Word to delete
        try{
          d.remove(str); // Remove word from dictionary.
        }
        catch(DictionaryException e){
          System.out.println("The word "+str+" is not in the dictionary.");
        }
        
      // ADD  
      } else if(inputOperation.equals("add")){
        String wordToAdd=tokenizer.nextToken(); // Word to add to dictionary.
        String definitionToAdd=tokenizer.nextToken(); // First word in definition of this word.
        while(tokenizer.hasMoreTokens()){ // Until we have reached the end of the definition...
          definitionToAdd=definitionToAdd+" "; // Add space before next word in definition.
          definitionToAdd=definitionToAdd+tokenizer.nextToken(); // Add next word to definition.
        }
        try{
          if (definitionToAdd.length()<4){ // If definition is too short to be .wav, .mid, or .jpg...
              d.insert(wordToAdd,definitionToAdd,1); // Insert word and definition, and indicate entry is text (1) file.
            } else if (definitionToAdd.substring(definitionToAdd.length()-4).equals(".wav")||definitionToAdd.substring(definitionToAdd.length()-4).equals(".mid")){ // If last 4 characters of definition correspond to .wav or .mid files...
              d.insert(wordToAdd,definitionToAdd,2); // Insert word and definition, and indicate entry is audio (2) file.
            } else if (definitionToAdd.substring(definitionToAdd.length()-4).equals(".jpg")){ // If last 4 characters of definition correspond to .jpg file...
              d.insert(wordToAdd,definitionToAdd,3); // Insert word and definition, and indicate entry is image (3) file.
            } else{ // Otherwise, must be a text file
              d.insert(wordToAdd,definitionToAdd,1); // Insert word and definition, and indicate entry is text (1) file.
            }
        }
        catch(DictionaryException e){
        }
        
      // LIST  
      }else if(inputOperation.equals("list")){
         String prefix=tokenizer.nextToken(); // Prefix to which we will be comparing all words.
         DictEntry leftMostEntry; // Lexicographically first word in dictionary. Leftmost entry in tree.
         String testDictEntryWord; // Word that will start at furthest left, and scan through dictionary using in-order traversal.
         if (d.root!=null){ // If tree is not empty...
           leftMostEntry=d.root; // Initialize leftmost entry to root.
           while(leftMostEntry.leftChild!=null){ // Until we have reached left extreme.
             leftMostEntry=leftMostEntry.leftChild; // Move leftmost entry down side of tree.
           }
           testDictEntryWord=leftMostEntry.entryWord; // Set testDictEntryWord to start at leftmost entry.
           while (testDictEntryWord!=null){ // Until we reach rightmost entry...
             if (testDictEntryWord.length()>=prefix.length()){ // If word is at least as long as prefix...
               if (prefix.compareTo(testDictEntryWord.substring(0,prefix.length()))==0){ // If prefix is a prefix to the word...
                 System.out.println(testDictEntryWord); // Print word.
               }
             }
             testDictEntryWord=d.successor(testDictEntryWord); // Move one entry to the right inOrder.
           }
         } else{ // If root was null...
           System.out.println("Dictionary is empty.");
         }
         
      // NEXT   
      } else if(inputOperation.equals("next")){
        System.out.println(d.successor(tokenizer.nextToken())); // Print next consecutive entry.
        
      // PREVIOUS  
      } else if(inputOperation.equals("previous")){
        System.out.println(d.predecessor(tokenizer.nextToken())); // Print previous consecutive entry.
        
      // END  
      } else if(inputOperation.equals("end")){
        return; // Quit
      } 
      
      // ANY OTHER INPUT
      else{
        System.out.println(""); // Skip a line.
        System.out.println(inputOperation+" is an invalid input");
      }
    }
  }
  // END Main
  
  
  
  private static void readFileIntoDictionary(File file, OrderedDictionary dictionary) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(file)); // Convert file reader to buffered reader.
    String word = null; // String into which we will read words from file, one at a time.
    String definition = null; // String into which we will read definitions from file, one at a time.
    while ((word = reader.readLine()) != null) { // Until we reach last line...(and read odd-numbered lines into 'word')
      definition=reader.readLine(); // Read even-numbered line into 'definition'.
        try{
          if (definition.substring(definition.length()-4).equals(".wav")||definition.substring(definition.length()-4).equals(".mid")){ // If last 4 characters of definition correspond to .wav or .mid files...
            dictionary.insert(word,definition,2); // Insert word and definition, and indicate entry is audio (2) file.
          } else if (definition.substring(definition.length()-4).equals(".jpg")){ // If last 4 characters of definition correspond to .jpg file...
            dictionary.insert(word,definition,3); // Insert word and definition, and indicate entry is image (3) file.
          } else{ // Otherwise, must be a text file
            dictionary.insert(word,definition,1); // Insert word and definition, and indicate entry is text (1) file.
          }
        }
        catch(DictionaryException e){
          System.out.println("Dictionary exception");
        }
        catch(StringIndexOutOfBoundsException r){
          System.out.println("Dictionary exception");
        }
    } 
    reader.close();
  }
  
}