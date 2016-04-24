//package cheatdetect;

import java.util.Iterator; 
import java.util.LinkedList;
import java.util.List;

public class HashDictionary implements Dictionary{
  
  // START Member Variables
  
  private Entry[] tableEntries; // Array storing all entry objects of our hash table.
  private int tableSize; // Number of slots in table.
  private int numEntries; // Number of entries currently in table.
  private float maxLoadFactor; // Maximum load factor. Table will be rehashed if load factor is > maxLoadFactor.
  private StringHashCode hCode; // StringHashCode object whose giveCode() will be used to compute hash code of a given key.
  private int numOperations; // Total number of calls so far to any of find(), insert(), or remove(). Note that find() and remove() both invoke find(), but this fact influences numberOfOperations by only a constant factor, and so is unimportant.
  private int numProbes; // Total number of probes so far in hash table.
  private int indexOfLocatedEntry; // Index set by find() in order for remove(), which locates item to remove by calling find(), to be able to efficiently locate and remove entry. This variable is global because it is easier to have it as such than it is to modify find() and remove() functions in order for the index to be passed directly between them.
  
  // END Member Variables
  
  
  // START Constructors
  
  // Constructor in which user did not provide hash code and maximum load factor. Throws exception because our implementatino of the hash table requires that these parameters be provided.
  public HashDictionary() throws DictionaryException{ // Throw exception because user must specify hascode and maximum load factor in constructor.
    throw new DictionaryException("Must provide hashcode and maximum load factor as parameters to constructor.");
  }
  
  // Constructor in which hash code and maximum load factor ARE provided, as required by our implementation of the hash table.
  public HashDictionary(StringHashCode inputCode, float inputLoadFactor){
    tableSize=7; // Initialize tableSize to 7. If load factor becomes greater than maxLoadFactor, table will be re-hashed and tableSize will be set to next consecutive prime number (to ensure that the compression function will not 're-map' an entry to the same slot, which would occur if tableSize were a multiple of the secondary hash function).
    tableEntries=new Entry[tableSize]; // Initialize empty hash table.
    numEntries=0; // Initialize to 0 because the hash table initially contains no entries.
    maxLoadFactor=inputLoadFactor; // Set to input paramemter.
    hCode=inputCode; // Set to input paramemter.
    numOperations=0; // Initialize to 0 because at its creation, no operations have been performed on the hash table.
    numProbes=0; // Initialize to 0 because at its creation, no probes have been performed in the hash table.
  }
  
  // END Constructors
  
  
  // START Public Methods
  
  // Search for entry with key 'key' in tableEntries. If found, return corresponding entry. If not found, return null.
  public Entry find(String key) {
    // Print error statement if attempt was made to find entry with null string key.
    if (key.equals(null)){
      System.out.println("Attempted to find entry with null string key into D.");
      return null;
    }
    
    numOperations++; // At this point, we have determined that input key is a valid key, and that we will be searching table for it. Thus, increment numOperations.
    
    // Compute primary and secondary hash functions.
    int q=5; // Constant used for secondary hash function.
    int k=hCode.giveCode((Object) key); // Hashed index to compress.
    int h1=k%tableSize; // Primary hash function.
    int h2=q-k%q; // Secondary hash function.
    
    // Compute consecutive terms of double hashing series, and place entryToInsert in first available corresponding slot of table.
    for (int j=0; j<tableSize; j++){ // Until we have considered every element of tableEntries...
      numProbes++;
      int index=(int)(h1+j*h2)%tableSize; // ...compute double hashing function.
      if (tableEntries[index]==null){ // If no entry is found at this index in tableEntries
        return null;
      } else{ // If an entry is found at this index in tableEntries (could be real entry or DELETED)...
        if (tableEntries[index].Key()!=null){ // If key of given entry is not null (ie. it is not a DELETED entry)...
          if (tableEntries[index].Key().equals(key)){ // ...if key of given entry is equal to input key...
            indexOfLocatedEntry=index; // ...save index so that remove() will know which element of tableEntries to delete.
            return tableEntries[index]; // Return this entry.
          }
        }
      }
    }
    return null; // At this point, we have considered every element of tableEntries and have found that all contain an entry, none of which contains input key. Conclude that no such entry exists at present, and return null.
  }
  
  // Search for entry with key 'key' in tableEntries. If found, remove entry from hash table and return it. If not found, return null.
  public Entry remove(String key) throws DictionaryException{
    if (find(key)!=null){ // If entry with 'key' is found in tableEntries...
      numOperations++; // ...increment numOperations because we will now remove the element (numOperations will have already been incremented once by using find();
      Entry removedEntry=tableEntries[indexOfLocatedEntry]; // Extract this entry.
      tableEntries[indexOfLocatedEntry]=new Entry(null, null); // Replace entry with DELETED entry: entry with null key and value.
      return removedEntry;
    } else{ // If entry with 'key' is NOT found in tableEntries...
      throw new DictionaryException("Cannot delete entry with key "+key+" because it does not exist in table.");
    }
  }
  
