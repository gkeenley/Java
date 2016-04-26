public class Dictionary implements DictionaryADT{

 // START Attributes 
  
 private int size; // Size of hash table.
 private int elemCount = 0; // Number of config/score pairs stored in hash table. Initialize to 0.
 private DictEntryList hashTable[]; // Declare linked list that we will sort using hashing.
 
 // END Attributes
 
 
 // START Constructors
 
 public Dictionary(int size) { // Return public dictionary of specified size.
  this.size = size; // Size of dictionary.
  hashTable = new DictEntryList[size]; // Size of hash table.
 } 
 
 // END Constructors
 
 
 
 // START Public Methods
 
 public int insert (DictEntry pair) throws DictionaryException{ // Inserts given DictEntry pair in dictionary.
  int hash = hash(pair.getConfig()); // Perform hash function on given config.
  int flag=0; // Flag that will be set to 1 if there is NO collision.
  if (hashTable[hash]==null) { // If there is no linked list at given position in hash table...
    hashTable[hash]=new DictEntryList(); // Create new list to store at that position in hash table.
   hashTable[hash].add(pair); // Add this config to hash table at that position.
   elemCount++; // Increment number of configs stored in hash table.  
   flag=1; // Signal that there was no collision.
  }
  else if (!(hashTable[hash].find(pair.getConfig())==null)){ // If there is a collision...
   throw new DictionaryException("DictionaryException: Cannot insert pair.config into dictionary because pair.config is already in dictionary."); // Throw exception: add config to next consecutive spot in linked list.
  }
  else { // If there is already a linked list at this location, but it is empty (ie. all config(s) have been deleted)... (2294 times)
   hashTable[hash].add(pair); // Add config to list.
   elemCount++;
   flag=0; // Signal that there was no collision.
   }
  if (flag==0){ // If there was a collision...
    return 0;
  }
  else{ // If there was no collision...
    return 1;
  }
  }
 
 public void remove(String config) throws DictionaryException{ // Removes entry with given configuration from dictionary.
   int hash=hash(config); // Get position of hashed element.
   if (hashTable[hash]!=null && hashTable[hash].remove(config)) { // Short-circuit
    elemCount--; // Decrement number of configs in hash table.
   }
   else {
    throw new DictionaryException("DictionaryException: Cannot remove "+config+" from dictionary because "+config+" is not in dictionary."); 
   }
 }
 
 public int find(String config) { // Return score stored in dictionary for given configuration.
  int score = -1; // Initially make score negative (for tracking).
  int hash = hash(config); // Hash configuration.
  if(hashTable[hash]!=null) { // If a linked list exists at this location...
   DictEntry entry = hashTable[hash].find(config); // Retrieve config at this location (could be empty).
   if(!(entry==null)) { // If this slot contained a config...
    score = entry.getScore(); // Get score of this config.
    return score;
   }
  }
  return score; // Return -1 since config was not in dictionary.
 }
 
 public int numElements() { // Return number of DictEntry objects stored in dictionary.
  return elemCount;
 }
 
 // END Public Methods
 
 
 // START Private Methods
 

 private int hash(String config) { // For each configuration, evaluate hash function for polynomial accumulation using Horner's rule.
  int length=config.length(); // Number of elements in configuration.
  int hash=(int)config.charAt(length-1); // Last element of configuration, cast as integer for polynomial accumulation. This is p0 of Horner's algorithm.
  int param=41; // Hash table parameter.
  // Horner's recursive algorithm.
  for(int i=length-2; i>=0; i--) { // From the second last to the first element of config...
   hash = (hash*param^3 + (int)config.charAt(i))%size; // i-th computed value of Horner's algorithm, calculated iteratively. Restricted to 'size' via '%' operator.
  }
  return hash; // Hashed position of element.
 }
 
 // END Private Methods

}
