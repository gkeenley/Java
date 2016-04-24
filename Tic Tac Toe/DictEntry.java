public class DictEntry { // Individual entry in dictionary: consists of a configuration and its associated score.
  

  // START Attributes
  
  private String config = ""; // Initialize string which we will fill with characters from the given configuration.
  private int score; // Integer score for a given configuration.
  
  // END Attributes
  
  
  // START Constructors//
  
  public DictEntry(String config, int score) { // return new DictEntry with the specified configuration and score.
    this.config = config; // Set input configuration as attribute of DictEntry.
    this.score = score; // Set input score as attribute of DictEntry.
  }
  
  // END Constructors
  
  
  // START Methods (all public)
  
  public String getConfig() { // Return configuration stored in DictEntry.
    return config; // Return given configuration as 1D string.
  }
  
  public int getScore() { // Return score in DictEntry.
    return score; // Return given score as integer.
  }
  
  // END Methods (all public)
  
}
