
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class HashDictionary<K,V> implements Dictionary<K,V> 
{
  // START MEMBER VARIABLES
  private HashCode<K> hCode; // Hash code object used for computing the hash code corresponding to a string, using polynomial accumulation.
  private Entry<K,V>[] tableEntries; // Array storing all entry objects of our hash table.
  private int numEntries; // Number of entries currently in table.
  private int N; // Number of slots in table.
  private int numProbes; // Total number of probes so far in hash table.
  private int indexOfLocatedEntry; // Index set by find() in order for remove(), which locates item to remove by calling find(), to be able to efficiently locate and remove entry. This variable is global because it is easier to have it as such than it is to modify find() and remove() functions in order for the index to be passed directly between them.
  private float maxLoadFactor; // Maximum load factor. Table will be rehashed if load factor is > maxLoadFactor.
  private int numOperations; // Total number of calls so far to any of find(), insert(), or remove(). Note that find() and remove() both invoke find(), but this fact influences numberOfOperations by only a constant factor, and so is unimportant.
  private Random rand=new Random(); // 'Random' object, used for computing random integers to use in primary hash function.
  private int a; // Parameter used in primary hash function.
  private int b; // Parameter used in primary hash function.
  private int q; // Parameter used in secondary hash function.
  // END MEMBER VARIABLES
    
  // START CONSTRUCTOR
  
  // Creates a new instance of HashDictionary 
  public HashDictionary(HashCode<K> inputCode, float maxLFactor){
    hCode=inputCode; // input hash code
    N=7; // Initialize N to 7. If load factor becomes greater than maxLoadFactor, table will be re-hashed and N will be set to next consecutive prime number (to ensure that the compression function will not 're-map' an entry to the same slot, which would occur if N were a multiple of the secondary hash function).
    numProbes=0; // Initialize to 0 because at its creation, no probes have been performed in the hash table.
    tableEntries=(Entry<K,V>[])new Entry[N]; // Initialize empty hash table.
    maxLoadFactor=maxLFactor; // Set to input paramemter.
    numOperations=0; // Initialize to 0 because at its creation, no operations have been performed on the hash table.
    a=(rand.nextInt(N-1)+1); // Constant used in primary hash function.
    b=rand.nextInt(N); // Constant used in primary hash function.
    q=5; // Constant used for secondary hash function.
  }

  // END CONSTRUCTOR
  
  
  // START PUBLIC METHODS
    
  // Returns true if there is  an entry with specified key. Returns null otherwise     
  public Entry<K,V> find(K key)
  {
    // Print error statement if attempt was made to find entry with null string key.
    if (key==null){
      System.out.println("Attempted to find entry with null string key into D.");
      return null;
    }
    
    numOperations++; // At this point, we have determined that input key is a valid key, and that we will be searching table for it. Thus, increment numOperations.
    
    // Compute primary and secondary hash functions.
    int k=hCode.giveCode(key); // Hashed index to compress.
    int h1=Math.abs(a*k+b)%N; // Primary hash function.
    int h2=q-k%q; // Secondary hash function.
    
    for (int p=0; p<N; p++){ // For a maximum number of probes as the number of table entries...
      numProbes++; // Increment number of probes.
      int index=(int)(h1+p*h2)%N; // ...compute double hashing function.
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

  public void insert(K key, V value) throws DictionaryException
  {
    // Test load factor to determine whether table needs to be re-hashed.
    while (loadFactor()>maxLoadFactor){
      rehash();
    }
    
    // Print error statement if attempt was made to insert entry with null string key.
    if (key==null){
      System.out.println("Attempted to insert entry with null string key into D.");
      return;
    }
    
    // Create entry with key and value, and insert this entry into current table.
    Entry newEntry=new Entry(key,value); // Create entry corresponding to input key and value.
    Entry foundEntry=find(key); // Attempt to find newEntry in tableEntries, return result as foundEntry.
    if ((foundEntry==null)||(foundEntry.Key().equals(null))){ // If newEntry was not found in tableEntries...
      insertEntry(newEntry, tableEntries, true); // ...insert new entry in tableEntries.
    } else{ // If entry already exists in tableEntries...
      throw new DictionaryException("Attempted to insert "+key+" into dictionary, but entry already exists.");
    }
  }
     
  public Iterator <Entry<K,V>> elements()
  {
    LinkedList list=new LinkedList(); // Create linked list to which our iterator will correspond.
    for (int i=0; i<numEntries; i++){ // For each member of list.
      list.add(tableEntries[i]); // Add member to list.
    }
    Iterator itr=list.iterator(); // Create iterator over list.
    return itr; // Return this iterator.
  }
     
  public void remove(K key) throws DictionaryException
  {
    if (find(key)!=null){ // If entry with 'key' is found in tableEntries...
      numOperations++; // ...increment numOperations because we will now remove the element (numOperations will have already been incremented once by using find();
      Entry removedEntry=tableEntries[indexOfLocatedEntry]; // Extract this entry.
      tableEntries[indexOfLocatedEntry]=new Entry(null, null); // Replace entry with DELETED entry: entry with null key and value.
    } else{ // If entry with 'key' is NOT found in tableEntries...
      throw new DictionaryException("Cannot delete entry with key "+key+" because it does not exist in table.");
    }
  }
     
  public float averNumProbes()
  {
       return ((float)numProbes/(float)numOperations);
  }
     
  // Return size of (number of entries in) hash table.
  public int size(){
    return numEntries;
  }
  
  // END PUBLIC METHODS
  
  
  // START PRIVATE METHODS
  
  // Compute what the load factor will be with one more entry added (because it is called only when judging whether or not to rehash before inserting new entry).
  private float loadFactor(){
    return (float)(numEntries+1)/(float)N; // Must cast both ints as float in order to obtain decimal load factor.
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
    int oldTableSize=N; // Save old table size for when transferring entries from old table to new.
    q=N; // Update q to be equal to old table size.
    N=nextPrime(2*N); // Update N to be next prime number at least twice as large as old table size.
    a=(rand.nextInt(N-1)+1); // Choose new random constant based on new table size.
    b=rand.nextInt(N); // Choose new random constant based on new table size.
    
    Entry<K,V>[] newEntries=(Entry<K,V>[])new Entry[N]; // Create new array of entries in which we will insert all existing entries.
    
    // Transfer entries from current table into new table.
    for (int i=0; i<oldTableSize; i++){ // For each element of current table...
      if (tableEntries[i]!=null){ // If slot in table contains entry (could be real or DELETED)...
        if (tableEntries[i].Key()!=null){ // ...and if entry is not DELETED...
          insertEntry(tableEntries[i], newEntries, false); // ...insert entry from current table into new table.
        }
      }
    }
    tableEntries=newEntries; // Update table entries.
  }
  
  // Insert input entryToInsert into input hashTable. If insertingNewEntries==true (occurs whenever we are inserting a new entry into table, rather than rehashing it) increment numEntries.
  private void insertEntry(Entry<K,V> entryToInsert, Entry<K,V>[] hashTable, boolean insertingNewEntries){
    numOperations++;
    
    // Compute primary and secondary hash functions.
    int k=hCode.giveCode(entryToInsert.Key()); // hash code to compress
    int h1=Math.abs(a*k+b)%N; // primary hash function
    int h2=q-k%q; // secondary hash function
    
    // Compute consecutive terms of double hashing series, and place entryToInsert in first available corresponding slot of table.
    for (int p=0; p<N; p++){ // Until we have considered every element of tableEntries...
      numProbes++; // Increment number of probes
      int index=(int)(h1+p*h2)%N; // ...compute double hashing function.
      if ((hashTable[index]==null)||((hashTable[index]!=null)&&(hashTable[index].Key().equals(null)))){ // If slot in hashTable is empty, or if key of entryToInsert is null (ie. slot has had entry deleted from it)...
        hashTable[index]=entryToInsert; // ...insert entryToInsert into hashTable.
        if (insertingNewEntries){ // If we are inserting a new entry, rather than rehashing.
          numEntries++; // Increment entry count.
        }
        return; // Exit function.
      }
    }
    
  }
  
  // END PRIVATE METHODS
  
 }