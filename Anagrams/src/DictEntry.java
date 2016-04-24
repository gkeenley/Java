
public class DictEntry< K, V >{
  
  // START Private Fields
  
  private K entryKey;
  private V entryValue;
  
  // END Private Fields
  
  
  // START Constructors
  
  public DictEntry(K key,V value){ // Takes key of type K and value of type V.
    entryKey=key;
    entryValue=value;
  }
  
  // END Constructors
  
  
  // START Public Methods
  
  public K key(){ // Returns key of DictEntry.
    return entryKey;
  }
  
  public V value(){ // Returns value of DictEntry.
    return entryValue;
  }
  
  public void changeValue(V newVal){ // Changes value of key of DictEntry to newVal.
    entryValue=newVal;
  }
  
  // END Public Methods
  
  
  // START Private Methods
  
  
  
  // END Private Methods
  
}