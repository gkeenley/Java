//package A5;


public class Pair {
    private int first;
    private int second;
    
    public Pair(){}
    
    public Pair(int f, int s) {
        first  = f;
        second = s;
    }
    
    public int getFirst(){return first;}
    public int getSecond(){return second;}
    
    public void setFirst(int i){ first = i; }
    public void setSecond(int i){ second = i; }
}