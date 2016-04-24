public class StringHashCode implements HashCode<String>{
  
  // Return polynomial accumulation hash code for strings.
  public int giveCode(String key){
    
    int a=33; // Polynomial accumulation coefficient.
    
    // Create array of ASCII codes of characters of key.
    String stringKey=key; // Cast key as string in order to break it into terms (characters);
    char[] charKeyArray=stringKey.toCharArray(); // Convert stringKey to character array.
    int[] intKeyArray=new int[stringKey.length()]; // Initialize new array that will hold ASCII codes of elements of charKeyArray.
    for (int i=0; i<stringKey.length(); i++){ // For each character in key...
      intKeyArray[i]=(int) charKeyArray[i]; // ...cast character as corresponding ASCII code.
    }
    // Compute polynomial accumulation hash code of key using Horner's algorithm.
    int p=intKeyArray[stringKey.length()-1]; // Initialize p to rightmost element of longKeyArray.
    int i=stringKey.length()-2; // Initialize index i.
    while (i>=0){ // Until we have computed last term in expansion...
      p=p*a+intKeyArray[i]; // ...compute given expansion term.
      i--; // Decrement i, in order to consider next term.
    }
    return Math.abs(p);
  }
}