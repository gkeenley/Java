import java.util.LinkedList; // Import Linked List class from java.util package.
public class DictEntryList { // Gathers list of all configurations and their associated scores.
 
  
  // START Attributes
  
  private LinkedList<DictEntry> dictList;
 private int size; // Size of board.
 
 // END Attributes
 
 
 // START Methods (all public)
 
 public DictEntryList() {
   super(); // Allow method to override superclass.
   dictList=new LinkedList<DictEntry>(); // Initialize list.
   size=0; // Initialize size of configuration to 0.
 }
 
 public void add(DictEntry pair) { // Add pair to existing list.
   dictList.add(pair);
   size+=1; // Increment size of configuration
 }
 
 public DictEntry getEntry() { // Return first entry from list.
   return dictList.getFirst();
 }
 
 public int size() { // Return size of configuration.
   return size;
 }
 
 public void clear() { // Clear configuration.
   dictList.clear();
   size=0;
 }
 
 public boolean contains(DictEntry pair) { // Determine whether given configuration contains pair or not.
   if(dictList.contains(pair)){
     return true;
   }
   else{
     return false;
   }
 }
 
 public DictEntry find(String config) { // Return the score stored in the dictionary for the given configuration.
  for(int i=0; i<size; i++) { // For each element of configuration...
   if(dictList.get(i).getConfig().equals(config)) { // If the configurations match...
    return dictList.get(i);
   }
  }
  return null;
 }
 
 public boolean remove(String config) { // Remove the entry with the given configuration.
  DictEntry item=this.find(config); // Locate given configuration.
  if(item==null){
    return false;  
  }
  else {
    dictList.remove(item);
    size--; // Decrement size of configuration.
    return true;
  }
 }
 
 // END Methods (all public)
 
}
