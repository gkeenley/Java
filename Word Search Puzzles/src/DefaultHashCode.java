
public class DefaultHashCode<K> implements HashCode<K>{
  
  public int giveCode(K key)
  {
       return(key.hashCode());      
  }     
}
