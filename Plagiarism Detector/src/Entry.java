//package cheatdetect;

public class Entry{
  
  private String entryKey;
  private Pair entryValue;
  
  public Entry(String key, Pair value){
    entryKey=key;
    entryValue=value;
  }
  
  public String Key(){
    return entryKey;
  }
  
  public Pair Value(){
    return entryValue;
  }
}