  // Prepare to insert new entry into table by rehashing if necessary, and creating new entry containing input key and value. Insert this entry into table using insertEntry().
  public void insert(String key, Pair value){
    
    // Print error statement if attempt was made to insert entry with null string key.
    if (key.equals(null)){
      System.out.println("Attempted to insert entry with null string key into D.");
      return;
    }
    
    // Test load factor to determine whether table needs to be re-hashed.
    if (loadFactor()>maxLoadFactor){
      rehash();
    }
    
    // Create entry with key and value, and insert this entry into current table.
    Entry newEntry=new Entry(key,value); // Create entry corresponding to input key and value.
    Entry foundEntry=find(key); // Attempt to find newEntry in tableEntries, return result as foundEntry.
    numOperations++;
    if (foundEntry==null){ // If newEntry was not found in tableEntries...
      insertEntry(newEntry, tableEntries, true); // ...insert new entry in tableEntries.
    } else{ // If find(newEntry) returned a non-null entry (either newEntry itself or DELETED)...
      if (foundEntry.Key().equals(null)){ // ...if slot in tableEntries contained DELETED entry...
        insertEntry(newEntry, tableEntries, true); // ...insert new entry in tableEntries.
      } else{ // If entry already exists in tableEntries...
        return;
      }
    }
    return;
  }
  
  // Return size of (number of entries in) hash table.
  public int size(){
    return numEntries;
  }
  
  // Return average number of probes per operation (find(), insert(), or remove()) so far in table.
  public float averNumProbes(){
    return ((float)numProbes/(float)numOperations);
  }
  
  // END Public Methods
  
  
  // START Private Methods
  
  // Compute what the load factor will be with one more entry added (because it is called only when judging whether or not to rehash before inserting new entry).
  private float loadFactor(){
    return (float)(numEntries+1)/(float)tableSize; // Must cast both ints as float in order to obtain decimal load factor.
  }
  
  // Compute next consecutive prime number from set of positive integers. Used in order to determine size of expanded table when rehashing.
  private int nextPrime(int num){
    
    boolean isItPrime; // Indicates whether given number is prime or not.
    num=num+1; // Consider next consecutive integer.
    
    while(true){ // Loop until next prime is found.
      isItPrime=true; // Initially assume number is prime. Number will be rejected if it is found not to be prime.
      for (int i=2; i<num/2; i++){ // For each integer from 2 to num/2 (a non-prime will be divisible by at least one of these)...
        if ((num%i)==0){ // ...if num is divisible by i...
          isItPrime=false; // ...number is NOT prime.
          break; // Reject current number.
        }
      }
      if (isItPrime){ // If num was not rejected, ie. was not divisible by any integers from 2 to num/2...
        return num; // ...conclude that this number IS prime, and since we have checked every consecutive integer after input num, that this is the next consecutive prime number after input num.
      }
      num++; // At this point, num was found to be not prime, so we incerment num in order to test next consecutive integer.
    }
  }
  
  // Rehash table: compute next consecutive prime number, create new empty table of this size, and re-hash all existing entries into this new, expanded table.
  private void rehash(){ 
    int oldTableSize=tableSize;
    tableSize=nextPrime(oldTableSize); // Size of expanded table is next consecutive prime number after current size.
    Entry[] newEntries=new Entry[tableSize]; // Create new array of entries in which we will insert all existing entries.
    
    // Transfer entries from current table into new table.
    for (int i=0; i<oldTableSize; i++){ // For each element of current table...
      if (tableEntries[i]!=null){ // If slot in table contains entry (could be real or DELETED)...
        if (tableEntries[i].Key()!=null){ // ...and if entry is not DELETED...
          insertEntry(tableEntries[i], newEntries, false); // ...insert entry from current table into new table.
        }
      }
    }
    // Update table entries.
    tableEntries=newEntries;
  }
  
  // Insert input entryToInsert into input hashTable. If insertingNewEntries==true (occurs whenever we are inserting a new entry into table, rather than rehashing it) increment numEntries.
  private void insertEntry(Entry entryToInsert, Entry[] hashTable, boolean insertingNewEntries){
    
    // Compute primary and secondary hash functions.
    int q=5; // Constant used for secondary hash function.
    int h1=hCode.giveCode((Object) entryToInsert.Key())%tableSize; // Primary hash function.
    int h2=q-hCode.giveCode((Object) entryToInsert.Key())%q; // Secondary hash function.
    
    // Compute consecutive terms of double hashing series, and place entryToInsert in first available corresponding slot of table.
    for (int j=0; j<tableSize; j++){ // Until we have considered every element of tableEntries...
      numProbes++;
      int index=(int)(h1+j*h2)%tableSize; // ...compute double hashing function.
      if (hashTable[index]==null){ // If slot in hashTable is empty...
        hashTable[index]=entryToInsert; // ...insert entryToInsert into hashTable.
        if (insertingNewEntries){ // If we are inserting a new entry, rather than rehashing.
          numEntries++; // Increment entry count.
        }
        return; // Exit function.
      } else if (hashTable[index].Key().equals(null)){ // ...if key of entryToInsert is null (ie. slot has had entry deleted from it)...
        hashTable[index]=entryToInsert; // ...insert entryToInsert into hashTable.
        if (insertingNewEntries){ // If we are inserting a new entry, rather than rehashing.
          numEntries++; // Increment entry count.
        }
        return; // Exit function.
      }
    }
    
  }
  
  // END Private Methods
  
}