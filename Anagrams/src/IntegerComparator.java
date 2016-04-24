/* Comparator for Integers */


public class IntegerComparator implements Comparator<Integer>{
    public IntegerComparator(){}
   
    public int compare(Integer a, Integer b){
	    
	if ( a.intValue() < b.intValue() ) return(-1);
        else if  (a.intValue() == b.intValue() ) return(0);
	else return(1);
    };  
}

