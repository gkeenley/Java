
// Comparator for strings, using lexicographical order.

public class StringComparator implements Comparator{
  public StringComparator(){}
  // Comparison returns 0 if a=b, a negative integer if a<b, and a positive integer if a>b.
  public int compare(Object a, Object b) throws ClassCastException{
    String sa = (String) a;
    String sb = (String) b;
    return(sa.compareTo(sb));
  }  
}