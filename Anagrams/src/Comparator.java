/* Comparator Interface.  Has to have only one method, the method used for comparison */


public interface Comparator<K>{
  
    /* compare returns negative integer if a < b, 0 if a = b, and positive integer if a > b */
    public int compare(K a, K b);  
}

