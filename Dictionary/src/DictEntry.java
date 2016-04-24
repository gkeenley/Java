public class DictEntry{
  public String entryWord;
  public String entryDefinition;
  public int entryType;
  public DictEntry leftChild;
  public DictEntry rightChild;
  public DictEntry entryParent;
  public DictEntry entryRoot;
  
  public DictEntry(String word, String definition, int type){
    entryWord=word;
    entryDefinition=definition;
    entryType=type;
  }
  public String word(){ // Returns the word in the entry.
    return entryWord;
  }
  public String definition(){ // Returns the definition of the entry: textual definition or name of corresponding media file.
    return entryDefinition;
  }
  public int type(){ // Returns the type of the entry. (1) text, (2) audio, (3) image.
    return entryType;
  }
}