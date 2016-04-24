
public interface Dictionary<K,V> {
   
    // Inserts the entry with the specified key and value to the dictionary
 // if entry with input key already exists in the dictionary, throws exception
    public void insert(K k, V v) throws DictionaryException;
    
    // Removes the entry with the specified key from the dictionary. Throws 
    // DictionaryException if no entry with key in the dictionary          
    public void remove(K k) throws DictionaryException;

    // If entry with this key exists in the dictionary, returns this entry
    // otherwise returns null
    public Entry<K,V> find(K k);  
    
    public int size();
}

