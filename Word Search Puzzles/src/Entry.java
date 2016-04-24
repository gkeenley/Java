
public class Entry<K,V>{
  
  private K key;
  private V value;
  
  public Entry(K k, V v){
    key=k;
    value=v;
  }
  
  public K Key(){
    return key;
  }
  
  public V Value(){
    return value;
  }
  
  public void modifyValue(V v){
    value=v;
  }
}